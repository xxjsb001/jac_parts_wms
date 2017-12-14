package com.vtradex.wms.server.service.middle;

public interface MiddleTableName {
	/*****@JAC_FDJ_136,@JAC_FDJ_92  fdjmiddle/fdjmiddle******/
	/**物料中间表*/
	String MIDDLEMATERIAL = "MIDDLE_MATERIAL@JAC_FDJ_136";//MIDDLE_MATERIAL_WMS
	/**供应商中间表*/
	String MIDDLESUPPLIER = "MIDDLE_SUPPLIER@JAC_FDJ_136";//MIDDLE_SUPPLIER_WMS
	/**发货单明细中间表*/
	String MIDDLEDELIVERYDOCDETAIL = "MIDDLE_DELIVERY_DOC_DETAIL@JAC_FDJ_136";//MIDDLE_DELIVERY_DOC_DETAIL_WMS
	/**物料供应商关系中间表*/
	String MIDDLESUPPLIERSOFMATERIALS = "MIDDLE_SUPPLIERS_OF_MATERIALS@JAC_FDJ_136";//MIDDLE_SUPPLIERS_OF_MATERIALS_WMS
	/**质检中间表 WMS插入*/
	String MIDDLEQUALITYTESTING = "MIDDLE_QUALITY_TESTING@JAC_FDJ_136";//MIDDLE_QUALITY_TESTING_WMS
	/**WMS读取ERP中间表信息往ASN传数据*/
	String srm_erp_v_Poasn106 = "srm_erp.v_Poasn106@srm_dcs";//srm_erp.v_Poasn106@srm_dcs,v_Poasn106
	/**LFCS 视图：fdj_bms_fee_data_view*/
	String FDJ_BMS_FEE_DATA_VIEW = "FDJ_BMS_FEE_DATA_VIEW";
	/**LFCS 費用明細表：BMS_FEE_DATA*/
	String BMS_FEE_DATA="FDJLFCS.BMS_FEE_DATA";
	/**MES要货报缺库存中间表*/
	String MIDDLE_MES_ORDER = "ITDEP_MIDDLE_MES_ORDER@JAC_FDJ_136";//0-读取,1-已读取,9-锁定,8-期初导入更新
	/**----------------------------新港----------------------------------**/
	/**MES退料单*/
	String W_ASN_TL = "W_ASN_TL";//wmsDealInterfaceDataManager.mesReturnOrder
	/**SRM送货单*/
	String W_ASN_SRM = "W_ASN_SRM";//wmsDealInterfaceDataManager.srmOrder
	/**临采件入库*/
	String W_ASN_ERP = "W_ASN_ERP";//暂时不用
	/**入库数据传ERP*/
	String W_RECEIVE_ERP = "W_RECEIVE_ERP";//ASN过账确认时传值
	/**MES计划件料单*/
	String W_ORDER_JH = "W_ORDER_JH";//wmsDealInterfaceDataManager.dealOrderJh
	/**MES时序件料单*/
	String W_ORDER_SPS = "W_ORDER_SPS";//wmsDealInterfaceDataManager.dealOrderSps
	/**时序件器具明细表*/
	String W_SPS_APPLIANCE = "W_SPS_APPLIANCE";//saveSpsApplianceData(wmsDealInterfaceDataManager.dealOrderSps 中调用)
	
	/**MES看板件料单*/
	String W_ORDER_KB = "W_ORDER_KB";//wmsDealInterfaceDataManager.dealOrderKb
	/**临采件调整出库发货单*/
	String W_ORDER_ERP = "W_ORDER_ERP";//wmsDealInterfaceDataManager.dealPicketTicketData
	/**器具对应关系表*/
	String W_APPLIANCE_ITEM_MES = "W_APPLIANCE_ITEM_MES";//wmsDealInterfaceDataManager.dealStationAndItemData
	/**物料基础信息表*/
	String W_ITEM_ERP = "W_ITEM_ERP";//wmsDealInterfaceDataManager.dealErpItemData
	/**供应商物料对应关系表*/
	String W_SUPPLY_ITEM_ERP = "W_SUPPLY_ITEM_ERP";//wmsDealInterfaceDataManager.dealSupplierAndItemData
	/**供应商基础信息表*/
	String W_SUPPLY_ERP = "W_SUPPLY_ERP";//wmsDealInterfaceDataManager.dealErpSupplierData
	/**质检结果表*/
//	String W_RESULT_QIS = "W_RESULT_QIS";
	/**MES库存实收*/
	String W_RECEIVE_MES = "W_RECEIVE_MES";//wmsDealInterfaceDataManager.receiveMes
	/**送检计划表 WMS插入*/
	String W_QUALITY_TESTING_QIS = "W_QUALITY_TESTING_QIS";//wmsDealInterfaceDataManager.dealQisResultData
	/**出库数据传ERP*/
	String W_DELIVER_ERP = "W_DELIVER_ERP";//bol发运插入 WmsMasterBOLManager.shippingWmsBOL
	/**出库数据传MES*/
	String W_DELIVER_MES = "W_DELIVER_MES";//bol发运插入 WmsMasterBOLManager.shippingWmsBOL
	/**器具信息传MES*/
	String W_APPLIANCE_MES = "W_APPLIANCE_MES";//调用方法被注释,目前未用
	/**质检基础信息*/
	String W_QISPLAN = "W_QISPLAN";//wmsDealInterfaceDataManager.readMonthQisPlan
	/**质检状态映射表,维护表*/
	String MIDDLE_QUALITY_STATUS = "MIDDLE_QUALITY_STATUS";//MIDDLE_QUALITY_STATUS_WMS
	/**接口消息表,EDI读取*/
	String M_MESSAGE = "M_MESSAGE";
	/**时序BOM*/
	String WMS_MATBOM_VIEW = "mesxg.wms_matbom_view@xgmes";
	String W_BOM_MES = "W_BOM_MES";
}
