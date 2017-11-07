package com.vtradex.wms.server.service.receiving.pojo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;

import com.vtradex.rule.server.loader.IRuleTableLoader;
import com.vtradex.thorn.client.ui.page.IPage;
import com.vtradex.thorn.client.ui.table.RowData;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.exception.OriginalBusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.ShipLotInfo;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.inventory.WmsInventoryLogType;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.model.move.WmsMoveDocType;
import com.vtradex.wms.server.model.move.WmsQualityStatus;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.move.WmsTaskLog;
import com.vtradex.wms.server.model.move.WmsTaskStatus;
import com.vtradex.wms.server.model.move.WmsWorkDocStatus;
import com.vtradex.wms.server.model.organization.TypeOfBill;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsBlgItem;
import com.vtradex.wms.server.model.organization.WmsItemState;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.model.shipping.WmsBOLStateLog;
import com.vtradex.wms.server.model.shipping.WmsPickTicket;
import com.vtradex.wms.server.model.shipping.WmsPickTicketDetail;
import com.vtradex.wms.server.model.shipping.WmsWaveDoc;
import com.vtradex.wms.server.model.shipping.WmsWaveDocDetail;
import com.vtradex.wms.server.model.shipping.WmsWaveDocWorkMode;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
import com.vtradex.wms.server.service.base.WmsBillTypeManager;
import com.vtradex.wms.server.service.interfaces.WmsDealInterfaceDataManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.middle.MiddleTableName;
import com.vtradex.wms.server.service.receiving.WmsMoveDocManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.rule.WmsTransactionalManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.task.WmsTaskManager;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.LotInfoUtil;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.utils.NewLotInfoParser;
import com.vtradex.wms.server.utils.PackageUtils;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.server.utils.WmsStringUtils;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

