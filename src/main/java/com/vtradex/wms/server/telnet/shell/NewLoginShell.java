package com.vtradex.wms.server.telnet.shell;

import com.vtradex.kangaroo.shell.LoginShell;
import com.vtradex.thorn.server.model.security.User;
import com.vtradex.thorn.server.security.acegi.holder.SecurityContextHolder;
import com.vtradex.thorn.server.service.security.SecurityFilter;
import com.vtradex.thorn.server.service.security.UserManager;

/**
 * @author: 李炎
 */
public class NewLoginShell extends LoginShell {
	public NewLoginShell(UserManager userManager) {
		super(userManager);
	}
	public void afterPropertySet(User user){
		SecurityFilter sf = userManager.getSecurityFilter(user,user.getReferenceModel(),user.getLocale().getLanguage());
//		sf.addHql("switchWareHousePage.loadWarehouseDefault", "FROM WmsWarehouse wh WHERE 1=1 AND wh.status = 'ENABLED'");
		sf.addHql("switchWareHousePage.loadWarehouseDefault", warehouseHql(user));
		SecurityContextHolder.setRFSecurityFilter(sf);
	}
	
	public String warehouseHql(User user){
		String hql = "";
		if("admin".equals(user.getLoginName())){
			hql = "FROM WmsWarehouse wh WHERE 1=1 AND wh.status = 'ENABLED'";
		}else{
			hql = "FROM WmsWarehouseAndUser w "
					+ "WHERE w.user.id="+user.getId() +" order by priority asc";
		}
		return hql;
	}
	
//	public void checkWarehouse(User loginUser) throws ContinueException{
//		final List<WmsWarehouse> whs = queryWarehouse(loginUser);
//		if(whs.size() <= 0){
//			reset("用户"+loginUser.getName()+"没有在仓库用户关系表里维护,无法登录");
//		}
//	}
}
