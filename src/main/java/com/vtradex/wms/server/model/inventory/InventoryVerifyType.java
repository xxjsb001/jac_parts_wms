package com.vtradex.wms.server.model.inventory;

public interface InventoryVerifyType {
	
	/** 质检通过 */
	public static String PASS = "PASS";
	/** 质检不合格 */
	public static String NOTPASS = "NOTPASS";
	/** 质检报废 */
	public static String DISCARD = "DISCARD";
	
}
