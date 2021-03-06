package com.vtradex.wms.server.service.inventory.pojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.exception.OriginalBusinessException;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.exception.ExceptionLog;
import com.vtradex.thorn.server.model.message.Task;
import com.vtradex.thorn.server.model.security.User;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.LotInfo;
import com.vtradex.wms.server.model.count.WmsCountRecord;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.inventory.WmsInventoryCount;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.inventory.WmsInventoryFee;
import com.vtradex.wms.server.model.inventory.WmsInventoryLockType;
import com.vtradex.wms.server.model.inventory.WmsInventoryLog;
import com.vtradex.wms.server.model.inventory.WmsInventoryLogType;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.inventory.WmsMisInventory;
import com.vtradex.wms.server.model.inventory.WmsSafeInventory;
import com.vtradex.wms.server.model.inventory.WmsSafeInventoryType;
import com.vtradex.wms.server.model.inventory.WmsStorageDaily;
import com.vtradex.wms.server.model.middle.MesMisInventory;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsMoveDocType;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.move.WmsTaskLog;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsCompanyAndBillType;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsItemState;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.model.receiving.WmsReceivedRecord;
import com.vtradex.wms.server.model.shipping.WmsPickTicket;
import com.vtradex.wms.server.model.shipping.WmsWaveDocDetail;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsLocationStatus;
import com.vtradex.wms.server.model.warehouse.WmsLocationType;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWarehouseArea;
import com.vtradex.wms.server.service.GlobalParamUtils;
import com.vtradex.wms.server.service.base.WmsItemManager;
import com.vtradex.wms.server.service.base.WmsLocationManager;
import com.vtradex.wms.server.service.bean.WarehouseInfoInClient;
import com.vtradex.wms.server.service.inventory.WmsInventoryExtendManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.middle.MiddleTableName;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.utils.DateUtil;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.utils.PackageUtils;
import com.vtradex.wms.server.utils.WmsStringUtils;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

