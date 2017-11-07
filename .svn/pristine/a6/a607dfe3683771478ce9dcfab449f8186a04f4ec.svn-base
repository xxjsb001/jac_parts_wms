package com.vtradex.wms.server.service.bean;

import com.vtradex.thorn.server.service.bean.ClientDisplayInfo;
import com.vtradex.thorn.server.web.security.UserHolder;

/**
 *@author <a href="kaisheng.liu@vtradex.com">刘开胜</a>
 *@version $Project:e2wms $Date: 2015/03/25 02:48:52 $Version:V1.1
 */
public class CurrentUserSession implements ClientDisplayInfo {

	public String getAllDisplayInfo() {
		return UserHolder.getUser().getLoginName();
	}

}
