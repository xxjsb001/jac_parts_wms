package com.vtradex.wms.server.telnet.shell.replenishment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vtradex.kangaroo.shell.PageableBaseShell;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.model.move.WmsMoveDocType;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;
import com.vtradex.wms.server.web.filter.WmsWorkerHolder;

/**
 * 补货上架
 * @author Administrator
 *
 */
public class WmsReplenishmentPutawayShell extends PageableBaseShell{

	public static final String MOVEDOC_ID = "MOVEDOC_ID";
	public static final String PAGE_ID = "wmsReplenishmentPutawayShell";
	
	@Override
	public String[] getTableHeader() {
		return new String[]{"序号", "拣货单号","作业人员"};
	}

	@Override
	public String getHql() {
		String hql = " SELECT doc.id,doc.code,doc.worker.name FROM WmsMoveDoc doc WHERE 1=1 " +
		" /~状态: AND doc.status in  ({状态})~/" +
		" /~类型: AND doc.type in ({类型})~/" +
		" /~作业人员: AND doc.worker.id = {作业人员}~/" +
		" /~仓库: AND doc.warehouse.id = {仓库}~/";
		return hql;
	}

	@Override
	public String getNextShell() {
		Object[] rowData = (Object[])get(ROW_DATA_KEY);
		if(rowData != null){
			this.put(MOVEDOC_ID, rowData[0]);
		}
		return WmsReplenishmentPutawayDoShell.PAGE_ID;
	}

	@Override
	public Map<String, Object> getParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		List<String> status = new ArrayList<String>();
		List<String> types = new ArrayList<String>();
		status.add(WmsMoveDocStatus.ACTIVE);
		status.add(WmsMoveDocStatus.WORKING);
		types.add(WmsMoveDocType.MV_REPLENISHMENT_MOVE);
		params.put("状态", status);
		params.put("类型", types);
		params.put("作业人员", WmsWorkerHolder.getWmsWorker().getId());
		params.put("仓库", WmsWarehouseHolder.getWmsWarehouse().getId());
		return params;
	}

}
