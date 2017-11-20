package com.vtradex.wms.server.telnet.pick.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.message.Task;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.ShipLotInfo;
import com.vtradex.wms.server.model.base.WmsLogTitle;
import com.vtradex.wms.server.model.base.WmsLogType;
import com.vtradex.wms.server.model.carrier.WmsVehicle;
import com.vtradex.wms.server.model.interfaces.HeadType;
import com.vtradex.wms.server.model.interfaces.WBols;
import com.vtradex.wms.server.model.interfaces.WContainers;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.inventory.WmsInventoryLogType;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsMoveDocShipStatus;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.move.WmsTaskStatus;
import com.vtradex.wms.server.model.move.WmsWorkDoc;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.shipping.WmsMoveDocAndStation;
import com.vtradex.wms.server.model.shipping.WmsPickTicket;
import com.vtradex.wms.server.model.shipping.WmsPickTicketAndAppliance;
import com.vtradex.wms.server.model.shipping.WmsPickTicketDetail;
import com.vtradex.wms.server.model.shipping.WmsTaskAndStation;
import com.vtradex.wms.server.model.warehouse.WmsBoxType;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsLocationType;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.rule.WmsTransactionalManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.task.WmsTaskManager;
import com.vtradex.wms.server.service.workDoc.WmsWorkDocManager;
import com.vtradex.wms.server.telnet.dto.WmsBOLDTO;
import com.vtradex.wms.server.telnet.dto.WmsPickContainerDTO;
import com.vtradex.wms.server.telnet.dto.WmsPickTaskDTO;
import com.vtradex.wms.server.telnet.manager.pojo.DefaultLimitQueryBaseManager;
import com.vtradex.wms.server.telnet.pick.WmsPickRFManager;
import com.vtradex.wms.server.telnet.putaway.WmsPutawayRFManager;
import com.vtradex.wms.server.telnet.shell.ShellExceptions;
import com.vtradex.wms.server.telnet.shell.pick.WmsBOLShell;
import com.vtradex.wms.server.telnet.shell.pick.WmsPickBackMoveDocShell;
import com.vtradex.wms.server.telnet.shell.pick.WmsScanPickShell;
import com.vtradex.wms.server.utils.LotInfoUtil;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.utils.PackageUtils;
import com.vtradex.wms.server.utils.WmsTables;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;
import com.vtradex.wms.server.web.filter.WmsWorkerHolder;

