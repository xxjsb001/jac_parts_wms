package com.vtradex.wms.server.telnet.dto;

public class WmsPickContainerDTO {
	//拣货明细与器具关系ID
	private Long wmsMoveDocAndStationId;
	//拣货明细与器具关系订单数量
	private Double orderBU = 0D;
	//异常描述
	private String errorMes;
	
	//作业任务ID
	private Long taskId;
	//容器ID
	private Long moveDocAndStationId;
	//容器IDS--解决时序件多个明细分层拣货业务,目的是选择其中任何一个序号,系统自动将其余的相同物料的器具关系表加入,当选择task拣货时,根据录入总量,系统自动将其余的关系表也做拣选
//	private String moveDocAndStationIds;
	//器具型号
	private String type;
	//器具型号
	private String typeName;
	//器具编码
	private String container;
	//货品ID
	private Long itemId;
	//货品编码
	private String itemCode;
	//货品名称
	private String itemName;
	//移出库位编码
	private String locationCode;
	//拣货库位编码
	private String pickLocCode;
	//供应商
	private String supplier;
	//待拣选数量
	private Double planQuantityBU = 0D;
	//未拣选数量
	private Double unMoveQuantityBU = 0D;
	//拣选数量
	private Double pickQuantity = 0D;
	//库存ID
	private Long inventoryId = 0L;
	/**生产线*/
	private String productionLine;
	//器具标签
	private String boxTag;
	
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getBoxTag() {
		return boxTag;
	}
	public void setBoxTag(String boxTag) {
		this.boxTag = boxTag;
	}
	public Long getWmsMoveDocAndStationId() {
		return wmsMoveDocAndStationId;
	}
	public void setWmsMoveDocAndStationId(Long wmsMoveDocAndStationId) {
		this.wmsMoveDocAndStationId = wmsMoveDocAndStationId;
	}
	public Double getOrderBU() {
		return orderBU;
	}
	public void setOrderBU(Double orderBU) {
		this.orderBU = orderBU;
	}
	public String getErrorMes() {
		return errorMes;
	}
	public void setErrorMes(String errorMes) {
		this.errorMes = errorMes;
	}
	public String getProductionLine() {
		return productionLine;
	}
	public void setProductionLine(String productionLine) {
		this.productionLine = productionLine;
	}
	public String getContainer() {
		return container;
	}
	public void setContainer(String container) {
		this.container = container;
	}
	public Long getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}
	public String getPickLocCode() {
		return pickLocCode;
	}
	public void setPickLocCode(String pickLocCode) {
		this.pickLocCode = pickLocCode;
	}
	public Double getPickQuantity() {
		return pickQuantity;
	}
	public void setPickQuantity(Double pickQuantity) {
		this.pickQuantity = pickQuantity;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public Long getMoveDocAndStationId() {
		return moveDocAndStationId;
	}
	public void setMoveDocAndStationId(Long moveDocAndStationId) {
		this.moveDocAndStationId = moveDocAndStationId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
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
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public Double getPlanQuantityBU() {
		return planQuantityBU;
	}
	public void setPlanQuantityBU(Double planQuantityBU) {
		this.planQuantityBU = planQuantityBU;
	}
	public Double getUnMoveQuantityBU() {
		return unMoveQuantityBU;
	}
	public void setUnMoveQuantityBU(Double unMoveQuantityBU) {
		this.unMoveQuantityBU = unMoveQuantityBU;
	}
//	public String getMoveDocAndStationIds() {
//		return moveDocAndStationIds;
//	}
//	public void setMoveDocAndStationIds(String moveDocAndStationIds) {
//		this.moveDocAndStationIds = moveDocAndStationIds;
//	}
}
