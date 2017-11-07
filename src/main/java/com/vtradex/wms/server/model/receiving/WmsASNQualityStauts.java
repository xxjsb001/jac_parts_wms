package com.vtradex.wms.server.model.receiving;

public interface WmsASNQualityStauts {
	/** 不质检 */
	public static final String NOQUALITY = "NOQUALITY";
	/** 待质检 */
	public static final String UNQUALITY = "UNQUALITY";
	/** 质检完成 */
	public static final String QUALITYFINISHED = "QUALITYFINISHED";
	
	
	
	public static final String UNQUALITY_KEY = "待检";
	public static final String QUALITY_FAIL_KEY = "质检未通过";
	public static final String QUALITY_DAMAGE_KEY = "质损";
}