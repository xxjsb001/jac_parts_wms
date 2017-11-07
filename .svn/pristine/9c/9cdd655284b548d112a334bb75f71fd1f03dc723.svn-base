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
 * 补货拣选
 * @author Administrator
 *
 */
public class WmsReplenishmentPickDetailShell extends Thorn4BaseShell{
	
	public static final String PAGE_ID = "wmsReplenishmentPickDetailShell";
	private final WmsMoveRFManager rfWmsMoveManager;

	public WmsReplenishmentPickDetailShell(WmsMoveRFManager rfWmsMoveManager) {
		this.rfWmsMoveManager = rfWmsMoveManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		
		WmsMoveTaskDTO dto = (WmsMoveTaskDTO) this.getParentContext().get("CURRENT_DTO");
		Long invExtId = (Long)this.getParentValue("invExtId");
		dto = rfWmsMoveManager.findMoveTaskByInvExtId(invExtId, dto);
		this.output("物料编码", dto.getItemCode());
		this.output("物料名称", dto.getItemName());
		this.output("库存可用数量", dto.getUnMoveQuantityBU().toString());
		String pickQuantity = this.getTextField("移位数量");
		if(StringUtils.isEmpty(pickQuantity)){
			this.setStatusMessage("移位数量不能为空");
		}
		if(Double.valueOf(pickQuantity) > dto.getUnMoveQuantityBU()){
			this.setStatusMessage("移位数量不得大于库存可用数量");
		}
		dto.setActualMovedQuantityBU(Double.valueOf(pickQuantity));
		rfWmsMoveManager.createReplenishmentMoveDocDetail(dto);
		this.put("pickLocCode", dto.getFromLocationCode());
		this.put("CURRENT_DTO", dto);
		this.forward(WmsReplenishmentPickInventoryShell.PAGE_ID);
	}
}
