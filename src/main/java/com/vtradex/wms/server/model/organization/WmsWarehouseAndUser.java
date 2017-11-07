package com.vtradex.wms.server.model.organization;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.thorn.server.model.security.User;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
/**
 * 用户对应的仓库优先级,登录默认进入最高优先级的仓库
 * @author fs
 *
 */
public class WmsWarehouseAndUser extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6990514622450353336L;

	//仓库	
	private WmsWarehouse warehouse;
	
	//用户
	private User user;
	
	//优先级
	private Integer priority;

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	
}
