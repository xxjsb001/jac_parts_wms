package com.vtradex.wms.server.model.inventory;

import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;

public class WmsSafeInventory extends VersionalEntity{

	private static final long serialVersionUID = 4258640218720912888L;
	
	//供应商
	private WmsOrganization suppiler;
	//物料
	private WmsItem item;
	//状态
	private String status;
	//安全库存
	private Double safeInvQty;
	//最小库存
	private Double minInvQty;
	//备注
	private String remark;
	/**
	 * 工艺状态
	 * {@link TypeOfExtendPropC1}
	 */
	private String artStatus;
	
	//是否预警
	private Boolean isRed;
	
	/**
	 * 库存类型：仓库安全库存，备料区安全库存
	 * WmsSafeInventoryType
	 */
	private String type;
	/**实际库存*/
	private Double realInventory;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getRealInventory() {
		return realInventory;
	}
	public void setRealInventory(Double realInventory) {
		this.realInventory = realInventory;
	}
	public Boolean getIsRed() {
		return isRed;
	}
	public void setIsRed(Boolean isRed) {
		this.isRed = isRed;
	}
	public String getArtStatus() {
		return artStatus;
	}
	public void setArtStatus(String artStatus) {
		this.artStatus = artStatus;
	}
	public WmsOrganization getSuppiler() {
		return suppiler;
	}
	public void setSuppiler(WmsOrganization suppiler) {
		this.suppiler = suppiler;
	}
	public WmsItem getItem() {
		return item;
	}
	public void setItem(WmsItem item) {
		this.item = item;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Double getSafeInvQty() {
		return safeInvQty;
	}
	public void setSafeInvQty(Double safeInvQty) {
		this.safeInvQty = safeInvQty;
	}
	public Double getMinInvQty() {
		return minInvQty;
	}
	public void setMinInvQty(Double minInvQty) {
		this.minInvQty = minInvQty;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
