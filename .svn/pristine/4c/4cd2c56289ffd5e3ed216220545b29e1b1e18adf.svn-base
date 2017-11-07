package com.vtradex.wms.server.model.warehouse;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.thorn.server.model.security.User;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;


/**
 * @category 作业人员
 * @author shengpei.zhang
 * @version 1.0
 * @created 15-二月-2011 10:04:39
 */
public class WmsWorker extends Entity{
	
	/** */
	private static final long serialVersionUID = -703316509180923640L;
	/** 所属仓库*/
	@UniqueKey
	private WmsWarehouse warehouse;
	/**
	 * 当前工作区
	 */
	private WmsWorkArea currentWorkArea; 
	/** 工号*/
	@UniqueKey
	private String code;
	/** 姓名*/
	private String name;
	/** 对应操作员*/
	private User user;
	/** 描述*/
	private String description;
	/** 状态*/
	private String status;
	/**
	 * 资源类型
	 * {@link :WmsWorkerType}
	 */
	private String type;
	/** 岗位*/
	private String station;
	
	/** 所属班组*/
	private WmsWorker worker;

	public WmsWorker getWorker() {
		return worker;
	}

	public void setWorker(WmsWorker worker) {
		this.worker = worker;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}
    
	public WmsWorkArea getCurrentWorkArea() {
		return currentWorkArea;
	}

	public void setCurrentWorkArea(WmsWorkArea currentWorkArea) {
		this.currentWorkArea = currentWorkArea;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public WmsWorker(){

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