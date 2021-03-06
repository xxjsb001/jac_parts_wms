package com.vtradex.wms.server.model.receiving;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.base.LotInfo;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;


/**
 * @category ASN明细
 * @author peng.lei
 * @version 1.0
 * @created 15-二月-2011 10:07:19
 */
public class WmsASNDetail extends Entity{
	
	private static final long serialVersionUID = -5874135491663001562L;
	/** ASN*/
	private WmsASN asn;
	/** 行号*/
	private Integer lineNo;
	/** 货品*/
	private WmsItem item;
	/** 托盘号 */
	private String pallet;
	/** 箱号 */
	private String carton;
	/** 序列号 */
	private String serialNo;
	/** 批次属性*/
	private LotInfo lotInfo = new LotInfo();
	/** 期待包装数量*/
	private Double expectedQuantity = 0.0D;
	/** 包装单位*/
	private WmsPackageUnit packageUnit;
	/** 期待数量BU*/
	private Double expectedQuantityBU = 0.0D;
	/** 收货数量BU*/
	private Double receivedQuantityBU = 0.0D;
	/** 上架数量BU*/
	private Double movedQuantityBU = 0.0D;
	
	/** 月台预约 **/
	private WmsBooking booking;
	
	/** 是否需要质检 **/
	private boolean beQuality = false;
	/**===============JAC======================*/
	/** 托盘用量*/
	private Integer palletNo = 0;
	/**是否收货*/
	private Boolean beReceived = false;
	/** 描述 */
	private String description;
	/** 待检量BU*/
	private Double qualityQuantityBU = 0.0D;
	/** 质检唯一编码 */
	private String qualityCode;
	
	/** 是否码托*/
	private Boolean isSupport;
	
	/**s是否送检码托*/
	private Boolean isCheckMT;
	
	private Integer polineNo;
	private Integer asnLineNo;
	
	public Integer getPolineNo() {
		return polineNo;
	}

	public void setPolineNo(Integer polineNo) {
		this.polineNo = polineNo;
	}

	public Integer getAsnLineNo() {
		return asnLineNo;
	}

	public void setAsnLineNo(Integer asnLineNo) {
		this.asnLineNo = asnLineNo;
	}
	
	public WmsASNDetail(){
	}
	
	
	
	public WmsASNDetail(WmsASN asn, Integer lineNo, WmsItem item,
			Double expectedQuantity,WmsPackageUnit packageUnit,
			 Double expectedQuantityBU,Double receivedQuantityBU,
			 Double movedQuantityBU,Integer palletNo, Double qualityQuantityBU,
			 Boolean isSupport) {
		super();
		this.asn = asn;
		this.lineNo = lineNo;
		this.item = item;
		this.expectedQuantity = expectedQuantity;
		this.packageUnit = packageUnit;
		this.expectedQuantityBU = expectedQuantityBU;
		this.receivedQuantityBU = receivedQuantityBU;
		this.movedQuantityBU = movedQuantityBU;
		this.palletNo = palletNo;
		this.qualityQuantityBU = qualityQuantityBU;
		this.isSupport = isSupport;
	}



	public Boolean getIsCheckMT() {
		return isCheckMT;
	}



	public void setIsCheckMT(Boolean isCheckMT) {
		this.isCheckMT = isCheckMT;
	}



	public Boolean getIsSupport() {
		return isSupport;
	}



	public void setIsSupport(Boolean isSupport) {
		this.isSupport = isSupport;
	}



	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getBeReceived() {
		return beReceived;
	}

	public void setBeReceived(Boolean beReceived) {
		this.beReceived = beReceived;
	}

	public Integer getPalletNo() {
		return palletNo;
	}

	public void setPalletNo(Integer palletNo) {
		this.palletNo = palletNo;
	}

	public WmsBooking getBooking() {
		return booking;
	}



	public void setBooking(WmsBooking booking) {
		this.booking = booking;
	}



	public boolean isBeQuality() {
		return beQuality;
	}

	public void setBeQuality(boolean beQuality) {
		this.beQuality = beQuality;
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

	public WmsASN getAsn() {
		return asn;
	}

	public void setAsn(WmsASN asn) {
		this.asn = asn;
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

	public WmsItem getItem() {
		return item;
	}

	public void setItem(WmsItem item) {
		this.item = item;
	}

	public Integer getLineNo() {
		return lineNo;
	}

	public void setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
	}

	public LotInfo getLotInfo() {
		return lotInfo;
	}

	public void setLotInfo(LotInfo lotInfo) {
		this.lotInfo = lotInfo;
	}

	public WmsPackageUnit getPackageUnit() {
		return packageUnit;
	}

	public void setPackageUnit(WmsPackageUnit packageUnit) {
		this.packageUnit = packageUnit;
	}

	public Double getReceivedQuantityBU() {
		return receivedQuantityBU;
	}

	public void setReceivedQuantityBU(Double receivedQuantityBU) {
		this.receivedQuantityBU = receivedQuantityBU;
	}

	/**
	 * 获取未收货数量
	 * @return
	 */
	public double getUnReceivedQtyBU() {
		return this.expectedQuantityBU - this.receivedQuantityBU;
	}

	/**
	 * 收货
	 * @param quantity
	 */
	public void receive(double quantity) {
		this.receivedQuantityBU += quantity;
		this.beReceived = true;
		this.asn.receive(quantity);
	}
	
	/**
	 * 取消收货
	 * @param quantity
	 */
	public void cancelReceive(double quantity) {
		this.receivedQuantityBU -= quantity;
		this.beReceived = false;//yc.min
		this.asn.cancelReceive(quantity);
	}
	
	/** 上架 */
	public void addMovedQuantity(Double movedQuantityBU) {
		this.movedQuantityBU += movedQuantityBU;
		asn.addMovedQuantity(movedQuantityBU);
	}
	
	/** 取消上架 */
	public void cancelMovedQuantity(Double movedQuantityBU) {
		this.movedQuantityBU -= movedQuantityBU;
		asn.cancelMovedQuantity(movedQuantityBU);
	}
	public double getUnMoveQtyBU() {
		return this.receivedQuantityBU - this.movedQuantityBU;
	}
	public Double getMovedQuantityBU() {
		return movedQuantityBU;
	}


	public void setMovedQuantityBU(Double movedQuantityBU) {
		this.movedQuantityBU = movedQuantityBU;
	}

	public Double getQualityQuantityBU() {
		return qualityQuantityBU;
	}

	public void setQualityQuantityBU(Double qualityQuantityBU) {
		this.qualityQuantityBU = qualityQuantityBU;
	}

	public String getQualityCode() {
		return qualityCode;
	}

	public void setQualityCode(String qualityCode) {
		this.qualityCode = qualityCode;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.getId()).toHashCode();
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof WmsASNDetail))
			return false;
		WmsASNDetail castOther = (WmsASNDetail) other;
		return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
	}
	
}