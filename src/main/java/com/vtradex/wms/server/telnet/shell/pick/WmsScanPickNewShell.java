package com.vtradex.wms.server.telnet.shell.pick;

import java.io.IOException;
import java.util.Map;

import net.wimpi.telnetd.net.Connection;

import org.apache.commons.lang.StringUtils;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.pick.WmsPickRFManager;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

public class WmsScanPickNewShell extends Thorn4BaseShell{
	public static final String PAGE_ID = "wmsScanPickNewShell";
	public static final String ITEM_CODE_R = "ITEM_CODE_R";
	public static final String ITEM_NAME_R = "ITEM_NAME_R";
	
	private final WmsPickRFManager pickRFManager;
	public WmsScanPickNewShell(WmsPickRFManager pickRFManager) {
		this.pickRFManager = pickRFManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		// TODO Auto-generated method stub
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
			this.forward(WmsScanPickNewShell.PAGE_ID, messge);
		}
		else if(StringUtils.isEmpty(moveDetailId)){
			output("pick.container",container);
			moveDetailId = this.getTextField("scan.itemCode");
			if (StringUtils.isEmpty (moveDetailId) || "-".equals(moveDetailId)) {
				this.setStatusMessage("拣货码不规范");
			}
			moveDetailId = moveDetailId.trim();
			if(MyUtils.OVER.equals(moveDetailId)){
				messge = "扫描器具标签";
				this.forward(WmsScanPickNewShell.PAGE_ID, messge);
			}
			Map<String,String> result = pickRFManager.picBarCode(moveDetailId,true);
			messge = result.get(WmsScanPickShellMessage.ERROR_MESG);
			if(!StringUtils.isEmpty(messge)){
				if(messge.equals(WmsScanPickShellMessage.ERROR_ITEM_NULL) //物料明细不存在
						|| messge.equals(WmsScanPickShellMessage.ERROR_ITEM_FINISHED)//物料拣货完成
						){
					this.put(WmsScanPickShellMessage.CONTAINER, container);
					this.forward(WmsScanPickNewShell.PAGE_ID, messge);
				}
			}
			this.put(ITEM_CODE_R, result.get(ITEM_CODE_R));
			this.put(ITEM_NAME_R, result.get(ITEM_NAME_R));
			messge = "确认拣货量";
			this.put(WmsScanPickShellMessage.CONTAINER, container);
			this.put(WmsScanPickShellMessage.ITEM_CODE, moveDetailId);
			this.forward(WmsScanPickNewShell.PAGE_ID, messge);
		}else{
			itemC = (String) this.getParentValue(ITEM_CODE_R);
			itemN = (String) this.getParentValue(ITEM_NAME_R);
			output("soi.itemCode",itemC);
			output("soi.itemName",itemN);
			String value = this.getTextField("pick.qty");//this.getNumberField("put.qty");
			Double picQuantity = 0D;
			if(StringUtils.isEmpty(value)){
				picQuantity = Double.MAX_VALUE;
			}else{
				if(MyUtils.OVER.equals(value)){
					messge = "扫描拣货码";
					this.put(WmsScanPickShellMessage.CONTAINER, container);
					this.forward(WmsScanPickNewShell.PAGE_ID, messge);
				}
				if(!JavaTools.isNumber(value)){
					messge = "失败!拣货量不是数字";
					this.setStatusMessage(messge);
				}
				picQuantity = JavaTools.stringToDouble(value);
			}
			if(picQuantity<=0){
				messge = "失败!拣货量不可小于等于0";
				this.setStatusMessage(messge);
			}
			Map<String,String> result = pickRFManager.singlePicNewQty(container, moveDetailId,picQuantity,"RF扫码拣货",true,WmsWarehouseHolder.getWmsWarehouse().getId());
			messge = result.get(WmsScanPickShellMessage.ERROR_MESG);
			if(!StringUtils.isEmpty(messge)){
				if(messge.equals(WmsScanPickShellMessage.ERROR_ITEM_NULL) //物料明细不存在
						|| messge.equals(WmsScanPickShellMessage.ERROR_ITEM_FINISHED)//物料拣货完成
						|| messge.equals(WmsScanPickShellMessage.ERROR_LOSS)//系统库存不足
						|| messge.equals(WmsScanPickShellMessage.ERROR_SHIP_LOC_NULL)){//备货库位不存在
					this.put(WmsScanPickShellMessage.CONTAINER, container);
					this.forward(WmsScanPickNewShell.PAGE_ID, messge);
				}else if(messge.equals(WmsScanPickShellMessage.ERROR_CONTAINER_NULL)){//容器不存在或失效
					this.forward(WmsScanPickNewShell.PAGE_ID, messge);
				}else if(messge.equals(WmsScanPickShellMessage.ERROR_LOC_NULL)){//库位号不存在
					this.put(WmsScanPickShellMessage.CONTAINER, container);
					this.forward(WmsScanPickNewShell.PAGE_ID, messge);
				}else if(messge.equals(WmsScanPickShellMessage.ERROR_PIC_MOVE)
						|| messge.equals(WmsScanPickShellMessage.ERROR_PIC_MOVE_INV)){//失败!拣货量超出可用量
					this.put(WmsScanPickShellMessage.CONTAINER, container);
					this.put(WmsScanPickShellMessage.ITEM_CODE, moveDetailId);
					this.put(ITEM_CODE_R, itemC);
					this.put(ITEM_NAME_R, itemN);
					this.forward(WmsScanPickNewShell.PAGE_ID, messge);
				}else{
					this.setStatusMessage(messge);
				}
			}
			messge = "成功,继续扫描物料";
			this.put(WmsScanPickShellMessage.CONTAINER, container);
			this.forward(WmsScanPickNewShell.PAGE_ID, messge);
		}
	}
}
