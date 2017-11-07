package com.vtradex.wms.server.telnet.shell.move;

import java.io.IOException;

import net.wimpi.telnetd.net.Connection;

import org.apache.commons.lang.StringUtils;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.wms.server.telnet.dto.WmsMoveTaskDTO;
import com.vtradex.wms.server.telnet.move.WmsMoveRFManager;
import com.vtradex.wms.server.telnet.shell.TelnetConstants;

/**
 * 处理移位累货
 *
 * @category Shell
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2015/03/25 02:48:55 $
 */
public class WmsMovePlanTiredShell extends Thorn4BaseShell {
	
	public static final String PAGE_ID = "wmsMovePlanTiredShell";

	private final WmsMoveRFManager rfWmsMoveManager;

	public WmsMovePlanTiredShell(WmsMoveRFManager rfWmsMoveManager) {
		this.rfWmsMoveManager = rfWmsMoveManager;
	}

	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		WmsMoveTaskDTO parentDto = (WmsMoveTaskDTO)this.getParentContext().get("dto");
		WmsMoveTaskDTO dto = null;
		try {
			dto = rfWmsMoveManager.findMovePutawayTaskByTired(parentDto.getMoveDocId());
		} catch (BusinessException be) {
			this.put("moveDocId", parentDto.getMoveDocId());
			this.forward(WmsMovePlanShell.PAGE_ID, be.getMessage());
		}
		this.output("fromLocationCode", dto.getFromLocationCode());
		this.output("itemName", dto.getItemCode() + "-" + dto.getItemName());
		this.output("unMoveQuantityBU", dto.getUnMoveQuantityBU().toString());
//		this.output("tiredMovedQuantityBU", dto.getTiredMovedQuantityBU().toString());
		
		this.output("toLocationCode", dto.getToLocationCode());
		String actualToLocationCode = this.getTextField("actualToLocationCode");
		if (StringUtils.isEmpty(actualToLocationCode)) {
			this.setStatusMessage("必填, 0-标记为异常库位并重新分配");
		}
		if ("0".equals(actualToLocationCode)) {
			try {
				rfWmsMoveManager.markExceptionWmsLocation(dto.getToLocationId());
				rfWmsMoveManager.resetAllocate(dto);
				this.remove("actualToLocationCode");
				this.setStatusMessage("标记成功, 重新分配库位成功");
			} catch (BusinessException be) {
				this.setStatusMessage(be.getMessage());
			}
		}
		if (!dto.getToLocationCode().equals(actualToLocationCode)) {
			if (!rfWmsMoveManager.validateLocation(actualToLocationCode)) {
				this.setStatusMessage("库位不存在，请重新输入");
			}
			String beContinue = this.getTextField("beContinue");
			if (StringUtils.isNotEmpty(beContinue) && TelnetConstants.CANCEL.equals(beContinue)) {
				this.remove("actualToLocationCode");
				this.remove("beContinue");
				setStatusMessage("请重确认移位目标库位");
			}
		}
		Double actualMovedQuantityBU = this.getNumberField("actualMovedQuantityBU");
		if (actualMovedQuantityBU == null || actualMovedQuantityBU.doubleValue() <= 0) {
			this.setStatusMessage("移位数量必填");
		}
		if (actualMovedQuantityBU.doubleValue() > dto.getUnMoveQuantityBU()) {
			this.setStatusMessage("移位数量不可超过待移位数量");
		}
		dto.setActualMovedQuantityBU(actualMovedQuantityBU);
		try {
			rfWmsMoveManager.confirmMoveByTired(dto, actualToLocationCode);
			this.reset("移位确认成功");
		} catch (BusinessException be) {
			this.remove("actualMovedQuantityBU");
			this.remove("actualToLocationCode");
			this.remove("beContinue");
			this.setStatusMessage(be.getMessage());
		}
	}
}
