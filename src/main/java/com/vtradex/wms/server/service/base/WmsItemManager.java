package com.vtradex.wms.server.service.base;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.base.LotInfo;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;

@SuppressWarnings("rawtypes")
public interface WmsItemManager extends BaseManager {
	
	@Transactional
	void addPackageUnit(Long itemId,WmsPackageUnit packageUnit);
	
	/**
	 * 根据货品和批次属性获取对应的itemKey
	 * @param warehouse
	 * @param soi
	 * @param item
	 * @param lotInfo
	 * @return
	 */
	@Transactional
	WmsItemKey getItemKey(WmsWarehouse warehouse, String soi, WmsItem item, LotInfo lotInfo);
	
	@Transactional
	WmsItemKey getItemKey(WmsWarehouse warehouse, String soi, WmsItem item, LotInfo lotInfo, Boolean beVerifyLotInfo);
	
	/**
	 * 获取ItemKey
	 * @param itemKey
	 * @return
	 */
	@Transactional
	WmsItemKey getItemKey(WmsItemKey itemKey);
	
	/**
	 * 获取批次属性要求
	 * @param map
	 * @return
	 */
	@Transactional
	List<Boolean> getLotRuleTackersByPageMap(Map map);
	
	/**
	 * 单一明细收货获取货品批次属性要求
	 * @param map
	 * @return
	 */
	@Transactional
	List<Boolean> getLotRuleTrackersByASNDetailMap(Map map);
	
	/**
	 * 获取发货批次属性要求
	 * @param map
	 * @return
	 */
	@Transactional
	List<Boolean> getShipLotRuleTackersByPageMap(Map map);
	
	/**
	 * 获取批次属性要求
	 * @param itemId
	 * @return
	 */
	@Transactional
	List<Boolean> getLotRuleTackers(Long itemId);
	
	@Transactional
	List<Boolean> getShipLotRuleTackers(Long itemId);
	
	/**
	 * 判断批次规则是否被用
	 * @param 
	 * @return
	 */
	@Transactional
	Boolean isContainLotRule(long lotRuleId);
	
	/** 打印条码 */ 
	Map printBarCode(WmsItem item,Long printNumber);
	/**报缺库存物料**/
	@Transactional
	void sysInventoryMissDo(List<Object[]> doObj,Map<String,String> itemMap);
	/**物料拣选分类维护**/
	void importItemClass2(File file);
}