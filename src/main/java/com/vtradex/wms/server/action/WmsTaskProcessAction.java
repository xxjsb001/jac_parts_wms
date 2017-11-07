package com.vtradex.wms.server.action;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.move.WmsTaskStatus;

public class WmsTaskProcessAction implements ProcessAction {

	public String processAction(Object object) {
		if(object instanceof WmsTask){
			WmsTask task = (WmsTask)object;
			if(task.getPlanQuantityBU() <= 0D && task.getMovedQuantityBU() <= 0D){
				return WmsTaskStatus.FINISHED;
			}
			if(task.getMovedQuantityBU() <= 0D){
				return WmsTaskStatus.DISPATCHED;
			}
			if(task.isFinished()) {
				return WmsTaskStatus.FINISHED;
			}
			else{
				return WmsTaskStatus.WORKING;
			}
		}
		return WmsTaskStatus.OPEN;
	}
}
