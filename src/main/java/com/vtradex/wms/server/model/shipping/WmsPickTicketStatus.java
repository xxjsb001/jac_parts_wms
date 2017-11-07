package com.vtradex.wms.server.model.shipping;

/**
 * 发货单状态 
 *
 * @category 枚举
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:51 $
 */
public interface WmsPickTicketStatus {
    
	/**
     * 打开
     */
    public static String OPEN = "OPEN";
    
    /**
     * 取消
     */
    public static String CANCELED = "CANCELED";
    
    /**
     * 作业中
     */
    public static String WORKING = "WORKING";
    
    /**
     * 完成
     */
    public static String FINISHED = "FINISHED";
}