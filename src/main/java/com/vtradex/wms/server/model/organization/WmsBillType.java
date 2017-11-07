package com.vtradex.wms.server.model.organization;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;

/**
 * @category 单据类型
 * @author shengpei.zhang
 * @version 1.0
 * @created 15-二月-2011 10:05:46
 */
public class WmsBillType extends Entity{
	
	private static final long serialVersionUID = 5384566798945250641L;
	
	/** 货主*/
	@UniqueKey
	private WmsOrganization company;
	
	/** 单据类型编码*/
	@UniqueKey
	private String code;
	
	/** 单据类型名称*/
	private String name;
	
	/**
	 * 业务类型：收货、发货、移位
	 * 
	 * {@link TypeOfBill}
	 */
	private String type;
	
	/** 是否内置单据类型*/
	private Boolean beInner = Boolean.FALSE;
	
	/** 描述*/
	private String description;
	
	/** 状态*/
	private String status;
	//***********JAC****************
	/** 是否同一单号--质检类型下使用*/
	private Boolean beSameASN = Boolean.FALSE;
	
	public Boolean getBeSameASN() {
		return beSameASN;
	}

	public void setBeSameASN(Boolean beSameASN) {
		this.beSameASN = beSameASN;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Boolean getBeInner() {
		return beInner;
	}

	public void setBeInner(Boolean beInner) {
		this.beInner = beInner;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public WmsOrganization getCompany() {
		return company;
	}

	public void setCompany(WmsOrganization company) {
		this.company = company;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public WmsBillType(){

	}

	public void finalize() throws Throwable {
	}
}