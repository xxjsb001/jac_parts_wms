package com.vtradex.wms.server.model.shipping;

import java.util.HashSet;
import java.util.Set;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.base.ShipLotInfo;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;

public class WmsWaveDocDetail extends Entity{
	/** */
	private static final long serialVersionUID = 5157996277234675808L;
	/** 波次单*/
	private WmsWaveDoc waveDoc;
	/** 货品*/
	private WmsItem item;
	/** 批次属性要求*/
	private ShipLotInfo shipLotInfo;
	/** 包装单位*/
	private WmsPackageUnit packageUnit;
	/** 计划数量*/
	private Double expectedQuantity = 0D;
	/** 计划数量BU*/
	private Double expectedQuantityBU = 0D;
	/** 分配数量BU*/
	private Double allocatedQuantityBU = 0D;
	/** 拣货数量BU*/
	private Double pickedQuantityBU = 0D;
	/** 分单数量BU*/
	private Double splitedQuantityBU = 0D;
	/** 关联拣货单明细 */
	private WmsMoveDocDetail moveDocDetail;
	
	/**
	 * 波次拣货单明细
	 */
	private Set<WmsMoveDocDetail> waveMoveDocDetails = new HashSet<WmsMoveDocDetail>();
	
	public WmsWaveDocDetail() {}

	public WmsWaveDoc getWaveDoc() {
		return waveDoc;
	}

	public void setWaveDoc(WmsWaveDoc waveDoc) {
		this.waveDoc = waveDoc;
	}

	public WmsItem getItem() {
		return item;
	}

	public void setItem(WmsItem item) {
		this.item = item;
	}

	public ShipLotInfo getShipLotInfo() {
		return shipLotInfo;
	}

	public void setShipLotInfo(ShipLotInfo shipLotInfo) {
		this.shipLotInfo = shipLotInfo;
	}

	public WmsPackageUnit getPackageUnit() {
		return packageUnit;
	}

	public void setPackageUnit(WmsPackageUnit packageUnit) {
		this.packageUnit = packageUnit;
	}

	public Double getExpectedQuantity() {
		return expectedQuantity;
	}

	public void setExpectedQuantity(Double expectedQuantity) {
		this.expectedQuantity = expectedQuantity;
	}

	public Double getExpectedQuantityBU() {
		return expectedQuantityBU;
	}

	public void setExpectedQuantityBU(Double expectedQuantityBU) {
		this.expectedQuantityBU = expectedQuantityBU;
	}

	public Double getAllocatedQuantityBU() {
		return allocatedQuantityBU;
	}

	public void setAllocatedQuantityBU(Double allocatedQuantityBU) {
		this.allocatedQuantityBU = allocatedQuantityBU;
	}

	public Double getPickedQuantityBU() {
		return pickedQuantityBU;
	}

	public void setPickedQuantityBU(Double pickedQuantityBU) {
		this.pickedQuantityBU = pickedQuantityBU;
	}

	public Double getSplitedQuantityBU() {
		return splitedQuantityBU;
	}

	public void setSplitedQuantityBU(Double splitedQuantityBU) {
		this.splitedQuantityBU = splitedQuantityBU;
	}

	public WmsMoveDocDetail getMoveDocDetail() {
		return moveDocDetail;
	}

	public void setMoveDocDetail(WmsMoveDocDetail moveDocDetail) {
		this.moveDocDetail = moveDocDetail;
	}

	public Set<WmsMoveDocDetail> getWaveMoveDocDetails() {
		return waveMoveDocDetails;
	}

	public void setWaveMoveDocDetails(Set<WmsMoveDocDetail> waveMoveDocDetails) {
		this.waveMoveDocDetails = waveMoveDocDetails;
	}

	
	public void addWmsMoveDocDetail(WmsMoveDocDetail moveDocDetail){
		if(waveMoveDocDetails == null){
			waveMoveDocDetails = new HashSet<WmsMoveDocDetail>();
		}
		waveMoveDocDetails.add(moveDocDetail);
	}
	
	
	public void removeWmsMoveDocDetail(WmsMoveDocDetail moveDocDetail){
		if(waveMoveDocDetails == null){
			return;
		}
		waveMoveDocDetails.remove(moveDocDetail);
	}
	
	/**
	 * 移位
	 * @param movedQuantity
	 */
	public void addMovedQuantity(Double movedQuantity) {
		moveDocDetail.move(movedQuantity);
		this.pickedQuantityBU += movedQuantity;
		waveDoc.addMovedQuantity(movedQuantity);
	}
	
	/** 取消移位数量 */
	public void cancelMovedQuantity(Double movedQuantity) {
		moveDocDetail.cancelMove(movedQuantity);
		this.pickedQuantityBU -= movedQuantity;
		waveDoc.cancelMovedQuantity(movedQuantity);
	}
	
	
	/**
	 * 分配数量
	 * @param quantity
	 */
	public void allocate(Double quantity) {
		moveDocDetail.allocate(quantity);
		this.allocatedQuantityBU += quantity;
		this.waveDoc.allocate(quantity);
	}
	
	/**
	 * 分单数量
	 * @param planQuantityBU
	 */
	public void seprate(Double quantity) {
		this.splitedQuantityBU += quantity;
		this.waveDoc.seprate(quantity);
	}
	
	/**
	 * 取消分单数量
	 * @param planQuantityBU
	 */
	public void cancelSeprate(Double quantity) {
		this.splitedQuantityBU -= quantity;
		this.waveDoc.cancelSeprate(quantity);
	}
	
	/**
	 * 取消分配数量
	 * @param planQuantityBU
	 */
	public void unallocate(Double quantityBU){
		moveDocDetail.unallocate(quantityBU);
		this.allocatedQuantityBU -= quantityBU;
		this.waveDoc.unallocate(quantityBU);
	}
	
	
	/** 根据移位单明细添加波次单明细 */
	public void addMoveDocDetail(WmsMoveDocDetail moveDocDetail) {
		this.setExpectedQuantity(moveDocDetail.getPlanQuantity());
		this.setExpectedQuantityBU(moveDocDetail.getPlanQuantityBU());
		
		this.waveDoc.addExpectedQuantityBU(moveDocDetail.getPlanQuantityBU());
	}
	public double getUnlocatedQuantity(){
		return this.expectedQuantityBU - this.allocatedQuantityBU;
	}
	public double getUnMovedQuantity(){
		return this.allocatedQuantityBU - this.getPickedQuantityBU();
	}
	
	/** 获取为分单数量 */
	public Double getUnSplitedQuantity(){
		return this.expectedQuantityBU - this.getSplitedQuantityBU();
	}
	
	public void pickBack(Double quantity) {	
		this.allocatedQuantityBU -= quantity;
        this.pickedQuantityBU -= quantity;
        this.splitedQuantityBU -= quantity;
        this.waveDoc.pickBack(quantity);
        this.moveDocDetail.originalPickBack(quantity);
    }
}
