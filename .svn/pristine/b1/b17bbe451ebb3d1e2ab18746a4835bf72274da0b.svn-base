package com.vtradex.wms.server.model.receiving;

import com.vtradex.thorn.server.model.Entity;

/**
 * @category 托盘序列码表
 * @author yc.min
 * @version 1.0
 * @created 20150407
 */
public class JacPalletSerial extends Entity {
	private static final long serialVersionUID = 1L;
	/** ASN明细*/
	private WmsASNDetail asnDetail;
	/** 托盘号 */
	private String palletNo;
	/** 期待数量BU*/
	private Double expectedQuantityBU = 0D;
	/** 上架数量BU*/
	private Double movedQuantityBU = 0D;
	/**推荐库位ID*/
	private Long toLocationId;
	/** 推荐库位号 */
	private String toLocationCode;
	/**是否分配*/
	private Boolean bePutawayAuto = false;
	
	public JacPalletSerial(){}

	public Boolean getBePutawayAuto() {
		return bePutawayAuto;
	}

	public void setBePutawayAuto(Boolean bePutawayAuto) {
		this.bePutawayAuto = bePutawayAuto;
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

	public Double getExpectedQuantityBU() {
		return expectedQuantityBU;
	}

	public void setExpectedQuantityBU(Double expectedQuantityBU) {
		this.expectedQuantityBU = expectedQuantityBU;
	}

	public Double getMovedQuantityBU() {
		return movedQuantityBU;
	}

	public void setMovedQuantityBU(Double movedQuantityBU) {
		this.movedQuantityBU = movedQuantityBU;
	}

	public String getPalletNo() {
		return palletNo;
	}

	public void setPalletNo(String palletNo) {
		this.palletNo = palletNo;
	}

	public WmsASNDetail getAsnDetail() {
		return asnDetail;
	}

	public void setAsnDetail(WmsASNDetail asnDetail) {
		this.asnDetail = asnDetail;
	}

}