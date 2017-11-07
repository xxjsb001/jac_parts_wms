package com.vtradex.wms.server.telnet.shell.replenishment;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.dto.WmsMoveTaskDTO;
import com.vtradex.wms.server.telnet.move.WmsMoveRFManager;

public class WmsReplenishmentPutawayDoShell extends Thorn4BaseShell{

	public static final String PAGE_ID = "wmsReplenishmentPutawayDoShell";
	private final WmsMoveRFManager rfWmsMoveManager;

	public WmsReplenishmentPutawayDoShell(WmsMoveRFManager rfWmsMoveManager) {
		this.rfWmsMoveManager = rfWmsMoveManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		
		Long moveDocId = (Long)this.getParentValue("MOVEDOC_ID");
		WmsMoveTaskDTO dto = rfWmsMoveManager.findMoveDocDetailByMoveDocId(moveDocId);
		if(dto == null){
			this.forward(WmsReplenishmentPutawayShell.PAGE_ID,"上架完成");
		}
		this.output("移出库位", dto.getFromLocationCode());
		this.output("物料编码", dto.getItemCode());
		this.output("物料名称", dto.getItemName());
		this.output("移位数量", dto.getUnMoveQuantityBU().toString());
		this.output("移入库位", dto.getToLocationCode());
		String pickLocCode = this.getTextField("上架库位");
		if(StringUtils.isEmpty(pickLocCode)){
			this.setStatusMessage("上架库位不能为空");
		}
		if(!pickLocCode.equals(dto.getToLocationCode())){
			this.setStatusMessage("与系统推荐库位不一致");
		}
		rfWmsMoveManager.confimReplenishment(dto);
		this.put("MOVEDOC_ID", dto.getMoveDocId());
		this.forward(WmsReplenishmentPutawayDoShell.PAGE_ID);
	}

}
