package com.vtradex.wms.client.ui.page.allocate.page.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CustomItem implements IsSerializable {
	private Long id;
	private String code;
	private String name;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
