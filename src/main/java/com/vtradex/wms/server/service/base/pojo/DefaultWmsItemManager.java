package com.vtradex.wms.server.service.base.pojo;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang.StringUtils;

import com.vtradex.thorn.client.ui.page.IPage;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.LotInfo;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.inventory.WmsMisInventory;
import com.vtradex.wms.server.model.organization.WmsBlgItem;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsLotRule;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageChangeLog;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.organization.WmsStationAndItem;
import com.vtradex.wms.server.model.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.shipping.WmsPickTicket;
import com.vtradex.wms.server.model.shipping.WmsPickTicketStatus;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsLocationType;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
import com.vtradex.wms.server.service.base.WmsItemManager;
import com.vtradex.wms.server.service.receiving.WmsMoveDocManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.shipping.WmsPickTicketManager;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

@SuppressWarnings("unchecked")
public class DefaultWmsItemManager extends DefaultBaseManager implements WmsItemManager {
	protected WmsBussinessCodeManager wmsBussinessCodeManager;
	protected WorkflowManager workflowManager;
	protected WmsRuleManager wmsRuleManager;
	
	public DefaultWmsItemManager(WmsBussinessCodeManager wmsBussinessCodeManager, WorkflowManager workflowManager,
			WmsRuleManager wmsRuleManager) {
		this.wmsBussinessCodeManager = wmsBussinessCodeManager;
		this.workflowManager = workflowManager;
		this.wmsRuleManager = wmsRuleManager;
	}
	
	public void addPackageUnit(Long itemId, WmsPackageUnit packageUnit) {
		WmsItem item = null;
		//新增商品包装
		if (packageUnit.isNew()) {
			//判断该货品下是否有件装量为1的包装,有则不允许新建
			//String hql1 = "SELECT COUNT(*) FROM WmsPackageUnit packageUnit WHERE packageUnit.convertFigure = 1 AND packageUnit.item.id = :itemId";
			//Long count1 = (Long) commonDao.findByQueryUniqueResult(hql1, "itemId", itemId);
			//if (count1 > 0 && packageUnit.getConvertFigure().intValue() == 1) {
			//	throw new BusinessException("packageUnit.base.convertFigure.exists");
			//}
			
			//校验行号的唯一性
			String hql2 = "SELECT COUNT(*) FROM WmsPackageUnit packageUnit WHERE packageUnit.lineNo = :lineNo AND packageUnit.item.id = :itemId";
			
			Long count2 = (Long) commonDao.findByQueryUniqueResult(hql2, 
					new String[]{"lineNo","itemId"}, 
					new Object[]{packageUnit.getLineNo(),itemId});
			
			if (count2 > 0) {
				throw new BusinessException("packageUnit.lineNo.exists");
			}
			
			item = commonDao.load(WmsItem.class, itemId);
			item.addPackageUnit(packageUnit);
		} else {
			WmsPackageUnit oldUnit = (WmsPackageUnit) packageUnit.getOldEntity();
			//不允许修改最小包装的件装量和行号
			if (oldUnit.getLineNo().intValue() == 1 && oldUnit.getConvertFigure().intValue() == 1) {
				if (packageUnit.getLineNo().intValue() != 1 || packageUnit.getConvertFigure().intValue() != 1) {
					throw new BusinessException("packageUnit.can.not.edit.base.unit"); 
				}
			}
			item = this.commonDao.load(WmsItem.class, itemId);
			
			Integer oldConvertFigure = oldUnit.getConvertFigure();
			Integer oldPrecision = oldUnit.getPrecision();
			
			if ((oldConvertFigure.intValue()!=packageUnit.getConvertFigure().intValue()) || (oldPrecision.intValue()!=packageUnit.getPrecision().intValue())) {
				addWmsPackageChangeLog(item, packageUnit, oldConvertFigure, oldPrecision);
			}
		}
	}
	
	private void addWmsPackageChangeLog(WmsItem item, WmsPackageUnit packageUnit, Integer oldConvertFigure, Integer oldPrecision){
		WmsPackageChangeLog log = EntityFactory.getEntity(WmsPackageChangeLog.class);
		
		log.setItem(item);
		log.setPackageUnit(packageUnit);
		log.setOldConvertFigure(oldConvertFigure);
		log.setOldPrecision(oldPrecision);
		log.setNewConvertFigure(packageUnit.getConvertFigure());
		log.setNewPrecision(packageUnit.getPrecision());
		
		commonDao.store(log);
	}
	
	/**
	 * 产生库内批次号
	 * @param record
	 */
	private void createItemKeyLot(WmsWarehouse warehouse, WmsItemKey itemKey,LotInfo lotInfo) {
		Map<String,Object> problem = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		//客户名称
		String companyName = itemKey.getItem().getCompany().getName();
		String referenceRule = "批次号规则" ;
		
		problem.put("货品代码", itemKey.getItem().getCode());
		problem.put("生产日期", itemKey.getLotInfo().getProductDate());
					
		result = wmsRuleManager.execute(warehouse.getName(), companyName, referenceRule, problem);										
		String lot = result.get("批次号").toString();		
		itemKey.setLot(lot);		
	}

