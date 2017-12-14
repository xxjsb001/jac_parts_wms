package com.vtradex.wms.server.telnet.shell.putaway;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.putaway.WmsPutawayRFManager;
import com.vtradex.wms.server.utils.MyUtils;
/**扫码直接入库上架*/
public class WmsPutAsnScanShell extends Thorn4BaseShell{
	public static final String PAGE_ID = "wmsPutAsnScanShell";
	public static final String ASN_ID = "ASN_ID";
	protected static WmsPutawayRFManager wmsPutawayRFManager;
	@SuppressWarnings("static-access")
	public WmsPutAsnScanShell(WmsPutawayRFManager wmsPutawayRFManager) {
		this.wmsPutawayRFManager = wmsPutawayRFManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String messge = "操作成功,请继续"+MyUtils.enter;
		Long asnId = (Long)this.getParentValue(ASN_ID);
		if(asnId==null){
			String asnCode = this.getTextField("lotRule.soi");
			if (StringUtils.isEmpty (asnCode) || "-".equals(asnCode)) {
				this.setStatusMessage("收货单号不规范");
			}
			asnCode = asnCode.trim();
			if(MyUtils.OVER.equals(asnCode)){
				forward(ShellFactory.getMainShell());
			}
			asnId = wmsPutawayRFManager.findAsnId(asnCode);
			if(asnId==null || asnId == 0L){
				messge = "失败!单号不存在:"+MyUtils.enter;
				this.forward(WmsPutAsnScanShell.PAGE_ID,messge);
			}else{
				messge = "选择序号";
				this.put(ASN_ID, asnId);
				this.forward(ViewPutAsnDetailShell.PAGE_ID,messge);
			}
		}
	}

}
