package com.vtradex.inport;

import java.io.File;

import com.vtradex.inport.manager.WmsItemTestManager;
import com.vtradex.wms.WmsTestCase;

public class ImportWmsItemTest extends WmsTestCase {
	public void testImport() {
		File file = new File("d:\\wms_item.xls");
		WmsItemTestManager itemManager = (WmsItemTestManager)this.applicationContext.getBean("wmsItemTestManager");
		itemManager.importItem(file);		
	}
}
