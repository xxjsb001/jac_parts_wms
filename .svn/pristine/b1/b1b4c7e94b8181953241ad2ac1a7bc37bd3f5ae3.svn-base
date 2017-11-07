package com.vtradex.wms.server.telnet.shell.putaway;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.component.support.PropertyOptionDisplayer;
import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.thorn.client.utils.StringUtils;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.telnet.putaway.WmsPutawayRFManager;
import com.vtradex.wms.server.telnet.shell.TelnetConstants;

import net.wimpi.telnetd.io.TerminalIO;

/**
 * 货品上架
 *
 * @category Shell
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2015/03/25 02:48:55 $
 */
public class WmsItemPutawayShell extends Thorn4BaseShell {
	
	public static final String PAGE_ID = "wmsItemPutawayShell";
	
	private WmsPutawayRFManager wmsPutawayRFManager;
	
	public WmsItemPutawayShell(WmsPutawayRFManager wmsPutawayRFManager) {
		this.wmsPutawayRFManager = wmsPutawayRFManager;
	}

	protected void mainProcess(Connection connection) throws BreakException, ContinueException, IOException, Exception {
		
		String asnCode = this.getParentContext().get("asnCode").toString();
		output("收货单号",asnCode);
		String barCode = getTextField("barCode");
		if (StringUtils.isEmpty(barCode)) {
			this.setStatusMessage("货品条码必填,0-重新扫描收货单!");
		}
		if("0".equals(barCode)){
			this.forward(WmsASNPutawayShell.PAGE_ID);
		}
		Map<String,Object> result = wmsPutawayRFManager.getUnPutawayItemInfoByAsnAndBarCode(asnCode, barCode,null);
		List<WmsPackageUnit> unitList = wmsPutawayRFManager.getUnPutawayItemUnitByAsnAndBarCode(asnCode, barCode);
		output("货品",result.get("itemCode").toString() + "-" + result.get("itemName").toString());
		if(unitList == null || unitList.size() == 1){
			output("包装",result.get("packageUnit").toString());
		}else if(unitList.size() > 1){
			WmsPackageUnit packageUnit = (WmsPackageUnit)getListField("packageUnit", unitList, new PropertyOptionDisplayer("unit"));
			if(!result.get("packageUnit").toString().equals(packageUnit.getUnit())){
				result = wmsPutawayRFManager.getUnPutawayItemInfoByAsnAndBarCode(asnCode, barCode,packageUnit.getUnit());
			}
		}
		output("待上架数量",result.get("itemQty").toString());
		
		Double putawayQuantity = this.getNumberField("putawayQuantity");
		if (putawayQuantity == null || putawayQuantity.doubleValue() <= 0) {
			this.setStatusMessage("上架数量必填");
		}
		if (putawayQuantity.doubleValue() > Double.valueOf(result.get("itemQty").toString()).doubleValue()) {
			this.setStatusMessage("上架数量不可超过待上架数量");
		}
		
		//创建上架单
		Long moveDocId = (Long)this.getParentContext().get("moveDocId");
		if(moveDocId == null){
			moveDocId = (Long)(this.getContext().get("moveDocId"));
		}
		WmsMoveDoc moveDoc = wmsPutawayRFManager.createWmsMoveDoc(moveDocId,asnCode,
				Long.valueOf(result.get("inventoryId").toString()),
				Long.valueOf(result.get("packageId").toString()),putawayQuantity);
		if(moveDoc == null){
			this.forward(WmsASNPutawayShell.PAGE_ID,"生成上架单失败");
		}
		if(moveDocId == null){
			moveDocId = moveDoc.getId();
		}
		this.context.put("moveDocId", moveDocId);
		this.context.put("asnCode",asnCode);
		if(moveDoc.getPlanQuantityBU() > moveDoc.getAllocatedQuantityBU()){
			//分配失败,手工分配
			output("库位分配失败,请指定上架库位");
			String putawayLocationCode = getTextField("putawayLocationCode");
			try {
				wmsPutawayRFManager.manualAllocate(moveDocId,putawayLocationCode);
			} catch (Exception e) {
				this.getContext().put("moveDocId", moveDocId);
				this.setStatusMessage(e.getMessage());
			}
		}
		String bePutawayNow = this.getTextField("bePutawayNow");
		if (StringUtils.isEmpty(bePutawayNow) || TelnetConstants.YES.equals(bePutawayNow)) {
			this.forward(WmsItemPutawayShell.PAGE_ID);
		}else{
			//生效上架单
			wmsPutawayRFManager.activeMoveDoc(moveDocId);
			this.forward(WmsItemPutawayGoShell.PAGE_ID);
		}
	}
}
