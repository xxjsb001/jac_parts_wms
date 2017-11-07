package com.vtradex.wms.server.model.organization;

import com.vtradex.thorn.server.model.Entity;

/**
 * 枚举表
 * @author Administrator
 *
 */
public class WmsEnumType extends Entity{

	private static final long serialVersionUID = -5611463531104818928L;
	/**
	 * 枚举类型
	 */
	private String enumType;
	/**
	 * 枚举值
	 */
	private String enumValue;
	/**
	 * 枚举名称
	 */
	private String enumName;
	
	
	public WmsEnumType(){}
	public WmsEnumType(String enumType, String enumValue,String enumName) {
		super();
		this.enumType = enumType;
		this.enumValue = enumValue;
		this.enumName = enumName;
	}
	public String getEnumType() {
		return enumType;
	}
	public void setEnumType(String enumType) {
		this.enumType = enumType;
	}
	public String getEnumValue() {
		return enumValue;
	}
	public void setEnumValue(String enumValue) {
		this.enumValue = enumValue;
	}
	public String getEnumName() {
		return enumName;
	}
	public void setEnumName(String enumName) {
		this.enumName = enumName;
	}
}
