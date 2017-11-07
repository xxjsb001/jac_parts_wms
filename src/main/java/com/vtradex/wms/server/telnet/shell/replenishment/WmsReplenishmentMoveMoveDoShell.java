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
/**补货移位*/
public class WmsReplenishmentMoveMoveDoShell extends Thorn4BaseShell {
	public static final String PAGE_ID = "wmsReplenishmentMoveMoveDoShell";
	private final WmsMoveRFManager rfWmsMoveManager;

	public WmsReplenishmentMoveMoveDoShell(WmsMoveRFManager rfWmsMoveManager) {
		this.rfWmsMoveManager = rfWmsMoveManager;
	}
	@SuppressWarnings("unchecked")
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String messge = "待补货移位计划管理";
		String moveCode = this.getParentContext().get("moveCode").toString();
		Long moveId = Long.parseLong(this.getParentValue("moveId").toString());
		List<Object[]> tasks = (List<Object[]>) this.getParentContext().get("tasks");
		String beGo = "-",beJump = "-";
		for(int i =0 ; i<tasks.size() ; i++){
			Object[] obj = tasks.get(i);//[taskId,fromLocationCode,itemCode,unMoveQty,toLocationCode]
			output("拣货库位",obj[1].toString());
			output("物料",obj[2].toString());
			output("数量",obj[3].toString());
			output("备料库位",obj[4].toString());
			List<String> goList =new ArrayList<String>();
			goList.add("是");
			goList.add("否");
			beGo = (String)getListField("是否确认", goList, new ObjectOptionDisplayer());
			if(beGo.equals("是")){
				rfWmsMoveManager.singleConfirm(new Object[]{
						moveId,moveCode,obj[0],obj[3]	
				});
				tasks.remove(i);
				this.context.put("tasks", tasks);
				this.context.put("moveCode", moveCode);
				this.context.put("moveId", moveId);
				beGo = "break";
				break;
			}else{
				output("--------","---");
				List<String> goJump =new ArrayList<String>();
				goJump.add("是");
				goJump.add("否");
				beJump = (String)getListField("是否跳过", goList, new ObjectOptionDisplayer());
				if(beJump.equals("是")){
					tasks.remove(i);
					this.context.put("tasks", tasks);
					this.context.put("moveCode", moveCode);
					this.context.put("moveId", moveId);
					beGo = "break";
					break;
				}
				this.forward(WmsReplenishmentMoveMoveShell.PAGE_ID,messge);
			}
		}
		if(beGo.equals("break")){
			messge = "待补货移位明细";
			this.forward(WmsReplenishmentMoveMoveDoShell.PAGE_ID,messge);
		}else{
			messge = "未完成补货移位计划";
			this.forward(WmsReplenishmentMoveMoveShell.PAGE_ID,messge);
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
