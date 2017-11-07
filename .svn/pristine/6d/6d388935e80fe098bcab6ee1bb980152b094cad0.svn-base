package com.vtradex.wms.server.model.warehouse;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;

/**
 * @category 月台
 * @author peng.lei
 * @version 1.0
 * @created 15-二月-2011 10:04:38
 */
public class WmsDock extends Entity{
	private static final long serialVersionUID = 8943164439062858838L;
	/** 所在库区*/
	@UniqueKey
	private WmsWarehouseArea warehouseArea;
	/** 月台编码*/
	@UniqueKey
	private String code;
	/** 是否收货月台 */
	private Boolean beReceive;
	/** 是否发货月台*/
	private Boolean beShip;
	/** 对应收货库位ID */
	private Long receiveLocationId;
	/** 状态*/
	private String status;

	public Boolean getBeReceive() {
		return beReceive;
	}

	public void setBeReceive(Boolean beReceive) {
		this.beReceive = beReceive;
	}

	public Boolean getBeShip() {
		return beShip;
	}

	public void setBeShip(Boolean beShip) {
		this.beShip = beShip;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public WmsWarehouseArea getWarehouseArea() {
		return warehouseArea;
	}

	public void setWarehouseArea(WmsWarehouseArea warehouseArea) {
		this.warehouseArea = warehouseArea;
	}
		
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public WmsDock() {}

	
	public void finalize() throws Throwable {
	}

	public Long getReceiveLocationId() {
		return receiveLocationId;
	}

	public void setReceiveLocationId(Long receiveLocationId) {
		this.receiveLocationId = receiveLocationId;
	}

}