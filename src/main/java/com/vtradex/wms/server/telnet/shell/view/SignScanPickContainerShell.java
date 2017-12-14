package com.vtradex.wms.server.telnet.shell.view;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.pick.WmsPickRFManager;
import com.vtradex.wms.server.telnet.shell.ShellExceptions;
import com.vtradex.wms.server.utils.MyUtils;
/**器具签收*/
public class SignScanPickContainerShell extends Thorn4BaseShell{
	public static final String PAGE_ID = "signScanPickContainerShell";
	
	private final WmsPickRFManager pickRFManager;
	public SignScanPickContainerShell(WmsPickRFManager pickRFManager) {
		this.pickRFManager = pickRFManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		
		String messge = "成功";
		String container = this.getTextField("scan.boxTag");
		if (StringUtils.isEmpty (container) || "-".equals(container)) {
			this.setStatusMessage("器具标签不规范");
		}
		container = container.trim();
		if(MyUtils.OVER.equals(container)){
			forward(ShellFactory.getMainShell());
		}
		Map<String,String> result = pickRFManager.signScanPickContainer(container);
		messge = result.get(ShellExceptions.MESSAGE);
		this.forward(SignScanPickContainerShell.PAGE_ID, messge);
	}

}
