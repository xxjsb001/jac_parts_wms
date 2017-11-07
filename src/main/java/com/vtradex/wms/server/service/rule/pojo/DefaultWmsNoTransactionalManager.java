package com.vtradex.wms.server.service.rule.pojo;

import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.service.receiving.WmsASNManager;
import com.vtradex.wms.server.service.rule.WmsNoTransactionalManager;

/**
 * @author: 李炎
 */
public class DefaultWmsNoTransactionalManager extends DefaultBaseManager
		implements WmsNoTransactionalManager {
	
	protected WmsASNManager wmsASNManager;
	private WorkflowManager workflowManager;
	
	public DefaultWmsNoTransactionalManager(WmsASNManager wmsASNManager,WorkflowManager workflowManager){
		this.wmsASNManager = wmsASNManager;
		this.workflowManager = workflowManager;
	}

	public void manualCreateMoveDoc(WmsASN wmsAsn) {
		WmsMoveDoc moveDoc = wmsASNManager.manualCreateMoveDoc(wmsAsn);
		//自动分配
		try {
			workflowManager.sendMessage(moveDoc, "wmsMoveDocProcess.autoAllocate");
			moveDoc = load(WmsMoveDoc.class,moveDoc.getId());
			if(WmsMoveDocStatus.ALLOCATED.equals(moveDoc.getStatus())){
				workflowManager.doWorkflow(moveDoc, "wmsMoveDocProcess.active");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
