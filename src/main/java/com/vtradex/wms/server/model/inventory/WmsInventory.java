package com.vtradex.wms.server.model.inventory;

import com.vtradex.thorn.server.exception.OriginalBusinessException;
import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.PackageUtils;

/**
 * @category 库存表
 * @author peng.lei
 * @version 1.0
 * @created 15-二月-2011 10:06:58
 */
public class WmsInventory extends VersionalEntity{
	
	private static final long serialVersionUID = 3341564672644528022L;	
	
	/** 库位*/
	private WmsLocation location;
	
	/** 批次属性*/
	private WmsItemKey itemKey;
	
	/** 包装*/
	private WmsPackageUnit packageUnit;
	
	/** 库存数量*/
	private Double quantity = 0D;
	
	/** 库存数量BU*/
	private Double quantityBU = 0D;
	
	/** 上架分配数量 BU*/
	private Double putawayQuantityBU = 0D;
	
	/** 拣货分配数量BU*/
	private Double allocatedQuantityBU = 0D;
	
	/** 状态(说明：状态可重叠，例如"质检-贴标-喷码")*/
	private String status = BaseStatus.NULLVALUE;
	
	/**
	 * 说明：库位批次锁
	 */
	private Boolean lockLot = Boolean.FALSE;
	
	public WmsInventory(){

	}

	public Boolean getLockLot() {
		return lockLot;
	}

	public void setLockLot(Boolean lockLot) {
		this.lockLot = lockLot;
	}

	public Double getPutawayQuantityBU() {
		return putawayQuantityBU;
	}

	public void setPutawayQuantityBU(Double putawayQuantityBU) {
		this.putawayQuantityBU = putawayQuantityBU;
	}

	public Double getAllocatedQuantityBU() {
		return allocatedQuantityBU;
	}

	public void setAllocatedQuantityBU(Double allocatedQuantityBU) {
		this.allocatedQuantityBU = allocatedQuantityBU;
	}

	public WmsItemKey getItemKey() {
		return itemKey;
	}

	public void setItemKey(WmsItemKey itemKey) {
		this.itemKey = itemKey;
	}

	public WmsLocation getLocation() {
		return location;
	}

	public void setLocation(WmsLocation location) {
		this.location = location;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getQuantityBU() {
		return quantityBU;
	}

	public void setQuantityBU(Double quantityBU) {
		this.quantityBU = quantityBU;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public WmsPackageUnit getPackageUnit() {
		return packageUnit;
	}

	public void setPackageUnit(WmsPackageUnit packageUnit) {
		this.packageUnit = packageUnit;
	}
	
	/**
	 * 生成HashCode
	 */
	public String genHashCode() {
		return BeanUtils.getFormat(this.location.getId(), this.itemKey.getId(), this.packageUnit.getId(), this.status);
	}
	
	/**
	 * 库存可用数量
	 * @return
	 */
	public Double getAvailableQuantityBU(){
		return this.quantityBU-this.allocatedQuantityBU;
	}
	
	/**
	 * 拣货分配
	 * @param allocateQtyBU
	 */
	public void allocatePickup(Double allocateQtyBU) {
		if (DoubleUtils.compareByPrecision(this.getAvailableQuantityBU(), allocateQtyBU, this.packageUnit.getPrecision()) < 0) {
			throw new OriginalBusinessException("库存可用数量不足！"+this.getItemKey().getItem().getCode());
		}
		this.allocatedQuantityBU += allocateQtyBU;
	}
	
	/**
	 * 上架分配
	 * @param allocateQtyBu
	 */
	public void allocatePutaway(Double allocateQtyBu) {
		this.putawayQuantityBU += allocateQtyBu;
	}
	
	/**
	 * 取消拣货分配
	 * @param processQty
	 */
	public void unallocatePickup(Double processQty) {
		if(DoubleUtils.compareByPrecision(this.allocatedQuantityBU, processQty, this.packageUnit.getPrecision()) <0) {
			throw new OriginalBusinessException("取消拣货分配数量超过已分配数量!"+this.getItemKey().getItem().getCode());
		}
		
		this.allocatedQuantityBU -= processQty;
		
	}
	
	/**
	 * 取消上架分配
	 * @param processQty
	 */
	public void unallocatePutaway(Double processQty) {
		if(DoubleUtils.compareByPrecision(this.putawayQuantityBU, processQty, this.packageUnit.getItem().getPrecision()) <0) {
			throw new OriginalBusinessException("取消上架分配数量超过已分配数量!");
		}
		
		this.putawayQuantityBU -= processQty;
	}
	
	/**
	 * 增加库存
	 * @param quantity
	 */
	public void addQuantityBU(Double quantity){
		this.quantityBU += quantity;
		refreshPackageQuantity();
	}
	public void addAllocatedQuantityBU(Double quantity){
		this.allocatedQuantityBU += quantity;
	}

	/**
	 * 减少库存
	 * @param quantity
	 */
	public void removeQuantityBU(Double quantity) {
		this.quantityBU -= quantity;
		refreshPackageQuantity();
		if(this.quantityBU<=0){
			this.location.setPalletQuantity(0);
		}
	}

	/**
	 * 根据包装小数精度和库存数量BU调整包装数量
	 */
	private void refreshPackageQuantity() {
		this.quantity = PackageUtils.convertPackQuantity(this.quantityBU, this.packageUnit);
	}
	
	/** 
	 * 是否锁定
	 * */
	public boolean isLocked() {
		if (this.location.getLockCount()) {
			return true;
		}
		
		return false;
	}
	
	public Double getPackQty(Double quantity) {
		Double packQty=0D; 
		if (this.packageUnit.getPrecision() == 0) {
			//向上取整，得到整包装数量
			packQty = Math.ceil(quantity / this.packageUnit.getConvertFigure());
		} else {
			//根据包装精度计算包装数量
			packQty = DoubleUtils.formatByPrecision(quantity / this.packageUnit.getConvertFigure(), 
					this.packageUnit.getPrecision());
		}
		return packQty;
	}

	/**
	 * 检查库存记录是否有效
	 * @return
	 */
	public boolean isValid() {
		if (this.quantityBU > 0 || this.putawayQuantityBU > 0) {
			return true;
		}
		return false;
	}
}