package com.vtradex.wms.server.service.shipping.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.client.packagingtable.data.MoveDocDetailDTO;
import com.vtradex.wms.client.packagingtable.data.PackagingTableConstants;
import com.vtradex.wms.client.packagingtable.data.PackagingTableLocationDTO;
import com.vtradex.wms.client.packagingtable.data.PackagingTableWokerDTO;
import com.vtradex.wms.server.model.move.WmsBoxDetail;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.warehouse.WmsBoxType;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.shipping.WmsPackagingTableManager;

/**
 * 
 *
 * @category Manager
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:54 $
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DefaultWmsPackagingTableManager extends DefaultBaseManager implements WmsPackagingTableManager {
	
	protected WmsBussinessCodeManager codeManager;
	
	protected WorkflowManager workflowManager;
	
	public DefaultWmsPackagingTableManager(WmsBussinessCodeManager codeManager) {
		this.codeManager = codeManager;
	}
	
	public WorkflowManager getWorkflowManager(){
		if(workflowManager == null){
			workflowManager = (WorkflowManager)applicationContext.getBean("workflowManager");
		}
		return workflowManager;
	}
	
	public Map getPackagingJobs(Map params) {
		Long workGroupId = new Long(params.get(PackagingTableConstants.KEY_WORKGROUPID).toString());
		String hql = "FROM WmsWorker w where w.worker.id = :workGroupId";
		List<WmsWorker> workerList = commonDao.findByQuery(hql, "workGroupId", workGroupId);
		Map jobs = new HashMap();
		for (WmsWorker w : workerList) {
			PackagingTableWokerDTO dto = new PackagingTableWokerDTO(w.getId(), w.getCode(), w.getName(), w.getStation());
			jobs.put(w.getId(), dto);
		}
		return jobs;
	}
	
	public Map getStatisticsInfo(Map params) {
		Map result = new HashMap();
		PackagingTableLocationDTO locationDTO = (PackagingTableLocationDTO)params.get(PackagingTableConstants.KEY_PACKAGING);
		
		String hql1 = "select sum(mdd.movedQuantityBU), sum(mdd.packagingQuantityBU) from WmsMoveDocDetail mdd " +
				" left join mdd.moveDoc md" +
				" where md.type in ('MV_PICKTICKET_PICKING', 'MV_WAVE_PICKING') and md.status = 'FINISHED' and md.shipStatus = 'UNSHIP'" +
				" and mdd.id in (select t.moveDocDetail.id from WmsTask t where t.toLocationId = :toLocationId)";
		
		// 总订单数
		String hql2 = "select distinct md.id from WmsMoveDocDetail mdd " +
				" left join mdd.moveDoc md" +
				" where md.type in ('MV_PICKTICKET_PICKING', 'MV_WAVE_PICKING') and md.status = 'FINISHED' and md.shipStatus = 'UNSHIP'" +
				" and mdd.id in (select t.moveDocDetail.id from WmsTask t where t.toLocationId = :toLocationId)";
		
		// 已完成订单
		String hql3 = "select distinct md.id from WmsMoveDocDetail mdd " +
				" left join mdd.moveDoc md" +
				" where md.type in ('MV_PICKTICKET_PICKING', 'MV_WAVE_PICKING') and md.status = 'FINISHED' and md.shipStatus = 'UNSHIP'" +
				" and md.movedQuantityBU - md.packagingQuantityBU = 0" +
				" and mdd.id in (select t.moveDocDetail.id from WmsTask t where t.toLocationId = :toLocationId)";
		
		try {
			Object obj1 = commonDao.findByQueryUniqueResult(hql1, "toLocationId", locationDTO.getId());
			if (obj1 != null) {
				Object[] obj1s = (Object[])obj1;
				Double movedQuantityBU = obj1s[0] == null ? 0.0D : Double.valueOf(obj1s[0].toString());
				Double finishQuantity = obj1s[1] == null ? 0.0D : Double.valueOf(obj1s[1].toString());
				result.put(PackagingTableConstants.KEY_ORDER_FINISH_QUANTITY, finishQuantity);
				result.put(PackagingTableConstants.KEY_ORDER_UNFINISH_QUANTITY, movedQuantityBU -  finishQuantity);
			}
			
			List obj2 = commonDao.findByQuery(hql2, "toLocationId", locationDTO.getId());
			List obj3 = commonDao.findByQuery(hql3, "toLocationId", locationDTO.getId());
			result.put(PackagingTableConstants.KEY_ORDER_FINISH_NUM, obj3.size());
			result.put(PackagingTableConstants.KEY_ORDER_UNFINISH_NUM, obj2.size() - obj3.size());
			
			return result;
		} catch (Exception ex) {
			logger.error("", ex);
			result.put(PackagingTableConstants.ERROR, "统计订单数失败, 请重试.");
			return result;
		}
	}
	
	public Map queryMoveDocDetail(Map params) {
		Map result = new HashMap();
		
		String moveDocCode = params.get(PackagingTableConstants.KEY_MOVEDOC_CODE).toString();
		PackagingTableLocationDTO locDTO = (PackagingTableLocationDTO)params.get(PackagingTableConstants.KEY_PACKAGING);
		
		String hql = " from WmsMoveDocDetail mdd where mdd.moveDoc.code = :moveDocCode and mdd.moveDoc.type='MV_PICKTICKET_PICKING'" +
			" and ((mdd.id in (select t.moveDocDetail.id from WmsTask t where t.toLocationId = :toLocationId and t.workDoc.type='MV_PICKTICKET_PICKING'))" +
			" or (mdd.id in (select waveDocDetail.moveDocDetail.id from WmsWaveDocDetail waveDocDetail where waveDocDetail " +
			" in (select elements(t.moveDocDetail.waveDocDetails) from WmsTask t where t.toLocationId = :toLocationId and t.workDoc.type='MV_WAVE_PICKING')) ))" +
			" order by mdd.packagingQuantityBU ASC";
		
		try {
			List<WmsMoveDocDetail> resultList = commonDao.findByQuery(hql, 
					new String[]{"moveDocCode", "toLocationId"}, new Object[]{moveDocCode, locDTO.getId()});
			if (resultList == null || resultList.isEmpty()) {
				result.put(PackagingTableConstants.ERROR, "拣货单:" + moveDocCode + ", 不存在.");
				return result;
			}
			result.put(PackagingTableConstants.KEY_ITEMINFO_LIST, generateMonitorWmsMoveDocDetail(resultList));
			return result;
		} catch (Exception ex) {
			logger.error("", ex);
			result.put(PackagingTableConstants.ERROR, "加载货品信息失败, 请重试.");
			return result;
		}
	}
	
	public Map getMoveDocDetail(Map params) {
		Map result = new HashMap();
		String moveDocCode = params.get(PackagingTableConstants.KEY_MOVEDOC_CODE).toString();
		String barCode = params.get(PackagingTableConstants.KEY_BARCODE).toString();
		List<Long> mddIds = (List<Long>)params.get(PackagingTableConstants.KEY_SCANING_IDS);
		mddIds.add(0L);
		String hql = " from WmsMoveDocDetail mdd where mdd.moveDoc.code = :moveDocCode and mdd.item.barCode = :barCode and mdd.id not in (:ids)";
		
		try {
			List<WmsMoveDocDetail> resultList = commonDao.findByQuery(hql, new String[]{"moveDocCode", "barCode", "ids"}, new Object[]{moveDocCode, barCode, mddIds});
			if (resultList == null || resultList.isEmpty()) {
				if (Integer.parseInt(params.get(PackagingTableConstants.SUCCESS).toString()) == -1) {
					result.put(PackagingTableConstants.ERROR, "条码:" + barCode + ", 已包装完成.");
				} else {
					result.put(PackagingTableConstants.ERROR, "条码:" + barCode + ", 不存在该拣货单中.");
				}
				return result;
			}
			WmsMoveDocDetail md = resultList.get(0);
			if (md.getBePackageFinish()) {
				result.put(PackagingTableConstants.ERROR, "条码:" + barCode + ", 已包装完成.");
				return result;
			}
			
			WmsPackageUnit unit = commonDao.load(WmsPackageUnit.class, md.getPackageUnit().getId());
			if (unit.getUnit().equals("箱")) {
				result.put(PackagingTableConstants.ERROR, "包装单位为箱的货品不能包装！");
				return result;
			}
			
			MoveDocDetailDTO dto = new MoveDocDetailDTO();
			dto.setId(md.getId());
			dto.setMoveDocId(md.getMoveDoc().getId());
			WmsItem item = commonDao.load(WmsItem.class, md.getItem().getId());
			dto.setItemCode(item.getCode());
			dto.setItemName(item.getName());
			dto.setBarCode(item.getBarCode());
			dto.setUnit(unit.getUnit());
			dto.setWeight(unit.getWeight().toString());
			dto.setVolume(unit.getVolume().toString());
			dto.setAllocatedQuantityBU(md.getAllocatedQuantityBU().toString());
			dto.setMovedQuantityBU(md.getMovedQuantityBU().toString());
			dto.setPackagedQuantityBU(md.getPackagingQuantityBU().toString());
			dto.setScanQuantityBU("1");
			dto.setUnPackageQuantityBU((md.getMovedQuantityBU() - md.getPackagingQuantityBU()) + "");
			dto.setLotInfoStr(md.getShipLotInfo().stringValue());
			result.put(PackagingTableConstants.KEY_MOVEDOCDETAIL_DTO, dto);
		} catch (Exception ex) {
			logger.error("", ex);
			result.put(PackagingTableConstants.ERROR, "扫描条码失败, 请重试.");
		}
		return result;
	}
	
	public Map finishPackaging(Map params) {
		Map result = new HashMap();
		List list = (List)params.get(PackagingTableConstants.KEY_SCANING_DATA);
		String calculateWeight = params.get(PackagingTableConstants.KEY_CALCULATE_WEIGHT).toString();
		String autoCalculateWeight = params.get(PackagingTableConstants.KEY_AUTOCALCULATE_WEIGHT).toString();
		String caculateVolume = params.get(PackagingTableConstants.KEY_CALCULATE_VOLUME).toString();
		String quantity = params.get(PackagingTableConstants.KEY_QUANTITY).toString();
		Long boxTypeId = Long.valueOf(params.get(PackagingTableConstants.KEY_BOX_TYPE_ID).toString());
		
		PackagingTableLocationDTO locationDTO = (PackagingTableLocationDTO)params.get(PackagingTableConstants.KEY_PACKAGING);
		
		WmsBoxDetail boxDetailCharges = null;
		try {
			WmsBoxType boxType = commonDao.load(WmsBoxType.class, boxTypeId);
			WmsWorker workerGroup = commonDao.load(WmsWorker.class, locationDTO.getWorkGroupId());
			WmsMoveDoc moveDoc = null;
			String boxNo = "";
			for (int i = 0; i < list.size(); i++) {
				MoveDocDetailDTO dto = (MoveDocDetailDTO)list.get(i);
				if (moveDoc == null) {
					moveDoc = commonDao.load(WmsMoveDoc.class, dto.getMoveDocId());
					String hql = "select distinct b.boxNo from WmsBoxDetail b where b.moveDoc.id = :moveDocId";
					List obj = commonDao.findByQuery(hql, "moveDocId", moveDoc.getId());
					int boxNum = obj.size() + 1;
					boxNo = moveDoc.getCode() + "_" + boxNum;
				}
				WmsMoveDocDetail moveDocDetail = commonDao.load(WmsMoveDocDetail.class, dto.getId());
				WmsBoxDetail boxDetail = new WmsBoxDetail();
				boxDetail.setMoveDoc(moveDoc);
				boxDetail.setMoveDocDetail(moveDocDetail);
				boxDetail.setBoxType(boxType);
				boxDetail.setBoxNo(boxNo);
				boxDetail.setQuantity(Double.valueOf(dto.getScanQuantityBU()));
				boxDetail.setWeight(Double.valueOf(calculateWeight));
				boxDetail.setActualWeight(Double.valueOf(autoCalculateWeight));
				boxDetail.setVolume(Double.valueOf(caculateVolume));
				boxDetail.setTotalQuantity((Double.valueOf(quantity)));
				boxDetail.setWorkerGroup(workerGroup);
				this.commonDao.store(boxDetail);
				
				moveDocDetail.addPackagingQuantityBU(boxDetail.getQuantity());
				moveDoc.addBoxDetail(boxDetail);
				
				// 判断是否全部包装完成
				if (moveDoc.getMovedQuantityBU().doubleValue() == moveDoc.getPackagingQuantityBU().doubleValue()) {
					result.put(PackagingTableConstants.KEY_ALL_PACKAGED, Boolean.TRUE);
				} else {
					result.put(PackagingTableConstants.KEY_ALL_PACKAGED, Boolean.FALSE);
				}
				result.put(PackagingTableConstants.KEY_WPDID, boxDetail.getId());
				result.put(PackagingTableConstants.KEY_BOX_RAQ, "printBoxNo.raq");
				
				boxDetailCharges = boxDetail;
			}
			getWorkflowManager().sendMessage(boxDetailCharges, "wmsBoxDetail.charges");
		} catch (Exception ex) {
			logger.error("", ex);
			result.put(PackagingTableConstants.ERROR, "包装确认失败, 请重试.");
		}
		return result;
	}
	
	
	private List<MoveDocDetailDTO> generateMonitorWmsMoveDocDetail(List<WmsMoveDocDetail> resultList) {
		List<MoveDocDetailDTO> listDTO = new ArrayList<MoveDocDetailDTO>();
		for (WmsMoveDocDetail md : resultList) {
			MoveDocDetailDTO dto = new MoveDocDetailDTO();
			dto.setId(md.getId());
			WmsItem item = commonDao.load(WmsItem.class, md.getItem().getId());
			WmsPackageUnit unit = commonDao.load(WmsPackageUnit.class, md.getPackageUnit().getId());
			dto.setItemCode(item.getCode());
			dto.setItemName(item.getName());
			dto.setUnit(unit.getUnit());
			dto.setAllocatedQuantityBU(md.getAllocatedQuantityBU().toString());
			dto.setMovedQuantityBU(md.getMovedQuantityBU().toString());
			dto.setLotInfoStr(md.getShipLotInfo().stringValue());
			dto.setPackagedQuantityBU(md.getPackagingQuantityBU().toString());
			dto.setUnPackageQuantityBU( (md.getMovedQuantityBU() - md.getPackagingQuantityBU()) + "" );
			listDTO.add(dto);
		}
		return listDTO;
	}
}
