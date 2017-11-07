package com.vtradex.rule;

import com.vtradex.rule.server.service.rule.RuleManager;
import com.vtradex.wms.WmsTestCase;

/**
 * @author: 李炎
 */
public class RuleTableTestCase extends WmsTestCase {
	
	public RuleManager getRuleManager(){
		return (RuleManager) applicationContext.getBean("ruleManager");
	}
	
	public void testRuleTable(){
		getRuleManager().refreshVersionHashCode();
	}
}
