package com.vtradex.wms.server.model.billing;

import java.util.Date;

public class WMSBillDetailWrap {
	private WMSBillDetail bill;
	//发生日期
	private Date happenDate;
	
	public WMSBillDetailWrap(WMSBillDetail bill, Date happenDate) {
		super();
		this.bill = bill;
		this.happenDate = happenDate;
	}
	public WMSBillDetail getBill() {
		return bill;
	}
	public void setBill(WMSBillDetail bill) {
		this.bill = bill;
	}
	public Date getHappenDate() {
		return happenDate;
	}
	public void setHappenDate(Date happenDate) {
		this.happenDate = happenDate;
	}
	
}
