package com.vtradex.wms.server.model.inventory;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;


/**
 * @category 进出存日报
 * @author peng.lei
 * @version 1.0
 * @created 15-二月-2011 10:06:58
 */
public class WmsStorageDaily extends Entity{
	private static final long serialVersionUID = -8190611419129304325L;
	
	/** 仓库*/
	@UniqueKey
	private WmsWarehouse warehouse;
	/** 批次属性*/
	@UniqueKey
	private WmsItemKey itemKey;	
	/** 结转日期*/
	@UniqueKey
	private Date computeDate;
	/** 前日结余*/
	private Double previousQuantityBU;
	/** 当日收货数量BU*/
	private Double receiveQuantityBU;
	/** 当日发货数量BU*/
	private Double shipQuantityBU;
	/** 当日加工消耗数量BU*/
	private Double processDecQuantityBU;
	/** 当日加工增加数量BU*/
	private Double processIncQuantityBU;		
	/** 当日盘点调整数量BU*/
	private Double countAdjustQuantityBU;
	/** 当日结余数量BU*/
	private Double leftQuantityBU;

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof WmsStorageDaily))
			return false;
		WmsStorageDaily castOther = (WmsStorageDaily) other;
		return new EqualsBuilder().append(warehouse, castOther.warehouse)
				.append(itemKey, castOther.itemKey).append(computeDate,
						castOther.computeDate).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(warehouse).append(itemKey).append(
				computeDate).toHashCode();
	}

	public WmsStorageDaily(){

	}

	public Date getComputeDate() {
		return computeDate;
	}

	public void setComputeDate(Date computeDate) {
		this.computeDate = computeDate;
	}

	public Double getCountAdjustQuantityBU() {
		return countAdjustQuantityBU;
	}

	public void setCountAdjustQuantityBU(Double countAdjustQuantityBU) {
		this.countAdjustQuantityBU = countAdjustQuantityBU;
	}

	public WmsItemKey getItemKey() {
		return itemKey;
	}

	public void setItemKey(WmsItemKey itemKey) {
		this.itemKey = itemKey;
	}

	public Double getLeftQuantityBU() {
		return leftQuantityBU;
	}

	public void setLeftQuantityBU(Double leftQuantityBU) {
		this.leftQuantityBU = leftQuantityBU;
	}

	public Double getPreviousQuantityBU() {
		return previousQuantityBU;
	}

	public void setPreviousQuantityBU(Double previousQuantityBU) {
		this.previousQuantityBU = previousQuantityBU;
	}

	public Double getReceiveQuantityBU() {
		return receiveQuantityBU;
	}

	public void setReceiveQuantityBU(Double receiveQuantityBU) {
		this.receiveQuantityBU = receiveQuantityBU;
	}

	public Double getShipQuantityBU() {
		return shipQuantityBU;
	}

	public void setShipQuantityBU(Double shipQuantityBU) {
		this.shipQuantityBU = shipQuantityBU;
	}

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Double getProcessDecQuantityBU() {
		return processDecQuantityBU;
	}

	public void setProcessDecQuantityBU(Double processDecQuantityBU) {
		this.processDecQuantityBU = processDecQuantityBU;
	}

	public Double getProcessIncQuantityBU() {
		return processIncQuantityBU;
	}

	public void setProcessIncQuantityBU(Double processIncQuantityBU) {
		this.processIncQuantityBU = processIncQuantityBU;
	}

	public void finalize() throws Throwable {

	}

}