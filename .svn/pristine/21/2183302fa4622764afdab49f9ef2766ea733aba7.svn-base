package com.vtradex.inport;

import java.io.File;

import com.vtradex.inport.manager.WmsOrganizationTestManager;
import com.vtradex.wms.WmsTestCase;

public class ImportWmsOrganizationTest extends WmsTestCase {
	   public void testImportCompany(){		   
		   File file=new File("D:\\wms_organization.xls");
	       WmsOrganizationTestManager wmsOrganizationManager=(WmsOrganizationTestManager) applicationContext.getBean("wmsOrganizationTestManager");
	       wmsOrganizationManager.importCompany(file);
	   }     
}
