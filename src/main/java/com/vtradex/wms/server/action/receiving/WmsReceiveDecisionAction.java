package com.vtradex.wms.server.action.receiving;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.wms.server.model.receiving.WmsASN;

public class WmsReceiveDecisionAction implements ProcessAction {

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
		
		if (asn.getReceivedQuantityBU().doubleValue() > 0) {
			return "Y";
		} else {
			return "N";
		}
	}
}
