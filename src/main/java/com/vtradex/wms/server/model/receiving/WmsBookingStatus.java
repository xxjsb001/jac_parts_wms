package com.vtradex.wms.server.model.receiving;

/**
 * @Description: 
 * @Author: <a href="qiufeng.chen@vtradex.net"/>陈秋凤</a>
 * @CreateDate：  2012-11-20
 * @version: v1.0
 */
public interface WmsBookingStatus {
	/** 预约 */
	public static final String OPEN = "OPEN";
	/** 到库 */
	public static final String EXECUTING = "EXECUTING";
	/** 完成*/
	public static final String FINISH = "FINISH";
}