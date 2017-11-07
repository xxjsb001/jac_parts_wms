/**
 * 
 */
package com.vtradex.wms.server.action;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.wms.server.model.move.WmsMoveDoc;

/**
 * @author <a href="mailto:jin.liu@vtradex.net">刘晋</a>
 * @since 2012-7-16 下午12:50:24
 */
public class WmsProcessRecordDecistionAction implements ProcessAction{
	public String processAction(Object object) {
		WmsMoveDoc moveDoc = (WmsMoveDoc)object;
		String value = "";
		
		if(moveDoc.getProcessQuantityBU().doubleValue() >= moveDoc.getPlanQuantityBU().doubleValue()) {
			value = "Y";
		} else {
			value = "N";
		}
		return value;
	}
}
