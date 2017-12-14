package com.vtradex.wms.server.telnet.shell.move;

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
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;
/**移位下架 */
public class WmsScanMoveShell extends Thorn4BaseShell{
	public static final String PAGE_ID = "wmsScanMoveShell";
	String messge = "操作成功,请继续"+MyUtils.enter;
	
	
	private final WmsPickRFManager pickRFManager;
	public WmsScanMoveShell(WmsPickRFManager pickRFManager) {
		this.pickRFManager = pickRFManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String moveNo = (String) this.getParentValue(WmsScanPickShellMessage.PICK_CODE);
		String location = (String) this.getParentValue(WmsScanPickShellMessage.LOC_CODE);
		String itemCode = (String) this.getParentValue(WmsScanPickShellMessage.ITEM_CODE);
		if(StringUtils.isEmpty(moveNo)){
			moveNo = this.getTextField("scan.pickNo");
			if (StringUtils.isEmpty (moveNo) || "-".equals(moveNo)) {
				this.setStatusMessage("移位单号不规范");
			}
			moveNo = moveNo.trim();
			if(MyUtils.OVER.equals(moveNo)){
				forward(ShellFactory.getMainShell());
			}
			
			Map<String,String> result = pickRFManager.findMove(moveNo);
			messge = result.get(WmsScanPickShellMessage.ERROR_MESG);
			if(!StringUtils.isEmpty(messge)){ 
				this.setStatusMessage(messge);
			}
			messge = "扫描下架库位";
			this.put(WmsScanPickShellMessage.PICK_CODE, moveNo);
			this.forward(WmsScanMoveShell.PAGE_ID, messge);
		}else if(StringUtils.isEmpty(location)){
			output("scan.pickNo",moveNo);
			location = this.getTextField("scan.location");
			if (StringUtils.isEmpty (location) || "-".equals(location)) {
				this.setStatusMessage("库位号不规范");
			}
			location = location.trim();
			if(MyUtils.OVER.equals(location)){
				messge = "扫描移位单号";
				this.forward(WmsScanMoveShell.PAGE_ID, messge);
			}
			messge = "扫描物料条码";
			this.put(WmsScanPickShellMessage.PICK_CODE, moveNo);
			this.put(WmsScanPickShellMessage.LOC_CODE, location);
			this.forward(WmsScanMoveShell.PAGE_ID, messge);
		}else if(StringUtils.isEmpty(itemCode)){
			output("scan.location",location);
			itemCode = this.getTextField("scan.itemCode");
			if (StringUtils.isEmpty (itemCode) || "-".equals(itemCode)) {
				this.setStatusMessage("物料条码不规范");
			}
			itemCode = itemCode.trim();
			if(MyUtils.OVER.equals(itemCode)){
				messge = "扫描库位";
				this.put(WmsScanPickShellMessage.PICK_CODE, moveNo);
				this.forward(WmsScanMoveShell.PAGE_ID, messge);
			}else if(MyUtils.THIS.equals(itemCode)){
				messge = "扫描移位单号";
				this.forward(WmsScanMoveShell.PAGE_ID, messge);
			}
			messge = "确认移位量";
			this.put(WmsScanPickShellMessage.PICK_CODE, moveNo);
			this.put(WmsScanPickShellMessage.LOC_CODE, location);
			this.put(WmsScanPickShellMessage.ITEM_CODE, itemCode);
			this.forward(WmsScanMoveShell.PAGE_ID, messge);
		}else{
			output("scan.location",location);
			output("scan.itemCode",itemCode);
			String value = this.getTextField("pick.qty");//this.getNumberField("put.qty");
			Double picQuantity = 0D;
			if(StringUtils.isEmpty(value)){
				picQuantity = Double.MAX_VALUE;
			}else{
				if(MyUtils.OVER.equals(value)){
					messge = "扫描物料条码";
					this.put(WmsScanPickShellMessage.PICK_CODE, moveNo);
					this.put(WmsScanPickShellMessage.LOC_CODE, location);
					this.forward(WmsScanMoveShell.PAGE_ID, messge);
				}
				if(!JavaTools.isNumber(value)){
					messge = "失败!移位量不是数字";
					this.setStatusMessage(messge);
				}
				picQuantity = JavaTools.stringToDouble(value);
			}
			if(picQuantity<=0){
				messge = "失败!移位量不可小于等于0";
				this.setStatusMessage(messge);
			}
			Map<String,String> result = pickRFManager.singleMoveQty(moveNo, location, itemCode,picQuantity,WmsWarehouseHolder.getWmsWarehouse().getId());
			messge = result.get(WmsScanPickShellMessage.ERROR_MESG);
			if(!StringUtils.isEmpty(messge)){
				if(messge.equals(WmsScanPickShellMessage.ERROR_ITEM_NULL) //物料明细不存在
						|| messge.equals(WmsScanPickShellMessage.ERROR_ITEM_FINISHED)//物料拣货完成
						|| messge.equals(WmsScanPickShellMessage.ERROR_LOSS)//系统库存不足
						){
					this.put(WmsScanPickShellMessage.PICK_CODE, moveNo);
					this.put(WmsScanPickShellMessage.LOC_CODE, location);
					this.forward(WmsScanMoveShell.PAGE_ID, messge);
				}else if(messge.equals(WmsScanPickShellMessage.ERROR_LOC_NULL)){//库位号不存在
					this.put(WmsScanPickShellMessage.PICK_CODE, moveNo);
					this.forward(WmsScanMoveShell.PAGE_ID, messge);
				}else if(messge.equals(WmsScanPickShellMessage.ERROR_PIC_MOVE)
						|| messge.equals(WmsScanPickShellMessage.ERROR_PIC_MOVE_INV)){//失败!拣货量超出可用量
					this.put(WmsScanPickShellMessage.PICK_CODE, moveNo);
					this.put(WmsScanPickShellMessage.LOC_CODE, location);
					this.put(WmsScanPickShellMessage.ITEM_CODE, itemCode);
					this.forward(WmsScanMoveShell.PAGE_ID, messge);
				}else{
					this.setStatusMessage(messge);
				}
			}
			messge = "成功,继续扫描物料";
			this.put(WmsScanPickShellMessage.PICK_CODE, moveNo);
			this.put(WmsScanPickShellMessage.LOC_CODE, location);
			this.forward(WmsScanMoveShell.PAGE_ID, messge);
		}
		
	}

}
