package com.vtradex.wms.server.telnet.move.pojo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTransactionManager;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.ShipLotInfo;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.inventory.WmsInventoryLogType;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsMoveDocLocation;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.model.move.WmsMoveDocType;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.move.WmsTaskStatus;
import com.vtradex.wms.server.model.move.WmsWorkDoc;
import com.vtradex.wms.server.model.organization.TypeOfBill;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsLocationType;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
import com.vtradex.wms.server.service.base.WmsBillTypeManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryExtendManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.task.WmsTaskManager;
import com.vtradex.wms.server.service.workDoc.WmsWorkDocManager;
import com.vtradex.wms.server.telnet.base.WmsWarehouseRFManager;
import com.vtradex.wms.server.telnet.dto.WmsMoveTaskDTO;
import com.vtradex.wms.server.telnet.move.WmsMoveRFManager;
import com.vtradex.wms.server.utils.PackageUtils;
import com.vtradex.wms.server.utils.WmsStringUtils;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;
import com.vtradex.wms.server.web.filter.WmsWorkerHolder;


@SuppressWarnings("unchecked")
public class DefaultWmsMoveRFManager extends DefaultBaseManager implements WmsMoveRFManager {

	private final WmsWorkDocManager workDocManager;
	private final WorkflowManager workflowManager;
	private final WmsWarehouseRFManager wmsWarehouseRFManager;
	private final WmsInventoryManager wmsInventoryManager;
	private final WmsBussinessCodeManager wmsBussinessCodeManager;
	private final WmsRuleManager wmsRuleManager;
	protected WmsBillTypeManager wmsBillTypeManager;
	protected WmsTaskManager wmsTaskManager;
	protected WmsInventoryExtendManager inventoryExtendManager;
	
	public DefaultWmsMoveRFManager(WmsWorkDocManager workDocManager , WorkflowManager workflowManager , WmsWarehouseRFManager wmsWarehouseRFManager
					,WmsInventoryManager wmsInventoryManager, WmsBussinessCodeManager wmsBussinessCodeManager
					, WmsRuleManager wmsRuleManager, WmsBillTypeManager wmsBillTypeManager,WmsTaskManager wmsTaskManager
					,WmsInventoryExtendManager inventoryExtendManager) {
		this.workDocManager = workDocManager;
		this.workflowManager = workflowManager;
		this.wmsWarehouseRFManager = wmsWarehouseRFManager;
		this.wmsInventoryManager = wmsInventoryManager;
		this.wmsBussinessCodeManager = wmsBussinessCodeManager;
		this.wmsRuleManager = wmsRuleManager;
		this.wmsBillTypeManager =  wmsBillTypeManager;
		this.wmsTaskManager = wmsTaskManager;
		this.inventoryExtendManager = inventoryExtendManager;
	}

	public WmsMoveTaskDTO findMoveTaskByMoveDocId(final Long moveDocId) {
		String hql = "SELECT task FROM WmsTask task , WmsLocation loc " +
				" WHERE task.fromLocationId = loc.id " +
				" AND task.moveDocDetail.moveDoc.id=:moveDocId" +
				" AND task.status in (:statuss)" +
				" AND (task.worker.id = :workerId or task.worker.id is null)" +
				" AND (task.planQuantityBU - task.movedQuantityBU) > 0" +
				" AND task.tiredMovedQuantityBU <= task.planQuantityBU" +
				" AND task.tiredMovedQuantityBU - task.movedQuantityBU <= 0" +
				" ORDER BY task.pallet DESC,loc.routeNo ASC";
		List<WmsTask> taskList = commonDao.findByQueryMaxNum(hql, new String[]{"moveDocId", "statuss", "workerId"}, 
				new Object[]{moveDocId , 
					Arrays.asList(new String[]{WmsTaskStatus.DISPATCHED , WmsTaskStatus.WORKING}), 
					WmsWorkerHolder.getWmsWorker().getId()}, 0, 1);
		if (taskList == null || taskList.isEmpty()) {
			String taskTiredHql = "SELECT task FROM WmsTask task , WmsLocation loc " +
					" WHERE task.fromLocationId = loc.id " +
					" AND task.moveDocDetail.moveDoc.id=:moveDocId" +
					" AND task.status in (:statuss)" +
					" AND task.worker.id = :workerId" +
					" AND task.tiredMovedQuantityBU <= task.planQuantityBU" +
					" AND (task.tiredMovedQuantityBU - task.movedQuantityBU) > 0" +
					" ORDER BY task.pallet DESC,loc.routeNo ASC";
			List<WmsTask> taskTiredList = commonDao.findByQueryMaxNum(taskTiredHql, new String[]{"moveDocId", "statuss", "workerId"}, 
					new Object[]{moveDocId , 
						Arrays.asList(new String[]{WmsTaskStatus.DISPATCHED , WmsTaskStatus.WORKING}),
						WmsWorkerHolder.getWmsWorker().getId()}, 0, 1);
			if (!taskTiredList.isEmpty()) {
				WmsMoveTaskDTO dto = new WmsMoveTaskDTO();
				dto.setBeTiredPutaway(Boolean.TRUE);
				return dto;
			}
			throw new BusinessException("暂无移位任务或者已完成");
		}
		WmsTask task = taskList.get(0);
		return createWmsMoveTaskDTO(task);
	}
	
	
	public WmsMoveTaskDTO findMovePutawayTaskByTired(final Long moveDocId) {
		String hql = "SELECT task FROM WmsTask task , WmsLocation loc " +
				" WHERE task.fromLocationId = loc.id " +
				" AND task.moveDocDetail.moveDoc.id=:moveDocId" +
				" AND task.status in (:statuss)" +
				" AND task.worker.id = :workerId" +
				" AND (task.tiredMovedQuantityBU - task.movedQuantityBU) > 0" +
				" ORDER BY loc.routeNo ASC";
		List<WmsTask> taskList = commonDao.findByQueryMaxNum(hql, new String[]{"moveDocId", "statuss", "workerId"}, 
				new Object[]{moveDocId , 
					Arrays.asList(new String[]{WmsTaskStatus.DISPATCHED , WmsTaskStatus.WORKING}), 
					WmsWorkerHolder.getWmsWorker().getId()}, 0, 1);
		if(taskList.isEmpty()){
			throw new BusinessException("暂无移位任务或者已完成");
		}
		WmsTask task = taskList.get(0);
		WmsMoveTaskDTO dto = new WmsMoveTaskDTO();
		dto.setMoveDocId(moveDocId);
		dto.setMoveDocDetailId(task.getMoveDocDetail().getId());
		dto.setSrcInventoryId(task.getSrcInventoryId());
		dto.setFromLocationId(task.getFromLocationId());
		dto.setFromLocationCode(task.getFromLocationCode());
		dto.setDestInventoryId(task.getDescInventoryId());
		dto.setToLocationId(task.getToLocationId());
		dto.setToLocationCode(task.getToLocationCode());
		dto.setTaskId(task.getId());
		dto.setItemId(task.getItemKey().getItem().getId());
		dto.setItemCode(task.getItemKey().getItem().getCode());
		dto.setItemName(task.getItemKey().getItem().getName());
		dto.setBarCode(task.getItemKey().getItem().getBarCode());
		dto.setUnMoveQuantityBU(task.getTiredMovedQuantityBU() - task.getMovedQuantityBU());
		return dto;
	}
	
	/**
	 * 获取非计划散货上架明细
	 */
	public WmsMoveTaskDTO findMoveTaskByTired(final Long moveDocId) {
		String moveDocDetailHql = "SELECT mdd FROM WmsMoveDocDetail mdd" +
				" where mdd.moveDoc.id = :moveDocId " +
				" and (mdd.planQuantityBU - mdd.movedQuantityBU) > 0";
		List<WmsMoveDocDetail> mddList = commonDao.findByQueryMaxNum(moveDocDetailHql, "moveDocId", moveDocId, 0, 1);
		if (mddList.isEmpty()) {
			throw new BusinessException("");
		}
		WmsMoveDocDetail mdd = mddList.get(0);
		WmsItem item = commonDao.load(WmsItem.class, mdd.getItem().getId());
		
		WmsMoveTaskDTO dto = new WmsMoveTaskDTO();
		dto.setMoveDocId(moveDocId);
		dto.setMoveDocDetailId(mdd.getId());
		dto.setSrcInventoryId(mdd.getInventoryId());
		dto.setFromLocationId(mdd.getFromLocationId());
		dto.setFromLocationCode(mdd.getFromLocationCode());
		dto.setDestInventoryId(mdd.getDestInventoryId());
		dto.setToLocationId(mdd.getToLocationId());
		dto.setToLocationCode(mdd.getToLocationCode());
		dto.setItemId(item.getId());
		dto.setItemCode(item.getCode());
		dto.setItemName(item.getName());
		dto.setBarCode(item.getBarCode());
		//be added on 2015-10-21
		dto.setSrcInventoryExtendId(mdd.getSrcInvExId());
		if (mdd.getAllocatedQuantityBU() <= 0) {
			mdd.setAllocatedQuantityBU(mdd.getPlanQuantityBU() - mdd.getMovedQuantityBU());
		}
		dto.setUnMoveQuantityBU(mdd.getUnMovedQuantityBU());
		commonDao.store(mdd);
		
		return dto;
	}
	
	/**
	 * 手工散货移位，需要生成虚拟移位计划
	 * 
	 * @param moveDocId
	 * @return
	 */
	public WmsMoveTaskDTO findMoveTaskByVirtual(Long moveDocId) {
		WmsMoveDoc moveDoc = null;
		if (moveDocId == null) {
			String hql = "FROM WmsMoveDoc md " +
					" where md.beVirtualMove = true " +
					" and md.worker.id = :workerId " +
					" and md.status not in ('FINISHED', 'CANCELED')";
			List<WmsMoveDoc> moveDocList = commonDao.findByQueryMaxNum(hql, "workerId", WmsWorkerHolder.getWmsWorker().getId(), 0, 1);
			if (moveDocList.isEmpty()) {
				return null;
			}
			moveDoc = moveDocList.get(0);
		}
		WmsMoveTaskDTO dto = new WmsMoveTaskDTO();
		dto.setMoveDocId(moveDoc.getId());
		return dto;
	}
	
	/**
	 * 非计划手工托盘移位
	 * 
	 * @param moveDocId
	 * @return
	 */
	public WmsMoveTaskDTO findMoveTaskByPallet(String pallet) {
		String hql = "SELECT wie.inventory.id, sum(wie.quantityBU) FROM WmsInventoryExtend wie where wie.pallet = :pallet group by wie.inventory.id";
		Object obj = commonDao.findByQueryUniqueResult(hql, "pallet", pallet);
		if (obj == null) {
			throw new BusinessException("该托盘不能移位或者不存在");
		}
		Object[] objs = (Object[])obj;
		Long invId = new Long(objs[0].toString());
		Double palletQuantityBU = new Double(objs[1].toString());
		WmsInventory inv = commonDao.load(WmsInventory.class, invId);
		if (palletQuantityBU > inv.getAvailableQuantityBU()) {
			throw new BusinessException("托盘中货品总数超过库存可用数，不能移位");
		}
		WmsLocation srcLocation = commonDao.load(WmsLocation.class, inv.getLocation().getId());
		if (!WmsLocationType.STORAGE.equals(srcLocation.getType())) {
			throw new BusinessException("非存货区的托盘不能移位");
		}
		WmsItemKey itemKey = commonDao.load(WmsItemKey.class, inv.getItemKey().getId());
		WmsItem item = commonDao.load(WmsItem.class, itemKey.getItem().getId());
		WmsMoveTaskDTO dto = new WmsMoveTaskDTO();
		dto.setSrcInventoryId(inv.getId());
		dto.setFromLocationId(srcLocation.getId());
		dto.setFromLocationCode(srcLocation.getCode());
		dto.setItemId(item.getId());
		dto.setItemCode(item.getCode());
		dto.setItemName(item.getName());
		dto.setItemKeyId(itemKey.getId());
		dto.setSrcInventoryStatus(inv.getStatus());
		dto.setUnMoveQuantityBU(palletQuantityBU);
		dto.setPackageUnitId(inv.getPackageUnit().getId());
		
		return dto;
	}
	
	private WmsMoveTaskDTO createWmsMoveTaskDTO(WmsTask task) {
		WmsWorkDoc workerDoc = commonDao.load(WmsWorkDoc.class, task.getWorkDoc().getId());
		workerDoc.setWorker(WmsWorkerHolder.getWmsWorker());
		commonDao.store(workerDoc);
		
		task.setWorker(WmsWorkerHolder.getWmsWorker());
		commonDao.store(task);
		
		WmsMoveDocDetail moveDocDetail = commonDao.load(WmsMoveDocDetail.class, task.getMoveDocDetail().getId());
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, moveDocDetail.getMoveDoc().getId());
		
