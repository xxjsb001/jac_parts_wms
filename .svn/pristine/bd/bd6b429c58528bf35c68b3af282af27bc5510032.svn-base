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
/**容器退拣*/
public class WmsPickBackMoveDocShell extends Thorn4BaseShell{
	
	private final WmsPickRFManager pickRFManager;
	
	public WmsPickBackMoveDocShell(WmsPickRFManager pickRFManager) {
		this.pickRFManager = pickRFManager;
	}

	public static final String PAGE_ID = "wmsPickBackMoveDocShell"; 
	
	public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
	public static final String CURRENT_MDIDS = "CURRENT_MDIDS";
	public static final String CURRENT_MDID = "CURRENT_MDID";
	public static final String CONTAINER = "CONTAINER";
	public static final String CURRENT_LIST = "CURRENT_LIST";
	public static final String PICK_OBJ = "PICK_OBJ";
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String container = (String) this.getParentValue(WmsPickBackMoveDocShell.CONTAINER);
		String messge = "";
		if(StringUtils.isEmpty(container)){
			container = this.getTextField("scan.container");
		}
		if(StringUtils.isEmpty(container)){
			this.setStatusMessage(ShellExceptions.CONTAINER_CODE_IS_NULL);
		}
		container = container.trim();
		Map result = pickRFManager.checkContainerPicking(container);
		messge = (String) result.get(ERROR_MESSAGE);
		if(StringUtils.isNotEmpty(messge)){
			this.forward(WmsPickBackMoveDocShell.PAGE_ID,messge);
		}
		messge = "选择退拣物料NO";
		this.put(CURRENT_MDIDS, result.get(CURRENT_MDIDS));
		this.put(CONTAINER, container);
		this.forward(WmsPickBackItemsShell.PAGE_ID,messge);
	}

}
