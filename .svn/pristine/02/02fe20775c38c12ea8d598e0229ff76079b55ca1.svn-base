package com.vtradex.wms.server.telnet.shell.replenishment;

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
import com.vtradex.wms.server.telnet.move.WmsMoveRFManager;
/***拼托*/
public class WmsMovePalletShell extends Thorn4BaseShell{
	public static final String PAGE_ID = "wmsMovePalletShell";
	
	private final WmsMoveRFManager rfWmsMoveManager;

	public WmsMovePalletShell(WmsMoveRFManager rfWmsMoveManager) {
		this.rfWmsMoveManager = rfWmsMoveManager;
	}
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String messge = "拼托成功,请继续(..返回主菜单;...退出登录)";
		String srcPallet = this.getParentContext().get("srcPallet")==null?null:this.getParentContext().get("srcPallet").toString();
		if(srcPallet==null){
			srcPallet = this.getTextField("源托盘号");
		}else{
			output("源托盘号",srcPallet);
		}
		if (StringUtils.isEmpty (srcPallet)) {
			this.setStatusMessage("源托盘号必填");
		}
		String descPallet = this.getTextField("目标托盘号");
		if (StringUtils.isEmpty (descPallet)) {
			this.setStatusMessage("目标托盘号必填");
		}
		List<String> goList =new ArrayList<String>();
		goList.add("是");
		goList.add("否");
		String beGo = (String)getListField("是否确认", goList, new ObjectOptionDisplayer());
		if(beGo.equals("是")){
			messge = rfWmsMoveManager.checkPallet(srcPallet, descPallet);
			this.reset(messge);
		}else{
			this.context.put("srcPallet", srcPallet);
			this.forward(WmsMovePalletShell.PAGE_ID,"请重新扫描目标托盘");
		}
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
