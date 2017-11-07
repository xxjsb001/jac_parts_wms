package com.vtradex.wms.server.action;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.wms.server.model.shipping.WmsPickTicket;
import com.vtradex.wms.server.model.shipping.WmsPickTicketStatus;

public class PickTicketBaseShipDecision implements ProcessAction{
	public String processAction(Object object) {
		WmsPickTicket pickTicket = (WmsPickTicket)object;
		String value = "";
		if(pickTicket.getShippedQuantityBU().doubleValue() >= pickTicket.getExpectedQuantityBU().doubleValue() && pickTicket.getShippedQuantityBU().doubleValue() >= pickTicket.getPickedQuantityBU().doubleValue()) {
			value = WmsPickTicketStatus.FINISHED;
		} else {
			value = WmsPickTicketStatus.WORKING;
		}
		return value;
	}
}
