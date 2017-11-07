package com.vtradex.wms.server.service.base.pojo;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.service.base.WmsBillTypeManager;

/**
 * 单据类型管理
 *
 * @category Manager
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:52 $
 */
public class DefaultWmsBillTypeManager extends DefaultBaseManager implements WmsBillTypeManager {

	/**
	 * 获取单据类型
	 * 
	 * @param company 货主
	 * @param billType 单据类型
	 * @param code 单据代码
	 * @return
	 */
	public WmsBillType getWmsBillType(WmsOrganization company, String billType, String code) {
		String hql = " FROM WmsBillType wbt where wbt.company.id = :companyId and wbt.type = :type and wbt.code = :code";
		Object obj = commonDao.findByQueryUniqueResult(hql, new String[]{"companyId", "type", "code"}, new Object[]{company.getId(), billType, code});
		if (obj == null) {
			throw new BusinessException("wmsbilltype.isnull");
		}
		return (WmsBillType)obj;
	}
}
