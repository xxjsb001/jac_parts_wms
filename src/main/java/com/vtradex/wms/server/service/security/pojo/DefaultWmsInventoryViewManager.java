package com.vtradex.wms.server.service.security.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.service.security.UserManager;
import com.vtradex.wms.client.inventoryviewUI.constant.CT_IV;
import com.vtradex.wms.client.ui.javabean.JB_IV;
import com.vtradex.wms.client.ui.javabean.JB_Location_IV;
import com.vtradex.wms.client.ui.javabean.JB_Location_IV_List;
import com.vtradex.wms.client.ui.javabean.JB_Location_Inventory;
import com.vtradex.wms.client.ui.javabean.JB_Location_RC_IV;
import com.vtradex.wms.client.ui.javabean.JB_Location_RC_IV_List;
import com.vtradex.wms.client.ui.javabean.JB_Region_IV;
import com.vtradex.wms.client.ui.javabean.JB_Zone_IV;
import com.vtradex.wms.client.ui.javabean.JB_Zone_Location_data;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsLocationType;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWarehouseArea;
import com.vtradex.wms.server.service.security.WmsInventoryViewManager;
import com.vtradex.wms.server.utils.DoubleUtils;


public class DefaultWmsInventoryViewManager extends DefaultBaseManager
		implements WmsInventoryViewManager {
	protected final UserManager userManager;
	
	protected int currentMaxLine = 0;
	protected int currentMaxRow = 0;
	protected int currentMaxCol = 0;
	
	public DefaultWmsInventoryViewManager(UserManager userManager) {
		super();
		this.userManager = userManager;
	}
	
	public Map init_WM_IV(Map params) {
		String hql = "SELECT new com.vtradex.wms.client.ui.javabean.JB_IV(" +
				"w.id , w.name," +
				"(SELECT sum(lc.usedRate)/(COUNT(lc.id)*100) FROM WmsLocation lc " +
				"WHERE lc.type=:type AND lc.warehouse.id=w.id Group By lc.warehouse.id)" +
				",w.x_Pos,w.y_Pos,w.image_url) " +
				"FROM WmsWarehouse w Order By w.id";
		List<JB_IV> ivs = commonDao.findByQuery(hql , new String[]{"type"} , new Object[]{WmsLocationType.STORAGE});
		Map<String ,List> result = new HashMap<String,List>();
		result.put(CT_IV.IV_RESULT , ivs);
		return result;
	}

	//update WM_IV
	
		public Map update_WM_IV(Map params) {
			Long wh_id = (Long)params.get(CT_IV.IV_WH_ID);
			Long wh_xPos = (Long)params.get(CT_IV.IV_WH_XPOS);
			Long wh_yPos = (Long)params.get(CT_IV.IV_WH_YPOS);
			WmsWarehouse wh = commonDao.load(WmsWarehouse.class,wh_id);
			wh.setX_Pos(wh_xPos.intValue());
			wh.setY_Pos(wh_yPos.intValue());
			commonDao.store(wh);
			Map result = new HashMap();
			//result.put(CT_IV.IV_WH_ID,wh.getId());
			return result;
		}
		
		public Map update_WM_ZIV(Map params){
			Long wh_zone_id = (Long)params.get(CT_IV.IV_ZONE_ID);
			Long wh_zone_xPos = (Long)params.get(CT_IV.IV_ZONE_XPOS);
			Long wh_zone_yPos = (Long)params.get(CT_IV.IV_ZONE_YPOS);
			WmsWarehouseArea zone = commonDao.load(WmsWarehouseArea.class,wh_zone_id);
			zone.setX_Pos(wh_zone_xPos.intValue());
			zone.setY_Pos(wh_zone_yPos.intValue());
			commonDao.store(zone);
			Map result = new HashMap();
			//result.put(CT_IV.IV_WH_ID,wh.getId());
			return result;
		}
		
		public Map init_Zone_IV(Map params) {
			Long wh_id = (Long)params.get(CT_IV.IV_WH_ID);
			String hql = "SELECT new com.vtradex.wms.client.ui.javabean.JB_Zone_IV(" +
					"zone.id," +
					"zone.code," +
					"zone.name," +
					"(SELECT sum(lc.usedRate)/(COUNT(lc.id)*100) FROM WmsLocation lc " +
					"WHERE lc.type=:type AND lc.warehouseArea.id=zone.id Group By lc.warehouseArea.id)," +
					"zone.x_Pos," +
					"zone.y_Pos ) " +
					"FROM WmsWarehouseArea zone " +
					"WHERE zone.warehouse.id=:id " +
					"Order By zone.id";
			List<JB_Zone_IV> zivs = commonDao.findByQuery(hql , 
					new String[]{"type" , "id"} , new Object[]{WmsLocationType.STORAGE , wh_id});
			Map<String ,List> result = new HashMap<String,List>();
			result.put(CT_IV.IV_RESULT , zivs);
			return result;
		}
		
		public Map init_Region_IV(Map params) {
			Long wh_id = (Long)params.get(CT_IV.IV_WH_ID);
			Long zone_id = (Long)params.get(CT_IV.IV_ZONE_ID);
			String hql = "SELECT new com.vtradex.wms.client.ui.javabean.JB_Region_IV(" +
					"location.zone," +
					"sum(location.usedRate)/(COUNT(location.zone)*100)) from WmsLocation location " +
					"WHERE location.warehouse.id=:id and location.warehouseArea.id=:zoneId " +
					"AND location.type=:type " +
					"GROUP BY location.zone Order By location.zone";
			List<JB_Region_IV> rivs = commonDao.findByQuery(hql , 
					new String[]{"type" , "id" , "zoneId"} , new Object[]{WmsLocationType.STORAGE , wh_id , zone_id});
			Map<String ,List> result = new HashMap<String,List>();
			result.put(CT_IV.IV_RESULT , rivs);
			return result;
		}

		public Map init_Location_IV(Map params) {
			Long zone_id = (Long)params.get(CT_IV.IV_ZONE_ID);
			Integer region_id = Integer.parseInt(params.get(CT_IV.IV_REGION_ID).toString());
			String hql = "SELECT new com.vtradex.wms.client.ui.javabean.JB_Location_IV(" +
					" lc.line,lc.zone,sum(lc.usedRate)/(COUNT(lc.id)*100)) " +
					" FROM WmsLocation lc " +
					" WHERE lc.type=:type AND lc.warehouseArea.id=:id AND lc.line > 0 " +
					" AND lc.zone =:region_id " +
					" Group By lc.line,lc.zone " +
					" Order By lc.line ASC";
			List<JB_Location_IV> livs = commonDao.findByQuery(hql , 
					new String[]{"type" , "id", "region_id"} , new Object[]{WmsLocationType.STORAGE , zone_id, region_id});
			Map<String ,Object> result = new HashMap<String,Object>();
			List<JB_Location_IV_List> ivls = new ArrayList<JB_Location_IV_List>();
			getMaxZoneAndLineByLocation(zone_id);
			for(int i = 1; i <= currentMaxLine ; i++) {
				JB_Location_IV_List ivl = new JB_Location_IV_List();
				ivls.add(ivl);
				JB_Location_IV iv = queryIVByLineAndAisle( i , region_id ,livs);
				if(iv != null) {
					ivl.add(iv);
				}else{
					ivl.add(new JB_Location_IV(i,region_id,-1D));
				}
			}
			JB_Zone_Location_data result2 = new JB_Zone_Location_data(currentMaxLine, 0, 0);
			result.put(CT_IV.IV_RESULT , ivls);
			result.put(CT_IV.IV_RESULT2 , result2);
			return result;
		}
		
		public Map init_Location_RC_IV(Map params) {
			Long zone_id = (Long)params.get(CT_IV.IV_ZONE_ID);
			Integer aisle = Integer.parseInt(params.get(CT_IV.IV_AISLE).toString());
			Integer line = (Integer)params.get(CT_IV.IV_LINE);
			
			String hql = "SELECT lc " +
					"FROM WmsLocation lc " +
					"WHERE lc.type=:type AND lc.warehouseArea.id=:id AND lc.zone=:zone AND lc.line=:line AND lc.layer>0 AND lc.column>0 " +
					"Order By lc.layer DESC , lc.column ASC";
			List<WmsLocation> lcs = commonDao.findByQuery(hql , 
					new String[]{"type" , "id" , "zone" , "line"} , new Object[]{WmsLocationType.STORAGE , zone_id , aisle , line});
			
			List<JB_Location_RC_IV> lrcivs = new ArrayList<JB_Location_RC_IV>();
			getMaxColAndRowByLocation(zone_id, aisle , line);
			for(WmsLocation lc : lcs) {
//				lrcivs.add(lc.construct_JB_Location());
				JB_Location_RC_IV lv=new JB_Location_RC_IV();
				lv.setId(lc.getId());
				lv.setCode(lc.getCode());
				lv.setAisle(lc.getZone());
				lv.setCol(lc.getColumn());
				lv.setLine(lc.getLine());
				lv.setRow(lc.getLayer());
				lv.setRate(lc.getUsedRate()/100);
				lv.setFullRate(100);
				lrcivs.add(lv);
			}
			
			List<JB_Location_RC_IV_List> lrcivls = new ArrayList<JB_Location_RC_IV_List>();
			for(int i = currentMaxRow ; i >= 1 ; i--) {
				JB_Location_RC_IV_List lrcivl = new JB_Location_RC_IV_List();
				lrcivls.add(lrcivl);
				for(int j = 1 ; j <= currentMaxCol ; j++) {
					JB_Location_RC_IV iv = queryIVByRowAndCol( i , j ,lrcivs);
					if(iv != null) {
						lrcivl.add(iv);
					}else{
						lrcivl.add(new JB_Location_RC_IV(i,j,-1D));
					}
				}
			}
			JB_Zone_Location_data result2 = new JB_Zone_Location_data(0 , currentMaxRow, currentMaxCol);
			
			Map<String ,Object> result = new HashMap<String,Object>();
			result.put(CT_IV.IV_RESULT , lrcivls);
			result.put(CT_IV.IV_RESULT2 , result2);
			return result;
		}
		
		@SuppressWarnings("unchecked")
		public Map init_Inventory_Data(Map params) {
			Long loc_id = (Long)params.get(CT_IV.IV_LOCATION_ID);
			String hql = "SELECT new com.vtradex.wms.client.ui.javabean.JB_Location_Inventory(" +
					"inventory.id," +
					"inventory.item.company.name," +
					"inventory.locationCode," +
					"inventory.item.code," +
					"inventory.item.name," +
					"inventory.inventory.itemKey.lot," +
					"(select pu.unit from WmsPackageUnit pu where pu.item.id=inventory.inventory.itemKey.item.id and pu.lineNo=1)," +
					"inventory.inventory.quantityBU," +
					"inventory.inventory.quantity*inventory.inventory.packageUnit.weight," +
					"inventory.inventory.quantity*inventory.inventory.packageUnit.volume," +
					"inventory.inventory.itemKey.lotInfo.storageDate,inventory.pallet) " +
					"FROM WmsInventoryExtend inventory " +
					"WHERE inventory.locationId="+loc_id;
			List<JB_Location_Inventory> lis = commonDao.findByQuery(hql);
			
			//构造合计行记录
			double totalQuantity = 0D;
			double totalWeight = 0D;
			double totalVolume = 0D;
			int totalAges = 0;
			for(JB_Location_Inventory li : lis) {
				li.setQuantity(DoubleUtils.format2Fraction(li.getQuantity()));
				li.setWeight(DoubleUtils.format2Fraction(li.getWeight()));
				li.setVolume(DoubleUtils.format2Fraction(li.getVolume()));
				totalQuantity += li.getQuantity();
				totalWeight += li.getWeight();
				totalVolume += li.getVolume();
				totalAges += li.getAge();
			}
			if(lis.size() > 0 ) {
				JB_Location_Inventory l = new JB_Location_Inventory(0L," "," "," "," "," " +
						"","<font color='red' style='font-weight:bold;'>总计:</font>"
						,DoubleUtils.format2Fraction(totalQuantity) 
						,DoubleUtils.format2Fraction(totalWeight)
						,DoubleUtils.format2Fraction(totalVolume));
				l.setAge(totalAges);
				l.setLp(" ");
				lis.add(l);
			}
			
			Map<String ,List> result = new HashMap<String,List>();
			result.put(CT_IV.IV_RESULT , lis);
			return result;
		}
		
		private JB_Location_RC_IV queryIVByRowAndCol(int row,int col , List<JB_Location_RC_IV> lrcivs) {
			for(JB_Location_RC_IV lrciv : lrcivs) {
				if(lrciv.getRow() == row && lrciv.getCol() == col) {
					return lrciv;
				}
			}
			return null;
		}
		
		private JB_Location_IV queryIVByLineAndAisle( int line , int aisle , List<JB_Location_IV> livs) {
			for(JB_Location_IV liv : livs) {
				if(liv.getLoc_line() == line && liv.getLoc_aisle() == aisle) {
					return liv;
				}
			}
			return null;
		}
		
		private void getMaxZoneAndLineByLocation(Long zone_id) {
			String hql = "SELECT max(lc.line) " +
				"FROM WmsLocation lc " +
				"WHERE lc.type=:type AND lc.warehouseArea.id=:id AND lc.line > 0 AND lc.zone > 0 ";
			Integer lineNum = (Integer) commonDao.findByQueryUniqueResult(hql , new String[]{"type" , "id"} , new Object[]{WmsLocationType.STORAGE , zone_id});
			if (lineNum != null) {	
				currentMaxLine = lineNum;
			} else {
				currentMaxLine = 0;
			}
		}
		
		private void getMaxColAndRowByLocation(Long zone_id, Integer aisle , Integer line) {
			String hql = "SELECT max(lc.column)" +
				"FROM WmsLocation lc " +
				"WHERE lc.type=:type AND lc.warehouseArea.id=:id AND lc.zone=:zone AND lc.line=:line AND lc.layer>0 AND lc.column>0 ";
			Integer columnNum = (Integer) commonDao.findByQueryUniqueResult(hql , new String[]{"type" , "id" ,"zone", "line"} , new Object[]{WmsLocationType.STORAGE , zone_id , aisle , line});
			if (columnNum != null) {	
				currentMaxCol = columnNum;
			} else {
				currentMaxCol = 0;
			}
		
			hql = "SELECT max(lc.layer) " +
				"FROM WmsLocation lc " +
				"WHERE lc.type=:type AND lc.warehouseArea.id=:id AND lc.zone=:zone AND lc.line=:line AND lc.layer>0 AND lc.column>0 ";
			Integer layerNum = (Integer) commonDao.findByQueryUniqueResult(hql , new String[]{"type" , "id" ,"zone", "line"} , new Object[]{WmsLocationType.STORAGE , zone_id , aisle , line});
			if (layerNum != null) {	
				currentMaxRow = layerNum;
			} else {
				currentMaxRow = 0;
			}
		}
}
