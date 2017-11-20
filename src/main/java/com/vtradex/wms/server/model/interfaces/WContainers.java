package com.vtradex.wms.server.model.interfaces;

import com.vtradex.thorn.server.model.VersionalEntity;

public class WContainers extends VersionalEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WBols bol;
	
	private String container;
	
	private String descrption;

	public WContainers(WBols bol, String container) {
		super();
		this.bol = bol;
		this.container = container;
	}

	public WContainers() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WBols getBol() {
		return bol;
	}

	public void setBol(WBols bol) {
		this.bol = bol;
	}

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	public String getDescrption() {
		return descrption;
	}

	public void setDescrption(String descrption) {
		this.descrption = descrption;
	}
	
}
