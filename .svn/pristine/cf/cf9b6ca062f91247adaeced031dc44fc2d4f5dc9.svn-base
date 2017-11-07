package com.vtradex.wms.server.model.inventory;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;


/**
 * @category 库存积数
 * @author peng.lei
 * @version 1.0
 * @created 15-二月-2011 10:06:58
 */
public class WmsInventoryCount extends Entity{

	private static final long serialVersionUID = -8917790749392690694L;
	
	/** 记账日期*/
	private Date recordDate;
	/** 仓库*/
	private WmsWarehouse warehouse;
	/** 货主*/
	private WmsOrganization company;
	/** 货品*/
	private WmsItem item;
	/** 数量BU*/
	private Double quantityBU;
	/** 重量*/
	private Double weight;
	/** 体积*/
	private Double volume;
	/** 托数*/
	private Double palletQuantity;	
	
	public WmsInventoryCount(){}

	public WmsOrganization getCompany() {
		return company;
	}

	public void setCompany(WmsOrganization company) {
		this.company = company;
	}

	public WmsItem getItem() {
		return item;
	}

	public void setItem(WmsItem item) {
		this.item = item;
	}

	public Double getPalletQuantity() {
		return palletQuantity;
	}

	public void setPalletQuantity(Double palletQuantity) {
		this.palletQuantity = palletQuantity;
	}

	public Double getQuantityBU() {
		return quantityBU;
	}

	public void setQuantityBU(Double quantityBU) {
		this.quantityBU = quantityBU;
	}
			
	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public void finalize() throws Throwable {

	}

}