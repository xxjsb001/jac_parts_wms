package com.vtradex.wms.server.telnet.dto;

import java.util.HashSet;
import java.util.Set;

public class WmsBOLDTO {
	
	/**
	 * 器具编码
	 */
	private Set<String> containers = new HashSet<String>();

	public Set<String> getContainers() {
		return containers;
	}

	public void setContainers(Set<String> containers) {
		this.containers = containers;
	}
}
