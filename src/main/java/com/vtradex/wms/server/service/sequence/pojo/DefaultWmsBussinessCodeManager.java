package com.vtradex.wms.server.service.sequence.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.vtradex.sequence.service.sequence.SequenceGenerater;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;

@SuppressWarnings("unused")
public class DefaultWmsBussinessCodeManager extends DefaultBaseManager 
		implements WmsBussinessCodeManager, ApplicationContextAware {
	private static ApplicationContext ac;
	protected final SequenceGenerater sequenceGenerater;
	private WmsRuleManager wmsRuleManager;
	
	protected final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
	protected final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
	
	public DefaultWmsBussinessCodeManager(SequenceGenerater sequenceGenerater,WmsRuleManager wmsRuleManager) {
		this.sequenceGenerater = sequenceGenerater;
		this.wmsRuleManager = wmsRuleManager;
	}
	
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		ac = arg0;
	}
	
	public String generateLot() {
		return sequenceGenerater.generateSequence(sdf.format(new Date()), 3);
	}
	
	public String generateMoveDocCode(String key) {
		return sequenceGenerater.generateSequence(key + "_" + sdf1.format(new Date()), 3);
	}
	
	public String generateCodeByRule(WmsWarehouse warehouse, String companyName, String document,String type){
		String temp = "";
		
		Map<String, Object> problem = new HashMap<String, Object>();
		
		problem.put("单据名", document);
		problem.put("类型", type);
		problem.put("仓库编码", warehouse.getCode());
		
		Map<String, Object>  result = wmsRuleManager.execute(warehouse == null ? null : warehouse.getName(), companyName, "单号编码规则", problem);
		
		temp = result.get("流水号").toString();
		//temp = StringUtils.removeStart(temp, warehouse.getCode());
		return temp;
	}

	public Map<String,Object> generateLocationCodeByRule(String warehouseName, String zoneCode, 
			Integer zone, Integer line, Integer col,Integer row) {
		Map<String, Object> problem = new HashMap<String, Object>();
		
		problem.put("库区编码", zoneCode);
		problem.put("区", zone);
		problem.put("排", line);
		problem.put("列", col);
		problem.put("层", row);
		
		Map<String, Object>  result = wmsRuleManager.execute(warehouseName, warehouseName, "库位编码规则", problem);	
	
		return result;
	}
	public String getWmsBillDetailCode(String fromType , String supplierCode){
       return this.sequenceGenerater.generateSequence(fromType+supplierCode+sdf.format(new Date()), 4);
	}
}