package com.vtradex.wms.server.telnet.shell.pick;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.pick.WmsPickRFManager;
import com.vtradex.wms.server.telnet.shipping.WmsShippingRFManager;
/**空箱出库*/
public class WmsShippingByEmptyBoxShell extends Thorn4BaseShell {
	public static final String PAGE_ID = "wmsShipRecordShell";
	private final WmsPickRFManager pickRFManager;
	
	public WmsShippingByEmptyBoxShell(WmsPickRFManager pickRFManager) {
		this.pickRFManager = pickRFManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String boxNo = this.getTextField("barCode");
		if (StringUtils.isEmpty (boxNo)) {
			this.setStatusMessage("空箱号必填");
		}
		String messge = "空箱出库成功,请继续(XX:返回主菜单;QQ:退出登录)";
		this.reset(messge);
		
	}

}
