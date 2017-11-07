package com.vtradex.wms.server.telnet.shell.move;

import java.io.IOException;
import java.util.Map;

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
 * 非计划手工散货移位，需要生成虚拟移位计划
 * 
 * @category Shell
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.2 $ $Date: 2015/10/22 03:16:52 $
 */
public class WmsMoveShell_BackUp extends Thorn4BaseShell {
	
	public static final String PAGE_ID = "wmsMoveShell";

	private final WmsMoveRFManager rfWmsMoveManager;
	private WmsMoveTaskDTO dto = null;

	public WmsMoveShell_BackUp(WmsMoveRFManager rfWmsMoveManager) {
		this.rfWmsMoveManager = rfWmsMoveManager;
	}

	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		Long moveDocId = null;
		if (dto != null) {
			moveDocId = dto.getMoveDocId();
		}
		try {
			if (dto == null) {
				dto = rfWmsMoveManager.findMoveTaskByVirtual(moveDocId); // 查找未未完成的虚拟计划
				if (dto != null) {
					String beContinueTired = this.getTextField("beContinueTired");
					if (StringUtils.isEmpty(beContinueTired) || TelnetConstants.CANCEL.equals(beContinueTired)) {
						this.put("dto", dto);
						this.forward(WmsMoveTiredShell.PAGE_ID);
					}
				}
			}
		} catch (BusinessException be) {
			this.forward(WmsMovePlanTableShell.PAGE_ID, be.getMessage());
		}
		if (dto == null) {
			dto = new WmsMoveTaskDTO();
		}
		String fromLocationCode = this.getTextField("fromLocationCode");
		if (StringUtils.isEmpty(fromLocationCode)) {
			this.setStatusMessage("移位库位必填");
		}
		if (!rfWmsMoveManager.validateLocation(fromLocationCode)) {
			this.setStatusMessage("移位库位不存在，请重填");
		}
		String barCode = this.getTextField("barCode");
		if (StringUtils.isEmpty(barCode)) {
			this.setStatusMessage("货品条码必填");
		}
		Double availableQuantityBU = 0D;
		try {
			Map<String, Object> result = rfWmsMoveManager.checkLocationInventoryValid(fromLocationCode, barCode);
			availableQuantityBU = (Double)result.get("availableQuantityBU");
			this.output("itemName", result.get("item").toString());
		} catch (BusinessException be) {
			this.setStatusMessage(be.getMessage());
		}
		Double actualMovedQuantityBU = this.getNumberField("actualMovedQuantityBU");
		if (actualMovedQuantityBU == null || actualMovedQuantityBU.doubleValue() <= 0) {
			this.setStatusMessage("移位数量必填");
		}
		if (actualMovedQuantityBU.doubleValue() > availableQuantityBU) {
			this.setStatusMessage("移位数量不可超过库存数");
		}
		String beTired = this.getTextField("beTired");
		if (StringUtils.isEmpty(beTired) || "0".equals(beTired)) { // 不累货
			try {
				dto.setFromLocationCode(fromLocationCode);
				dto.setBarCode(barCode);
				dto.setActualMovedQuantityBU(actualMovedQuantityBU);
				rfWmsMoveManager.createMoveDocDetail(dto);
				this.put("dto", dto);
				
				this.forward(WmsMoveTiredShell.PAGE_ID);
			} catch (BusinessException be) {
				this.setStatusMessage(be.getMessage());
			}
		} else {
			try {
				dto.setFromLocationCode(fromLocationCode);
				dto.setBarCode(barCode);
				dto.setActualMovedQuantityBU(actualMovedQuantityBU);
				rfWmsMoveManager.createMoveDocDetail(dto);
			} catch (BusinessException be) {
				this.setStatusMessage(be.getMessage());
			}
			this.reset("累货成功");
		}
	}
}
