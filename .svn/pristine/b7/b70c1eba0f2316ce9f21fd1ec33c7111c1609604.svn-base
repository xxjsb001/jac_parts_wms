package com.vtradex.wms.server.service.base.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vtradex.thorn.client.ui.page.IPage;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.base.LotInfo;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.inventory.WmsMisInventory;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsLotRule;
import com.vtradex.wms.server.model.organization.WmsPackageChangeLog;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.service.base.WmsItemManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;

@SuppressWarnings("unchecked")
public class DefaultWmsItemManager extends DefaultBaseManager implements WmsItemManager {
	protected WmsBussinessCodeManager wmsBussinessCodeManager;
	protected WorkflowManager workflowManager;
	protected WmsRuleManager wmsRuleManager;
	
	public DefaultWmsItemManager(WmsBussinessCodeManager wmsBussinessCodeManager, WorkflowManager workflowManager,
			WmsRuleManager wmsRuleManager) {
		this.wmsBussinessCodeManager = wmsBussinessCodeManager;
		this.workflowManager = workflowManager;
		this.wmsRuleManager = wmsRuleManager;
	}
	
	public void addPackageUnit(Long itemId, WmsPackageUnit packageUnit) {
		WmsItem item = null;
		//新增商品包装
		if (packageUnit.isNew()) {
			//判断该货品下是否有件装量为1的包装,有则不允许新建
			//String hql1 = "SELECT COUNT(*) FROM WmsPackageUnit packageUnit WHERE packageUnit.convertFigure = 1 AND packageUnit.item.id = :itemId";
			//Long count1 = (Long) commonDao.findByQueryUniqueResult(hql1, "itemId", itemId);
			//if (count1 > 0 && packageUnit.getConvertFigure().intValue() == 1) {
			//	throw new BusinessException("packageUnit.base.convertFigure.exists");
			//}
			
			//校验行号的唯一性
			String hql2 = "SELECT COUNT(*) FROM WmsPackageUnit packageUnit WHERE packageUnit.lineNo = :lineNo AND packageUnit.item.id = :itemId";
			
			Long count2 = (Long) commonDao.findByQueryUniqueResult(hql2, 
					new String[]{"lineNo","itemId"}, 
					new Object[]{packageUnit.getLineNo(),itemId});
			
			if (count2 > 0) {
				throw new BusinessException("packageUnit.lineNo.exists");
			}
			
			item = commonDao.load(WmsItem.class, itemId);
			item.addPackageUnit(packageUnit);
		} else {
			WmsPackageUnit oldUnit = (WmsPackageUnit) packageUnit.getOldEntity();
			//不允许修改最小包装的件装量和行号
			if (oldUnit.getLineNo().intValue() == 1 && oldUnit.getConvertFigure().intValue() == 1) {
				if (packageUnit.getLineNo().intValue() != 1 || packageUnit.getConvertFigure().intValue() != 1) {
					throw new BusinessException("packageUnit.can.not.edit.base.unit"); 
				}
			}
			item = this.commonDao.load(WmsItem.class, itemId);
			
			Integer oldConvertFigure = oldUnit.getConvertFigure();
			Integer oldPrecision = oldUnit.getPrecision();
			
			if ((oldConvertFigure.intValue()!=packageUnit.getConvertFigure().intValue()) || (oldPrecision.intValue()!=packageUnit.getPrecision().intValue())) {
				addWmsPackageChangeLog(item, packageUnit, oldConvertFigure, oldPrecision);
			}
		}
	}
	
	private void addWmsPackageChangeLog(WmsItem item, WmsPackageUnit packageUnit, Integer oldConvertFigure, Integer oldPrecision){
		WmsPackageChangeLog log = EntityFactory.getEntity(WmsPackageChangeLog.class);
		
		log.setItem(item);
		log.setPackageUnit(packageUnit);
		log.setOldConvertFigure(oldConvertFigure);
		log.setOldPrecision(oldPrecision);
		log.setNewConvertFigure(packageUnit.getConvertFigure());
		log.setNewPrecision(packageUnit.getPrecision());
		
		commonDao.store(log);
	}
	
	/**
	 * 产生库内批次号
	 * @param record
	 */
	private void createItemKeyLot(WmsWarehouse warehouse, WmsItemKey itemKey,LotInfo lotInfo) {
		Map<String,Object> problem = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		//客户名称
		String companyName = itemKey.getItem().getCompany().getName();
		String referenceRule = "批次号规则" ;
		
		problem.put("货品代码", itemKey.getItem().getCode());
		problem.put("生产日期", itemKey.getLotInfo().getProductDate());
					
		result = wmsRuleManager.execute(warehouse.getName(), companyName, referenceRule, problem);										
		String lot = result.get("批次号").toString();		
		itemKey.setLot(lot);		
	}

