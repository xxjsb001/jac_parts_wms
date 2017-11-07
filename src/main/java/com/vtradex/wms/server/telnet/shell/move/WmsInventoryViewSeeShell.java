package com.vtradex.wms.server.telnet.shell.move;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.PageableBaseShell;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

public class WmsInventoryViewSeeShell  extends PageableBaseShell{
	public static final String PAGE_ID = "wmsInventoryViewSeeShell";
	public String[] getTableHeader() {
//		System.out.println("getTableHeader");
		return new String[]{"序号", "库位号/库存状态/工艺状态/数量"};
	}

	public String getHql() {
//		System.out.println("getHql");
		String hql = "select a.id, a.location.code || '/' || a.status || '/' || a.itemKey.lotInfo.extendPropC1 || '/' || a.quantityBU"+ 
				" from WmsInventory a where 1=1 AND a.location.type <> 'COUNT'" +
				" AND (a.quantity <> 0 )"+//OR a.putawayQuantityBU <> 0 OR a.allocatedQuantityBU <> 0
				" /~物料编码: AND a.itemKey.item.code = {物料编码}~/" +
				" /~仓库: AND a.location.warehouse.id = {仓库}~/";
		return hql;
	}

	public String getNextShell() {
//		System.out.println("getNextShell");
		Object[] rowData = (Object[])get(ROW_DATA_KEY);
		this.put("asn.id", rowData[0]);
		return WmsInventoryViewShell.PAGE_ID;
	}

	public Map<String, Object> getParams() {
//		System.out.println("getParams");
		String itemCode = this.getParentContext().get("itemCode").toString();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("仓库", WmsWarehouseHolder.getWmsWarehouse().getId());
		params.put("物料编码", itemCode);
		return params;
	}
	protected void forwardByKeyboard(String value) throws BreakException {  
	    if (value.equalsIgnoreCase("XX")) {  
	        if(StringUtils.isEmpty(getShellByXX()))  
	            forward(ShellFactory.getMainShell());  
	        else  
	            forward(getShellByXX());  
	    } else if (value.equalsIgnoreCase("QQ")) {  
	        if(StringUtils.isEmpty(getShellByQQ()))  
	            forward(ShellFactory.getEntranceShell());  
	        else  
	            forward(getShellByQQ());  
	    }else if(value.equalsIgnoreCase("..")){//跳转至主页面 
	        forwardByKeyboard("XX");  
	    }else if(value.equalsIgnoreCase("...")){//退出登录  
	        forwardByKeyboard("QQ");  
	    }else if(value.equalsIgnoreCase("0")){//跳转至上一屏  
	    	this.forward(WmsInventoryViewShell.PAGE_ID);
	    } 
	}

}
