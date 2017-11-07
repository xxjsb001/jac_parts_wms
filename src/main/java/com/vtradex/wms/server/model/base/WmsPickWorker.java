package com.vtradex.wms.server.model.base;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;
/**
 * 备料工保管员关系
 * @author Administrator
 *
 */
public class WmsPickWorker extends Entity{
	private static final long serialVersionUID = -2350606355908629156L;
	
	/** 仓库 */
	@UniqueKey
	private WmsWarehouse wareHouse = WmsWarehouseHolder.getWmsWarehouse();
	
	/** 备料工 */
	@UniqueKey
	private WmsWorker pickWorker;
	
	/** 保管员 */
	private WmsWorker worker;
	
	/**
	 * 状态
	 * {@link WmsPickWorkerStatus}
	 */
	private String status;

	public WmsWarehouse getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(WmsWarehouse wareHouse) {
		this.wareHouse = wareHouse;
	}

	public WmsWorker getPickWorker() {
		return pickWorker;
	}

	public void setPickWorker(WmsWorker pickWorker) {
		this.pickWorker = pickWorker;
	}

	public WmsWorker getWorker() {
		return worker;
	}

	public void setWorker(WmsWorker worker) {
		this.worker = worker;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public void doSaveBefore() {
		if (this.isNew() && this.wareHouse == null) {
			this.setWareHouse(WmsWarehouseHolder.getWmsWarehouse());
		}
		super.doSaveBefore();
	}
}
