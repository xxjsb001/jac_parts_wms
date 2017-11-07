package com.vtradex.wms.server.model.billing;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;

@SuppressWarnings("serial")
// 结算类型
public class WMSBillingCategory extends Entity {

	//父类型
	private WMSBillingCategory parentCategory;
	//类型@WMSBillingCategoryTypeInterface
	private String type;
	//名称
	private String name;
	//编码
	@UniqueKey
	private String code;
	//备注
	private String memo;
	//状态@BaseStatus.ENABLED\DISABLED
	private  String status;
	//是否成本
	private Boolean isCost=Boolean.FALSE;
	
	//税点
	private Double taxingPoint=0.06D;
	
	public WMSBillingCategory getParentCategory() {
		return parentCategory;
	}
	public void setParentCategory(WMSBillingCategory parentCategory) {
		this.parentCategory = parentCategory;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Boolean getIsCost() {
		return isCost;
	}
	public void setIsCost(Boolean isCost) {
		this.isCost = isCost;
	}
	public Double getTaxingPoint() {
		return taxingPoint;
	}
	public void setTaxingPoint(Double taxingPoint) {
		this.taxingPoint = taxingPoint;
	}
	
	
	
}
