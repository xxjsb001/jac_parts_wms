package com.vtradex.wms.server.telnet.shell.receiving;

import java.io.IOException;

import net.wimpi.telnetd.net.Connection;

import org.apache.commons.lang.StringUtils;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.wms.server.telnet.base.WmsBaseModelRFManager;
import com.vtradex.wms.server.telnet.receiving.WmsReceivingRFManager;
import com.vtradex.wms.server.telnet.shell.CustomBaseShell;
import com.vtradex.wms.server.utils.MyUtils;

/**
 * 
 *
 * @category Shell
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:55 $
 */
public class WmsASNDetailShell extends CustomBaseShell {
	
	public static final String PAGE_ID = "wmsASNDetailShell";
	private WmsReceivingRFManager wmsReceivingRFManager;
	
	public WmsASNDetailShell(WmsBaseModelRFManager wmsBaseModeRFManager, WmsReceivingRFManager wmsReceivingRFManager) {
		super(wmsBaseModeRFManager);
		this.wmsReceivingRFManager = wmsReceivingRFManager;
	}

	@Override
	protected void mainProcess(Connection connection) throws BreakException, ContinueException, IOException, Exception {
		
		String asnCode = this.getTextField("lotRule.soi");
		if (StringUtils.isEmpty (asnCode)) {
			this.setStatusMessage("ASN号必填");
		}
		String messge = "操作成功,请继续(00上一级;01退出)"+MyUtils.enter;
		Long asnId = wmsReceivingRFManager.findAsnId(asnCode);
		if(asnId==null || asnId == 0L){
			messge = "失败!找不到符合条件的ASN号";
			messge += asnCode;
			this.setStatusMessage(messge);
		}
		this.put("asnId", asnId);
		this.forward(WmsASNListShell.PAGE_ID, "请选择ASN明细");
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
	    }else if(value.equalsIgnoreCase("00")){//跳转至上一屏  
	        forwardByKeyboard("XX");  
	    }else if(value.equalsIgnoreCase("01")){//退出登录  
	        forwardByKeyboard("QQ");  
	    } 
	}
}
