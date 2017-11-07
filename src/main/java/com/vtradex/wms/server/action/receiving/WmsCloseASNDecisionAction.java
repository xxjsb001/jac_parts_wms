package com.vtradex.wms.server.action.receiving;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.model.receiving.WmsReceivedRecord;

public class WmsCloseASNDecisionAction implements ProcessAction {
	
	public String processAction(Object object) {
		WmsASN asn = null;
		
		try {
			asn = (WmsASN) object;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		
		for (WmsReceivedRecord wr : asn.getRecords()) {
			if (!wr.getBeVerified()) {
				return "Y";
			}
		}
		return "N";
	}
}
