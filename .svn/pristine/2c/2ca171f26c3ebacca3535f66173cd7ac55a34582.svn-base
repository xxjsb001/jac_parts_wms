package com.vtradex.wms.server.telnet.shell.receiving;

import java.io.IOException;

import net.wimpi.telnetd.net.Connection;

import org.apache.commons.lang.StringUtils;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.wms.server.telnet.base.WmsBaseModelRFManager;
import com.vtradex.wms.server.telnet.receiving.WmsReceivingRFManager;
import com.vtradex.wms.server.telnet.shell.CustomBaseShell;
import com.vtradex.wms.server.utils.MyUtils;

/**
 * 
 *
 * @category Shell
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:55 $
 */
public class WmsASNDetailShell extends CustomBaseShell {
	
	public static final String PAGE_ID = "wmsASNDetailShell";
	private WmsReceivingRFManager wmsReceivingRFManager;
	
	public WmsASNDetailShell(WmsBaseModelRFManager wmsBaseModeRFManager, WmsReceivingRFManager wmsReceivingRFManager) {
		super(wmsBaseModeRFManager);
		this.wmsReceivingRFManager = wmsReceivingRFManager;
	}

	@Override
	protected void mainProcess(Connection connection) throws BreakException, ContinueException, IOException, Exception {
		
		/*Object asnIdObj = this.getParentContext().get("asn.id");
		if (asnIdObj == null) {
			this.forward(WmsASNListShell.PAGE_ID, "请重新选择收货单");
		}
		
		String barCode = this.getTextField("barCode");
		if (StringUtils.isEmpty(barCode)) {
			this.setStatusMessage("货品条码必填.");
		}*/
		
		String asnCode = this.getTextField("lotRule.soi");
		if (StringUtils.isEmpty (asnCode)) {
			this.setStatusMessage("ASN号必填");
		}
		String messge = "操作成功,请继续(..返回主菜单;...退出登录)"+MyUtils.enter;
		Long asnId = wmsReceivingRFManager.findAsnId(asnCode);
		if(asnId==null || asnId == 0L){
			messge = "失败!找不到符合条件的ASN号";
			messge += asnCode;
			this.setStatusMessage(messge);
		}
		this.put("asnId", asnId);
		this.forward(WmsASNListShell.PAGE_ID, "请选择ASN明细");
//		String detailIdStr = this.getTextField("detail.id");
//		Long detailId = Long.valueOf(detailIdStr);
//		WmsASNDetailDTO dto = null;
//		try {
//			dto = wmsReceivingRFManager.getWmsASNDetailDTO(detailId);
//		} catch (BusinessException be) {
//			be.printStackTrace();
//			this.forward(WmsASNListShell.PAGE_ID, be.getMessage());
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			this.forward(WmsASNListShell.PAGE_ID, "请重新选择ASN");
//		}
//		
//		this.output("itemName", dto.getItemCode() + "-" + dto.getItemName());
//		this.output("expectedQuantityBU", dto.getExpectedQuantityBU().toString());
//		this.output("locationCode", dto.getLocationCode());
//		
//		WmsPackageUnit packageUnit = (WmsPackageUnit)getListField("packageUnit", dto.getPackageUnitList(), new PropertyOptionDisplayer("unit"));
//		if (packageUnit != null) {
//			dto.setCurrentPackageUnit(packageUnit.getId());
//		}
//		
//		Double receivingQuantityBU = this.getNumberField("receivingQuantityBU");
//		if (receivingQuantityBU == null || receivingQuantityBU.doubleValue() == 0) {
//			this.setStatusMessage("数量必填");
//		}
//		if (receivingQuantityBU.doubleValue() > dto.getExpectedQuantityBU()) {
//			this.setStatusMessage("不能大于期待收货数量");
//		}
//		dto.setReceivingQuantityBU(receivingQuantityBU);
//		
//		if (!dto.getStatusList().isEmpty()) {
//			WmsItemState itemState = (WmsItemState)getListField("itemState", dto.getStatusList(), new PropertyOptionDisplayer("name"));
//			if (itemState != null) {
//				dto.setItemStateId(itemState.getId());
//			}
//		}
//		
//		getComplexByLotInfo(dto.getLotRule(), dto.getLotInfo());
//		if (dto.getLotRule().getTrackSOI()) {
//			dto.getLotInfo().setSoi(dto.getAsnCode());
//		}
//		
//		try {
//			wmsReceivingRFManager.detailReceive(dto);
//		} catch (RFFinishException rfe) {
//			this.forward(WmsASNListShell.PAGE_ID, "收货完成");
//		} catch (BusinessException be) {
//			String msg = LocalizedMessage.getLocalizedMessage(be.getMessage(), UserHolder.getReferenceModel(), UserHolder.getLanguage());
//			this.reset(msg);
//		} catch (Exception ex) {
//			this.reset("收货失败请重试");
//		}
//		this.reset("ASN明细收货成功.");
	}
}