/**
 * 移位管理
 * 
 * @category Manager
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.30 $Date: 2017/05/10 02:34:09 $
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DefaultWmsMoveDocManager extends DefaultBaseManager implements
		WmsMoveDocManager {

	private WmsBussinessCodeManager wmsBussinessCodeManager;
	private WmsTransactionalManager wmsTransactionalManager;
	private WorkflowManager workflowManager;
	protected WmsBillTypeManager wmsBillTypeManager;
	protected WmsBussinessCodeManager codeManager;
	protected WmsRuleManager ruleManager;
	protected WmsInventoryManager wmsInventoryManager;
	protected WmsTaskManager wmsTaskManager;
	protected IRuleTableLoader ruleTableLoader;
	private WmsDealInterfaceDataManager wmsDealInterfaceDataManager;

	public DefaultWmsMoveDocManager(
			WmsBussinessCodeManager wmsBussinessCodeManager,
			WmsTransactionalManager wmsTransactionalManager,
			WorkflowManager workflowManager,
			WmsBillTypeManager wmsBillTypeManager,
			WmsBussinessCodeManager codeManager, WmsRuleManager ruleManager,
			WmsInventoryManager wmsInventoryManager,
			WmsTaskManager wmsTaskManager,IRuleTableLoader ruleTableLoader,
			WmsDealInterfaceDataManager wmsDealInterfaceDataManager) {

		this.wmsBussinessCodeManager = wmsBussinessCodeManager;
		this.wmsTransactionalManager = wmsTransactionalManager;
		this.workflowManager = workflowManager;
		this.wmsBillTypeManager = wmsBillTypeManager;
		this.codeManager = codeManager;
		this.ruleManager = ruleManager;
		this.wmsInventoryManager = wmsInventoryManager;
		this.wmsTaskManager = wmsTaskManager;
		this.ruleTableLoader = ruleTableLoader;
		this.wmsDealInterfaceDataManager = wmsDealInterfaceDataManager;
	}

	/**
	 * 创建移位单
	 * 
	 * @param entity
	 */
	public void createWmsMoveDoc(Object entity) throws BusinessException {
		if (entity == null) {
			throw new BusinessException("create.wmsmovedoc.entity.isnull");
		}
		if (entity instanceof WmsPickTicket) {
			createWmsMoveDocByPickTicket((WmsPickTicket) entity);
		} else if (entity instanceof WmsWaveDoc) {
			createWmsMoveDocByWaveDoc((WmsWaveDoc) entity);
		} else if (entity instanceof WmsASN) {

		} else {
			throw new BusinessException(
					"create.wmsmovedoc.entity.nonsupport.instanceof");
		}
	}

	/**
	 * 自动分配
	 * 
	 * @param moveDoc
	 *            移位单
	 */
	public void autoAllocate(WmsMoveDocDetail moveDocDetail)
			throws BusinessException {
		moveDocDetail = commonDao.load(WmsMoveDocDetail.class,
				moveDocDetail.getId());

		if (moveDocDetail.getUnAllocateQuantityBU() <= 0) {
			return;
		}
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, moveDocDetail
				.getMoveDoc().getId());
		moveDocDetail.setMoveDoc(moveDoc);

		Map<String, Object> problem = new HashMap<String, Object>();
		problem.put("仓库ID", moveDoc.getWarehouse().getId());
		problem.put("货主", moveDocDetail.getItem().getCompany().getName());
		problem.put("货品ID", moveDocDetail.getItem().getId());
		problem.put("货品编码", moveDocDetail.getItem().getCode());
		problem.put("待拣选数量", moveDocDetail.getUnAllocateQuantityBU());
		problem.put("库存状态", moveDocDetail.getInventoryStatus());
		if (WmsMoveDocType.MV_PICKTICKET_PICKING.equals(moveDoc.getType())) {
			WmsPickTicket pickTicket = commonDao.load(WmsPickTicket.class,
					moveDoc.getPickTicket().getId());
			if (pickTicket.getCustomer() != null) {
				WmsOrganization customer = commonDao
						.load(WmsOrganization.class, pickTicket.getCustomer()
								.getId());
				problem.put("收货人", pickTicket.getCustomer() == null ? ""
						: customer.getName());
			} else {
				problem.put("收货人", "");
			}
		} else {
			problem.put("收货人", "");
		}

		ShipLotInfo shipLotInfo = moveDocDetail.getShipLotInfo();
		if (shipLotInfo == null) {
			shipLotInfo = new ShipLotInfo();
		}
		/**
		 * FIXME Boolean.TRUE 表示严格批次匹配发货, 实际项目中可供用户灵活设置该属性
		 */
		LotInfoUtil.generateShipLotInfo(problem, shipLotInfo, Boolean.TRUE);

		String orgName = moveDoc.getCompany().getName();
		if (moveDoc.getCompany().isBeVirtual()) {
			orgName = moveDoc.getWarehouse().getName();
		}
		Map<String, Object> result = ruleManager.execute(moveDoc.getWarehouse()
				.getName(), orgName, "拣货分配规则", problem);
		doAutoAllocateResult(moveDocDetail, result);
		workflowManager.doWorkflow(moveDoc, "wmsMoveDocProcess.allocate"); // 调用分配流程,
																			// 判断是否整单分配
	}

	public void manuelAllocate(Long moveDocDetailId,
			Map<Long, Double> allocateInfo) {
		WmsMoveDocDetail moveDocDetail = commonDao.load(WmsMoveDocDetail.class,
				moveDocDetailId);
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, moveDocDetail
				.getMoveDoc().getId());
		doManualAllocateResult(moveDocDetail, allocateInfo);
		workflowManager.doWorkflow(moveDoc, "wmsMoveDocProcess.allocate"); // 调用分配流程,
																			// 判断是否整单分配
	}

	/**
	 * 取消分配
	 * 
	 * @param moveDoc
	 *            移位单
	 */
	public void cancelAllocate(WmsMoveDoc moveDoc) {
		String hql = "SELECT d.id FROM WmsMoveDocDetail d where d.moveDoc.id = :moveDocId and d.allocatedQuantityBU > 0";
		List<Long> moveDocDetailIdList = commonDao.findByQuery(hql,
				"moveDocId", moveDoc.getId());
//		if (moveDocDetailIdList.isEmpty()) {
//			throw new BusinessException("wmsmovedoc.detail.not.exist");
//		}
		String taskHql = "SELECT t.id FROM WmsTask t where t.moveDocDetail.id = :moveDocDetailId and t.planQuantityBU>0";
		for (Long moveDocDetailId : moveDocDetailIdList) {
			WmsMoveDocDetail moveDocDetail = commonDao.load(
					WmsMoveDocDetail.class, moveDocDetailId);
			List<Long> taskIdList = commonDao.findByQuery(taskHql,
					"moveDocDetailId", moveDocDetail.getId());
			List<WmsTask> readyDeleteTaskList = new ArrayList<WmsTask>(500);
			for (Long taskId : taskIdList) {
				WmsTask task = commonDao.load(WmsTask.class, taskId);
				Double unallocateQty = task.getUnmovedQuantityBU();
				WmsInventory fromInv = commonDao.load(WmsInventory.class,
						task.getSrcInventoryId());
				fromInv.unallocatePickup(unallocateQty);

				wmsInventoryManager.refreshLocationUseRate(fromInv.getLocation(),  0);

				task.unallocatePick(unallocateQty);
				commonDao.store(task);

				if (task.getPlanQuantityBU().doubleValue() <= 0) {
					String deleteTaskLog = "DELETE FROM WmsTaskLog taskLog where taskLog.task.id = :taskId";
					commonDao.executeByHql(deleteTaskLog, "taskId", task.getId());
					readyDeleteTaskList.add(task);
				}
				doProcessEntityUnAllocateResult(moveDocDetail, unallocateQty);

			}
			commonDao.deleteAll(readyDeleteTaskList);
		}
		workflowManager.doWorkflow(moveDoc,
				"wmsMoveDocProcess.cancelAllocateNode");
	}

	private void doProcessEntityUnAllocateResult(
			WmsMoveDocDetail moveDocDetail, double unallocateQty) {
		if (WmsMoveDocType.MV_PICKTICKET_PICKING.equals(moveDocDetail
				.getMoveDoc().getType())) {
			WmsPickTicketDetail pickTicketDetail = commonDao.load(
					WmsPickTicketDetail.class, moveDocDetail.getRelatedId());
			pickTicketDetail.unallocate(unallocateQty);
			commonDao.store(pickTicketDetail);
		} else if (WmsMoveDocType.MV_MOVE.equals(moveDocDetail.getMoveDoc()
				.getType())) {

		} else if (WmsMoveDocType.MV_WAVE_PICKING.equals(moveDocDetail
				.getMoveDoc().getType())) {
			WmsWaveDocDetail waveDocDetail = commonDao.load(
					WmsWaveDocDetail.class, moveDocDetail.getRelatedId());
			if (WmsWaveDocWorkMode.WORK_BY_DOC.equals(waveDocDetail
					.getWaveDoc().getWorkMode())) {
				waveDocDetail.unallocate(unallocateQty);
				commonDao.store(waveDocDetail);

				WmsPickTicketDetail pickTicketDetail = commonDao.load(
						WmsPickTicketDetail.class, waveDocDetail
								.getMoveDocDetail().getRelatedId());
				pickTicketDetail.unallocate(unallocateQty);
				commonDao.store(pickTicketDetail);
			} else {

			}
		} else if (WmsMoveDocType.MV_REPLENISHMENT_MOVE.equals(moveDocDetail
				.getMoveDoc().getType())) {

		} else if (WmsMoveDocType.MV_PROCESS_PICKING.equals(moveDocDetail
				.getMoveDoc().getType())) {
			// TODO 回写加工单问题
			Double totalQuantity = (Double)commonDao.findByQueryUniqueResult("Select sum(planQuantityBU) From WmsMoveDocDetail wmdd Where wmdd.moveDoc.id=:moveDocId",
					new String[] { "moveDocId" },new Object[] { moveDocDetail.getMoveDoc().getId() });
			Double allocatedPack = moveDocDetail.getMoveDoc().getAllocatedQuantityBU();
			Double pack = moveDocDetail.getMoveDoc().getPlanQuantityBU() * (unallocateQty / totalQuantity);
			allocatedPack = DoubleUtils.format3Fraction(allocatedPack-pack);
			if(Math.abs(allocatedPack) < 0.01) {
				allocatedPack = DoubleUtils.format2Fraction(allocatedPack);
			}
			moveDocDetail.getMoveDoc().setAllocatedQuantityBU(allocatedPack);
		}
	}

	/**
	 * 取消分配
	 * 
	 * @param moveDocId
	 *            移位单ID
	 */
	public void cancelAllocate(Long moveDocId) {
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, moveDocId);
		String hql = "SELECT d.id FROM WmsMoveDocDetail d where d.moveDoc.id = :moveDocId and d.allocatedQuantityBU > 0";
		List<Long> moveDocDetailIdList = commonDao.findByQuery(hql,
				"moveDocId", moveDoc.getId());
		if (moveDocDetailIdList.isEmpty()) {
			throw new BusinessException("wmsmovedoc.detail.not.exist");
		}
		String taskHql = "SELECT t.id FROM WmsTask t where t.moveDocDetail.id = :moveDocDetailId";
		for (Long moveDocDetailId : moveDocDetailIdList) {
			WmsMoveDocDetail moveDocDetail = commonDao.load(
					WmsMoveDocDetail.class, moveDocDetailId);
			List<Long> taskIdList = commonDao.findByQuery(taskHql,
					"moveDocDetailId", moveDocDetail.getId());
			List<WmsTask> readyDeleteTaskList = new ArrayList<WmsTask>(500);
			for (Long taskId : taskIdList) {
				WmsTask task = commonDao.load(WmsTask.class, taskId);
				Double unallocateQty = task.getUnmovedQuantityBU();
				WmsInventory fromInv = commonDao.load(WmsInventory.class,
						task.getSrcInventoryId());
				fromInv.unallocatePickup(unallocateQty);
				moveDocDetail.unallocate(unallocateQty);
				if(moveDocDetail.getMoveDoc().getType().equals(WmsMoveDocType.MV_PROCESS_PICKING)) {
					// TODO 回写加工单问题
					Double totalQuantity = (Double)commonDao.findByQueryUniqueResult("Select sum(planQuantityBU) From WmsMoveDocDetail wmdd Where wmdd.moveDoc.id=:moveDocId",
							new String[] { "moveDocId" },new Object[] { moveDocDetail.getMoveDoc().getId() });
					Double allocatedPack = moveDocDetail.getMoveDoc().getAllocatedQuantityBU();
					Double pack = moveDocDetail.getMoveDoc().getPlanQuantityBU() * (unallocateQty / totalQuantity);
					allocatedPack = DoubleUtils.format3Fraction(allocatedPack-pack);
					if(Math.abs(moveDocDetail.getMoveDoc().getPlanQuantityBU() - allocatedPack) < 0.01) {
						allocatedPack = DoubleUtils.format2Fraction(allocatedPack);
					}
					moveDocDetail.getMoveDoc().setAllocatedQuantityBU(allocatedPack);
				}
				WmsPickTicketDetail pickTicketDetail = load(
						WmsPickTicketDetail.class, moveDocDetail.getRelatedId());
				if (pickTicketDetail != null) {
					pickTicketDetail.unallocate(unallocateQty);
				}
				readyDeleteTaskList.add(task);
			}
			commonDao.deleteAll(readyDeleteTaskList);

			if (WmsMoveDocType.MV_PICKTICKET_PICKING.equals(moveDoc.getType())) {
				WmsPickTicketDetail pickTicketDetail = commonDao
						.load(WmsPickTicketDetail.class,
								moveDocDetail.getRelatedId());
				pickTicketDetail.unallocate(moveDocDetail
						.getAllocatedQuantityBU());
				commonDao.store(pickTicketDetail);
			} else if (WmsMoveDocType.MV_MOVE.equals(moveDoc.getType())) {

			} else if (WmsMoveDocType.MV_WAVE_PICKING.equals(moveDoc.getType())) {

			} else if (WmsMoveDocType.MV_REPLENISHMENT_MOVE.equals(moveDoc
					.getType())) {

			} else if (WmsMoveDocType.MV_PROCESS_PICKING.equals(moveDoc
					.getType())) {

			}
		}
		workflowManager.doWorkflow(moveDoc,
				"wmsMoveDocProcess.cancelAllocateNode");
	}

	/**
	 * 手工取消分配
	 * 
	 * @param moveDoc
	 *            移位单
	 */
	public void manualCancelAllocate(Long moveDocId,
			Map<Long, Double> cancelInfo) {
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, moveDocId);
		List<WmsTask> readyDeleteTaskList = new ArrayList<WmsTask>(500);
		for (Long taskId : cancelInfo.keySet()) {
			WmsTask task = commonDao.load(WmsTask.class, taskId);
			Double unallocateQty = cancelInfo.get(taskId);
			if (task.getPlanQuantityBU() < unallocateQty) {
				throw new OriginalBusinessException("取消分配数量超过了待移位数量！");
			}
			WmsInventory fromInv = commonDao.load(WmsInventory.class,
					task.getSrcInventoryId());
			fromInv.unallocatePickup(unallocateQty);
			task.unallocatePick(unallocateQty);
			commonDao.store(task);

			if (task.getPlanQuantityBU().doubleValue() <= 0) {
				readyDeleteTaskList.add(task);
			}

			doProcessEntityUnAllocateResult(task.getMoveDocDetail(),
					unallocateQty);
		}
		if (readyDeleteTaskList != null && readyDeleteTaskList.size() > 0) {
			commonDao.deleteAll(readyDeleteTaskList);
		}

		workflowManager.doWorkflow(moveDoc,
				"wmsMoveDocProcess.cancelAllocateNode");
	}

	/**
	 * 处理自动分配规则返回结果
	 * 
	 * @param moveDocDetail
	 *            移位单明细
	 * @param result
	 *            规则返回结果
	 */
	public void doAutoAllocateResult(WmsMoveDocDetail moveDocDetail,
			Map<String, Object> result) throws BusinessException {
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) result
				.get("返回列表");

		for (Map<String, Object> allocateObject : resultList) {
			double allocateQuantityBU = Double.valueOf(allocateObject.get(
					"分配数量").toString());
			if (allocateQuantityBU == 0) {
				continue;
			}
			Long srcInventoryId = Long.valueOf(allocateObject.get("库存ID")
					.toString());

			// 改写源库存待拣货数量
			WmsInventory srcInv = commonDao.load(WmsInventory.class,
					srcInventoryId);
			WmsItemKey itemKey = commonDao.load(WmsItemKey.class, srcInv
					.getItemKey().getId());
			srcInv.allocatePickup(allocateQuantityBU);
			commonDao.store(srcInv);

			moveDocDetail.setInventoryId(srcInv.getId());
			moveDocDetail.setFromLocationId(srcInv.getLocation().getId());
			moveDocDetail.setFromLocationCode(srcInv.getLocation().getCode());
			moveDocDetail.allocate(allocateQuantityBU);
			
			commonDao.store(moveDocDetail);
			wmsTaskManager.createWmsTask(moveDocDetail, itemKey,
					srcInv.getStatus(), allocateQuantityBU);
			
			doProcessEntityAllocateResult(moveDocDetail, allocateQuantityBU);
		}
		workflowManager.doWorkflow(moveDocDetail.getMoveDoc(), "wmsMoveDocProcess.allocate"); // 调用分配流程, 判断是否整单分配
	}

	/**
	 * 处理补货单自动分配返回结果
	 * 
	 * @param moveDocDetail
	 *            移位单明细
	 * @param result
	 *            规则返回结果
	 */
	public void doReplenishmentAutoAllocateResult(
			WmsMoveDocDetail moveDocDetail, Map<String, Object> result)
			throws BusinessException {
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("上架分配返回列表");

		for (Map<String, Object> allocateObject : resultList) {
			double quantityBU = Double.valueOf(allocateObject.get("上架数量BU").toString());
			if (quantityBU == 0) {
				continue;
			}
			Long srcInventoryId = Long.valueOf(allocateObject.get("库存序号").toString());
			Long toLocationId = Long.valueOf(allocateObject.get("库位序号").toString());
			Long packageUnitId = Long.valueOf(allocateObject.get("包装序号").toString());

			// 改写源库存待拣货数量
			WmsInventory srcInv = commonDao.load(WmsInventory.class, srcInventoryId);
			srcInv.allocatePickup(quantityBU);
			commonDao.store(srcInv);

			moveDocDetail.setInventoryId(srcInv.getId());
			moveDocDetail.setFromLocationId(srcInv.getLocation().getId());
			moveDocDetail.setFromLocationCode(srcInv.getLocation().getCode());
			moveDocDetail.allocate(quantityBU);
			commonDao.store(moveDocDetail);

			// 回写目标库存并生成Task
			WmsLocation toLocation = commonDao.load(WmsLocation.class, toLocationId);
			WmsPackageUnit packageUnit = commonDao.load(WmsPackageUnit.class, packageUnitId);
			WmsInventory dstInventory = wmsInventoryManager
					.getInventoryWithNew(toLocation, srcInv.getItemKey(),
							packageUnit, moveDocDetail.getInventoryStatus());
			
			dstInventory.allocatePutaway(quantityBU);
			commonDao.store(dstInventory);
			wmsTaskManager.getTaskByConditions(moveDocDetail,
					srcInv.getLocation(), toLocation, srcInv.getItemKey(),
					packageUnit, moveDocDetail.getInventoryStatus(),
					quantityBU, srcInventoryId, dstInventory.getId(),
					Boolean.FALSE);

			// 调用规则刷新库满度
			wmsInventoryManager.refreshLocationUseRate(toLocation, 0);
			commonDao.store(toLocation);

			doProcessEntityAllocateResult(moveDocDetail,
					moveDocDetail.getAllocatedQuantityBU());
		}
	}

	private void doProcessEntityAllocateResult(WmsMoveDocDetail moveDocDetail,
			double allocateQuantityBU) {
		if (WmsMoveDocType.MV_PICKTICKET_PICKING.equals(moveDocDetail
				.getMoveDoc().getType())) {
			WmsPickTicketDetail pickTicketDetail = commonDao.load(
					WmsPickTicketDetail.class, moveDocDetail.getRelatedId());
			pickTicketDetail.allocate(allocateQuantityBU);
			commonDao.store(pickTicketDetail);
		} else if (WmsMoveDocType.MV_MOVE.equals(moveDocDetail.getMoveDoc()
				.getType())) {

		} else if (WmsMoveDocType.MV_WAVE_PICKING.equals(moveDocDetail
				.getMoveDoc().getType())) {
			WmsWaveDocDetail waveDocDetail = commonDao.load(
					WmsWaveDocDetail.class, moveDocDetail.getRelatedId());
			if (WmsWaveDocWorkMode.WORK_BY_DOC.equals(waveDocDetail
					.getWaveDoc().getWorkMode())) {
				waveDocDetail.allocate(allocateQuantityBU);
				commonDao.store(waveDocDetail);

				WmsPickTicketDetail pickTicketDetail = commonDao.load(
						WmsPickTicketDetail.class, waveDocDetail
								.getMoveDocDetail().getRelatedId());
				pickTicketDetail.allocate(allocateQuantityBU);
				commonDao.store(pickTicketDetail);
			} else {
				/**
				 * TODO 批拣 分单分配
				 */
				// allocateByBatch(moveDocDetail,allocateQuantityBU);
				// waveDocDetail.getWaveDoc().allocate(allocateQuantityBU);
			}

		} else if (WmsMoveDocType.MV_REPLENISHMENT_MOVE.equals(moveDocDetail
				.getMoveDoc().getType())) {

		} else if (WmsMoveDocType.MV_PROCESS_PICKING.equals(moveDocDetail
				.getMoveDoc().getType())) {
			if(moveDocDetail.getMoveDoc().getType().equals(WmsMoveDocType.MV_PROCESS_PICKING)) {
				// TODO 回写加工单问题
				Double totalQuantity = (Double)commonDao.findByQueryUniqueResult("Select sum(planQuantityBU) From WmsMoveDocDetail wmdd Where wmdd.moveDoc.id=:moveDocId",
						new String[] { "moveDocId" },new Object[] { moveDocDetail.getMoveDoc().getId() });
				Double allocatedPack = moveDocDetail.getMoveDoc().getAllocatedQuantityBU();
				Double pack = moveDocDetail.getMoveDoc().getPlanQuantityBU() * (allocateQuantityBU / totalQuantity);
				allocatedPack = DoubleUtils.format3Fraction(allocatedPack+pack);
				if(Math.abs(moveDocDetail.getMoveDoc().getPlanQuantityBU() - allocatedPack) < 0.01) {
					allocatedPack = DoubleUtils.format2Fraction(allocatedPack);
				}
				moveDocDetail.getMoveDoc().setAllocatedQuantityBU(allocatedPack);
			} 
		}
	}

	/**
	 * 处理自动分配规则返回结果
	 * 
	 * @param moveDocDetail
	 *            移位单明细
	 * @param allocateInfo
	 *            库存手工分配信息
	 */
	private void doManualAllocateResult(WmsMoveDocDetail moveDocDetail,
			Map<Long, Double> allocateInfo) throws BusinessException {
		for (Long srcInventoryId : allocateInfo.keySet()) {
			double allocateQuantityBU = allocateInfo.get(srcInventoryId);
			if (allocateQuantityBU == 0) {
				continue;
			}

			if (moveDocDetail.getUnAllocateQuantityBU() < allocateQuantityBU) {
				throw new OriginalBusinessException("库存分配数量超过了移位单的待分配数量！");
			}

			// 改写源库存待拣货数量
			WmsInventory srcInv = commonDao.load(WmsInventory.class,
					srcInventoryId);
			WmsItemKey itemKey = commonDao.load(WmsItemKey.class, srcInv
					.getItemKey().getId());
			srcInv.allocatePickup(allocateQuantityBU);
			commonDao.store(srcInv);

			moveDocDetail.setInventoryId(srcInv.getId());
			moveDocDetail.setFromLocationId(srcInv.getLocation().getId());
			moveDocDetail.setFromLocationCode(srcInv.getLocation().getCode());
			moveDocDetail.allocate(allocateQuantityBU);
			
			commonDao.store(moveDocDetail);
			wmsTaskManager.createWmsTask(moveDocDetail, itemKey,
					srcInv.getStatus(), allocateQuantityBU);

			doProcessEntityAllocateResult(moveDocDetail, allocateQuantityBU);
		}
	}

	/**
	 * 根据发货单创建移位单
	 * 
	 * @param pickTicket
	 *            发货单
	 * @throws BusinessException
	 */
	private void createWmsMoveDocByPickTicket(WmsPickTicket pickTicket)
			throws BusinessException {
		String warehouseName = pickTicket.getWarehouse().getName();
		String orgName = pickTicket.getCompany().getName();
		Map<String,Object> problem = new HashMap<String, Object>();
		problem.put("发货单序号", pickTicket.getId());
		Map<String,Object> result = ruleManager.execute(warehouseName, orgName, "拣货单生成规则", problem);
		List<List<Map<String,Object>>> list = (List<List<Map<String,Object>>>)result.get("返回列表");
		for(List<Map<String,Object>> moveList : list){
			if(moveList.isEmpty()){
				continue;
			}
			String station = "";//工位
			for(Map<String,Object> detailMap : moveList){
				Long detailId = (Long)detailMap.get("明细序号");
				Double quantityBU = Double.valueOf(detailMap.get("数量").toString());
				String classType = (String)detailMap.get("分类");
				WmsPickTicketDetail pickTicketDetail = load(WmsPickTicketDetail.class,detailId);
				String hql = "FROM WmsBlgItem wbi WHERE 1=1 AND wbi.item.id =:itemId";//AND wbi.billType.id = :billTypeId 
				//库存调整出库和退供应商出库单据类型采用B,其他采用A
				if("TSUPPLY_PICKING".equals(pickTicket.getBillType().getCode()) 
						|| "ALLOT_PICKING".equals(pickTicket.getBillType().getCode())){
					hql += " AND wbi.isA = 'N'";
				}else{
					hql += " AND wbi.isA = 'Y'";
				}
				WmsBlgItem wbi = (WmsBlgItem) commonDao.findByQueryUniqueResult(hql, new String[]{"itemId"}, 
						new Object[]{pickTicketDetail.getItem().getId()});
				if(wbi == null){
					throw new BusinessException("货品"+pickTicketDetail.getItem().getCode()+"的备料工物料对应关系未维护");
				}
				if(StringUtils.isEmpty(pickTicketDetail.getStation())){
					station = "-";
				}else{
					station = pickTicketDetail.getStation();
				}
				
				hql = "FROM WmsMoveDoc doc WHERE doc.pickTicket.id =:pickTicketId AND doc.blg.id =:blgId ";
				if(!"SPS_PICKING".equals(pickTicket.getBillType().getCode())){
					hql += " AND doc.station = '"+station+"'";
				}
				WmsMoveDoc moveDoc = (WmsMoveDoc) commonDao.findByQueryUniqueResult(hql, new String[]{"pickTicketId","blgId"}, 
						new Object[]{pickTicket.getId(),wbi.getBlg().getId()});
				if(moveDoc == null){
					moveDoc = new WmsMoveDoc();
					moveDoc.setWarehouse(pickTicket.getWarehouse());
					moveDoc.setCompany(pickTicket.getCompany());
					moveDoc.setCarrier(pickTicket.getCarrier());
					moveDoc.setClassType(classType);
//					moveDoc.setBeCrossDock(pickTicket.getBeCrossDock());
//					moveDoc.setShipLocation(pickTicket.getShipLocation());
					moveDoc.setDock(pickTicket.getDock());
					moveDoc.setType(WmsMoveDocType.MV_PICKTICKET_PICKING);
					
					WmsBillType billType = wmsBillTypeManager.getWmsBillType(
							moveDoc.getCompany(), TypeOfBill.MOVE, moveDoc.getType());
					moveDoc.setBillType(billType);

					if (moveDoc.isNew()) {
						String code = codeManager.generateCodeByRule(
								moveDoc.getWarehouse(), moveDoc.getCompany().getName(),
								"拣货单", billType.getName());
						moveDoc.setCode(code);
					}
					moveDoc.setBlg(wbi.getBlg());
					moveDoc.setPickTicket(pickTicket);
					moveDoc.setOriginalBillType(pickTicket.getBillType());
					moveDoc.setOriginalBillCode(pickTicket.getCode());
					moveDoc.setDriver(pickTicket.getDriver());
					moveDoc.setVehicleNo(pickTicket.getVehicleNo());
					moveDoc.setIntendShipDate(pickTicket.getIntendShipDate());
					moveDoc.setStation(station);
					moveDoc.setRelatedBill1(pickTicket.getRelatedBill1());
					commonDao.store(moveDoc);
					
					this.workflowManager.doWorkflow(moveDoc, "wmsMoveDocProcess.new");
				}
				WmsMoveDocDetail moveDocDetail = new WmsMoveDocDetail();
				moveDocDetail.setMoveDoc(moveDoc);
				moveDocDetail.setRelatedId(pickTicketDetail.getId());
				moveDocDetail.setItem(pickTicketDetail.getItem());
				moveDocDetail.setShipLotInfo(pickTicketDetail.getShipLotInfo());
				moveDocDetail.setInventoryStatus(pickTicketDetail.getInventoryStatus());
				moveDocDetail.setPackageUnit(pickTicketDetail.getPackageUnit());
				moveDocDetail.setPlanQuantityBU(pickTicketDetail.getExpectedQuantityBU());
				moveDocDetail.setPlanQuantity(PackageUtils.convertPackQuantity(pickTicketDetail.getExpectedQuantityBU(), moveDocDetail.getPackageUnit()));
				moveDocDetail.calculateLoad();
				moveDocDetail.setNeedTime(pickTicketDetail.getNeedTime());
				moveDocDetail.setPickWorker(pickTicketDetail.getPickWorker());
				moveDocDetail.setPickWorkerCode(pickTicketDetail.getPickWorkerCode());
				moveDocDetail.setProductionLine(pickTicketDetail.getProductionLine());
				moveDoc.addDetail(moveDocDetail);
				commonDao.store(moveDocDetail);
			}
		}
		
	}

	/**
	 * 根据退拣创建移位单
	 * 
	 * @param taskLog
	 * @param quantity
	 * @throws BusinessException
	 */
	public void createWmsMoveDocByPickBack(WmsTaskLog taskLog, Double quantityBU)
			throws BusinessException {
		WmsMoveDocDetail oldMoveDocDetail = this.commonDao.load(
				WmsMoveDocDetail.class, taskLog.getTask().getMoveDocDetail()
						.getId());
		String document = "拣货单";
		WmsMoveDoc moveDoc = null;
		List<WmsMoveDoc> moveDocs = null;
		if(oldMoveDocDetail.getMoveDoc().isPickTicketType()){
			WmsPickTicketDetail pickTicketDetail = this.commonDao.load(
					WmsPickTicketDetail.class, oldMoveDocDetail.getRelatedId());
			String hql = "from WmsMoveDoc doc where doc.pickTicket.id = :pickTicketId and doc.status= 'OPEN'";
			moveDocs = this.commonDao.findByQuery(hql,
					new String[] { "pickTicketId" },
					new Object[] { pickTicketDetail.getPickTicket().getId() });
		}
		if(oldMoveDocDetail.getMoveDoc().isWaveType()){
			document = "波次拣货单";
			String hql = "from WmsMoveDoc doc where doc.waveDoc.id = :waveDocId and doc.status= 'OPEN'";
			moveDocs = this.commonDao.findByQuery(hql,
					new String[] { "waveDocId" },
					new Object[] { oldMoveDocDetail.getMoveDoc().getWaveDoc().getId() });
		}
		if (moveDocs.size() > 0) {
			moveDoc = moveDocs.get(0);
		} else {
			WmsMoveDocDetail moveDocDetail = this.commonDao.load(
					WmsMoveDocDetail.class, taskLog.getTask()
							.getMoveDocDetail().getId());
			moveDoc = moveDocDetail.getMoveDoc().cloneWmsMoveDoc();
			String companyName = moveDoc.getCompany().getName();
			if(moveDoc.getCompany().isBeVirtual()){
				companyName = moveDoc.getWarehouse().getName();
			}
			String code = codeManager.generateCodeByRule(
					moveDoc.getWarehouse(), companyName,
					document, moveDoc.getBillType().getName());
			moveDoc.setCode(code);
			workflowManager.doWorkflow(moveDoc, "wmsMoveDocProcess.new");
			commonDao.store(moveDoc);
		}
		WmsMoveDocDetail moveDocDetail = null;
		String hql = "from WmsMoveDocDetail detail where detail.moveDoc.id = :moveDocId and detail.item.id = :itemId and detail.packageUnit.id = :packageUnitId AND detail.relatedId = :relatedId";
		List<WmsMoveDocDetail> details = this.commonDao.findByQuery(hql,
				new String[] { "moveDocId", "itemId", "packageUnitId",
						"relatedId" }, new Object[] { moveDoc.getId(),
				oldMoveDocDetail.getItem().getId(),
				oldMoveDocDetail.getPackageUnit().getId(),
				oldMoveDocDetail.getRelatedId()});
		if (details.size() > 0) {
			moveDocDetail = details.get(0);
		} else {
			moveDocDetail = new WmsMoveDocDetail();
		}
		if (moveDocDetail.isNew()) {
			if(oldMoveDocDetail.getPlanQuantityBU() <= 0D){
				oldMoveDocDetail.setMoveDoc(moveDoc);
				oldMoveDocDetail.setPlanQuantityBU(quantityBU);
				oldMoveDocDetail.setPlanQuantity(PackageUtils.convertPackQuantity(
						quantityBU, oldMoveDocDetail.getPackageUnit()));
				oldMoveDocDetail.calculateLoad();
				moveDoc.addDetail(oldMoveDocDetail);
				moveDoc.refreshQuantity();
			}
			else{
				moveDocDetail.setMoveDoc(moveDoc);
				moveDocDetail.setRelatedId(oldMoveDocDetail.getRelatedId());
				moveDocDetail.setItem(oldMoveDocDetail.getItem());
				moveDocDetail.setShipLotInfo(oldMoveDocDetail.getShipLotInfo());
				moveDocDetail.setInventoryId(taskLog.getSrcInventoryId());
				moveDocDetail.setFromLocationId(taskLog.getFromLocationId());
				moveDocDetail.setFromLocationCode(taskLog.getFromLocationCode());
				WmsLocation toLocation = this.commonDao.load(WmsLocation.class,
						taskLog.getToLocationId());
				moveDocDetail.setToLocationId(toLocation.getPickBackLoc().getId());
				moveDocDetail.setToLocationCode(toLocation.getPickBackLoc()
						.getCode());
				moveDocDetail.setInventoryStatus(oldMoveDocDetail
						.getInventoryStatus());
				moveDocDetail.setAllocatedQuantityBU(quantityBU);
				moveDocDetail.setPackageUnit(oldMoveDocDetail.getPackageUnit());
				moveDocDetail.setPlanQuantityBU(quantityBU);
				moveDocDetail.setPlanQuantity(PackageUtils.convertPackQuantity(
						quantityBU, moveDocDetail.getPackageUnit()));
				moveDocDetail.setTasks(new HashSet<WmsTask>());
				moveDocDetail.setWaveDocDetails(new HashSet<WmsWaveDocDetail>());
				moveDocDetail.setMovedQuantityBU(0D);
				moveDocDetail.setShippedQuantityBU(0D);
				moveDocDetail.setAllocatedQuantityBU(0D);
				moveDocDetail.setMoveDoc(moveDoc);
				moveDocDetail.calculateLoad();
				commonDao.store(moveDocDetail);
				moveDoc.addDetail(moveDocDetail);
				moveDoc.refreshQuantity();
				for(WmsWaveDocDetail waveDocDetail : oldMoveDocDetail.getWaveDocDetails()){
					waveDocDetail.addWmsMoveDocDetail(moveDocDetail);
				}
			}
			
		} else {
			if(oldMoveDocDetail.getPlanQuantityBU() <= 0D){
				oldMoveDocDetail.setMoveDoc(moveDoc);
				oldMoveDocDetail.setPlanQuantityBU(moveDocDetail.getPlanQuantityBU()
						+ quantityBU);
				oldMoveDocDetail.setPlanQuantity(PackageUtils.convertPackQuantity(
						moveDocDetail.getPlanQuantityBU(),
						moveDocDetail.getPackageUnit()));
				oldMoveDocDetail.calculateLoad();
				moveDoc.removeDetail(moveDocDetail);
				moveDoc.addDetail(oldMoveDocDetail);
				moveDoc.refreshQuantity();
				commonDao.delete(moveDocDetail);
			}
			else{
				moveDocDetail.setPlanQuantityBU(moveDocDetail.getPlanQuantityBU()
						+ quantityBU);
				moveDocDetail.setPlanQuantity(PackageUtils.convertPackQuantity(
						moveDocDetail.getPlanQuantityBU(),
						moveDocDetail.getPackageUnit()));
				moveDocDetail.calculateLoad();
				moveDoc.refreshQuantity();
			}
		}
	}

	public WmsMoveDoc createWmsMoveDoc(WmsASN asn) {
		String query = "FROM WmsBillType bill WHERE bill.company.id = :companyId AND bill.code = :code";

		WmsBillType billType = (WmsBillType) commonDao.findByQueryUniqueResult(
				query, new String[] { "companyId", "code" }, new Object[] {
						asn.getCompany().getId(), WmsMoveDocType.MV_PUTAWAY });

		WmsMoveDoc moveDoc = EntityFactory.getEntity(WmsMoveDoc.class);
		moveDoc.setAsn(asn);
		moveDoc.setCode(wmsBussinessCodeManager.generateCodeByRule(
				asn.getWarehouse(), asn.getCompany().getName(), "上架单", ""));
		moveDoc.setCompany(asn.getCompany());
		moveDoc.setBillType(billType);
		moveDoc.setDetails(new HashSet<WmsMoveDocDetail>());
		moveDoc.setOriginalBillCode(asn.getCode());
		moveDoc.setOriginalBillType(asn.getBillType());
		moveDoc.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
		moveDoc.setType(WmsMoveDocType.MV_PUTAWAY);

		return moveDoc;
	}

	public void createMoveDocDetail(WmsMoveDoc moveDoc,WmsInventory inv) {
		Double moveQuantityBU = inv.getAvailableQuantityBU();
		inv.allocatePickup(moveQuantityBU);
		
		String hql = " FROM WmsInventoryExtend invExtend WHERE invExtend.inventory.id = :inventoryId AND invExtend.quantityBU - invExtend.allocatedQuantityBU  > 0";
		List<WmsInventoryExtend> invExtends = commonDao.findByQuery(hql, "inventoryId", inv.getId());
		
		// 找到明细，追加数量
		for(WmsInventoryExtend invExtend : invExtends){
			boolean f = true;
			Double quantityBU = invExtend.getAvailableQuantityBU();
			for (WmsMoveDocDetail wmdd : moveDoc.getDetails()) {
				if (wmdd.getFromLocationId().equals(inv.getLocation().getId())
						&& wmdd.getPallet().equals(invExtend.getPallet())
						&& wmdd.getCarton().equals(invExtend.getCarton())
						&& wmdd.getSerialNo().equals(invExtend.getSerialNo())
						&& wmdd.getItemKey().getId().equals(inv.getItemKey().getId())
						&& wmdd.getInventoryStatus().equals(inv.getStatus())
						&& wmdd.getPackageUnit().getId().equals(inv.getPackageUnit().getId())
						&& wmdd.getInventoryId().equals(inv.getId())) {
					wmdd.addPlanQuantityBU(quantityBU);
					moveDoc.refreshQuantity();
					f = false;
					break;
				}
			}
			if(f){
				// 没找到明细，创建新明细
				WmsMoveDocDetail detail = EntityFactory
				.getEntity(WmsMoveDocDetail.class);
				detail.setMoveDoc(moveDoc);
				detail.setItem(inv.getItemKey().getItem());
				detail.setItemKey(inv.getItemKey());
				detail.setInventoryStatus(inv.getStatus());
				detail.setInventoryId(inv.getId());
				detail.setFromLocationId(inv.getLocation().getId());
				detail.setFromLocationCode(inv.getLocation().getCode());
				detail.setPallet(invExtend.getPallet());
				detail.setCarton(invExtend.getCarton());
				detail.setSerialNo(invExtend.getSerialNo());
				if (BaseStatus.NULLVALUE.equals(detail.getPallet())) {
					detail.setBePallet(Boolean.FALSE);
				} else {
					detail.setBePallet(Boolean.TRUE);
				}
				detail.setInventoryId(inv.getId());
				detail.setPackageUnit(inv.getPackageUnit());
				detail.addPlanQuantityBU(quantityBU);
				detail.calculateLoad();
				moveDoc.addDetail(detail);
			}
			invExtend.allocatePickup(quantityBU);
		}
	}

	
	public void createMoveDocDetail(WmsMoveDoc moveDoc,WmsInventory inv,Double moveQuantityBU) {
		inv.allocatePickup(moveQuantityBU);
		//必须是未码托的物料
		String hql = " FROM WmsInventoryExtend invExtend WHERE invExtend.inventory.id = :inventoryId " +
				" AND invExtend.quantityBU - invExtend.allocatedQuantityBU  > 0 AND invExtend.pallet='-'";
		List<WmsInventoryExtend> invExtends = commonDao.findByQuery(hql, "inventoryId", inv.getId());

		// 每次操作创建新明细
		for(WmsInventoryExtend invExtend : invExtends){
			if(moveQuantityBU <= 0.0){
				break;
			}
			boolean f = true;
			Double quantityBU = invExtend.getAvailableQuantityBU();
			Double addQuantity = (moveQuantityBU <= quantityBU) ? moveQuantityBU : quantityBU;
			for (WmsMoveDocDetail wmdd : moveDoc.getDetails()) {
				if (wmdd.getFromLocationId().equals(inv.getLocation().getId())
						&& wmdd.getPallet().equals(invExtend.getPallet())
						&& wmdd.getCarton().equals(invExtend.getCarton())
						&& wmdd.getSerialNo().equals(invExtend.getSerialNo())
						&& wmdd.getItemKey().getId().equals(inv.getItemKey().getId())
						&& wmdd.getInventoryStatus().equals(inv.getStatus())
						&& wmdd.getPackageUnit().getId().equals(inv.getPackageUnit().getId())
						&& wmdd.getInventoryId().equals(inv.getId())) {
					wmdd.addPlanQuantityBU(addQuantity);
					moveDoc.refreshQuantity();
					f = false;
					break;
				}
			}
			if(f){
				// 没找到明细，创建新明细
				WmsMoveDocDetail detail = EntityFactory
						.getEntity(WmsMoveDocDetail.class);
				detail.setMoveDoc(moveDoc);
				detail.setItem(inv.getItemKey().getItem());
				detail.setItemKey(inv.getItemKey());
				detail.setInventoryStatus(inv.getStatus());
				detail.setInventoryId(inv.getId());
				detail.setFromLocationId(inv.getLocation().getId());
				detail.setFromLocationCode(inv.getLocation().getCode());
				detail.setPallet(invExtend.getPallet());
				detail.setCarton(invExtend.getCarton());
				detail.setSerialNo(invExtend.getSerialNo());
				if (BaseStatus.NULLVALUE.equals(detail.getPallet())) {
					detail.setBePallet(Boolean.FALSE);
				} else {
					detail.setBePallet(Boolean.TRUE);
				}
				detail.setInventoryId(inv.getId());
				detail.setPackageUnit(inv.getPackageUnit());
				detail.addPlanQuantityBU(addQuantity);
				detail.calculateLoad();
				moveDoc.addDetail(detail);
			}
			invExtend.allocatePickup(addQuantity);
			
			moveQuantityBU -= addQuantity;
		}
	}

	public void manualAllocate(WmsMoveDocDetail detail, Long locationId,
			Double planQuantityBU) {
		if (planQuantityBU.doubleValue() <= 0) {
			throw new BusinessException("分配数量不能小于等于0！");
		}
		wmsTransactionalManager.allocate(detail.getId(), locationId,
				planQuantityBU);

		LocalizedMessage.addMessage("手工分配完成！");
	}
	public void manualAllocateQuality(WmsMoveDoc moveDoc){
		List<WmsTask> tasks = commonDao.findByQuery("FROM WmsTask t WHERE t.moveDocDetail.moveDoc.id :=moveDocId", 
				new String[]{"moveDocId"}, new Object[]{moveDoc.getId()});
		for(WmsTask task : tasks){
			if(task.getBeManual()){//返库
				manualAllocateQuality(moveDoc, task);
			}else{
				workflowManager.doWorkflow(moveDoc, "wmsMoveDocProcess.allocate"); // 调用分配流程, 判断是否整单分配
			}
		}
	}
	public void manualAllocateQuality(WmsMoveDoc moveDoc, WmsTask task){
		manualAllocateQuality(task, task.getFromLocationId(), task.getPlanQuantityBU());
//		if(task.getBeManual()){//返库
//			manualAllocateQuality(task, task.getFromLocationId(), task.getPlanQuantityBU());
//		}
//		else{
//			moveDoc.setStatus(WmsMoveDocStatus.PARTALLOCATED);
//			commonDao.store(moveDoc);
//		}
	}
	public void manualAllocateQuality(WmsTask task, Long locationId,
			Double planQuantityBU) {
		if (planQuantityBU.doubleValue() <= 0) {
			throw new BusinessException("分配数量不能小于等于0！");
		}
		if(planQuantityBU>task.getPlanQuantityBU()){
			throw new BusinessException("分配数量不能大于计划量");
		}
		
		WmsMoveDocDetail wdd = task.getMoveDocDetail();
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, wdd.getMoveDoc().getId());
		
		WmsLocation toLoc = null;
		if(task.getBeManual()){
			toLoc = commonDao.load(WmsLocation.class, locationId);
		}else{//不返库指不返回原库位,分配虚拟存货库位,做账使用,可库存调整将帐做掉
			//A-报废品区 找不到报错
			String location = "A-报废品区";
			List<Long> locIds = commonDao.findByQuery("SELECT l.id FROM WmsLocation l WHERE l.code =:lcode" +
					" AND l.warehouse.id =:warehouseId AND l.status = 'ENABLED'", 
					new String[]{"lcode","warehouseId"}, new Object[]{location,moveDoc.getWarehouse().getId()});
			if(locIds!=null && locIds.size()>0){
				toLoc = commonDao.load(WmsLocation.class, locIds.get(0));
			}else{
				throw new BusinessException("库位不存在:"+location);
			}
		}
		
		wmsInventoryManager.verifyLocation(BaseStatus.LOCK_IN,toLoc.getId(), wdd.getPackageUnit(),task.getItemKey().getId(),
				task.getItemKey().getItem().getId(),  planQuantityBU, task.getPallet());
		WmsTask newTask = wmsTaskManager.createWmsTask(wdd, task.getItemKey(), task.getInventoryStatus(), planQuantityBU);
		newTask.setToLocationId(toLoc.getId());
		newTask.setToLocationCode(toLoc.getCode());
		newTask.setBeManual(task.getBeManual());
		commonDao.store(newTask);
		
		task.addPlanQuantityBU(-planQuantityBU);
		if(task.getPlanQuantityBU()<=0){
			wdd.removeDetail(task);
			commonDao.store(wdd);
		}
		wdd.allocate(planQuantityBU>wdd.getUnAllocateQuantityBU()?wdd.getUnAllocateQuantityBU():planQuantityBU);
		workflowManager.doWorkflow(moveDoc, "wmsMoveDocProcess.allocate"); // 调用分配流程, 判断是否整单分配
		LocalizedMessage.addMessage("手工分配完成！");
	}
	public void cancelAllocateNode(WmsMoveDoc moveDoc){
		List<WmsTask> tasks = commonDao.findByQuery(MyUtils.getTask(), 
				new String[]{"billCode","type","status"},
				new Object[]{moveDoc.getCode(),moveDoc.getType(),WmsTaskStatus.OPEN});
		for(WmsTask task : tasks){
			if(!task.getBeManual()){
				continue;
			}
			task.setToLocationId(null);
			task.setToLocationCode(null);
			commonDao.store(task);
			WmsMoveDocDetail wdd = task.getMoveDocDetail();
			wdd.unallocate(task.getUnmovedQuantityBU());
		}
	}

	public void activeMoveByJac(WmsMoveDoc moveDoc){
		List<WmsTask> tasks = commonDao.findByQuery(MyUtils.getTask(), 
				new String[]{"billCode","type","status"},
				new Object[]{moveDoc.getCode(),moveDoc.getType(),WmsTaskStatus.OPEN});
		for(WmsTask task : tasks){
			task.setStatus(WmsTaskStatus.DISPATCHED);
			commonDao.store(task);
		}
	}
	public void unActiveMoveByJac(WmsMoveDoc moveDoc){
		List<WmsTask> tasks = commonDao.findByQuery(MyUtils.getTask(), 
				new String[]{"billCode","type","status"},
				new Object[]{moveDoc.getCode(),moveDoc.getType(),WmsTaskStatus.DISPATCHED});
		for(WmsTask task : tasks){
			task.setStatus(WmsTaskStatus.OPEN);
			commonDao.store(task);
		}
	}
	
	public RowData getQuantityByWmsMoveDocDetail(Map map) {
		WmsMoveDocDetail detail = commonDao.load(WmsMoveDocDetail.class,
				(Long) ((List) map.get("parentIds")).get(0));

		RowData rd = new RowData();

		rd.addColumnValue(detail.getUnAllocateQuantityBU());

		return rd;
	}
	public RowData getQuantityByWmsTask(Map map) {
		WmsTask task = commonDao.load(WmsTask.class,
				(Long) ((List) map.get("parentIds")).get(0));
		if(task==null){
			return null;
		}
		RowData rd = new RowData();

		rd.addColumnValue(task.getPlanQuantityBU());

		return rd;
	}

	public RowData getPalletByWmsMoveDocDetail(Map map) {
		WmsMoveDocDetail detail = commonDao.load(WmsMoveDocDetail.class,
				(Long) ((List) map.get("parentIds")).get(0));

		RowData rd = new RowData();

		rd.addColumnValue(detail.getPallet());

		return rd;
	}

	public void storeMoveDoc(WmsMoveDoc moveDoc) {
		moveDoc.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
		WmsOrganization company = getCompanyByWarehouse(moveDoc.getWarehouse());
		moveDoc.setCompany(company);
		
		String query = "FROM WmsBillType bill WHERE bill.company.id = :companyId AND bill.code = :code";
		WmsBillType billType = (WmsBillType) commonDao.findByQueryUniqueResult(
				query, new String[] { "companyId", "code" }, new Object[] {
						moveDoc.getCompany().getId(), WmsMoveDocType.MV_MOVE });
		moveDoc.setCode(wmsBussinessCodeManager.generateCodeByRule(
				moveDoc.getWarehouse(), moveDoc.getWarehouse().getName(), "移位单",""));
		moveDoc.setBillType(billType);
		moveDoc.setType(WmsMoveDocType.MV_MOVE);
		moveDoc.setOriginalBillType(null);
		moveDoc.setOriginalBillCode(null);
		moveDoc.setDetails(new HashSet<WmsMoveDocDetail>());
	}
	public void storeMoveDocQuality(WmsMoveDoc moveDoc) {
		moveDoc.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
		moveDoc.setCompany(moveDoc.getBillType().getCompany());
		moveDoc.setCode(wmsBussinessCodeManager.generateCodeByRule(
				moveDoc.getWarehouse(), moveDoc.getWarehouse().getName(), "质检单",""));
		
		moveDoc.setType(WmsMoveDocType.MV_QUALITY_MOVE);
		moveDoc.setTransStatus(WmsWorkDocStatus.OPEN);//BaseStatus.DISABLED
		moveDoc.setOriginalBillType(null);
		moveDoc.setOriginalBillCode(null);
		moveDoc.setProcessStatus(WmsQualityStatus.NULLVALUE);
		moveDoc.setDetails(new HashSet<WmsMoveDocDetail>());
	}
	public void storeMoveDocDetailQuality(Long parentId,WmsMoveDocDetail wdd,Long itemStateId,Double planQty){
		wdd = commonDao.load(WmsMoveDocDetail.class, wdd.getId());
		if(wdd.getUnProcessQuantityBU()<planQty || planQty<=0){
			LocalizedMessage.addLocalizedMessage(MyUtils.font("失败!数量有误"));
		}else{
			WmsItemState itemState = commonDao.load(WmsItemState.class, itemStateId);
			if(itemState==null){
				LocalizedMessage.addLocalizedMessage(MyUtils.font("失败!状态找不到数据"));
			}else{
				List<String> itemStates = findItemStates(wdd);
				if(itemStates!=null && itemStates.size()>0
						&& itemStates.contains(itemState.getName())){
					LocalizedMessage.addLocalizedMessage(MyUtils.font("失败!质检同一物料状态不允许重复"));
				}else{
					Boolean beRepel = beRepel(wdd, itemStates, itemState);
					if(beRepel){
						LocalizedMessage.addLocalizedMessage(MyUtils.font("失败!质检同一物料状态存在互斥"));
					}else{
						writeQuality(wdd, planQty, itemState.isBeBackInv(),itemState.getName());
					}
				}
			}
		}
	}
	public void upQualityDetail(Long parentId,WmsMoveDocDetail wdd,Long itemStateId){
//		System.out.println(parentId+","+wdd.getId()+","+itemStateId);
		storeMoveDocDetailQuality(parentId, wdd, itemStateId, wdd.getUnProcessQuantityBU());
	}
	private List<String> findItemStates(WmsMoveDocDetail wdd){
		List<String> itemStates = commonDao.findByQuery("select task.inventoryStatus from WmsTask task"
				+ " where task.moveDocDetail.id =:moveDocDetailId", 
				new String[]{"moveDocDetailId"}, new Object[]{wdd.getId()});	
		return itemStates;
	}
	private void writeQuality(WmsMoveDocDetail wdd,Double planQty,Boolean isBeBackInv,String itemStatus){
		WmsTask task = wmsTaskManager.createWmsTask(wdd, wdd.getItemKey(), itemStatus, planQty);
		task.setBeManual(isBeBackInv);
		commonDao.store(task);
		wdd.process(planQty);
	}
	/**itemStates-已经存在状态,itemState-当前选择状态*/
	private Boolean beRepel(WmsMoveDocDetail wdd,List<String> itemStates,WmsItemState itemState){
		Boolean beRepel = false;
		List<Object[]> repelStatus = commonDao.findByQuery("SELECT s.itemState.name,s.repelStatus"
				+ " FROM WmsQualityBillStatus s "
				+ "WHERE s.repelStatus is not null AND s.billType.id =:billTypeId", 
				new String[]{"billTypeId"}, 
				new Object[]{wdd.getMoveDoc().getBillType().getId()});
		if(repelStatus!=null && repelStatus.size()>0){
			if(itemStates!=null && itemStates.size()>0){
				String[] repels = null;
				for(Object[] repel : repelStatus){
					if(itemStates.contains(repel[0])){
						repels = repel[1].toString().split(",");//互斥状态集
						for(String r : repels){
							if(r.equals(itemState.getName())){
								beRepel = true;//所选状态和已经存在状态的互斥状态集匹配
								break;
							}
						}
					}
					if(beRepel){
						break;
					}
				}
			}
		}
		return beRepel;
	}
	/**
	 * 添加移位计划明细:按货品添加
	 * @param moveDocId
	 * @param wsn
	 */
	public void addMoveDocDetail(Long moveDocId, WmsInventoryExtend wsn,
			List tableValues) {
		double quantity = Double.parseDouble(tableValues.get(0).toString());
		String qualityType = tableValues.get(1)==null?"-":tableValues.get(1).toString();
		String[] types = new String[]{
				"新品","切换件","量产品"
		};
		if(!StringHelper.in(qualityType.trim(), types)){
			LocalizedMessage.setMessage(MyUtils.font2("送检分类可用:新品,切换件,量产品"));
		}else{
			addMoveDocDetail(moveDocId, wsn, quantity,qualityType.trim());
		}
	}
	public void addMoveDocDetail(Long moveDocId, WmsInventoryExtend wsn,
			double quantity,String qualityType) {
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, moveDocId);
		List<Object[]> qualityQuantityBUs = commonDao.findByQuery("SELECT aa.qualityQuantityBU,aa.qualityCode,aa.receivedQuantityBU FROM WmsASNDetail aa" +
				" WHERE aa.asn.code =:asnCode AND aa.item.code =:itemCode AND aa.qualityQuantityBU>0", 
				new String[]{"asnCode","itemCode"}, 
				new Object[]{wsn.getInventory().getItemKey().getLotInfo().getSoi(),wsn.getItem().getCode()});
		String qualityCode = null;
		Double receivedQuantityBU = 0D;//质检记录收货量
		if(quantity==0){//默认给待检量
			if(qualityQuantityBUs!=null && qualityQuantityBUs.size()>0){
				quantity = Double.valueOf(qualityQuantityBUs.get(0)[0]==null?"0":qualityQuantityBUs.get(0)[0].toString());
				qualityCode = qualityQuantityBUs.get(0)[1]==null?null:qualityQuantityBUs.get(0)[1].toString();
				receivedQuantityBU = Double.valueOf(qualityQuantityBUs.get(0)[2]==null?"0":qualityQuantityBUs.get(0)[2].toString());
			}
		}else{//记录人工调整量
			if(qualityQuantityBUs!=null && qualityQuantityBUs.size()>0){
				qualityCode = qualityQuantityBUs.get(0)[1]==null?null:qualityQuantityBUs.get(0)[1].toString();
				receivedQuantityBU = Double.valueOf(qualityQuantityBUs.get(0)[2]==null?"0":qualityQuantityBUs.get(0)[2].toString());
			}
			this.bolLog(moveDoc, wsn.getItem().getCode(), "修改待检量", quantity);
		}

		// 如果移位数量大于库存可用数量
		if (quantity > wsn.getInventory().getAvailableQuantityBU()
				|| quantity <= 0) {
			throw new BusinessException("数量要大于0小于等于库存可用数量！");
		}
		if(quantity>wsn.getAvailableQuantityBU()){
			throw new BusinessException("数量要大于0小于等于托盘可用量");
		}
		String pallet = BaseStatus.NULLVALUE;
		if(quantity == wsn.getAvailableQuantityBU()){
			pallet = wsn.getPallet();
		}
		//新建明细时一个ASN一个物料建议只传一次
		WmsMoveDocDetail wmdd = createMoveDocDetail(moveDoc, wsn.getInventory()
				.getItemKey(), wsn.getInventory().getPackageUnit(), wsn
				.getInventory().getPackQty(quantity), quantity, wsn
				.getInventory().getLocation().getId(), wsn.getInventory()
				.getLocation().getCode(), pallet, wsn.getInventory()
				.getStatus(), wsn.getInventory().getId(), wsn,qualityCode,receivedQuantityBU);
		wmdd.setQualityType(qualityType);
		commonDao.store(wmdd);
		
		// 对库存拣货数量进行锁定
		wsn.getInventory().allocatePickup(quantity);
		wsn.allocatePickup(quantity);
		wsn.getInventory().setLockLot(true);
