package com.vtradex.wms.server.service.shipping.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vtradex.thorn.client.ui.page.IPage;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.message.Task;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.carrier.WmsVehicle;
import com.vtradex.wms.server.model.interfaces.HeadType;
import com.vtradex.wms.server.model.interfaces.WBols;
import com.vtradex.wms.server.model.interfaces.WContainers;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocType;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.shipping.WmsBOL;
import com.vtradex.wms.server.model.shipping.WmsBOLDetail;
import com.vtradex.wms.server.model.shipping.WmsBOLStatus;
import com.vtradex.wms.server.model.shipping.WmsBolDetailExtend;
import com.vtradex.wms.server.model.shipping.WmsMasterBOL;
import com.vtradex.wms.server.model.shipping.WmsMoveDocAndStation;
import com.vtradex.wms.server.model.shipping.WmsPickTicket;
import com.vtradex.wms.server.model.shipping.WmsPickTicketDetail;
import com.vtradex.wms.server.model.shipping.WmsTaskAndStation;
import com.vtradex.wms.server.model.warehouse.WmsBoxType;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.service.interfaces.WmsDealInterfaceDataManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.shipping.WmsMasterBOLManager;
import com.vtradex.wms.server.service.workDoc.WmsWorkDocManager;
import com.vtradex.wms.server.telnet.dto.WmsBOLDTO;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

