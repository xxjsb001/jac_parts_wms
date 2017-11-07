package com.vtradex.wms.server.telnet.shell.shipping;

import java.io.IOException;

import net.wimpi.telnetd.net.Connection;

import org.apache.commons.lang.StringUtils;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.telnet.shipping.WmsShippingRFManager;

/**
 * 扫描面单发运
 *
 * @category Shell
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2015/03/25 02:48:55 $
 */
public class WmsShippingByBoxNoShell extends Thorn4BaseShell {
	
	private WmsShippingRFManager wmsShippingRFManager;
	
	public WmsShippingByBoxNoShell(WmsShippingRFManager wmsShippingRFManager) {
		this.wmsShippingRFManager = wmsShippingRFManager;
	}

	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		
		String boxNo = this.getTextField("boxNo");
		if (StringUtils.isEmpty(boxNo)) {
			this.setStatusMessage("箱号必填");
		}
		
		try {
			wmsShippingRFManager.shippingByBoxNo(boxNo);
		} catch (BusinessException be) {
			String msg = LocalizedMessage.getLocalizedMessage(be.getMessage(), UserHolder.getReferenceModel(), UserHolder.getLanguage());
			this.setStatusMessage(msg);
		}
		this.reset("["+boxNo+"]已发运");
	}
}
