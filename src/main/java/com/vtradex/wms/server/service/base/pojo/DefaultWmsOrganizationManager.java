package com.vtradex.wms.server.service.base.pojo;


import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang.StringUtils;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.exception.ExceptionLog;
import com.vtradex.thorn.server.model.security.User;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.organization.WmsBlgItem;
import com.vtradex.wms.server.model.organization.WmsEnumType;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsUserSupplier;
import com.vtradex.wms.server.model.organization.WmsUserSupplierHead;
import com.vtradex.wms.server.model.warehouse.WmsBoxType;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
import com.vtradex.wms.server.service.base.WmsOrganizationManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.utils.DateUtil;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

public class DefaultWmsOrganizationManager extends DefaultBaseManager implements WmsOrganizationManager {

	private WmsRuleManager wmsRuleManager;
	
	public DefaultWmsOrganizationManager(WmsRuleManager wmsRuleManager){
		this.wmsRuleManager = wmsRuleManager;
	}
	
	public void saveOrganization(WmsOrganization organization) {}
	public void fixLotRule(Object lotRule){
	}
	
	
	/**
	 * 根据货主统计货主盘点周期，下库位总数，已盘点数，作业中数，待盘点数 
	 * */
	public List<Double> countQuantity(Long wmsCompanyId) {
		WmsOrganization company = load(WmsOrganization.class,wmsCompanyId);
		List<Double> listCount = new ArrayList<Double>();
		
		//盘点周期
		Map problem = new HashMap();
		problem.put("类型", "获取盘点周期");
		problem.put("货主", company.getName());
		Map<String, Object> result = wmsRuleManager.execute(WmsWarehouseHolder.getWmsWarehouse().getName(), 
				WmsWarehouseHolder.getWmsWarehouse().getName(), "盘点规则", problem);
		Double cycleDate = Double.parseDouble(result.get("盘点周期").toString());
		
		//总库位数
		String hqlCount = "SELECT COUNT(wmsLocation.id) FROM WmsLocation wmsLocation WHERE wmsLocation.id IN " +
			" (SELECT wmsInventory.location.id FROM WmsInventory wmsInventory " +
			" WHERE location.type='STORAGE' " +
			" AND wmsInventory.itemKey.item.company.id=:companyId)";
		Long locationCount = (Long)commonDao.findByQueryUniqueResult(
				hqlCount, new String[]{"companyId"}, new Object[]{wmsCompanyId});
		
		//已盘点数
		String hqlFinished = "FROM WmsLocation wmsLocation WHERE wmsLocation.id IN " +
				" (SELECT wmsInventory.location.id FROM WmsInventory wmsInventory " +
				" WHERE wmsInventory.location.type='STORAGE' " +
				" AND wmsInventory.itemKey.item.company.id=:companyId) " +
				" AND wmsLocation.cycleDate IS NOT NULL";
		Long finished = 0l;
		List<WmsLocation> listLoc = commonDao.findByQuery(hqlFinished, new String[]{"companyId"}, new Object[]{wmsCompanyId});
		
		if(!listLoc.isEmpty()) {
			for(WmsLocation wmsLocation : listLoc){
				if(DateUtil.getMargin((new Date()).toString(),wmsLocation.getCycleDate().toString())<30){
					finished++;
				}
			}
		}
		
		//作业中数
		String hqlProcessing = "SELECT COUNT(wmsLocation.id) FROM WmsLocation wmsLocation " +
			" WHERE wmsLocation.warehouse.id = :warehouseId " +
			" AND (wmsLocation.id IN (SELECT wmsInventory.location.id FROM WmsInventory wmsInventory " +
			"     WHERE wmsInventory.location.warehouse.id = :warehouseId " +
			"         AND wmsInventory.location.type='STORAGE' " +
			"         AND (wmsInventory.putawayQuantityBU<>0 OR wmsInventory.allocatedQuantityBU<>0) " +
			"         AND wmsInventory.itemKey.item.company.id=:companyId) " +
			"     OR wmsLocation.lockCount=true)";
		Long processing = (Long)commonDao.findByQueryUniqueResult(hqlProcessing,
				new String[]{"warehouseId", "companyId"}, 
				new Object[]{WmsWarehouseHolder.getWmsWarehouse().getId(), wmsCompanyId});
		
		listCount.add(cycleDate);
		listCount.add(Double.valueOf(locationCount.toString()));
		listCount.add(Double.valueOf(finished.toString()));
		listCount.add(Double.valueOf(processing.toString()));
		
		return listCount;
	}

