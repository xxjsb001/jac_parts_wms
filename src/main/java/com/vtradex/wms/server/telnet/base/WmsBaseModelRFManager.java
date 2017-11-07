package com.vtradex.wms.server.telnet.base;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.organization.WmsOrganization;

/**
 * 基础资料
 *
 * @category Manager
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:54 $
 */
public interface WmsBaseModelRFManager extends BaseManager {
	
	WmsOrganization getSupplier(String code);
}
