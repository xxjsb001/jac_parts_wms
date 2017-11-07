package com.vtradex.wms.server.telnet.shell.replenishment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.component.support.ObjectOptionDisplayer;
import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.move.WmsMoveRFManager;
/**补货下单业务处理*/
public class WmsReplenishmentMoveOrderDoShell extends Thorn4BaseShell {
	public static final String PAGE_ID = "wmsReplenishmentMoveOrderDoShell";
	private final WmsMoveRFManager rfWmsMoveManager;

	public WmsReplenishmentMoveOrderDoShell(WmsMoveRFManager rfWmsMoveManager) {
		this.rfWmsMoveManager = rfWmsMoveManager;
	}
	@SuppressWarnings("unchecked")
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		List<Map<String, Object>> details = (List<Map<String, Object>>) 
				this.getParentContext().get("details");
		List<Map<String, Object>> dds = (List<Map<String, Object>>) this.getParentContext().get("dds");
		
		String blCode = this.getTextField("blCode");
		if (StringUtils.isEmpty(blCode)) {
			this.setStatusMessage("备料库位必填");
		}
		
		Integer isWorng = 0;
		Object[] obj = rfWmsMoveManager.findLocBHKW(details, blCode, dds);
		isWorng = (Integer) obj[0];
		//{工艺状态=-, 货主=发动机, 货品代码=1023060GAZC, 补货上限=180.0, 供应商=L21016, 库位=A-03010101, 备料工工号=test}
		dds = (List<Map<String, Object>>) obj[1];
		if(isWorng==0){
			this.setStatusMessage("R101_备货库位定位表:找不到数据");
		}else if(isWorng==2){
			this.setStatusMessage(blCode+":已扫过");
		}
		
		this.context.put("details", details);
		this.context.put("dds", dds);
		
		List<String> goList =new ArrayList<String>();
		goList.add("是");
		goList.add("否");
		String beGo = (String)getListField("是否继续备料", goList, new ObjectOptionDisplayer());
		if(beGo.equals("是")){
			this.forward(WmsReplenishmentMoveOrderDoShell.PAGE_ID,"扫扫扫");
		}else{
			String error = rfWmsMoveManager.initWmsReplenishMove(dds);
			this.forward(WmsReplenishmentMoveOrderShell.PAGE_ID,error);
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
