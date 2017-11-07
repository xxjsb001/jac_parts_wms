package com.vtradex.wms.server.web.filter;

import com.vtradex.wms.server.model.warehouse.WmsWorker;

/**
 * 
 * @category 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:55 $
 */
public class WmsWorkerHolder {
	
	private static ThreadLocal<WmsWorker> wmsWorkerLocal = new InheritableThreadLocal<WmsWorker>();

	public static WmsWorker getWmsWorker() {
		return wmsWorkerLocal.get();
	}

	public static void setWmsWorker(WmsWorker wmsWorker) {
		wmsWorkerLocal.set(wmsWorker);
	}
}
