package com.vtradex.wms.server.service.base.pojo;

import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.warehouse.WmsWarehouseArea;
import com.vtradex.wms.server.service.base.WmsWarehouseAreaManager;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

public class DefaultWmsWarehouseAreaManager extends DefaultBaseManager implements WmsWarehouseAreaManager {
	
	protected WorkflowManager workflowManager;
	
	public DefaultWmsWarehouseAreaManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}
	
	public void storeWarehouseArea(WmsWarehouseArea wareHouseAreas) {
		wareHouseAreas.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
		commonDao.store(wareHouseAreas);
	}

}