		WmsMoveTaskDTO dto = new WmsMoveTaskDTO();
		
		dto.setMoveDocId(moveDoc.getId());
		dto.setMoveDocCode(moveDoc.getCode());
		dto.setMoveDocDetailId(moveDocDetail.getId());
		dto.setWorkDocId(workerDoc.getId());
		dto.setTaskId(task.getId());
		
		WmsItemKey itemKey = commonDao.load(WmsItemKey.class, task.getItemKey().getId());
		WmsItem item = commonDao.load(WmsItem.class, itemKey.getItem().getId());
		dto.setItemKeyId(itemKey.getId());
		dto.setItemId(item.getId());
		dto.setItemCode(item.getCode());
		dto.setItemName(item.getName());
		dto.setBarCode(item.getBarCode());
		
		dto.setSrcInventoryId(task.getSrcInventoryId());
		dto.setFromLocationId(task.getFromLocationId());
		dto.setFromLocationCode(task.getFromLocationCode());
		
		dto.setDestInventoryId(task.getDescInventoryId());
		dto.setToLocationId(task.getToLocationId());
		dto.setToLocationCode(task.getToLocationCode());
		
		WmsMoveDocDetail mdd = commonDao.load(WmsMoveDocDetail.class, task.getMoveDocDetail().getId());
		dto.setPallet(mdd.getPallet());
		dto.setCarton(mdd.getCarton());
		dto.setUnMoveQuantityBU(task.getPlanQuantityBU() - task.getTiredMovedQuantityBU());
		dto.setTiredMovedQuantityBU(task.getTiredMovedQuantityBU());
		return dto;
	}
	
	public boolean checkBeItemCode(String code, Long taskId) {
		//条码是否是物料条码
		WmsTask task = commonDao.load(WmsTask.class, taskId);
		if(task.getItemKey().getItem().getCode().equals(code) || 
				task.getItemKey().getItem().getBarCode().equals(code)) {
			return true;
		}
		//其他条码
		Long inventoryExtendCount = (Long)commonDao.findByQueryUniqueResult(
				"SELECT count(wie.id) FROM WmsInventoryExtend wie WHERE wie.inventory.id=:invId AND (wie.pallet=:code OR wie.carton=:code OR wie.serialNo=:code) AND wie.quantityBU > wie.allocatedQuantityBU"
				, new String[]{"invId","code"}, new Object[]{task.getSrcInventoryId(),code});
		if(inventoryExtendCount == null || inventoryExtendCount <= 0) {
			throw new BusinessException("不存在该条码");
		}
		return false;
	}
	
	/**
	 * 按托盘检验
	 * @param dto
	 * @param pallet
	 */
	public void checkMoveQuantityValidByPallet(WmsMoveTaskDTO dto, String pallet) {
		String hql = "select sum(inve.quantityBU) from WmsInventoryExtend inve where inve.inventory.id = :inventoryId and inve.pallet = :pallet";
		Double totalQuantityBU = (Double)commonDao.findByQueryUniqueResult(hql, new String[]{"inventoryId", "pallet"},
				new Object[]{dto.getSrcInventoryId(), pallet});
		if (totalQuantityBU.doubleValue() != dto.getActualMovedQuantityBU().doubleValue()) {
			throw new BusinessException("实际库存数：" + totalQuantityBU.toString() + "与移位数量不符");
		}
		WmsInventory inv = commonDao.load(WmsInventory.class, dto.getSrcInventoryId());
		if (inv.getAllocatedQuantityBU().doubleValue() < dto.getActualMovedQuantityBU().doubleValue()) {
			throw new BusinessException("移位数量不能大于分配数量：" + inv.getAllocatedQuantityBU().toString());
		}
	}
	
	/**
	 * 按箱号检验
	 * @param dto
	 * @param carton
	 */
	public void checkMoveQuantityValidByCarton(WmsMoveTaskDTO dto, String carton) {
		String hql = "select sum(inve.quantityBU) from WmsInventoryExtend inve where inve.inventory.id = :inventoryId and inve.carton = :carton";
		Double totalQuantityBU = (Double)commonDao.findByQueryUniqueResult(hql, new String[]{"inventoryId", "carton"},
				new Object[]{dto.getSrcInventoryId(), carton});
		if (totalQuantityBU.doubleValue() != dto.getActualMovedQuantityBU().doubleValue()) {
			throw new BusinessException("实际库存数：" + totalQuantityBU.toString() + "与移位数量不符");
		}
		WmsInventory inv = commonDao.load(WmsInventory.class, dto.getSrcInventoryId());
		if (inv.getAllocatedQuantityBU().doubleValue() < dto.getActualMovedQuantityBU().doubleValue()) {
			throw new BusinessException("移位数量不能大于分配数量：" + inv.getAllocatedQuantityBU().toString());
		}
	}
	
	/**
	 * 按货品条码检验
	 * @param dto
	 * @param barCode
	 */
	public void checkMoveQuantityValidByBarCode(WmsMoveTaskDTO dto, String barCode) {
		String hql = "select sum(inve.quantityBU) from WmsInventoryExtend inve where inve.inventory.id = :inventoryId and inve.item.barCode = :barCode";
		Double totalQuantityBU = (Double)commonDao.findByQueryUniqueResult(hql, new String[]{"inventoryId", "carton"},
				new Object[]{dto.getSrcInventoryId(), barCode});
		if (totalQuantityBU.doubleValue() < dto.getActualMovedQuantityBU().doubleValue()) {
			throw new BusinessException("实际库存数：" + totalQuantityBU.toString() + "与移位数量不符");
		}
		WmsInventory inv = commonDao.load(WmsInventory.class, dto.getSrcInventoryId());
		if (inv.getAllocatedQuantityBU().doubleValue() < dto.getActualMovedQuantityBU().doubleValue()) {
			throw new BusinessException("移位数量不能大于分配数量：" + inv.getAllocatedQuantityBU().toString());
		}
	}
	
	/**
	 * 按托盘移位确认
	 * @param dto
	 * @param actualToLocationCode
	 */
	public void confirmMoveByPallet(WmsMoveTaskDTO dto, String pallet, String actualToLocationCode) {
		WmsTask task = commonDao.load(WmsTask.class, dto.getTaskId());
		String hql = "SELECT task FROM WmsTask task , WmsInventoryExtend invExt " +
				" WHERE task.srcInventoryId = invExt.inventory.id " +
				" AND task.originalBillCode=:billCode " +
				" AND invExt.pallet= :pallet " +
				" AND task.status in (:statuss) " +
				" AND task.fromLocationId=:fromLocationId";
		List<WmsTask> taskList = commonDao.findByQuery(hql, 
				new String[]{"billCode" , "pallet" , "statuss" , "fromLocationId"}, 
				new Object[]{task.getOriginalBillCode() , pallet , 
				Arrays.asList(new String[]{WmsTaskStatus.DISPATCHED , WmsTaskStatus.WORKING}), task.getFromLocationId()});
		if (taskList.isEmpty()) {
			throw new BusinessException("无可移位确认的任务");
		}
		WmsLocation toLocation = queryWmsLocationByCode(actualToLocationCode);
		for(WmsTask t : taskList) {
			workDocManager.singleWorkConfirm(t, toLocation.getId(), t.getFromLocationId(), 
					t.getUnmovedQuantityBU(), WmsWorkerHolder.getWmsWorker().getId());
			workflowManager.doWorkflow(t, "taskProcess.confirm");
		}
	}
	
	/**
	 * 非计划手工按托盘移位确认
	 * @param dto
	 * @param actualToLocationCode
	 */
	public void confirmMoveManualByPallet(WmsMoveTaskDTO dto, String pallet, String actualToLocationCode) {
		String wieHql = "FROM WmsInventoryExtend wie where wie.inventory.id = :inventoryId and wie.pallet = :pallet";
		List<WmsInventoryExtend> wieList = commonDao.findByQueryMaxNum(wieHql, new String[]{"inventoryId", "pallet"}, 
				new Object[]{dto.getSrcInventoryId(), pallet}, 0, 1);
		if (wieList.isEmpty()) {
			throw new BusinessException("托盘不存在");
		}
		WmsInventoryExtend wie = wieList.get(0);
		WmsInventory srcInv = commonDao.load(WmsInventory.class, wie.getInventory().getId());
		WmsPackageUnit packageUnit = commonDao.load(WmsPackageUnit.class, srcInv.getPackageUnit().getId());
		srcInv.setPackageUnit(packageUnit);
		wie.setInventory(srcInv);
		WmsLocation toLocation = this.queryWmsLocationByCode(actualToLocationCode);
		wmsInventoryManager.inventoryManualMove(wie, toLocation.getId(), dto.getUnMoveQuantityBU(), WmsWorkerHolder.getWmsWorker().getId());
	}
	
	/**
	 * 非计划手工散货按货品条码移位确认
	 * @param dto
	 * @param actualToLocationCode
	 */
	public void confirmMoveManualByBarCode(WmsMoveTaskDTO dto, String actualToLocationCode) {
		String wieHql = "FROM WmsInventoryExtend wie " +
				" where wie.inventory.id = :inventoryId " +
				" and wie.item.barCode = :barCode" +
				" AND wie.pallet=:pallet AND wie.carton=:carton AND wie.serialNo=:serialNo ";
		List<WmsInventoryExtend> wieList = commonDao.findByQueryMaxNum(wieHql, new String[]{"inventoryId", "barCode", "pallet", "carton", "serialNo"}, 
				new Object[]{dto.getSrcInventoryId(), dto.getBarCode(), BaseStatus.NULLVALUE, BaseStatus.NULLVALUE, BaseStatus.NULLVALUE}, 0, 1);
		if (wieList.isEmpty()) {
			throw new BusinessException("移位库存不存在");
		}
		WmsInventoryExtend wie = wieList.get(0);
		WmsInventory srcInv = commonDao.load(WmsInventory.class, dto.getSrcInventoryId());
		WmsPackageUnit packageUnit = commonDao.load(WmsPackageUnit.class, srcInv.getPackageUnit().getId());
		srcInv.setPackageUnit(packageUnit);
		wie.setInventory(srcInv);
		srcInv.unallocatePickup(dto.getActualMovedQuantityBU());
		WmsLocation toLocation = this.queryWmsLocationByCode(actualToLocationCode);
		wmsInventoryManager.moveInventory(wie, toLocation,BaseStatus.NULLVALUE,BaseStatus.NULLVALUE,packageUnit, dto.getActualMovedQuantityBU(),srcInv.getStatus(),WmsInventoryLogType.MOVE, "库存手工移位");
		
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, dto.getMoveDocId());
		WmsMoveDocDetail mdd = commonDao.load(WmsMoveDocDetail.class, dto.getMoveDocDetailId());
		WmsItemKey itemKey = commonDao.load(WmsItemKey.class, srcInv.getItemKey().getId());
		
		if (dto.getDestInventoryId() != null) {
			WmsInventory dstInventory = wmsInventoryManager.getInventoryWithNew(
					toLocation, itemKey, packageUnit, srcInv.getStatus());
			dstInventory.setPutawayQuantityBU(dstInventory.getPutawayQuantityBU() - dto.getUnMoveQuantityBU());
			commonDao.store(dstInventory);
		}
		mdd.move( dto.getActualMovedQuantityBU());
		commonDao.store(mdd);
		
		if (moveDoc.getPlanQuantityBU().doubleValue() == moveDoc.getMovedQuantityBU().doubleValue()) {
			moveDoc.setStatus(WmsMoveDocStatus.FINISHED);
		}
		commonDao.store(moveDoc);
	}
	
	/**
	 * 按箱号移位确认
	 * @param dto
	 * @param actualToLocationCode
	 */
	public void confirmMoveByCarton(WmsMoveTaskDTO dto, String carton, String actualToLocationCode) {
		WmsTask task = commonDao.load(WmsTask.class, dto.getTaskId());
		String hql = "SELECT task FROM WmsTask task , WmsInventoryExtend invExt " +
				" WHERE task.srcInventoryId = invExt.inventory.id " +
				" AND task.originalBillCode=:billCode " +
				" AND invExt.carton= :carton " +
				" AND task.status in (:statuss) " +
				" AND task.fromLocationId=:fromLocationId";
		List<WmsTask> taskList = commonDao.findByQuery(hql, 
				new String[]{"billCode" , "pallet" , "statuss" , "fromLocationId"}, 
				new Object[]{task.getOriginalBillCode() , carton , 
				Arrays.asList(new String[]{WmsTaskStatus.DISPATCHED , WmsTaskStatus.WORKING}), task.getFromLocationId()});
		if (taskList.isEmpty()) {
			throw new BusinessException("无可移位确认的任务");
		}
		WmsLocation toLocation = queryWmsLocationByCode(actualToLocationCode);
		for(WmsTask t : taskList) {
			workDocManager.singleWorkConfirm(t, toLocation.getId(), t.getFromLocationId(), 
					t.getUnmovedQuantityBU(), WmsWorkerHolder.getWmsWorker().getId());
			workflowManager.doWorkflow(t, "taskProcess.confirm");
		}
	}
	
	/**
	 * 按货品条码移位确认
	 * @param dto
	 * @param actualToLocationCode
	 */
	public void confirmMoveByBarCode(WmsMoveTaskDTO dto, String actualToLocationCode) {
		WmsTask task = commonDao.load(WmsTask.class, dto.getTaskId());
		WmsLocation toLocation = queryWmsLocationByCode(actualToLocationCode);
		workDocManager.singleWorkConfirm(task, toLocation.getId(), task.getFromLocationId(), 
				dto.getActualMovedQuantityBU(), WmsWorkerHolder.getWmsWorker().getId());
		workflowManager.doWorkflow(task, "taskProcess.confirm");
	}
	
	/**
	 * 按累货货品条码移位确认
	 * @param dto
	 * @param actualToLocationCode
	 */
	public void confirmMoveByTired(WmsMoveTaskDTO dto, String actualToLocationCode) {
		WmsTask task = commonDao.load(WmsTask.class, dto.getTaskId());
		WmsLocation toLocation = queryWmsLocationByCode(actualToLocationCode);
		workDocManager.singleWorkConfirm(task, toLocation.getId(), task.getFromLocationId(), 
				dto.getActualMovedQuantityBU(), WmsWorkerHolder.getWmsWorker().getId());
		workflowManager.doWorkflow(task, "taskProcess.confirm");
//		task.setTiredMovedQuantityBU(task.getTiredMovedQuantityBU() - dto.getActualMovedQuantityBU());
	}
	
	/**
	 * 按累货货品条码移位确认
	 * @param dto
	 * @param actualToLocationCode
	 */
	
	public void confirmMoveByVirtualTired(WmsMoveTaskDTO dto, String actualToLocationCode) {
		if (dto == null || dto.getMoveDocId() == null) {
			createMoveDoc();
		}
		
		WmsTask task = commonDao.load(WmsTask.class, dto.getTaskId());
		WmsLocation toLocation = queryWmsLocationByCode(actualToLocationCode);
		workDocManager.singleWorkConfirm(task, toLocation.getId(), task.getFromLocationId(), 
				dto.getActualMovedQuantityBU(), WmsWorkerHolder.getWmsWorker().getId());
		workflowManager.doWorkflow(task, "taskProcess.confirm");
	}
	
	private WmsMoveDoc createMoveDoc() {
		WmsMoveDoc moveDoc = new WmsMoveDoc();
		String str = new SimpleDateFormat("yyyyMMdd").format(new Date());
		moveDoc.setMovePlanTitle("虚拟移位计划-"+str);
		moveDoc.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
		WmsOrganization company = getCompanyByWarehouse(moveDoc.getWarehouse());
		moveDoc.setCompany(company);
		
		String query = "FROM WmsBillType bill WHERE bill.company.id = :companyId AND bill.code = :code";
		WmsBillType billType = (WmsBillType) commonDao.findByQueryUniqueResult(
				query, new String[] { "companyId", "code" }, new Object[] {
						moveDoc.getCompany().getId(), WmsMoveDocType.MV_MOVE });
		moveDoc.setCode(wmsBussinessCodeManager.generateCodeByRule(moveDoc.getWarehouse(), moveDoc.getWarehouse().getName(), "移位单",""));
		moveDoc.setBillType(billType);
		moveDoc.setType(WmsMoveDocType.MV_MOVE);
		moveDoc.setOriginalBillType(null);
		moveDoc.setOriginalBillCode(null);
		moveDoc.setDetails(new HashSet<WmsMoveDocDetail>());
		moveDoc.setBeVirtualMove(Boolean.TRUE);
		moveDoc.setWorker(WmsWorkerHolder.getWmsWorker());
		commonDao.store(moveDoc);
		return moveDoc;
	}
	
	/**
	 * 非计划手工创建虚拟移位单和移位明细
	 */
	public void createMoveDocDetail(WmsMoveTaskDTO dto) {
		WmsMoveDoc moveDoc = null;
		if ( dto.getMoveDocId() == null) {
			moveDoc = createMoveDoc();
		} else {
			moveDoc = commonDao.load(WmsMoveDoc.class, dto.getMoveDocId());
		}
		WmsLocation fromLocation = queryWmsLocationByCode(dto.getFromLocationCode());
		
		List<WmsInventoryExtend> wieList = this.getWmsInventoryExtend(dto.getFromLocationCode(), dto.getBarCode());
		
		Double totalMoveQty = dto.getActualMovedQuantityBU();
		for(WmsInventoryExtend inventoryExtend : wieList) {
			if(totalMoveQty <= 0) {
				break;
			}
			WmsInventory inv = commonDao.load(WmsInventory.class, inventoryExtend.getInventory().getId());
			Double moveQty = 0D;
			if(inventoryExtend.getAvailableQuantityBU() > totalMoveQty){
				moveQty = totalMoveQty;
				totalMoveQty = 0D;
			} else {
				moveQty = inventoryExtend.getAvailableQuantityBU();
				totalMoveQty -= moveQty;
			}
			// 创建移位计划明细
			WmsMoveDocDetail wmdd = createMoveDocDetail(moveDoc, 
					inv.getItemKey(), 
					inv.getPackageUnit(), 
					inv.getPackQty(moveQty), 
					moveQty, 
					fromLocation.getId(), 
					fromLocation.getCode(), 
					inventoryExtend.getPallet(), 
					inv.getStatus(), 
					inv.getId(), 
					inventoryExtend);
			inv.allocatePickup(moveQty);
			moveDoc.addDetail(wmdd);
			commonDao.store(wmdd);
			commonDao.store(moveDoc);
			
			WmsMoveTaskDTO readyAllocateDTO = new WmsMoveTaskDTO();
			readyAllocateDTO.setMoveDocId(moveDoc.getId());
			readyAllocateDTO.setMoveDocDetailId(wmdd.getId());
			
			this.autoAllocateVirtual(readyAllocateDTO);
		}
		dto.setMoveDocId(moveDoc.getId());
		if(totalMoveQty > 0) {
			throw new BusinessException("待移位数量大于库存数量");
		}
	}
	
	/**
	 * 根据仓库获取
	 * 
	 * @param warehouse
	 * @return
	 */
	private WmsOrganization getCompanyByWarehouse(WmsWarehouse warehouse) {
		String hql = "from WmsOrganization org where org.code = :code AND org.beCompany = true";
		Object obj = this.commonDao.findByQueryUniqueResult(hql, "code",
				warehouse.getCode());
		if (obj == null) {
			throw new BusinessException("仓库: " + warehouse.getCode()
					+ ", 虚拟货主未设置");
		}
		return (WmsOrganization) obj;
	}
	
	/**
	 * 标记累货
	 * @param dto
	 */
	public void markTiredTask(WmsMoveTaskDTO dto) {
		WmsTask task = commonDao.load(WmsTask.class, dto.getTaskId());
		task.addTiredMovedQuantityBU(dto.getTiredMovedQuantityBU());
		commonDao.store(task);
	}

	public void confirmMoveTask(WmsMoveTaskDTO moveTaskDTO, String toLocationCode) {
		WmsLocation toLocation = queryWmsLocationByCode(toLocationCode);
		WmsTask task = commonDao.load(WmsTask.class, moveTaskDTO.getTaskId());
		List<WmsWorker> workers = wmsWarehouseRFManager.getWmsWorker(UserHolder.getUser().getId(), WmsWarehouseHolder.getWmsWarehouse().getId());
		if(workers.size() <= 0){
			throw new BusinessException("未找到对应的工作人员");
		}
		WmsWorker worker = workers.get(0);
		if(moveTaskDTO.getActualMovedQuantityBU() <=0){
			//整托、整箱移位
			List<WmsTask> tasks = commonDao.findByQuery("SELECT task FROM WmsTask task , WmsInventoryExtend invExt WHERE task.srcInventoryId = invExt.inventory.id " +
					"AND task.originalBillCode=:billCode AND (invExt.pallet=:code OR invExt.carton=:code OR invExt.serialNo=:code) AND task.status in (:statuss) AND task.fromLocationId=:locId"
					,new String[]{"billCode" , "code" , "statuss" , "locId"}
					,new Object[]{task.getOriginalBillCode() , moveTaskDTO.getBarCode() , 
							Arrays.asList(new String[]{WmsTaskStatus.DISPATCHED , WmsTaskStatus.WORKING}),task.getFromLocationId()});
			for(WmsTask t : tasks) {
				workDocManager.singleWorkConfirm(t, toLocation.getId(), t.getFromLocationId(), t.getUnmovedQuantityBU(), worker.getId());
				workflowManager.doWorkflow(t, "taskProcess.confirm");
			}
		}else{
			workDocManager.singleWorkConfirm(task, toLocation.getId(), task.getFromLocationId(), moveTaskDTO.getActualMovedQuantityBU(), worker.getId());
			workflowManager.doWorkflow(task, "taskProcess.confirm");
		}
	}
	
	//获取散货库存
	private List<WmsInventoryExtend> getWmsInventoryExtend(String locationCode, String barCode){
		String hql = "FROM WmsInventoryExtend wie " +
				" WHERE wie.locationCode = :locCode " +
				" AND wie.item.barCode = :barCode " +
				" AND wie.pallet=:pallet AND wie.carton=:carton AND wie.serialNo=:serialNo " +
				" AND wie.quantityBU > wie.allocatedQuantityBU " +
				" ORDER BY wie.inventory.itemKey.lotInfo.productDate ";
		List<WmsInventoryExtend> wieList = commonDao.findByQuery(hql, new String[]{"locCode", "barCode", "pallet", "carton", "serialNo"}, 
				new Object[]{locationCode, barCode, BaseStatus.NULLVALUE, BaseStatus.NULLVALUE, BaseStatus.NULLVALUE});
		return wieList;
	}
	
	//任意获取一条库存
	private WmsInventoryExtend getPalletCartonInventoryExtend(String locationCode , String barCode) {
		return (WmsInventoryExtend)commonDao.findByQueryMaxNum(
				"FROM WmsInventoryExtend wie WHERE wie.locationCode=:locCode " +
				" AND (wie.pallet=:code OR wie.carton=:code OR wie.serialNo=:code) AND wie.quantityBU > wie.allocatedQuantityBU"
			, new String[]{"locCode","code"}, new Object[]{locationCode,barCode}, 1, 1);
	}
	
	public void confirmMoveTask(List<WmsMoveTaskDTO> moveTaskDTOs,
			String toLocationCode) {
		WmsLocation toLocation = queryWmsLocationByCode(toLocationCode);
		for(WmsMoveTaskDTO moveTaskDTO : moveTaskDTOs) {
			//整托、整箱、序列化移位
			if(moveTaskDTO.getActualMovedQuantityBU() <= 0) {
				WmsInventoryExtend inventoryExtend = getPalletCartonInventoryExtend(moveTaskDTO.getFromLocationCode(),moveTaskDTO.getBarCode());
				if(inventoryExtend == null) {
					throw new BusinessException("库存不足，不能移位");
				}
				//手工移位确认自动完成该托盘、箱的整体移位
				wmsInventoryManager.inventoryManualMove(inventoryExtend, toLocation.getId(), inventoryExtend.getAvailableQuantityBU(), WmsWorkerHolder.getWmsWorker().getId());
			}else{
				//散货移位,考虑散货的生产日期FIFO移动库存
//				List<WmsInventoryExtend> inventoryExtends = getItemInventoryExtends(moveTaskDTO.getFromLocationCode(),moveTaskDTO.getBarCode());
//				Double totalMoveQty = moveTaskDTO.getActualMovedQuantityBU();
//				for(WmsInventoryExtend inventoryExtend : inventoryExtends) {
//					Double moveQty = .0;
//					if(inventoryExtend.getAvailableQuantityBU() > totalMoveQty){
//						moveQty = totalMoveQty;
//						totalMoveQty = .0;
//					}else{
//						moveQty = inventoryExtend.getAvailableQuantityBU();
//						totalMoveQty -= moveQty;
//					}
//					wmsInventoryManager.inventoryManualMove(inventoryExtend, toLocation.getId(), moveQty, WmsWorkerHolder.getWmsWorker().getId());
//					if(totalMoveQty <= 0){
//						break;
//					}
//				}
//				if(totalMoveQty > 0) {
//					throw new BusinessException("待移位数量大于库存数量");
//				}
			}
		}
	}
	
	public Map<String, Object> checkLocationInventoryValid(String locationCode, String barCode) {
		WmsLocation srcLocation = this.queryWmsLocationByCode(locationCode);
		if(srcLocation == null) {
			throw new BusinessException("库位非法");
		}
		String hql = "select inv.itemKey.item.code, inv.itemKey.item.name, sum(inv.quantityBU - inv.allocatedQuantityBU) from WmsInventory inv " +
				" where inv.itemKey.item.barCode = :barCode " +
				" and inv.location.id = :locationId " +
				" and (inv.quantityBU - inv.allocatedQuantityBU) > 0" +
				" GROUP BY inv.itemKey.item.code, inv.itemKey.item.name";
		Object obj = commonDao.findByQueryUniqueResult(hql, 
				new String[]{"barCode", "locationId"}, new Object[]{barCode, srcLocation.getId()});
		if (obj != null) {
			Object[] objs = (Object[])obj;
			Double availableQuantityBU = new Double(objs[2].toString());
			if (availableQuantityBU <= 0) {
				throw new BusinessException("在库位：" + locationCode + ",货品：" + objs[1].toString() + "可用库存不够");
			}
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("item", objs[0].toString() + "-" + objs[1].toString());
			result.put("availableQuantityBU", availableQuantityBU);
			
			return result;
		} else {
			throw new BusinessException("当前库位不存在此货品条码的库存");
		}
	}
	
	public WmsLocation queryWmsLocationByCode(String locationCode) {
		String hql = "FROM WmsLocation loc WHERE loc.code=:code AND loc.warehouse.id=:whId";
		Object obj = commonDao.findByQueryUniqueResult(hql, new String[]{"code","whId"}, 
				new Object[]{locationCode , WmsWarehouseHolder.getWmsWarehouse().getId()});
		if (obj == null) {
			return null;
		}
		return (WmsLocation)obj;
	}
	
	public Boolean validateLocation(String locationCode) {
		String locHql = "FROM WmsLocation loc where loc.code = :code and loc.warehouse.id = :warehouseId";
		Object locObj = this.commonDao.findByQueryUniqueResult(locHql, new String[]{"code", "warehouseId"}, 
				new Object[]{locationCode, WmsWarehouseHolder.getWmsWarehouse().getId()});
		if (locObj == null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	//验证托盘标签
	public Boolean validatePalletSign(String pallet) {
		String locHql = "FROM WmsInventoryExtend wie where wie.pallet = :pallet and wie.inventory.location.warehouse.id = :warehouseId";
		List<Object> locObjs = this.commonDao.findByQuery(locHql, new String[]{"pallet", "warehouseId"}, 
				new Object[]{pallet, WmsWarehouseHolder.getWmsWarehouse().getId()});
		if (0 == locObjs.size()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	public void markExceptionWmsLocation(Long locationId) throws BusinessException {
		try {
			WmsLocation loc = commonDao.load(WmsLocation.class, locationId);
			loc.setExceptionFlag(Boolean.TRUE);
		} catch (BusinessException be) {
			throw new BusinessException("标识异常库位失败，请重试");
		}
	}
	
	public void resetAllocate(WmsMoveTaskDTO dto) {
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, dto.getMoveDocId());
		WmsTask task = commonDao.load(WmsTask.class, dto.getTaskId());
		WmsMoveDocDetail moveDocDetail = commonDao.load(WmsMoveDocDetail.class, task.getMoveDocDetail().getId());
		WmsBillType billType =commonDao.load(WmsBillType.class, moveDoc.getBillType().getId());
		WmsOrganization company = commonDao.load(WmsOrganization.class, moveDoc.getCompany().getId());
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class, moveDoc.getWarehouse().getId());				
		String companyName = company.getName();
		if(company.isBeVirtual()){
			companyName = warehouse.getName();
		}
		String warehouseName = warehouse.getName();
		Long warehouseId = warehouse.getId();
		
		Map<String,Object> problem = new HashMap<String, Object>();
		double planQuantity = 0.0;
		String locationType = "";
		if (moveDocDetail.getBePallet()) {
			planQuantity = 1;
			locationType = "是";
		} else {
			planQuantity = PackageUtils.convertPackQuantity(task.getUnmovedQuantityBU(), task.getPackageUnit());
			locationType = "否";
		}

		WmsLocation fromLocation = commonDao.load(WmsLocation.class, task.getFromLocationId());
		WmsItemKey itemKey = commonDao.load(WmsItemKey.class, task.getItemKey().getId());
		WmsItem item = commonDao.load(WmsItem.class, itemKey.getItem().getId());			
		WmsPackageUnit packageUnit = commonDao.load(WmsPackageUnit.class, task.getPackageUnit().getId());
		
		//每托数量BU 和规则里一致
		Double palletQuantity = moveDocDetail.getPlanQuantity() * packageUnit.getConvertFigure();
		
		problem.put("仓库ID", warehouseId);					
		problem.put("所属仓库", warehouseName);					
		problem.put("货主", companyName);
		problem.put("单据类型", billType.getName());			
		problem.put("是否托盘", locationType);
		problem.put("货品状态", task.getInventoryStatus());
		problem.put("计划移位数量", planQuantity);
		problem.put("每托数量", palletQuantity);	
		problem.put("批次序号", task.getItemKey().getId());
		problem.put("货品代码", item.getCode());
		problem.put("货品序号", task.getItemKey().getItem().getId());	
		problem.put("货品分类", task.getItemKey().getItem().getClass1());	
		problem.put("包装单位序号", packageUnit.getId());						
		problem.put("货品重量", packageUnit.getWeight());	
		problem.put("货品体积", packageUnit.getVolume());
		problem.put("包装级别", packageUnit.getLevel());
		problem.put("折算系数", packageUnit.getConvertFigure());
		problem.put("月台区号", fromLocation.getZone());			
		problem.put("移出库位ID", fromLocation.getId());			
		problem.put("库区序号", fromLocation.getWarehouseArea().getId());	
		problem.put("越库相关单号", "");
		problem.put("越库已分配数量", 0);
		
		Map<String, Object> result = null;
		try {
			result = wmsRuleManager.execute(warehouseName, companyName, "上架分配规则", problem);
		} catch (Exception ex) {
			logger.error("", ex);
			return;
		}
		
		//作业任务取消分配
		// 如果是托盘明细，取消分配时要扣减库位托盘占用数---一个task一个托盘
		if (moveDocDetail.getBePallet()) {
			WmsLocation location = load(WmsLocation.class,task.getToLocationId());
			location.removePallet(1);
			commonDao.store(location);
		}
		Double unallocateQty = task.getUnmovedQuantityBU();
		WmsInventory inv = commonDao.load(WmsInventory.class,task.getDescInventoryId());
		inv.unallocatePutaway(unallocateQty);
		wmsInventoryManager.refreshLocationUseRate(inv.getLocation(), 0);
		task.unallocatePick(unallocateQty);
		if(task.getStatus().equals(WmsTaskStatus.WORKING) && task.getMovedQuantityBU()>0) {
			workflowManager.doWorkflow(task, "taskProcess.confirm");
		}
		
		int size = ((List<Map<String, Object>>) result.get("返回列表")).size();
		for (int i = 0; i < size; i++) {
			Map<String, Object> wmsTaskInfos = ((List<Map<String, Object>>) result.get("返回列表")).get(i);
			double quantity = Double.valueOf(String.valueOf(wmsTaskInfos.get("上架数量")));
			if (quantity <= 0) {
				continue;
			}
			
			double quantityBU;
			WmsLocation toLoc = commonDao.load(WmsLocation.class,(Long) wmsTaskInfos.get("库位序号"));

			quantityBU = quantity * packageUnit.getConvertFigure();
			double unAllocateQtyBU = moveDocDetail.getUnAllocateQuantityBU();
			double allocateQuantityBU = (unAllocateQtyBU >= quantityBU) ? quantityBU : unAllocateQtyBU;
			
			//托盘上架
			if(moveDocDetail.getBePallet()) {
				quantity--;
				if (i == 0 && task.getMovedQuantityBU() == 0) {
					putawayAllocate(task, toLoc,moveDocDetail.getPlanQuantityBU(),moveDocDetail,false);
				} else {
					putawayAllocate(task, toLoc,moveDocDetail.getPlanQuantityBU(),moveDocDetail,true);
				}
			} else {
				if(allocateQuantityBU<=0) {
					break;
				}
				if (i == 0 && task.getMovedQuantityBU() == 0) {
					putawayAllocate(task, toLoc,allocateQuantityBU,moveDocDetail,false);
				} else {
					putawayAllocate(task, toLoc,allocateQuantityBU,moveDocDetail,true);
				}
			}
			
			
		}
	}
	
	private void putawayAllocate(WmsTask task, WmsLocation toLoc, double quantityBU, 
			WmsMoveDocDetail detail, boolean isNew) {
		WmsLocation fromLoc = commonDao.load(WmsLocation.class, detail.getFromLocationId());
		WmsInventory dstInventory = wmsInventoryManager.getInventoryWithNew( toLoc, detail.getItemKey(), detail.getPackageUnit(),
				detail.getInventoryStatus());
		dstInventory.allocatePutaway(quantityBU);
		
		commonDao.store(dstInventory);
		// 如果是托盘上架，还要更新库位上的托盘占用数，用于计算托盘库位满度(每条明细就是一个托盘)
		if (detail.getBePallet()
				&& WmsLocationType.STORAGE.equals(toLoc.getType())) {
			toLoc.addPallet(1);
		}
		// 调用规则刷新库满度
		wmsInventoryManager.refreshLocationUseRate(toLoc, 0);
		commonDao.store(toLoc);

		if(isNew) {
			WmsTask newTask = EntityFactory.getEntity(WmsTask.class);
			BeanUtils.copyEntity(newTask, task);
			newTask.setId(null);
			newTask.setPlanQuantityBU(quantityBU);
			newTask.setPlanQuantity(PackageUtils.convertPackQuantity(quantityBU,task.getPackageUnit()));
			newTask.setMovedQuantityBU(0D);
			newTask.setTiredMovedQuantityBU(quantityBU);
			newTask.setStatus(WmsTaskStatus.DISPATCHED);
			newTask.setToLocationId(dstInventory.getLocation().getId());
			newTask.setToLocationCode(dstInventory.getLocation().getCode());
			newTask.setDescInventoryId(dstInventory.getId());
			newTask.calculateLoad();
			detail.getTasks().add(newTask);
			newTask.getWorkDoc().addTask(newTask);
			commonDao.store(newTask);
		} else {
			task.addPlanQuantityBU(quantityBU);
			task.setToLocationId(dstInventory.getLocation().getId());
			task.setToLocationCode(dstInventory.getLocation().getCode());
			task.setDescInventoryId(dstInventory.getId());
			task.getWorkDoc().addTask(task);
			commonDao.store(task);
		}
		// 更新上架单及明细数量
		detail.allocate(quantityBU);
		commonDao.store(detail);
	}
	
	private WmsMoveDocDetail createMoveDocDetail(WmsMoveDoc moveDoc,
			WmsItemKey itemKey, WmsPackageUnit packageUnit,
			Double planQuantity, Double planQuantityBU, Long fromLocationId,
			String fromLocationCode, String pallet, String invStatus,
			Long inventoryId, WmsInventoryExtend wsn) {
		WmsMoveDocDetail wmd = EntityFactory.getEntity(WmsMoveDocDetail.class);
		wmd.setMoveDoc(moveDoc);
		wmd.setItemKey(itemKey);
		wmd.setItem(itemKey.getItem());
		wmd.setPackageUnit(packageUnit);
		wmd.setPlanQuantity(planQuantity);
		wmd.setPlanQuantityBU(planQuantityBU);
		wmd.setPallet(pallet);
		// 托盘整托移位时是托盘移位，否则是散货移位
		if (!WmsStringUtils.isEmpty(pallet)) {
			wmd.setBePallet(true);
		} else {
			wmd.setBePallet(false);
		}
		wmd.setFromLocationId(fromLocationId);
		wmd.setFromLocationCode(fromLocationCode);
		wmd.setInventoryStatus(invStatus);
		wmd.setInventoryId(inventoryId);
		wmd.setCarton(wsn.getCarton());
		commonDao.store(wmd);
		return wmd;
	}
	
	/**
	 * 虚拟移位计划自动分配，不产生Task
	 */
	public void autoAllocateVirtual(WmsMoveTaskDTO dto) {
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, dto.getMoveDocId());
		WmsMoveDocDetail mdd = commonDao.load(WmsMoveDocDetail.class, dto.getMoveDocDetailId());
		
		WmsBillType billType = commonDao.load(WmsBillType.class, moveDoc.getBillType().getId());;
		WmsOrganization company = commonDao.load(WmsOrganization.class, moveDoc.getCompany().getId());
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class, moveDoc.getWarehouse().getId());				
		String companyName = company.getName();
		if(company.isBeVirtual()){
			companyName = warehouse.getName();
		}
		String warehouseName = warehouse.getName();
		Long warehouseId = warehouse.getId();
		
		WmsLocation fromLocation = commonDao.load(WmsLocation.class, mdd.getFromLocationId());
		WmsItemKey itemKey = commonDao.load(WmsItemKey.class, mdd.getItemKey().getId());
		WmsItem item = commonDao.load(WmsItem.class, itemKey.getItem().getId());
		WmsPackageUnit packageUnit = commonDao.load(WmsPackageUnit.class, mdd.getPackageUnit().getId());
		
		Map<String,Object> problem = new HashMap<String, Object>();
		double planQuantity = 0.0;
		String locationType = "";
		if (mdd.getBePallet()) {
			planQuantity = 1;
			locationType = "是";
		} else {
			planQuantity = PackageUtils.convertPackQuantity(mdd.getUnAllocateQuantityBU(), packageUnit);
			locationType = "否";
		}
		
		//每托数量BU 和规则里一致
		problem.put("仓库ID", warehouseId);					
		problem.put("所属仓库", warehouseName);					
		problem.put("货主", companyName);
		problem.put("单据类型", billType.getName());			
		problem.put("是否托盘", locationType);
		problem.put("货品状态", mdd.getInventoryStatus());
		problem.put("计划移位数量", planQuantity);
		problem.put("每托数量", planQuantity);	
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
		problem.put("越库相关单号", "");
		problem.put("越库已分配数量", 0);
		
		try {
			Map<String, Object> result = wmsRuleManager.execute(warehouseName, companyName, "上架分配规则", problem);
			doAutoAllocateVirtual(result, moveDoc, mdd, itemKey, packageUnit);
		} catch (Exception ebe) {
			logger.error("", ebe);
		}
	}
	
	public void doAutoAllocateVirtual(Map<String, Object> result, WmsMoveDoc moveDoc, WmsMoveDocDetail mdd, WmsItemKey itemKey, WmsPackageUnit packageUnit) {
		int size = ((List<Map<String, Object>>) result.get("返回列表")).size();
		double tempQuantityBU = mdd.getUnAllocateQuantityBU();
		for (int i = 0; i < size; i++) {
			Map<String, Object> resultMap = ((List<Map<String, Object>>) result.get("返回列表")).get(i);
			double planPutawayQuantity = Double.valueOf(String.valueOf(resultMap.get("上架数量")));
			if (planPutawayQuantity <= 0) {
				continue;
			}
			WmsLocation toLocation = commonDao.load(WmsLocation.class, (Long) resultMap.get("库位序号"));
			
			double planPutawayQuantityBU = planPutawayQuantity * packageUnit.getConvertFigure();
			double unAllocateQtyBU = mdd.getUnAllocateQuantityBU();
			
			double allocateQuantityBU = (unAllocateQtyBU >= planPutawayQuantityBU) ? planPutawayQuantityBU : unAllocateQtyBU;
			tempQuantityBU -= allocateQuantityBU;
			// 更新上架单及明细数量
			if (mdd.getUnAllocateQuantityBU() != 0) {
				mdd.setToLocationId(toLocation.getId());
				mdd.setToLocationCode(toLocation.getCode());
				mdd.allocate(allocateQuantityBU);
				if (mdd.getUnAllocateQuantityBU() > 0) {
					mdd.setPlanQuantityBU(mdd.getAllocatedQuantityBU());
					mdd.setPlanQuantity(PackageUtils.convertPackQuantity(mdd.getAllocatedQuantityBU(), packageUnit));
				}
				
				WmsInventory dstInventory = wmsInventoryManager.getInventoryWithNew(
						toLocation, itemKey, packageUnit, mdd.getInventoryStatus());
				dstInventory.allocatePutaway(allocateQuantityBU);
				commonDao.store(dstInventory);
				mdd.setDestInventoryId(dstInventory.getId());
				commonDao.store(mdd);
				
				// 调用规则刷新库满度
				wmsInventoryManager.refreshLocationUseRate(toLocation, 0);
				commonDao.store(toLocation);
				
			} else {
				WmsMoveDocDetail newMdd = EntityFactory.getEntity(WmsMoveDocDetail.class);
				BeanUtils.copyEntity(newMdd, mdd);
				newMdd.setId(null);
				newMdd.setPlanQuantity(0D);
				newMdd.setPlanQuantityBU(0D);
				newMdd.setAllocatedQuantityBU(0D);
				moveDoc.addDetail(newMdd);
				commonDao.store(newMdd);
				
				newMdd.setToLocationId(toLocation.getId());
				newMdd.setToLocationCode(toLocation.getCode());
				newMdd.setPlanQuantityBU(allocateQuantityBU);
				newMdd.setPlanQuantity(PackageUtils.convertPackQuantity(allocateQuantityBU, packageUnit));
				newMdd.allocate(allocateQuantityBU);
				commonDao.store(newMdd);
			}
		}
		
		// 对于部分分配的明细，进行拆分
		if (tempQuantityBU > 0) {
			WmsMoveDocDetail newMdd = EntityFactory.getEntity(WmsMoveDocDetail.class);
			BeanUtils.copyEntity(newMdd, mdd);
			newMdd.setId(null);
			newMdd.setPlanQuantity(0D);
			newMdd.setPlanQuantityBU(0D);
			newMdd.setAllocatedQuantityBU(0D);
			moveDoc.addDetail(newMdd);
			commonDao.store(newMdd);
			
			newMdd.setPlanQuantityBU(tempQuantityBU);
			newMdd.setPlanQuantity(PackageUtils.convertPackQuantity(tempQuantityBU, packageUnit));
			commonDao.store(newMdd);
		}
	}
	
	/**
	 * 非计划手工托盘移位自动分配
	 */
	public void autoAllocateByPallet(WmsMoveTaskDTO dto) {
		WmsLocation fromLocation = commonDao.load(WmsLocation.class, dto.getFromLocationId());
		WmsItemKey itemKey = commonDao.load(WmsItemKey.class, dto.getItemKeyId());
		WmsItem item = commonDao.load(WmsItem.class, dto.getItemId());
		WmsPackageUnit packageUnit = commonDao.load(WmsPackageUnit.class, dto.getPackageUnitId());
		
		Map<String,Object> problem = new HashMap<String, Object>();
		double planQuantity = 1;
		String locationType = "是";
		
		WmsOrganization company = getCompanyByWarehouse(WmsWarehouseHolder.getWmsWarehouse());
		String companyName = company.getName();
		if(company.isBeVirtual()){
			companyName = WmsWarehouseHolder.getWmsWarehouse().getName();
		}
		String query = "FROM WmsBillType bill WHERE bill.company.id = :companyId AND bill.code = :code";
		WmsBillType billType = (WmsBillType) commonDao.findByQueryUniqueResult(
				query, new String[] { "companyId", "code" }, new Object[] {company.getId(), WmsMoveDocType.MV_MOVE });
		
		//每托数量BU 和规则里一致
		problem.put("仓库ID", WmsWarehouseHolder.getWmsWarehouse().getId());					
		problem.put("所属仓库", WmsWarehouseHolder.getWmsWarehouse().getName());					
		problem.put("货主", companyName);
		problem.put("单据类型", billType.getName());			
		problem.put("是否托盘", locationType);
		problem.put("货品状态", dto.getSrcInventoryStatus());
		problem.put("计划移位数量", planQuantity);
		problem.put("每托数量", dto.getUnMoveQuantityBU());
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
		problem.put("越库相关单号", "");
		problem.put("越库已分配数量", 0);
		
		Map<String, Object> result = null;
		try {
			result = wmsRuleManager.execute(WmsWarehouseHolder.getWmsWarehouse().getName(), companyName, "上架分配规则", problem);
		} catch (Exception ex) {
			logger.error("", ex);
			return;
		}
		
		// 处理分配结果
		int size = ((List<Map<String, Object>>) result.get("返回列表")).size();
		for (int i = 0; i < size; i++) {
			Map<String, Object> resultMap = ((List<Map<String, Object>>) result.get("返回列表")).get(i);
			double planPutawayQuantity = Double.valueOf(String.valueOf(resultMap.get("上架数量")));
			if (planPutawayQuantity <= 0) {
				continue;
			}
			WmsLocation toLocation = commonDao.load(WmsLocation.class, (Long) resultMap.get("库位序号"));
			dto.setToLocationId(toLocation.getId());
			dto.setToLocationCode(toLocation.getCode());
		}
	}
	public List<Object[]> findTaskByMoveCode(String moveCode){
		String hql = "SELECT task.id,task.fromLocationCode,task.itemKey.item.code,"
				+ "task.planQuantityBU-task.movedQuantityBU AS unMoveQty,task.toLocationCode"
				+ " FROM WmsTask task WHERE 1=1 AND (task.planQuantityBU-task.movedQuantityBU) > 0"
				+ " AND task.moveDocDetail.moveDoc.code =:moveCode"
				+ " ORDER BY task.toLocationCode";
		List<Object[]> tasks = commonDao.findByQuery(hql, 
				new String[]{"moveCode"}, new Object[]{moveCode});
		return tasks;
	}
	public List<Object[]> findUnFinshMove(){
		String hql = "SELECT moveDoc.code,moveDoc.status,moveDoc.id "
				+ "FROM WmsMoveDoc moveDoc WHERE (moveDoc.status =:status1 OR moveDoc.status =:status2)"
				+ " AND moveDoc.worker.id =:workerId AND moveDoc.type =:type";
		List<Object[]> moveDocs = commonDao.findByQuery(hql, 
				new String[]{"status1","status2","workerId","type"}, 
				new Object[]{WmsMoveDocStatus.ACTIVE,WmsMoveDocStatus.WORKING,
				WmsWorkerHolder.getWmsWorker().getId(),WmsMoveDocType.MV_MOVE});
		return moveDocs;
	}
	public void singleConfirm(Object[] obj){
		Long moveId = Long.parseLong(obj[0].toString());
		WmsTask task = commonDao.load(WmsTask.class, Long.parseLong(obj[2].toString()));
		Double unMoveQty = Double.valueOf(obj[3].toString());
		workDocManager.singleConfirm(task, moveId, unMoveQty, null, WmsWorkerHolder.getWmsWorker().getId());
	}
	public String pickConfirmAll(String moveCode){
		String hql = "SELECT moveDoc.id"
				+ " FROM WmsMoveDoc moveDoc WHERE (moveDoc.status =:status1 OR moveDoc.status =:status2)"
				+ " AND moveDoc.code =:moveCode AND moveDoc.type =:type";
		List<Long> moveDocs = commonDao.findByQuery(hql, 
				new String[]{"status1","status2","moveCode","type"}, 
				new Object[]{WmsMoveDocStatus.ACTIVE,WmsMoveDocStatus.WORKING,
				moveCode,WmsMoveDocType.MV_MOVE});
		if(moveDocs!=null && moveDocs.size()>0){
			WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, moveDocs.get(0));
			workDocManager.pickConfirmAll(moveDoc);
		}else{
			return "NULL";
		}
		return "END";
	}
	public List<Map<String, Object>> findRuleTableBHKW(){
		List<Map<String, Object>> details = wmsRuleManager.getAllRuleTableDetail(WmsWarehouseHolder.getWmsWarehouse().getName(),
				"R101_备货库位定位表",WmsWarehouseHolder.getWmsWarehouse().getName());
		return details;
	}
	public Object[] findLocBHKW(List<Map<String, Object>> details,String blCode
			,List<Map<String, Object>> dds){
		Integer isWorng = 0;//未找到
		if(details!=null && details.size()>0){
			for(Map<String, Object> dd : details){
				//{工艺状态=-, 货主=发动机, 货品代码=1023060GAZC, 补货上限=180.0, 供应商=L21016, 库位=A-03010101, 备料工工号=test}
				if(dd.get("库位").equals(blCode)){
					if(dds.contains(dd)){
						return new Object[]{//重复
								isWorng=2,dds
						};
					}
					dds.add(dd);
					isWorng = 1;//找到
					break;
				}
			}
		}
		return new Object[]{
				isWorng,dds
		};
	}
	public String initWmsReplenishMove(List<Map<String, Object>> dds){
		//WmsMoveDoc-按照'备料工工号'分组
		Map<String,List<Map<String, Object>>> blMove = new HashMap<String, List<Map<String, Object>>>();
		List<Map<String, Object>> blgs = null;
		
		WmsWarehouse warehouse = WmsWarehouseHolder.getWmsWarehouse();
		String key = null,companyName = null,itemCode = null,locationCode = null;
		Map<String,WmsOrganization> companys = new HashMap<String, WmsOrganization>();
		WmsOrganization company  = null;
		Map<String,WmsWorker> workers = new HashMap<String, WmsWorker>();
		WmsWorker worker = null;
		Map<String,WmsItem> items = new HashMap<String, WmsItem>();
		WmsItem item = null;
		Map<String,WmsPackageUnit> pus = new HashMap<String, WmsPackageUnit>();
		WmsPackageUnit basePackageUnit = null;
		Map<String,WmsLocation> locs = new HashMap<String, WmsLocation>();
		WmsLocation loc = null;
		for(Map<String, Object> dd : dds){
			//{工艺状态=-, 货主=发动机, 货品代码=1023060GAZC, 补货上限=180.0, 供应商=L21016, 库位=A-03010101, 备料工工号=test}
			key = dd.get("备料工工号").toString().trim();
			companyName = dd.get("货主").toString().trim();
			itemCode = dd.get("货品代码").toString().trim();
			locationCode = dd.get("库位").toString().trim();
			if(blMove.containsKey(key)){
				blgs = blMove.get(key);
			}else{
				blgs = new ArrayList<Map<String,Object>>();
			}
			blgs.add(dd);
			blMove.put(key, blgs);
			if(!companys.containsKey(companyName)){
				company  = (WmsOrganization) commonDao.findByQueryUniqueResult("FROM WmsOrganization o WHERE o.name =:name"
						+ " AND o.beCompany = true", 
						new String[]{"name"}, new Object[]{companyName});
				if(company==null){
					return "失败!货主["+companyName+"]不存在";
				}
				companys.put(companyName, company);
			}
			if(!workers.containsKey(key)){
				worker = (WmsWorker) commonDao.findByQueryUniqueResult("FROM WmsWorker w WHERE w.code =:code"
						+ " AND w.warehouse.name =:warehouseName", 
						new String[]{"code","warehouseName"}, new Object[]{key,warehouse.getName()});
				if(worker==null){
					return "失败!备料工["+key+"]不存在";
				}
				workers.put(key, worker);
			}
			if(!items.containsKey(itemCode)){
				item = (WmsItem) commonDao.findByQueryUniqueResult("FROM WmsItem i WHERE i.code =:code"
						+ " AND i.company.name =:companyName", 
						new String[]{"code","companyName"}, new Object[]{itemCode,companyName});
				if(item==null){
					return "失败!物料["+itemCode+"]不存在";
				}
				items.put(itemCode, item);
			}
			if(!pus.containsKey(itemCode)){
				basePackageUnit = (WmsPackageUnit) commonDao.findByQueryUniqueResult("FROM WmsPackageUnit pu"
						+ " WHERE 1=1 AND pu.item.id =:itemId AND pu.lineNo = 1", 
						new String[]{"itemId"}, new Object[]{item.getId()});
				if(basePackageUnit==null){
					return "失败!物料基本单位["+itemCode+"]不存在";
				}
				pus.put(itemCode, basePackageUnit);
			}
			if(!locs.containsKey(locationCode)){
				loc = (WmsLocation) commonDao.findByQueryUniqueResult("FROM WmsLocation l WHERE l.code =:code"
						+ " AND l.warehouse.name =:warehouseName", 
						new String[]{"code","warehouseName"}, new Object[]{locationCode,warehouse.getName()});
				if(loc==null){
					return "失败!库位["+locationCode+"]不存在";
				}
				locs.put(locationCode, loc);
			}
			
		}
		String error = "操作完成,输入'qq'退出登录,扫描继续补料",extendPropC1=null,supplier=null;
		dds.clear();
		Iterator<Entry<String, List<Map<String, Object>>>> ii = blMove.entrySet().iterator();
		while(ii.hasNext()){
			Entry<String, List<Map<String, Object>>> entry = ii.next();
			//key=备料工工号,value=[{工艺状态=-, 货主=发动机, 货品代码=1023060GAZC, 补货上限=180.0, 供应商=L21016, 库位=A-03010101, 备料工工号=test},....]
			WmsMoveDoc moveDoc = newMoveDoc(company,workers.get(entry.getKey()),warehouse,WmsMoveDocType.MV_REPLENISHMENT_MOVE);
			dds = entry.getValue();
			for(Map<String, Object> dd : dds){
				supplier = dd.get("供应商").toString().trim();
				itemCode = dd.get("货品代码").toString().trim();
				locationCode = dd.get("库位").toString().trim();
				extendPropC1 = dd.get("工艺状态").toString().trim();
				item = items.get(itemCode);
				basePackageUnit = pus.get(itemCode);
				loc = locs.get(locationCode);
				WmsMoveDocDetail moveDocDetial = newMoveDetail(moveDoc, item, supplier, extendPropC1, 
						basePackageUnit, loc.getCode(), loc.getId());
				moveDoc.addDetail(moveDocDetial);
			}
			commonDao.store(moveDoc);
		}
		return error;
	}
	/**extendPropC1-工艺状态*/
	private WmsMoveDocDetail newMoveDetail(WmsMoveDoc moveDoc,WmsItem item,String supplier,String extendPropC1
			,WmsPackageUnit basePackageUnit,String toLocationCode,Long toLocationId){
		WmsMoveDocDetail moveDocDetial = EntityFactory.getEntity(WmsMoveDocDetail.class);
		moveDocDetial.setMoveDoc(moveDoc);
		moveDocDetial.setItem(item);
		moveDocDetial.setPackageUnit(basePackageUnit);
		moveDocDetial.setPlanQuantityBU(0D);
		moveDocDetial.setPlanQuantity(0D);
		ShipLotInfo shipLotInfo = new ShipLotInfo();
		shipLotInfo.setSupplier(supplier);
		shipLotInfo.setExtendPropC1(extendPropC1);
		moveDocDetial.setShipLotInfo(shipLotInfo);
		moveDocDetial.setToLocationCode(toLocationCode);
		moveDocDetial.setToLocationId(toLocationId);
		this.commonDao.store(moveDocDetial);
		return moveDocDetial;
	}
	private WmsMoveDoc newMoveDoc(WmsOrganization company,WmsWorker worker,WmsWarehouse warehouse,String moveType){
		WmsMoveDoc moveDoc = EntityFactory.getEntity(WmsMoveDoc.class);
		moveDoc.setType(moveType);//WmsMoveDocType.MV_REPLENISHMENT_MOVE
		moveDoc.setWarehouse(warehouse);
		moveDoc.setWorker(worker);
		if(company == null){
			moveDoc.setCompany(this.getCompanyByWarehouse(warehouse));
		}else{
			moveDoc.setCompany(company);
		}
		
		WmsBillType billType = wmsBillTypeManager.getWmsBillType(moveDoc.getCompany(), TypeOfBill.MOVE, moveDoc.getType());
		moveDoc.setBillType(billType);
		if (moveDoc.isNew()) {
			String code = wmsBussinessCodeManager.generateCodeByRule(moveDoc.getWarehouse(), 
					moveDoc.getWarehouse().getName(), "补货单", billType.getName());
			moveDoc.setCode(code);
		}
		this.commonDao.store(moveDoc);
		return moveDoc;
	}
	public List<Object[]> findUnFinshReplenishMove(){
		String hql = "SELECT moveDoc.code,moveDoc.status,moveDoc.id,moveDoc.updateInfo.creator "
				+ "FROM WmsMoveDoc moveDoc WHERE (moveDoc.status =:status1 OR moveDoc.status =:status2 OR moveDoc.status =:status3)"
				+ " AND moveDoc.worker.code =:workerCode AND moveDoc.type =:type";
		List<Object[]> moveDocs = commonDao.findByQuery(hql, 
				new String[]{"status1","status2","status3","workerCode","type"}, 
				new Object[]{WmsMoveDocStatus.OPEN,WmsMoveDocStatus.PARTALLOCATED,WmsMoveDocStatus.ALLOCATED,
				WmsWorkerHolder.getWmsWorker().getCode(),WmsMoveDocType.MV_REPLENISHMENT_MOVE});
		return moveDocs;
	}
	public List<Object[]> findUnActiveReplenishMove(){
		String hql = "SELECT moveDoc.code,moveDoc.status,moveDoc.id,moveDoc.updateInfo.creator "
				+ "FROM WmsMoveDoc moveDoc WHERE (moveDoc.status =:status1 OR moveDoc.status =:status2 OR moveDoc.status =:status3)"
				+ " AND moveDoc.worker.id =:workerId AND moveDoc.type =:type";
		List<Object[]> moveDocs = commonDao.findByQuery(hql, 
				new String[]{"status1","status2","status3","workerId","type"}, 
				new Object[]{WmsMoveDocStatus.ALLOCATED,WmsMoveDocStatus.ACTIVE,WmsMoveDocStatus.WORKING,
				WmsWorkerHolder.getWmsWorker().getId(),WmsMoveDocType.MV_REPLENISHMENT_MOVE});
		return moveDocs;
	}
	public List<Object[]> findMoveDetails(String moveCode){
		String hql = "SELECT dd.id,dd.item.id,dd.item.code,dd.shipLotInfo.extendPropC1,"
				+ "dd.shipLotInfo.supplier,dd.toLocationId,dd.toLocationCode"
				+ " FROM WmsMoveDocDetail dd WHERE 1=1 AND dd.moveDoc.code =:moveCode"
				+ " AND dd.bePackageFinish = false"
				+ " ORDER BY dd.shipLotInfo.supplier";
		List<Object[]> dds = commonDao.findByQuery(hql, 
				new String[]{"moveCode"}, new Object[]{moveCode});
		return dds;
	}
	public void setMoveDetailOver(Object[] obj){
		//id,item.id,item.code,extendPropC1,supplier,toLocationId,toLocationCode
		WmsMoveDocDetail moveDocDetail = commonDao.load(WmsMoveDocDetail.class, Long.parseLong(obj[0].toString()));
		moveDocDetail.setBePackageFinish(true);
		commonDao.store(moveDocDetail);
	}
	public List<Object[]> findInventory(Object[] obj){
		//id,item.id,item.code,extendPropC1,supplier,toLocationId,toLocationCode
		String sql = "select ix.ID as col_0_0_,"+
				" ix.LOCATION_CODE as col_1_0_,"+
				" ix.LOCATION_ID as col_2_0_,"+
				" inventory.QUANTITY - inventory.ALLOCATED_QUANTITY_BU as col_3_0_," +
				"(ix.quantity_bu-ix.allocated_quantity_bu) as col_4_0_"+
				" from WMS_INVENTORY_EXTEND ix,"+
				" WMS_INVENTORY        inventory,"+
				" WMS_ITEM_KEY         ik,"+
				" WMS_ORGANIZATION     supper,"+
				" WMS_LOCATION         loc"+
				" where inventory.LOCATION_ID = loc.ID"+
				" and ik.SUPPLIER_ID = supper.ID"+
				" and inventory.ITEM_KEY_ID = ik.ID"+
				" and ix.INVENTORY_ID = inventory.ID"+
				" and inventory.QUANTITY > inventory.ALLOCATED_QUANTITY_BU"+
				" and ix.ITEM_ID = ?"+
				" and ik.EXTEND_PROPC1 = ?"+
				" and supper.CODE = ?"+
				" and inventory.STATUS = ?"+
				" and loc.WAREHOUSE_ID = ?"+
				" and ix.LOCATION_CODE <> ?"+
				" order by ik.PRODUCT_DATE asc";
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().openSession();
		List<Object[]> ix = new ArrayList<Object[]>();
		try {
			SQLQuery query = session.createSQLQuery(sql);
			query.setLong(0, Long.parseLong(obj[1].toString()));//ITEM_ID
			query.setString(1, obj[3].toString());//EXTEND_PROPC1
			query.setString(2, obj[4].toString());//supper
			query.setString(3, BaseStatus.NULLVALUE);//STATUS
			query.setLong(4, WmsWarehouseHolder.getWmsWarehouse().getId());//WAREHOUSE_ID
			query.setString(5, obj[6].toString());//LOCATION_CODE
			query.setMaxResults(1);
			ix = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
		return ix;
	}
	public String subscriberAutoAllocateWmsMoveDoc(Object[] ixs,Double allocateQuantityBU,
			Long ddId){
		//ix.id,locationCode,locationId,availableQty,托盘可用量
		String messge = "0";//正常
		
		//分配
		WmsInventoryExtend ix = commonDao.load(WmsInventoryExtend.class, Long.parseLong(ixs[0].toString()));
		// 改写源库存待拣货数量
		WmsInventory srcInv = commonDao.load(WmsInventory.class,
				ix.getInventory().getId());
		WmsItemKey itemKey = commonDao.load(WmsItemKey.class, srcInv
				.getItemKey().getId());
		srcInv.allocatePickup(allocateQuantityBU);
		commonDao.store(srcInv);

		WmsMoveDocDetail moveDocDetail = commonDao.load(WmsMoveDocDetail.class, ddId);
		moveDocDetail.setInventoryId(srcInv.getId());
		moveDocDetail.setFromLocationId(srcInv.getLocation().getId());
		moveDocDetail.setFromLocationCode(srcInv.getLocation().getCode());
		moveDocDetail.allocate(allocateQuantityBU);
		moveDocDetail.addPlanQuantityBU(allocateQuantityBU);
		moveDocDetail.setRelatedId(ix.getId());//补货明细与子表一对一
		commonDao.store(moveDocDetail);
		wmsTaskManager.createWmsTask(moveDocDetail, itemKey,
				srcInv.getStatus(), allocateQuantityBU);
		WmsMoveDoc moveDoc = moveDocDetail.getMoveDoc();
		moveDoc.setStatus(WmsMoveDocStatus.ALLOCATED);
		moveDoc.refreshQuantity();
		commonDao.store(moveDoc);
//		workflowManager.doWorkflow(moveDocDetail.getMoveDoc(), "wmsMoveDocProcess.allocate"); // 调用分配流程, 判断是否整单分配
		return messge;
	} 
	public void activePickByRf(String moveCode){
		WmsMoveDoc moveDoc = (WmsMoveDoc) commonDao.findByQueryUniqueResult("FROM WmsMoveDoc w WHERE w.code =:code", 
				new String[]{"code"}, new Object[]{moveCode});
		if(moveDoc!=null && moveDoc.getStatus().equals(WmsMoveDocStatus.ALLOCATED)){
			//激活
			workDocManager.activePickByJac(moveDoc);
			moveDoc.setStatus(WmsMoveDocStatus.ACTIVE);
			commonDao.store(moveDoc);
		}
	}
	public List<Object[]> getInventoyExtendByPalletJac(String pallet){
		//wie.id, item.id,item.code,locationCode,availableQuantityBU,extendPropC1,supplier.id,packageUnit.id,location.id
		String hql = "select wie.id, wie.item.id,wie.item.code,wie.locationCode,(wie.inventory.quantityBU - wie.inventory.allocatedQuantityBU) as q1" +
				",wie.inventory.itemKey.lotInfo.extendPropC1,wie.inventory.itemKey.lotInfo.supplier.id,wie.inventory.packageUnit.id" +
				",wie.inventory.location.id,(wie.quantityBU-wie.allocatedQuantityBU) as q2" +
		" from WmsInventoryExtend wie where wie.pallet=:pallet and wie.inventory.location.warehouse.id=:warehouseId" +
		" and wie.inventory.quantityBU > wie.inventory.allocatedQuantityBU";
		List<Object[]> inventoryExtends = commonDao.findByQuery(hql,
				new String[]{"pallet","warehouseId"},
				new Object[]{pallet,WmsWarehouseHolder.getWmsWarehouse().getId()});
		return inventoryExtends;
	}
	public List<Object[]> getInventoyExtendByPallet(String pallet){
		
		String hql = "select wie.id, wie.item.name,wie.item.code,wie.locationCode,(wie.quantityBU - wie.allocatedQuantityBU)  " +
		" from WmsInventoryExtend wie where wie.pallet=:pallet and wie.inventory.location.warehouse.id=:warehouseId";
		List<Object[]> inventoryExtends = commonDao.findByQuery(hql,
				new String[]{"pallet","warehouseId"},
				new Object[]{pallet,WmsWarehouseHolder.getWmsWarehouse().getId()});
		return inventoryExtends;
	}
    public WmsInventoryExtend getWmsInventoryExtend(Long id){
    	return this.commonDao.load(WmsInventoryExtend.class, id);
    }
    public WmsInventory getWmsInventoryByExtendId(Long id ){
    	WmsInventoryExtend wie = this.commonDao.load(WmsInventoryExtend.class, id);
    	return this.commonDao.load(WmsInventory.class, wie.getInventory().getId());
    }
    /**
	 * 非计划手工创建虚拟移位单和移位明细
	 */
	public void createMoveDocDetailJac(WmsMoveTaskDTO dto) {
		WmsMoveDoc moveDoc = null;
		if (dto.getMoveDocId() == null) {
			moveDoc = createMoveDoc();
		} else {
			moveDoc = commonDao.load(WmsMoveDoc.class, dto.getMoveDocId());
		}
		dto = this.getRelateInfo(dto);
		WmsInventory inv = this.commonDao.load(WmsInventory.class, dto.getSrcInventoryId());
		if("待检".equals(inv.getStatus())){
			throw new BusinessException("待检状态不允许移位");
		}

		WmsInventoryExtend invEx = this.commonDao.load(WmsInventoryExtend.class, dto.getSrcInventoryExtendId());

		// 创建移位计划明细
		WmsMoveDocDetail wmdd = createMoveDocDetail(moveDoc, 
				inv.getItemKey(), 
				inv.getPackageUnit(), 
				inv.getPackQty(dto.getActualMovedQuantityBU()), 
				dto.getActualMovedQuantityBU(), 
				dto.getFromLocationId(), 
				dto.getFromLocationCode(), 
				null, 
				inv.getStatus(), 
				inv.getId(), 
				invEx);
		wmdd.setSrcInvExId(invEx.getId());
		inv.allocatePickup(dto.getActualMovedQuantityBU());
		moveDoc.addDetail(wmdd);
		commonDao.store(wmdd);
		commonDao.store(moveDoc);
		
		/*WmsMoveTaskDTO readyAllocateDTO = new WmsMoveTaskDTO();
		readyAllocateDTO.setMoveDocId(moveDoc.getId());
		readyAllocateDTO.setMoveDocDetailId(wmdd.getId());
		
		this.autoAllocateVirtual(readyAllocateDTO);*/
		dto.setMoveDocId(moveDoc.getId());
	}
	
	
	
   private WmsMoveTaskDTO  getRelateInfo(WmsMoveTaskDTO dto){
	   WmsInventoryExtend invEx = this.commonDao.load(WmsInventoryExtend.class, dto.getSrcInventoryExtendId());
	   WmsInventory inv = invEx.getInventory();
	   WmsItem item = commonDao.load(WmsItem.class, invEx.getItem().getId());
	   dto.setFromLocationCode(invEx.getLocationCode());
	   dto.setFromLocationId(invEx.getLocationId());
	   dto.setItemCode(item.getCode());
	   dto.setItemId(item.getId());
	   dto.setItemKeyId(inv.getItemKey().getId());
	   dto.setSrcInventoryId(inv.getId());
	   dto.setSrcInventoryStatus(inv.getStatus());
	   return dto;
   }
   
   /**
	 * 非计划手工散货按托盘标签移位确认
	 * @param dto
	 * @param actualToLocationCode
	 */
	public void confirmMoveManualByPalletItem(WmsMoveTaskDTO dto, String actualToLocationCode) {
		if(null == dto.getSrcInventoryExtendId()){
			throw new BusinessException("库存序列异常");
		}
		WmsInventoryExtend wie = this.commonDao.load(WmsInventoryExtend.class, dto.getSrcInventoryExtendId());
		if(null == wie){
			throw new BusinessException("货品已被他人移走,联系管理员");
		}
		WmsInventory srcInv = commonDao.load(WmsInventory.class, dto.getSrcInventoryId());
		WmsPackageUnit packageUnit = commonDao.load(WmsPackageUnit.class, srcInv.getPackageUnit().getId());
		srcInv.setPackageUnit(packageUnit);
		wie.setInventory(srcInv);
		srcInv.unallocatePickup(dto.getActualMovedQuantityBU());
		WmsLocation toLocation = this.queryWmsLocationByCode(actualToLocationCode);
		wmsInventoryManager.moveInventory(wie, toLocation,BaseStatus.NULLVALUE,BaseStatus.NULLVALUE,packageUnit, dto.getActualMovedQuantityBU(),srcInv.getStatus(),WmsInventoryLogType.MOVE, "库存手工移位");
		
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, dto.getMoveDocId());
		WmsMoveDocDetail mdd = commonDao.load(WmsMoveDocDetail.class, dto.getMoveDocDetailId());
		WmsItemKey itemKey = commonDao.load(WmsItemKey.class, srcInv.getItemKey().getId());
		
		if (dto.getDestInventoryId() != null) {
			WmsInventory dstInventory = wmsInventoryManager.getInventoryWithNew(
					toLocation, itemKey, packageUnit, srcInv.getStatus());
			dstInventory.setPutawayQuantityBU(dstInventory.getPutawayQuantityBU() - dto.getUnMoveQuantityBU());
			commonDao.store(dstInventory);
		}
		mdd.move( dto.getActualMovedQuantityBU());
		commonDao.store(mdd);
		
		if (moveDoc.getPlanQuantityBU().doubleValue() == moveDoc.getMovedQuantityBU().doubleValue()) {
			moveDoc.setStatus(WmsMoveDocStatus.FINISHED);
		}
		commonDao.store(moveDoc);
	}
	//判断录入的托盘号是否存在
	public String checkPallet(String srcPallet,String descPallet){
		List<Long> srcIds = pallets(srcPallet);
		if(srcIds==null || srcIds.size()<=0){
			return "失败!源托盘号系统不存在";
		}
		List<Long> descIds = pallets(descPallet);
		if(descIds==null || descIds.size()<=0){
			return "失败!目标托盘号系统不存在";
		}
		//
		String movePlanTitle = "整托移位";
		WmsInventoryExtend descIx = commonDao.load(WmsInventoryExtend.class, descIds.get(0));
		WmsInventory dstInventory = commonDao.load(WmsInventory.class,descIx.getInventory().getId());
		List<WmsInventoryExtend> deleteSrcIx = new ArrayList<WmsInventoryExtend>();
		Double availabQty = 0D;
		for(Long id : srcIds){
			WmsInventoryExtend srcIx = commonDao.load(WmsInventoryExtend.class, id);
			WmsInventory srcInventory = commonDao.load(WmsInventory.class,srcIx.getInventory().getId());
			availabQty = srcIx.getAvailableQuantityBU();
			srcInventory.removeQuantityBU(availabQty);
			commonDao.store(srcInventory);
			srcIx.removeQuantity(availabQty);
			if (srcIx.getQuantityBU() <= 0) {
				deleteSrcIx.add(srcIx);
			}
			
			WmsInventory newDstInventory = wmsInventoryManager.allocatePutaway(dstInventory.getLocation(),
					srcInventory.getItemKey(), srcInventory.getPackageUnit() 
					,srcInventory.getStatus(),availabQty,1);
			//
			newDstInventory = wmsInventoryManager.moveDescInv(newDstInventory.getId(),availabQty);
			WmsLocation toLoc = commonDao.load(WmsLocation.class,newDstInventory.getLocation().getId());
			wmsInventoryManager.refreshLocationUseRate(toLoc, 0);
			commonDao.store(toLoc);
			//
			inventoryExtendManager.addInventoryExtend(newDstInventory, descIx.getPallet()
					, BaseStatus.NULLVALUE,BaseStatus.NULLVALUE,availabQty);
			
			wmsInventoryManager.addInventoryLog(WmsInventoryLogType.MOVE, -1, movePlanTitle,
					null, srcInventory.getLocation(), srcInventory.getItemKey(),
					availabQty, srcInventory.getPackageUnit(),srcInventory.getStatus(), movePlanTitle);
			
			wmsInventoryManager.addInventoryLog(WmsInventoryLogType.MOVE, 1, movePlanTitle,
					null, dstInventory.getLocation(), dstInventory.getItemKey(),
					availabQty, dstInventory.getPackageUnit(),dstInventory.getStatus(), movePlanTitle);
		}
		//
		commonDao.deleteAll(deleteSrcIx);
		return "成功拼托";
	}
	private List<Long> pallets(String palletNo){
		List<Long> ids = commonDao.findByQuery("SELECT wsn.id FROM WmsInventoryExtend wsn "
				+ " LEFT JOIN wsn.inventory inventory " 	
				+ " WHERE wsn.pallet =:palletNo " 
				+ " AND inventory.location.type IN ('STORAGE','PROCESS','SPLIT','CROSS_DOCK') "
				+ " AND ((inventory.quantityBU-inventory.allocatedQuantityBU)>0) "
				+ " AND (inventory.location.lockCount = false)", 
				new String[]{"palletNo"}, new Object[]{palletNo});
		return ids;
	}
	
	public String quickMove(Object[] ix,Double moveQty,String toLocationCode){
		WmsWarehouse warehouse = WmsWarehouseHolder.getWmsWarehouse();
//		WmsOrganization company = null;
//		List<MiddleCompanyExtends> mis = commonDao.findByQuery("FROM MiddleCompanyExtends mi WHERE mi.warehouse.id =:warehouseId",
//				new String[]{"warehouseId"}, new Object[]{warehouse.getId()});
//		if(mis!=null && mis.size()>0){
//			company = mis.get(0).getCompany();
//		}
		
		WmsPackageUnit basePackageUnit = commonDao.load(WmsPackageUnit.class, Long.parseLong(ix[7].toString()));
//		WmsItem item = commonDao.load(WmsItem.class,Long.parseLong(ix[1].toString()));
//		WmsOrganization supplier = commonDao.load(WmsOrganization.class, Long.parseLong(ix[6].toString()));
//		String extendPropC1 = ix[5].toString();
//		String locationCode = ix[3].toString();
//		Long toLocationId = Long.parseLong(ix[8].toString());
//		WmsMoveDoc moveDoc = newMoveDoc(company,null,warehouse,WmsMoveDocType.MV_MOVE);
//		WmsMoveDocDetail moveDocDetial = newMoveDetail(moveDoc, item, supplier.getCode(), extendPropC1, 
//				basePackageUnit, locationCode, toLocationId);
//		moveDoc.addDetail(moveDocDetial);
//		commonDao.store(moveDoc);
		
//		//直接移位
		WmsLocation toLocation = (WmsLocation) commonDao
		.findByQueryUniqueResult("from WmsLocation location where location.code=:code" +
				" AND location.warehouse.code=:warehouse AND location.status='ENABLED'",
				new String[] { "code", "warehouse"}, new Object[] {toLocationCode,warehouse.getCode()});
		if(toLocation==null){
			return "目标库位不存在";
		}
		//原库存扣减库存量
		WmsInventoryExtend srcIx = commonDao.load(WmsInventoryExtend.class, Long.parseLong(ix[0].toString()));
		WmsInventory srcInv = commonDao.load(WmsInventory.class,
				srcIx.getInventory().getId());
		if(srcInv.getAvailableQuantityBU()<moveQty){
			return "当前库存可用量不足";
		}
		if(srcIx.getAvailableQuantityBU()<moveQty){
			return "当前托盘可用量不足";
		}
		if("待检".equals(srcInv.getStatus())){
			return "待检品不允许移位";
		}
		//-----代码复用▽
		wmsInventoryManager.inventoryQuickMove(srcInv, moveQty, "货品快捷移位", toLocation, basePackageUnit, srcIx);
		/*srcInv.removeQuantityBU(moveQty);
		commonDao.store(srcInv);
		wmsInventoryManager.addInventoryLog(WmsInventoryLogType.MOVE, -1, null,null, srcInv.getLocation(),
				srcInv.getItemKey(),moveQty, srcInv.getPackageUnit(),srcInv.getStatus(), "货品快捷移位");
		//目标库存增加库存量
		WmsInventory dstInventory = wmsInventoryManager.getInventoryWithNew(toLocation, srcInv.getItemKey(), basePackageUnit);
		dstInventory.addQuantityBU(moveQty);
		commonDao.store(dstInventory);
		wmsInventoryManager.addInventoryLog(WmsInventoryLogType.MOVE, 1, null,null, dstInventory.getLocation(),
				dstInventory.getItemKey(),moveQty, dstInventory.getPackageUnit(),dstInventory.getStatus(), "货品快捷移位");
		//扣减原序列号库存信息
		srcIx.removeQuantity(moveQty);
		if (srcIx.getQuantityBU() <= 0) {
			commonDao.delete(srcIx);
		}else{
			commonDao.store(srcIx);
			pallet = BaseStatus.NULLVALUE;//没有整托移位,托盘信息不允许带走
		}
		//增加目标库位序列号
		workDocManager.addDescWie(dstInventory, pallet, toLocation, moveQty);*/
		//-----代码复用△
		return "-";
	}
	public Boolean checkLocationByCode(String locationCode){
		String hql = "FROM WmsLocation loc WHERE loc.code =:code AND loc.status =:status AND loc.type =:type ";
		WmsLocation loc = (WmsLocation) commonDao.findByQueryUniqueResult(hql, new String[]{"code","status","type"}, new Object[]{locationCode,BaseStatus.ENABLED,WmsLocationType.STORAGE});
		if(loc == null){
			return false;
		}else{
			return true;
		}
	}
	
	public void createMoveDocLocation(String locationCode){
		WmsWorker worker = WmsWorkerHolder.getWmsWorker();
		String hql = "FROM WmsLocation loc WHERE loc.code =:code AND loc.status =:status AND loc.type =:type ";
		WmsLocation loc = (WmsLocation) commonDao.findByQueryUniqueResult(hql, new String[]{"code","status","type"}, new Object[]{locationCode,BaseStatus.ENABLED,WmsLocationType.STORAGE});
		hql = "FROM WmsMoveDocLocation mdl WHERE mdl.worker.id =:workerId AND mdl.location.code =:locationCode AND mdl.moveDoc IS NULL ";
		WmsMoveDocLocation mdl = (WmsMoveDocLocation) commonDao.findByQueryUniqueResult(hql, new String[]{"workerId","locationCode"}, new Object[]{worker.getId(),locationCode});
		if(mdl == null){
			mdl = EntityFactory.getEntity(WmsMoveDocLocation.class);
			mdl.setLocation(loc);
			mdl.setWorker(worker);
			commonDao.store(mdl);
		}
	}
	
	public void createMoveDocReplenishment(WmsOrganization company){
		WmsWorker worker = WmsWorkerHolder.getWmsWorker();
		WmsWarehouse warehouse = WmsWarehouseHolder.getWmsWarehouse();
		WmsMoveDoc moveDoc = newMoveDoc(company, worker, warehouse, WmsMoveDocType.MV_REPLENISHMENT_MOVE);
		String hql = "FROM WmsMoveDocLocation mdl WHERE mdl.worker.id =:workerId AND mdl.moveDoc IS NULL ";
		List<WmsMoveDocLocation> mdls = commonDao.findByQuery(hql, new String[]{"workerId"}, new Object[]{worker.getId()});
		for(WmsMoveDocLocation mdl : mdls){
			mdl.setMoveDoc(moveDoc);
			commonDao.store(mdl);
		}
	}
	
	public List<WmsOrganization> getWmsOrganization(){
		String hql = "FROM WmsOrganization o WHERE o.beCompany =:beCompany ";
		List<WmsOrganization> companys = commonDao.findByQuery(hql, "beCompany", Boolean.TRUE);
		return companys;
	}
	
	public WmsMoveTaskDTO getLocationByMoveDocId(Long moveDocId){
		WmsMoveTaskDTO dto = null;
		String hql = "FROM WmsMoveDocLocation mdl WHERE mdl.moveDoc.id =:moveDocId AND mdl.isEnd =:isEnd ORDER BY mdl.location.routeNo ASC";
		List<WmsMoveDocLocation> mdls = commonDao.findByQuery(hql, new String[]{"moveDocId","isEnd"}, new Object[]{moveDocId,Boolean.FALSE});
		if(!mdls.isEmpty()){
			WmsLocation loc = commonDao.load(WmsLocation.class, mdls.get(0).getLocation().getId());
			WmsMoveDoc doc = commonDao.load(WmsMoveDoc.class, moveDocId);
			dto = new WmsMoveTaskDTO();
			dto.setToLocationId(loc.getId());
			dto.setToLocationCode(loc.getCode());
			dto.setCompanyCode(doc.getCompany().getCode());
		}
		return dto;
	}
	
	public void activeReplenishment(Long moveDocId){
		WmsMoveDoc doc = commonDao.load(WmsMoveDoc.class, moveDocId);
		activePickByRf(doc.getCode());
	}
	
	public WmsMoveTaskDTO findMoveTaskByInvExtId(Long invExtId,WmsMoveTaskDTO dto){
		WmsInventoryExtend invE = commonDao.load(WmsInventoryExtend.class, invExtId);
		WmsInventory inv = commonDao.load(WmsInventory.class, invE.getInventory().getId());
		WmsItem item = commonDao.load(WmsItem.class, inv.getItemKey().getItem().getId());
		dto.setSrcInventoryExtendId(invExtId);
		dto.setSrcInventoryId(inv.getId());
		dto.setItemId(item.getId());
		dto.setItemCode(item.getCode());
		dto.setItemName(item.getName());
		dto.setUnMoveQuantityBU(inv.getAvailableQuantityBU());
		return dto;
	}
	
	public void createReplenishmentMoveDocDetail(WmsMoveTaskDTO dto){
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, dto.getMoveDocId());
		WmsInventoryExtend inventoryExtend = commonDao.load(WmsInventoryExtend.class, dto.getSrcInventoryExtendId());
		WmsInventory inv = commonDao.load(WmsInventory.class, dto.getSrcInventoryId());
		String hql = "FROM WmsLocation loc WHERE loc.code =:code AND loc.status =:status AND loc.type =:type ";
		WmsLocation fromLocation = (WmsLocation) commonDao.findByQueryUniqueResult(hql, new String[]{"code","status","type"}, new Object[]{dto.getFromLocationCode(),BaseStatus.ENABLED,WmsLocationType.STORAGE});
		
		// 创建补货明细
		WmsMoveDocDetail wmdd = createMoveDocDetail(moveDoc, 
				inv.getItemKey(), 
				inv.getPackageUnit(), 
				0D, 
				0D,
				fromLocation.getId(), 
				fromLocation.getCode(), 
				inventoryExtend.getPallet(), 
				inv.getStatus(), 
				inv.getId(), 
				inventoryExtend);
		wmdd.setToLocationCode(dto.getToLocationCode());
		wmdd.setToLocationId(dto.getToLocationId());
		moveDoc.addDetail(wmdd);
		commonDao.store(wmdd);
		commonDao.store(moveDoc);
		Object[] ixs = new Object[]{inventoryExtend.getId()};
		subscriberAutoAllocateWmsMoveDoc(ixs, dto.getActualMovedQuantityBU(), wmdd.getId());
	}
	
	public void markLocationFinished(WmsMoveTaskDTO dto){
		String hql = "FROM WmsMoveDocLocation mdl WHERE mdl.moveDoc.id =:moveDocId AND mdl.location.id =:locationId ";
		WmsMoveDocLocation mdl = (WmsMoveDocLocation) commonDao.findByQueryUniqueResult(hql, new String[]{"moveDocId","locationId"}, new Object[]{dto.getMoveDocId(),dto.getToLocationId()});
		mdl.setIsEnd(Boolean.TRUE);
		commonDao.store(mdl);
	}
	
	public WmsMoveTaskDTO findMoveDocDetailByMoveDocId(Long moveDocId){
		WmsMoveTaskDTO dto = null;
		String hql = "FROM WmsMoveDocDetail det WHERE det.moveDoc.id =:moveDocId AND det.planQuantityBU > det.movedQuantityBU ";
		List<WmsMoveDocDetail> details = commonDao.findByQuery(hql, new String[]{"moveDocId"}, new Object[]{moveDocId});
		if(!details.isEmpty()){
			dto = new WmsMoveTaskDTO();
			WmsMoveDocDetail detail = details.get(0);
			WmsItem item = commonDao.load(WmsItem.class, detail.getItem().getId());
			dto.setItemId(item.getId());
			dto.setFromLocationCode(detail.getFromLocationCode());
			dto.setItemCode(item.getCode());
			dto.setItemName(item.getName());
			dto.setUnMoveQuantityBU(detail.getPlanQuantityBU());
			dto.setToLocationCode(detail.getToLocationCode());
			dto.setMoveDocId(moveDocId);
			dto.setMoveDocDetailId(detail.getId());
		}
		return dto;
	}
	
	public void confimReplenishment(WmsMoveTaskDTO dto){
		String hql = "FROM WmsTask task WHERE task.moveDocDetail.id =:moveDocDetailId AND task.status !=:status ";
		WmsTask task = (WmsTask) commonDao.findByQueryUniqueResult(hql, new String[]{"moveDocDetailId","status"}, new Object[]{dto.getMoveDocDetailId(),WmsTaskStatus.FINISHED});
		if(task != null){
			workDocManager.singleConfirm(task, dto.getMoveDocId(), dto.getUnMoveQuantityBU(), null, WmsWorkerHolder.getWmsWorker().getId());
		}
	}
}
