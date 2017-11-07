package com.vtradex.wms.server.model.shipping;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.move.WmsMoveDoc;

public class WmsBOLStateLog extends Entity {

	private static final long serialVersionUID = 1L;
	
	/**订单登记状态 
	 * WmsTransState
	 * ||WmsMoveDocDetail.ID(质检)
	 * */
	private String type;
	/**订单登记时间*/
	private Date inputTime;
	/**关联的BOL*/
	private WmsMoveDoc moveDoc;
	/** 司机 || WmsInventoryExtend.id(质检)*/
	private String driver;
	/** 车牌号 || 托盘号(质检) */
	private String vehicleNo;
	
	private Double quantityBU = 0D;
	
	public Double getQuantityBU() {
		return quantityBU;
	}
	public void setQuantityBU(Double quantityBU) {
		this.quantityBU = quantityBU;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getVehicleNo() {
		return vehicleNo;
	}
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	public WmsMoveDoc getMoveDoc() {
		return moveDoc;
	}
	public void setMoveDoc(WmsMoveDoc moveDoc) {
		this.moveDoc = moveDoc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getInputTime() {
		return inputTime;
	}
	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}
}
