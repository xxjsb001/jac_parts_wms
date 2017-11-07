package com.vtradex.wms.server.service.security;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.model.security.User;
import com.vtradex.thorn.server.service.BaseManager;

/**
 * @author: 李炎
 */
@SuppressWarnings("unchecked")
public interface UserPasswordManager extends BaseManager{
	/**
	 * 自动得到登陆用户名
	 */
	@Transactional
	String getUserName(Map map);
	
	/**
	 * 保存用户
	 */
	@Transactional
	void saveUser(User user);
}
