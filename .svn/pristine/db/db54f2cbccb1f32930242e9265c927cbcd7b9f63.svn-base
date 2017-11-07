package com.vtradex.wms.server.service.base.pojo;

import org.springframework.dao.DataIntegrityViolationException;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.organization.WmsTransState;
import com.vtradex.wms.server.service.base.WmsTransStateManager;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

public class DefaultWmsTransStateManager extends DefaultBaseManager implements
		WmsTransStateManager {


	public void storeTransState(WmsTransState transState) {
		transState.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
		try {
			commonDao.store(transState);
		}catch (DataIntegrityViolationException e) {
			throw new BusinessException("当前数据已存在,请检查修改后在提交!");
		} 
		catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		
	}

}
