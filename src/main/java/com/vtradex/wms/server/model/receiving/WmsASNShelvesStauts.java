package com.vtradex.wms.server.model.receiving;

public interface WmsASNShelvesStauts {
	/** 待上架 */
	public static final String UNPUTAWAY = "UNPUTAWAY";
	/** 上架中 */
	public static final String PUTAWAY = "PUTAWAY";
	/** 上架完成 */
	public static final String FINISHED = "FINISHED";
}