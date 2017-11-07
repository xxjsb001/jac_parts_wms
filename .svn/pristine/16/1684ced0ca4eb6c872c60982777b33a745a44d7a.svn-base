package com.vtradex.wms.client.inventoryviewUI.data;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vtradex.thorn.client.data.DataAccessor;
import com.vtradex.thorn.client.message.IMessagePage;
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

public class Page_IV_DataAccessor extends DataAccessor{
	
	/** 消息句柄--初始化库存可视化 */
	public static final String MSG_INIT_IV_DATA = "msg_initIVDatas";
	public static final String MSG_INIT_ZONE_DATAS = "init_zone_datas";
	public static final String MSG_INIT_REGION_DATAS = "init_region_datas";
	public static final String MSG_INIT_LOCATION_DATAS = "msg_init_location_datas";
	public static final String MSG_INIT_LOCATION_RC_DATAS = "msg_init_location_rc_datas";
	public static final String MSG_INIT_INVENTORY_DATAS = "msg_init_inventory_datas";
	
	public static final String MSG_CLOSE_ALL_COLUMN_CHART = "msg_close_charts";
	public static final String MSG_REFRESH_NAVIGATE_PANEL = "msg_refresh_navigate";
	public static final String MSG_OPEN_WAREHOUSE_IMAGE_PANEL = "msg_open_warehouse_panel";
	public static final String MSG_OPEN_ZONE_IMAGE_PANEL = "msg_open_zone_panel";
	public static final String MSG_OPEN_LOCATION_IMAGE_PANEL = "msg_open_location_panel";
	
	public static final String MSG_UPDATE_IV_DATA = "msg_updateIVData";
	public static final String MSG_UPDATE_ZIV_DATA = "msg_updateZIVData";
	
	/**
	 * 库位-区
	 */
	public static final String MSG_OPEN_REGION_IMAGE_PANEL = "msg_open_region_panel";
	
	/** 所有仓库的容积率 */
	private List<JB_IV> ivs;
	/** 所有库区的容积率 */
	private List<JB_Zone_IV> zivs;
	
	/** 所有区的容积率 */
	private List<JB_Region_IV> rivs;
	
	/** 所有库位道排的容积率 */
	private List<JB_Location_IV_List> livs;
	/** 所有库位层列的容积率 */
	private List<JB_Location_RC_IV_List> lrivs;
	/** 所有库存 */
	private List<JB_Location_Inventory> lis;
	/** 当前选中的仓库 */
	private JB_IV cur_JB_IV;
	/** 当前选中的库区 */
	private JB_Zone_IV cur_JB_Zone_IV;
	/** 当前选中的区 */
	private JB_Region_IV cur_JB_Region_IV;
	
	private JB_Zone_Location_data zld;
	
	public JB_Zone_Location_data getZld() {
		return zld;
	}
	public void setZld(JB_Zone_Location_data zld) {
		this.zld = zld;
	}
	
	
	/** 当前选中的库位 */
	private JB_Location_RC_IV cur_JB_Location_RC_IV;
	
	public JB_Location_RC_IV getCur_JB_Location_RC_IV() {
		return cur_JB_Location_RC_IV;
	}
	public void setCur_JB_Location_RC_IV(JB_Location_RC_IV cur_JB_Location_RC_IV) {
		this.cur_JB_Location_RC_IV = cur_JB_Location_RC_IV;
	}
	public Page_IV_DataAccessor(IMessagePage page) {
		super(page);
	}
	/** 远程调用后台方法获取库存可视化数据 */
	public void initIVDatas(){
		this.remoteCall(MSG_INIT_IV_DATA , CT_IV.IV_MANAGER , CT_IV.IV_INIT_DATA , new HashMap());
	}
	/** 远程调用后台方法改变库存可视化数据中的坐标值 */
	public void updateIVData(){
		this.remoteCall(MSG_UPDATE_IV_DATA,CT_IV.IV_MANAGER,CT_IV.IV_UPDATE_DATA,construct_WarehousePos_Param());
	}
	public void updateZIVData(){
		this.remoteCall(MSG_UPDATE_ZIV_DATA,CT_IV.IV_MANAGER,CT_IV.ZIV_UPDATE_DATA,construct_ZonePos_Param());
	}
	/** 异步获取后台的区容积率 */
	public void initRegionIvDatas() {
		this.remoteCall(MSG_INIT_REGION_DATAS,CT_IV.IV_MANAGER , CT_IV.IV_INIT_REGION_DATA ,construct_Region_Param());
	}
	
