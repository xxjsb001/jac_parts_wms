package com.vtradex.wms.server.web.filter;

import com.vtradex.thorn.server.security.acegi.holder.SecurityContextHolder;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.web.servlet.WMSLoginServlet;

public class WmsWarehouseHolder {
	
	private static ThreadLocal<WmsWarehouse> wmsWarehouses = new InheritableThreadLocal<WmsWarehouse>();

	public static WmsWarehouse getWmsWarehouse() {
		if (SecurityContextHolder.getCurrentSession() == null) {
			return wmsWarehouses.get();
		}
		return (WmsWarehouse) SecurityContextHolder.getCurrentSession().getAttribute(WMSLoginServlet.WMS_SESSION_WAREHOUSE);
	}

	public static void setWmsWarehouse(WmsWarehouse wmsWarehouse) {
		wmsWarehouses.set(wmsWarehouse);
	}

}
