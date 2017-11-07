package com.vtradex.wms.server.service.bean;


import com.vtradex.thorn.server.service.bean.base.BaseParamBean;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

public class WmsWarehouseSession extends BaseParamBean{

	public WmsWarehouse getServerGlobalParamValue() {
		return WmsWarehouseHolder.getWmsWarehouse();
	}

	public Long getClientGlobalParamValue() {
		return WmsWarehouseHolder.getWmsWarehouse().getId();
	}
}
