package com.vtradex.wms.server.model.shipping;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.move.WmsMoveDocShipStatus;
import com.vtradex.wms.server.model.move.WmsTask;

/**
 * 作业任务和器具信息关系
 * @author Administrator
 *
 */
public class WmsTaskAndStation extends Entity{

	private static final long serialVersionUID = 8361552022632002738L;
	public static final String short_name = "wmsTaskAndStation";//规范:对象简称一律用这个替换
	//作业任务
	private WmsTask task;
	//拣货器具对应信息
	private WmsMoveDocAndStation station;
	//拣货数量
	private Double pickQuantityBu;
	//是否确认
	private Boolean isConfirm = Boolean.FALSE;
	//库存ID
	private Long inventoryId = 0L ;
	//器具编码
	private String container;
	//器具标签
	private String boxTag;
	
	/**是否加入装车单*/
	private Boolean isJoinBOL = Boolean.FALSE;
	/**
	 * 状态: 未发运，已发运
	 * 
	 * {@link WmsMoveDocShipStatus}
	 */
	private String shipStatus = WmsMoveDocShipStatus.UNSHIP;
	
	public String getBoxTag() {
		return boxTag;
	}

	public void setBoxTag(String boxTag) {
		this.boxTag = boxTag;
	}

	public String getShipStatus() {
		return shipStatus;
	}

	public void setShipStatus(String shipStatus) {
		this.shipStatus = shipStatus;
	}

	public Boolean getIsJoinBOL() {
		return isJoinBOL;
	}

	public void setIsJoinBOL(Boolean isJoinBOL) {
		this.isJoinBOL = isJoinBOL;
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
	public WmsTask getTask() {
		return task;
	}
	public void setTask(WmsTask task) {
		this.task = task;
	}
	public WmsMoveDocAndStation getStation() {
		return station;
	}
	public void setStation(WmsMoveDocAndStation station) {
		this.station = station;
	}
	public Double getPickQuantityBu() {
		return pickQuantityBu;
	}
	public void setPickQuantityBu(Double pickQuantityBu) {
		this.pickQuantityBu = pickQuantityBu;
	}
	public Boolean getIsConfirm() {
		return isConfirm;
	}
	public void setIsConfirm(Boolean isConfirm) {
		this.isConfirm = isConfirm;
	}
	public void cancelPickedQuantityBU(Double quantityBU) {
		this.pickQuantityBu -= quantityBU;
	}
}
