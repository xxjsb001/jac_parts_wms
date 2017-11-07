package com.vtradex.wms.server.action;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.wms.server.model.organization.WmsLotRule;
import com.vtradex.wms.server.service.base.WmsItemManager;

public class WmsLotRuleUsedDecision implements ProcessAction {
	protected final WmsItemManager itemManager;
	
	public WmsLotRuleUsedDecision(WmsItemManager itemManager){
		this.itemManager=itemManager;
	}
	
	public String processAction(Object object) {
		WmsLotRule lotRule = (WmsLotRule)object;
		String value = "ENABLED";
		Boolean isContained=itemManager.isContainLotRule(lotRule.getId());
		if(isContained){
			value="Y";
			throw new BusinessException("此规则已被使用，不能失效！");
		}else{
			value="N";
		}
		return value;
	}

}