public class DefaultWmsPickRFManager extends DefaultLimitQueryBaseManager
		implements WmsPickRFManager {
	
	private final WmsWorkDocManager workDocManager;
	private final WorkflowManager workflowManager;
	private final WmsRuleManager wmsRuleManager;
	private final WmsInventoryManager wmsInventoryManager;
	private WmsBussinessCodeManager codeManager;
	
	private JdbcTemplate jdbcTemplate;
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	public DefaultWmsPickRFManager(WmsWorkDocManager workDocManager , WorkflowManager workflowManager, 
			WmsRuleManager wmsRuleManager,WmsInventoryManager wmsInventoryManager,WmsBussinessCodeManager codeManager) {
		this.workDocManager = workDocManager;
		this.workflowManager = workflowManager;
		this.wmsRuleManager = wmsRuleManager;
		this.wmsInventoryManager = wmsInventoryManager;
		this.codeManager = codeManager;
	}

	public WmsPickTaskDTO findPickTaskById(Long workDocId) {
		WmsWorkDoc workDoc = commonDao.load(WmsWorkDoc.class, workDocId);
		workDoc.setWorker(WmsWorkerHolder.getWmsWorker());
		commonDao.store(workDoc);
		String hql = "SELECT new com.vtradex.wms.server.telnet.dto.WmsPickTaskDTO(task.moveDocDetail.moveDoc.id,task.moveDocDetail.moveDoc.code," +
			"task.workDoc.id,task.workDoc.code,task.id,task.fromLocationId,task.fromLocationCode,task.toLocationId," +
			"task.toLocationCode,task.itemKey.item.code,task.itemKey.item.name,task.itemKey.item.barCode,task.planQuantityBU-task.movedQuantityBU)" +
			" FROM WmsTask task , WmsLocation loc" +
			" WHERE task.fromLocationId = loc.id AND task.workDoc.id=:workDocId" +
			" AND task.status in (:statuss)" +
			" ORDER BY loc.routeNo ASC , task.itemKey.item.code ASC";
		return (WmsPickTaskDTO)this.findByHqlLimitQuery(hql, new String[]{"workDocId" , "statuss"}, new Object[]{workDocId , Arrays.asList(new String[]{WmsTaskStatus.DISPATCHED ,WmsTaskStatus.WORKING})}, 1);
	}

	public Long checkWorkDocCode(String workDocCode) {
		WmsWorkDoc workDoc = this.findWorkDocByCode(workDocCode);
		if(workDoc == null) {
			throw new BusinessException("不存在该作业单");
		}
//		if(!workDoc.getWorkArea().getId().equals(WmsWorkAreaHolder.getWmsWorkArea().getId())) {
//			throw new BusinessException("作业单不属于当前作业区");
//		}
		if(workDoc.getWorker() != null && !workDoc.getWorker().getId().equals(WmsWorkerHolder.getWmsWorker().getId())) {
			throw new BusinessException("作业单已被其他作业人员申请");
		}
		return workDoc.getId();
	}

	private WmsWorkDoc findWorkDocByCode(String code){
		return (WmsWorkDoc)commonDao.findByQueryUniqueResult("FROM WmsWorkDoc wd WHERE wd.code=:code AND wd.warehouse.id=:wId"
				, new String[]{"code","wId"}, new Object[]{code , WmsWarehouseHolder.getWmsWarehouse().getId()});
	}

	public void confirmPick(WmsPickTaskDTO pickTaskDTO, String locationCode,
			Double quantity) {
		WmsLocation srcLocation = queryWmsLocationByCode(locationCode);
		WmsTask task = commonDao.load(WmsTask.class, pickTaskDTO.getTaskId());
		workDocManager.singleWorkConfirm(task, pickTaskDTO.getToLocationId(), srcLocation.getId(), quantity, WmsWorkerHolder.getWmsWorker().getId());
		workflowManager.doWorkflow(task, "taskProcess.confirm");
	}
	
	public void markExceptionWmsLocation(Long locationId) throws BusinessException {
		try {
			WmsLocation loc = commonDao.load(WmsLocation.class, locationId);
			loc.setExceptionFlag(Boolean.TRUE);
		} catch (BusinessException be) {
			throw new BusinessException("标识异常库位失败，请重试");
		}
	}

	public void resetAllocate(WmsPickTaskDTO pickTaskDTO) {
		
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, pickTaskDTO.getMoveDocId());
		WmsTask task = commonDao.load(WmsTask.class, pickTaskDTO.getTaskId());
		WmsMoveDocDetail moveDocDetail = commonDao.load(WmsMoveDocDetail.class, task.getMoveDocDetail().getId());
		
		WmsPickTicket pickTicket = commonDao.load(WmsPickTicket.class, moveDoc.getPickTicket().getId());
		WmsBillType billType = commonDao.load(WmsBillType.class, pickTicket.getBillType().getId());
		String billTypeName = billType.getName();
		
		WmsOrganization company = commonDao.load(WmsOrganization.class, moveDoc.getCompany().getId());
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class, moveDoc.getWarehouse().getId());				
		String companyName = company.getName();
		if(company.isBeVirtual()){
			companyName = warehouse.getName();
		}
		String warehouseName = warehouse.getName();
		Long warehouseId = warehouse.getId();
		
		Map<String, Object> problem = new HashMap<String, Object>();
		problem.put("仓库ID", warehouseId);
		problem.put("货主", companyName);
		problem.put("单据类型", billTypeName);
		problem.put("货主", companyName);
		WmsItem item = commonDao.load(WmsItem.class, task.getItemKey().getItem().getId());
		WmsPackageUnit packageUnit = commonDao.load(WmsPackageUnit.class, task.getPackageUnit().getId());
		problem.put("拣货分类", item.getClass2());
		problem.put("包装级别", packageUnit.getLevel());
		problem.put("源拣货库位序号", task.getFromLocationId());
		problem.put("货品ID", item.getId());
		problem.put("货品编码", item.getCode());
		problem.put("待拣选数量", task.getUnmovedQuantityBU());
		problem.put("库存状态", task.getInventoryStatus());
		problem.put("数量", task.getUnmovedQuantityBU());
		if (pickTicket.getCustomer() != null) {
			WmsOrganization customer = commonDao.load(WmsOrganization.class, pickTicket.getCustomer().getId());
			problem.put("收货人", pickTicket.getCustomer() == null ? "" : customer.getName());
		} else {
			problem.put("收货人", "");
		}
		
		ShipLotInfo shipLotInfo = moveDocDetail.getShipLotInfo();
		if (shipLotInfo == null) {
			shipLotInfo = new ShipLotInfo();
		}
		LotInfoUtil.generateShipLotInfo(problem, shipLotInfo, Boolean.TRUE);
		
		Map<String, Object> result = wmsRuleManager.execute(warehouseName, companyName, "拣货分配规则", problem);
		
		//作业任务取消分配
		Double unallocateQty = task.getUnmovedQuantityBU();
		WmsInventory fromInv = commonDao.load(WmsInventory.class,
				task.getSrcInventoryId());
		fromInv.unallocatePickup(unallocateQty);
		WmsPickTicketDetail pickTicketDetail = load(
				WmsPickTicketDetail.class, moveDocDetail.getRelatedId());
		if (pickTicketDetail != null) {
			pickTicketDetail.unallocate(unallocateQty);
		}
		
		task.unallocatePick(unallocateQty);
		if(task.getStatus().equals(WmsTaskStatus.WORKING) && task.getMovedQuantityBU()>0) {
			workflowManager.doWorkflow(task, "taskProcess.confirm");
		}
		
		double tempQuantity = 0D;
		//作业任务重新分配
		int size = ((List<Map<String, Object>>) result.get("返回列表")).size();
		for (int i = 0; i < size; i++) {
			Map<String, Object> wmsTaskInfos = ((List<Map<String, Object>>) result.get("返回列表")).get(i);
			double quantity = Double.valueOf(wmsTaskInfos.get(
					"分配数量").toString());
			if (quantity == 0) {
				continue;
			}
			Long srcInventoryId = Long.valueOf(wmsTaskInfos.get("库存ID").toString());
			WmsInventory srcInv = commonDao.load(WmsInventory.class,srcInventoryId);
			
			if (moveDocDetail.getPlanQuantityBU() > tempQuantity) {
				if (i == 0 && task.getMovedQuantityBU() == 0) {
					pickupAllocate(task, srcInv,quantity,moveDocDetail,false);
				} else {
					pickupAllocate(task, srcInv,quantity,moveDocDetail,true);
				}
				pickTicketDetail.allocate(quantity);
				commonDao.store(pickTicketDetail);

				tempQuantity+=quantity;
			} else {
				break;
			}
		}
	}
	
	private void pickupAllocate(WmsTask task, WmsInventory srcInv, double quantityBU, 
			WmsMoveDocDetail detail, boolean isNew) {
		
		srcInv.allocatePickup(quantityBU);
		commonDao.store(srcInv);
		
		detail.setInventoryId(srcInv.getId());
		detail.setFromLocationId(srcInv.getLocation().getId());
		detail.setFromLocationCode(srcInv.getLocation().getCode());
		detail.allocate(quantityBU);
		commonDao.store(detail);
		
		if(isNew) {
			WmsTask newTask = EntityFactory.getEntity(WmsTask.class);
			BeanUtils.copyEntity(newTask, task);
			newTask.setId(null);
			newTask.setPlanQuantityBU(quantityBU);
			newTask.setPlanQuantity(PackageUtils.convertPackQuantity(quantityBU,task.getPackageUnit()));
			newTask.setMovedQuantityBU(0D);
			newTask.setStatus(WmsTaskStatus.DISPATCHED);
			newTask.setFromLocationId(srcInv.getLocation().getId());
			newTask.setFromLocationCode(srcInv.getLocation().getCode());
			newTask.setSrcInventoryId(srcInv.getId());
			detail.getTasks().add(newTask);
			newTask.getWorkDoc().addTask(newTask);
			commonDao.store(newTask);
		} else {
			task.addPlanQuantityBU(quantityBU);
			task.setFromLocationId(srcInv.getLocation().getId());
			task.setFromLocationCode(srcInv.getLocation().getCode());
			task.setSrcInventoryId(srcInv.getId());
			task.getWorkDoc().addTask(task);
			commonDao.store(task);
		}
		
	}
	public String pickConfirmAll(String pickNo){
		WmsPickTicket pic = checkPicIsNull(pickNo);
		if(pic==null){
			return "pic is null";
		}
		List<WmsMoveDoc> mm = checkMoveIsNull(pic.getId());//,WmsMoveDocStatus.ACTIVE,WmsMoveDocStatus.WORKING
		if(mm==null){
			return "move is null";
		}
		Boolean anyExits = true;
		for(WmsMoveDoc m : mm){
			if(m.getStatus().equals(WmsMoveDocStatus.ACTIVE) 
					|| m.getStatus().equals(WmsMoveDocStatus.WORKING)){
				workDocManager.pickConfirmAll(m);
				workflowManager.doWorkflow(m, "wmsMoveDocProcess.confirm");
				anyExits = false;
			}
		}
		if(anyExits){
			String mesg = "未知的拣货单状态错误";
			for(WmsMoveDoc m : mm){
				if(m.getStatus().equals(WmsMoveDocStatus.OPEN) 
						|| m.getStatus().equals(WmsMoveDocStatus.PARTALLOCATED)){
					mesg = "失败!拣货单未整单分配";
				}else if(m.getStatus().equals(WmsMoveDocStatus.ALLOCATED)){
					mesg = "失败!拣货单未生效";
				}else if(m.getStatus().equals(WmsMoveDocStatus.FINISHED)){
					mesg = "失败!已拣货扫码,重复扫码";
				}else if(m.getStatus().equals(WmsMoveDocStatus.CANCELED)){
					mesg = "失败!拣货单已取消";
				}
				return mesg;
			}
		}
		return MyUtils.SUCCESS;
	}
	private WmsPickTicket checkPicIsNull(String pickNo){
		WmsPickTicket pic = (WmsPickTicket) commonDao.findByQueryUniqueResult("FROM WmsPickTicket p"
				+ " WHERE (p.code =:code OR p.relatedBill1 =:code) ", 
				new String[]{"code"}, new Object[]{pickNo});
		if(pic==null){
			return null;//"pic is null";
		}
		return pic;
	}
	@SuppressWarnings("unchecked")
	private List<WmsMoveDoc> checkMoveIsNull(Long pickTicketId){//,String status1,String status2
		List<WmsMoveDoc> mm = commonDao.findByQuery("FROM WmsMoveDoc m"
				 +" WHERE m.pickTicket.id =:pickTicketId",
//				 +" AND (m.status =:status1 OR m.status =:status2)", 
				new String[]{"pickTicketId"},//,"status1","status2"
				new Object[]{pickTicketId});//,status1,status2
		if(mm==null || mm.size()<=0){
			return null;
		}
		return mm;
	}
	public String shipRecord(String pickNo,String vehicleNo){
		WmsPickTicket pic = checkPicIsNull(pickNo);
		if(pic==null){
			return "pic is null";
		}
		List<WmsMoveDoc> mm = checkMoveIsNull(pic.getId());//,WmsMoveDocStatus.FINISHED,WmsMoveDocStatus.WORKING
		if(mm==null){
			return "move is null";
		}
		for(WmsMoveDoc m : mm){
			if(!m.getShipStatus().equals(WmsMoveDocShipStatus.UNSHIP)
					&& !m.getStatus().equals(WmsMoveDocStatus.FINISHED)
					&& !m.getStatus().equals(WmsMoveDocStatus.WORKING)){
//				return "move is ship";
				continue;
			}
			m.setVehicleNo(vehicleNo);
			commonDao.store(m);
			workDocManager.shipRecord(m);
		}
		return pickNo;
		
	}
	
	public WmsPickContainerDTO findPickContainer(WmsWorker blg){
		String taskHql = " FROM WmsTask task WHERE task.status !=:status AND task.moveDocDetail.id =:moveDocDetailId AND task.planQuantityBU > 0 ";
		String hql = " FROM WmsMoveDocAndStation mds WHERE mds.isFinished =:isFinished AND mds.moveDocDetail.moveDoc.blg.id =:blgId" +
				" AND mds.quantity > mds.pickQuantity AND mds.moveDocDetail.moveDoc.status IN(:status)" +
				" ORDER BY mds.id ASC ";
		WmsMoveDocAndStation mds = (WmsMoveDocAndStation) this.findByHqlLimitQuery(hql, new String[]{"isFinished","blgId","status"}, 
				new Object[]{Boolean.FALSE,blg.getId(),Arrays.asList(WmsMoveDocStatus.ACTIVE,WmsMoveDocStatus.WORKING)},1);
		if(mds == null){
			return null;
		}
		WmsTask task = (WmsTask) this.findByHqlLimitQuery(taskHql, 
				new String[]{"status","moveDocDetailId"}, 
				new Object[]{WmsTaskStatus.FINISHED,mds.getMoveDocDetail().getId()}, 1);
		if(task==null){
			throw new BusinessException(mds.getMoveDocDetail().getMoveDoc().getCode()+"无拣货任务!");
		}
		WmsPickContainerDTO dto = new WmsPickContainerDTO();
		dto.setTaskId(task.getId());
		dto.setMoveDocAndStationId(mds.getId());
		dto.setItemId(mds.getItem().getId());
		dto.setItemCode(mds.getItem().getCode());
		dto.setItemName(mds.getItem().getName());
		dto.setType(mds.getType());
		dto.setLocationCode(task.getFromLocationCode());
		dto.setSupplier(mds.getMoveDocDetail().getShipLotInfo().getSupplier());
		if(task.getUnmovedQuantityBU() >= mds.getQuantity()-mds.getPickQuantity()){
			dto.setPlanQuantityBU(mds.getQuantity()-mds.getPickQuantity());
			dto.setUnMoveQuantityBU(mds.getQuantity()-mds.getPickQuantity());
		}else{
			dto.setPlanQuantityBU(task.getUnmovedQuantityBU());
			dto.setUnMoveQuantityBU(task.getUnmovedQuantityBU());
		}
		return dto;
	}
	
	@SuppressWarnings("unchecked")
	public void confimPickByContainer(WmsPickContainerDTO dto,Boolean isFinished){
		WmsTask task = commonDao.load(WmsTask.class, dto.getTaskId());
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, task.getMoveDocDetail().getMoveDoc().getId());
		WmsWorker worker = WmsWorkerHolder.getWmsWorker();
		String hql = " FROM WmsTaskAndStation wts WHERE wts.station.id =:stationId AND wts.isConfirm =:isConfirm ORDER BY wts.id ASC";
		List<WmsTaskAndStation> stations = commonDao.findByQuery(hql, new String[]{"stationId","isConfirm"}, new Object[]{dto.getMoveDocAndStationId(),Boolean.FALSE});
		Double qty = 0D;
		for(WmsTaskAndStation wts : stations){
			workDocManager.singleConfirm(wts.getTask(), moveDoc.getId(), wts.getPickQuantityBu(), wts.getInventoryId(), worker.getId());
			qty += wts.getPickQuantityBu();
			wts.setIsConfirm(Boolean.TRUE);
			commonDao.store(wts);
		}
		WmsMoveDocAndStation mds = commonDao.load(WmsMoveDocAndStation.class, dto.getMoveDocAndStationId());
