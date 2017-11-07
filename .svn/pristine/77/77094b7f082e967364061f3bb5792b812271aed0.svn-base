package com.vtradex.wms.server.telnet.shell.move;

import java.io.IOException;

import net.wimpi.telnetd.net.Connection;

import org.apache.commons.lang.StringUtils;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.wms.server.telnet.dto.WmsMoveTaskDTO;
import com.vtradex.wms.server.telnet.move.WmsMoveRFManager;
import com.vtradex.wms.server.telnet.shell.TelnetConstants;

/**
 * 手工散货移位上架
 *
 * @category Shell
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.2 $ $Date: 2015/10/22 03:16:52 $
 */
public class WmsMoveTiredShell extends Thorn4BaseShell {
	
	public static final String PAGE_ID = "wmsMoveTiredShell";

	private final WmsMoveRFManager rfWmsMoveManager;

	public WmsMoveTiredShell(WmsMoveRFManager rfWmsMoveManager) {
		this.rfWmsMoveManager = rfWmsMoveManager;
	}

	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		WmsMoveTaskDTO parentDto = (WmsMoveTaskDTO)this.getParentContext().get("dto");
		WmsMoveTaskDTO dto = null;
		try {
			dto = rfWmsMoveManager.findMoveTaskByTired(parentDto.getMoveDocId());
			if(null == dto.getSrcInventoryExtendId()){
				dto.setSrcInventoryExtendId(parentDto.getSrcInventoryExtendId());//一个移位单明细对应一个库存序列（jac特殊需求）
			}
		} catch (BusinessException be) {
			this.forward(WmsMoveShell.PAGE_ID, be.getMessage());
		}
		this.output("fromLocationCode", dto.getFromLocationCode());
		this.output("itemName", dto.getItemCode() + "-" + dto.getItemName());
		this.output("unMoveQuantityBU", dto.getUnMoveQuantityBU().toString());

		
		if (StringUtils.isNotEmpty(dto.getToLocationCode())) {
			this.output("toLocationCode", dto.getToLocationCode());
		}
		String actualToLocationCode = this.getTextField("actualToLocationCode");
		if (StringUtils.isEmpty(actualToLocationCode)) {
			this.setStatusMessage("必填");
		}
		if (StringUtils.isNotEmpty(dto.getToLocationCode()) && !dto.getToLocationCode().equals(actualToLocationCode)) {
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
			rfWmsMoveManager.confirmMoveManualByPalletItem(dto, actualToLocationCode);
			this.reset("移位确认成功");
		} catch (BusinessException be) {
			this.remove("actualMovedQuantityBU");
			this.remove("actualToLocationCode");
			this.remove("beContinue");
			this.setStatusMessage(be.getMessage());
		}
	}
	
	protected void forwardByKeyboard(String value) throws BreakException {  
	    if (value.equalsIgnoreCase("XX")) {  
	        if(StringUtils.isEmpty(getShellByXX()))  
	            forward(ShellFactory.getMainShell());  
	        else  
	            forward(getShellByXX());  
	    } else if (value.equalsIgnoreCase("QQ")) {  
	        if(StringUtils.isEmpty(getShellByQQ()))  
	            forward(ShellFactory.getEntranceShell());  
	        else  
	            forward(getShellByQQ());  
	    }else if(value.equalsIgnoreCase("..")){//跳转至上一屏  
	        forwardByKeyboard("XX");  
	    }else if(value.equalsIgnoreCase("...")){//退出登录  
	        forwardByKeyboard("QQ");  
	    } 
	}
}
