package com.vtradex.wms.server.telnet.shell.putaway;

import java.io.IOException;
import java.util.List;

import net.wimpi.telnetd.net.Connection;

import org.apache.commons.lang.StringUtils;

import com.vtradex.kangaroo.component.support.PropertyOptionDisplayer;
import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.receiving.WmsASNDetail;
import com.vtradex.wms.server.telnet.dto.WmsASNDetailDTO;
import com.vtradex.wms.server.telnet.exception.RFFinishException;
import com.vtradex.wms.server.telnet.putaway.WmsPutawayRFManager;

/**
 * 托盘上架
 *
 * @category Shell
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2015/03/25 02:48:55 $
 */
public class WmsPalletPutawayShell extends Thorn4BaseShell {
	
	public static final String PAGE_ID = "wmsPalletPutawayShell";
	
	private WmsPutawayRFManager wmsPutawayRFManager;
	
	public WmsPalletPutawayShell(WmsPutawayRFManager wmsPutawayRFManager) {
		this.wmsPutawayRFManager = wmsPutawayRFManager;
	}

	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		
		String asnCode = this.getParentContext().get("asnCode").toString();
		output("收货单号",asnCode);
		
		String barCode = this.getTextField("barCode");
		if (StringUtils.isEmpty(barCode)) {
			this.setStatusMessage("货品条码必填,0-重新扫描收货单!");
		}
		if("0".equals(barCode)){
			this.forward(WmsASNPutawayByPalletShell.PAGE_ID);
		}
		this.put("barCode", barCode);
		
		List<WmsPackageUnit> packages = null;
		try {
			packages = wmsPutawayRFManager.getPackageUnitList(asnCode, barCode);
		} catch (BusinessException be) {
			this.setStatusMessage(be.getMessage());
		}
		WmsPackageUnit packageUnit = null;
		if(packages == null || packages.size() == 1){
			this.output("packageUnitName",packages.get(0).getUnit());
			packageUnit = packages.get(0);
		}else {
			packageUnit = (WmsPackageUnit)getListField("packageUnitName", packages, new PropertyOptionDisplayer("unit"));
		}
		WmsASNDetailDTO detailDTO = null;
		try {
			detailDTO = wmsPutawayRFManager.getWmsASNDetailDTO(asnCode, barCode, packageUnit.getId());
		} catch (BusinessException be) {
			this.reset(be.getMessage());
		}
		
		String resultStr = "";
		try {
			resultStr = wmsPutawayRFManager.validatePalletRule(detailDTO.getAsnDetailId());
		} catch (BusinessException be) {
			this.reset(be.getMessage());
		}
		
		if(!StringUtils.isEmpty(resultStr)) {
			this.output("palletRule", resultStr);
		} else {
			this.output("palletRule", "码托规则未设置");
		}

		this.output("itemName", detailDTO.getItemCode()+"-"+detailDTO.getItemName());
		this.output("packageQuantity", detailDTO.getPackageQuantity().toString());
		String palletNum = this.getTextField("palletNum");
		if (StringUtils.isEmpty(palletNum)) {
			this.setStatusMessage("每托数量必填！");
		}
		this.context.put("palletNum", palletNum);
		
		WmsMoveDoc moveDoc = null;
		if(!palletNum.equals("0")) {
			try {
				moveDoc = wmsPutawayRFManager.manualPalletAndAutoCreateMoveDoc(detailDTO.getAsnDetailId(), palletNum, detailDTO.getItemCode(), null);
			} catch (RFFinishException be) {
				output("提示","没找到上架规则，请手工输入上架库位！");
				String putawayLocationCode = this.getTextField("putawayLocationCode");
				if (StringUtils.isEmpty(putawayLocationCode)) {
					this.setStatusMessage("请输入上架库位！");			
				}
				if(putawayLocationCode.equals("0")) {
					this.remove("barCode");
					this.remove("palletNum");
					this.refresh();
				}
				try {
					moveDoc = wmsPutawayRFManager.manualPalletAndAutoCreateMoveDoc(detailDTO.getAsnDetailId(), palletNum, detailDTO.getItemCode(), putawayLocationCode);
				} catch (Exception e) {
					this.setStatusMessage(e.getMessage());
				}
			} 
			catch (BusinessException be) {
				this.setStatusMessage(be.getMessage());
			}
		}
		else {
			this.remove("barCode");
			this.remove("palletNum");
			this.refresh();
		}
		this.remove("barCode");
		this.remove("palletNum");
		if(moveDoc == null){
			this.forward(WmsASNPutawayByPalletShell.PAGE_ID,"生成上架单失败");
		}
		Long moveDocId = moveDoc.getId();
		this.context.put("moveDocId", moveDocId);
		this.context.put("asnCode",asnCode);
		this.forward(WmsPalletPutawayGoShell.PAGE_ID);
	}
}
