package com.vtradex.wms.server.service.shipping.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.vtradex.engine.utils.DateUtils;
import com.vtradex.sequence.service.sequence.SequenceGenerater;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.exception.OriginalBusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.client.ui.constant.CT_PA;
import com.vtradex.wms.client.ui.javabean.PT_ALLOCATED;
import com.vtradex.wms.client.ui.javabean.PT_AVAILABLE;
import com.vtradex.wms.client.ui.javabean.PT_DETAILS;
import com.vtradex.wms.client.ui.javabean.PT_INFO;
import com.vtradex.wms.server.action.PickTicketBaseShipDecision;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.ShipLotInfo;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.inventory.WmsInventoryLogType;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.model.move.WmsMoveDocType;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.move.WmsTaskLog;
import com.vtradex.wms.server.model.move.WmsWorkDoc;
import com.vtradex.wms.server.model.organization.TypeOfBill;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsItemState;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.shipping.WmsBOLStateLog;
import com.vtradex.wms.server.model.shipping.WmsPickTicket;
import com.vtradex.wms.server.model.shipping.WmsPickTicketDetail;
import com.vtradex.wms.server.model.shipping.WmsPickTicketStatus;
import com.vtradex.wms.server.model.shipping.WmsWaveDocDetail;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
import com.vtradex.wms.server.service.base.WmsBillTypeManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.receiving.WmsMoveDocManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.rule.WmsTransactionalManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.shipping.WmsPickTicketManager;
import com.vtradex.wms.server.service.workDoc.WmsWorkDocManager;
import com.vtradex.wms.server.utils.NewLotInfoParser;
import com.vtradex.wms.server.utils.PackageUtils;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

/**
 * 发货单管理 
 *
 * @category Manager
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.10 $Date: 2016/06/14 03:05:34 $
 */
@SuppressWarnings("unchecked")
public class DefaultWmsPickTicketManager extends DefaultBaseManager implements WmsPickTicketManager {
	
	protected WorkflowManager workflowManager;
	protected WmsInventoryManager inventoryManager;
	protected WmsBussinessCodeManager codeManager;
	protected WmsRuleManager wmsRuleManager;
	protected WmsWorkDocManager wmsWorkDocManager;
	protected WmsTransactionalManager wmsTransactionalManager;
	protected WmsMoveDocManager moveDocManager;
	protected WmsBillTypeManager wmsBillTypeManager;

	public DefaultWmsPickTicketManager(WorkflowManager workflowManager, WmsInventoryManager inventoryManager,
			WmsBussinessCodeManager codeManager,WmsRuleManager wmsRuleManager ,WmsWorkDocManager wmsWorkDocManager
			,WmsTransactionalManager wmsTransactionalManager,WmsMoveDocManager moveDocManager,WmsBillTypeManager wmsBillTypeManager) {
		this.workflowManager = workflowManager;
		this.inventoryManager = inventoryManager;
		this.codeManager =  codeManager;
		this.wmsRuleManager =  wmsRuleManager;
		this.wmsWorkDocManager =  wmsWorkDocManager;
		this.wmsTransactionalManager =  wmsTransactionalManager;
		this.moveDocManager  = moveDocManager;
		this.wmsBillTypeManager = wmsBillTypeManager;
	}
	
	public void storePickTicket(WmsPickTicket pickTicket) {
		try {
			pickTicket.setCompany(pickTicket.getBillType().getCompany());
			pickTicket.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
			if (pickTicket.isNew()) {
				String code = codeManager.generateCodeByRule(pickTicket.getWarehouse(), pickTicket.getCompany().getName(), "发货单", pickTicket.getBillType().getName());
				pickTicket.setCode(code);
			}
			if("NORMOL_PICKING".equals(pickTicket.getBillType().getCode())){
				if(null != pickTicket.getIntendShipDate() && pickTicket.getIntendShipDate().before(new Date())){
					throw new BusinessException("不能早于当前日期");
				}
			}
			if(!pickTicket.getBillType().getCompany().getId().equals(pickTicket.getCompany().getId())){
				throw new BusinessException("单据类型与货主不匹配");
			}
			workflowManager.doWorkflow(pickTicket,"wmsPickTicketBaseProcess.new");
		} catch (BusinessException be) {
			logger.error("store pickticket error.", be);
			throw new BusinessException(be.getLocalizedMessage());
		}
	}

	public void addPickTicketDetail(Long pickTicketId,WmsPickTicketDetail pickTicketDetail
			,Long status) {
		try {
			WmsPackageUnit packageUnit = commonDao.load(WmsPackageUnit.class, pickTicketDetail.getPackageUnit().getId());
			WmsPickTicket pickTicket = commonDao.load(WmsPickTicket.class, pickTicketId);
			WmsItemState itemStatus = commonDao.load(WmsItemState.class, status);
			WmsOrganization supplier = commonDao.load(WmsOrganization.class, pickTicketDetail.getSupplier().getId());
			
			if (pickTicketDetail.getExpectedQuantity().doubleValue() <= 0) {
				throw new BusinessException("add.pickticket.detail.expected.quantity.error");
			}
			if (pickTicketDetail.getShipLotInfo() != null) {
				pickTicketDetail.getShipLotInfo().setSupplier(supplier.getCode());//yc.min
				pickTicketDetail.getShipLotInfo().setExtendPropC20(supplier.getName());
			}
			if (WmsPickTicketStatus.OPEN.equals(pickTicket.getStatus())) {
				// 新增明细
				if(itemStatus!=null) {
					pickTicketDetail.setInventoryStatus(itemStatus.getName());
				} else {
					pickTicketDetail.setInventoryStatus(BaseStatus.NULLVALUE);
				}
				if (pickTicketDetail.isNew()) {
					pickTicketDetail.setExpectedQuantityBU(pickTicketDetail.getExpectedQuantity() * packageUnit.getConvertFigure());
					pickTicketDetail.setAllocatedQuantityBU(0.0);
					pickTicketDetail.setPickedQuantityBU(0.0);
					pickTicketDetail.setShippedQuantityBU(0.0);
					pickTicket.addPickTicketDetail(pickTicketDetail);
				} else {
					pickTicketDetail.setExpectedQuantityBU(pickTicketDetail.getExpectedQuantity() * packageUnit.getConvertFigure());
					pickTicket.refreshPickTicketQty();
				}
			}
		} catch (BusinessException be) {
			logger.error("store pickticket detail error.", be);
			throw new BusinessException("store.pickticket.detail.error");
		}
	}
	
