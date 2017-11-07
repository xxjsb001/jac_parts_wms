package com.vtradex.wms.server.telnet.shell.move;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.vtradex.kangaroo.shell.PageableBaseShell;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.model.move.WmsMoveDocType;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

public class WmsMovePlanTableShell extends PageableBaseShell {
	
	public static final String PAGE_ID = "wmsMovePlanTableShell";

	public String[] getTableHeader() {
		return new String[]{"序号", "计划号/制单人"};
	}

	public String getHql() {
		return "SELECT mDoc.id,mDoc.code||'/'||mDoc.updateInfo.creator FROM WmsMoveDoc mDoc WHERE 1=1 " +
//				" /~waId: AND wd.workArea.id={waId})~/" +
				" /~whId: AND mDoc.warehouse.id={whId}~/" +
				" /~statuss: AND mDoc.status IN ({statuss})~/" +
				" /~type: AND mDoc.type={type}~/";
	}

	public String getNextShell() {
		Object[] rowData = (Object[])get(ROW_DATA_KEY);
		this.put("moveDocId", rowData[0]);
		return WmsMovePlanShell.PAGE_ID;
	}

	public Map<String, Object> getParams() {
		Map<String,Object> params = new HashMap<String,Object>();
//		params.put("waId", WmsWorkAreaHolder.getWmsWorkArea().getId());
		params.put("whId", WmsWarehouseHolder.getWmsWarehouse().getId());
		params.put("statuss", Arrays.asList(new String[]{WmsMoveDocStatus.ACTIVE , WmsMoveDocStatus.WORKING}));
		params.put("type", WmsMoveDocType.MV_MOVE);
		return params ;
	}

}
