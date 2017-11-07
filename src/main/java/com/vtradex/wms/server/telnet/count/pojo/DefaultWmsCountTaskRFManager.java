package com.vtradex.wms.server.telnet.count.pojo;

import java.util.Arrays;
import java.util.List;

import com.vtradex.rule.server.loader.IRuleTableLoader;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.UpdateInfo;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.count.WmsCountDetail;
import com.vtradex.wms.server.model.count.WmsCountDetailStatus;
import com.vtradex.wms.server.model.count.WmsCountPlan;
import com.vtradex.wms.server.model.count.WmsCountRecord;
import com.vtradex.wms.server.model.count.WmsCountStatus;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsWorkArea;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
import com.vtradex.wms.server.service.count.WmsCountPlanManager;
import com.vtradex.wms.server.telnet.count.WmsCountTaskRFManager;
import com.vtradex.wms.server.telnet.dto.WmsCountTaskDTO;
import com.vtradex.wms.server.telnet.manager.pojo.DefaultLimitQueryBaseManager;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;
import com.vtradex.wms.server.web.filter.WmsWorkAreaHolder;
import com.vtradex.wms.server.web.filter.WmsWorkerHolder;

@SuppressWarnings("unchecked")
public class DefaultWmsCountTaskRFManager extends DefaultLimitQueryBaseManager implements WmsCountTaskRFManager {
	
	private final WorkflowManager workflowManager;
	private final WmsCountPlanManager countPlanManager;
	private final IRuleTableLoader ruleTableLoader;
	
	public DefaultWmsCountTaskRFManager(WorkflowManager workflowManager , WmsCountPlanManager countPlanManager , IRuleTableLoader ruleTableLoader) {
		this.workflowManager = workflowManager;
		this.countPlanManager = countPlanManager;
		this.ruleTableLoader = ruleTableLoader;
	}

	public WmsCountTaskDTO queryUnFinishCountTask() {
		String hql ="SELECT new com.vtradex.wms.server.telnet.dto.WmsCountTaskDTO(detail.countPlan.id,detail.countPlan.code,detail.id,detail.locationId,detail.locationCode) " +
				" FROM WmsCountDetail detail,WmsLocation location WHERE location.warehouse.id =:warehouseID " +
				" AND detail.status=:detailStatus " +
				" AND detail.countPlan.status IN (:status)" +
				" AND detail.worker.id =:workerID " +
				" AND detail.locationId =location.id ORDER BY location.routeNo ASC,location.code ASC";
		
		return (WmsCountTaskDTO)this.findByHqlLimitQuery(hql, new String[]{"warehouseID","detailStatus","status","workerID"}
				, new Object[]{WmsWarehouseHolder.getWmsWarehouse().getId() , WmsCountDetailStatus.OPEN,Arrays.asList(new String[]{WmsCountStatus.ACTIVE,WmsCountStatus.COUNT}),WmsWorkerHolder.getWmsWorker().getId() }, 1);
	}


