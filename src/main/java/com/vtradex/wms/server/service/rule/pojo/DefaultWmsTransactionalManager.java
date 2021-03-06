package com.vtradex.wms.server.service.rule.pojo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.mlw.vlh.ValueList;
import net.mlw.vlh.ValueListInfo;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;

import com.vtradex.rule.server.model.rule.Version;
import com.vtradex.rule.server.service.version.VersionManager;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.exception.OriginalBusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.valuelist.adapter.hib3.ValueListAdapter;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.inventory.WmsInventoryFee;
import com.vtradex.wms.server.model.inventory.WmsInventoryLogType;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.inventory.WmsQualityMoveSoiLog;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.model.move.WmsMoveDocType;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.move.WmsTaskLog;
import com.vtradex.wms.server.model.move.WmsTaskType;
import com.vtradex.wms.server.model.move.WmsTempMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsWorkDoc;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.model.receiving.WmsASNShelvesStauts;
import com.vtradex.wms.server.model.receiving.WmsReceivedRecord;
import com.vtradex.wms.server.model.shipping.WmsBOLStateLog;
import com.vtradex.wms.server.model.shipping.WmsPickTicket;
import com.vtradex.wms.server.model.shipping.WmsPickTicketDetail;
import com.vtradex.wms.server.model.shipping.WmsShipLot;
import com.vtradex.wms.server.model.shipping.WmsWaveDoc;
import com.vtradex.wms.server.model.shipping.WmsWaveDocDetail;
import com.vtradex.wms.server.model.shipping.WmsWaveDocWorkMode;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsLocationType;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
import com.vtradex.wms.server.service.GlobalParamUtils;
import com.vtradex.wms.server.service.base.WmsLocationManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryExtendManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.rule.WmsTransactionalManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.task.WmsTaskManager;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.PackageUtils;
import com.vtradex.wms.server.utils.WmsStringUtils;