	/**
	 * 根据ASN上的批次信息获取 WmsItemKey
	 */
	public WmsItemKey getItemKey(WmsWarehouse warehouse, String soi, WmsItem item, LotInfo lotInfo) {
		WmsLotRule lotRule = item.getDefaultLotRule();
		if (lotInfo == null) {
			lotInfo = new LotInfo();
		}
		lotInfo.prepare(lotRule, item, soi);
		if (!lotRule.verify(lotInfo)) {
			throw new BusinessException("lotInfo.is.not.completion");
		}

		// 根据ASN明细信息创建ItemKey信息
		WmsItemKey tempItemKey = EntityFactory.getEntity(WmsItemKey.class);
		tempItemKey.setItem(item);
		tempItemKey.setLotInfo(lotInfo);
		tempItemKey.setHashCode(tempItemKey.genHashCode());
		
		createItemKeyLot(warehouse, tempItemKey,lotInfo);

		return getItemKey(tempItemKey);
	}
	
	public WmsItemKey getItemKey(WmsWarehouse warehouse, String soi, WmsItem item, LotInfo lotInfo, Boolean beVerifyLotInfo) {
		WmsLotRule lotRule = item.getDefaultLotRule();
		if (lotInfo == null) {
			lotInfo = new LotInfo();
		}
		lotInfo.prepare(lotRule, item, soi);
		if (beVerifyLotInfo) {
			if (!lotRule.verify(lotInfo)) {
				throw new BusinessException("lotInfo.is.not.completion");
			}
		}

		// 根据ASN明细信息创建ItemKey信息
		WmsItemKey tempItemKey = EntityFactory.getEntity(WmsItemKey.class);
		tempItemKey.setItem(item);
		tempItemKey.setLotInfo(lotInfo);
		tempItemKey.setHashCode(tempItemKey.genHashCode());
		
		createItemKeyLot(warehouse, tempItemKey,lotInfo);

		return getItemKey(tempItemKey);
	}
	
	public WmsItemKey getItemKey(WmsItemKey itemKey) {
		String query = "FROM WmsItemKey itemKey WHERE itemKey.hashCode = :hashCode";
		
		WmsItemKey dbItemKey = (WmsItemKey) commonDao.findByQueryUniqueResult(query, "hashCode", itemKey.getHashCode());
		
		// 如果数据库中不存在当前ItemKey, 则新创建
		if (dbItemKey == null) {
			dbItemKey = EntityFactory.getEntity(WmsItemKey.class);
			dbItemKey.setItem(itemKey.getItem());
			dbItemKey.setLotInfo(itemKey.getLotInfo());
			dbItemKey.setLot(itemKey.getLot());		
			dbItemKey.setHashCode(itemKey.genHashCode());
			
			commonDao.store(dbItemKey);
			
		}
		
		return dbItemKey;
	}
	
	public List<Boolean> getLotRuleTackersByPageMap(Map map) {
		Long itemId = (Long) map.get("itemId");
		
		if (itemId == null) {
			itemId = (Long) map.get("asnDetail.item.id");
		}
		if (itemId == null) {
			itemId = (Long) map.get("countRecord.itemKey.item.id");
		}
		if (itemId == null) {
			itemId = (Long) map.get("task.itemKey.item.id");
		}
		if (itemId == null) {
			itemId = (Long) map.get("processDoc.item.id");
		}
		
		return getLotRuleTackers(itemId);
	}
	
	public List<Boolean> getShipLotRuleTackersByPageMap(Map map) {
		Long itemId = (Long) map.get("itemId");
		
		if (itemId == null) {
			itemId = (Long) map.get("pickTicketDetail.item.id");
		}
		if (itemId == null) {
			itemId = (Long) map.get("processPlanDetail.item.id");
		}
		
		return getShipLotRuleTackers(itemId);
	}
	
	public List<Boolean> getLotRuleTrackersByASNDetailMap(Map map) {
		WmsASNDetail detail = commonDao.load(WmsASNDetail.class, (Long) map.get("parentId"));
		Long itemId = detail.getItem().getId();
		
		return getLotRuleTackers(itemId);
	}
	
