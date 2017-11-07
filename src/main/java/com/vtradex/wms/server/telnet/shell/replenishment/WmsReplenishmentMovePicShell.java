package com.vtradex.wms.server.telnet.shell.replenishment;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.move.WmsMoveRFManager;
/**补货下架-入口*/
public class WmsReplenishmentMovePicShell extends Thorn4BaseShell {
	public static final String PAGE_ID = "wmsReplenishmentMovePicShell";
	private final WmsMoveRFManager rfWmsMoveManager;

	public WmsReplenishmentMovePicShell(WmsMoveRFManager rfWmsMoveManager) {
		this.rfWmsMoveManager = rfWmsMoveManager;
	}
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		List<Object[]> moveDocs = rfWmsMoveManager.findUnFinshReplenishMove();
		String messge = "补货任务明细";
		if(moveDocs.size()>0){
			Map<String,String> moveCodes = new HashMap<String,String>();
			Map<String,Long> moveIds = new HashMap<String, Long>();
			String key = null;
			for(int i = 0 ; i<moveDocs.size() ; i++){
				Object[] obj = moveDocs.get(i);//[code,status,id,创建人]
				key = (i+1)+"";
				output(key,obj[0]+"/"+obj[3]);
				moveCodes.put(key, obj[0].toString());
				moveIds.put(obj[0].toString(), Long.parseLong(obj[2].toString()));
			}
			String moveCode = this.getTextField("任务序号");
			moveCode = moveCodes.get(moveCode);
			if(moveCode==null){
				messge = "任务序号有误,请重新录入";
				this.forward(WmsReplenishmentMovePicShell.PAGE_ID,messge);
			}
			List<Object[]> dds = rfWmsMoveManager.findMoveDetails(moveCode);
			this.context.put("dds", dds);
			this.context.put("moveCode", moveCode);
			this.context.put("moveId", moveIds.get(moveCode));
			this.context.put("movedQty", 0D);
			this.forward(WmsReplenishmentMovePicDoShell.PAGE_ID,messge);
		}else{
			this.getTextField("无备料任务(..返回主菜单;...退出登录)");
			this.forward(WmsReplenishmentMovePicShell.PAGE_ID);
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
