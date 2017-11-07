package com.vtradex.wms.server.action.receiving;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.service.receiving.WmsASNManager;

public class WmsASNQualityConfirmDecistionAction implements ProcessAction {
	
	protected WmsASNManager wmsASNManager;
	
	public WmsASNQualityConfirmDecistionAction(WmsASNManager wmsASNManager){
		this.wmsASNManager = wmsASNManager;
	}
	
	public String processAction(Object object) {
		if(object instanceof WmsASN){
			boolean isQuality = wmsASNManager.isExistUnQuality((WmsASN)object);
			if(isQuality){
				return "Y";
			}
		}
		return "N";
	}
}
