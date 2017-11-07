package com.vtradex.wms.server.telnet.count;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.wms.server.telnet.dto.WmsCountTaskDTO;
import com.vtradex.wms.server.telnet.manager.WmsCommonRFManager;

@Transactional
public interface WmsCountTaskRFManager extends WmsCommonRFManager{

	/**
	 * 查询当前作业人员是否存在未完成的盘点任务
	 * @param user
	 * @return
	 */
	@Transactional
	WmsCountTaskDTO queryUnFinishCountTask();
	
	/**
	 * 根据当前扫描库位自动申请盘点任务
	 * @param anyLocationCode
	 * @return
	 */
	@Transactional
	WmsCountTaskDTO applyNewCountTaskByCurrentLocationCode(String anyLocationCode);
	/**
	 * 检查物料是否存在
	 * @param code
	 */
	void checkItemExists(String code);
	/**
	 * 盘点登记
	 * @param countDetailId
	 * @param itemCode
	 * @param quantity
	 * @return
	 */
	@Transactional
	void countRecord(Long countDetailId , String itemCode , Double quantity);
	/**
	 * 盘点关闭
	 * @param countPlanId
	 * @return
	 */
	@Transactional
	WmsCountTaskDTO quantityAdjust(Long countPlanId , Long countPlanDetailId , String locationCode);
	/**
	 * 取消申请的盘点任务
	 * @param countDetailId
	 */
	@Transactional
	void cancelCountTask(Long countDetailId);
}
