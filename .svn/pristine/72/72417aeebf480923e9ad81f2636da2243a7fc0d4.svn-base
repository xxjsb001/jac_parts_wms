package com.vtradex.wms.server.model.inventory;

import java.text.ParseException;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.utils.DoubleUtils;

public class WmsInventoryExtend extends Entity {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -222959274696426016L;
	/** 
	 * 库存ID 
	 */
	private WmsInventory inventory;
	/** 
	 * 库位ID 
	 */
	private Long locationId;
	/** 
	 * 库位编码 
	 */
	private String locationCode;
	/**
	 * 托盘号
	 */
	private String pallet = BaseStatus.NULLVALUE;
	/**
	 * 箱号
	 */
	private String carton = BaseStatus.NULLVALUE;
	/**
	 * 序列号
	 */
	private String serialNo = BaseStatus.NULLVALUE;
	/**
	 * 货品
	 */
	private WmsItem item;
	
	/**
	 * 拣货分配数量BU, 在收货区时上架时使用,质检时作为新建数量预扣除
	 */
	private Double allocatedQuantityBU = 0D;
	/**
	 * 数量BU
	 */
	private Double quantityBU = 0D;
	/** 哈希码值 */
	@UniqueKey
	private String hashCode;
	/**
	 * 状态
	 */
	private String status;
	
	
	public Double getAllocatedQuantityBU() {
		return allocatedQuantityBU;
	}

	public void setAllocatedQuantityBU(Double allocatedQuantityBU) {
		this.allocatedQuantityBU = allocatedQuantityBU;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getHashCode() {
		return hashCode;
	}
	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}
	public WmsInventory getInventory() {
		return inventory;
	}
	public void setInventory(WmsInventory inventory) {
		this.inventory = inventory;
	}
	public Long getLocationId() {
		return locationId;
	}
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public Double getQuantityBU() {
		return quantityBU;
	}
	public void setQuantityBU(Double quantityBU) {
		this.quantityBU = quantityBU;
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
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public WmsItem getItem() {
		return item;
	}
	public void setItem(WmsItem item) {
		this.item = item;
	}
	
	
	
	/**
	 * 生成HashCode
	 */
	public String genHashCode() {
		return BeanUtils.getFormat(this.inventory.getId(), this.pallet, this.carton, this.serialNo);
	}
	public static void main(String[] args) throws ParseException {
		Long id = 317366L;
		String pallet = "-";//221647_1,221647_2,221647_3,221647_4,221647_5,221647_6
		//BaseStatus.NULLVALUE
		String[] pallets = pallet.split(",");
		for(String p : pallets){
			System.out.println(p+":"+BeanUtils.getFormat(id, p, BaseStatus.NULLVALUE, BaseStatus.NULLVALUE));
		}
		
	}
	/**
	 * 添加数量
	 * @param quantityBU2
	 */
	public void addQuantity(Double quantityBU2) {
		this.quantityBU += quantityBU2;
	}
	
	/**
	 * 减少数量
	 * @param quantityBU2
	 */
	public void removeQuantity(Double quantityBU2) {
		this.quantityBU -= quantityBU2;
	}
	
	/**
	 * 根据库存更新序列号信息
	 * @param inv
	 */
	public void updateInventoryInfo(WmsInventory inv) {
		this.setInventory(inv);
		this.setLocationId(inv.getLocation().getId());
		this.setLocationCode(inv.getLocation().getCode());
	}
	
	/**
	 * 库存可用数量
	 * @return
	 */
	public Double getAvailableQuantityBU(){
		return this.quantityBU - this.allocatedQuantityBU;
	}
	
	/**
	 * 拣货分配
	 * @param allocateQtyBU
	 */
	public void allocatePickup(Double allocateQtyBU) {
		if (DoubleUtils.compareByPrecision(this.getAvailableQuantityBU(), allocateQtyBU, inventory.getPackageUnit().getPrecision()) < 0) {
			this.allocatedQuantityBU += this.getAvailableQuantityBU();
		}
		else{
			this.allocatedQuantityBU += allocateQtyBU;
		}
	}
	
	/**
	 * 取消拣货分配
	 * @param processQty
	 */
	public void unallocatePickup(Double allocateQtyBU) {
		if(DoubleUtils.compareByPrecision(this.allocatedQuantityBU, allocateQtyBU, inventory.getPackageUnit().getPrecision()) < 0) {
			this.allocatedQuantityBU = 0D;
		}
		else{
			this.allocatedQuantityBU -= allocateQtyBU;
		}
		
		
	}
}
