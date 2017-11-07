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
/**整单收货*/
public class WmsASNCodeShell extends Thorn4BaseShell{
	public static final String PAGE_ID = "wmsASNCodeShell";
	private WmsReceivingRFManager wmsReceivingRFManager;
	
	public WmsASNCodeShell(WmsReceivingRFManager wmsReceivingRFManager) {
		this.wmsReceivingRFManager = wmsReceivingRFManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String asnCode = this.getTextField("lotRule.soi");
		if (StringUtils.isEmpty (asnCode) || "-".equals(asnCode)) {
			this.setStatusMessage("收货单号不规范");
		}
		String messge = "操作成功,请继续(..返回主菜单;...退出登录)"+MyUtils.enter;
		Long asnId = wmsReceivingRFManager.findAsnId(asnCode);
		if(asnId==null || asnId == 0L){
			messge = "失败!找不到符合条件的ASN号:"+MyUtils.enter;
			messge += asnCode;
		}else{
			String num1 = wmsReceivingRFManager.asnReceiveAll(asnId);
			if(!num1.equals("1")){
				messge = num1+MyUtils.enter;
				messge += asnCode;
			}else{
				String num2  = wmsReceivingRFManager.palletAuto(asnId);
				if(!num2.equals("1")){
					List<String> goList =new ArrayList<String>();
					goList.add("明白");
					goList.add("不明白");
					String beGo = (String)getListField(num2, goList, new ObjectOptionDisplayer());
					if(beGo.equals("不明白")){
						this.getTextField("RF扫描会自动码托,"+MyUtils.enter+"但系统检测到不满足自动码托");
					}
				}
				String num3  = wmsReceivingRFManager.driectPrint(asnId);
				if(!num3.equals("1")){
					List<String> goList =new ArrayList<String>();
					goList.add("明白");
					goList.add("不明白");
					String beGo = (String)getListField(num3, goList, new ObjectOptionDisplayer());
					if(beGo.equals("不明白")){
						this.getTextField("RF扫描会自动打印,但未打印");
					}
				}
			}
		}
		this.forward(WmsASNCodeShell.PAGE_ID,messge);
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
