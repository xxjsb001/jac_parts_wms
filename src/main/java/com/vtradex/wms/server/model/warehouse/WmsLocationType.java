package com.vtradex.wms.server.model.warehouse;

public interface WmsLocationType {
	/** 收货 */
	public static String RECEIVE = "RECEIVE";
	/** 存货 */
	public static String STORAGE = "STORAGE";
	/** 备货 */
	public static String SHIP = "SHIP";
	/** 盘点 */
	public static String COUNT = "COUNT";
	/** 加工 */
	public static String PROCESS = "PROCESS";
	/** 二次分拣 */
	public static String SPLIT = "SPLIT";
}