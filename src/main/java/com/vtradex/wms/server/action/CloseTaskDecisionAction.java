package com.vtradex.wms.server.action;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.wms.server.model.move.WmsTask;

public class CloseTaskDecisionAction implements ProcessAction {
	public String processAction(Object object) {
		WmsTask task = (WmsTask)object;
		String value = "CANCEL";
		if(task.getMovedQuantityBU().doubleValue() > 0){
			value = "FINISHED";
		}else{
			value = "CANCEL";
		}
		return value;
	}
}
