package com.vtradex.wms.server.web.filter;

import com.vtradex.wms.server.telnet.dto.WmsWorkAreaExtDTO;

/**
 * 工作区扩展
 *
 * @category Holder
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:55 $
 */
public class WmsWorkAreaExtHolder {
	
	private static ThreadLocal<WmsWorkAreaExtDTO> wmsWorkAreaExtLocal = new InheritableThreadLocal<WmsWorkAreaExtDTO>();
	
	public static WmsWorkAreaExtDTO getWmsWorkAreaExt() {
		return wmsWorkAreaExtLocal.get();
	}

	public static void setWmsWorkAreaExt(WmsWorkAreaExtDTO wmsWorkAreaExt) {
		wmsWorkAreaExtLocal.set(wmsWorkAreaExt);
	}
}
