package com.vtradex.wms.server.model.billing;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.organization.WmsOrganization;

@SuppressWarnings("serial")
/**合同明细：同一个结算小类，不可以出现重叠日期*/
public class WMSContactDetail extends Entity {
    //编码：默认WMSBillingCategory的code
	private String code;
	//所属合同
	private WMSContact contact;
	//供应商
	private WmsOrganization supplier;
	//结算小类
	private WMSBillingCategory smallCategory;
	//结算模式@WMSBillingModelInterface
	private String billingMode;
	//起始日期	不能超越Contact日期
	private Date startDate;
	//结束日期      不能超越Contact日期
	private Date endDate;
	//备注
	private String memo;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public WMSContact getContact() {
		return contact;
	}
	public void setContact(WMSContact contact) {
		this.contact = contact;
	}
	public WmsOrganization getSupplier() {
		return supplier;
	}
	public void setSupplier(WmsOrganization supplier) {
		this.supplier = supplier;
	}

	public WMSBillingCategory getSmallCategory() {
		return smallCategory;
	}
	public void setSmallCategory(WMSBillingCategory smallCategory) {
		this.smallCategory = smallCategory;
	}
	public String getBillingMode() {
		return billingMode;
	}
	public void setBillingMode(String billingMode) {
		this.billingMode = billingMode;
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
	
	
}