	public void addWorkUser(Long workerGroupId, Long workId, String station) {
		WmsWorker workerGroup = commonDao.load(WmsWorker.class, workerGroupId);
		WmsWorker worker = commonDao.load(WmsWorker.class, workId);
		worker.setStation(station);
		worker.setWorker(workerGroup);
		commonDao.store(worker);
	}
	
	public void removeWorkUser(WmsWorker worker) {
		worker.setStation(null);
		worker.setWorker(null);
		commonDao.store(worker);
	}
	
	public void importAdditionalInfo(Map<String,String> map){
		String code = map.get("代码");
		String name = map.get("名称");
		String telephone=map.get("联系电话");
		String address = map.get("联系地址");
		String taxCode = map.get("税号");
		String bankAccountNum = map.get("开户行及账号");
		if(StringUtils.isEmpty(code) &&  StringUtils.isEmpty(name)){
			throw new BusinessException("代码、名称不能同时为空");
		}
		String hql ;
		WmsOrganization supplier;
		if(StringUtils.isEmpty(code)){
			hql="from WmsOrganization org where org.name=:name and org.status='ENABLED'";
			supplier= (WmsOrganization)this.commonDao.findByQueryUniqueResult(hql, "name", name);
		}else{
			hql="from WmsOrganization org where org.code=:code and org.status='ENABLED'";
			supplier= (WmsOrganization)this.commonDao.findByQueryUniqueResult(hql, "code", code);
		}
		if(null == supplier){
			throw new BusinessException("找不多对应生效供应商，请检查代码或者名称是否完全匹配");
		}
		if(StringUtils.isEmpty(address)){
			supplier.getContact().setAddress(address);
		}
		if(StringUtils.isEmpty(telephone)){
			supplier.getContact().setTelephone(telephone);
		}
		if(StringUtils.isEmpty(taxCode)){
			supplier.getContact().setTaxCode(taxCode);
		}
		if(StringUtils.isEmpty(bankAccountNum)){
			supplier.getContact().setBankAccountNum(bankAccountNum);
		}
		this.commonDao.store(supplier);
	}
	
	public void addUserSupplier(Long usdId,WmsUserSupplier us){
		WmsUserSupplierHead usd = commonDao.load(WmsUserSupplierHead.class, usdId);
		us.setUserHead(usd);
		commonDao.store(us);
	}
	public void removeUserSupplier(WmsUserSupplier us){
		commonDao.delete(us);
	}
	public void removeUserSupplierHead(WmsUserSupplierHead usd){
		List<WmsUserSupplier> uss = commonDao.findByQuery("FROM WmsUserSupplier us WHERE us.userHead.id =:userHead", "userHead",usd.getId());
		if(uss!=null && uss.size()>0){
			commonDao.deleteAll(uss);
		}
		commonDao.delete(usd);
	}

