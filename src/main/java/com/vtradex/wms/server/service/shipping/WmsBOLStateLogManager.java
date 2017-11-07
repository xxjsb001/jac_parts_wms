package com.vtradex.wms.server.service.shipping;

import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.shipping.WmsBOLStateLog;

@Transactional
public interface WmsBOLStateLogManager {
	
	@Transactional
	void storeBOLStateLog(WmsMoveDoc moveDoc , String type, Date inputTime);
}
