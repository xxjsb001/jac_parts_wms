package com.vtradex.wms.server.service.message.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.exception.OriginalBusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.ShipLotInfo;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.inventory.WmsInventoryFee;
import com.vtradex.wms.server.model.inventory.WmsInventoryLog;
import com.vtradex.wms.server.model.inventory.WmsInventoryLogType;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.move.WmsBoxDetail;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsMoveDocShipStatus;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.model.move.WmsMoveDocType;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.move.WmsTaskLog;
import com.vtradex.wms.server.model.move.WmsTaskStatus;
import com.vtradex.wms.server.model.move.WmsTempMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsWorkDoc;
import com.vtradex.wms.server.model.organization.TypeOfBill;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsItemState;
import com.vtradex.wms.server.model.organization.WmsLotRule;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.receiving.WmsBookingStatus;
import com.vtradex.wms.server.model.receiving.WmsReceivedRecord;
import com.vtradex.wms.server.model.shipping.WmsBOLStateLog;
import com.vtradex.wms.server.model.shipping.WmsMasterBOL;
import com.vtradex.wms.server.model.shipping.WmsPickTicket;
import com.vtradex.wms.server.model.shipping.WmsPickTicketDetail;
import com.vtradex.wms.server.model.shipping.WmsPickTicketStatus;
import com.vtradex.wms.server.model.shipping.WmsWaveDocDetail;
import com.vtradex.wms.server.model.warehouse.WmsDock;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsLocationType;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWarehouseArea;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.message.WmsMessageManager;
import com.vtradex.wms.server.service.receiving.WmsMoveDocManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.rule.WmsTransactionalManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.task.WmsTaskManager;
import com.vtradex.wms.server.service.workDoc.WmsWorkDocManager;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.LotInfoUtil;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.utils.PackageUtils;
import com.vtradex.wms.server.utils.WmsStringUtils;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DefaultWmsMessageManager extends DefaultBaseManager implements WmsMessageManager {
	
	protected WorkflowManager workflowManager;
	protected WmsBussinessCodeManager bussinessCodeManager;
	protected WmsInventoryManager wmsInventoryManager;
	protected WmsRuleManager wmsRuleManager;
	protected WmsTransactionalManager wmsTransactionalManager;
	protected WmsTaskManager wmsTaskManager;
	protected WmsWorkDocManager wmsWorkDocManager;
	protected WmsMoveDocManager wmsMoveDocManager;
	
	public DefaultWmsMessageManager(WorkflowManager workflowManager, WmsBussinessCodeManager bussinessCodeManager,
			WmsInventoryManager wmsInventoryManager, WmsRuleManager wmsRuleManager, 
			WmsTransactionalManager wmsTransactionalManager, WmsTaskManager wmsTaskManager,
			WmsWorkDocManager wmsWorkDocManager, WmsMoveDocManager wmsMoveDocManager) {
		super();
		this.workflowManager = workflowManager;
		this.bussinessCodeManager = bussinessCodeManager;
		this.wmsInventoryManager = wmsInventoryManager;
		this.wmsRuleManager = wmsRuleManager;
		this.wmsTransactionalManager = wmsTransactionalManager;
		this.wmsTaskManager = wmsTaskManager;
		this.wmsWorkDocManager = wmsWorkDocManager;
		this.wmsMoveDocManager = wmsMoveDocManager;
	}
	
	/**
	 * 创建内置的单据类型
	 */
	public void subscriberCreateBillType(Object object) {
		WmsOrganization organization = (WmsOrganization) object;
		
		if (organization.isBeCompany()) {
			if (!organization.isNew()) {
				List<WmsBillType> wbts = commonDao.findByQuery("FROM WmsBillType billType " +
						" WHERE billType.company.id = :companyId AND billType.beInner = true",
						new String[] {"companyId"}, new Object[] {organization.getId()});
				if (wbts != null && wbts.size() > 0) {
					return;
				}
			}

			//收货上架单			
			WmsBillType billType1 = EntityFactory.getEntity(WmsBillType.class);
			billType1.setCompany(organization);
			billType1.setCode(WmsMoveDocType.MV_PUTAWAY);
			billType1.setName("收货上架单");
			billType1.setDescription("");
			billType1.setType(TypeOfBill.MOVE);
			billType1.setBeInner(true);
			billType1.setStatus(BaseStatus.ENABLED);
			commonDao.store(billType1);
			
			//库内移位单
			WmsBillType billType2 = EntityFactory.getEntity(WmsBillType.class);
			billType2.setCompany(organization);
			billType2.setCode(WmsMoveDocType.MV_MOVE);
			billType2.setName("库内移位单");
			billType2.setDescription("");
			billType2.setType(TypeOfBill.MOVE);
			billType2.setBeInner(true);
			billType2.setStatus(BaseStatus.ENABLED);
			commonDao.store(billType2);
			
			//发货拣货单
			WmsBillType billType3 = EntityFactory.getEntity(WmsBillType.class);
			billType3.setCompany(organization);
			billType3.setCode(WmsMoveDocType.MV_PICKTICKET_PICKING);
			billType3.setName("发货单拣货");
			billType3.setDescription("");
			billType3.setType(TypeOfBill.MOVE);
			billType3.setBeInner(true);
			billType3.setStatus(BaseStatus.ENABLED);
			commonDao.store(billType3);
			
			//波次拣货单
			WmsBillType billType4 = EntityFactory.getEntity(WmsBillType.class);
			billType4.setCompany(organization);
			billType4.setCode(WmsMoveDocType.MV_WAVE_PICKING);
			billType4.setName("波次拣货单");
			billType4.setDescription("");
			billType4.setType(TypeOfBill.MOVE);
			billType4.setBeInner(true);
			billType4.setStatus(BaseStatus.ENABLED);
			commonDao.store(billType4);

			//加工拣货单
			WmsBillType billType5 = EntityFactory.getEntity(WmsBillType.class);
			billType5.setCompany(organization);
			billType5.setCode(WmsMoveDocType.MV_PROCESS_PICKING);
			billType5.setName("加工拣货单");
			billType5.setDescription("");
			billType5.setType(TypeOfBill.MOVE);
			billType5.setBeInner(true);
			billType5.setStatus(BaseStatus.ENABLED);
			commonDao.store(billType5);
			
			//补货移位单
			WmsBillType billType6 = EntityFactory.getEntity(WmsBillType.class);
			billType6.setCompany(organization);
			billType6.setCode(WmsMoveDocType.MV_REPLENISHMENT_MOVE);
			billType6.setName("补货移位单");
			billType6.setDescription("");
			billType6.setType(TypeOfBill.MOVE);
			billType6.setBeInner(true);
			billType6.setStatus(BaseStatus.ENABLED);
			commonDao.store(billType6);
		}
	}
	public void subscriberCreateWmsOrganization(Object object){
		WmsWarehouse warehouse = (WmsWarehouse)object;
		WmsOrganization organization = new WmsOrganization();
		organization.setCode(warehouse.getCode());
		organization.setName(warehouse.getName()+"--虚拟");
		organization.setBeCompany(true);
		organization.setBeVirtual(true);
		this.workflowManager.doWorkflow(organization, "organizationProcess.new");
	}
	
	public void subscriberEditWmsOrganization(Object object){
		WmsWarehouse warehouse = (WmsWarehouse)object;
		String hql = "FROM WmsOrganization organ WHERE organ.code = :code AND organ.beCompany = true AND organ.beVirtual = true";
		
		WmsOrganization organization = (WmsOrganization)commonDao.findByQueryUniqueResult(hql, "code", warehouse.getCode());
		if(organization == null){
			organization = new WmsOrganization();
		}
		organization.setCode(warehouse.getCode());
		organization.setName(warehouse.getName()+"--虚拟");
		organization.setBeCompany(true);
		organization.setBeVirtual(true);
		if(organization.isNew()){
			this.workflowManager.doWorkflow(organization, "organizationProcess.new");
		}
		commonDao.store(organization);
	}
	/**
	 * 创建移位单
	 * 
	 * @param entity
	 */
	public void subscriberCreateWmsMoveDoc(Object entity) {
		wmsMoveDocManager.createWmsMoveDoc(entity);
	}
	
	/**
	 * 移位单自动分配
	 * 
	 * @param entity
	 */
	public void subscriberAutoAllocateWmsMoveDoc(Object entity) {
		WmsMoveDoc moveDoc = (WmsMoveDoc)entity;
		
		if(moveDoc.isPutawayType() || moveDoc.isMoveType()) {
			this.subscriberPutawayAllocate(entity);
		} else if (moveDoc.isPickTicketType() || moveDoc.isReplenishmentType()){
			if (moveDoc.getUnAllocateQuantityBU() <= 0) {
				return;
			}
			subscriberPickTicketModelAllocate(moveDoc);
		} else if(moveDoc.isWaveType()){
			subscriberPickTicketModelAllocate(moveDoc);
		} else if (moveDoc.isReplenishmentType()) {
			subscriberReplenishmentModelAllocate(moveDoc);
		} else if(moveDoc.isProcessType()){
			if (moveDoc.getUnAllocateQuantityBU() <= 0) {
				return;
			}
			subscriberPickTicketModelAllocate(moveDoc);
		}
	}
	
	public void subscriberPutawayAllocate(Object object) {
		WmsMoveDoc moveDoc = (WmsMoveDoc) object;
		WmsBillType billType = null;
		if (moveDoc.getOriginalBillType() != null) {
			billType = commonDao.load(WmsBillType.class, moveDoc.getOriginalBillType().getId());
		} else {
			billType = commonDao.load(WmsBillType.class, moveDoc.getBillType().getId());
		}
		
		WmsOrganization company = commonDao.load(WmsOrganization.class, moveDoc.getCompany().getId());
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class, moveDoc.getWarehouse().getId());				
		String companyName = company.getName();
		if(company.isBeVirtual()){
			companyName = warehouse.getName();
		}
		String warehouseName = warehouse.getName();
		Long warehouseId = warehouse.getId();
		
		List<WmsTempMoveDocDetail> tempMoveDocDetails = constructTempDetails(moveDoc);
		String relateBill = null;
		Long tempCrossDockitemId = null;
		double tempCrossDockQuantity = 0D;
		
		StringBuffer buf = new StringBuffer();
		for (WmsTempMoveDocDetail detail : tempMoveDocDetails) {
			try {
				//根据移位单明细创建对应的TASK
				Map<String,Object> problem = new HashMap<String, Object>();
				
				double planQuantity = 0.0;
				double planQuantityBU = 0.0;
				
				String locationType = detail.getLocationType();
				
				WmsLocation fromLocation = commonDao.load(WmsLocation.class, detail.getFromLocationId());
				WmsItemKey itemKey = commonDao.load(WmsItemKey.class, detail.getItemKey().getId());
				WmsItem item = commonDao.load(WmsItem.class, itemKey.getItem().getId());			
				WmsPackageUnit packageUnit = commonDao.load(WmsPackageUnit.class, detail.getPackageUnit().getId());
				
				if (BaseStatus.PALLET.equals(detail.getLocationType())) {
					planQuantity = detail.getDetails().size();
					planQuantityBU = planQuantity;
					locationType = "是";
				} else {
					planQuantity = detail.getPlanQuantity();
					planQuantityBU = planQuantity * packageUnit.getConvertFigure();
					locationType = "否";
				}
				if(tempCrossDockitemId == null 
					|| (tempCrossDockitemId.longValue() != item.getId().longValue())) {
					relateBill = null;
					tempCrossDockitemId = item.getId();
					tempCrossDockQuantity = 0D;
				}
				//每托数量BU 和规则里一致
				Double palletQuantity = detail.getPalletQuantity() * packageUnit.getConvertFigure();
				
				problem.put("仓库ID", warehouseId);					
				problem.put("所属仓库", warehouseName);					
				problem.put("货主", companyName);
				problem.put("单据类型", billType.getName());			
				problem.put("是否托盘", locationType);
				problem.put("货品状态", detail.getInventoryStatus());
				problem.put("计划移位数量", planQuantity);
				problem.put("计划移位数量BU", planQuantityBU);
				problem.put("每托数量", palletQuantity);	
				problem.put("批次序号", itemKey.getId());
				problem.put("货品代码", item.getCode());
				problem.put("货品序号", item.getId());	
				problem.put("货品分类", item.getClass1());	
				problem.put("包装单位序号", packageUnit.getId());						
				problem.put("货品重量", packageUnit.getWeight());	
				problem.put("货品体积", packageUnit.getVolume());
				problem.put("包装级别", packageUnit.getLevel());
				problem.put("折算系数", packageUnit.getConvertFigure());
				problem.put("月台区号", fromLocation.getZone());			
				problem.put("移出库位ID", fromLocation.getId());			
				problem.put("库区序号", fromLocation.getWarehouseArea().getId());	
				problem.put("越库相关单号", relateBill);
				problem.put("越库已分配数量", tempCrossDockQuantity);
				
				Map<String, Object> result = wmsRuleManager.execute(warehouseName, companyName, "上架分配规则", problem);				
				
				//处理返回数据时加入事务
				tempCrossDockQuantity += wmsTransactionalManager.putawayAllocate(detail, result, detail.getLocationType());	
				relateBill = moveDoc.getCode();
			} catch (BusinessException e) {
				buf.append(e.getMessage() + "\n");
			}
		}
		if(buf.length() > 0){
			LocalizedMessage.addLocalizedMessage(buf.toString());
		}
	}
	
	/**
	 * 发货拣货分配，按单
	 * @param moveDoc
	 */
	protected void subscriberPickTicketModelAllocate(WmsMoveDoc moveDoc){
		String hql = "SELECT d.id FROM WmsMoveDocDetail d where d.moveDoc.id = :moveDocId and (d.planQuantityBU - d.allocatedQuantityBU) > 0";
		List<Long> moveDocDetailIdList = commonDao.findByQuery(hql, "moveDocId", moveDoc.getId());
		if (moveDocDetailIdList.isEmpty()) {
//			throw new BusinessException("wmsmovedoc.detail.not.exist");
			return;
		}
		
		WmsOrganization company = commonDao.load(WmsOrganization.class, moveDoc.getCompany().getId());
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class, moveDoc.getWarehouse().getId());
		WmsBillType billType = null;
		WmsOrganization customer = null;
		if(moveDoc.isReplenishmentType()){
			billType = commonDao.load(WmsBillType.class, moveDoc.getBillType().getId());
		}else{
			WmsPickTicket pickTicket = commonDao.load(WmsPickTicket.class, moveDoc.getPickTicket().getId());
			if (pickTicket.getCustomer() != null) {
				customer = commonDao.load(WmsOrganization.class, pickTicket.getCustomer().getId());
			}
			billType = commonDao.load(WmsBillType.class, pickTicket.getBillType().getId());
		}
		
		String companyName = company.getName();
		if(company.isBeVirtual()){
			companyName = warehouse.getName();
		}
		String warehouseName = warehouse.getName();
		Long warehouseId = warehouse.getId();
		
		for (Long moveDocDetailId : moveDocDetailIdList) {
			try {
				WmsMoveDocDetail moveDocDetail = commonDao.load(WmsMoveDocDetail.class, moveDocDetailId);
				
				if (moveDocDetail.getUnAllocateQuantityBU() <= 0) {
					return;
				}
				moveDocDetail.setMoveDoc(moveDoc);
				Map<String, Object> problem = new HashMap<String, Object>();
				problem.put("仓库ID", warehouseId);
				problem.put("货主", companyName);
				problem.put("单据类型编码", billType.getCode());
//				problem.put("源拣货库位序号", 0L);
				problem.put("收货人",customer==null?"":customer.getName());
				WmsItem item = commonDao.load(WmsItem.class, moveDocDetail.getItem().getId());
//				WmsPackageUnit packageUnit = commonDao.load(WmsPackageUnit.class, moveDocDetail.getPackageUnit().getId());
//				problem.put("包装级别", packageUnit.getLevel());
				problem.put("货品ID", item.getId());
				problem.put("货品编码", item.getCode());
				problem.put("待拣选数量", moveDocDetail.getUnAllocateQuantityBU());
				problem.put("库存状态", moveDocDetail.getInventoryStatus());
				problem.put("数量", moveDocDetail.getPlanQuantityBU());
				problem.put("类型", MyUtils.getMoveType(moveDoc.getType()));
				ShipLotInfo shipLotInfo = moveDocDetail.getShipLotInfo();
				if (shipLotInfo == null) {
					shipLotInfo = new ShipLotInfo();
				}
				/**
				 * FIXME Boolean.TRUE 表示严格批次匹配发货, 实际项目中可供用户灵活设置该属性
				 */
				LotInfoUtil.generateShipLotInfo(problem, shipLotInfo, Boolean.TRUE);
				
				Map<String, Object> result = wmsRuleManager.execute(warehouseName, companyName, "拣货分配规则", problem);
				wmsMoveDocManager.doAutoAllocateResult(moveDocDetail, result);
			} catch (BusinessException be) {
				logger.error("auto allocate error", be);
				throw new BusinessException(be.getLocalizedMessage());
			}
		}
	}
	/**
	 * 补货单自动分配规则
	 * @param moveDoc
	 */
	protected void subscriberReplenishmentModelAllocate(WmsMoveDoc moveDoc) {
		String hql = "SELECT d.id FROM WmsMoveDocDetail d where d.moveDoc.id = :moveDocId and (d.planQuantityBU - d.allocatedQuantityBU) > 0";
		List<Long> moveDocDetailIdList = commonDao.findByQuery(hql, "moveDocId", moveDoc.getId());
		if (moveDocDetailIdList.isEmpty()) {
			throw new BusinessException("wmsmovedoc.detail.not.exist");
		}
		
		WmsOrganization company = commonDao.load(WmsOrganization.class, moveDoc.getCompany().getId());
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class, moveDoc.getWarehouse().getId());				
		String companyName = company.getName();
		if(company.isBeVirtual()){
			companyName = warehouse.getName();
		}
		String warehouseName = warehouse.getName();
		Long warehouseId = warehouse.getId();
		
		for (Long moveDocDetailId : moveDocDetailIdList) {
			try {
				WmsMoveDocDetail moveDocDetail = commonDao.load(WmsMoveDocDetail.class, moveDocDetailId);
				if (moveDocDetail.getUnAllocateQuantityBU() <= 0) {
					return;
				}
				moveDocDetail.setMoveDoc(moveDoc);
				WmsItem item = commonDao.load(WmsItem.class, moveDocDetail.getItem().getId());
				
				Map<String, Object> problem = new HashMap<String, Object>();
				problem.put("仓库序号", warehouseId);
				problem.put("货品序号", moveDocDetail.getItem().getId());
				problem.put("货品代码", item.getCode());
				problem.put("货品状态", moveDocDetail.getInventoryStatus());
				problem.put("待拣选数量", moveDocDetail.getUnAllocateQuantityBU());
				problem.put("补货区", moveDocDetail.getReplenishmentArea());
				problem.put("拣货区", moveDocDetail.getPickArea());
				
				Map<String, Object> result = wmsRuleManager.execute(warehouseName, companyName, "补货分配规则", problem);
				
				wmsMoveDocManager.doReplenishmentAutoAllocateResult(moveDocDetail, result);
			} catch (BusinessException be) {
				logger.error("auto allocate error", be);
			}
		}
		
		workflowManager.doWorkflow(moveDoc, "wmsMoveDocProcess.allocate"); // 调用分配流程, 判断是否整单分配
	}
	
	/**
	 * 移位单取消分配
	 * 
	 * @param entity
	 */
	public void subscriberCancelAllocateWmsMoveDoc(Object entity) {
		WmsMoveDoc moveDoc = (WmsMoveDoc)entity;
		if(moveDoc.isPutawayType() || moveDoc.isMoveType()){
			wmsTransactionalManager.unallocateMoveDoc(moveDoc);
		} else if (moveDoc.isReplenishmentType()) {
//			wmsTransactionalManager.unallocateMoveDocForReplenishment(moveDoc);
			wmsMoveDocManager.cancelAllocate(moveDoc);
		} else{
			wmsMoveDocManager.cancelAllocate(moveDoc);
		}
	}
	
	public void subScriberWmsLocationForArea(Object object) {
		WmsWarehouse wmsWarehouse = (WmsWarehouse) object;
		
		List locs = commonDao.findByQuery("FROM WmsLocation loc WHERE loc.warehouse.id = :warehouseId AND loc.type = :type", 
				new String[] {"warehouseId", "type"}, new Object[] {wmsWarehouse.getId(), WmsLocationType.COUNT});
		if (locs == null || locs.size() == 0) {
			WmsWarehouseArea wwa = null;
			if (wmsWarehouse.getWarehouseAreas() == null || wmsWarehouse.getWarehouseAreas().size() == 0) {
				//为仓库创建默认库区
				wwa = EntityFactory.getEntity(WmsWarehouseArea.class);
				wwa.setWarehouse(wmsWarehouse);
				wwa.setCode("DEFAULT");
				wwa.setName("默认库区");
				workflowManager.doWorkflow(wwa, "warehouseAreaProcess.new");
				commonDao.store(wwa);
			} else {
				//如果有就随便选 一个库区
				Iterator iterator = wmsWarehouse.getWarehouseAreas().iterator();
				wwa = (WmsWarehouseArea) iterator.next();
			}
			
			//为库区创建虚拟库位
			WmsLocation location = EntityFactory.getEntity(WmsLocation.class);
			location.setWarehouseArea(wwa);
			location.setWarehouse(wmsWarehouse);
			location.setCode(WmsLocationType.COUNT);
			location.setType(WmsLocationType.COUNT);
			location.setVerifyCode("-");
			workflowManager.doWorkflow(location, "wmsLocationBaseProcess.new");
			commonDao.store(location);
			
			WmsLocation processLocation = EntityFactory.getEntity(WmsLocation.class);
			processLocation.setWarehouseArea(wwa);
			processLocation.setWarehouse(wmsWarehouse);
			processLocation.setCode(WmsLocationType.PROCESS);
			processLocation.setType(WmsLocationType.PROCESS);
			processLocation.setVerifyCode("-");
			workflowManager.doWorkflow(processLocation, "wmsLocationBaseProcess.new");
			commonDao.store(processLocation);
		}
	}
	
	public void subscriberCreatePackageUnit(Object object) {
		WmsItem item = (WmsItem) object;
		if(item.getLotRule() == null){
			List<WmsLotRule> lots = commonDao.findByQuery("from WmsLotRule where code='默认批次规则'");
			if(lots.size() <= 0){
				throw new BusinessException("系统没有维护默认批次规则");
			}
			item.setLotRule(lots.get(0));
		}
		//判断商品是否已有最小包装，如没有则自动创建，如有则修改最小包装名称为当前包装名称
		WmsPackageUnit packageUnit = EntityFactory.getEntity(WmsPackageUnit.class);
		//设置数字1为默认的拆箱级别
		packageUnit.setLineNo(1);
		packageUnit.setConvertFigure(1);
		packageUnit.setUnit(item.getBaseUnit());
		packageUnit.setLevel(BaseStatus.DEFAULT_PACKAGE_LEVEL);
		
		item.addPackageUnit(packageUnit);
		
		commonDao.store(item);
	}
	
	public void subscriberCreateItemStateByOrganization(Object object){
		WmsOrganization organization = (WmsOrganization) object;
		
		if(organization.isBeCompany() == false || organization.isBeVirtual()){
			return;
		}
		
		List<String> names = new ArrayList<String>();
		names.add("-");
		names.add("质检");
		names.add("锁定");
		names.add("质损");
		names.add("包装破损");
		names.add("待检");
		names.add("质检未通过");
		names.add("报废");
		for(String name : names){
			WmsItemState itemState = new WmsItemState();
			boolean beReceive = false;
			if("质损".equals(name) || "包装破损".equals(name) || "-".equals(name)){
				beReceive = true;
			}
			itemState.setCompany(organization);
			itemState.setName(name);
			itemState.setBeReceive(beReceive);
			workflowManager.doWorkflow(itemState, "wmsItemStateProcess.new");
			commonDao.store(itemState);
		}
	}
	
	/**
	 * 将相同货品批次和数量的托盘放在一起进行上架分配，以提高效率
	 * @param moveDoc
	 * @return
	 */
	private List<WmsTempMoveDocDetail> constructTempDetails(WmsMoveDoc moveDoc){
		String hql = "FROM WmsMoveDocDetail detail WHERE detail.moveDoc.id = :moveDocId ORDER BY detail.planQuantityBU DESC";						
		
		List<WmsMoveDocDetail> list = commonDao.findByQuery(hql, 
				new String[]{"moveDocId"}, new Object[]{moveDoc.getId()});		
		
		String tempMoveDocDetailHql = "SELECT detail.itemKey.id, detail.packageUnit.id, " +
				" detail.fromLocationId, detail.bePallet, detail.inventoryStatus, detail.planQuantity, " +
				" SUM(detail.planQuantity) " +
				" FROM WmsMoveDocDetail detail " +
				" WHERE detail.moveDoc.id = :moveDocId " +
				" AND (detail.planQuantityBU - detail.allocatedQuantityBU) > 0 " +
				" GROUP BY detail.itemKey.id,detail.packageUnit.id,detail.fromLocationId,detail.bePallet,detail.inventoryStatus,detail.planQuantity" +
				" ORDER BY detail.planQuantity DESC";

		List<Object[]> objectLists = commonDao.findByQuery(tempMoveDocDetailHql, 
				new String[]{"moveDocId"}, new Object[]{moveDoc.getId()});
		
		List<WmsTempMoveDocDetail> tempMoveDocDetails = new ArrayList<WmsTempMoveDocDetail>();
		
		for (Object[] objects : objectLists) {
			WmsItemKey itemKey = commonDao.load(WmsItemKey.class, (Long)objects[0]);
			WmsPackageUnit packageUnit = commonDao.load(WmsPackageUnit.class, (Long)objects[1]);
			Long fromLocationId = (Long)objects[2];
			String locationType = ((Boolean)objects[3]) ? BaseStatus.PALLET : BaseStatus.BULK;
			String inventoryStatus = (String)objects[4];
			Double palletQuantity = (Double)objects[5];
			Double planQuantity = (Double)objects[6];
			WmsTempMoveDocDetail tempMoveDocDetail = new WmsTempMoveDocDetail(moveDoc, itemKey,
					packageUnit, planQuantity, palletQuantity, fromLocationId, locationType, inventoryStatus);
			
			for (WmsMoveDocDetail detail : list) {
				if (detail.getUnAllocateQuantityBU() <= 0) {
					continue;
				}
				WmsItemKey itemKey1 = commonDao.load(WmsItemKey.class, detail.getItemKey().getId());
				WmsPackageUnit packageUnit1 = commonDao.load(WmsPackageUnit.class, detail.getPackageUnit().getId());
				WmsItem item  = commonDao.load(WmsItem.class, packageUnit1.getItem().getId());
				String locationType1 = "";
				if (WmsStringUtils.isEmpty(detail.getPallet())) {
					locationType1 = BaseStatus.BULK;
				} else {
					locationType1 = BaseStatus.PALLET;
				}
				
				if(itemKey.getId().equals(itemKey1.getId())
						&& packageUnit.getId().equals(packageUnit1.getId())
						&& fromLocationId.equals(detail.getFromLocationId())
						&& locationType.equals(locationType1)
						&& inventoryStatus.equals(detail.getInventoryStatus())
						&& DoubleUtils.compareByPrecision(detail.getPlanQuantity(), palletQuantity, item.getPrecision()) == 0){
					tempMoveDocDetail.getDetails().add(detail);
				}
			}
			
			tempMoveDocDetails.add(tempMoveDocDetail);
		}
		
		return tempMoveDocDetails;
	}
	
	public void subScriberWmsLocationForDock(Object object) {
		WmsDock dock = (WmsDock) object;

		if (dock.getBeReceive()) {
			if (dock.getReceiveLocationId() != null) {
				WmsLocation revLoc = commonDao.load(WmsLocation.class, dock.getReceiveLocationId());
				if (revLoc == null) {
					createLocationByDock(dock, "IN");
				}
			} else if (dock.getReceiveLocationId() == null) {	
				createLocationByDock(dock, "IN");
			}
		} else {
			commonDao.executeByHql("DELETE FROM WmsLocation loc WHERE loc.id = :locationId", 
					new String[] {"locationId"}, new Object[] {dock.getReceiveLocationId()});
			dock.setReceiveLocationId(null);
		}
		 
	}
	
	private void createLocationByDock(WmsDock dock, String type) {
		WmsLocation location = EntityFactory.getEntity(WmsLocation.class);

		location.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
		location.setWarehouseArea(commonDao.load(WmsWarehouseArea.class, dock.getWarehouseArea().getId()));
		if ("IN".equals(type)) {
			location.setCode(dock.getCode() + "-收货");
			location.setType(WmsLocationType.RECEIVE);
		} else {
			location.setCode(dock.getCode() + "-发货");
			location.setType(WmsLocationType.SHIP);
		}
		location.setVerifyCode("-");
		
		workflowManager.doWorkflow(location, "wmsLocationBaseProcess.new");
		
		commonDao.store(location);

		if ("IN".equals(type)) {
			dock.setReceiveLocationId(location.getId());
		} 
	}
	
	/**
	 *  激活移位单步骤
	 *  1.拆分移位单
	 *  2.对拆分完毕的移位单，调用规则生成作业单
	 * */
	public void subscriberCreateWorkDocByWmsMoveDoc(Object object) {
		WmsMoveDoc moveDoc = load(WmsMoveDoc.class,((WmsMoveDoc) object).getId());
		String type = MyUtils.getMoveType(moveDoc.getType());
		wmsWorkDocManager.splitMoveDoc(moveDoc, type);
		
		wmsWorkDocManager.createWorkDocByRule(moveDoc);
	}
	
	public void subscriberCancelWorkDocByWmsMoveDoc(Object object) {
		WmsMoveDoc moveDoc = (WmsMoveDoc)object;
		List<WmsTask> tasks = commonDao.findByQuery("FROM WmsTask task WHERE task.moveDocDetail.moveDoc.id = :moveDocId AND task.status != :status", new String[]{"moveDocId","status"}, new Object[]{moveDoc.getId(),WmsTaskStatus.FINISHED});
		Set<Long> workDocIds = new HashSet<Long>();
		WmsWorkDoc workDoc = null;
		for (WmsTask task : tasks) {
			if (WmsMoveDocType.MV_PICKTICKET_PICKING.equals(moveDoc.getType()) || WmsMoveDocType.MV_WAVE_PICKING.equals(moveDoc.getType())
					|| WmsMoveDocType.MV_PROCESS_PICKING.equals(moveDoc.getType())) {
				//上架库存取消分配
				WmsInventory inv = commonDao.load(WmsInventory.class, task.getDescInventoryId());
				inv.unallocatePutaway(task.getUnmovedQuantityBU());
				
				wmsInventoryManager.refreshLocationUseRate(inv.getLocation(), 0);
			}
			
			workDoc = task.getWorkDoc();
			if(workDoc == null){
				continue;
			}
			workDoc.removeTask(task);
			if(workDoc.getTasks().isEmpty()){
				workDocIds.add(workDoc.getId());
			}
			workflowManager.doWorkflow(task, "taskProcess.undispatch");
		}
		List<Long> workDocIdList = new ArrayList<Long>();
		for (Long workDocId : workDocIds) {
			workDocIdList.add(workDocId);
		}
		if(!workDocIdList.isEmpty()){
			commonDao.executeByHql("DELETE WmsWorkDoc workDoc WHERE workDoc.id IN (:workDocIds)", "workDocIds", workDocIdList);
		}
	}

	public void subscriberCreateUnloadingCharges(Object object) {
		
//		WmsASN asn = (WmsASN) object;
//		Set<WmsReceivedRecord> records = asn.getRecords();
//		List<WmsReceivedRecordTemp> recordTemps = new ArrayList<WmsReceivedRecordTemp>();
//		for(WmsReceivedRecord detail : records){
//			//未过账的不予计费
//			if (!detail.getBeVerified()) {
//				continue;
//			}
//			Double unloadQty = detail.getReceivedQuantity();
//			boolean flag = Boolean.TRUE;
//			if (!recordTemps.isEmpty()) {
//				for (WmsReceivedRecordTemp temp : recordTemps) {
//					  if(temp.getCompanyName().equals(asn.getCompany().getName())
//							  && temp.getItem().getId().equals(detail.getItemKey().getItem().getId())
//							  && temp.getUnit().getId().equals(detail.getPackageUnit().getId())
//							  && (detail.getWorker() == null || detail.getWorker().getName().equals(temp.getWorkerName()))) {
//						  temp.setQuantity(temp.getQuantity()+detail.getReceivedQuantity());
//						  temp.setWeight(temp.getWeight() + detail.getPackageUnit().getWeight() * detail.getReceivedQuantity());
//						  temp.setVolume(temp.getVolume() + detail.getPackageUnit().getVolume() * detail.getReceivedQuantity());
//						  flag = Boolean.FALSE;
//					  }
//				}
//			}
//			if (flag) {
//				WmsReceivedRecordTemp temp = new WmsReceivedRecordTemp();
//				temp.setCompanyName(asn.getCompany().getName());
//				temp.setWorkerName(detail.getWorker() == null ? null : detail.getWorker().getName());
//				temp.setItem(detail.getItemKey().getItem());
//				temp.setUnit(detail.getPackageUnit());
//				temp.setQuantity(unloadQty);
//				temp.setWeight(detail.getPackageUnit().getWeight() * unloadQty);
//				temp.setVolume(detail.getPackageUnit().getVolume() * unloadQty);
//				recordTemps.add(temp);
//			}
//		}
//		for (WmsReceivedRecordTemp temp : recordTemps) {
//			Map problem = new HashMap();
//			String feeType = "卸车费";
//			problem.put("类型", feeType);
//			problem.put("货主", temp.getCompanyName());
//			problem.put("作业人员", temp.getWorkerName());
//			problem.put("件数", temp.getQuantity());
//			problem.put("重量", temp.getWeight());
//			problem.put("体积", temp.getVolume());
//			Map<String, Object> result = wmsRuleManager.execute(asn.getWarehouse().getName(),
//					asn.getCompany().getName(), "计费规则", problem);
//			Double fee = (result.get(feeType)==null ? 0D : Double.parseDouble(result.get(feeType).toString()));
//			if (fee.doubleValue() > 0) {
//				WmsInventoryFee wif = EntityFactory.getEntity(WmsInventoryFee.class);
//				wif.setFeeType(feeType);
//				wif.setFee(Double.parseDouble(result.get(feeType).toString()));
//				wif.setFeeDate(new Date());
//				wif.setWarehouse(asn.getWarehouse());
//				wif.setCompany(asn.getCompany());
//				wif.setItem(temp.getItem());
//				wif.setQuantity(temp.getQuantity());
//				wif.setWeight(temp.getWeight());
//				wif.setVolume(temp.getVolume());
//				wif.setFeeRate(Double.parseDouble(result.get("费率").toString()));
//				this.commonDao.store(wif);
//			}
//		}
	
}

	public void subscriberCreateloadedCharges(Object obj) {
		/*
		WmsPickTicket pickTicket = (WmsPickTicket) obj;
		List<WmsReceivedRecordTemp> recordTemps = new ArrayList<WmsReceivedRecordTemp>();
		List<WmsTaskLog> logs = commonDao.findByQuery("FROM WmsTaskLog log WHERE log.task.pickTicketDetail.pickTicket.id = :pickTicketId",
					new String[] {"pickTicketId"}, new Object[] {pickTicket.getId()});
		for (WmsTaskLog detail : logs) {
				Double loadQty = PackageUtils.convertPackQuantity(detail.getMovedQuantityBU() - detail.getPickBackQuantityBU(), detail.getPackageUnit());
				if (loadQty <=0) {
					continue;
				}
				
				boolean flag = Boolean.TRUE;
				if (!recordTemps.isEmpty()) {
					for (WmsReceivedRecordTemp temp : recordTemps) {
						  if(temp.getCompanyName().equals(pickTicket.getCompany().getName())
								  && temp.getItem().getId().equals(detail.getItemKey().getItem().getId())
								  && temp.getUnit().getId().equals(detail.getPackageUnit().getId())
								  && (detail.getWorker() == null || detail.getWorker().getName().equals(temp.getWorkerName()))) {
							  temp.setQuantity(temp.getQuantity()+loadQty);
							  temp.setWeight(temp.getWeight() + detail.getPackageUnit().getWeight() * loadQty);
							  temp.setVolume(temp.getVolume() + detail.getPackageUnit().getVolume() * loadQty);
							  flag = Boolean.FALSE;
						  }
					}
				}
				if (flag) {
					WmsReceivedRecordTemp temp = new WmsReceivedRecordTemp();
					temp.setCompanyName(pickTicket.getCompany().getName());
					temp.setWorkerName(detail.getWorker()==null ? null : detail.getWorker().getName());
					temp.setItem(detail.getItemKey().getItem());
					temp.setUnit(detail.getPackageUnit());
					temp.setQuantity(loadQty);
					temp.setWeight(detail.getPackageUnit().getWeight() * loadQty);
					temp.setVolume(detail.getPackageUnit().getVolume() * loadQty);
					recordTemps.add(temp);
				}
				
			}
			
			for (WmsReceivedRecordTemp temp : recordTemps) {
				Map problem = new HashMap();
				String feeType = "装车费";
				problem.put("类型", feeType);
				problem.put("货主", pickTicket.getCompany().getName());
				problem.put("作业人员", temp.getWorkerName());
				problem.put("件数", temp.getQuantity());
				problem.put("重量", temp.getWeight());
				problem.put("体积", temp.getVolume());
				Map<String, Object> result = null;
				try {
					result = wmsRuleManager.execute(pickTicket.getWarehouse().getName(),
							pickTicket.getCompany().getName(), "计费规则", problem);
				} catch (Exception e) {
					return;
				}
				Double fee = (result.get(feeType)==null ? 0D : Double.parseDouble(result.get(feeType).toString()));
				if (fee.doubleValue() > 0) {
					WmsInventoryFee wif = EntityFactory.getEntity(WmsInventoryFee.class);
					wif.setFeeType(feeType);
					wif.setFee(Double.parseDouble(result.get(feeType).toString()));
					wif.setFeeDate(new Date());
					wif.setWarehouse(pickTicket.getWarehouse());
					wif.setCompany(pickTicket.getCompany());
					wif.setItem(temp.getItem());
					wif.setQuantity(temp.getQuantity());
					wif.setWeight(temp.getWeight());
					wif.setVolume(temp.getVolume());
					wif.setFeeRate(Double.parseDouble(result.get("费率").toString()));
					this.commonDao.store(wif);
				}
			}
		*/
	}
	
	public void subscriberToMoveDocLog(Object obj) {
		WmsMoveDoc moveDoc = (WmsMoveDoc)obj;
		if(moveDoc.isPickTicketType()) {
			WmsBOLStateLog bolStateLog = EntityFactory.getEntity(WmsBOLStateLog.class);
			bolStateLog.setType("订单下达");
			bolStateLog.setInputTime(new Date());
			bolStateLog.setMoveDoc(moveDoc);
			commonDao.store(bolStateLog);
		}
	}
	
	public void subscriberPickLog(Object obj) {
		WmsMoveDoc moveDoc = (WmsMoveDoc)obj;
		if((moveDoc.isPickTicketType() || moveDoc.isWaveType()) && WmsMoveDocStatus.FINISHED.equals(moveDoc.getStatus())) {
			WmsBOLStateLog bolStateLog = EntityFactory.getEntity(WmsBOLStateLog.class);
			bolStateLog.setType("拣选完成");
			bolStateLog.setInputTime(new Date());
			bolStateLog.setMoveDoc(moveDoc);
			commonDao.store(bolStateLog);
		}
		
	}
	
	public void subscriberShipLog(Object obj){
		WmsMoveDoc moveDoc = (WmsMoveDoc)obj;
		if(WmsMoveDocShipStatus.SHIPPED.equals(moveDoc.getShipStatus())) {
			WmsBOLStateLog bolStateLog = EntityFactory.getEntity(WmsBOLStateLog.class);
			bolStateLog.setType("已发运");
			bolStateLog.setInputTime(new Date());
			bolStateLog.setMoveDoc(moveDoc);
			commonDao.store(bolStateLog);
		}
	}

	public void subscriberShip(Object obj) {
		WmsMoveDoc moveDoc = (WmsMoveDoc)obj;
		//波次拣货不回写数量，回写数量的处理在原始移位单中一并处理
		if(moveDoc.getType().equals(WmsMoveDocType.MV_WAVE_PICKING)) {
			return;
		}
		for(WmsMoveDocDetail detail : moveDoc.getDetails()) {

			if(moveDoc.getBeWave()) {
				//原始移位单如果是做的波次拣货，调用波次拣货发运
				waveShip(detail.getId());
			} else if(moveDoc.isBeCrossDock()) {
				shipByCrossDock(detail.getId());
			} else {
				//原始移位单如果是普通拣货，调用一般的拣货发运
				WmsPickTicketDetail pickTickDetail = commonDao.load(WmsPickTicketDetail.class, detail.getRelatedId());
				if(pickTickDetail != null && pickTickDetail.getPickTicket().getStatus().equals(WmsPickTicketStatus.FINISHED)) {
					return;
				}
				ship(detail.getId());
			}
		}
	}
	
	private void ship(Long moveDocDetailId) {
		WmsMoveDocDetail moveDocDetail = commonDao.load(WmsMoveDocDetail.class, moveDocDetailId);
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, moveDocDetail.getMoveDoc().getId());
		if(!moveDoc.getStatus().equals(WmsMoveDocStatus.FINISHED)) {
			throw new OriginalBusinessException("BOL状态不正确！");
		}
		String hql = "FROM WmsTaskLog log WHERE log.task.planQuantityBU > 0 AND log.task.movedQuantityBU > 0 AND log.task.moveDocDetail.id=:moveDocDetailId AND log.bePickBack = false";
		List<WmsTaskLog> logs = commonDao.findByQuery(hql, 
			new String[] {"moveDocDetailId"}, new Object[] {moveDocDetailId});
		double planShipQuantity = moveDocDetail.getMovedQuantityBU() - moveDocDetail.getShippedQuantityBU();
		for(WmsTaskLog log : logs) {
			if(planShipQuantity <= 0) {
				break;
			}
			double shipQty = planShipQuantity >= log.getMovedQuantityBU() ? log.getMovedQuantityBU() : planShipQuantity;
			//从原始移位单拣货数量上扣除已经移位的数量
			planShipQuantity -= shipQty;
				
				wmsInventoryManager.ship(log, shipQty);
				removeInventoryExtendQtyTaskLog(log,shipQty);
//				log.ship(shipQty);
				log.getTask().getMoveDocDetail().ship(shipQty);
				WmsPickTicketDetail pickTickDetail = commonDao.load(WmsPickTicketDetail.class, log.getTask().getMoveDocDetail().getRelatedId());
				pickTickDetail.ship(shipQty);
				workflowManager.doWorkflow(pickTickDetail.getPickTicket(), "wmsPickTicketBaseProcess.ship");
		}
	}
	
	private void waveShip(Long moveDocDetailId) {
		WmsMoveDocDetail originMoveDocDetail = commonDao.load(WmsMoveDocDetail.class, moveDocDetailId);
		String shipLotInfo = getLotInfoString(originMoveDocDetail);
		//通过原始移位(拣货)单ID查找对应的波次单明细
		String hql = "FROM WmsWaveDocDetail waveDocDetail WHERE waveDocDetail.moveDocDetail.id=:moveDocDetailId";
		List<WmsWaveDocDetail> waveDocDetails = commonDao.findByQuery(hql, 
			new String[] {"moveDocDetailId"}, new Object[] {moveDocDetailId});
		hql = null;
		//通过原始移位单的拣货移位数量控制发运数量
		//原因：波次批拣时无法直接找到对应的TaskLog，只能通过找出波次批拣对应的TaskLog，循环扣取发运数量
		//注：循环扣取发运数量时，目前版本不对TaskLog的数量进行控制
		double planShipQuantity = originMoveDocDetail.getMovedQuantityBU();
		for(WmsWaveDocDetail waveDocDetail : waveDocDetails) {
			//根据波次单明细的关联ID查找对应的波次拣货移位明细
//			hql = "FROM WmsMoveDocDetail moveDocDetail WHERE moveDocDetail.id=:moveDocDetailId";
//			List<WmsMoveDocDetail> moveDocDetails = commonDao.findByQuery(hql, 
//				new String[] {"moveDocDetailId"}, new Object[] {waveDocDetail.getRelatedId()});
			//发运确认
			for(WmsMoveDocDetail moveDocDetail : waveDocDetail.getWaveMoveDocDetails()) {
				if(moveDocDetail.getMovedQuantityBU() < moveDocDetail.getPlanQuantityBU()){
					continue;
				}
				WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, moveDocDetail.getMoveDoc().getId());
				if(!moveDoc.getStatus().equals(WmsMoveDocStatus.FINISHED)) {
					throw new OriginalBusinessException("BOL状态不正确！");
				}
				hql = "FROM WmsTaskLog log WHERE log.task.planQuantityBU > 0 AND log.task.movedQuantityBU > 0 AND  log.task.moveDocDetail.id=:moveDocDetailId AND log.itemKey.item.id=:itemId AND log.bePickBack = false";
				List<WmsTaskLog> logs = commonDao.findByQuery(hql, 
					new String[] {"moveDocDetailId", "itemId"}, new Object[] {moveDocDetail.getId(), originMoveDocDetail.getItem().getId()});
				for(WmsTaskLog log : logs) {
					WmsItemKey itemKey = commonDao.load(WmsItemKey.class, log.getItemKey().getId());
					if(shipLotInfo != null && !"".equals(shipLotInfo) 
						&& !shipLotInfo.equals(getLotInfoString(itemKey))) {
						continue;
					}
					//当原始移位单拣货数量小于等于0，终止循环
					if(planShipQuantity <= 0) {
						break;
					}
					WmsTask task = commonDao.load(WmsTask.class, log.getTask().getId());
					WmsMoveDocDetail waveMoveDocDetail = commonDao.load(WmsMoveDocDetail.class, task.getMoveDocDetail().getId());
					WmsMoveDoc waveMoveDoc = commonDao.load(WmsMoveDoc.class, waveMoveDocDetail.getMoveDoc().getId());
					//比较原始移位单拣货数量与TaskLog的实际移位数量哪个较大，取较小值作为库存扣减数量
					double shipQty = planShipQuantity >= log.getMovedQuantityBU() ? log.getMovedQuantityBU() : planShipQuantity;
					//从原始移位单拣货数量上扣除已经移位的数量
					planShipQuantity -= shipQty;
					//发运确认扣减库存
					wmsInventoryManager.ship(log, shipQty);
					//清除相应的库存子表信息
					removeInventoryExtendQtyTaskLog(log, shipQty);
					//波次移位(拣货)单发运确认
					waveMoveDocDetail.ship(shipQty);
					commonDao.store(waveMoveDocDetail);
					//因为页面只对原始移位单做发运流程控制，因此需要手工调用移位单发运确认流程改写波次移位(拣货)单状态
					if(WmsMoveDocShipStatus.UNSHIP.equals(waveMoveDoc.getShipStatus())) {
						workflowManager.doWorkflow(waveMoveDoc, "wmsMoveDocShipProcess.ship");
					}
					//原始移位单发运确认,改写发运数量
					originMoveDocDetail.ship(shipQty);
					commonDao.store(originMoveDocDetail);
					//对应发货单回写
					WmsPickTicketDetail pickTickDetail = commonDao.load(WmsPickTicketDetail.class, originMoveDocDetail.getRelatedId());
					pickTickDetail.ship(shipQty);
					commonDao.store(pickTickDetail);
					workflowManager.doWorkflow(pickTickDetail.getPickTicket(), "wmsPickTicketBaseProcess.ship");
				}
			}
		}
		if(planShipQuantity > 0) {
			throw new OriginalBusinessException("未找到对应的拣货记录！");
		}
	}
	
	private void shipByCrossDock(Long moveDocDetailId) {
		WmsMoveDocDetail moveDocDetail = commonDao.load(WmsMoveDocDetail.class, moveDocDetailId);
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, moveDocDetail.getMoveDoc().getId());
		WmsPickTicket wpt = commonDao.load(WmsPickTicket.class, moveDoc.getPickTicket().getId());
		if(!moveDoc.getStatus().equals(WmsMoveDocStatus.FINISHED)) {
			throw new OriginalBusinessException("BOL状态不正确！");
		}
		Set<WmsInventory> invs = getInventoriesByCodition(moveDocDetailId);
		double shipQty = moveDocDetail.getMovedQuantityBU();
		for(WmsInventory inv : invs) {
			if(shipQty <= 0) {
				break;
			}
			double quantity = DoubleUtils.compareByPrecision(inv.getQuantityBU(),
				shipQty, moveDocDetail.getPackageUnit().getPrecision()) > 0 ? shipQty : inv.getQuantityBU();
			inv.removeQuantityBU(quantity);

			//写库存日志
			WmsInventoryLog log = new WmsInventoryLog(WmsInventoryLogType.SHIPPING, -1, 
				wpt.getCode(), wpt.getBillType(), inv.getLocation(), wpt.getCompany(),
				inv.getItemKey(), quantity, moveDocDetail.getPackageUnit(), inv.getStatus(), "发货确认");
			commonDao.store(log);
			
			removeInventoryExtendQtyByInventory(inv, quantity);
			
			moveDocDetail.ship(shipQty);
			WmsPickTicketDetail pickTickDetail = commonDao.load(WmsPickTicketDetail.class, moveDocDetail.getRelatedId());
			pickTickDetail.ship(shipQty);
			workflowManager.doWorkflow(pickTickDetail.getPickTicket(), "wmsPickTicketBaseProcess.ship");
			
			shipQty -= quantity;
		}
	}
	
	private Set<WmsInventory> getInventoriesByCodition(Long moveDocDetailId) {
		WmsMoveDocDetail moveDocDetail = commonDao.load(WmsMoveDocDetail.class, moveDocDetailId);
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, moveDocDetail.getMoveDoc().getId());
		Set<WmsInventory> inventories = new HashSet<WmsInventory>();
		StringBuffer hqlBuffer = new StringBuffer();
		hqlBuffer.append("FROM WmsInventory inv WHERE inv.location.id=:locationId")
			.append(" AND inv.itemKey.item.id=:itemId")
			.append(" AND inv.packageUnit.id =:packageUnitId AND inv.quantityBU > 0 ");
		if(moveDocDetail.getInventoryStatus() != null && !"".equals(moveDocDetail.getInventoryStatus())) {
			hqlBuffer.append(" AND inv.status ='" + moveDocDetail.getInventoryStatus() + "'");
		}
		List<WmsInventory> invs = commonDao.findByQuery(hqlBuffer.toString(), 
			new String[] {"locationId", "itemId", "packageUnitId"}, 
			new Object[] {moveDoc.getShipLocation().getId(), moveDocDetail.getItem().getId(), 
				moveDocDetail.getPackageUnit().getId()});
		if(invs.size()==0 ) {
			hqlBuffer = new StringBuffer();
			hqlBuffer.append("FROM WmsInventory inv WHERE inv.location.id=:locationId")
			.append(" AND inv.itemKey.item.id=:itemId AND inv.quantityBU > 0 ");
			if(moveDocDetail.getInventoryStatus() != null && !"".equals(moveDocDetail.getInventoryStatus())) {
				hqlBuffer.append(" AND inv.status ='" + moveDocDetail.getInventoryStatus() + "'");
			}
			invs = commonDao.findByQuery(hqlBuffer.toString(), 
					new String[] {"locationId", "itemId"}, 
					new Object[] {moveDoc.getShipLocation().getId(), moveDocDetail.getItem().getId()});
		}
		
		if(invs.size() == 0) {
			throw new OriginalBusinessException("未找到发货库存！");
		}
		
		for(WmsInventory inv : invs) {
			WmsItemKey itemKey = commonDao.load(WmsItemKey.class, inv.getItemKey().getId());
			if(!"".equals(getLotInfoString(moveDocDetail)) 
				&& !getLotInfoString(moveDocDetail).equals(getLotInfoString(itemKey))) {
				continue;
			}
			inventories.add(inv);
		}
		return inventories;
	}
	
	private String getLotInfoString(WmsMoveDocDetail moveDocDetail) {
		String lotInfo = "";
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getSoi() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getSoi())) {
			lotInfo += moveDocDetail.getShipLotInfo().getSoi();
		} 
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getSupplier() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getSupplier())) {
			lotInfo += moveDocDetail.getShipLotInfo().getSupplier();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC1() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC1())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC1();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC2() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC2())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC2();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC3() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC3())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC3();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC4() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC4())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC4();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC5() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC5())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC5();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC6() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC6())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC6();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC7() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC7())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC7();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC8() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC8())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC8();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC9() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC9())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC9();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC10() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC10())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC10();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC11() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC11())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC11();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC12() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC12())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC12();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC13() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC13())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC13();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC14() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC14())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC14();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC15() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC15())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC15();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC16() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC16())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC16();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC17() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC17())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC17();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC18() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC18())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC18();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC19() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC19())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC19();
		}
		if(moveDocDetail.getShipLotInfo() != null 
			&& moveDocDetail.getShipLotInfo().getExtendPropC20() != null 
			&& !"".equals(moveDocDetail.getShipLotInfo().getExtendPropC20())) {
			lotInfo += moveDocDetail.getShipLotInfo().getExtendPropC20();
		}
		return lotInfo;
	}
	
	private String getLotInfoString(WmsItemKey itemKey) {
		String lotInfo = "";
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getSoi() != null 
			&& !"".equals(itemKey.getLotInfo().getSoi())) {
			lotInfo += itemKey.getLotInfo().getSoi();
		} 
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getSupplier() != null) {
			lotInfo += itemKey.getLotInfo().getSupplier().getName();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC1() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC1())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC1();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC2() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC2())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC2();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC3() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC3())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC3();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC4() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC4())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC4();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC5() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC5())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC5();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC6() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC6())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC6();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC7() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC7())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC7();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC8() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC8())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC8();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC9() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC9())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC9();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC10() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC10())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC10();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC11() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC11())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC11();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC12() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC12())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC12();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC13() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC13())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC13();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC14() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC14())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC14();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC15() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC15())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC15();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC16() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC16())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC16();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC17() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC17())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC17();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC18() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC18())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC18();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC19() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC19())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC19();
		}
		if(itemKey.getLotInfo() != null 
			&& itemKey.getLotInfo().getExtendPropC20() != null 
			&& !"".equals(itemKey.getLotInfo().getExtendPropC20())) {
			lotInfo += itemKey.getLotInfo().getExtendPropC20();
		}
		return lotInfo;
	}

	
	private void removeInventoryExtendQtyTaskLog(WmsTaskLog taskLog, double quantityBU) {
		List<WmsInventoryExtend> inventoryExtends = null;
		if (!WmsStringUtils.isEmpty(taskLog.getFromSerialNo())) {
			//一个taskLog对应一个序列号(托盘+箱号+序列号的，在taskLog层面应拆解为一个序列号一条记录)
			inventoryExtends = commonDao.findByQuery("FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId AND wsn.serialNo = :serialNo", 
				new String[] {"inventoryId", "serialNo"}, new Object[] {taskLog.getDescInventoryId(), taskLog.getFromSerialNo()});
		} else if (!WmsStringUtils.isEmpty(taskLog.getToCarton())) {
			//一个taskLog对应一个箱号
			inventoryExtends = commonDao.findByQuery("FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId AND wsn.carton = :carton", 
				new String[] {"inventoryId", "carton"}, new Object[] {taskLog.getDescInventoryId(), taskLog.getToCarton()});
		} else if (!WmsStringUtils.isEmpty(taskLog.getToPallet())) {
			//一个taskLog对应一个托盘
			inventoryExtends = commonDao.findByQuery("FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId AND wsn.pallet = :pallet", 
				new String[] {"inventoryId", "pallet"}, new Object[] {taskLog.getDescInventoryId(), taskLog.getToPallet()});
		}
		if(inventoryExtends == null || inventoryExtends.size() == 0) {
			//一个taskLog对应一种货品
			inventoryExtends = commonDao.findByQuery("FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId", 
				new String[] {"inventoryId"}, new Object[] {taskLog.getDescInventoryId()});
			quantityBU = removeInventoryExtendQty(inventoryExtends, quantityBU);
		} else {
			quantityBU = removeInventoryExtendQty(inventoryExtends, quantityBU);
			if (quantityBU > 0) {
				inventoryExtends = commonDao.findByQuery("FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId", 
					new String[] {"inventoryId"}, new Object[] {taskLog.getDescInventoryId()});
				quantityBU = removeInventoryExtendQty(inventoryExtends, quantityBU);
			}
		}		
		if (quantityBU > 0) {
			throw new BusinessException("库存数量不足！");
		}
	}
	
	private Double removeInventoryExtendQty(List<WmsInventoryExtend> inventoryExtends, double quantityBU) {
		for (WmsInventoryExtend inventoryExtend : inventoryExtends){
			Double moveQty = 0D;
			if (inventoryExtend.getQuantityBU() >= quantityBU) {
				moveQty = quantityBU;
			} else {
				moveQty = inventoryExtend.getQuantityBU();
			}
			quantityBU -= moveQty;
			
			inventoryExtend.removeQuantity(moveQty);
			if (inventoryExtend.getQuantityBU().doubleValue() <= 0) {
				commonDao.delete(inventoryExtend);
			}
		}
		return quantityBU;
	}
	
	private void removeInventoryExtendQtyByInventory(WmsInventory inventory, double quantityBU) {
		String hql = "FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId";
		List<WmsInventoryExtend> inventoryExtends = commonDao.findByQuery(hql, 
			new String[] {"inventoryId"}, new Object[] {inventory.getId()});
		removeInventoryExtendQty(inventoryExtends, quantityBU);
//		quantityBU = removeInventoryExtendQty(inventoryExtends, quantityBU);		
//		if (quantityBU > 0) {
//			throw new BusinessException("库存数量不足！");
//		}
	}

	public void subscriberMasterBOLShip(Object obj) {
		WmsMasterBOL masterBOL = (WmsMasterBOL) obj;
		List<String> moveDocStatus = new ArrayList<String>();
		moveDocStatus.add(WmsMoveDocStatus.FINISHED);
		List<String> moveDocShipStatus = new ArrayList<String>();
		moveDocShipStatus.add(WmsMoveDocShipStatus.UNSHIP);
		
		String hql = "SELECT COUNT(*) FROM WmsMoveDoc moveDoc WHERE moveDoc.masterBOL.id = :masterBOLId" +
				" AND moveDoc.status != :moveDocStatus";
		Long count = (Long)commonDao.findByQueryUniqueResult(hql, 
			new String[] {"masterBOLId", "moveDocStatus"}, 
			new Object[] {masterBOL.getId(), WmsMoveDocStatus.FINISHED});
		
		if(count != null && count.intValue() > 0) {
			throw new OriginalBusinessException("装车单中存在未拣货完成的BOL！");
		}
		
		hql = "FROM WmsMoveDoc moveDoc WHERE moveDoc.masterBOL.id = :masterBOLId" +
			" AND moveDoc.status in (:moveDocStatus) AND moveDoc.shipStatus in (:moveDocShipStatus)";
		List<WmsMoveDoc> moveDocs = commonDao.findByQuery(hql, 
			new String[] {"masterBOLId", "moveDocStatus", "moveDocShipStatus"}, 
			new Object[] {masterBOL.getId(), moveDocStatus, moveDocShipStatus});
		
		
		for (WmsMoveDoc moveDoc : moveDocs) {
			workflowManager.doWorkflow(moveDoc, "wmsMoveDocShipProcess.ship");
		}
		masterBOL.setShipTime(new Date());
		
		hql = "UPDATE WmsBooking booking SET booking.finishTime = :finishTime, booking.status = :status  " +
				"WHERE booking.masterBOL.id = :masterBOLId";
		commonDao.executeByHql(hql, new String[]{"finishTime","status","masterBOLId"}, new Object[]{new Date(),WmsBookingStatus.FINISH,masterBOL.getId()});
	}
	
	public void subscriberPickTicketApportion(Object object) {
		WmsPickTicket pickTicket = (WmsPickTicket)object;
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class, pickTicket.getWarehouse().getId());
		WmsOrganization company = commonDao.load(WmsOrganization.class, pickTicket.getCompany().getId());
		String province = pickTicket.getShipToContact() == null ? "" : pickTicket.getShipToContact().getProvince();
		String city = pickTicket.getShipToContact() == null ? "" : pickTicket.getShipToContact().getCity();
		Map<String, Object> problem = new HashMap<String, Object>();
		problem.put("省", province);
		problem.put("城市", city);
		
		Map<String, Object> resultMap = wmsRuleManager.execute(warehouse.getName(), company.getName(), "发货信息指派规则", problem);
		
		Map<String, Object> result = (Map<String, Object>)resultMap.get("返回结果");
		
		String carrierName = (String)result.get("承运商");
		String hql = "FROM WmsOrganization carrier WHERE carrier.name = :name" +
				" AND carrier.beCarrier=true AND carrier.beVirtual=false AND carrier.status=:status";
		WmsOrganization carrier = (WmsOrganization)commonDao.findByQueryUniqueResult(hql, 
				new String[] {"name", "status"}, new Object[] {carrierName.toString(), BaseStatus.ENABLED});;
		pickTicket.setCarrier(carrier);
		
		String dockCode = result.get("发货月台") != null ? result.get("发货月台").toString() : "";
		hql = "FROM WmsDock dock WHERE dock.code = :code AND dock.status = :status AND dock.warehouseArea.warehouse.id = :warehouseId";
		WmsDock dock = (WmsDock)commonDao.findByQueryUniqueResult(hql, 
			new String[] {"code", "status", "warehouseId"}, new Object[] {dockCode.toString(), BaseStatus.ENABLED, warehouse.getId()});
		pickTicket.setDock(dock);
		commonDao.store(pickTicket);
	}
	
	public void subscriberTaskLogCharges(Long taskLogId){
		WmsTaskLog taskLog = load(WmsTaskLog.class,taskLogId);
		Double moveQty = PackageUtils.convertPackQuantity(
				taskLog.getMovedQuantityBU(), taskLog.getPackageUnit());
		Double weight = moveQty * taskLog.getPackageUnit().getWeight();
		Double volume = moveQty * taskLog.getPackageUnit().getVolume();
		WmsOrganization company = taskLog.getItemKey().getItem().getCompany();
		WmsWarehouse warehouse = commonDao.load(WmsLocation.class,
				taskLog.getFromLocationId()).getWarehouse();

		Map problem = new HashMap();
		String feeType = "";

		if (taskLog.getTask().isMoveType() || taskLog.getTask().isPutawayType()) {
			feeType = "上架费";
		} else if (taskLog.getTask().isPickTicketType() || taskLog.getTask().isWaveType() || taskLog.getTask().isProcessType()) {
			feeType = "拣货费";
		}
		problem.put("类型", feeType);
		problem.put("货主", company.getName());
		problem.put("货品分类", taskLog.getItemKey().getItem().getClass1());
		problem.put("作业人员", taskLog.getWorker() == null ? null : taskLog
				.getWorker().getName());
		problem.put("件数", moveQty);
		problem.put("重量", weight);
		problem.put("体积", volume);
		Map<String, Object> result = null;
		try {
			result = wmsRuleManager.execute(warehouse.getName(),
					company.getName(), "计费规则", problem);
		} catch (Exception e) {
			return;
		}
		Double fee = (result.get("费用") == null ? 0D : Double
				.parseDouble(result.get("费用").toString()));
		if (fee.doubleValue() > 0) {
			WmsInventoryFee wif = EntityFactory
					.getEntity(WmsInventoryFee.class);
			wif.setFeeType(feeType);
			wif.setFee(fee);
			wif.setFeeDate(new Date());
			wif.setWarehouse(warehouse);
			wif.setCompany(company);
			wif.setItem(taskLog.getItemKey().getItem());
			wif.setQuantity(moveQty);
			wif.setWeight(weight);
			wif.setVolume(volume);
			wif.setFeeRate(Double.parseDouble(result.get("费率").toString()));
			wif.setWorker(taskLog.getWorker());
			this.commonDao.store(wif);
		}
	
	}
	
	public void subscriberReceivedRecordCharges(Long rrId){
		WmsReceivedRecord receivedRecord = load(WmsReceivedRecord.class,rrId);
		Double moveQty = PackageUtils.convertPackQuantity(receivedRecord.getMovedQuantity(), receivedRecord.getPackageUnit());
		Double weight = moveQty * receivedRecord.getPackageUnit().getWeight();
		Double volume = moveQty * receivedRecord.getPackageUnit().getVolume();
		WmsOrganization company = receivedRecord.getItemKey().getItem().getCompany();
		WmsWarehouse warehouse = commonDao.load(WmsLocation.class, receivedRecord.getLocationId()).getWarehouse();
		
		Map problem = new HashMap();
		String feeType = "上架费";
		problem.put("类型", feeType);
		problem.put("货主", company.getName());
		problem.put("货品分类", receivedRecord.getItemKey().getItem().getClass1());
		problem.put("作业人员", receivedRecord.getWorker() == null ? null : receivedRecord.getWorker().getName());
		problem.put("件数", moveQty);
		problem.put("重量", weight);
		problem.put("体积", volume);
		Map<String, Object> result = null;
		try {
			result = wmsRuleManager.execute(warehouse.getName(), company.getName(), "计费规则", problem); 
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		Double fee = (result.get("费用") == null ? 0D : Double.parseDouble(result.get("费用").toString()));
		if (fee.doubleValue() > 0) {
			WmsInventoryFee wif = EntityFactory.getEntity(WmsInventoryFee.class);
			wif.setFeeType(feeType);
			wif.setFee(fee);
			wif.setFeeDate(new Date());
			wif.setWarehouse(warehouse);
			wif.setCompany(company);
			wif.setItem(receivedRecord.getItemKey().getItem());
			wif.setQuantity(moveQty);
			wif.setWeight(weight);
			wif.setVolume(volume);
			wif.setFeeRate(Double.parseDouble(result.get("费率").toString()));
			wif.setWorker(receivedRecord.getWorker());
			this.commonDao.store(wif);
		}
	
	}
	
	public void subscriberBoxDetailCharges(Long boxDetailId){

		WmsBoxDetail boxDetail = load(WmsBoxDetail.class,boxDetailId);
		WmsWorker workerGroup = boxDetail.getWorkerGroup();
		
		String hql = "SELECT sum(bd.actualWeight),sum(bd.volume),sum(bd.quantity) FROM WmsBoxDetail bd WHERE bd.boxNo = :boxNo";
		Object[] values = (Object[])commonDao.findByQueryUniqueResult(hql, "boxNo", boxDetail.getBoxNo());
		
		Double quantity = 1D;
		Double weight = 0D;
		Double volume = 0D;
		if(values != null){
			volume = (Double)values[1];
			weight = (Double)values[0];
		}
		
		WmsOrganization company = boxDetail.getMoveDoc().getCompany();
		WmsWarehouse warehouse = boxDetail.getMoveDoc().getWarehouse();
		String companyName = company.getName();
		if(company.isBeVirtual()){
			companyName = warehouse.getName();
		}
		
		hql = "FROM WmsWorker worker WHERE worker.worker.id = :workerGroupId AND worker.status = 'ENABLED'";
		List<WmsWorker> workers = commonDao.findByQuery(hql, "workerGroupId", workerGroup.getId());
		for(WmsWorker worker : workers){
			Map problem = new HashMap();
			String feeType = worker.getStation() + "费";
			problem.put("费用类型", "包装费用");
			problem.put("类型", feeType);
			problem.put("货主", company.getName());
			problem.put("货品分类", boxDetail.getMoveDocDetail().getItem().getClass1());
			problem.put("作业人员", worker.getName());
			problem.put("件数", quantity);
			problem.put("重量", weight);
			problem.put("体积", volume);
			Map<String, Object> result = null;
			try {
				result = wmsRuleManager.execute(warehouse.getName(),companyName , "计费规则", problem); 
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			Double fee = (result.get("费用") == null ? 0D : Double.parseDouble(result.get("费用").toString()));
			if (fee.doubleValue() > 0) {
				WmsInventoryFee wif = EntityFactory.getEntity(WmsInventoryFee.class);
				wif.setFeeType(feeType);
				wif.setFee(fee);
				wif.setFeeDate(new Date());
				wif.setWarehouse(warehouse);
				wif.setCompany(company);
				wif.setQuantity(quantity);
				wif.setWeight(weight);
				wif.setVolume(volume);
				wif.setFeeRate(Double.parseDouble(result.get("费率").toString()));
				wif.setWorker(worker);
				wif.setDescription(boxDetail.getBoxNo());
				this.commonDao.store(wif);
			}
		}
	}
}