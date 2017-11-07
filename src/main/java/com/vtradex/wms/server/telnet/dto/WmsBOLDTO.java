package com.vtradex.wms.server.telnet.dto;

import java.util.HashSet;
import java.util.Set;

public class WmsBOLDTO {
	
//	/**
//	 * 车牌号
//	 */
//	private String license;
	/**
	 * 器具编码
	 */
	private Set<String> containers = new HashSet<String>();
//	/**
//	 * 异常信息
//	 */
//	private String message;
//	/**
//	 * 发货单号
//	 */
//	private String pickCode;

//	public String getLicense() {
//		return license;
//	}
//
//	public void setLicense(String license) {
//		this.license = license;
//	}

	public Set<String> getContainers() {
		return containers;
	}

	public void setContainers(Set<String> containers) {
		this.containers = containers;
	}

//	public String getMessage() {
//		return message;
//	}
//
//	public void setMessage(String message) {
//		this.message = message;
//	}

//	public String getPickCode() {
//		return pickCode;
//	}
//
//	public void setPickCode(String pickCode) {
//		this.pickCode = pickCode;
//	}
	
}
