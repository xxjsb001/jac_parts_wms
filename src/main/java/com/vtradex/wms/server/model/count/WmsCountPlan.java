package com.vtradex.wms.server.model.count;

import java.util.HashSet;
import java.util.Set;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;


/**
 * @category 盘点计划
 * @author peng.lei
 * @version 1.0
 * @created 16-二月-2011 22:08:42
 */
public class WmsCountPlan extends Entity{
	
	private static final long serialVersionUID = 6271582690996250781L;
	
	/** 仓库*/
	@UniqueKey
	private WmsWarehouse warehouse;
	/** 货主*/
	private WmsOrganization company;
	/**
	 * 盘点方式
	 * 说明：库位、货品、动碰库位
	 */
	private String type;
	/** 计划号*/
	@UniqueKey
	private String code;
	/**
	 * 说明：打开、生效、盘点、完成、取消、盘点调整
	 */
	private String status;
	/** 盘点货品
	 * 说明：按货品循环盘点时，记录待盘点货品信息，在盘点计划保存时，将所有包含该货品的库位全部提取创建盘点计划
	 */
	private WmsItem item ;
	/**
	 * 盘点库位数
	 * 说明：根据明细中库位数量汇总后回写
	 */
	private Integer locationCount = 0;
	/** 是否包含当天已盘点库位*/
	private Boolean beIncludeToday = Boolean.FALSE;
	/** 说明*/
	private String description;
	
	
	/** 包含盘点明细 */
	private Set<WmsCountDetail> details = new HashSet<WmsCountDetail>();
	/** 盘点表清单*/
	private Set<WmsCountRecord> records = new HashSet<WmsCountRecord>();
	
	/** 锁定方式（不锁库位-UNLOCK、锁库位-LOCK） */
	private String lockType;
	/** 计划类型（普通计划-NORMAL、复盘计划-RECOUNT） */
	private String planType;
	/** 动碰次数 */
	private Integer touchTimes = 0;
	/**===============JAC====================*/
	/** 供应商*/
	private WmsOrganization supplier;
	
	public WmsCountPlan(){

	}
	
	public WmsOrganization getSupplier() {
		return supplier;
	}

	public void setSupplier(WmsOrganization supplier) {
		this.supplier = supplier;
	}

	public Integer getTouchTimes() {
		return touchTimes;
	}

	public void setTouchTimes(Integer touchTimes) {
		this.touchTimes = touchTimes;
	}

	public String getLockType() {
		return lockType;
	}

	public void setLockType(String lockType) {
		this.lockType = lockType;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public Set<WmsCountDetail> getDetails() {
		return details;
	}

	public void setDetails(Set<WmsCountDetail> details) {
		this.details = details;
	}

	public Boolean getBeIncludeToday() {
		return beIncludeToday;
	}

	public void setBeIncludeToday(Boolean beIncludeToday) {
		this.beIncludeToday = beIncludeToday;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public WmsOrganization getCompany() {
		return company;
	}

	public void setCompany(WmsOrganization company) {
		this.company = company;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public WmsItem getItem() {
		return item;
	}

	public void setItem(WmsItem item) {
		this.item = item;
	}

	public Integer getLocationCount() {
		return locationCount;
	}

	public void setLocationCount(Integer locationCount) {
		this.locationCount = locationCount;
	}

	public Set<WmsCountRecord> getRecords() {
		return records;
	}

	public void setRecords(Set<WmsCountRecord> records) {
		this.records = records;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public void finalize() throws Throwable {

	}
}