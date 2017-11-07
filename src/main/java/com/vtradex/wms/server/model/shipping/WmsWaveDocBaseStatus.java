package com.vtradex.wms.server.model.shipping;

public interface WmsWaveDocBaseStatus {
	/**
     * 打开;
     */
    public static String OPEN = "OPEN";
	/**
     * 作业中
     */
    public static String WORKING = "WORKING";
    /**
     * 完成
     */
    public static String FINISHED = "FINISHED";
}