	@Override
	public void importBlgItem(File file) {
		Integer error = 0;
		String name = file.getName();
		if (file == null) {
			throw new BusinessException("file.not.found");
		} else if (!name.substring(name.lastIndexOf('.') + 1,
				name.lastIndexOf('.') + 4).equals("xls")) {
			throw new BusinessException("this.file.error");
		}

		Sheet dataSheet = null;
		List<Cell[]> cellArray = new ArrayList<Cell[]>();
		try {
			Workbook wb = Workbook
					.getWorkbook(new java.io.FileInputStream(file));
			dataSheet = wb.getSheet(0);

			int rowNum = dataSheet.getRows();
			for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
				cellArray.add(dataSheet.getRow(rowIndex));
				if (cellArray.size() > 0 && cellArray.size() % 50 == 0) {
					error += this.resolveBlgItemJac(cellArray);
					cellArray.clear();
				}
			}
			if (cellArray.size() > 0 && cellArray.size() < 50) {
				System.out.println(error);
				error += this.resolveBlgItemJac(cellArray);
				System.out.println(error);
				cellArray.clear();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		LocalizedMessage.addMessage("导入失败" + error + "条,请查看日志");
	}
	public int resolveBlgItemJac(List<Cell[]> strList) throws ParseException {
		StringBuffer logBuffer = new StringBuffer("");
		Integer errorNum = 0;
		Cell[] strArr;
		
		for (int i = 0; i < strList.size(); i++) {
			String lineNum = (i+1)+"";
			try {
				strArr = strList.get(i);
				String company = strArr[0].getContents();//货主
				String gh = strArr[1].getContents();// 工号
				String itemCode = strArr[2].getContents();// 物料编码

				if(StringHelper.isNullOrEmpty(company)){
					errorNum++;
					logBuffer.append(lineNum + "行货主不能为空/");
					// log.debug(lineNum+",1 error \n");
					continue;
				}
				
				String hql = "from WmsOrganization w where w.neiBuName=:neiBuName and w.beCompany='Y'";
				List<WmsOrganization> list = commonDao.findByQuery(hql,"neiBuName",company.trim());
				if(list.size() <= 0){
					errorNum++;
					logBuffer.append(lineNum + "行货主不存在/");
					// log.debug(lineNum+",1 error \n");
					continue;
				}
				
				if(StringHelper.isNullOrEmpty(gh)){
					errorNum++;
					logBuffer.append(lineNum + "行工号不能为空/");
					// log.debug(lineNum+",1 error \n");
					continue;
				}
				hql = "from WmsWorker w where w.code=:code";
				List<WmsWorker> workers = commonDao.findByQuery(hql,"code",gh);
				if(workers.size() <= 0){
					errorNum++;
					logBuffer.append(lineNum + "行工号不存在/");
					// log.debug(lineNum+",1 error \n");
					continue;
				}
				
				if(StringHelper.isNullOrEmpty(itemCode)){
					errorNum++;
					logBuffer.append(lineNum + "行物料编码不能为空/");
					// log.debug(lineNum+",1 error \n");
					continue;
				}
				hql = "from WmsItem w where code=:code and w.company.id=:id";
				List<WmsItem> items = commonDao.findByQuery(hql,
						new String[]{"code","id"},new Object[]{itemCode,list.get(0).getId()});
				if(items.size() <= 0){
					errorNum++;
					logBuffer.append(lineNum + "行物料编码不存在/");
					// log.debug(lineNum+",1 error \n");
					continue;
				}
				WmsBlgItem blg = new WmsBlgItem(items.get(0), workers.get(0), BaseStatus.ENABLED);
				commonDao.store(blg);
				
			} catch (Exception e) {
				logBuffer.append(lineNum + "行导入失败/");
				errorNum++;
				logger.error("", e);
			}
		}
		ExceptionLog log = EntityFactory.getEntity(ExceptionLog.class);
		if (UserHolder.getUser() != null) {
			log.setOperUserId(UserHolder.getUser().getId());
			log.setOperUserName(UserHolder.getUser().getName());
		} else {
			User user = this.commonDao.load(User.class, new Long(1));
			log.setOperUserId(user.getId());
			log.setOperUserName(user.getName());
		}
		log.setType("备料工物料导入");
		log.setOperDate(new Date());
		log.setOperException(logBuffer.toString());
		log.setOperExceptionMess("");
		log.setOperPageId("maintainWmsBlgItemPage");
		log.setOperPageName("备料工物料管理");
		log.setOperComponentId("备料工物料导入");
		log.setOperComponentName("importBlgItem");
		if (org.apache.commons.lang.StringUtils.isEmpty(log.getOperException())) {
			log.setStrExtend1("成功");
		} else {
			log.setStrExtend1("失败");
		}
		commonDao.store(log);
		return errorNum;
	}
}
