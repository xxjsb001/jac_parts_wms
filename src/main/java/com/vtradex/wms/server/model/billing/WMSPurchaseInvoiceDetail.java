package com.vtradex.wms.server.model.billing;

import java.util.Date;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;

/**发动机采购发票*/
@SuppressWarnings("serial")
public class WMSPurchaseInvoiceDetail extends Entity {
	//仓库	
	private WmsWarehouse warehouse;
	//供应商
	@UniqueKey
	private WmsOrganization supplier;
	//采购发票号
	@UniqueKey
	private String code;
	//金额
	private Double amount=0D;
	//发票日期
	private Date invoiceDate;
	
	//状态：WMSPurchaseStatus :生效状态下的才可以参与计费
	private String status;
	
	//billDetail回写时，根据回传的采购发票号，将billDetailId填写，报表使用
	private Long billDetailId;
	
	//备注
	private String memo;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getBillDetailId() {
		return billDetailId;
	}

	public void setBillDetailId(Long billDetailId) {
		this.billDetailId = billDetailId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
}
