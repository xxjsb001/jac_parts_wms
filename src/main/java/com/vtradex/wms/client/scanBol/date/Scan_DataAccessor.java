package com.vtradex.wms.client.scanBol.date;

import java.util.HashMap;
import java.util.Map;

import com.vtradex.thorn.client.data.DataAccessor;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.wms.client.scanBol.EditWmsScanBol;
import com.vtradex.wms.client.scanBol.businessObject.BusinessNode;
import com.vtradex.wms.client.scanPickOver.EditScanPickOver;

public class Scan_DataAccessor extends DataAccessor{
	public Map result = new HashMap();
	public Scan_DataAccessor(IMessagePage page) {
		super(page);
	}
	/** 扫码核单发运 yc.min*/
	public void getWmsScanBol(Map params) {
		this.remoteCall(EditWmsScanBol.INFO, EditWmsScanBol.SAVE_MANAGER, EditWmsScanBol.SAVE_METHOD, params);
	}
	/** 备料交接_容器校验 yc.min*/
	public void getWmsScanContainer(Map params) {
		this.remoteCall(EditScanPickOver.INFO, EditScanPickOver.SAVE_MANAGER, EditScanPickOver.SAVE_METHOD_C, params);
	}
	public void onSuccess(String message, Map result) {
		if(EditWmsScanBol.INFO.equals(message)){
			this.result = result;
			this.sendMessage(result.get(BusinessNode.MSG).toString());
		}else if(EditScanPickOver.INFO.equals(message)){
			this.result = result;
			this.sendMessage(result.get(BusinessNode.MSG).toString());
		}
	}

}
