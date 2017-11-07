package com.vtradex.wms.server.telnet.shell.move;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.move.WmsMoveRFManager;
/**移位单整单确认*/
public class WmsMoveByCodeShell extends Thorn4BaseShell {
	public static final String PAGE_ID = "wmsMoveByCodeShell";
	private final WmsMoveRFManager rfWmsMoveManager;

	public WmsMoveByCodeShell(WmsMoveRFManager rfWmsMoveManager) {
		this.rfWmsMoveManager = rfWmsMoveManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String messge = "整单移位成功,请继续(..返回主菜单;...退出登录)";
		String moveCode = this.getTextField("移位单号");
		if (StringUtils.isEmpty (moveCode)) {
			this.setStatusMessage("移位单号必填");
		}
		String message = rfWmsMoveManager.pickConfirmAll(moveCode);
		if(message.equals("NULL")){
			messge = "找不到符合条件的移位单号";
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
