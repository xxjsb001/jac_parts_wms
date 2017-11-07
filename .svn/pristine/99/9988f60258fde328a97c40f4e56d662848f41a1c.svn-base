package com.vtradex.wms.server.telnet.shell.pick;

import java.io.IOException;

import net.wimpi.telnetd.net.Connection;

import org.apache.commons.lang.StringUtils;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.exception.OriginalBusinessException;
import com.vtradex.wms.server.telnet.dto.WmsPickTaskDTO;
import com.vtradex.wms.server.telnet.pick.WmsPickRFManager;

public class WmsPickTaskShell extends Thorn4BaseShell {
	
	private static final String CURRENT_DTO = "currentDTO";

	private final WmsPickRFManager pickRFManager;
	
	public WmsPickTaskShell(WmsPickRFManager pickRFManager) {
		this.pickRFManager = pickRFManager;
	}
	
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		Long workDocId = (Long)this.getParentValue(WmsWorkDocTableShell.WORK_DOC_ID);
		//通过作业单号和动线号获取任务
		
		WmsPickTaskDTO pickTaskDTO = this.getCurrentTaskDTO();
		
		if(pickTaskDTO == null) {
			pickTaskDTO = pickRFManager.findPickTaskById(workDocId);
		}
		
		if(pickTaskDTO == null) {
			this.forward("wmsWorkDocTableShell", "拣货完成");
		}
		
		putCurrentTaskDTO(pickTaskDTO);
		
		this.output("目标库位", pickTaskDTO.getFromLocationCode());
		this.output("货品", pickTaskDTO.getItemCode()+"-"+pickTaskDTO.getItemName());
		this.output("计划数量", pickTaskDTO.getUnFinishQuantity()+"");
		
		String confirmSrcLocCode = this.getTextField("拣选库位");
		if(StringUtils.isEmpty(confirmSrcLocCode)){
			this.setStatusMessage("拣选库位不能为空, 0-标记为异常库位, 待盘点");
		}
		if ("0".equals(confirmSrcLocCode)) {
			try {
				pickRFManager.markExceptionWmsLocation(pickTaskDTO.getFromLocationId());
				//重新分配库位
				pickRFManager.resetAllocate(pickTaskDTO);
				//重新分配任务
				pickTaskDTO = pickRFManager.findPickTaskById(workDocId);
				putCurrentTaskDTO(pickTaskDTO);
				this.setStatusMessage("标记成功,重新分配库位成功");
			} catch (BusinessException be) {
				this.setStatusMessage(be.getMessage());
			} catch (OriginalBusinessException ex) {
				this.setStatusMessage(ex.getMessage());
			}
		}

		this.pickRFManager.checkLocationValid(confirmSrcLocCode);
		
		if(!pickTaskDTO.getFromLocationCode().equals(confirmSrcLocCode)) {
			String choose = this.getTextField("库位不一致,是否继续(1/0)");
			if(!"0".equals(choose) && !"1".equals(choose)) {
				this.setStatusMessage("输入错误只支持(1/0)");
			}
			if("0".equals(choose)) {
				this.remove("拣选库位");
				this.setStatusMessage("");
			}
		}
		
		String itemCode = this.getTextField("货品条码");
		if(StringUtils.isEmpty(itemCode)){
			this.setStatusMessage("拣选货品不能为空");
		}
		if(!(pickTaskDTO.getItemCode().equals(itemCode) || pickTaskDTO.getBarCode().trim().equals(itemCode))) {
			this.setStatusMessage("货品不一致");
		}
		Double moveQty = this.getNumberField("拣选数量");
		if(moveQty <= 0){
			this.setStatusMessage("拣选数量非法");
		}
		if(moveQty > pickTaskDTO.getUnFinishQuantity()) {
			this.setStatusMessage("拣选数量大于待拣选数量");
		}
		pickRFManager.confirmPick(getCurrentTaskDTO(), confirmSrcLocCode, moveQty);
		this.reset("拣选完成");
	}
	
	private WmsPickTaskDTO getCurrentTaskDTO(){
		return (WmsPickTaskDTO)this.context.get(CURRENT_DTO);
	}
	
	private void putCurrentTaskDTO(WmsPickTaskDTO pickTaskDTO) {
		this.context.put(CURRENT_DTO, pickTaskDTO);
	}

}
