package com.vtradex.wms.server.telnet.shell.move;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.move.WmsMoveRFManager;

public class WmsInventoryViewShell extends Thorn4BaseShell {
	public static final String PAGE_ID = "wmsInventoryViewShell";
	private final WmsMoveRFManager rfWmsMoveManager;

	public WmsInventoryViewShell(WmsMoveRFManager rfWmsMoveManager) {
		this.rfWmsMoveManager = rfWmsMoveManager;
	}
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String itemCode = this.getTextField("货品条码");
		if (StringUtils.isEmpty (itemCode)) {
			this.setStatusMessage("货品条码必填");
		}
		this.context.put("itemCode", itemCode);
		this.forward(WmsInventoryViewSeeShell.PAGE_ID,"(..主菜单,...退出)");
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
	    }else if(value.equalsIgnoreCase("..")){//跳转至上一屏  
	        forwardByKeyboard("XX");  
	    }else if(value.equalsIgnoreCase("...")){//退出登录  
	        forwardByKeyboard("QQ");  
	    } 
	}
}
