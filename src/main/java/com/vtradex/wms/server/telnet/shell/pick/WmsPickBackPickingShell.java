package com.vtradex.wms.server.telnet.shell.pick;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.pick.WmsPickRFManager;
import com.vtradex.wms.server.telnet.shell.ShellExceptions;
/**容器退拣->退拣数量*/
public class WmsPickBackPickingShell extends Thorn4BaseShell{
	public static final String PAGE_ID = "wmsPickBackPickingShell";
	private final WmsPickRFManager pickRFManager;
	public WmsPickBackPickingShell(WmsPickRFManager pickRFManager) {
		this.pickRFManager = pickRFManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String messge = "";
		Long mdsId = (Long) this.getParentValue(WmsPickBackMoveDocShell.CURRENT_MDID);//WmsMoveDocAndStation.id
		Object[] value = (Object[]) this.getParentValue(WmsPickBackMoveDocShell.PICK_OBJ);//itemcode,itemname,supcode,locCode,qty
		String container = (String) this.getParentValue(WmsPickBackMoveDocShell.CONTAINER);
		String[] keys = new String[]{
				"itemcode","itemname","supcode","locCode","pickQty"
		};
		for(int i = 0 ; i< value.length ; i++){
			output(keys[i],value[i].toString());
		}
		Double pickBackQty = this.getNumberField("pickBackQty");
		if(pickBackQty==null || pickBackQty<0){
			this.setStatusMessage(ShellExceptions.PICK_BACK_QTY_IS_ERROE);
		}
		if(pickBackQty >  Double.valueOf(value[4].toString())){
			this.setStatusMessage(ShellExceptions.PICK_BACK_QTY_IS_OVER);
		}
		if(pickBackQty==0){
			messge = "请重新扫描器具退拣";
			this.forward(WmsPickBackMoveDocShell.PAGE_ID,messge);
		}
		Map result = pickRFManager.containerPickBack(mdsId, (String)value[2], pickBackQty, (String)value[3]);
		messge = (String) result.get(WmsPickBackMoveDocShell.ERROR_MESSAGE);
		if(StringUtils.isNotEmpty(messge)){
			this.put(WmsPickBackMoveDocShell.CURRENT_MDID, mdsId);//WmsMoveDocAndStation.id
			this.put(WmsPickBackMoveDocShell.PICK_OBJ, new Object[]{////itemcode,itemname,supcode,qty
					value[0],value[1],value[2],value[4]
			});
			this.put(WmsPickBackMoveDocShell.CONTAINER, container);
			this.forward(WmsPickBackLocationShell.PAGE_ID,messge);
		}
		
		this.put(WmsPickBackMoveDocShell.CONTAINER, container);
		this.forward(WmsPickBackMoveDocShell.PAGE_ID);
	}
	

}
