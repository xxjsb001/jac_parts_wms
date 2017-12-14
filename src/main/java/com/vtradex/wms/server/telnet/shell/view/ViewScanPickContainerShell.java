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
import com.vtradex.wms.server.telnet.shell.pick.WmsScanPickShellMessage;
import com.vtradex.wms.server.utils.MyUtils;
/**器具查询扫码*/
public class ViewScanPickContainerShell extends Thorn4BaseShell{
	public static final String PAGE_ID = "viewScanPickContainerShell";
	private final WmsPickRFManager pickRFManager;
	public ViewScanPickContainerShell(WmsPickRFManager pickRFManager) {
		this.pickRFManager = pickRFManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String messge = "操作成功,请继续"+MyUtils.enter;
		String container = (String) this.getParentValue(WmsScanPickShellMessage.CONTAINER);
		if(StringUtils.isEmpty(container)){
			container = this.getTextField("scan.container");
			if (StringUtils.isEmpty (container) || "-".equals(container)) {
				this.setStatusMessage("器具标签不规范");
			}
			container = container.trim();
			if(MyUtils.OVER.equals(container)){
				forward(ShellFactory.getMainShell());
			}
			
			Map<String,String> result = pickRFManager.viewScanPickContainer(container);
			messge = result.get(WmsScanPickShellMessage.ERROR_MESG);
			if(StringUtils.isEmpty(messge)){
				messge = container+"明细";
				this.put(WmsScanPickShellMessage.CONTAINER, container);
				this.forward(ViewScanPickShell.PAGE_ID, messge);
			}else{
				this.forward(ViewScanPickContainerShell.PAGE_ID, messge);
			}
		}
	}

}
