package com.vtradex.wms.server.telnet.shell.replenishment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.component.support.PropertyOptionDisplayer;
import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.telnet.move.WmsMoveRFManager;
/**
 * 补货下单
 * @author Administrator
 *
 */
public class WmsReplenishmentOrderShell extends Thorn4BaseShell{
	
	public static final String PAGE_ID = "wmsReplenishmentOrderShell";
	private final WmsMoveRFManager rfWmsMoveManager;

	public WmsReplenishmentOrderShell(WmsMoveRFManager rfWmsMoveManager) {
		this.rfWmsMoveManager = rfWmsMoveManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String locationCode = this.getTextField("扫描上架库位");
		if(StringUtils.isEmpty(locationCode)){
			this.setStatusMessage("上架库位不能为空");
		}
		if("00".equals(locationCode)){
			WmsOrganization company = (WmsOrganization) this.getListField("货主", rfWmsMoveManager.getWmsOrganization(),  new PropertyOptionDisplayer("neiBuName"));
			rfWmsMoveManager.createMoveDocReplenishment(company);
			this.forward(WmsReplenishmentOrderShell.PAGE_ID,"创建成功");
		}else{
			if(!rfWmsMoveManager.checkLocationByCode(locationCode)){
				this.setStatusMessage("库位不可用，请扫描其他库位");
			}
			rfWmsMoveManager.createMoveDocLocation(locationCode);
			this.forward(WmsReplenishmentOrderShell.PAGE_ID,"扫描成功");
		}
	}

}
