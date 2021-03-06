package com.vtradex.wms.server.telnet.shell.pick;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.pick.WmsPickRFManager;
import com.vtradex.wms.server.utils.MyUtils;
/**容器退拣->多供应商列表*/
public class WmsPickBackTasksShell extends Thorn4BaseShell{
	
	public static final String PAGE_ID = "wmsPickBackTasksShell";
	
	private final WmsPickRFManager pickRFManager;
	
	public WmsPickBackTasksShell(WmsPickRFManager pickRFManager) {
		this.pickRFManager = pickRFManager;
	}
	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String messge = "";
		List<Object[]> list = (List<Object[]>) this.getParentValue(WmsPickBackMoveDocShell.CURRENT_LIST);
		Long mdsId = (Long) this.getParentValue(WmsPickBackMoveDocShell.CURRENT_MDID);//WmsMoveDocAndStation.id
		String container = (String) this.getParentValue(WmsPickBackMoveDocShell.CONTAINER);
		String key = null;
		Map<String,Object[]> supCodes = new HashMap<String,Object[]>();
		output("NO"+" |"+"物料编码"+" |"+"供应商编码"+" |"+"拣货量");
		for(int i = 0 ; i<list.size() ; i++){
			Object[] obj = list.get(i);//itemcode,itemname,supcode,qty
			key = (i+1)+"";
			supCodes.put(key, obj);
			output(key,obj[0]+" |"+obj[2]+" | "+obj[3]);
		}
		String supcode = this.getTextField("任务序号(00结束)");
		if(MyUtils.OVER.equals(supcode)){
			messge = "请重新扫描器具退拣";
			this.forward(WmsPickBackMoveDocShell.PAGE_ID,messge);
		}
		Object[] value = supCodes.get(supcode);//itemcode,itemname,supcode,qty
		if(value==null){
			messge = "任务序号有误,请重新录入";
			this.put(WmsPickBackMoveDocShell.CURRENT_LIST,list);
			this.put(WmsPickBackMoveDocShell.CURRENT_MDID, mdsId);//WmsMoveDocAndStation.id
			this.put(WmsPickBackMoveDocShell.CONTAINER, container);
			this.forward(this.PAGE_ID,messge);
		}else{
			messge = "扫描库位";
			this.put(WmsPickBackMoveDocShell.CURRENT_MDID, mdsId);//WmsMoveDocAndStation.id
			this.put(WmsPickBackMoveDocShell.PICK_OBJ, value);
			this.put(WmsPickBackMoveDocShell.CONTAINER, container);
			this.forward(WmsPickBackLocationShell.PAGE_ID,messge);
		}
	
	}

}
