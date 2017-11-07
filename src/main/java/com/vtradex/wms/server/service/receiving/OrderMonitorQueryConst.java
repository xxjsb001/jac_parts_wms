package com.vtradex.wms.server.service.receiving;

/**
 * @author: 李炎
 */

public interface OrderMonitorQueryConst {

	/** 一页显示的默认要监控的订单数 */
	public static int DEFAULT_DISPLAY_COUNT = 15;
	/** 一页显示的行数 */
	public static String FILTER_DISPLAY_ROW_COUNT = "FILTER_DISPLAY_ROW_COUNT";
	/** 当前页数 */
	public static String FILTER_CURRENT_PAGE_NUM = "FILTER_CURRENT_PAGE_NUM";
	/** 只显示异常订单 */
	public static String FILTER_SHOW_ABNORMAL_ONLY = "FILTER_SHOW_ABNORMAL_ONLY";
	/** 要显示的客户代码 */
	public static String FILTER_CUSTOMER_CODE_INCLUDE = "FILTER_CUSTOMER_CODE_INCLUDE";
	/** 不显示的客户代码 */
	public static String FILTER_CUSTOMER_CODE_EXCLUDE = "FILTER_CUSTOMER_CODE_EXCLUDE";
	/** 要显示的订单代码 */
	public static String FILTER_ORDER_CODE_INCLUDE = "FILTER_ORDER_CODE_INCLUDE";
	/** 不显示的订单代码 */
	public static String FILTER_ORDER_CODE_EXCLUDE = "FILTER_ORDER_CODE_EXCLUDE";
	/** 要显示的订单类型代码 */
	public static String FILTER_ORDER_TYPE_CODE = "FILTER_ORDER_TYPE_CODE";
	/** 查询全部或者按条件查询 */
	public static String FILTER_CONDITION_TYPE = "FILTER_CONDITION_TYPE";
	
	public static String IS_QUERY_ORDER_TYPE = "IS_QUERY_ORDER_TYPE";
}

