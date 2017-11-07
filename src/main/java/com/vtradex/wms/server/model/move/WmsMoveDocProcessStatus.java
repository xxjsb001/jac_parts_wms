/**
 * 
 */
package com.vtradex.wms.server.model.move;
/**
 * 加工单加工状态
 * @author <a href="mailto:jin.liu@vtradex.net">刘晋</a>
 * @since 2012-7-16 上午11:50:54
 */
public interface WmsMoveDocProcessStatus {

	/** 待加工 */
	public static final String UNPROCESS = "UNPROCESS";
	
	/** 加工中 */
	public static final String PROCESSING = "PROCESSING";
	
	/** 完成 */
	public static final String PROCESSED = "PROCESSED";
}
