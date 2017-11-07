package com.vtradex.wms.server.model.shipping;


import java.util.Date;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;

public class WmsWaveDoc extends Entity {
	/** */
	private static final long serialVersionUID = -6490315421184808793L;
	/** 仓库*/
	@UniqueKey
	private WmsWarehouse warehouse;
	/** 波次号*/
	@UniqueKey
	private String code;
	/** 相关单号 */
	private String relatedBill;
	/** 波次类型 */
	private String type;
	/** 状态 */
	private String status;
	/** 
	 * 作业模式(说明：按单作业、波次批拣)
	 * 
	 * {@link WmsWaveDocWorkMode}
	 * */
	private String workMode;
	/** 计划数量BU*/
	private Double expectedQuantityBU = 0D;
	/** 分配数量BU*/
	private Double allocatedQuantityBU = 0D;
	/** 拣货数量BU*/
	private Double pickedQuantityBU = 0D;
	/** 分单数量BU*/
	private Double splitedQuantityBU = 0D;
	/** 描述*/
	private String description;
	
	/** 波次结束时间 */
	private Date finishDate;
	

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRelatedBill() {
		return relatedBill;
	}

	public void setRelatedBill(String relatedBill) {
		this.relatedBill = relatedBill;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWorkMode() {
		return workMode;
	}

	public void setWorkMode(String workMode) {
		this.workMode = workMode;
	}

	public Double getExpectedQuantityBU() {
		return expectedQuantityBU;
	}

	public void setExpectedQuantityBU(Double expectedQuantityBU) {
		this.expectedQuantityBU = expectedQuantityBU;
	}

	public Double getAllocatedQuantityBU() {
		return allocatedQuantityBU;
	}

	public void setAllocatedQuantityBU(Double allocatedQuantityBU) {
		this.allocatedQuantityBU = allocatedQuantityBU;
	}

	public Double getPickedQuantityBU() {
		return pickedQuantityBU;
	}

	public void setPickedQuantityBU(Double pickedQuantityBU) {
		this.pickedQuantityBU = pickedQuantityBU;
	}

	public Double getSplitedQuantityBU() {
		return splitedQuantityBU;
	}

	public void setSplitedQuantityBU(Double splitedQuantityBU) {
		this.splitedQuantityBU = splitedQuantityBU;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	/** 添加期待数量 */
	public void addExpectedQuantityBU(Double expectedQuantityBU) {
		this.expectedQuantityBU += expectedQuantityBU;
	}
	
	/** 减少期待数量 */
	public void removeExpectedQuantityBU(Double expectedQuantityBU) {
		this.expectedQuantityBU -= expectedQuantityBU;
	}
	
	/**
	 * 移位数量BU
	 * @param quantityBU
	 */
	public void addMovedQuantity(Double quantity) {
		this.pickedQuantityBU += quantity;
	}
	
	/**
	 * 取消移位数量BU
	 * @param quantityBU
	 */
	public void cancelMovedQuantity(Double quantity) {
		this.pickedQuantityBU -= quantity;
	}
	
	/**
	 * 分配数量BU
	 * @param quantityBU
	 */
	public void allocate(Double quantity) {
		this.allocatedQuantityBU += quantity;
	}
	
	/**
	 * 分单数量BU
	 * @param quantityBU
	 */
	public void seprate(Double quantity) {
		this.splitedQuantityBU += quantity;
	}
	
	/**
	 * 取消分单数量BU
	 * @param quantityBU
	 */
	public void cancelSeprate(Double quantity) {
		this.splitedQuantityBU -= quantity;
	}
	
	
	/**
	 * 取消分配数量BU
	 * @param quantityBU
	 */
	public void unallocate(Double quantityBU){
		this.allocatedQuantityBU -= quantityBU;
	}
	
	public void pickBack(Double quantity) {	
		this.allocatedQuantityBU -= quantity;
        this.pickedQuantityBU -= quantity;
        this.splitedQuantityBU -= quantity;
    }
}
