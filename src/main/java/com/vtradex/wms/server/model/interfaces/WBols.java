package com.vtradex.wms.server.model.interfaces;

import java.util.HashSet;
import java.util.Set;

import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.carrier.WmsVehicle;

public class WBols extends VersionalEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String pickCode;
	
	private WmsVehicle vehicle;

	private Set<WContainers> details = new HashSet<WContainers>();
	
	public WBols(String pickCode, WmsVehicle vehicle) {
		super();
		this.pickCode = pickCode;
		this.vehicle = vehicle;
	}

	public WBols() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Set<WContainers> getDetails() {
		return details;
	}

	public void setDetails(Set<WContainers> details) {
		this.details = details;
	}

	public String getPickCode() {
		return pickCode;
	}

	public void setPickCode(String pickCode) {
		this.pickCode = pickCode;
	}

	public WmsVehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(WmsVehicle vehicle) {
		this.vehicle = vehicle;
	}
	
}
