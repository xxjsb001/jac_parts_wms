package com.vtradex.wms.server.service.bean;

import com.vtradex.thorn.server.service.bean.ClientDisplayInfo;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.UserHolder;

/**
 * @author: 李炎
 */

public class UserInfoInClient implements ClientDisplayInfo {

	public String getAllDisplayInfo() {
		return LocalizedMessage.getLocalizedMessage("current.user",UserHolder.getReferenceModel(),UserHolder.getLanguage())+UserHolder.getUser().getName();
	}
}
