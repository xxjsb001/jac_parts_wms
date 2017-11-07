package com.vtradex.wms.server.model.process;

/**
 * 加工计划状态
 *
 * @category 枚举 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:51 $
 */
public interface WmsProcessPlanStatus {
	
    /**
     * 打开
     */
    public static String OPEN = "OPEN";

    /**
     * 生效
     */
    public static String ENABLED = "ENABLED";
    
    /**
     * 失效
     */
    public static String DISABLED = "DISABLED";

}
