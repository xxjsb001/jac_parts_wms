package com.vtradex.wms.server.service.shipping.pojo;

import java.util.Date;

import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.shipping.WmsBOLStateLog;
import com.vtradex.wms.server.service.shipping.WmsBOLStateLogManager;

public class DefaultWmsBOLStateLogManager extends DefaultBaseManager implements
		WmsBOLStateLogManager {

	public void storeBOLStateLog(WmsMoveDoc moveDoc , String type, Date inputTime) {
		moveDoc.setTransStatus(type);
		commonDao.store(moveDoc);
		
		WmsBOLStateLog stateLog = EntityFactory.getEntity(WmsBOLStateLog.class);
		stateLog.setMoveDoc(moveDoc);
		stateLog.setType(type);
		stateLog.setInputTime(inputTime);
		commonDao.store(stateLog);
	}
}
