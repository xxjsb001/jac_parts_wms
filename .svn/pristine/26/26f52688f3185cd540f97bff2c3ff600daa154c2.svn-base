package com.vtradex.wms.server.service.inventory;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Cell;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.base.LotInfo;
import com.vtradex.wms.server.model.count.WmsCountRecord;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.inventory.WmsInventoryLog;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.middle.MesMisInventory;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.move.WmsTaskLog;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.receiving.WmsReceivedRecord;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;

public interface WmsInventoryManager extends BaseManager{
	
	
	/**
	 * 根据条件查找库存，没找到返回null
	 * @param locationId
	 * @param itemKeyId
	 * @param packageUnitId
	 * @param inventoryStatus
	 * @return
	 */
	@Transactional
	public WmsInventory getInventory(Long locationId, Long itemKeyId, Long packageUnitId, String inventoryStatus);
	
	/**
	 * 获取库存，如果没招到则创建库存
	 * @param wmsLocation
	 * @param wmsItemKey
	 * @param wmsPackageUnit
	 * @param inventoryStatus
	 * @return
	 */
	@Transactional
	WmsInventory getInventoryWithNew(WmsLocation wmsLocation, WmsItemKey wmsItemKey, 
			WmsPackageUnit wmsPackageUnit, String inventoryStatus);
	/**找库存,不考虑状态 yc*/
	@Transactional
	WmsInventory getInventoryWithNew(WmsLocation wmsLocation, WmsItemKey wmsItemKey, 
			WmsPackageUnit wmsPackageUnit);

	/**
	 * 收货过账
	 * @param receivedRecord
	 */
	@Transactional
	void receive(WmsReceivedRecord receivedRecord,String inventoryStatus);
	
	List<WmsInventory> getInventoryBySoi(String soi);
	
	
	@Transactional
	WmsInventoryExtend addInventoryExtend(WmsInventory inv, WmsInventoryExtend wie,Double quantityBU);

	/**
	 * 发货确认
	 * @param taskLog
	 * @param shippedQuantityBU
	 */
	@Transactional
	void ship(WmsTaskLog taskLog, Double shippedQuantityBU);
	
	/**
	 * 库位出入锁校验
	 * @param type
	 * @param locationId
	 */
	@Transactional
	void verifyLocation(String type, Long locationId);
	
	/**
	 * 库位存放校验规则
	 * @param locationId
	 * @param currentItemKeyId
	 * @param currentItemId
	 * @param packageUnitId
	 * @param quantity
	 * @param pallet
	 */
	@Transactional
	void verifyLocation(String type, Long locationId,WmsPackageUnit wpu,
			Long currentItemKeyId, Long currentItemId, Double quantityBU,
			String pallet);
	
	/**
	 * 刷新库满度
	 * @param warehouseName
	 * @param location
	 * @param itemKeyId
	 * @param itemId
	 * @param inOrOut
	 */
	@Transactional
	void refreshLocationUseRate(WmsLocation location, int inOrOut);
	
	/**
	 * 手工码托 
	 * @param wsn
	 * @param quantity
	 */
	@Transactional
	void manualPallet(WmsInventoryExtend wsn, Double quantity,String dstPalletNo);
	
	
	/** 自动码托 */
	@Transactional
	void autoPallet(WmsInventoryExtend inventoryExtend);
	
	/**
	 * 手工拆托
	 * @param wsn
	 * @param quantity
	 */
	@Transactional
	void manualSplitPallet(WmsInventoryExtend wsn , Double quantity);

	/**
	 * 手工移位
	 * @param wsn
	 * @param destLocId
	 * @param quantityBU
	 * @param workerId
	 */
	@Transactional
	void inventoryManualMove(WmsInventoryExtend wsn, Long destLocId, Double quantityBU, Long workerId);
	
	/**
	 * 库内移位
	 * @param srcInv
	 * @param dstLoc
	 * @param packageUnit
	 * @param moveQuantityBU
	 * @param status
	 * @param logType
	 * @param description
	 */
	@Transactional
	WmsInventory moveInventory(WmsInventoryExtend wsn, WmsLocation dstLoc, String pallet, String carton,  
			WmsPackageUnit packageUnit, Double moveQuantityBU, String status, String logType, String description);
	
	WmsInventory moveInventory(boolean isMatchLoc ,WmsInventoryExtend wsn, WmsLocation dstLoc, String pallet, String carton,  
			WmsPackageUnit packageUnit, Double moveQuantityBU, String status, String logType, String description);
	
	WmsInventory moveInventory(Boolean isManual,boolean isMatchLoc ,WmsInventoryExtend wsn, WmsLocation dstLoc, String pallet, String carton,  
			WmsPackageUnit packageUnit, Double moveQuantityBU, String status, String logType, String description);
	
	/**
	 * 删除无效库存记录 
	 */
	@Transactional
	void deleteInvalidInventory();
	
	/** 
	 * 库存锁定
	 * @param parentIds
	 * @param lockType
	 */
	@Transactional
	void lockInventory(WmsInventoryExtend wsn,Long itemStateId ,String lockType,String quantityBu);
	
	/**
	 * 库存解锁
	 * @param parentIds
	 * @param lockType
	 */
	@Transactional
	void unlockInventory(WmsInventoryExtend wsn,Long itemStateId,String lockType,String quantityBu);
	
	/**
	 * 库存调整-修改库存
	 */
	@Transactional
	void modifyInventory(WmsInventoryExtend wsn, double packQty, String description);
	
