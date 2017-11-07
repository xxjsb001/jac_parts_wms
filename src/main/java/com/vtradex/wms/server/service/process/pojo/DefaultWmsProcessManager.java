package com.vtradex.wms.server.service.process.pojo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.ShipLotInfo;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.inventory.WmsInventoryFee;
import com.vtradex.wms.server.model.inventory.WmsInventoryLogType;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.move.WmsBoxDetail;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsMoveDocType;
import com.vtradex.wms.server.model.organization.TypeOfBill;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsLotRule;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.process.WmsProcessPlan;
import com.vtradex.wms.server.model.process.WmsProcessPlanDetail;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
import com.vtradex.wms.server.service.base.WmsBillTypeManager;
import com.vtradex.wms.server.service.base.WmsItemManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryExtendManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.process.WmsProcessManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.rule.WmsTransactionalManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.task.WmsTaskManager;
import com.vtradex.wms.server.service.workDoc.WmsWorkDocManager;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.NewLotInfoParser;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

public class DefaultWmsProcessManager extends DefaultBaseManager implements WmsProcessManager {
	protected WorkflowManager workflowManager;
	protected WmsInventoryManager inventoryManager;
	protected WmsBussinessCodeManager codeManager;
	protected WmsRuleManager wmsRuleManager;
	protected WmsWorkDocManager wmsWorkDocManager;
	protected WmsTransactionalManager wmsTransactionalManager;
	protected WmsTaskManager wmsTaskManager;
	protected WmsItemManager itemManager;
	protected WmsInventoryExtendManager inventoryExtendManager;
	protected WmsBillTypeManager wmsBillTypeManager;
	
	public DefaultWmsProcessManager(WorkflowManager workflowManager, WmsInventoryManager inventoryManager,
			WmsBussinessCodeManager codeManager,WmsRuleManager wmsRuleManager ,WmsWorkDocManager wmsWorkDocManager,
			WmsTransactionalManager wmsTransactionalManager,WmsTaskManager wmsTaskManager,
			WmsItemManager itemManager, WmsInventoryExtendManager inventoryExtendManager,
			WmsBillTypeManager wmsBillTypeManager) {
		this.workflowManager = workflowManager;
		this.inventoryManager = inventoryManager;
		this.codeManager =  codeManager;
		this.wmsRuleManager =  wmsRuleManager;
		this.wmsWorkDocManager =  wmsWorkDocManager;
		this.wmsTransactionalManager =  wmsTransactionalManager;
		this.wmsTaskManager =  wmsTaskManager;
		this.itemManager = itemManager;
		this.inventoryExtendManager = inventoryExtendManager;
		this.wmsBillTypeManager = wmsBillTypeManager;
		
	}
	
