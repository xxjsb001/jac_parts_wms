package com.vtradex.wms.server.model.process;

import java.util.Set;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;

/**
 * @category 加工方案
 * @author peng.lei
 * @version 1.0
 * @created 15-二月-2011 10:08:22
 */
public class WmsProcessPlan extends Entity {
	
	private static final long serialVersionUID = 6884329446772353845L;
	
	/** 仓库*/
	@UniqueKey
	private WmsWarehouse warehouse;
	
	/** 货主*/
	@UniqueKey
	private WmsOrganization company;
	
	/** 方案编码*/
	@UniqueKey
	private String code;
	
	/** 方案名称*/
	private String name;
	
	/** 加工货品原料
	 * 说明：如果是对成品的喷码、贴标等动作时，不要在方案头上设置成品信息，以示区分，同时解决加工成品的批次属性设置问题；
	 */
	private WmsItem item;
	
	/** 成品数量*/
	private Double quantity = 0D;
	
	/** 加工单价
	 * 说明：一套成品的单价
	 */
	private Double price = 0D;
	
	/** 加工成品库存状态*/
	private String inventoryStatus;
	
	/** 
	 * 状态
	 * 
	 * {@link WmsProcessPlanStatus}
	 */
	private String status;
	
	/** 描述*/
	private String description;
	
	/** 原料清单*/
	private Set<WmsProcessPlanDetail> details;
	
	public WmsProcessPlan(){

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

	public Set<WmsProcessPlanDetail> getDetails() {
		return details;
	}

	public void setDetails(Set<WmsProcessPlanDetail> details) {
		this.details = details;
	}

	public String getInventoryStatus() {
		return inventoryStatus;
	}

	public void setInventoryStatus(String inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public WmsItem getItem() {
		return item;
	}

	public void setItem(WmsItem item) {
		this.item = item;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public void finalize() throws Throwable {

	}
	
	public void removeProcessPlanDetail(WmsProcessPlanDetail detail) {
		this.details.remove(detail);
	}

	public void addProcessPlanDetail(WmsProcessPlanDetail detail) {
		detail.setProcessPlan(this);
		this.details.add(detail);
	}
	
	public void recalculate() {
		Double d = 0D;
		for (WmsProcessPlanDetail detail : this.details) {
			d+=detail.getQuantityBU();
		}
		this.setQuantity(d);
	}
}