@SuppressWarnings("unchecked")
public class DefaultWmsMasterBOLManager extends DefaultBaseManager implements
		WmsMasterBOLManager {
	
	private WmsBussinessCodeManager codeManager;
	private WorkflowManager workflowManager;
	private WmsWorkDocManager workDocManager;
	private WmsDealInterfaceDataManager wmsDealInterfaceDataManager;
	
	public DefaultWmsMasterBOLManager(WmsBussinessCodeManager codeManager ,
			WorkflowManager workflowManager,WmsWorkDocManager workDocManager,
			WmsDealInterfaceDataManager wmsDealInterfaceDataManager){
		this.codeManager = codeManager;
		this.workflowManager = workflowManager;
		this.workDocManager = workDocManager;
		this.wmsDealInterfaceDataManager = wmsDealInterfaceDataManager;
	}
	//wmsMasterBOLManager.invokeMethod
	public void invokeMethod(){
		this.scanSubBol("WMS_1767001");
	}
	public void storeMasterBOL(WmsBOL bol) {
		bol.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
		bol.setCode(codeManager.generateCodeByRule(bol.getWarehouse(), 
				bol.getWarehouse().getName(), "装车单", ""));
		workflowManager.doWorkflow(bol, "masterBOLProcess.new");
		commonDao.store(bol);
	}

	public void deleteMasterBOL(WmsMasterBOL masterBOL) {
		String hql = "FROM WmsMoveDoc moveDoc WHERE moveDoc.masterBOL.id=:masterBOLId";
		List<WmsMoveDoc> moveDocs = commonDao.findByQuery(hql, 
			new String[] {"masterBOLId"}, new Object[] {masterBOL.getId()});
		for(WmsMoveDoc moveDoc : moveDocs) {
			moveDoc.setMasterBOL(null);
			if(moveDoc.getCarrier()!= null && moveDoc.getCarrier().equals(masterBOL.getCarrier())) {
				moveDoc.setCarrier(null);
			}
			if(moveDoc.getDriver()!= null && moveDoc.getDriver().equals(masterBOL.getDriver())) {
				moveDoc.setDriver(null);
			}
			if(moveDoc.getVehicleNo()!= null && moveDoc.getVehicleNo().equals(masterBOL.getVehicleNo())) {
				moveDoc.setVehicleNo(null);
			}
			commonDao.store(moveDoc);
		}
		commonDao.delete(masterBOL);
	}

	public void deleteMasterBOLDetail(WmsMoveDoc moveDoc) {
		WmsMasterBOL masterBOL = moveDoc.getMasterBOL();
		
//		Double scatteredQuantity = masterBOL.getScatteredQuantity();
//		Double packageQuantity = masterBOL.getPackageQuantity();
//		Double volume = masterBOL.getVolume();
//		Double weight = masterBOL.getWeight();
//		
//		scatteredQuantity = scatteredQuantity - bol.getScatteredQuantity();
//		packageQuantity = packageQuantity - bol.getBoxQuantity();
//		volume = volume - bol.getVolume();
//		weight = weight - bol.getWeight();
		moveDoc.setCarrier(null);
		moveDoc.setDriver(null);
		moveDoc.setVehicleNo(null);
		moveDoc.setMasterBOL(null);
//		
//		masterBOL.setScatteredQuantity(scatteredQuantity);
//		masterBOL.setPackageQuantity(packageQuantity);
//		masterBOL.setVolume(volume);
//		masterBOL.setWeight(weight);
		commonDao.store(masterBOL);
	}

	public void joinMasterBOLDetail(Long masterBOLId,WmsMoveDoc moveDoc) {
		WmsMasterBOL masterBOL = commonDao.load(WmsMasterBOL.class, masterBOLId);
//
//		Double scatteredQuantity = 0D;
//		Double packageQuantity = 0D;
//		Double volume = 0D;
//		Double weight = 0D;
//		packageQuantity += bol.getBoxQuantity();
//		scatteredQuantity += bol.getScatteredQuantity();
//		volume += bol.getVolume();
//		weight += bol.getWeight();
		moveDoc.setCarrier(masterBOL.getCarrier());
		moveDoc.setDriver(masterBOL.getDriver());
		moveDoc.setVehicleNo(masterBOL.getVehicleNo());
		moveDoc.setLineNo(getMaxLineNoByMasterBOL(masterBOL));
		moveDoc.setMasterBOL(masterBOL);
//
//		masterBOL.setScatteredQuantity(scatteredQuantity);
//		masterBOL.setPackageQuantity(packageQuantity);
//		masterBOL.setVolume(volume);
//		masterBOL.setWeight(weight);
		commonDao.store(masterBOL);
	}

	public void reservationDock(WmsMasterBOL masterBOL) {
		String hql = "from WmsMoveDoc doc where doc.masterBOL.id = :masterId";
		List<WmsMoveDoc> lists = this.commonDao.findByQuery(hql, new String[]{"masterId"}, new Object[]{masterBOL.getId()});
		if(lists.size()>0){
			for(WmsMoveDoc moveDoc:lists){
				//发货预约
			}
		}
	}
	
	public Integer getMaxLineNoByMasterBOL(WmsMasterBOL masterBOL ) {				
		Integer lineNo = (Integer) commonDao.findByQueryUniqueResult("SELECT MAX(moveDoc.lineNo) FROM WmsMoveDoc moveDoc WHERE moveDoc.masterBOL.id = :masterBOLId", 		
				new String[] {"masterBOLId"}, new Object[] {masterBOL.getId()});
		if (lineNo == null || lineNo.intValue() == 0) {		
			lineNo = 10;	
		} else {		
			lineNo += 10;	
		}	
		return lineNo;
	}

	public void adjustMasterBOLDetail(WmsMoveDoc bol, List<String> inputValue) {
		Integer lineNo;
		try {
			lineNo = Integer.valueOf(inputValue.get(0));
		} catch (NumberFormatException nfe) {
			throw new BusinessException("行号只能为数值类型！");
		}
		bol.setLineNo(lineNo);
	}
	
	public Boolean beRequirePopupPage(Map map) {
		List<Long> idsList = (List<Long>) map.get("ids");
		Long count = (Long)commonDao.findByQueryUniqueResult("SELECT count ( DISTINCT moveDoc.pickTicket.code) FROM WmsMoveDoc moveDoc WHERE moveDoc.id in (:idsList)", 
				new String[] {"idsList"}, new Object[] {idsList});
		if(count > 1) {
			throw new BusinessException("请选择同一发货单下的BOL");
//			LocalizedMessage.addMessage("请选择同一发货单下的BOL");
//			return false;
		}
		return true;
	}
	
	public void removeBOLDetail(WmsBOLDetail bolDetail){
		WmsBOL bol = bolDetail.getBol();
		WmsTask task = commonDao.load(WmsTask.class, bolDetail.getTask().getId());
		String hql = "FROM WmsTaskAndStation wt WHERE wt.task.id =:taskId ";
		List<WmsTaskAndStation> wts = commonDao.findByQuery(hql, new String[]{"taskId"}, new Object[]{task.getId()});
		for(WmsTaskAndStation wt : wts){
			wt.setIsJoinBOL(Boolean.FALSE);
			commonDao.store(wt);
		}
		bol.removeDetail(bolDetail);
		bol.refreshQuantity();
		commonDao.delete(bolDetail);
		commonDao.store(bol);
	}
	public void shippingWmsBOL(WmsBOL bol){
//		System.out.println("01::"+JavaTools.format(new Date(), JavaTools.dmy_hms));
		workDocManager.upBolTagsNum(bol);
//		System.out.println("02::"+JavaTools.format(new Date(), JavaTools.dmy_hms));
		String hql = "FROM WmsBOLDetail bd WHERE bd.bol.id =:bolId ";
		List<WmsBOLDetail> details = commonDao.findByQuery(hql, "bolId", bol.getId());
		int i = 1; //数据条数标识
		List<String> boxTags = new ArrayList<String>();
		List<String> containers = new ArrayList<String>();
		List<Long> wmsTaskAndStationIds = new ArrayList<Long>();
		//统计相同子单号下相同物料的配送总量
		Map<String,Double> itemSubQtys = new HashMap<String, Double>();
		Double itemSubQty = 0D;
		String key = "",billCode = "";
		for(WmsBOLDetail detail : details){
			WmsTask task = commonDao.load(WmsTask.class, detail.getTask().getId());
			WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, task.getMoveDocDetail().getMoveDoc().getId());
			WmsPickTicket pickTicket = commonDao.load(WmsPickTicket.class, moveDoc.getPickTicket().getId());
//			System.out.println("03::"+JavaTools.format(new Date(), JavaTools.dmy_hms));
			workDocManager.pickShipByTask(task, detail.getQuantityBU(), moveDoc);
//			System.out.println("04::"+JavaTools.format(new Date(), JavaTools.dmy_hms));
			
			billCode= pickTicket.getBillType().getCode();
			
			if("PLAN_PICKING".equals(billCode) || 
					"SPS_PICKING".equals(billCode) || 
						"NORMOL_PICKING".equals(billCode) ||
							"BL_PICKING".equals(billCode) ||
							 "KB_PICKING".equals(billCode)//紧急补料出货单
							 ){
				/**时序件、看板件、计划件出库数据传MES*/
				wmsDealInterfaceDataManager.outBoundToMes(detail,moveDoc,i,task,billCode);
			}else if(WmsMoveDocType.LOT_PICKING.equals(billCode)){//批拣单后台任务执行
				//下面统一任务执行数据...
			}else{
				/**出库数据传ERP*/
				wmsDealInterfaceDataManager.outBoundToErp(detail,moveDoc,i,task,billCode);
			}
			i += 1;
			
			if(detail.getWmsTaskAndStationId()!=null){
				wmsTaskAndStationIds.add(detail.getWmsTaskAndStationId());
			}
			
			boxTags.add(detail.getBoxTag());
			containers.add(detail.getContainer());
//			System.out.println("05::"+JavaTools.format(new Date(), JavaTools.dmy_hms));
			
			key = detail.getSubCode()+detail.getItemKey().getItem().getId();
			if(itemSubQtys.containsKey(key)){
				itemSubQty = itemSubQtys.get(key);
			}else{
				itemSubQty = 0D;
			}
			itemSubQty += detail.getQuantityBU();
			itemSubQtys.put(key, itemSubQty);
			detail.setItemSubQty(itemSubQtys.get(key));
			commonDao.store(detail);
		}itemSubQtys.clear();
//		System.out.println("06::"+JavaTools.format(new Date(), JavaTools.dmy_hms));
		workDocManager.upBolTagsShip(bol,boxTags,containers,wmsTaskAndStationIds);
//		System.out.println("07::"+JavaTools.format(new Date(), JavaTools.dmy_hms));
		bol.setShipTime(new Date());
		commonDao.store(bol);
//		System.out.println("08::"+JavaTools.format(new Date(), JavaTools.dmy_hms));
		if(WmsMoveDocType.LOT_PICKING.equals(billCode)){//一种BOL只允许装同一类型的发货单,所以理论讲这里应该都相同
			Task t = new Task(HeadType.W_DELIVER_MES, 
			"wmsDealInterfaceDataManager"+MyUtils.spiltDot+"outBoundLotToMes", bol.getId());
			commonDao.store(t);
		}
	}
	
	public void deleteWmsBOL(WmsBOL bol){
		if(bol.getDetails().size()>0){
			throw new BusinessException("该装车单存在明细不能删除");
		}
		workflowManager.doWorkflow(bol, "masterBOLProcess.delete");
		commonDao.store(bol);
	}
	//wmsMasterBOLManager.scanSubBol
	public void scanSubBol(String subCode){
//		System.out.println(subCode);
		List<WmsBOLDetail> dels = commonDao.findByQuery("FROM WmsBOLDetail bolDetail WHERE bolDetail.subCode =:subCode" +
				" AND bolDetail.bol.status =:status AND bolDetail.beReturn =:beReturn", 
				new String[]{"subCode","status","beReturn"}, new Object[]{subCode.trim(),WmsBOLStatus.SHIPPED,Boolean.FALSE});
		if(dels==null || dels.size()<=0){
			LocalizedMessage.setMessage(MyUtils.font("失败:子单号未找到符合条件的数据:"+subCode.trim()));
		}else{
			for(WmsBOLDetail bolDetail : dels){
				bolDetail.setBeReturn(Boolean.TRUE);
				commonDao.store(bolDetail);
			}
		}
		
	}
	public void createWmsBolDoAsyn(Long id){
		WBols w = commonDao.load(WBols.class, id);
		Set<WContainers> details = w.getDetails();
		
		Map<String,WmsBOLDTO> dtos = new HashMap<String, WmsBOLDTO>();
		WmsBOLDTO dto = new WmsBOLDTO();
		for(WContainers c : details){
			dto.getContainers().add(c.getContainer());
			dtos.put(w.getPickCode(), dto);
		}
		createWmsBolDo(dto, w.getVehicle(), w.getVehicle().getLicense(), w.getPickCode(),w.getWarehouse());
	}
	private void createWmsBolDo(WmsBOLDTO dto,WmsVehicle vehicle,String license,String pickCode,WmsWarehouse warehouse){
		Set<String> containers = dto.getContainers();
		if(containers.size()<=0){
			return;
		}
		//创建装车单
		WmsBOL bol = EntityFactory.getEntity(WmsBOL.class);
		bol.setWarehouse(warehouse);
		bol.setCode(codeManager.generateCodeByRule(bol.getWarehouse(), 
				bol.getWarehouse().getName(), "装车单", ""));
		bol.setVehicle(vehicle);
		bol.setVehicleNo(license);
		bol.setWmsDriver(vehicle.getMasterDriver());
		bol.setPickCode(pickCode);
		String hql = "FROM WmsPickTicket pick WHERE pick.code=:code ";
		WmsPickTicket pick = (WmsPickTicket) commonDao.findByQueryUniqueResult(hql, "code", pickCode);
		bol.setBillTypeName(pick.getBillType().getName());
		bol.setRequireArriveDate(pick.getRequireArriveDate());
//		workflowManager.doWorkflow(bol, "masterBOLProcess.new");
		bol.setStatus(WmsBOLStatus.UNSHIP);
		commonDao.store(bol);
		
		Iterator<String> it = containers.iterator();
		Map<String,String> subMap = new HashMap<String, String>();
		while(it.hasNext()){
			String container = it.next();
			
			hql = "FROM WmsTaskAndStation ws WHERE ws.station.moveDocDetail.moveDoc.originalBillCode =:originalBillCode" +
					" AND ws.container =:container AND ws.isJoinBOL =:isJoinBOL";
			List<WmsTaskAndStation> wtss = commonDao.findByQuery(hql, 
					new String[]{"originalBillCode","container","isJoinBOL"}, 
					new Object[]{pickCode,container,Boolean.FALSE});
			
			Set<WmsMoveDocAndStation> mds = new HashSet<WmsMoveDocAndStation>();
			for(WmsTaskAndStation wt : wtss){
				mds.add(wt.getStation());
			}
			if(mds.size() <= 0){
				throw new BusinessException("未找到符合要求的器具!!");//(拣货单状态=作业中或完成,发运状态=未发运,器具已经完成并未加入装车单)
			}
			//加入装车单明细
			Integer detailLineNo = null;
			for(WmsMoveDocAndStation wmds : mds){
				WmsMoveDoc doc = commonDao.load(WmsMoveDoc.class, wmds.getMoveDocDetail().getMoveDoc().getId());
				if(!doc.getOriginalBillCode().equals(pickCode)){
					continue;
				}
				int lineNo = 0;
				for(WmsTaskAndStation wt : wtss){
					if(!wt.getStation().getId().equals(wmds.getId())){
						continue;
					}
					WmsTask task = commonDao.load(WmsTask.class, wt.getTask().getId());
					WmsItemKey itemKey = commonDao.load(WmsItemKey.class, task.getItemKey().getId());
					WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class,task.getMoveDocDetail().getMoveDoc().getId());
					WmsPickTicketDetail pickDetail = commonDao.load(WmsPickTicketDetail.class, task.getMoveDocDetail().getRelatedId());
					WmsPickTicket pickTicket = commonDao.load(WmsPickTicket.class,pickDetail.getPickTicket().getId());
                    //生成装车单明细
					WmsBOLDetail bolDetail = EntityFactory.getEntity(WmsBOLDetail.class);
                    if(detailLineNo==null){
                    	hql = "SELECT MAX(detail.lineNo) FROM WmsBOLDetail detail WHERE detail.bol.id =:bolId ";
                    	detailLineNo = (Integer) commonDao.findByQueryUniqueResult(hql, "bolId", bol.getId());
                    	if(detailLineNo==null){
                    		detailLineNo = 0;
                    	}
                    }
                    bolDetail.setLineNo(detailLineNo+1);
                    
                    WmsPackageUnit packageUnit = task.getPackageUnit();
                    if(itemKey!=null){
                        bolDetail.setItemKey(itemKey);
                    }
                    bolDetail.setPackageUnit(packageUnit);
                    bolDetail.setPallet(BaseStatus.NULLVALUE);
                    bolDetail.setQuantity(wt.getPickQuantityBu());
                    bolDetail.setQuantityBU(wt.getPickQuantityBu());
                    bolDetail.setTask(task);
                    bolDetail.setSlr(doc.getBlg().getCode());
                    bolDetail.setTfd(pick.getReceiveDoc());
                    bolDetail.setProductionLine(pickDetail.getProductionLine());
                    bolDetail.setRequireArriveDate(pick.getRequireArriveDate());
                    bolDetail.setContainer(wt.getContainer());
                    bolDetail.setBoxTag(wmds.getBoxTag());
                    bolDetail.setWmsTaskAndStationId(wt.getId());//目的回写关系表发运状态
                    commonDao.store(bolDetail);
                    bolDetail = commonDao.load(WmsBOLDetail.class, bolDetail.getId());
                    String subCode = "";
                    String key = pickDetail.getProductionLine()==null ? "-" : pickDetail.getProductionLine();
                    if(subMap.containsKey(key)){
                    	subCode = subMap.get(key);
                    }else{
                    	lineNo++;
                    	subCode = "WMS_"+bolDetail.getId()+StringHelper.addCharBeforeStr(lineNo+"", 3, "0");
                    	subMap.put(key, subCode);
                    }
                    bolDetail.setSubCode(subCode);
                    bolDetail.setBol(bol);
                    
                    detailLineNo += bolDetail.getLineNo();
				
					wt.setIsJoinBOL(Boolean.TRUE);
	                commonDao.store(wt);
					bol.addDetail(bolDetail);
		            this.commonDao.store(bolDetail);
		            bol.refreshQuantity();
		            bol.setProductionLine(moveDoc.getProductionLine());
		            commonDao.store(bol);
		            
		            WmsBolDetailExtend bolExt = new WmsBolDetailExtend(bolDetail, moveDoc.getRelatedBill1(), pickTicket.getOdrSu(),wmds.getFromStorage(), 
		            		 wmds.getToStorage(), wmds.getDockNo(), pickDetail.getFromSource(), pickTicket.getBatch(), pickDetail.getStation(), 
		            		pickDetail.getIsJp(), wmds.getType(), wmds.getBoxTag(), pickDetail.getPackageQty(), pickDetail.getSx(),
		            		wmds.getLoadage(),wt.getContainer(),wmds.getSx(),wmds.getSeq(),wmds.getEndseq(),wt.getPickQuantityBu());
		            commonDao.store(bolExt);
				}
				hql = "FROM WmsBoxType bt WHERE bt.code =:code ";
				WmsBoxType bt = (WmsBoxType) commonDao.findByQueryUniqueResult(hql, new String[]{"code"}, new Object[]{container});
				bt.setIsBol(Boolean.TRUE);
				commonDao.store(bt);
				
				wmds.setIsBol(Boolean.TRUE);
				commonDao.store(wmds);
			}
		}
		workDocManager.upBolTagsNum(bol);
	}
	public Map printWmsBOL(WmsBOL bol){
		
		bol.setPrintTime(new Date());
		bol.setPrintWorker(UserHolder.getUser().getName());
		commonDao.store(bol);
		
		Map result = new HashMap();
		Map<Long,String> reportValue = new HashMap<Long, String>();
		reportValue.put(bol.getId(), "wmsBOLReport.raq");
		result.put(IPage.REPORT_VALUES, reportValue);
		result.put(IPage.REPORT_PRINT_NUM, 1);
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("parentIds", bol.getId());
		result.put(IPage.REPORT_PARAMS, params);
		return result;
	}
	
	public Map printWmsBOLTag(WmsBOL bol){
		
		bol.setPrintTagTime(new Date());
		bol.setPrintTagWorker(UserHolder.getUser().getName());
		commonDao.store(bol);
		
		Map result = new HashMap();
		Map<Long,String> reportValue = new HashMap<Long, String>();
		reportValue.put(bol.getId(), "wmsBolContainer.raq");
		result.put(IPage.REPORT_VALUES, reportValue);
		result.put(IPage.REPORT_PRINT_NUM, 1);
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("parentIds", bol.getId());
		result.put(IPage.REPORT_PARAMS, params);
		return result;
	}
}