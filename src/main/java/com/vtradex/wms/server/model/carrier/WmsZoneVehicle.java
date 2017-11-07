package com.vtradex.wms.server.model.carrier;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.thorn.server.model.enumerate.BaseStatus;

public class WmsZoneVehicle  extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8237100089559544421L;
	/** 编码*/
	private String code;
	/** 名称*/
	private String name;
	/** 
	 * 状态
	 * {@link BaseStatus}
	 */
	private String status = BaseStatus.ACTIVE;
	/** 描述*/
	private String description;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
