package com.vtradex.wms.server.telnet.shell.putaway;

import java.util.HashMap;
import java.util.Map;
import com.vtradex.wms.server.telnet.shell.JacPageableBaseShell;

public class ViewPutAsnDetailShell extends JacPageableBaseShell{
	public static final String PAGE_ID = "viewPutAsnDetailShell";
	public static final String DETAIL_ID = "DETAIL_ID";
	@Override
	public String[] getTableHeader() {
		return new String[]{"序号", "物料编码", "物料名称","数量","库区"};
	}

	@Override
	public String getHql() {
		String hql = " SELECT detail.id" +
				",detail.item.code,detail.item.name,detail.expectedQuantityBU,detail.item.class4" +
				" FROM WmsASNDetail detail" +
				" LEFT JOIN detail.asn asn"+
				" WHERE detail.beReceived = false" +
				" /~ASNID: AND asn.id = {ASNID}~/";		
		return hql;
	}

	@Override
	public String getNextShell() {
		Object[] rowData = (Object[])get(ROW_DATA_KEY);
		Long asnId = (Long) this.getParentValue(WmsPutAsnScanShell.ASN_ID);
		this.put(DETAIL_ID, rowData[0]);
		this.put(WmsPutAsnScanShell.ASN_ID, asnId);
		return WmsPutAsnDetailShell.PAGE_ID;
	}

	@Override
	public Map<String, Object> getParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		Long asnId = (Long) this.getParentValue(WmsPutAsnScanShell.ASN_ID);
		params.put("ASNID", asnId);
		return params;
	}

	@Override
	public String getUpShell() {
		return WmsPutAsnScanShell.PAGE_ID;
	}

}
