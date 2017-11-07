package com.vtradex.wms.server.service.notransactional.pojo;

import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.service.notransactional.WmsNoTransactionalOne;
import com.vtradex.wms.server.service.workDoc.WmsWorkDocManager;

public class DefaultWmsNoTransactionalOne extends DefaultBaseManager implements WmsNoTransactionalOne {
	private WorkflowManager workflowManager;
	public DefaultWmsNoTransactionalOne(WorkflowManager workflowManager){
		super();
		this.workflowManager = workflowManager;
	}
	public void activeConfirm(WmsMoveDoc moveDoc) {
		WmsWorkDocManager wmsWorkDocManager = (WmsWorkDocManager) applicationContext.getBean("wmsWorkDocManager");
		moveDoc = commonDao.load(WmsMoveDoc.class, moveDoc.getId());
		wmsWorkDocManager.activePickByJac(moveDoc);
		if(!WmsMoveDocStatus.WORKING.equals(moveDoc.getStatus())){
			workflowManager.doWorkflow(moveDoc, "wmsMoveDocProcess.active");
		}
		
		wmsWorkDocManager.pickConfirmAll(moveDoc);
		moveDoc = commonDao.load(WmsMoveDoc.class, moveDoc.getId());
		workflowManager.doWorkflow(moveDoc, "wmsMoveDocProcess.confirmAll");
	}

}
