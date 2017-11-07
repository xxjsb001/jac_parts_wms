package com.vtradex.wms.server.action;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.wms.server.model.move.WmsMoveDoc;

public class WmsMoveDocProcessAction implements ProcessAction {

	public String processAction(Object object) {
		if(object instanceof WmsMoveDoc){
			WmsMoveDoc wmsMoveDoc = (WmsMoveDoc)object;
			return wmsMoveDoc.isAllocate();
		}
		return "NONE";
	}
}
