package com.vtradex.wms.server.model.receiving;

import com.vtradex.thorn.server.exception.OriginalBusinessException;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.warehouse.WmsWorker;


/**
 * @category 收货日志
 * @author peng.lei
 * @version 1.0
 * @created 15-二月-2011 10:07:19
 */
public class WmsReceivedRecord extends Entity {
	private static final long serialVersionUID = 2674110633634290860L;
	/** ASN*/
	private WmsASN asn;
	/** ASN明细*/
	private WmsASNDetail asnDetail;
	/** 批次属性*/
	private WmsItemKey itemKey;
	/** 库存状态说明：收货过账入库时填写具体的库存状态 */
	private String inventoryStatus;
	/** 收货库位ID */
	private Long locationId;
	/** 托盘号 */
	private String pallet = BaseStatus.NULLVALUE;
	/** 箱号 */
	private String carton = BaseStatus.NULLVALUE;
	/** 序列号 */
	private String serialNo = BaseStatus.NULLVALUE;
	/** 收货包装*/
	private WmsPackageUnit packageUnit;	
	/** 收货数量BU*/
	private Double receivedQuantityBU = 0.0D;
	/** 收货数量*/
	private Double receivedQuantity = 0.0D;		
	/**上架数量*/
	private Double movedQuantity=0.0D;
	/** 作业人员*/
	private WmsWorker worker;
	/**================================JAC================================*/
	/** 收货库存ID */
	private Long inventoryId;
	/** 是否分配*/
	private Boolean beVerified = Boolean.FALSE;
	
	public Long getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}

	public Double getMovedQuantity() {
		return movedQuantity;
	}

	public void setMovedQuantity(Double movedQuantity) {
		this.movedQuantity = movedQuantity;
	}

	public WmsReceivedRecord(){}

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

	public WmsASN getAsn() {
		return asn;
	}

	public void setAsn(WmsASN asn) {
		this.asn = asn;
	}

	public WmsASNDetail getAsnDetail() {
		return asnDetail;
	}

	public void setAsnDetail(WmsASNDetail asnDetail) {
		this.asnDetail = asnDetail;
	}

	public Boolean getBeVerified() {
		return beVerified;
	}

	public void setBeVerified(Boolean beVerified) {
		this.beVerified = beVerified;
	}

	public String getInventoryStatus() {
		return inventoryStatus;
	}

	public void setInventoryStatus(String inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}

	public WmsItemKey getItemKey() {
		return itemKey;
	}

	public void setItemKey(WmsItemKey itemKey) {
		this.itemKey = itemKey;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public WmsPackageUnit getPackageUnit() {
		return packageUnit;
	}

	public void setPackageUnit(WmsPackageUnit packageUnit) {
		this.packageUnit = packageUnit;
	}

	public Double getReceivedQuantity() {
		return receivedQuantity;
	}

	public void setReceivedQuantity(Double receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
	}

	public Double getReceivedQuantityBU() {
		return receivedQuantityBU;
	}

	public void setReceivedQuantityBU(Double receivedQuantityBU) {
		this.receivedQuantityBU = receivedQuantityBU;
	}

	public WmsWorker getWorker() {
		return worker;
	}

	public void setWorker(WmsWorker worker) {
		this.worker = worker;
	}

	/** 上架 */
	public void addMovedQuantity(Double movedQuantity) {
		this.movedQuantity += movedQuantity;
		if(this.movedQuantity>this.receivedQuantityBU){
			throw new OriginalBusinessException("上架量不允许超出收货量"+this.receivedQuantityBU);
		}
		asnDetail.addMovedQuantity(movedQuantity);
	}
	
	/** 取消上架 */
	public void cancelMovedQuantity(Double movedQuantity) {
		if (this.movedQuantity >= movedQuantity) {
			this.movedQuantity -= movedQuantity;
			asnDetail.cancelMovedQuantity(movedQuantity);
		}
	}
	
	/** 取消收货 */
	public void cancelReceive(Double cancelQtyBU) {
		this.receivedQuantityBU -= cancelQtyBU;
		this.receivedQuantity -= Math.ceil(cancelQtyBU / this.getPackageUnit().getConvertFigure());
		
		this.asnDetail.cancelReceive(cancelQtyBU);
	}
	/**
	 * 扣减收货包装数量，并刷新收货数量BU
	 * @param quantity
	 */
	public void removeQuantity(Double quantity) {
		this.receivedQuantity -= quantity;
		this.receivedQuantityBU = this.receivedQuantity * this.packageUnit.getConvertFigure();
	}
}