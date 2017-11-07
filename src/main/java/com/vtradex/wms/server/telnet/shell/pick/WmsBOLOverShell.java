package com.vtradex.wms.server.telnet.shell.pick;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.dto.WmsBOLDTO;
import com.vtradex.wms.server.telnet.pick.WmsPickRFManager;
import com.vtradex.wms.server.telnet.shell.ShellExceptions;
import com.vtradex.wms.server.utils.JavaTools;

public class WmsBOLOverShell extends Thorn4BaseShell{
	public static final String CURRENT_LICENSE = "CURRENT_LICENSE";
	public static final String CURRENT_DTOS = "CURRENT_DTOS";
	public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
	public static final String PAGE_ID = "wmsBOLOverShell";
	private final WmsPickRFManager pickRFManager;
	
	public WmsBOLOverShell(WmsPickRFManager pickRFManager) {
		this.pickRFManager = pickRFManager;
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {

		Map<String,WmsBOLDTO> dtos = (Map<String, WmsBOLDTO>) this.getParentContext().get(CURRENT_DTOS);
		if(dtos==null){
			dtos = new HashMap<String, WmsBOLDTO>();
		}
		String license = (String) this.getParentContext().get(CURRENT_LICENSE);
		this.output("请耐心等待...");
		try{
			pickRFManager.createWmsBol(dtos,license);
		}catch(Exception e){
			this.put(CURRENT_LICENSE, license);
			this.forward(WmsBOLShell.PAGE_ID,JavaTools.spiltLast(e.getMessage(), ":"));
		}
		this.output("成功,2秒后自动跳转...");
		try {
			Thread.sleep(1000*2);//2s
		} catch (InterruptedException e) {
			logger.error("", e);
		}
		this.forward(WmsBOLShell.PAGE_ID,ShellExceptions.NEXT_VEHICLE);
	}
}
