package com.vtradex.wms.server.telnet.shell.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vtradex.wms.server.model.move.WmsTaskStatus;
import com.vtradex.wms.server.telnet.shell.JacPageableBaseShell;
import com.vtradex.wms.server.telnet.shell.pick.WmsScanPickShellMessage;
/**器具查询*/
public class ViewScanPickShell extends JacPageableBaseShell{
	public static final String PAGE_ID = "viewScanPickShell";
	@Override
	public String[] getTableHeader() {
		return new String[]{"序号", "图号","生产时间","产线"};
	}

	@Override
	public String getHql() {
		String hql = " SELECT task.id" +
				",task.itemKey.item.code" +
				",to_char(pickTicket.requireArriveDate,'yyMMdd') as requireArriveDate" +
				",task.moveDocDetail.productionLine" +
				" FROM WmsTask task" +
				" LEFT JOIN task.moveDocDetail.moveDoc moveDoc"+
				" LEFT JOIN moveDoc.pickTicket pickTicket" +
				" WHERE 1=1" +
				" /~器具码: AND task.relatedBill = {器具码}~/"+
				" /~状态: AND task.status in  ({状态})~/" ;		
		return hql;
	}

	@Override
	public String getNextShell() {
		return ViewScanPickContainerShell.PAGE_ID;
	}

	@Override
	public Map<String, Object> getParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		String container = (String) this.getParentValue(WmsScanPickShellMessage.CONTAINER);
		params.put("器具码", container);
		List<String> status = new ArrayList<String>();
		status.add(WmsTaskStatus.OPEN);
		status.add(WmsTaskStatus.WORKING);
		params.put("状态", status);
		return params;
	}

	@Override
	public String getUpShell() {
		return ViewScanPickContainerShell.PAGE_ID;
	}

}
