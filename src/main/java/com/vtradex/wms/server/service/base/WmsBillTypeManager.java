package com.vtradex.wms.server.service.base;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsOrganization;

/**
 * 单据类型管理
 *
 * @category Manager
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:52 $
 */
public interface WmsBillTypeManager extends BaseManager {
	
	/**
	 * 获取单据类型
	 * 
	 * @param company 货主
	 * @param billType 单据类型
	 * @param code 单据代码
	 * @return
	 */
	WmsBillType getWmsBillType(WmsOrganization company, String billType, String code);
}
