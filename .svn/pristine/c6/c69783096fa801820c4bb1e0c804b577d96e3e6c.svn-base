package com.vtradex.wms.server.model.carrier;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.thorn.server.model.enumerate.BaseStatus;

public class WmsCity extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7538631050472417246L;
	
	
	/**
	 * 编码
	 */
	private String code;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 省份
	 */
	private WmsProvince province;
	
	/**
	 * 城市级别
	 * {@link WmsCityLevel}
	 */
	private String cityLevel = WmsCityLevel.PREFECTURE;
	
	/**
	 * 状态
	 * {@link BaseStatus}
	 */
	private String status = BaseStatus.ACTIVE;
	
	/**
	 * 描述
	 */
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
	public WmsProvince getProvince() {
		return province;
	}
	public void setProvince(WmsProvince province) {
		this.province = province;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCityLevel() {
		return cityLevel;
	}
	public void setCityLevel(String cityLevel) {
		this.cityLevel = cityLevel;
	}
}
