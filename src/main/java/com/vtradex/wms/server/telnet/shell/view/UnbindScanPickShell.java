package com.vtradex.wms.server.telnet.shell.view;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.pick.WmsPickRFManager;
import com.vtradex.wms.server.telnet.shell.pick.WmsScanPickShellMessage;
import com.vtradex.wms.server.utils.MyUtils;
/**扫码解绑*/
public class UnbindScanPickShell extends Thorn4BaseShell{
	public static final String PAGE_ID = "unbindScanPickShell";
	public static final String ITEM_CODE_R = "ITEM_CODE_R";
	public static final String ITEM_NAME_R = "ITEM_NAME_R";
	
	private final WmsPickRFManager pickRFManager;
	public UnbindScanPickShell(WmsPickRFManager pickRFManager) {
		this.pickRFManager = pickRFManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String messge = "操作成功,请继续"+MyUtils.enter;
		String container = (String) this.getParentValue(WmsScanPickShellMessage.CONTAINER);
		String moveDetailId = (String) this.getParentValue(WmsScanPickShellMessage.ITEM_CODE);
		String itemC = (String) this.getParentValue(ITEM_CODE_R);
		String itemN = (String) this.getParentValue(ITEM_NAME_R);
		
		if(StringUtils.isEmpty(container)){
			container = this.getTextField("scan.container");
			if (StringUtils.isEmpty (container) || "-".equals(container)) {
				this.setStatusMessage("器具标签不规范");
			}
			container = container.trim();
			if(MyUtils.OVER.equals(container)){
				forward(ShellFactory.getMainShell());
			}
			messge = "扫描拣货码";
			this.put(WmsScanPickShellMessage.CONTAINER, container);
			this.forward(UnbindScanPickShell.PAGE_ID, messge);
		}else if(StringUtils.isEmpty(moveDetailId)){
			output("pick.container",container);
			moveDetailId = this.getTextField("scan.itemCode");
			if (StringUtils.isEmpty (moveDetailId) || "-".equals(moveDetailId)) {
				this.setStatusMessage("拣货码不规范");
			}
			moveDetailId = moveDetailId.trim();
			try {
				Long.parseLong(moveDetailId);
			} catch (Exception e) {
				messge = "失败!拣货码类型错误";
				this.put(WmsScanPickShellMessage.CONTAINER, container);
				this.forward(UnbindScanPickShell.PAGE_ID, messge);
			}
			if(MyUtils.OVER.equals(moveDetailId)){
				messge = "扫描器具标签";
				this.forward(UnbindScanPickShell.PAGE_ID, messge);
			}
			Map<String,String> result = pickRFManager.picBarCode(moveDetailId,false);
			messge = result.get(WmsScanPickShellMessage.ERROR_MESG);
			if(!StringUtils.isEmpty(messge)){
				if(messge.equals(WmsScanPickShellMessage.ERROR_ITEM_NULL) //物料明细不存在
						|| messge.equals(WmsScanPickShellMessage.ERROR_ITEM_FINISHED)//物料拣货完成
						){
					this.put(WmsScanPickShellMessage.CONTAINER, container);
					this.forward(UnbindScanPickShell.PAGE_ID, messge);
				}
			}
			this.put(ITEM_CODE_R, result.get(ITEM_CODE_R));
			this.put(ITEM_NAME_R, result.get(ITEM_NAME_R));
			messge = "确认解绑";
			this.put(WmsScanPickShellMessage.CONTAINER, container);
			this.put(WmsScanPickShellMessage.ITEM_CODE, moveDetailId);
			this.forward(UnbindScanPickShell.PAGE_ID, messge);
		}else{
			itemC = (String) this.getParentValue(ITEM_CODE_R);
			itemN = (String) this.getParentValue(ITEM_NAME_R);
			output("soi.pickNo",moveDetailId);
			output("soi.itemCode",itemC);
			output("soi.itemName",itemN);
			
			String value = this.getTextField("unbind.order");
			if(StringUtils.isEmpty(value) || MyUtils.ZERO.equals(value)){
				Map<String,String> result = pickRFManager.unbindPicQty(container, moveDetailId);
				messge = result.get(WmsScanPickShellMessage.ERROR_MESG);
				if(StringUtils.isEmpty(messge)){
					messge = "解绑成功!";
				}
			}else if(MyUtils.OVER.equals(value)){
				messge = "扫描拣货码";
			}else{
				messge = "错误!指令不对(空/0/00)";
			}
			this.put(WmsScanPickShellMessage.CONTAINER, container);
			this.forward(UnbindScanPickShell.PAGE_ID, messge);
		}
	}

}
