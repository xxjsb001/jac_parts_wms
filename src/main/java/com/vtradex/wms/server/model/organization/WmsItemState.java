package com.vtradex.wms.server.model.organization;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;

/**
 * @category 货品状态
 * @author shun.luo
 * @version 1.0
 * @created 26-四月-2011 15:22:46
 */

public class WmsItemState extends Entity {

	private static final long serialVersionUID = -911554073761692854L;

	/**
	 * 货主
	 */
	@UniqueKey
	private WmsOrganization company;
	
	/**
	 * 名称
	 */
	@UniqueKey
	private String name;
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 描述
	 */
	private String description;
	
	/**
	 * 是否可收
	 */
	private boolean beReceive = false;
	
	/**
	 * 是否可发
	 */
	private boolean beSend = false;
	
	/**
	 * 是否可质检
	 */
	private boolean beQuality = false;
	//=========================JAC=========================
	/**
	 * 是否返库
	 */
	private boolean beBackInv = true;
	/** 质检排序*/
	private Integer orderbyQuality = 0;
	/** 备用字段 */
	private String backInventoryState;
	
	public String getBackInventoryState() {
		return backInventoryState;
	}

	public void setBackInventoryState(String backInventoryState) {
		this.backInventoryState = backInventoryState;
	}

	public Integer getOrderbyQuality() {
		return orderbyQuality;
	}

	public void setOrderbyQuality(Integer validPeriod) {
		this.orderbyQuality = validPeriod;
	}
	public boolean isBeQuality() {
		return beQuality;
	}

	public void setBeQuality(boolean beQuality) {
		this.beQuality = beQuality;
	}

	public boolean isBeSend() {
		return beSend;
	}

	public void setBeSend(boolean beSend) {
		this.beSend = beSend;
	}

	public boolean isBeReceive() {
		return beReceive;
	}

	public void setBeReceive(boolean beReceive) {
		this.beReceive = beReceive;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isBeBackInv() {
		return beBackInv;
	}

	public void setBeBackInv(boolean beBackInv) {
		this.beBackInv = beBackInv;
	}

}
