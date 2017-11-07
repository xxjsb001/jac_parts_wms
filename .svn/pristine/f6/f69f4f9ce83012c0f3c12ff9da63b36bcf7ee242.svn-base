package com.vtradex.wms.server.telnet.dto;

import java.util.ArrayList;
import java.util.List;

import com.vtradex.wms.server.model.base.LotInfo;
import com.vtradex.wms.server.model.organization.WmsItemState;
import com.vtradex.wms.server.model.organization.WmsLotRule;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;


/**
 * 
 * @category DTO
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:54 $
 */
public class WmsASNDetailDTO {
	
	private Long asnId;
	private String asnCode;
	private Long asnDetailId;
	private String itemCode;
	private String itemName;
	private Boolean beTrackLotInfo = Boolean.FALSE;
	private Double expectedQuantityBU;
	private Long locationId;
	private String locationCode;
	private Long currentPackageUnit;
	private Double receivingQuantityBU;
	private LotInfo lotInfo;
	private WmsLotRule lotRule;
	private Long itemStateId;
	
	private String packageUnitName;
	private Double packageQuantity;
	
	public String getPackageUnitName() {
		return packageUnitName;
	}

	public void setPackageUnitName(String packageUnitName) {
		this.packageUnitName = packageUnitName;
	}

	public Double getPackageQuantity() {
		return packageQuantity;
	}

	public void setPackageQuantity(Double packageQuantity) {
		this.packageQuantity = packageQuantity;
	}

	private List<WmsItemState> statusList = new ArrayList<WmsItemState>();
	private List<WmsPackageUnit> packageUnitList = new ArrayList<WmsPackageUnit>();
	
	public WmsASNDetailDTO() {
	}

	public Long getAsnId() {
		return asnId;
	}

	public void setAsnId(Long asnId) {
		this.asnId = asnId;
	}

	public Long getAsnDetailId() {
		return asnDetailId;
	}

	public void setAsnDetailId(Long asnDetailId) {
		this.asnDetailId = asnDetailId;
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

	public Double getExpectedQuantityBU() {
		return expectedQuantityBU;
	}

	public void setExpectedQuantityBU(Double expectedQuantityBU) {
		this.expectedQuantityBU = expectedQuantityBU;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	
	public Long getCurrentPackageUnit() {
		return currentPackageUnit;
	}

	public void setCurrentPackageUnit(Long currentPackageUnit) {
		this.currentPackageUnit = currentPackageUnit;
	}

	public List<WmsPackageUnit> getPackageUnitList() {
		return packageUnitList;
	}

	public void setPackageUnitList(List<WmsPackageUnit> packageUnitList) {
		this.packageUnitList = packageUnitList;
	}

	public Double getReceivingQuantityBU() {
		return receivingQuantityBU;
	}

	public void setReceivingQuantityBU(Double receivingQuantityBU) {
		this.receivingQuantityBU = receivingQuantityBU;
	}

	public LotInfo getLotInfo() {
		return lotInfo;
	}

	public void setLotInfo(LotInfo lotInfo) {
		this.lotInfo = lotInfo;
	}

	public List<WmsItemState> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<WmsItemState> statusList) {
		this.statusList = statusList;
	}

	public WmsLotRule getLotRule() {
		return lotRule;
	}

	public void setLotRule(WmsLotRule lotRule) {
		this.lotRule = lotRule;
	}

	public String getAsnCode() {
		return asnCode;
	}

	public void setAsnCode(String asnCode) {
		this.asnCode = asnCode;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Long getItemStateId() {
		return itemStateId;
	}

	public void setItemStateId(Long itemStateId) {
		this.itemStateId = itemStateId;
	}

	public Boolean getBeTrackLotInfo() {
		return beTrackLotInfo;
	}

	public void setBeTrackLotInfo(Boolean beTrackLotInfo) {
		this.beTrackLotInfo = beTrackLotInfo;
	}
}
