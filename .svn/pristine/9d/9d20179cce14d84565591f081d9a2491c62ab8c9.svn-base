package com.vtradex.wms.server.model.organization;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;

public class WmsUserSupplier extends Entity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@UniqueKey
	private WmsUserSupplierHead userHead;
	@UniqueKey
	private WmsOrganization supplier;

	public WmsUserSupplierHead getUserHead() {
		return userHead;
	}

	public void setUserHead(WmsUserSupplierHead userHead) {
		this.userHead = userHead;
	}

	public WmsOrganization getSupplier() {
		return supplier;
	}

	public void setSupplier(WmsOrganization supplier) {
		this.supplier = supplier;
	}

	public WmsUserSupplier() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WmsUserSupplier(WmsUserSupplierHead userHead,
			WmsOrganization supplier) {
		super();
		this.userHead = userHead;
		this.supplier = supplier;
	}

}
