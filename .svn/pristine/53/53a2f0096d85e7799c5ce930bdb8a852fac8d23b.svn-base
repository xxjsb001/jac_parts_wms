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
 * 上架
 * 
 * @category Shell
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2015/03/25 02:48:55 $
 */
public class WmsASNPutawayShell extends Thorn4BaseShell {
	
	public static final String PAGE_ID = "wmsASNPutawayShell";
	
	protected WmsPutawayRFManager wmsPutawayRFManager;
	
	public WmsASNPutawayShell(WmsPutawayRFManager wmsPutawayRFManager) {
		this.wmsPutawayRFManager = wmsPutawayRFManager;
	}

	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String asnCode = this.getTextField("asnCode");
		if (StringUtils.isEmpty(asnCode)) {
			this.setStatusMessage("ASN必填");
		}
		//是否存在打开状态的上架单,如果有,直接删除
		wmsPutawayRFManager.deleteOPENMoveDoc(asnCode);
		
		//是否存在的未完成的上架单任务
		WmsTask task = wmsPutawayRFManager.getPutawayTaskByAsnCode(asnCode, false);
		if(task != null){
			WmsMoveDoc moveDoc = wmsPutawayRFManager.getWmsMoveDocByTaskId(task.getId());
			//判断上架单是否已经生效，如果没生效，先生效上架单
			wmsPutawayRFManager.activeMoveDoc(moveDoc.getId());
			this.context.put("asnCode", asnCode);
			this.context.put("moveDocId", moveDoc.getId());
			this.forward(WmsItemPutawayGoShell.PAGE_ID,"请先上架以下物料");
		}else{
			wmsPutawayRFManager.findAsnByCode(asnCode);
			this.context.put("asnCode", asnCode);
			this.forward(WmsItemPutawayShell.PAGE_ID);
		}
	}
}
