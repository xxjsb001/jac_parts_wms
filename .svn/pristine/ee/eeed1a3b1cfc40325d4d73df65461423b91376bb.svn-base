package com.vtradex.wms.server.action;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.thorn.server.dao.CommonDao;
import com.vtradex.wms.server.model.move.WmsWorkDoc;
import com.vtradex.wms.server.model.move.WmsWorkDocStatus;

public class WmsWorkDocProcessAction implements ProcessAction {
	private CommonDao commonDao;

	public WmsWorkDocProcessAction(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public String processAction(Object object) {
		if(object instanceof WmsWorkDoc){
			WmsWorkDoc workDoc = (WmsWorkDoc)object;
			if(workDoc.getExpectedQuantityBU() <= 0D && workDoc.getMovedQuantityBU() <= 0D){
				return WmsWorkDocStatus.FINISHED;
			}
			if(workDoc.getMovedQuantityBU() <= 0D){
				return WmsWorkDocStatus.OPEN;
			}
			Long cnt = (Long) commonDao.findByQueryUniqueResult(
					"SELECT COUNT(*) FROM WmsTask task WHERE task.workDoc.id = :workDocId AND task.status NOT IN ('FINISHED','CANCEL')", 
					new String[] {"workDocId"}, 
					new Object[] {workDoc.getId()});
			if (cnt != null && cnt > 0) {
				return WmsWorkDocStatus.WORKING;
			} else {
				return WmsWorkDocStatus.FINISHED;
			}
		}
		return WmsWorkDocStatus.OPEN;
	}
}
