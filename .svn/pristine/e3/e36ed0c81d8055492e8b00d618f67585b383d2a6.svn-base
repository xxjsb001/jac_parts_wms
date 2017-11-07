/**
 * 
 */
package com.vtradex.wms.server.telnet.shell.putaway;

import java.io.IOException;

import net.wimpi.telnetd.net.Connection;

import org.apache.commons.lang.StringUtils;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.telnet.putaway.WmsPutawayRFManager;

/**
 * @author <a href="mailto:jin.liu@vtradex.net">刘晋</a>
 * @since 2012-8-10 上午11:27:14
 */
public class WmsASNPutawayByPalletShell extends Thorn4BaseShell{
public static final String PAGE_ID = "wmsASNPutawayByPalletShell";
	
	protected WmsPutawayRFManager wmsPutawayRFManager;
	
	public WmsASNPutawayByPalletShell(WmsPutawayRFManager wmsPutawayRFManager) {
		this.wmsPutawayRFManager = wmsPutawayRFManager;
	}

	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String asnCode = this.getTextField("asnCode");
		if (StringUtils.isEmpty(asnCode)) {
			this.setStatusMessage("ASN必填");
		}
		//是否有存在的未完成的上架单任务
		WmsTask task = wmsPutawayRFManager.getPutawayTaskByAsnCode(asnCode, true);
		if(task != null){
			WmsMoveDoc moveDoc = wmsPutawayRFManager.getWmsMoveDocByTaskId(task.getId());
			//判断上架单是否已经生效，如果没生效，先生效上架单
			wmsPutawayRFManager.activeMoveDoc(moveDoc.getId());
			this.context.put("asnCode", asnCode);
			this.context.put("moveDocId", moveDoc.getId());
			this.forward(WmsPalletPutawayGoShell.PAGE_ID,"请先上架以下物料");
		}else{
			wmsPutawayRFManager.findAsnByCode(asnCode);
			this.context.put("asnCode", asnCode);
			this.forward(WmsPalletPutawayShell.PAGE_ID);
		}
	}
}
