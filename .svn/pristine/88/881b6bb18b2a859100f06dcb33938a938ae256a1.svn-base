package com.vtradex.inport;

import java.io.File;

import com.vtradex.inport.manager.WmsItemTestManager;
import com.vtradex.wms.WmsTestCase;

public class ImportPackageUnitTest extends WmsTestCase {
	public void testImportUnit() {
		File file = new File("d:\\wms_package_unit.xls");
		WmsItemTestManager itemManager = (WmsItemTestManager)this.applicationContext.getBean("wmsItemTestManager");
		itemManager.importPackageUnit(file);		
	}
}
