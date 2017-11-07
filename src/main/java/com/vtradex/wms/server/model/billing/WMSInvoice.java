package com.vtradex.wms.server.model.billing;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.utils.DoubleUtils;

@SuppressWarnings("serial")
/**发票信息*/
public class WMSInvoice extends Entity {
	//发票流水号
	private String serialCode ;
	//仓库
	private WmsWarehouse warehouse;
	//供应商
	private WmsOrganization supplier;
     //合同
	private WMSContact contact;
	//会计科目
	private WMSBillingCategory accountingSubject;
	//开票日期
	private Date invoiceDate;
	//系统费用
	private Double sumSysAmount=0D;
	/**人工费用**/
	private Double sumManualAmount=0D;
	/**税点**/
	private Double taxingPoint=0.06D;
	/**税金(税前) = 人工费用%税点**/
	private Double tax=0D; 
	/**已支付税金*/
	private Double paidTax=0D;
	//对方已付款金额
	private Double paidAmount=0D;
	//开票人
	private String makeupPerson;
	//发票号
	private String code;
	
	//@WMSInvoiceStatusInterface未审核，已审核，已开票，已支付
	private String status;
    //支付方式:WMSInvoicePayTypeInterface
	private String payType;
	
	//可以为空
	private Date startDate;
	//可以为空
	private Date endDate;
    
	//开票涉及类别
	private Set<WMSInvoiceDetailCategory> categories = new HashSet<WMSInvoiceDetailCategory>();
	
	//备注
	private String memo;
	
	//账单期
	private String billScope;
	
	//开票备注
	private String makeUpMemo;

	
	public String getSerialCode() {
		return serialCode;
	}

	public void setSerialCode(String serialCode) {
		this.serialCode = serialCode;
	}

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public WMSContact getContact() {
		return contact;
	}

	public void setContact(WMSContact contact) {
		this.contact = contact;
	}

	public WMSBillingCategory getAccountingSubject() {
		return accountingSubject;
	}

	public void setAccountingSubject(WMSBillingCategory accountingSubject) {
		this.accountingSubject = accountingSubject;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Double getSumSysAmount() {
		return sumSysAmount;
	}

	public void setSumSysAmount(Double sumSysAmount) {
		this.sumSysAmount = DoubleUtils.format2Fraction(sumSysAmount);
	}

	public Double getSumManualAmount() {
		return sumManualAmount;
	}

	public void setSumManualAmount(Double sumManualAmount) {
		this.sumManualAmount =  DoubleUtils.format2Fraction(sumManualAmount);
	}

	public Double getTaxingPoint() {
		return taxingPoint;
	}

	public void setTaxingPoint(Double taxingPoint) {
		this.taxingPoint = taxingPoint;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = DoubleUtils.formatByPrecision(this.sumManualAmount % this.taxingPoint, 2);
	}

	public Double getPaidTax() {
		return paidTax;
	}

	public void setPaidTax(Double paidTax) {
		this.paidTax = paidTax;
	}

	public String getMakeupPerson() {
		return makeupPerson;
	}

	public void setMakeupPerson(String makeupPerson) {
		this.makeupPerson = makeupPerson;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
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

	public Set<WMSInvoiceDetailCategory> getCategories() {
		return categories;
	}

	public void setCategories(Set<WMSInvoiceDetailCategory> categories) {
		this.categories = categories;
	}

	

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getBillScope() {
		return billScope;
	}

	public void setBillScope(String billScope) {
		this.billScope = billScope;
	}

	public WmsOrganization getSupplier() {
		return supplier;
	}

	public void setSupplier(WmsOrganization supplier) {
		this.supplier = supplier;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}
	
	
	
	public String getMakeUpMemo() {
		return makeUpMemo;
	}

	public void setMakeUpMemo(String makeUpMemo) {
		this.makeUpMemo = makeUpMemo;
	}

	public void addInvoiceDetails(List<WMSInvoiceDetailCategory> details){
		if(0 == details.size()){
			return ;
		}
		for(WMSInvoiceDetailCategory invoiceDetail : details){
			invoiceDetail.setInvoice(this);
			this.getCategories().add(invoiceDetail);
		}
		this.calculator();
	}
	
	public void calculator(){
		Double sumSysAmount=0d,sumManualAmount=0d;
		for(WMSInvoiceDetailCategory invoiceDetail : this.categories){
			sumSysAmount+=invoiceDetail.getSumSysAmount();
			sumManualAmount+=invoiceDetail.getSumManualAmount();
		}
		this.setSumManualAmount(sumManualAmount);
		this.setSumSysAmount(sumSysAmount);
	}
	
	public void afterPropertySet(){
		if(null == this.sumManualAmount){
			this.sumManualAmount =0D;
		}
		if(null == this.sumSysAmount){
			this.sumSysAmount=0D;
		}
		if(null == this.tax){
			this.tax=0D;
		}
		if(null == this.paidAmount){
			this.paidAmount=0D;
		}
		if(null == this.paidTax){
			this.paidTax=0D;
		}
	}
	
}
