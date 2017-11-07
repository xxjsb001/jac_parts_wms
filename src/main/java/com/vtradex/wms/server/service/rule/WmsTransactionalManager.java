package com.vtradex.wms.server.service.rule;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.springframework.transaction.annotation.Transactional;

import com.vtradex.rule.server.model.rule.Version;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.move.WmsTempMoveDocDetail;
import com.vtradex.wms.server.model.shipping.WmsPickTicket;
import com.vtradex.wms.server.model.shipping.WmsWaveDoc;

@SuppressWarnings({"rawtypes"})
public interface WmsTransactionalManager extends BaseManager {
	/**
	 * 明细手工分配
	 * @param moveDocDetailId
	 * @param dstLocationId
	 * @param planQuantity
	 */
	@Transactional
	void allocate(Long moveDocDetailId,Long dstLocationId,Double planQuantity);
	
	/**
	 * 上架明细自动分配
	 * @param detail
	 * @param result
	 */
	@Transactional
	double putawayAllocate(WmsTempMoveDocDetail detail, Map<String, Object> result,String locationType);


	/**
	 * 整单取消分配
	 * @param moveDocs
	 */
	@Transactional
	void unallocateMoveDoc(WmsMoveDoc moveDoc);
	
	/**
	 * 整单取消分配-补货单
	 * 
	 * @param moveDocs
	 */
	@Transactional
	void unallocateMoveDocForReplenishment(WmsMoveDoc moveDoc);
	
	/**
	 * 1、波次分单时对发货单分配
	 * @param pickTicket,waveDoc
	 */
	@Transactional
	void autoAllocate(WmsPickTicket pickTicket,WmsWaveDoc waveDoc);
	
	
	/**
	 * 作业确认---整单自动作业确认（随机处理序列号和任务的关系）
	 * @param workDocId
	 * @param workerId
	 */
	@Transactional
	void confirmAll(Long workDocId, Long workerId);
	
	/** 单一作业确认 */
	@Transactional
	void singleWorkConfirm(WmsTask task, Long toLocationId, Long fromLocationId, Double quantityBU, Long workerId);

	/**
	 * 作业确认---按托盘作业确认
	 * @param taskId
	 * @param WmsInventoryExtendId
	 * @param toLocationId
	 * @param quantityBU
	 * @param workerId
	 */
	@Transactional
	void confirmByPallet(Long taskId,Long fromLocationId,
			Long toLocationId, Double quantityBU, Long workerId);

	/**
	 * 作业确认---按箱作业确认
	 * @param taskId
	 * @param WmsInventoryExtendId
	 * @param toLocationId
	 * @param newPallet
	 * @param quantityBU
	 * @param workerId
	 */
	@Transactional
	void confirmByCarton(Long taskId, Long fromLocationId,
			Long toLocationId, String newPallet, Double quantityBU, Long workerId);

	/**
	 * 作业确认---按序列号作业确认
	 * @param taskId
	 * @param WmsInventoryExtendId
	 * @param toLocationId
	 * @param newPallet
	 * @param newCarton
	 * @param workerId
	 */
	@Transactional
	void confirmBySerialNo(Long taskId, Long fromLocationId,Long toLocationId, 
			String newPallet, String newCarton,Double quantityBU, Long workerId);
	
	/**
	 * 作业确认---按货品作业确认
	 * @param taskId
	 * @param WmsInventoryExtendId
	 * @param toLocationId
	 * @param quantityBU
	 * @param workerId
	 */
	@Transactional
	void confirmByItem(Long taskId,Long fromLocationId, Long toLocationId, Double quantityBU, Long workerId);

	/**
	 * 作业确认
	 * @param taskId
	 * @param fromLocationId
	 * @param toLocationId
	 * @param pallet
	 * @param carton
	 * @param serialNo
	 * @param newPallet
	 * @param newCarton
	 * @param quantityBU
	 * @param workerId
	 */
	@Transactional
	void confirm(Long taskId, Long fromLocationId, Long toLocationId, String pallet, String carton, String serialNo, 
			String newPallet, String newCarton, Double quantityBU, Long workerId);
			
	/**
	 * 对波次单进行自动分单
	 * @param waveDoc
	 */
	@Transactional
	void autoSeprate(WmsWaveDoc waveDoc);
	@Transactional
	void updatePickTicketMovedQuantity(WmsMoveDocDetail mdd,
			Double movedQuantityBU);
	
	@Transactional
	List<WmsInventoryExtend> getWmsInventoryExtendByTask(WmsTask task);
	@Transactional
	void uplineRuleTable(Version version) throws HttpException, IOException;
	@Transactional
	void uplineRuleTableRf(Long versionId);
	@Transactional
	void saveQualityMoveSoiLog(Object[] obj);
}
