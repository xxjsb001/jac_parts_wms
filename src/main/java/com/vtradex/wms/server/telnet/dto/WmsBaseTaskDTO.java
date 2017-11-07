package com.vtradex.wms.server.telnet.dto;

public class WmsBaseTaskDTO {

	protected Long moveDocId;
	protected String moveDocCode;
	protected Long workDocId;
	protected String workDocCode;
	protected Long taskId;
	protected Long fromLocationId;
	protected String fromLocationCode;
	protected Long toLocationId;
	protected String toLocationCode;
	protected String itemCode;
	protected Double unFinishQuantity;
	
	public WmsBaseTaskDTO(){}
	
	public WmsBaseTaskDTO(Long moveDocId, String moveDocCode, Long workDocId,
			String workDocCode, Long taskId, Long fromLocationId,
			String fromLocationCode, Long toLocationId, String toLocationCode,String itemCode,Double unFinishQuantity) {
		this.moveDocId = moveDocId;
		this.moveDocCode = moveDocCode;
		this.workDocId = workDocId;
		this.workDocCode = workDocCode;
		this.taskId = taskId;
		this.fromLocationId = fromLocationId;
		this.fromLocationCode = fromLocationCode;
		this.toLocationId = toLocationId;
		this.toLocationCode = toLocationCode;
		this.itemCode = itemCode;
		this.unFinishQuantity = unFinishQuantity;
	}
	
	public Double getUnFinishQuantity() {
		return unFinishQuantity;
	}

	public void setUnFinishQuantity(Double unFinishQuantity) {
		this.unFinishQuantity = unFinishQuantity;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Long getMoveDocId() {
		return moveDocId;
	}
	public void setMoveDocId(Long moveDocId) {
		this.moveDocId = moveDocId;
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
	public String getWorkDocCode() {
		return workDocCode;
	}
	public void setWorkDocCode(String workDocCode) {
		this.workDocCode = workDocCode;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
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
	
}
