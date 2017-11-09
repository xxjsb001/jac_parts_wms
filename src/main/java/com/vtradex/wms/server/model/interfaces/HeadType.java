package com.vtradex.wms.server.model.interfaces;

public interface HeadType {

	/**ASN*/
	public static String ASN = "ASN";
	
	/**看板发货单*/
	public static String PICK_KB = "PICK_KB";
	
	/**计划件发货单*/
	public static String PICK_JH = "PICK_JH";
	
	/**时序件发货单*/
	public static String PICK_SPS = "PICK_SPS";
	
	/**创建BOL*/
	public static String CREATE_BOL = "CREATE_BOL";
	
	/**发运数据传MES*/
	public static String W_DELIVER_MES = "W_DELIVER_MES";
}
