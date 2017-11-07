package com.vtradex.wms.server.web.filter;

import com.vtradex.wms.server.model.warehouse.WmsWorkArea;

/**
 * 
 *
 * @category 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:55 $
 */
public class WmsWorkAreaHolder {
	
	private static ThreadLocal<WmsWorkArea> wmsWorkAreas = new InheritableThreadLocal<WmsWorkArea>();
	
	public static WmsWorkArea getWmsWorkArea() {
		return wmsWorkAreas.get();
	}

	public static void setWmsWorkArea(WmsWorkArea wmsWorkArea) {
		wmsWorkAreas.set(wmsWorkArea);
	}
}
