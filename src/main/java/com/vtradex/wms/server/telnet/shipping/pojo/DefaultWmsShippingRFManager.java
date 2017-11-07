package com.vtradex.wms.server.telnet.shipping.pojo;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.exception.OriginalBusinessException;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.base.OperatorInfo;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.move.WmsBoxDetail;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsMoveDocShipStatus;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.move.WmsTaskLog;
import com.vtradex.wms.server.model.shipping.WmsPickTicketDetail;
import com.vtradex.wms.server.model.shipping.WmsPickTicketStatus;
import com.vtradex.wms.server.model.shipping.WmsWaveDocDetail;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.telnet.shipping.WmsShippingRFManager;
import com.vtradex.wms.server.utils.WmsStringUtils;

/**
 * RF 发运
 *
 * @category Manager
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2015/03/25 02:48:55 $
 */
public class DefaultWmsShippingRFManager extends DefaultBaseManager implements WmsShippingRFManager {
	
	private WmsInventoryManager wmsInventoryManager;
	private WorkflowManager workflowManager;
	
	public DefaultWmsShippingRFManager(WmsInventoryManager wmsInventoryManager, WorkflowManager workflowManager) {
		this.wmsInventoryManager = wmsInventoryManager;
		this.workflowManager = workflowManager;
	}

