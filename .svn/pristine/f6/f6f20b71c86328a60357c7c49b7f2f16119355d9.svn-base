package com.vtradex.wms.server.telnet.shell.move;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wimpi.telnetd.net.Connection;

import org.apache.commons.lang.StringUtils;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.telnet.dto.WmsMoveTaskDTO;
import com.vtradex.wms.server.telnet.move.WmsMoveRFManager;
import com.vtradex.wms.server.telnet.shell.TelnetConstants;

/**
 * 非计划手工散货移位，需要生成虚拟移位计划
 * 
 * @category Shell
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.5 $ $Date: 2016/05/26 06:09:04 $
 */
public class WmsMoveShell extends Thorn4BaseShell {
	
	public static final String PAGE_ID = "wmsMoveShell";

	private final WmsMoveRFManager rfWmsMoveManager;
	private WmsMoveTaskDTO dto = null;

	public WmsMoveShell(WmsMoveRFManager rfWmsMoveManager) {
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
		String pallet = this.getTextField("palletSign");
		if (StringUtils.isEmpty(pallet)) {
			this.setStatusMessage("托盘标签必填");
		}
		if (!rfWmsMoveManager.validatePalletSign(pallet)) {
			this.setStatusMessage("托盘标签不存在，请重填");
		}
		//根据托盘标签选择物料
		List<Object[]> inventoryExtends = rfWmsMoveManager.getInventoyExtendByPallet(pallet);
		Long invExId = null;
		WmsInventoryExtend invEx =null;
		WmsInventory inv =null;
		if(inventoryExtends.size()>0){
			Map<String,String> inventroyExtendMap = new HashMap<String,String>();
			String key = null;
			for(int i = 0 ; i<inventoryExtends.size() ; i++){
				Object[] inventroyExtend = inventoryExtends.get(i);//[ wie.id, wie.item.name,wie.item.code,wie.locationCode,(wie.quantityBU - wie.allocatedQuantityBU)]
				key = (i+1)+"";
				output(key,inventroyExtend[1]+"/"+inventroyExtend[2]+"/"+inventroyExtend[4]);
				inventroyExtendMap.put(key, inventroyExtend[0].toString());
			}
			String indexKey = this.getTextField("inventoryIndex");
			if (StringUtils.isEmpty(indexKey)) {
				this.setStatusMessage("库存序列号必填");
			}
			try {
				invExId = Long.valueOf(inventroyExtendMap.get(indexKey));
				invEx= rfWmsMoveManager.getWmsInventoryExtend(invExId);
				inv = rfWmsMoveManager.getWmsInventoryByExtendId(invExId);
				if("待检".equals(inv.getStatus())){
					this.remove(indexKey);
					this.setStatusMessage("待检状态不允许移位");
				}
			} catch (BusinessException be) {
				this.setStatusMessage(be.getMessage());
			}
		}
		Double availableQuantityBU = invEx.getAvailableQuantityBU();
		Double actualMovedQuantityBU = this.getNumberField("actualMovedQuantityBU");
		if (actualMovedQuantityBU == null || actualMovedQuantityBU.doubleValue() <= 0) {
			this.setStatusMessage("移位数量必填");
		}
		if (actualMovedQuantityBU.doubleValue() > availableQuantityBU) {
			this.setStatusMessage("移位数量不可超过库存数");
		}
		
		try {
			dto.setSrcInventoryExtendId(invExId);
			dto.setActualMovedQuantityBU(actualMovedQuantityBU);
			rfWmsMoveManager.createMoveDocDetailJac(dto);
			this.put("dto", dto);
			this.forward(WmsMoveTiredShell.PAGE_ID);
		} catch (BusinessException be) {
			this.setStatusMessage(be.getMessage());
		}
		
		/*String beTired = this.getTextField("beTired");
		if (StringUtils.isEmpty(beTired) || "0".equals(beTired)) { // 不累货
			try {
				dto.setSrcInventoryExtendId(invExId);
				dto.setActualMovedQuantityBU(actualMovedQuantityBU);
				rfWmsMoveManager.createMoveDocDetailJac(dto);
				this.put("dto", dto);
				this.forward(WmsMoveTiredShell.PAGE_ID);
			} catch (BusinessException be) {
				this.setStatusMessage(be.getMessage());
			}
		} else {
			try {
				dto.setSrcInventoryExtendId(invExId);
				dto.setActualMovedQuantityBU(actualMovedQuantityBU);
				rfWmsMoveManager.createMoveDocDetailJac(dto);
			} catch (BusinessException be) {
				this.setStatusMessage(be.getMessage());
			}
			this.reset("累货成功");
		}*/
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
