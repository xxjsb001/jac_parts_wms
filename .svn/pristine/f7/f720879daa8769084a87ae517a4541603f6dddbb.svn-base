package com.vtradex.wms.server.action.receiving;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.model.receiving.WmsASNDetail;

public class WmsConfirmDecisionAction implements ProcessAction {
	
	public String processAction(Object object) {
		WmsASN asn = null;
		
		try {
			asn = (WmsASN) object;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		
		if (asn == null) {
			throw new BusinessException("parement.error");
		}
		if (asn.getReceivedQuantityBU().doubleValue() <= 0) {
			return "-1";
		}
		if (asn.getExpectedQuantityBU().doubleValue() > asn.getReceivedQuantityBU().doubleValue()) {
			return "1";
		}
		else {
			boolean finish = true;
			for(WmsASNDetail detail : asn.getDetails()){
				if(detail.getExpectedQuantityBU().doubleValue() > detail.getReceivedQuantityBU().doubleValue()){
					finish = false;
					break;
				}
			}
			return finish ? "0" : "1";
		}
	}
}