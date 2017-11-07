package com.vtradex.wms.server.service.security;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;


public interface WmsInventoryViewManager {
	/**
	 * 构造所有仓库的库存可视化信息
	 * @param params
	 * @return
	 */
	/**
	 * 计算各仓库库存容积
	 * @param params
	 * @return
	 */
	Map init_WM_IV(Map params);
	
	@Transactional
	Map update_WM_IV(Map params);
	
	@Transactional
	Map update_WM_ZIV(Map params);
	
	/**
	 * 根据计算各库区的库存容积
	 * @param params
	 * @return
	 */
	Map init_Zone_IV(Map params);
	
	/**
	 * 根据计算库区内区的库存容积
	 * @param params
	 * @return
	 */
	Map init_Region_IV(Map params);
	
	/**
	 * 计算各库位的库存容积
	 * @param params
	 * @return
	 */
	Map init_Location_IV(Map params);
	
	Map init_Location_RC_IV(Map params);
	
	/**
	 * 查询库存信息
	 * @param params
	 * @return
	 */
	Map init_Inventory_Data(Map params);
}
