package com.vtradex.wms.server.action;

import com.vtradex.thorn.server.action.ProcessAction;

public class ProcessDocRecordDecisionAction implements ProcessAction {

	public String processAction(Object object) {
//		if(object instanceof WmsProcessDoc){
//			WmsProcessDoc processDoc = (WmsProcessDoc)object;
//			if(processDoc.getProcessQuantity() > 0) return "Y";
//		}
		return "N";
	}

}
