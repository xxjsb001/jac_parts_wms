package com.vtradex.wms.server.telnet.shipping;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.BaseManager;

/**
 * 
 *
 * @category Manager
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2015/03/25 02:48:55 $
 */
public interface WmsShippingRFManager extends BaseManager {
	
	/**
	 * 按面单发运
	 * 
	 * @param boxNo 面单包装箱号
	 */
	public void shippingByBoxNo(String boxNo) throws BusinessException;
}