@SuppressWarnings("all")
public class DefaultWmsInventoryManager extends DefaultBaseManager implements
		WmsInventoryManager, ApplicationContextAware {
	/**500*/
	public static Integer PAGE_NUMBER = 500;
	protected WorkflowManager workflowManager;
	protected WmsRuleManager wmsRuleManager;
	protected WmsLocationManager locationManager;
	protected WmsInventoryExtendManager inventoryExtendManager;
	protected WmsItemManager wmsItemManager;

	public DefaultWmsInventoryManager(WorkflowManager workflowManager,
			WmsRuleManager wmsRuleManager, WmsLocationManager locationManager,
			WmsInventoryExtendManager inventoryExtendManager,
			WmsItemManager wmsItemManager) {
		this.workflowManager = workflowManager;
		this.wmsRuleManager = wmsRuleManager;
		this.locationManager = locationManager;
		this.inventoryExtendManager = inventoryExtendManager;
		this.wmsItemManager = wmsItemManager;
	}

	public DefaultWmsInventoryManager() {
	}

	public WmsPackageUnit getWmsPackageUnitByLocation(WmsLocation location,
			WmsItem item) {
		Map<String, Object> problem = new HashMap<String, Object>();
		problem.put("库区", location.getWarehouseArea().getCode());
		problem.put("区", location.getZone());
		problem.put("排", location.getLine());
		problem.put("列", location.getColumn());
		problem.put("层", location.getLayer());
		Map<String, Object> result = wmsRuleManager.execute(location
				.getWarehouse().getName(), location.getWarehouse().getName(),
				"库位包装级别规则", problem);
		String level = null;
		Map<String, Object> locationMap = (Map<String, Object>) result
				.get("返回结果");
		if (locationMap != null) {
			level = (String) locationMap.get("包装级别");
		}
		return item.getPackageByUnitLevel(level);
	}


	public WmsInventory getInventoryWithNew(WmsLocation wmsLocation,
			WmsItemKey wmsItemKey, WmsPackageUnit wmsPackageUnit,
			String inventoryStatus) {
		// 根据目标库位拆箱级别决定包装形态
		WmsPackageUnit wpu = null;
		if(WmsLocationType.SHIP.equals(wmsLocation.getType())){
			wpu = wmsItemKey.getItem().getPackageByLineNo(1);
		}
//		else{
//			wpu = getWmsPackageUnitByLocation(wmsLocation,
//					wmsItemKey.getItem());
//		}
		if (wpu != null) {
			wmsPackageUnit = wpu;
		}
		
		WmsInventory inventory = getInventory(wmsLocation.getId(),
				wmsItemKey.getId(), wmsPackageUnit.getId(), inventoryStatus);
		if (inventory == null) {
			inventory = createWmsInventory(wmsLocation, wmsItemKey,
					wmsPackageUnit, 0, inventoryStatus);
		}
		return inventory;
	}
	public WmsInventory getInventoryWithNew(WmsLocation wmsLocation,
			WmsItemKey wmsItemKey, WmsPackageUnit wmsPackageUnit) {
		// 根据目标库位拆箱级别决定包装形态
		WmsPackageUnit wpu = null;
		if(WmsLocationType.SHIP.equals(wmsLocation.getType())){
			wpu = wmsItemKey.getItem().getPackageByLineNo(1);
		}
		if (wpu != null) {
			wmsPackageUnit = wpu;
		}
		
		WmsInventory inventory = getInventory(wmsLocation.getId(),
				wmsItemKey.getId(), wmsPackageUnit.getId());
		if (inventory == null) {
			inventory = createWmsInventory(wmsLocation, wmsItemKey,
					wmsPackageUnit, 0, BaseStatus.NULLVALUE);
		}
		return inventory;
	}

	public WmsInventory getInventory(Long locationId, Long itemKeyId,
			Long packageUnitId, String inventoryStatus) {
//		String hashCode = BeanUtils.getFormat(locationId, itemKeyId,
//				packageUnitId, inventoryStatus);
		String hql = " FROM WmsInventory inventory WHERE inventory.location.id = :locationId "
				+ "AND inventory.itemKey.id = :itemKeyId and inventory.packageUnit.id = :packageUnitId"
				+ " AND inventory.status = :status AND inventory.quantityBU>0";
		List<WmsInventory> invs = commonDao.findByQuery(hql, new String[] { "locationId",
				"itemKeyId", "packageUnitId", "status" }, new Object[] {
				locationId, itemKeyId, packageUnitId, inventoryStatus });
		WmsInventory inventory = null;
		if(invs!=null && invs.size()>0){
			inventory = invs.get(0);
		}
		if(inventory==null){//测试发现频繁对同一库位同一KEY信息的库存操作时部分为0的库存数据未及时删除导致数据查询异常
			//优先找数据大于0的库存记录,找不到再找所有,都没有则认为库存需要新建
			hql = " FROM WmsInventory inventory WHERE inventory.location.id = :locationId "
					+ "AND inventory.itemKey.id = :itemKeyId and inventory.packageUnit.id = :packageUnitId"
					+ " AND inventory.status = :status AND inventory.quantityBU = 0";
			List<WmsInventory> inventorys =  commonDao
					.findByQuery(hql, new String[] { "locationId",
							"itemKeyId", "packageUnitId", "status" }, new Object[] {
							locationId, itemKeyId, packageUnitId, inventoryStatus });
			if(!inventorys.isEmpty()){
				inventory=inventorys.get(0);
			}
		}
		return inventory;
	}
	public WmsInventory getInventory(Long locationId, Long itemKeyId,
			Long packageUnitId) {
		WmsInventory inventory = null;
		String hql = " FROM WmsInventory inventory WHERE inventory.location.id = :locationId "
				+ "AND inventory.itemKey.id = :itemKeyId and inventory.packageUnit.id = :packageUnitId"
				+ " AND inventory.quantityBU>0";
		List<WmsInventory> inventorys = commonDao
				.findByQuery(hql, new String[] { "locationId",
						"itemKeyId", "packageUnitId"}, new Object[] {
						locationId, itemKeyId, packageUnitId});
		if(inventorys==null || inventorys.size()<=0){//测试发现频繁对同一库位同一KEY信息的库存操作时部分为0的库存数据未及时删除导致数据查询异常
			//优先找数据大于0的库存记录,找不到再找所有,都没有则认为库存需要新建
			hql = " FROM WmsInventory inventory WHERE inventory.location.id = :locationId "
					+ "AND inventory.itemKey.id = :itemKeyId and inventory.packageUnit.id = :packageUnitId AND inventory.quantityBU = 0";
			inventorys = commonDao
					.findByQuery(hql, new String[] { "locationId",
							"itemKeyId", "packageUnitId"}, new Object[] {
							locationId, itemKeyId, packageUnitId});
			if(inventorys!=null && inventorys.size()>0){
				inventory = inventorys.get(0);
			}
		}else{
			inventory = inventorys.get(0);
		}
		return inventory;
	}

	/**
	 * 根据SOI找库存
	 */
	public List<WmsInventory> getInventoryBySoi(Long locationId, Long itemKeyId) {
		String hql = " FROM WmsInventory inventory WHERE inventory.location.id = :locationId "
				+ "AND inventory.itemKey.id = :itemKeyId AND inventory.quantityBU>0";
		return commonDao.findByQuery(hql, new String[] { "locationId",
				"itemKeyId" }, new Object[] { locationId, itemKeyId });
	}

	public List<WmsInventory> getInventoryBySoi(String soi) {
		String hql = " FROM WmsInventory inv WHERE inv.itemKey.lotInfo.soi = :soi AND inv.quantityBU>0";
		return commonDao.findByQuery(hql, "soi", soi);
	}

	public WmsInventoryExtend addInventoryExtend(WmsInventory inv,
			WmsInventoryExtend wie, Double quantityBU) {
		return inventoryExtendManager.addInventoryExtend(inv, wie, quantityBU);
	}

	/**
	 * 创建新的库存记录
	 * */
	private WmsInventory createWmsInventory(WmsLocation wmsLocation,
			WmsItemKey wmsItemKey, WmsPackageUnit wmsPackageUnit,
			double quantity, String status) {
		WmsInventory wmsInventory = EntityFactory.getEntity(WmsInventory.class);

		wmsInventory.setLocation(wmsLocation);
		wmsInventory.setItemKey(wmsItemKey);
		wmsInventory.setPackageUnit(wmsPackageUnit);
		wmsInventory.setQuantity(quantity);
		wmsInventory
				.setQuantityBU(quantity * wmsPackageUnit.getConvertFigure());
		wmsInventory.setStatus(status);

		commonDao.store(wmsInventory);

		return wmsInventory;
	}
	

	public void receive(WmsReceivedRecord receivedRecord,String inventoryStatus) {
		WmsLocation location = commonDao.load(WmsLocation.class,
				receivedRecord.getLocationId());
		
		if(WmsLocationType.STORAGE.equals(location.getType())){
			verifyLocation(BaseStatus.LOCK_IN, location.getId(),receivedRecord.getPackageUnit(), receivedRecord.getItemKey().getId(),
					receivedRecord.getItemKey().getItem().getId(), receivedRecord.getReceivedQuantityBU(), null);
		}
		// 增加库存数量
		WmsInventory inv = getInventoryWithNew(location,
				receivedRecord.getItemKey(), receivedRecord.getPackageUnit(),
				inventoryStatus);
		inv.addQuantityBU(receivedRecord.getReceivedQuantityBU());
		commonDao.store(inv);
		// 过账前，要先对收货记录进行码托处理，以确保序列号表中关联信息的正确采集
		// 将托盘号、箱号和序列号信息写入序列号表中
		if(WmsLocationType.STORAGE.equals(location.getType())){
			WmsInventoryExtend invExtend = inventoryExtendManager
					.addInventoryExtend(inv, receivedRecord,
							receivedRecord.getReceivedQuantityBU());
		}
		// 写库存日志
		WmsInventoryLog log = new WmsInventoryLog(
				WmsInventoryLogType.RECEIVING, 1, receivedRecord.getAsn()
						.getCode(), receivedRecord.getAsn().getBillType(),
				inv.getLocation(), receivedRecord.getAsn().getSupplier(),inv.getItemKey(),
				receivedRecord.getReceivedQuantityBU(), inv.getPackageUnit(),
				inv.getStatus(), "收货过账");
		commonDao.store(log);

		// 调用规则刷新库满度
		refreshLocationUseRate(location, 1);

		receivedRecord.setInventoryId(inv.getId());//yc.min
		commonDao.store(receivedRecord);
	}

	public void ship(WmsTaskLog taskLog, Double shippedQuantityBU) {
		WmsPickTicket wpt = null;
		WmsTask task = commonDao.load(WmsTask.class, taskLog.getTask().getId());
		WmsMoveDocDetail moveDocDetail = commonDao.load(WmsMoveDocDetail.class,
				task.getMoveDocDetail().getId());
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, moveDocDetail
				.getMoveDoc().getId());
		if (WmsMoveDocType.MV_WAVE_PICKING.equals(moveDoc.getType())) {
			WmsWaveDocDetail waveDocDetail = commonDao.load(
					WmsWaveDocDetail.class, moveDocDetail.getRelatedId());
			WmsMoveDocDetail orginalMoveDocDetail = commonDao.load(
					WmsMoveDocDetail.class, waveDocDetail.getMoveDocDetail()
							.getId());
			WmsMoveDoc orginalMoveDoc = commonDao.load(WmsMoveDoc.class,
					orginalMoveDocDetail.getMoveDoc().getId());
			wpt = commonDao.load(WmsPickTicket.class, orginalMoveDoc
					.getPickTicket().getId());
		} else {
			wpt = taskLog.getTask().getMoveDocDetail().getMoveDoc()
					.getPickTicket();
		}

		WmsInventory inventory = getInventory(taskLog.getToLocationId(),
				taskLog.getItemKey().getId(), taskLog.getPackageUnit().getId(),
				taskLog.getInventoryStatus());
		if(inventory == null) {
			WmsPackageUnit invPackageUnit = taskLog.getPackageUnit().getItem().getPackageByLineNo(1);
			inventory = getInventory(taskLog.getToLocationId(),
					taskLog.getItemKey().getId(), invPackageUnit.getId(),
					taskLog.getInventoryStatus());
			if(inventory == null) {
				throw new BusinessException("找不到发货库存！");
			}
		}
		
		inventory.removeQuantityBU(shippedQuantityBU);

		// 写库存日志
		WmsInventoryLog log = new WmsInventoryLog(WmsInventoryLogType.SHIPPING,
				-1, wpt.getCode(), wpt.getBillType(), inventory.getLocation(),wpt.getCompany(),
				inventory.getItemKey(), shippedQuantityBU,
				taskLog.getPackageUnit(), inventory.getStatus(), "发货确认");
		commonDao.store(log);
	}

	private void getRelatedBillCodeByTask(WmsTask task, String billCode,
			WmsBillType billType) {
		if (task.getMoveDocDetail().getMoveDoc().getPickTicket() != null) {
			billCode = task.getMoveDocDetail().getMoveDoc().getPickTicket()
					.getCode();
			billType = task.getMoveDocDetail().getMoveDoc().getPickTicket()
					.getBillType();
		} else if (task.getMoveDocDetail() != null) {
			billCode = task.getMoveDocDetail().getMoveDoc()
					.getOriginalBillCode();
			billType = task.getMoveDocDetail().getMoveDoc()
					.getOriginalBillType();
		}
	}

	public void verifyLocation(String type, Long locationId) {
		WmsLocation location = load(WmsLocation.class, locationId);
		if (!WmsLocationType.STORAGE.equals(location.getType())) {
			return;
		}
		String warehouseName = location.getWarehouse().getName();
		Map problem = new HashMap();
		problem.put("出入锁类型", type);
		problem.put("库位序号", location.getId());
		problem.put("库区", location.getWarehouseArea().getCode());
		problem.put("区", location.getZone());
		problem.put("排", location.getLine());
		problem.put("列", location.getColumn());
		problem.put("层", location.getLayer());
		problem.put("校验类型", BaseStatus.VERIFY_LOCK);
		
		wmsRuleManager.execute(warehouseName, warehouseName, "库位校验规则", problem);
	}

	public void verifyLocation(String type, Long locationId,WmsPackageUnit wpu,
			Long currentItemKeyId, Long currentItemId, Double quantityBU,
			String pallet) {
		WmsLocation location = load(WmsLocation.class, locationId);
		if (!WmsLocationType.STORAGE.equals(location.getType())) {
			return;
		}
		String warehouseName = location.getWarehouse().getName();
		Map problem = new HashMap();
		problem.put("出入锁类型", type);
		problem.put("库位序号", location.getId());
		problem.put("库区", location.getWarehouseArea().getCode());
		problem.put("区", location.getZone());
		problem.put("排", location.getLine());
		problem.put("列", location.getColumn());
		problem.put("层", location.getLayer());
		problem.put("校验类型", BaseStatus.VERIFY_ALL);

		Double rate = location.getUsedRate();
		problem.put("库位占用比例", rate);

		problem.put("货品序号", currentItemId);
		problem.put("批次序号", currentItemKeyId);

		WmsItem item = load(WmsItem.class, currentItemId);
		WmsPackageUnit packageUnit = getWmsPackageUnitByLocation(location, item);
		if(packageUnit == null){
			packageUnit = wpu;
		}
		problem.put("货品重量", packageUnit.getWeight());
		problem.put("货品体积", packageUnit.getVolume());

//		String hql = "SELECT wsn.pallet FROM WmsInventoryExtend wsn WHERE wsn.inventory.location.id = :locationId AND wsn.pallet is not null AND wsn.pallet != '-'";
//		List<String> palletList = commonDao.findByQuery(hql, "locationId",
//				locationId);
//		Set<String> pallets = new HashSet<String>(palletList);
		String hql = "SELECT wsn.pallet,SUM(wsn.inventory.quantityBU+ wsn.inventory.putawayQuantityBU-wsn.inventory.allocatedQuantityBU)"
				+ " FROM WmsInventoryExtend wsn"
				+ " WHERE wsn.inventory.location.id = :locationId"
				+ " AND wsn.pallet is not null AND wsn.pallet != '-'"
				+ " GROUP BY wsn.pallet";
		List<Object[]> palletList = commonDao.findByQuery(hql, "locationId",locationId);
		Set<String> pallets = new HashSet<String>();
		Double totalQuantity = 0D; 
		for(Object[] o : palletList){
			pallets.add(o[0].toString());
			totalQuantity += o[1]==null?0D:Double.valueOf(o[1].toString());
		}
		problem.put("件数", totalQuantity);
		if (StringUtils.isNotBlank(pallet)
				&& !BaseStatus.NULLVALUE.equals(pallet)) {
			pallets.add(pallet);
			problem.put("是否托盘", "是");
		} else {
			problem.put("是否托盘", "否");
		}
		double palletQuantity = pallets.size();//location.getPalletQuantity()
		problem.put("托盘数量", palletQuantity);
		problem.put("待上架数量",
				PackageUtils.convertPackQuantity(quantityBU, packageUnit));
		wmsRuleManager.execute(warehouseName, warehouseName, "库位校验规则", problem);
	}
	

	public void refreshLocationUseRate(WmsLocation location, int inOrOut) {
		if (!WmsLocationType.STORAGE.equals(location.getType())) {
			return;
		}

		// 提取库位托数、件数、重量、体积信息
		Integer palletQuantity = location.getPalletQuantity();
		Double totalQuantity = 0D;
		Double totalWeight = 0D;
		Double totalVolume = 0D;

		String hql1 = "SELECT SUM((inv.quantityBU + inv.putawayQuantityBU) / inv.packageUnit.convertFigure),"//SUM((inv.quantityBU + inv.putawayQuantityBU) / inv.packageUnit.convertFigure)
				+ " SUM(((inv.quantityBU + inv.putawayQuantityBU) / inv.packageUnit.convertFigure) * inv.packageUnit.weight),"
				+ " SUM(((inv.quantityBU + inv.putawayQuantityBU) / inv.packageUnit.convertFigure )* inv.packageUnit.volume), "
				+ " inv.location.id "
				+ " FROM WmsInventory inv WHERE inv.location.id = :locationId "
				+ " GROUP BY inv.location.id ";
		Object[] results = (Object[]) commonDao.findByQueryUniqueResult(hql1,
				new String[] { "locationId" },
				new Object[] { location.getId() });
		if (results != null) {
			totalQuantity = (Double) results[0];
			totalWeight = (Double) results[1];
			totalVolume = (Double) results[2];
		}
		
		//计算库位占用比较，包含待上架数量
		Double rate = refreshLocationUseRate(location, palletQuantity, totalQuantity, totalWeight, totalVolume);
		location.setUsedRate(rate);
		
//		String hql = "SELECT wsn.pallet FROM WmsInventoryExtend wsn WHERE wsn.inventory.location.id = :locationId AND wsn.pallet is not null AND wsn.pallet != '-'";
//		List<String> palletList = commonDao.findByQuery(hql, "locationId",
//				location.getId());
//		
//		palletQuantity = palletList == null ? 0 : palletList.size();
//		totalQuantity = 0D;
//		totalWeight = 0D;
//		totalVolume = 0D;
//		if (results != null) {
//			totalQuantity = (Double) results[3];
//			totalWeight = (Double) results[4];
//			totalVolume = (Double) results[5];
//		}
//		//计算库位占用比较，不包含待上架数量
//		Double realUsedRate = refreshLocationUseRate(location, palletQuantity, totalQuantity, totalWeight, totalVolume);
//		location.setRealUsedRate(realUsedRate);

		// 更新库位存空拣状态
		if (location.getUsedRate().doubleValue() == 0.0) {
			location.setLocationStatus(WmsLocationStatus.EMPTY);
		} else if (inOrOut == 1) {
			location.setLocationStatus(WmsLocationStatus.STORE);
		} else if (inOrOut == -1) {
			location.setLocationStatus(WmsLocationStatus.PICK);
		}

		// 设置库位动碰标志
		location.addTouchCount();
	}
	
	private Double refreshLocationUseRate(WmsLocation location,Integer palletQuantity,Double totalQuantity ,Double totalWeight ,Double totalVolume){
		Double rate = 0D;
		String warehouseName = location.getWarehouse().getName();

		// 调用规则计算库位满度
		Map problem = new HashMap();
		problem.put("库位ID", location.getId());
		problem.put("库区", location.getWarehouseArea().getCode());
		problem.put("托数", palletQuantity);
		problem.put("件数", totalQuantity);
		problem.put("重量", totalWeight);
		problem.put("体积", totalVolume);
		problem.put("区", location.getZone());
		problem.put("排", location.getLine());
		problem.put("列", location.getColumn());
		problem.put("层", location.getLayer());
		Map<String, Object> result = wmsRuleManager.execute(warehouseName,
				warehouseName, "库位满度计算规则", problem);
		rate = Double.parseDouble(result.get("库位占比").toString());
		rate = Double.valueOf(new DecimalFormat(".##").format(rate))
				.doubleValue();
		return rate;
	}

	public void manualPallet(WmsInventoryExtend wsn, Double quantity,
			String dstPalletNo) {
		if (DoubleUtils.compareByPrecision(quantity, wsn.getQuantityBU(), wsn
				.getInventory().getPackageUnit().getPrecision()) > 0) {
			throw new OriginalBusinessException("码托数量不能超过未码托库存数！");
		}

		// 扣减原序列号数量
		wsn.removeQuantity(quantity);
		if (DoubleUtils.compareByPrecision(wsn.getQuantityBU(), 0D, wsn
				.getInventory().getPackageUnit().getPrecision()) == 0) {
			commonDao.delete(wsn);
		}
		// 生成新序列号信息
		inventoryExtendManager.addInventoryExtend(wsn.getInventory(),
				dstPalletNo, wsn.getCarton(), wsn.getSerialNo(),
				quantity);
	}

	public void autoPallet(WmsInventoryExtend inventoryExtend) {
		Double quantity = inventoryExtend.getQuantityBU();
		Map problem = new HashMap();
		problem.put("类型", "手工码托");
		Map<String, Object> result = wmsRuleManager.execute(inventoryExtend
				.getInventory().getLocation().getWarehouse().getName(),
				inventoryExtend.getInventory().getItemKey().getItem()
						.getCompany().getName(), "码托规则", problem);
		Object pallet = result.get("托盘号");

		// 扣减原序列号数量
		inventoryExtend.removeQuantity(quantity);
		if (DoubleUtils.compareByPrecision(inventoryExtend.getQuantityBU(), 0D,
				inventoryExtend.getInventory().getPackageUnit().getPrecision()) == 0) {
			commonDao.delete(inventoryExtend);
		}
		// 生成新序列号信息
		inventoryExtendManager.addInventoryExtend(
				inventoryExtend.getInventory(), pallet.toString(),
				inventoryExtend.getCarton(), inventoryExtend.getSerialNo(),
				 quantity);
	}

	public void manualSplitPallet(WmsInventoryExtend wsn, Double quantity) {
		if (!WmsStringUtils.isEmpty(wsn.getSerialNo())) {
			// 数量必须为1
			if (quantity.doubleValue() != 1) {
				throw new OriginalBusinessException("拆托数量只能为1！");
			}

			// 从托盘上取下一个序列号的库存，直接抹除序列号上的托盘号和箱号信息
			wsn.setPallet(BaseStatus.NULLVALUE);
			wsn.setCarton(BaseStatus.NULLVALUE);
		} else if (!WmsStringUtils.isEmpty(wsn.getCarton())) {
			// 不能超出箱子数量
			if (DoubleUtils.compareByPrecision(quantity, wsn.getQuantityBU(),
					wsn.getInventory().getPackageUnit().getPrecision()) > 0) {
				throw new OriginalBusinessException("拆托数量不能超过箱子库存数！");
			}

			// 从托盘一个箱子中取下的库存数量
			wsn.removeQuantity(quantity);
			if (DoubleUtils.compareByPrecision(wsn.getQuantityBU(), 0D, wsn
					.getInventory().getPackageUnit().getPrecision()) == 0) {
				commonDao.delete(wsn);
			}
			// 取下来的库存没有托盘号、箱号和序列号
			inventoryExtendManager.addInventoryExtend(wsn.getInventory(),
					BaseStatus.NULLVALUE, BaseStatus.NULLVALUE,
					BaseStatus.NULLVALUE,  quantity);
		} else {
			// 不能超出托盘数量
			if (DoubleUtils.compareByPrecision(quantity, wsn.getQuantityBU(),
					wsn.getInventory().getPackageUnit().getPrecision()) > 0) {
				throw new OriginalBusinessException("拆托数量不能超过托盘库存数！");
			}

			// 从托盘上取下一定数量的库存
			wsn.removeQuantity(quantity);
			if (DoubleUtils.compareByPrecision(wsn.getQuantityBU(), 0D, wsn
					.getInventory().getPackageUnit().getPrecision()) == 0) {
				commonDao.delete(wsn);
			}
			// 取下来的库存没有托盘号、箱号和序列号
			inventoryExtendManager.addInventoryExtend(wsn.getInventory(),
					BaseStatus.NULLVALUE, BaseStatus.NULLVALUE,
					BaseStatus.NULLVALUE,  quantity);
		}
	}

	/**
	 * 库存手工移位
	 * */
	public void inventoryManualMove(WmsInventoryExtend wsn, Long destLocId,
			Double quantityBU, Long workerId) {
		WmsLocation dstLoc = commonDao.load(WmsLocation.class, destLocId);
		WmsInventory srcInv = wsn.getInventory();
		if (DoubleUtils.compareByPrecision(quantityBU, srcInv
				.getAvailableQuantityBU(), srcInv.getPackageUnit()
				.getPrecision()) > 0) {
			throw new BusinessException("移位数量超过库存可用数量!");
		}
		if (DoubleUtils.compareByPrecision(quantityBU, wsn
				.getAvailableQuantityBU(), srcInv.getPackageUnit()
				.getPrecision()) > 0) {
			throw new BusinessException("移位数量超过托盘可用数量!");
		}
		this.inventoryQuickMove(srcInv, quantityBU, "库存手工移位", dstLoc, srcInv.getPackageUnit(), wsn);
		// 考虑整托移位或整箱移位处理逻辑-------旧的逻辑▽
		/*String newCarton = BaseStatus.NULLVALUE;
		String newPallet = BaseStatus.NULLVALUE;
		if (!WmsStringUtils.isEmpty(wsn.getCarton())) {
			newCarton = wsn.getCarton();
		} else if (!WmsStringUtils.isEmpty(wsn.getPallet())) {
			newPallet = wsn.getPallet();
		}
		List<WmsInventoryExtend> invExtends = new ArrayList<WmsInventoryExtend>();
		String hql = "FROM WmsInventoryExtend wsn WHERE wsn.pallet = :pallet";
		if (StringUtils.isNotBlank(newPallet)
				&& !BaseStatus.NULLVALUE.equals(newPallet)) {
			invExtends = commonDao.findByQuery(hql, "pallet", newPallet);
		} else if (StringUtils.isNotBlank(newCarton)
				&& !BaseStatus.NULLVALUE.equals(newCarton)) {
			hql = "FROM WmsInventoryExtend wsn WHERE wsn.carton = :carton";
			invExtends = commonDao.findByQuery(hql, "carton", newCarton);
		} else {
			invExtends.add(wsn);
		}
		for (WmsInventoryExtend invExtend : invExtends) {
			Double moveQty = invExtend.getQuantityBU();
			if (BaseStatus.NULLVALUE.equals(newCarton)
					&& BaseStatus.NULLVALUE.equals(newPallet)) {
				moveQty = quantityBU;
			}
			WmsInventory dstInv = moveInventory(false,invExtend, dstLoc, newPallet,
					newCarton, srcInv.getPackageUnit(), moveQty,
					srcInv.getStatus(), WmsInventoryLogType.MOVE, "库存手工移位");
		}*/
	}
	public void inventoryQuickMove(WmsInventory srcInv,Double moveQty,String description,
			WmsLocation toLocation,WmsPackageUnit basePackageUnit,WmsInventoryExtend srcIx){
		String pallet = srcIx.getPallet() == null ? BaseStatus.NULLVALUE : srcIx.getPallet();
		//原库存减少库存量
		srcInv.removeQuantityBU(moveQty);
		commonDao.store(srcInv);
		this.addInventoryLog(WmsInventoryLogType.MOVE, -1, null,null, srcInv.getLocation(),
				srcInv.getItemKey(),moveQty, srcInv.getPackageUnit(),srcInv.getStatus(), description);
		//目标库存增加库存量
//		WmsInventory dstInventory = this.getInventoryWithNew(toLocation, srcInv.getItemKey(), basePackageUnit);
		WmsInventory dstInventory = getInventory(toLocation.getId(),
				srcInv.getItemKey().getId(), basePackageUnit.getId(),srcInv.getStatus());
		if (dstInventory == null) {
			dstInventory = createWmsInventory(toLocation, srcInv.getItemKey(),
					basePackageUnit, 0, srcInv.getStatus());
		}
		dstInventory.addQuantityBU(moveQty);
		commonDao.store(dstInventory);
		this.addInventoryLog(WmsInventoryLogType.MOVE, 1, null,null, dstInventory.getLocation(),
				dstInventory.getItemKey(),moveQty, dstInventory.getPackageUnit(),dstInventory.getStatus(), description);
		//扣减原序列号库存信息
		srcIx.removeQuantity(moveQty);
		if (srcIx.getQuantityBU() <= 0) {
			commonDao.delete(srcIx);
		}else{
			commonDao.store(srcIx);
			pallet = BaseStatus.NULLVALUE;//没有整托移位,托盘信息不允许带走
		}
		//增加目标库位序列号
		this.addDescWie(dstInventory, pallet, toLocation, moveQty);
	}

	public WmsInventory moveInventory(WmsInventoryExtend wsn,
			WmsLocation dstLoc, String pallet, String carton,
			WmsPackageUnit packageUnit, Double moveQuantityBU, String status,
			String logType, String description) {
		return moveInventory(true, wsn, dstLoc, pallet, carton, packageUnit,
				moveQuantityBU, status, logType, description);
	}
	
	public WmsInventory moveInventory(Boolean isManual,boolean isMatchLoc ,WmsInventoryExtend wsn, WmsLocation dstLoc, String pallet, String carton,  
			WmsPackageUnit packageUnit, Double moveQuantityBU, String status, String logType, String description){
		String warehouseName = dstLoc.getWarehouse().getName();
		Boolean isSameLoc = wsn.getLocationId().equals(dstLoc.getId());
		WmsInventory srcInv = wsn.getInventory();

		if (srcInv.isValid() && srcInv.isLocked()) {
			throw new BusinessException("inventory.locked");
		}

		if (!isSameLoc) {
			// 源库位出锁检查
			if (WmsLocationType.STORAGE.equals(srcInv.getLocation().getType())) {
				verifyLocation(BaseStatus.LOCK_OUT, srcInv.getLocation().getId());
			}
			// 目标库库位入锁检查
			if (WmsLocationType.STORAGE.equals(dstLoc.getType())) {
				if(isManual){
					verifyLocation(BaseStatus.LOCK_IN, dstLoc.getId(),srcInv.getPackageUnit(), srcInv
							.getItemKey().getId(), srcInv.getItemKey().getItem()
							.getId(), moveQuantityBU, pallet);
				}
				else{
					verifyLocation(BaseStatus.LOCK_IN, srcInv.getLocation().getId());
				}
			}
		}

		// 库存移位处理
		WmsInventory dstInv = getInventoryWithNew(dstLoc, srcInv.getItemKey(),
				packageUnit, status);
		dstInv.addQuantityBU(moveQuantityBU);

		// 加工拣货
		if (logType.equals(WmsMoveDocType.MV_PROCESS_PICKING)) {
			addInventoryLog(WmsInventoryLogType.PROCESS_UP, 1,
					BaseStatus.NULLVALUE, null, dstInv.getLocation(),
					dstInv.getItemKey(), moveQuantityBU,
					dstInv.getPackageUnit(), dstInv.getStatus(), description);

			srcInv.removeQuantityBU(moveQuantityBU);
			addInventoryLog(WmsInventoryLogType.PROCESS_DOWN, -1,
					BaseStatus.NULLVALUE, null, srcInv.getLocation(),
					srcInv.getItemKey(), moveQuantityBU,
					srcInv.getPackageUnit(), srcInv.getStatus(), description);
		} else {
			addInventoryLog(WmsInventoryLogType.MOVE, 1, BaseStatus.NULLVALUE,
					null, dstInv.getLocation(), dstInv.getItemKey(),
					moveQuantityBU, dstInv.getPackageUnit(),
					dstInv.getStatus(), description);

			srcInv.removeQuantityBU(moveQuantityBU);
			addInventoryLog(WmsInventoryLogType.MOVE, -1, BaseStatus.NULLVALUE,
					null, srcInv.getLocation(), srcInv.getItemKey(),
					moveQuantityBU, srcInv.getPackageUnit(),
					srcInv.getStatus(), description);
		}

		
		// 整托移位时，源托盘库位减少数量，目标库位增加托盘数量
		if (!WmsStringUtils.isEmpty(pallet) && wsn.getPallet().equals(pallet)) {
			if (WmsLocationType.STORAGE.equals(srcInv.getLocation().getType())) {
				WmsLocation location = srcInv.getLocation();
				String hql = "SELECT COUNT(*) FROM WmsInventoryExtend wsn WHERE wsn.quantityBU > 0 AND wsn.locationId = :locationId and wsn.pallet = :pallet";
				Long count = (Long)commonDao.findByQueryUniqueResult(hql, new String[]{"locationId","pallet"}, new Object[]{location.getId(),pallet});
				if(count != null && count.intValue() == 1){
					location.removePallet(1);
				}
			}
			if (!isMatchLoc && WmsLocationType.STORAGE.equals(dstLoc.getType())) {
				WmsLocation location = dstInv.getLocation();
				String hql = "SELECT COUNT(*) FROM WmsInventoryExtend wsn WHERE wsn.quantityBU > 0 AND  wsn.locationId = :locationId and wsn.pallet = :pallet";
				Long count = (Long)commonDao.findByQueryUniqueResult(hql, new String[]{"locationId","pallet"}, new Object[]{location.getId(),pallet});
				if(count == null || count.intValue() == 0){
					location.addPallet(1);
				}
			}
		}
		
		// 扣减原序列号库存信息
		wsn.removeQuantity(moveQuantityBU);
		if (wsn.getQuantityBU() <= 0) {
			commonDao.delete(wsn);
		}else{
			wsn.unallocatePickup(moveQuantityBU);
		}
				
		// 增加目标库位序列号信息
		inventoryExtendManager.addInventoryExtend(dstInv, pallet, carton,
				wsn.getSerialNo(),
				moveQuantityBU);

		// 更新库位满度信息
		if (!isSameLoc) {
			if (WmsLocationType.STORAGE.equals(srcInv.getLocation().getType())) {
				refreshLocationUseRate(srcInv.getLocation(),-1);
			}
			if (WmsLocationType.STORAGE.equals(dstLoc.getType())) {
				refreshLocationUseRate(dstLoc,1);
			}
		}

		return dstInv;
	}

	public WmsInventory moveInventory(boolean isMatchLoc,
			WmsInventoryExtend wsn, WmsLocation dstLoc, String pallet,
			String carton, WmsPackageUnit packageUnit, Double moveQuantityBU,
			String status, String logType, String description) {
		return moveInventory(true, isMatchLoc, wsn, dstLoc, pallet, carton, packageUnit, moveQuantityBU, status, logType, description);
	}
	

	/**
	 * 根据规则检索结果进行数据处理
	 * 
	 * @param ruleTableList
	 * @param location
	 * @param requireOutput
	 * @return
	 */
	private Map<String, Object> getRuleResult(
			List<Map<String, Object>> ruleTableList, WmsLocation location,
			String[] requireOutput) {
		String area = "";
		Long[] zoneIn = new Long[2];
		Long[] lineIn = new Long[2];
		Long[] columnIn = new Long[2];
		Long[] layerIn = new Long[2];
		Boolean isIn = false;
		Map<String, Object> result = new HashMap<String, Object>();
		for (Map<String, Object> locationMap : ruleTableList) {
			area = (String) locationMap.get("库区");
			zoneIn = generateAreaArray((String) locationMap.get("区区间"));
			lineIn = generateAreaArray((String) locationMap.get("排区间"));
			columnIn = generateAreaArray((String) locationMap.get("列区间"));
			layerIn = generateAreaArray((String) locationMap.get("层区间"));
			if ((location.getWarehouseArea().getCode().equals(area))
					&& (location.getZone().longValue() >= zoneIn[0].longValue() && location
							.getZone().longValue() <= zoneIn[1].longValue())
					&& (location.getLine().longValue() >= lineIn[0].longValue() && location
							.getLine().longValue() <= lineIn[1].longValue())
					&& (location.getColumn().longValue() >= columnIn[0]
							.longValue() && location.getColumn().longValue() <= columnIn[1]
							.longValue())
					&& (location.getLayer().longValue() >= layerIn[0]
							.longValue() && location.getLayer().longValue() <= layerIn[1]
							.longValue())) {
				isIn = true;
				if (requireOutput != null) {
					for (int i = 0; i < requireOutput.length; i++) {
						result.put(requireOutput[i],
								locationMap.get(requireOutput[i]));
					}
				}
				break;
			}
		}
		result.put("是否存在", isIn);
		return result;

	}

	/**
	 * 写入库存日志
	 * 
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
	 * @return
	 */
	public WmsInventoryLog addInventoryLog(String logType, int incOrDec,
			String relatedBill, WmsBillType billType, WmsLocation location,
			WmsItemKey itemKey, Double occurQuantity, WmsPackageUnit unit,
			String status, String description) {
		WmsInventoryLog log = new WmsInventoryLog(logType, incOrDec,
				relatedBill, billType, location, itemKey.getLotInfo().getSupplier(),itemKey,
				occurQuantity, unit, status, description);
		commonDao.store(log);
		return log;
	}

	public void deleteInvalidInventory() {
		// 删除与0库存inventory有关联的serialNO
		List<WmsInventoryExtend> wsns = commonDao.findByQuery("FROM WmsInventoryExtend wsn WHERE wsn.inventory.quantityBU = 0 "
				+ " AND wsn.inventory.putawayQuantityBU = 0 AND wsn.inventory.allocatedQuantityBU = 0 ");
		if(wsns!=null && wsns.size()>0){
			commonDao.deleteAll(wsns);
		}
		// 删除0库存inventory
		List<WmsInventory> invs = commonDao.findByQuery("FROM WmsInventory inv WHERE inv.quantityBU = 0 "
				+ " AND inv.putawayQuantityBU = 0 AND inv.allocatedQuantityBU = 0 ");
		if(invs!=null && invs.size()>0){
			commonDao.deleteAll(invs);
		}
			
//		String hql_wsn = "DELETE FROM WmsInventoryExtend wsn WHERE wsn.inventory.quantityBU = 0 "
//				+ " AND wsn.inventory.putawayQuantityBU = 0 AND wsn.inventory.allocatedQuantityBU = 0 ";
//		commonDao.executeByHql(hql_wsn, new String[] {}, new Object[] {});
		
//		String hql_inv = "DELETE FROM WmsInventory inv WHERE inv.quantityBU = 0 "
//				+ " AND inv.putawayQuantityBU = 0 AND inv.allocatedQuantityBU = 0 ";
//		commonDao.executeByHql(hql_inv, new String[] {}, new Object[] {});
	}

	public void lockInventory(WmsInventoryExtend wsn, Long itemStateId,
			String lockType, String quantityBu) {
		Object obj = getObjectToLock(wsn, lockType);
		WmsItemState itemState = commonDao
				.load(WmsItemState.class, itemStateId);
		Double tempQuantityBu = StringUtils.isEmpty(quantityBu) ? null : Double
				.parseDouble(quantityBu);
		if (tempQuantityBu <= 0D) {
			return;
		}
		// 如果未选定锁定类型，则对当前库存来锁定库存
		if (StringUtils.isEmpty(lockType)) {
			if (null != tempQuantityBu && tempQuantityBu > wsn.getQuantityBU()) {
				throw new BusinessException("超出锁定数量");
			}
			lockInventory(wsn, itemState, "LOCK", tempQuantityBu);
		} else {
			// 查找满足集合里条件的所有库存记录，并锁定
			lockInventoryByType(obj, itemState, lockType, "LOCK",
					tempQuantityBu);
		}
	}

	public void unlockInventory(WmsInventoryExtend wsn, Long itemStateId,
			String lockType, String quantityBu) {
		Object obj = getObjectToLock(wsn, lockType);
		WmsItemState itemState = commonDao
				.load(WmsItemState.class, itemStateId);
		Double tempQuantityBu = StringUtils.isEmpty(quantityBu) ? null : Double
				.parseDouble(quantityBu);
		if (tempQuantityBu <= 0D) {
			return;
		}
		// 如果未选定锁定类型，则对当前库存解锁
		if (StringUtils.isEmpty(lockType)) {
			if (null != tempQuantityBu && tempQuantityBu > wsn.getQuantityBU()) {
				throw new BusinessException("超出解锁数量");
			}
			lockInventory(wsn, itemState, "UNLOCK", tempQuantityBu);
		} else {
			// 查找满足集合里条件的所有库存记录，并锁定
			lockInventoryByType(obj, itemState, lockType, "UNLOCK",
					tempQuantityBu);
		}
	}

	private Object getObjectToLock(WmsInventoryExtend wsn, String lockType) {
		// 库位锁：锁定和选中记录库位相同的所有记录
		if (WmsInventoryLockType.LOCATION.equals(lockType))
			// 找出所有被选中记录的库位，并保存进集合
			return wsn.getInventory().getLocation();
		// 货主锁
		else if (WmsInventoryLockType.COMPANY.equals(lockType))
			return wsn.getItem().getCompany();
		// 货品锁
		else if (WmsInventoryLockType.ITEM.equals(lockType))
			return wsn.getItem();
		// 批次锁
		else if (WmsInventoryLockType.LOT.equals(lockType))
			return wsn.getInventory().getItemKey();
		// 收货单号锁
		else if (WmsInventoryLockType.SOI.equals(lockType))
			return wsn.getInventory().getItemKey().getLotInfo();

		return null;
	}

	private void lockInventoryByType(Object object, WmsItemState itemState,
			String lockType, String beLock, Double quantityBu) {
		String sql = "";
		if (WmsInventoryLockType.LOCATION.equals(lockType)) {
			// 库位锁
			WmsLocation location = (WmsLocation) object;
			sql = "FROM WmsInventoryExtend wsn WHERE wsn.locationId = "
					+ location.getId();
		} else if (WmsInventoryLockType.ITEM.equals(lockType)) {
			// 货品锁
			WmsItem item = (WmsItem) object;
			sql = "FROM WmsInventoryExtend wsn WHERE wsn.item.id = "
					+ item.getId();
		} else if (WmsInventoryLockType.COMPANY.equals(lockType)) {
			// 货主锁
			WmsOrganization organization = (WmsOrganization) object;
			sql = "FROM WmsInventoryExtend wsn WHERE wsn.item.company.id = "
					+ organization.getId();
		} else if (WmsInventoryLockType.LOT.equals(lockType)) {
			// 批次锁
			WmsItemKey itemKey = (WmsItemKey) object;
			sql = "FROM WmsInventoryExtend wsn WHERE wsn.inventory.itemKey.id = "
					+ itemKey.getId();
		} else {
			// 收货单号锁
			LotInfo lotInfo = (LotInfo) object;
			if (StringUtils.isEmpty(lotInfo.getSoi())) {
				return;
			} else {
				sql = "FROM WmsInventoryExtend wsn WHERE wsn.inventory.itemKey.lotInfo.soi = '"
						+ lotInfo.getSoi() + "'";
			}
		}

		if (beLock.equals("LOCK")) {
			// 只对有数量的库存进行锁定操作
			sql += " AND wsn.quantityBU>0";
			// 无待作业数量的库存才需要锁定
			// sql
			// +="AND inv.quantityBU>0 AND inv.putawayQuantityBU=0 AND allocatedQuantityBU=0";
		} else {
			// 只对包含该状态的库存进行解锁
			sql += " AND (wsn.inventory.status LIKE '%" + itemState.getName()
					+ "%') ";
		}

		List<WmsInventoryExtend> wsns = this.commonDao.findByQuery(sql);
		Double tempNum = 0.0;
		for (WmsInventoryExtend WmsInventoryExtend : wsns) {
			tempNum += WmsInventoryExtend.getQuantityBU();
		}
		quantityBu = quantityBu == null ? tempNum : quantityBu;
		// 判断可锁定数量是否超出可锁定总数量
		if (quantityBu.doubleValue() > tempNum) {
			throw new BusinessException("超出可锁定数量");
		}
		for (WmsInventoryExtend wsn : wsns) {
			if (quantityBu.doubleValue() > 0
					&& quantityBu.doubleValue() > wsn.getQuantityBU()) {
				quantityBu = quantityBu - wsn.getQuantityBU();
				lockInventory(wsn, itemState, beLock, wsn.getQuantityBU());

			} else if (quantityBu.doubleValue() > 0
					&& quantityBu.doubleValue() <= wsn.getQuantityBU()) {
				lockInventory(wsn, itemState, beLock, quantityBu);
				quantityBu = 0.0;
				return;
			}
		}
	}

	private void lockInventory(WmsInventoryExtend wsn, WmsItemState itemState,
			String beLock, Double quantityBu) {
		String logType = "";
		String description = "";
		String status = "";
		Double quantityBU = quantityBu == null ? wsn.getQuantityBU()
				: quantityBu;

		WmsInventory srcInv = wsn.getInventory();
		// 如果序列号对应库存数量已被占用，则放弃锁定/解锁作业
		if (srcInv.getAllocatedQuantityBU() > 0
				|| srcInv.getPutawayQuantityBU() > 0) {
			throw new BusinessException("当前库存已被分配,不能进行锁定解锁操作！");
		}

		if (beLock.equals("LOCK")) {
			logType = WmsInventoryLogType.LOCK;
			status = WmsStringUtils.addStatus(srcInv.getStatus(),
					itemState.getName());
			description = "按状态锁定库存:" + itemState.getName();
		} else {
			logType = WmsInventoryLogType.UNLOCK;
			status = WmsStringUtils.removeStatus(srcInv.getStatus(),
					itemState.getName());
			description = "按状态解锁库存：" + itemState.getName();
		}

		// 对inventory进行拆分
		WmsInventory lockInv = getInventoryWithNew(srcInv.getLocation(),
				srcInv.getItemKey(), srcInv.getPackageUnit(), status);
		lockInv.addQuantityBU(quantityBU);
		addInventoryLog(logType, 1, BaseStatus.NULLVALUE, null,
				lockInv.getLocation(), lockInv.getItemKey(), quantityBU,
				lockInv.getPackageUnit(), status, description);
		srcInv.removeQuantityBU(quantityBU);
		addInventoryLog(logType, -1, BaseStatus.NULLVALUE, null,
				srcInv.getLocation(), srcInv.getItemKey(), quantityBU,
				srcInv.getPackageUnit(), srcInv.getStatus(), description);

		// 对serialNo进行拆分
		WmsInventoryExtend lockWsn = inventoryExtendManager.addInventoryExtend(
				lockInv, wsn, quantityBU);
		wsn.removeQuantity(quantityBU);
		if (DoubleUtils.compareByPrecision(wsn.getQuantityBU(), 0D, wsn
				.getInventory().getPackageUnit().getPrecision()) == 0) {
			commonDao.delete(wsn);
		}
	}

	private Long[] generateAreaArray(String areaStr) {
		Long[] result = new Long[2];
		String strTemp = areaStr;
		String[] strArray = strTemp.split(",");
		result[0] = Long.parseLong(strArray[0].substring(1));
		result[1] = Long.parseLong(strArray[1].substring(0,
				strArray[1].indexOf(")")));
		return result;
	}

	private Double getMaxRate(Double qtyRate, Double weightRate,
			Double volumnRate) {
		return ((qtyRate.doubleValue() >= weightRate.doubleValue()) ? qtyRate
				: weightRate) >= volumnRate.doubleValue() ? ((qtyRate
				.doubleValue() >= weightRate.doubleValue()) ? qtyRate
				: weightRate) : volumnRate;
	}

	public void compute() {
		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DATE, -1);

		List<Long> warehouseIds = commonDao
				.findByQuery("select id from WmsWarehouse wh where wh.status = 'ENABLED'");

		for (Long warehouseId : warehouseIds) {
			try {
				compute(calendar.getTime(), warehouseId);
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void initdayend(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar = DateUtils.truncate(calendar, Calendar.DATE);
		Date computeDate = calendar.getTime();
		String queryStorageDaily = "SELECT COUNT(*) From WmsStorageDaily sd";
		List sdCount = commonDao.findByQuery(queryStorageDaily);
		Long allCount = (Long) sdCount.get(0);
		if (allCount > 0) {
			throw new OriginalBusinessException("存在日结数据，不允许初始化！");
		}
		String hql = "SELECT inventory.itemKey.id,SUM(inventory.quantityBU) "
				+ " FROM WmsInventory inventory "
				+ " WHERE inventory.location.warehouse.id = :warehouseid "
				+ " GROUP BY inventory.itemKey.id";
		List wis = commonDao.findByQuery(hql, new String[] { "warehouseid" },
				new Object[] { WmsWarehouseHolder.getWmsWarehouse().getId() });
		for (Object wi : wis) {
			Object[] invs = (Object[]) wi;
			WmsStorageDaily storageDaily = EntityFactory
					.getEntity(WmsStorageDaily.class);
			storageDaily.setComputeDate(date);
			storageDaily.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
			storageDaily.setItemKey(commonDao.load(WmsItemKey.class,
					(Long) invs[0]));
			storageDaily.setPreviousQuantityBU((Double) invs[1]);
			storageDaily.setReceiveQuantityBU(Double.valueOf(0));
			storageDaily.setShipQuantityBU(Double.valueOf(0));
			storageDaily.setProcessIncQuantityBU(Double.valueOf(0));
			storageDaily.setProcessDecQuantityBU(Double.valueOf(0));
			storageDaily.setCountAdjustQuantityBU(Double.valueOf(0));
			storageDaily.setLeftQuantityBU((Double) invs[1]);
			commonDao.store(storageDaily);
		}
	}

	public void inventorydayend(Date beginDate, Date endDate) {
		Date[] dateArray = DateUtil.getDateArrays(beginDate, endDate,
				Calendar.DAY_OF_YEAR);
		for (Date sdate : dateArray) {
			compute(sdate, WmsWarehouseHolder.getWmsWarehouse().getId());
		}
	}

	public void compute(Date date, Long wmsWarehouseId) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar = DateUtils.truncate(calendar, Calendar.DATE);
		Date currentDate = calendar.getTime();

		calendar.add(Calendar.DATE, -1);
		Date beforeOneDate = calendar.getTime();

		calendar.add(Calendar.DATE, 2);
		Date afterOneDate = calendar.getTime();

		// 删除当天该库区的StorageDaily数据
		String hql = "from WmsStorageDaily where warehouse.id=:warehouseId and computeDate=:curDate";
		List<WmsStorageDaily> storageDailys = commonDao.findByQuery(hql,
				new String[] { "warehouseId", "curDate" }, new Object[] {
						wmsWarehouseId, currentDate });
		if (storageDailys != null && storageDailys.size() > 0) {
			commonDao.deleteAll(storageDailys);
		}

		logger.info("Start compute storageDaily" + currentDate);

		// 拷贝前一天的数据作为当天的初始数据
		long startTimeMillis = System.currentTimeMillis();
		logger.debug("Duplicate previous storageDaily!");
		String queryPreStorageDaily = "from WmsStorageDaily where warehouse.id=:warehouseId and computeDate=:computeDate";
		List<WmsStorageDaily> preStorageDailys = commonDao.findByQuery(
				queryPreStorageDaily, new String[] { "warehouseId",
						"computeDate" }, new Object[] { wmsWarehouseId,
						beforeOneDate });
		List<WmsStorageDaily> curStorageDailys = new ArrayList<WmsStorageDaily>();
		for (WmsStorageDaily preStorageDaily : preStorageDailys) {
			WmsStorageDaily curStorageDaily = EntityFactory
					.getEntity(WmsStorageDaily.class);
			curStorageDaily.setComputeDate(currentDate);
			curStorageDaily.setItemKey(preStorageDaily.getItemKey());
			curStorageDaily.setWarehouse(preStorageDaily.getWarehouse());
			curStorageDaily.setPreviousQuantityBU(preStorageDaily
					.getLeftQuantityBU());
			curStorageDaily.setLeftQuantityBU(preStorageDaily
					.getLeftQuantityBU());
			curStorageDaily.setShipQuantityBU(Double.valueOf(0));
			curStorageDaily.setReceiveQuantityBU(Double.valueOf(0));
			curStorageDaily.setProcessDecQuantityBU(Double.valueOf(0));
			curStorageDaily.setProcessIncQuantityBU(Double.valueOf(0));
			curStorageDaily.setCountAdjustQuantityBU(Double.valueOf(0));
			commonDao.store(curStorageDaily);
			curStorageDailys.add(curStorageDaily);
		}
		preStorageDailys = null;
		logger.debug("Duplicate previous storageDaily finished!");

		logger.debug("Compute inventoryLog!");
		String queryInventoryLogHql = "from WmsInventoryLog log where log.updateInfo.createdTime <=:endTime "
				+ " and log.updateInfo.createdTime>=:startTime and log.warehouse.id=:warehouseId "
				+ " and log.logType NOT IN ('MOVE','CONVERT_PACKAGEUNIT','LOCK','UNLOCK','QUALITY')";
		List<WmsInventoryLog> inventoryLogs = commonDao.findByQuery(
				queryInventoryLogHql, new String[] { "endTime", "startTime",
						"warehouseId" }, new Object[] { afterOneDate,
						currentDate, wmsWarehouseId });
		for (WmsInventoryLog inventoryLog : inventoryLogs) {
			WmsStorageDaily tempStorageDaily = EntityFactory
					.getEntity(WmsStorageDaily.class);
			tempStorageDaily.setComputeDate(currentDate);
			tempStorageDaily.setWarehouse(inventoryLog.getWarehouse());
			WmsItemKey itemKey = load(WmsItemKey.class,
					inventoryLog.getItemKeyId());
			tempStorageDaily.setItemKey(itemKey);
			tempStorageDaily.setReceiveQuantityBU(Double.valueOf(0));
			tempStorageDaily.setShipQuantityBU(Double.valueOf(0));
			tempStorageDaily.setProcessIncQuantityBU(Double.valueOf(0));
			tempStorageDaily.setProcessDecQuantityBU(Double.valueOf(0));
			tempStorageDaily.setCountAdjustQuantityBU(Double.valueOf(0));
			if (inventoryLog.getLogType().equals(WmsInventoryLogType.RECEIVING)) {
				tempStorageDaily.setReceiveQuantityBU(inventoryLog
						.getQuantityBU());
			} else if (inventoryLog.getLogType().equals(
					WmsInventoryLogType.SHIPPING)) {
				tempStorageDaily
						.setShipQuantityBU(inventoryLog.getQuantityBU());
			} else if (inventoryLog.equals(WmsInventoryLogType.PROCESS_DOWN)) {
				tempStorageDaily.setProcessDecQuantityBU(inventoryLog
						.getQuantityBU());
			} else if (inventoryLog.getLogType().equals(
					WmsInventoryLogType.PROCESS_UP)) {
				tempStorageDaily.setProcessIncQuantityBU(inventoryLog
						.getQuantityBU());
			} else if (inventoryLog.getLogType().equals(
					WmsInventoryLogType.COUNT_ADJUST)
					|| inventoryLog.getLogType().equals(
							WmsInventoryLogType.INVENTORY_ADJUST)) {
				tempStorageDaily.setCountAdjustQuantityBU(inventoryLog
						.getQuantityBU());
			}
			WmsStorageDaily daily = null;
			if (curStorageDailys.contains(tempStorageDaily)) {
				daily = curStorageDailys.get(curStorageDailys
						.indexOf(tempStorageDaily));
				daily.setShipQuantityBU(daily.getShipQuantityBU()
						+ tempStorageDaily.getShipQuantityBU());
				daily.setReceiveQuantityBU(daily.getReceiveQuantityBU()
						+ tempStorageDaily.getReceiveQuantityBU());
				daily.setProcessDecQuantityBU(daily.getProcessDecQuantityBU()
						+ tempStorageDaily.getProcessDecQuantityBU());
				daily.setProcessIncQuantityBU(daily.getProcessIncQuantityBU()
						+ tempStorageDaily.getProcessIncQuantityBU());
				daily.setCountAdjustQuantityBU(daily.getCountAdjustQuantityBU()
						+ tempStorageDaily.getCountAdjustQuantityBU());
				daily.setLeftQuantityBU((daily.getPreviousQuantityBU()
						+ daily.getReceiveQuantityBU()
						+ daily.getProcessIncQuantityBU() + daily
							.getCountAdjustQuantityBU())
						- (daily.getShipQuantityBU() + daily
								.getProcessDecQuantityBU()));
			} else {
				daily = EntityFactory.getEntity(WmsStorageDaily.class);
				daily.setPreviousQuantityBU(Double.valueOf(0));
				daily.setReceiveQuantityBU(Double.valueOf(0));
				daily.setShipQuantityBU(Double.valueOf(0));
				daily.setProcessIncQuantityBU(Double.valueOf(0));
				daily.setProcessDecQuantityBU(Double.valueOf(0));
				daily.setCountAdjustQuantityBU(Double.valueOf(0));
				daily.setWarehouse(inventoryLog.getWarehouse());
				daily.setComputeDate(currentDate);
				daily.setItemKey(itemKey);
				String queryLeftQuantityHql = "select leftQuantityBU from WmsStorageDaily where computeDate=:computeDate and warehouse.id=:warehouseId and itemKey.id=:itemKeyId";
				Double leftQuantity = (Double) commonDao
						.findByQueryUniqueResult(queryLeftQuantityHql,
								new String[] { "computeDate", "warehouseId",
										"itemKeyId" }, new Object[] {
										currentDate,
										inventoryLog.getWarehouse().getId(),
										inventoryLog.getItemKeyId() });
				if (leftQuantity != null) {
					tempStorageDaily.setPreviousQuantityBU(leftQuantity);
				}
				daily.setReceiveQuantityBU(daily.getReceiveQuantityBU()
						+ tempStorageDaily.getReceiveQuantityBU());
				daily.setShipQuantityBU(daily.getShipQuantityBU()
						+ tempStorageDaily.getShipQuantityBU());
				daily.setProcessDecQuantityBU(daily.getProcessDecQuantityBU()
						+ tempStorageDaily.getProcessDecQuantityBU());
				daily.setProcessIncQuantityBU(daily.getProcessIncQuantityBU()
						+ tempStorageDaily.getProcessIncQuantityBU());
				daily.setCountAdjustQuantityBU(daily.getCountAdjustQuantityBU()
						+ tempStorageDaily.getCountAdjustQuantityBU());
				daily.setLeftQuantityBU((daily.getPreviousQuantityBU()
						+ daily.getReceiveQuantityBU()
						+ daily.getProcessIncQuantityBU() + daily
							.getCountAdjustQuantityBU())
						- (daily.getShipQuantityBU() + daily
								.getProcessDecQuantityBU()));
				curStorageDailys.add(daily);
			}
			commonDao.store(daily);
		}
		logger.debug("Compute inventoryLog finished!");
		inventoryLogs = null;
		logger.info("Compute storageDaily finished");
	}

	public void inventoryFeeAutoCompute() {
		List<WmsInventoryCount> inventoryCounts = commonDao
				.findByQuery("from WmsInventoryCount wic");
		Long warehouseId = 0L;
		Long companyId = 0L;
		Long itemId = 0L;
		Set<String> temp = new HashSet<String>();
		String checkStr = "";
		if (inventoryCounts != null && !inventoryCounts.isEmpty()) {
			for (WmsInventoryCount wic : inventoryCounts) {
				/**
				 * 费用计算：托数*每托费率(给定) 托数=件数*折算率 折算率通过查码托策略表得出
				 * 所需参数：件数，货主，货品分类class3，货品包装(如：箱)
				 * */
				Map<String, Object> problem = new HashMap<String, Object>();
				String feeType = "仓租费";
				problem.put("类型", feeType);
				problem.put("货主", wic.getCompany().getName());
				problem.put("码托数", wic.getPalletQuantity());

				Date storageDate = new Date();
				Date recordDate = wic.getRecordDate();
				Long distDays = getDistDates(storageDate, recordDate);

				problem.put("库龄", distDays);

				String companyName = wic.getCompany().getName();
				String referenceRule = "计费规则";

				Map<String, Object> result = null;
				result = wmsRuleManager.execute(wic.getWarehouse().getName(),
						companyName, referenceRule, problem);

				if (result.get(feeType) != null) {
					// 插入费用表
					autoInsertWarehouseHireFee(result, new Date(),
							wic.getWarehouse(), wic.getCompany(),
							wic.getItem(), wic.getQuantityBU(),
							wic.getWeight(), wic.getVolume(), feeType);
				}
			}
		}
	}

	/**
	 * 返回两个日期相差的天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	private long getDistDates(Date startDate, Date endDate) {
		long totalDate = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		long timestart = calendar.getTimeInMillis();
		calendar.setTime(endDate);
		long timeend = calendar.getTimeInMillis();
		totalDate = Math.abs((timeend - timestart)) / (1000 * 60 * 60 * 24);
		return totalDate;
	}

	private void autoInsertWarehouseHireFee(Map<String, Object> result,
			Date recordDate, WmsWarehouse warehouse, WmsOrganization company,
			WmsItem item, Double quantityBU, Double weight, Double volume,
			String feeType) {
		WmsInventoryFee wif = EntityFactory.getEntity(WmsInventoryFee.class);
		wif.setFeeType(feeType);
		wif.setFee(Double.parseDouble(result.get(feeType).toString()));
		wif.setFeeDate(recordDate);
		wif.setWarehouse(warehouse);
		wif.setCompany(company);
		wif.setItem(item);
		wif.setQuantity(quantityBU);
		wif.setWeight(weight);
		wif.setVolume(volume);
		wif.setFeeRate((Double) result.get("费率"));
		this.commonDao.store(wif);
	}

	public void inventoryCountAutoCompute() {
		List<Long> warehouseIds = commonDao
				.findByQuery("select id from WmsWarehouse wh where wh.status = 'ENABLED'");
		for (Long warehouseId : warehouseIds) {
			try {
				if (warehouseId != null) {
					inventoryCountAutoCompute(warehouseId);
					Thread.sleep(1000);
				} else {
					continue;
				}
			} catch (Exception e) {
				ExceptionLog log = EntityFactory.getEntity(ExceptionLog.class);
				// 记录计费错误日志
				log.setOperDate(new Date());
				log.setOperExceptionMess(warehouseId + "仓库的库存结存数据采集有误");
				// log.setOperUserId(UserHolder.getUser().getId());
				log.setOperUserName("后台进程");
				log.setOperPageId("后台进程");
				log.setOperPageName("库存结存数据采集");
				log.setOperComponentId("DefaultWmsInventory");
				log.setOperComponentName("inventoryCountAutoCompute");
				commonDao.store(log);
				e.printStackTrace();
			}
		}
	}

	private void inventoryCountAutoCompute(Long warehouseId) {
		WmsWarehouse curWareHouse = commonDao.load(WmsWarehouse.class,
				warehouseId);

		String hql = "SELECT inventory FROM WmsInventory inventory "
				+ "INNER JOIN inventory.location location "
				+ "INNER JOIN inventory.location.warehouse warehouse "
				+ "WHERE warehouse.id = :curWareHouseId ";
		List<WmsInventory> wis = commonDao.findByQuery(hql, "curWareHouseId",
				curWareHouse.getId());
		for (WmsInventory wi : wis) {
			if (wi.getQuantityBU() > 0) {
				WmsInventoryCount wc = EntityFactory
						.getEntity(WmsInventoryCount.class);
				wc.setWarehouse(curWareHouse);
				wc.setCompany(wi.getItemKey().getItem().getCompany());
				wc.setItem(wi.getItemKey().getItem());
				wc.setQuantityBU(wi.getQuantityBU());
				wc.setWeight(wi.getQuantityBU()
						* wi.getPackageUnit().getWeight());
				wc.setVolume(wi.getQuantityBU()
						* wi.getPackageUnit().getVolume());
				wc.setPalletQuantity(1D);
				wc.setRecordDate(new Date());
				commonDao.store(wc);
			}
		}
		//
		//
		//
		//
		// Set<WmsWarehouseArea> areas= curWareHouse.getWarehouseAreas();
		// for(WmsWarehouseArea area : areas){
		// String hql =
		// "FROM WmsLocation location WHERE location.warehouseArea.id = :warehouseAreaId";
		// List<WmsLocation> locations = commonDao.findByQuery(hql,
		// "warehouseAreaId", area.getId());
		// for(WmsLocation loc : locations){
		// hql =
		// "FROM WmsInventory inventory WHERE inventory.location.id = :locationId";
		//
		// List<WmsInventory> wis= commonDao.findByQuery(hql, "locationId",
		// loc.getId());
		// for(WmsInventory wi : wis){
		// WmsInventoryCount wc =
		// EntityFactory.getEntity(WmsInventoryCount.class);
		// wc.setWarehouse(curWareHouse);
		// wc.setCompany(wi.getItemKey().getItem().getCompany());
		// wc.setItem(wi.getItemKey().getItem());
		// wc.setQuantityBU(wi.getQuantityBU());
		// wc.setWeight(wi.getQuantityBU() * wi.getPackageUnit().getWeight());
		// wc.setVolume(wi.getQuantityBU() * wi.getPackageUnit().getVolume());
		// wc.setPalletQuantity(1D);
		// wc.setRecordDate(new Date());
		// // wc.setStorageDate(new Date());
		// commonDao.store(wc);
		// }
		// }
		// }
	}

	/**
	 * 盘点--库存数量差异调整
	 * */
	public void quantityAdjust(WmsCountRecord countRecord) {
		WmsPackageUnit wpu = countRecord.getItemKey().getItem()
				.getPackageByLineNo(1);

		WmsLocation countLoc = getCountLocationByWarehouse(countRecord
				.getCountPlan().getWarehouse());
		if (countLoc == null) {
			throw new OriginalBusinessException("找不到盘点库位！");
		}

		double deltaQuantity = countRecord.getDeltaQuantityBU();
		if (countRecord.getInventoryId() != null) {
			// 已有库存的数量修改
			WmsInventory srcInv = commonDao.load(WmsInventory.class,
					countRecord.getInventoryId());
			srcInv.removeQuantityBU(countRecord.getDeltaQuantityBU());
			commonDao.store(srcInv);
			WmsLocation location = srcInv.getLocation();

			// 序列号表数量调整
			String hashCode = BeanUtils.getFormat(srcInv.getId(),
					countRecord.getPallet(), countRecord.getCarton(),
					countRecord.getSerialNo());
			WmsInventoryExtend wsn = (WmsInventoryExtend) commonDao
					.findByQueryUniqueResult(
							"FROM WmsInventoryExtend wsn WHERE wsn.hashCode = :hashCode",
							new String[] { "hashCode" },
							new Object[] { hashCode });
			if (wsn == null) {
				throw new OriginalBusinessException("找不到库存对应序列号信息，无法差异调整！");
			} else {
				wsn.removeQuantity(countRecord.getDeltaQuantityBU());
				if (DoubleUtils.compareByPrecision(wsn.getQuantityBU(), 0D,
						srcInv.getPackageUnit().getPrecision()) == 0) {
					commonDao.delete(wsn);
				}
				// 刷新库位上托盘数量(解决库位上托盘全部盘空的情况)
				Long count = (Long)commonDao
						.findByQueryUniqueResult(
								"SELECT COUNT(*) FROM WmsInventoryExtend wsn "
										+ " WHERE wsn.locationId = :locationId AND wsn.pallet <> '-' "
										+ " GROUP BY wsn.pallet",
								new String[] { "locationId" },
								new Object[] { wsn.getLocationId() });
				Integer palletQty = count == null ? 0 : count.intValue();
				location.setPalletQuantity(palletQty);
			}

			// 写库存日志
			addInventoryLog(WmsInventoryLogType.MOVE, -1, countRecord
					.getCountPlan().getCode(), null, srcInv.getLocation(),
					srcInv.getItemKey(), countRecord.getDeltaQuantityBU(),
					srcInv.getPackageUnit(), srcInv.getStatus(), "盘点差异调整");

			// 刷新库满度
			refreshLocationUseRate(srcInv.getLocation(), -1);

			// 创建盘点库存
			WmsInventory countInv = getInventoryWithNew(countLoc,
					countRecord.getItemKey(), wpu, BaseStatus.NULLVALUE);
			countInv.addQuantityBU(countRecord.getDeltaQuantityBU());
			// 写盘点库位的库存日志
			addInventoryLog(WmsInventoryLogType.MOVE, 1, countRecord
					.getCountPlan().getCode(), null, countLoc,
					countInv.getItemKey(), countRecord.getDeltaQuantityBU(),
					countInv.getPackageUnit(), countInv.getStatus(), "盘点差异调整");

		} else {
			// 增加新库存
			WmsInventory srcInv = getInventoryWithNew(
					commonDao.load(WmsLocation.class,
							countRecord.getLocationId()),
					countRecord.getItemKey(), wpu, BaseStatus.NULLVALUE);
			srcInv.addQuantityBU(countRecord.getCountQuantityBU());

			WmsLocation location = srcInv.getLocation();
			// 新增序列号信息
			inventoryExtendManager.addInventoryExtend(srcInv,
					countRecord.getPallet(), countRecord.getCarton(),
					countRecord.getSerialNo(),
					countRecord.getCountQuantityBU());
			// 刷新库位上托盘数量
			Long count = (Long)commonDao
					.findByQueryUniqueResult(
							"SELECT COUNT(*) FROM WmsInventoryExtend wsn "
									+ " WHERE wsn.locationId = :locationId AND wsn.pallet <> '-' "
									+ " GROUP BY wsn.pallet",
							new String[] { "locationId" },
							new Object[] { location.getId()});
			Integer palletQty = count == null ? 0 : count.intValue();
			location.setPalletQuantity(palletQty);

			// 写库存日志
			addInventoryLog(WmsInventoryLogType.MOVE, -1, countRecord
					.getCountPlan().getCode(), null, srcInv.getLocation(),
					srcInv.getItemKey(), countRecord.getCountQuantityBU(),
					srcInv.getPackageUnit(), srcInv.getStatus(), "盘点差异调整");

			// 刷新库满度
			refreshLocationUseRate(srcInv.getLocation(), -1);

			// 创建盘点库存
			WmsInventory countInv = getInventoryWithNew(countLoc,
					countRecord.getItemKey(), wpu, BaseStatus.NULLVALUE);
			countInv.addQuantityBU(countRecord.getDeltaQuantityBU());
			// 写盘点库位的库存日志
			addInventoryLog(WmsInventoryLogType.MOVE, 1, countRecord
					.getCountPlan().getCode(), null, countLoc,
					countInv.getItemKey(), countRecord.getDeltaQuantityBU(),
					countInv.getPackageUnit(), countInv.getStatus(), "盘点差异调整");
		}
	}

	public WmsLocation getCountLocationByWarehouse(WmsWarehouse warehouse) {
		String hql = "FROM WmsLocation location "
				+ " WHERE location.type = :type "
				+ " AND location.warehouse.id=:warehouseId "
				+ " AND location.status = 'ENABLED' "
				+ " ORDER BY location.code";
		List<WmsLocation> countLocations = commonDao.findByQuery(hql,
				new String[] { "type", "warehouseId" }, new Object[] {
						WmsLocationType.COUNT, warehouse.getId() });
		if (countLocations == null || countLocations.size() <= 0) {
			return null;
		}
		return countLocations.get(0);
	}

	public void modifyInventory(WmsInventoryExtend wsn, double adjustQtyBU,
			String description) {
		wsn = commonDao.load(WmsInventoryExtend.class, wsn.getId());
		if (!WmsStringUtils.isEmpty(wsn.getSerialNo())) {
			if (adjustQtyBU == 1) {
				// 序列号跟踪时，数量只能为1，无需修改
				return;
			} else if (adjustQtyBU != 0) {
				throw new OriginalBusinessException("序列号追踪时，只能修改数量为0");
			}
		}

		// 盘点中的库位不能调整数量
		WmsInventory inventory = wsn.getInventory();
		if (wsn.getInventory().getLocation().getLockCount()) {
			throw new BusinessException("location.locked");
		}

		// 调整数量有效性校验
		double deltaQuantity = adjustQtyBU - wsn.getQuantityBU().doubleValue();
		if (adjustQtyBU < inventory.getAllocatedQuantityBU()) {
			throw new BusinessException("inventory.quantity.insufficient");
		}

		// 修改总库存数
		inventory.addQuantityBU(deltaQuantity);
		// 修改序列号表库存数
		wsn.addQuantity(deltaQuantity);
		// 如果序列号无对应库存，则删除序列号信息
		if (DoubleUtils.compareByPrecision(wsn.getQuantityBU(), 0D, inventory
				.getPackageUnit().getPrecision()) == 0) {
			commonDao.delete(wsn);
		}

		// 写库存日志
		int inOrOut = (deltaQuantity > 0 ? 1 : -1);
		addInventoryLog(WmsInventoryLogType.INVENTORY_ADJUST, inOrOut, null,
				null, inventory.getLocation(), inventory.getItemKey(),
				Math.abs(deltaQuantity), inventory.getPackageUnit(),
				inventory.getStatus(), description);
		// 刷新库满度
		refreshLocationUseRate(inventory.getLocation(),  0);

		//发动机库库存调整只记录结果,不新增盘点库位信息 yc 20160407
		// 查询仓库中的盘点库位
//		String hql = "FROM WmsLocation location WHERE location.type = :type AND location.warehouse.id=:warehouseId AND location.status = 'ENABLED' ORDER BY location.code";
//		List<WmsLocation> countLocations = commonDao.findByQuery(hql,
//				new String[] { "type", "warehouseId" }, new Object[] {
//						WmsLocationType.COUNT,
//						inventory.getLocation().getWarehouse().getId() });
//		if (countLocations == null || countLocations.size() <= 0) {
//			throw new OriginalBusinessException("找不到盘点库位！");
//		}
//
//		// 将调整数量计入盘点库位
//		WmsInventory countInventory = getInventoryWithNew(
//				countLocations.get(0), inventory.getItemKey(), inventory
//						.getItemKey().getItem().getPackageByLineNo(1),
//				inventory.getStatus());
//		countInventory.addQuantityBU(-deltaQuantity);
//		commonDao.store(countInventory);
//		// 差异数量在盘点库位的序列号处理
//		inventoryExtendManager.addInventoryExtend(countInventory, wsn,
//				-deltaQuantity);
//
//		// 写库存日志
//		inOrOut = (-deltaQuantity > 0 ? 1 : -1);
//		addInventoryLog(WmsInventoryLogType.INVENTORY_ADJUST, inOrOut, null,
//				null, countInventory.getLocation(),
//				countInventory.getItemKey(), Math.abs(deltaQuantity),
//				countInventory.getPackageUnit(), countInventory.getStatus(),
//				description);
	}

	public void countAdjust(WmsInventoryExtend wsn, Double modifyQuantity,
			String description) {
		// 盈亏库位正数表示减少,负数表示增加
		double oldQuantityBU = wsn.getQuantityBU();

		if (Math.abs(modifyQuantity) > Math.abs(oldQuantityBU)) {
			throw new OriginalBusinessException("调整库存不能大于原库存！");
		}

		WmsInventory inv = wsn.getInventory();
		inv.removeQuantityBU(modifyQuantity);
		wsn.removeQuantity(modifyQuantity);
		if (DoubleUtils.compareByPrecision(wsn.getQuantityBU(), 0D, inv
				.getPackageUnit().getPrecision()) == 0) {
			commonDao.delete(wsn);
		}

		int incOrDec = (modifyQuantity > 0 ? -1 : 1);

		// 库存调整日志记录
		addInventoryLog(WmsInventoryLogType.COUNT_ADJUST, incOrDec, null, null,
				inv.getLocation(), inv.getItemKey(), Math.abs(modifyQuantity),
				inv.getPackageUnit(), inv.getStatus(), description);
	}

	public void inventoryCountAutoCompute(Date computeDate) {
		String hql = " FROM WmsInventoryCount invcount WHERE invcount.recordDate = :recordDate";
		List<WmsInventoryCount> countList = commonDao.findByQuery(hql,
				new String[] { "recordDate" }, new Object[] { computeDate });
		if (countList.size() <= 0) {

			// long startTime=System.currentTimeMillis();
			WmsWarehouse curWareHouse = commonDao.load(WmsWarehouse.class,
					WmsWarehouseHolder.getWmsWarehouse().getId());

			hql = "SELECT inventory FROM WmsInventory inventory "
					+ "INNER JOIN inventory.location location "
					+ "INNER JOIN inventory.location.warehouse warehouse "
					+ "WHERE warehouse.id = :curWareHouseId ";
			List<WmsInventory> wis = commonDao.findByQuery(hql,
					"curWareHouseId", curWareHouse.getId());
			for (WmsInventory wi : wis) {
				if (wi.getQuantityBU() > 0) {
					WmsInventoryCount wc = EntityFactory
							.getEntity(WmsInventoryCount.class);
					wc.setWarehouse(curWareHouse);
					wc.setCompany(wi.getItemKey().getItem().getCompany());
					wc.setItem(wi.getItemKey().getItem());
					wc.setQuantityBU(wi.getQuantityBU());
					wc.setWeight(wi.getQuantityBU()
							* wi.getPackageUnit().getWeight());
					wc.setVolume(wi.getQuantityBU()
							* wi.getPackageUnit().getVolume());
					wc.setPalletQuantity(1D);
					wc.setRecordDate(computeDate);
					commonDao.store(wc);
				}
			}

			// WmsWarehouse curWareHouse = commonDao.load(WmsWarehouse.class,
			// WmsWarehouseHolder.getWmsWarehouse().getId());
			// Set<WmsWarehouseArea> areas= curWareHouse.getWarehouseAreas();
			// for(WmsWarehouseArea area : areas){
			// hql =
			// "FROM WmsLocation location WHERE location.warehouseArea.id = :warehouseAreaId";
			// List<WmsLocation> locations = commonDao.findByQuery(hql,
			// "warehouseAreaId", area.getId());
			// for(WmsLocation loc : locations){
			// hql =
			// "FROM WmsInventory inventory WHERE inventory.location.id = :locationId";
			//
			// List<WmsInventory> wis= commonDao.findByQuery(hql, "locationId",
			// loc.getId());
			// for(WmsInventory wi : wis){
			// if(wi.getQuantityBU()>0){
			// WmsInventoryCount wc =
			// EntityFactory.getEntity(WmsInventoryCount.class);
			// wc.setWarehouse(curWareHouse);
			// wc.setCompany(wi.getItemKey().getItem().getCompany());
			// wc.setItem(wi.getItemKey().getItem());
			// wc.setQuantityBU(wi.getQuantityBU());
			// wc.setWeight(wi.getQuantityBU() *
			// wi.getPackageUnit().getWeight());
			// wc.setVolume(wi.getQuantityBU() *
			// wi.getPackageUnit().getVolume());
			// wc.setPalletQuantity(1D);
			// wc.setRecordDate(computeDate);
			// commonDao.store(wc);
			// }
			// }
			// }
			// }

			// long endTime=System.currentTimeMillis();
			// System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
		}
	}

	public void initInventoryByCVS(Long companyId,File file) {
		Integer error = 0;
		String name = file.getName();
		if (file == null) {
			throw new BusinessException("file.not.found");
		} else if (!name.substring(name.lastIndexOf('.') + 1,
				name.lastIndexOf('.') + 4).equals("xls")) {
			throw new BusinessException("this.file.error");
		}

		Sheet dataSheet = null;
		List<Cell[]> cellArray = new ArrayList<Cell[]>();
		try {
			Workbook wb = Workbook
					.getWorkbook(new java.io.FileInputStream(file));
			dataSheet = wb.getSheet(0);

			int rowNum = dataSheet.getRows();
			for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
				cellArray.add(dataSheet.getRow(rowIndex));
				if (cellArray.size() > 0 && cellArray.size() % 50 == 0) {
					error += this.resolveInventoryJac(companyId,cellArray);
					cellArray.clear();
				}
			}
			if (cellArray.size() > 0 && cellArray.size() < 50) {
				error += this.resolveInventoryJac(companyId,cellArray);
				cellArray.clear();
			}
		} catch (Exception e1) {
			throw new BusinessException(e1.getMessage());
		}
		LocalizedMessage.addMessage("导入失败" + error + "条,请查看日志");
	}
	public int resolveInventoryJac(Long companyId,List<Cell[]> strList) throws ParseException {
		StringBuffer logBuffer = new StringBuffer("");
		Integer errorNum = 0;
		Cell[] strArr;
		List<WmsWarehouse> ws = commonDao.findByQuery("FROM WmsWarehouse w WHERE w.status = 'ENABLED'");
		Map<String,WmsWarehouse> wss = new HashMap<String, WmsWarehouse>();
		if(ws!=null && ws.size()>0){
			for(WmsWarehouse obj : ws){
				wss.put(obj.getCode(),obj);
			}
		}
		Map<String,WmsLocation> wlss = new HashMap<String,WmsLocation>();
		Map<String,WmsItem> wiss = new HashMap<String, WmsItem>();
		Map<String,WmsPackageUnit> ppp = new HashMap<String, WmsPackageUnit>();
		Map<String,WmsOrganization> oos = new HashMap<String, WmsOrganization>();
		Map<String,WmsWarehouseArea> was = new HashMap<String, WmsWarehouseArea>();
		WmsWarehouse wmsWarehouse = WmsWarehouseHolder.getWmsWarehouse();
		WmsWarehouseArea houseArea = null;
		WmsLocation wmsLocation = null;
		WmsItem wmsItem = null;
		WmsPackageUnit packageUnit = null;
		WmsOrganization supplier = null;
		for (int i = 0; i < strList.size(); i++) {
			String lineNum = "";
			try {
				//仓库编码 库区编码  库位编码 货品编码 货品名称  包装单位  托盘号  箱号  序列号
				strArr = strList.get(i);
				lineNum = strArr[31].getContents();// 行号
				String wmsWarehouseAreaCode = strArr[0].getContents();// 库区编码
				String wmsLocationCode = strArr[1].getContents();// 库位编码
				String goodsCode = strArr[2].getContents();// 货品编码
				String goodsName = strArr[3].getContents();// 货品名称
				String packageUnitStr = strArr[4].getContents();// 包装单位
				String pallet = strArr[5].getContents();// 托盘号
				String carton = strArr[6].getContents();// 箱号
				String serialNo = strArr[7].getContents();// 序列号
				String buNum = strArr[8].getContents().trim().equals("") ? "0"
						: strArr[8].getContents();// BU数
				String inventoryStatus = strArr[9].getContents();// 库存状态
				String supplierCode = strArr[10].getContents();// 供货商
				String extendPropC1 = strArr[11].getContents();
				String extendPropC2 = strArr[12].getContents();
				String extendPropC3 = strArr[13].getContents();
				String extendPropC4 = strArr[14].getContents();
				String extendPropC5 = strArr[15].getContents();
				String extendPropC6 = strArr[16].getContents();
				String extendPropC7 = strArr[17].getContents();
				String extendPropC8 = strArr[18].getContents();
				String extendPropC9 = strArr[19].getContents();
				String extendPropC10 = strArr[20].getContents();
				String extendPropC11 = strArr[21].getContents();
				String extendPropC12 = strArr[22].getContents();
				String extendPropC13 = strArr[23].getContents();
				String extendPropC14 = strArr[24].getContents();
				String extendPropC15 = strArr[25].getContents();
				String extendPropC16 = strArr[26].getContents();
				String extendPropC17 = strArr[27].getContents();
				String extendPropC18 = strArr[28].getContents();
				String extendPropC19 = strArr[29].getContents();
				String extendPropC20 = strArr[30].getContents();

				//库区
				if(was.containsKey(wmsWarehouseAreaCode)){
					houseArea = was.get(wmsWarehouseAreaCode);
				}else{
					houseArea = (WmsWarehouseArea) commonDao
							.findByQueryUniqueResult(
									"from WmsWarehouseArea house where house.code=:code AND house.warehouse.code=:warehouseCode AND house.status='ENABLED'",
									new String[] { "code", "warehouseCode" },
									new Object[] { wmsWarehouseAreaCode,wmsWarehouse.getCode()});
				}
				if (houseArea == null) {
					throw new OriginalBusinessException(lineNum + "行库区异常/");
					//errorNum++;
					//logBuffer.append(lineNum + "行库区异常/");
					// log.debug(lineNum+",2 error \n");
					//continue;
				}else{
					was.put(wmsWarehouseAreaCode, houseArea);
				}
				// 库位
				if(wlss.containsKey(wmsLocationCode)){
					wmsLocation = wlss.get(wmsLocationCode);
				}else{
					wmsLocation = (WmsLocation) commonDao
							.findByQueryUniqueResult(
									" from WmsLocation location where location.code=:code AND location.warehouse.code=:warehouse AND location.warehouseArea.code=:warehouseArea AND location.status='ENABLED'",
									new String[] { "code", "warehouse",
											"warehouseArea" }, new Object[] {
											wmsLocationCode, wmsWarehouse.getCode(),wmsWarehouseAreaCode });
				}
				if (wmsLocation == null) {
					throw new OriginalBusinessException(lineNum + "行库位异常/");
					//logBuffer.append(lineNum + "行库位异常/");
					//errorNum++;
					// log.debug(lineNum+",3 error \n");
					//continue;
				}else if(!wlss.containsKey(wmsLocationCode)){
					wlss.put(wmsLocationCode, wmsLocation);
				}
				// 货品
				if(wiss.containsKey(goodsCode)){
					wmsItem = wiss.get(goodsCode);
				}else{
					wmsItem = (WmsItem) commonDao
							.findByQueryUniqueResult(
									"from WmsItem wi where wi.code = :code AND wi.status='ENABLED'" +//AND wi.name=:name 
									" AND wi.company.id =:companyId",
									new String[] { "code", "companyId" }, new Object[] {//"name",
											goodsCode, companyId});//goodsName ,
				}
				if (wmsItem == null) {
					throw new OriginalBusinessException(lineNum + "行货品异常/");
					//logBuffer.append(lineNum + "行货品异常/");
					//errorNum++;
					// log.debug(lineNum+",4 error \n");
					//continue;
				}else if(!wiss.containsKey(goodsCode)){
					wiss.put(goodsCode, wmsItem);
				}
				// 包装单位
				if(ppp.containsKey(goodsCode+packageUnitStr)){
					packageUnit = ppp.get(goodsCode+packageUnitStr);
				}else{
					packageUnit = (WmsPackageUnit) commonDao
							.findByQueryUniqueResult(
									"from WmsPackageUnit wpu where wpu.item.id=:item and wpu.unit=:unit",
									new String[] { "item", "unit" }, new Object[] {
											wmsItem.getId(), packageUnitStr });
				}
				if (packageUnit == null) {
					throw new OriginalBusinessException(lineNum + "行包装异常/");
					//errorNum++;
					//logBuffer.append(lineNum + "行包装异常/");
					// log.debug(lineNum+"6 error \n");
					//continue;
				}else if(!ppp.containsKey(goodsCode+packageUnitStr)){
					ppp.put(goodsCode+packageUnitStr, packageUnit);
				}
				//供应商
				if(oos.containsKey(supplierCode)){
					supplier = oos.get(supplierCode);
				}else{
					supplier = (WmsOrganization) commonDao
							.findByQueryUniqueResult(
									"from WmsOrganization wo where wo.code=:code and wo.beSupplier=true",
									new String[] { "code" },
									new Object[] { supplierCode });
				}
				if (supplier == null) {
					throw new OriginalBusinessException(lineNum + "行供应商异常/");
				}else if(!oos.containsKey(supplierCode)){
					oos.put(supplierCode, supplier);
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date storageDate = StringUtils
						.isEmpty(extendPropC2)
						|| extendPropC2.equals("-") ? null : sdf
						.parse((extendPropC2));// 收货日期
				Date produceDate = StringUtils
						.isEmpty(extendPropC4)
						|| extendPropC4.equals("-") ? null : sdf.parse(extendPropC4);// 生产日期
				Date expireDate = StringUtils.isEmpty(extendPropC5)
						|| extendPropC5.equals("-") ? null : sdf.parse(extendPropC5);// 失效日期
				Date warnDate = StringUtils.isEmpty(extendPropC6)
						|| extendPropC6.equals("-") ? null : sdf.parse(extendPropC6);// 近效期
				Date receivedDate = sdf
						.parse(com.vtradex.thorn.server.util.DateUtil
								.formatDateYMDToStr(new Date()));
                LotInfo lotInfo = new LotInfo();
                lotInfo.setStorageDate(storageDate==null?new Date():storageDate);
                lotInfo.setProductDate(produceDate==null?new Date():produceDate);
                lotInfo.setExpireDate(expireDate);
                lotInfo.setWarnDate(warnDate);
                lotInfo.setReceivedDate(receivedDate);
				lotInfo.setSoi("initSoi");// 收货单号
				lotInfo.setSupplier(supplier);
				lotInfo.setExtendPropC1(StringUtils.isEmpty(extendPropC1)
						? null : extendPropC1);
				lotInfo.setExtendPropC2(StringUtils.isEmpty(extendPropC2)
						|| extendPropC2.equals("-") ? null : extendPropC2);
				lotInfo.setExtendPropC3(StringUtils.isEmpty(extendPropC3)
						|| extendPropC3.equals("-") ? null : extendPropC3);
				lotInfo.setExtendPropC4(StringUtils.isEmpty(extendPropC4)
						|| extendPropC4.equals("-") ? null : extendPropC4);
				lotInfo.setExtendPropC5(StringUtils.isEmpty(extendPropC5)
						|| extendPropC5.equals("-") ? null : extendPropC5);
				lotInfo.setExtendPropC6(StringUtils.isEmpty(extendPropC6)
						|| extendPropC6.equals("-") ? null : extendPropC6);
				lotInfo.setExtendPropC7(StringUtils.isEmpty(extendPropC7)
						|| extendPropC7.equals("-") ? null : extendPropC7);
				lotInfo.setExtendPropC8(StringUtils.isEmpty(extendPropC8)
						|| extendPropC8.equals("-") ? null : extendPropC8);
				lotInfo.setExtendPropC9(StringUtils.isEmpty(extendPropC9)
						|| extendPropC9.equals("-") ? null : extendPropC9);
				lotInfo.setExtendPropC10(StringUtils.isEmpty(extendPropC10)
						|| extendPropC10.equals("-") ? null : extendPropC10);
				lotInfo.setExtendPropC11(StringUtils.isEmpty(extendPropC11)
						|| extendPropC11.equals("-") ? null : extendPropC11);
				lotInfo.setExtendPropC12(StringUtils.isEmpty(extendPropC12)
						|| extendPropC12.equals("-") ? null : extendPropC12);
				lotInfo.setExtendPropC13(StringUtils.isEmpty(extendPropC13)
						|| extendPropC13.equals("-") ? null : extendPropC13);
				lotInfo.setExtendPropC14(StringUtils.isEmpty(extendPropC14)
						|| extendPropC14.equals("-") ? null : extendPropC14);
				lotInfo.setExtendPropC15(StringUtils.isEmpty(extendPropC15)
						|| extendPropC15.equals("-") ? null : extendPropC15);
				lotInfo.setExtendPropC16(StringUtils.isEmpty(extendPropC16)
						|| extendPropC16.equals("-") ? null : extendPropC16);
				lotInfo.setExtendPropC17(StringUtils.isEmpty(extendPropC17)
						|| extendPropC17.equals("-") ? null : extendPropC17);
				lotInfo.setExtendPropC18(StringUtils.isEmpty(extendPropC18)
						|| extendPropC18.equals("-") ? null : extendPropC18);
				lotInfo.setExtendPropC19(StringUtils.isEmpty(extendPropC19)
						|| extendPropC19.equals("-") ? null : extendPropC19);
				lotInfo.setExtendPropC20(StringUtils.isEmpty(extendPropC20)
						|| extendPropC20.equals("-") ? null : extendPropC20);
				lotInfo.format();
				WmsItemKey wmsItemKey = wmsItemManager.getItemKey(wmsWarehouse,
						lotInfo.getSoi(), wmsItem, lotInfo);// 货品批次属性
				WmsInventory wmsInventory = this.getInventoryWithNew(
						wmsLocation, wmsItemKey, packageUnit, StringUtils.isEmpty(inventoryStatus)?BaseStatus.NULLVALUE:inventoryStatus);
				WmsInventoryExtend wmsSerialNo = this.inventoryExtendManager
						.addInventoryExtend(wmsInventory, pallet, carton,
								serialNo,
								Double.parseDouble(buNum));
				wmsSerialNo.setLocationId(wmsLocation.getId());
				wmsSerialNo.setLocationCode(wmsLocation.getCode());
				wmsSerialNo.setItem(wmsItem);
				wmsInventory.addQuantityBU(Double.parseDouble(buNum));
				Double quantityToDouble = wmsInventory.getQuantityBU()
						/ packageUnit.getConvertFigure();
				Integer quantity = quantityToDouble > quantityToDouble
						.intValue() ? quantityToDouble.intValue() + 1
						: quantityToDouble.intValue();
				wmsInventory.setQuantity(Double.valueOf(quantity));
				wmsSerialNo.setInventory(wmsInventory);
				this.refreshLocationUseRate(wmsLocation,  1);
			} catch (Exception e) {
				throw new OriginalBusinessException(e.getMessage());
			}
		}
		ExceptionLog log = EntityFactory.getEntity(ExceptionLog.class);
		if (UserHolder.getUser() != null) {
			log.setOperUserId(UserHolder.getUser().getId());
			log.setOperUserName(UserHolder.getUser().getName());
		} else {
			User user = this.commonDao.load(User.class, new Long(1));
			log.setOperUserId(user.getId());
			log.setOperUserName(user.getName());
		}
		log.setType("库存导入");
		log.setOperDate(new Date());
		log.setOperException(logBuffer.toString());
		log.setOperExceptionMess("");
		log.setOperPageId("maintainInventoryPage");
		log.setOperPageName("库存查询");
		log.setOperComponentId("库存导入");
		log.setOperComponentName("inventoryCountAutoCompute");
		if (org.apache.commons.lang.StringUtils.isEmpty(log.getOperException())) {
			log.setStrExtend1("成功");
		} else {
			log.setStrExtend1("失败");
		}
		commonDao.store(log);
		return errorNum;
	}
	public int resolveInventory(List<Cell[]> strList) throws ParseException {
		StringBuffer logBuffer = new StringBuffer("");
		Integer errorNum = 0;
		Cell[] strArr;
		for (int i = 0; i < strList.size(); i++) {
			String lineNum = "";
			try {
				strArr = strList.get(i);
				lineNum = strArr[38].getContents();// 行号
				String wmsWarehouseCode = strArr[0].getContents();// 仓库编码
				String wmsWarehouseAreaCode = strArr[1].getContents();// 库区编码
				String wmsLocationCode = strArr[2].getContents();// 库位编码
				String goodsCode = strArr[3].getContents();// 货品编码
				String goodsName = strArr[4].getContents();// 货品名称
				String packageUnitStr = strArr[5].getContents();// 包装单位
				String pallet = strArr[6].getContents();// 托盘号
				String carton = strArr[7].getContents();// 箱号
				String serialNo = strArr[8].getContents();// 序列号
				String buNum = strArr[9].getContents().trim().equals("") ? "0"
						: strArr[9].getContents();// BU数
				String inventoryStatus = strArr[10].getContents();// 库存状态
				String supplierCode = strArr[17].getContents();// 供货商
				String extendPropC1 = strArr[18].getContents();
				String extendPropC2 = strArr[19].getContents();
				String extendPropC3 = strArr[20].getContents();
				String extendPropC4 = strArr[21].getContents();
				String extendPropC5 = strArr[22].getContents();
				String extendPropC6 = strArr[23].getContents();
				String extendPropC7 = strArr[24].getContents();
				String extendPropC8 = strArr[25].getContents();
				String extendPropC9 = strArr[26].getContents();
				String extendPropC10 = strArr[27].getContents();
				String extendPropC11 = strArr[28].getContents();
				String extendPropC12 = strArr[29].getContents();
				String extendPropC13 = strArr[30].getContents();
				String extendPropC14 = strArr[31].getContents();
				String extendPropC15 = strArr[32].getContents();
				String extendPropC16 = strArr[33].getContents();
				String extendPropC17 = strArr[34].getContents();
				String extendPropC18 = strArr[35].getContents();
				String extendPropC19 = strArr[36].getContents();
				String extendPropC20 = strArr[37].getContents();

				// 仓库
				WmsWarehouse wmsWarehouse = (WmsWarehouse) commonDao
						.findByQueryUniqueResult(
								"from WmsWarehouse house where house.code=:code AND code.status='ENABLED'",
								new String[] { "code" },
								new Object[] { wmsWarehouseCode });
				if (wmsWarehouse == null) {
					errorNum++;
					logBuffer.append(lineNum + "行仓库异常/");
					// log.debug(lineNum+",1 error \n");
					continue;
				}
				WmsWarehouseArea houseArea = (WmsWarehouseArea) commonDao
						.findByQueryUniqueResult(
								"from WmsWarehouseArea house where house.code=:code AND house.warehouse.code=:warehouseCode AND house.status='ENABLED'",
								new String[] { "code", "warehouseCode" },
								new Object[] { wmsWarehouseAreaCode,
										wmsWarehouseCode });
				if (houseArea == null) {
					errorNum++;
					logBuffer.append(lineNum + "行库区异常/");
					// log.debug(lineNum+",2 error \n");
					continue;
				}
				// 库位
				WmsLocation wmsLocation = (WmsLocation) commonDao
						.findByQueryUniqueResult(
								" from WmsLocation location where location.code=:code AND location.warehouse.code=:warehouse AND location.warehouseArea.code=:warehouseArea AND location.status='ENABLED'",
								new String[] { "code", "warehouse",
										"warehouseArea" }, new Object[] {
										wmsLocationCode, wmsWarehouseCode,
										wmsWarehouseAreaCode });
				if (wmsLocation == null) {
					logBuffer.append(lineNum + "行库位异常/");
					errorNum++;
					// log.debug(lineNum+",3 error \n");
					continue;
				}
				// 货品
				WmsItem wmsItem = (WmsItem) commonDao
						.findByQueryUniqueResult(
								"from WmsItem wi where wi.code = :code AND wi.name=:name AND wi.status='ENABLED'",
								new String[] { "code", "name" }, new Object[] {
										goodsCode, goodsName });
				if (wmsItem == null) {
					logBuffer.append(lineNum + "行货品异常/");
					errorNum++;
					// log.debug(lineNum+",4 error \n");
					continue;
				}
				// 库存状态
				WmsItemState itemState = (WmsItemState) commonDao
						.findByQueryUniqueResult(
								"from WmsItemState itemState where itemState.name=:inventoryStatus AND itemState.company.code=:compayCode  AND itemState.status='ENABLED' ",
								new String[] { "inventoryStatus", "compayCode" },
								new Object[] { inventoryStatus,
										wmsItem.getCompany().getCode() });
				if (itemState == null) {
					logBuffer.append(lineNum + "行状态异常/");
					errorNum++;
					continue;
				}
				// 包装单位
				WmsPackageUnit packageUnit = (WmsPackageUnit) commonDao
						.findByQueryUniqueResult(
								"from WmsPackageUnit wpu where wpu.item.code=:code and wpu.unit=:unit",
								new String[] { "code", "unit" }, new Object[] {
										goodsCode, packageUnitStr });
				if (packageUnit == null) {
					errorNum++;
					logBuffer.append(lineNum + "行包装异常/");
					// log.debug(lineNum+"6 error \n");
					continue;
				}
				WmsOrganization supplier = (WmsOrganization) commonDao
						.findByQueryUniqueResult(
								"from WmsOrganization wo where wo.code=:code and wo.beSupplier=true",
								new String[] { "code" },
								new Object[] { supplierCode });
				LotInfo lotInfo = new LotInfo();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date storageDate = StringUtils
						.isEmpty(strArr[11].getContents())
						|| strArr[11].equals("-") ? null : sdf
						.parse((strArr[11].getContents()));// 收货日期
				Date produceDate = StringUtils
						.isEmpty(strArr[14].getContents())
						|| strArr[14].equals("-") ? null : sdf.parse(strArr[14]
						.getContents());// 生产日期
				Date expireDate = StringUtils.isEmpty(strArr[15].getContents())
						|| strArr[15].equals("-") ? null : sdf.parse(strArr[15]
						.getContents());// 失效日期
				Date warnDate = StringUtils.isEmpty(strArr[16].getContents())
						|| strArr[16].getContents().equals("-") ? null : sdf
						.parse(strArr[16].getContents());// 近效期
				Date receivedDate = sdf
						.parse(com.vtradex.thorn.server.util.DateUtil
								.formatDateYMDToStr(new Date()));
                lotInfo.setStorageDate(storageDate);
                lotInfo.setProductDate(produceDate);
                lotInfo.setExpireDate(expireDate);
                lotInfo.setWarnDate(warnDate);
                lotInfo.setReceivedDate(receivedDate);
				lotInfo.setSoi(strArr[12].getContents());// 收货单号
				lotInfo.setSupplier(supplier);
				lotInfo.setExtendPropC1(StringUtils.isEmpty(extendPropC1)
						? null : extendPropC1);
				lotInfo.setExtendPropC2(StringUtils.isEmpty(extendPropC2)
						|| extendPropC2.equals("-") ? null : extendPropC2);
				lotInfo.setExtendPropC3(StringUtils.isEmpty(extendPropC3)
						|| extendPropC3.equals("-") ? null : extendPropC3);
				lotInfo.setExtendPropC4(StringUtils.isEmpty(extendPropC4)
						|| extendPropC4.equals("-") ? null : extendPropC4);
				lotInfo.setExtendPropC5(StringUtils.isEmpty(extendPropC5)
						|| extendPropC5.equals("-") ? null : extendPropC5);
				lotInfo.setExtendPropC6(StringUtils.isEmpty(extendPropC6)
						|| extendPropC6.equals("-") ? null : extendPropC6);
				lotInfo.setExtendPropC7(StringUtils.isEmpty(extendPropC7)
						|| extendPropC7.equals("-") ? null : extendPropC7);
				lotInfo.setExtendPropC8(StringUtils.isEmpty(extendPropC8)
						|| extendPropC8.equals("-") ? null : extendPropC8);
				lotInfo.setExtendPropC9(StringUtils.isEmpty(extendPropC9)
						|| extendPropC9.equals("-") ? null : extendPropC9);
				lotInfo.setExtendPropC10(StringUtils.isEmpty(extendPropC10)
						|| extendPropC10.equals("-") ? null : extendPropC10);
				lotInfo.setExtendPropC11(StringUtils.isEmpty(extendPropC11)
						|| extendPropC11.equals("-") ? null : extendPropC11);
				lotInfo.setExtendPropC12(StringUtils.isEmpty(extendPropC12)
						|| extendPropC12.equals("-") ? null : extendPropC12);
				lotInfo.setExtendPropC13(StringUtils.isEmpty(extendPropC13)
						|| extendPropC13.equals("-") ? null : extendPropC13);
				lotInfo.setExtendPropC14(StringUtils.isEmpty(extendPropC14)
						|| extendPropC14.equals("-") ? null : extendPropC14);
				lotInfo.setExtendPropC15(StringUtils.isEmpty(extendPropC15)
						|| extendPropC15.equals("-") ? null : extendPropC15);
				lotInfo.setExtendPropC16(StringUtils.isEmpty(extendPropC16)
						|| extendPropC16.equals("-") ? null : extendPropC16);
				lotInfo.setExtendPropC17(StringUtils.isEmpty(extendPropC17)
						|| extendPropC17.equals("-") ? null : extendPropC17);
				lotInfo.setExtendPropC18(StringUtils.isEmpty(extendPropC18)
						|| extendPropC18.equals("-") ? null : extendPropC18);
				lotInfo.setExtendPropC19(StringUtils.isEmpty(extendPropC19)
						|| extendPropC19.equals("-") ? null : extendPropC19);
				lotInfo.setExtendPropC20(StringUtils.isEmpty(extendPropC20)
						|| extendPropC20.equals("-") ? null : extendPropC20);
				lotInfo.format();
				WmsItemKey wmsItemKey = wmsItemManager.getItemKey(wmsWarehouse,
						lotInfo.getSoi(), wmsItem, lotInfo);// 货品批次属性
				WmsInventory wmsInventory = this.getInventoryWithNew(
						wmsLocation, wmsItemKey, packageUnit, inventoryStatus);
				WmsInventoryExtend wmsSerialNo = this.inventoryExtendManager
						.addInventoryExtend(wmsInventory, pallet, carton,
								serialNo,
								Double.parseDouble(buNum));
				wmsSerialNo.setLocationId(wmsLocation.getId());
				wmsSerialNo.setLocationCode(wmsLocation.getCode());
				wmsSerialNo.setItem(wmsItem);
				wmsInventory.addQuantityBU(Double.parseDouble(buNum));
				Double quantityToDouble = wmsInventory.getQuantityBU()
						/ packageUnit.getConvertFigure();
				Integer quantity = quantityToDouble > quantityToDouble
						.intValue() ? quantityToDouble.intValue() + 1
						: quantityToDouble.intValue();
				wmsInventory.setQuantity(Double.valueOf(quantity));
				wmsSerialNo.setInventory(wmsInventory);
				this.refreshLocationUseRate(wmsLocation,  1);
			} catch (Exception e) {
				logBuffer.append(lineNum + "行导入失败/");
				errorNum++;
				logger.error("", e);
			}
		}
		ExceptionLog log = EntityFactory.getEntity(ExceptionLog.class);
		if (UserHolder.getUser() != null) {
			log.setOperUserId(UserHolder.getUser().getId());
			log.setOperUserName(UserHolder.getUser().getName());
		} else {
			User user = this.commonDao.load(User.class, new Long(1));
			log.setOperUserId(user.getId());
			log.setOperUserName(user.getName());
		}
		log.setType("库存导入");
		log.setOperDate(new Date());
		log.setOperException(logBuffer.toString());
		log.setOperExceptionMess("");
		log.setOperPageId("maintainInventoryPage");
		log.setOperPageName("库存查询");
		log.setOperComponentId("库存导入");
		log.setOperComponentName("inventoryCountAutoCompute");
		if (org.apache.commons.lang.StringUtils.isEmpty(log.getOperException())) {
			log.setStrExtend1("成功");
		} else {
			log.setStrExtend1("失败");
		}
		commonDao.store(log);
		return errorNum;
	}

	public void cancelMoveDoc(WmsMoveDocDetail wmsMoveDocDetail) {
		Double cancelQtyBU = wmsMoveDocDetail.getPlanQuantityBU();
		WmsInventory inventory = load(WmsInventory.class,
				wmsMoveDocDetail.getInventoryId());
		if (inventory == null || inventory.getQuantityBU() < cancelQtyBU) {
			throw new BusinessException("发货库位库存不足，不能取消收货！");
		}
		//
		if (DoubleUtils.compareByPrecision(inventory.getAllocatedQuantityBU(),
				cancelQtyBU, inventory.getPackageUnit().getPrecision()) < 0) {
			inventory.unallocatePickup(inventory.getAllocatedQuantityBU());
		} else {
			inventory.unallocatePickup(cancelQtyBU);
		}
		commonDao.store(inventory);

		List<WmsInventoryExtend> inventoryExtends = commonDao
				.findByQuery(
						"FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId "
								+ "AND wsn.serialNo = :serialNo AND wsn.carton = :carton AND wsn.pallet = :pallet AND wsn.quantityBU > 0",
						new String[] { "inventoryId", "serialNo", "carton",
								"pallet" },
						new Object[] { inventory.getId(),
								wmsMoveDocDetail.getSerialNo(),
								wmsMoveDocDetail.getCarton(),
								wmsMoveDocDetail.getPallet() });

		Double needCancelQty = unallocatePickupInventoryExtend(
				inventoryExtends, cancelQtyBU);
		if (needCancelQty > 0D) {
			inventoryExtends = commonDao
					.findByQuery(
							"FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId "
									+ "AND wsn.serialNo = :serialNo AND wsn.carton = :carton AND wsn.quantityBU > 0",
							new String[] { "inventoryId", "serialNo", "carton" },
							new Object[] { inventory.getId(),
									wmsMoveDocDetail.getSerialNo(),
									wmsMoveDocDetail.getCarton() });
			needCancelQty = unallocatePickupInventoryExtend(inventoryExtends,
					needCancelQty);
		}
		if (needCancelQty > 0D) {
			inventoryExtends = commonDao
					.findByQuery(
							"FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId "
									+ "AND wsn.serialNo = :serialNo AND wsn.quantityBU > 0",
							new String[] { "inventoryId", "serialNo" },
							new Object[] { inventory.getId(),
									wmsMoveDocDetail.getSerialNo() });
			needCancelQty = unallocatePickupInventoryExtend(inventoryExtends,
					needCancelQty);
		}
		if (needCancelQty > 0D) {
			inventoryExtends = commonDao
					.findByQuery(
							"FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId AND wsn.quantityBU > 0",
							new String[] { "inventoryId" },
							new Object[] { inventory.getId() });
			needCancelQty = unallocatePickupInventoryExtend(inventoryExtends,
					needCancelQty);
		}

		if (needCancelQty > 0D) {
			throw new BusinessException("发货库位拣货分配数量不足，不能取消删除上架单！");
		}
	}
	public void cancelReceive(WmsReceivedRecord receivedRecord) {
		Double cancelQtyBU = receivedRecord.getReceivedQuantity();
		WmsLocation location = commonDao.load(WmsLocation.class,
				receivedRecord.getLocationId());
		List<WmsInventory> inventorys = getInventoryBySoi(
				receivedRecord.getLocationId(), receivedRecord.getItemKey()
						.getId());
		Boolean secondSkip = false;
		for (WmsInventory inventory : inventorys) {
			Double cancelQty = 0D;
			if (inventory.getAvailableQuantityBU() >= cancelQtyBU) {
				cancelQty = cancelQtyBU;
			} else {
				cancelQty = inventory.getAvailableQuantityBU();
			}
			if(secondSkip){
				break;
			}
			inventory.removeQuantityBU(cancelQty);
			commonDao.store(inventory);
			String hql = "FROM WmsInventoryExtend ix WHERE ix.inventory.id =:inventoryId";
			List<WmsInventoryExtend> ixs = commonDao.findByQuery(hql, "inventoryId", inventory.getId());
			if(ixs!=null && ixs.size()>0){
				Double ixQty = 0D,picQty = 0D;
				for(WmsInventoryExtend ix : ixs){
					ixQty = ix.getQuantityBU();
					picQty = ixQty<=cancelQty?ixQty:cancelQty;//实际可减量
					ix.removeQuantity(picQty);
					commonDao.store(ix);
					if(ix.getQuantityBU()<=0){
						commonDao.delete(ix);
					}
					WmsInventoryLog log = new WmsInventoryLog(
							WmsInventoryLogType.CANCEL_RECEIVING, -1, receivedRecord
									.getAsn().getCode(), receivedRecord.getAsn()
									.getBillType(), inventory.getLocation(),receivedRecord.getAsn().getSupplier(), inventory
									.getItemKey(), picQty,
							inventory.getPackageUnit(), inventory.getStatus(), "取消收货");
					commonDao.store(log);
					cancelQtyBU -= picQty;
					if(cancelQtyBU<=0){//做二级跳出
						secondSkip = true;
						break;
					}
				}
			}else{
				WmsInventoryLog log = new WmsInventoryLog(
						WmsInventoryLogType.CANCEL_RECEIVING, -1, receivedRecord
								.getAsn().getCode(), receivedRecord.getAsn()
								.getBillType(), inventory.getLocation(),receivedRecord.getAsn().getSupplier(), inventory
								.getItemKey(), cancelQty,
						inventory.getPackageUnit(), inventory.getStatus(), "取消收货");
				commonDao.store(log);
				
				cancelQtyBU -= cancelQty;
				if(cancelQtyBU<=0){//一级跳出
					break;
				}
			}
		}
		if(cancelQtyBU>0){
			throw new BusinessException("库存不足,不能取消收货!");
		}
	}
	public void cancelReceive(WmsReceivedRecord receivedRecord,
			Double cancelQtyBU) {
		WmsLocation location = commonDao.load(WmsLocation.class,
				receivedRecord.getLocationId());
		verifyLocation(BaseStatus.LOCK_OUT, location.getId());
		
		List<WmsInventory> inventorys = getInventoryBySoi(
				receivedRecord.getLocationId(), receivedRecord.getItemKey()
						.getId());
		for (WmsInventory inventory : inventorys) {
			Double cancelQty = 0D;
			if (inventory.getAvailableQuantityBU() >= cancelQtyBU) {
				cancelQty = cancelQtyBU;
			} else {
				cancelQty = inventory.getAvailableQuantityBU();
			}

			inventory.removeQuantityBU(cancelQty);
			commonDao.store(inventory);
			/**=====JAC收货不产生库存子表信息=======*/
			/*List<WmsInventoryExtend> inventoryExtends = commonDao
					.findByQuery(
							"FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId "
									+ "AND wsn.serialNo = :serialNo AND wsn.carton = :carton AND wsn.pallet = :pallet AND wsn.quantityBU > 0",
							new String[] { "inventoryId", "serialNo", "carton",
									"pallet" },
							new Object[] { inventory.getId(),
									receivedRecord.getSerialNo(),
									receivedRecord.getCarton(),
									receivedRecord.getPallet() });
			Double needCancelQty = removeInventoryExtend(inventoryExtends,
					cancelQty);
			if (needCancelQty > 0D) {
				inventoryExtends = commonDao
						.findByQuery(
								"FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId "
										+ "AND wsn.serialNo = :serialNo AND wsn.carton = :carton AND wsn.quantityBU > 0",
								new String[] { "inventoryId", "serialNo",
										"carton" },
								new Object[] { inventory.getId(),
										receivedRecord.getSerialNo(),
										receivedRecord.getCarton() });
				needCancelQty = removeInventoryExtend(inventoryExtends,
						needCancelQty);
			}
			if (needCancelQty > 0D) {
				inventoryExtends = commonDao
						.findByQuery(
								"FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId "
										+ "AND wsn.serialNo = :serialNo AND wsn.quantityBU > 0",
								new String[] { "inventoryId", "serialNo" },
								new Object[] { inventory.getId(),
										receivedRecord.getSerialNo() });
				needCancelQty = removeInventoryExtend(inventoryExtends,
						needCancelQty);
			}
			if (needCancelQty > 0D) {
				inventoryExtends = commonDao
						.findByQuery(
								"FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId AND wsn.quantityBU > 0",
								new String[] { "inventoryId" },
								new Object[] { inventory.getId() });
				needCancelQty = removeInventoryExtend(inventoryExtends,
						needCancelQty);
			}

			if (needCancelQty > 0D) {
				throw new BusinessException("收货库位库存不足，不能取消收货！");
			}*/

			// 写库存日志
			WmsInventoryLog log = new WmsInventoryLog(
					WmsInventoryLogType.CANCEL_RECEIVING, -1, receivedRecord
							.getAsn().getCode(), receivedRecord.getAsn()
							.getBillType(), inventory.getLocation(),receivedRecord.getAsn().getSupplier(), inventory
							.getItemKey(), cancelQtyBU,
					inventory.getPackageUnit(), inventory.getStatus(), "取消收货");
			commonDao.store(log);

			cancelQtyBU -= cancelQty;

			if (cancelQtyBU <= 0D) {
				break;
			}
		}
		if (cancelQtyBU > 0D) {
			throw new BusinessException("收货库位库存不足，不能取消收货！");
		}
		// 调用规则刷新库满度
		refreshLocationUseRate(location, -1);
	}

	public Double removeInventoryExtend(
			List<WmsInventoryExtend> inventoryExtends, Double removeQtyBU) {
		for (WmsInventoryExtend inventoryExtend : inventoryExtends) {
			Double moveQty = 0D;
			if (inventoryExtend.getQuantityBU() >= removeQtyBU) {
				moveQty = removeQtyBU;
			} else {
				moveQty = inventoryExtend.getQuantityBU();
				inventoryExtend.setPallet(BaseStatus.NULLVALUE);
			}
			removeQtyBU -= moveQty;
			inventoryExtend.removeQuantity(moveQty);
			if (inventoryExtend.getQuantityBU() <= 0) {
				commonDao.delete(inventoryExtend);
			}
			if (removeQtyBU <= 0D) {
				return 0D;
			}
		}
		return removeQtyBU;
	}

	public Double unallocatePickupInventoryExtend(
			List<WmsInventoryExtend> inventoryExtends, Double unallocateQtyBU) {
		for (WmsInventoryExtend inventoryExtend : inventoryExtends) {
			Double moveQty = 0D;
			if (inventoryExtend.getAllocatedQuantityBU() >= unallocateQtyBU) {
				moveQty = unallocateQtyBU;
			} else {
				moveQty = inventoryExtend.getAllocatedQuantityBU();
			}
			unallocateQtyBU -= moveQty;
			inventoryExtend.unallocatePickup(moveQty);
			if (unallocateQtyBU <= 0D) {
				return 0D;
			}
		}
		return unallocateQtyBU;
	}

	/**
	 * 
	 * @param inv
	 * @param productDate
	 *            生产日期
	 * @param addReason
	 *            调整原因
	 */
	public void addInventory(WmsInventory inv, Long itemId, Long supplierId,
			LotInfo lotInfo, String addReason) {
		try {
			WmsItem item = commonDao.load(WmsItem.class, itemId);
			WmsOrganization supplier = commonDao.load(WmsOrganization.class, supplierId);
			if(supplier!=null) {
				lotInfo.setSupplier(supplier);
			}
			if (StringUtils.isNotBlank(lotInfo.getSoi())) {
				Long soi = null;
				try {
					soi = Long.parseLong(lotInfo.getSoi());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				if (soi != null) {
					WmsASN asn = this.commonDao.load(WmsASN.class, soi);
					lotInfo.setSoi(asn.getCode());
				}
			}
			Double quantityBU = DoubleUtils.formatByPrecision(inv.getQuantity()
					* inv.getPackageUnit().getConvertFigure(),
					item.getPrecision());
			WmsItemKey tempItemKey = wmsItemManager.getItemKey(
					WmsWarehouseHolder.getWmsWarehouse(), lotInfo.getSoi(),
					item, lotInfo, Boolean.TRUE);
			
			
			verifyLocation(BaseStatus.LOCK_IN, inv.getLocation().getId(),inv.getPackageUnit(), tempItemKey.getId(), 
					item.getId(), quantityBU, BaseStatus.NULLVALUE);
			
			WmsInventory dbInventory = this.getInventoryWithNew(
					inv.getLocation(), tempItemKey, inv.getPackageUnit(),
					BaseStatus.NULLVALUE);

			dbInventory.addQuantityBU(quantityBU);
			this.commonDao.store(dbInventory);

			WmsInventoryExtend invExt = new WmsInventoryExtend();
			invExt.setInventory(dbInventory);
			invExt.setLocationId(dbInventory.getLocation().getId());
			invExt.setLocationCode(dbInventory.getLocation().getCode());
			invExt.setItem(tempItemKey.getItem());
			inventoryExtendManager.addInventoryExtend(dbInventory, invExt,
					quantityBU);

			this.addInventoryLog(WmsInventoryLogType.INVENTORY_ADJUST, 1, null,
					null, dbInventory.getLocation(), dbInventory.getItemKey(),
					quantityBU, dbInventory.getPackageUnit(),
					dbInventory.getStatus(), "新增库存---人工调整库存");

			
			WmsLocation location = load(WmsLocation.class,dbInventory.getLocation().getId());
			refreshLocationUseRate(location, 1);
			
			//新增库存不记录盘点库存信息 yc 20160408
			// 查询仓库中的盘点库位
//			String hql = "FROM WmsLocation location WHERE location.type = :type AND location.warehouse.id=:warehouseId AND location.status = 'ENABLED' ORDER BY location.code";
//			List<WmsLocation> countLocations = commonDao.findByQuery(hql,
//					new String[] { "type", "warehouseId" }, new Object[] {
//							WmsLocationType.COUNT,
//							dbInventory.getLocation().getWarehouse().getId() });
//			if (countLocations == null || countLocations.size() <= 0) {
//				throw new BusinessException("找不到盘点库位！");
//			}
//
//			// 将调整数量计入盘点库位
//			WmsInventory countInventory = getInventoryWithNew(
//					countLocations.get(0), dbInventory.getItemKey(),
//					dbInventory.getPackageUnit(), dbInventory.getStatus());
//			countInventory.addQuantityBU(-quantityBU);
//			commonDao.store(countInventory);
//			// 差异数量在盘点库位的序列号处理
//			inventoryExtendManager.addInventoryExtend(countInventory, invExt,
//					-quantityBU);
//
//			// 写库存日志
//			addInventoryLog(WmsInventoryLogType.INVENTORY_ADJUST, -1, null,
//					null, countInventory.getLocation(),
//					countInventory.getItemKey(), quantityBU,
//					countInventory.getPackageUnit(),
//					countInventory.getStatus(), "新增库存--虚增库存自动差异调整");
		} catch (BusinessException be) {
			logger.error("", be);
			throw new BusinessException(be.getMessage());
		}
	}
	public void qualityInventory(WmsInventoryExtend wsn, Boolean beView001, Boolean beView002,
			String inventoryStatus, String extendPropC1,Double qualityNum) {
		WmsInventory srcInv = wsn.getInventory();
		if(srcInv.getAvailableQuantityBU()<qualityNum){
			throw new BusinessException("质检数量超过库存可用量");
		}
		if(qualityNum<=0){
			throw new BusinessException("质检数量有误");
		}
		WmsItemKey tempItemKey = null;
		if(beView001 && beView002){//库存状态&工艺状态
			LotInfo oldLotInfo = srcInv.getItemKey().getLotInfo();
			LotInfo lotInfo = new LotInfo(oldLotInfo.getStorageDate(), oldLotInfo.getProductDate(), oldLotInfo.getExpireDate(),
					oldLotInfo.getWarnDate(), oldLotInfo.getReceivedDate(), oldLotInfo.getSoi(), oldLotInfo.getSupplier(), 
					extendPropC1, oldLotInfo.getExtendPropC2(),oldLotInfo.getExtendPropC3(), oldLotInfo.getExtendPropC4(), 
					oldLotInfo.getExtendPropC5(), oldLotInfo.getExtendPropC6(), 
					oldLotInfo.getExtendPropC7(),oldLotInfo.getExtendPropC8(), oldLotInfo.getExtendPropC9(), 
					oldLotInfo.getExtendPropC10(), oldLotInfo.getExtendPropC11(), oldLotInfo.getExtendPropC12(), 
					oldLotInfo.getExtendPropC13(), oldLotInfo.getExtendPropC14(), oldLotInfo.getExtendPropC15(), 
					oldLotInfo.getExtendPropC16(), oldLotInfo.getExtendPropC17(), oldLotInfo.getExtendPropC18(), 
					oldLotInfo.getExtendPropC19(), oldLotInfo.getExtendPropC20());
			tempItemKey = wmsItemManager.getItemKey(WmsWarehouseHolder.getWmsWarehouse(), 
					lotInfo.getSoi(), srcInv.getItemKey().getItem(), lotInfo);
			qualityInventory(wsn, srcInv, tempItemKey, inventoryStatus, qualityNum);
		}else if(beView001 && !beView002){
			tempItemKey = srcInv.getItemKey();
			qualityInventory(wsn, srcInv, tempItemKey, inventoryStatus, qualityNum);
		}else if(beView002 && !beView001){
			LotInfo oldLotInfo = srcInv.getItemKey().getLotInfo();
			LotInfo lotInfo = new LotInfo(oldLotInfo.getStorageDate(), oldLotInfo.getProductDate(), oldLotInfo.getExpireDate(),
					oldLotInfo.getWarnDate(), oldLotInfo.getReceivedDate(), oldLotInfo.getSoi(), oldLotInfo.getSupplier(), 
					extendPropC1, oldLotInfo.getExtendPropC2(),oldLotInfo.getExtendPropC3(), oldLotInfo.getExtendPropC4(), 
					oldLotInfo.getExtendPropC5(), oldLotInfo.getExtendPropC6(), 
					oldLotInfo.getExtendPropC7(),oldLotInfo.getExtendPropC8(), oldLotInfo.getExtendPropC9(), 
					oldLotInfo.getExtendPropC10(), oldLotInfo.getExtendPropC11(), oldLotInfo.getExtendPropC12(), 
					oldLotInfo.getExtendPropC13(), oldLotInfo.getExtendPropC14(), oldLotInfo.getExtendPropC15(), 
					oldLotInfo.getExtendPropC16(), oldLotInfo.getExtendPropC17(), oldLotInfo.getExtendPropC18(), 
					oldLotInfo.getExtendPropC19(), oldLotInfo.getExtendPropC20());
			tempItemKey = wmsItemManager.getItemKey(WmsWarehouseHolder.getWmsWarehouse(), 
					lotInfo.getSoi(), srcInv.getItemKey().getItem(), lotInfo);
			qualityInventory(wsn, srcInv, tempItemKey, srcInv.getStatus(), qualityNum);
		}
	}
	private void qualityInventory(WmsInventoryExtend wsn,WmsInventory srcInv,WmsItemKey tempItemKey,
			String inventoryStatus, Double qualityNum){
		// 增加库存数量
		WmsLocation location = commonDao.load(WmsLocation.class, wsn.getLocationId());
		WmsInventory qualityInv = getInventoryWithNew(location,
				tempItemKey, srcInv.getPackageUnit(),inventoryStatus);
		qualityInv.addQuantityBU(qualityNum);
		commonDao.store(qualityInv);
		addInventoryLog(WmsInventoryLogType.QUALITY, 1, BaseStatus.NULLVALUE, null,
				qualityInv.getLocation(), qualityInv.getItemKey(), qualityNum,
				qualityInv.getPackageUnit(), inventoryStatus,WmsInventoryLogType.QUALITY_IN_ADD);
		srcInv.removeQuantityBU(qualityNum);
		addInventoryLog(WmsInventoryLogType.QUALITY, -1, BaseStatus.NULLVALUE, null,
				srcInv.getLocation(), srcInv.getItemKey(), qualityNum,
				srcInv.getPackageUnit(), srcInv.getStatus(),WmsInventoryLogType.QUALITY_IN_DEL);
		// 对inventoryExtend进行拆分
		WmsInventoryExtend qualityWsn = inventoryExtendManager.addInventoryExtend(
				qualityInv, wsn, qualityNum);
		wsn.removeQuantity(qualityNum);
		if (DoubleUtils.compareByPrecision(wsn.getQuantityBU(), 0D, 
				srcInv.getPackageUnit().getPrecision()) == 0) {
			commonDao.delete(wsn);
		}
	}
	//标准版,未用
	public void qualityInventory(WmsInventoryExtend inv, Long itemStateId, Double qualityNum) {
		WmsItemState itemState = commonDao.load(WmsItemState.class, itemStateId);
		WmsInventory srcInv = inv.getInventory();
		
		if (qualityNum <= 0D) {
			return;
		}
		if(qualityNum > inv.getQuantityBU()) {
			throw new BusinessException("超出质检数量");
		}
		
		// 如果序列号对应库存数量已被占用，则放弃库内质检操作
		if (srcInv.getAllocatedQuantityBU() > 0
				|| srcInv.getPutawayQuantityBU() > 0) {
			throw new BusinessException("当前库存已被分配,不能进行库内质检操作！");
		}
		String newStatus = "";
		if(itemState.getName().equals(BaseStatus.NULLVALUE)) {
			String hql = "FROM WmsItemState itemState " +
					" WHERE itemState.company.id = :companyId" +
					" AND itemState.beQuality = true AND itemState.name != '-'";
			List<WmsItemState> itemStates = commonDao.findByQuery(hql,
					new String[] { "companyId" }, new Object[] {inv.getItem().getCompany().getId()});
			for (WmsItemState wmsItemState : itemStates) {
				if (srcInv.getStatus().indexOf(wmsItemState.getName()) > -1) {
					newStatus = WmsStringUtils.removeStatus(srcInv.getStatus(),wmsItemState.getName());
					break;
				}
			}
		}
		else {
			if (srcInv.getStatus().indexOf(itemState.getName()) != -1) {
				return;
			}
			newStatus = WmsStringUtils.addStatus(srcInv.getStatus(),itemState.getName());
		}
		newStatus = newStatus == "" ?  BaseStatus.NULLVALUE : newStatus;
		// 对inventory进行拆分
		
		WmsInventory qualityInv = getInventoryWithNew(srcInv.getLocation(),
				srcInv.getItemKey(), srcInv.getPackageUnit(), newStatus);
		qualityInv.addQuantityBU(qualityNum);
		addInventoryLog(WmsInventoryLogType.QUALITY, 1, BaseStatus.NULLVALUE, null,
				qualityInv.getLocation(), qualityInv.getItemKey(), qualityNum,
				qualityInv.getPackageUnit(), newStatus, "库内质检-库存增加");
		srcInv.removeQuantityBU(qualityNum);
		addInventoryLog(WmsInventoryLogType.QUALITY, -1, BaseStatus.NULLVALUE, null,
				srcInv.getLocation(), srcInv.getItemKey(), qualityNum,
				srcInv.getPackageUnit(), srcInv.getStatus(), "库内质检-库存减少");
		
		// 对inventoryExtend进行拆分
		WmsInventoryExtend qualityWsn = inventoryExtendManager.addInventoryExtend(
				qualityInv, inv, qualityNum);
		inv.removeQuantity(qualityNum);
		if (DoubleUtils.compareByPrecision(inv.getQuantityBU(), 0D, inv
				.getInventory().getPackageUnit().getPrecision()) == 0) {
			commonDao.delete(inv);
		}
	}
	/**原库存扣减库存量/取消拣货分配*/
	public WmsInventory moveSrcInv(Long srcInventoryId,Double quantity){
		WmsInventory srcInventory = load(WmsInventory.class,srcInventoryId);
		srcInventory.removeQuantityBU(quantity);
		srcInventory.unallocatePickup(quantity);
		commonDao.store(srcInventory);
		return srcInventory;
	}
	/**目标库存增加库存量/取消上架分配*/
	public WmsInventory moveDescInv(Long descInventoryId,Double quantity){
		WmsInventory dstInventory = load(WmsInventory.class,descInventoryId);
		if(dstInventory==null){
			return null;
		}
		dstInventory.addQuantityBU(quantity);
		dstInventory.unallocatePutaway(quantity);
		commonDao.store(dstInventory);
		return dstInventory;
	}
	/**预分配目标库存*/
	public WmsInventory allocatePutaway(WmsLocation toLoc,WmsItemKey itemKey
			,WmsPackageUnit wmsPackageUnit, String inventoryStatus,Double quantity
			,Integer palletQty){
		WmsInventory dstInventory = this.getInventoryWithNew(
				toLoc, itemKey, wmsPackageUnit,inventoryStatus);
		dstInventory.allocatePutaway(quantity);
		commonDao.store(dstInventory);
		toLoc.addPallet(palletQty);
		return dstInventory;
	}
	
	public void sysInventoryMiss(){
		String rijiewarehouse = GlobalParamUtils.getGloableStringValue("rijie_sys");
		String[] warehouses = rijiewarehouse.split(MyUtils.spilt1);
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().openSession();
		try {
			String sql = "truncate table wms_mis_inventory";
			session.createSQLQuery(sql).executeUpdate();
		} catch (Exception e) {
			List<WmsMisInventory> miss = commonDao.findByQuery("FROM WmsMisInventory wi WHERE 1=1");
			if(miss!=null && miss.size()>0){
				commonDao.deleteAll(miss);
			}
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
		for(String warehouse : warehouses){
			Long warehouseId = Long.parseLong(warehouse);
			sysInventoryMissImp(warehouseId);
		}
	}
	private void sysInventoryMissImp(Long warehouseId){
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class, warehouseId);
		List<Long> companyids = commonDao.findByQuery("SELECT distinct w.itemKey.item.company.id"
				+ " FROM WmsInventory w"
				+ " WHERE w.location.warehouse.id =:warehouseId", 
				new String[]{"warehouseId"}, new Object[]{warehouse.getId()});
		for(Long id : companyids){
			List<Object[]> doObj = new ArrayList<Object[]>();
			Map<String,String> supMap = new HashMap<String, String>();
			WmsOrganization company = commonDao.load(WmsOrganization.class, id);
			List<Map<String, Object>> list =null;
			try {
				list = wmsRuleManager.getAllRuleTableDetail(warehouse.getName(),
						"R102_料号级安全库存规则表",company.getName());
			} catch (Exception e) {
				try {
					Thread.sleep(10000);//10min=600000,1min=60000
				} catch (InterruptedException e1) {
					logger.error("", e1);
				}
				try {
					list = wmsRuleManager.getAllRuleTableDetail(warehouse.getName(),
							"R102_料号级安全库存规则表",company.getName());
				} catch (Exception e2) {
					try {
						Thread.sleep(20000);//10min=600000,1min=60000
					} catch (InterruptedException e1) {
						logger.error("", e1);
					}
					try {
						list = wmsRuleManager.getAllRuleTableDetail(warehouse.getName(),
								"R102_料号级安全库存规则表",company.getName());
					} catch (Exception e3) {
						System.out.println("----------"+e3.getMessage());
					}
				}
			}
			Map<String,String> itemMap = new HashMap<String, String>();
			if(list!=null && list.size()>0){
				List<Object[]> inventoryQtys = commonDao.findByQuery("SELECT w.itemKey.lotInfo.supplier.code,"
						+ " w.itemKey.item.code,w.itemKey.lotInfo.extendPropC1,SUM(w.quantityBU+w.putawayQuantityBU)"
						+ " FROM WmsInventory w WHERE w.location.warehouse.id =:warehouseId"
						+ " AND w.itemKey.item.company.id =:companyId AND w.location.type =:type"
						+ " GROUP BY w.itemKey.lotInfo.supplier.code,w.itemKey.item.code,w.itemKey.lotInfo.extendPropC1", 
						new String[]{"warehouseId","companyId","type"}, 
						new Object[]{warehouse.getId(),company.getId(),WmsLocationType.STORAGE});
				Map<String,Double> inventoryMap = new HashMap<String, Double>();
				if(inventoryQtys!=null && inventoryQtys.size()>0){
					for(Object[] obj : inventoryQtys){
						inventoryMap.put(obj[0]+MyUtils.spilt1+obj[1]+MyUtils.spilt1+obj[2], 
								obj[3]==null?0D:Double.valueOf(obj[3].toString()));
					}
				}
				List<Object[]> sups = commonDao.findByQuery("SELECT supplier.code,supplier.name" +
						" FROM WmsOrganization supplier WHERE supplier.beSupplier = true");
				for(Object[] s : sups){
					supMap.put(s[0].toString(), s[1].toString());
				}
				List<Object> itemCodes = new ArrayList<Object>();
				Map<String,String> extendPropC1s = new HashMap<String, String>();
				String key = null,supName = null, extendPropC1 = null;
				Double misQty = 0D,inventoryQty = 0D;
				for(Map<String, Object> obj : list){
					if(extendPropC1s.containsKey(obj.get("工艺状态"))){
						extendPropC1 = extendPropC1s.get(obj.get("工艺状态"));
					}else{
						extendPropC1 = MyUtils.checkExtendPropc1Back(obj.get("工艺状态").toString());
						extendPropC1s.put(obj.get("工艺状态").toString(), extendPropC1);
					}
					key = obj.get("供应商编码")+MyUtils.spilt1+obj.get("物料编码")+MyUtils.spilt1+extendPropC1;
					try {
						misQty = Double.valueOf(obj.get("安全库存").toString());
					} catch (Exception e) {
						e.printStackTrace();
						throw new BusinessException("安全库存值维护错误");
					}
					if(inventoryMap.containsKey(key)){
						inventoryQty = inventoryMap.get(key);
						supName = supMap.get(obj.get("供应商编码"));
					}else{
						//当库存无规则表维护数据时,不显示LED
//						continue;
						//当库存无规则表维护数据时,显示LED 20160901
						inventoryQty = 0D;
						supName = "供应商库存不存在";//supMap.get(obj.get("供应商编码"));
					}
					if(inventoryQty<=misQty && misQty>0){//当库存量<=大于0的报缺库存量时,记录
						doObj.add(new Object[]{
								company.getName(),obj.get("供应商编码"),obj.get("物料编码"),extendPropC1,inventoryQty,misQty,warehouse	
								,supName
						});
					}
					itemCodes.add("'"+obj.get("物料编码")+"'");
				}
				//查询物料
				int PAGE_NUMBER = 500,size = itemCodes.size();
				int j = JavaTools.getSize(size, PAGE_NUMBER);
				for(int k = 0 ; k<j ;k++){
					int toIndex = JavaTools.getIndex(k, size, PAGE_NUMBER);
					List saveList = JavaTools.getList(itemCodes, k, toIndex, PAGE_NUMBER);
					List<Object[]> objs = commonDao.findByQuery("SELECT i.code,i.name FROM WmsItem i WHERE i.code in ("+
							StringUtils.substringBetween(saveList.toString(), "[", "]")+")");
					for(Object[] obj : objs){
						itemMap.put(obj[0].toString(), obj[1].toString());
					}
				}itemCodes.clear();
			}
			supMap.clear();
			
			int size = doObj.size();
			int j = JavaTools.getSize(size, PAGE_NUMBER);
			for(int k=0;k<j;k++){
				int toIndex = JavaTools.getIndex(k, size, PAGE_NUMBER);
				List<Object[]> ret = JavaTools.getListObj(doObj, k, toIndex, PAGE_NUMBER);
				wmsItemManager.sysInventoryMissDo(ret,itemMap);
			}
		}
		
	}
	public void hgInventoryOutInit(){
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().getCurrentSession();
		String rijiewarehouse = GlobalParamUtils.getGloableStringValue("rijie_sys");
		String[] warehouses = rijiewarehouse.split(MyUtils.spilt1);
//		session.createSQLQuery("truncate table SCREEN_HG_INVENTORY_OUT").executeUpdate();
		for(String warehouse : warehouses){
			Long warehouseId = Long.parseLong(warehouse);
			hgInventoryOutInit(warehouseId,session);
		}
	}
	private void hgInventoryOutInit(Long warehouseId,Session session){
		List<Object[]> objs = session.createSQLQuery
				("select v.code,v.name,v.pallets from sup_pallet_view v where v.warehouse_id = "+warehouseId).list();
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,warehouseId);
		if(objs!=null&&objs.size()>0){
			int PALLET_QTY_IN = 0,PALLET_QTY_INFO = 0;
			for(Object[] obj : objs){
				Object value = null;
				try {
					value = wmsRuleManager.getSingleRuleTableDetail(warehouse.getName(),
							"R101_供应商托盘规则表","租凭托盘个数",obj[0]);
				} catch (Exception e) {
					try {
						Thread.sleep(10000);//10min=600000,1min=60000
					} catch (InterruptedException ine) {
						logger.error("", ine);
					}
					try {
						value = wmsRuleManager.getSingleRuleTableDetail(warehouse.getName(),
								"R101_供应商托盘规则表","租凭托盘个数",obj[0]);
					} catch (Exception e2) {
						try {
							Thread.sleep(10000);//10min=600000,1min=60000
						} catch (InterruptedException ine) {
							logger.error("", ine);
						}
						try {
							value = wmsRuleManager.getSingleRuleTableDetail(warehouse.getName(),
									"R101_供应商托盘规则表","租凭托盘个数",obj[0]);
						} catch (Exception e3) {
							System.out.println(e3.getLocalizedMessage());
						}
					}
				}
				if(value!=null){
					PALLET_QTY_IN = Integer.valueOf(obj[2].toString());
					PALLET_QTY_INFO = Integer.valueOf(value.toString());
					if(PALLET_QTY_IN>PALLET_QTY_INFO){
						SQLQuery query = session.createSQLQuery("insert into SCREEN_HG_INVENTORY_OUT(type,SUP_CODE,SUP_NAME,PALLET_QTY_IN,PALLET_QTY_INFO)"
								+ " values('合格品超库存',?,?,?,?)");
						query.setString(0, obj[0].toString());
						query.setString(1, obj[1].toString());
						query.setInteger(2, PALLET_QTY_IN);//实际托盘个数
						query.setInteger(3, PALLET_QTY_INFO);//租赁托盘个数
						query.executeUpdate();
					}
				}
			}
		}
	}
	public void unHgInventoryOutInit(){
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().getCurrentSession();
		String rijiewarehouse = GlobalParamUtils.getGloableStringValue("rijie_sys");
		String[] warehouses = rijiewarehouse.split(MyUtils.spilt1);
//		session.createSQLQuery("truncate table SCREEN_HG_INVENTORY_OUT").executeUpdate();
		for(String warehouse : warehouses){
			Long warehouseId = Long.parseLong(warehouse);
			unHgInventoryOutInit(warehouseId,session);
		}
	}
	private void unHgInventoryOutInit(Long warehouseId,Session session){
		List<Object[]> objs = session.createSQLQuery
				("select v.code,v.name,v.pallets from sup_pallet_view v where v.warehouse_id = "+warehouseId).list();
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,warehouseId);
		if(objs!=null&&objs.size()>0){
			int PALLET_QTY_IN = 0,PALLET_QTY_INFO = 0;
			for(Object[] obj : objs){
				Object value = null;
				try {
					value = wmsRuleManager.getSingleRuleTableDetail(warehouse.getName(),
							"R101_供应商托盘规则表","标准托盘个数",obj[0]);
				} catch (Exception e3) {
					System.out.println(e3.getLocalizedMessage());
					try {
						Thread.sleep(10000);//10min=600000,1min=60000
					} catch (InterruptedException ine) {
						logger.error("", ine);
					}
					try {
						value = wmsRuleManager.getSingleRuleTableDetail(warehouse.getName(),
								"R101_供应商托盘规则表","标准托盘个数",obj[0]);
					} catch (Exception e4) {
						System.out.println(e4.getLocalizedMessage());
						try {
							Thread.sleep(10000);//10min=600000,1min=60000
						} catch (InterruptedException ine) {
							logger.error("", ine);
						}
						try {
							value = wmsRuleManager.getSingleRuleTableDetail(warehouse.getName(),
									"R101_供应商托盘规则表","标准托盘个数",obj[0]);
						} catch (Exception e5) {
							System.out.println(e5.getLocalizedMessage());
						}
					}
				}
				if(value!=null){
					PALLET_QTY_IN = Integer.valueOf(obj[2].toString());
					PALLET_QTY_INFO = Integer.valueOf(value.toString());
					if(PALLET_QTY_IN>PALLET_QTY_INFO){
						SQLQuery query = session.createSQLQuery("insert into SCREEN_HG_INVENTORY_OUT(type,SUP_CODE,SUP_NAME,PALLET_QTY_IN,PALLET_QTY_INFO)"
								+ " values('不合格品超库存',?,?,?,?)");
						query.setString(0, obj[0].toString());
						query.setString(1, obj[1].toString());
						query.setInteger(2, PALLET_QTY_IN);//实际托盘个数
						query.setInteger(3, PALLET_QTY_INFO);//标准托盘个数
						query.executeUpdate();
					}
				}
			}
		}
	}
	public String screenAsnPre(){
		HibernateTransactionManager t = (HibernateTransactionManager)   
                applicationContext.getBean("transactionManager");  
        Session session = t.getSessionFactory().getCurrentSession();  
        String produce = "call insert_screen_asn_pre(?,?,?)";  
        CallableStatement call = null;  
        String mes = "success";  
        try {  
            call = session.connection().prepareCall(produce);  
            call.setInt(1, 50);  
            call.registerOutParameter(2, Types.INTEGER);  
            call.registerOutParameter(3, Types.VARCHAR);  
            call.execute();  
            if(call.getInt(2)!=0){  
                mes = call.getString(3);  
                mes = MyUtils.font(mes);  
            }  
        } catch (HibernateException e) {  
            e.printStackTrace();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }finally{  
            if(call!=null){  
                try {  
                    call.close();  
                } catch (SQLException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
		return mes;  
	}
	public Object getSingleRuleTableDetail(String warehouseName,Object... objects){
		Object obj = wmsRuleManager.getSingleRuleTableDetail(warehouseName,objects);
		return obj;
	}
	public List<Map<String,Object>>  getAllRuleTableDetail(String warehouseName,Object... objects){
		List<Map<String,Object>>  list = wmsRuleManager.getAllRuleTableDetail(warehouseName, objects);
		return list;
	}
	//增加目标序列号
	public void addDescWie(WmsInventory dstInventory,String pallet,WmsLocation toLocation,Double moveQty){
		List<WmsInventoryExtend> wsns = null;
		if(dstInventory!=null){
			if(toLocation.getType().equals(WmsLocationType.STORAGE)){
				WmsInventoryExtend wsn = null;
				//增加目标库位序列号的时候,根据库位ID获取,找不到则正常新增,error->[找到且含有托盘则放弃源托盘-合并,找到且不含有托盘则带走-合并]
				//找到:{原不含有托盘,上量少的}{原含有托盘:[匹配-上匹配托盘上][匹配不上-新建]}
				wsns = commonDao.findByQuery(
						"FROM WmsInventoryExtend wsn WHERE wsn.inventory.id = :inventoryId",
						new String[] { "inventoryId" },new Object[] { dstInventory.getId() });
				if(wsns!=null && wsns.size()>0){
					if(pallet.equals(BaseStatus.NULLVALUE)){//上量少的
						double qty = 0;
						int i = 0;
						for(WmsInventoryExtend ws : wsns){
							if(i==0){
								qty = ws.getAvailableQuantityBU();
								wsn = ws;
								i++;
								continue;
							}
							if(qty>ws.getAvailableQuantityBU()){
								qty = ws.getAvailableQuantityBU();
								wsn = ws;
							}
						}
					}else{
						for(WmsInventoryExtend ws : wsns){
							if(pallet.equals(ws.getPallet())){
								wsn = ws;break;
							}
						}
					}
				}
				if(wsn==null){
					wsn = inventoryExtendManager.newInventoryExtend(dstInventory, pallet, BaseStatus.NULLVALUE,BaseStatus.NULLVALUE);
				}else{
					if(wsn.getPallet()==null || wsn.getPallet().equals(BaseStatus.NULLVALUE)){
						wsn.setPallet(pallet);
					}
				}
				wsn.addQuantity(moveQty);
				commonDao.store(wsn);
			}
		}
	}
	public void saveTask(String[] obj){
//		Task task = new Task(businessType, taskSubscriber, wmsInterfaceLog.getId());
		Task task = new Task(obj[0],obj[1],Long.parseLong(obj[2]));
		this.commonDao.store(task);
		if(task.getMessageId()==null || task.getMessageId() == 0L){
			task.setMessageId(task.getId());
			commonDao.store(task);
		}
	}
	public void mesMisAdjust(MesMisInventory mes,List<String> values){
		//实际结存=实际结存-合格品出库-不合格品出库-检验报废-其它出库+入库调整
		//合格品出库-hgout,不合格品出库-unhgout,检验报废-scrapout,其它出库-otherout,入库调整+asnAdjust
		String[] function = new String[]{//依次代表页面输入列参与加减
				"-","-","-","-",""
		};
		if(function.length!=values.size()){
			throw new BusinessException("当前页面输入列与需求计算列不符,请联系技术人员");
		}
		String[] columns = new String[]{//页面输入列
				"hgout","unhgout","scrapout","otherout","asnAdjust"
		};
		//如果将来要调整业务,就调整上面的两个String数组即可(function,columns)
		Class<MesMisInventory> c = MesMisInventory.class; 
		int j = 0;
		Double quantity = 0D;
		Double tempQty = mes.getCalQuantity();
		for(String s : values){
			if(JavaTools.isNumber(s)){
				quantity = Double.valueOf(function[j]+s);
			}else{
				quantity = 0D;//录入不为数字时默认0
			}
			tempQty += quantity;
			//字段赋值
			try {
				Field xf = c.getField(columns[j]);
				try {
					xf.set(mes,Math.abs(quantity));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			} catch (SecurityException e) {
				e.printStackTrace();
				throw new BusinessException(e.getMessage());
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				throw new BusinessException(e.getMessage());
			}
			j++;
		}
		mes.setCalQuantity(tempQty);
		commonDao.store(mes);
	}

	@Override
	public void monitorActualInventory() {
		
		logger.error("-----开始监控实际库存------");
		
		HibernateDaoSupport support = (HibernateDaoSupport) commonDao;
		Session session = support.getHibernateTemplate().getSessionFactory().openSession();
		CallableStatement callableStatement = null;
		
		try {
			callableStatement = session.connection().prepareCall("{call monitorActualInventory()}");
			callableStatement.execute();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		} catch (SQLException ee) {
			try {
				 if(callableStatement != null){
					 callableStatement.close();
				 }
			} catch (SQLException e) {
				e.printStackTrace();
			}
			 session.close();
			 ee.printStackTrace();
		}
		logger.error("-----结束监控实际库存------");
	}
	@Override
	public void importSafeInventory(File file){
		String name = file.getName();
		if (file == null) {
			throw new BusinessException("file_not_found");
		} else if (!name.substring(name.lastIndexOf(".") + 1,
				name.lastIndexOf(".") + 4).equals("xls")) {
			throw new BusinessException("this.file.error");
		}
		Sheet sheet = null;
		@SuppressWarnings("unused")
		Sheet[] sheets = null;
		try {
			Workbook wb = Workbook.getWorkbook(new FileInputStream(file));
			sheet = wb.getSheet(0);
			sheets = wb.getSheets();// 获取所有的sheet  
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int rowNum = sheet.getRows();
		List<String> errorLog = new ArrayList<String>();//记录报错信息
		List<Object[]> objs = new ArrayList<Object[]>();
		for (int i = 1; i < rowNum; i++) {//含头信息
			String company = sheet.getCell(0,i).getContents();//货主
			String type = sheet.getCell(1,i).getContents();//级别
			String supplyNo = sheet.getCell(2,i).getContents();//供应商编码
			String itemCode = sheet.getCell(3,i).getContents();//物料编码
			String safeNums = sheet.getCell(4,i).getContents();//安全库存
			String warnNum = sheet.getCell(5,i).getContents();//报警库存
			
			Double minInvQty = 0D,safeInvQty=0d;
			try {
				minInvQty = Double.valueOf(warnNum);
				safeInvQty = Double.valueOf(safeNums);
			} catch (Exception e) {
				throw new BusinessException("第["+rowNum+"]行报警库存或安全库存格式有误");
			}
			validateIsNull(type,errorLog,"第["+(i+1)+"]行级别不能为空!!");
			validateIsNull(supplyNo,errorLog,"第["+(i+1)+"]行供应商不能为空!!");
			validateIsNull(itemCode,errorLog,"第["+(i+1)+"]行货品编码不能为空!!");
			validateIsNull(safeNums,errorLog,"第["+(i+1)+"]行安全库存不能为空!!");
			validateIsNull(warnNum,errorLog,"第["+(i+1)+"]行报缺库存不能为空!!");
			validateIsNull(company,errorLog,"第["+(i+1)+"]行货主不能为空!!");
			
			WmsOrganization organization = null;//货主
			if(company.equals("寄存") || company.equals("临采")){
				organization = findCompanyByName(company);
			}else{
				throw new BusinessException("第["+(i+1)+"]行货主只能填寄存/临采!!");
			}
			
			if(type.equals("仓库")){
				type = WmsSafeInventoryType.WAREHOUSE_INVENTORY;
			}else if(type.equals("备料区")){
				type = WmsSafeInventoryType.ZONE_INVENTORY;
			}else{
				throw new BusinessException("第["+(i+1)+"]行级别只能填仓库/备料区!!");
			}
			WmsOrganization org = validateSupplyNo(supplyNo,errorLog,"根据第["+(i+1)+"]行供应商编码："+supplyNo+"没有找到对应信息,请检查!");
			WmsItem item = validateItemCode(itemCode,errorLog,"根据第["+(i+1)+"]行货品编码："+itemCode+"没有找到对应信息,请检查!",organization,(i+1));
			Double invQty = countInventory(item.getId(),org.getId());//查实际库存
			
			WmsSafeInventory safeInventory = new WmsSafeInventory();
			safeInventory.setIsRed(Boolean.FALSE);
			safeInventory.setItem(item);
			safeInventory.setMinInvQty(minInvQty);
			safeInventory.setSafeInvQty(safeInvQty);
			safeInventory.setSuppiler(org);
			safeInventory.setType(type);
			safeInventory.setStatus(BaseStatus.ENABLED);
			safeInventory.setRealInventory(invQty);
			commonDao.store(safeInventory);
//			safeInventory.setRealInventory();
			
//			objs.add(new Object[]{//供应商编码,供应商名称,物料编码,物料名称,期初量,入库量,MES要货量
//					supCode,supName,itemCode,itemName,initQty,0,0
//			});
		}
		int size = objs.size();
		if(size>0){
			String sql = "truncate table "+MesMisInventory.tableName;
//			jdbcTemplate.execute(sql);
//			sql = "update "+MiddleTableName.MIDDLE_MES_ORDER+" m set m.flag = 8 where m.flag = 0";
//			jdbcTemplate.execute(sql);
			int j = JavaTools.getSize(size, PAGE_NUMBER);
			for(int k = 0 ; k < j ; k++){
				System.out.println(j+"..."+(k+1));
				int index = JavaTools.getIndex(k, size, PAGE_NUMBER);
				List<Object[]> obj = JavaTools.getListObj(objs, k, index, PAGE_NUMBER);
//				milldleSessionManager.initMesMisInventory(obj, 1);
			}
		}
	}
	
	void validateIsNull(String value,List<String> errorLog,String msg){
		if(StringUtils.isEmpty(value)){
			errorLog.add(msg);
			throw new BusinessException(msg);
		}
	}
	/**校验供应商是否存在*/
	WmsOrganization validateSupplyNo(String value,List<String> errorLog,String msg){
		if(null != value && !"".equals(value)){
			value = value.toUpperCase();
		}
		String hql = "from WmsOrganization where code=:code and status='ENABLED'";
		WmsOrganization org = (WmsOrganization) commonDao.
							findByQueryUniqueResult(hql, "code", value);
		if(null == org){
			errorLog.add(msg);
			throw new BusinessException(msg);
		}
		return org;
	}
	/**校验货品编码是否存在*/
	WmsItem validateItemCode(String value,List<String> errorLog,String msg,WmsOrganization org,int rownum){
		if(null != value && !"".equals(value)){
			value = value.toUpperCase();
		}
		String hql = "from WmsItem where code=:code and status='ENABLED' and company.id=:cId";
		WmsItem item = null;
		try {
			item = (WmsItem) commonDao.
					findByQueryUniqueResult(hql, new String[]{"code","cId"},new Object[]{value,org.getId()});
		} catch (Exception e) {
			throw new BusinessException("根据第["+rownum+"]行货主和物料找到了多条数据");
		}
		if(null == item){
			errorLog.add(msg);
			throw new BusinessException(msg);
		}
		return item;
	}
	
	/**
	 * 查实际库存
	 * @param itemId
	 * @param supplyId
	 * @return
	 */
	private Double countInventory(Long itemId,Long supplyId){
		String hql = "select sum(w.quantityBU) from WmsInventoryExtend w "
					+ "where w.inventory.itemKey.item.id=:itemId "
					+ "and w.inventory.itemKey.lotInfo.supplier.id=:supplierId";
		Double invQty = (Double) commonDao.findByQueryUniqueResult(hql,
				new String[]{"itemId","supplierId"},new Object[]{itemId,supplyId});
		return invQty == null ? 0d : invQty;
	}
	
	/**校验货主是否存在*/
	private WmsOrganization findCompanyByName(String value){
		String hql = "from WmsOrganization where neiBuName=:neiBuName";
		List<WmsOrganization> org = commonDao.findByQuery(hql, "neiBuName", value);
		if(org.size() <= 0 ){
			throw new BusinessException("没有找到货主["+value+"]");
		}
		return org.get(0);
	}
}