package com.vtradex.wms.server.action;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.wms.server.model.shipping.WmsWaveDoc;

public class WaveDocUnSeprateAllDecisionAction implements ProcessAction {

	public String processAction(Object object) {
		WmsWaveDoc waveDoc = (WmsWaveDoc)object;
		String value = "Y";
		if(waveDoc.getSplitedQuantityBU() > 0){
			value = "N";
		}
		return value;
	}
}
