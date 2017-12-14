package com.vtradex.wms.server.model.shipping;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;

public class WmsShipLot extends Entity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 拣货单 */
	private WmsPickTicket pickTicket;	
	/** 计划时间 */
	private Date requireArriveDate;
	/** 批次 */
	private Integer batch;
	/** 货品 */
	private String itemCode;
	private String itemName;
	private String class2;
	/** 计划数量BU */
	private Double expectedQuantityBU = 0D;
	/** 拣货数量BU */
	private Double pickedQuantityBU = 0D;
	/**生产线*/
	private String productionLine;
	
	
	public WmsShipLot(WmsPickTicket pickTicket, Date requireArriveDate,
			Integer batch, String itemCode, String itemName, String class2,
			Double expectedQuantityBU, String productionLine) {
		super();
		this.pickTicket = pickTicket;
		this.requireArriveDate = requireArriveDate;
		this.batch = batch;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.class2 = class2;
		this.expectedQuantityBU = expectedQuantityBU;
		this.productionLine = productionLine;
	}
	public WmsShipLot() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 获取计划件未拣货量
	 */
	public Double getUnPicQuantityBU() {
		return this.expectedQuantityBU - this.pickedQuantityBU;
	}
	public void removePickQty(Double quantity){
		this.pickedQuantityBU -= quantity;
	}
	
	public WmsPickTicket getPickTicket() {
		return pickTicket;
	}
	public void setPickTicket(WmsPickTicket pickTicket) {
		this.pickTicket = pickTicket;
	}
	public Date getRequireArriveDate() {
		return requireArriveDate;
	}
	public void setRequireArriveDate(Date requireArriveDate) {
		this.requireArriveDate = requireArriveDate;
	}
	public Integer getBatch() {
		return batch;
	}
	public void setBatch(Integer batch) {
		this.batch = batch;
	}
	public Double getExpectedQuantityBU() {
		return expectedQuantityBU;
	}
	public void setExpectedQuantityBU(Double expectedQuantityBU) {
		this.expectedQuantityBU = expectedQuantityBU;
	}
	public Double getPickedQuantityBU() {
		return pickedQuantityBU;
	}
	public void setPickedQuantityBU(Double pickedQuantityBU) {
		this.pickedQuantityBU = pickedQuantityBU;
	}
	public String getProductionLine() {
		return productionLine;
	}
	public void setProductionLine(String productionLine) {
		this.productionLine = productionLine;
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
	public String getClass2() {
		return class2;
	}
	public void setClass2(String class2) {
		this.class2 = class2;
	}	
}
