package com.vtradex.wms.server.model.receiving;

public interface WmsASNStatus {
	/** 打开 */
	public static final String OPEN = "OPEN";
	/** 生效 */
	public static final String ACTIVE = "ACTIVE";
	/** 部分收货 */
	public static final String RECEIVING = "RECEIVING";
	/** 收货完成 */
	public static final String RECEIVED = "RECEIVED";
	/** 取消 */
	public static final String CANCELED = "CANCELED";
}