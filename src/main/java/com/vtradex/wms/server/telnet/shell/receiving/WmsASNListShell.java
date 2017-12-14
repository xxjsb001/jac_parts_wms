package com.vtradex.wms.server.telnet.shell.receiving;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vtradex.wms.server.model.receiving.WmsASNStatus;
import com.vtradex.wms.server.telnet.shell.JacPageableBaseShell;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

/**
 * 
 * @category Shell
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:55 $
 */
public class WmsASNListShell extends JacPageableBaseShell {
	
	public static final String PAGE_ID = "wmsASNListShell";

	@Override
	public String[] getTableHeader() {
		return new String[]{"序号", "物料编码/物料名称/期待数量/收货数量"};
	}

	@Override
	public String getHql() {
		String hql = "select a.id, a.item.code || '/' ||a.item.name|| '/' || a.expectedQuantityBU || '/' || a.receivedQuantityBU "+ 
				" from WmsASNDetail a where 1=1 and a.beReceived = false" +//(a.expectedQuantityBU- a.receivedQuantityBU)>0
				" /~状态: AND a.asn.status not in  ({状态})~/" +
				" /~仓库: AND a.asn.warehouse.id = {仓库}~/" +
				" /~收货ID: AND a.asn.id = {收货ID}~/";//
		return hql;
	}

	@Override
	public String getNextShell() {
		Object[] rowData = (Object[])get(ROW_DATA_KEY);
		this.put("detail.id", rowData[0]);
		this.put("lotRule.soi", this.getParentContext().get("lotRule.soi").toString());
		this.put("asnId", this.getParentContext().get("asnId").toString());
		return WmsAsnDetailReceiveShell.PAGE_ID;
	}

	@Override
	public Map<String, Object> getParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		List<String> status = new ArrayList<String>();
//		String asnCode = (String) this.getParentContext().get("lotRule.soi");
		Long asnId = (Long) this.getParentContext().get("asnId");
		status.add(WmsASNStatus.RECEIVED);
		status.add(WmsASNStatus.CANCELED);
		status.add(WmsASNStatus.OPEN);
		params.put("状态", status);
		params.put("仓库", WmsWarehouseHolder.getWmsWarehouse().getId());
//		params.put("收货单号", asnCode);
		params.put("收货ID", asnId);
		return params;
	}

	@Override
	public String getUpShell() {
		// TODO Auto-generated method stub
		return WmsASNListShell.PAGE_ID;
	}

}
