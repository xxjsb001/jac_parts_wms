package com.vtradex.wms.server.telnet.shell.putaway;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.telnet.putaway.WmsPutawayRFManager;
import com.vtradex.wms.server.telnet.receiving.WmsReceivingRFManager;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;
/**明细收货上架*/
public class WmsPutAsnDetailShell extends Thorn4BaseShell{
	public static final String PAGE_ID = "wmsPutAsnDetailShell";
	protected static WmsPutawayRFManager wmsPutawayRFManager;
	private WmsReceivingRFManager wmsReceivingRFManager;
	
	public static final String ITEM_CODE = "ITEM_CODE";
	public static final String ITEM_NAME = "ITEM_NAME";
	public static final String LOCATION = "LOCATION";
	public static final String QUANTITY = "QUANTITY";
	
	public static final String ERROR = "ERROR";
	@SuppressWarnings("static-access")
	public WmsPutAsnDetailShell(WmsPutawayRFManager wmsPutawayRFManager,WmsReceivingRFManager wmsReceivingRFManager) {
		this.wmsPutawayRFManager = wmsPutawayRFManager;
		this.wmsReceivingRFManager = wmsReceivingRFManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String messge = "";
		Long detailId = (Long)this.getParentValue(ViewPutAsnDetailShell.DETAIL_ID);
		Long asnId = (Long) this.getParentValue(WmsPutAsnScanShell.ASN_ID);
		Map<String,String> result = wmsPutawayRFManager.getItems(detailId);
		if(result.get(ITEM_CODE)!=null){
			String locationCode = result.get(LOCATION)==null?"-":result.get(LOCATION);
			output("物料编码",result.get(ITEM_CODE));
			output("物料名称",result.get(ITEM_NAME));
			output("库区",locationCode);
			output("计划量",result.get(QUANTITY));
			
			String value = this.getTextField("asn.qty");//this.getNumberField("put.qty");
			Double receivingQuantityBU = 0D;
			if(StringUtils.isEmpty(value)){
				receivingQuantityBU = Double.MAX_VALUE;
			}else{
				if(MyUtils.OVER.equals(value)){
					messge = "选择序号";
					this.put(WmsPutAsnScanShell.ASN_ID, asnId);
					this.forward(ViewPutAsnDetailShell.PAGE_ID,messge);
				}
				
				if(!JavaTools.isNumber(value)){
					messge = "失败!收货量不是正整数";
					this.setStatusMessage(messge);
				}
				receivingQuantityBU = JavaTools.stringToDouble(value);
				if(receivingQuantityBU<0){
					messge = "失败!收货量不可小于0";
					this.setStatusMessage(messge);
				}
			}
			try {
				Map<String,String> results = wmsReceivingRFManager.detailReceiveUp(detailId,receivingQuantityBU,UserHolder.getUser().getId(),"-",locationCode);
				if(!StringUtils.isEmpty(results.get(ERROR))){
					messge = results.get(ERROR);
					this.put(WmsPutAsnScanShell.ASN_ID, asnId);
					this.forward(ViewPutAsnDetailShell.PAGE_ID,messge);
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.reset(e.getMessage());
			}
			//验证收货单明细是否全部确认
			Map<String,String> results = wmsReceivingRFManager.checkAsnAll(asnId);
			if(!StringUtils.isEmpty(results.get(ERROR))){
				messge = "继续扫码";
				this.forward(WmsPutAsnScanShell.PAGE_ID,messge);
			}
			messge = "选择序号";
			this.put(WmsPutAsnScanShell.ASN_ID, asnId);
			this.forward(ViewPutAsnDetailShell.PAGE_ID,messge);
		}else{
			messge = "物料明细不存在";
			this.put(WmsPutAsnScanShell.ASN_ID, asnId);
			this.forward(WmsPutAsnScanShell.PAGE_ID,messge);
		}
	}

}
