package com.vtradex.wms.server.service.count.pojo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.exception.OriginalBusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.UpdateInfo;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.LotInfo;
import com.vtradex.wms.server.model.count.WmsCountDetail;
import com.vtradex.wms.server.model.count.WmsCountDetailStatus;
import com.vtradex.wms.server.model.count.WmsCountLockType;
import com.vtradex.wms.server.model.count.WmsCountPlan;
import com.vtradex.wms.server.model.count.WmsCountPlanType;
import com.vtradex.wms.server.model.count.WmsCountRecord;
import com.vtradex.wms.server.model.count.WmsCountStatus;
import com.vtradex.wms.server.model.count.WmsCountType;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.inventory.WmsInventoryLogType;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
import com.vtradex.wms.server.service.base.WmsItemManager;
import com.vtradex.wms.server.service.count.WmsCountPlanManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryExtendManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.security.WmsWarehouseManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.sequence.WmsCommonDao;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

@SuppressWarnings("unchecked")
public class DefaultWmsCountPlanManager extends DefaultBaseManager implements
		WmsCountPlanManager {

	private WorkflowManager workflowManager;
	private WmsInventoryManager wmsInventoryManager;
	private WmsBussinessCodeManager codeManager;
	private WmsInventoryExtendManager inventoryExtendManager;
	private WmsWarehouseManager warehouseManager;
	private WmsCommonDao wmsCommonDao;
	private WmsItemManager itemManager;
	private WmsRuleManager wmsRuleManager;
	
	public DefaultWmsCountPlanManager(WorkflowManager workflowManager,
			WmsInventoryManager wmsInventoryManager,
			WmsBussinessCodeManager codeManager,
			WmsInventoryExtendManager inventoryExtendManager,
			WmsWarehouseManager warehouseManager, WmsCommonDao wmsCommonDao,
			WmsItemManager itemManager, WmsRuleManager wmsRuleManager) {
		this.workflowManager = workflowManager;
		this.wmsInventoryManager = wmsInventoryManager;
		this.codeManager = codeManager;
		this.inventoryExtendManager = inventoryExtendManager;
		this.warehouseManager = warehouseManager;
		this.wmsCommonDao = wmsCommonDao;
		this.itemManager = itemManager;
		this.wmsRuleManager = wmsRuleManager;
	}

	/**
	 * 创建盘点计划--主入口方法
	 * */
	public void createWmsCountPlan(WmsCountPlan wmsCountPlan, Integer cycleDays) {
		wmsCountPlan.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
		wmsCountPlan
				.setCode(codeManager.generateCodeByRule(wmsCountPlan
						.getWarehouse(), wmsCountPlan.getCompany().getName(),
						"盘点单", ""));
		wmsCountPlan.updateInfo(UserHolder.getUser());
		wmsCountPlan.setPlanType(WmsCountPlanType.NORMAL);
		if (wmsCountPlan.isNew()) {
			wmsCountPlan.setDetails(new HashSet<WmsCountDetail>());
		}
		commonDao.store(wmsCountPlan);

		// 根据盘点类型自动添加对应盘点库位明细
		if (WmsCountType.ALL.equals(wmsCountPlan.getType())) {
			// 全盘
			createWmsCountPlanForAll(wmsCountPlan);
		} else if (WmsCountType.ITEM.equals(wmsCountPlan.getType())) {
			// 货品盘点
			createWmsCountPlanForItem(wmsCountPlan, false);
		} else if (WmsCountType.LOCATION_EXCEPTION.equals(wmsCountPlan.getType())) {
			// 库位异常盘点
			createWmsCountPlanForLocationException(wmsCountPlan);
		} else if (WmsCountType.LOCATION_CYCLE.equals(wmsCountPlan.getType())) {
			// 循环盘点库位
			if (wmsCountPlan.getLocationCount() <= 0) {
				throw new OriginalBusinessException("盘点库位数小于0,创建盘点计划失败！");
			}
			createWmsCountPlanForLocation(wmsCountPlan, cycleDays);
		} else if (WmsCountType.LOCATION_MOVED.equals(wmsCountPlan.getType())) {
			// 动碰盘点库位
			if (wmsCountPlan.getTouchTimes() <= 0) {
				throw new OriginalBusinessException("动碰次数必须大于0！");
			}
			createWmsCountPlanForMovedLocation(wmsCountPlan);
		} else if (WmsCountType.SUPPLY.equals(wmsCountPlan.getType())){
			createWmsCountPlanForSupply(wmsCountPlan);
		}
	}

	/**
	 * 全盘
	 */
	private void createWmsCountPlanForAll(WmsCountPlan wmsCountPlan) {
		List<WmsLocation> wmsLocations = new ArrayList<WmsLocation>();

		String hql = "";
		if (WmsCountLockType.LOCK.equals(wmsCountPlan.getLockType())) {
			hql = "FROM WmsLocation loc WHERE loc.warehouse.id = :warehouseId " +
					" AND loc.type = 'STORAGE' AND loc.lockCount = false " +
					" AND loc.id IN " +
					"	(SELECT DISTINCT w1.location.id FROM WmsInventory w1 WHERE w1.location.warehouse.id = :warehouseId AND w1.quantityBU > 0 " +
					"   AND w1.itemKey.item.company.id = :companyId ) "+
					" AND loc.id NOT IN " +
					"	(SELECT DISTINCT w2.location.id FROM WmsInventory w2 WHERE w2.location.warehouse.id = :warehouseId AND w2.quantityBU > 0 " +
					"   AND (w2.status <> '-' OR w2.putawayQuantityBU <> 0 OR w2.allocatedQuantityBU <> 0)) ";

		} else {
			hql = "FROM WmsLocation loc WHERE loc.warehouse.id = :warehouseId " +
					" AND loc.type = 'STORAGE' " +
					" AND loc.id IN (SELECT DISTINCT w1.location.id FROM WmsInventory w1 WHERE w1.location.warehouse.id = :warehouseId AND w1.status='-' AND w1.quantityBU > 0 " +
					" AND w1.itemKey.item.company.id = :companyId )" +
					" ORDER BY loc.cycleDate DESC";
			
		}
		wmsLocations = commonDao.findByQuery(
				hql,
				new String[] { "warehouseId", "companyId"},
				new Object[] { wmsCountPlan.getWarehouse().getId(),wmsCountPlan.getCompany().getId()});
		
		if (wmsLocations.size() <= 0) {
			return;
		}
		for (WmsLocation loc : wmsLocations) {
			createWmsCountDetail(wmsCountPlan, loc);
		}
	}
	/**
	 * 按供应商盘点
	 * @param wmsCountPlan
	 */
	private void createWmsCountPlanForSupply(WmsCountPlan wmsCountPlan) {
		List<WmsLocation> wmsLocations = new ArrayList<WmsLocation>();

		String hql = "";
		if (WmsCountLockType.LOCK.equals(wmsCountPlan.getLockType())) {
			hql = "FROM WmsLocation loc WHERE loc.warehouse.id = :warehouseId " +
					" AND loc.type = 'STORAGE' AND loc.lockCount = false " +
					" AND loc.id IN " +
					"	(SELECT DISTINCT w1.location.id FROM WmsInventory w1 WHERE w1.location.warehouse.id = :warehouseId AND w1.quantityBU > 0 " +
					" 	 AND w1.itemKey.item.company.id = :companyId AND w1.itemKey.lotInfo.supplier.id = :supplierId)"+ 
					" AND loc.id NOT IN " +
					"	(SELECT DISTINCT w2.location.id FROM WmsInventory w2 WHERE w2.location.warehouse.id = :warehouseId AND w2.quantityBU > 0 " +
					"     	AND (w2.status <> '-' OR w2.putawayQuantityBU <> 0 OR w2.allocatedQuantityBU <> 0)) ";

		} else {
			hql = "FROM WmsLocation loc WHERE loc.warehouse.id = :warehouseId " +
					" AND loc.type = 'STORAGE' " +
					" AND loc.id IN (SELECT DISTINCT w1.location.id FROM WmsInventory w1 WHERE w1.location.warehouse.id = :warehouseId AND w1.quantityBU > 0 " +
					" 	  AND w1.itemKey.lotInfo.supplier.id = :supplierId)"+
					" ORDER BY loc.cycleDate DESC";
			
		}
		wmsLocations = commonDao.findByQuery(
				hql,
				new String[] { "warehouseId","companyId","supplierId"},
				new Object[] { wmsCountPlan.getWarehouse().getId(),wmsCountPlan.getCompany().getId(),wmsCountPlan.getSupplier().getId()});
		
		if (wmsLocations.size() <= 0) {
			return;
		}
		for (WmsLocation loc : wmsLocations) {
			createWmsCountDetail(wmsCountPlan, loc);
		}
	}
	
	/**
	 * 库位异常盘点
	 */
	private void createWmsCountPlanForLocationException(WmsCountPlan wmsCountPlan) {
		List<WmsLocation> wmsLocations = new ArrayList<WmsLocation>();

		String hql = "";
		if (WmsCountLockType.LOCK.equals(wmsCountPlan.getLockType())) {
			hql = "FROM WmsLocation loc WHERE loc.warehouse.id = :warehouseId " +
					" AND loc.exceptionFlag = true AND loc.lockCount = false ";

		} else {
			hql = "FROM WmsLocation loc WHERE loc.warehouse.id = :warehouseId " +
					" AND loc.exceptionFlag = true ";
		}
		wmsLocations = commonDao.findByQuery(hql, new String[] { "warehouseId"}, new Object[] { wmsCountPlan.getWarehouse().getId()});
		if (wmsLocations.size() <= 0) {
			return;
		}
		for (WmsLocation loc : wmsLocations) {
			createWmsCountDetail(wmsCountPlan, loc);
		}
	}
	/**
	 * 按货品创建盘点计划
	 * */
	private void createWmsCountPlanForItem(WmsCountPlan wmsCountPlan, boolean recountFlag ) {
		List<WmsLocation> wmsLocations = new ArrayList<WmsLocation>();
		String hql = "";
		String recountStr = recountFlag ? "" : " AND loc.lockCount = false ";
		if (WmsCountLockType.LOCK.equals(wmsCountPlan.getLockType())) {
			hql = "FROM WmsLocation loc WHERE loc.warehouse.id = :warehouseId "
					+ " AND loc.type = 'STORAGE' "+ recountStr + " AND loc.id IN "
					+ "	(SELECT DISTINCT w1.location.id FROM WmsInventory w1 WHERE w1.location.warehouse.id = :warehouseId "
					+ "		AND w1.itemKey.item.id = :itemId AND w1.quantityBU > 0 )"
					+ " AND loc.id NOT IN "
					+ "	(SELECT DISTINCT w2.location.id FROM WmsInventory w2 WHERE w2.location.warehouse.id = :warehouseId AND w2.quantityBU > 0 "
					+ "		AND (w2.status <> '-' OR w2.putawayQuantityBU <> 0 OR w2.allocatedQuantityBU <> 0))";
		} else {
			hql = "SELECT DISTINCT loc FROM WmsInventory w1 LEFT JOIN w1.location loc WHERE loc.warehouse.id = :warehouseId "
					+ " AND loc.type = 'STORAGE' "
					+ " AND w1.itemKey.item.id = :itemId AND w1.quantityBU > 0";
		}

		wmsLocations = commonDao.findByQuery(hql, new String[] { "warehouseId",
				"itemId"}, new Object[] { wmsCountPlan.getWarehouse().getId(),
				wmsCountPlan.getItem().getId() });
		if (wmsLocations.size() <= 0) {
			 throw new OriginalBusinessException("货品【"+wmsCountPlan.getItem().getName()+"】无待盘点库位！");
		}

		for (WmsLocation loc : wmsLocations) {
			createWmsCountDetail(wmsCountPlan, loc);
		}
	}

	/**
	 * 按库位创建盘点计划---一次提取一定数量库位
	 * */
	private void createWmsCountPlanForLocation(WmsCountPlan wmsCountPlan,
			Integer cycleDays) {
		// 本次要盘点的库位
		List<WmsLocation> wmsLocations = new ArrayList<WmsLocation>();

		String hql = "";
		if (WmsCountLockType.LOCK.equals(wmsCountPlan.getLockType())) {
			//获取当前仓库所有可盘点库位（包含指定货主）
			hql = "FROM WmsLocation loc WHERE loc.warehouse.id = :warehouseId " +
					" AND loc.type = 'STORAGE' AND loc.lockCount = false " +
					" AND (loc.cycleDate IS NULL OR (current_date() - loc.cycleDate) > :countDays) " +
					" AND (loc.touchDate IS NULL OR (current_date() - loc.touchDate) > :countDays)" + 
					" AND loc.id IN " +
					"	(SELECT DISTINCT w1.location.id FROM WmsInventory w1 WHERE w1.location.warehouse.id = :warehouseId " +
					"     	AND w1.itemKey.item.company.id = :companyId" +
					" 		AND w1.itemKey.lotInfo.supplier.id = :supplierId)"+
					" AND loc.id NOT IN " +
					"	(SELECT DISTINCT w2.location.id FROM WmsInventory w2 WHERE w2.location.warehouse.id = :warehouseId " +
					"     	AND (w2.status <> '-' OR w2.putawayQuantityBU <> 0 OR w2.allocatedQuantityBU <> 0)) " +
					" ORDER BY loc.cycleDate DESC";
		} else {
			//不锁库位盘点(最后盘点日期和最后动碰日期都要大于盘点周期)
			hql = "FROM WmsLocation loc WHERE loc.warehouse.id = :warehouseId " +
					" AND loc.type = 'STORAGE' " +
					" AND (loc.cycleDate IS NULL OR (current_date() - loc.cycleDate) > :countDays) " +
					" AND (loc.touchDate IS NULL OR (current_date() - loc.touchDate) > :countDays)" +
					" AND loc.id IN (SELECT DISTINCT w1.location.id FROM WmsInventory w1 WHERE w1.location.warehouse.id = :warehouseId " +
					"     AND w1.itemKey.item.company.id = :companyId" +
					" 	  AND w1.itemKey.lotInfo.supplier.id = :supplierId)"+ 
					" ORDER BY loc.cycleDate DESC";
		}

		wmsLocations.addAll(commonDao.findByQuery(
				hql,
				new String[] { "warehouseId", "countDays","companyId","supplierId" },
				new Object[] { wmsCountPlan.getWarehouse().getId(),Double.valueOf(cycleDays.toString()),
						wmsCountPlan.getCompany().getId(), wmsCountPlan.getSupplier().getId()}));
		if (wmsLocations.size() <= 0) {
			return;
			// throw new
			// OriginalBusinessException("货主【"+wmsCountPlan.getCompany().getName()+"】无待盘点库位！");
		}

		if (wmsCountPlan.getLocationCount() == 0) {
			// 所有库位全部盘点
			for (WmsLocation loc : wmsLocations) {
				createWmsCountDetail(wmsCountPlan, loc);
			}
		} else {
			// 盘点一定数量库位
			List<Long> usedLocs = new ArrayList<Long>();
			// 两次循环
			// 先取未盘点过的库位
			int cnt = 0;
			for (int i = wmsLocations.size() - 1; i >= 0; i--) {
				WmsLocation loc = wmsLocations.get(i);
				if (cnt == wmsCountPlan.getLocationCount()) {
					break;
				}
				if (loc.getCycleDate() == null) {
					usedLocs.add(loc.getId());
					createWmsCountDetail(wmsCountPlan, loc);
					cnt++;
				} else {
					break;
				}
			}
			// 再取最早盘点过的库位
			for (WmsLocation loc : wmsLocations) {
				if (usedLocs.contains(loc.getId())) {
					break;
				} else {
					if (cnt == wmsCountPlan.getLocationCount()) {
						break;
					}
					usedLocs.add(loc.getId());
					createWmsCountDetail(wmsCountPlan, loc);
					cnt++;
				}
			}
		}
	}

	/**
	 * 按库位动碰创建盘点计划
	 * */
	private void createWmsCountPlanForMovedLocation(WmsCountPlan wmsCountPlan) {
		List<WmsLocation> wmsLocations = new ArrayList<WmsLocation>();

		String hql = "";
		if (WmsCountLockType.LOCK.equals(wmsCountPlan.getLockType())) {
			hql = "FROM WmsLocation loc WHERE loc.warehouse.id = :warehouseId " +
					" AND loc.type = 'STORAGE' AND loc.lockCount = false " +
					" AND loc.touchCount >= :touchCount " +
					" AND loc.id IN " +
					"	(SELECT DISTINCT w1.location.id FROM WmsInventory w1 WHERE w1.location.warehouse.id = :warehouseId " +
					"     	AND w1.itemKey.item.company.id = :companyId" + 
					" 		AND w1.itemKey.lotInfo.supplier.id = :supplierId)"+
					" AND loc.id NOT IN " +
					"	(SELECT DISTINCT w2.location.id FROM WmsInventory w2 WHERE w2.location.warehouse.id = :warehouseId " +
					"     	AND (w2.status <> '-' OR w2.putawayQuantityBU <> 0 OR w2.allocatedQuantityBU <> 0)) ";

		} else {
			hql = "FROM WmsLocation loc WHERE loc.warehouse.id = :warehouseId " +
					" AND loc.type = 'STORAGE' " +
					" AND loc.touchCount >= :touchCount " +
					" AND loc.id IN (SELECT DISTINCT w1.location.id FROM WmsInventory w1 WHERE w1.location.warehouse.id = :warehouseId " +
					"     AND w1.itemKey.item.company.id = :companyId" +
					" 		AND w1.itemKey.lotInfo.supplier.id = :supplierId)"+
					" ORDER BY loc.cycleDate DESC";
			
		}
		wmsLocations = commonDao.findByQuery(
				hql,
				new String[] { "warehouseId", "touchCount", "companyId" },
				new Object[] { wmsCountPlan.getWarehouse().getId(),wmsCountPlan.getTouchTimes(),
						wmsCountPlan.getCompany().getId(),wmsCountPlan.getSupplier().getId() });
		if (wmsLocations.size() <= 0) {
			return;
			// throw new
			// OriginalBusinessException("货主【"+wmsCountPlan.getCompany().getName()+"】无待盘点库位！");
		}

		for (WmsLocation loc : wmsLocations) {
			createWmsCountDetail(wmsCountPlan, loc);
		}
	}

	/**
	 * 根据库位创建盘点计划明细
	 * */
	private void createWmsCountDetail(WmsCountPlan wmsCountPlan,
			WmsLocation location) {
		WmsCountDetail wmsCountDetail = EntityFactory
				.getEntity(WmsCountDetail.class);
		wmsCountDetail.setLocationId(location.getId());
		wmsCountDetail.setLocationCode(location.getCode());
		wmsCountDetail.setCountPlan(wmsCountPlan);

		wmsCountPlan.getDetails().add(wmsCountDetail);
		wmsCountPlan.setLocationCount(wmsCountPlan.getDetails().size());
		commonDao.store(wmsCountPlan);
	}

	/**
	 * 盘点计划修改页面保存(只修改备注跟盘点单创建时间)
	 * */
	public void saveCountPlan(WmsCountPlan wmsCountPlan) {
		WmsCountPlan countPlan = load(WmsCountPlan.class, wmsCountPlan.getId());
		countPlan.setDescription(wmsCountPlan.getDescription());
		countPlan.updateInfo(UserHolder.getUser());
		commonDao.store(countPlan);
	}

	/**
	 * 删除盘点单明细
	 * */
	public void removeDetail(WmsCountDetail wmsCountDetail) {
		WmsCountPlan countPlan = commonDao.load(WmsCountPlan.class,
				wmsCountDetail.getCountPlan().getId());
		if (!WmsCountStatus.OPEN.equals(wmsCountDetail.getCountPlan()
				.getStatus())) {
			throw new OriginalBusinessException("盘点计划状态错误！");
		}
		countPlan.getDetails().remove(wmsCountDetail);
		countPlan.setLocationCount(countPlan.getDetails().size());

		commonDao.store(countPlan);
	}

	/**
	 * 添加盘点单明细
	 * */
	public void addDetail(Long wmsCountPlanId, WmsLocation location) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		WmsCountPlan wmsCountPlan = load(WmsCountPlan.class, wmsCountPlanId);
		createWmsCountDetail(wmsCountPlan, location);
		// 不包含当天已盘点库位
		/*if ((location.getCycleDate() == null)
				|| (location.getCycleDate() != null && !sdf.format(
						location.getCycleDate()).equals(
						sdf.format(new Date())))) {
			createWmsCountDetail(wmsCountPlan, location);
		}else{
			LocalizedMessage.addLocalizedMessage(MyUtils.font("失败!当前库位当天已盘点"));
		}*/
	}

	/**
	 * 激活盘点计划
	 * */
	public void active(WmsCountPlan wmsCountPlan) {
		String hql = "SELECT wmsCountDetail.locationId FROM WmsCountDetail wmsCountDetail WHERE wmsCountDetail.countPlan.id = :countPlanId";
		List<Long> locationIds = commonDao.findByQuery(hql,
				new String[] { "countPlanId" },
				new Object[] { wmsCountPlan.getId() });
		if (locationIds == null || locationIds.size() <= 0) {
			throw new OriginalBusinessException("盘点计划中无待盘点库位，不能激活！");
		}

		if (WmsCountLockType.LOCK.equals(wmsCountPlan.getLockType())) {
			// 锁定所有待盘点库位
			List<WmsLocation> locs = commonDao.findByQuery(
					"FROM WmsLocation loc WHERE loc.id in (:locationIds)",
					new String[] { "locationIds" },
					new Object[] { locationIds });
			for (WmsLocation loc : locs) {
				if(!wmsCountPlan.getPlanType().equals(WmsCountPlanType.RECOUNT)) {
					loc.countLockLocations();
				}
				commonDao.store(loc);
			}
		}
		Long itemId = (wmsCountPlan.getItem() == null ? 0L : wmsCountPlan
				.getItem().getId());
		// 提取库存数据写入盘点表
		List<WmsInventoryExtend> wsns = commonDao
				.findByQuery(
						"SELECT wsn FROM WmsInventoryExtend wsn "
								+ " LEFT JOIN wsn.item "
								+ " WHERE wsn.locationId in (:locationIds) "
								+ " AND (wsn.inventory.allocatedQuantityBU > 0 OR wsn.inventory.putawayQuantityBU > 0 OR wsn.inventory.quantityBU > 0) "
								+ " AND wsn.inventory.status = '-' "
								+ " AND wsn.item.company.id =:companyId "
								+ " AND (wsn.item.id = :itemId OR :itemId = 0)",
						new String[] { "locationIds","companyId", "itemId" },
						new Object[] { locationIds,wmsCountPlan.getCompany().getId(), itemId });
		createCountRecordByItem(wmsCountPlan, wsns);
		//记录库位锁定日志
		if (WmsCountLockType.LOCK.equals(wmsCountPlan.getLockType())) {
			for(WmsInventoryExtend wsn : wsns ){
				wmsInventoryManager.addInventoryLog(WmsInventoryLogType.LOCK, 0, 
						wmsCountPlan.getCode(), null, wsn.getInventory().getLocation(), wsn.getInventory().getItemKey(), 0D,  
						wsn.getInventory().getPackageUnit(),wsn.getInventory().getStatus(),"盘点激活-库位锁定");
			}
		}
		// 如果盘点计划不锁库位，则直接激活盘点计划即可
		// workflowManager.doWorkflow(wmsCountPlan, "countPlanProcess.active");
	}

	/**
	 * 盘点计划反激活
	 * */
	public void unActive(WmsCountPlan wmsCountPlan) {
		if (!WmsCountStatus.ACTIVE.equals(wmsCountPlan.getStatus())) {
			return;
		}

		if (WmsCountLockType.LOCK.equals(wmsCountPlan.getLockType())) {
			//记录库位解锁日志
			String hql = " FROM WmsCountRecord cr WHERE cr.countPlan.id = :countPlanId ";
			List<WmsCountRecord> wcrs = commonDao.findByQuery(hql, "countPlanId", wmsCountPlan.getId());
			for(WmsCountRecord wcr : wcrs){
				WmsLocation loc = commonDao.load(WmsLocation.class, wcr.getLocationId());
				wmsInventoryManager.addInventoryLog(WmsInventoryLogType.UNLOCK, 0, 
						wmsCountPlan.getCode(), null, loc, wcr.getItemKey(), 0D,  
						wcr.getPackageUnit(),"-","盘点反激活-库位解锁");
			}
			// 释放库位
			for (WmsCountDetail detail : wmsCountPlan.getDetails()) {
				WmsLocation loc = commonDao.load(WmsLocation.class,
						detail.getLocationId());
				loc.countUnLockLocations();
				commonDao.store(loc);
			}
		}
		// 删除盘点记录表
		commonDao
				.executeByHql(
						"DELETE FROM WmsCountRecord cr WHERE cr.countPlan.id = :countPlanId",
						new String[] { "countPlanId" },
						new Object[] { wmsCountPlan.getId() });
	}

	/**
	 * 根据库存创建盘点记录表
	 * */
	private void createCountRecord(WmsCountPlan wmsCountPlan,
			List<WmsInventoryExtend> wsns) {
		for (WmsInventoryExtend wsn : wsns) {
			WmsCountRecord record = EntityFactory
					.getEntity(WmsCountRecord.class);
			record.setInventoryId(wsn.getInventory().getId());
			record.setLocationId(wsn.getLocationId());
			record.setLocationCode(wsn.getLocationCode());
			record.setPallet(wsn.getPallet());
			record.setCarton(wsn.getCarton());
			record.setSerialNo(wsn.getSerialNo());
			record.setItemKey(wsn.getInventory().getItemKey());
			record.setPackageUnit(wsn.getInventory().getPackageUnit());
			record.setQuantityBU(wsn.getQuantityBU());
			record.setCountQuantityBU(wsn.getQuantityBU());
			record.setDeltaQuantityBU(0D);
			record.setCountPlan(wmsCountPlan);
			commonDao.store(record);
		}
	}

	/**
	 * 根据库存创建盘点记录表
	 * */
	private void createCountRecordByItem(WmsCountPlan wmsCountPlan,
			List<WmsInventoryExtend> wsns) {
		List<WmsCountRecord> records = new ArrayList<WmsCountRecord>();
		for (WmsInventoryExtend wsn : wsns) {
			WmsCountRecord record = getCountRecord(records,
					wsn.getLocationId(), wsn.getItem().getId());
			if (record == null) {
				record = EntityFactory.getEntity(WmsCountRecord.class);
				record.setInventoryId(wsn.getInventory().getId());
				record.setLocationId(wsn.getLocationId());
				record.setLocationCode(wsn.getLocationCode());
				record.setItem(wsn.getItem());
				record.setPackageUnit(wsn.getItem().getPackageByLineNo(1));
				record.setQuantityBU(wsn.getQuantityBU());
				record.setCountQuantityBU(wsn.getQuantityBU());
				record.setDeltaQuantityBU(0D);
				record.setItemKey(wsn.getInventory().getItemKey());
				record.setCountPlan(wmsCountPlan);
				record.setPallet(wsn.getPallet());//added by yuan.sun 
				record.setSerialNo(wsn.getSerialNo());//added by yuan.sun 
				record.setCarton(wsn.getCarton());//added by yuan.sun 
			} else {
				record.addQuantityBU(wsn.getQuantityBU());
			}
			record.setUpdateInfo(new UpdateInfo(UserHolder.getUser()));

			commonDao.store(record);
		}
	}

	private WmsCountRecord getCountRecord(List<WmsCountRecord> records,
			Long locationId, Long itemId) {
		for (WmsCountRecord record : records) {
			if (record.getLocationId().equals(locationId)
					&& record.getItem().getId().equals(itemId)) {
				return record;
			}
		}
		return null;
	}

	/**
	 * 盘点登记---批量修改模式
	 * */
	public void record(WmsCountRecord wmsCountRecord, List records) {
		WmsCountPlan wmsCountPlan = wmsCountRecord.getCountPlan();

		WmsCountDetail countDetail = (WmsCountDetail) wmsCommonDao
				.findByQueryUniqueResult(
						"FROM WmsCountDetail wcd WHERE wcd.countPlan.id = :countPlanId AND wcd.locationId = :locationId",
						new String[] { "countPlanId", "locationId" },
						new Object[] { wmsCountPlan.getId(),
								wmsCountRecord.getLocationId() });
		if (countDetail.getStatus().equals(WmsCountDetailStatus.OPEN)) {
			countDetail.setStatus(WmsCountDetailStatus.COUNT);
		}
		// 更新盘点数量
		Double countQuantityBU = Double.valueOf(String.valueOf(records.get(0)));// 盘点箱数
		if (countQuantityBU < 0) {
			throw new OriginalBusinessException("登记数量不能小于0");
		}
		wmsCountRecord.adjust(countQuantityBU);

		// 普通类型盘点计划，需要检查盘点差异数量是否超过阀值
		//wmsCountRecord.setBeReCount(Boolean.FALSE);
		if (WmsCountPlanType.NORMAL.equals(wmsCountPlan.getPlanType())) {
			// WmsItemStorageType wist = (WmsItemStorageType)
			// commonDao.findByQueryUniqueResult(
			// "FROM WmsItemStorageType wist WHERE wist.item.id = :itemId AND wist.warehouse.id = :warehouseId",
			// new String[] {"itemId", "warehouseId"}, new Object[]
			// {wmsCountRecord.getItem().getId(),
			// wmsCountPlan.getWarehouse().getId()});
			// if (wist != null) {
			// if (wist.getCountLimit() > 0 &&
			// Math.abs(wmsCountRecord.getDeltaQuantityBU()) >
			// wist.getCountLimit()) {
			// wmsCountRecord.setBeReCount(Boolean.TRUE);
			// }
			// }
		}

		commonDao.store(wmsCountRecord);

		commonDao.store(wmsCountPlan);
		workflowManager.doWorkflow(wmsCountPlan, "countPlanProcess.count");
	}

	/**
	 * 盘点登记---手工增加盘点记录
	 */
	public void record(Long countPlanId, Long locationId, Long itemId,
			Double quantity) {
		// 不锁库位盘点登记时，每次都对之前的盘点结果进行覆盖处理
		if (quantity <= 0) {
			throw new BusinessException("盘点数量不能小于等于0！");
		}

		WmsCountPlan wmsCountPlan = load(WmsCountPlan.class, countPlanId);
		WmsItem item = commonDao.load(WmsItem.class, itemId);
		WmsPackageUnit packageUnit = item.getPackageByLineNo(1);
		WmsLocation wmsLocation = commonDao.load(WmsLocation.class, locationId);
		WmsCountDetail countDetail = (WmsCountDetail) wmsCommonDao
				.findByQueryUniqueResult(
						"FROM WmsCountDetail wcd WHERE wcd.countPlan.id = :countPlanId AND wcd.locationId = :locationId",
						new String[] { "countPlanId", "locationId" },
						new Object[] { countPlanId, locationId });
		if (countDetail.getStatus().equals(WmsCountDetailStatus.OPEN)) {
			countDetail.setStatus(WmsCountDetailStatus.COUNT);
		}

		// 不锁库位盘点时，先检查盘点计划中是否已存在对应盘点记录，如果存在则数量覆盖处理
		WmsCountRecord record = null;
		Double inventoryQtyBU = 0D;
		List<WmsCountRecord> records = commonDao
				.findByQuery(
						"FROM WmsCountRecord wcr WHERE wcr.countPlan.id = :countPlanId",
						new String[] { "countPlanId" },
						new Object[] { countPlanId });
		if (WmsCountLockType.UNLOCK.equals(wmsCountPlan.getLockType())) {
			// 提取库位货品库存数量
			inventoryQtyBU = (Double) commonDao
					.findByQueryUniqueResult(
							"SELECT SUM(inv.quantityBU) FROM WmsInventory inv "
									+ " WHERE inv.location.id = :locationId AND inv.itemKey.item.id = :itemId",
							new String[] { "locationId", "itemId" },
							new Object[] { locationId, itemId });
			// 检查库位货品是否已经有盘点记录
			record = getCountRecord(records, locationId, itemId);
		}

		if (record == null) {
			record = EntityFactory.getEntity(WmsCountRecord.class);
			record.setLocationId(locationId);
			record.setLocationCode(wmsLocation.getCode());
			record.setItem(item);
			record.setPackageUnit(packageUnit);
		}

		if (WmsCountLockType.UNLOCK.equals(wmsCountPlan.getLockType())) {
			// 不锁库位盘点时，临时提取库存数量作为账面库存数量
			record.setQuantityBU(inventoryQtyBU == null ? 0D : inventoryQtyBU);
		} else {
			// 锁库位盘点时，库存账面数量默认为0
			record.setQuantityBU(0D);
		}
		WmsItemKey itemKey = new WmsItemKey();
		LotInfo lotInfo = new LotInfo();
		if(item.getDefaultLotRule().getTrackProduceDate()){
			lotInfo.setProductDate(new Date());
		}
		if(item.getDefaultLotRule().getTrackSupplier()){
			lotInfo.setSupplier(item.getCompany());
		}
		lotInfo.setExtendPropC1(BaseStatus.NULLVALUE);
		lotInfo.setExtendPropC2(BaseStatus.NULLVALUE);
		lotInfo.setExtendPropC3(BaseStatus.NULLVALUE);
		lotInfo.setExtendPropC4(BaseStatus.NULLVALUE);
		lotInfo.setExtendPropC5(BaseStatus.NULLVALUE);
		itemKey = itemManager.getItemKey(wmsCountPlan.getWarehouse(),
				wmsCountPlan.getCode(),
				item, lotInfo);
		itemKey.setLotInfo(lotInfo);
		commonDao.store(itemKey);
		if(record.getItemKey() == null){
			record.setItemKey(itemKey);
		}
		record.setCountQuantityBU(quantity);
		record.setDeltaQuantityBU(record.getQuantityBU()
				- record.getCountQuantityBU());
		record.setCountPlan(wmsCountPlan);
		record.setUpdateInfo(new UpdateInfo(UserHolder.getUser()));

		// 普通类型盘点计划，需要检查盘点差异数量是否超过阀值
		//record.setBeReCount(Boolean.FALSE);
		if (WmsCountPlanType.NORMAL.equals(wmsCountPlan.getPlanType())) {
			// WmsItemStorageType wist = (WmsItemStorageType)
			// commonDao.findByQueryUniqueResult(
			// "FROM WmsItemStorageType wist WHERE wist.item.id = :itemId AND wist.warehouse.id = :warehouseId",
			// new String[] {"itemId", "warehouseId"}, new Object[]
			// {item.getId(), wmsCountPlan.getWarehouse().getId()});
			// if (wist != null) {
			// if (wist.getCountLimit() > 0 &&
			// Math.abs(record.getDeltaQuantityBU()) > wist.getCountLimit()) {
			// record.setBeReCount(Boolean.TRUE);
			// }
			// }
		}
		record.setBeCounted(Boolean.TRUE);
		commonDao.store(record);

		workflowManager.doWorkflow(wmsCountPlan, "countPlanProcess.count");
	}

	/**
	 * 关闭 ,差异调整两个功能合并为 关闭
	 * */
	public void quantityAdjust(WmsCountPlan wmsCountPlan) {
		Set<WmsCountRecord> wmsCountRecordSet = wmsCountPlan.getRecords();
		if (wmsCountRecordSet.size() <= 0) {
			return;
		}

		Set<Long> locationIds = new HashSet<Long>();
		Set<Long> itemIds = new HashSet<Long>();
		String warehouseName = WmsWarehouseHolder.getWmsWarehouse().getName();
//		String companyName = wmsCountPlan.getCompany().getName();
		
		// 对有差异的盘点记录进行差异调整
		for (WmsCountRecord wmsCountRecord : wmsCountRecordSet) {
			if (wmsCountRecord.getDeltaQuantityBU() != 0) {
				// 进行差异调整
				quantityAdjust(wmsCountRecord);
				// 记录需要复盘货品--复盘计划手动创建
				/*Object value = wmsRuleManager.getSingleRuleTableDetail(warehouseName,
						"R101_盘点_复盘表","复盘数量",companyName,wmsCountRecord.getItem().getClass1(),
						wmsCountRecord.getItem().getCode());
				if(value != null) {
					if(Math.abs(wmsCountRecord.getDeltaQuantityBU()) >= Double.valueOf(value.toString())) {
						locationIds.add(wmsCountRecord.getLocationId());
						itemIds.add(wmsCountRecord.getItem().getId());
					}
				}*/
				
			}
		}

		// 对需要复盘的记录创建货品复盘计划--不要自动创建复盘计划
		/*if (locationIds.size() > 0) {
			WmsCountPlan countPlan = EntityFactory.getEntity(WmsCountPlan.class);

			// 产生盘点计划头
			countPlan.setWarehouse(wmsCountPlan.getWarehouse());
			countPlan.setCompany(wmsCountPlan.getCompany());
			countPlan.setCode(codeManager.generateCodeByRule(wmsCountPlan
					.getWarehouse(), wmsCountPlan.getCompany().getName(),
					"盘点单", ""));
			countPlan.updateInfo(UserHolder.getUser());
			countPlan.setType(WmsCountType.LOCATION);
			countPlan.setPlanType(WmsCountPlanType.RECOUNT);
			countPlan.setLockType(wmsCountPlan.getLockType());
			commonDao.store(countPlan);
			workflowManager.doWorkflow(countPlan, "countPlanProcess.new");
			
			for (Long locationId : locationIds) {
				WmsLocation location = commonDao.load(WmsLocation.class, locationId);
				createWmsCountDetail(countPlan, location);
			}
			
			// 提取库存数据写入盘点表
			List<WmsInventoryExtend> wsns = commonDao
					.findByQuery(
							"SELECT wsn FROM WmsInventoryExtend wsn "
									+ " LEFT JOIN wsn.item "
									+ " WHERE wsn.locationId in (:locationIds) "
									+ " AND (wsn.inventory.allocatedQuantityBU > 0 OR wsn.inventory.putawayQuantityBU > 0 OR wsn.inventory.quantityBU > 0) "
									+ " AND wsn.inventory.status = '-' "
									+ " AND wsn.item.id in (:itemIds)",
							new String[] { "locationIds", "itemIds" },
							new Object[] { locationIds, itemIds });
			createCountRecordByItem(countPlan, wsns);
			workflowManager.doWorkflow(countPlan, "countPlanProcess.active");
		}*/
	}

	private WmsInventory getWmsInventory(Long locationId, Long itemId) {
		List<Long> inventorys = commonDao
				.findByQuery(
						"SELECT inventory.id FROM WmsInventory inventory "
								+ " LEFT JOIN inventory.itemKey itemKey "
								+ " LEFT JOIN inventory.itemKey.item item "
								+ " WHERE inventory.location.id = :locationId "
								+ " AND item.id = :itemId AND inventory.quantityBU > 0 "
								+ " ORDER BY itemKey.lot", new String[] {
								"locationId", "itemId" }, new Object[] {
								locationId, itemId });
		if (!inventorys.isEmpty()) {
			Long id = inventorys.get(0);
			return load(WmsInventory.class, id);
		}
		return null;
	}

	/**
	 * 盘点--库存数量差异调整
	 * */
	private void quantityAdjust(WmsCountRecord countRecord) {
		WmsPackageUnit wpu = countRecord.getItem().getPackageByLineNo(1);

		WmsLocation countLoc = warehouseManager
				.getCountLocationByWarehouse(countRecord.getCountPlan()
						.getWarehouse());
		if (countLoc == null) {
			throw new OriginalBusinessException("找不到盘点库位！");
		}

		double deltaQuantity = countRecord.getDeltaQuantityBU();
		if (deltaQuantity > 0) {
			// 盘亏处理：移走最老批次物料
			List<WmsInventoryExtend> wsns = commonDao.findByQuery(
					"SELECT wsn FROM WmsInventoryExtend wsn "
							+ " LEFT JOIN wsn.inventory inv "
							+ " LEFT JOIN wsn.inventory.itemKey itemKey "
							+ " LEFT JOIN wsn.item item "
							+ " WHERE wsn.locationId = :locationId "
							+ " AND item.id = :itemId AND wsn.quantityBU > 0 "
							+ " ORDER BY itemKey.lot", new String[] {
							"locationId", "itemId" }, new Object[] {
							countRecord.getLocationId(),
							countRecord.getItem().getId() });
			for (WmsInventoryExtend wsn : wsns) {
				if (deltaQuantity == 0) {
					break;
				}

				Double movedQuantity = 0D;
				if (wsn.getQuantityBU() >= deltaQuantity) {
					movedQuantity = deltaQuantity;
				} else {
					movedQuantity = wsn.getQuantityBU();
				}
				deltaQuantity -= movedQuantity;

				wsn.removeQuantity(movedQuantity);
				if (DoubleUtils.compareByPrecision(wsn.getQuantityBU(), 0D,
						wpu.getPrecision()) == 0) {
					commonDao.delete(wsn);
				}

				// 已有库存的数量修改
				WmsInventory srcInv = wsn.getInventory();
				srcInv.removeQuantityBU(movedQuantity);

				// 写库存日志
				wmsInventoryManager.addInventoryLog(WmsInventoryLogType.MOVE,
						-1, countRecord.getCountPlan().getCode(), null,
						srcInv.getLocation(), srcInv.getItemKey(),
						movedQuantity, srcInv.getPackageUnit(),
						srcInv.getStatus(), "盘点差异调整");

				// 刷新库满度
				wmsInventoryManager.refreshLocationUseRate(srcInv.getLocation(), 0);

				// 增加盘点库位库存
//				addCountInventory(countRecord, countLoc, srcInv.getItemKey(),wpu, movedQuantity);
			}
		} else {
			double quantity = Math.abs(deltaQuantity);
			// 盘盈处理：
			// 1.依次扣减盘点库位批次最老库存
			deltaQuantity = removeCountInventory(countRecord, countLoc, wpu,
					deltaQuantity);
			// 2.不足时新建库存
			if (deltaQuantity < 0) {
				// 取该物料库存最老批次
				WmsItemKey itemKey = (WmsItemKey) wmsCommonDao
						.findByQueryUniqueResult(
								"SELECT inv.itemKey FROM WmsInventory inv WHERE inv.itemKey.item.id = :itemId ORDER BY inv.itemKey.lot ",
								new String[] { "itemId" },
								new Object[] { countRecord.getItem().getId() });
				if (itemKey == null) {
					// 如果无物料库存，以当前日期作为生产日期，其他批次属性全部用"-"代替
					LotInfo lotInfo = new LotInfo();
					if(countRecord.getItem().getDefaultLotRule().getTrackProduceDate()){
						lotInfo.setProductDate(new Date());
					}
					lotInfo.setExtendPropC1(BaseStatus.NULLVALUE);
					lotInfo.setExtendPropC2(BaseStatus.NULLVALUE);
					lotInfo.setExtendPropC3(BaseStatus.NULLVALUE);
					lotInfo.setExtendPropC4(BaseStatus.NULLVALUE);
					lotInfo.setExtendPropC5(BaseStatus.NULLVALUE);
					itemKey = itemManager.getItemKey(countLoc.getWarehouse(),
							countRecord.getCountPlan().getCode(),
							countRecord.getItem(), lotInfo,false);
				}
				/*// 新增库存
				WmsInventory countInv = wmsInventoryManager
						.getInventoryWithNew(countLoc, itemKey, wpu,
								BaseStatus.NULLVALUE);
				countInv.addQuantityBU(deltaQuantity);
				// 新增序列号信息
				inventoryExtendManager.addInventoryExtend(countInv,
						countRecord.getPallet(), countRecord.getCarton(),
						countRecord.getSerialNo(), deltaQuantity);*/

				// 写盘点库位的库存日志
				wmsInventoryManager.addInventoryLog(WmsInventoryLogType.MOVE,
						1, countRecord.getCountPlan().getCode(), null,
						countLoc, itemKey, deltaQuantity, wpu,
						BaseStatus.NULLVALUE, "盘点差异调整");
			}

			WmsInventory inventory = getWmsInventory(
					countRecord.getLocationId(), countRecord.getItem().getId());

			if (inventory == null) {
				WmsLocation loc = load(WmsLocation.class,
						countRecord.getLocationId());
				WmsItemKey itemKey = (WmsItemKey) wmsCommonDao
						.findByQueryUniqueResult(
								"SELECT inv.itemKey FROM WmsInventory inv WHERE inv.itemKey.item.id = :itemId ORDER BY inv.itemKey.lot ",
								new String[] { "itemId" },
								new Object[] { countRecord.getItem().getId() });
				if (itemKey == null) {
					// 如果无物料库存，以当前日期作为生产日期，其他批次属性全部用"-"代替
					LotInfo lotInfo = new LotInfo();
					lotInfo.setExtendPropC1(BaseStatus.NULLVALUE);
					lotInfo.setExtendPropC2(BaseStatus.NULLVALUE);
					lotInfo.setExtendPropC3(BaseStatus.NULLVALUE);
					lotInfo.setExtendPropC4(BaseStatus.NULLVALUE);
					lotInfo.setExtendPropC5(BaseStatus.NULLVALUE);
					itemKey = itemManager.getItemKey(countLoc.getWarehouse(),
							countRecord.getCountPlan().getCode(),
							countRecord.getItem(), lotInfo);
				}
				// 新增库存
				inventory = wmsInventoryManager.getInventoryWithNew(loc,
						itemKey, wpu, BaseStatus.NULLVALUE);
				inventory.addQuantityBU(quantity);
			} else {
				// 已有库存的数量修改
				inventory.addQuantityBU(quantity);
			}

			// 新增序列号信息
			inventoryExtendManager.addInventoryExtend(inventory,
					countRecord.getPallet(), countRecord.getCarton(),
					countRecord.getSerialNo(),
					quantity);

			// 刷新库满度
			wmsInventoryManager.refreshLocationUseRate(inventory.getLocation(), 0);

			// 写盘点库位的库存日志
			wmsInventoryManager.addInventoryLog(WmsInventoryLogType.MOVE, 1,
					countRecord.getCountPlan().getCode(), null,
					inventory.getLocation(), inventory.getItemKey(), quantity,
					wpu, inventory.getStatus(), "盘点差异调整");

		}
	}

	/**
	 * 按照批次先后顺序扣减盘点库位库存（注意传入的deltaQuantity是负数--盘盈）
	 */
	private double removeCountInventory(WmsCountRecord countRecord,
			WmsLocation countLoc, WmsPackageUnit wpu, Double deltaQuantity) {
		List<WmsInventoryExtend> wsns = commonDao.findByQuery(
				"SELECT wsn FROM WmsInventoryExtend wsn "
						+ " LEFT JOIN wsn.inventory inv "
						+ " LEFT JOIN wsn.inventory.itemKey itemKey "
						+ " LEFT JOIN wsn.item item "
						+ " WHERE wsn.locationId = :locationId "
						+ " AND item.id = :itemId AND wsn.quantityBU > 0"
						+ " ORDER BY itemKey.lot", new String[] { "locationId",
						"itemId" }, new Object[] { countLoc.getId(),
						countRecord.getItem().getId() });
		Double removeQty = 0D;
		for (WmsInventoryExtend wsn : wsns) {
			if (deltaQuantity >= 0) {
				break;
			}
			if (wsn.getQuantityBU() >= Math.abs(deltaQuantity)) {
				removeQty = Math.abs(deltaQuantity);
			} else {
				removeQty = wsn.getQuantityBU();
			}
			deltaQuantity += removeQty;

			wsn.removeQuantity(removeQty);
			if (DoubleUtils.compareByPrecision(wsn.getQuantityBU(), 0D,
					wpu.getPrecision()) == 0) {
				commonDao.delete(wsn);
			}

			// 已有库存的数量修改
			WmsInventory srcInv = wsn.getInventory();
			srcInv.removeQuantityBU(removeQty);

			// 写库存日志
			wmsInventoryManager.addInventoryLog(WmsInventoryLogType.MOVE, -1,
					countRecord.getCountPlan().getCode(), null,
					srcInv.getLocation(), srcInv.getItemKey(), removeQty,
					srcInv.getPackageUnit(), srcInv.getStatus(), "盘点差异调整");
		}
		return deltaQuantity;
	}

	/**
	 * 先冲减盘点库位相同货品负库存（盘盈时产生的），冲减剩余数量再增加盘点库存
	 */
	private void addCountInventory(WmsCountRecord countRecord,
			WmsLocation countLoc, WmsItemKey itemKey, WmsPackageUnit wpu,
			Double movedQuantity) {
		List<WmsInventoryExtend> cwsns = commonDao.findByQuery(
				"SELECT wsn FROM WmsInventoryExtend wsn "
						+ " LEFT JOIN wsn.inventory inv "
						+ " LEFT JOIN wsn.inventory.itemKey itemKey "
						+ " LEFT JOIN wsn.item item "
						+ " WHERE wsn.locationId = :locationId "
						+ " AND item.id = :itemId AND wsn.quantityBU < 0"
						+ " ORDER BY itemKey.lot", new String[] { "locationId",
						"itemId" }, new Object[] { countLoc.getId(),
						countRecord.getItem().getId() });
		Double quantity = 0D;
		Double leftQuantity = movedQuantity;
		for (WmsInventoryExtend cwsn : cwsns) {
			if (leftQuantity <= 0) {
				break;
			}
			if (Math.abs(cwsn.getQuantityBU()) >= leftQuantity) {
				quantity = Math.abs(cwsn.getQuantityBU());
			} else {
				quantity = leftQuantity;
			}
			leftQuantity -= quantity;

			cwsn.addQuantity(quantity);
			if (DoubleUtils.compareByPrecision(cwsn.getQuantityBU(), 0D,
					wpu.getPrecision()) == 0) {
				commonDao.delete(cwsn);
			}

			// 盘点库存数量修改
			WmsInventory srcInv = cwsn.getInventory();
			srcInv.addQuantityBU(quantity);
		}

		// 冲减盘点库位负库存后的剩余数量创建盘点库存
		if (leftQuantity > 0) {
			// 新增库存
			WmsInventory countInv = wmsInventoryManager.getInventoryWithNew(
					countLoc, itemKey, wpu, BaseStatus.NULLVALUE);
			countInv.addQuantityBU(leftQuantity);
			// 新增序列号信息
			inventoryExtendManager.addInventoryExtend(countInv,
					countRecord.getPallet(), countRecord.getCarton(),
					countRecord.getSerialNo(),
					leftQuantity);
		}

		// 写盘点库位的库存日志
		wmsInventoryManager.addInventoryLog(WmsInventoryLogType.MOVE, 1,
				countRecord.getCountPlan().getCode(), null, countLoc, itemKey,
				movedQuantity, wpu, BaseStatus.NULLVALUE, "盘点差异调整");
	}

	/**
	 * 关闭
	 * */
	public void close(WmsCountPlan wmsCountPlan) {
		if(wmsCountPlan.getLockType().equals(WmsCountLockType.UNLOCK)) {
			for (WmsCountDetail countDetail : wmsCountPlan.getDetails()) {
				String hql = "select count(*) FROM WmsCountRecord record WHERE " +
						"record.countPlan.id = :wmsCountPlanId and " +
						"record.locationId = :locationId " ;

				Long count = (Long)commonDao.findByQueryUniqueResult(hql,
						new String[] { "wmsCountPlanId", "locationId" },
						new Object[] { wmsCountPlan.getId(),countDetail.getLocationId()});
				if(count==0) {
					throw new BusinessException("存在没有盘点的库位，不允许关闭！");
				}
			}
		}
		quantityAdjust(wmsCountPlan);
		for (WmsCountDetail detail : wmsCountPlan.getDetails()) {
			detail.setStatus(WmsCountDetailStatus.FINISHED);
			WmsLocation loc = commonDao.load(WmsLocation.class,
					detail.getLocationId());
			// 释放库位
			if (WmsCountLockType.LOCK.equals(wmsCountPlan.getLockType())) {
				loc.countUnLockLocations();
			}
			// 初始化库位盘点和动碰信息
			loc.setCycleDate(new Date());
			loc.setTouchDate(null);
			loc.setTouchCount(0);
			commonDao.store(loc);
		}
		if (WmsCountLockType.LOCK.equals(wmsCountPlan.getLockType())) {
			//记录库位解锁日志
			String hql = " FROM WmsCountRecord cr WHERE cr.countPlan.id = :countPlanId ";
			List<WmsCountRecord> wcrs = commonDao.findByQuery(hql, "countPlanId", wmsCountPlan.getId());
			for(WmsCountRecord wcr : wcrs){
				WmsLocation loc = commonDao.load(WmsLocation.class, wcr.getLocationId());
				wmsInventoryManager.addInventoryLog(WmsInventoryLogType.UNLOCK, 0, 
						wmsCountPlan.getCode(), null, loc, wcr.getItemKey(), 0D,  
						wcr.getPackageUnit(),"-","盘点关闭-库位解锁");
			}
		}
	}

	public void changeWorker(WmsCountPlan wmsCountPlan , Long workerID) {
		WmsWorker worker = commonDao.load(WmsWorker.class, workerID);
		String hql = "FROM WmsCountDetail detail WHERE detail.countPlan.id =:countPlanId ";
		List<WmsCountDetail> details = commonDao.findByQuery(hql, "countPlanId", wmsCountPlan.getId());
		for(WmsCountDetail countDetail : details){
			if (countDetail.getStatus().equals(WmsCountDetailStatus.FINISHED)) {
				throw new BusinessException("该库位已盘点完成，不允许指派");
			}
			countDetail.setWorker(worker);
			countDetail.setStatus(WmsCountDetailStatus.OPEN);

			commonDao.store(countDetail);

			workflowManager.doWorkflow(countDetail.getCountPlan(),
					"countPlanProcess.count");
		}
	}
	
	public void deleteCountPlan(WmsCountPlan wmsCountPlan){
		List<WmsCountRecord> wmsCountRecords = new ArrayList<WmsCountRecord>(wmsCountPlan.getRecords());
		for (WmsCountRecord wmsCountRecord : wmsCountRecords) {
			WmsCountDetail countDetail = (WmsCountDetail) wmsCommonDao.findByQueryUniqueResult("FROM WmsCountDetail wcd WHERE wcd.countPlan.id = :countPlanId AND wcd.locationId = :locationId", 
					new String[] {"countPlanId", "locationId"}, new Object[] {wmsCountPlan.getId(), wmsCountRecord.getLocationId()});
			if (countDetail.getStatus().equals(WmsCountDetailStatus.COUNT)) {
				countDetail.setStatus(WmsCountDetailStatus.OPEN);
			}
		}
		
	}
}