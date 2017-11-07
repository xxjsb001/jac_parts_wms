package com.vtradex.wms.server.telnet.shell.count;

import java.io.IOException;

import net.wimpi.telnetd.net.Connection;

import org.apache.commons.lang.StringUtils;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.count.WmsCountTaskRFManager;
import com.vtradex.wms.server.telnet.dto.WmsCountTaskDTO;

public class WmsCountPlanRecordShell extends Thorn4BaseShell {
	
	private final WmsCountTaskRFManager rfCountTaskManager;
	
	private static final String CURRENT_TASK = "CURRENT_TASK";
	
	public WmsCountPlanRecordShell(WmsCountTaskRFManager rfCountTaskManager) {
		this.rfCountTaskManager = rfCountTaskManager;
	}
	
	private WmsCountTaskDTO getCurrentCountTask(){
		return (WmsCountTaskDTO)this.context.get(CURRENT_TASK);
	}

	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		//当前是否存在任务
		WmsCountTaskDTO unFinishCountTask = null;
		//获取当前作业人员下是否存在盘点任务
		if(unFinishCountTask == null)
			unFinishCountTask = rfCountTaskManager.queryUnFinishCountTask();
		//如果不存在则进行扫描库位
		if(unFinishCountTask == null) {
			String anyLocationCode = this.getTextField("申请任务");
			unFinishCountTask = rfCountTaskManager.applyNewCountTaskByCurrentLocationCode(anyLocationCode);
		}
		if(unFinishCountTask == null) {
			this.setStatusMessage("未找到任何盘点任务");
		}
		this.context.put(CURRENT_TASK, unFinishCountTask);
		this.output("盘点计划号", unFinishCountTask.getCountPlanCode());
		this.output("盘点库位", unFinishCountTask.getCountLocationCode());
		String confirmLocationCode = this.getTextField("库位");
		if(StringUtils.isEmpty(confirmLocationCode)) {
			this.setStatusMessage("库位不能为空");
		}
		if(!unFinishCountTask.getCountLocationCode().equals(confirmLocationCode)) {
			this.setStatusMessage("库位与盘点库位不一致");
		}
		String itemCode = this.getTextField("货品条码");
		
		//输0则是结束盘点
		if("0".equals(itemCode)) {
			unFinishCountTask = this.rfCountTaskManager.quantityAdjust(unFinishCountTask.getCountPlanId(),unFinishCountTask.getCountPlanDetailId(), unFinishCountTask.getCountLocationCode());
			this.context.clear();
			if(unFinishCountTask != null) {
				this.context.put(CURRENT_TASK, unFinishCountTask);
			}
			this.setStatusMessage(confirmLocationCode+"盘点完成");
		}
		
		this.rfCountTaskManager.checkItemExists(itemCode);
		
		Double quantity = this.getNumberField("数量");
		//数量不能为非正数
		if(quantity <= 0) {
			this.setStatusMessage("盘点数量非法");
		}
		//盘点登记
		this.rfCountTaskManager.countRecord(unFinishCountTask.getCountPlanDetailId(), itemCode, quantity);
		//清除货品和数量缓存
		this.remove("库位");
		this.remove("货品条码");
		this.remove("数量");
		this.setStatusMessage(itemCode+"盘点登记完成");
	}
	
	protected void forwardByKeyboard(String value) throws BreakException {
		if (value.equalsIgnoreCase("XX") || value.equalsIgnoreCase("QQ")) {
			WmsCountTaskDTO countTaskDTO = this.getCurrentCountTask();
			if(countTaskDTO != null) {
				this.rfCountTaskManager.cancelCountTask(countTaskDTO.getCountPlanDetailId());
			}
		}
	}
}
