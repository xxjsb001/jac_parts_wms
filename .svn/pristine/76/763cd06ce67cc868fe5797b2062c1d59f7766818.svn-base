package com.vtradex.wms.server.service.bean;

import java.util.ArrayList;
import java.util.List;

import com.vtradex.thorn.client.config.page.TableConfig;
import com.vtradex.thorn.client.ui.table.RowData;
import com.vtradex.thorn.server.service.intercepter.AfterReturnIntercepter;
import com.vtradex.wms.server.service.base.WmsItemManager;

@SuppressWarnings("unchecked")
public class LotInfoInterceptorImpl implements AfterReturnIntercepter {
	private WmsItemManager itemManager;

	public LotInfoInterceptorImpl(WmsItemManager itemManager) {
		super();
		this.itemManager = itemManager;
	}

	public Object afterReturn(Object object) {
		if(object instanceof TableConfig){
			TableConfig config = (TableConfig)object;
			List<RowData> list = config.getTableRows();
			
			for (RowData rw : list) {
				Long itemId = (Long) rw.getColumnValue(0);
				
				List<Boolean> results = itemManager.getLotRuleTackers(itemId);
				
				rw.addColumnValue(results);
			}
			
			return config;
		}
		else if(object instanceof RowData[]){
			RowData[] rowDatas = (RowData[])object;			
			 List<RowData> list = new ArrayList();
				for(RowData rs : rowDatas){
					list.add(rs);
				}
				for (RowData rw : list) {
					Long itemId = (Long) rw.getColumnValue(0);
					
					List<Boolean> results = itemManager.getLotRuleTackers(itemId);
					
					rw.addColumnValue(results);
				}				
				return rowDatas;
		}
		return null;
	}
}