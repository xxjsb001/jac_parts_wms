package com.vtradex.wms.server.model.billing;

import java.util.Date;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.utils.DoubleUtils;

@SuppressWarnings("serial")
//账单明细:字段不全，接口开发时需要增加相应字段，前提是尽量将接口字段全部抓过来
public class WMSBillDetail extends Entity {

	//编码
	@UniqueKey
	private String code;
	//仓库
	private WmsWarehouse warehouse;
	//仓库代码
	private String wmsWarehouseCode;
	//供应商
	private WmsOrganization supplier;
	//供应商代码
	private String supplierCode;
	//供应商名称
	private String supplierName;
	//费用类型：name
	private String billingSmallCategory;
	//结算模式 :@WMSBillingModelInterface
	private String billingModel;
	//费用金额
	private Double amount=0D;
	//历史金额,接口回传时，默认amount
	private Double historyAmount=0D;
	//发生日期
	private Date happenDate;
	//关联发票,创建发票时，关联发票明细
	private WMSInvoiceDetailCategory invoiceDetail;
	//状态：BillStatus未审核、已审核
	private String status;
	//备注
	private String memo;
	//采购发票号，回传时需要根据purchaseInvoiceCode，关联对应的采购发票,形如：1234 或者 123#1234  
	private String purchaseInvoiceCode;
	
	//来源:BillTypeFrom.WMS\LFCS
	private String billfromType="LFCS";
	//零部件编码\托盘编码
	private String materialCode;
	//数量(采购发票金额、托盘个数、配送个数、分选个数、不合格品个数、退货个数、管理工装个数)
	private Double qty_Amount=0D;
	//费率
	private Double rate=0D;
	
	//合同编码
	private String wmsContactCode;
	
	//一口价
	private Double fixedPrice=0D;
	
	//LFCS id
	private Long  lfcsDataId;
	
	//LFCS 生成时间
	private Date lfcsCreateTime;
	
	
	
	public Long getLfcsDataId() {
		return lfcsDataId;
	}
	public void setLfcsDataId(Long lfcsDataId) {
		this.lfcsDataId = lfcsDataId;
	}
	public Date getLfcsCreateTime() {
		return lfcsCreateTime;
	}
	public void setLfcsCreateTime(Date lfcsCreateTime) {
		this.lfcsCreateTime = lfcsCreateTime;
	}
	public String getWmsContactCode() {
		return wmsContactCode;
	}
	public void setWmsContactCode(String wmsContactCode) {
		this.wmsContactCode = wmsContactCode;
	}
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
	public String getWmsWarehouseCode() {
		return wmsWarehouseCode;
	}
	public void setWmsWarehouseCode(String wmsWarehouseCode) {
		this.wmsWarehouseCode = wmsWarehouseCode;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getBillingSmallCategory() {
		return billingSmallCategory;
	}
	public void setBillingSmallCategory(String billingSmallCategory) {
		this.billingSmallCategory = billingSmallCategory;
	}
	public String getBillingModel() {
		return billingModel;
	}
	public void setBillingModel(String billingModel) {
		this.billingModel = billingModel;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = DoubleUtils.format2Fraction(amount);
	}
	public Double getHistoryAmount() {
		return historyAmount;
	}
	public void setHistoryAmount(Double historyAmount) {
		this.historyAmount = DoubleUtils.format2Fraction(historyAmount);
	}
	public Date getHappenDate() {
		return happenDate;
	}
	public void setHappenDate(Date happenDate) {
		this.happenDate = happenDate;
	}
	
	
	public WMSInvoiceDetailCategory getInvoiceDetail() {
		return invoiceDetail;
	}
	public void setInvoiceDetail(WMSInvoiceDetailCategory invoiceDetail) {
		this.invoiceDetail = invoiceDetail;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getPurchaseInvoiceCode() {
		return purchaseInvoiceCode;
	}
	public void setPurchaseInvoiceCode(String purchaseInvoiceCode) {
		this.purchaseInvoiceCode = purchaseInvoiceCode;
	}
	public String getBillfromType() {
		return billfromType;
	}
	public void setBillfromType(String billfromType) {
		this.billfromType = billfromType;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public Double getQty_Amount() {
		return qty_Amount;
	}
	public void setQty_Amount(Double qty_Amount) {
		this.qty_Amount = DoubleUtils.formatByPrecision(qty_Amount,2);
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = DoubleUtils.formatByPrecision(rate,6);
	}
	
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
	public Double getFixedPrice() {
		return fixedPrice;
	}
	public void setFixedPrice(Double fixedPrice) {
		this.fixedPrice = DoubleUtils.format2Fraction(fixedPrice);
	}
	public WmsOrganization getSupplier() {
		return supplier;
	}
	public void setSupplier(WmsOrganization supplier) {
		this.supplier = supplier;
	}
	
}
