package com.vtradex.wms.server.service.formatter;

import java.util.List;

import com.vtradex.rule.server.model.rule.RuleTableVersionStatus;
import com.vtradex.thorn.server.format.Formatter;

@SuppressWarnings("unchecked")
public class RuleTableVersionStatusFormatter implements Formatter{
	
	public String exportFormat(String property, Object cellValue,
			List origenData, String referenceModel, String locale) {
	    return cellValue == null ? "" : cellValue.toString();
	}

	public String format(String property, Object cellValue, List origenData,
			String referenceModel, String locale) {
		String status = cellValue == null ? "" : cellValue.toString();
		String result = "";
		if(RuleTableVersionStatus.ON_LINE.equals(status)) {
			result = "<img src=\"./ruleImage/vs_online.gif\" />";
		} else if(RuleTableVersionStatus.UNACTIVE.equals(status)) {
			result = "<img src=\"./ruleImage/vs_unactive.gif\" />";
		} else if(RuleTableVersionStatus.MODIFY.equals(status)) {
			result = "<img src=\"./ruleImage/vs_modify.gif\" />";
		} else {
			result = "&nbsp;";
		}
	    return result;
	}
}
