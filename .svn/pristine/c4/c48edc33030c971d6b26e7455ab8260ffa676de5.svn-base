package com.vtradex.wms.server.telnet.shell.pick;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.pick.WmsPickRFManager;
/**发运登记*/
public class WmsShipRecordShell extends Thorn4BaseShell {
	public static final String PAGE_ID = "wmsShipRecordShell";
	private final WmsPickRFManager pickRFManager;
	
	public WmsShipRecordShell(WmsPickRFManager pickRFManager) {
		this.pickRFManager = pickRFManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String messge = "发运登记成功,请继续(..返回主菜单;...退出登录)";
		
		String pickNo = this.getTextField("pickNo");
		if (StringUtils.isEmpty (pickNo)) {
			this.setStatusMessage("发货单号必填");
		}
		String vehicleNo = this.getTextField("vehicleNo");
		if (StringUtils.isEmpty (vehicleNo)) {
			this.setStatusMessage("车牌号必填");
		}
		String mesg = pickRFManager.shipRecord(pickNo, vehicleNo);
		if("pic is null".equals(mesg)){
			messge = "失败!发货单找不到";
		}else if("move is null".equals(mesg)){
			messge = "失败!拣货单找不到";
		}else if("move is ship".equals(mesg)){
			messge = "失败!发货单已发运";
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
