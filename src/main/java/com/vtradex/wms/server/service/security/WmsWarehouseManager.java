package com.vtradex.wms.server.service.security;

import java.util.List;
import java.util.Map;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWorkArea;

@SuppressWarnings("unchecked")
public interface WmsWarehouseManager extends BaseManager {
	/** 自动得到登陆用户名 */
	WmsWarehouse getDefaultLoginWmsWarehouse();
	
	Map getThisWarehouse(Map m);
	
	Map getWmsWareHousesForThorn(Map map);
	
	/**
	 * 跟据用户Id获得用户全部的仓库列表
	 * @param userId
	 * @return
	 */
	List<WmsWarehouse> getWmsAvailableWarehousesByUserId();
	
	/**
	 * 根据用户id去找优先级最高的仓库
	 * @param userId
	 * @return
	 */
	List<WmsWarehouse> getWmsAvailableWarehousesByUserId(Long userId);

	/**
	 * 获取指定仓库的盘点库位
	 * @param warehouse
	 * @return
	 */
	WmsLocation getCountLocationByWarehouse(WmsWarehouse warehouse);
	
	List<WmsWarehouse> getWareHouse(Long userId);
	
}