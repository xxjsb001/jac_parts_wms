package com.vtradex.wms.server.service.security.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.vtradex.thorn.client.ui.table.RowData;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.thorn.server.security.acegi.holder.SecurityContextHolder;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.service.security.UserManager;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsLocationType;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWorkArea;
import com.vtradex.wms.server.service.security.WmsWarehouseManager;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

@SuppressWarnings("unchecked")
public class DefaultWmsWarehouseManager extends DefaultBaseManager implements WmsWarehouseManager {
	protected  final UserManager userManager;
	
	public DefaultWmsWarehouseManager(UserManager userManager) {
		super();
		this.userManager = userManager;
	}
	
	@Override
	public <T extends Entity> List<T> load(Class<T> clazz, List<Long> ids) {
		return null;
	}
	public WmsWarehouse getDefaultLoginWmsWarehouse() {
		return getWmsAvailableWarehousesByUserId().get(0);
	}
	
	public Map getThisWarehouse(Map m) {
		Map map = new HashMap();
		
	    map.put("1", WmsWarehouseHolder.getWmsWarehouse().getName());
	    map.put("userId", UserHolder.getUser().getId());
	    map.put("userName", UserHolder.getUser().getName());
	    
		return map;
	}
	
	public Map getWmsWareHousesForThorn(Map map){
		Map result = new HashMap();
		
		List<WmsWarehouse> warehouses = getWmsAvailableWarehousesByUserId(UserHolder.getUser().getId());		
		List rowDatas = new ArrayList();
		
		for (WmsWarehouse warehouse : warehouses) {
			RowData rowData = new RowData();
			
			rowData.addColumnValue(warehouse.getId());
			rowData.addColumnValue(warehouse.getName());
			
			rowDatas.add(rowData);
		}
		
		result.put("wareHouses.list", rowDatas);
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<WmsWarehouse> getWmsAvailableWarehousesByUserId() {
		String hql = SecurityContextHolder.getSecurityFilterHql("switchWareHousePage.loadWarehouseDefault");
		
		if (StringUtils.isEmpty(hql)) {
			hql = "FROM WmsWarehouse wh WHERE 1=1 AND wh.status = 'ENABLED'";
		}
		
		return commonDao.findByQuery(hql);
	}
	//根据用户仓库关系映射表来找,默认进入优先级最高的仓库
	public List<WmsWarehouse> getWmsAvailableWarehousesByUserId(Long userId) {
		String hql = "";
		if(userId == 1){
			hql = "FROM WmsWarehouse wh WHERE 1=1 AND wh.status = 'ENABLED'";
		}else{
			hql = "select w.warehouse from WmsWarehouseAndUser w "
					+ "where w.user.id="+userId +"order by priority asc";
		}
		return commonDao.findByQuery(hql);
	}
	
	public WmsLocation getCountLocationByWarehouse(WmsWarehouse warehouse) {
		String hql = "FROM WmsLocation location " +
				" WHERE location.type = :type " + 
				" AND location.warehouse.id=:warehouseId " +
				" AND location.status = 'ENABLED' " +
				" ORDER BY location.code";
		List<WmsLocation> countLocations = commonDao.findByQuery(hql,
				new String[]{"type","warehouseId"},new Object[]{WmsLocationType.COUNT, 
				warehouse.getId()});
		if (countLocations == null || countLocations.size()<=0) {
			return null;
		}
		return countLocations.get(0);
	}

	public List<WmsWarehouse> getWareHouse(Long userID) {
		
		return commonDao.findByQuery("FROM WmsWarehouse wh WHERE 1=1 AND wh.status = 'ENABLED' " +
				"AND wh.id IN(select worker.warehouse.id from WmsWorker worker where worker.user.id=:userId)",new String []{"userId"},new Object[]{userID});
	}
}