//		wsn.getInventory().getLocation().setLockCount(true);
		// 写库存日志
//		WmsInventoryManager wmsInventoryManager = (WmsInventoryManager) applicationContext.getBean("wmsInventoryManager");
		wmsInventoryManager.addInventoryLog(WmsInventoryLogType.LOCK, 0, 
				moveDoc.getCode(), moveDoc.getBillType(), wsn.getInventory().getLocation(), wsn.getInventory().getItemKey(), 0D,  
				wsn.getInventory().getPackageUnit(),wsn.getInventory().getStatus(),"批次锁");
		moveDoc.addDetail(wmdd);
		commonDao.store(moveDoc);
	}
	
	private void bolLog(WmsMoveDoc moveDoc,String itemCode
			,String type,Double quantityBU){
		WmsBOLStateLog bol = EntityFactory.getEntity(WmsBOLStateLog.class);
		bol.setInputTime(new Date());
		bol.setMoveDoc(moveDoc);
		bol.setType(type);
		bol.setDriver(itemCode);
//		bol.setVehicleNo(pallet);//托盘号(质检)
		bol.setQuantityBU(quantityBU);
		commonDao.store(bol);
	}
	
	/**
	 * 添加移位计划明细:按托盘添加
	 * @param moveDocId
	 * @param wsn
	 */
	public void addMoveDocDetailByPallet(Long moveDocId, WmsInventoryExtend wsn){
		String hql = "from WmsInventoryExtend wsn where wsn.pallet = :pallet and wsn.locationId = :locationId and (wsn.inventory.quantityBU-wsn.inventory.allocatedQuantityBU)>0 and wsn.inventory.location.lockCount = false";
		List<WmsInventoryExtend> wsns = this.commonDao.findByQuery(hql, new String[]{"pallet","locationId"}, new Object[]{wsn.getPallet(),wsn.getLocationId()});
		for (WmsInventoryExtend wmsInventoryExtend : wsns) {
			List<String> tableValues = new ArrayList<String>();
			tableValues.add(wmsInventoryExtend.getQuantityBU().toString());
			this.addMoveDocDetail(moveDocId, wmsInventoryExtend, tableValues);
		}
	}
	/**
	 * 添加移位计划明细:按箱号添加
	 * @param moveDocId
	 * @param wsn
	 */
	public void addMoveDocDetailByCarton(Long moveDocId, WmsInventoryExtend wsn){
		String hql = "from WmsInventoryExtend wsn where wsn.carton = :carton and (wsn.inventory.quantityBU-wsn.inventory.allocatedQuantityBU)>0 and wsn.inventory.location.lockCount = false";
		List<WmsInventoryExtend> wsns = this.commonDao.findByQuery(hql, new String[]{"carton"}, new Object[]{wsn.getCarton()});
		for (WmsInventoryExtend wmsInventoryExtend : wsns) {
			List<String> tableValues = new ArrayList<String>();
			tableValues.add(wmsInventoryExtend.getQuantityBU().toString());
			this.addMoveDocDetail(moveDocId, wmsInventoryExtend, tableValues);
		}
	}

	private WmsMoveDocDetail createMoveDocDetail(WmsMoveDoc moveDoc,
			WmsItemKey itemKey, WmsPackageUnit packageUnit,
			Double planQuantity, Double planQuantityBU, Long fromLocationId,
			String fromLocationCode, String pallet, String invStatus,
			Long inventoryId, WmsInventoryExtend wsn,String qualityCode,Double receivedQuantityBU) {
		WmsMoveDocDetail wmd = EntityFactory.getEntity(WmsMoveDocDetail.class);
		wmd.setMoveDoc(moveDoc);
		wmd.setItemKey(itemKey);
		wmd.setItem(itemKey.getItem());
		wmd.setPackageUnit(packageUnit);
		wmd.setPlanQuantity(planQuantity);
		wmd.setPlanQuantityBU(planQuantityBU);
		wmd.setPallet(pallet);
		// 托盘整托移位时是托盘移位，否则是散货移位
		if (!WmsStringUtils.isEmpty(pallet) && !BaseStatus.NULLVALUE.equals(pallet)) {
			wmd.setBePallet(true);
		} else {
			wmd.setBePallet(false);
		}
		wmd.setFromLocationId(fromLocationId);
		wmd.setFromLocationCode(fromLocationCode);
		wmd.setInventoryStatus(invStatus);
		wmd.setInventoryId(inventoryId);
		wmd.setCarton(wsn.getCarton());
		wmd.setRelatedId(wsn.getId());//质检明细与子表一对一
		wmd.setReplenishmentArea(qualityCode);//JAC质检唯一单号
		wmd.setProcessPlanQuantityBU(receivedQuantityBU);//质检作为ASN明细入库量
		commonDao.store(wmd);
		return wmd;
	}

	public void deleteMoveDocPlan(WmsMoveDoc moveDoc) {
		if(moveDoc.isPickTicketType() || moveDoc.isWaveType()){
			String deleteTaskLog = "DELETE FROM WmsTaskLog taskLog where taskLog.task.id = :taskId";
			String taskHql = "FROM WmsTask t where t.moveDocDetail.moveDoc.id = :moveDocId";
			List<WmsTask> tasks = commonDao.findByQuery(taskHql, "moveDocId", moveDoc.getId());
			for(WmsTask task : tasks){
				commonDao.executeByHql(deleteTaskLog, "taskId", task.getId());
				commonDao.delete(task);
			}
			commonDao.delete(moveDoc);
		}
		else{
			List<WmsMoveDocDetail> moveDocDetails = new ArrayList<WmsMoveDocDetail>(moveDoc.getDetails());
			for (WmsMoveDocDetail moveDocDetail : moveDocDetails) {
				deleteMoveDocDetails(moveDocDetail);
			}
			workflowManager.doWorkflow(moveDoc, "wmsMoveDocProcess.delete");
		}
	}

	public void deleteMoveDocDetails(WmsMoveDocDetail detail) {
		detail.unprocess(detail.getProcessQuantityBU());
		// 单头移除明细,并刷新计划数量
		WmsMoveDoc moveDoc = detail.getMoveDoc();
		moveDoc.removeDetail(detail);

		// 将库存已拣货数量解锁
		WmsInventory inventory = this.commonDao.load(WmsInventory.class,
				detail.getInventoryId());
		inventory.setLockLot(false);
//		inventory.getLocation().setLockCount(false);
		wmsInventoryManager.addInventoryLog(WmsInventoryLogType.UNLOCK, 0, 
				moveDoc.getCode(), moveDoc.getBillType(), inventory.getLocation(), inventory.getItemKey(), 0D,   
				inventory.getPackageUnit(),inventory.getStatus(),"批次锁");
		inventory.unallocatePickup(detail.getPlanQuantityBU());
		commonDao.store(inventory);
		WmsInventoryExtend wsn = commonDao.load(WmsInventoryExtend.class, detail.getRelatedId());//质检子表ID已保存在明细表字段relatedId
		if(wsn==null){
			throw new OriginalBusinessException("明细找不到库存管理ID:"+detail.getRelatedId());
		}else{
			wsn.unallocatePickup(detail.getPlanQuantityBU());
			commonDao.store(wsn);
		}
		// 删除明细
		this.commonDao.delete(detail);
	}
	
	public void deleteTask(WmsTask task){
		WmsMoveDocDetail detail = task.getMoveDocDetail();
		detail.unprocess(task.getPlanQuantityBU());
		detail.removeDetail(task);
		commonDao.store(detail);
		commonDao.delete(task);
	}

	public void cancelMoveDoc(WmsMoveDoc moveDoc) {
		boolean needCancelMoveDoc = false;
		for (WmsMoveDocDetail moveDocDetail : moveDoc.getDetails()) {
			wmsInventoryManager.cancelMoveDoc(moveDocDetail);
			needCancelMoveDoc = true;
		}
		if (needCancelMoveDoc) {
			WmsASN asn = load(WmsASN.class,moveDoc.getAsn().getId());
			moveDoc.setAsn(null);
			workflowManager.doWorkflow(asn,
					"wmsASNPutawayProcess.cancelMoveDoc");
		}
	}
	public void cancelPickDoc(WmsMoveDoc moveDoc){
		moveDoc.setStatus(WmsMoveDocStatus.CANCELED);
		commonDao.store(moveDoc);
	}

	public void splitMoveDoc(WmsMoveDocDetail moveDocDetail,
			List<String> splitQtyList, WmsMoveDoc splitedMoveDoc) {
		double splitQuantity = 0D;
		try {
			splitQuantity = Double.parseDouble(splitQtyList.get(0));
		} catch (NumberFormatException nfe) {
			throw new OriginalBusinessException("拆分数量必须是数字！");
		}

		if (splitQuantity <= 0) {
			throw new OriginalBusinessException("拆分数量必须是大于0的数字！");
		}
		if (moveDocDetail.getPlanQuantityBU() < splitQuantity) {
			throw new OriginalBusinessException("输入的拆分数量大于计划数量！");
		}
		WmsMoveDoc originalMoveDoc = commonDao.load(WmsMoveDoc.class,
				moveDocDetail.getMoveDoc().getId());
		// WmsMoveDoc splitedMoveDoc =
		// EntityFactory.getEntity(WmsMoveDoc.class);
		if (splitedMoveDoc.isNew()) {
			String type = "移位单";
			if (WmsMoveDocType.MV_PICKTICKET_PICKING.equals(originalMoveDoc
					.getType())) {
				type = "拣货单";
			} else if (WmsMoveDocType.MV_MOVE.equals(originalMoveDoc.getType())) {
				type = "移位单";
			} else if (WmsMoveDocType.MV_WAVE_PICKING.equals(originalMoveDoc
					.getType())) {
				type = "波次拣货单";
			} else if (WmsMoveDocType.MV_REPLENISHMENT_MOVE
					.equals(originalMoveDoc.getType())) {
				type = "补货单";
			} else if (WmsMoveDocType.MV_PROCESS_PICKING.equals(originalMoveDoc
					.getType())) {
				type = "加工单";
			} else if (WmsMoveDocType.MV_PUTAWAY.equals(originalMoveDoc
					.getType())) {
				type = "上架单";
			}
			String companyName = originalMoveDoc.getCompany().getName();
			if(originalMoveDoc.getCompany().isBeVirtual()){
				companyName = originalMoveDoc.getWarehouse().getName();
			}
			
			WmsBillType billType = commonDao.load(WmsBillType.class,
					originalMoveDoc.getBillType().getId());
			String code = codeManager.generateCodeByRule(originalMoveDoc
					.getWarehouse(), companyName,
					type, billType.getName());
			splitedMoveDoc = originalMoveDoc.cloneWmsMoveDoc(splitedMoveDoc);
			splitedMoveDoc.setCode(code);
			workflowManager.doWorkflow(splitedMoveDoc, "wmsMoveDocProcess.new");
			commonDao.store(splitedMoveDoc);
		}

		WmsMoveDocDetail splitedDetail = EntityFactory
				.getEntity(WmsMoveDocDetail.class);
		BeanUtils.copyEntity(splitedDetail, moveDocDetail);
		splitedDetail.setId(null);
		splitedDetail.setMovedQuantityBU(0D);
		splitedDetail.setPlanQuantityBU(splitQuantity);
		splitedDetail.setPlanQuantity(PackageUtils.convertPackQuantity(
				splitQuantity, splitedDetail.getPackageUnit()));
		splitedDetail.setShippedQuantityBU(0D);
		splitedDetail.setAllocatedQuantityBU(0D);
		splitedDetail.setMoveDoc(splitedMoveDoc);
		splitedDetail.calculateLoad();
		splitedDetail.setTasks(new HashSet<WmsTask>());
		splitedDetail.setWaveDocDetails(new HashSet<WmsWaveDocDetail>());
		splitedMoveDoc.addDetail(splitedDetail);
		for(WmsWaveDocDetail waveDocDetail : moveDocDetail.getWaveDocDetails()){
			waveDocDetail.addWmsMoveDocDetail(splitedDetail);
		}
		commonDao.store(splitedDetail);
		commonDao.store(splitedMoveDoc);

		moveDocDetail.setPlanQuantityBU(moveDocDetail.getPlanQuantityBU()
				- splitQuantity);
		moveDocDetail.setPlanQuantity(PackageUtils.convertPackQuantity(
				moveDocDetail.getPlanQuantityBU(),
				moveDocDetail.getPackageUnit()));
		moveDocDetail.calculateLoad();
		commonDao.store(moveDocDetail);
		originalMoveDoc.setPlanQuantityBU(originalMoveDoc.getPlanQuantityBU()
				- splitQuantity);
		commonDao.store(originalMoveDoc);
		if (moveDocDetail.getPlanQuantityBU() == 0D) {
			commonDao.delete(moveDocDetail);
		}
	}

	/**
	 * 根据波次创建移位单
	 * 
	 * @param waveDoc
	 * @throws BusinessException
	 */
	private void createWmsMoveDocByWaveDoc(WmsWaveDoc waveDoc)
			throws BusinessException {
		if (WmsWaveDocWorkMode.WORK_BY_DOC.equals(waveDoc.getWorkMode())) {
			createWmsMoveDocByWaveDocOnDocMode(waveDoc);
		} else {
			createWmsMoveDocByWaveDocOnWaveMode(waveDoc);
		}

	}

	// 单拣
	private void createWmsMoveDocByWaveDocOnDocMode(WmsWaveDoc waveDoc) {
		WmsMoveDoc moveDoc = new WmsMoveDoc();

		moveDoc.setWarehouse(waveDoc.getWarehouse());
		moveDoc.setType(WmsMoveDocType.MV_WAVE_PICKING);
		WmsOrganization company = this.getCompanyByWarehouse(waveDoc
				.getWarehouse());
		WmsBillType billType = wmsBillTypeManager.getWmsBillType(company,
				TypeOfBill.MOVE, moveDoc.getType());
		moveDoc.setBillType(billType);
		moveDoc.setCompany(company);
		if (moveDoc.isNew()) {
			String code = codeManager.generateCodeByRule(
					moveDoc.getWarehouse(), moveDoc.getWarehouse().getName(),
					"波次拣货单", billType.getName());
			moveDoc.setCode(code);
		}

		moveDoc.setWaveDoc(waveDoc);
		moveDoc.setOriginalBillCode(waveDoc.getCode());
		moveDoc.setPlanQuantityBU(waveDoc.getExpectedQuantityBU());
		moveDoc.setIntendShipDate(waveDoc.getFinishDate());
		commonDao.store(moveDoc);
		
		this.workflowManager.doWorkflow(moveDoc, "wmsMoveDocProcess.new");
		
		List<WmsWaveDocDetail> waveDocDetails = this.commonDao
				.findByQuery(
						"from WmsWaveDocDetail detail where detail.waveDoc.id = :waveDocId and detail.expectedQuantityBU > 0",
						"waveDocId", waveDoc.getId());
		for (WmsWaveDocDetail waveDocDetail : waveDocDetails) {
			WmsMoveDocDetail moveDocDetail = new WmsMoveDocDetail();
			moveDocDetail.setMoveDoc(moveDoc);
			moveDocDetail.setRelatedId(waveDocDetail.getId());
			moveDocDetail.setItem(waveDocDetail.getItem());
			moveDocDetail.setShipLotInfo(waveDocDetail.getShipLotInfo());
			moveDocDetail.setInventoryStatus(waveDocDetail.getMoveDocDetail()
					.getInventoryStatus());
			moveDocDetail.setPackageUnit(waveDocDetail.getPackageUnit());
			moveDocDetail.setPlanQuantity(waveDocDetail.getExpectedQuantity());
			moveDocDetail.setPlanQuantityBU(waveDocDetail
					.getExpectedQuantityBU());
			moveDocDetail.calculateLoad();
			commonDao.store(moveDocDetail);
			waveDocDetail.addWmsMoveDocDetail(moveDocDetail);
			this.commonDao.store(waveDocDetail);
		}
	}
	
	// 批拣时生成拣货单
	private void createWmsMoveDocByWaveDocOnWaveMode(WmsWaveDoc waveDoc) {
		WmsMoveDoc moveDoc = new WmsMoveDoc();
		moveDoc.setWarehouse(waveDoc.getWarehouse());
		moveDoc.setType(WmsMoveDocType.MV_WAVE_PICKING);
		WmsOrganization company = this.getCompanyByWarehouse(waveDoc
				.getWarehouse());
		WmsBillType billType = wmsBillTypeManager.getWmsBillType(company,
				TypeOfBill.MOVE, moveDoc.getType());
		moveDoc.setBillType(billType);
		moveDoc.setCompany(company);
		if (moveDoc.isNew()) {
			String code = codeManager.generateCodeByRule(
					moveDoc.getWarehouse(), moveDoc.getWarehouse().getName(),
					"波次拣货单", billType.getName());
			moveDoc.setCode(code);
		}

		moveDoc.setWaveDoc(waveDoc);
		moveDoc.setOriginalBillCode(waveDoc.getCode());
		moveDoc.setPlanQuantityBU(waveDoc.getExpectedQuantityBU());
		moveDoc.setIntendShipDate(waveDoc.getFinishDate());
		commonDao.store(moveDoc);
		
		this.workflowManager.doWorkflow(moveDoc, "wmsMoveDocProcess.new");
		
		String hql = "SELECT d.id FROM WmsWaveDocDetail d where d.waveDoc.id = :waveDocId ";
		List<Long> waveDocDetailIdList = commonDao.findByQuery(hql,
				"waveDocId", waveDoc.getId());
		if (waveDocDetailIdList.isEmpty()) {
			throw new BusinessException("wmsmovedoc.detail.not.exist");
		}
		try {
			for (Long waveDocDetailId : waveDocDetailIdList) {
				WmsWaveDocDetail waveDocDetail = commonDao.load(
						WmsWaveDocDetail.class, waveDocDetailId);
				
				WmsMoveDocDetail moveDocDetail = (WmsMoveDocDetail) commonDao
						.findByQueryUniqueResult(
								getWaveDocDetailHql(waveDocDetail),
								new String[] { "moveDocId", "itemId",
										"packageUnitId" },
								new Object[] { moveDoc.getId(),
										waveDocDetail.getItem().getId(),
										waveDocDetail.getPackageUnit().getId() });
				if (moveDocDetail != null) {
					moveDocDetail.addPlanQuantityBU(waveDocDetail
							.getExpectedQuantityBU());
					waveDocDetail.addWmsMoveDocDetail(moveDocDetail);
					moveDocDetail.calculateLoad();
				} else {
					moveDocDetail = new WmsMoveDocDetail();
					moveDocDetail.setMoveDoc(moveDoc);
					moveDocDetail.setRelatedId(waveDocDetail.getId());
					moveDocDetail.setItem(waveDocDetail.getItem());
					moveDocDetail
							.setShipLotInfo(waveDocDetail.getShipLotInfo());
					moveDocDetail.setInventoryStatus(waveDocDetail
							.getMoveDocDetail().getInventoryStatus());
					moveDocDetail
							.setPackageUnit(waveDocDetail.getPackageUnit());
					moveDocDetail.setPlanQuantity(waveDocDetail
							.getExpectedQuantity());
					moveDocDetail.setPlanQuantityBU(waveDocDetail
							.getExpectedQuantityBU());
					moveDocDetail.calculateLoad();
					commonDao.store(moveDocDetail);
					waveDocDetail.addWmsMoveDocDetail(moveDocDetail);
				}
				this.commonDao.store(waveDocDetail);
			}
		} catch (BusinessException be) {
			logger.error("auto allocate error", be);
		}
	}

	public Map printPallet(WmsMoveDoc moveDoc, Long number) {
		Map result = new HashMap();
		Map<Long, String> reportValue = new HashMap<Long, String>();
		List<WmsMoveDocDetail> list = commonDao
				.findByQuery(
						"Select wmdd From WmsMoveDocDetail wmdd Where wmdd.moveDoc.id=:moveDocId",
						new String[] { "moveDocId" },
						new Object[] { moveDoc.getId() });
		int i = 0;
		for (WmsMoveDocDetail moveDocDetail : list) {
			if (org.apache.commons.lang.StringUtils.isEmpty(moveDocDetail
					.getPallet())
					|| BaseStatus.NULLVALUE.equals(moveDocDetail.getPallet())) {
				continue;
			}
			String str = ";pallet=" + moveDocDetail.getPallet();
			reportValue.put(new Long(i++), "wmsPallet.raq&raqParams=" + str);
		}
		if (!reportValue.isEmpty()) {
			result.put(IPage.REPORT_VALUES, reportValue);
			result.put(IPage.REPORT_PRINT_NUM, number.intValue());
		}
		return result;
	}

	private String getWaveDocDetailHql(WmsWaveDocDetail waveDocDetail) {
		StringBuffer hqlBuffer = new StringBuffer();
		hqlBuffer
				.append("from WmsMoveDocDetail detail where detail.moveDoc.id = :moveDocId ")
				.append(" and detail.item.id = :itemId and detail.packageUnit.id = :packageUnitId ")
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.soi", waveDocDetail
								.getShipLotInfo().getSoi()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.supplier", waveDocDetail
								.getShipLotInfo().getSupplier()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC1", waveDocDetail
								.getShipLotInfo().getExtendPropC1()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC2", waveDocDetail
								.getShipLotInfo().getExtendPropC2()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC3", waveDocDetail
								.getShipLotInfo().getExtendPropC3()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC4", waveDocDetail
								.getShipLotInfo().getExtendPropC4()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC5", waveDocDetail
								.getShipLotInfo().getExtendPropC5()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC6", waveDocDetail
								.getShipLotInfo().getExtendPropC6()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC7", waveDocDetail
								.getShipLotInfo().getExtendPropC7()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC8", waveDocDetail
								.getShipLotInfo().getExtendPropC8()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC9", waveDocDetail
								.getShipLotInfo().getExtendPropC9()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC10", waveDocDetail
								.getShipLotInfo().getExtendPropC10()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC11", waveDocDetail
								.getShipLotInfo().getExtendPropC11()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC12", waveDocDetail
								.getShipLotInfo().getExtendPropC12()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC13", waveDocDetail
								.getShipLotInfo().getExtendPropC13()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC14", waveDocDetail
								.getShipLotInfo().getExtendPropC14()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC15", waveDocDetail
								.getShipLotInfo().getExtendPropC15()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC16", waveDocDetail
								.getShipLotInfo().getExtendPropC16()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC17", waveDocDetail
								.getShipLotInfo().getExtendPropC17()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC18", waveDocDetail
								.getShipLotInfo().getExtendPropC18()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC19", waveDocDetail
								.getShipLotInfo().getExtendPropC19()))
				.append(" AND ")
				.append(NewLotInfoParser.decryptStringOfWaveLot(
						"detail.shipLotInfo.extendPropC20", waveDocDetail
								.getShipLotInfo().getExtendPropC20()));
		String hql = hqlBuffer.toString();
		return hql;
	}

	/**
	 * 根据仓库获取
	 * 
	 * @param warehouse
	 * @return
	 */
	public WmsOrganization getCompanyByWarehouse(WmsWarehouse warehouse) {
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
	 * 设置备货库位
	 * 
	 * @param moveDoc
	 * @return
	 */
	public void setShippingLocation(WmsMoveDoc moveDoc) {
		moveDoc.setBeCrossDock(Boolean.TRUE);
		this.commonDao.store(moveDoc);
	}


	/**
	 * 取消分配(整单取消)
	 * @param moveDoc
	 * @return
	 */
	public void cancelAllocateWhole(WmsMoveDoc moveDoc) {
		workflowManager.doWorkflow(moveDoc,"wmsMoveDocProcess.cancelAllocate");
	}

	/**
	 * 取消分配(部分取消)
	 * @param moveDoc, task
	 * @return
	 */
	public void cancelAllocatePart(WmsMoveDoc moveDoc, WmsTask task) {
		WmsMoveDocDetail wmsMoveDocDetail = task.getMoveDocDetail();

		Double unallocateQty = task.getUnmovedQuantityBU();
		if(moveDoc.isPutawayType() || moveDoc.isMoveType()){
			
			// 如果是托盘明细，取消分配时要扣减库位托盘占用数---一个task一个托盘
			if (wmsMoveDocDetail.getBePallet()) {
				WmsLocation location = load(WmsLocation.class,task.getToLocationId());
				location.removePallet(1);
			}
			
			// 上架库存取消分配
			WmsInventory inv = commonDao.load(WmsInventory.class,task.getDescInventoryId());
			inv.unallocatePutaway(unallocateQty);

			wmsInventoryManager.refreshLocationUseRate(inv.getLocation(), 0);

			// 更新上架单数量
			wmsMoveDocDetail.unallocate(task.getPlanQuantityBU());
			if (wmsMoveDocDetail.getAllocatedQuantityBU().doubleValue() < 0 
					|| wmsMoveDocDetail.getMoveDoc().getAllocatedQuantityBU().doubleValue() < 0) {
				throw new OriginalBusinessException("上架单分配数量不足,取消分配失败!");
			}

			// 删除task
			if (task.getMovedQuantityBU().doubleValue() == 0.0) {
				commonDao.delete(task);
			} else {
				task.unallocatePutaway();
			}
		} else if (moveDoc.isReplenishmentType()) {

			// 如果是托盘明细，取消分配时要扣减库位托盘占用数---一个task一个托盘
			if (wmsMoveDocDetail.getBePallet()) {
				WmsLocation location = load(WmsLocation.class,task.getToLocationId());
				location.removePallet(1);
			}
			
			// 拣货库存取消分配
			WmsInventory fromInv = commonDao.load(WmsInventory.class,task.getSrcInventoryId());
			fromInv.unallocatePickup(unallocateQty);
			wmsInventoryManager.refreshLocationUseRate(fromInv.getLocation(),0);

			// 上架库存取消分配
			WmsInventory inv = commonDao.load(WmsInventory.class,task.getDescInventoryId());
			inv.unallocatePutaway(unallocateQty);
			wmsInventoryManager.refreshLocationUseRate(inv.getLocation(), 0);

			// 更新上架单数量
			wmsMoveDocDetail.unallocate(task.getPlanQuantityBU());
			if (wmsMoveDocDetail.getAllocatedQuantityBU().doubleValue() < 0
					|| wmsMoveDocDetail.getMoveDoc().getAllocatedQuantityBU().doubleValue() < 0) {
				throw new OriginalBusinessException("上架单分配数量不足,取消分配失败!");
			}

			// 删除task
			if (task.getMovedQuantityBU().doubleValue() == 0.0) {
				commonDao.delete(task);
			} else {
				task.unallocatePutaway();
			}
		} else{
			
		}
		workflowManager.doWorkflow(moveDoc,"wmsMoveDocProcess.cancelAllocateNode");
	}

	public void cancelAllocatePickTicketPart(WmsMoveDoc moveDoc, WmsTask task,
			List<String> adjustQuantityBUList) {
		Double adjustQuantityBU;
		try {
			adjustQuantityBU = Double.valueOf(adjustQuantityBUList.get(0));
		} catch (NumberFormatException nfe) {
			throw new BusinessException("数量只能为数值类型！");
		}
		if(adjustQuantityBU <= 0) {
			throw new BusinessException("数量必须为正整数！");
		}
		if (adjustQuantityBU.doubleValue() > task.getPlanQuantityBU().doubleValue()) {
			throw new BusinessException("数量不能大于原库存数量！");
		}
		
		WmsMoveDocDetail wmsMoveDocDetail = task.getMoveDocDetail();
		WmsInventory fromInv = commonDao.load(WmsInventory.class,task.getSrcInventoryId());
		fromInv.unallocatePickup(adjustQuantityBU);

		wmsInventoryManager.refreshLocationUseRate(fromInv.getLocation(),0);

		task.unallocatePick(adjustQuantityBU);
		task.calculateLoad();
		commonDao.store(task);

		if (task.getPlanQuantityBU().doubleValue() <= 0) {
			String deleteTaskLog = "DELETE FROM WmsTaskLog taskLog where taskLog.task.id = :taskId";
			commonDao.executeByHql(deleteTaskLog, "taskId", task.getId());
			commonDao.delete(task);
		}
		doProcessEntityUnAllocateResult(wmsMoveDocDetail, adjustQuantityBU);
		workflowManager.doWorkflow(moveDoc,"wmsMoveDocProcess.cancelAllocateNode");
		
	}
	
	public String getTitleByMovePlanTitle(Map param) {
		String str = new SimpleDateFormat("yyyyMMdd").format(new Date());
		return "移位计划-"+str;
	}
	public String getTitleByQualityPlanTitle(Map param) {
		String str = new SimpleDateFormat("yyyyMMdd").format(new Date());
		return "质检计划-"+str;
	}
	//{moveDocDetail.id=8, maintainWmsMoveDocQualityPage.tableInputValues={7=[]}, editCreateMoveDocQualityPage.moveDoc.billType.id=342, modifyMoveDocQualityPage.moveDocDetail.id=8, pageId=editCreateMoveDocQualityPage, editCreateMoveDocQualityPage.moveDoc.company.id=1, editCreateMoveDocQualityPage.id=8, moveDoc.code=FDJ01ZJ150429000004, rowData=8,A-11010103,-,500002,旺仔牛奶（原味 罐装 245ml）,个,5,5,0,0,-,2015-04-28,FDJ01IN150428000002,2015-04-28,2016-07-21,2016-02-22,-,测试供应商1,OPEN,1, editCreateMoveDocQualityPage.moveDoc.movePlanTitle=质检计划-20150429, id=8, moveDoc.billType.id=342, parentId=7, moveDoc.id=7, editCreateMoveDocQualityPage.moveDoc.code=FDJ01ZJ150429000004, editCreateMoveDocQualityPage.ids=[8], moveDocDetail.itemKey.item.name=旺仔牛奶（原味 罐装 245ml）, moveDocDetail.moveDoc.company.id=1, modifyMoveDocQualityPage.moveDocDetail.inventoryStatus=-, maintainWmsMoveDocQualityPage.ids=[7], modifyMoveDocQualityPage.id=8, parentIds=[7], modifyMoveDocQualityPage.moveDocDetail.moveDoc.company.id=1, tableInputValues={8=[]}, moveDocDetail.inventoryStatus=-, ids=[8], modifyMoveDocQualityPage.planQuantityBU=0, modifyMoveDocQualityPage.moveDocDetail.itemKey.item.name=旺仔牛奶（原味 罐装 245ml）, buttonId=modify, parentPageId=maintainWmsMoveDocQualityPage, editCreateMoveDocQualityPage.moveDoc.id=7, planQuantityBU=0, isEditPage=true, moveDoc.company.id=1, moveDoc.movePlanTitle=质检计划-20150429, maintainWmsMoveDocQualityPage.id=7}
	public Double getQualityPlanQuantityBu(Map param){
		WmsMoveDocDetail wdd = commonDao.load(WmsMoveDocDetail.class, Long.parseLong(param.get("moveDocDetail.id").toString()));
		return wdd.getUnProcessQuantityBU();
	}
	public String getQualityCompanyId(Map param){
		WmsMoveDocDetail wdd = commonDao.load(WmsMoveDocDetail.class, Long.parseLong(param.get("moveDocDetail.id").toString()));
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, wdd.getMoveDoc().getId());
		WmsOrganization company = commonDao.load(WmsOrganization.class, moveDoc.getCompany().getId());
		return company.getCode();
	}
	public void findWorker(WmsMoveDoc moveDoc,Long workerId){
		moveDoc.setWorker(commonDao.load(WmsWorker.class, workerId));
		commonDao.store(moveDoc);
	}
	public String getQualityItemStates(Map param){
		WmsMoveDocDetail wdd = commonDao.load(WmsMoveDocDetail.class, Long.parseLong(param.get("moveDocDetail.id").toString()));
		String hql = "SELECT itemState.name"+
	 " FROM WmsItemState itemState"+
	 " WHERE itemState.company.id IN ("+
	 " select mdd.moveDoc.company.id from WmsMoveDocDetail mdd where mdd.id = :moveDocDetailId"+
	 " ) "+
	 " AND itemState.name IN ("+
	 " select wqb.itemState.name from WmsQualityBillStatus wqb where wqb.billType.id IN ("+
		 " select mdd.moveDoc.billType.id from WmsMoveDocDetail mdd where mdd.id =:moveDocDetailId"+
		 " )"+
	 " )"+
	 " AND itemState.name NOT IN ("+
	 " select task.inventoryStatus from WmsTask task where task.moveDocDetail.id =:moveDocDetailId"+
	 " )";
		List<String> names = commonDao.findByQuery(hql, new String[]{"moveDocDetailId"}, new Object[]{wdd.getId()});
		String itemStates = "";
		if(names!=null && names.size()>0){
			StringBuffer ss = new StringBuffer();
			int over = 0;
			for(String name : names){
				over++;
				if(over==names.size()){
					ss.append("'"+name+"'");
				}else{
					ss.append("'"+name+"',");
				}
			}
			itemStates = ss.toString();
		}
		System.out.println(itemStates);
		return itemStates;
		
	}
	public void sendQuality(WmsMoveDoc moveDoc){
		/*InterfaceLog log = EntityFactory.getEntity(InterfaceLog.class);
		log.setType(BaseStatus.QUALITY);
		log.setFromSYS("FDJ");
		log.setToSYS("MES");
		UpdateInfo updateInfo = new UpdateInfo();
		updateInfo.setCreatedTime(new Date());
		updateInfo.setCreatorId(0L);
		updateInfo.setCreator("");
		log.setUpdateInfo(updateInfo);
		commonDao.store(log);*/
//		Task task = new Task(BaseStatus.QUALITY, "wmsMoveDocManager.createQuality", moveDoc.getId());
//		this.commonDao.store(task);
		createQuality(moveDoc.getId());
		moveDoc.setTransStatus(WmsWorkDocStatus.WORKING);//BaseStatus.ENABLED
		commonDao.store(moveDoc);
	}
	public void createQuality(Long moveDocId){
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, moveDocId);
		//查询质检单明细,按照asn+sup+item+extends1+质检唯一单号,汇总计算sendqty
		//modify 按照asn+sup+item+extends1只传一次,避免一个物料多个托盘多选导致接口质检量和ASN打印报表不一致
		List<WmsMoveDocDetail> mdd = commonDao.findByQuery("FROM WmsMoveDocDetail m"
				+ " WHERE m.moveDoc.id =:moveDocId","moveDocId", moveDocId);
		String s1=null,s2=null,s3=null,s4=null,s5=null,s6=null,s7=null,s8=null,s9=null,key = null,key1 = null;
		Double sendQty = 0D,receivedQuantityBU = 0D,availableQty=0D;
		Map<String,Double[]> map = new HashMap<String, Double[]>();
		Map<String,String> mapKey = new HashMap<String, String>();
		List<WmsMoveDocDetail> mms = new ArrayList();
		String company = moveDoc.getCompany().getCode();//货主编码
		for(WmsMoveDocDetail mm : mdd){
			s1 = mm.getItemKey().getLotInfo().getSoi();
			s2 = mm.getItemKey().getLotInfo().getSupplier().getCode();
			s3 = mm.getItem().getCode();
			s4 = mm.getItemKey().getLotInfo().getExtendPropC1();
			s5 = mm.getReplenishmentArea();
			s6 = mm.getItem().getName();
			s7 = mm.getItemKey().getLotInfo().getSupplier().getName();
			s8 = getRelateBill1ByAsnCode(s1);//送货单号
			s9 = mm.getQualityType();//送检分类
			availableQty = getAvailableQty(mm);//总数量
			key = s1+MyUtils.spilt1+s2+MyUtils.spilt1+s3+MyUtils.spilt1+s4+MyUtils.spilt1+s9;
			key1 = s1+MyUtils.spilt1+s2+MyUtils.spilt1+s3+MyUtils.spilt1+s4+MyUtils.spilt1
					+s5+MyUtils.spilt1+s6+MyUtils.spilt1+s7+MyUtils.spilt1
					+s8+MyUtils.spilt1+s9;
			if(!mapKey.containsKey(key)){
				sendQty = mm.getPlanQuantityBU();
				receivedQuantityBU = mm.getProcessPlanQuantityBU();
				map.put(key1, new Double[]{
						sendQty,receivedQuantityBU,availableQty
				});
				mapKey.put(key, "过滤重复明细");
			}else{
				mms.add(mm);
			}
		}
		if(mms.size()>0){
			for(WmsMoveDocDetail detail : mms){
				this.deleteMoveDocDetails(detail);
			}
		}
