package com.vtradex.wms.server.service.receiving.pojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.exception.OriginalBusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.LotInfo;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.organization.TypeOfCompanyAndBill;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.model.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.receiving.WmsASNQualityStauts;
import com.vtradex.wms.server.model.receiving.WmsASNStatus;
import com.vtradex.wms.server.model.receiving.WmsBooking;
import com.vtradex.wms.server.model.receiving.WmsBookingStatus;
import com.vtradex.wms.server.model.receiving.WmsReceivedRecord;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsLocationType;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
import com.vtradex.wms.server.service.base.WmsItemManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.receiving.WmsASNDetailManager;
import com.vtradex.wms.server.service.receiving.WmsASNManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.utils.PackageUtils;
import com.vtradex.wms.server.utils.WmsStringUtils;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

public class DefaultWmsASNDetailManager extends DefaultBaseManager implements
		WmsASNDetailManager {
	private WorkflowManager workflowManager;
	private WmsRuleManager wmsRuleManager;
	private WmsItemManager itemManager;
	private WmsInventoryManager wmsInventoryManager;
	
	public DefaultWmsASNDetailManager(WorkflowManager workflowManager, WmsRuleManager wmsRuleManager,
			WmsItemManager itemManager,WmsInventoryManager wmsInventoryManager) {
		super();
		this.workflowManager = workflowManager;
		this.wmsRuleManager = wmsRuleManager;
		this.itemManager = itemManager;
		this.wmsInventoryManager = wmsInventoryManager;
	}
	@SuppressWarnings("unchecked")
	public WmsLocation findReceiveLocation(WmsWarehouse warehouse, String type
			,String billCode,Boolean bePallet,String supper){
		Map<String, Object> problem = new HashMap<String, Object>();
		problem.put("仓库ID", warehouse.getId());
		problem.put("类型", type);
		problem.put("单据类型编码",billCode);
		problem.put("供应商编码",supper);
//		problem.put("是否托盘", bePallet?"是":"否");
		problem.put("存储类型",BaseStatus.NULLVALUE);
		
		Map<String, Object> resultMap = null;
		List<Map<String, Object>> resultObjs = null;//[{库位序号=21233, 库位代码=1号包装台}]
		try {
			resultMap = wmsRuleManager.execute(warehouse.getName(), warehouse.getName(), "备货库位分配规则", problem);
			resultObjs = (List<Map<String, Object>>) resultMap.get("收货策略库位列表");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getLocalizedMessage());
		}
		Long locId = null;
		if(resultObjs!=null && resultObjs.size()>0){
			locId = (Long) resultObjs.get(0).get("库位序号");
		}else{
			throw new BusinessException("无法找到对应的收货库位!");
		}
		if (locId == null) {
			throw new BusinessException("无法找到对应的收货库位!");
		}
		WmsLocation loc = commonDao.load(WmsLocation.class, locId);
		if (loc == null) {
			throw new BusinessException("无法找到对应的收货库位!");
		}
		return loc;
		
		//收货库位只新建一条[A-1-收货],库位类型[收货],库区[虚拟库区],区排列层[0,0,0,0]
		/*WmsLocation loc = null;
		try {
			loc = (WmsLocation) commonDao.findByQueryUniqueResult("FROM WmsLocation loc WHERE 1=1"
					+ " AND loc.warehouse =:warehouse AND loc.status = 'ENABLED'"
					+ " AND loc.lockCount = false AND loc.type = 'RECEIVE'", 
					new String[]{"warehouse"}, new Object[]{WmsWarehouseHolder.getWmsWarehouse()});
		} catch (Exception e) {
			throw new OriginalBusinessException(e.getLocalizedMessage());
		}
		if(loc==null){
			throw new OriginalBusinessException("当前仓库下未维护虚拟收货库位");
		}
		return loc;*/
	}
	public void receive(Long detailId, LotInfo lotInfo, Long packageUnitId, double quantity,String status, 
			Long receiveLocId, String pallet, String carton, String serialNo,Long workerId) {
		WmsASNDetail detail = commonDao.load(WmsASNDetail.class, detailId);
		detail.setLotInfo(lotInfo);
		if (receiveLocId == 0) {
			WmsLocation loc = findReceiveLocation(detail.getAsn().getWarehouse(), "ASN", 
					detail.getAsn().getBillType().getCode(), false, detail.getLotInfo().getSupplier().getCode());
			receiveLocId = loc.getId();
			/*WmsBooking booking = detail.getBooking();
			WmsDock dock = booking.getDock();
			receiveLocId = dock.getReceiveLocationId();*/
		}
		
		pallet = StringUtils.isEmpty(pallet) ? BaseStatus.NULLVALUE : pallet;
		carton = StringUtils.isEmpty(carton) ? BaseStatus.NULLVALUE : carton;
		serialNo = StringUtils.isEmpty(serialNo) ? BaseStatus.NULLVALUE : serialNo;
		
		//序列号收货数量校验
		if (!WmsStringUtils.isEmpty(serialNo)) {
			if (quantity != 1) {
				throw new OriginalBusinessException("序列号收货时，收货数量必须为1！");
			}
		}
		
		WmsPackageUnit packageUnit = commonDao.load(WmsPackageUnit.class, packageUnitId);
		WmsLocation recLoc = commonDao.load(WmsLocation.class, receiveLocId);
		if(recLoc == null) {
			throw new OriginalBusinessException("收货登记时必须指定收货库位！");
		}	
		
		receiving(detail, quantity, packageUnit, lotInfo, status, recLoc, pallet, carton, serialNo, workerId);
		
		workflowManager.doWorkflow(detail.getAsn(), "wmsASNProcess.receiveAll");
		
	}
	
	public void receive(Long detailId, LotInfo lotInfo, Long packageUnitId, double quantity,String status, 
			Long receiveLocId, String pallet, String carton, String serialNo,Long workerId,String inventoryState) {
		WmsASNDetail detail = commonDao.load(WmsASNDetail.class, detailId);
		detail.setLotInfo(lotInfo);
		if (receiveLocId == 0) {
			WmsLocation loc = findReceiveLocation(detail.getAsn().getWarehouse(), "ASN", 
					detail.getAsn().getBillType().getCode(), false, detail.getLotInfo().getSupplier().getCode());
			receiveLocId = loc.getId();
		}
		
		pallet = StringUtils.isEmpty(pallet) ? BaseStatus.NULLVALUE : pallet;
		carton = StringUtils.isEmpty(carton) ? BaseStatus.NULLVALUE : carton;
		serialNo = StringUtils.isEmpty(serialNo) ? BaseStatus.NULLVALUE : serialNo;
		
		//序列号收货数量校验
		if (!WmsStringUtils.isEmpty(serialNo)) {
			if (quantity != 1) {
				throw new OriginalBusinessException("序列号收货时，收货数量必须为1！");
			}
		}
		
		WmsPackageUnit packageUnit = commonDao.load(WmsPackageUnit.class, packageUnitId);
		WmsLocation recLoc = commonDao.load(WmsLocation.class, receiveLocId);
		if(recLoc == null) {
			throw new OriginalBusinessException("收货登记时必须指定收货库位！");
		}	
		
		receiving(detail, quantity, packageUnit, lotInfo, status, recLoc, pallet, carton, serialNo, workerId,inventoryState);
		
		workflowManager.doWorkflow(detail.getAsn(), "wmsASNProcess.receiveAll");
		
	}
	
	/**
	 * 判断预约单是否完成
	 * @param detail
	 */
	private void bookingReceived(WmsASNDetail detail){
		WmsBooking booking = detail.getBooking();
		if(booking == null){
			return;
		}
		String hql = "SELECT count(*) FROM WmsASNDetail detail WHERE detail.booking.id = :bookingId AND detail.expectedQuantityBU > detail.receivedQuantityBU ";
		Long count = (Long)commonDao.findByQueryUniqueResult(hql, "bookingId", booking.getId());
		if(count == null || count.equals(0L)){
			booking.setFinishTime(new Date());
			booking.setStatus(WmsBookingStatus.FINISH);
			commonDao.store(booking);
			
			hql = "UPDATE WmsBooking booking SET booking.finishTime = :finishTime, booking.status = :status  " +
					"WHERE booking.preId = :preId";
			commonDao.executeByHql(hql, new String[]{"finishTime","status","preId"}, new Object[]{new Date(),WmsBookingStatus.FINISH,booking.getId()});
		}
	}
	
	/**
	 * 收货确认
	 * @param detail
	 * @param receiveQty
	 * @param packageUnit
	 * @param lotInfo
	 * @param recLoc
	 */
	@SuppressWarnings("unchecked")
	public void receiving(WmsASNDetail detail, double receiveQty, WmsPackageUnit packageUnit, LotInfo lotInfo, 
			String status, WmsLocation recLoc, String pallet, String carton, String serialNo,Long workerId) {
		if(detail.getBeReceived()){
			return;
		}
		// 根据ASN明细信息创建ItemKey信息
		WmsItemKey tempItemKey = itemManager.getItemKey(detail.getAsn().getWarehouse(), 
				lotInfo.getSoi(), detail.getItem(), lotInfo);
		
		WmsReceivedRecord record = getReceivedRecord(detail, packageUnit, tempItemKey, status, 
				recLoc, pallet, carton, serialNo,workerId);
		// 根据收货包装和数量写收货记录
		record.setReceivedQuantity(receiveQty);
		record.setReceivedQuantityBU(PackageUtils.convertBUQuantity(receiveQty, packageUnit));
		
		detail.receive(record.getReceivedQuantityBU());
		String hql = "select rule from WmsCompanyAndBillType w "
						+ "where w.company.id=:cId and w.billType.id=:typeId AND w.rule =:rule";
		List<Object> list = commonDao.findByQuery(hql,new String[]{"cId","typeId","rule"},
							new Object[]{detail.getAsn().getCompany().getId(),
							detail.getAsn().getBillType().getId(),TypeOfCompanyAndBill.PRINT_ASN});
		if(list != null && list.size() > 0  && detail.getAsn().getPrintASNReportDate() == null){
			throw new BusinessException("未打印收货单不允许收货");
		}
		
		wmsInventoryManager.receive(record,record.getInventoryStatus());
		record.setBeVerified(Boolean.TRUE);
		WmsLocation dstLoc = commonDao.load(WmsLocation.class, record.getLocationId());
		//暂定一步收货方式
		if (WmsLocationType.STORAGE.equals(dstLoc.getType())) {
			record.addMovedQuantity(record.getReceivedQuantityBU());
			workflowManager.doWorkflow(record.getAsn(), "wmsASNPutawayProcess.moveConfirm");
		}
		if(!WmsASNQualityStauts.NOQUALITY.equals(record.getAsn().getQualityStauts())){
			workflowManager.doWorkflow(record.getAsn(), "wmsASNQualityProcess.qualitySuccess");
		}
	}
	
	/**
	 * 收货确认 Telnet
	 */
	@SuppressWarnings("unchecked")
	public void receiving(WmsASNDetail detail, double receiveQty, WmsPackageUnit packageUnit, LotInfo lotInfo, 
			String status, WmsLocation recLoc, String pallet, String carton, String serialNo,Long workerId,String inventoryState) {
		if(detail.getBeReceived()){
			return;
		}
		if(detail.getExpectedQuantityBU() - detail.getReceivedQuantityBU() <= 0){
			return;
		}
		// 根据ASN明细信息创建ItemKey信息
		WmsItemKey tempItemKey = itemManager.getItemKey(detail.getAsn().getWarehouse(), 
				lotInfo.getSoi(), detail.getItem(), lotInfo);
		
		WmsReceivedRecord record = getReceivedRecord(detail, packageUnit, tempItemKey, status, 
				recLoc, pallet, carton, serialNo,workerId);
		// 根据收货包装和数量写收货记录
		record.setReceivedQuantity(receiveQty);
		record.setReceivedQuantityBU(PackageUtils.convertBUQuantity(receiveQty, packageUnit));
		
		detail.receive(record.getReceivedQuantityBU());
		String hql = "select rule from WmsCompanyAndBillType w "
						+ "where w.company.id=:cId and w.billType.id=:typeId AND w.rule =:rule";
		List<Object> list = commonDao.findByQuery(hql,new String[]{"cId","typeId","rule"},
							new Object[]{detail.getAsn().getCompany().getId(),
							detail.getAsn().getBillType().getId(),TypeOfCompanyAndBill.PRINT_ASN});
		if(list != null && list.size() > 0  && detail.getAsn().getPrintASNReportDate() == null){
			throw new BusinessException("未打印收货单不允许收货");
		}
		
		wmsInventoryManager.receive(record,inventoryState);
		record.setBeVerified(Boolean.TRUE);
		WmsLocation dstLoc = commonDao.load(WmsLocation.class, record.getLocationId());
		//暂定一步收货方式
		if (WmsLocationType.STORAGE.equals(dstLoc.getType())) {
			record.addMovedQuantity(record.getReceivedQuantityBU());
			workflowManager.doWorkflow(record.getAsn(), "wmsASNPutawayProcess.moveConfirm");
		}
		if(!WmsASNQualityStauts.NOQUALITY.equals(record.getAsn().getQualityStauts())){
			workflowManager.doWorkflow(record.getAsn(), "wmsASNQualityProcess.qualitySuccess");
		}
	}
	
	protected Double getReceivedOvercharges(WmsReceivedRecord record){
		String warehouseName = WmsWarehouseHolder.getWmsWarehouse().getName();
		String companyName = record.getAsn().getCompany().getName();
		String class1 = record.getItemKey().getItem().getClass1();
		String item = record.getItemKey().getItem().getCode(); 
		Object value = wmsRuleManager.getSingleRuleTableDetail(warehouseName,
				"R101_收货_收货规则表","可超收数量",companyName,class1,item);
		if(value == null){
			value = wmsRuleManager.getSingleRuleTableDetail(warehouseName,
					"R101_收货_收货规则表","可超收数量",companyName,class1,"所有");
		}
		return value == null ? 0D : Double.valueOf(value.toString());
	}
	
	/**
	 * 创建收货记录
	 * @param detail
	 * @param itemKey
	 * @param recLoc
	 */
	private WmsReceivedRecord getReceivedRecord(WmsASNDetail detail, WmsPackageUnit packageUnit, WmsItemKey itemKey, 
			String status, WmsLocation recLoc, String pallet, String carton, String serialNo,Long workerId) {
		// 调用库存状态规则获取当前明细的库存状态
		String inventoryStatus = "";
				
		Map<String,Object> problem = new HashMap<String, Object>();
		problem.put("供应商",detail.getAsn().getSupplier().getCode());
		problem.put("货品代码", detail.getItem().getCode());
		problem.put("工艺状态", 
				MyUtils.getTypeOfExtendPropC1("TypeOfExtendPropC1."+detail.getLotInfo().getExtendPropC1()));
		problem.put("单据类型", detail.getAsn().getBillType().getCode());
		problem.put("货主代码",detail.getAsn().getCompany().getCode());
		problem.put("待检量",detail.getQualityQuantityBU()==null?0:detail.getQualityQuantityBU());
		Map<String, Object> result = wmsRuleManager.execute(detail.getAsn().getWarehouse().getName(), 
				detail.getAsn().getCompany().getName(), "收货货品状态规则", problem);
		inventoryStatus = result.get("状态").toString();
		// 生成收货记录
		WmsReceivedRecord record = EntityFactory.getEntity(WmsReceivedRecord.class);
		
		record.setAsn(detail.getAsn());
		record.setAsnDetail(detail);
		record.setItemKey(itemKey);
		record.setInventoryStatus(inventoryStatus);
		record.setPackageUnit(packageUnit);
		record.setLocationId(recLoc.getId());
		record.setPallet(pallet);
		record.setCarton(carton);
		record.setSerialNo(serialNo);
		record.setBeVerified(Boolean.FALSE);
		record.setWorker(workerId==null?null:commonDao.load(WmsWorker.class, workerId));
		
		commonDao.store(record);
		return record;
	}
	public void asnDetailReservation(Long dockId,Date reserveBeginTime,Date reserveFinishTime,WmsASN asn){
		commonDao.executeByHql("UPDATE WmsASNDetail detail SET detail.dock.id = :dockId, detail.reserveBeginTime = :reserveBeginTime,detail.reserveFinishTime=:reserveFinishTime WHERE detail.asn.id = :asnId ",
			new String[]{"dockId","reserveBeginTime","reserveFinishTime","asnId"}, 
			new Object[]{dockId,reserveBeginTime,reserveFinishTime,asn.getId()});
	}
	
	public Boolean isRequireQuality(WmsASN wmsAsn) {
		Map<String,Object> problem = new HashMap<String, Object>();
		problem.put("货主", wmsAsn.getCompany().getName());
		problem.put("单据类型", wmsAsn.getBillType().getName());		
		Map<String, Object> result = wmsRuleManager.execute(wmsAsn.getWarehouse().getName(), 
				wmsAsn.getCompany().getName(), "收货质检规则", problem);
		
		Integer isLock = Integer.valueOf(result.get("是否质检").toString());
		if (isLock.intValue() == 0) {
			return false;
		}
		return true;
	}
	@SuppressWarnings({ "unused", "unchecked" })
	public void importFile(Long companyId,Long billTypeId,File file){
		String name = file.getName();
		if (file == null) {
			throw new BusinessException("file_not_found");
		} else if (!name.substring(name.lastIndexOf(".") + 1,
				name.lastIndexOf(".") + 4).equals("xls")) {
			throw new BusinessException("this.file.error");
		}
		Workbook book = null;
		Sheet sheet = null;
		try {
			book = Workbook.getWorkbook(new FileInputStream(file));
			sheet = book.getSheet(0);
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int rowNum = sheet.getRows();
		WmsOrganization company = commonDao.load(WmsOrganization.class, companyId);
		String[] length = new String[]{
				"供方名称","供方代码","订单日期","预计到货日期","零件图号","零件名称","问题描述","数量","工艺状态","库存状态"
		};
		List<Object[]> objs = new ArrayList<Object[]>();
		Boolean isError = false;
		String errorMesg = "";
		Map<String,WmsOrganization> companys = new HashMap<String, WmsOrganization>();
		Map<String,WmsOrganization> sups = new HashMap<String, WmsOrganization>();
		Map<String,WmsItem> items = new HashMap<String, WmsItem>();
		Map<String,WmsPackageUnit> units = new HashMap<String, WmsPackageUnit>();
		for (int i = 1; i < rowNum; i++) {//含头信息
			int j = 0;
//			String companyCode = sheet.getCell(j, i).getContents();j++;
//			if(companys.containsKey(companyCode)){
//				company = companys.get(companyCode);
//			}else{
//				List<WmsOrganization> cc = commonDao.findByQuery("FROM WmsOrganization company"
//						+ " WHERE company.beCompany = true AND company.code =:companyCode"
//						+ " and company.beVirtual =false AND company.status = 'ENABLED'",
//						new String[]{"companyCode"},new Object[]{companyCode});
//				if(cc!=null && cc.size()>0){
//					company = cc.get(0);
//					companys.put(companyCode, company);
//				}else{
//					isError = true;
//					errorMesg = "货主["+companyCode+"]不存在或已失效("+(i+1)+"行"+j+"列)";
//					break;
//				}
//			}
			String supName = sheet.getCell(j, i).getContents();j++;
			String supCode = sheet.getCell(j, i).getContents();j++;
			WmsOrganization supplier = null;
			if(sups.containsKey(supCode)){
				supplier = sups.get(supCode);
			}else{
				List<WmsOrganization> suu = commonDao.findByQuery("FROM WmsOrganization company WHERE company.beSupplier = true"
						+ " and company.beVirtual =false AND company.status = 'ENABLED' AND company.code =:supCode",
						new String[]{"supCode"},new Object[]{supCode});
				if(suu!=null && suu.size()>0){
					supplier = suu.get(0);
					sups.put(supCode, supplier);
				}else{
					isError = true;
					errorMesg = "供应商["+supCode+"]不存在或已失效("+(i+1)+"行"+j+"列)";
					break;
				}
			}
			//yyyy-MM-dd
			String orderDates = sheet.getCell(j, i).getContents();j++;
			Date orderDate = null;
			if(StringUtils.isEmpty(orderDates)){
				orderDate = new Date();
			}else{
				if(JavaTools.patternYMD(orderDates)){
					orderDate = JavaTools.stringToDate(orderDates);
				}else{
					isError = true;
					errorMesg = "订单日期不合法(如2001-01-01)("+(i+1)+"行"+j+"列)";
					break;
				}
			}
			//yyyy-MM-dd
			String estimateDates = sheet.getCell(j, i).getContents();j++;
			Date estimateDate = null;
			if(StringUtils.isEmpty(estimateDates)){
				estimateDate = new Date();
			}else{
				if(JavaTools.patternYMD(estimateDates)){
					estimateDate = JavaTools.stringToDate(estimateDates);
				}else{
					isError = true;
					errorMesg = "预计到货日期不合法(如2001-01-01)("+(i+1)+"行"+j+"列)";
					break;
				}
			}
			String itemCode = sheet.getCell(j, i).getContents();j++;
			WmsItem item = null;
			WmsPackageUnit packageUnit = null;
			if(items.containsKey(itemCode)){
				item = items.get(itemCode);
				packageUnit = units.get(itemCode);
			}else{
				List<WmsItem> its = commonDao.findByQuery("FROM WmsItem item WHERE item.status = 'ENABLED'"
						+ " AND item.company.id =:companyId AND item.code =:itemCode",
						new String[]{"companyId","itemCode"},new Object[]{company.getId(),itemCode});
				if(its!=null && its.size()>0){
					item = its.get(0);
					items.put(itemCode, item);
					
					List<WmsPackageUnit> pus = commonDao.findByQuery("FROM WmsPackageUnit packageUnit WHERE 1=1"
							+ " AND packageUnit.item.id =:itemId ORDER BY packageUnit.lineNo", 
							new String[]{"itemId"}, new Object[]{item.getId()});
					if(pus!=null && pus.size()>0){
						packageUnit = pus.get(0);
						units.put(itemCode, packageUnit);
					}else{
						isError = true;
						errorMesg = "物料["+itemCode+"]包装不存在("+(i+1)+"行"+j+"列)";
						break;
					}
				}else{
					isError = true;
					errorMesg = "物料["+itemCode+"]不存在或已失效("+(i+1)+"行"+j+"列)";
					break;
				}
			}
			String itemName = sheet.getCell(j, i).getContents();j++;
			
			String notes = sheet.getCell(j, i).getContents();j++;
			String quantity = sheet.getCell(j, i).getContents();j++;
			if(quantity==null || !JavaTools.isNumber(quantity)){
				isError = true;
				errorMesg = "数量为空或不是合法的正整数("+(i+1)+"行"+j+"列)";
				break;
			}
			String gyzt = sheet.getCell(j, i).getContents();j++;
			String extendPropC1 = MyUtils.checkExtendPropc1Back(gyzt);
			if(extendPropC1==null){
				isError = true;
				errorMesg = "工艺状态不合法(-,最新,新,老,最老)("+(i+1)+"行"+j+"列)";
				break;
			}
			String inventotyStatus = sheet.getCell(j, i).getContents();j++;
			
			objs.add(new Object[]{
					company,supplier,orderDate,estimateDate,item,notes,quantity,extendPropC1,company.getCode(),supCode,packageUnit
			});
			
		}
		if(isError){
			LocalizedMessage.addLocalizedMessage(MyUtils.font(errorMesg));
		}else{
			Map<String,List<Object[]>> csMap = groupByCS(objs);
			Map<String,WmsASN> asns = matchAsn(csMap,billTypeId);
			
			WmsASNManager wmsASNManager = (WmsASNManager)applicationContext.getBean("wmsASNManager");
			wmsASNManager.saveAsnFile(company,billTypeId, csMap, asns);
		}
		file.delete();
	}
	/**根据货主和供应商将数据分组**/
	private Map<String,List<Object[]>> groupByCS(List<Object[]> objs){
		Map<String,List<Object[]>> csMap = new HashMap<String, List<Object[]>>();
		String key = null;
		List<Object[]> temp = null;
		for(Object[] obj : objs){
			key = obj[8]+MyUtils.spilt1+obj[9];
			if(csMap.containsKey(key)){
				temp = csMap.get(key);
			}else{
				temp = new ArrayList<Object[]>();
			}
			temp.add(obj);
			csMap.put(key, temp);
		}objs.clear();
		return csMap;
	}
	/**根据货主+供应商+单据类型匹配相同的ASN**/
	@SuppressWarnings({ "unchecked" })
	private Map<String,WmsASN> matchAsn(Map<String,List<Object[]>> csMap,Long billTypeId){
		Map<String,WmsASN> asns = new HashMap<String, WmsASN>();
		Iterator<Entry<String, List<Object[]>>> ii = csMap.entrySet().iterator();
		while(ii.hasNext()){
			Entry<String, List<Object[]>> entry = ii.next();
			String[] keys = entry.getKey().split(MyUtils.spilt1);
			List<WmsASN> as = commonDao.findByQuery("FROM WmsASN a WHERE a.company.code =:companyCode"
				+ " AND a.supplier.code =:supplierCode AND a.status =:status AND a.billType.id =:billTypeId", 
				new String[]{"companyCode","supplierCode","status","billTypeId"},
				new Object[]{keys[0],keys[1],WmsASNStatus.OPEN,billTypeId});
			if(as!=null && as.size()>0){
				asns.put(entry.getKey(), as.get(0));
			}
		}
		return asns;
	}
}
