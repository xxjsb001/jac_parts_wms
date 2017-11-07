package com.vtradex.wms.server.model.process;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.base.ShipLotInfo;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;


/**
 * @category 加工方案明细
 * @author peng.lei
 * @version 1.0
 * @created 15-二月-2011 10:08:22
 */
public class WmsProcessPlanDetail extends Entity{

	private static final long serialVersionUID = 6986037023385944066L;
	
	/** 加工计划*/
	private WmsProcessPlan processPlan;
	/** 货品原料*/
	private WmsItem item;
	/** 原料批次属性要求*/
	private ShipLotInfo shipLotInfo = new ShipLotInfo();
	/** 数量BU*/
	private Double quantityBU;	
	/** 待加工原料库存状态
	 * 说明：如果要提取多种状态库存，录入时用逗号分隔，例如：待喷码,待贴标;
	 */
	private String inventoryStatus;
	
	public WmsProcessPlanDetail(){

	}
	
	public ShipLotInfo getShipLotInfo() {
		return shipLotInfo;
	}

	public void setShipLotInfo(ShipLotInfo shipLotInfo) {
		this.shipLotInfo = shipLotInfo;
	}

	public String getInventoryStatus() {
		return inventoryStatus;
	}

	public void setInventoryStatus(String inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}

	public WmsItem getItem() {
		return item;
	}

	public void setItem(WmsItem item) {
		this.item = item;
	}

	public WmsProcessPlan getProcessPlan() {
		return processPlan;
	}

	public void setProcessPlan(WmsProcessPlan processPlan) {
		this.processPlan = processPlan;
	}

	public Double getQuantityBU() {
		return quantityBU;
	}

	public void setQuantityBU(Double quantityBU) {
		this.quantityBU = quantityBU;
	}

	public void finalize() throws Throwable {

	}

}