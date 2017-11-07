package com.vtradex.wms.server.model.move;

import java.util.ArrayList;
import java.util.List;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.DomainModel;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;

@SuppressWarnings("serial")
public class WmsTempMoveDocDetail  extends DomainModel {
	
	/** 移位单*/
	@UniqueKey
	private WmsMoveDoc moveDoc;
	/** 批次属性*/
	private WmsItemKey itemKey;
	/** 包装单位*/
	private WmsPackageUnit packageUnit;
	/** 计划移位数量(包装数)*/
	private Double planQuantity;
	/** 每托数量(包装数) */
	private Double palletQuantity;
	/** 移出库位ID */
	private Long fromLocationId;
	/** 库位类型 */
	private String locationType;
	/** 库存状态*/
	private String inventoryStatus;
	/** 包含明细*/
	private List<WmsMoveDocDetail> details = new ArrayList<WmsMoveDocDetail>();
	
	public WmsTempMoveDocDetail(WmsMoveDoc moveDoc, WmsItemKey itemKey, 
			WmsPackageUnit packageUnit, Double planQuantity, Double palletQuantity, 
			Long fromLocationId, String locationType, String inventoryStatus){
		this.moveDoc = moveDoc;
		this.itemKey = itemKey;		
		this.packageUnit = packageUnit;		
		this.planQuantity = planQuantity;	
		this.palletQuantity = palletQuantity;
		this.fromLocationId = fromLocationId;		
		this.locationType = locationType;
		this.inventoryStatus = inventoryStatus;
	}

	public Double getPalletQuantity() {
		return palletQuantity;
	}

	public void setPalletQuantity(Double palletQuantity) {
		this.palletQuantity = palletQuantity;
	}

	/**
	 * @return the moveDoc
	 */
	public WmsMoveDoc getMoveDoc() {
		return moveDoc;
	}

	/**
	 * @param moveDoc the moveDoc to set
	 */
	public void setMoveDoc(WmsMoveDoc moveDoc) {
		this.moveDoc = moveDoc;
	}

	/**
	 * @return the itemKey
	 */
	public WmsItemKey getItemKey() {
		return itemKey;
	}

	/**
	 * @param itemKey the itemKey to set
	 */
	public void setItemKey(WmsItemKey itemKey) {
		this.itemKey = itemKey;
	}

	/**
	 * @return the packageUnit
	 */
	public WmsPackageUnit getPackageUnit() {
		return packageUnit;
	}

	/**
	 * @param packageUnit the packageUnit to set
	 */
	public void setPackageUnit(WmsPackageUnit packageUnit) {
		this.packageUnit = packageUnit;
	}

	/**
	 * @return the planQuantity
	 */
	public Double getPlanQuantity() {
		return planQuantity;
	}

	/**
	 * @param planQuantity the planQuantity to set
	 */
	public void setPlanQuantity(Double planQuantity) {
		this.planQuantity = planQuantity;
	}

	/**
	 * @return the fromLocationId
	 */
	public Long getFromLocationId() {
		return fromLocationId;
	}

	/**
	 * @param fromLocationId the fromLocationId to set
	 */
	public void setFromLocationId(Long fromLocationId) {
		this.fromLocationId = fromLocationId;
	}

	/**
	 * @return the locationType
	 */
	public String getLocationType() {
		return locationType;
	}

	/**
	 * @param locationType the locationType to set
	 */
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	/**
	 * @return the inventoryStatus
	 */
	public String getInventoryStatus() {
		return inventoryStatus;
	}

	/**
	 * @param inventoryStatus the inventoryStatus to set
	 */
	public void setInventoryStatus(String inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}

	/**
	 * @return the details
	 */
	public List<WmsMoveDocDetail> getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(List<WmsMoveDocDetail> details) {
		this.details = details;
	}

}
