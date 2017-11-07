package com.vtradex.wms.server.model.count;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.warehouse.WmsWorker;

/**
 * @category 盘点明细
 * @author peng.lei
 * @version 1.0
 * @created 16-二月-2011 22:08:42
 */
public class WmsCountDetail extends Entity{

	private static final long serialVersionUID = 7357919369650484057L;
	/** 盘点计划*/
	@UniqueKey
	private WmsCountPlan countPlan;
	/** 盘点库位ID*/
	@UniqueKey
	private Long locationId;
	/** 盘点库位 */
	private String locationCode;
	/** 作业人员 */
	private WmsWorker worker;
	/** 盘点状态（OPEN-打开、COUNT-盘点,FINISHED-完成） */
	private String status = WmsCountDetailStatus.OPEN;

	public WmsCountDetail(){

	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public WmsWorker getWorker() {
		return worker;
	}

	public void setWorker(WmsWorker worker) {
		this.worker = worker;
	}

	public WmsCountPlan getCountPlan() {
		return countPlan;
	}

	public void setCountPlan(WmsCountPlan countPlan) {
		this.countPlan = countPlan;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public void finalize() throws Throwable {

	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
}