	/**
	 * 获取仓库盘点库位
	 * @param warehouse
	 * @return
	 */
	WmsLocation getCountLocationByWarehouse(WmsWarehouse warehouse);
	
	WmsPackageUnit getWmsPackageUnitByLocation(WmsLocation location,WmsItem item);
	
	/**
	 * 盘点--库存数量差异调整
	 */
	@Transactional
	void quantityAdjust(WmsCountRecord wmsCountRecord);
	
    /**
	 * 盘点盈亏调整
	 */
	@Transactional
	void countAdjust(WmsInventoryExtend wsn, Double modifyQuantity, String description);

	/**
	 * 增加库存日志
	 * @param logType
	 * @param incOrDec
	 * @param relatedBill
	 * @param billType
	 * @param location
	 * @param pallet
	 * @param itemKey
	 * @param occurQuantity
	 * @param unit
	 * @param status
	 * @param description
	 * @return
	 */
	@Transactional
	WmsInventoryLog addInventoryLog(String logType, int incOrDec, String relatedBill, WmsBillType billType, 
			WmsLocation location, WmsItemKey itemKey,Double occurQuantity, WmsPackageUnit unit, 
			String status, String description);
	
	//-------------------------------------------------------------------------------
	//剩余未验证接口函数
	//-------------------------------------------------------------------------------
	/**
	 * 自动计算每日库存结转
	 */
	@Transactional
	void compute();
	
	/**
	 * 按照指定日期指定仓库计算库存结转
	 */
	@Transactional
	void compute(Date date,Long wmsWarehouseId);
	
	/**
	 * 指定期初库存日结数据
	 */
	@Transactional
	void initdayend(Date date);
	
	/**
	 * 指定库存日结截止数据
	 */
	@Transactional
	void inventorydayend(Date beginDate,Date endDate);
	
	/**
	 * 自动根据库存积数进行计费
	 */
	@Transactional
	void inventoryFeeAutoCompute();
	
	/**
	 * 计费：新增库存积数采集功能，定时将每日库存数据写入库存积数表
	 */
	@Transactional
	void inventoryCountAutoCompute();
	
	/**
	 * 计费：按日期计算库存积数
	 */
	@Transactional
	void inventoryCountAutoCompute(Date computeDate);
	/**
	 * 根据CSV文件初始化库存
	 * @param file 
	 */
	void initInventoryByCVS(File file);

	/**
	 * 根据CSV文件增加库存
	 * @param strList
	 * @param commitNum
	 * @return
	 * @throws ParseException
	 */
	@Transactional
	int resolveInventory(List<Cell[]> strList) throws ParseException;

	/** 取消收货 */
	@Transactional
	void cancelReceive(WmsReceivedRecord receivedRecord,Double cancelQtyBU);
	
	@Transactional
	void cancelMoveDoc(WmsMoveDocDetail wmsMoveDocDetail);
	
	/**
	 * 新增库存
	 * @param inv 
	 * @param addReason 调整原因
	 */
	@Transactional
	void addInventory(WmsInventory inv, Long itemId, Long supplierId, LotInfo lotInfo, String addReason);
	
	/**
	 * 库内质检/库存管理页面-jac yc.min
	 */
	@Transactional
	void qualityInventory(WmsInventoryExtend inv, Boolean beView001, Boolean beView002,
			String extendPropC1,String itemState, Double qualityNum);
	/**原库存扣减库存量/取消拣货分配 yc.min*/
	@Transactional
	WmsInventory moveSrcInv(Long srcInventoryId,Double quantity);
	/**目标库存增加库存量/取消上架分配 yc.min*/
	@Transactional
	WmsInventory moveDescInv(Long descInventoryId,Double quantity);
	/**预分配目标库存 yc.min*/
	@Transactional
	WmsInventory allocatePutaway(WmsLocation toLoc,WmsItemKey itemKey
			,WmsPackageUnit wmsPackageUnit, String inventoryStatus,Double quantity
			,Integer palletQty);
	/**报缺库存*/
	void sysInventoryMiss();
	/**合格品超库存-初始化*/
	@Transactional
	void hgInventoryOutInit();
	/**不合格品超库存-初始化*/
	@Transactional
	void unHgInventoryOutInit();
	/**预到货预警-初始化*/
	@Transactional
	String screenAsnPre();
	
	Object getSingleRuleTableDetail(String warehouseName,Object... objects);
	
	List<Map<String,Object>> getAllRuleTableDetail(String warehouseName,Object... objects);
	
	/**库内快捷移位 yc.min*/
	@Transactional
	void inventoryQuickMove(WmsInventory srcInv,Double moveQty,String description,
			WmsLocation toLocation,WmsPackageUnit basePackageUnit,WmsInventoryExtend srcIx);
	
	/**增加目标库位序列号 yc.min*/
	@Transactional
	void addDescWie(WmsInventory dstInventory,String pallet,WmsLocation toLocation,Double moveQty);
	@Transactional
	void saveTask(String[] obj);
	/**MES报缺调整 yc*/
	@Transactional
	void mesMisAdjust(MesMisInventory mes,List<String> values);
	
	/**监控实际库存,和安全库存进行比对,如果低于安全库存-报缺库存,进行预警*/
	@Transactional
	void monitorActualInventory();
	/**导入安全库存*/
	void importSafeInventory(File file);
	
}
