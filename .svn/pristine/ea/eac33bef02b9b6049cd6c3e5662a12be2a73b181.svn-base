package com.vtradex.wms.client.ui.javabean;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PT_DETAILS implements IsSerializable {

	private Long pickTicketDetailId;
	private Long itemId;
	private Long packageUnitId;

	private Integer packageUnitConvertFigure;

	private String itemCode;
	private String itemName;
	private String packageUnit;

	private double expectedQuantityBU;
	private double allocatedQuantityBU;
	private double pickedQuantityBU;
	private double shippedQuantityBU;
	private String shipLotInfo;
	private String storageDate;
	private String soi;
	private String batchNum;
	private String produceDate;
	private String expireDate;
	private String warnDate;
	private String supplier;

	public PT_DETAILS() {
	}
//
//	public PT_DETAILS(Object pickTicketDetailId, Object itemId, Object itemCode, Object itemName,
//			Object packageUnit, Object expectedQuantityBU, Object allocatedQuantityBU, 
//			Object pickedQuantityBU, Object splitedQuantityBU,
//			Object storageDate, Object soi,Object batchNum,Object serialNo,
//			Object produceDate,Object expireDate,Object warnDate,Object supplier) {
//		
//		this.pickTicketDetailId = (Long)this.pickTicketDetailId;
//		this.itemId = (Long)itemId;
//		this.itemCode = (String)itemCode;
//		this.itemName = (String)itemName;
//		this.packageUnit = (String)packageUnit;
//		this.expectedQuantityBU = (Double)expectedQuantityBU;
//		this.allocatedQuantityBU = (Double)allocatedQuantityBU;
//		this.pickedQuantityBU = (Double)pickedQuantityBU;
//		this.splitedQuantityBU = (Double)splitedQuantityBU;
//		
//		this.storageDate = storageDate == null ? "" : (String) storageDate;
//		this.soi = soi == null ? "" : (String) soi;
//		this.batchNum = batchNum == null ? "" : (String) batchNum;
//		this.serialNo = serialNo == null ? "" : (String) serialNo;
//		this.produceDate = produceDate == null ? "" : (String) produceDate;
//		this.expireDate = expireDate == null ? "" : (String) expireDate;
//		this.warnDate = warnDate == null ? "" : (String) warnDate;
//		this.supplier = supplier == null ? "" : (String) supplier;
//
//		this.shipLotInfo = this.toLotInfor(this.storageDate, this.soi,
//				this.batchNum, this.serialNo, this.produceDate, this.expireDate, this.warnDate, this.supplier);
//	}
	
	public PT_DETAILS(Object pickTicketDetailId, Object itemId, Object packageUnitId, Object packageUnitConvertFigure,
			Object itemCode, Object itemName, Object packageUnit, Object expectedQuantityBU, Object allocatedQuantityBU,
			Object pickedQuantityBU, Object shippedQuantityBU, Object storageDate, Object soi,
			Object batchNum, Object produceDate, Object expireDate, Object warnDate, Object supplier) {
		this.pickTicketDetailId = (Long) pickTicketDetailId;
		this.itemId = (Long) itemId;
		this.packageUnitId = (Long) packageUnitId;
		this.packageUnitConvertFigure = (Integer) packageUnitConvertFigure;
		this.itemCode = (String) itemCode;
		this.itemName = (String) itemName;
		this.packageUnit = (String) packageUnit;
		this.expectedQuantityBU = (Double) expectedQuantityBU;
		this.allocatedQuantityBU = (Double) allocatedQuantityBU;
		this.pickedQuantityBU = (Double) pickedQuantityBU;
		this.shippedQuantityBU = (Double) shippedQuantityBU;

		this.storageDate = storageDate == null ? "" : (String) storageDate;
		this.soi = soi == null ? "" : (String) soi;
		this.batchNum = batchNum == null ? "" : (String) batchNum;
		this.produceDate = produceDate == null ? "" : (String) produceDate;
		this.expireDate = expireDate == null ? "" : (String) expireDate;
		this.warnDate = warnDate == null ? "" : (String) warnDate;
		this.supplier = supplier == null ? "" : (String) supplier;

		this.shipLotInfo = this.toLotInfor(this.storageDate, this.soi,
				this.batchNum, this.produceDate, this.expireDate, this.warnDate, this.supplier);
	}

	public String toLotInfor(String propC1, String propC2, String propC3,
			String propC4, String propC5, String propC6, String propC7) {
		String result = "";
		
		if (propC1 != null && !"".equals(propC1)) {
			result += "#" + propC1;
		}
		if (propC2 != null && !"".equals(propC2)) {
			result += "#" + propC2;
		}
		if (propC3 != null && !"".equals(propC3)) {
			result += "#" + propC3;
		}
		if (propC4 != null && !"".equals(propC4)) {
			result += "#" + propC4;
		}
		if (propC5 != null && !"".equals(propC5)) {
			result += "#" + propC5;
		}
		if (propC6 != null && !"".equals(propC6)) {
			result += "#" + propC6;
		}
		if (propC7 != null && !"".equals(propC7)) {
			result += "#" + propC7;
		}
		if (result != null) {
			result = result.replaceFirst("#", "");
		}
		
		return result;
	}

	public Long getPickTicketDetailId() {
		return pickTicketDetailId;
	}

	public void setPickTicketDetailId(Long pickTicketDetailId) {
		this.pickTicketDetailId = pickTicketDetailId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getPackageUnitId() {
		return packageUnitId;
	}

	public void setPackageUnitId(Long packageUnitId) {
		this.packageUnitId = packageUnitId;
	}

	public Integer getPackageUnitConvertFigure() {
		return packageUnitConvertFigure;
	}

	public void setPackageUnitConvertFigure(Integer packageUnitConvertFigure) {
		this.packageUnitConvertFigure = packageUnitConvertFigure;
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

	public String getPackageUnit() {
		return packageUnit;
	}

	public void setPackageUnit(String packageUnit) {
		this.packageUnit = packageUnit;
	}

	public double getExpectedQuantityBU() {
		return expectedQuantityBU;
	}

	public void setExpectedQuantityBU(double expectedQuantityBU) {
		this.expectedQuantityBU = expectedQuantityBU;
	}

	public double getAllocatedQuantityBU() {
		return allocatedQuantityBU;
	}

	public void setAllocatedQuantityBU(double allocatedQuantityBU) {
		this.allocatedQuantityBU = allocatedQuantityBU;
	}

	public double getPickedQuantityBU() {
		return pickedQuantityBU;
	}

	public void setPickedQuantityBU(double pickedQuantityBU) {
		this.pickedQuantityBU = pickedQuantityBU;
	}

	public double getShippedQuantityBU() {
		return shippedQuantityBU;
	}

	public void setShippedQuantityBU(double shippedQuantityBU) {
		this.shippedQuantityBU = shippedQuantityBU;
	}

	public String getShipLotInfo() {
		return shipLotInfo;
	}

	public void setShipLotInfo(String shipLotInfo) {
		this.shipLotInfo = shipLotInfo;
	}

	public String getStorageDate() {
		return storageDate;
	}

	public void setStorageDate(String storageDate) {
		this.storageDate = storageDate;
	}

	public String getSoi() {
		return soi;
	}

	public void setSoi(String soi) {
		this.soi = soi;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public String getProduceDate() {
		return produceDate;
	}

	public void setProduceDate(String produceDate) {
		this.produceDate = produceDate;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getWarnDate() {
		return warnDate;
	}

	public void setWarnDate(String warnDate) {
		this.warnDate = warnDate;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public Object[] toArray() {
		return new Object[] { this.pickTicketDetailId, this.itemId,
				this.packageUnitId, this.packageUnitConvertFigure,
				this.itemCode, this.itemName, this.packageUnit,
				this.expectedQuantityBU, this.allocatedQuantityBU,
				this.pickedQuantityBU, this.shippedQuantityBU,
				this.shipLotInfo, this.storageDate, this.soi, this.batchNum,
				this.produceDate, this.expireDate,
				this.warnDate, this.supplier };
	}
}