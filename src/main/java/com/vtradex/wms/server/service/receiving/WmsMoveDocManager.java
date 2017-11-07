package com.vtradex.wms.server.service.receiving;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.client.ui.table.RowData;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.move.WmsTaskLog;
import com.vtradex.wms.server.model.receiving.WmsASN;

/**
 * 移位管理
 *
 * @category Manager
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.14 $Date: 2016/06/24 01:32:22 $
 */
public interface WmsMoveDocManager extends BaseManager {
	
	/**
	 * 自动创建上架计划
	 * @param asn
	 * @param type
	 * @return
	 */
	@Transactional
	WmsMoveDoc createWmsMoveDoc(WmsASN asn);
	
	/**
	 * 创建移位单
	 * @param entity
	 */
	@Transactional
	void createWmsMoveDoc(Object entity) throws BusinessException;
	
	/**
	 * 自动分配
	 * @param moveDoc 移位单
	 */
	@Transactional
	void autoAllocate(WmsMoveDocDetail moveDocDetail);
	
	/**
	 * 取消分配
	 * @param moveDoc 移位单
	 */
	@Transactional
	void cancelAllocate(WmsMoveDoc moveDoc);
	
	/**
	 * 取消分配
	 * @param moveDocId 移位单ID
	 */
	@Transactional
	void cancelAllocate(Long moveDocId);
	
	/**
	 * 手工取消分配
	 * @param moveDocId 移位单ID
	 * @param map 任务取消分配信息
	 */
	@Transactional
	void manualCancelAllocate(Long moveDocId, Map<Long, Double> cancelInfo);
	
	/**
	 * 手工分配
	 * @param moveDocDetailId 移位单明细ID
	 * @param map 库存手工分配信息
	 */
	@Transactional
	void manuelAllocate(Long moveDocDetailId, Map<Long, Double> allocateInfo);

	/**
	 * 保存移位单
	 * @param moveDoc
	 */
	@Transactional
	void storeMoveDoc(WmsMoveDoc moveDoc);

	/** 删除移位计划*/
	@Transactional
	void cancelMoveDoc(WmsMoveDoc moveDoc);
	/** 取消拣货单(状态都是打开的拣货单不允许取消，建议删除，便于发货单能够继续操作，只能已发运并退捡过的拣货单才可以取消)*/
	@Transactional
	void cancelPickDoc(WmsMoveDoc moveDoc);

	/**
	 * 添加移位计划明细:按货品添加
	 * @param moveDocId
	 * @param wsn
	 * @param map
	 */
	@Transactional
	void addMoveDocDetail(Long moveDocId, WmsInventoryExtend wsn, List tableValues);
	@Transactional
	void addMoveDocDetail(Long moveDocId, WmsInventoryExtend wsn,
			double quantity,String qualityType);
	/**
	 * 添加移位计划明细:按托盘添加
	 * @param moveDocId
	 * @param wsn
	 */
	@Transactional
	void addMoveDocDetailByPallet(Long moveDocId, WmsInventoryExtend wsn);
	/**
	 * 添加移位计划明细:按箱号添加
	 * @param moveDocId
	 * @param wsn
	 */
	@Transactional
	void addMoveDocDetailByCarton(Long moveDocId, WmsInventoryExtend wsn);

	/** 删除移位计划 */
	@Transactional
	void deleteMoveDocPlan(WmsMoveDoc moveDoc);
	
	/** 删除移位计划明细*/
	@Transactional
	void deleteMoveDocDetails(WmsMoveDocDetail detail);
	/** 删除任务明细*/
	@Transactional
	void deleteTask(WmsTask task);

	/**
	 * 自动创建上架计划明细
	 * @param moveDoc
	 * @param inv
	 * @param quantity
	 * @param qtyBU
	 * @return
	 */
	@Transactional
	void createMoveDocDetail(WmsMoveDoc moveDoc,WmsInventory inventory);
	
	/**
	 * 自动创建上架计划明细
	 * @param moveDoc
	 * @param inv
	 * @param quantity
	 * @param qtyBU
	 * @return
	 */
	@Transactional
	void createMoveDocDetail(WmsMoveDoc moveDoc,WmsInventory inventory,Double putawayQtyBU);

