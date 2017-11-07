package com.vtradex.wms.server.model.count;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.utils.DateUtil;

/**
 * @category 盘点表
 * @author peng.lei
 * @version 1.0
 * @created 16-二月-2011 22:08:42
 */
public class WmsCountRecord extends Entity{
	private static final long serialVersionUID = -1890324458712143642L;
	
	/** 盘点计划*/
	private WmsCountPlan countPlan;
	/** 库存ID */
	private Long inventoryId;
	/** 盘点库位ID */
	private Long locationId;
	/** 盘点库位 */
	private String locationCode;
	/** 托盘 */
	private String pallet;
	/** 箱号 */
	private String carton;
	/** 序列号 */
	private String serialNo;
	/** 货品 */
	private WmsItem item;
	/** 批次属性*/
	private WmsItemKey itemKey;
	/** 包装单位*/
	private WmsPackageUnit packageUnit;
	/** 库存数 */
	private Double quantityBU;
	/** 盘点数*/
	private Double countQuantityBU=0.0;
	/** 差异数量BU*/
	private Double deltaQuantityBU=0.0;
	/** 盘点标志*/
	private Boolean beCounted = Boolean.FALSE;
	
	public WmsCountRecord(){

	}
	
	public WmsItem getItem() {
		return item;
	}

	public void setItem(WmsItem item) {
		this.item = item;
	}

	public Long getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}

	public Boolean getBeCounted() {
		return beCounted;
	}

	public void setBeCounted(Boolean beCounted) {
		this.beCounted = beCounted;
	}

	public WmsCountPlan getCountPlan() {
		return countPlan;
	}

	public void setCountPlan(WmsCountPlan countPlan) {
		this.countPlan = countPlan;
	}

	public Double getDeltaQuantityBU() {
		return deltaQuantityBU;
	}

	public void setDeltaQuantityBU(Double deltaQuantityBU) {
		this.deltaQuantityBU = deltaQuantityBU;
	}

	public WmsItemKey getItemKey() {
		return itemKey;
	}

	public void setItemKey(WmsItemKey itemKey) {
		this.itemKey = itemKey;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public WmsPackageUnit getPackageUnit() {
		return packageUnit;
	}

	public void setPackageUnit(WmsPackageUnit packageUnit) {
		this.packageUnit = packageUnit;
	}

	public String getLocationCode() {
		return locationCode;
	}
	
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getPallet() {
		return pallet;
	}

	public void setPallet(String pallet) {
		this.pallet = pallet;
	}

	public String getCarton() {
		return carton;
	}

	public void setCarton(String carton) {
		this.carton = carton;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Double getQuantityBU() {
		return quantityBU;
	}

	public void setQuantityBU(Double quantityBU) {
		this.quantityBU = quantityBU;
	}

	public Double getCountQuantityBU() {
		return countQuantityBU;
	}

	public void setCountQuantityBU(Double countQuantityBU) {
		this.countQuantityBU = countQuantityBU;
	}

	public void finalize() throws Throwable {

	}
	
	/**
	 * 登记盘点数量
	 * */
	public void adjust(double countQuantityBU){
		this.countQuantityBU = countQuantityBU;
		this.deltaQuantityBU = this.quantityBU - countQuantityBU;
		this.beCounted = true;
	}

	/**
	 * 增加盘点记录数量
	 * @param quantity
	 */
	public void addQuantityBU(Double quantity) {
		this.quantityBU += quantity;
		this.countQuantityBU += quantity;
	}
}