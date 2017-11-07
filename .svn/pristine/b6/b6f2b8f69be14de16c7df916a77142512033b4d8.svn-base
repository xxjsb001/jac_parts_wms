package com.vtradex.wms.server.service.middle;

import java.io.File;

import com.vtradex.thorn.server.service.BaseManager;
/**发动机中间表接口*/
public interface MilldleTableManager extends BaseManager {
	
	void sysMiddleSupplier();
	
	void sysMiddleMaterial();
	/**同步mes发货单*/
	void sysMiddleDeliverydoc();
	/**发运签收*/
	void sysPickShip();
	
	void readMidQuality();
	
	void sysASNSrm();
	
	void readLFCSBillDetail();
	/**补发MES签收WMS未发运 3>2 20160318 yc.min*/
	void sysPickShipBack(String mes,String item);
	/**批量更新发货单明细的hash值 20160322 yc.min*/
	void updateHashCode();
	/**批量更新MES签收WMS无拣货任务 7>2 20160323 yc.min*/
	void updateFlag7(String mes,String item);
	/**mes报缺库存 结合MES要货与理论ASN计算*/
	void mesMisPick();
	/**mes报缺期初导入*/
	void mesDisInitImport(File file);
	/**
	 * 接口方法调用
	 */
	void invokeMethod(String type,String subscriber) throws Exception;
	
	/**
	 * 人工执行TASK方法
	 */
	void thornTaskMethod(String type,String taskIds) throws Exception;
}
