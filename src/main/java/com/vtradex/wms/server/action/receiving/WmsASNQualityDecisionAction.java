package com.vtradex.wms.server.action.receiving;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.service.receiving.WmsASNManager;

public class WmsASNQualityDecisionAction implements ProcessAction {
	
	protected WmsASNManager wmsASNManager;
	
	public WmsASNQualityDecisionAction(WmsASNManager wmsASNManager){
		this.wmsASNManager = wmsASNManager;
	}
	
	public String processAction(Object object) {
		if(object instanceof WmsASN){
			boolean isQuality = wmsASNManager.asnQuality((WmsASN)object);
			if(isQuality){
				return "Y";
			}
		}
		return "N";
	}
}
