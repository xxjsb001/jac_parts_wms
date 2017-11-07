package com.vtradex.wms.server.action;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.wms.server.model.shipping.WmsWaveDoc;

public class WaveDocPickConfirmDecisionAction implements ProcessAction {

	public String processAction(Object object) {
		WmsWaveDoc waveDoc = (WmsWaveDoc)object;
		String value = "PICKED";
		if(!waveDoc.getExpectedQuantityBU().equals(waveDoc.getPickedQuantityBU())){
			value = "PICKING";
		}
		return value;
	}
}
