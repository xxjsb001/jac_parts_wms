package com.vtradex.wms;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.vtradex.rule.server.loader.IRuleTableLoader;
import com.vtradex.rule.server.service.rule.RuleManager;
import com.vtradex.thorn.server.dao.CommonDao;

/**
 * 
 * @author <a href="yan.li@vtradex.com">李炎</a>
 * @description 
 * 
 */

public abstract class WmsTestCase extends AbstractDependencyInjectionSpringContextTests {
	
	protected CommonDao getCommonDao(){
		return (CommonDao)applicationContext.getBean("commonDao");
	}
	
	protected RuleManager getRuleManager() {
		return (RuleManager)applicationContext.getBean("ruleManager");
	}
	
	protected IRuleTableLoader getIRuleTableLoader() {
		return (IRuleTableLoader)applicationContext.getBean("ruleTableLoader");
	}
	
	
	protected String[] getConfigLocations() {
		return new String[]{
				"classpath:dataSourceContext.xml",
	            "classpath*:daoContext.xml",
				"classpath*:sequenceContext.xml",
	            "classpath*:serviceContext.xml",
				"classpath*:actionContext.xml",
				"classpath*:validateContext.xml",
				"classpath*:formatContext.xml",
				"classpath*:configContext.xml",
				"classpath*:tableContext.xml",
				"classpath*:valueListContext.xml",
				"classpath*:serverList.xml",
				"classpath:dbList.xml",
				"classpath:overrideContext.xml",
				"classpath*:messageContext.xml"
		};
	}

}


