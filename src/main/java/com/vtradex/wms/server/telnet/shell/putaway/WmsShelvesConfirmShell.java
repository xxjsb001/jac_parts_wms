package com.vtradex.wms.server.telnet.shell.putaway;

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
import com.vtradex.wms.server.telnet.putaway.WmsPutawayRFManager;
import com.vtradex.wms.server.utils.ChineseUtill;
import com.vtradex.wms.server.utils.MyUtils;

/**上架确认*/
public class WmsShelvesConfirmShell extends Thorn4BaseShell {
	public static final String PAGE_ID = "wmsShelvesConfirmShell";

	protected static WmsPutawayRFManager wmsPutawayRFManager;

	@SuppressWarnings("static-access")
	public WmsShelvesConfirmShell(WmsPutawayRFManager wmsPutawayRFManager) {
		this.wmsPutawayRFManager = wmsPutawayRFManager;
	}

	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String messge = "上架成功,请继续(..返回主菜单;...退出登录)";
		
		String palletNo = this.getTextField("jps.palletNo");
		if (StringUtils.isEmpty (palletNo)) {
			this.setStatusMessage("托盘标签必填");
		}
		String palletNos = wmsPutawayRFManager.palletSerial(palletNo);
		if(StringUtils.isEmpty (palletNos)){
			messge = "失败!系统无法获取该托盘号信息:"+palletNo;
		}else{
			output("系统推荐库位",palletNos.split(MyUtils.spilt1)[0]);
			String locationCode = this.getTextField("locationCode");
			if (StringUtils.isEmpty (locationCode)) {//不扫描时默认系统推荐库位
				locationCode = palletNos.split(MyUtils.spilt1)[0];
				messge = shelvesConfirm(palletNos, locationCode, palletNo, messge);
			}else{//当扫描了库位号时,需要人工确认上架库位
				List<String> goList =new ArrayList<String>();
				goList.add("是");
				goList.add("否");
				String beGo = (String)getListField("是否确认", goList, new ObjectOptionDisplayer());
				if(beGo.equals("是")){
					messge = shelvesConfirm(palletNos, locationCode, palletNo, messge);
				}else{
					this.context.put("palletNos", palletNos);
					this.context.put("palletNo", palletNo);
					this.forward(WmsShelvesConfirmTagShell.PAGE_ID,"请重新扫描上架库位");
				}
			}
		}
		this.forward(WmsShelvesConfirmShell.PAGE_ID,messge);
	}
	public static String shelvesConfirm(String palletNos,String locationCode,String palletNo,String messge){
		locationCode = ChineseUtill.toChinese(locationCode);
		String mesg = wmsPutawayRFManager.shelvesConfirm(palletNos, locationCode);
		if("task is null".equals(mesg)){
			messge = "失败!托盘已上架或不存在可用TASK:"+palletNo;
		}else if("loc is null".equals(mesg)){
			messge = "失败!目标库位不存在或已满:"+locationCode;
		}
		return messge;
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