	public void removePickTicketDetail(WmsPickTicketDetail pickTicketDetail) {
		WmsPickTicket pickTicket = load(WmsPickTicket.class,pickTicketDetail.getPickTicket().getId());
		pickTicket.removeDetails(pickTicketDetail);
		pickTicket.refreshPickTicketQty();
	}
	
	public void activeWmsPickTicket(WmsPickTicket pickTicket){
		//发货单自动分派
		//wmsMessageManager.subscriberPickTicketApportion
		//创建发货拣货单
		//wmsMessageManager.subscriberCreateWmsMoveDoc
		
		Set<WmsPickTicketDetail> details = pickTicket.getDetails();
		for(WmsPickTicketDetail ptd : details){
			if(null == ptd.getSupplier()){//激活供应商不能为空
				throw new BusinessException("供应商不能为空,请先维护再激活!!");
			}
			ShipLotInfo shipLotInfo = setExtendPropC1(ptd);
			if(StringUtils.isBlank(shipLotInfo.getExtendPropC1())){
				throw new BusinessException("工艺状态为空,请维护");
			}
		}
		moveDocManager.createWmsMoveDoc(pickTicket);
		pickTicket.setStatus(WmsPickTicketStatus.WORKING);
		commonDao.store(pickTicket);
		
		//以下代码无用
		/*Boolean beCreatMove = pickTicket.getBillType().getCode().equals("OUT-004");
		if(!beCreatMove){
			Set<WmsPickTicketDetail> details = pickTicket.getDetails();
			for(WmsPickTicketDetail ptd : details){
				ShipLotInfo shipLotInfo = setExtendPropC1(ptd);
				if(StringUtils.isBlank(shipLotInfo.getExtendPropC1())){
					throw new BusinessException("工艺状态为空,请维护");
				}
			}
			moveDocManager.createWmsMoveDoc(pickTicket);
			pickTicket.setStatus(WmsPickTicketStatus.WORKING);
			commonDao.store(pickTicket);
		}else{//发货单直接完成
			Set<WmsPickTicketDetail> detailFinshs = pickTicket.getDetails();
			for(WmsPickTicketDetail ptd : detailFinshs){
				ptd.ship(ptd.getExpectedQuantityBU());
			}
			PickTicketBaseShipDecision p = new PickTicketBaseShipDecision();
			String status = p.processAction(pickTicket);
			pickTicket.setStatus(status);
			commonDao.store(pickTicket);
		}*/
	}
	private ShipLotInfo setExtendPropC1(WmsPickTicketDetail ptd){
		ShipLotInfo shipLotInfo = ptd.getShipLotInfo();
		if(shipLotInfo==null){
			shipLotInfo = new ShipLotInfo();
			//当工艺状态为空时,发货单激活要提示选择工艺状态
			if(ptd.getPickTicket().getCompany().getName().equals("FDJ")){
				shipLotInfo.setExtendPropC1(ptd.getDescription()==null?BaseStatus.NULLVALUE:null);
			}else{
				shipLotInfo.setExtendPropC1(BaseStatus.NULLVALUE);
			}
			shipLotInfo.setSupplier(ptd.getSupplier().getCode());
			ptd.setShipLotInfo(shipLotInfo);
			commonDao.store(ptd);
		}else{
			if(ptd.getPickTicket().getCompany().getName().equals("FDJ")){
				shipLotInfo.setExtendPropC1(ptd.getDescription()==null?BaseStatus.NULLVALUE:null);
			}else{
				shipLotInfo.setExtendPropC1(BaseStatus.NULLVALUE);
			}
		}
		return shipLotInfo;
	}
	public void activePickTicket(Long pickTicketId){
		List<Long> ptds = commonDao.findByQuery("SELECT pp.id FROM WmsPickTicketDetail pp WHERE pp.pickTicket.id =:pickTicketId", 
				new String[]{"pickTicketId"},new Object[]{pickTicketId});
		for(Long id : ptds){
			WmsPickTicketDetail ptd = commonDao.load(WmsPickTicketDetail.class, id);
			setExtendPropC1(ptd);
		}
		WmsPickTicket pickTicket = commonDao.load(WmsPickTicket.class, pickTicketId);
		this.createWmsMoveDocByPickTicket(pickTicket,ptds);
		pickTicket.setStatus(WmsPickTicketStatus.WORKING);
		commonDao.store(pickTicket);
	}
	private void createWmsMoveDocByPickTicket(WmsPickTicket pickTicket,List<Long> ptds)
			throws BusinessException {
		WmsMoveDoc moveDoc = new WmsMoveDoc();
		moveDoc.setWarehouse(pickTicket.getWarehouse());
		moveDoc.setCompany(pickTicket.getCompany());
		moveDoc.setCarrier(pickTicket.getCarrier());
		moveDoc.setDock(pickTicket.getDock());
		moveDoc.setType(WmsMoveDocType.MV_PICKTICKET_PICKING);
		
		WmsBillType billType = wmsBillTypeManager.getWmsBillType(
				moveDoc.getCompany(), TypeOfBill.MOVE, moveDoc.getType());
		moveDoc.setBillType(billType);

		if (moveDoc.isNew()) {
			String code = null;
			try {
				code = codeManager.generateCodeByRule(
						moveDoc.getWarehouse(), moveDoc.getCompany().getName(),
						"拣货单", billType.getName());
			} catch (Exception e) {
				//编码 = 仓库编码 + 编码 + 格式化日期($现在,"yyMMdd")
				code = moveDoc.getWarehouse().getCode()+"ON"+DateUtils.format(new Date(),"yyMMdd");
				SequenceGenerater ss = (SequenceGenerater) applicationContext.getBean("sequenceGenerater");
				code = ss.generateSequence(code, 6);	
			}
			moveDoc.setCode(code);
		}

		moveDoc.setPickTicket(pickTicket);
		moveDoc.setOriginalBillType(pickTicket.getBillType());
		moveDoc.setOriginalBillCode(pickTicket.getCode());
		moveDoc.setDriver(pickTicket.getDriver());
		moveDoc.setVehicleNo(pickTicket.getVehicleNo());
		moveDoc.setIntendShipDate(pickTicket.getIntendShipDate());
		moveDoc.setStatus(WmsMoveDocStatus.OPEN);
		commonDao.store(moveDoc);
		
		WmsBOLStateLog bolStateLog = EntityFactory.getEntity(WmsBOLStateLog.class);
		bolStateLog.setType("订单下达");
		bolStateLog.setInputTime(new Date());
		bolStateLog.setMoveDoc(moveDoc);
		commonDao.store(bolStateLog);
		
		for(Long detailId : ptds){
			WmsPickTicketDetail pickTicketDetail = commonDao.load(WmsPickTicketDetail.class, detailId);
			Double quantityBU = pickTicketDetail.getExpectedQuantityBU();
//			moveDoc.setClassType(classType);
			WmsMoveDocDetail moveDocDetail = new WmsMoveDocDetail();
			moveDocDetail.setMoveDoc(moveDoc);
			moveDocDetail.setRelatedId(pickTicketDetail.getId());
			moveDocDetail.setItem(pickTicketDetail.getItem());
			moveDocDetail.setShipLotInfo(pickTicketDetail.getShipLotInfo());
			moveDocDetail.setInventoryStatus(pickTicketDetail.getInventoryStatus());
			moveDocDetail.setPackageUnit(pickTicketDetail.getPackageUnit());
			moveDocDetail.setPlanQuantityBU(quantityBU);
			moveDocDetail.setPlanQuantity(PackageUtils.convertPackQuantity(quantityBU, moveDocDetail.getPackageUnit()));
			moveDocDetail.calculateLoad();
			moveDoc.addDetail(moveDocDetail);
			commonDao.store(moveDocDetail);
		}
		
	}
	/**
	 * 反激活发货单
	 * @param pickTicket
	 */
	public void unActiveWmsPickTicket(WmsPickTicket pickTicket) {
		Long waveDocDetailCount = (Long)commonDao.findByQueryUniqueResult("SELECT COUNT (*) FROM WmsWaveDocDetail waveDocDetail" +
			" LEFT JOIN waveDocDetail.moveDocDetail.moveDoc.pickTicket pickTicket WHERE pickTicket.id = :pickTicketId",
			"pickTicketId", pickTicket.getId());
		if(waveDocDetailCount.doubleValue() > 0) {
			throw new OriginalBusinessException("发货单对应的拣货单已加入波次单，不能反激活！");
		}
		
		List<String> status = new ArrayList<String>();
		status.add(WmsMoveDocStatus.OPEN);
		String hql = "SELECT COUNT (*) FROM WmsMoveDoc moveDoc WHERE" +
			" moveDoc.pickTicket.id=:pickTicketId AND moveDoc.status NOT IN (:status)";
		Long count = (Long)commonDao.findByQueryUniqueResult(hql, 
			new String[] {"pickTicketId", "status"}, new Object[] {pickTicket.getId(), status});
		if(count.doubleValue() > 0) {
			throw new OriginalBusinessException("发货单对应的拣货单已作业！");
		}
		
		hql = "FROM WmsMoveDoc moveDoc WHERE moveDoc.pickTicket.id=:pickTicketId" +
			" AND moveDoc.status IN (:status)";
		List<WmsMoveDoc> moveDocs = commonDao.findByQuery(hql, 
			new String[] {"pickTicketId", "status"}, new Object[] {pickTicket.getId(), status});
		commonDao.deleteAll(moveDocs);
		
		pickTicket.setCarrier(null);
		pickTicket.setDock(null);
		commonDao.store(pickTicket);
	}
	
