package com.vtradex.wms.server.service.security.pojo;


import java.util.Locale;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.vtradex.thorn.server.model.security.User;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.service.security.UserManager;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.service.security.UserPasswordManager;

@SuppressWarnings("all")
public class DefaultUserPasswordManager extends DefaultBaseManager implements UserPasswordManager {
	protected final UserManager userManager;
	
	public DefaultUserPasswordManager(UserManager userManager){
		this.userManager=userManager;
	}
	
	public String getUserName(Map map) {
		return UserHolder.getUser().getName();
	}

	public void saveUser(User user) {
		if (user.isNew()) {
			user.setPassword(DigestUtils.shaHex(user.getPassword()));
		} else {
			User dbUser = commonDao.load(User.class, user.getId());
			
			if (!dbUser.getPassword().equals(user.getPassword())) {
				user.setPassword(DigestUtils.shaHex(user.getPassword()));
			}
		}
		
		user.setLocale(Locale.CHINESE);
		
		this.store(user);
	}
}