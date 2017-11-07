package com.vtradex.wms.server.telnet.shell.receiving;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.component.support.ObjectOptionDisplayer;
import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.receiving.WmsReceivingRFManager;
import com.vtradex.wms.server.utils.MyUtils;

public class WmsASNCodeConfirmShell extends Thorn4BaseShell{
	public static final String PAGE_ID = "wmsASNCodeConfirmShell";
	private WmsReceivingRFManager wmsReceivingRFManager;
	public WmsASNCodeConfirmShell(WmsReceivingRFManager wmsReceivingRFManager) {
		this.wmsReceivingRFManager = wmsReceivingRFManager;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String asnCode = this.getTextField("lotRule.soi");
		if (StringUtils.isEmpty (asnCode) || "-".equals(asnCode)) {
			this.setStatusMessage("收货单号不规范");
		}
		String messge = "操作成功,请继续(..返回主菜单;...退出登录)"+MyUtils.enter;
		Long asnId = wmsReceivingRFManager.findConfirmAsnId(asnCode);
		if(asnId==null || asnId == 0L){
			messge = "失败!找不到符合条件的ASN号:"+MyUtils.enter;
			messge += asnCode;
		}else{
			List<String> goList =new ArrayList<String>();
			goList.add("是");
			goList.add("否");
			String beGo = (String)getListField("是否过账", goList, new ObjectOptionDisplayer());
			if(beGo.equals("是")){
				String num1 = wmsReceivingRFManager.asnConfirmAll(asnId);
				if(!num1.equals("1")){
					messge = num1+MyUtils.enter;
					messge += asnCode;
				}
			}
		}
		this.forward(WmsASNCodeConfirmShell.PAGE_ID,messge);
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
