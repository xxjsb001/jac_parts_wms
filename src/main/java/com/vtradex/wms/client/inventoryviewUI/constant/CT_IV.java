package com.vtradex.wms.client.inventoryviewUI.constant;


import com.google.gwt.user.client.rpc.IsSerializable;


public class CT_IV implements IsSerializable{
	/** 缺省道、排、行、列的数量 */
	public static int DEFAULT_AISLE = 1;
	public static int DEFAULT_LINE = 10;
	public static int DEFAULT_ROW = 5;
	public static int DEFAULT_COL = 10;
	
	public static int CURRENT_ROW = 0;
	public static int CURRENT_COL = 0;
	
	/** 缺省X，Y离左上角(0,0)的坐标偏差 */
	public static final int DEFAULT_OFF_HEIGHT = 80;
	public static final int DEFAULT_OFF_WIDTH = 25;
	/** 缺省容积率%格式 */
	public static final String DEFAULT_PERCENT_FORMAT = "##0.##%";
	public static final String DEFAULT_INTEGER_FORMAT = "##0";
	/** 缺省柱图的色标 */
	public static final String DEFAULT_CHART_COLOR = "red";
	/** 缺省带背景面板样式，如仓库布局Panel */
	public static final String DEFAULT_BODYSTYLE = "text-align:center;padding:1px 1px;" +
	 					"border:1px dotted #99bbe8;background:#dfe8f6 url(images/inventoryview/grid-back.gif) repeat left top;" +
	 					"color:#15428b;cursor:default;margin:0px;" +
	 					"font:bold 11px tahoma,arial,sans-serif;";
	/** 导航条箭头IMG */
	public static final String DEFAULT_ARROW_IMG = "<img src='images/inventoryview/rule_arrow.gif'/>";
	/** 缺省带背景图片的面板，如仓库布局Panel */
	public static final String DEFAULT_IMAGE_STYLE = "iv_sub_image_panel";
	/** 仓库可视化后台manager */
	public static final String IV_MANAGER = "wmsInventoryViewManager";
	/** 获取后台仓库可视化的方法 */
	public static final String IV_INIT_DATA = "init_WM_IV";
	
	//新增方法
	/** 修改后台仓库可视化的方法 */
	public static final String IV_UPDATE_DATA = "update_WM_IV";
	public static final String ZIV_UPDATE_DATA = "update_WM_ZIV";
	
	/** 获取库区的可视化的方法 */
	public static final String IV_INIT_ZONE_DATA = "init_Zone_IV";
	/** 获取区的可视化的方法 */
	public static final String IV_INIT_REGION_DATA = "init_Region_IV";
	/** 获取库位的道排可视化的方法 */
	public static final String IV_INIT_LOCATION_DATA = "init_Location_IV";
	/** 获取库位行列可视化的方法 */
	public static final String IV_INIT_LOCATION_RC_DATA = "init_Location_RC_IV";
	/** 获取库存详细的方法 */
	public static final String IV_INIT_INVENTORY_DATA = "init_Inventory_Data";
	/** 结果集标识key */
	public static final String IV_RESULT = "iv_result";
	public static final String IV_RESULT2 = "iv_result2";
	/** 仓库ID */
	public static final String IV_WH_ID = "wh_id";
	/**仓库的坐标*/
	public static final String IV_WH_XPOS = "wh_xPos";
	public static final String IV_WH_YPOS = "wh_yPos";
	
	/** 库区ID */
	public static final String IV_ZONE_ID = "zone_id";
	/** 区ID */
	public static final String IV_REGION_ID = "region_id";
	
	/**库区的坐标*/
	public static final String IV_ZONE_XPOS = "zone_xPos";
	public static final String IV_ZONE_YPOS = "zone_yPos";
	
	/** 道 */
	public static final String IV_AISLE = "aisle";
	/** 排 */
	public static final String IV_LINE = "line";
	/** 行 */
	public static final String IV_ROW = "row";
	/** 列 */
	public static final String IV_COL = "col";
	/** 仓库可视化对象 */
	public static final String IV_JB_IV = "jb_iv";
	/** 库位ID */
	public static final String IV_LOCATION_ID = "loc_id";
	
}