//		mds.setContainer(dto.getContainer());
		mds.setPickQuantity(mds.getPickQuantity()+qty);
		if(mds.getQuantity() - mds.getPickQuantity() == 0D){
			mds.setIsFinished(Boolean.TRUE);
		}
		if(isFinished){
			mds.setIsFinished(Boolean.TRUE);
		}
		commonDao.store(mds);
		if(task.getUnmovedQuantityBU() > 0){//库存短缺  原库位取消拣货分配占用量并标记异常库位
			// 原库位取消拣货分配
			WmsInventory srcInv = commonDao.load(WmsInventory.class, task.getSrcInventoryId());
			/*srcInv.unallocatePickup(task.getUnmovedQuantityBU());
			//目标库位取消上架分配
			WmsInventory discInv =commonDao.load(WmsInventory.class, task.getDescInventoryId());
			discInv.unallocatePutaway(task.getUnmovedQuantityBU());*/
			//标记异常库位记录库存日志--库位异常暂不标记
//			WmsLocation loc = commonDao.load(WmsLocation.class, task.getFromLocationId());
//			loc.setExceptionFlag(Boolean.TRUE);
//			commonDao.store(loc);
			wmsInventoryManager.addInventoryLog(WmsInventoryLogType.INVENTORY_EXCEPTION, 0, 
					moveDoc.getCode(), moveDoc.getBillType(), srcInv.getLocation(), srcInv.getItemKey(), 0D,   
					srcInv.getPackageUnit(),srcInv.getStatus(),"拣货异常");
		}
	}
	
	public WmsInventory getInventoryQtyByLocation(String location,String itemCode,String supplier){
		String hql = "FROM WmsInventory inv WHERE inv.location.code =:locationCode AND inv.itemKey.item.code =:itemCode AND inv.itemKey.lotInfo.supplier.code =:supplier AND inv.quantityBU -inv.allocatedQuantityBU >0 ";
		WmsInventory inv = (WmsInventory) this.findByHqlLimitQuery(hql, new String[]{"locationCode","itemCode","supplier"}, new Object[]{location,itemCode,supplier}, 1);
		return inv;
	}
	
	//调用类:WmsPickContainerCodeShell,WmsPickPartShell
	@SuppressWarnings("unchecked")
	public WmsPickContainerDTO getWmsTaskByMoveDocId(Long moveDetailId,String picktype){
		String hql = "SELECT task FROM WmsTask task,WmsLocation loc WHERE task.fromLocationId =loc.id" +
				" AND task.moveDocDetail.id =:moveDetailId " +
				" AND task.status in(:status) AND task.exceptionFlag =:exceptionFlag" +
				" AND (task.planQuantityBU-task.movedQuantityBU)>0" +
				" ORDER BY loc.routeNo ASC ";
		WmsTask task = (WmsTask) this.findByHqlLimitQuery(hql, 
				new String[]{"moveDetailId","status","exceptionFlag"}, 
				new Object[]{moveDetailId,Arrays.asList(WmsTaskStatus.DISPATCHED,WmsTaskStatus.WORKING),Boolean.FALSE}, 1);
		if(task == null){// || task.getUnmovedQuantityBU() == 0
			return null;
		}
		String type = MyUtils.PARTS,boxTagNo = "",typeName = MyUtils.PARTS;
		//每次先推送量大的容器拣货
		hql = "SELECT DISTINCT w.type,w.quantity,w.boxTag,w.typeName FROM WmsMoveDocAndStation w WHERE w.moveDocDetail.id = "+moveDetailId+
				" AND w.isFinished = false ORDER BY w.quantity DESC";
		List<Object[]> types = commonDao.findByQuery(hql);
		if(types!=null && types.size()>0){
			type = (String) types.get(0)[0];
			boxTagNo = (String) types.get(0)[2];
			typeName = (String) types.get(0)[3];
		}
		WmsItem item = commonDao.load(WmsItem.class, task.getItemKey().getItem().getId());
		WmsPickContainerDTO dto = new WmsPickContainerDTO();
		dto.setTaskId(task.getId());
		dto.setMoveDocAndStationId(null);
		dto.setItemId(item.getId());
		dto.setItemCode(item.getCode());
		dto.setItemName(item.getName());
		dto.setType(type);
		dto.setTypeName(typeName);
		dto.setLocationCode(task.getFromLocationCode());
		dto.setSupplier(task.getMoveDocDetail().getShipLotInfo().getExtendPropC20());//getSupplier()
		dto.setPlanQuantityBU(task.getUnmovedQuantityBU());
		dto.setUnMoveQuantityBU(task.getUnmovedQuantityBU());
		dto.setProductionLine(task.getMoveDocDetail().getProductionLine());
		dto.setBoxTag(boxTagNo);
		return dto;
	}
	//调用类:WmsPickPartShell
	@SuppressWarnings("unchecked")
	public WmsPickContainerDTO confimPickByPart(WmsPickContainerDTO dto,String picktype){
		String message = MyUtils.SUCCESS;
		WmsTask task = commonDao.load(WmsTask.class, dto.getTaskId());
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, task.getMoveDocDetail().getMoveDoc().getId());
		WmsWorker worker = WmsWorkerHolder.getWmsWorker();
		dto.setPlanQuantityBU(task.getUnmovedQuantityBU());
		WmsMoveDocAndStation w = commonDao.load(WmsMoveDocAndStation.class, dto.getWmsMoveDocAndStationId());
		//如果更换了库位,要验证该库位物料是否满足
		if(!dto.getLocationCode().equals(dto.getPickLocCode())){
			WmsInventory inv =this.getInventoryQtyByLocation(dto.getPickLocCode(),dto.getItemCode(),dto.getSupplier());
			if(inv == null){
				message = "库位["+dto.getPickLocCode()+"]未匹配到库存["+dto.getItemCode()+"]";
				dto.setErrorMes(message);
				saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_PICKING, message, dto.getContainer());
				return dto;
			}
			if(inv.getAvailableQuantityBU()<dto.getPickQuantity()){
				message = "库位["+dto.getPickLocCode()+"]可用量:"+inv.getAvailableQuantityBU()+"不足";
				dto.setErrorMes(message);
				saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_PICKING, message, dto.getContainer());
				return dto;
			}
			dto.setInventoryId(inv.getId());
		}else{
			dto.setInventoryId(task.getSrcInventoryId());
		}
		if(!MyUtils.PARTS.equals(picktype)){//容器要验证是否超拣
			if(!MyUtils.SPS_APPLIANCE.equals(picktype)){//时序件理论任务待拣量小于等于器具该物料总装载量
				if(w.getAvailableQuantityBU()<dto.getPickQuantity()){
					message = "拣选量大于容器待拣量:"+w.getAvailableQuantityBU();
					dto.setErrorMes(message);
					saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_PICKING, message, dto.getContainer());
					return dto;
				}
			}
		}
		if(task.getUnmovedQuantityBU()>0 && task.getUnmovedQuantityBU()>=dto.getPickQuantity()){
			workDocManager.singleConfirm(task, moveDoc.getId(),dto.getPickQuantity(), dto.getInventoryId(), worker.getId());
		}else{
			dto.setErrorMes(ShellExceptions.PICK_QTY_NOT_FULL);
			saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_PICKING, ShellExceptions.PICK_QTY_NOT_FULL+"["+task.getUnmovedQuantityBU()+"]", dto.getContainer());
			return dto;
		}
		if(MyUtils.SPS_APPLIANCE.equals(picktype)){//时序件先这么拣选,后面如果测试没问题,看散件和容器拣货是否满足
			//以下根据拣货数量,循环处理该拣货明细下关系表的拣货量.....
			Double pickQty = dto.getPickQuantity(),availableQuantity = 0D;
			List<WmsMoveDocAndStation> wss = commonDao.findByQuery("FROM WmsMoveDocAndStation ws WHERE ws.moveDocDetail.id =:moveDocDetailId" +
					" AND ws.boxTag =:boxTag AND ws.isFinished =:isFinished", 
					new String[]{"moveDocDetailId","boxTag","isFinished"}, 
					new Object[]{task.getMoveDocDetail().getId(),dto.getBoxTag(),Boolean.FALSE});
			int size = wss.size();
			for(WmsMoveDocAndStation ws : wss){
				availableQuantity = pickQty>=ws.getAvailableQuantityBU()?ws.getAvailableQuantityBU():pickQty;
				ws.setPickQuantity(ws.getPickQuantity()+availableQuantity);
				if(ws.getAvailableQuantityBU()==0){
					ws.setIsFinished(true);
					message = ShellExceptions.CONTAINER_FULL;
					dto.setErrorMes(message);
				}else{
					message = ShellExceptions.CONTAINER_NOT_FULL;
					dto.setErrorMes(message);
//					dto.setWmsMoveDocAndStationId(ws.getId());
//					dto.setOrderBU(ws.getAvailableQuantityBU());
				}
				commonDao.store(ws);
				WmsTaskAndStation wts = EntityFactory.getEntity(WmsTaskAndStation.class);
				wts.setTask(task);
				wts.setStation(ws);
				wts.setInventoryId(dto.getInventoryId());
				wts.setPickQuantityBu(availableQuantity);
				wts.setIsConfirm(Boolean.TRUE);
				wts.setContainer(dto.getContainer());
				wts.setBoxTag(dto.getBoxTag());
				commonDao.store(wts);
				
				size--;
				pickQty -= availableQuantity;
				if(pickQty<=0){
					break;
				}
			}
			if(size>0){//业务场景:task条数小于关系表条数,祥见[容器分配与关系表.png]
				message = ShellExceptions.CONTAINER_NOT_FULL;
				dto.setErrorMes(message);
			}
		}else{
			w.setPickQuantity(w.getPickQuantity()+dto.getPickQuantity());
			if(w.getAvailableQuantityBU()==0){
				w.setIsFinished(true);
				message = ShellExceptions.CONTAINER_FULL;
				dto.setErrorMes(message);
			}else{
				message = ShellExceptions.CONTAINER_NOT_FULL;
				dto.setErrorMes(message);
				dto.setOrderBU(w.getAvailableQuantityBU());
			}
			commonDao.store(w);
			WmsTaskAndStation wts = EntityFactory.getEntity(WmsTaskAndStation.class);
			wts.setTask(task);
			wts.setStation(w);
			wts.setInventoryId(dto.getInventoryId());
			wts.setPickQuantityBu(dto.getPickQuantity());
			wts.setIsConfirm(Boolean.TRUE);
			wts.setContainer(dto.getContainer());
			wts.setBoxTag(dto.getBoxTag());
			commonDao.store(wts);
		}

		dto.setUnMoveQuantityBU(dto.getPlanQuantityBU()-dto.getPickQuantity());//判断当前task是否已拣完
		return dto;
	}
	public Double viewQty(Long moveDocDetailId,String boxTag){
		Double viewQty = (Double) commonDao.findByQueryUniqueResult("SELECT SUM(ws.quantity-ws.pickQuantity)" +
				" FROM WmsMoveDocAndStation ws WHERE ws.moveDocDetail.id =:moveDocDetailId" +
				" AND ws.boxTag =:boxTag AND ws.isFinished =:isFinished", 
				new String[]{"moveDocDetailId","boxTag","isFinished"}, 
				new Object[]{moveDocDetailId,boxTag,Boolean.FALSE});
		return viewQty;
	}
	
	//WmsPickPartShell WmsPickContainerCodeShell
	@SuppressWarnings("unchecked")
	public String checkContainerByBoxType(String container,String type,String boxTagNo,Long moveDocId){
		String hql = "FROM WmsBoxType bt WHERE bt.code =:code AND bt.status =:status";
		if(!MyUtils.PARTS.equals(type)){
			hql += " AND bt.type ='"+type+"'";
		}
		WmsBoxType bt = (WmsBoxType) commonDao.findByQueryUniqueResult(hql, new String[]{"code","status"}, 
				new Object[]{container,BaseStatus.ENABLED});
		if(bt == null){
			saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_PICKING, ShellExceptions.CONTAINER_TYPE_NOT_EXITS, container);
			return ShellExceptions.CONTAINER_TYPE_NOT_EXITS;
		}
		
		if(!MyUtils.PARTS.equals(type)){
			if(bt.getIsPicking()){
				//容器部分拣货未完成,允许这部分容器继续扫描拣货(相同标签下)
				hql = "SELECT DISTINCT "+WmsTaskAndStation.short_name+".station.boxTag" +
						" FROM WmsTaskAndStation "+WmsTaskAndStation.short_name+
						" WHERE "+WmsTaskAndStation.short_name+".station.moveDocDetail.moveDoc.shipStatus =:status" +
						" AND "+WmsTaskAndStation.short_name+".container =:container";
				List<String> boxTags = commonDao.findByQuery(hql, new String[]{"status","container"}, 
						new Object[]{WmsMoveDocShipStatus.UNSHIP,container});
				if(boxTags!=null && boxTags.size()>0){
					if(boxTags.size()>1){//同一个容器,只允许存在一个未发运的标签
						saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_PICKING, ShellExceptions.CONTAINER_TAG_ONE, container);
						return ShellExceptions.CONTAINER_TAG_ONE;
					}
					//如果A标签用了SX001,但是B标签还没用,此时如果选择B扫描SX001,会出现数据库并没有绑定信息,所以boxTags.size的值=1,此处应有提示
					//本方法带入当前容器的标签号,然后和数据库已存在的匹配,匹配不上说明不同标签的容器,报错;
					if(!boxTagNo.equals(boxTags.get(0))){
						saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_PICKING, ShellExceptions.CONTAINER_IS_USING, container);
						return ShellExceptions.CONTAINER_IS_USING;
					}
					//如果匹配上,验证该容器是否已完成还未发运
					hql = "SELECT COUNT(*) as NUM FROM WmsMoveDocAndStation w WHERE w.boxTag =:boxTag AND w.isFinished = false";
					Long mds = (Long) commonDao.findByQueryUniqueResult(hql, new String[]{"boxTag"}, 
							new Object[]{boxTags.get(0)});
					if(mds<=0){
						saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_PICKING, ShellExceptions.CONTAINER_IS_USING, container);
						return ShellExceptions.CONTAINER_IS_USING;
					}
				}
			}
		}else{//散件要在扫码和装车之前验证是否已加入过装车单(因为散件制约因素太少,可以理论上无限制的扫描装料)
			if(bt.getIsBol()){
				saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_PICKING, ShellExceptions.CONTAINER_IN_BOL, container);
				return ShellExceptions.CONTAINER_IN_BOL;
			}
			//以下是散件校验是否混装逻辑,注释
			/*else if(bt.getIsPicking()){//验证是否已经存在了不同发货单下的数据,相同发货单下的不同拣货单允许
				hql = "SELECT DISTINCT w.moveDocDetail.moveDoc.originalBillCode FROM WmsMoveDocAndStation w" +
						" WHERE w.moveDocDetail.moveDoc.shipStatus =:status" +
						" AND w.container =:container";
				List<String> originalBillCodes = commonDao.findByQuery(hql, new String[]{"status","container"}, 
						new Object[]{WmsMoveDocShipStatus.UNSHIP,container});
				if(originalBillCodes!=null && originalBillCodes.size()>0){
					if(originalBillCodes.size()>1){
						saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_PICKING, ShellExceptions.CONTAINER_PART_DIF_PICK, container);
						return ShellExceptions.CONTAINER_PART_DIF_PICK;
					}
					if(taskId!=null){
						WmsTask task = commonDao.load(WmsTask.class, taskId);
						WmsMoveDocDetail moveDocDetail = commonDao.load(WmsMoveDocDetail.class,task.getMoveDocDetail().getId());
						WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class,moveDocDetail.getMoveDoc().getId());
						if(!moveDoc.getOriginalBillCode().equals(originalBillCodes.get(0))){
							saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_PICKING, ShellExceptions.CONTAINER_PART_DIF_PICK, container);
							return ShellExceptions.CONTAINER_PART_DIF_PICK;
						}
					}
				}
			}*/
			//对于相同类型的发货单,散件和器具标签要不限制装载 yc.min 20171031
			else if(bt.getIsPicking()){//验证是否已经存在了不同类型发货单下的数据,相同类型发货单下的不同拣货单允许
				hql = "SELECT DISTINCT "+WmsTaskAndStation.short_name+".station.moveDocDetail.moveDoc.originalBillType.name" +
						" FROM WmsTaskAndStation "+WmsTaskAndStation.short_name +
						" WHERE "+WmsTaskAndStation.short_name+".shipStatus =:status" +//station.moveDocDetail.moveDoc.shipStatus
						" AND "+WmsTaskAndStation.short_name+".container =:container";
				List<String> originalBillNames = commonDao.findByQuery(hql, new String[]{"status","container"}, 
						new Object[]{WmsMoveDocShipStatus.UNSHIP,container});
				if(originalBillNames!=null && originalBillNames.size()>0){
					if(originalBillNames.size()>1){
						saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_PICKING, ShellExceptions.CONTAINER_PART_DIF_PICK, container);
						return ShellExceptions.CONTAINER_PART_DIF_PICK;
					}
					if(moveDocId!=null){
						WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class,moveDocId);
						if(!moveDoc.getOriginalBillType().getName().equals(originalBillNames.get(0))){
							saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_PICKING, ShellExceptions.CONTAINER_PART_DIF_PICK, container);
							return ShellExceptions.CONTAINER_PART_DIF_PICK;
						}
					}
				}
			}
		}
		bt.setIsPicking(true);
		commonDao.store(bt);
		return MyUtils.SUCCESS;
	}
	//WmsPickPartShell
	@SuppressWarnings("unchecked")
	public WmsPickContainerDTO getBindByContainerId(Long moveDetailId,WmsPickContainerDTO dto){
		//每次先推送量大的容器拣货
		String hql = "FROM WmsMoveDocAndStation w WHERE w.moveDocDetail.id =:moveDetailId"+
				" AND w.isFinished = false AND w.type =:type ORDER BY w.quantity DESC";
		List<WmsMoveDocAndStation> wds = commonDao.findByQuery(hql, 
				new String[]{"moveDetailId","type"}, new Object[]{moveDetailId,dto.getType()});
		if(wds==null || wds.size()<=0){
			dto.setErrorMes(ShellExceptions.CONTAINER_NULL_FINISH);
			saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_PICKING, dto.getErrorMes(), dto.getType());
			return dto;
		}
		dto.setWmsMoveDocAndStationId(wds.get(0).getId());
		dto.setOrderBU(wds.get(0).getAvailableQuantityBU());
		return dto;
		
	}
	public WmsVehicle checkVehicleByLicense(String license){
		String hql = "FROM WmsVehicle v WHERE v.license =:license and v.status =:status";
		WmsVehicle vehicle = (WmsVehicle) commonDao.findByQueryUniqueResult(hql, 
				new String[]{"license","status"}, new Object[]{license,BaseStatus.ENABLED});
		return vehicle;
	}
	//WmsBOLShell
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map checkMoveDocByContainer(String container,Map<String,WmsBOLDTO> dtos){
		Map result  = new HashMap();
		//未加入BOL的一定是未发运的,有了容器绑定且未加入BOL的一定是有了拣货量的,所以这种关系明细表是符合加入BOL的
		String hql = "FROM WmsTaskAndStation ws WHERE ws.container =:container" +
				" AND ws.isJoinBOL =:isJoinBOL";//允许相同标签下,部分拣货后发运
		List<WmsTaskAndStation> wts = commonDao.findByQuery(hql, 
				new String[]{"container","isJoinBOL"}, 
				new Object[]{container,Boolean.FALSE});
		Set<WmsMoveDocAndStation> mds = new HashSet<WmsMoveDocAndStation>();
		for(WmsTaskAndStation wt : wts){
			mds.add(wt.getStation());
		}
		result.put(WmsBOLShell.ERROR_MESSAGE, "");
		if(mds.isEmpty()){
			result.put(WmsBOLShell.ERROR_MESSAGE, ShellExceptions.CONTAINER_NOT_PICKING);
			saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_BOL,  ShellExceptions.CONTAINER_NOT_PICKING, container);
			
			result.put(WmsBOLShell.CURRENT_DTOS, dtos);
			return result;
		}else{
			//去掉验证 yc.min 20171031
			/*if(mds.size()>1){
				//器具型号
				String type = "";
				//判断器具标签是否相同
				Set<String> boxTagNo = new HashSet<String>();
				for(WmsMoveDocAndStation w : mds){
					boxTagNo.add(w.getBoxTag());
					type = w.getType();
				}
				if(boxTagNo.size()>1 && !MyUtils.PARTS.equals(type)){
					result.put(WmsBOLShell.ERROR_MESSAGE, ShellExceptions.CONTAINER_UNSHIP_MOVE);
					saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_BOL, ShellExceptions.CONTAINER_UNSHIP_MOVE, container);
					
					result.put(WmsBOLShell.CURRENT_DTOS, dtos);
					return result;
				}
			}*/
			hql = "FROM WmsBoxType bt WHERE bt.code =:code AND bt.status =:status ";
			WmsBoxType bt = (WmsBoxType) commonDao.findByQueryUniqueResult(hql, 
					new String[]{"code","status"}, new Object[]{container,BaseStatus.ENABLED});
			if(bt.getIsBol()){
				result.put(WmsBOLShell.ERROR_MESSAGE, ShellExceptions.CONTAINER_IN_BOL);
				saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_BOL, ShellExceptions.CONTAINER_IN_BOL, container);
				
				result.put(WmsBOLShell.CURRENT_DTOS, dtos);
				return result;
			}else{
				for(WmsMoveDocAndStation ms : mds){//支持一个器具多个类型发货单混装,生成BOL时自动拆分  yc.min 20171031
					WmsMoveDoc doc = commonDao.load(WmsMoveDoc.class, ms.getMoveDocDetail().getMoveDoc().getId());
					WmsBOLDTO dto = null;
					if(dtos.containsKey(doc.getOriginalBillCode())){
						dto = dtos.get(doc.getOriginalBillCode());
					}else{
						dto = new WmsBOLDTO();
					}
					dto.getContainers().add(container);
					dtos.put(doc.getOriginalBillCode(), dto);
					result.put(WmsBOLShell.CURRENT_DTOS, dtos);
				}
				/*WmsMoveDoc doc = commonDao.load(WmsMoveDoc.class, mds.get(0).getMoveDocDetail().getMoveDoc().getId());
				WmsBOLDTO dto = null;
				if(dtos.containsKey(doc.getOriginalBillCode())){
					dto = dtos.get(doc.getOriginalBillCode());
				}else{
					dto = new WmsBOLDTO();
				}
				dto.getContainers().add(container);
				dtos.put(doc.getOriginalBillCode(), dto);
				result.put(WmsBOLShell.CURRENT_DTOS, dtos);*/
			}
		}
		return result;
	}
	//WmsBOLOverShell
	public void createWmsBol(Map<String, WmsBOLDTO> dtos,String license){
		WmsVehicle vehicle = checkVehicleByLicense(license);
		Iterator<Entry<String, WmsBOLDTO>> iterator = dtos.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, WmsBOLDTO> entry = iterator.next();
			String pickCode = entry.getKey();
			WmsBOLDTO dto = entry.getValue();
//			createWmsBolDo(dto, vehicle, license, pickCode);
			//以任务的形式后台去跑 pickRFManager.createWmsBolDoAsyn
			Set<String> containers = dto.getContainers();
			if(containers.size()<=0){
				return;
			}
			WBols w = new WBols(pickCode, vehicle,WmsWarehouseHolder.getWmsWarehouse());
			commonDao.store(w);
			Set<WContainers> details = new HashSet<WContainers>();
			Iterator<String> it = containers.iterator();
			while(it.hasNext()){
				String container = it.next();
				WContainers c = new WContainers(w, container);
				commonDao.store(c);
				details.add(c);
			}
			w.setDetails(details);
			commonDao.store(w);
			Task task = new Task(HeadType.CREATE_BOL, 
					"wmsMasterBOLManager"+MyUtils.spiltDot+"createWmsBolDoAsyn", w.getId());
			commonDao.store(task);
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> findUnPickMove(Long moveDocId){
		String hql = " SELECT det.id,det.item.code,det.planQuantityBU - det.movedQuantityBU FROM WmsMoveDocDetail det " +
				" WHERE 1=1 AND det.planQuantityBU-det.movedQuantityBU > 0 " +
				" AND det.moveDoc.id = "+moveDocId+" ORDER BY det.id";
		List<Object[]> moveDocDetails = commonDao.findByQuery(hql);
		if(moveDocDetails==null || moveDocDetails.size()<=0){
			return null;
		}
		List<Object[]> viewObjs = new ArrayList<Object[]>();
		for(Object[] o : moveDocDetails){
			hql = "SELECT DISTINCT w.isPartPick FROM WmsMoveDocAndStation w WHERE w.moveDocDetail.id = "+Long.parseLong(o[0].toString());
			List<Boolean> list = commonDao.findByQuery(hql);
			if(list!=null && list.size()>0){
				viewObjs.add(new Object[]{
						o[0],o[1],o[2],list.get(0)?MyUtils.PARTS:MyUtils.CONTAINER
				});
			}else{
				viewObjs.add(new Object[]{
						o[0],o[1],o[2],MyUtils.PARTS
				});
			}
		}
		
		return viewObjs;
	}
	//WmsPickContainerCodeShell
	@SuppressWarnings("unchecked")
	public List<Object[]> findUnAppliance(Long moveDocId){
		String sql = "select w.type,w.type_name,count(distinct w.item_id) as counts,sum(w.quantity) as nums,w.box_tag"+ 
				" from "+WmsTables.WMS_MOVEDOC_AND_STATION+" w "+
				" left join "+WmsTables.WMS_MOVE_DOC_DETAIL+" mm on mm.id = w.movedoc_detail_id"+
				" left join "+WmsTables.WMS_MOVE_DOC+" m on m.id = mm.move_doc_id"+
				" where m.id = ? AND w.IS_FINISHED = 'N'"+
				" group by w.type,w.type_name,w.box_tag,w.CONTAINER"+
				" order by w.box_tag";
		List<Object[]> list = jdbcTemplate.queryForList(sql, new Object[]{moveDocId});
		return list;
	}
	//WmsPickContainerCodeShell
	//统计标签+容器是否已拣货
	//应该统计:还未加入装车的是否存在拣货量,因为加入过的,无需算作本次拣货判断依据,按照现有的未加入BOL的拣货量统计
	public Integer sumPickQtys(Long moveDocId,String boxTagNo,String container){
		String sql = "select sum(wts.pick_quantity_bu) as pick_quantity"+
				" from "+WmsTables.WMS_TASK_AND_STATION+" wts"+
				" left join "+WmsTables.WMS_MOVEDOC_AND_STATION+" ms on ms.id = wts.station_id"+
				" left join "+WmsTables.WMS_MOVE_DOC_DETAIL+" mm on mm.id = ms.movedoc_detail_id"+
				" left join "+WmsTables.WMS_MOVE_DOC+" m on m.id = mm.move_doc_id"+
				" where m.id = ? AND ms.box_tag = ? AND wts.container = ? and ms.is_bol = 'N' and wts.ship_status='UNSHIP'";
		int picQty = jdbcTemplate.queryForInt(sql, new Object[]{moveDocId,boxTagNo,container});
		return picQty;
	}
	//调用类:WmsPickContainerCodeShell
	@SuppressWarnings("unchecked")
	public List<Object[]> findUnApplianceItems(Long moveDocId,String boxTagNo){
		String sql = "select w.id,i.code as item_code,(w.quantity-w.pick_quantity) as quantity,w.seq,w.end_seq,mm.id as mm_id" +
				" from "+WmsTables.WMS_MOVEDOC_AND_STATION+" w "+ 
				" left join "+WmsTables.WMS_MOVE_DOC_DETAIL+" mm on mm.id = w.movedoc_detail_id"+
				" left join "+WmsTables.WMS_MOVE_DOC+" m on m.id = mm.move_doc_id" +
				" left join "+WmsTables.WMS_ITEM+" i on i.id = mm.item_id"+
				" where m.id = ? and w.box_tag = ? AND w.IS_FINISHED = 'N'"+
				" order by w.seq desc";
		List<Object[]> list = jdbcTemplate.queryForList(sql, new Object[]{moveDocId,boxTagNo});
		return list;
	}
	public void saveLogs(String logType,String logTitle,String exception,String message){
		WmsTaskManager wmsTaskManager = (WmsTaskManager) applicationContext.getBean("wmsTaskManager");
		wmsTaskManager.saveWmsJobLog(logType, logTitle, exception, message);
	}
	public void releaseContainer(String container){
		String hql = "FROM WmsBoxType bt WHERE bt.code =:code";
		WmsBoxType bt = (WmsBoxType) commonDao.findByQueryUniqueResult(hql, new String[]{"code"}, 
				new Object[]{container});
		if(bt!=null){
			bt.setIsPicking(false);
			commonDao.store(bt);
			saveLogs(WmsLogType.NOTES, WmsLogTitle.CONTAINER_RELEASE, container, container);
		}
	}
	//WmsPickBackMoveDocShell
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map checkContainerPicking(String container){
		Map result = new HashMap();
		String hql = "SELECT DISTINCT "+WmsTaskAndStation.short_name+".station.id" +
				" FROM WmsTaskAndStation "+WmsTaskAndStation.short_name+
				" WHERE "+WmsTaskAndStation.short_name+".container =:container" +
				" AND "+WmsTaskAndStation.short_name+".station.pickQuantity > 0"+
				" AND "+WmsTaskAndStation.short_name+".station.isBol =:isBol"
				;//已拣货但是未加入过装车单的
		List<Long> ids = commonDao.findByQuery(hql, 
				new String[]{"container","isBol"}, new Object[]{container,Boolean.FALSE});
		if(ids!=null && ids.size()>0){
			result.put(WmsPickBackMoveDocShell.CURRENT_MDIDS, ids);
		}else{
			result.put(WmsPickBackMoveDocShell.ERROR_MESSAGE, "提示:容器没有符合条件的已拣货信息");
		}
		return result;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map getContainerList(List<Long> ids,String container){
		Map result = new HashMap();
		List<Object[]> list = new ArrayList<Object[]>();
		for(Long id : ids){
			WmsMoveDocAndStation mds = commonDao.load(WmsMoveDocAndStation.class, id);
			WmsMoveDocDetail moveDocDetail = commonDao.load(WmsMoveDocDetail.class, mds.getMoveDocDetail().getId());
			WmsItem item = commonDao.load(WmsItem.class, moveDocDetail.getItem().getId());
			list.add(new Object[]{
					id,item.getCode(),mds.getPickQuantity()
			});
		}
		result.put(WmsPickBackMoveDocShell.CURRENT_LIST, list);
		return result;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map checkContainerList(Long id){
		Map result = new HashMap();
		result.put(WmsPickBackMoveDocShell.ERROR_MESSAGE, "");
		String hql = "FROM WmsTaskAndStation ts WHERE ts.station.id = :stationId AND ts.isJoinBOL =:isJoinBOL ";
		List<WmsTaskAndStation> wtss = commonDao.findByQuery(hql, 
				new String[]{"stationId","isJoinBOL"}, 
				new Object[]{id,Boolean.FALSE});
		List<Object[]> list = new ArrayList<Object[]>();
		if(wtss!=null && wtss.size()>1){
			Map<String,List<Object[]>> supMap = new HashMap<String, List<Object[]>>();
			List<Object[]> supList = null;
			Map<String,Double> supQty = new HashMap<String, Double>();
			Double picQty = 0D;
			String key = null;
			for(WmsTaskAndStation ts : wtss){
				WmsTask task = commonDao.load(WmsTask.class, ts.getTask().getId());
				WmsItemKey itemKey = commonDao.load(WmsItemKey.class, task.getItemKey().getId());
				WmsItem item = commonDao.load(WmsItem.class,itemKey.getItem().getId());
				WmsOrganization supplier = commonDao.load(WmsOrganization.class,itemKey.getLotInfo().getSupplier().getId());
				key = supplier.getCode();
				if(supMap.containsKey(key)){
					supList = supMap.get(key);
					picQty = supQty.get(key);
				}else{
					supList = new ArrayList<Object[]>();
					picQty = 0D;
					supList.add(new Object[]{
							item.getCode(),item.getName(),supplier.getCode()
					});
				}
				picQty += ts.getPickQuantityBu();
				supMap.put(key, supList);
				supQty.put(key, picQty);
			}
			Iterator<Entry<String, List<Object[]>>> iter = supMap.entrySet().iterator();
			while(iter.hasNext()){
				Entry<String, List<Object[]>> entry = iter.next();
				List<Object[]> ll = entry.getValue();
				for(Object[] o : ll){
					list.add(new Object[]{//itemcode,itemname,supcode,qty
							o[0],o[1],o[2],supQty.get(entry.getKey())
					});
				}
			}
		}else if(wtss==null || wtss.size()<=0){
			WmsMoveDocAndStation mds = commonDao.load(WmsMoveDocAndStation.class, id);
			saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_PIC_BACK, ShellExceptions.PICK_BACK_IS_NULL, mds.getBoxTag());
			result.put(WmsPickBackMoveDocShell.ERROR_MESSAGE, ShellExceptions.PICK_BACK_IS_NULL);
		}else{
			WmsTask task = commonDao.load(WmsTask.class, wtss.get(0).getTask().getId());
			WmsItemKey itemKey = commonDao.load(WmsItemKey.class, task.getItemKey().getId());
			WmsItem item = commonDao.load(WmsItem.class,itemKey.getItem().getId());
			WmsOrganization supplier = commonDao.load(WmsOrganization.class,itemKey.getLotInfo().getSupplier().getId());
			list.add(new Object[]{//itemcode,itemname,supcode,qty
					item.getCode(),item.getName(),supplier.getCode(),wtss.get(0).getPickQuantityBu()
			});
		}
		result.put(WmsPickBackMoveDocShell.CURRENT_LIST, list);
		return result;
		
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map containerPickBack(Long id,String supCode,Double pickBackQty,String descLoc){
		Map result = new HashMap();
		result.put(WmsPickBackMoveDocShell.ERROR_MESSAGE, "");
		String hql = "FROM WmsLocation loc WHERE loc.code =:code AND loc.status =:status AND loc.type =:type";
		WmsLocation toLocation = (WmsLocation) commonDao.findByQueryUniqueResult(hql, 
				new String[]{"code","status","type"}, new Object[]{descLoc,BaseStatus.ENABLED,WmsLocationType.STORAGE});
		if(toLocation==null){
			result.put(WmsPickBackMoveDocShell.ERROR_MESSAGE, "错误:存货库位信息有误");
		}else{
			hql = "FROM WmsTaskAndStation ts WHERE ts.station.id = :stationId AND ts.isJoinBOL =:isJoinBOL" +
					" ORDER BY ts.pickQuantityBu";
			List<WmsTaskAndStation> wtss = commonDao.findByQuery(hql, 
					new String[]{"stationId","isJoinBOL"},new Object[]{id,Boolean.FALSE});
			Double quantityBU = 0D,backQty = 0D;
			for(WmsTaskAndStation ts : wtss){
				if(pickBackQty<=0){
					break;
				}
				WmsTask task = commonDao.load(WmsTask.class, ts.getTask().getId());
				WmsItemKey itemKey = commonDao.load(WmsItemKey.class, task.getItemKey().getId());
				WmsOrganization supplier = commonDao.load(WmsOrganization.class,itemKey.getLotInfo().getSupplier().getId());
				if(!supCode.equals(supplier.getCode())){
					continue;
				}
				quantityBU = ts.getPickQuantityBu()>=pickBackQty?pickBackQty:ts.getPickQuantityBu();
				workDocManager.unPickConfirm(ts, supCode, quantityBU, toLocation.getId());
				ts.cancelPickedQuantityBU(quantityBU);
				if(ts.getPickQuantityBu()<=0){
					ts.setStation(null);
					commonDao.delete(ts);
				}else{
					commonDao.store(ts);
				}
				backQty += quantityBU;
				pickBackQty -= quantityBU;
				
				if(task.getPlanQuantityBU()<=0){
					task.removeMove();
					commonDao.store(task);
				}
			}
			WmsMoveDocAndStation mds = commonDao.load(WmsMoveDocAndStation.class, id);
			mds.cancelPickQuantity(backQty);
			commonDao.store(mds);
			saveLogs(WmsLogType.NOTES, WmsLogTitle.CONTAINER_PIC_BACK, mds.getBoxTag(), descLoc+"->"+backQty+"->"+supCode);
		}
		return result;
	}
	
	public Map<String,String> findMove(String pickNo){
		Map<String,String> result = new HashMap<String, String>();
		result.put(WmsScanPickShell.ERROR_MESG, "");
		
		String hql = "FROM WmsMoveDoc moveDoc WHERE moveDoc.code =:pickNo";
		WmsMoveDoc moveDoc = (WmsMoveDoc) commonDao.findByQueryUniqueResult(hql, "pickNo", pickNo);
		if(moveDoc==null){
			result.put(WmsScanPickShell.ERROR_MESG, "失败!拣货单不存在");
		}else if(moveDoc.getStatus().equals(WmsMoveDocStatus.FINISHED)){
			result.put(WmsScanPickShell.ERROR_MESG, "失败!拣货单已完成");
		}else if(moveDoc.getStatus().equals(WmsMoveDocStatus.OPEN)){
			moveDoc.setReserveBeginTime(new Date());//备料单扫描时间
			moveDoc.setDriver(UserHolder.getUser().getName());//备料单扫描人
			moveDoc.setStatus(WmsMoveDocStatus.ACTIVE);
			commonDao.store(moveDoc);
		}
		return result;
	}
	
//	public Map<String,String> findContainer(String container){
//		Map<String,String> result = new HashMap<String, String>();
//		result.put(WmsScanPickShell.ERROR_MESG, "");
//		
//		String hql = "FROM WmsBoxType bt WHERE bt.code =:code AND bt.status =:status";
//		WmsBoxType bt = (WmsBoxType) commonDao.findByQueryUniqueResult(hql, new String[]{"code","status"}, 
//				new Object[]{container,BaseStatus.ENABLED});
//		if(bt == null){
//			result.put(WmsScanPickShell.ERROR_MESG, "失败!容器不存在或失效");
//		}
//		return result;
//	}
	
//	public Map<String,String> findLoc(String location){
//		Map<String,String> result = new HashMap<String, String>();
//		result.put(WmsScanPickShell.ERROR_MESG, "");
//		WmsPutawayRFManager wmsPutawayRFManager = (WmsPutawayRFManager) applicationContext.getBean("wmsPutawayRFManager");
//		Long locId = wmsPutawayRFManager.findLoc(location);
//		if(locId==null || locId == 0L){
//			result.put(WmsScanPickShell.ERROR_MESG, "失败!库位号不存在");
//		}
//		return result;
//	}
	
//	public Map<String,String> findMoveDetail(String pickNo,String itemCode){
//		Map<String,String> result = new HashMap<String, String>();
//		result.put(WmsScanPickShell.ERROR_MESG, "");
//		String hql = "FROM WmsMoveDocDetail mm WHERE mm.moveDoc.code =:pickNo AND mm.item.code =:itemCode";
//		WmsMoveDocDetail mm = (WmsMoveDocDetail) commonDao.findByQueryUniqueResult(hql, 
//				new String[]{"pickNo","itemCode"}, new Object[]{pickNo,itemCode});
//		if(mm==null){
//			result.put(WmsScanPickShell.ERROR_MESG, "失败!物料明细不存在");
//		}else if(mm.unMoveQty()<=0){
//			result.put(WmsScanPickShell.ERROR_MESG, "失败!物料拣货完成");
//		}
//		return result;
//	}
	
	@SuppressWarnings("unchecked")
	public Map<String,String> singlePicQty(String pickNo,String container,String fromLocationCode
			,String itemCode,Double picQuantity,String description,Boolean checkBox){
		Map<String,String> result = new HashMap<String, String>();
		result.put(WmsScanPickShell.ERROR_MESG, "");
		String hql = "FROM WmsMoveDocDetail mm WHERE mm.moveDoc.code =:pickNo AND mm.item.code =:itemCode";
		WmsMoveDocDetail wdd = (WmsMoveDocDetail) commonDao.findByQueryUniqueResult(hql, 
				new String[]{"pickNo","itemCode"}, new Object[]{pickNo,itemCode});
		if(wdd==null){
			hql = "FROM WmsMoveDocDetail mm WHERE mm.moveDoc.pickTicket.relatedBill1 =:pickNo AND mm.item.code =:itemCode";
			wdd = (WmsMoveDocDetail) commonDao.findByQueryUniqueResult(hql, 
					new String[]{"pickNo","itemCode"}, new Object[]{pickNo,itemCode});
		}
		if(wdd==null){
			result.put(WmsScanPickShell.ERROR_MESG, WmsScanPickShell.ERROR_ITEM_NULL);
		}else if(wdd.unMoveQty()<=0){
			result.put(WmsScanPickShell.ERROR_MESG, WmsScanPickShell.ERROR_ITEM_FINISHED);
		}else{
			hql = "SELECT SUM(inventory.quantityBU - inventory.allocatedQuantityBU)" +
					" FROM WmsInventory inventory" +
					" WHERE inventory.location.code =:location AND inventory.lockLot = false" +
					" AND inventory.itemKey.item.code =:itemCode";
			Double qq = (Double) commonDao.findByQueryUniqueResult(hql, 
					new String[]{"location","itemCode"}, new Object[]{fromLocationCode,itemCode});
			qq  = qq==null?0D:qq;
			if(picQuantity == Double.MAX_VALUE){
				picQuantity = wdd.unMoveQty();
			}
			if(picQuantity>wdd.unMoveQty()){
				result.put(WmsScanPickShell.ERROR_MESG, WmsScanPickShell.ERROR_PIC_MOVE);
			}else if(qq<picQuantity){
				result.put(WmsScanPickShell.ERROR_MESG, WmsScanPickShell.ERROR_PIC_MOVE_INV);
			}else{
				hql = "FROM WmsBoxType bt WHERE bt.code =:code AND bt.status =:status";
				WmsBoxType bt = (WmsBoxType) commonDao.findByQueryUniqueResult(hql, new String[]{"code","status"}, 
						new Object[]{container,BaseStatus.ENABLED});
				if(bt == null && checkBox){
					result.put(WmsScanPickShell.ERROR_MESG, WmsScanPickShell.ERROR_CONTAINER_NULL);
				}else{
					WmsPutawayRFManager wmsPutawayRFManager = (WmsPutawayRFManager) applicationContext.getBean("wmsPutawayRFManager");
					WmsTransactionalManager transactionalManager = (WmsTransactionalManager) applicationContext.getBean("wmsTransactionalManager");
					Long fromLocationId = wmsPutawayRFManager.findLoc(fromLocationCode);
					if(fromLocationId==null || fromLocationId == 0L){
						result.put(WmsScanPickShell.ERROR_MESG, WmsScanPickShell.ERROR_LOC_NULL);
					}else{
						//产生task,直接为可发运数据,原库位和目标库位(先找,再判断是否新建)同时做相应的减法和加法
						hql = "FROM WmsInventory inventory" +
							  " WHERE inventory.location.code =:location AND inventory.lockLot = false" +
							  " AND inventory.itemKey.item.code =:itemCode" +// AND supplier.code =:supplierCode
							  " AND (inventory.quantityBU - inventory.allocatedQuantityBU) > 0";
						List<WmsInventory> srcInvs = commonDao.findByQuery(hql, 
								new String[]{"location","itemCode"}, new Object[]{fromLocationCode,itemCode});
						if(srcInvs==null || srcInvs.size()<=0){
							result.put(WmsScanPickShell.ERROR_MESG, WmsScanPickShell.ERROR_LOSS);
						}else{
							WmsLocation toLocation = (WmsLocation) commonDao.findByQueryUniqueResult("FROM WmsLocation l WHERE l.type =:type" +
									" AND l.status = 'ENABLED' AND l.warehouse.id =:warehouse",
									new String[]{"type","warehouse"},
									new Object[]{WmsLocationType.SHIP,WmsWarehouseHolder.getWmsWarehouse().getId()});
							if(toLocation==null){
								result.put(WmsScanPickShell.ERROR_MESG, WmsScanPickShell.ERROR_SHIP_LOC_NULL);
							}else{
								wdd.setFromLocationId(fromLocationId);
								wdd.setFromLocationCode(fromLocationCode);
								commonDao.store(wdd);
								
								WmsTaskManager wmsTaskManager = (WmsTaskManager) applicationContext.getBean("wmsTaskManager");
								picQuantity = moveInv(srcInvs, wdd, wmsTaskManager, picQuantity, toLocation, fromLocationId, 
										fromLocationCode, transactionalManager,true,container,description);
								if(picQuantity>0){
									moveInv(srcInvs, wdd, wmsTaskManager, picQuantity, toLocation, fromLocationId, 
											fromLocationCode, transactionalManager,false,container,description);
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private Double moveInv(List<WmsInventory> srcInvs,WmsMoveDocDetail wdd,WmsTaskManager wmsTaskManager
			,Double picQuantity,WmsLocation toLocation,Long fromLocationId,String fromLocationCode,
			WmsTransactionalManager transactionalManager,Boolean isA,String container,String description){
		Double availableQuantityBU = 0D,moveQty = 0D,ixQty = 0D,picQty = 0D;
		for(WmsInventory srcInv : srcInvs){
			if(srcInv.getItemKey().getLotInfo().getSupplier().getCode()
					.equals(wdd.getShipLotInfo().getSupplier()) && isA){//优先扣减需求供应商库存
				availableQuantityBU = srcInv.getAvailableQuantityBU();
				moveQty = availableQuantityBU<=picQuantity?availableQuantityBU:picQuantity;
				String hql = "FROM WmsInventoryExtend ix WHERE ix.inventory.id =:inventoryId";
				List<WmsInventoryExtend> ixs = commonDao.findByQuery(hql, "inventoryId", srcInv.getId());
				if(ixs!=null && ixs.size()>0){
					for(WmsInventoryExtend ix : ixs){
						wdd.setSrcInvExId(ix.getId());
						commonDao.store(wdd);
						ixQty = ix.getQuantityBU();
						picQty = ixQty<=moveQty?ixQty:moveQty;//实际可减量
						ix.removeQuantity(picQty);
						commonDao.store(ix);
						if(ix.getQuantityBU()<=0){
							commonDao.delete(ix);
						}
						srcInv.removeQuantityBU(picQty);
						commonDao.store(srcInv);
						newTask(wmsTaskManager, wdd, toLocation, srcInv, picQty, fromLocationId, 
								fromLocationCode,transactionalManager,container);
						inventoryLog(wdd, srcInv, toLocation, picQty,description);
						picQuantity -= picQty;
						if(picQuantity<=0){
							break;
						}
					}
				}else{//没有子表也继续扣减
					srcInv.removeQuantityBU(moveQty);
					commonDao.store(srcInv);
					newTask(wmsTaskManager, wdd, toLocation, srcInv, moveQty, fromLocationId, 
							fromLocationCode,transactionalManager,container);
					inventoryLog(wdd, srcInv, toLocation, moveQty,description);
					picQuantity -= moveQty;
					if(picQuantity<=0){
						break;
					}
				}
				
			}
		}
		return picQuantity;
	}
	
	private WmsTask newTask(WmsTaskManager wmsTaskManager,WmsMoveDocDetail wdd,WmsLocation toLocation
			,WmsInventory srcInv,double quantity,Long fromLocationId,String fromLocationCode,
			WmsTransactionalManager transactionalManager,String container){
		WmsInventory dstInventory = wmsInventoryManager.getInventoryWithNew(
				toLocation, srcInv.getItemKey(), srcInv.getPackageUnit(), srcInv.getStatus());
		dstInventory.addQuantityBU(quantity); 
		commonDao.store(dstInventory);
		
		WmsTask task = wmsTaskManager.createWmsTask(wdd, srcInv.getItemKey(), srcInv.getStatus(),quantity,container);
		task.setSrcInventoryId(srcInv.getId());
		task.setFromLocationId(fromLocationId);
		task.setFromLocationCode(fromLocationCode);
		task.setDescInventoryId(dstInventory.getId());
		task.setToLocationId(toLocation.getId());
		task.setToLocationCode(toLocation.getCode());
		task.editMovedQuantityBU(quantity);
		commonDao.store(task);
		
		wdd.move(quantity);
		transactionalManager.updatePickTicketMovedQuantity(wdd,quantity);
		workflowManager.doWorkflow(wdd.getMoveDoc(), "wmsMoveDocProcess.confirm");
		return task;
	}
	private void inventoryLog(WmsMoveDocDetail wdd,WmsInventory srcInv,WmsLocation toLocation
			,double quantity,String description){
		wmsInventoryManager.addInventoryLog(WmsInventoryLogType.MOVE, -1,wdd.getMoveDoc().getCode(),
				wdd.getMoveDoc().getBillType(), srcInv.getLocation(), srcInv.getItemKey(),
				quantity, srcInv.getPackageUnit(),srcInv.getStatus(),description);
		
		wmsInventoryManager.addInventoryLog(WmsInventoryLogType.MOVE, 1,wdd.getMoveDoc().getCode(),
				wdd.getMoveDoc().getBillType(), toLocation, srcInv.getItemKey(),
				quantity, srcInv.getPackageUnit(),srcInv.getStatus(),description);
	}
	@SuppressWarnings("unchecked")
	public void spsPicking(Long id){
		//系统先根据时序明细匹配库存目前的所在库位/库位可用量,用时序明细的总需求量做减法,得到预扣减信息<物料-库位-拣货量>
		WBols w = commonDao.load(WBols.class, id);
		Set<WContainers> details = w.getDetails();
		String hql = "SELECT appl.id FROM WmsPickTicketAndAppliance appl WHERE appl.sheetNo =:sheetNo" +
				" AND appl.no =:boxTag";//AND (appl.qty-appl.pickQty)>0 这块后面再详细设计
		String itemCode = "";
		Map<String,Double> itemQum = new HashMap<String, Double>();
		Double qty = 0D;
		for(WContainers c : details){
			List<Long> appls = commonDao.findByQuery(hql, new String[]{"sheetNo","boxTag"}, 
					new Object[]{w.getPickCode(),c.getContainer()});
			if(appls!=null && appls.size()>0){
				for(Long applid : appls){
					WmsPickTicketAndAppliance appl = commonDao.load(WmsPickTicketAndAppliance.class, applid);
					itemCode = appl.getPartNo();
					if(itemQum.containsKey(itemCode)){
						qty = itemQum.get(itemCode);
					}else{
						qty = 0D;
					}
					qty += appl.getAvailableQuantityBU();
					itemQum.put(itemCode, qty);
				}
				//依次匹配库存并决定每个库存的扣减量
				Map<String,Map<String,Double>> itemMap = new HashMap<String, Map<String,Double>>();
				Map<String,Double> locMap = new HashMap<String, Double>();//拣选库位,拣选量
				String locationCode = "";
				Double invQty = 0D,availableQty = 0D,locQty = 0D;
				hql = "SELECT inventory.location.code,(inventory.quantityBU-inventory.allocatedQuantityBU)" +
						  " FROM WmsInventory inventory" +
						  " WHERE inventory.lockLot = false" +
						  " AND inventory.itemKey.item.code =:itemCode" +// AND supplier.code =:supplierCode
						  " AND (inventory.quantityBU - inventory.allocatedQuantityBU) > 0";
				Iterator<Entry<String, Double>> ii = itemQum.entrySet().iterator();
				while(ii.hasNext()){
					Entry<String, Double> entry = ii.next();
					itemCode = entry.getKey();
					qty = entry.getValue();
					List<Object[]> srcInvs = commonDao.findByQuery(hql, 
							new String[]{"itemCode"}, new Object[]{itemCode});
					if(srcInvs!=null && srcInvs.size()>0){
						for(Object[] obj : srcInvs){
							locationCode = obj[0].toString();
							invQty = Double.valueOf(obj[1].toString());
							availableQty = qty>=invQty?invQty:qty;//得到本次库位可扣减量
							invQty -= availableQty;//库位剩余量
							qty -= availableQty;//物料剩余待拣量
							if(locMap.containsKey(locationCode)){
								locQty = locMap.get(locationCode);
							}else{
								locQty = 0D;
							}
							locQty += availableQty;//累加当前库位的可拣货量
							locMap.put(locationCode, locQty);
							if(qty<=0){
								break;//跳出,继续下一个料的拣货
							}
						}
						itemMap.put(itemCode, locMap);
					}
				}itemQum.clear();
				//依次拣货
				String messge = "";
				locMap = new HashMap<String, Double>();//拣选库位,拣选量
				Iterator<Entry<String, Map<String, Double>>> iii = itemMap.entrySet().iterator();
				while(iii.hasNext()){
					Entry<String, Map<String, Double>>  entry = iii.next();
					itemCode = entry.getKey();
					locMap = entry.getValue();
					Iterator<Entry<String, Double>> i = locMap.entrySet().iterator();
					while(i.hasNext()){
						Entry<String, Double> e = i.next();
						locationCode = e.getKey();
						locQty = e.getValue();
						Map<String,String> result = this.singlePicQty(w.getPickCode(),c.getContainer(), 
								locationCode,itemCode,locQty,"时序任务拣货:"+c.getContainer(),false);
						
						messge = result.get(WmsScanPickShell.ERROR_MESG);
						if(!StringUtils.isEmpty(messge)){
							c.setDescrption(messge);
							commonDao.store(c);
						}
					}
				}
			}
		}
	}
}
