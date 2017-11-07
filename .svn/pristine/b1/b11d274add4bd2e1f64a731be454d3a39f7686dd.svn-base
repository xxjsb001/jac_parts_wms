package com.vtradex.wms.server.action.move;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.wms.server.model.move.WmsMoveDoc;

/**
 * 移位单分配Action
 *
 * @category Decision Action
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:50 $
 */
public class WmsMoveDocPickBackAction implements ProcessAction {
	
	private static final String ACTIVED = "ACTIVED";
	private static final String WORKING = "WORKING";
	private static final String FINISHED = "FINISHED";
	private static final String OPEN = "OPEN";

	public String processAction(Object object) {
		WmsMoveDoc moveDoc = (WmsMoveDoc)object;
		if(moveDoc.getMovedQuantityBU() <= 0D && moveDoc.getAllocatedQuantityBU() <= 0D){
			return OPEN;
		}
		if (moveDoc.getMovedQuantityBU() <= 0D) {
			return ACTIVED;
		} else if(moveDoc.getMovedQuantityBU().doubleValue() >= moveDoc.getPlanQuantityBU().doubleValue()){
			return FINISHED;
		}else{
			return WORKING;
		}
	}
}
