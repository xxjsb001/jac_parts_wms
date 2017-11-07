package com.vtradex.wms.server.service.shipping.pojo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.vtradex.thorn.client.ui.table.RowData;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.exception.OriginalBusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.DateUtil;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.model.shipping.WmsPickTicketDetail;
import com.vtradex.wms.server.model.shipping.WmsWaveDoc;
import com.vtradex.wms.server.model.shipping.WmsWaveDocBaseStatus;
import com.vtradex.wms.server.model.shipping.WmsWaveDocDetail;
import com.vtradex.wms.server.model.shipping.WmsWaveDocWorkMode;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.message.WmsMessageManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.rule.WmsTransactionalManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.shipping.WmsPickTicketManager;
import com.vtradex.wms.server.service.shipping.WmsWaveDocManager;
import com.vtradex.wms.server.service.task.WmsTaskManager;
import com.vtradex.wms.server.service.workDoc.WmsWorkDocManager;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

public class DefaultWmsWaveDocManager extends DefaultBaseManager implements WmsWaveDocManager {
	protected WorkflowManager workflowManager;
	protected WmsInventoryManager inventoryManager;
	protected WmsBussinessCodeManager codeManager;
	protected WmsRuleManager wmsRuleManager;
	protected WmsTaskManager wmsTaskManager;
	protected WmsTransactionalManager wmsTransactionalManager;
	protected WmsWorkDocManager wmsWorkDocManager;
	protected WmsMessageManager wmsMessageManager;
	protected WmsPickTicketManager wmsPickTicketManager;

	public DefaultWmsWaveDocManager(WorkflowManager workflowManager, WmsInventoryManager inventoryManager,
			WmsBussinessCodeManager codeManager, WmsRuleManager wmsRuleManager, WmsTaskManager wmsTaskManager
			,WmsTransactionalManager wmsTransactionalManager,WmsWorkDocManager wmsWorkDocManager
			,WmsMessageManager wmsMessageManager,WmsPickTicketManager wmsPickTicketManager) {
		this.workflowManager = workflowManager;
		this.inventoryManager = inventoryManager;
		this.codeManager =  codeManager;
		this.wmsRuleManager = wmsRuleManager;
		this.wmsTaskManager = wmsTaskManager;
		this.wmsTransactionalManager = wmsTransactionalManager;
		this.wmsWorkDocManager = wmsWorkDocManager;
		this.wmsMessageManager = wmsMessageManager;
		this.wmsPickTicketManager = wmsPickTicketManager;
	} 
	