//		HibernateTransactionManager t = (HibernateTransactionManager) 
//				applicationContext.getBean("transactionManager");
//		Session s = t.getSessionFactory().getCurrentSession();
		//'MOVE_001','ASN_002','SUP_002','ITEM_002','-',12,mes质检单号,物料入库量
		//MOVECODE in verchar2,ASNCODE in varchar2,SUPPLIERCODE in varchar2,MATERIALCODE in varchar2,
	    //PROCESSSTATE in varchar2,SENDQTY in float,MESQUALITYCODE in varchar2,RECEIVEDQUANTITYBU in float
//		String produce = "call insert_middle_quality_testing(?,?,?,?,?,?,?,?,?,?,?)";
		
		String[] values = null;
		Double[] quantitys = null;
		Iterator<Entry<String, Double[]>> iter = map.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, Double[]> entry = iter.next();
			values = entry.getKey().split(MyUtils.spilt1);//asn+sup+item+extends1+质检唯一单号
			quantitys = entry.getValue();
			sendQty = quantitys[0];
			receivedQuantityBU = quantitys[1];
			availableQty = quantitys[2];
			try {
//				CallableStatement call = s.connection().prepareCall(produce);
//				call.setString(1, moveDoc.getCode());
//				call.setString(2, values[0]);
//				call.setString(3, values[1]);
//				call.setString(4, values[2]);
//				call.setString(5, values[3]);
//				call.setDouble(6, sendQty);
//				call.setString(7, values[4]);
//				call.setDouble(8, receivedQuantityBU);
//				call.execute();
//				call.close();
				/**插入送检数据*/
				wmsDealInterfaceDataManager.insertMiddleTable(moveDoc.getCode(), values[0],values[1],
						  values[2], values[3], sendQty, values[4], receivedQuantityBU, values[5],
						  values[6], UserHolder.getUser().getName(),values[7],availableQty,company,values[8]);
			} catch (HibernateException e) {
				e.printStackTrace();
			} 
		}
		mapKey.clear();map.clear();
	}
	
	Double getAvailableQty(WmsMoveDocDetail detail){
		String hql = "select inv.quantityBU from WmsInventory inv where inv.id=:id";
		Double qty = (Double) commonDao.findByQueryUniqueResult(hql,"id", detail.getInventoryId());
		if(null == qty){
			return 0d;
		}
		return qty;
	}
	//根据收货单号获取送货单号
	String getRelateBill1ByAsnCode(String asnCode){
		String hql = "select relatedBill1 From WmsASN where code=:code";
		Object obj = commonDao.findByQueryUniqueResult(hql, "code", asnCode);
		if(null == obj || "".equals(obj)){
			return null;
		}
		return obj.toString();
	}
	public void readMidQuality(WmsMoveDocDetail wdd,Double planQty,Boolean isBeBackInv,String itemStatus){
		if(planQty>0){
			writeQuality(wdd, planQty, isBeBackInv, itemStatus);
			wdd.setTransStatus();
		}
	}
	public void cancelAllTask(WmsMoveDoc moveDoc){
		if(moveDoc.getProcessQuantityBU()>0){
			LocalizedMessage.setMessage(MyUtils.font("加工量不为0,不允许取消;"));
		}else{
			//回写中间表WMS_CANCEL_FLAG字段为1
			String sql = "update "+MiddleTableName.W_QUALITY_TESTING_QIS+" m "
					+ "set m.wms_cancel_flag = 1,m.wms_memo = '"+moveDoc.getVehicleNo()+"'"
					+ " where m.code = '"+moveDoc.getCode()+"' and m.flag in (0,1)";
			
			wmsDealInterfaceDataManager.cancelQuality(sql);
//			HibernateTransactionManager t = (HibernateTransactionManager) 
//					applicationContext.getBean("transactionManager");
//			Session s = t.getSessionFactory().getCurrentSession();
//			int nums = 0;
//			try {
//				SQLQuery query = s.createSQLQuery(sql);
//				query.setString(0, moveDoc.getVehicleNo());//备注
//				query.setString(1, moveDoc.getCode());
//				nums = query.executeUpdate();
//			} catch (Exception e) {
//				e.printStackTrace();
//				throw new BusinessException(e.getLocalizedMessage());
//			}
//			if(nums<=0){
//				throw new BusinessException("当前质检单已开始作业或找不到数据,无法取消");
//			}
			//取消锁定量
			List<WmsMoveDocDetail> moveDocDetails = new ArrayList<WmsMoveDocDetail>(moveDoc.getDetails());
			for (WmsMoveDocDetail moveDocDetail : moveDocDetails) {
				deleteMoveDocDetails(moveDocDetail);
			}
			//质检单状态取消
			moveDoc.setStatus(WmsMoveDocStatus.CANCELED);
		}
	}
	
	/**
	 * 质检拆单
	 * 
	 */
	@Override
	public void wmsMoveDocQualitysplit(WmsMoveDoc oldMoveDoc) {
		if(oldMoveDoc.getPlanQuantityBU()-oldMoveDoc.getProcessQuantityBU()==0){
			LocalizedMessage.setMessage(MyUtils.font2("加工量等于计划量,不符合拆单需求"));
		}else{
			//新增一条质检单,明细为所选质检单已质检结果的明细,所选质检单从状态到单据号都不变,量减少
			//oldMoveDoc:加工量减去;newMoveDoc:计划量加工量都等于oldMoveDoc的加工量
			WmsMoveDoc newMoveDoc = new WmsMoveDoc();
			newMoveDoc.setBillType(oldMoveDoc.getBillType());
			newMoveDoc.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
			newMoveDoc.setCode(wmsBussinessCodeManager.generateCodeByRule(
					WmsWarehouseHolder.getWmsWarehouse(), WmsWarehouseHolder.getWmsWarehouse().getName(), "质检单",""));
			newMoveDoc.setType(WmsMoveDocType.MV_QUALITY_MOVE);
			newMoveDoc.setOriginalBillCode(oldMoveDoc.getCode());
			newMoveDoc.setProcessStatus(WmsQualityStatus.NULLVALUE);
			newMoveDoc.setDetails(new HashSet<WmsMoveDocDetail>());
			newMoveDoc.setCompany(oldMoveDoc.getCompany());
			newMoveDoc.setMovePlanTitle("送检拆单:"+oldMoveDoc.getCode());
			newMoveDoc.setTransStatus(WmsWorkDocStatus.WORKING);
			newMoveDoc.setStatus(WmsMoveDocStatus.OPEN);
			
			String hql = "From WmsMoveDocDetail md where md.moveDoc.id =:Id";
			List<WmsMoveDocDetail> oldMoveDocDetail = commonDao.findByQuery(hql, "Id",oldMoveDoc.getId());
			if(oldMoveDocDetail.size()>0 && oldMoveDocDetail!=null){
				for(WmsMoveDocDetail moveDocDetail : oldMoveDocDetail){
					Double processQuantityBU = moveDocDetail.getProcessQuantityBU();//加工数量
					Double planQuantityBU = moveDocDetail.getPlanQuantityBU();//计划移位数量
					Double count = planQuantityBU-processQuantityBU;
					//将已质检的明细拆分到新的质检单
					if(count==0.0){
//						oldMoveDoc.removeDetail(moveDocDetail);
						newMoveDoc.addDetail(moveDocDetail);
						commonDao.store(newMoveDoc);
						moveDocDetail.setMoveDoc(newMoveDoc);
						commonDao.store(moveDocDetail);
						newMoveDoc.setProcessQuantityBU(newMoveDoc.getProcessQuantityBU()+processQuantityBU);
						oldMoveDoc.setProcessQuantityBU(oldMoveDoc.getProcessQuantityBU()-processQuantityBU);//原质检单加工数量减少
						oldMoveDoc.setPlanQuantityBU(oldMoveDoc.getPlanQuantityBU()-planQuantityBU);//原质检单计划移位数量减少
					}
				}
				commonDao.store(oldMoveDoc);
			}
		}
	}
	
	public Map printSendQualityReport(WmsMoveDoc moveDoc){
		
		moveDoc.setPrintWorker(UserHolder.getUser().getName());
		moveDoc.setPrintQualityReportDate(new Date());
		commonDao.store(moveDoc);
		
		Map result = new HashMap();
		Map<Long,String> reportValue = new HashMap<Long, String>();
		reportValue.put(moveDoc.getId(), "jacSendQualityReport.raq");
		result.put(IPage.REPORT_VALUES, reportValue);
		result.put(IPage.REPORT_PRINT_NUM, 1);
			
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("moveDocId", moveDoc.getId());
		result.put(IPage.REPORT_PARAMS, params);
		return result;
	}

	@Override
	public Boolean printTask(Map map) {
		List<Long> idStr = (List) map.get("parentIds");
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, idStr.get(0));
		moveDoc.setIsPrint(Boolean.TRUE);
		moveDoc.setPrintDate(new Date());
		moveDoc.setPrintUser(UserHolder.getUser().getName());
		commonDao.store(moveDoc);
		return Boolean.TRUE;
	}
}