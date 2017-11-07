package com.vtradex.rule;


import java.util.HashMap;
import java.util.Map;

import com.vtradex.engine.EcadLauncher;
import com.vtradex.engine.groovy.Launcher;
import com.vtradex.engine.spring.ruleLoader.RuleParserReader;
import com.vtradex.engine.utils.ParenOperateUtil;
import com.vtradex.wms.WmsTestCase;

/**
 * @author <a href="mailto:yan.li@vtradex.com">李炎</a>
 * @version
 * @description
 */
public class RuleParserTest extends WmsTestCase{
	
	public void testBooleanParser() throws Exception{
		String rule = "$费率=查表('里程数','上海','长沙')\r\n" +
		"$里程数=单值查表('里程数','里程数','上海','长沙')\r\n" +
		"$订单1=数据查询('订单','ordercode',代码)\r\n" +
		"$订单2=单数据查询('订单','序号','ordercode',代码)\r\n" ;
		
		rule = ParenOperateUtil.formatParen(rule);
		RuleParserReader reader = new RuleParserReader(rule);
		assertNotNull(reader.read());
		System.out.println(reader.read());
		System.out.println("子函数如下：");
		System.out.println(reader.readSubFunctions());
		
		Map problem = new HashMap();
		problem.put("代码", "aaaaaaa");
		EcadLauncher ecadLauncher = (EcadLauncher)applicationContext.getBean("proxyEcadLauncherFactory");
		Launcher launcher = ecadLauncher.createLauncher(rule , "");
		launcher.setBeanFactory(applicationContext);
		System.out.println(launcher.calculate(problem));
//		rule = "$DO=DO\r\n" +
//			   "$DO.发生时间='XXXX'\r\n";
//		rule = ParenOperateUtil.formatParen(rule);
//		reader = new RuleParserReader(rule);
//		assertNotNull(reader.read());
//		
//		System.out.println(reader.read());
//		System.out.println(reader.readSubFunctions());
	}
	
//	public void testAssignParser() throws Exception {
//		String rule = "((承运商==\"bbb\" 且 委托方==\"cccc\"))\r\n";
//		rule = ParenOperateUtil.formatParen(rule);
//		RuleParserReader reader = new RuleParserReader(rule);
//		assertNotNull(reader.read( ));
//		System.out.println(reader.read());
//	}
}
