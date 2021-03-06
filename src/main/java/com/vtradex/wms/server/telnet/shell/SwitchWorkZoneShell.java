package com.vtradex.wms.server.telnet.shell;

import java.io.IOException;
import java.util.List;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.component.support.PropertyOptionDisplayer;
import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWorkArea;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
import com.vtradex.wms.server.telnet.base.WmsWarehouseRFManager;
import com.vtradex.wms.server.telnet.dto.WmsWorkAreaExtDTO;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;
import com.vtradex.wms.server.web.filter.WmsWorkAreaExtHolder;
import com.vtradex.wms.server.web.filter.WmsWorkAreaHolder;
import com.vtradex.wms.server.web.filter.WmsWorkerHolder;

/**
 * @author: 李炎
 */
public class SwitchWorkZoneShell extends Thorn4BaseShell {

	public static final String PAGE_ID = "newLoginShell";
	protected WmsWarehouseRFManager wmsWarehouseRFManager;
	
	public SwitchWorkZoneShell(WmsWarehouseRFManager wmsWarehouseRFManager) {
		this.wmsWarehouseRFManager = wmsWarehouseRFManager;
	}

	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		
		List<WmsWarehouse> warehouses = wmsWarehouseRFManager.getWmsAvailableWarehousesByUserId();
		
		// 绑定仓库
		WmsWarehouse warehouse = (WmsWarehouse)getListField("warehouse", warehouses, new PropertyOptionDisplayer("name"));
		WmsWarehouseHolder.setWmsWarehouse(warehouse);
		
		// 绑定工作区
//		List<WmsWorkArea> workAreas = wmsWarehouseRFManager.getWorkAreaByDefaultWarehouse();
//		WmsWorkArea workArea = (WmsWorkArea)getListField("workArea", workAreas, new PropertyOptionDisplayer("code"));
//		WmsWorkAreaHolder.setWmsWorkArea(workArea);
		
		// 绑定工作人员
		List<WmsWorker> workers = wmsWarehouseRFManager.getWmsWorker(UserHolder.getUser().getId(), WmsWarehouseHolder.getWmsWarehouse().getId());
		if(workers.size() <= 0){
			this.forward(SwitchWorkZoneShell.PAGE_ID, "此用户未维护工作人员,无法登录");
		}
		WmsWorker worker = workers.get(0);
		WmsWorkerHolder.setWmsWorker(worker);
		
		// 绑定工作区扩展信息
//		WmsWorkAreaExtDTO workAreaExt = wmsWarehouseRFManager.getWmsWorkAreaExt(workArea);
//		WmsWorkAreaExtHolder.setWmsWorkAreaExt(workAreaExt);
				
		forward(getDefaultNextShell());
	}
}
