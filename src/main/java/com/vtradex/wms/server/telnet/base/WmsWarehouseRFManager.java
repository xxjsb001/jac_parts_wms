package com.vtradex.wms.server.telnet.base;

import java.util.List;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWarehouseArea;
import com.vtradex.wms.server.model.warehouse.WmsWorkArea;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
import com.vtradex.wms.server.telnet.dto.WmsWorkAreaExtDTO;

/**
 * @author: 李炎
 */
public interface WmsWarehouseRFManager extends BaseManager {

	
	List<WmsWarehouse> getWmsAvailableWarehousesByUserId();
	
	/**
	 * 获得仓库下工作区
	 */
	List<WmsWorkArea> getWorkAreaByDefaultWarehouse();
	
	List<WmsWorker> getWmsWorker(Long userId, Long warehouseId);
	
	WmsWarehouseArea getWmsWarehouseArea(Long warehouseAreaId);
	
	WmsWorkAreaExtDTO getWmsWorkAreaExt(WmsWorkArea workArea);
}
