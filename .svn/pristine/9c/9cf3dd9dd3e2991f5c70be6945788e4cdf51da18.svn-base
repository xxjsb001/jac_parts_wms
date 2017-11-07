package com.vtradex.wms.server.action;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.wms.server.model.shipping.WmsWaveDoc;

public class WaveDocPickDecisionAction implements ProcessAction {

	public String processAction(Object object) {
		WmsWaveDoc waveDoc = (WmsWaveDoc)object;
		String value = "";
		if(waveDoc.getPickedQuantityBU().doubleValue() == 0){
			value = "NONE";
		} else {
			if (waveDoc.getPickedQuantityBU().doubleValue() < waveDoc.getAllocatedQuantityBU().doubleValue()) {
				value = "PART";
			} else {
				value = "ALL";
			}
		}
		return value;
	}

}