	/**
	 * 根据ASN上的批次信息获取 WmsItemKey
	 */
	public WmsItemKey getItemKey(WmsWarehouse warehouse, String soi, WmsItem item, LotInfo lotInfo) {
		WmsLotRule lotRule = item.getDefaultLotRule();
		if (lotInfo == null) {
			lotInfo = new LotInfo();
		}
		lotInfo.prepare(lotRule, item, soi);
		if (!lotRule.verify(lotInfo)) {
			throw new BusinessException("lotInfo.is.not.completion");
		}

		// 根据ASN明细信息创建ItemKey信息
		WmsItemKey tempItemKey = EntityFactory.getEntity(WmsItemKey.class);
		tempItemKey.setItem(item);
		tempItemKey.setLotInfo(lotInfo);
		tempItemKey.setHashCode(tempItemKey.genHashCode());
		
		createItemKeyLot(warehouse, tempItemKey,lotInfo);

		return getItemKey(tempItemKey);
	}
	
	public WmsItemKey getItemKey(WmsWarehouse warehouse, String soi, WmsItem item, LotInfo lotInfo, Boolean beVerifyLotInfo) {
		WmsLotRule lotRule = item.getDefaultLotRule();
		if (lotInfo == null) {
			lotInfo = new LotInfo();
		}
		lotInfo.prepare(lotRule, item, soi);
		if (beVerifyLotInfo) {
			if (!lotRule.verify(lotInfo)) {
				throw new BusinessException("lotInfo.is.not.completion");
			}
		}

		// 根据ASN明细信息创建ItemKey信息
		WmsItemKey tempItemKey = EntityFactory.getEntity(WmsItemKey.class);
		tempItemKey.setItem(item);
		tempItemKey.setLotInfo(lotInfo);
		tempItemKey.setHashCode(tempItemKey.genHashCode());
		
		createItemKeyLot(warehouse, tempItemKey,lotInfo);

		return getItemKey(tempItemKey);
	}
	
	public WmsItemKey getItemKey(WmsItemKey itemKey) {
		String query = "FROM WmsItemKey itemKey WHERE itemKey.hashCode = :hashCode";
		
		WmsItemKey dbItemKey = (WmsItemKey) commonDao.findByQueryUniqueResult(query, "hashCode", itemKey.getHashCode());
		
		// 如果数据库中不存在当前ItemKey, 则新创建
		if (dbItemKey == null) {
			dbItemKey = EntityFactory.getEntity(WmsItemKey.class);
			dbItemKey.setItem(itemKey.getItem());
			dbItemKey.setLotInfo(itemKey.getLotInfo());
			dbItemKey.setLot(itemKey.getLot());		
			dbItemKey.setHashCode(itemKey.genHashCode());
			
			commonDao.store(dbItemKey);
			
		}
		
		return dbItemKey;
	}
	
	public List<Boolean> getLotRuleTackersByPageMap(Map map) {
		Long itemId = (Long) map.get("itemId");
		
		if (itemId == null) {
			itemId = (Long) map.get("asnDetail.item.id");
		}
		if (itemId == null) {
			itemId = (Long) map.get("countRecord.itemKey.item.id");
		}
		if (itemId == null) {
			itemId = (Long) map.get("task.itemKey.item.id");
		}
		if (itemId == null) {
			itemId = (Long) map.get("processDoc.item.id");
		}
		
		return getLotRuleTackers(itemId);
	}
	
	public List<Boolean> getShipLotRuleTackersByPageMap(Map map) {
		Long itemId = (Long) map.get("itemId");
		
		if (itemId == null) {
			itemId = (Long) map.get("pickTicketDetail.item.id");
		}
		if (itemId == null) {
			itemId = (Long) map.get("processPlanDetail.item.id");
		}
		
		return getShipLotRuleTackers(itemId);
	}
	
	public List<Boolean> getLotRuleTrackersByASNDetailMap(Map map) {
		WmsASNDetail detail = commonDao.load(WmsASNDetail.class, (Long) map.get("parentId"));
		Long itemId = detail.getItem().getId();
		
		return getLotRuleTackers(itemId);
	}
	
