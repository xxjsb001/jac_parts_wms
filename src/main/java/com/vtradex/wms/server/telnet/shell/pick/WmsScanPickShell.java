package com.vtradex.wms.server.telnet.shell.pick;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.pick.WmsPickRFManager;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;
/**扫码备料 */
public class WmsScanPickShell extends Thorn4BaseShell{
	public static final String PAGE_ID = "wmsScanPickShell";
	
	private final WmsPickRFManager pickRFManager;
	public WmsScanPickShell(WmsPickRFManager pickRFManager) {
		this.pickRFManager = pickRFManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		// TODO Auto-generated method stub
		String messge = "操作成功,请继续"+MyUtils.enter;
		String pickNo = (String) this.getParentValue(WmsScanPickShellMessage.PICK_CODE);
		String container = (String) this.getParentValue(WmsScanPickShellMessage.CONTAINER);
		String location = (String) this.getParentValue(WmsScanPickShellMessage.LOC_CODE);
		String itemCode = (String) this.getParentValue(WmsScanPickShellMessage.ITEM_CODE);
		
		if(StringUtils.isEmpty(pickNo)){
			pickNo = this.getTextField("scan.pickNo");
			if (StringUtils.isEmpty (pickNo) || "-".equals(pickNo)) {
				this.setStatusMessage("拣货单号不规范");
			}
			pickNo = pickNo.trim();
			if(MyUtils.OVER.equals(pickNo)){
				forward(ShellFactory.getMainShell());
			}
			
			Map<String,String> result = pickRFManager.findMove(pickNo);
			messge = result.get(WmsScanPickShellMessage.ERROR_MESG);
			if(!StringUtils.isEmpty(messge)){ 
				this.setStatusMessage(messge);
			}
			messge = "扫描器具标签";
			this.put(WmsScanPickShellMessage.PICK_CODE, pickNo);
			this.forward(WmsScanPickShell.PAGE_ID, messge);
		}else if(StringUtils.isEmpty(container)){
			output("pick.pickNo",pickNo);
			container = this.getTextField("scan.container");
			if (StringUtils.isEmpty (container) || "-".equals(container)) {
				this.setStatusMessage("器具标签不规范");
			}
			container = container.trim();
			if(MyUtils.OVER.equals(container)){
				messge = "扫描拣货单号";
				this.forward(WmsScanPickShell.PAGE_ID, messge);
			}
//			Map<String,String> result = pickRFManager.findContainer(container);
//			messge = result.get(ERROR_MESG);
//			if(!StringUtils.isEmpty(messge)){ 
//				this.setStatusMessage(messge);
//			}
			messge = "扫描物料条码";
			this.put(WmsScanPickShellMessage.PICK_CODE, pickNo);
			this.put(WmsScanPickShellMessage.CONTAINER, container);
			this.forward(WmsScanPickShell.PAGE_ID, messge);
		}
		/*else if(StringUtils.isEmpty(location)){
			output("pick.container",container);
			location = this.getTextField("scan.location");
			if (StringUtils.isEmpty (location) || "-".equals(location)) {
				this.setStatusMessage("库位号不规范");
			}
			location = location.trim();
			if(MyUtils.OVER.equals(location)){
				messge = "扫描器具标签";
				this.put(WmsScanPickShellMessage.PICK_CODE, pickNo);
				this.forward(WmsScanPickShell.PAGE_ID, messge);
			}
			messge = "扫描物料条码";
			this.put(WmsScanPickShellMessage.PICK_CODE, pickNo);
			this.put(WmsScanPickShellMessage.CONTAINER, container);
			this.put(WmsScanPickShellMessage.LOC_CODE, location);
			this.forward(WmsScanPickShell.PAGE_ID, messge);
		}*/
		else if(StringUtils.isEmpty(itemCode)){
			output("pick.container",container);
			itemCode = this.getTextField("scan.itemCode");
			if (StringUtils.isEmpty (itemCode) || "-".equals(itemCode)) {
				this.setStatusMessage("物料条码不规范");
			}
			itemCode = itemCode.trim();
			if(MyUtils.OVER.equals(itemCode)){
				messge = "扫描器具标签";
				this.put(WmsScanPickShellMessage.PICK_CODE, pickNo);
				this.forward(WmsScanPickShell.PAGE_ID, messge);
			}else if(MyUtils.THIS.equals(itemCode)){
				messge = "扫描拣货单号";
				this.forward(WmsScanPickShell.PAGE_ID, messge);
			}
			/*if(MyUtils.OVER.equals(itemCode)){
				messge = "扫描库位";
				this.put(WmsScanPickShellMessage.PICK_CODE, pickNo);
				this.put(WmsScanPickShellMessage.CONTAINER, container);
				this.forward(WmsScanPickShell.PAGE_ID, messge);
			}else if(MyUtils.THIS.equals(itemCode)){
				messge = "扫描拣货单号";
				this.forward(WmsScanPickShell.PAGE_ID, messge);
			}else if(MyUtils.SECOND.equals(itemCode)){
				messge = "扫描器具标签";
				this.put(WmsScanPickShellMessage.PICK_CODE, pickNo);
				this.forward(WmsScanPickShell.PAGE_ID, messge);
			}*/
			messge = "确认拣货量";
			this.put(WmsScanPickShellMessage.PICK_CODE, pickNo);
			this.put(WmsScanPickShellMessage.CONTAINER, container);
			this.put(WmsScanPickShellMessage.LOC_CODE, location);
			this.put(WmsScanPickShellMessage.ITEM_CODE, itemCode);
			this.forward(WmsScanPickShell.PAGE_ID, messge);
		}else{
			itemCode = (String) this.getParentValue(WmsScanPickShellMessage.ITEM_CODE);
			output("soi.itemCode",itemCode);
			String value = this.getTextField("pick.qty");//this.getNumberField("put.qty");
			Double picQuantity = 0D;
			if(StringUtils.isEmpty(value)){
				picQuantity = Double.MAX_VALUE;
			}else{
				if(MyUtils.OVER.equals(value)){
					messge = "扫描物料条码";
					this.put(WmsScanPickShellMessage.PICK_CODE, pickNo);
					this.put(WmsScanPickShellMessage.CONTAINER, container);
					this.put(WmsScanPickShellMessage.LOC_CODE, location);
					this.forward(WmsScanPickShell.PAGE_ID, messge);
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
			Map<String,String> result = pickRFManager.singlePicQty(pickNo, container, itemCode,picQuantity,"RF扫码拣货",true,WmsWarehouseHolder.getWmsWarehouse().getId());
			messge = result.get(WmsScanPickShellMessage.ERROR_MESG);
			if(!StringUtils.isEmpty(messge)){
				if(messge.equals(WmsScanPickShellMessage.ERROR_ITEM_NULL) //物料明细不存在
						|| messge.equals(WmsScanPickShellMessage.ERROR_ITEM_FINISHED)//物料拣货完成
						|| messge.equals(WmsScanPickShellMessage.ERROR_LOSS)//系统库存不足
						|| messge.equals(WmsScanPickShellMessage.ERROR_SHIP_LOC_NULL)){//备货库位不存在
					this.put(WmsScanPickShellMessage.PICK_CODE, pickNo);
					this.put(WmsScanPickShellMessage.CONTAINER, container);
					this.put(WmsScanPickShellMessage.LOC_CODE, location);
					this.forward(WmsScanPickShell.PAGE_ID, messge);
				}else if(messge.equals(WmsScanPickShellMessage.ERROR_CONTAINER_NULL)){//容器不存在或失效
					this.put(WmsScanPickShellMessage.PICK_CODE, pickNo);
					this.forward(WmsScanPickShell.PAGE_ID, messge);
				}else if(messge.equals(WmsScanPickShellMessage.ERROR_LOC_NULL)){//库位号不存在
					this.put(WmsScanPickShellMessage.PICK_CODE, pickNo);
					this.put(WmsScanPickShellMessage.CONTAINER, container);
					this.forward(WmsScanPickShell.PAGE_ID, messge);
				}else if(messge.equals(WmsScanPickShellMessage.ERROR_PIC_MOVE)
						|| messge.equals(WmsScanPickShellMessage.ERROR_PIC_MOVE_INV)){//失败!拣货量超出可用量
					this.put(WmsScanPickShellMessage.PICK_CODE, pickNo);
					this.put(WmsScanPickShellMessage.CONTAINER, container);
					this.put(WmsScanPickShellMessage.LOC_CODE, location);
					this.put(WmsScanPickShellMessage.ITEM_CODE, itemCode);
					this.forward(WmsScanPickShell.PAGE_ID, messge);
				}else{
					this.setStatusMessage(messge);
				}
			}
			messge = "成功,继续扫描物料";
			this.put(WmsScanPickShellMessage.PICK_CODE, pickNo);
			this.put(WmsScanPickShellMessage.CONTAINER, container);
			this.put(WmsScanPickShellMessage.LOC_CODE, location);
			this.forward(WmsScanPickShell.PAGE_ID, messge);
		}
	}
}
