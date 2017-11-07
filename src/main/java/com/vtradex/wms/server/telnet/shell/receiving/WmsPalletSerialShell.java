package com.vtradex.wms.server.telnet.shell.receiving;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.receiving.WmsReceivingRFManager;
import com.vtradex.wms.server.utils.MyUtils;
/**明细码托*/
public class WmsPalletSerialShell extends Thorn4BaseShell {
	public static final String PAGE_ID = "wmsPalletSerialShell";
	private WmsReceivingRFManager wmsReceivingRFManager;
	public WmsPalletSerialShell(WmsReceivingRFManager wmsReceivingRFManager) {
		this.wmsReceivingRFManager = wmsReceivingRFManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String palletNo = this.getTextField("jps.palletNo");
		if (StringUtils.isEmpty (palletNo)) {
			this.setStatusMessage("托盘标签必填");
		}
		Double quantity = this.getNumberField("receivingQuantityBU");
		if (quantity == null || quantity.doubleValue() <= 0) {
			this.setStatusMessage("码托量数量必填");
		}
		String messge = "操作成功,请继续(XX:返回主菜单;QQ:退出登录)";
		Integer num = wmsReceivingRFManager.palletSerial(palletNo, quantity);
		if(num==0){
			messge = "失败!找不到符合条件的托盘标签:"+MyUtils.enter;
			messge += palletNo;
		}
		this.forward(WmsPalletSerialShell.PAGE_ID,messge);
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
