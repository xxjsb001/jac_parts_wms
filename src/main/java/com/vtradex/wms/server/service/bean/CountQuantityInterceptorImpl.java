package com.vtradex.wms.server.service.bean;

import java.util.List;

import com.vtradex.thorn.client.config.page.TableConfig;
import com.vtradex.thorn.client.ui.table.RowData;
import com.vtradex.thorn.server.service.intercepter.AfterReturnIntercepter;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.service.base.WmsOrganizationManager;

@SuppressWarnings("unchecked")
public class CountQuantityInterceptorImpl extends DefaultBaseManager implements AfterReturnIntercepter{
	private WmsOrganizationManager wmsOrganizationManager;

	public CountQuantityInterceptorImpl(WmsOrganizationManager wmsOrganizationManager) {
		super();
		this.wmsOrganizationManager = wmsOrganizationManager;
	}

	public TableConfig afterReturn(Object value) {
		TableConfig config = (TableConfig)value;
		List<RowData> list = config.getTableRows();
		
		for (RowData rw : list) {
			Long companyId = (Long) rw.getColumnValue(0);
			List<Double> result = wmsOrganizationManager.countQuantity(companyId);
			for(double count : result){
				rw.addColumnValue(count);
			}
		}
		return config;
	}
}