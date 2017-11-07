package com.vtradex.wms.server.service.security.pojo;

import java.util.ArrayList;
import java.util.List;

import com.vtradex.thorn.server.model.security.Group;
import com.vtradex.thorn.server.model.security.User;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.service.security.ClientUserManager;

public class DefaultClientUserManager extends DefaultBaseManager implements ClientUserManager{
	/**
	 * 增加组用户
	 */
	public void addGroupUsers(Long groupId, List<Long> userIds) {
          Group group=commonDao.load(Group.class,groupId);
          List<User> users= new ArrayList<User>();
          for(Long userId:userIds){
        	  users.add(this.commonDao.load(User.class,userId));
          }
          group.getUsers().addAll(users);
          this.commonDao.store(group);          
		
	}

	public void cancelGroupUsers(Long groupId, List<Long> userIds) {
		Group group=commonDao.load(Group.class,groupId);
		List<User> users=new ArrayList<User>();
		for(Long userId:userIds){
			users.add(this.commonDao.load(User.class,userId));
		}
		group.getUsers().removeAll(users);
		this.commonDao.store(group);
	}

}
