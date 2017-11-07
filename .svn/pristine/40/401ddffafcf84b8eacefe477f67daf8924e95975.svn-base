package com.vtradex.wms.server.telnet.dto;

public class WmsPickTaskDTO extends WmsBaseTaskDTO{
	
	private String itemName;
	private String barCode;
	
	public WmsPickTaskDTO(Long moveDocId, String moveDocCode, Long workDocId,
			String workDocCode, Long taskId, Long fromLocationId,
			String fromLocationCode, Long toLocationId, String toLocationCode , String itemCode , String itemName ,String barCode, Double planQuantity) {
		super(moveDocId, moveDocCode, workDocId, workDocCode, taskId, fromLocationId,
				fromLocationCode, toLocationId, toLocationCode, itemCode ,planQuantity);
		this.itemName = itemName;
		this.barCode = barCode;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public WmsPickTaskDTO(){super();}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	
}