	public void storeProcessPlan(WmsProcessPlan processPlan) {	

		processPlan.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());	
		//processPlan.setQuantity(0D);
		if (StringUtils.isEmpty(processPlan.getInventoryStatus())) {
			processPlan.setInventoryStatus(BaseStatus.NULLVALUE);
		}
		workflowManager.doWorkflow(processPlan,"wmsProcessPlanProcess.new");	
	}

	public void addProcessPlanDetail(Long processPlanId, WmsProcessPlanDetail processPlanDetail) {
		WmsProcessPlan processPlan = commonDao.load(WmsProcessPlan.class, processPlanId);
		WmsPackageUnit packageUnit = processPlan.getItem().getPackageByLineNo(1);

		if (StringUtils.isEmpty(processPlanDetail.getInventoryStatus())) {
			processPlanDetail.setInventoryStatus(BaseStatus.NULLVALUE);
		}
		if(processPlanDetail.isNew()) {
			List<WmsProcessPlanDetail> list = commonDao.findByQuery("FROM WmsProcessPlanDetail detail WHERE detail.item.id =:itemId " +
					" AND detail.inventoryStatus =:inventoryStatus " +
					" AND detail.processPlan.id =:processPlanId", 
					new String[]{"itemId","inventoryStatus","processPlanId"}, new Object[]{processPlanDetail.getItem().getId(),
					processPlanDetail.getInventoryStatus(),processPlanId});
			if(list.size()>0) {
				WmsProcessPlanDetail oldDetail = list.get(0);
				oldDetail.setQuantityBU(oldDetail.getQuantityBU()
						+(processPlanDetail.getQuantityBU()));
				commonDao.store(oldDetail);
			}
			else {
				processPlan.addProcessPlanDetail(processPlanDetail);
				if(processPlanDetail.getShipLotInfo()==null) {
					processPlanDetail.setShipLotInfo(new ShipLotInfo());
				}
				commonDao.store(processPlanDetail);
			}
		} else {
			commonDao.store(processPlanDetail);
		}
	}

	public void deleteProcessPlan(WmsProcessPlan processPlan) {
		commonDao.delete(processPlan);		
	}

	public void deleteProcessPlanDetail(WmsProcessPlan processPlan, WmsProcessPlanDetail detail) {
		processPlan.removeProcessPlanDetail(detail);
		commonDao.delete(detail);		
	}

	public void storeProcessDoc(WmsMoveDoc moveDoc) {	
		
		if(moveDoc.isNew()){
			String code = codeManager.generateCodeByRule(WmsWarehouseHolder.getWmsWarehouse(), moveDoc.getCompany().getName(), "加工单", "");
			moveDoc.setCode(code);
		}
		
		WmsItem item = commonDao.load(WmsItem.class, moveDoc.getItem().getId());
		WmsLotRule lotRule = item.getDefaultLotRule();
		if (moveDoc.getLotInfo() != null) {
			moveDoc.getLotInfo().prepare(lotRule, item, moveDoc.getCode());
		}
		if (!lotRule.verify(moveDoc.getLotInfo())) {
			throw new BusinessException("lotInfo.is.not.complete");
		}
		
		if(!moveDoc.isNew()){
			//删除原有加工单的明细
			commonDao.executeByHql("DELETE WmsMoveDocDetail detail WHERE detail.moveDoc.id = :processDocId", 
					new String[]{"processDocId"}, new Object[]{moveDoc.getId()});
		}
		
		
		//构建加工单
		moveDoc.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());	
		moveDoc.setType(WmsMoveDocType.MV_PROCESS_PICKING);
		WmsBillType billType = wmsBillTypeManager.getWmsBillType(
				moveDoc.getCompany(), TypeOfBill.MOVE, moveDoc.getType());
		moveDoc.setBillType(billType);
		moveDoc.setPayment(moveDoc.getPlanQuantityBU() * moveDoc.getProcessPlan().getPrice());
		
		commonDao.store(moveDoc);

		workflowManager.doWorkflow(moveDoc,"wmsMoveDocProcess.new");
		
		//重新新建明细			
		WmsProcessPlan processPlan = commonDao.load(WmsProcessPlan.class, moveDoc.getProcessPlan().getId());		
		List<WmsProcessPlanDetail> processPlanDetails = commonDao.findByQuery("FROM WmsProcessPlanDetail detail WHERE detail.processPlan.id =:processPlanId", 
				new String[]{"processPlanId"}, new Object[]{processPlan.getId()});
		//根据加工单对应的加工方案构建加工单明细
		for(WmsProcessPlanDetail processPlanDetail : processPlanDetails){
			WmsMoveDocDetail moveDocDetail = new WmsMoveDocDetail();
			moveDocDetail.setMoveDoc(moveDoc);
			moveDocDetail.setRelatedId(processPlanDetail.getId());
			moveDocDetail.setItem(processPlanDetail.getItem());
			moveDocDetail.setShipLotInfo(processPlanDetail.getShipLotInfo());
			moveDocDetail.setInventoryStatus(processPlanDetail.getInventoryStatus());
			moveDocDetail.setPackageUnit(processPlanDetail.getItem().getPackageByLineNo(1));
			moveDocDetail.setPlanQuantity(processPlanDetail.getQuantityBU() * moveDoc.getPlanQuantityBU());
			moveDocDetail.setPlanQuantityBU(processPlanDetail.getQuantityBU() * moveDoc.getPlanQuantityBU());
			
			moveDocDetail.setProcessPlanQuantityBU(processPlanDetail.getQuantityBU());
			moveDocDetail.calculateLoad();
			commonDao.store(moveDocDetail);
			workflowManager.doWorkflow(moveDocDetail,"wmsMoveDocProcess.addDetail");
		}
	}
	
	public void deleteProcessDoc(WmsMoveDoc processDoc) {
		commonDao.delete(processDoc);	
	}
		
	@SuppressWarnings("unchecked")
	public void processRecord(WmsMoveDoc processDoc,Double processQuantity){
		
		//processQuantity = processQuantity * processDoc.getProcessPlan().getQuantity();
		
		if(processQuantity.doubleValue()+ processDoc.getProcessQuantityBU() 
				> processDoc.getMovedQuantityBU().doubleValue()){
			throw new BusinessException("processQuantity.mutch.to.pickedQuantity");
		}
		
		processDoc.setProcessQuantityBU(processDoc.getProcessQuantityBU()+ processQuantity);
		commonDao.store(processDoc);
		
		WmsLocation wmsLocation = load(WmsLocation.class,processDoc.getShipLocation().getId());
		WmsItemKey bomItemKey = itemManager.getItemKey(processDoc.getWarehouse(), 
				processDoc.getLotInfo().getSoi(), processDoc.getItem(),
				processDoc.getLotInfo(), Boolean.FALSE);
		
		WmsInventory bomInventory = inventoryManager.getInventoryWithNew(wmsLocation, bomItemKey, processDoc.getProcessPlan().getItem().getPackageByLineNo(1), processDoc.getProcessPlan().getInventoryStatus());
		
		Double bomQuantityBU = processDoc.getProcessPlan().getQuantity() * DoubleUtils.formatByPrecision(processQuantity / bomInventory.getPackageUnit().getConvertFigure(), bomInventory.getPackageUnit().getPrecision());
		bomInventory.addQuantityBU(bomQuantityBU);
		this.commonDao.store(bomInventory);
		
		
		inventoryExtendManager.addInventoryExtend(bomInventory, BaseStatus.NULLVALUE,BaseStatus.NULLVALUE,BaseStatus.NULLVALUE,bomQuantityBU);
		
		inventoryManager.addInventoryLog(WmsInventoryLogType.PROCESS_UP, 1, processDoc.getRelatedBill1(), processDoc.getBillType(), 
				bomInventory.getLocation(), bomInventory.getItemKey(), bomQuantityBU, 
				bomInventory.getPackageUnit(),bomInventory.getStatus(), "加工登记---成品新增");
		
		
		for (WmsMoveDocDetail processDocDetail : processDoc.getDetails()) {
			WmsProcessPlanDetail processPlanDetail = commonDao.load(WmsProcessPlanDetail.class, processDocDetail.getRelatedId());
			
			Double quantity = processQuantity * processPlanDetail.getQuantityBU();
			
			processDocDetail.setProcessQuantityBU(processDocDetail.getProcessQuantityBU() + quantity);
			commonDao.store(processDocDetail);
			
			WmsInventory inventory = getComponentInevntory(processDocDetail,wmsLocation);
			if(inventory == null){
				throw new BusinessException("加工库位中子件库存不存在！");
			}
			inventory.removeQuantityBU(quantity);
			
			String hql = "FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId AND wsn.quantityBU > 0 ORDER BY wsn.id DESC";
			List<WmsInventoryExtend> inventoryExtends = commonDao.findByQuery(hql,
							new String[] { "inventoryId" },
							new Object[] { inventory.getId() });
			double moveQty = quantity;
			for (WmsInventoryExtend wsn : inventoryExtends) {
				Double temp = 0D;
				if (wsn.getQuantityBU() >= moveQty) {
					temp = moveQty;
				} else {
					temp = wsn.getQuantityBU();
				}
				moveQty -= temp;
				wsn.removeQuantity(temp);
				if(moveQty <= 0D){
					break;
				}
			}
			//写日志
			inventoryManager.addInventoryLog(WmsInventoryLogType.PROCESS_DOWN, -1, processDoc.getRelatedBill1(), processDoc.getBillType(), inventory.getLocation(), 
					inventory.getItemKey(), quantity, inventory.getPackageUnit(), inventory.getStatus(), "加工登记--子件减少");
		}
		inventoryManager.refreshLocationUseRate(processDoc.getShipLocation(), 0);
		//workflowManager.doWorkflow(processDoc,"wmsMoveDocProcess.processRecord");
		subscriberProcessDocCharges(processDoc,processQuantity);
	}
	
	
	public void subscriberProcessDocCharges(WmsMoveDoc processDoc,Double processQuantity){
		
		WmsItem item = processDoc.getProcessPlan().getItem();
		WmsPackageUnit wpu = item.getPackageByLineNo(1);
		Double quantity = processQuantity;
		Double weight = processQuantity * wpu.getWeight();
		Double volume =  processQuantity * wpu.getVolume();
		
		WmsOrganization company = processDoc.getCompany();
		WmsWarehouse warehouse = processDoc.getWarehouse();
		String companyName = company.getName();
		if(company.isBeVirtual()){
			companyName = warehouse.getName();
		}
		String hql = "FROM WmsWorker worker WHERE worker.worker.id = :workerGroupId AND worker.status = 'ENABLED'";
		List<WmsWorker> workers = commonDao.findByQuery(hql, "workerGroupId", processDoc.getWorker()==null? null : processDoc.getWorker().getId());
		for(WmsWorker worker : workers){
			Map problem = new HashMap();
			String feeType = worker.getStation() + "费";
			problem.put("费用类型", "包装费用");
			problem.put("类型", feeType);
			problem.put("货主", company.getName());
			problem.put("货品分类", item.getClass1());
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
				wif.setItem(item);
				wif.setCompany(company);
				wif.setQuantity(quantity);
				wif.setWeight(weight);
				wif.setVolume(volume);
				wif.setFeeRate(Double.parseDouble(result.get("费率").toString()));
				wif.setWorker(worker);
				this.commonDao.store(wif);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private WmsInventory getComponentInevntory(WmsMoveDocDetail processDocDetail,WmsLocation processLocatoin) {
		StringBuffer hqlBuffer = new StringBuffer();
		hqlBuffer
				.append("from WmsInventory inv where inv.location.id = :locationId ")
				.append(" and inv.itemKey.item.id = :itemId  ")
				.append(" and inv.packageUnit.id = :packageUnitId  ");
				
	   if(processDocDetail.getShipLotInfo() != null){
		   ShipLotInfo shipLogInfo = processDocDetail.getShipLotInfo();
		   hqlBuffer.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.soi", shipLogInfo.getSoi()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.supplier.name", shipLogInfo.getSupplier()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.storageDate", shipLogInfo.getStorageDate()==null?null:shipLogInfo.getStorageDate().toString()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC1", shipLogInfo.getExtendPropC1()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC2", shipLogInfo.getExtendPropC2()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC3",shipLogInfo.getExtendPropC3()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC4", shipLogInfo.getExtendPropC4()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC5", shipLogInfo.getExtendPropC5()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC6", shipLogInfo.getExtendPropC6()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC7", shipLogInfo.getExtendPropC7()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC8", shipLogInfo.getExtendPropC8()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC9", shipLogInfo.getExtendPropC9()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC10",shipLogInfo.getExtendPropC10()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC11",shipLogInfo.getExtendPropC11()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC12",shipLogInfo.getExtendPropC12()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC13",shipLogInfo.getExtendPropC13()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC14", shipLogInfo.getExtendPropC14()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC15", shipLogInfo.getExtendPropC15()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC16", shipLogInfo.getExtendPropC16()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC17", shipLogInfo.getExtendPropC17()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC18",shipLogInfo.getExtendPropC18()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC19", shipLogInfo.getExtendPropC19()))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfInventoryLot(
					"inv.itemKey.lotInfo.extendPropC20", shipLogInfo.getExtendPropC20()));
	   }
		
	   List<WmsInventory> inventorys = commonDao.findByQuery(hqlBuffer.toString(),
			   new String[]{"locationId","itemId","packageUnitId"}, new Object[]{processLocatoin.getId(),processDocDetail.getItem().getId(),processDocDetail.getPackageUnit().getId()});
			
	   WmsInventory inventory = null;
	   if(!inventorys.isEmpty()){
		   inventory = inventorys.get(0);
	   }
	   return inventory;
	}
}
