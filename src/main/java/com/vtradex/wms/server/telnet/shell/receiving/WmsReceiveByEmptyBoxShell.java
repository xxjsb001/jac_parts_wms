package com.vtradex.wms.server.telnet.shell.receiving;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.receiving.WmsReceivingRFManager;
/**空箱入库*/
public class WmsReceiveByEmptyBoxShell extends Thorn4BaseShell {
	public static final String PAGE_ID = "wmsReceiveByEmptyBoxShell";
	private WmsReceivingRFManager wmsReceivingRFManager;
	
	public WmsReceiveByEmptyBoxShell(WmsReceivingRFManager wmsReceivingRFManager) {
		this.wmsReceivingRFManager = wmsReceivingRFManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String boxNo = this.getTextField("barCode");
		if (StringUtils.isEmpty (boxNo)) {
			this.setStatusMessage("空箱号必填");
		}
		String messge = "空箱入库成功,请继续(XX:返回主菜单;QQ:退出登录)";
		this.reset(messge);
	}

}
