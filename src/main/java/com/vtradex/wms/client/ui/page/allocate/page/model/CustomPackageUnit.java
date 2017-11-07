package com.vtradex.wms.client.ui.page.allocate.page.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CustomPackageUnit implements IsSerializable{
	private Long id;
	private String unit;
	private int convertFigure;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getConvertFigure() {
		return convertFigure;
	}
	public void setConvertFigure(int convertFigure) {
		this.convertFigure = convertFigure;
	}
	
}