	/**
	 * 按面单发运
	 * 
	 * @param boxNo 面单包装箱号
	 */
	@Transactional
	public void shippingByBoxNo(String boxNo) throws BusinessException {
		String boxHql = "FROM WmsBoxDetail b where b.boxNo = :boxNo";
		
		List<WmsBoxDetail> boxList = commonDao.findByQuery(boxHql, "boxNo", boxNo);
		if (boxList == null || boxList.isEmpty()) {
			throw new BusinessException("箱号不存在");
		}
		for (WmsBoxDetail box : boxList) {
			if (box.getBeShipping()) {
				throw new BusinessException("当前箱号已发运");
			}
			WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, box.getMoveDoc().getId());
			WmsMoveDocDetail moveDocDetail = commonDao.load(WmsMoveDocDetail.class, box.getMoveDocDetail().getId());
			
			if(moveDoc.getBeWave()) {
				WmsMoveDocDetail originMoveDocDetail = commonDao.load(WmsMoveDocDetail.class, moveDocDetail.getId());
				//String shipLotInfo = getLotInfoString(originMoveDocDetail);
				//通过原始移位(拣货)单ID查找对应的波次单明细
				String hql = "FROM WmsWaveDocDetail waveDocDetail WHERE waveDocDetail.moveDocDetail.id=:moveDocDetailId";
				List<WmsWaveDocDetail> waveDocDetails = commonDao.findByQuery(hql, 
					new String[] {"moveDocDetailId"}, new Object[] {moveDocDetail.getId()});
				hql = null;
				//通过原始移位单的拣货移位数量控制发运数量
				//原因：波次批拣时无法直接找到对应的TaskLog，只能通过找出波次批拣对应的TaskLog，循环扣取发运数量
				//注：循环扣取发运数量时，目前版本不对TaskLog的数量进行控制
				double planShipQuantity = originMoveDocDetail.getMovedQuantityBU();
				for(WmsWaveDocDetail waveDocDetail : waveDocDetails) {
					//根据波次单明细的关联ID查找对应的波次拣货移位明细
//					hql = "FROM WmsMoveDocDetail moveDocDetail WHERE moveDocDetail.id=:moveDocDetailId";
//					List<WmsMoveDocDetail> moveDocDetails = commonDao.findByQuery(hql, 
//						new String[] {"moveDocDetailId"}, new Object[] {waveDocDetail.getRelatedId()});
					//发运确认
					for(WmsMoveDocDetail detail : waveDocDetail.getWaveMoveDocDetails()) {
						if(detail.getMovedQuantityBU() < detail.getPlanQuantityBU()){
							continue;
						}
						WmsMoveDoc md = commonDao.load(WmsMoveDoc.class, detail.getMoveDoc().getId());
						if(!md.getStatus().equals(WmsMoveDocStatus.FINISHED)) {
							throw new OriginalBusinessException("BOL状态不正确！");
						}
						hql = "FROM WmsTaskLog log WHERE log.task.planQuantityBU > 0 AND log.task.movedQuantityBU > 0 AND  log.task.moveDocDetail.id=:moveDocDetailId AND log.itemKey.item.id=:itemId AND log.bePickBack = false";
						List<WmsTaskLog> logs = commonDao.findByQuery(hql, 
							new String[] {"moveDocDetailId", "itemId"}, new Object[] {detail.getId(), originMoveDocDetail.getItem().getId()});
						for(WmsTaskLog log : logs) {
							WmsItemKey itemKey = commonDao.load(WmsItemKey.class, log.getItemKey().getId());
//							if(shipLotInfo != null && !"".equals(shipLotInfo) 
//								&& !shipLotInfo.equals(getLotInfoString(itemKey))) {
//								continue;
//							}
							//当原始移位单拣货数量小于等于0，终止循环
							if(planShipQuantity <= 0) {
								break;
							}
							WmsTask task = commonDao.load(WmsTask.class, log.getTask().getId());
							WmsMoveDocDetail waveMoveDocDetail = commonDao.load(WmsMoveDocDetail.class, task.getMoveDocDetail().getId());
							WmsMoveDoc waveMoveDoc = commonDao.load(WmsMoveDoc.class, waveMoveDocDetail.getMoveDoc().getId());
							//比较原始移位单拣货数量与TaskLog的实际移位数量哪个较大，取较小值作为库存扣减数量
							double shipQty = planShipQuantity >= log.getMovedQuantityBU() ? log.getMovedQuantityBU() : planShipQuantity;
							//从原始移位单拣货数量上扣除已经移位的数量
							planShipQuantity -= shipQty;
							//发运确认扣减库存
							wmsInventoryManager.ship(log, shipQty);
							//清除相应的库存子表信息
							removeInventoryExtendQtyTaskLog(log, shipQty);
							//波次移位(拣货)单发运确认
							waveMoveDocDetail.ship(shipQty);
							commonDao.store(waveMoveDocDetail);
							//因为页面只对原始移位单做发运流程控制，因此需要手工调用移位单发运确认流程改写波次移位(拣货)单状态
							if(WmsMoveDocShipStatus.UNSHIP.equals(waveMoveDoc.getShipStatus())) {
								workflowManager.doWorkflow(waveMoveDoc, "wmsMoveDocShipProcess.ship");
							}
							//原始移位单发运确认,改写发运数量
							originMoveDocDetail.ship(shipQty);
							commonDao.store(originMoveDocDetail);
							//对应发货单回写
							WmsPickTicketDetail pickTickDetail = commonDao.load(WmsPickTicketDetail.class, originMoveDocDetail.getRelatedId());
							pickTickDetail.ship(shipQty);
							commonDao.store(pickTickDetail);
							workflowManager.doWorkflow(pickTickDetail.getPickTicket(), "wmsPickTicketBaseProcess.ship");
							
							if(pickTickDetail.getPickTicket().getStatus().equals(WmsPickTicketStatus.FINISHED) && moveDoc.getShippedQuantityBU()>=moveDoc.getPlanQuantityBU()) {
								if(WmsMoveDocShipStatus.UNSHIP.equals(moveDoc.getShipStatus())) {
									moveDoc.setShipStatus(WmsMoveDocShipStatus.SHIPPED);
								}
							}
						}
					}
				}
				if(planShipQuantity > 0) {
					throw new OriginalBusinessException("未找到对应的拣货记录！");
				}
			} else {
			
				String hql = "FROM WmsTaskLog log WHERE log.task.planQuantityBU > 0 " +
						" AND log.task.moveDocDetail.id=:moveDocDetailId " +
						" AND log.task.movedQuantityBU > 0 " +
						" AND log.bePickBack = false " +
						" ORDER BY log.movedQuantityBU DESC";
				List<WmsTaskLog> taskLogList = commonDao.findByQuery(hql, new String[] {"moveDocDetailId"}, new Object[] {moveDocDetail.getId()});
				
				Double planShipingQuantity = box.getQuantity();
				for (WmsTaskLog taskLog : taskLogList) {
					if(planShipingQuantity <= 0) {
						break;
					}
					double shippingQty = planShipingQuantity >= taskLog.getMovedQuantityBU() ? taskLog.getMovedQuantityBU() : planShipingQuantity;
					planShipingQuantity -= shippingQty;
					wmsInventoryManager.ship(taskLog, shippingQty);
					removeInventoryExtendQtyTaskLog(taskLog, shippingQty);
					taskLog.getTask().getMoveDocDetail().ship(shippingQty);
					WmsPickTicketDetail pickTickDetail = commonDao.load(WmsPickTicketDetail.class, taskLog.getTask().getMoveDocDetail().getRelatedId());
					pickTickDetail.ship(shippingQty);
					workflowManager.doWorkflow(pickTickDetail.getPickTicket(), "wmsPickTicketBaseProcess.ship");
					
					if(pickTickDetail.getPickTicket().getStatus().equals(WmsPickTicketStatus.FINISHED) && moveDoc.getShippedQuantityBU()>=moveDoc.getPlanQuantityBU()) {
						workflowManager.doWorkflow(moveDoc, "wmsMoveDocShipProcess.ship");
					}
				}
			}
			
			box.setBeShipping(Boolean.TRUE);
			box.setShippingOperator(new OperatorInfo(new Date(), UserHolder.getUser().getId(), UserHolder.getUser().getName()));
		}
	}
	
	@SuppressWarnings("unchecked")
	private void removeInventoryExtendQtyTaskLog(WmsTaskLog taskLog, double quantityBU) {
		List<WmsInventoryExtend> inventoryExtends = null;
		if (!WmsStringUtils.isEmpty(taskLog.getFromSerialNo())) {
			//一个taskLog对应一个序列号(托盘+箱号+序列号的，在taskLog层面应拆解为一个序列号一条记录)
			inventoryExtends = commonDao.findByQuery("FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId AND wsn.serialNo = :serialNo", 
				new String[] {"inventoryId", "serialNo"}, new Object[] {taskLog.getDescInventoryId(), taskLog.getFromSerialNo()});
		} else if (!WmsStringUtils.isEmpty(taskLog.getToCarton())) {
			//一个taskLog对应一个箱号
			inventoryExtends = commonDao.findByQuery("FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId AND wsn.carton = :carton", 
				new String[] {"inventoryId", "carton"}, new Object[] {taskLog.getDescInventoryId(), taskLog.getToCarton()});
		} else if (!WmsStringUtils.isEmpty(taskLog.getToPallet())) {
			//一个taskLog对应一个托盘
			inventoryExtends = commonDao.findByQuery("FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId AND wsn.pallet = :pallet", 
				new String[] {"inventoryId", "pallet"}, new Object[] {taskLog.getDescInventoryId(), taskLog.getToPallet()});
		}
		if(inventoryExtends == null || inventoryExtends.size() == 0) {
			//一个taskLog对应一种货品
			inventoryExtends = commonDao.findByQuery("FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId", 
				new String[] {"inventoryId"}, new Object[] {taskLog.getDescInventoryId()});
			quantityBU = removeInventoryExtendQty(inventoryExtends, quantityBU);
		} else {
			quantityBU = removeInventoryExtendQty(inventoryExtends, quantityBU);
			if (quantityBU > 0) {
				inventoryExtends = commonDao.findByQuery("FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId", 
					new String[] {"inventoryId"}, new Object[] {taskLog.getDescInventoryId()});
				quantityBU = removeInventoryExtendQty(inventoryExtends, quantityBU);
			}
		}		
		if (quantityBU > 0) {
			throw new BusinessException("库存数量不足！");
		}
	}
	
	private Double removeInventoryExtendQty(List<WmsInventoryExtend> inventoryExtends, double quantityBU) {
		for (WmsInventoryExtend inventoryExtend : inventoryExtends){
			Double moveQty = 0D;
			if (inventoryExtend.getQuantityBU() >= quantityBU) {
				moveQty = quantityBU;
			} else {
				moveQty = inventoryExtend.getQuantityBU();
			}
			quantityBU -= moveQty;
			
			inventoryExtend.removeQuantity(moveQty);
			if (inventoryExtend.getQuantityBU().doubleValue() <= 0) {
				commonDao.delete(inventoryExtend);
			}
		}
		return quantityBU;
	}
}