	public WmsCountTaskDTO applyNewCountTaskByCurrentLocationCode(String anyLocationCode) {
		WmsWorker worker = WmsWorkerHolder.getWmsWorker();
		WmsLocation anyLocation = (WmsLocation)commonDao.findByQueryUniqueResult("FROM WmsLocation loc WHERE loc.code=:code AND loc.warehouse.id=:whId"
				, new String[]{"code","whId"}, new Object[]{anyLocationCode,WmsWarehouseHolder.getWmsWarehouse().getId()});
		if(anyLocation == null) {
			return null;
		}
//		WmsWorkArea workArea = commonDao.load(WmsWorkArea.class, WmsWorkAreaHolder.getWmsWorkArea().getId());
//		String[] binds = new String[]{WmsWarehouseHolder.getWmsWarehouse().getName(),
//				Constant.NULL,Constant.NULL,Constant.NULL,Constant.NULL};
//		Object[] objects = new Object[]{"R101_基础数据_工作区定位表",workArea.getWarehouseArea().getCode(),workArea.getCode()};
//		Map<String,String> values = (Map)ruleTableLoader.getRuleTableDetail(new Date(), binds, objects);
//		String areaBetweens = values.get("区区间");
//		String lineBetweens = values.get("排区间");
//		int startZone = Integer.valueOf(StringUtils.substringBetween(areaBetweens, "(", ",").trim());
//		int endZone = Integer.valueOf(StringUtils.substringBetween(areaBetweens, ",", ")").trim());
//		int startLine = Integer.valueOf(StringUtils.substringBetween(lineBetweens, "(", ",").trim());
//		int endLine = Integer.valueOf(StringUtils.substringBetween(lineBetweens, ",", ")").trim());
		int routeNo = anyLocation.getRouteNo();
//		Long areaId = workArea.getWarehouseArea().getId();
		String hql ="SELECT new com.vtradex.wms.server.telnet.dto.WmsCountTaskDTO(detail.countPlan.id,detail.countPlan.code,detail.id,detail.locationId,detail.locationCode) " +
					" FROM WmsCountDetail detail,WmsLocation location " +
					" WHERE location.warehouse.id =:warehouseID " +
					" AND detail.status=:detailStatus " +
					" AND detail.locationId = location.id " +
					" AND location.routeNo >= :routeNo" +
//					" AND location.zone>=:startZone AND location.zone<=:endZone" +
//					" AND location.line>=:startLine AND location.line<=:endLine" +
//					" AND location.warehouseArea.id=:areaId" +
					" AND detail.countPlan.status IN (:statuss)" +
					" ORDER BY location.routeNo ASC,location.code ASC";
		WmsCountTaskDTO applyCountTaskDTO = (WmsCountTaskDTO)this.findByHqlLimitQuery(hql
					, new String[]{"warehouseID","detailStatus","routeNo" , "statuss"}
					, new Object[]{WmsWarehouseHolder.getWmsWarehouse().getId(),WmsCountDetailStatus.OPEN,routeNo,Arrays.asList(new String[]{WmsCountStatus.ACTIVE , WmsCountStatus.COUNT})}, 1);
		if(applyCountTaskDTO == null) {
			hql ="SELECT new com.vtradex.wms.server.telnet.dto.WmsCountTaskDTO(detail.countPlan.id,detail.countPlan.code,detail.id,detail.locationId,detail.locationCode) " +
					" FROM WmsCountDetail detail,WmsLocation location " +
					" WHERE location.warehouse.id =:warehouseID " +
					" AND detail.status=:detailStatus " +
					" AND detail.locationId = location.id " +
					" AND location.routeNo < :routeNo" +
//					" AND location.warehouseArea.id=:areaId" +
					" AND detail.countPlan.status IN (:statuss)" +
					" ORDER BY location.routeNo ASC,location.code ASC";
			 applyCountTaskDTO = (WmsCountTaskDTO)this.findByHqlLimitQuery(hql
						, new String[]{"warehouseID","detailStatus","routeNo", "statuss"}
						, new Object[]{WmsWarehouseHolder.getWmsWarehouse().getId(),WmsCountDetailStatus.OPEN,routeNo,Arrays.asList(new String[]{WmsCountStatus.ACTIVE , WmsCountStatus.COUNT})}, 1);
		}
		
		if(applyCountTaskDTO!=null) {
			WmsCountDetail countDetail = commonDao.load(WmsCountDetail.class, applyCountTaskDTO.getCountPlanDetailId());
			countDetail.setStatus(WmsCountDetailStatus.COUNT);
			countDetail.setWorker(worker);
			commonDao.store(countDetail);
			this.workflowManager.doWorkflow(countDetail.getCountPlan(), "countPlanProcess.count");
		}
		
		return applyCountTaskDTO;
	}


	public void checkItemExists(String code) {
		WmsItem item = queryItemByCode(code);
		if(item == null) {
			throw new BusinessException("货品不存在");
		}
	}
	
	private WmsItem queryItemByCode(String barCode) {
		return (WmsItem)commonDao.findByQueryUniqueResult("FROM WmsItem item WHERE item.barCode=:code OR item.code=:code","code",barCode);
	}

	public void countRecord(Long countDetailId, String itemCode,
			Double quantity) {
		WmsCountDetail countDetail = commonDao.load(WmsCountDetail.class, countDetailId);
		WmsItem item = queryItemByCode(itemCode);
		if(item == null) {
			throw new BusinessException("盘点货品不存在");
		}
		WmsCountRecord countRecord = this.getCountRecords(countDetail.getLocationId(), item.getId(),countDetail.getCountPlan());
		this.countPlanManager.record(countRecord, Arrays.asList(new Double[]{quantity}));
	}
	
	private List<WmsCountRecord> findAllCountRecords(Long countPlanId){
		return commonDao.findByQuery("FROM WmsCountRecord record WHERE record.countPlan.id=:cpId" , "cpId" , countPlanId);
	}
	
	private WmsCountRecord getCountRecords(
			Long locationId, Long itemId , WmsCountPlan countPlan) {
		List<WmsCountRecord> records = findAllCountRecords(countPlan.getId());
		WmsCountRecord record = getCountRecord(records , locationId , itemId);
		if(record != null)
			return record;
		WmsLocation location = commonDao.load(WmsLocation.class, locationId);
		WmsItem item = commonDao.load(WmsItem.class, itemId);
		Double inventoryQtyBU = (Double) commonDao.findByQueryUniqueResult(
				"SELECT SUM(inv.quantityBU) FROM WmsInventory inv "
						+ " WHERE inv.location.id = :locationId AND inv.itemKey.item.id = :itemId",
				new String[] { "locationId", "itemId" },
				new Object[] { locationId, itemId });
		return createCountRecord(location,item,inventoryQtyBU == null ? 0D : inventoryQtyBU,countPlan);
	}
	
