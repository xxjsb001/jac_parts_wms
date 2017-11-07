package com.vtradex.wms.server.model.middle;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
/**仓库货主接口映射表*/
public class MiddleCompanyExtends extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//发送方	
	@UniqueKey
	private String sender;
	//仓库	
	private WmsWarehouse warehouse;
	//货主
	private WmsOrganization company;
	//备注
	private String description;
	
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}
	
	public WmsOrganization getCompany() {
		return company;
	}

	public void setCompany(WmsOrganization company) {
		this.company = company;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
