package com.vtradex.wms.server.model.receiving;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class WmsReceivedRecordTemp {
	private Long locationId;
	private Long itemKeyId;
	private Long packageUnitId;
	private String inventoryStatus;
	private Double quantity = 0D;
	
	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof WmsReceivedRecordTemp))
			return false;
		WmsReceivedRecordTemp castOther = (WmsReceivedRecordTemp) other;
		return new EqualsBuilder().append(locationId, castOther.locationId)
				.append(itemKeyId, castOther.itemKeyId)
				.append(packageUnitId, castOther.packageUnitId).append(inventoryStatus,
						castOther.inventoryStatus).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(locationId).append(itemKeyId).append(
				packageUnitId).append(inventoryStatus).toHashCode();
	}
	

	public String getInventoryStatus() {
		return inventoryStatus;
	}

	public void setInventoryStatus(String inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Long getItemKeyId() {
		return itemKeyId;
	}

	public void setItemKeyId(Long itemKeyId) {
		this.itemKeyId = itemKeyId;
	}

	public Long getPackageUnitId() {
		return packageUnitId;
	}

	public void setPackageUnitId(Long packageUnitId) {
		this.packageUnitId = packageUnitId;
	}

	public void addQuantity(Double receivedQuantity) {
		this.quantity += receivedQuantity;
	}
}