	/**
	 * 1.退拣效果：拣货库存移动到发货单发货月台对应的退拣库位，更新发货单及其明细的分配数量和拣货数量；
	 * 2.退拣完成后，同步调用发货单的退拣流程，更新发货单状态；
	 */
	public void pickBack(WmsTaskLog taskLog,List<String> tableValue){
		Double pickBackQuantity = Double.parseDouble(tableValue.get(0).toString());
		if(pickBackQuantity > 0 && pickBackQuantity <= (taskLog.getMovedQuantityBU() - taskLog.getPickBackQuantityBU())){
			this.pickBack(taskLog, pickBackQuantity);
		}else{
			throw new BusinessException("本次退拣数量BU必须为大于0的数字");
		}
	}
	public void pickBack(WmsTaskLog taskLog,Double pickBackQuantity){
		if(pickBackQuantity == 0){
			return;
		}
		if(pickBackQuantity > taskLog.getMovedQuantityBU()){
			throw new BusinessException("pickBackQuantityMoreThanMovedQuantityBU");
		}
		if(taskLog.getTask().getMoveDocDetail().getMoveDoc().getMasterBOL()!=null){
			throw new BusinessException("joinedMastBOL");
		}
		WmsTask wmsTask = this.commonDao.load(WmsTask.class, taskLog.getTask().getId());
		WmsMoveDocDetail moveDocDetail = this.commonDao.load(WmsMoveDocDetail.class, taskLog.getTask().getMoveDocDetail().getId());
		WmsMoveDoc moveDoc = this.commonDao.load(WmsMoveDoc.class, moveDocDetail.getMoveDoc().getId());
		
		if(moveDoc.isPickTicketType()){
			WmsPickTicketDetail pickTicketDetail = this.commonDao.load(WmsPickTicketDetail.class, moveDocDetail.getRelatedId());
			pickTicketDetail.pickBack(pickBackQuantity);
		}
		if(moveDoc.isWaveType()){
			double wavePickBackQuantity  = pickBackQuantity;
			for(WmsWaveDocDetail waveDocDetail : moveDocDetail.getWaveDocDetails()){
				if(waveDocDetail.getPickedQuantityBU() <= 0D){
					continue;
				}
				double quantity = wavePickBackQuantity;
				if(waveDocDetail.getPickedQuantityBU() < quantity){
					quantity = waveDocDetail.getPickedQuantityBU();
				}
				wavePickBackQuantity -= quantity;
				waveDocDetail.pickBack(quantity);
				WmsPickTicketDetail pickTicketDetail = this.commonDao.load(WmsPickTicketDetail.class, waveDocDetail.getMoveDocDetail().getRelatedId());
				pickTicketDetail.pickBack(pickBackQuantity);
				
				WmsMoveDoc originalMoveDoc = load(WmsMoveDoc.class,waveDocDetail.getMoveDocDetail().getMoveDoc().getId());
				workflowManager.doWorkflow(originalMoveDoc, "wmsMoveDocProcess.pickBack");
				if(wavePickBackQuantity <= 0D){
					break;
				}
			}
		}
		
		WmsWorkDoc wmsWorkDoc = this.commonDao.load(WmsWorkDoc.class, taskLog.getTask().getWorkDoc().getId());
		
		//退拣移位单 数量
		taskLog.pickBack(pickBackQuantity);
		WmsLocation dstLoc = this.commonDao.load(WmsLocation.class, taskLog.getToLocationId());
		if(dstLoc.getPickBackLoc()==null){
			throw new BusinessException("picktiketLocationActionPickbackLocationISNULL");
		}
		List<WmsInventoryExtend> wsns = new ArrayList<WmsInventoryExtend>();
		if (taskLog.getFromSerialNo() != null  && !BaseStatus.NULLVALUE.equals(taskLog.getFromSerialNo())) {
			// 按序列号移位
			wsns = commonDao
					.findByQuery(
							"FROM WmsInventoryExtend wsn WHERE wsn.serialNo = :serialNo AND wsn.inventory.id = :inventoryId ",
							new String[] { "serialNo", "inventoryId" },
							new Object[] { taskLog.getFromSerialNo(), taskLog.getDescInventoryId() });
		} else if (taskLog.getToCarton() != null  && !BaseStatus.NULLVALUE.equals(taskLog.getToCarton())) {
			// 按箱移位
			wsns = commonDao
					.findByQuery(
							"FROM WmsInventoryExtend wsn WHERE wsn.carton = :carton AND wsn.inventory.id = :inventoryId ",
							new String[] { "carton", "inventoryId" },
							new Object[] { taskLog.getToCarton(), taskLog.getDescInventoryId() });
		} else if (taskLog.getToPallet() != null   && !BaseStatus.NULLVALUE.equals(taskLog.getToPallet())) {
			// 按托移位
			wsns = commonDao
					.findByQuery(
							"FROM WmsInventoryExtend wsn WHERE wsn.pallet = :pallet AND wsn.inventory.id = :inventoryId ",
							new String[] { "pallet", "inventoryId" },
							new Object[] { taskLog.getToPallet(), taskLog.getDescInventoryId() });
		} else {
				wsns = commonDao
						.findByQuery(
								"FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId AND wsn.inventory.status = :status",
								new String[] { "inventoryId",
										"status" },
								new Object[] { taskLog.getDescInventoryId(),
										taskLog.getInventoryStatus()});
		}
		double quantityBU = pickBackQuantity;
		for (WmsInventoryExtend wsn : wsns) {
			Double moveQty = 0D;
			if (wsn.getQuantityBU() >= pickBackQuantity) {
				moveQty = pickBackQuantity;
			} else {
				moveQty = wsn.getQuantityBU();
			}
			pickBackQuantity -= moveQty;
			
			WmsInventory dstInv = inventoryManager.moveInventory(wsn, dstLoc.getPickBackLoc(), wsn.getPallet(), wsn.getCarton(), taskLog.getPackageUnit(), moveQty, taskLog.getInventoryStatus(), WmsInventoryLogType.MOVE, "退拣移位");
			
			WmsTaskLog pickBackTaskLog =  addTaskLog(taskLog.getTask(), taskLog.getToLocationId(),
					taskLog.getToLocationCode(), taskLog.getFromPallet(), taskLog.getFromCarton(),
					taskLog.getFromCarton(), dstInv.getLocation().getId(),
					dstInv.getLocation().getCode(), taskLog.getToPallet(), taskLog.getToCarton(),
					taskLog.getPackageUnit(), moveQty, taskLog.getDescInventoryId(),
					dstInv.getId(), null);
			pickBackTaskLog.setBePickBack(true);
			if (pickBackQuantity <= 0D) {
				break;
			}
		}
		
		//创建新的拣货移位单
		moveDocManager.createWmsMoveDocByPickBack(taskLog, quantityBU);
		workflowManager.doWorkflow(wmsTask, "taskProcess.pickBack");
		workflowManager.doWorkflow(moveDoc, "wmsMoveDocProcess.pickBack");
		workflowManager.doWorkflow(wmsWorkDoc, "workDocProcess.pickBack");
		if(wmsWorkDoc.getExpectedQuantityBU().doubleValue() <= 0D){
			cancelWorkDocByWmsMoveDoc(wmsWorkDoc);
		}
		if(moveDoc.getPlanQuantityBU().doubleValue() <= 0D){
			commonDao.executeByHql("DELETE WmsBOLStateLog wbd WHERE wbd.moveDoc.id = :moveDocId", "moveDocId", moveDoc.getId());
			commonDao.executeByHql("DELETE WmsBoxDetail wbd WHERE wbd.moveDoc.id = :moveDocId", "moveDocId", moveDoc.getId());
			commonDao.executeByHql("DELETE WmsMoveDoc moveDoc WHERE moveDoc.id = :moveDocId", "moveDocId", moveDoc.getId());
		}
	}
	
