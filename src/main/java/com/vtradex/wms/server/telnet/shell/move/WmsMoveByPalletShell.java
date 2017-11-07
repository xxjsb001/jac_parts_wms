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
 * 按托盘直接移位，不走移位计划
 *
 * @category Shell
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.2 $ $Date: 2015/10/22 03:16:52 $
 */
public class WmsMoveByPalletShell extends Thorn4BaseShell {
	
	public static final String PAGE_ID = "wmsMoveByPallet";
	
	private final WmsMoveRFManager rfWmsMoveManager;

	public WmsMoveByPalletShell(WmsMoveRFManager rfWmsMoveManager) {
		this.rfWmsMoveManager = rfWmsMoveManager;
	}

	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String pallet = this.getTextField("pallet");
		if (StringUtils.isEmpty(pallet)) {
			this.setStatusMessage("必填");
		}
		
		WmsMoveTaskDTO dto = null;
		try {
			dto = rfWmsMoveManager.findMoveTaskByPallet(pallet.trim());
		} catch (BusinessException be) {
			this.setStatusMessage(be.getMessage());
		}
		this.output("fromLocationCode", dto.getFromLocationCode());
		if("待检".equals(dto.getSrcInventoryStatus())){
			this.remove(pallet);
			this.setStatusMessage("待检状态不允许移位");
		}
//		this.output("itemName", dto.getItemCode() + "-" + dto.getItemName());
//		this.output("unMoveQuantityBU", dto.getUnMoveQuantityBU().toString());
		
		/*try {
			rfWmsMoveManager.autoAllocateByPallet(dto);
		} catch (Exception ex) {
			//自动推荐库位失败后手工指定目标库位
			
		}*/
		/*if (dto.getToLocationId() != null) {
			this.output("toLocationCode", dto.getToLocationCode());
		} else {
			this.output("toLocationCode", "未找到合适库位，请手工指定");
		}*/
		String actualToLocationCode = this.getTextField("actualToLocationCode");
		if (StringUtils.isEmpty(actualToLocationCode)) {
			this.setStatusMessage("必填, 0-标记异常库位并重新自动分配");
		}
//		if ("0".equals(actualToLocationCode) && dto.getToLocationId() != null) {
//			try {
//				rfWmsMoveManager.markExceptionWmsLocation(dto.getToLocationId());
//				rfWmsMoveManager.autoAllocateByPallet(dto);
//				this.remove("toLocationCode");
//				this.remove("actualToLocationCode");
//				this.setStatusMessage("标记成功");
//			} catch (BusinessException be) {
//				this.setStatusMessage(be.getMessage());
//			} 
//		}
		/*if (!dto.getToLocationCode().equals(actualToLocationCode)) {}*/

		if (!rfWmsMoveManager.validateLocation(actualToLocationCode)) {
			this.setStatusMessage("库位不存在，请重新输入");
		}
		String beContinue = this.getTextField("beContinue");
		if (StringUtils.isNotEmpty(beContinue) && TelnetConstants.CANCEL.equals(beContinue)) {
			this.remove("actualToLocationCode");
			this.remove("beContinue");
			setStatusMessage("请重新确认移位目标库位");
		}
		try {
			rfWmsMoveManager.confirmMoveManualByPallet(dto, pallet, actualToLocationCode);
			this.reset("托盘移位确认成功");
		} catch (BusinessException be) {
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
