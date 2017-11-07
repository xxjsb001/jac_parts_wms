package com.vtradex.wms.server.telnet.shell.quickMove;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wimpi.telnetd.net.Connection;

import org.apache.commons.lang.StringUtils;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.move.WmsMoveRFManager;

public class WmsQickMoveShell extends Thorn4BaseShell{
public static final String PAGE_ID = "wmsQickMoveShell";
	
	private final WmsMoveRFManager rfWmsMoveManager;

	public WmsQickMoveShell(WmsMoveRFManager rfWmsMoveManager) {
		this.rfWmsMoveManager = rfWmsMoveManager;
	}
	protected void mainProcess(Connection connection) throws BreakException,
		ContinueException, IOException, Exception {
		String pallet = this.getTextField("palletSign");
		if (StringUtils.isEmpty(pallet)) {
			this.setStatusMessage("托盘标签必填");
		}
		//根据托盘标签选择物料
		//wie.id, item.id,item.code,locationCode,availableQuantityBU,extendPropC1,supplier.id,packageUnit.id,location.id,ixAvailableQuantityBU
		List<Object[]> ixs = rfWmsMoveManager.getInventoyExtendByPalletJac(pallet);
		Object[] ix = null;
		if(ixs.size()>0){
			if(ixs.size()==1){
				ix = ixs.get(0);
			}else{
				Map<String,Object[]> tempIx = new HashMap<String,Object[]>();
				String key = null;
				for(int i = 0 ; i<ixs.size() ; i++){
					ix = ixs.get(i);
					key = (i+1)+"";
					output(key,ix[2]+"/"+ix[4]+"/"+ix[5]);
					tempIx.put(key, ix);
				}
				String ixNo = this.getTextField("任务序号");
				if(StringUtils.isEmpty(ixNo)){
					this.setStatusMessage("任务序号必填");
				}
				if(!tempIx.containsKey(ixNo)){
					this.setStatusMessage("任务序号不存在");
				}
				ix = tempIx.get(ixNo);
			}
			//跳转页面
			this.context.put("ix", ix);
			
			this.forward(WmsQickMoveDoShell.PAGE_ID,"移位开始");
		}else{
			this.setStatusMessage("托盘标签不存在,请重填");
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