	protected void cancelWorkDocByWmsMoveDoc(WmsWorkDoc worDoc) {
		List<WmsTask> tasks = commonDao.findByQuery("FROM WmsTask task WHERE task.workDoc.id = :workDocId","workDocId", worDoc.getId());
		for (WmsTask task : tasks) {
			worDoc.removeTask(task);
		}
		commonDao.delete(worDoc);
	}
	
	
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
		return taskLog;
	}
	// 查询发货单信息
	public Map init_PA_INFO(Map params) {
		Long pickTicketId = (Long) params.get("pickTicketId");

		final String hql = "SELECT new com.vtradex.wms.client.ui.javabean.PT_INFO(pickTicket.id, " +
				"pickTicket.code, " +
				"pickTicket.expectedQuantityBU, " +
				"pickTicket.allocatedQuantityBU, " +
				"pickTicket.pickedQuantityBU) " + 
				"FROM WmsPickTicket pickTicket " + 
				"WHERE pickTicket.id = :pickTicketId";
		
		PT_INFO pt_info = (PT_INFO) commonDao.findByQueryUniqueResult(hql,
				new String[] { "pickTicketId" }, 
				new Object[] { pickTicketId });

		Map<String, PT_INFO> result = new HashMap<String, PT_INFO>();
		
		result.put(CT_PA.PT_RESULT, pt_info);
		
		return result;
	}
	
	// 查询发货单明细信息
	public Map init_PD_INFO(Map params) {
		Long pickTicketId = (Long) params.get("pickTicketId");

		//拣货单明细查询。。。
		final String hql = "SELECT new com.vtradex.wms.client.ui.javabean.PT_DETAILS(p.id, "
				+ "p.item.id, "
				+ "p.packageUnit.id, "
				+ "p.packageUnit.convertFigure, "
				+ "p.item.code, "
				+ "p.item.name, "
				+ "p.packageUnit.unit, "
				+ "p.expectedQuantityBU, "
				+ "p.allocatedQuantityBU, "
				+ "p.pickedQuantityBU, "
				+ "p.shippedQuantityBU, "
				+ "p.shipLotInfo.storageDate, "
				+ "p.shipLotInfo.soi, "
				+ "p.shipLotInfo.batchNum, "
				+ "p.shipLotInfo.produceDate, "
				+ "p.shipLotInfo.expireDate, "
				+ "p.shipLotInfo.warnDate, "
				+ "p.shipLotInfo.supplier) "
				+ "FROM WmsPickTicketDetail p "
				+ "WHERE p.pickTicket.id = :pickTicketId AND p.expectedQuantityBU > p.allocatedQuantityBU "
				+ "AND p.pickTicket.warehouse.id = :warehouseId";
		
		List<PT_DETAILS> pt_details = commonDao.findByQuery(hql, 
				new String[] {"pickTicketId", "warehouseId"}, 
				new Object[] {pickTicketId, WmsWarehouseHolder.getWmsWarehouse().getId()});

		Map<String, List> result = new HashMap<String, List>();
		
		result.put(CT_PA.PD_RESULT, pt_details);
		
		return result;
	}
	
	// 查询发货单已分配信息
	public Map init_ALD_INFO(Map params) {
		Long pickTicketId = (Long) params.get("pickTicketId");

		final String hql = "SELECT new com.vtradex.wms.client.ui.javabean.PT_ALLOCATED(" +
				"task.id," +
				"fromLocation.code," +
				"task.itemKey.item.code," +
				"task.itemKey.item.name," +
				"task.packageUnit.unit," +
				"task.planQuantity," +
				"task.planQuantityBU,0," +
				"task.itemKey.lotInfo.storageDate," +
				"task.itemKey.lotInfo.soi," +
				"task.itemKey.lotInfo.batchNum," +
				"task.itemKey.lotInfo.produceDate," +
				"task.itemKey.lotInfo.expireDate," +
				"task.itemKey.lotInfo.warnDate," +
				"task.itemKey.lotInfo.supplier.name) " +
				"FROM WmsTask task,WmsLocation fromLocation " +
				"LEFT JOIN task.itemKey.lotInfo.supplier " +
				"WHERE fromLocation.id = task.fromLocationId " +
				"AND task.pickTicketDetail.pickTicket.id = :pickTicketId " +
				"AND task.movedQuantityBU = 0 " +
				"AND task.pickTicketDetail.pickTicket.warehouse.id = :warehouseId";
		
		List<PT_ALLOCATED> ptad_info = commonDao.findByQuery(hql, 
				new String[] {"pickTicketId", "warehouseId"}, 
				new Object[] {pickTicketId, WmsWarehouseHolder.getWmsWarehouse().getId()});

		Map<String, List> result = new HashMap<String, List>();
		result.put(CT_PA.PTAD_RESULT, ptad_info);
		
		return result;
	}
	
	// 查询发货单明细对应的库存信息
	public Map init_AL_INFO(Map params) {
		Long itemId = (Long) params.get("itemId");
		
		boolean containLockInv = (Boolean) params.get("containLockInv");
		boolean isFitAsLot = (Boolean) params.get("isFitAsLot");
		
		StringBuffer hqlb = new StringBuffer();
		
		String storageDate = (String) params.get("storageDate");
		String soi = (String) params.get("soi");
		String batchNum = (String) params.get("batchNum");
		String produceDate = (String) params.get("produceDate");
		String expireDate = (String) params.get("expireDate");
		String warnDate = (String) params.get("warnDate");
		String supplier = (String) params.get("supplier");

		hqlb.append("SELECT new com.vtradex.wms.client.ui.javabean.PT_AVAILABLE(inventory.id,")
				.append("inventory.location.code,inventory.itemKey.item.code,")
				.append("inventory.itemKey.item.name,inventory.packageUnit.unit,")
				.append("inventory.packageUnit.convertFigure,")
				.append("inventory.quantity,")
				.append("inventory.quantityBU - inventory.allocatedQuantityBU,0,")
				.append("inventory.status,")
				.append("inventory.itemKey.lot,")
				.append("inventory.itemKey.lotInfo.storageDate,inventory.itemKey.lotInfo.soi,")
				.append("inventory.itemKey.lotInfo.batchNum,")
				.append("inventory.itemKey.lotInfo.produceDate,inventory.itemKey.lotInfo.expireDate,")
				.append("inventory.itemKey.lotInfo.warnDate,inventory.itemKey.lotInfo.supplier.name) ")
				.append("FROM WmsInventory inventory ")
				.append("LEFT JOIN inventory.itemKey ")
				.append("LEFT JOIN inventory.itemKey.item ")
				.append("LEFT JOIN inventory.itemKey.lotInfo.supplier ")
				.append("WHERE 1=1 ")
				.append("AND inventory.itemKey.item.id = :itemId ")
				.append("AND (inventory.quantityBU - inventory.allocatedQuantityBU) > 0 ")
				.append("AND inventory.location.warehouse.id = :warehouseId ")
				.append("AND inventory.location.type IN ('STORAGE') ");
		
		if (!containLockInv) {
			hqlb.append("AND (inventory.status = '-')");
		}
		
		if (isFitAsLot) {
			hqlb
			.append(" AND ")
			.append(NewLotInfoParser.decryptDateOfLot("storageDate", storageDate))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfLot("soi", soi))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfLot("batchNum", batchNum))
			.append(" AND ")
			.append(NewLotInfoParser.decryptDateOfLot("produceDate", produceDate))
			.append(" AND ")
			.append(NewLotInfoParser.decryptDateOfLot("expireDate", expireDate))
			.append(" AND ")
			.append(NewLotInfoParser.decryptDateOfLot("warnDate", warnDate))
			.append(" AND ")
			.append(NewLotInfoParser.decryptStringOfLot("supplier.name", supplier)).append(")");
		}

		final String hql = hqlb.toString();

		List<PT_AVAILABLE> pt_available = commonDao.findByQuery(hql,
				new String[] {"itemId", "warehouseId"},
				new Object[] {itemId, WmsWarehouseHolder.getWmsWarehouse().getId()});
		
		Map<String, List> result = new HashMap<String, List>();
		result.put(CT_PA.PTA_RESULT, pt_available);
		
		return result;
	}

	public void pickConfirm(List<WmsPickTicket> pickTickets){
		for(WmsPickTicket pickTicket : pickTickets){	
			pickConfirm(pickTicket);				
		}
	}
	
	public void pickConfirm(WmsPickTicket pickTicket){
		List<WmsTask> tasks = (List<WmsTask>)commonDao.findByQuery("FROM WmsTask task " +
					"WHERE task.pickTicketDetail.pickTicket.id = :pickTicketId",
				new String[]{"pickTicketId"},
				new Object[]{pickTicket.getId()});
		
		for(WmsTask task : tasks){
//			task.getMoveDocDetail().addPickedQuantityBU(task.getMovedQuantityBU());
		}
	}

	public void createBOL(WmsPickTicket pickTicket){
	}

	private void createBOLByPickTicket(WmsPickTicket pickTicket) {
	}

	private void cancelAllocate(WmsTask task,Double quantity){		
		//对未作业确认数量库存取消分配
		WmsInventory fromInventory = commonDao.load(WmsInventory.class, task.getSrcInventoryId());
		fromInventory.unallocatePickup(quantity);

		WmsInventory toInventory = commonDao.load(WmsInventory.class, task.getDescInventoryId());
		toInventory.unallocatePutaway(quantity);
		
		//对作业任务，作业单扣减计划数量
		task.unallocatePick(quantity);
		
		//如果task无计划作业数量，则删除task
		if (task.getPlanQuantityBU().doubleValue() == 0) {
			commonDao.delete(task);
		} else {
			//作业任务作业确认
			workflowManager.doWorkflow(task, "taskProcess.confirm");	
		}

		//作业单作业确认，更新作业单状态
		workflowManager.doWorkflow(task.getWorkDoc(), "workDocProcess.confirm");	
	}

	public void seprate(WmsPickTicket pickTicket){
		//分单时对发货单分配
//		wmsTransactionalManager.autoAllocate(pickTicket,pickTicket.getWaveDoc());
//		workflowManager.doWorkflow(pickTicket, "wmsPickTicketBaseProcess.seprate");	
//		
//		workflowManager.doWorkflow(pickTicket.getWaveDoc(), "wmsWaveDocPickProcess.seprate");
	}

	public void unseprate(WmsPickTicket pickTicket){
		//分单时对发货单分配			
//		wmsTransactionalManager.autoCancelAllocate(pickTicket,pickTicket.getWaveDoc());
//		workflowManager.doWorkflow(pickTicket, "wmsPickTicketBaseProcess.unseprate");
//
//		workflowManager.doWorkflow(pickTicket.getWaveDoc(), "wmsWaveDocPickProcess.unseprate");
	}

	public String getMaxLineNoByPickTicketDetail(Map param) {
		Integer lineNo = (Integer) commonDao.findByQueryUniqueResult("SELECT MAX(detail.lineNo) FROM WmsPickTicketDetail detail WHERE detail.pickTicket.id = :pickTicketId", 
				new String[] {"pickTicketId"}, new Object[] {(Long) param.get("pickTicket.id")});
		if (lineNo == null || lineNo.intValue() == 0) {
			lineNo = 10;
		} else {
			lineNo += 10;
		}

		return ""+lineNo;
	}
	
	public Long getStatusByPickTicketDetail(Map param) {
		Long companyId = (Long) param.get("pickTicket.company.id");
		Long pickTicketId = (Long) param.get("pickTicket.id");
		String inventoryStatus = (String) commonDao.findByQueryUniqueResult("SELECT max(detail.inventoryStatus) FROM WmsPickTicketDetail detail " +
				"WHERE detail.pickTicket.id = :pickTicketId", 
				new String[] {"pickTicketId"}, new Object[] {pickTicketId});
		 
		List<WmsItemState> itemState = (List<WmsItemState>)commonDao.findByQuery("FROM WmsItemState itemState " +
				" WHERE itemState.company.id = :companyId and " +
				" itemState.beSend = true and itemState.name=:inventoryStatus ",
				new String[]{"companyId","inventoryStatus"},new Object[]{companyId,inventoryStatus});
		if(itemState.size()>0) {
			return itemState.get(0).getId();
		} else
			return 0L;
	}
	
	public void copyPickTicket(WmsPickTicket pickTicket, Double quantity) {
		int num = 0;
		try {
			num = (int)Math.round(quantity);
		} catch (NumberFormatException nfe) {
			throw new BusinessException("数量只能为整数类型！");
		}
		if(num <= 0) {
			throw new BusinessException("数量必须为正整数！");
		}
		if(!pickTicket.getStatus().equals(WmsPickTicketStatus.OPEN)) {
			throw new BusinessException("只能对打开状态下的发货单进行复制！");
		}
		
		for (int i = 0; i < num; i++) {
			WmsPickTicket pk = EntityFactory.getEntity(WmsPickTicket.class);
			BeanUtils.copyEntity(pk, pickTicket);
			String code = codeManager.generateCodeByRule(pickTicket.getWarehouse(), pickTicket.getCompany().getName(), "发货单", pickTicket.getBillType().getName());
			pk.setId(null);
			pk.setCode(code);
			pk.setOrderDate(new Date());
			pk.setAllocatedQuantityBU(0D);
			pk.setExpectedQuantityBU(0D);
			pk.setPickedQuantityBU(0D);
			pk.setShippedQuantityBU(0D);
			pk.setDetails(new HashSet<WmsPickTicketDetail>());
			commonDao.store(pk);
			for (WmsPickTicketDetail detail : pickTicket.getDetails()) {
				WmsPickTicketDetail d = EntityFactory.getEntity(WmsPickTicketDetail.class);
				BeanUtils.copyEntity(d, detail);
				d.setId(null);
				d.setPickTicket(pk);
				d.setAllocatedQuantityBU(0D);
				d.setPickedQuantityBU(0D);
				d.setShippedQuantityBU(0D);
				pk.addPickTicketDetail(d);
				commonDao.store(d);
			}
		}
	}
	
	public void batchPickTicket(Long companyId, Long billTypeId, Long num,
			Long skuKindNum, Long skuNum, String skuLevel, String skuClass1,
			String skuClass2) {
		Long errorNum = 0L;
		String errorResult = "";
		List<WmsPackageUnit> packageUnits = getPackageUnitList(skuKindNum, skuLevel, skuClass1, skuClass2);
		for (int i = 0; i < num; i++) {
			List<WmsPackageUnit> currentPackageUnits  = getRandomByPackageUnitList(packageUnits, skuKindNum);
			String result = batchPickTicketSingle(companyId, billTypeId, num, skuNum, currentPackageUnits);
			if(!StringUtils.isEmpty(result)) {
				errorNum++;
				errorResult += result;
			}
		}	
		if(errorNum == num) {
			throw new BusinessException("批量创建发货单失败！"+errorResult);
		} else {
			LocalizedMessage.addLocalizedMessage("批量创建发货单成功！创建发货单成功【"+ (num-errorNum)+"】单，失败【"+ errorNum+"】");
		}
	}

	/**
	 * @param skuKindNum
	 * @return
	 */
	private List<WmsPackageUnit> getRandomByPackageUnitList(List<WmsPackageUnit> packageUnits, Long skuKindNum) {
		int pSize =  packageUnits.size();
		if(skuKindNum >= pSize) {
			return packageUnits;
		}
		List<WmsPackageUnit> reList = new ArrayList<WmsPackageUnit>();
		Random random = new Random();
		for (int i = 0; i < skuKindNum; i++) {
			int target = random.nextInt(packageUnits.size());
			reList.add(packageUnits.get(target));
			//packageUnits.remove(target);
		}
		return reList;
	}

	/**
	 * @param skuKindNum
	 * @param skuLevel
	 * @param skuClass1
	 * @param skuClass2
	 * @return
	 */
	private List<WmsPackageUnit> getPackageUnitList(Long skuKindNum,
		String skuLevel, String skuClass1, String skuClass2) {
		String joinHql = "";
		if(StringUtils.isEmpty(skuClass1) && StringUtils.isEmpty(skuClass2)) {
			joinHql = " OR 1=1 ";
		} else {
			if(!StringUtils.isEmpty(skuClass1)) {
				joinHql = " OR packageUnit.item.class1 ='" + skuClass1+"'";
			} 
			if(!StringUtils.isEmpty(skuClass2)) {
				joinHql += " OR packageUnit.item.class1 ='" + skuClass2 +"'";
			}
		}
		String hql = " FROM WmsPackageUnit packageUnit WHERE packageUnit.level =:skuLevel " 
			+ "AND (0=1 " + joinHql + ")";
		
		List<WmsPackageUnit> packageUnits = commonDao.findByQuery(hql,
				new String[] {"skuLevel"}, new Object[] {skuLevel});
		if(packageUnits.size() == 0) {
			throw new BusinessException("找不到符合条件的货品！");
		}
		return packageUnits;
	}

	public String batchPickTicketSingle(Long companyId, Long billTypeId,
			Long num, Long skuNum, List<WmsPackageUnit> currentPackageUnits) {
		WmsPickTicket pickTicket = EntityFactory.getEntity(WmsPickTicket.class);
		WmsOrganization company = this.commonDao.load(WmsOrganization.class, companyId);
		WmsBillType billType = this.commonDao.load(WmsBillType.class, billTypeId);
		try {
			pickTicket.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
			String code = codeManager.generateCodeByRule(pickTicket.getWarehouse(), company.getName(), "发货单", billType.getName());
			pickTicket.setCode(code);
			pickTicket.setCompany(company);
			pickTicket.setBillType(billType);
			pickTicket.setOrderDate(new Date());
			pickTicket.setDetails(new HashSet<WmsPickTicketDetail>());
			workflowManager.doWorkflow(pickTicket,"wmsPickTicketBaseProcess.new");
			Integer lineNo = 0;
			for (WmsPackageUnit packageUnit : currentPackageUnits) {
				WmsPickTicketDetail detail = EntityFactory.getEntity(WmsPickTicketDetail.class);
				lineNo +=10;
				detail.setLineNo(lineNo);
				detail.setItem(packageUnit.getItem());
				detail.setPackageUnit(packageUnit);
				detail.setInventoryStatus(BaseStatus.NULLVALUE);
				detail.setExpectedQuantity(skuNum.doubleValue());
				detail.setExpectedQuantityBU(detail.getExpectedQuantity() * packageUnit.getConvertFigure());
				detail.setAllocatedQuantityBU(0D);
				detail.setPickedQuantityBU(0D);
				detail.setShippedQuantityBU(0D);
				pickTicket.addPickTicketDetail(detail);
				commonDao.store(detail);
			}
			commonDao.store(pickTicket);
			workflowManager.doWorkflow(pickTicket, "wmsPickTicketBaseProcess.active");
		} catch (BusinessException be) {
			be.printStackTrace();
			return be.getMessage();
		}
		return "";
	}
	
	/**
	 *  打印MES
	 */
	public Boolean printMes(Map<Object, Object> map){
		List<Long> parentIds = (List<Long>) map.get("parentIds");
		if(parentIds != null){
			for(Long id : parentIds){
//				WmsPickTicket pickTicket = this.commonDao.load(WmsPickTicket.class, id);
			}
		}
		return true;
	}
}