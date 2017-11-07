package com.vtradex.wms.server.model.billing;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;

@SuppressWarnings("serial")
//合同
public class WMSContact extends Entity {
	//合同编码
	@UniqueKey
	private String code;
	//仓库
	@UniqueKey
	private WmsWarehouse warehouse;
	//供应商
	@UniqueKey
	private WmsOrganization supplier;
	//起始日期
	private Date startDate;
	//结束日期
	private Date endDate;
	//备注
	private String memo;
	//状态
	private String status;
	
    //支付方式:
	private String payType;
	
	//税点
	private Double taxPoints=0D;
	
	private Set<WMSContactDetail>details= new HashSet<WMSContactDetail>();
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public WmsWarehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}
	public WmsOrganization getSupplier() {
		return supplier;
	}
	public void setSupplier(WmsOrganization supplier) {
		this.supplier = supplier;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	public Set<WMSContactDetail> getDetails() {
		return details;
	}
	public void setDetails(Set<WMSContactDetail> details) {
		this.details = details;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public Double getTaxPoints() {
		return taxPoints;
	}
	public void setTaxPoints(Double taxPoints) {
		this.taxPoints = taxPoints;
	}
	
}
