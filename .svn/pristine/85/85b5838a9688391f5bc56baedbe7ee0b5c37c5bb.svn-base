package com.vtradex.wms.server.model.inventory;

import java.util.Date;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
/**记录质检SOI级别*/
public class WmsQualityMoveSoiLog extends Entity{
	private static final long serialVersionUID = 6456489656159652471L;
	public static String QUALITY_MOVE_DOC_SOI = "质检管理SOI回写状态";//仅用于备注 yc.min
	/** 所属仓库 */
	@UniqueKey
	private WmsWarehouse warehouse;
	/**供应商编码 */
	private String supCode;
	/**供应商名称 */
	private String supName;
	/**类型 */
	private String type;
	/**存货日期 */
	private Date storageDate;
	/**物料编码 */
	private String itemCode;
	/**物料名称 */
	private String itemName;
	/**数量 */
	private Double quantity = 0D;
	/**库存状态 */
	private String inventoryStatus;
	/**质检状态 */
	private String qualityStatus;
	/**工艺状态 */
	private String extendPropc1;
	/**库位*/
	private String location;
	/**备注 */
	private String description;
	/**送检单号 */
	private String qualityCode;
	public WmsQualityMoveSoiLog(){
		
	}
	public WmsWarehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}
	public String getSupCode() {
		return supCode;
	}
	public void setSupCode(String supCode) {
		this.supCode = supCode;
	}
	public String getSupName() {
		return supName;
	}
	public void setSupName(String supName) {
		this.supName = supName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getStorageDate() {
		return storageDate;
	}
	public void setStorageDate(Date storageDate) {
		this.storageDate = storageDate;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public String getInventoryStatus() {
		return inventoryStatus;
	}
	public void setInventoryStatus(String inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}
	public String getQualityStatus() {
		return qualityStatus;
	}
	public void setQualityStatus(String qualityStatus) {
		this.qualityStatus = qualityStatus;
	}
	public String getExtendPropc1() {
		return extendPropc1;
	}
	public void setExtendPropc1(String extendPropc1) {
		this.extendPropc1 = extendPropc1;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getQualityCode() {
		return qualityCode;
	}
	public void setQualityCode(String qualityCode) {
		this.qualityCode = qualityCode;
	}
	
}
