package com.vtradex.wms.server.model.inventory;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWorker;

public class WmsInventoryFee extends Entity {
	private static final long serialVersionUID = 1L;
	/** 仓库 */
	private WmsWarehouse warehouse;
	/** 客户 */
	private WmsOrganization company;
	/** 计费日期 */
	private Date feeDate;
	/** 费用项目 */
	private String feeType;
	/** 货品 */
	private WmsItem item;
	/** 数量 */
	private Double quantity = 0D;
	/** 重量 */
	private Double weight = 0D;
	/** 体积 */
	private Double volume = 0D;
	/** 托数 */
	private Double pallet = 0D;
	/** 费用 */
	private Double fee = 0D;
	/** 费率 */
	private Double feeRate = 0D;
	/** 备注 */
	private String description;
	
	/**
	 * 作业人员
	 */
	private WmsWorker worker;
	
	
	public WmsWorker getWorker() {
		return worker;
	}
	public void setWorker(WmsWorker worker) {
		this.worker = worker;
	}
	public WmsWarehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}
	public WmsOrganization getCompany() {
		return company;
	}
	public void setCompany(WmsOrganization company) {
		this.company = company;
	}
	public Date getFeeDate() {
		return feeDate;
	}
	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public WmsItem getItem() {
		return item;
	}
	public void setItem(WmsItem item) {
		this.item = item;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getVolume() {
		return volume;
	}
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	public Double getPallet() {
		return pallet;
	}
	public void setPallet(Double pallet) {
		this.pallet = pallet;
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
	public Double getFeeRate() {
		return feeRate;
	}
	public void setFeeRate(Double feeRate) {
		this.feeRate = feeRate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
