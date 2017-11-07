package com.vtradex.wms.server.telnet.shell.pick;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.pick.WmsPickRFManager;
import com.vtradex.wms.server.utils.MyUtils;

/**拣货单确认*/
public class WmsPickConfirmAllShell extends Thorn4BaseShell {
	
	public static final String PAGE_ID = "wmsPickConfirmAllShell";
	private final WmsPickRFManager pickRFManager;
	
	public WmsPickConfirmAllShell(WmsPickRFManager pickRFManager) {
		this.pickRFManager = pickRFManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String messge = "整单拣货成功,请继续(..返回主菜单;...退出登录)";
		
		String pickNo = this.getTextField("pickNo");
		if (StringUtils.isEmpty (pickNo)) {
			this.setStatusMessage("发货单号必填");
		}
		String mesg = pickRFManager.pickConfirmAll(pickNo);
		if(!MyUtils.SUCCESS.equals(mesg)){
			if("pic is null".equals(mesg)){
				messge = "失败!发货单找不到";
			}else if("move is null".equals(mesg)){
				messge = "失败!拣货单找不到";
			}else{
				messge = mesg;
			}
		}
		
		this.reset(messge);
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
