package com.vtradex.wms.server.telnet.base.pojo;

import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.telnet.base.WmsBaseModelRFManager;

/**
 * 
 *
 * @category Manager
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:54 $
 */
public class DefaultWmsBaseModelRFManager extends DefaultBaseManager implements WmsBaseModelRFManager {
	
	public WmsOrganization getSupplier(String code) {
		String hql = "FROM WmsOrganization s where s.code = :code and s.status = 'ENABLED' and s.beSupplier = true";
		Object obj = commonDao.findByQueryUniqueResult(hql, "code", code);
		if (obj != null) {
			return (WmsOrganization)obj;
		}
		return null;
	}
}
