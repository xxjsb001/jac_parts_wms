package com.vtradex.wms.server.telnet.shell.putaway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.component.support.ObjectOptionDisplayer;
import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.putaway.WmsPutawayRFManager;
import com.vtradex.wms.server.utils.MyUtils;
/**上架确认tag*/
public class WmsShelvesConfirmTagShell extends Thorn4BaseShell {
	public static final String PAGE_ID = "wmsShelvesConfirmTagShell";

	protected WmsPutawayRFManager wmsPutawayRFManager;

	public WmsShelvesConfirmTagShell(WmsPutawayRFManager wmsPutawayRFManager) {
		this.wmsPutawayRFManager = wmsPutawayRFManager;
	}
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String messge = "上架成功,请继续(..返回主菜单;...退出登录)";
		
		String palletNos = this.getParentContext().get("palletNos").toString();
		String palletNo = this.getParentContext().get("palletNo").toString();
		output("系统推荐库位",palletNos.split(MyUtils.spilt1)[0]);
		String locationCode = this.getTextField("locationCode");
		if (StringUtils.isEmpty (locationCode)) {//不扫描时默认系统推荐库位
			locationCode = palletNos.split(MyUtils.spilt1)[0];
			messge = WmsShelvesConfirmShell.shelvesConfirm(palletNos, locationCode, palletNo, messge);
		}else{//当扫描了库位号时,需要人工确认上架库位
			List<String> goList =new ArrayList<String>();
			goList.add("是");
			goList.add("否");
			String beGo = (String)getListField("是否确认", goList, new ObjectOptionDisplayer());
			if(beGo.equals("是")){
				messge = WmsShelvesConfirmShell.shelvesConfirm(palletNos, locationCode, palletNo, messge);
			}else{
				this.context.put("palletNos", palletNos);
				this.forward(WmsShelvesConfirmTagShell.PAGE_ID,"请重新扫描上架库位");
			}
		}
		this.forward(WmsShelvesConfirmShell.PAGE_ID,messge);
	}

}