	@SuppressWarnings("unchecked")
	public void createWaveDoc(String type) {
		WmsWarehouse warehouse = WmsWarehouseHolder.getWmsWarehouse();
		
		String workMode = getWaveDocWorkMode(type,warehouse.getName());
		if (workMode == null) {
			throw new BusinessException("作业模式不能为空");
		}
		
		
		Map problem = new HashMap();
		problem.put("波次类型", type);
		problem.put("仓库ID", warehouse.getId());
		
		Map<String, Object> result = wmsRuleManager.execute(warehouse.getName(), warehouse.getName(), "波次生成规则", problem);
		List<Map<String,Object>> waves = (List<Map<String,Object>>)result.get("波次列表");
		for (Map<String,Object> wave : waves) {
			createWaveDoc(warehouse,type, workMode , wave);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void createWaveDoc(WmsWarehouse warehouse,String type, String workMode, Map<String,Object> result) {
		String masterBOLCode = (String)result.get("相关单号");
		Date finishDate = (Date)result.get("波次结束时间");;
		List<Long> moveDocIds = (List<Long>)result.get("移位单列表");
		
		boolean loadSameWaveDoc = "是".equals((String)result.get("是否匹配已有波次"));
		String waveId = (String)result.get("波次名称");
		if(moveDocIds == null || moveDocIds.isEmpty()){
			return;
		}
		if(StringUtils.isNotBlank(waveId)){
			type = waveId + "_" + type;
		}
		WmsWaveDoc waveDoc = null;
		if (loadSameWaveDoc) {
			List<WmsWaveDoc> waveDocs = (List<WmsWaveDoc>)commonDao.findByQuery("FROM WmsWaveDoc waveDoc WHERE waveDoc.status = :status AND waveDoc.type = :type " +
				"AND ((waveDoc.relatedBill = :relatedBill AND :relatedBill IS NOT NULL) OR :relatedBill IS NULL) AND waveDoc.warehouse.id = :warehouseId",
				new String[]{"status","type","relatedBill","warehouseId"},
				new Object[]{WmsWaveDocBaseStatus.OPEN,type,masterBOLCode,warehouse.getId()});
			if (waveDocs != null && waveDocs.size() > 0) {
				waveDoc = waveDocs.get(0);
			}
		}
		if (waveDoc == null) {
			waveDoc = EntityFactory.getEntity(WmsWaveDoc.class);
			
			waveDoc.setCode(codeManager.generateCodeByRule(warehouse, warehouse.getName(), "波次单", type));
			waveDoc.setWarehouse(warehouse);
			waveDoc.setType(type);
			waveDoc.setWorkMode(workMode);
			waveDoc.setRelatedBill(masterBOLCode);
			waveDoc.setFinishDate(finishDate);
			workflowManager.doWorkflow(waveDoc, "wmsWaveDocProcess.new");
		}
		createWaveDocDetails(waveDoc, moveDocIds);
	}
	
	public void createWaveDocDetails(WmsWaveDoc waveDoc, List<Long> moveDocIds) {
		for (Long moveDocId : moveDocIds) {
			WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, moveDocId);
			moveDoc.setIntendShipDate(waveDoc.getFinishDate());
			addWaveDocDetailByMoveDoc(waveDoc, moveDoc);
		}
	}
	
	private void addWaveDocDetailByMoveDoc(WmsWaveDoc waveDoc, WmsMoveDoc moveDoc) {
		for (WmsMoveDocDetail moveDocDetail : moveDoc.getDetails()) {
			WmsWaveDocDetail waveDocDetail = EntityFactory.getEntity(WmsWaveDocDetail.class);
			waveDocDetail.setWaveDoc(waveDoc);
			waveDocDetail.setItem(moveDocDetail.getItem());
			waveDocDetail.setShipLotInfo(moveDocDetail.getShipLotInfo());
			waveDocDetail.setPackageUnit(moveDocDetail.getPackageUnit());
			waveDocDetail.setMoveDocDetail(moveDocDetail);
			waveDocDetail.addMoveDocDetail(moveDocDetail);
	
			commonDao.store(waveDocDetail);
		}
		moveDoc.setBeWave(true);
	}
	
	
	private String getWaveDocWorkMode(String type,String warehouseName) {
		String result = null;
		List<Map<String,Object>> ruleValues = findRuleWaveTableValue(warehouseName);
		for (Map<String,Object> ruleValue : ruleValues) {
			if (type.equals(ruleValue.get("波次类型"))) {
				if ("是".equals(ruleValue.get("是否批拣").toString().toString())) {
					result = WmsWaveDocWorkMode.WORK_BY_WAVE;
				} else {
					result = WmsWaveDocWorkMode.WORK_BY_DOC;
				}
				break;
			}
		}
		return result;
	}	
	
	
	@SuppressWarnings("unchecked")
	public RowData[] getRuleConfigWave(Map params) {
		WmsWarehouse warehouse = WmsWarehouseHolder.getWmsWarehouse();
		List<Map<String,Object>> ruleValues = findRuleWaveTableValue(warehouse.getName());
		
		RowData[] rowDatas = new RowData[ruleValues.size()];
		int index = 0;
		for (Map<String,Object> ruleValue : ruleValues) {
			RowData rowData = new RowData();
			rowData.addColumnValue(ruleValue.get("波次类型"));
			rowData.addColumnValue(ruleValue.get("波次类型"));
			
			rowDatas[index++] = rowData;
		}
		return rowDatas;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findRuleWaveTableValue(String warehouseName) {
		List<Map<String,Object>> ruleValues = wmsRuleManager.getMultipleRuleTableDetail(warehouseName, new Object[]{"R101_波次_波次类型表"});
		return ruleValues;
	}
	
	@SuppressWarnings("unchecked")
	public void deleteWaveDoc(WmsWaveDoc waveDoc) {
		List<WmsWaveDocDetail> waveDocDetails = commonDao.findByQuery("FROM WmsWaveDocDetail waveDocDetail where waveDocDetail.waveDoc.id = :waveDocId",
			"waveDocId", waveDoc.getId());
		WmsMoveDoc moveDoc;
		for (WmsWaveDocDetail waveDocDetail : waveDocDetails) {
			moveDoc = waveDocDetail.getMoveDocDetail().getMoveDoc();
			moveDoc.setBeWave(false);
			commonDao.store(moveDoc);
			
			commonDao.delete(waveDocDetail);
		}
		workflowManager.doWorkflow(waveDoc, "wmsWaveDocProcess.delete");
	}
	
	
	/**
	 * 添加指定的拣货单
	 */
	public void addWaveDocDetail(Long waveDocId,WmsMoveDoc moveDoc) {
		WmsWaveDoc waveDoc = commonDao.load(WmsWaveDoc.class, waveDocId);
		addWaveDocDetailByMoveDoc(waveDoc, moveDoc);
	}
	
	/**
	 * 移出指定的拣货单
	 */
	public void removeWaveDocDetail(WmsWaveDocDetail waveDocDetail){
//		WmsMoveDocDetail moveDocDetail = commonDao.get(WmsMoveDocDetail.class, waveDocDetail.getMoveDocDetail().getId());
		
		WmsWaveDoc waveDoc = waveDocDetail.getWaveDoc();
		WmsMoveDoc moveDoc = commonDao.get(WmsMoveDoc.class, waveDocDetail.getMoveDocDetail().getMoveDoc().getId());
		WmsWaveDocDetail delWaveDocDetail;
		for(WmsMoveDocDetail moveDocDetail : moveDoc.getDetails()){
			delWaveDocDetail = (WmsWaveDocDetail)commonDao.findByQueryUniqueResult("FROM WmsWaveDocDetail waveDocDetail WHERE waveDocDetail.moveDocDetail.id = :moveDocDetailId",
				"moveDocDetailId", moveDocDetail.getId());
			waveDoc.removeExpectedQuantityBU(delWaveDocDetail.getExpectedQuantityBU());
			commonDao.delete(delWaveDocDetail);
		}
		moveDoc.setBeWave(false);
		commonDao.store(moveDoc);
		commonDao.store(waveDoc);
	}
	
	@SuppressWarnings("unchecked")
	public void unActiveWaveDoc(WmsWaveDoc waveDoc){
		List<WmsMoveDoc> moveDocs = commonDao.findByQuery("From WmsMoveDoc wmd Where wmd.waveDoc.id = :waveDocId", "waveDocId", waveDoc.getId());
		for(WmsMoveDoc moveDoc : moveDocs){
			if(!WmsMoveDocStatus.OPEN.equals(moveDoc.getStatus())){
				throw new OriginalBusinessException("该波次单的拣货单必须为打开状态！");
			}
			commonDao.delete(moveDoc);
		}
		List<WmsWaveDocDetail> waveDocDetails = commonDao.findByQuery("FROM WmsWaveDocDetail waveDocDetail where waveDocDetail.waveDoc.id = :waveDocId",
				"waveDocId", waveDoc.getId());
		for(WmsWaveDocDetail waveDocDetail : waveDocDetails){
			waveDocDetail.getWaveMoveDocDetails().clear();
			commonDao.store(waveDocDetail);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void seprateAdjustConfirm(WmsWaveDocDetail waveDocDetail, WmsWaveDocDetail pickedWaveDocDetail, List<String> seprateQuantityList) {
		Double seprateQuantity;
		try {
			seprateQuantity = Double.valueOf(seprateQuantityList.get(0));
		} catch (NumberFormatException nfe) {
			throw new BusinessException("调整数量只能为数值类型！");
		}
		if (seprateQuantity.doubleValue() > pickedWaveDocDetail.getSplitedQuantityBU().doubleValue()) {
			throw new BusinessException("分单调整数量不能大于已拣货数量！");
		}
		if (seprateQuantity.doubleValue() > waveDocDetail.getUnSplitedQuantity().doubleValue()) {
			throw new BusinessException("分单调整数量不能大于未拣货数量！");
		}
		if (seprateQuantity.doubleValue() <= 0) {
			return;
		}
		
		// 分到目标明细的调整
		waveDocDetail.allocate(seprateQuantity);
		waveDocDetail.addMovedQuantity(seprateQuantity);
		waveDocDetail.seprate(seprateQuantity);
		commonDao.store(waveDocDetail);
		
		WmsPickTicketDetail pickTicketDetail = commonDao.load(WmsPickTicketDetail.class, waveDocDetail.getMoveDocDetail().getRelatedId());
		pickTicketDetail.allocate(seprateQuantity);
		pickTicketDetail.addPickedQuantityBU(seprateQuantity);
		commonDao.store(pickTicketDetail);
		
		String hql = "SELECT DISTINCT wwdd.moveDocDetail.moveDoc.id FROM WmsWaveDocDetail wwdd " +
		"WHERE wwdd.moveDocDetail.moveDoc.planQuantityBU <= wwdd.moveDocDetail.moveDoc.movedQuantityBU " +
		"AND wwdd.moveDocDetail.moveDoc.status != :status AND wwdd.id = :waveDocDetailId";
		List<Long> pickingIds = commonDao.findByQuery(hql, new String[]{"waveDocDetailId","status"}, 
			new Object[]{waveDocDetail.getId(),WmsMoveDocStatus.FINISHED});
		for(Long id : pickingIds){
			WmsMoveDoc picking = load(WmsMoveDoc.class,id);
			picking.setStatus(WmsMoveDocStatus.FINISHED);
			commonDao.store(picking);
		}
		
		// 分单源明细的调整
		pickedWaveDocDetail.unallocate(seprateQuantity);
		pickedWaveDocDetail.cancelMovedQuantity(seprateQuantity);
		pickedWaveDocDetail.cancelSeprate(seprateQuantity);
		commonDao.store(pickedWaveDocDetail);
		
		WmsPickTicketDetail pickedPickTicketDetail = commonDao.load(WmsPickTicketDetail.class, pickedWaveDocDetail.getMoveDocDetail().getRelatedId());
		pickedPickTicketDetail.unallocate(seprateQuantity);
		pickedPickTicketDetail.cancelPickedQuantityBU(seprateQuantity);
		commonDao.store(pickedPickTicketDetail);
		
		String pickedHql = "SELECT DISTINCT wwdd.moveDocDetail.moveDoc.id FROM WmsWaveDocDetail wwdd " +
		"WHERE wwdd.moveDocDetail.moveDoc.status = :status AND wwdd.id = :waveDocDetailId";
		List<Long> pickPickingIds = commonDao.findByQuery(pickedHql, new String[]{"waveDocDetailId","status"}, 
			new Object[]{pickedWaveDocDetail.getId(),WmsMoveDocStatus.FINISHED});
		for(Long id : pickPickingIds){
			WmsMoveDoc picking = load(WmsMoveDoc.class,id);
			picking.setStatus(WmsMoveDocStatus.OPEN);
			commonDao.store(picking);
		}
	}
}
