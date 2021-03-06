package com.vtradex.wms.server.service.count;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.count.WmsCountDetail;
import com.vtradex.wms.server.model.count.WmsCountPlan;
import com.vtradex.wms.server.model.count.WmsCountRecord;
import com.vtradex.wms.server.model.warehouse.WmsLocation;

@SuppressWarnings("unchecked")
public interface WmsCountPlanManager extends BaseManager {
	
	/**
	 * 创建盘点计划--主入口方法
	 * */
	@Transactional
	void createWmsCountPlan(WmsCountPlan wmsCountPlan, Integer cycleDays);
	
	/**
	 * 盘点计划修改页面保存
	 * */
	@Transactional
	void saveCountPlan(WmsCountPlan wmsCountPlan);
	
	/**
	 * 删除盘点明细
	 * */
	@Transactional
	void removeDetail(WmsCountDetail wmsCountDetail);
	
	/**
	 * 添加盘点明细
	 * */
	@Transactional
	void addDetail(Long wmsCountPlanId, WmsLocation location);
	
	/**
	 * 激活盘点计划
	 * */
	@Transactional
	void active(WmsCountPlan wmsCountPlan);
	
	/**
	 * 盘点计划反激活
	 * */
	@Transactional
	void unActive(WmsCountPlan wmsCountPlan);
	
	/**
	 * 盘点登记
	 * */
	@Transactional
	void record(WmsCountRecord wmsCountRecord, List countRecords);
	
	/**
	 * 盘点增补
	 * */
	@Transactional
	void record(Long countPlanId, Long locationId, Long itemId, Double quantity);
	
	/**
	 * 差异调整
	 * */
	@Transactional
	void quantityAdjust(WmsCountPlan wmsCountPlan);
	
	/**
	 * 关闭
	 * */
	@Transactional
	void close(WmsCountPlan wmsCountPlan);
	
	/**
	 * 任务指派
	 */
	@Transactional
	void changeWorker(WmsCountPlan wmsCountPlan , Long workerID);
	/**
	 * 取消盘点
	 */
	@Transactional
	void deleteCountPlan(WmsCountPlan wmsCountPlan);
}