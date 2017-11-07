package com.vtradex.rule;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.vtradex.thorn.server.util.Constant;
import com.vtradex.wms.WmsTestCase;

/**
 * 
 *
 * @category 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:56 $
 */
public class InvocationRuleTable extends WmsTestCase {
	
	
	public void testInvocationRuleTable() {
		Date invalidDate = new Date();
		String[] binds = new String[]{
				"上海桃浦库",
				Constant.NULL,
				Constant.NULL,
				Constant.NULL,
				Constant.NULL
		};
		
		Object[] objects = new Object[]{"R101_补货_补货规则表"};
		
		List<Map<String,Object>> ruleValues = (List<Map<String, Object>>)this.getIRuleTableLoader().getAllRuleTableDetail(invalidDate, binds, objects);
		
		System.out.println(ruleValues);
	}
}
