/**
 * 
 */
package com.vtradex.wms.server.telnet.shell.putaway;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.engine.exception.EcadBaseException;
import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.telnet.putaway.WmsPutawayRFManager;
import com.vtradex.wms.server.telnet.shell.TelnetConstants;

/**
 * @author <a href="mailto:jin.liu@vtradex.net">刘晋</a>
 * @since 2012-8-10 上午11:31:48
 */
public class WmsPalletPutawayGoShell  extends Thorn4BaseShell{
public static final String PAGE_ID = "wmsPalletPutawayGoShell";
	
	private WmsPutawayRFManager wmsPutawayRFManager;
	
	public WmsPalletPutawayGoShell(WmsPutawayRFManager wmsPutawayRFManager) {
		this.wmsPutawayRFManager = wmsPutawayRFManager;
	}

	protected void mainProcess(Connection connection) throws BreakException, ContinueException, IOException, Exception {
		
		String asnCode = this.getParentContext().get("asnCode").toString();
		output("收货单号",asnCode);
		//根据收货单获得上架任务
		WmsTask task= wmsPutawayRFManager.getPutawayTaskByAsnCode(asnCode, true);
		if(task == null){
			//如果当前上架单全部完成，则从新扫描上架单，否则直接扫描货品
			try {
				wmsPutawayRFManager.findAsnByCode(asnCode);
			} catch (Exception e) {
				this.forward(WmsASNPutawayByPalletShell.PAGE_ID,"收货单待码托货品全部上架完成");
			}
			this.context.put("asnCode", asnCode);
			this.forward(WmsPalletPutawayShell.PAGE_ID,"上架完成");
		}
		Map<String,Object> result = wmsPutawayRFManager.getInfoByTask(task.getId());
		output("货品条码",result.get("barCode").toString());
		output("货品",result.get("itemCode").toString());
		output("包装单位",result.get("packageUnit").toString());
		output("上架数量",result.get("putawayQty").toString());
		output("托盘号",result.get("pallet").toString());
		
		//显示库位
		String sysToLocationCode = "";//系统推荐的上架库位
		sysToLocationCode = result.get("toLocation").toString();
		output("推荐库位",sysToLocationCode);
		
		String putawayLocationCode = "";//用户确认的上架库位
		putawayLocationCode = getTextField("putawayLocationCode");
		if (StringUtils.isEmpty(putawayLocationCode)) {
//			putawayLocationCode = sysToLocationCode;
			this.setStatusMessage("上架库位必填, 0-标记为异常库位, 待盘点");
		}
		if ("0".equals(putawayLocationCode)) {
			try {
				wmsPutawayRFManager.markExceptionWmsLocation(Long.valueOf(result.get("toLocationId").toString()));
				//重新分配库位
				wmsPutawayRFManager.resetAllocate(task.getId());
				this.context.put("asnCode", asnCode);
				this.forward(WmsPalletPutawayGoShell.PAGE_ID);
			} catch (Exception be) {
				this.setStatusMessage(be.getMessage());
			}
		}else{
			if (!putawayLocationCode.equals(sysToLocationCode)) {
				if (!wmsPutawayRFManager.validateLocation(putawayLocationCode)) {
					this.setStatusMessage("当前上架库位不存在，请重新确认");
				}
				String beContinue = this.getTextField("beContinue");
				if (!StringUtils.isEmpty(beContinue) && !TelnetConstants.YES.equals(beContinue)) {
					this.context.put("asnCode", asnCode);
					this.forward(WmsPalletPutawayGoShell.PAGE_ID);
				}
			}
			try {
				wmsPutawayRFManager.confirmPutaway(task.getId(),putawayLocationCode);
			} catch (EcadBaseException ebe) {
				ebe.printStackTrace();
				this.context.put("asnCode", asnCode);
				this.forward(WmsPalletPutawayGoShell.PAGE_ID);
			}
			this.context.put("asnCode", asnCode);
			this.forward(WmsPalletPutawayGoShell.PAGE_ID,"上架成功");
		}
	}

}
