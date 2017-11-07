package com.vtradex.wms.server.model.warehouse;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

/**
 * @category 库区
 * @author shengpei.zhang
 * @version 1.0
 * @created 15-二月-2011 10:04:39
 */

public class WmsWarehouseArea extends Entity {	
	/** */
	private static final long serialVersionUID = -19854306095214382L;
	/** 所属仓库*/
	@UniqueKey
	private WmsWarehouse warehouse;
	/** 库区代码*/
	@UniqueKey
	private String code;
	/** 库区名称*/
	private String name;
	/** 额定最低温度*/
	private Double lowTemperature = 0D;
	/** 额定最高温度*/
	private Double highTemperature = 0D;
	/** 库区说明*/
	private String description;
	/** x坐标*/
	private Integer x_Pos = 0;
	/** y坐标*/
	private Integer y_Pos = 0;	
	/** 状态*/
	private String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getHighTemperature() {
		return highTemperature;
	}

	public void setHighTemperature(Double highTemperature) {
		this.highTemperature = highTemperature;
	}

	public Double getLowTemperature() {
		return lowTemperature;
	}

	public void setLowTemperature(Double lowTemperature) {
		this.lowTemperature = lowTemperature;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Integer getX_Pos() {
		return x_Pos;
	}

	public void setX_Pos(Integer x_Pos) {
		this.x_Pos = x_Pos;
	}

	public Integer getY_Pos() {
		return y_Pos;
	}

	public void setY_Pos(Integer y_Pos) {
		this.y_Pos = y_Pos;
	}

	public WmsWarehouseArea(){

	}

	public void finalize() throws Throwable {

	}

	@Override
	public void doSaveBefore() {
		if (this.isNew() && this.warehouse == null) {
			this.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
		}
		super.doSaveBefore();
	}

}