	public List<Boolean> getLotRuleTackers(Long itemId) {
		List<Boolean> results = new ArrayList<Boolean>();
		if(itemId!=null){
			WmsItem item = commonDao.load(WmsItem.class, itemId);
			
			WmsLotRule rule = item.getDefaultLotRule();
			
			if (item != null && rule != null) {
				results.add(rule.getTrackProduceDate());
				results.add(rule.getTrackSupplier());
				results.add(rule.getTrackExtendPropC1());
				results.add(rule.getTrackExtendPropC2());
				results.add(rule.getTrackExtendPropC3());
				results.add(rule.getTrackExtendPropC4());
				results.add(rule.getTrackExtendPropC5());
				results.add(rule.getTrackExtendPropC6());
				results.add(rule.getTrackExtendPropC7());
				results.add(rule.getTrackExtendPropC8());
				results.add(rule.getTrackExtendPropC9());
				results.add(rule.getTrackExtendPropC10());
				results.add(rule.getTrackExtendPropC11());
				results.add(rule.getTrackExtendPropC12());
				results.add(rule.getTrackExtendPropC13());
				results.add(rule.getTrackExtendPropC14());
				results.add(rule.getTrackExtendPropC15());
				results.add(rule.getTrackExtendPropC16());
				results.add(rule.getTrackExtendPropC17());
				results.add(rule.getTrackExtendPropC18());
				results.add(rule.getTrackExtendPropC19());
				results.add(rule.getTrackExtendPropC20());
			}
		}
		return results;
	}
	
	
	public List<Boolean> getShipLotRuleTackers(Long itemId){

		List<Boolean> results = new ArrayList<Boolean>();
		if(itemId!=null){
			WmsItem item = commonDao.load(WmsItem.class, itemId);
			
			WmsLotRule rule = item.getDefaultLotRule();
			
			if (item != null && rule != null) {
				results.add(rule.getTrackStorageDate());
				results.add(rule.getTrackSOI());
				results.add(rule.getTrackSupplier());
				results.add(rule.getTrackExtendPropC1());
				results.add(rule.getTrackExtendPropC2());
				results.add(rule.getTrackExtendPropC3());
				results.add(rule.getTrackExtendPropC4());
				results.add(rule.getTrackExtendPropC5());
				results.add(rule.getTrackExtendPropC6());
				results.add(rule.getTrackExtendPropC7());
				results.add(rule.getTrackExtendPropC8());
				results.add(rule.getTrackExtendPropC9());
				results.add(rule.getTrackExtendPropC10());
				results.add(rule.getTrackExtendPropC11());
				results.add(rule.getTrackExtendPropC12());
				results.add(rule.getTrackExtendPropC13());
				results.add(rule.getTrackExtendPropC14());
				results.add(rule.getTrackExtendPropC15());
				results.add(rule.getTrackExtendPropC16());
				results.add(rule.getTrackExtendPropC17());
				results.add(rule.getTrackExtendPropC18());
				results.add(rule.getTrackExtendPropC19());
				results.add(rule.getTrackExtendPropC20());
			}
		}
		return results;
	
	}
	
	public Boolean isContainLotRule(long lotRuleId){
		List<WmsItem> items = this.commonDao.findByQuery("from WmsItem item left join item.lotRule lotRule where 1=1 and lotRule.id=:lotRuleId", 
				     new String[]{"lotRuleId"}, new Object[]{lotRuleId});
		if(items!=null&&!items.isEmpty()){
			return true;
		}
		return false;
	}
	
	public Map printBarCode(WmsItem item, Long printNumber) {
		Map result = new HashMap();
		Map<Long,String> reportValue = new HashMap<Long, String>();
		String param = ";barCode=" + item.getBarCode();
		reportValue.put(new Long(0), "wmsBarCode.raq&raqParams=" + param);
		if(!reportValue.isEmpty()){
			result.put(IPage.REPORT_VALUES, reportValue);
			result.put(IPage.REPORT_PRINT_NUM, printNumber.intValue());
		}
		return result;
	}
	public void sysInventoryMissDo(List<Object[]> doObj,Map<String,String> itemMap){
		for(Object[] obj : doObj){
			WmsMisInventory mis = EntityFactory.getEntity(WmsMisInventory.class);
			mis.setCompanyName(obj[0].toString());
			mis.setSupCode(obj[1].toString());
			mis.setItemCode(obj[2].toString());
			mis.setExtendPropC1(obj[3].toString());
			mis.setInventoryQty(Double.valueOf(obj[4].toString()));
			mis.setMisQty(Double.valueOf(obj[5].toString()));
			mis.setWarehouse((WmsWarehouse) obj[6]);
			mis.setSupName(obj[7].toString());
			mis.setItemName(itemMap.get(mis.getItemCode()));
			commonDao.store(mis);
//			System.out.println(obj[0]+","+obj[1]+","+obj[2]+","+obj[3]+","+obj[4]+","+obj[5]);
		}
	}
}