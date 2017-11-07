package com.vtradex.wms.server.model.organization;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
/**质检单据状态表*/
public class WmsQualityBillStatus extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 货主*/
	@UniqueKey
	private WmsOrganization company;
	
	/** 单据类型 */
	@UniqueKey
	private WmsBillType billType;
	
	/** 库存状态 */
	@UniqueKey
	private WmsItemState itemState;
	
	/** 互斥状态*/
	private String repelStatus;
	
	/** 描述*/
	private String description;
	
	/** 库存状态 */
	private WmsItemState backInventoryState;
	
	public WmsItemState getBackInventoryState() {
		return backInventoryState;
	}

	public void setBackInventoryState(WmsItemState backInventoryState) {
		this.backInventoryState = backInventoryState;
	}

	public String getRepelStatus() {
		return repelStatus;
	}

	public void setRepelStatus(String repelStatus) {
		this.repelStatus = repelStatus;
	}

	public WmsOrganization getCompany() {
		return company;
	}

	public void setCompany(WmsOrganization company) {
		this.company = company;
	}

	public WmsBillType getBillType() {
		return billType;
	}

	public void setBillType(WmsBillType billType) {
		this.billType = billType;
	}

	public WmsItemState getItemState() {
		return itemState;
	}

	public void setItemState(WmsItemState itemState) {
		this.itemState = itemState;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
