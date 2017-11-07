package com.vtradex.wms.server.service.security.pojo;

import java.util.Locale;

import org.apache.commons.codec.digest.DigestUtils;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.security.User;
import com.vtradex.thorn.server.service.security.pojo.DefaultUserManager;

public class DefaultWmsUserManager extends DefaultUserManager {
	public void saveUser(User user,Locale locale) {
		if(this.retrieve(user.getLoginName()) != null && user.isNew()) {
			throw new BusinessException("user.already.exsits", new String[]{user.getLoginName()});
		}
		User dbuser;
		if (user.isNew()) {
			dbuser = user;
			dbuser.enable();
		}else{
			dbuser = load(User.class, user.getId());
		}
		if(user.getFirstPage() != null && user.getFirstPage().getId() != null) {
			dbuser.setFirstPage(user.getFirstPage());
		} else {
			dbuser.setFirstPage(null);
		}
		dbuser.setPassword(shaEncodePassword(user.getPassword()));
		dbuser.setStrExtend1(user.getStrExtend1());
		dbuser.setStrExtend2(user.getStrExtend2());
		dbuser.setLocale(locale);
		dbuser.setExpiryDate(user.getExpiryDate());
		dbuser.setPasswordExpiryDate(user.getPasswordExpiryDate());
		dbuser.setEmail(user.getEmail());
		dbuser.setLoginName(user.getLoginName());
		dbuser.setName(user.getName());
		dbuser.setReferenceModel(user.getReferenceModel());
		dbuser.setLongExtend1(user.getLongExtend1());
		dbuser.setStrExtend3(user.getStrExtend3());
		if(user.getLongExtend1() == null || user.getLongExtend1().longValue() == 0){
			dbuser.setStrExtend3("");
		}
		this.commonDao.store(dbuser);
	}
	
	private String shaEncodePassword(String painPwd){
		painPwd = painPwd == null ? "" : painPwd;
		if(painPwd.length() == 40){
			return painPwd;
		}
		return DigestUtils.shaHex(painPwd);
	}
}
