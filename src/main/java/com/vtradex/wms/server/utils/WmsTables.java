package com.vtradex.wms.server.utils;

public interface WmsTables {
	/**拣货明细与器具关系*/
	String WMS_MOVEDOC_AND_STATION = "WMS_MOVEDOC_AND_STATION";//WmsMoveDocAndStation
	
	String WMS_TASK_AND_STATION = "WMS_TASK_AND_STATION";//WmsTaskAndStation
	
	
	/**供应商物料关系表A,WMS的表*/
	String MIDDLESUPPLYITEMERPA = "MIDDLE_SUPPLYITEMERP_A";
	/**供应商物料关系表B,WMS的表*/
	String MIDDLESUPPLYITEMERPB = "MIDDLE_SUPPLYITEMERP_B";
	
	/**装车明细*/
	String WMS_BOL_DETAIL = "WMS_BOL_DETAIL";//WmsBOLDetail
	/**装车单*/
	String WMS_BOL = "WMS_BOL";//WmsBOL
	
	/**拣货移位明细*/
	String WMS_MOVE_DOC_DETAIL = "WMS_MOVE_DOC_DETAIL";//WmsMoveDocDetail
	/**拣货移位*/
	String WMS_MOVE_DOC = "WMS_MOVE_DOC";//WmsMoveDoc
	
	/**货品*/
	String WMS_ITEM = "WMS_ITEM";//WmsItem
}
