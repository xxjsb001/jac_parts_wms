package com.vtradex.wms.server.model.shipping;

public interface WmsBOLStatus {
	/** 打开*/
	public static final String OPEN = "OPEN";
	
	/** 已装车*/
	public static final String ISLOAD = "ISLOAD";
	
	/** 作业中*/
	public static final String WORKING = "WORKING";
	
	/** 完成*/
	public static final String FINISHED = "FINISHED";
	
	/** 待发运*/
	public static final String UNSHIP = "UNSHIP";
	
	/** 已发运*/
	public static final String SHIPPED = "SHIPPED";
	
}
