package com.vtradex.wms.server.model.move;

/**
 * 移位单类型 
 * 
 * @category 枚举
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.2 $Date: 2015/04/30 04:31:31 $
 */
public interface WmsMoveDocType {
	
	/** 
	 * 收货上架
	 */
	public static String MV_PUTAWAY = "MV_PUTAWAY";	
	
	/**
	 * 发货拣货
	 */
    public static String MV_PICKTICKET_PICKING = "MV_PICKTICKET_PICKING";
    /**
     * 批次拣货
     */
    public static String LOT_PICKING = "LOT_PICKING";
    /**
     * 时序拣货
     */
    public static String SPS_PICKING = "SPS_PICKING";
    
    /**
     * 波次拣货
     */
    public static String MV_WAVE_PICKING = "MV_WAVE_PICKING";
    
    /**
     * 加工拣货
     */
    public static String MV_PROCESS_PICKING = "MV_PROCESS_PICKING";
    
    /**
	 * 库内移位
	 */
	public static String MV_MOVE = "MV_MOVE";
    
    /**
     * 补货移位
     */
    public static String MV_REPLENISHMENT_MOVE = "MV_REPLENISHMENT_MOVE";
    
    /**
     * 质检移位
     */
    public static String MV_QUALITY_MOVE = "MV_QUALITY_MOVE";
}