@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
public class DefaultWmsTransactionalManager extends DefaultBaseManager
		implements WmsTransactionalManager {
	protected WorkflowManager workflowManager;
	protected WmsInventoryManager wmsInventoryManager;
	protected WmsTaskManager taskManager;
	protected WmsRuleManager ruleManager;
	protected WmsBussinessCodeManager codeManager;
	protected WmsLocationManager locationManager;
	protected ValueListAdapter valueListAdapter;
	protected WmsInventoryExtendManager inventoryExtendManager;

	public static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd");

	public DefaultWmsTransactionalManager(WorkflowManager workflowManager,
			WmsInventoryManager wmsInventoryManager,
			WmsTaskManager taskManager, WmsRuleManager ruleManager,
			WmsBussinessCodeManager codeManager,
			WmsLocationManager locationManager,
			ValueListAdapter valueListAdapter,
			WmsInventoryExtendManager inventoryExtendManager) {
		this.workflowManager = workflowManager;
		this.wmsInventoryManager = wmsInventoryManager;
		this.taskManager = taskManager;
		this.ruleManager = ruleManager;
		this.codeManager = codeManager;
		this.locationManager = locationManager;
		this.valueListAdapter = valueListAdapter;
		this.inventoryExtendManager = inventoryExtendManager;
	}

	public void allocate(Long moveDocDetailId, Long dstLocationId,
			Double planQuantity) {
		WmsMoveDocDetail detail = commonDao.load(WmsMoveDocDetail.class,
				moveDocDetailId);
		WmsLocation toLoc = commonDao.load(WmsLocation.class, dstLocationId);

		if (detail.getUnAllocateQuantityBU() < planQuantity) {
			throw new OriginalBusinessException("分配数量不能超过待上架数量！");
		}
		String pallet = detail.getPallet();
		if(planQuantity<detail.getUnAllocateQuantityBU()){
			pallet = BaseStatus.NULLVALUE;
		}
		wmsInventoryManager.verifyLocation(BaseStatus.LOCK_IN,dstLocationId, detail.getPackageUnit(),detail.getItemKey().getId(),
				detail.getItemKey().getItem().getId(),  planQuantity, pallet);
		putawayAllocate(detail, toLoc, planQuantity, null, true);
	}

	public double putawayAllocate(WmsTempMoveDocDetail mdd,
			Map<String, Object> result, String locationType) {
		int size = ((List<Map<String, Object>>) result.get("返回列表")).size();
		double tempQuantity = 0D;
		for (int i = 0; i < size; i++) {
			Map<String, Object> wmsTaskInfos = ((List<Map<String, Object>>) result
					.get("返回列表")).get(i);

			WmsMoveDocDetail detail = null;
			WmsMoveDocDetail pickDetail = null;

			String isCrossStock = String.valueOf(wmsTaskInfos.get("越库移位"));
			if ("是".equals(isCrossStock)) {
				Long pickDetailId = (Long) wmsTaskInfos.get("移位单明细序号");
				pickDetail = commonDao.load(WmsMoveDocDetail.class,
						pickDetailId);
			}

			// 如果是托盘分配，返回的上架数量必须是整托数量
			// 同时不允许上架分配时拆包装
			double planQuantity = Double.valueOf(String.valueOf(wmsTaskInfos
					.get("上架数量")));
			if (planQuantity <= 0) {
				continue;
			}
			WmsLocation toLoc = commonDao.load(WmsLocation.class,
					(Long) wmsTaskInfos.get("库位序号"));

			if (BaseStatus.PALLET.equals(locationType)) {
				// 托盘上架分配：
				for (int j = 0; j < mdd.getDetails().size(); j++) {
					detail = commonDao.load(WmsMoveDocDetail.class, mdd
							.getDetails().get(j).getId());
					if (planQuantity <= 0) {
						break;
					} else if (detail.getUnAllocateQuantityBU() > 0) {
						putawayAllocate(detail, toLoc,
								detail.getPlanQuantityBU(), pickDetail, false);
						planQuantity--;
						if ("是".equals(isCrossStock)) {
							tempQuantity += detail.getPlanQuantityBU();
						}
					}
				}
			} else {
				// 散货上架分配：对mdd包含明细逐一处理，直到所有分配数量全部和明细挂钩
				double planQuantityBU = planQuantity;
				for (WmsMoveDocDetail md : mdd.getDetails()) {
					md = commonDao.load(WmsMoveDocDetail.class, md.getId());
					double unAllocateQtyBU = md.getUnAllocateQuantityBU();
					if (planQuantityBU <= 0) {
						break;
					} else if (unAllocateQtyBU >= planQuantityBU) {
						putawayAllocate(md, toLoc, planQuantityBU, pickDetail,
								false);
						planQuantityBU = 0;
						if ("是".equals(isCrossStock)) {
							tempQuantity += planQuantityBU;
						}
					} else {
						putawayAllocate(md, toLoc, unAllocateQtyBU, pickDetail,
								false);
						planQuantityBU -= unAllocateQtyBU;
						if ("是".equals(isCrossStock)) {
							tempQuantity += unAllocateQtyBU;
						}
					}
				}
			}
		}
		return tempQuantity;
	}

	private void putawayAllocate(WmsMoveDocDetail detail, WmsLocation toLoc,
			double quantityBU, WmsMoveDocDetail pickDetail, Boolean beManual) {
		WmsLocation fromLoc = commonDao.load(WmsLocation.class,
				detail.getFromLocationId());
		WmsItemKey itemKey = commonDao.load(WmsItemKey.class, detail
				.getItemKey().getId());
		WmsItem item = commonDao.load(WmsItem.class, itemKey.getItem().getId());

		WmsInventory dstInventory = putawayAllocateJac(detail, toLoc, quantityBU);
		
		WmsTask task = taskManager.getTaskByConditions(detail, fromLoc, toLoc,
				detail.getItemKey(), dstInventory.getPackageUnit(),
				detail.getInventoryStatus(), quantityBU,
				detail.getInventoryId(), dstInventory.getId(), beManual);


		if (pickDetail != null) {
			WmsMoveDoc pickDoc = commonDao.load(WmsMoveDoc.class, pickDetail
					.getMoveDoc().getId());
			WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, detail
					.getMoveDoc().getId());
			pickDoc.setRelatedBill1(moveDoc.getCode());
			commonDao.store(pickDoc);
		}

	}
	private WmsInventory putawayAllocateJac(WmsMoveDocDetail detail,WmsLocation toLoc,double quantityBU){
		WmsInventory srcInventory = load(WmsInventory.class,detail.getInventoryId());
		WmsInventory dstInventory = wmsInventoryManager.getInventoryWithNew(
				toLoc, detail.getItemKey(), detail.getPackageUnit(),
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
		
		// 更新上架单及明细数量
		detail.allocate(quantityBU);
		commonDao.store(detail);
		
		workflowManager.doWorkflow(detail.getMoveDoc(),"wmsMoveDocProcess.allocate");
		return dstInventory;
	}

	public void unallocateMoveDoc(WmsMoveDoc moveDoc) {
		String hql = "FROM WmsMoveDocDetail detail WHERE detail.moveDoc.id = :moveDocId AND detail.allocatedQuantityBU > 0";

		List<WmsMoveDocDetail> list = commonDao.findByQuery(hql,
				new String[] { "moveDocId" }, new Object[] { moveDoc.getId() });

		if (list == null || list.size() <= 0) {
			return;
		}

		for (WmsMoveDocDetail wmsMoveDocDetail : list) {
			List<WmsTask> tasks = commonDao
					.findByQuery(
							"FROM WmsTask task "
									+ "where task.moveDocDetail.id = :moveDocDetailId and task.planQuantityBU>movedQuantityBU",
							new String[] { "moveDocDetailId" },
							new Object[] { wmsMoveDocDetail.getId() });

			for (WmsTask task : tasks) {
				// 如果是托盘明细，取消分配时要扣减库位托盘占用数---一个task一个托盘
				if (wmsMoveDocDetail.getBePallet()) {
					WmsLocation location = load(WmsLocation.class,task.getToLocationId());
					location.removePallet(1);
				}

				Double unallocateQty = task.getUnmovedQuantityBU();
				// 上架库存取消分配
				WmsInventory inv = commonDao.load(WmsInventory.class,
						task.getDescInventoryId());
				inv.unallocatePutaway(unallocateQty);

				wmsInventoryManager.refreshLocationUseRate(inv.getLocation(), 0);

				// 更新上架单数量
				wmsMoveDocDetail.unallocate(task.getPlanQuantityBU());
				if (wmsMoveDocDetail.getAllocatedQuantityBU().doubleValue() < 0
						|| wmsMoveDocDetail.getMoveDoc()
								.getAllocatedQuantityBU().doubleValue() < 0) {
					throw new OriginalBusinessException("上架单分配数量不足,取消分配失败!");
				}

				// 删除task
				if (task.getMovedQuantityBU().doubleValue() == 0.0) {
					commonDao.delete(task);
				} else {
					task.unallocatePutaway();
				}
			}
		}
		workflowManager.doWorkflow(moveDoc,
				"wmsMoveDocProcess.cancelAllocateNode");
	}

	/**
	 * 整单取消分配-补货单
	 * 
	 * @param moveDocs
	 */
	public void unallocateMoveDocForReplenishment(WmsMoveDoc moveDoc) {
		String hql = "FROM WmsMoveDocDetail detail WHERE detail.moveDoc.id = :moveDocId AND detail.allocatedQuantityBU > 0";

		List<WmsMoveDocDetail> list = commonDao.findByQuery(hql,
				new String[] { "moveDocId" }, new Object[] { moveDoc.getId() });
		if (list == null || list.size() <= 0) {
			return;
		}

		String taskHql = "SELECT task.id FROM WmsTask task where task.moveDocDetail.id = :moveDocDetailId and task.planQuantityBU>movedQuantityBU";
		for (WmsMoveDocDetail wmsMoveDocDetail : list) {
			List<Long> taskIds = commonDao.findByQuery(taskHql,
					new String[] { "moveDocDetailId" },
					new Object[] { wmsMoveDocDetail.getId() });
			for (Long taskId : taskIds) {
				WmsTask task = commonDao.load(WmsTask.class, taskId);
				// 如果是托盘明细，取消分配时要扣减库位托盘占用数---一个task一个托盘
				if (wmsMoveDocDetail.getBePallet()) {
					WmsLocation location = load(WmsLocation.class,task.getToLocationId());
					location.removePallet(1);
				}

				Double unallocateQty = task.getUnmovedQuantityBU();

				// 拣货库存取消分配
				WmsInventory fromInv = commonDao.load(WmsInventory.class,
						task.getSrcInventoryId());
				fromInv.unallocatePickup(unallocateQty);
				wmsInventoryManager.refreshLocationUseRate(fromInv.getLocation(), 0);

				// 上架库存取消分配
				WmsInventory inv = commonDao.load(WmsInventory.class,
						task.getDescInventoryId());
				inv.unallocatePutaway(unallocateQty);
				wmsInventoryManager.refreshLocationUseRate(inv.getLocation(),0);

				// 更新上架单数量
				wmsMoveDocDetail.unallocate(task.getPlanQuantityBU());
				if (wmsMoveDocDetail.getAllocatedQuantityBU().doubleValue() < 0
						|| wmsMoveDocDetail.getMoveDoc()
								.getAllocatedQuantityBU().doubleValue() < 0) {
					throw new OriginalBusinessException("上架单分配数量不足,取消分配失败!");
				}

				// 删除task
				if (task.getMovedQuantityBU().doubleValue() == 0.0) {
					commonDao.delete(task);
				} else {
					task.unallocatePutaway();
				}
			}
		}
		workflowManager.doWorkflow(moveDoc,
				"wmsMoveDocProcess.cancelAllocateNode");
	}

	private WmsWaveDocDetail getWaveDocDetailByPickTicketDetail(
			WmsWaveDoc waveDoc, WmsPickTicketDetail detail) {
		// if(BaseStatus.SINGLEPICK.equals(waveDoc.getWorkMode())){
		// //波次按单拣货
		// for (WmsWaveDocDetail wdd : waveDoc.getDetails()) {
		// if (wdd.getPickTicketCode().equals(detail.getPickTicket().getCode())
		// && wdd.getItem().getId().equals(detail.getItem().getId())
		// &&
		// wdd.getPackageUnit().getId().equals(detail.getPackageUnit().getId())
		// && ((wdd.getShipLotInfo()==null && detail.getShipLotInfo()==null)
		// ||
		// wdd.getShipLotInfo().genHashCode().equals(detail.getShipLotInfo().genHashCode())))
		// {
		// return wdd;
		// }
		// }
		// } else {
		// //波次批拣
		// for (WmsWaveDocDetail wdd : waveDoc.getDetails()) {
		// if (wdd.getItem().getId().equals(detail.getItem().getId())
		// &&
		// wdd.getPackageUnit().getId().equals(detail.getPackageUnit().getId())
		// && ((wdd.getShipLotInfo()==null && detail.getShipLotInfo()==null)
		// ||
		// wdd.getShipLotInfo().genHashCode().equals(detail.getShipLotInfo().genHashCode())))
		// {
		// return wdd;
		// }
		// }
		// }
		return null;
	}

	private Map getSplitInventoriesMap(Map<String, Object> problem,
			Boolean isMatchLot) {
		String hql = "SELECT inv.status AS 组号, inv.id AS 库存ID, loc.id AS 库位ID, loc.code AS 库位编码, "
				+ " ik.lot AS 批次号, item.code AS 货品编码, item.name AS 货品名称, "
				+ " unit.unit AS 货品包装, ik.lotInfo.storageDate AS 存货日期, (inv.quantityBU - inv.allocatedQuantityBU) AS 库存数量 "
				+ " FROM WmsInventory inv "
				+ " LEFT JOIN inv.location loc "
				+ " LEFT JOIN inv.itemKey ik "
				+ " LEFT JOIN inv.itemKey.item item "
				+ " LEFT JOIN inv.packageUnit unit "
				+ " LEFT JOIN inv.itemKey.lotInfo.supplier sup "
				+ " WHERE 1=1 "
				+ " AND inv.quantityBU - inv.allocatedQuantityBU > 0 "
				+ " /~库位ID: AND loc.id = {库位ID}~/ "
				+ " /~货品ID: AND item.id = {货品ID}~/ ";
		if (isMatchLot) {
			hql += " /~收货日期BEGIN: AND ik.lotInfo.storageDate >= {收货日期BEGIN}~/ "
					+ " /~收货日期END: AND ik.lotInfo.storageDate < {收货日期END}~/ "
					+ " /~收货日期: AND ik.lotInfo.storageDate = {收货日期}~/ "
					+ " /~生产日期BEGIN: AND ik.lotInfo.produceDate >= {生产日期BEGIN}~/ "
					+ " /~生产日期END: AND ik.lotInfo.produceDate < {生产日期END}~/ "
					+ " /~生产日期: AND ik.lotInfo.produceDate = {生产日期}~/ "
					+ " /~失效日期BEGIN: AND ik.lotInfo.expireDate >= {失效日期BEGIN}~/ "
					+ " /~失效日期END: AND ik.lotInfo.expireDate < {失效日期END}~/ "
					+ " /~失效日期: AND ik.lotInfo.expireDate = {失效日期}~/ "
					+ " /~近效期BEGIN: AND ik.lotInfo.warnDate >= {近效期BEGIN}~/ "
					+ " /~近效期END: AND ik.lotInfo.warnDate < {近效期END}~/ "
					+ " /~近效期: AND ik.lotInfo.warnDate = {近效期}~/ "
					+ " /~厂商批号: AND ik.batchNum = {厂商批号}~/ "
					+ " /~收货单号: AND ik.soi = {收货单号}~/ "
					+ " /~供应商: AND sup.name = {供应商}~/ "
					+ " /~扩展属性1: AND ik.extendPropC1 = {扩展属性1}~/ "
					+ " /~扩展属性2: AND ik.extendPropC2 = {扩展属性2}~/ "
					+ " /~扩展属性3: AND ik.extendPropC3 = {扩展属性3}~/ "
					+ " /~扩展属性4: AND ik.extendPropC4 = {扩展属性4}~/ "
					+ " /~扩展属性5: AND ik.extendPropC5 = {扩展属性5}~/ "
					+ " /~扩展属性6: AND ik.extendPropC6 = {扩展属性6}~/ "
					+ " /~扩展属性7: AND ik.extendPropC7 = {扩展属性7}~/ "
					+ " /~扩展属性8: AND ik.extendPropC8 = {扩展属性8}~/ "
					+ " /~扩展属性9: AND ik.extendPropC9 = {扩展属性9}~/ "
					+ " /~扩展属性10: AND ik.extendPropC10 = {扩展属性10}~/ "
					+ " /~扩展属性11: AND ik.extendPropC11 = {扩展属性11}~/ "
					+ " /~扩展属性12: AND ik.extendPropC12 = {扩展属性12}~/ "
					+ " /~扩展属性13: AND ik.extendPropC13 = {扩展属性13}~/ "
					+ " /~扩展属性14: AND ik.extendPropC14 = {扩展属性14}~/ "
					+ " /~扩展属性15: AND ik.extendPropC15 = {扩展属性15}~/ "
					+ " /~扩展属性16: AND ik.extendPropC16 = {扩展属性16}~/ "
					+ " /~扩展属性17: AND ik.extendPropC17 = {扩展属性17}~/ "
					+ " /~扩展属性18: AND ik.extendPropC18 = {扩展属性18}~/ "
					+ " /~扩展属性19: AND ik.extendPropC19 = {扩展属性19}~/ "
					+ " /~扩展属性20: AND ik.extendPropC20 = {扩展属性20}~/ ";
		}

		hql = removeHqlWithNullParam(hql, problem);

		ValueListInfo info = new ValueListInfo(problem);
		ValueList results = valueListAdapter.getValueList(hql, info);

		Map map = new HashMap();

		for (Object[] values : (List<Object[]>) results.getList()) {
			if (map.get("组号") == null) {
				map.put("组号", values[0]);
			}

			if (map.get("库存") == null) {
				List<Map> invs = new ArrayList();
				map.put("库存", invs);
			}

			Map map1 = new HashMap();
			map1.put("库存ID", values[1]);
			map1.put("库位ID", values[2]);
			map1.put("库位编码", values[3]);
			map1.put("批次号", values[4]);
			map1.put("货品编码", values[5]);
			map1.put("货品名称", values[6]);
			map1.put("货品包装", values[7]);
			map1.put("存货日期", values[8]);
			map1.put("库存数量", values[9]);

			((List<Map>) map.get("库存")).add(map1);
		}

		return map;
	}

	public void autoAllocate(WmsPickTicket pickTicket, WmsWaveDoc waveDoc) {
		// for (WmsPickTicketDetail detail : pickTicket.getDetails()) {
		// double initQty = detail.getUnallocateQuantity();
		// //调用规则进行波次分单---严格批次属性匹配
		// if (detail.getUnallocateQuantity() > 0) {
		// Map<String, Object> problem = pickProblemsCreator(detail,
		// Boolean.TRUE);
		// // problem.put("库位ID", waveDoc.getToLocationId());
		// //事务中准备库存，避免规则中无法取得正确的库存结果
		// problem.put("库存结果集", getSplitInventoriesMap(problem, Boolean.TRUE));
		// Map<String, Object> result =
		// ruleManager.execute(pickTicket.getWarehouse().getName(),
		// pickTicket.getCompany().getName(), "波次分单规则", problem);
		// pickAllocate(detail, result);
		// }
		// //未整单分配时，调用规则进行波次分单---不严格批次属性匹配
		// /*
		// if (detail.getUnallocateQuantity() > 0) {
		// Map<String, Object> problem = pickProblemsCreator(detail,
		// Boolean.FALSE);
		// problem.put("库位ID", waveDoc.getToLocationId());
		// Map<String, Object> result =
		// ruleManager.execute(detail.getItem().getCompany().getName(),
		// "波次分单规则", problem);
		// pickAllocate(detail, result);
		// }
		// */
		// //更新波次明细的分单数量
		// WmsWaveDocDetail waveDocDetail =
		// getWaveDocDetailByPickTicketDetail(waveDoc, detail);
		// if (waveDocDetail == null) {
		// throw new OriginalBusinessException("发货单明细与波次明细不匹配，不能分单！");
		// }
		// waveDocDetail.addSplitedQuantityBU(initQty -
		// detail.getUnallocateQuantity());
		// }
	}

	public void manualProcessDocAllocate(Map params) {
		// WmsProcessDocDetail detail =
		// commonDao.load(WmsProcessDocDetail.class,
		// (Long) params.get("processDocDetailId"));
		// WmsProcessDoc processDoc = detail.getProcessDoc();
		//
		// Map inv = (Map) params.get(IPage.TABLE_INPUT_VALUES);
		//
		// WmsInventory srcInv = null;
		// double allocateQty = 0.0D;
		//
		// for (Object obj : inv.keySet()) {
		// try {
		// srcInv = commonDao.load(WmsInventory.class, (Long) obj);
		// allocateQty = Double.valueOf(inv.get((Long) obj).toString());
		//
		// if (srcInv == null || allocateQty <= 0) {
		// return;
		// }
		//
		// WmsItemKey itemKey = srcInv.getItemKey();
		// WmsLocation toLoc = commonDao.load(WmsLocation.class,
		// processDoc.getLocationId());
		//
		// // 根据目标库位拆箱级别决定包装形态
		// WmsPackageUnit wpu = detail.getPackageUnit();
		// WmsInventory dstInv = wmsInventoryManager.getInventoryWithNew(
		// toLoc, itemKey, wpu, srcInv.getStatus());
		//
		// WmsTask task = null;
		// // task = taskManager.getTaskByConditions(detail,
		// // srcInv.getLocation(),
		// // dstInv.getLocation(), itemKey, detail.getPackageUnit(),
		// // srcInv.getStatus(),
		// // allocateQty, srcInv.getId(), dstInv.getId(), true);
		// // 改写源库存待拣货数量
		// srcInv.allocatePickup(allocateQty);
		// // 创建目标库存, 并将待上架数量写入该库存
		// dstInv.allocatePutaway(allocateQty);
		//
		// commonDao.store(task);
		// // 回写加工单明细分配数量
		// detail.allocate(allocateQty);
		// workflowManager.doWorkflow(detail,
		// "wmsProcessDocDetailProcess.allocate");
		// } catch (Exception e) {
		// throw new OriginalBusinessException("页面参数有误, 不能完成分配操作!");
		// }
		// }
	}

	public void confirmAll(Long workDocId, Long workerId) {
		List<WmsTask> tasks = commonDao
				.findByQuery(
						"FROM WmsTask task "
								+ " WHERE task.workDoc.id = :workDocId AND task.status NOT IN ('CANCEL','FINISHED')",
						new String[] { "workDocId" },
						new Object[] { workDocId });
		for (WmsTask task : tasks) {
			singleWorkConfirm(task, task.getToLocationId(),
					task.getFromLocationId(), task.getUnmovedQuantityBU(),
					workerId);
		}
	}

	public List<WmsInventoryExtend> getWmsInventoryExtendByTask(WmsTask task) {
		List<WmsInventoryExtend> wsns = new ArrayList<WmsInventoryExtend>();
		if (task.getMoveDocDetail().getBePallet()) {
			wsns = commonDao
					.findByQuery(
							"FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId AND wsn.pallet = :pallet AND wsn.quantityBU > 0 ",
							new String[] { "inventoryId", "pallet" },
							new Object[] { task.getSrcInventoryId(),
									task.getMoveDocDetail().getPallet() });

		} else if (StringUtils.isNotBlank(task.getMoveDocDetail().getCarton())
				&& !BaseStatus.NULLVALUE.equals(task.getMoveDocDetail()
						.getCarton())
				&& !BaseStatus.NULLVALUE.equals(task.getMoveDocDetail()
						.getCarton())) {
			wsns = commonDao
					.findByQuery(
							"FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId AND wsn.carton = :carton AND wsn.quantityBU > 0  ",
							new String[] { "inventoryId", "carton" },
							new Object[] { task.getSrcInventoryId(),
									task.getMoveDocDetail().getCarton() });
		} else if (StringUtils
				.isNotBlank(task.getMoveDocDetail().getSerialNo())
				&& !BaseStatus.NULLVALUE.equals(task.getMoveDocDetail()
						.getSerialNo())) {
			wsns = commonDao
					.findByQuery(
							"FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId AND wsn.serialNo = :serialNo AND wsn.quantityBU > 0  ",
							new String[] { "inventoryId", "serialNo" },
							new Object[] { task.getSrcInventoryId(),
									task.getMoveDocDetail().getSerialNo() });

		} else {
			if (task.bePutType()) {
				wsns = commonDao
						.findByQuery(
								"FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId "
										+ "AND wsn.serialNo = :serialNo AND wsn.carton = :carton AND wsn.pallet = :pallet AND wsn.quantityBU > 0  ",
								new String[] { "inventoryId", "serialNo",
										"carton", "pallet" }, new Object[] {
										task.getSrcInventoryId(),
										BaseStatus.NULLVALUE,
										BaseStatus.NULLVALUE,
										BaseStatus.NULLVALUE });
			} else {
				wsns = commonDao
						.findByQuery(
								"FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId AND wsn.quantityBU > 0 ",
								new String[] { "inventoryId" },
								new Object[] { task.getSrcInventoryId() });
			}
		}
		return wsns;
	}

	public List<WmsInventoryExtend> getWmsInventoryExtends(WmsTask task,
			Long locationId) {
		List<WmsInventoryExtend> wsns = new ArrayList<WmsInventoryExtend>();
		if (task.getMoveDocDetail().getBePallet()) {
			wsns = commonDao
					.findByQuery(
							"SELECT wsn FROM WmsInventoryExtend wsn LEFT JOIN wsn.inventory inv WHERE inv.location.id = :locationId AND inv.itemKey.id = :itemKeyId AND wsn.pallet = :pallet AND wsn.quantityBU > 0 ",
							new String[] { "locationId", "itemKeyId", "pallet" },
							new Object[] { locationId,
									task.getItemKey().getId(),
									task.getMoveDocDetail().getPallet() });

		} else if (StringUtils.isNotBlank(task.getMoveDocDetail().getCarton())
				&& !BaseStatus.NULLVALUE.equals(task.getMoveDocDetail()
						.getCarton())) {
			wsns = commonDao
					.findByQuery(
							"SELECT wsn FROM WmsInventoryExtend wsn LEFT JOIN wsn.inventory inv WHERE inv.location.id = :locationId AND inv.itemKey.id = :itemKeyId AND wsn.carton = :carton AND wsn.quantityBU > 0 ",
							new String[] { "locationId", "itemKeyId", "carton" },
							new Object[] { locationId,
									task.getItemKey().getId(),
									task.getMoveDocDetail().getCarton() });
		} else if (StringUtils
				.isNotBlank(task.getMoveDocDetail().getSerialNo())
				&& !BaseStatus.NULLVALUE.equals(task.getMoveDocDetail()
						.getSerialNo())) {
			wsns = commonDao
					.findByQuery(
							"SELECT wsn FROM WmsInventoryExtend wsn LEFT JOIN wsn.inventory inv WHERE inv.location.id = :locationId AND inv.itemKey.id = :itemKeyId AND wsn.serialNo = :serialNo AND wsn.quantityBU > 0  ",
							new String[] { "locationId", "itemKeyId",
									"serialNo" }, new Object[] { locationId,
									task.getItemKey().getId(),
									task.getMoveDocDetail().getSerialNo() });

		} else {
			if (task.bePutType()) {
				wsns = commonDao
						.findByQuery(
								"SELECT wsn FROM WmsInventoryExtend wsn LEFT JOIN wsn.inventory inv WHERE inv.location.id = :locationId AND inv.itemKey.id = :itemKeyId "
										+ "AND wsn.serialNo = :serialNo AND wsn.carton = :carton AND wsn.pallet = :pallet AND wsn.quantityBU > 0  ",
								new String[] { "locationId", "itemKeyId",
										"serialNo", "carton", "pallet" },
								new Object[] { locationId,
										task.getItemKey().getId(),
										BaseStatus.NULLVALUE,
										BaseStatus.NULLVALUE,
										BaseStatus.NULLVALUE });
			} else {
				wsns = commonDao
						.findByQuery(
								"SELECT wsn FROM WmsInventoryExtend wsn LEFT JOIN wsn.inventory inv WHERE inv.location.id = :locationId AND inv.itemKey.id = :itemKeyId AND wsn.quantityBU > 0 ",
								new String[] { "locationId", "itemKeyId" },
								new Object[] { locationId,
										task.getItemKey().getId() });
			}
		}
		return wsns;
	}

	public void singleWorkConfirm(WmsTask task, Long toLocationId,
			Long fromLocationId, Double quantityBU, Long workerId) {
		if (!WmsStringUtils.isEmpty(task.getMoveDocDetail().getSerialNo())
				&& !BaseStatus.NULLVALUE.equals(task.getMoveDocDetail()
						.getSerialNo())) {
			confirmBySerialNo(task.getId(), fromLocationId, toLocationId, null,
					null, quantityBU, workerId);
		} else if (!WmsStringUtils.isEmpty(task.getMoveDocDetail().getCarton())
				&& !BaseStatus.NULLVALUE.equals(task.getMoveDocDetail()
						.getCarton())) {
			confirmByCarton(task.getId(), fromLocationId, toLocationId, null,
					quantityBU, workerId);
		} else if (task.getMoveDocDetail().getBePallet()) {
			confirmByPallet(task.getId(), fromLocationId, toLocationId,
					quantityBU, workerId);
		} else {
			confirmByItem(task.getId(), fromLocationId, toLocationId,
					quantityBU, workerId);
		}
		inventoryCrossDock(task);
	}

	private void inventoryCrossDock(WmsTask task) {
		WmsLocation location = commonDao.load(WmsLocation.class,
				task.getToLocationId());
		if (!WmsLocationType.SHIP.equals(location.getType())) {
			return;
		}

		// 获取货品与批次属性都符合的越库拣货明细
		Set<WmsMoveDocDetail> pickDetails = getCrossDockDetailByTask(task);

		double allocateQuantity = task.getMovedQuantityBU();
		for (WmsMoveDocDetail pickDetail : pickDetails) {
			if (allocateQuantity <= 0) {
				break;
			}
			//未分配数量
			double unAllocatedQuantityBU = pickDetail.getPlanQuantityBU() - pickDetail.getAllocatedQuantityBU();
			
			double allcateQuanity4CrossDock = unAllocatedQuantityBU > allocateQuantity ? allocateQuantity
					: unAllocatedQuantityBU;
			confirmCrossDock(pickDetail, allcateQuanity4CrossDock, task);
			allocateQuantity -= allcateQuanity4CrossDock;
		}
	}

	private Set<WmsMoveDocDetail> getCrossDockDetailByTask(WmsTask task) {
		WmsMoveDocDetail moveDocDetail = commonDao.load(WmsMoveDocDetail.class,
				task.getMoveDocDetail().getId());
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, moveDocDetail
				.getMoveDoc().getId());
		WmsItemKey itemKey = commonDao.load(WmsItemKey.class, task.getItemKey()
				.getId());
		StringBuffer hqlBuf = new StringBuffer();
		hqlBuf.append(
				"FROM WmsMoveDocDetail detail WHERE detail.item.id=:itemId")
				.append(" AND detail.moveDoc.relatedBill1=:relateCode")
				.append(" AND detail.moveDoc.beCrossDock = true")
				.append(" AND detail.moveDoc.warehouse.id=:warehouseId")
				.append(" AND detail.moveDoc.company.id=:companyId")
				.append(" AND (detail.planQuantityBU - detail.allocatedQuantityBU) > 0");

		List<WmsMoveDocDetail> pickDetails = commonDao.findByQuery(
				hqlBuf.toString(), new String[] { "itemId", "relateCode",
						"warehouseId", "companyId" }, new Object[] {
						itemKey.getItem().getId(), moveDoc.getCode(),
						moveDoc.getWarehouse().getId(),
						moveDoc.getCompany().getId() });

		Set<WmsMoveDocDetail> details = new HashSet<WmsMoveDocDetail>();
		for (WmsMoveDocDetail pickDetail : pickDetails) {
			if (!validateShipLotInfo(pickDetail, itemKey)) {
				continue;
			}
			details.add(pickDetail);
		}

		return details;
	}

	private void confirmCrossDock(WmsMoveDocDetail pickDetail, double quantity,
			WmsTask task) {
		WmsInventory srcInv = commonDao.load(WmsInventory.class,
				task.getDescInventoryId());

		pickDetail.setInventoryId(srcInv.getId());
		pickDetail.setFromLocationId(task.getFromLocationId());
		pickDetail.setFromLocationCode(task.getFromLocationCode());
		pickDetail.setToLocationId(srcInv.getLocation().getId());
		pickDetail.setToLocationCode(srcInv.getLocation().getCode());
		pickDetail.allocate(quantity);
		pickDetail.move(quantity);
		commonDao.store(pickDetail);

		WmsPickTicketDetail pickTickDetail = commonDao.load(
				WmsPickTicketDetail.class, pickDetail.getRelatedId());
		pickTickDetail.allocate(quantity);
		pickTickDetail.addPickedQuantityBU(quantity);
		commonDao.store(pickTickDetail);

		workflowManager.doWorkflow(pickDetail.getMoveDoc(),
				"wmsMoveDocProcess.putAwayCrossDock");
	}

	private boolean validateShipLotInfo(WmsMoveDocDetail pickDetail,
			WmsItemKey itemKey) {
		boolean flag = true;
		String supplierName = null;
		if (itemKey != null && itemKey.getLotInfo() != null
				&& itemKey.getLotInfo().getSupplier() != null) {
			WmsOrganization supplier = commonDao.load(WmsOrganization.class,
					itemKey.getLotInfo().getSupplier().getId());
			supplierName = supplier.getName();
		}
		if (pickDetail.getShipLotInfo() != null && itemKey.getLotInfo() == null) {
			flag = false;
		}
		if (pickDetail.getShipLotInfo() != null && itemKey.getLotInfo() != null) {
			if ((pickDetail.getShipLotInfo().getSoi() != null && !""
					.equals(pickDetail.getShipLotInfo().getSoi()))
					&& (itemKey.getLotInfo().getSoi() == null || !itemKey
							.getLotInfo().getSoi()
							.equals(pickDetail.getShipLotInfo().getSoi()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getSupplier() != null && !""
					.equals(pickDetail.getShipLotInfo().getSupplier()))
					&& (supplierName == null || !supplierName.equals(pickDetail
							.getShipLotInfo().getSupplier()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC1() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC1()))
					&& (itemKey.getLotInfo().getExtendPropC1() == null || !itemKey
							.getLotInfo()
							.getExtendPropC1()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC1()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC2() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC2()))
					&& (itemKey.getLotInfo().getExtendPropC2() == null || !itemKey
							.getLotInfo()
							.getExtendPropC2()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC2()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC3() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC3()))
					&& (itemKey.getLotInfo().getExtendPropC3() == null || !itemKey
							.getLotInfo()
							.getExtendPropC3()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC3()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC4() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC4()))
					&& (itemKey.getLotInfo().getExtendPropC4() == null || !itemKey
							.getLotInfo()
							.getExtendPropC4()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC4()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC5() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC5()))
					&& (itemKey.getLotInfo().getExtendPropC5() == null || !itemKey
							.getLotInfo()
							.getExtendPropC5()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC5()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC6() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC6()))
					&& (itemKey.getLotInfo().getExtendPropC6() == null || !itemKey
							.getLotInfo()
							.getExtendPropC6()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC6()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC7() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC7()))
					&& (itemKey.getLotInfo().getExtendPropC7() == null || !itemKey
							.getLotInfo()
							.getExtendPropC7()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC7()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC8() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC8()))
					&& (itemKey.getLotInfo().getExtendPropC8() == null || !itemKey
							.getLotInfo()
							.getExtendPropC8()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC8()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC9() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC9()))
					&& (itemKey.getLotInfo().getExtendPropC9() == null || !itemKey
							.getLotInfo()
							.getExtendPropC9()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC9()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC10() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC10()))
					&& (itemKey.getLotInfo().getExtendPropC10() == null || !itemKey
							.getLotInfo()
							.getExtendPropC10()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC10()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC11() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC11()))
					&& (itemKey.getLotInfo().getExtendPropC11() == null || !itemKey
							.getLotInfo()
							.getExtendPropC11()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC11()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC12() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC12()))
					&& (itemKey.getLotInfo().getExtendPropC12() == null || !itemKey
							.getLotInfo()
							.getExtendPropC12()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC12()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC13() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC13()))
					&& (itemKey.getLotInfo().getExtendPropC13() == null || !itemKey
							.getLotInfo()
							.getExtendPropC13()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC13()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC14() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC14()))
					&& (itemKey.getLotInfo().getExtendPropC14() == null || !itemKey
							.getLotInfo()
							.getExtendPropC14()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC14()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC15() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC15()))
					&& (itemKey.getLotInfo().getExtendPropC15() == null || !itemKey
							.getLotInfo()
							.getExtendPropC15()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC15()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC16() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC16()))
					&& (itemKey.getLotInfo().getExtendPropC16() == null || !itemKey
							.getLotInfo()
							.getExtendPropC16()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC16()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC17() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC17()))
					&& (itemKey.getLotInfo().getExtendPropC17() == null || !itemKey
							.getLotInfo()
							.getExtendPropC17()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC17()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC18() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC18()))
					&& (itemKey.getLotInfo().getExtendPropC18() == null || !itemKey
							.getLotInfo()
							.getExtendPropC18()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC18()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC19() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC19()))
					&& (itemKey.getLotInfo().getExtendPropC19() == null || !itemKey
							.getLotInfo()
							.getExtendPropC19()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC19()))) {
				flag = false;
			}
			if ((pickDetail.getShipLotInfo().getExtendPropC20() != null && !""
					.equals(pickDetail.getShipLotInfo().getExtendPropC20()))
					&& (itemKey.getLotInfo().getExtendPropC20() == null || !itemKey
							.getLotInfo()
							.getExtendPropC20()
							.equals(pickDetail.getShipLotInfo()
									.getExtendPropC20()))) {
				flag = false;
			}
		}
		return flag;
	}

	public void confirmByPallet(Long taskId, Long fromLocationId,
			Long toLocationId, Double quantityBU, Long workerId) {
		WmsTask task = commonDao.load(WmsTask.class, taskId);
		Long locationId = fromLocationId;
		if (fromLocationId == null || fromLocationId == 0L) {
			locationId = task.getFromLocationId();
		}
		String pallet = task.getMoveDocDetail().getPallet();
		pallet = WmsStringUtils.isEmpty(pallet) ? BaseStatus.NULLVALUE : pallet;
		Double palletQuantityBU = (Double) commonDao
				.findByQueryUniqueResult(
						"SELECT SUM(quantityBU) FROM WmsInventoryExtend wsn WHERE wsn.inventory.location.id = :locationId AND wsn.inventory.itemKey.id = :itemKeyId"
								+ " AND wsn.pallet = :pallet", new String[] {
								"locationId", "itemKeyId", "pallet" },
						new Object[] { locationId, task.getItemKey().getId(),
								pallet });

		if (DoubleUtils.compareByPrecision(palletQuantityBU, quantityBU, task
				.getPackageUnit().getPrecision()) == 0) {
			// 整托移位
			confirm(taskId, fromLocationId, toLocationId, pallet, null, null,
					pallet, null, quantityBU, workerId);
		} else {
			// 拆托移位
			confirm(taskId, fromLocationId, toLocationId, pallet, null, null,
					BaseStatus.NULLVALUE, null, quantityBU, workerId);
		}
	}

	public void confirmByCarton(Long taskId, Long fromLocationId,
			Long toLocationId, String newPallet, Double quantityBU,
			Long workerId) {
		WmsTask task = commonDao.load(WmsTask.class, taskId);
		Long locationId = fromLocationId;
		if (fromLocationId == null || fromLocationId == 0L) {
			locationId = task.getFromLocationId();
		}
		newPallet = WmsStringUtils.isEmpty(newPallet) ? BaseStatus.NULLVALUE
				: newPallet;
		String newCarton = task.getMoveDocDetail().getCarton();
		newCarton = WmsStringUtils.isEmpty(newCarton) ? BaseStatus.NULLVALUE
				: newCarton;

		Double cartonQuantityBU = (Double) commonDao
				.findByQueryUniqueResult(
						"SELECT SUM(quantityBU) FROM WmsInventoryExtend wsn WHERE wsn.inventory.location.id = :locationId AND wsn.inventory.itemKey.id = :itemKeyId"
								+ " AND wsn.carton = :carton", new String[] {
								"locationId", "itemKeyId", "carton" },
						new Object[] { locationId, task.getItemKey().getId(),
								newCarton });

		if (DoubleUtils.compareByPrecision(cartonQuantityBU, quantityBU, task
				.getPackageUnit().getPrecision()) == 0) {
			// 整箱移位
			confirm(taskId, fromLocationId, toLocationId, null, newCarton,
					null, newPallet, newCarton, quantityBU, workerId);
		} else {
			// 拆箱移位
			confirm(taskId, fromLocationId, toLocationId, null, newCarton,
					null, newPallet, BaseStatus.NULLVALUE, quantityBU, workerId);
		}
	}

	public void confirmBySerialNo(Long taskId, Long fromLocationId,
			Long toLocationId, String newPallet, String newCarton,
			Double quantityBU, Long workerId) {
		WmsTask task = load(WmsTask.class, taskId);
		newPallet = WmsStringUtils.isEmpty(newPallet) ? BaseStatus.NULLVALUE
				: newPallet;
		newCarton = WmsStringUtils.isEmpty(newCarton) ? BaseStatus.NULLVALUE
				: newCarton;

		String serialNo = task.getMoveDocDetail().getSerialNo();
		serialNo = WmsStringUtils.isEmpty(serialNo) ? BaseStatus.NULLVALUE
				: serialNo;
		confirm(taskId, fromLocationId, toLocationId, null, null, serialNo,
				newPallet, newCarton, quantityBU, workerId);
	}

	public void confirmByItem(Long taskId, Long fromLocationId,
			Long toLocationId, Double quantityBU, Long workerId) {
		confirm(taskId, fromLocationId, toLocationId, null, null, null, null,
				null, quantityBU, workerId);
	}

	public void confirm(Long taskId, Long fromLocationId, Long toLocationId,
			String pallet, String carton, String serialNo, String newPallet,
			String newCarton, Double quantityBU, Long workerId) {
		WmsTask task = commonDao.load(WmsTask.class, taskId);
		boolean changeFromLocation = false;
		boolean changeToLocation = false;
		if (toLocationId == null || toLocationId == 0) {
			toLocationId = task.getToLocationId();
			changeToLocation = false;
		} else {
			changeToLocation = true;
		}
		if (fromLocationId == null || fromLocationId == 0) {
			fromLocationId = task.getFromLocationId();
			changeFromLocation = false;
		} else {
			changeFromLocation = true;
		}
		WmsWorkDoc workDoc = task.getWorkDoc();
		WmsLocation srcLocation = commonDao.load(WmsLocation.class,
				fromLocationId);
		WmsLocation dstLocation = commonDao.load(WmsLocation.class,
				toLocationId);

		WmsWorker worker = workerId == null ? null : commonDao.load(
				WmsWorker.class, workerId);

		Double unallocateQty = quantityBU;
		if (task.getUnmovedQuantityBU().doubleValue() < quantityBU
				.doubleValue()) {
			unallocateQty = task.getUnmovedQuantityBU();
		}
		WmsInventory srcwi = commonDao.load(WmsInventory.class,
				task.getSrcInventoryId());
		WmsInventory dstwi = commonDao.load(WmsInventory.class,
				task.getDescInventoryId());
		boolean isMatchLoc = true;
		boolean verifyLocation = false;
		if (unallocateQty.doubleValue() > 0) {
			// 原源库存取消拣货分配数量
			srcwi.unallocatePickup(unallocateQty);
			// 原目标库位取消上架分配数量
			dstwi.unallocatePutaway(unallocateQty);
			//
			if (task.bePutType()) {
				if (!toLocationId.equals(task.getToLocationId())) {
					isMatchLoc = false;
					verifyLocation = true;
					if (pallet != null) {
						WmsLocation location = load(WmsLocation.class,dstwi.getLocation()
										.getId());
						location.removePallet(1);
					}
					wmsInventoryManager.refreshLocationUseRate(dstwi.getLocation(), 0);
				}
			} else {
				if (!fromLocationId.equals(task.getFromLocationId())) {
					isMatchLoc = false;
					wmsInventoryManager.refreshLocationUseRate(srcwi.getLocation(), 0);
				}
			}

		}

		// 序列号处理
		// ---按托盘、箱号或序列号作业时，忽略序列号实际关联库存，直接按号码去查库存，查到以后直接进行处理
		// ---按货品作业时，必须要跟关联库存保持一致（要根据实际拣货库位进行库存移位）
		List<WmsInventoryExtend> inventoryExtends = new ArrayList<WmsInventoryExtend>();
		if (task.bePutType()) {
			inventoryExtends = getWmsInventoryExtendByTask(task);
		} else {
			if (changeFromLocation) {
				inventoryExtends = getWmsInventoryExtends(task, fromLocationId);
			} else {
				inventoryExtends = getWmsInventoryExtendByTask(task);
			}
		}

		String inventoryStatus = srcwi.getStatus();
		if (task.getMoveDocDetail().getMoveDoc().isProcessType()) {
			inventoryStatus = dstwi.getStatus();
		}
		String realPallet = newPallet;
		String realCarton = newCarton;
		for (WmsInventoryExtend wsn : inventoryExtends) {
			Double moveQty = 0D;
			if (wsn.getQuantityBU() >= quantityBU) {
				moveQty = quantityBU;
			} else {
				moveQty = wsn.getQuantityBU();
			}
			quantityBU -= moveQty;
			
			
			if(realPallet == null && StringUtils.isNotBlank(wsn.getPallet())  && !BaseStatus.NULLVALUE.equals(wsn.getPallet())){
				String hql = "SELECT SUM(wsn.quantityBU) FROM WmsInventoryExtend wsn WHERE wsn.quantityBU > 0 AND wsn.locationId = :locationId and wsn.pallet = :pallet";
				Double sumQuantity = (Double)commonDao.findByQueryUniqueResult(hql, new String[]{"locationId","pallet"}, new Object[]{wsn.getInventory().getLocation().getId(),wsn.getPallet()});
				if(DoubleUtils.compareByPrecision(sumQuantity, moveQty, srcwi.getPackageUnit().getPrecision()) == 0){
					realPallet = wsn.getPallet();
				}
			}
			if(realCarton == null && StringUtils.isNotBlank(wsn.getCarton())  && !BaseStatus.NULLVALUE.equals(wsn.getCarton())){
				String hql = "SELECT SUM(wsn.quantityBU) FROM WmsInventoryExtend wsn WHERE wsn.quantityBU > 0 AND wsn.locationId = :locationId and wsn.carton = :carton";
				Double sumQuantity = (Double)commonDao.findByQueryUniqueResult(hql, new String[]{"locationId","pallet"}, new Object[]{wsn.getInventory().getLocation().getId(),wsn.getCarton()});
				if(DoubleUtils.compareByPrecision(sumQuantity, moveQty, srcwi.getPackageUnit().getPrecision()) == 0){
					realCarton = wsn.getCarton();
				}
				
			}
			
			// 移位确认
			WmsInventory dstInv = wmsInventoryManager.moveInventory(verifyLocation,isMatchLoc,
					wsn, dstLocation, realPallet, realCarton,
					task.getPackageUnit(), moveQty, inventoryStatus,
					workDoc.getType(), "作业确认：" + realPallet + "-" + realCarton
							+ "-" + wsn.getSerialNo());
			// 更新task数量
			task.addMovedQuantityBU(moveQty);
			// 作业日志
			WmsTaskLog taskLog = addTaskLog(task, srcLocation.getId(),
					srcLocation.getCode(), wsn.getPallet(), wsn.getCarton(),
					wsn.getSerialNo(), dstLocation.getId(),
					dstLocation.getCode(), realPallet, realCarton,
					task.getPackageUnit(), moveQty, srcwi.getId(),
					dstInv.getId(), worker == null ? null : worker);
			// 更新单头和明细的数量
			updateTaskSource(task, taskLog, moveQty);
			// 更新作业单数量
			workDoc.addMovedQuantityBU(moveQty);

			if (quantityBU <= 0D) {
				break;
			}
		}

		// 超库存数量移位---产生盘点库存
		if (quantityBU > 0) {
			// if (!WmsStringUtils.isEmpty(serialNo) ||
			// !WmsStringUtils.isEmpty(carton) ||
			// !WmsStringUtils.isEmpty(pallet)) {
			// //按托盘、箱号或序列号移位时不能超数量移位
			// throw new OriginalBusinessException("按序列号作业时不能超库存数量移位");
			// }

			// 对未扣减到的数量在库位上虚增库存
			WmsInventory newInv = wmsInventoryManager.getInventoryWithNew(
					srcLocation, srcwi.getItemKey(), srcwi.getPackageUnit(),
					srcwi.getStatus());
			newInv.addQuantityBU(quantityBU);
			WmsInventoryExtend nwsn = inventoryExtendManager
					.addInventoryExtend(newInv,
							pallet == null ? BaseStatus.NULLVALUE : pallet,
							carton == null ? BaseStatus.NULLVALUE : carton,
							serialNo == null ? BaseStatus.NULLVALUE : serialNo, quantityBU);
			wmsInventoryManager.addInventoryLog(
					WmsInventoryLogType.COUNT_ADJUST, 1, null, null,
					newInv.getLocation(), newInv.getItemKey(), quantityBU,
					newInv.getPackageUnit(), newInv.getStatus(),
					"作业确认---虚增拣货库位库存");
			// 对虚增库存在盘点库位记录差异库存
			WmsLocation countLoc = wmsInventoryManager
					.getCountLocationByWarehouse(srcLocation.getWarehouse());
			if (countLoc == null) {
				throw new OriginalBusinessException("找不到盘点库位！");
			}
			WmsInventory countInv = wmsInventoryManager.getInventoryWithNew(
					countLoc, newInv.getItemKey(), newInv.getPackageUnit(),
					newInv.getStatus());
			countInv.addQuantityBU(-quantityBU);
			inventoryExtendManager.addInventoryExtend(countInv, nwsn,
					-quantityBU);
			wmsInventoryManager.addInventoryLog(WmsInventoryLogType.MOVE, 1,
					null, null, countInv.getLocation(), countInv.getItemKey(),
					-quantityBU, countInv.getPackageUnit(),
					countInv.getStatus(), "作业确认---虚增库存自动差异调整");

			// 对虚增库存做移位确认
			WmsInventory dstInv = wmsInventoryManager.moveInventory(isMatchLoc,
					nwsn, dstLocation, realPallet, realCarton,
					task.getPackageUnit(), quantityBU, inventoryStatus,
					WmsInventoryLogType.MOVE, "作业确认：" + pallet + "-" + carton
							+ "-" + serialNo);

			// 更新task数量
			task.addMovedQuantityBU(quantityBU);
			// 作业日志
			WmsTaskLog taskLog = addTaskLog(task, srcLocation.getId(),
					srcLocation.getCode(), nwsn.getPallet(), nwsn.getCarton(),
					nwsn.getSerialNo(), dstLocation.getId(),
					dstLocation.getCode(), realPallet, realCarton,
					task.getPackageUnit(), quantityBU, newInv.getId(),
					dstInv.getId(), worker == null ? null : worker);
			// 更新单头和明细的数量
			updateTaskSource(task, taskLog, quantityBU);
			// 更新作业单数量
			workDoc.addMovedQuantityBU(quantityBU);

			// TODO:同时发起对原计划拣货库位的盘点计划
		}

		workflowManager.doWorkflow(task, "taskProcess.confirm");
		workflowManager.doWorkflow(workDoc, "workDocProcess.confirm");
	}

	/**
	 * 根据任务类型更新对应移位单的数量和状态
	 * 
	 * @param task
	 * @param taskLog
	 */
	private void updateTaskSource(WmsTask task, WmsTaskLog taskLog,
			Double quantityBU) {
		taskMove(taskLog, quantityBU);
	}

	/**
	 * 更新移位单头和明细数量及状态
	 * 
	 * @param task
	 */
	private void taskMove(WmsTaskLog taskLog, Double quantityBU) {
		WmsMoveDocDetail mdd = taskLog.getTask().getMoveDocDetail();
		mdd.move(quantityBU);
		if(mdd.getMoveDoc().getType().equals(WmsMoveDocType.MV_PROCESS_PICKING)) {
			// TODO 0727
			Double totalQuantity = (Double)commonDao.findByQueryUniqueResult("Select sum(planQuantityBU) From WmsMoveDocDetail wmdd Where wmdd.moveDoc.id=:moveDocId",
					new String[] { "moveDocId" },new Object[] { mdd.getMoveDoc().getId() });
			Double movedPack = mdd.getMoveDoc().getMovedQuantityBU();
			Double pack = mdd.getMoveDoc().getPlanQuantityBU() * (quantityBU / totalQuantity);
			movedPack = DoubleUtils.format3Fraction(movedPack+pack);
			if(Math.abs(mdd.getMoveDoc().getPlanQuantityBU() - movedPack) < 0.01) {
				movedPack = DoubleUtils.format2Fraction(movedPack);
			}
			mdd.getMoveDoc().setMovedQuantityBU(movedPack);
		} 
		workflowManager.doWorkflow(mdd.getMoveDoc(),
				"wmsMoveDocProcess.confirm");

		if (mdd.getMoveDoc().isPutawayType()) {
			// 更新收货单上架数量 上架状态
			updateWmsAsnMovedQuantity(mdd, quantityBU);
		} else if (mdd.getMoveDoc().isPickTicketType()) {
			updatePickTicketMovedQuantity(mdd, quantityBU);
		} else if (mdd.getMoveDoc().isWaveType()) {
			if (WmsWaveDocWorkMode.WORK_BY_DOC.equals(mdd.getMoveDoc()
					.getWaveDoc().getWorkMode())) {
				updateWaveDocPickTicketModelMovedQuantity(mdd, quantityBU);
			} else {
				// 批拣时分单回写 分单数量
				seprateQuantityByBatch(mdd, quantityBU);
			}
			updateWaveDocPickingStatus(mdd.getMoveDoc());
		}
	}

	// 分单 数量
	private void seprateQuantityByBatch(WmsMoveDocDetail moveDocDetail,
			double toMovedQuantityBU) {
		List<Map<String, Object>> docDetailList = new ArrayList<Map<String, Object>>();
		for (WmsWaveDocDetail waveDocDetail : moveDocDetail.getWaveDocDetails()) {
			if (waveDocDetail.getUnlocatedQuantity() <= 0) {
				continue;
			}
			Map<String, Object> docDetail = new HashMap<String, Object>();
			docDetail.put("波次明细序号", waveDocDetail.getId());
			docDetail.put("计划数量", waveDocDetail.getUnlocatedQuantity());
			WmsPickTicketDetail pickTicketDetail = load(
					WmsPickTicketDetail.class, waveDocDetail.getMoveDocDetail()
							.getRelatedId());
			docDetail
					.put("优先级", pickTicketDetail.getPickTicket().getPriority());
			docDetailList.add(docDetail);
		}
		Map<String, Object> problem = new HashMap<String, Object>();
		problem.put("待分单数量", toMovedQuantityBU);
		problem.put("待分单集", docDetailList);
		Map<String, Object> aotuResult = ruleManager.execute(moveDocDetail
				.getMoveDoc().getWarehouse().getName(), moveDocDetail.getItem()
				.getCompany().getName(), "波次分单规则", problem);
		List<Map<String, Object>> autoResultList = (List<Map<String, Object>>) aotuResult
				.get("返回列表");
		Double movedQuantityBU = 0D;
		for (Map<String, Object> resultMap : autoResultList) {
			WmsWaveDocDetail waveDocDetail = load(WmsWaveDocDetail.class,
					Long.valueOf(resultMap.get("波次明细序号").toString()));
			movedQuantityBU = Double.valueOf(resultMap.get("分单数量").toString());
			waveDocDetail.allocate(movedQuantityBU);
			waveDocDetail.addMovedQuantity(movedQuantityBU);
			waveDocDetail.seprate(movedQuantityBU);
			commonDao.store(waveDocDetail);

			WmsPickTicketDetail pickTicketDetail = commonDao.load(
					WmsPickTicketDetail.class, waveDocDetail.getMoveDocDetail()
							.getRelatedId());
			pickTicketDetail.allocate(movedQuantityBU);
			pickTicketDetail.addPickedQuantityBU(movedQuantityBU);
			commonDao.store(pickTicketDetail);
		}
	}

	public void updateWaveDocPickingStatus(WmsMoveDoc moveDoc) {
		String hql = "SELECT DISTINCT wwdd.moveDocDetail.moveDoc.id FROM WmsWaveDocDetail wwdd "
				+ "WHERE wwdd.moveDocDetail.moveDoc.movedQuantityBU > 0 "
				+ "AND wwdd.moveDocDetail.moveDoc.status != :status AND wwdd.waveDoc.id = :waveDocId";
		List<Long> pickingIds = commonDao.findByQuery(hql, new String[] {
				"waveDocId", "status" }, new Object[] {
				moveDoc.getWaveDoc().getId(), WmsMoveDocStatus.FINISHED });
		for (Long id : pickingIds) {
			WmsMoveDoc picking = load(WmsMoveDoc.class, id);
			if ("Y".equals(picking.isFinish())) {
				picking.setStatus(WmsMoveDocStatus.FINISHED);
				WmsBOLStateLog log = EntityFactory.getEntity(WmsBOLStateLog.class);
				log.setType("拣选完成");
				log.setInputTime(new Date());
				log.setMoveDoc(picking);
				commonDao.store(log);
			} else {
				picking.setStatus(WmsMoveDocStatus.WORKING);
			}
			commonDao.store(picking);
		}
		workflowManager.doWorkflow(moveDoc.getWaveDoc(),
				"wmsWaveDocProcess.confirm");
	}

	private void updateWaveDocPickTicketModelMovedQuantity(
			WmsMoveDocDetail mdd, Double movedQuantityBU) {
		WmsWaveDocDetail waveDocDetail = commonDao.load(WmsWaveDocDetail.class,
				mdd.getRelatedId());
		waveDocDetail.addMovedQuantity(movedQuantityBU);
		waveDocDetail.seprate(movedQuantityBU);
		commonDao.store(waveDocDetail);

		WmsPickTicketDetail pickTicketDetail = commonDao.load(
				WmsPickTicketDetail.class, waveDocDetail.getMoveDocDetail()
						.getRelatedId());
		pickTicketDetail.addPickedQuantityBU(movedQuantityBU);
		commonDao.store(pickTicketDetail);
	}

	public void updatePickTicketMovedQuantity(WmsMoveDocDetail mdd,
			Double movedQuantityBU) {
		if(mdd.getRelatedId()!=null && mdd.getRelatedId()!=0){
			WmsPickTicketDetail pickTicketDetail = commonDao.load(
					WmsPickTicketDetail.class, mdd.getRelatedId());
			pickTicketDetail.addPickedQuantityBU(movedQuantityBU);
			commonDao.store(pickTicketDetail);
			//回写发运计划
			List<WmsShipLot> lots = commonDao.findByQuery("FROM WmsShipLot lot WHERE lot.pickTicket.id =:pickTicket" +
					" AND lot.itemCode =:itemCode AND lot.expectedQuantityBU>lot.pickedQuantityBU ORDER BY lot.batch", 
					new String[]{"pickTicket","itemCode"}, 
					new Object[]{pickTicketDetail.getPickTicket().getId(),pickTicketDetail.getItem().getCode()});
			if(lots!=null && lots.size()>0){
				Double quantity =0D,availableQuantityBU = 0D,picQty = 0D;
				for(WmsShipLot sl : lots){
					quantity = sl.getUnPicQuantityBU();
					picQty = quantity>=movedQuantityBU?movedQuantityBU:quantity;
					sl.setPickedQuantityBU(picQty);
					commonDao.store(sl);
					availableQuantityBU -= picQty;
					if(availableQuantityBU<=0){
						break;
					}
				}
			}
		}
	}

	/**
	 * 更新移位单头和明细数量及状态
	 * 
	 * @param task
	 */
	private void updateWmsAsnMovedQuantity(WmsMoveDocDetail mdd,
			Double movedQuantityBU) {
		if (movedQuantityBU <= 0D) {
			return;
		}
		String hql = "from WmsReceivedRecord rr where rr.itemKey.id = :itemKeyId AND rr.asn.id = :asnId AND rr.receivedQuantityBU > rr.movedQuantity";
		List<WmsReceivedRecord> wmsReceivedRecords = this.commonDao
				.findByQuery(hql, new String[] { "itemKeyId", "asnId" },
						new Object[] { mdd.getItemKey().getId(),
								mdd.getMoveDoc().getAsn().getId() });
		WmsASN asn = null;
		for (WmsReceivedRecord wmsReceivedRecord : wmsReceivedRecords) {
			double moveQty = movedQuantityBU;
			if (movedQuantityBU > wmsReceivedRecord.getReceivedQuantityBU()
					- wmsReceivedRecord.getMovedQuantity()) {
				moveQty = wmsReceivedRecord.getReceivedQuantityBU()
						- wmsReceivedRecord.getMovedQuantity();
			}
			wmsReceivedRecord.addMovedQuantity(moveQty);
			movedQuantityBU -= moveQty;
			if (asn == null) {
				asn = this.commonDao.load(WmsASN.class, wmsReceivedRecord
						.getAsnDetail().getAsn().getId());
			}
			if (movedQuantityBU == 0) {
				break;
			}
		}
		if (asn != null
				&& WmsASNShelvesStauts.PUTAWAY.equals(asn.getShelvesStauts())) {
			if (asn.getMovedQuantityBU().doubleValue() >= asn
					.getExpectedQuantityBU().doubleValue()
					&& asn.getMovedQuantityBU().doubleValue() >= asn
							.getReceivedQuantityBU().doubleValue()) {
				asn.setEndReceivedDate(new Date());
			}
			workflowManager.doWorkflow(asn, "wmsASNPutawayProcess.moveConfirm");
		}
	}

	public void taskPick(WmsTaskLog taskLog, Double quantityBU) {
		// WmsPickTicketDetail ptd = taskLog.getTask().getPickTicketDetail();
		//
		// ptd.addPickedQuantityBU(quantityBU);
		//
		// workflowManager.doWorkflow(ptd.getPickTicket(),
		// "wmsPickTicketPickProcess.pickup");
	}

	/**
	 * 更新移位单头和明细数量及状态
	 * 
	 * @param task
	 */
	public void taskProcess(WmsTaskLog taskLog, Double quantityBU) {
		// WmsProcessDocDetail processDocDetail =
		// taskLog.getTask().getProcessDocDetail();
		//
		// processDocDetail.pickQuantityBU(quantityBU);
		//
		// workflowManager.doWorkflow(processDocDetail.getProcessDoc(),
		// "wmsProcessDocProcess.pickConfirm");
	}

	/**
	 * 更新移位单头和明细数量及状态
	 * 
	 * @param task
	 */
	public void taskWave(WmsTaskLog taskLog, Double quantityBU) {
		// WmsWaveDocDetail waveDocDetail =
		// taskLog.getTask().getWaveDocDetail();
		//
		// waveDocDetail.addPickedQuantityBU(quantityBU);
		// workflowManager.doWorkflow(waveDocDetail.getWaveDoc(),
		// "wmsWaveDocPickProcess.pickup");
		// if ("PICKUP".equals(waveDocDetail.getWaveDoc().getPickStatus())) {
		// autoSeprate(waveDocDetail.getWaveDoc());
		// }
	}

	public void autoSeprate(WmsWaveDoc waveDoc) {
		String hql = "FROM WmsPickTicket pickTicket WHERE pickTicket.waveDoc.id = :waveDocId";
		List<WmsPickTicket> pickTickets = (List<WmsPickTicket>) commonDao
				.findByQuery(hql, new String[] { "waveDocId" },
						new Object[] { waveDoc.getId() });
		for (WmsPickTicket pickTicket : pickTickets) {
			// 分单时对发货单分配
			autoAllocate(pickTicket, waveDoc);
			// 改变发货单状态
			workflowManager.doWorkflow(pickTicket,
					"wmsPickTicketBaseProcess.seprate");
		}
		workflowManager.doWorkflow(waveDoc, "wmsWaveDocPickProcess.seprate");
	}

	/**
	 * 创建TaskLog
	 */
	private WmsTaskLog addTaskLog(WmsTask task, Long fromLocationId,
			String fromLocationCode, String fromPallet, String fromCarton,
			String fromSerialNo, Long toLocationId, String toLocationCode,
			String toPallet, String toCarton, WmsPackageUnit packageUnit,
			Double movedQuantityBU, Long fromInventoryId, Long toInventoryId,
			WmsWorker worker) {
		WmsTaskLog taskLog = EntityFactory.getEntity(WmsTaskLog.class);

		taskLog.setTask(task);
		taskLog.setFromLocationId(fromLocationId);
		taskLog.setFromLocationCode(fromLocationCode);
		taskLog.setFromPallet(fromPallet);
		taskLog.setFromCarton(fromCarton);
		taskLog.setFromSerialNo(fromSerialNo);
		taskLog.setToLocationId(toLocationId);
		taskLog.setToLocationCode(toLocationCode);
		taskLog.setToPallet(toPallet);
		taskLog.setToCarton(toCarton);
		taskLog.setItemKey(task.getItemKey());
		taskLog.setMovedQuantityBU(movedQuantityBU);
		taskLog.setMovedQuantity(PackageUtils.convertPackQuantity(
				movedQuantityBU, packageUnit));
		taskLog.setPackageUnit(packageUnit);
		taskLog.setInventoryStatus(task.getInventoryStatus());
		taskLog.setSrcInventoryId(fromInventoryId);
		taskLog.setDescInventoryId(toInventoryId);
		taskLog.setWorker(worker);
		taskLog.refreshBoxQty();

		commonDao.store(taskLog);

		// 计件工资计费（上架费&拣货费）
		createTaskCharges(taskLog);

		return taskLog;
	}

	public void createTaskCharges(WmsTaskLog taskLog) {
		try {
			workflowManager.sendMessage(taskLog, "wmsTaskLog.charges");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("计费失败！");
		}
	}

	private String removeHqlWithNullParam(String hql, Map params) {
		String _hql = StringUtils.substringBetween(hql, "/~", "~/");
		while (StringUtils.isNotEmpty(_hql)) {
			String param = StringUtils.substring(_hql, 0, _hql.indexOf(":"))
					.trim();
			Object o = params.get(param);
			if (o == null || "".equals(o)) {
				params.remove(param);
				hql = StringUtils.remove(hql, "/~" + _hql + "~/");
			} else if (o instanceof Collection) {
				if (((Collection) o).isEmpty() || ((Collection) o).size() == 0) {
					params.remove(param);
					hql = StringUtils.remove(hql, "/~" + _hql + "~/");
				}
			}
			hql = StringUtils.replace(hql, "/~" + _hql + "~/",
					StringUtils.substring(_hql, _hql.indexOf(":") + 1));
			_hql = StringUtils.substringBetween(hql, "/~", "~/");
		}
		return hql;
	}
	public void uplineRuleTable(final Version version) throws HttpException, IOException{
		
		VersionManager v = (VersionManager) applicationContext.getBean("aopVersionManager");
		v.onLine(version);
		
		final String rfurl = GlobalParamUtils.getGloableStringValue("rule_rf");
		if(StringUtils.isEmpty(rfurl)){
			System.out.println("rule_rf is null");
			return;
		}
		new Thread(new Runnable(){
			public void run(){
				HttpClient client = new HttpClient();
				PostMethod postMethod = new PostMethod(rfurl);//http://localhost:8088/jac_fdj_wms/rf
				//设置参数
				try {
					postMethod.setParameter("methodName", URLEncoder.encode("RuleTable.onLine","utf-8"));
					postMethod.setParameter("value", URLEncoder.encode(version.getId().toString(),"utf-8"));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				//编码设置
				postMethod.getParams().setHttpElementCharset("utf-8");
				postMethod.getParams().setContentCharset("utf-8");
				try {
					client.executeMethod(postMethod);
				} catch (HttpException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	public void uplineRuleTableRf(Long versionId){
		Version version = commonDao.load(Version.class, versionId);
		VersionManager v = (VersionManager) applicationContext.getBean("aopVersionManager");
		v.onLine(version);
		System.out.println("uplineRuleTableRf.........");
	}
	public void saveQualityMoveSoiLog(Object[] obj){
		WmsQualityMoveSoiLog w = EntityFactory.getEntity(WmsQualityMoveSoiLog.class);
		w.setExtendPropc1(obj[0]==null?"":obj[0].toString());
		w.setInventoryStatus(obj[1].toString());
		w.setItemCode(obj[2].toString());
		w.setItemName(obj[3].toString());
		w.setQuantity(Double.valueOf(obj[4].toString()));
		w.setStorageDate((Date)obj[5]);
		w.setSupCode(obj[6].toString());
		w.setSupName(obj[7].toString());
		w.setType(obj[8].toString());
		w.setWarehouse(commonDao.load(WmsWarehouse.class, Long.parseLong(obj[9].toString())));
		w.setDescription(obj[10].toString());
		w.setQualityStatus(obj[11].toString());
		w.setLocation(obj[12].toString());
		w.setQualityCode(obj[13].toString());//送检单号
		commonDao.store(w);
	}
}