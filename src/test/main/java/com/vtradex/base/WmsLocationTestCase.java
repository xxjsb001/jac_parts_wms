package com.vtradex.base;

import com.vtradex.wms.WmsTestCase;
import com.vtradex.wms.server.service.base.WmsLocationManager;

/**
 * @author: 李炎
 */
public class WmsLocationTestCase extends WmsTestCase {
	
	
	public WmsLocationManager getLocationManager(){
		return (WmsLocationManager)applicationContext.getBean("wmsLocationManager");
	}
	
	public void testLocationExtend(){
		getLocationManager().lockLocationtesting("abc");
	}
}