	private WmsCountRecord getCountRecord(List<WmsCountRecord> records ,Long locationId , Long itemId){
		for (WmsCountRecord record : records) {
			if (record.getLocationId().equals(locationId)
					&& record.getItem().getId().equals(itemId)) {
				return record;
			}
		}
		return null;
	}
	
	private WmsCountRecord createCountRecord(WmsLocation location , WmsItem item , Double quantity , WmsCountPlan countPlan) {
		List<WmsCountRecord> records = findAllCountRecords(countPlan.getId());
		WmsCountRecord record = getCountRecord(records , location.getId() , item.getId());
		if(record != null){
			record.setQuantityBU(record.getQuantityBU()+quantity);
			record.setDeltaQuantityBU(record.getQuantityBU() - record.getCountQuantityBU());
			return record;
		}
		record = EntityFactory.getEntity(WmsCountRecord.class);
		record.setLocationId(location.getId());
		record.setLocationCode(location.getCode());
		record.setItem(item);
		record.setPackageUnit(item.getPackageByLineNo(1));
		record.setQuantityBU(quantity);
		record.setCountQuantityBU(.0);
		record.setDeltaQuantityBU(record.getQuantityBU() - record.getCountQuantityBU());
		record.setCountPlan(countPlan);
		record.setBeCounted(true);
		record.setUpdateInfo(new UpdateInfo(UserHolder.getUser()));
		commonDao.store(record);
		return record;
	}

	public WmsCountTaskDTO quantityAdjust(Long countPlanId , Long countPlanDetailId , String locationCode) {
		WmsCountPlan countPlan = commonDao.load(WmsCountPlan.class, countPlanId);
		WmsCountDetail countDetail = commonDao.load(WmsCountDetail.class, countPlanDetailId);
		countDetail.setStatus(WmsCountDetailStatus.FINISHED);
		commonDao.store(countDetail);
		
		List<WmsInventory> inventories = commonDao.findByQuery(
				"FROM WmsInventory inv WHERE inv.location.id=:locId AND inv.quantityBU > 0 " +
				"AND NOT EXISTS(SELECT record.id FROM WmsCountRecord record WHERE record.countPlan.id=:cpId " +
				"AND record.item.id=inv.itemKey.item.id AND record.locationId = inv.location.id)" 
				, new String[]{"locId","cpId"} 
				, new Object[]{countDetail.getLocationId() , countPlanId}); 
		for(WmsInventory inventory : inventories) {
			createCountRecord(inventory.getLocation() , inventory.getItemKey().getItem() , inventory.getQuantityBU() , countPlan);
		}
		
		Long count = (Long)commonDao.findByQueryUniqueResult(
				"SELECT count(detail.id) FROM WmsCountDetail detail WHERE detail.countPlan.id=:cpId AND detail.status<>:status"
				, new String[]{"cpId" , "status"}, new Object[]{countPlanId , WmsCountDetailStatus.FINISHED});
		if(count <= 0) {
			//
			List<WmsCountRecord> records = this.commonDao.findByQuery("FROM WmsCountRecord record WHERE record.beCounted = false AND record.countPlan.id=:cpId" , "cpId" , countPlan.getId());
			for(WmsCountRecord record : records) {
				record.setCountQuantityBU(0D);
				record.setDeltaQuantityBU(record.getQuantityBU()-record.getCountQuantityBU());
				record.setBeCounted(true);
				commonDao.store(record);
			}
			//一次性差异调整+关闭盘点计划
			this.countPlanManager.close(countPlan);
			this.workflowManager.doWorkflow(countPlan, "countPlanProcess.close");
		}
		return this.queryUnFinishCountTask();
	}

	public void cancelCountTask(Long countDetailId) {
		WmsCountDetail countDetail = commonDao.load(WmsCountDetail.class, countDetailId);
		Long countRecordCount = (Long)commonDao.findByQueryUniqueResult(
				"SELECT count(record.id) FROM WmsCountRecord record WHERE record.locationId=:locationId AND record.countPlan.id=:countPlanId"
				, new String[]{"locationId","countPlanId"}, new Object[]{countDetail.getLocationId() , countDetail.getCountPlan().getId()});
		if(countRecordCount > 0) {
			return;
		}
		countDetail.setStatus(WmsCountDetailStatus.OPEN);
		countDetail.setWorker(null);
		commonDao.store(countDetail);
		this.workflowManager.doWorkflow(countDetail.getCountPlan(), "countPlanProcess.cancelCount");
	}
}
