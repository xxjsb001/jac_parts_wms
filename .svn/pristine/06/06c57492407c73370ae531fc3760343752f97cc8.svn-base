package com.vtradex.wms.server.telnet.shell.move;

import java.io.IOException;

import net.wimpi.telnetd.net.Connection;

import org.apache.commons.lang.StringUtils;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.telnet.dto.WmsMoveTaskDTO;
import com.vtradex.wms.server.telnet.move.WmsMoveRFManager;
import com.vtradex.wms.server.telnet.shell.TelnetConstants;

/**
 * 计划移位
 * 
 * @category Shell
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2015/03/25 02:48:55 $
 */
public class WmsMovePlanShell extends Thorn4BaseShell {

	public static final String PAGE_ID = "wmsMovePlanShell";

	private final WmsMoveRFManager rfWmsMoveManager;

	public WmsMovePlanShell(WmsMoveRFManager rfWmsMoveManager) {
		this.rfWmsMoveManager = rfWmsMoveManager;
	}

	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		Long moveDocId = (Long) this.getParentValue("moveDocId");
		WmsMoveTaskDTO dto = null;
		try {
			dto = rfWmsMoveManager.findMoveTaskByMoveDocId(moveDocId);
			if (dto != null) {
				if (dto.getBeTiredPutaway()) {
					dto.setMoveDocId(moveDocId);
					this.put("dto", dto);
					this.forward(WmsMovePlanTiredShell.PAGE_ID);
				}
			}
		} catch (BusinessException be) {
			this.forward(WmsMovePlanTableShell.PAGE_ID, be.getMessage());
		}
		this.output("fromLocationCode", dto.getFromLocationCode());
		if (StringUtils.isNotEmpty(dto.getPallet()) && !BaseStatus.NULLVALUE.equals(dto.getPallet())) { // 托盘移位
			this.output("pallet", dto.getPallet());
			String pallet = this.getTextField("pallet");
			if (StringUtils.isEmpty(pallet)) {
				this.setStatusMessage("托盘号必填");
			}
			if (!pallet.equals(dto.getPallet())) {
				this.setStatusMessage("托盘号错误，待移位托盘：" + dto.getPallet());
			}
			moveByPallet(dto, pallet);
		} else if (StringUtils.isNotEmpty(dto.getCarton()) && !BaseStatus.NULLVALUE.equals(dto.getCarton())) {// 箱号移位
			this.output("carton", dto.getCarton());
			String carton = this.getTextField("carton");
			if (StringUtils.isEmpty(carton)) {
				this.setStatusMessage("箱号必填");
			}
			if (!carton.equals(dto.getCarton())) {
				this.setStatusMessage("托盘号错误，待移位托盘：" + dto.getPallet());
			}
			this.moveByCarton(dto, carton);
		} else { // 货品条码移位
			this.output("itemName", dto.getItemCode() + "-" + dto.getItemName());
			this.output("unMoveQuantityBU", dto.getUnMoveQuantityBU().toString());
			String barCode = this.getTextField("barCode");
			if (StringUtils.isEmpty(barCode)) {
				this.setStatusMessage("货品条码必填");
			}
			if (!barCode.equals(dto.getBarCode().trim())) {
				this.setStatusMessage("条码错误，待移位货品条码：" + dto.getBarCode());
			}
			this.moveByBarCode(dto, barCode);
		}
	}
	
	private void moveByPallet(WmsMoveTaskDTO dto, String pallet) throws BreakException, ContinueException, IOException, Exception {
//		Double actualMovedQuantityBU = this.getNumberField("actualMovedQuantityBU");
//		if (actualMovedQuantityBU == null || actualMovedQuantityBU.doubleValue() <= 0) {
//			this.setStatusMessage("移位数量必填");
//		}
//		if (actualMovedQuantityBU.doubleValue() > dto.getUnMoveQuantityBU()) {
//			this.setStatusMessage("移位数量不可超过待移位数量");
//		}
		dto.setActualMovedQuantityBU(dto.getUnMoveQuantityBU());
		this.output("toLocationCode", dto.getToLocationCode());
		String actualToLocationCode = this.getTextField("actualToLocationCode");
		if (StringUtils.isEmpty(actualToLocationCode)) {
			this.setStatusMessage("必填, 0-标记为异常库位并重新分配");
		}
		if ("0".equals(actualToLocationCode)) {
			try {
				rfWmsMoveManager.markExceptionWmsLocation(dto.getToLocationId());
				rfWmsMoveManager.resetAllocate(dto);
				this.setStatusMessage("标记成功, 重新分配库位成功");
			} catch (BusinessException be) {
				this.setStatusMessage(be.getMessage());
			}
		}
		if (!dto.getToLocationCode().equals(actualToLocationCode)) {
			if (!rfWmsMoveManager.validateLocation(actualToLocationCode)) {
				this.setStatusMessage("库位不存，请重新输入");
			}
			String beContinue = this.getTextField("beContinue");
			if (StringUtils.isNotEmpty(beContinue) && TelnetConstants.CANCEL.equals(beContinue)) {
				this.remove("actualToLocationCode");
				this.remove("beContinue");
				setStatusMessage("请重确认移位目标库位");
			}
		}
		try {
			rfWmsMoveManager.confirmMoveByPallet(dto, pallet, actualToLocationCode);
			this.reset("移位确认成功");
		} catch (BusinessException be) {
			this.setStatusMessage(be.getMessage());
		}
	}
	
	private void moveByCarton(WmsMoveTaskDTO dto, String carton) throws BreakException, ContinueException, IOException, Exception {
//		Double actualMovedQuantityBU = this.getNumberField("actualMovedQuantityBU");
//		if (actualMovedQuantityBU == null || actualMovedQuantityBU.doubleValue() <= 0) {
//			this.setStatusMessage("移位数量必填");
//		}
//		if (actualMovedQuantityBU.doubleValue() > dto.getUnMoveQuantityBU()) {
//			this.setStatusMessage("移位数量不可超过待移位数量");
//		}
		dto.setActualMovedQuantityBU(dto.getUnMoveQuantityBU());
		this.output("toLocationCode", dto.getToLocationCode());
		String actualToLocationCode = this.getTextField("actualToLocationCode");
		if (StringUtils.isEmpty(actualToLocationCode)) {
			this.setStatusMessage("必填, 0-标记为异常库位并重新分配");
		}
		if ("0".equals(actualToLocationCode)) {
			try {
				rfWmsMoveManager.markExceptionWmsLocation(dto.getToLocationId());
				rfWmsMoveManager.resetAllocate(dto);
				this.remove("actualMovedQuantityBU");
				this.remove("actualToLocationCode");
				this.setStatusMessage("标记成功, 重新分配库位成功");
			} catch (BusinessException be) {
				this.setStatusMessage(be.getMessage());
			}
		}
		if (!dto.getToLocationCode().equals(actualToLocationCode)) {
			if (!rfWmsMoveManager.validateLocation(actualToLocationCode)) {
				this.setStatusMessage("库位不存，请重新输入");
			}
			String beContinue = this.getTextField("beContinue");
			if (StringUtils.isNotEmpty(beContinue) && TelnetConstants.CANCEL.equals(beContinue)) {
				this.remove("actualToLocationCode");
				this.remove("beContinue");
				setStatusMessage("请重确认移位目标库位");
			}
		}
		try {
			rfWmsMoveManager.confirmMoveByCarton(dto, carton, actualToLocationCode);
			this.reset("移位确认成功");
		} catch (BusinessException be) {
			this.setStatusMessage(be.getMessage());
		}
	}
	
	private void moveByBarCode(WmsMoveTaskDTO dto, String barCode) throws BreakException, ContinueException, IOException, Exception {
		Double actualMovedQuantityBU = this.getNumberField("actualMovedQuantityBU");
		if (actualMovedQuantityBU == null || actualMovedQuantityBU.doubleValue() <= 0) {
			this.setStatusMessage("移位数量必填");
		}
		if (actualMovedQuantityBU.doubleValue() > dto.getUnMoveQuantityBU()) {
			this.setStatusMessage("移位数量不可超过待移位数量");
		}
		dto.setActualMovedQuantityBU(actualMovedQuantityBU);
		dto.setTiredMovedQuantityBU(actualMovedQuantityBU);
		String beTired = this.getTextField("beTired");
		if (StringUtils.isEmpty(beTired) || "0".equals(beTired)) { // 不累货
			try {
				rfWmsMoveManager.markTiredTask(dto);
				this.remove("actualMovedQuantityBU");
				this.remove("actualToLocationCode");
			} catch (BusinessException be) {
				this.setStatusMessage(be.getMessage());
			}
			this.put("dto", dto);
			this.forward(WmsMovePlanTiredShell.PAGE_ID);
		} else {
			try {
				rfWmsMoveManager.markTiredTask(dto);
			} catch (BusinessException be) {
				this.setStatusMessage(be.getMessage());
			}
			this.reset("累货成功");
		}
	}
}
