package com.vtradex.wms.server.model.move;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.base.OperatorInfo;
import com.vtradex.wms.server.model.warehouse.WmsBoxType;
import com.vtradex.wms.server.model.warehouse.WmsWorker;

/**
 * 发货包装明细
 * 
 * @category Entity
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:50 $
 */
public class WmsBoxDetail extends Entity {
	
	private static final long serialVersionUID = 1L;
	
	private WmsMoveDoc moveDoc;
	
	private WmsMoveDocDetail moveDocDetail;
	
	/** 包装箱号 */
	private String boxNo;
	
	/** 箱型 */
	private WmsBoxType boxType;
	
	/**
	 * 包装数
	 */
	private Double quantity = 0D;
	
	/** 一箱总重量 */
	private Double weight = 0D;
	/** 一箱总体积 */
	private Double volume = 0D;
	/**
	 * 一箱总称重重量
	 */
	private Double actualWeight = 0D;
	
	/**
	 * 一箱总数量
	 */
	private Double totalQuantity = 0D;
	
	/**工作组 */
	private WmsWorker workerGroup;
	
	/** 是否发运 */
	private Boolean beShipping = Boolean.FALSE;
	
	/**发运操作信息 */
	private OperatorInfo shippingOperator = new OperatorInfo();
	
	public WmsBoxDetail() {
	}

	public WmsMoveDoc getMoveDoc() {
		return moveDoc;
	}

	public void setMoveDoc(WmsMoveDoc moveDoc) {
		this.moveDoc = moveDoc;
	}

	public WmsMoveDocDetail getMoveDocDetail() {
		return moveDocDetail;
	}

	public void setMoveDocDetail(WmsMoveDocDetail moveDocDetail) {
		this.moveDocDetail = moveDocDetail;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}
	
	public WmsBoxType getBoxType() {
		return boxType;
	}

	public void setBoxType(WmsBoxType boxType) {
		this.boxType = boxType;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getActualWeight() {
		return actualWeight;
	}

	public void setActualWeight(Double actualWeight) {
		this.actualWeight = actualWeight;
	}

	public Double getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Double totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public WmsWorker getWorkerGroup() {
		return workerGroup;
	}

	public void setWorkerGroup(WmsWorker workerGroup) {
		this.workerGroup = workerGroup;
	}

	public Boolean getBeShipping() {
		return beShipping;
	}

	public void setBeShipping(Boolean beShipping) {
		this.beShipping = beShipping;
	}

	public OperatorInfo getShippingOperator() {
		return shippingOperator;
	}

	public void setShippingOperator(OperatorInfo shippingOperator) {
		this.shippingOperator = shippingOperator;
	}
}