	public List<Boolean> getLotRuleTackers(Long itemId) {
		List<Boolean> results = new ArrayList<Boolean>();
		if(itemId!=null){
			WmsItem item = commonDao.load(WmsItem.class, itemId);
			
			WmsLotRule rule = item.getDefaultLotRule();
			
			if (item != null && rule != null) {
				results.add(rule.getTrackProduceDate());
				results.add(rule.getTrackSupplier());
				results.add(rule.getTrackExtendPropC1());
				results.add(rule.getTrackExtendPropC2());
				results.add(rule.getTrackExtendPropC3());
				results.add(rule.getTrackExtendPropC4());
				results.add(rule.getTrackExtendPropC5());
				results.add(rule.getTrackExtendPropC6());
				results.add(rule.getTrackExtendPropC7());
				results.add(rule.getTrackExtendPropC8());
				results.add(rule.getTrackExtendPropC9());
				results.add(rule.getTrackExtendPropC10());
				results.add(rule.getTrackExtendPropC11());
				results.add(rule.getTrackExtendPropC12());
				results.add(rule.getTrackExtendPropC13());
				results.add(rule.getTrackExtendPropC14());
				results.add(rule.getTrackExtendPropC15());
				results.add(rule.getTrackExtendPropC16());
				results.add(rule.getTrackExtendPropC17());
				results.add(rule.getTrackExtendPropC18());
				results.add(rule.getTrackExtendPropC19());
				results.add(rule.getTrackExtendPropC20());
			}
		}
		return results;
	}
	
	
	public List<Boolean> getShipLotRuleTackers(Long itemId){

		List<Boolean> results = new ArrayList<Boolean>();
		if(itemId!=null){
			WmsItem item = commonDao.load(WmsItem.class, itemId);
			
			WmsLotRule rule = item.getDefaultLotRule();
			
			if (item != null && rule != null) {
				results.add(rule.getTrackStorageDate());
				results.add(rule.getTrackSOI());
				results.add(rule.getTrackSupplier());
				results.add(rule.getTrackExtendPropC1());
				results.add(rule.getTrackExtendPropC2());
				results.add(rule.getTrackExtendPropC3());
				results.add(rule.getTrackExtendPropC4());
				results.add(rule.getTrackExtendPropC5());
				results.add(rule.getTrackExtendPropC6());
				results.add(rule.getTrackExtendPropC7());
				results.add(rule.getTrackExtendPropC8());
				results.add(rule.getTrackExtendPropC9());
				results.add(rule.getTrackExtendPropC10());
				results.add(rule.getTrackExtendPropC11());
				results.add(rule.getTrackExtendPropC12());
				results.add(rule.getTrackExtendPropC13());
				results.add(rule.getTrackExtendPropC14());
				results.add(rule.getTrackExtendPropC15());
				results.add(rule.getTrackExtendPropC16());
				results.add(rule.getTrackExtendPropC17());
				results.add(rule.getTrackExtendPropC18());
				results.add(rule.getTrackExtendPropC19());
				results.add(rule.getTrackExtendPropC20());
			}
		}
		return results;
	
	}
	
	public Boolean isContainLotRule(long lotRuleId){
		List<WmsItem> items = this.commonDao.findByQuery("from WmsItem item left join item.lotRule lotRule where 1=1 and lotRule.id=:lotRuleId", 
				     new String[]{"lotRuleId"}, new Object[]{lotRuleId});
		if(items!=null&&!items.isEmpty()){
			return true;
		}
		return false;
	}
	
