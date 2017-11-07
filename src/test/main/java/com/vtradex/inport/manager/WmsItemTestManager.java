package com.vtradex.inport.manager;

import java.io.File;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;

@SuppressWarnings("rawtypes")
public interface WmsItemTestManager extends BaseManager {	
	
	@Transactional
	void importItem(File file);

	void importPackageUnit(File file);	
	/**
     * 提交
     * @param file
     */
	@Transactional
	 void commit(List<WmsPackageUnit> objects) ;	
}