package com.vtradex.wms.server.action;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.thorn.server.dao.CommonDao;
import com.vtradex.wms.server.model.shipping.WmsWaveDoc;

public class WmsWaveDocSeprateDecisionAction implements ProcessAction {
	private CommonDao commonDao;

	public WmsWaveDocSeprateDecisionAction(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public String processAction(Object object) {
		WmsWaveDoc waveDoc = (WmsWaveDoc)object;
		String value = "";
		Long cnt = (Long) commonDao.findByQueryUniqueResult("SELECT COUNT(*) FROM WmsPickTicket wpt " +
				" WHERE wpt.waveDoc.id = :waveDocId AND wpt.status = 'SEPRATED'" , 
				new String[] {"waveDocId"}, new Object[] {waveDoc.getId()});
		if (cnt == null || cnt.longValue() == 0) {
			if(waveDoc.getPickedQuantityBU().doubleValue() < waveDoc.getAllocatedQuantityBU().doubleValue()){
				value = "PARTPICK";
			} else {
				value = "PICKUP";
			}
		} else {
			value = "SEPRATE";
		}
		return value;
	}

}