	public Map printBarCode(WmsItem item, Long printNumber) {
		Map result = new HashMap();
		Map<Long,String> reportValue = new HashMap<Long, String>();
		String param = ";barCode=" + item.getBarCode();
		reportValue.put(new Long(0), "wmsBarCode.raq&raqParams=" + param);
		if(!reportValue.isEmpty()){
			result.put(IPage.REPORT_VALUES, reportValue);
			result.put(IPage.REPORT_PRINT_NUM, printNumber.intValue());
		}
		return result;
	}
	public void sysInventoryMissDo(List<Object[]> doObj,Map<String,String> itemMap){
		for(Object[] obj : doObj){
			WmsMisInventory mis = EntityFactory.getEntity(WmsMisInventory.class);
			mis.setCompanyName(obj[0].toString());
			mis.setSupCode(obj[1].toString());
			mis.setItemCode(obj[2].toString());
			mis.setExtendPropC1(obj[3].toString());
			mis.setInventoryQty(Double.valueOf(obj[4].toString()));
			mis.setMisQty(Double.valueOf(obj[5].toString()));
			mis.setWarehouse((WmsWarehouse) obj[6]);
			mis.setSupName(obj[7].toString());
			mis.setItemName(itemMap.get(mis.getItemCode()));
			commonDao.store(mis);
//			System.out.println(obj[0]+","+obj[1]+","+obj[2]+","+obj[3]+","+obj[4]+","+obj[5]);
		}
	}
	public void importJjPick(Long companyId,Long billTypeId,File file){
		if (file == null) {
			throw new BusinessException("file_not_found");
		}
		String name = file.getName();
		if (!name.substring(name.lastIndexOf(".") + 1,
				name.lastIndexOf(".") + 4).equals("xls")) {
			throw new BusinessException("this.file.error");
		}
		Sheet sheet = null;
//		Sheet[] sheets = null;
		try {
			Workbook wb = Workbook.getWorkbook(new FileInputStream(file));
			sheet = wb.getSheet(0);
//			sheets = wb.getSheets();// 获取所有的sheet  
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Boolean iserror = false;
		String errorMes = "";
		
		Map<String,WmsItem> items = new HashMap<String, WmsItem>();
		WmsItem item = null;
		String hqlI = "FROM WmsItem item "+
				 " WHERE item.code =:code"+
				 " AND item.status = 'ENABLED'"+
				 " AND item.company.id =:company";
		
		Map<String,WmsPackageUnit> pus = new HashMap<String, WmsPackageUnit>();
		WmsPackageUnit pu = null;
		String hqlP = "FROM WmsPackageUnit p WHERE p.item.id = :itemId AND p.unit = :unit";
		
		Map<String,WmsOrganization> sups = new HashMap<String, WmsOrganization>();
		WmsOrganization sup = null;
		String hqlC = "FROM WmsOrganization sup WHERE sup.code =:code" +
					" AND sup.beSupplier = true"+
					" AND sup.beVirtual =false AND sup.status = 'ENABLED'";
		
		Set<String> itemSet = new HashSet<String>();
		List<Object[]> objs = new ArrayList<Object[]>();
		
		int rowNum = sheet.getRows(); 
		for (int i = 1; i < rowNum; i++) {//含头信息
			String itemCode = sheet.getCell(0,i).getContents();//物料编码
			String itemName = sheet.getCell(1,i).getContents();//物料名称
			String supCode = sheet.getCell(2,i).getContents();//供应商编码
			String supName = sheet.getCell(3,i).getContents();//供应商名称
			String quantity = sheet.getCell(4,i).getContents();//拣货量
			String description = sheet.getCell(5,i).getContents();//备注
			
			Double picQuantity = 0D;
			if(StringUtils.isEmpty(quantity)){
				iserror = true;
				errorMes = "失败!拣货量为空:"+itemCode;
				break;
			}else{
				if(!JavaTools.isNumber(quantity)){
					iserror = true;
					errorMes = "失败!拣货量不是数字";
					break;
				}
				picQuantity = JavaTools.stringToDouble(quantity);
			}
			
			if(sups.containsKey(supCode)){
				sup = sups.get(supCode);
			}else{
				sup = (WmsOrganization) commonDao.findByQueryUniqueResult(hqlC, "code", supCode.trim());
			}
			if(sup==null){
				iserror = true;
				errorMes = "失败!供应商信息为空:"+supCode;
				break;
			}else{
				sups.put(supCode, sup);
			}
			
			if(items.containsKey(itemCode)){
				item = items.get(itemCode);
			}else{
				item = (WmsItem) commonDao.findByQueryUniqueResult(hqlI, new String[]{"code","company"}, 
						new Object[]{itemCode.trim(),companyId});
			}
			if(item==null){
				iserror = true;
				errorMes = "失败!该货主下货品为空:"+itemCode;
				break;
			}else{
				items.put(itemCode, item);
			}
			if(itemSet.contains(itemCode)){
				iserror = true;
				errorMes = "失败!货品不可以重复"+itemCode;
				break;
			}
			itemSet.add(itemCode);
			
			if(pus.containsKey(itemCode)){
				pu = pus.get(itemCode);
			}else{
				pu = (WmsPackageUnit) commonDao.findByQueryUniqueResult(hqlP, 
						new String[]{"itemId", "unit"}, new Object[]{item.getId(),item.getBaseUnit()});
			}
			if(pu==null){
				iserror = true;
				errorMes = "失败!该货品基本包装单位为空:"+itemCode;
				break;
			}else{
				pus.put(itemCode, pu);
			}
			objs.add(new Object[]{
					sup,item,pu,picQuantity,description
			});
		}
		if(iserror){
			LocalizedMessage.setMessage(MyUtils.font2(errorMes));
		}else{
			WmsPickTicketManager wmsPickTicketManager = (WmsPickTicketManager) applicationContext.getBean("wmsPickTicketManager");
			wmsPickTicketManager.importJjPick(companyId,billTypeId,objs);
			errorMes = "成功条数:"+objs.size();
			LocalizedMessage.setMessage(MyUtils.fontByBlue(errorMes));
		}
	}
	//wmsItemManager.importMoveDoc
	public void importMoveDoc(Long companyId,File file){
		if (file == null) {
			throw new BusinessException("file_not_found");
		}
		String name = file.getName();
		if (!name.substring(name.lastIndexOf(".") + 1,
				name.lastIndexOf(".") + 4).equals("xls")) {
			throw new BusinessException("this.file.error");
		}
		Sheet sheet = null;
//		Sheet[] sheets = null;
		try {
			Workbook wb = Workbook.getWorkbook(new FileInputStream(file));
			sheet = wb.getSheet(0);
//			sheets = wb.getSheets();// 获取所有的sheet  
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Boolean iserror = false;
		String errorMes = "";
		
		Long warehouseId = WmsWarehouseHolder.getWmsWarehouse().getId();
		
		Map<String,WmsItem> items = new HashMap<String, WmsItem>();
		WmsItem item = null;
		String hqlI = "FROM WmsItem item "+
				 " WHERE item.code =:code"+
				 " AND item.status = 'ENABLED'"+
				 " AND item.company.id =:company";
		
		Map<String,WmsPackageUnit> pus = new HashMap<String, WmsPackageUnit>();
		WmsPackageUnit pu = null;
		String hqlP = "FROM WmsPackageUnit p WHERE p.item.id = :itemId AND p.unit = :unit";
		
		Map<String,WmsLocation> locs = new HashMap<String, WmsLocation>();
		WmsLocation srcLocation = null;
		WmsLocation descLocation = null;
		String hqlL = "FROM WmsLocation l WHERE l.code =:code AND l.type =:type" +
				" AND l.status = 'ENABLED' AND l.warehouse.id =:warehouse";
		
		Map<String,WmsOrganization> sups = new HashMap<String, WmsOrganization>();
		WmsOrganization sup = null;
		String hqlC = "FROM WmsOrganization sup WHERE sup.code =:code" +
					" AND sup.beSupplier = true"+
					" AND sup.beVirtual =false AND sup.status = 'ENABLED'";
		Set<String> itemSet = new HashSet<String>();
		List<Object[]> objs = new ArrayList<Object[]>();
		int rowNum = sheet.getRows(); 
		for (int i = 1; i < rowNum; i++) {//含头信息
			String srcLoc = sheet.getCell(0,i).getContents();//下架库位
			String supCode = sheet.getCell(1,i).getContents();//供应商编码
			String itemCode = sheet.getCell(2,i).getContents();//物料编码
			String descLoc = sheet.getCell(3,i).getContents();//上架库位
			String movaQuantity = sheet.getCell(4,i).getContents();//移位量
			
			Double putQuantity = 0D;
			if(StringUtils.isEmpty(movaQuantity)){
				iserror = true;
				errorMes = "失败!移位量为空:"+itemCode;
				break;
			}else{
				if(!JavaTools.isNumber(movaQuantity)){
					iserror = true;
					errorMes = "失败!移位量不是数字";
					break;
				}
				putQuantity = JavaTools.stringToDouble(movaQuantity);
			}
			
			if(locs.containsKey(srcLoc)){
				srcLocation = locs.get(srcLoc);
			}else{
				srcLocation = (WmsLocation) commonDao.findByQueryUniqueResult(hqlL,
						new String[]{"code","type","warehouse"},
						new Object[]{srcLoc,WmsLocationType.STORAGE,warehouseId});
			}
			if(srcLocation==null){
				iserror = true;
				errorMes = "失败!下架库位信息为空:"+srcLoc;
				break;
			}else{
				locs.put(srcLoc, srcLocation);
			}
			
			if(locs.containsKey(descLoc)){
				descLocation = locs.get(descLoc);
			}else{
				descLocation = (WmsLocation) commonDao.findByQueryUniqueResult(hqlL,
						new String[]{"code","type","warehouse"},
						new Object[]{descLoc,WmsLocationType.STORAGE,warehouseId});
			}
			if(descLocation==null){
				iserror = true;
				errorMes = "失败!上架库位信息为空:"+descLoc;
				break;
			}else{
				locs.put(descLoc, descLocation);
			}
			
			if(sups.containsKey(supCode)){
				sup = sups.get(supCode);
			}else{
				sup = (WmsOrganization) commonDao.findByQueryUniqueResult(hqlC, "code", supCode.trim());
			}
			if(sup==null){
				iserror = true;
				errorMes = "失败!供应商信息为空:"+supCode;
				break;
			}else{
				sups.put(supCode, sup);
			}
			
			if(items.containsKey(itemCode)){
				item = items.get(itemCode);
			}else{
				item = (WmsItem) commonDao.findByQueryUniqueResult(hqlI, new String[]{"code","company"}, 
						new Object[]{itemCode.trim(),companyId});
			}
			if(item==null){
				iserror = true;
				errorMes = "失败!该货主下货品为空:"+itemCode;
				break;
			}else{
				items.put(itemCode, item);
			}
			if(itemSet.contains(itemCode)){
				iserror = true;
				errorMes = "失败!货品不可以重复,RF上架无供应商扫:"+itemCode;
				break;
			}
			itemSet.add(itemCode);
			
			if(pus.containsKey(itemCode)){
				pu = pus.get(itemCode);
			}else{
				pu = (WmsPackageUnit) commonDao.findByQueryUniqueResult(hqlP, 
						new String[]{"itemId", "unit"}, new Object[]{item.getId(),item.getBaseUnit()});
			}
			if(pu==null){
				iserror = true;
				errorMes = "失败!该货品基本包装单位为空:"+itemCode;
				break;
			}else{
				pus.put(itemCode, pu);
			}
			
			objs.add(new Object[]{//下架库位ID  供应商ID    物料(对象)   上架库位ID   移位量   基本包装单位(对象)
					srcLocation.getId(),sup.getId(),item,descLocation.getId(),putQuantity,pu
			});
		}
		if(iserror){
			LocalizedMessage.setMessage(MyUtils.font2(errorMes));
		}else{
			int size = objs.size();
			if(size>0){
				WmsMoveDocManager wmsMoveDocManager = (WmsMoveDocManager) applicationContext.getBean("wmsMoveDocManager");
				wmsMoveDocManager.importMoveDoc(companyId, objs);
				/*int PAGE_NUMBER = 200;
				int j = JavaTools.getSize(size, PAGE_NUMBER);
				for(int k=0;k<j;k++){
					int toIndex = JavaTools.getIndex(k, size, PAGE_NUMBER);
					List<Object[]> obj = JavaTools.getListObj(objs, k, toIndex, PAGE_NUMBER);
					wmsMoveDocManager.importMoveDoc(companyId, obj);
				}*/
				errorMes = "成功条数:"+size;
				LocalizedMessage.setMessage(MyUtils.fontByBlue(errorMes));
			}
		}
	}
	public void importWmsSpsProductLine(File file){
		if (file == null) {
			throw new BusinessException("file_not_found");
		}
		String name = file.getName();
		if (!name.substring(name.lastIndexOf(".") + 1,
				name.lastIndexOf(".") + 4).equals("xls")) {
			throw new BusinessException("this.file.error");
		}
		Sheet sheet = null;
//		Sheet[] sheets = null;
		try {
			Workbook wb = Workbook.getWorkbook(new FileInputStream(file));
			sheet = wb.getSheet(0);
//			sheets = wb.getSheets();// 获取所有的sheet  
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Boolean iserror = false;
		String errorMes = "";
		
		Map<String,WmsPickTicket> mesNos = new HashMap<String, WmsPickTicket>();
		String hqlM = "FROM WmsPickTicket p WHERE p.relatedBill1 =:relatedBill1 AND p.status =:status";
		
		Map<String,List<Long>> lines = new HashMap<String, List<Long>>();
		String hqlL = "SELECT l.id FROM WmsSpsProductLine l WHERE l.sheetNo =:sheetNo";
		List<Long> ps = null;
		
		Map<String,WmsItem> items = new HashMap<String, WmsItem>();
		WmsItem item = null;
		String hqlI = "FROM WmsItem item "+
				 " WHERE item.code =:code"+
				 " AND item.status = 'ENABLED'";
		List<String[]> strs = new ArrayList<String[]>();
		int rowNum = sheet.getRows(); 
		for (int i = 1; i < rowNum; i++) {//含头信息
			String mesNo = sheet.getCell(0,i).getContents();//MES单号(三日计划单号)
			String productTime = sheet.getCell(1,i).getContents();//上线日期
			String productLine = sheet.getCell(2,i).getContents();//产线
			String station = sheet.getCell(3,i).getContents();//工位
			String sxNo = sheet.getCell(4,i).getContents();//顺序
			String itemCode = sheet.getCell(5,i).getContents();//物料编码
			String itemName = sheet.getCell(6,i).getContents();//物料名称
			String quantity = sheet.getCell(7,i).getContents();//数量
			String remark = sheet.getCell(8,i).getContents();//备注
			
			if(StringUtils.isEmpty(mesNo)){
				iserror = true;
				errorMes = "失败!mes单号为空:"+mesNo;
				break;
			}
			mesNo = mesNo.trim();
			if(!mesNos.containsKey(mesNo)){
				WmsPickTicket p = (WmsPickTicket) commonDao.findByQueryUniqueResult(hqlM, new String[]{"relatedBill1","status"}, 
						new Object[]{mesNo,WmsPickTicketStatus.OPEN});
				if(p==null){
					iserror = true;
					errorMes = "失败!发货单不存在或者状态不符:"+mesNo;
					break;
				}
				mesNos.put(mesNo, p);
			}
			
			if(lines.containsKey(mesNo)){
				ps = lines.get(mesNo);
			}else{
				ps = commonDao.findByQuery(hqlL, new String[]{"sheetNo"}, 
						new Object[]{mesNo});
			}
			if(ps!=null && ps.size()>0){
				iserror = true;
				errorMes = "失败!mes单号下排产顺序已存在:"+mesNo;
				break;
			}else{
				lines.put(mesNo, ps);
			}
			
			if(StringUtils.isEmpty(sxNo)){
				iserror = true;
				errorMes = "失败!顺序为空:"+mesNo;
				break;
			}
			if(!JavaTools.isNumber(sxNo)){
				iserror = true;
				errorMes = "失败!顺序不是数字:"+mesNo;
				break;
			}
			
			if(StringUtils.isEmpty(quantity)){
				iserror = true;
				errorMes = "失败!数量为空:"+mesNo;
				break;
			}
			if(!JavaTools.isNumber(quantity)){
				iserror = true;
				errorMes = "失败!数量不是数字:"+mesNo;
				break;
			}
			
			if(items.containsKey(itemCode)){
				item = items.get(itemCode);
			}else{
				item = (WmsItem) commonDao.findByQueryUniqueResult(hqlI, new String[]{"code"}, 
						new Object[]{itemCode.trim()});
			}
			if(item==null){
				iserror = true;
				errorMes = "失败!该货品为空:"+itemCode;
				break;
			}else{
				items.put(itemCode, item);
			}
			
			if(StringUtils.isEmpty(productLine)){
				iserror = true;
				errorMes = "失败!产线为空:"+itemCode;
				break;
			}
			if(StringUtils.isEmpty(station)){
				iserror = true;
				errorMes = "失败!工位为空:"+itemCode;
				break;
			}
			
			strs.add(new String[]{
					mesNo,productTime,sxNo,itemCode,itemName,station,quantity,remark,productLine
			});
		}
		
		if(iserror){
			LocalizedMessage.setMessage(MyUtils.font2(errorMes));
		}else{
			WmsMoveDocManager wmsMoveDocManager = (WmsMoveDocManager) applicationContext.getBean("wmsMoveDocManager");
			wmsMoveDocManager.importWmsSpsProductLine(strs);
			
			errorMes = "成功条数:"+strs.size();
			LocalizedMessage.setMessage(MyUtils.fontByBlue(errorMes));
		}
	}
	public void importItemClass2(Long companyId,File file){
		if (file == null) {
			throw new BusinessException("file_not_found");
		}
		String name = file.getName();
		if (!name.substring(name.lastIndexOf(".") + 1,
				name.lastIndexOf(".") + 4).equals("xls")) {
			throw new BusinessException("this.file.error");
		}
		Sheet sheet = null;
//		Sheet[] sheets = null;
		try {
			Workbook wb = Workbook.getWorkbook(new FileInputStream(file));
			sheet = wb.getSheet(0);
//			sheets = wb.getSheets();// 获取所有的sheet  
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		/*Map<String,WmsOrganization> companys = new HashMap<String, WmsOrganization>();
		WmsOrganization company = null;
		String hqlC = "FROM WmsOrganization company WHERE (company.name =:name OR company.neiBuName =:name OR company.code =:name)" +
					" AND company.beCompany = true"+
					" AND company.beVirtual =false AND company.status = 'ENABLED'";*/
		
		Map<String,WmsItem> items = new HashMap<String, WmsItem>();
		WmsItem item = null;
		String hqlI = "FROM WmsItem item "+
				 " WHERE item.code =:code"+
				 " AND item.status = 'ENABLED'"+
				 " AND item.company.id =:company";
		
		String hql = "FROM WmsStationAndItem sa WHERE sa.item.code =:code";
		WmsStationAndItem sa = null;
		Map<String,WmsStationAndItem> sas = new HashMap<String, WmsStationAndItem>();
		
		Set<String> itemSet = new HashSet<String>();
		
		Boolean iserror = false;
		String errorMes = "";
		Integer loadage;
		
		int rowNum = sheet.getRows(); 
		for (int i = 1; i < rowNum; i++) {//含头信息
//			String companyName = sheet.getCell(0,i).getContents();//货主名称/内部名称/编码	
			String itemCode = sheet.getCell(0,i).getContents();//物料编码
			String class2 = sheet.getCell(1,i).getContents();//物料属性/拣选分类
			String class5 = sheet.getCell(2,i).getContents();//器具名称
			String type = sheet.getCell(3,i).getContents();//器具型号
			String fullSize = sheet.getCell(4,i).getContents();//装载量
			
			/*if(companys.containsKey(companyName)){
				company = companys.get(companyName);
			}else{
				company = (WmsOrganization) commonDao.findByQueryUniqueResult(hqlC, "name", companyName.trim());
			}
			if(company==null){
				iserror = true;
				errorMes = "失败!货主信息为空:"+companyName;
				break;
			}else{
				companys.put(companyName, company);
			}*/
			
			if(itemSet.contains(itemCode)){
				iserror = true;
				errorMes = "失败!货品不可以重复:"+itemCode;
				break;
			}
			itemSet.add(itemCode);
			
			if(items.containsKey(itemCode)){
				item = items.get(itemCode);
			}else{
				item = (WmsItem) commonDao.findByQueryUniqueResult(hqlI, new String[]{"code","company"}, 
						new Object[]{itemCode.trim(),companyId});
			}
			if(item==null){
				iserror = true;
				errorMes = "失败!货品信息为空:"+itemCode;
				break;
			}else{
				items.put(itemCode, item);
			}
			
			if(!StringUtils.isEmpty(class2)){
				item.setClass2(class2);
			}
			if(!StringUtils.isEmpty(class5)){
				item.setClass5(class5);
				
				if(!StringUtils.isEmpty(fullSize)){
					if(!JavaTools.isNumber(fullSize)){
						iserror = true;
						errorMes = "失败!装载量不是数字:"+itemCode;
						break;
					}
					loadage = JavaTools.stringToInteger(fullSize);
					//查找关系表中是否存在了,存在了修改,不存在新建
					if(sas.containsKey(itemCode)){
						sa = sas.get(itemCode);
					}else{
						sa = (WmsStationAndItem) commonDao.findByQueryUniqueResult(hql, "code",itemCode);
					}
					if(sa==null){
						sa = EntityFactory.getEntity(WmsStationAndItem.class);
						sa.setItem(item);
					}
					if(!StringUtils.isEmpty(type)){
						sa.setType(type);
					}
					sa.setLoadage(loadage);
					sa.setName(class5);
					commonDao.store(sa);
				}
			}
			commonDao.store(item);
			
		}
		if(iserror){
			LocalizedMessage.setMessage(MyUtils.font2(errorMes));
		}else{
			errorMes = "成功更新条数:"+rowNum;
			LocalizedMessage.setMessage(MyUtils.fontByBlue(errorMes));
		}
	}
	
	public void importWmsBlgItem(File file){
		if (file == null) {
			throw new BusinessException("file_not_found");
		}
		String name = file.getName();
		if (!name.substring(name.lastIndexOf(".") + 1,
				name.lastIndexOf(".") + 4).equals("xls")) {
			throw new BusinessException("this.file.error");
		}
		Sheet sheet = null;
//		Sheet[] sheets = null;
		try {
			Workbook wb = Workbook.getWorkbook(new FileInputStream(file));
			sheet = wb.getSheet(0);
//			sheets = wb.getSheets();// 获取所有的sheet  
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Map<String,WmsItem> items = new HashMap<String, WmsItem>();
		WmsItem item = null;
		String hqlI = "FROM WmsItem item "+
				 " WHERE item.code =:code"+
				 " AND item.status = 'ENABLED'";
		List<WmsItem> its = null;
		
		Set<String> itemSet = new HashSet<String>();
		
		Map<String,WmsWorker> blgs = new HashMap<String, WmsWorker>();
		WmsWorker blg = null;
		String hqlB = "FROM WmsWorker wk WHERE wk.code =:code AND wk.status =:status";
		
		
		WmsBlgItem blgItem = null;
		String hqlBi = "FROM WmsBlgItem bi WHERE bi.item.code =:item AND bi.blg.code =:blg AND bi.isA =:isA";
		
		Boolean iserror = false,isA = true;
		String errorMes = "";
		
		int rowNum = sheet.getRows(); 
		for (int i = 1; i < rowNum; i++) {//含头信息
			String itemCode = sheet.getCell(0,i).getContents();//物料编码
			String itemName = sheet.getCell(1,i).getContents();//物料名称
			String blgCode = sheet.getCell(2,i).getContents();//备料工编码
			String blgName = sheet.getCell(3,i).getContents();//备料工名称
			String description = sheet.getCell(4,i).getContents();//备注
			String isBlg = sheet.getCell(5,i).getContents();//是否备料工
			
			if(itemSet.contains(itemCode)){
				iserror = true;
				errorMes = "失败!货品不可以重复:"+itemCode;
				break;
			}
			itemSet.add(itemCode);
			
			if(items.containsKey(itemCode)){
				item = items.get(itemCode);
			}else{
				its = commonDao.findByQuery(hqlI, "code", itemCode.trim());
				if(its!=null && its.size()>0){
					item = its.get(0);
				}
			}
			if(item==null){
				iserror = true;
				errorMes = "失败!该货品为空:"+itemCode;
				break;
			}else{
				items.put(itemCode, item);
			}
			
			if(StringUtils.isEmpty(blgCode)){
				iserror = true;
				errorMes = "失败!备料工编码为空:"+itemCode;
				break;
			}
			if(blgs.containsKey(blgCode)){
				blg = blgs.get(blgCode);
			}else{
				blg = (WmsWorker) commonDao.findByQueryUniqueResult(hqlB, 
						new String[]{"code","status"}, new Object[]{blgCode.trim(),BaseStatus.ENABLED});
			}
			if(blg==null){
				iserror = true;
				errorMes = "失败!备料工不存在或失效:"+blgCode;
				break;
			}
			blgs.put(blgCode, blg);
			
			if(StringUtils.isEmpty(isBlg)){
				iserror = true;
				errorMes = "失败!是否备料工为空:"+blgCode;
				break;
			}
			if("是".equals(isBlg)){
				isA = true;
			}else if("否".equals(isBlg)){
				isA = false;
			}else{
				iserror = true;
				errorMes = "失败!是否备料工只可维护[是/否]:"+isBlg;
				break;
			}
			
			blgItem = (WmsBlgItem) commonDao.findByQueryUniqueResult(hqlBi, 
					new String[]{"item","blg","isA"}, new Object[]{itemCode.trim(),blgCode.trim(),isA});
			if(blgItem==null){
				blgItem = EntityFactory.getEntity(WmsBlgItem.class);
			}
			blgItem.setBlg(blg);
			blgItem.setItem(item);
			blgItem.setIsA(isA);
			blgItem.setRemark(description);
			blgItem.setStatus(BaseStatus.ENABLED);
			commonDao.store(blgItem);
		}
		if(iserror){
			LocalizedMessage.setMessage(MyUtils.font2(errorMes));
		}else{
			errorMes = "成功更新条数:"+(rowNum-1);
			LocalizedMessage.setMessage(MyUtils.fontByBlue(errorMes));
		}
	}
}