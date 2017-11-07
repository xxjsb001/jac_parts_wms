package com.vtradex.wms.server.model.organization;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;

public class WmsTransState extends Entity {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8073575867673907479L;
	
	/**所属仓库*/
	@UniqueKey
	private WmsWarehouse warehouse;
	
	/**运输节点名称*/
	@UniqueKey
	private String name;
	
	/**运输节点描述*/
	private String description;
	
	/**运输节点失效状态*/
	private String status = BaseStatus.ENABLED; 
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public WmsWarehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}
}
