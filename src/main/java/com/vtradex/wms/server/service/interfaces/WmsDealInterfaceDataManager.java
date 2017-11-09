package com.vtradex.wms.server.service.interfaces;


import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.model.shipping.WmsBOLDetail;

/**
 * 处理中间表数据
 * @author 方顺
 * @date 2017-6-29 08:44:20
 */
public interface WmsDealInterfaceDataManager extends BaseManager{
	
	void executeT();
	/**
	 * 			收货接口
	 * 临采件入库、MES退料单、SRM送货单
	 * @author fs 
	 */
//	void dealErpOrder();
	void mesReturnOrder();
	void mesReturnOrderFor();
	
	void srmOrder();
	void srmOrderFor();
	
	
	/**
	 * ASN过账确认将明细传给ERP
	 * @param asn
	 * @author fs
	 */
	void dealAsnData(WmsASN asn);
	
	/**
	 * 				发货单接口
	 * 临采调整出库、MES料单、时序件料单、看板件料单
	 * @date 2017-7-2 15:16:57
	 * @author fs
	 */
	//临采调整出库
	void dealPicketTicketData();
	void dealPicketTicketDataFor();
	//MES料单
	void dealOrderJh();
	void dealOrderJhFor();
	//时序件料单
	void dealOrderSps();
	void dealOrderSpsFor();
	//看板件料单
	void dealOrderKb();
	void dealOrderKbFor();
	
	
	/**
	 * 处理中间表器具物料关系对应表数据
	 * @date 2017-7-4 09:42:01
	 * @author fs
	 */
	void dealStationAndItemData();
	void dealStationAndItemDataFor();
	
	/**
	 * 处理供应商物料关系对应表数据(接口全删全插)
	 * @date 2017-7-4 10:41:44
	 * @author fs
	 */
	void dealSupplierAndItemData();
	void dealSupplierAndItemDataFor();
	
	/**
	 * 处理物料基础信息中间表数据(接口增量)
	 * @date 2017-7-4 10:59:20
	 * @author fs
	 */
	void dealErpItemData();
	void dealErpItemDataFor();

	/**
	 * 处理供应商基础数据中间表数据(接口增量)
	 * @date 2017-7-4 14:28:24
	 * @author fs
	 */
	void dealErpSupplierData();
	void dealErpSupplierDataFor();
	
	/**
	 * QIS质检结果回传WMS
	 * @date 2017-7-5 15:01:18
	 * @author fs
	 */
	void dealQisResultData();
	void dealQisResultDataFor();
	
	/**
	 * 送检数据插入中间表
	 */
	void insertMiddleTable(String moveDocCode,String asnCode,String supplierCode,
			String itemCode,String itemState,Double sendQty,String replenishmentArea,
			Double receivedQuantityBU,String itemName,String supplyName,String userName,
			String relateBill1,Double totalQty,String company,String qualityType);
	
	/**当日库收货实收,WMS发货,MES收货并返回签收数量*/
	void receiveMes();
	void receiveMesFor();
	
	/**质检基础信息(月质检计划)*/
	void readMonthQisPlan();
	void readMonthQisPlanFor();
	
	/**出库数据传ERP*/
	@Transactional
	void outBoundToErp(WmsBOLDetail detail,WmsMoveDoc moveDoc,int i,WmsTask task,String billCode);
	
	/**出库数据传MES*/
	@Transactional
	void outBoundToMes(WmsBOLDetail detail,WmsMoveDoc moveDoc,int i,WmsTask task,String billCode);
	@Transactional
	void outBoundLotToMes(Long id);
	/**出库数据的器具信息传MES*/
//	void outBoundApplianceToMes(WmsTask task,WmsBOLDetail detail);
	/**取消质检*/
	void cancelQuality(String sql);
}
