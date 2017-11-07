package com.vtradex.wms.server.model.inventory;

import java.util.List;

import com.vtradex.thorn.client.utils.StringUtils;
import com.vtradex.thorn.server.format.Formatter;

public class InventoryRedFormat implements Formatter {

	@Override
	public String format(String property, Object cellValue, List origenData,
			String referenceModel, String locale) {
		String isRedStr = (String) origenData.get(12);
		Boolean isRed = Boolean.FALSE;
		if(!StringUtils.isEmpty(isRedStr) && "true".equals(isRedStr)){
			isRed = Boolean.TRUE;
		}
		String x = "";
		if(isRed) {
//			x = "<div style='background-color:#FFAEB9'>"+cellValue+"</div>";
			x = "<font style='font-weight:bold;color:red'>"+cellValue+"</font>";
		}else{
			x = (String) cellValue;
		}
		
		return  x;
	}

	@Override
	public String exportFormat(String property, Object cellValue,
			List origenData, String referenceModel, String locale) {
		return format(property, cellValue, origenData, referenceModel, locale);
	}

}

