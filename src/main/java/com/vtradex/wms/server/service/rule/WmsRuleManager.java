package com.vtradex.wms.server.service.rule;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.engine.exception.EcadBaseException;
import com.vtradex.thorn.server.service.BaseManager;

/**
 * @author: 李炎
 */
public interface WmsRuleManager extends BaseManager {

	
	/**
	 * 
	 * @param companyName 客户名称
	 * @param referenceRule  客户规则明细表值参考项
	 * @param problem  问题参数
	 * @return 规则引擎处理结果
	 */
	Map<String, Object> execute(String warehouseName,String orgName,String referenceRule,
			Map<String, Object> problem);
	
	
	Map<String, Object> executeRule(String warehouseName, String directoryName,String ruleName,
			Map<String, Object> problem);
	
	String getexecuteMainRule(String orgName,String referenceRule,String warehouseName);
	
	/**
	 * 提取规则表中相关数据
	 * @param orgName
	 * @param referenceRule
	 * @param objs
	 * @return
	 */
	List<Map<String,Object>> getAllRuleTableDetail(String warehouseName,Object... objects);
	
	Object getSingleRuleTableDetail(String warehouseName,Object... objects);
	
	Map<String,Object> getRuleTableDetail(String warehouseName,Object... objects);
	
	List<Map<String,Object>> getMultipleRuleTableDetail(String warehouseName,Object... objects);
	
	
	
	@Transactional()
	void storeRuleExceptionLog(String gourpNo,String process,EcadBaseException ecadBaseException);
	
	@Transactional
	void storeRuleExceptionLog(String process,EcadBaseException ecadBaseException);
	@Transactional
	void changeVersionHashCode();
}
