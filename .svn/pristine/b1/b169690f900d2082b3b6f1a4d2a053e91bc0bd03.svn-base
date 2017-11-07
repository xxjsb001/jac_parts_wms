package com.vtradex.wms.server.service.process;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.process.WmsProcessPlan;
import com.vtradex.wms.server.model.process.WmsProcessPlanDetail;

public interface WmsProcessManager extends BaseManager {
	/**
	 * 保存加工方案－自动设置所属仓库和单据状态
	 * @param processPlan
	 */
	@Transactional
	void storeProcessPlan(WmsProcessPlan processPlan);

	/**
	 * 保存加工方案明细
	 * 
	 * @param processPlanId
	 * @param processPlanDetail
	 */
	@Transactional
	void addProcessPlanDetail(Long processPlanId,WmsProcessPlanDetail processPlanDetail);

	/**
	 * 删除加工方案明细
	 * @param processPlanId
	 * @param processPlanDetails
	 */
	@Transactional
	void deleteProcessPlanDetail(WmsProcessPlan processPlan, WmsProcessPlanDetail processPlanDetail);
	
	/**
	 * 删除加工方案
	 * @param processPlans
	 */
	@Transactional
	void deleteProcessPlan(WmsProcessPlan processPlan);
	
	/**
	 * 保存加工单，同时生成加工单明细
	 * @param processDoc
	 */
	@Transactional
	void storeProcessDoc(WmsMoveDoc processDoc);
	
	/**
	 * 删除加工单
	 * @param processDocs
	 */
	@Transactional
	void deleteProcessDoc(WmsMoveDoc processDoc);
	
	/**
	 * 加工登记
	 * @param processDocId,processQuantity
	 */
	@Transactional
	void processRecord(WmsMoveDoc processDoc,Double processQuantity);
}