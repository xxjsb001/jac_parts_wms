package com.vtradex.wms.server.service.base.pojo;

import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.organization.WmsItemState;
import com.vtradex.wms.server.service.base.WmsItemStateManager;

public class DefaultWmsItemStateManager extends DefaultBaseManager implements
		WmsItemStateManager {

	public void deleteItemState(WmsItemState wmsItemState) {
		commonDao.delete(wmsItemState);
	}
}
