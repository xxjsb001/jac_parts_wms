package com.vtradex.wms.server.service.task;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.middle.WmsJobLog;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.warehouse.WmsLocation;

/**
 * 任务管理 
 *
 * @category Manager 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.3 $Date: 2015/12/09 09:40:23 $
 */
public interface WmsTaskManager extends BaseManager {
	
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
	@Transactional
	WmsTask createWmsTask(WmsMoveDocDetail moveDocDetail, WmsItemKey itemKey, String srcInventoryStatus, double allocateQuantityBU) throws BusinessException;
	
    /**
     * 创建上架任务
     * @param detail
     * @param srcLoc
     * @param dstLoc
     * @param itemKey
     * @param packageUnit
     * @param inventoryStatus
     * @param quantity
     * @param srcInventoryId
     * @param dstInventoryId
     * @param beManual
     * @return
     */
	@Transactional
	WmsTask getTaskByConditions(WmsMoveDocDetail detail, WmsLocation srcLoc, 
			WmsLocation dstLoc, WmsItemKey itemKey, WmsPackageUnit packageUnit, String inventoryStatus, 
			Double quantity, Long srcInventoryId, Long dstInventoryId, Boolean beManual);
	/**yc.min*/
	@Transactional
	WmsTask getTaskByJAC(WmsLocation srcLoc,String type,String originalBillCode, String relatedBill,
			String pallet,WmsLocation dstLoc, WmsItemKey itemKey, WmsPackageUnit packageUnit, String inventoryStatus, 
			Double quantityBU, Long srcInventoryId, Long dstInventoryId);
	/**
	 * 创建质检任务 yc.min
	 * srcInventoryId-源托盘表ID号
	 */
	@Transactional
	WmsTask getTaskByQuantity(WmsLocation srcLoc,String type,
			String pallet, WmsItemKey itemKey, WmsPackageUnit packageUnit, String inventoryStatus, 
			Long srcInventoryExdId,WmsMoveDocDetail moveDocDetail);
    /**
     * 对任务上剩余未作业数量取消分配
     * @param task
     * @param quantity
     * @return
     */
	@Transactional
	void cancelAllocate(WmsTask task,Double quantity);
	/**type-@WmsLogType,operName-标题内容@WmsLogTitle,operException-系统错误,operExceptionMess-日志内容*/
	@Transactional
	WmsJobLog saveWmsJobLog(String type,String operName,String operException,String operExceptionMess);
}


