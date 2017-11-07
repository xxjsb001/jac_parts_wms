package com.vtradex.wms.server.telnet.shell.pick;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.pick.WmsPickRFManager;
import com.vtradex.wms.server.utils.MyUtils;
/**容器退拣->退拣明细*/
public class WmsPickBackItemsShell extends Thorn4BaseShell{
	
	public static final String PAGE_ID = "wmsPickBackItemsShell";
	
	private final WmsPickRFManager pickRFManager;
	public WmsPickBackItemsShell(WmsPickRFManager pickRFManager) {
		this.pickRFManager = pickRFManager;
	}
	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String messge = "";
		String container = (String) this.getParentValue(WmsPickBackMoveDocShell.CONTAINER);
		List<Long> ids = (List<Long>) this.getParentValue(WmsPickBackMoveDocShell.CURRENT_MDIDS);
		Map result = pickRFManager.getContainerList(ids, container);
		List<Object[]> list = (List<Object[]>) result.get(WmsPickBackMoveDocShell.CURRENT_LIST);
		String key = null;
		Map<String,String> moveCodes = new HashMap<String,String>();
		output("NO"+" |"+"物料编码"+" |"+"拣货量");
		for(int i = 0 ; i<list.size() ; i++){
			Object[] obj = list.get(i);
			key = (i+1)+"";
			moveCodes.put(key, obj[0].toString());
			output(key,obj[1]+" | "+obj[2]);
		}
		String mdsId = this.getTextField("任务序号(00结束)");
		if(MyUtils.OVER.equals(mdsId)){
			messge = "请重新扫描器具退拣";
			this.forward(WmsPickBackMoveDocShell.PAGE_ID,messge);
		}
		mdsId = moveCodes.get(mdsId);
		if(mdsId==null){
			messge = "任务序号有误,请重新录入";
			this.put(WmsPickBackMoveDocShell.CURRENT_MDIDS,ids);
			this.put(WmsPickBackMoveDocShell.CONTAINER, container);
			this.forward(this.PAGE_ID,messge);
		}
		
		result = pickRFManager.checkContainerList(Long.parseLong(mdsId));
		messge = (String) result.get(WmsPickBackMoveDocShell.ERROR_MESSAGE);
		if(StringUtils.isNotEmpty(messge)){
			this.put(WmsPickBackMoveDocShell.CURRENT_MDIDS, ids);
			this.put(WmsPickBackMoveDocShell.CONTAINER, container);
			this.forward(WmsPickBackItemsShell.PAGE_ID,messge);
		}
		list = (List<Object[]>) result.get(WmsPickBackMoveDocShell.CURRENT_LIST);
		if(list.size()>1){
			messge = "请选择退拣供应商料";
			this.put(WmsPickBackMoveDocShell.CURRENT_LIST,list);
			this.put(WmsPickBackMoveDocShell.CURRENT_MDID, Long.parseLong(mdsId));//WmsMoveDocAndStation.id
			this.put(WmsPickBackMoveDocShell.CONTAINER, container);
			this.forward(WmsPickBackTasksShell.PAGE_ID,messge);
		}else{
			Object[] value = list.get(0);//itemcode,itemname,supcode,qty
			messge = "扫描库位";
			this.put(WmsPickBackMoveDocShell.CURRENT_MDID, Long.parseLong(mdsId));//WmsMoveDocAndStation.id
			this.put(WmsPickBackMoveDocShell.PICK_OBJ, value);
			this.put(WmsPickBackMoveDocShell.CONTAINER, container);
			this.forward(WmsPickBackLocationShell.PAGE_ID,messge);
		}
	}
}
