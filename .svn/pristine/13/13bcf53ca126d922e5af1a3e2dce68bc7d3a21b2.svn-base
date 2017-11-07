package com.vtradex.wms.server.telnet.shell.putaway;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.putaway.WmsPutawayRFManager;
import com.vtradex.wms.server.utils.MyUtils;

/**上架分配*/
public class WmsASNputawayAutoAllocateShell extends Thorn4BaseShell{
	public static final String PAGE_ID = "wmsASNputawayAutoAllocateShell";
	protected WmsPutawayRFManager wmsPutawayRFManager;
	
	public WmsASNputawayAutoAllocateShell(WmsPutawayRFManager wmsPutawayRFManager) {
		this.wmsPutawayRFManager = wmsPutawayRFManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String asnCode = this.getTextField("asnCode");
		if (StringUtils.isEmpty (asnCode)) {
			this.setStatusMessage("ASN号必填");
		}
		Integer num = wmsPutawayRFManager.putawayAutoAllocate(asnCode);
		String messge = "操作成功,请继续(..返回主菜单;...退出登录)";
		if(num==0){
			messge = "失败!找不到符合分配的ASN号:"+MyUtils.enter;
			messge += asnCode;
		}
		this.forward(WmsASNputawayAutoAllocateShell.PAGE_ID,messge);
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
