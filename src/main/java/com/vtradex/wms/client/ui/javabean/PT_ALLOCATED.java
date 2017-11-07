package com.vtradex.wms.client.ui.javabean;

import java.util.Date;
import com.google.gwt.user.client.rpc.IsSerializable;

public class PT_ALLOCATED implements IsSerializable {
	// 任务号
	private Long taskId;
	// 库存代号
	private String locationCode;
	// 货品代号
	private String itemCode;
	// 货品名称
	private String itemName;
	// 包装单位
	private String packageUnit;
	// 计划移位数量
	private double planQuantity;
	// 计划移位数量MU
	private double planQuantityBU;
	// 取消分配数量
	private Integer manualQuantity;
	// 批次属性
	private String lotInfor;
	// 存货日期
	private Date storageDate;
	private String soi;
	private String batchNum;
	private Date produceDate;
	private Date expireDate;
	private Date warnDate;
	private String supplier;

	public PT_ALLOCATED() {

	}

	public PT_ALLOCATED(Object taskId, Object locationCode, Object itemCode, Object itemName, 
			Object packageUnit, Object planQuantity, Object planQuantityBU, Object manualQuantity, 
			Object storageDate, Object soi, Object batchNum, Object produceDate, 
			Object expireDate, Object warnDate, Object supplier) {
		this.taskId = (Long) taskId;
		this.locationCode = (String) locationCode;
		this.itemCode = (String) itemCode;
		this.itemName = itemName == null ? "" : (String) itemName;
		this.packageUnit = (String) packageUnit;
		this.planQuantity = planQuantity == null ? 0D : (Double) planQuantity;
		this.planQuantityBU = planQuantityBU == null ? 0D : (Double) planQuantityBU;
		this.manualQuantity = 0;
		this.storageDate = (Date) storageDate;
		this.soi = (String) soi;
		this.batchNum = (String) batchNum;
		this.produceDate = (Date) produceDate;
		this.expireDate = (Date) expireDate;
		this.warnDate = (Date) warnDate;
		this.supplier = (String) supplier;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getPackageUnit() {
		return packageUnit;
	}

	public void setPackageUnit(String packageUnit) {
		this.packageUnit = packageUnit;
	}

	public double getPlanQuantity() {
		return planQuantity;
	}

	public void setPlanQuantity(double planQuantity) {
		this.planQuantity = planQuantity;
	}

	public double getPlanQuantityBU() {
		return planQuantityBU;
	}

	public void setPlanQuantityBU(double planQuantityBU) {
		this.planQuantityBU = planQuantityBU;
	}

	public double getManualQuantity() {
		return manualQuantity;
	}

	public void setManualQuantity(Integer manualQuantity) {
		this.manualQuantity = manualQuantity;
	}

	public String getLotInfor() {
		return lotInfor;
	}

	public void setLotInfor(String lotInfor) {
		this.lotInfor = lotInfor;
	}

	public Date getStorageDate() {
		return storageDate;
	}

	public void setStorageDate(Date storageDate) {
		this.storageDate = storageDate;
	}

	public String getSoi() {
		return soi;
	}

	public void setSoi(String soi) {
		this.soi = soi;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public Date getProduceDate() {
		return produceDate;
	}

	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public Date getWarnDate() {
		return warnDate;
	}

	public void setWarnDate(Date warnDate) {
		this.warnDate = warnDate;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
}