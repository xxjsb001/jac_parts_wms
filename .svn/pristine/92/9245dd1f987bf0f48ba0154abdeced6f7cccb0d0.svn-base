package com.vtradex.wms.server.model.billing;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.utils.DoubleUtils;
/**发票明细类别，仓库开票时需要选择费用小类，后续可以灵活增加类别*/
@SuppressWarnings("serial")
public class WMSInvoiceDetailCategory extends Entity {

	//仓库
	private WmsWarehouse warehouse;
	//发票
	@UniqueKey
	private WMSInvoice invoice;
	//结算小类
	@UniqueKey
	private WMSBillingCategory smallCategory;
	
	//开始日期默认为空，后续开关控制,不为空前提，发票头开始日期不为空
	private Date startDate;
	
	//结束日期默认为空，后续开关控制，不为空前提，发票头结束日期不为空
	private Date endDate;
	
	//备注
	private String memo;
	
	//费率
	private Double rate=0D;
	
	//数量或者金额
	private Double qty_amount=0D;
	//系统费用
	private Double sumSysAmount=0D;
	//人工费用
	private Double sumManualAmount=0D;
	
	//账单明细
	private Set<WMSBillDetail>billDetails = new HashSet<WMSBillDetail>();
	

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
		this.sumManualAmount = DoubleUtils.format2Fraction(sumManualAmount);
	}

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public WMSInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(WMSInvoice invoice) {
		this.invoice = invoice;
	}

	public WMSBillingCategory getSmallCategory() {
		return smallCategory;
	}

	public void setSmallCategory(WMSBillingCategory smallCategory) {
		this.smallCategory = smallCategory;
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
	
	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getQty_amount() {
		return qty_amount;
	}

	public void setQty_amount(Double qty_amount) {
		this.qty_amount = qty_amount;
	}

	public Set<WMSBillDetail> getBillDetails() {
		return billDetails;
	}

	public void setBillDetails(Set<WMSBillDetail> billDetails) {
		this.billDetails = billDetails;
	}

	public void addBillDetails(List<WMSBillDetail> details){
		if(0 == details.size()){
			return ;
		}
		for(WMSBillDetail billDetail : details){
			billDetail.setInvoiceDetail(this);
			this.getBillDetails().add(billDetail);
		}
		this.calculator();
	}
	
	public void calculator(){
		Double sumAmount=0d;
		for(WMSBillDetail billDetail : this.billDetails){
			sumAmount+=billDetail.getAmount();
		}
		this.sumSysAmount = DoubleUtils.format2Fraction(sumAmount);
		this.sumManualAmount = this.sumSysAmount;
	}
}
