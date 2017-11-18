package com.vtradex.wms.server.telnet.shell.putaway;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.putaway.WmsPutawayRFManager;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;
/**扫码上架*/
public class WmsASNPutScanShell extends Thorn4BaseShell{
	public static final String PAGE_ID = "wmsASNPutScanShell";
	public static final String ASN_ID = "ASN_ID";
	public static final String LOC_ID = "LOC_ID";
	public static final String DETAIL_ID = "DETAIL_ID";
	
	public static final String LOC_CODE = "LOC_CODE";
	public static final String ITEM_CODE = "ITEM_CODE";
	
	protected static WmsPutawayRFManager wmsPutawayRFManager;
	@SuppressWarnings("static-access")
	public WmsASNPutScanShell(WmsPutawayRFManager wmsPutawayRFManager) {
		this.wmsPutawayRFManager = wmsPutawayRFManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String messge = "操作成功,请继续"+MyUtils.enter;
		Long asnId = (Long)this.getParentValue(ASN_ID);
		Long locId = (Long)this.getParentValue(LOC_ID);
		Long detailId = (Long)this.getParentValue(DETAIL_ID);
		
		if(asnId==null){
			String asnCode = this.getTextField("lotRule.soi");
			if (StringUtils.isEmpty (asnCode) || "-".equals(asnCode)) {
				this.setStatusMessage("收货单号不规范");
			}
			asnCode = asnCode.trim();
			if(MyUtils.OVER.equals(asnCode)){
				forward(ShellFactory.getMainShell());
			}
			asnId = wmsPutawayRFManager.findAsnId(asnCode);
			if(asnId==null || asnId == 0L){
				messge = "失败!单号不存在:"+MyUtils.enter;
				messge += asnCode;
				this.forward(WmsASNPutScanShell.PAGE_ID,messge);
			}else{
				messge = "扫描库位";
				this.put(ASN_ID, asnId);
				this.forward(WmsASNPutScanShell.PAGE_ID,messge);
			}
		}else if(locId==null){
			String locCode = this.getTextField("soi.locCode");
			if (StringUtils.isEmpty (locCode) || "-".equals(locCode)) {
				this.setStatusMessage("失败!库位号不规范");
			}
			locCode = locCode.trim();
			if(MyUtils.OVER.equals(locCode)){
				messge = "扫描收货单号";
				this.forward(WmsASNPutScanShell.PAGE_ID,messge);
			}
			locId = wmsPutawayRFManager.findLoc(locCode);
			if(locId==null || locId == 0L){
				messge = "失败!库位号不存在:"+MyUtils.enter;
				messge += locCode;
				this.put(ASN_ID, asnId);
				this.forward(WmsASNPutScanShell.PAGE_ID,messge);
			}else{
				messge = "请扫码物料条码";
				this.put(ASN_ID, asnId);
				this.put(LOC_ID, locId);
				this.forward(WmsASNPutScanShell.PAGE_ID,messge);
			}
		}else if(detailId==null){
			String itemCode = this.getTextField("soi.itemCode");
			if (StringUtils.isEmpty (itemCode) || "-".equals(itemCode)) {
				this.setStatusMessage("失败!物料码不规范");
			}
			itemCode = itemCode.trim();
			if(MyUtils.OVER.equals(itemCode)){
				messge = "扫描库位";
				this.put(ASN_ID, asnId);
				this.forward(WmsASNPutScanShell.PAGE_ID,messge);
			}
			detailId = wmsPutawayRFManager.findDetail(itemCode, asnId);
			if(detailId==null || detailId == 0L){
				messge = "失败!明细不存在:"+MyUtils.enter;
				messge += itemCode;
				this.setStatusMessage(messge);
			}else{
				messge = "请录入上架量";
				this.put(ASN_ID, asnId);
				this.put(LOC_ID, locId);
				this.put(DETAIL_ID, detailId);
				this.put(ITEM_CODE, itemCode);
				this.forward(WmsASNPutScanShell.PAGE_ID,messge);
			}
		}else{
			String itemCode = (String) this.getParentValue(ITEM_CODE);
			output("soi.itemCode",itemCode);
			String value = this.getTextField("put.qty");//this.getNumberField("put.qty");
			Double putQuantity = 0D;
			if(StringUtils.isEmpty(value)){
				putQuantity = Double.MAX_VALUE;
			}else{
				if(!JavaTools.isNumber(value)){
					messge = "失败!上架量不是数字";
					this.setStatusMessage(messge);
				}
				putQuantity = JavaTools.stringToDouble(value);
			}
			if(putQuantity<=0){
				messge = "失败!上架量不可小于等于0";
				this.setStatusMessage(messge);
			}
			String error = wmsPutawayRFManager.putAway(detailId, putQuantity, locId);
			if(MyUtils.SUCCESS.equals(error)){
				messge = "成功!继续扫码上架";
				this.put(ASN_ID, asnId);
				this.put(LOC_ID, locId);
				this.forward(WmsASNPutScanShell.PAGE_ID,messge);
			}else{
				this.setStatusMessage(error);
			}
		}
	}
}
