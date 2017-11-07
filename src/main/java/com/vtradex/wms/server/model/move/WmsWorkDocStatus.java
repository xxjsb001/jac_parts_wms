package com.vtradex.wms.server.model.move;

/**
 * 作业单状态
 *
 * @category 枚举 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:51 $
 */
public interface WmsWorkDocStatus {
	
	/** 打开*/
	public static final String OPEN = "OPEN";
	
	/** 作业中*/
	public static final String WORKING = "WORKING";
	
	/** 完成*/
	public static final String FINISHED = "FINISHED";	
}
