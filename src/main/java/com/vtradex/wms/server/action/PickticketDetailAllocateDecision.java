package com.vtradex.wms.server.action;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.wms.server.model.shipping.WmsPickTicketDetail;

public class PickticketDetailAllocateDecision implements ProcessAction {

	public String processAction(Object object) {
		WmsPickTicketDetail detail = (WmsPickTicketDetail)object;
		String value = "NONE";
		
		if(detail.getExpectedQuantityBU().doubleValue()==detail.getAllocatedQuantityBU().doubleValue() 
				&& detail.getAllocatedQuantityBU().doubleValue()>0) {
			value = "ALL";
		}
		
		if(detail.getExpectedQuantityBU().doubleValue()>detail.getAllocatedQuantityBU().doubleValue()
				&&detail.getAllocatedQuantityBU().doubleValue()>0){
			value = "PART";
		}
		return value;
	}

}
