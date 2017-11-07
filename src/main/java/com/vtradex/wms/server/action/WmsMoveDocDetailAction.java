package com.vtradex.wms.server.action;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;

public class WmsMoveDocDetailAction implements ProcessAction {

	public String processAction(Object object) {
		if(object instanceof WmsMoveDocDetail){
			WmsMoveDocDetail moveDocDetail = (WmsMoveDocDetail)object;
			return moveDocDetail.isAllocate();
		}
		return "NONE";
	}
}