	/** 异步获取后台的库区容积率 */
	public void initZoneIvDatas() {
		this.remoteCall(MSG_INIT_ZONE_DATAS,CT_IV.IV_MANAGER , CT_IV.IV_INIT_ZONE_DATA ,construct_Zone_Param());
	}
	
	/** 异步获取后台的库位排容积率 */
	public void initLocationIvDatas() {
		this.remoteCall(MSG_INIT_LOCATION_DATAS,CT_IV.IV_MANAGER , CT_IV.IV_INIT_LOCATION_DATA ,construct_Location_Param());
	}
	
	/** 异步获取后台的库位层列容积率 */
	public void initLocationIvRCDatas(Map params) {
		this.remoteCall(MSG_INIT_LOCATION_RC_DATAS,CT_IV.IV_MANAGER , CT_IV.IV_INIT_LOCATION_RC_DATA ,params);
	}
	
	/** 异步获取后台的库存信息 */
	public void initInventoryDatas(Map params) {
		this.remoteCall(MSG_INIT_INVENTORY_DATAS,CT_IV.IV_MANAGER , CT_IV.IV_INIT_INVENTORY_DATA ,params);
	}
	
	/** 设置当前正在操作的仓库容积对象 */
	public void setCur_JB_IV(JB_IV cur_JB_IV) {
		this.cur_JB_IV = cur_JB_IV;
	}
	
	public void setCur_JB_IV_POS(int cur_JB_IV_XPOS,int cur_JB_IV_YPOS){
		this.cur_JB_IV.setX_Pos(cur_JB_IV_XPOS);
		this.cur_JB_IV.setY_Pos(cur_JB_IV_YPOS);
	}
	
	public void setCur_JB_Zone_IV(JB_Zone_IV ziv) {
		this.cur_JB_Zone_IV = ziv;
	}
	
	public void setCur_JB_Region_IV(JB_Region_IV riv) {
		this.cur_JB_Region_IV = riv;
	}
	
	public void setCur_JB_Zone_IV_POS(int cur_JB_Zone_IV_XPOS,int cur_JB_Zone_IV_YPOS){
		this.cur_JB_Zone_IV.setLoc_xPos(cur_JB_Zone_IV_XPOS);
		this.cur_JB_Zone_IV.setLoc_yPos(cur_JB_Zone_IV_YPOS);
	}
	
	public JB_Zone_IV getCur_JB_Zone_IV(){
		return this.cur_JB_Zone_IV;
	}
	
	public JB_Region_IV getCur_JB_Region_IV(){
		return this.cur_JB_Region_IV;
	}
	
	public JB_IV current_JB_IV(){
		return this.cur_JB_IV;
	}
	
	public List<JB_IV> all_IV() {
		return this.ivs;
	}
	
	public List<JB_Zone_IV> all_Zone_IV() {
		return this.zivs;
	}
	
	public List<JB_Region_IV> all_Region_IV(){
		return this.rivs;
	}
	
	public List<JB_Location_IV_List> all_Location_IV(){
		return this.livs;
	}
	
	public List<JB_Location_RC_IV_List> all_Location_RC_IV(){
		return this.lrivs;
	}
	
	public List<JB_Location_Inventory> all_Location_Inv(){
		return this.lis;
	}
	
	public JB_Location_IV getCur_JB_Location_IV(int row ,int col) {
		return this.livs.get(row).get(col);
	}
	
