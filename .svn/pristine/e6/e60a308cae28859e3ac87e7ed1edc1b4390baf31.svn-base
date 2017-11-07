package com.vtradex.wms.server.action;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.thorn.server.dao.CommonDao;
import com.vtradex.wms.server.model.shipping.WmsPickTicket;

public class PickTicketShipDecisionAction implements ProcessAction {
	private CommonDao commonDao;
	
	public PickTicketShipDecisionAction(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public String processAction(Object object) {
		WmsPickTicket pickTicket = (WmsPickTicket)object;
		String value = "";
		if(pickTicket.getShippedQuantityBU().doubleValue() < pickTicket.getExpectedQuantityBU()){
			value = "PART";
		} else {
			Long cnt = (Long) commonDao.findByQueryUniqueResult("SELECT COUNT(*) FROM WmsTaskLog log " +
					" WHERE log.task.pickTicketDetail.pickTicket.id = :pickTicketId " +
					" AND (log.movedQuantityBU - log.pickBackQuantityBU) > log.shippedQuantityBU", 
					new String[] {"pickTicketId"}, new Object[] {pickTicket.getId()});
			if (cnt == null || cnt.longValue() == 0) {
				value = "ALL";
			} else {
				value = "PART";
			}
		}
		return value;
	}

}
