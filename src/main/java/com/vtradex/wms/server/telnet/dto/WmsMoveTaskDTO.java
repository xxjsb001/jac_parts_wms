package com.vtradex.wms.server.telnet.dto;

/**
 * 移位计划任务
 *
 * @category DTO 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.2 $ $Date: 2015/10/22 03:16:52 $
 */
public class WmsMoveTaskDTO {
	
	private Long moveDocId;
	private Long moveDocDetailId;
	private String moveDocCode;
	
	private Long workDocId;
	private Long taskId;
	
	private Long itemId;
	private Long itemKeyId;
	private String itemCode;
	private String itemName;
	private String barCode;
	private Long packageUnitId;
	
	private Long srcInventoryId;
	private Long fromLocationId;
	private String fromLocationCode;
	private String srcInventoryStatus;
	
	private Long destInventoryId;
	private Long toLocationId;
	private String toLocationCode = "";
	
	private String pallet;
	private String carton;
	
	private Double unMoveQuantityBU = 0D;
	private Double tiredMovedQuantityBU = 0D;
	private Double actualMovedQuantityBU = 0D;
	
	private Boolean beTiredPutaway = Boolean.FALSE; 
	
	private Long srcInventoryExtendId;
	
	private String companyCode;
	
	
	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public Long getSrcInventoryExtendId() {
		return srcInventoryExtendId;
	}

	public void setSrcInventoryExtendId(Long srcInventoryExtendId) {
		this.srcInventoryExtendId = srcInventoryExtendId;
	}

	public WmsMoveTaskDTO() {
	}

	public Long getMoveDocId() {
		return moveDocId;
	}

	public void setMoveDocId(Long moveDocId) {
		this.moveDocId = moveDocId;
	}

	public Long getMoveDocDetailId() {
		return moveDocDetailId;
	}

	public void setMoveDocDetailId(Long moveDocDetailId) {
		this.moveDocDetailId = moveDocDetailId;
	}

	public String getMoveDocCode() {
		return moveDocCode;
	}

	public void setMoveDocCode(String moveDocCode) {
		this.moveDocCode = moveDocCode;
	}

	public Long getWorkDocId() {
		return workDocId;
	}

	public void setWorkDocId(Long workDocId) {
		this.workDocId = workDocId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getItemKeyId() {
		return itemKeyId;
	}

	public void setItemKeyId(Long itemKeyId) {
		this.itemKeyId = itemKeyId;
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
	
	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
	public Long getPackageUnitId() {
		return packageUnitId;
	}

	public void setPackageUnitId(Long packageUnitId) {
		this.packageUnitId = packageUnitId;
	}

	public Long getSrcInventoryId() {
		return srcInventoryId;
	}

	public void setSrcInventoryId(Long srcInventoryId) {
		this.srcInventoryId = srcInventoryId;
	}

	public Long getFromLocationId() {
		return fromLocationId;
	}

	public void setFromLocationId(Long fromLocationId) {
		this.fromLocationId = fromLocationId;
	}

	public String getFromLocationCode() {
		return fromLocationCode;
	}

	public void setFromLocationCode(String fromLocationCode) {
		this.fromLocationCode = fromLocationCode;
	}
	
	public String getSrcInventoryStatus() {
		return srcInventoryStatus;
	}

	public void setSrcInventoryStatus(String srcInventoryStatus) {
		this.srcInventoryStatus = srcInventoryStatus;
	}

	public Long getDestInventoryId() {
		return destInventoryId;
	}

	public void setDestInventoryId(Long destInventoryId) {
		this.destInventoryId = destInventoryId;
	}

	public Long getToLocationId() {
		return toLocationId;
	}

	public void setToLocationId(Long toLocationId) {
		this.toLocationId = toLocationId;
	}

	public String getToLocationCode() {
		return toLocationCode;
	}

	public void setToLocationCode(String toLocationCode) {
		this.toLocationCode = toLocationCode;
	}

	public String getPallet() {
		return pallet;
	}

	public void setPallet(String pallet) {
		this.pallet = pallet;
	}

	public String getCarton() {
		return carton;
	}

	public void setCarton(String carton) {
		this.carton = carton;
	}

	public Double getUnMoveQuantityBU() {
		return unMoveQuantityBU;
	}

	public void setUnMoveQuantityBU(Double unMoveQuantityBU) {
		this.unMoveQuantityBU = unMoveQuantityBU;
	}

	public Double getTiredMovedQuantityBU() {
		return tiredMovedQuantityBU;
	}

	public void setTiredMovedQuantityBU(Double tiredMovedQuantityBU) {
		this.tiredMovedQuantityBU = tiredMovedQuantityBU;
	}

	public Double getActualMovedQuantityBU() {
		return actualMovedQuantityBU;
	}

	public void setActualMovedQuantityBU(Double actualMovedQuantityBU) {
		this.actualMovedQuantityBU = actualMovedQuantityBU;
	}

	public Boolean getBeTiredPutaway() {
		return beTiredPutaway;
	}

	public void setBeTiredPutaway(Boolean beTiredPutaway) {
		this.beTiredPutaway = beTiredPutaway;
	}
}
