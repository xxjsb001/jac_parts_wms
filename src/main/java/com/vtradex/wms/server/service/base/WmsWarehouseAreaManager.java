package com.vtradex.wms.server.service.base;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.warehouse.WmsWarehouseArea;

public interface WmsWarehouseAreaManager extends BaseManager {
	/**
	 * 保存库区
	 */
	@Transactional
	void storeWarehouseArea(WmsWarehouseArea wareHouseAreas);
}