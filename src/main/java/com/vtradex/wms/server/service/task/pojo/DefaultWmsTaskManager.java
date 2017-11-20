package com.vtradex.wms.server.service.task.pojo;


import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.middle.WmsJobLog;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.shipping.WmsWaveDoc;
import com.vtradex.wms.server.model.shipping.WmsWaveDocDetail;
import com.vtradex.wms.server.model.shipping.WmsWaveDocWorkMode;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.service.task.WmsTaskManager;
import com.vtradex.wms.server.utils.PackageUtils;

/**
 * 任务管理 
 *
 * @category Manager 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.3 $Date: 2015/12/09 09:40:22 $
 */
public class DefaultWmsTaskManager extends DefaultBaseManager implements
		WmsTaskManager {
	
	protected WorkflowManager workflowManager;
	
	public DefaultWmsTaskManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}
	
	/**
	 * 创建移位任务
	 * 
	 * @param moveDocDetail 移位单明细
	 * @param itemKey
	 * @param srcInventoryStatus
	 * @param allocateQuantityBU
	 * @return
	 * @throws BusinessException
	 */
	public WmsTask createWmsTask(WmsMoveDocDetail moveDocDetail, WmsItemKey itemKey, String srcInventoryStatus
			,double allocateQuantityBU,String relatedBill) throws BusinessException {
		WmsTask newTask = EntityFactory.getEntity(WmsTask.class);
		newTask.setType(moveDocDetail.getMoveDoc().getType());
		newTask.setItemKey(itemKey);
		newTask.setPackageUnit(moveDocDetail.getPackageUnit());
		newTask.setInventoryStatus(srcInventoryStatus);
		newTask.setFromLocationId(moveDocDetail.getFromLocationId());
		newTask.setFromLocationCode(moveDocDetail.getFromLocationCode());
		newTask.setPlanQuantity(PackageUtils.convertPackQuantity(allocateQuantityBU, moveDocDetail.getPackageUnit()));
		newTask.setPlanQuantityBU(allocateQuantityBU);
		newTask.setOriginalBillCode(moveDocDetail.getMoveDoc().getCode());
		newTask.setRelatedBill(relatedBill);//容器拣货时,保存容器号
		newTask.setPallet(moveDocDetail.getPallet() == null ? BaseStatus.NULLVALUE : moveDocDetail.getPallet());
		if(moveDocDetail.getMoveDoc().isWaveType()){
			WmsWaveDoc waveDoc = load(WmsWaveDoc.class,moveDocDetail.getMoveDoc().getWaveDoc().getId());
			if(WmsWaveDocWorkMode.WORK_BY_DOC.equals(waveDoc.getWorkMode())){
				WmsWaveDocDetail detail = load(WmsWaveDocDetail.class,moveDocDetail.getRelatedId());
				newTask.setRelatedBill(detail.getMoveDocDetail().getMoveDoc().getCode());
			}
			
		}
		newTask.setSrcInventoryId(moveDocDetail.getInventoryId());
		newTask.setMoveDocDetail(moveDocDetail);
		moveDocDetail.getTasks().add(newTask);
		newTask.calculateLoad();
		commonDao.store(newTask);
		return newTask;
	}
	
	/**
	 * 创建上架任务
	 * @param quantityBU 任务数量BU
	 */
	public WmsTask getTaskByConditions(WmsMoveDocDetail detail, WmsLocation srcLoc, 
			WmsLocation dstLoc, WmsItemKey itemKey, WmsPackageUnit packageUnit, String inventoryStatus, 
			Double quantityBU, Long srcInventoryId, Long dstInventoryId, Boolean beManual) {
		//生成新的Task
		WmsTask newTask = EntityFactory.getEntity(WmsTask.class);
		newTask.setType(detail.getMoveDoc().getType());
		newTask.setItemKey(itemKey);
		newTask.setPackageUnit(packageUnit);
		newTask.setInventoryStatus(inventoryStatus);
		newTask.setFromLocationId(srcLoc.getId());
		newTask.setFromLocationCode(srcLoc.getCode());
		newTask.setToLocationId(dstLoc.getId());
		newTask.setToLocationCode(dstLoc.getCode());
		newTask.setPlanQuantity(PackageUtils.convertPackQuantity(quantityBU, packageUnit));
		newTask.setPlanQuantityBU(quantityBU);
		newTask.setOriginalBillCode(detail.getMoveDoc().getCode());
		newTask.setRelatedBill(detail.getMoveDoc().getCode());
		newTask.setPallet(detail.getPallet() == null ? BaseStatus.NULLVALUE : detail.getPallet());
		if(detail.getMoveDoc().isWaveType() && WmsWaveDocWorkMode.WORK_BY_DOC.equals(detail.getMoveDoc().getWaveDoc().getWorkMode())){
			WmsWaveDocDetail waveDocDetail = load(WmsWaveDocDetail.class,detail.getRelatedId());
			newTask.setRelatedBill(waveDocDetail.getMoveDocDetail().getMoveDoc().getCode());
		}
		newTask.setSrcInventoryId(srcInventoryId);
		newTask.setDescInventoryId(dstInventoryId);
		newTask.setBeManual(beManual);
		newTask.calculateLoad();
		newTask.setMoveDocDetail((WmsMoveDocDetail) detail);
		detail.getTasks().add(newTask);
		commonDao.store(newTask);
		workflowManager.doWorkflow(newTask, "taskProcess.new");
		return newTask;
	}
	/**
	 * 创建上架任务 yc.min
	 * @param quantityBU 任务数量BU
	 */
	public WmsTask getTaskByJAC(WmsLocation srcLoc,String type,String originalBillCode, String relatedBill,
			String pallet,WmsLocation dstLoc, WmsItemKey itemKey, WmsPackageUnit packageUnit, String inventoryStatus, 
			Double quantityBU, Long srcInventoryId, Long dstInventoryId) {
		//生成新的Task
		WmsTask newTask = EntityFactory.getEntity(WmsTask.class);
		newTask.setType(type);
		newTask.setItemKey(itemKey);
		newTask.setPackageUnit(packageUnit);
		newTask.setInventoryStatus(inventoryStatus);
		newTask.setFromLocationId(srcLoc.getId());
		newTask.setFromLocationCode(srcLoc.getCode());
		newTask.setToLocationId(dstLoc.getId());
		newTask.setToLocationCode(dstLoc.getCode());
		newTask.setPlanQuantity(PackageUtils.convertPackQuantity(quantityBU, packageUnit));
		newTask.setPlanQuantityBU(quantityBU);
		newTask.setOriginalBillCode(originalBillCode);//上架的话记录ASN号
		newTask.setRelatedBill(relatedBill);//上架的话记录ASN明细ID
		newTask.setPallet(pallet == null ? BaseStatus.NULLVALUE : pallet);
		newTask.setSrcInventoryId(srcInventoryId);
		newTask.setDescInventoryId(dstInventoryId);
		commonDao.store(newTask);
		workflowManager.doWorkflow(newTask, "taskProcess.new");
		return newTask;
	}
	/**
	 * 创建质检任务 yc.min
	 * srcInventoryExdId-源托盘表ID号
	 */
	public WmsTask getTaskByQuantity(WmsLocation srcLoc,String type,
			String pallet, WmsItemKey itemKey, WmsPackageUnit packageUnit, String inventoryStatus, 
			Long srcInventoryExdId,WmsMoveDocDetail moveDocDetail) {
		//生成新的Task
		WmsTask newTask = EntityFactory.getEntity(WmsTask.class);
		newTask.setType(type);
		newTask.setItemKey(itemKey);
		newTask.setPackageUnit(packageUnit);
		newTask.setInventoryStatus(inventoryStatus);
		newTask.setFromLocationId(srcLoc.getId());
		newTask.setFromLocationCode(srcLoc.getCode());
		newTask.setPallet(pallet == null ? BaseStatus.NULLVALUE : pallet);
		newTask.setSrcInventoryId(srcInventoryExdId);//源托盘表ID号
		newTask.setMoveDocDetail(moveDocDetail);
		commonDao.store(newTask);
		workflowManager.doWorkflow(newTask, "taskProcess.new");
		return newTask;
	}
	public void cancelAllocate(WmsTask task,Double quantity){		
		//对未作业确认数量库存取消分配
		WmsInventory fromInventory = commonDao.load(WmsInventory.class, task.getSrcInventoryId());
		fromInventory.unallocatePickup(quantity);

		WmsInventory toInventory = commonDao.load(WmsInventory.class, task.getDescInventoryId());
		toInventory.unallocatePutaway(quantity);
		
		//对作业任务，作业单扣减计划数量
		task.unallocatePick(quantity);
		
		//如果task无计划作业数量，则删除task
		if (task.getPlanQuantityBU() == 0D) {
			commonDao.delete(task);
		} else {
			//作业任务作业确认
			workflowManager.doWorkflow(task, "taskProcess.confirm");	
		}

		//作业单作业确认，更新作业单状态
		workflowManager.doWorkflow(task.getWorkDoc(), "workDocProcess.confirm");	
	}
	public WmsJobLog saveWmsJobLog(String type,String operName,String operException,String operExceptionMess){
		WmsJobLog log = new WmsJobLog(type, operName, operException, operExceptionMess);
		commonDao.store(log);
		return log;
	}
}