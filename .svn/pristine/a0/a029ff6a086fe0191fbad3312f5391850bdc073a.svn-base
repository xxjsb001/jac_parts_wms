package com.vtradex.wms.server.action;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.wms.server.model.shipping.WmsWaveDocDetail;

public class WaveDocDetailAllocateDecision  implements ProcessAction {

	public String processAction(Object object) {
		WmsWaveDocDetail detail = (WmsWaveDocDetail)object;
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
