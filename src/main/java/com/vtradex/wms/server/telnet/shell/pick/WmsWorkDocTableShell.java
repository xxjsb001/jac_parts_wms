package com.vtradex.wms.server.telnet.shell.pick;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.PageableBaseShell;
import com.vtradex.wms.server.model.move.WmsMoveDocType;
import com.vtradex.wms.server.model.move.WmsWorkDocStatus;
import com.vtradex.wms.server.telnet.pick.WmsPickRFManager;
import com.vtradex.wms.server.web.filter.WmsWorkerHolder;

public class WmsWorkDocTableShell extends PageableBaseShell {
	
	public static final String WORK_DOC_ID = "workDocId";
	
	private final WmsPickRFManager wmsPickRFManager;
	
	public WmsWorkDocTableShell(WmsPickRFManager wmsPickRFManager) {
		this.wmsPickRFManager = wmsPickRFManager;
	}

	public String[] getTableHeader() {
		return new String[]{"序号","作业单/作业人员"};
	}

	public String getHql() {
		return "SELECT wd.id,wd.code||'/'||(SELECT w.name FROM WmsWorker w WHERE w.id=wd.worker.id) FROM WmsWorkDoc wd WHERE 1=1" +
				" /~statuss: AND wd.status IN ({statuss})~/" +
//				" /~waId: AND wd.workArea.id = {waId}~/" +
				" /~wTypes: AND wd.type IN ({wTypes})~/" +
				" /~wId:  AND (wd.worker IS NULL OR wd.worker.id = {wId})~/";
	}

	public String getNextShell() {
		Object[] rowData = (Object[])get(ROW_DATA_KEY);
		if(rowData != null)
			this.put(WORK_DOC_ID, rowData[0]);
		return "wmsPickTaskShell";
	}

	public Map<String, Object> getParams() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("statuss", Arrays.asList(new String[]{WmsWorkDocStatus.OPEN , WmsWorkDocStatus.WORKING}));
//		params.put("waId", WmsWorkAreaHolder.getWmsWorkArea().getId());
		params.put("wTypes", Arrays.asList(new String[]{WmsMoveDocType.MV_PICKTICKET_PICKING,WmsMoveDocType.MV_WAVE_PICKING}));
		params.put("wId", WmsWorkerHolder.getWmsWorker().getId());
		return params;
	}
	
	protected void chooseChange(String choose) throws BreakException, ContinueException{
		if(!NumberUtils.isNumber(choose)){
			//选择不是数字则认为扫描的是作业单号
			Long workDocId = this.wmsPickRFManager.checkWorkDocCode(choose);
			put(WORK_DOC_ID,workDocId);
			forward(getNextShell());
		}else{
			super.chooseChange(choose);
		}
	}

}
