package com.vtradex.wms.server.telnet.shell.replenishment;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.dto.WmsMoveTaskDTO;
import com.vtradex.wms.server.telnet.move.WmsMoveRFManager;
/**
 * 补货拣选库位
 * @author Administrator
 *
 */
public class WmsReplenishmentPickLocShell extends Thorn4BaseShell{

	public static final String PAGE_ID = "wmsReplenishmentPickLocShell";
	
	public static final String MOVEDOC_ID = "MOVEDOC_ID";
	private final WmsMoveRFManager rfWmsMoveManager;

	public WmsReplenishmentPickLocShell(WmsMoveRFManager rfWmsMoveManager) {
		this.rfWmsMoveManager = rfWmsMoveManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		
		Long moveDocId = (Long)this.getParentValue("MOVEDOC_ID");
		WmsMoveTaskDTO dto = (WmsMoveTaskDTO) this.getParentContext().get("CURRENT_DTO");
		if(dto == null){
			dto = rfWmsMoveManager.getLocationByMoveDocId(moveDocId);
		}
		if(dto == null){
			rfWmsMoveManager.activeReplenishment(moveDocId);
			this.forward(WmsReplenishmentPickShell.PAGE_ID, "拣选完成");
		}
		dto.setMoveDocId(moveDocId);
		
		this.output("移入库位", dto.getToLocationCode());
		String pickLocCode = this.getTextField("拣选库位");
		if(StringUtils.isEmpty(pickLocCode)){
			this.setStatusMessage("拣选库位不能为空");
		}
		if("00".equals(pickLocCode)){
			rfWmsMoveManager.markLocationFinished(dto);
			this.put("MOVEDOC_ID", dto.getMoveDocId());
			this.forward(WmsReplenishmentPickLocShell.PAGE_ID);
		}else{
			if(!rfWmsMoveManager.checkLocationByCode(pickLocCode)){
				this.setStatusMessage("库位不可用，请扫描其他库位");
			}
		}
		dto.setFromLocationCode(pickLocCode);
		this.put("CURRENT_DTO", dto);
		this.put("pickLocCode", pickLocCode);
		this.forward(WmsReplenishmentPickInventoryShell.PAGE_ID);
	}
}
