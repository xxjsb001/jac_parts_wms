package com.vtradex.wms.server.action;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.wms.server.model.shipping.WmsWaveDoc;

public class WaveDocWorkingDecisionAction implements ProcessAction {

	public String processAction(Object object) {
		WmsWaveDoc waveDoc = (WmsWaveDoc)object;
		String value = "";
		if(waveDoc.getAllocatedQuantityBU().doubleValue() <= 0) {
			value = "OPEN";
		} else {
			value = "WORKING";
		}
		return value;
	}

}