	//param of the current warehousePos param
	public Map construct_WarehousePos_Param(){
		Map<String,Long> param = new HashMap<String,Long>();
		param.put(CT_IV.IV_WH_ID , this.cur_JB_IV.getIv_wh_id());
		
		Integer itx = new Integer(this.cur_JB_IV.getX_Pos());
		Long lox = new Long(itx.longValue());
		
		Integer ity = new Integer(this.cur_JB_IV.getY_Pos());
		Long loy = new Long(ity.longValue());
		
		param.put(CT_IV.IV_WH_XPOS,lox);
		param.put(CT_IV.IV_WH_YPOS,loy);
	    return param;
		
	}
   //param of zone pos 
	public Map construct_ZonePos_Param(){
		Map<String,Long> param = new HashMap<String,Long>();
		param.put(CT_IV.IV_ZONE_ID , this.cur_JB_Zone_IV.getLoc_id());
		
		Integer itx = new Integer(this.cur_JB_Zone_IV.getLoc_xPos());
		Long lox = new Long(itx.longValue());
		
		Integer ity = new Integer(this.cur_JB_Zone_IV.getLoc_yPos());
		Long loy = new Long(ity.longValue());
		
		param.put(CT_IV.IV_ZONE_XPOS,lox);
		param.put(CT_IV.IV_ZONE_YPOS,loy);
	    return param;
		
	}
	
	public Map construct_Zone_Param() {
		Map<String,Long> param = new HashMap<String,Long>();
		param.put(CT_IV.IV_WH_ID , this.cur_JB_IV.getIv_wh_id());
		return param;
	}
	
	public Map construct_Region_Param() {
		Map<String,Long> param = new HashMap<String,Long>();
		param.put(CT_IV.IV_WH_ID , this.cur_JB_IV.getIv_wh_id());
		param.put(CT_IV.IV_ZONE_ID , this.getCur_JB_Zone_IV().getLoc_id());
		return param;
	}
	
	private Map construct_Location_Param(){
		Map<String,Long> params = new HashMap<String,Long>();
		params.put(CT_IV.IV_ZONE_ID , this.getCur_JB_Zone_IV().getLoc_id());
		params.put(CT_IV.IV_REGION_ID , Long.parseLong(this.getCur_JB_Region_IV().getRegion_code()));
		return params;
	}

	@SuppressWarnings("unchecked")
	public void onSuccess(String message, Map result) {
		if(MSG_INIT_IV_DATA.equals(message)) {
			this.ivs = (List) result.get(CT_IV.IV_RESULT);
			this.sendMessage(MSG_INIT_IV_DATA);
		}else if(MSG_INIT_ZONE_DATAS.equals(message)) {
			this.zivs = (List)result.get(CT_IV.IV_RESULT);
			this.sendMessage(MSG_INIT_ZONE_DATAS);
		}else if(MSG_INIT_REGION_DATAS.equals(message)) {
			this.rivs = (List)result.get(CT_IV.IV_RESULT);
			this.sendMessage(MSG_INIT_REGION_DATAS);
		}
		else if(MSG_INIT_LOCATION_DATAS.equals(message)) {
			this.livs = (List)result.get(CT_IV.IV_RESULT);
			this.zld = (JB_Zone_Location_data) result.get(CT_IV.IV_RESULT2);
			this.sendMessage(MSG_INIT_LOCATION_DATAS);
		}else if(MSG_INIT_LOCATION_RC_DATAS.equals(message)) {
			this.lrivs = (List)result.get(CT_IV.IV_RESULT);
			this.zld = (JB_Zone_Location_data) result.get(CT_IV.IV_RESULT2);
			this.sendMessage(MSG_INIT_LOCATION_RC_DATAS);
		}else if(MSG_INIT_INVENTORY_DATAS.equals(message)) {
			this.lis = (List)result.get(CT_IV.IV_RESULT);
			this.sendMessage(MSG_INIT_INVENTORY_DATAS);
		}else if(MSG_UPDATE_IV_DATA.equals(message)){
			this.sendMessage(MSG_UPDATE_IV_DATA);
//			System.out.println("Success!!!");
		}else if(MSG_UPDATE_ZIV_DATA.equals(message)){
			this.sendMessage(MSG_UPDATE_ZIV_DATA);
//			System.out.println("SuccessAgain!!!");
		}
	
	}

	public void onFailure(String message, Map result) {
		
	}

	public boolean onTimeOutFailure(String message) {
		return false;
	}

}
