package com.vtradex.wms.client.packagingtable.data;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Item Info
 *
 * @category DTO
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:49 $
 */
public class MoveDocDetailDTO implements IsSerializable {

	private Long id;
	private Long moveDocId;
	private String itemCode;
	private String itemName;
	private String barCode;
	private String unit;
	private String weight;
	private String volume;
	private String allocatedQuantityBU;
	private String movedQuantityBU;
	private String packagedQuantityBU = "0";
	private String unPackageQuantityBU = "0";
	
	/** 扫描数 */
	private String scanQuantityBU = "0";
	
	private String lotInfoStr;
	
	public MoveDocDetailDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getMoveDocId() {
		return moveDocId;
	}

	public void setMoveDocId(Long moveDocId) {
		this.moveDocId = moveDocId;
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
	
	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getAllocatedQuantityBU() {
		return allocatedQuantityBU;
	}

	public void setAllocatedQuantityBU(String allocatedQuantityBU) {
		this.allocatedQuantityBU = allocatedQuantityBU;
	}

	public String getMovedQuantityBU() {
		return movedQuantityBU;
	}

	public void setMovedQuantityBU(String movedQuantityBU) {
		this.movedQuantityBU = movedQuantityBU;
	}

	public String getPackagedQuantityBU() {
		return packagedQuantityBU;
	}

	public void setPackagedQuantityBU(String packagedQuantityBU) {
		this.packagedQuantityBU = packagedQuantityBU;
	}

	public String getUnPackageQuantityBU() {
		return unPackageQuantityBU;
	}

	public void setUnPackageQuantityBU(String unPackageQuantityBU) {
		this.unPackageQuantityBU = unPackageQuantityBU;
	}

	public String getLotInfoStr() {
		return lotInfoStr;
	}

	public void setLotInfoStr(String lotInfoStr) {
		this.lotInfoStr = lotInfoStr;
	}
	
	public String setColor(Object value, String color){
		return "<font color='" + color + "'>" + (value == null ? "-" : value) + "</font>"; 
	}
	
	public String getScanQuantityBU() {
		return scanQuantityBU;
	}

	public void setScanQuantityBU(String scanQuantityBU) {
		this.scanQuantityBU = scanQuantityBU;
	}

	public Object[] getData() {
		if (Double.valueOf(unPackageQuantityBU).doubleValue() == 0.0D) {
			return new Object[] {
					this.id, 
					setColor(itemCode, "#007300"),
					setColor(itemName, "#007300"),
					setColor(unit, "#007300"),
					setColor(allocatedQuantityBU, "#007300"),
					setColor(movedQuantityBU, "#007300"),
					setColor(packagedQuantityBU, "#007300"),
					setColor(unPackageQuantityBU, "#007300"),
					setColor(lotInfoStr, "#007300"),
				};
		} else {
			return new Object[] {
					this.id, 
					setColor(itemCode, "#b90000"),
					setColor(itemName, "#b90000"),
					setColor(unit, "#b90000"),
					setColor(allocatedQuantityBU, "#b90000"),
					setColor(movedQuantityBU, "#b90000"),
					setColor(packagedQuantityBU, "#b90000"),
					setColor(unPackageQuantityBU, "#b90000"),
					setColor(lotInfoStr, "#b90000")
				};
		}
	}
	
	public Object[] getNormalData() {
		return new Object[] {
				this.id, 
				this.moveDocId,
				this.itemCode, 
				this.itemName, 
				this.barCode,
				this.unit, 
				this.weight,
				this.volume,
				this.allocatedQuantityBU, 
				this.movedQuantityBU,
				this.packagedQuantityBU,
				this.unPackageQuantityBU,
				this.scanQuantityBU,
				this.lotInfoStr
			};
	}
}