	/** 
	 * 手工分配
	 * */
	@Transactional
	void manualAllocate(WmsMoveDocDetail detail, Long locationId, Double planQuantityBU);
	/** 
	 * 质检单手工分配
	 * */
	@Transactional
	void manualAllocateQuality(WmsTask task, Long locationId,
			Double planQuantityBU);
	/** 
	 * 质检单自动分配-multi
	 * */
	@Transactional
	void manualAllocateQuality(WmsMoveDoc moveDoc, WmsTask task);
	/** 
	 * 质检单自动分配-none
	 * */
	@Transactional
	void manualAllocateQuality(WmsMoveDoc moveDoc);
	/** 
	 * 质检单取消分配
	 * */
	@Transactional
	void cancelAllocateNode(WmsMoveDoc moveDoc);
	/** 
	 * 移位单激活  yc.min
	 * */
	@Transactional
	void activeMoveByJac(WmsMoveDoc moveDoc);
	/** 
	 * 移位单反激活  yc.min
	 * */
	@Transactional
	void unActiveMoveByJac(WmsMoveDoc moveDoc);
	/** 
	 * 移位单手工分配时初始化计划移位数量
	 * */
	@Transactional
	RowData getQuantityByWmsMoveDocDetail(Map map);
	/** 
	 * 质检单手工分配时初始化计划移位数量
	 * */
	@Transactional
	RowData getQuantityByWmsTask(Map map);

	/** 
	 * 移位单手工分配时初始化托盘号
	 * */
	@Transactional
	RowData getPalletByWmsMoveDocDetail(Map map);
	
	/**
	 * 拆分移位单
	 * @param 
	 * @param 
	 * */
	@Transactional
	void splitMoveDoc(WmsMoveDocDetail moveDocDetail, List<String> splitQtyList, WmsMoveDoc splitedMoveDoc);
	
	/**
	 * 退拣创建移位单
	 */
	@Transactional
	void createWmsMoveDocByPickBack(WmsTaskLog taskLog,Double quantityBU);
	
	Map printPallet(WmsMoveDoc moveDoc,Long number);
	
	/**
	 * 处理自动分配规则返回结果
	 * 
	 * @param moveDocDetail 移位单明细
	 * @param result 规则返回结果
	 */
	@Transactional
	void doAutoAllocateResult(WmsMoveDocDetail moveDocDetail, Map<String, Object> result) throws BusinessException; 
	
	/**
	 * 处理补货单自动分配返回结果
	 * @param moveDocDetail 移位单明细
	 * @param result 规则返回结果
	 */
	@Transactional
	void doReplenishmentAutoAllocateResult(WmsMoveDocDetail moveDocDetail, Map<String, Object> result) throws BusinessException;
	/**
	 * 设置备货库位
	 * @param moveDoc
	 * @return
	 */
	@Transactional
	void setShippingLocation(WmsMoveDoc moveDoc);
	
	/**
	 * 取消分配(整单取消)
	 * @param moveDoc
	 * @return
	 */
	@Transactional
	void cancelAllocateWhole(WmsMoveDoc moveDoc);
	
	/**
	 * 取消分配(部分取消)
	 * @param moveDoc, task
	 * @return
	 */
	@Transactional
	void cancelAllocatePart(WmsMoveDoc moveDoc, WmsTask task);
	
	/**
	 * 取消分配(部分取消--拣货单)
	 * @param moveDoc, task
	 * @return
	 */
	@Transactional
	void cancelAllocatePickTicketPart(WmsMoveDoc moveDoc, WmsTask task, List<String> adjustQuantityBUList);

	
	/**
	 * 获取移位计划标题
	 */
	String getTitleByMovePlanTitle(Map param);
	
	String getTitleByQualityPlanTitle(Map param);
	/**质检单头信息  yc.min*/
	@Transactional
	void storeMoveDocQuality(WmsMoveDoc moveDoc);
	
	Double getQualityPlanQuantityBu(Map param);
	/**质检单质检  yc.min*/
	@Transactional
	void storeMoveDocDetailQuality(Long parentId,WmsMoveDocDetail wdd,Long itemStateId,Double planQty);
	/**质检单批量质检状态回写  yc.min*/
	@Transactional
	void upQualityDetail(Long parentId,WmsMoveDocDetail wdd,Long itemStateId);
	
	String getQualityCompanyId(Map param);
	
	void findWorker(WmsMoveDoc moveDoc,Long workerId);
	
	String getQualityItemStates(Map param);
	/**发送质检接口 yc.min */
	@Transactional
	void sendQuality(WmsMoveDoc moveDoc);
	/**发送质检接口 yc.min */
	@Transactional
	void createQuality(Long moveDocId);
	/**读取质检接口 yc.min */
	@Transactional
	void readMidQuality(WmsMoveDocDetail wdd,Double planQty,Boolean isBeBackInv,String itemStatus);
	/**质检接口取消 yc.min */
	@Transactional
	void cancelAllTask(WmsMoveDoc moveDoc);
	/** 质检拆单 shenda.yuan*/
	void wmsMoveDocQualitysplit(WmsMoveDoc moveDoc);
	
	/**
	 * 打印送检单
	 */
	@Transactional
	public Map printSendQualityReport(WmsMoveDoc moveDoc);

	Boolean printTask(Map map);
}
