package com.vtradex.wms.server.utils;

import java.util.ArrayList;
import java.util.List;

/**edi task type yc*/
public class EdiTaskType {

	/**合格品超库存*/
	public static String SCREEN_HG_INVENTORY_OUT = "SCREEN_HG_INVENTORY_OUT";
	
	/**物料供应商关系表*/
	public static String MIDDLE_SUPPLY_ITEM_ERP = "MIDDLE_SUPPLY_ITEM_ERP";
	
	/**器具对应关系表*/
	public static String W_APPLIANCE_ITEM_MES = "W_APPLIANCE_ITEM_MES";
	
	public static String types;
	
	static{
		List<String> list = new ArrayList<String>();
		list.add("'"+SCREEN_HG_INVENTORY_OUT+"'");
		list.add("'"+MIDDLE_SUPPLY_ITEM_ERP+"'");
		
		types = list.toString();
	}
	
	public EdiTaskType() {
		// TODO Auto-generated constructor stub
	}

}
