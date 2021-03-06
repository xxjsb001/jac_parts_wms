package com.vtradex.wms.server.model.move;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.ShipLotInfo;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.shipping.WmsWaveDocDetail;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.PackageUtils;

/**
 * 移位单明细
 *
 * @category Entity 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.9 $Date: 2016/05/16 07:15:01 $
 */
public class WmsMoveDocDetail extends Entity {
	
	private static final long serialVersionUID = 4429117429389060783L;

	/** 移位单*/
	private WmsMoveDoc moveDoc;
	
	/** 货品 */
	private WmsItem item;
	/** 批次属性*/
	private WmsItemKey itemKey;
	/** 批次属性要求 */
	private ShipLotInfo shipLotInfo;
	
	/**
	 * 相关ID, 上架明细Id、波次明细Id、库存子表id(质检新建明细)等
	 */
	private Long relatedId;
	
	/** 移出库位ID */
	private Long fromLocationId;
	/** 移出库位编码 */
	private String fromLocationCode;
	/** 源库存ID */
	private Long inventoryId;
	
	/** 库存状态*/
	private String inventoryStatus = BaseStatus.NULLVALUE;
	
	/** 托盘号 */
	private String pallet;
	/** 箱号 */
	private String carton;
	/** 序列号 */
	private String serialNo;
	/** 是否托盘  */
	private Boolean bePallet = Boolean.FALSE;
	
	/** 目标库存ID, 散货移位时使用 */
	private Long destInventoryId;
	/** 移入库位ID */
	private Long toLocationId;
	/** 移入库位 */
	private String toLocationCode;
	
	/** 包装单位*/
	private WmsPackageUnit packageUnit;
	
	/** 计划移位数量*/
	private Double planQuantity = 0D;
	/** 计划移位数量BU*/
	private Double planQuantityBU = 0.0D;
	/** 分配数量BU*/
	private Double allocatedQuantityBU = 0.0D;
	/** 移位数量BU*/
	private Double movedQuantityBU = 0.0D;
	/** 发货数量BU*/
	private Double shippedQuantityBU = 0.0D;
	/** 加工数量BU*/
	private Double processQuantityBU = 0.0D;
	/** 对应方案单位物料数量BU / 质检作为ASN明细入库量*/
	private Double processPlanQuantityBU;
	/** 包装数量 */
	private Double packagingQuantityBU = 0.0D;
	
	/** 重量 */
	private Double weight = 0D;
	/** 体积 */
	private Double volume = 0D;
	/**生产日期*/
	private Date productDate;
	/**保质日期至*/
	private Date expireDate;
	/**近效期*/
	private Date warnDate;
	/**收货日期*/
	private Date receivedDate;
	
	/** 补货区 /JAC质检唯一单号/批次号*/
	private String replenishmentArea;
	
	/** 拣货区 */
	private String pickArea;
	
	/**
	 * 波次明细
	 */
	private Set<WmsWaveDocDetail> waveDocDetails = new HashSet<WmsWaveDocDetail>();
	
	/** 任务清单*/
	private Set<WmsTask> tasks = new HashSet<WmsTask>();
	
	/** 是否包装完成,RF补货作为是否补货完成 */
	private Boolean bePackageFinish = Boolean.FALSE;
	
	private Long srcInvExId; //江淮RF货品移位专用
	
	/**===============新增 2017-6-22 15:18:38==============================*/
	/** 需求时间*/
	private Date needTime;
	/** 备料工号*/
	private String pickWorkerCode;
	/** 备料工*/
	private String pickWorker;
	/**生产线*/
	private String productionLine;	
	/**送检分类*/
	private String qualityType;
	/**物料条码*/
	private String itemBarCode;
	
	public WmsMoveDocDetail() {}
	
	
	
	
	public WmsMoveDocDetail(WmsMoveDoc moveDoc, WmsItem item,
			WmsItemKey itemKey, Double planQuantity, Double planQuantityBU) {
		super();
		this.moveDoc = moveDoc;
		this.item = item;
		this.itemKey = itemKey;
		this.planQuantity = planQuantity;
		this.planQuantityBU = planQuantityBU;
		this.allocatedQuantityBU = 0d;
		this.movedQuantityBU = 0d;
		this.processQuantityBU = 0d;
	}





	public String getItemBarCode() {
		return itemBarCode;
	}

	public void setItemBarCode(String itemBarCode) {
		this.itemBarCode = itemBarCode;
	}

	public Date getNeedTime() {
		return needTime;
	}

	public String getQualityType() {
		return qualityType;
	}

	public void setQualityType(String qualityType) {
		this.qualityType = qualityType;
	}




	public void setNeedTime(Date needTime) {
		this.needTime = needTime;
	}




	public String getPickWorkerCode() {
		return pickWorkerCode;
	}




	public void setPickWorkerCode(String pickWorkerCode) {
		this.pickWorkerCode = pickWorkerCode;
	}




	public String getPickWorker() {
		return pickWorker;
	}




	public void setPickWorker(String pickWorker) {
		this.pickWorker = pickWorker;
	}




	public String getProductionLine() {
		return productionLine;
	}




	public void setProductionLine(String productionLine) {
		this.productionLine = productionLine;
	}




	public Long getSrcInvExId() {
		return srcInvExId;
	}


	public void setSrcInvExId(Long srcInvExId) {
		this.srcInvExId = srcInvExId;
	}


	public String getCarton() {
		return carton;
	}

	public void setCarton(String carton) {
		this.carton = carton;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Date getProductDate() {
		return productDate;
	}

	public void setProductDate(Date productDate) {
		this.productDate = productDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public Date getWarnDate() {
		return warnDate;
	}

	public void setWarnDate(Date warnDate) {
		this.warnDate = warnDate;
	}


	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Long getInventoryId() {
		return inventoryId;
	}


	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
	}


	public WmsMoveDoc getMoveDoc() {
		return moveDoc;
	}

	public void setMoveDoc(WmsMoveDoc moveDoc) {
		this.moveDoc = moveDoc;
	}

	public WmsItem getItem() {
		return item;
	}

	public void setItem(WmsItem item) {
		this.item = item;
	}

	public WmsItemKey getItemKey() {
		return itemKey;
	}

	public void setItemKey(WmsItemKey itemKey) {
		this.itemKey = itemKey;
	}

	public ShipLotInfo getShipLotInfo() {
		return shipLotInfo;
	}

	public void setShipLotInfo(ShipLotInfo shipLotInfo) {
		this.shipLotInfo = shipLotInfo;
	}

	public Long getRelatedId() {
		return relatedId;
	}


	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}


	public Long getFromLocationId() {
		return fromLocationId;
	}

	public void setFromLocationId(Long fromLocationId) {
		this.fromLocationId = fromLocationId;
	}

	public String getFromLocationCode() {
		return fromLocationCode;
	}

	public void setFromLocationCode(String fromLocationCode) {
		this.fromLocationCode = fromLocationCode;
	}

	public String getPallet() {
		return pallet;
	}


	public void setPallet(String pallet) {
		this.pallet = pallet;
	}


	public Boolean getBePallet() {
		return bePallet;
	}


	public void setBePallet(Boolean bePallet) {
		this.bePallet = bePallet;
	}

	public Long getDestInventoryId() {
		return destInventoryId;
	}

	public void setDestInventoryId(Long destInventoryId) {
		this.destInventoryId = destInventoryId;
	}

	public Long getToLocationId() {
		return toLocationId;
	}

	public void setToLocationId(Long toLocationId) {
		this.toLocationId = toLocationId;
	}

	public String getToLocationCode() {
		return toLocationCode;
	}

	public void setToLocationCode(String toLocationCode) {
		this.toLocationCode = toLocationCode;
	}

	public String getInventoryStatus() {
		return inventoryStatus;
	}

	public void setInventoryStatus(String inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}

	public WmsPackageUnit getPackageUnit() {
		return packageUnit;
	}

	public void setPackageUnit(WmsPackageUnit packageUnit) {
		this.packageUnit = packageUnit;
	}

	public Double getPlanQuantity() {
		return planQuantity;
	}

	public void setPlanQuantity(Double planQuantity) {
		this.planQuantity = planQuantity;
	}

	public Double getPlanQuantityBU() {
		return planQuantityBU;
	}

	public void setPlanQuantityBU(Double planQuantityBU) {
		this.planQuantityBU = planQuantityBU;
	}

	public Double getAllocatedQuantityBU() {
		return allocatedQuantityBU;
	}

	public void setAllocatedQuantityBU(Double allocatedQuantityBU) {
		this.allocatedQuantityBU = allocatedQuantityBU;
	}

	public Double getMovedQuantityBU() {
		return movedQuantityBU;
	}

	public void setMovedQuantityBU(Double movedQuantityBU) {
		this.movedQuantityBU = movedQuantityBU;
	}

	public Double getShippedQuantityBU() {
		return shippedQuantityBU;
	}

	public void setShippedQuantityBU(Double shippedQuantityBU) {
		this.shippedQuantityBU = shippedQuantityBU;
	}

	public Double getProcessQuantityBU() {
		return processQuantityBU;
	}


	public void setProcessQuantityBU(Double processQuantityBU) {
		this.processQuantityBU = processQuantityBU;
	}


	public Set<WmsTask> getTasks() {
		return tasks;
	}

	public void setTasks(Set<WmsTask> tasks) {
		this.tasks = tasks;
	}
	
	public String getReplenishmentArea() {
		return replenishmentArea;
	}

	public void setReplenishmentArea(String replenishmentArea) {
		this.replenishmentArea = replenishmentArea;
	}
	
	public String getPickArea() {
		return pickArea;
	}

	public void setPickArea(String pickArea) {
		this.pickArea = pickArea;
	}

	public Set<WmsWaveDocDetail> getWaveDocDetails() {
		return waveDocDetails;
	}

	public void setWaveDocDetails(Set<WmsWaveDocDetail> waveDocDetails) {
		this.waveDocDetails = waveDocDetails;
	}

	public Double getProcessPlanQuantityBU() {
		return processPlanQuantityBU;
	}

	public void setProcessPlanQuantityBU(Double processPlanQuantityBU) {
		this.processPlanQuantityBU = processPlanQuantityBU;
	}
	
	public Double getPackagingQuantityBU() {
		return packagingQuantityBU;
	}

	public void setPackagingQuantityBU(Double packagingQuantityBU) {
		this.packagingQuantityBU = packagingQuantityBU;
	}

	public Boolean getBePackageFinish() {
		return bePackageFinish;
	}

	public void setBePackageFinish(Boolean bePackageFinish) {
		this.bePackageFinish = bePackageFinish;
	}

	public void addWaveDocDetail(WmsWaveDocDetail waveDocDetail){
		if(waveDocDetails == null){
			waveDocDetails = new HashSet<WmsWaveDocDetail>();
		}
		waveDocDetails.add(waveDocDetail);
	}
	
	public void removeWaveDocDetail(WmsWaveDocDetail waveDocDetail){
		if(waveDocDetails == null){
			return;
		}
		waveDocDetails.remove(waveDocDetail);
	}
	/**
	 * 删除TASK
	 * 
	 * @param detail
	 */
	public void removeDetail(WmsTask task) {
		this.getTasks().remove(task);
	}

	public String isAllocate(){
		if(this.planQuantityBU.doubleValue()==this.allocatedQuantityBU.doubleValue()){
			return "ALL";
		}
		if(this.planQuantityBU.doubleValue()!=this.allocatedQuantityBU.doubleValue() && this.allocatedQuantityBU.doubleValue()>0){
			return "PART";
		}
		return "NONE";
	}
	
	public void allocate(Double quantity) {
		this.allocatedQuantityBU += quantity;
		//加工单按套
		if(this.moveDoc.isProcessType()){
		} else {
			this.moveDoc.allocate(quantity);
		}
	}
	
	public void unallocate(Double quantity) {
		this.allocatedQuantityBU -= quantity;
		if(this.allocatedQuantityBU<0){
			this.allocatedQuantityBU = 0D;
		}
		//加工单按套
		if(this.moveDoc.isProcessType()){
		} else {
			this.moveDoc.unallocate(quantity);
		}
	}

	/**
	 * 移位确认
	 * @param
	 */
	public void move(double quantity) {
		this.movedQuantityBU += quantity;
		//加工单按套
		if(this.moveDoc.isProcessType()){
		} else {
			this.moveDoc.move(quantity);
		}
	}
	
	/**
	 * 取消移位确认
	 * @param
	 */
	public void cancelMove(double quantity) {
		this.movedQuantityBU -= quantity;
		//加工单按套
		if(this.moveDoc.isProcessType()){
		} else {
			this.moveDoc.cancelMove(quantity);
		}
	}
	/**增加加工数量*/
	public void process(Double quantity) {
		this.processQuantityBU += quantity;
		this.moveDoc.process(quantity);
	}
	/**接口状态*/
	public void setTransStatus() {
		if(this.moveDoc.getProcessQuantityBU().equals(this.moveDoc.getPlanQuantityBU())){
			this.moveDoc.setTransStatus(WmsWorkDocStatus.FINISHED);
		}
	}
	/**取消加工数量*/
	public void unprocess(Double quantity) {
		this.processQuantityBU -= quantity;
		this.moveDoc.unprocess(quantity);
	}
	/**
	 * 获取未分配数量
	 * @return
	 */
	public Double getUnAllocateQuantityBU() {
		return this.planQuantityBU - this.allocatedQuantityBU;
	}
	/**
	 * 获取未加工数量
	 * @return
	 */
	public Double getUnProcessQuantityBU() {
		return this.planQuantityBU - this.processQuantityBU;
	}
	
	/**
	 * 获取未移位数量
	 * @return
	 */
	public Double getUnMovedQuantityBU() {
		return this.allocatedQuantityBU - this.movedQuantityBU;
	}

	/**
	 * 增加移位单明细BU数量
	 * @param quantityBU
	 */
	public void addPlanQuantityBU(Double quantityBU) {
		this.planQuantityBU += quantityBU;
		this.planQuantity = PackageUtils.convertPackQuantity(this.planQuantityBU, this.packageUnit);
	}
	
	public void addPackagingQuantityBU(Double packagingQuantityBU) {
		this.packagingQuantityBU += packagingQuantityBU;
		
		if (DoubleUtils.compareByPrecision(this.packagingQuantityBU, this.movedQuantityBU, 3) == 0) {
			this.bePackageFinish = Boolean.TRUE;
		}
	}

	/**
	 * 发货确认
	 * @param quantity
	 */
	public void ship(Double quantity) {
		this.shippedQuantityBU += quantity;
		
		this.moveDoc.ship();
	}
	
	/**
	 * 退拣数量
	 * @param quantity
	 */
	public void pickBack(Double quantity) {
		this.planQuantityBU -= quantity;
		this.planQuantity = PackageUtils.convertPackQuantity(this.planQuantityBU, this.packageUnit);
		this.allocatedQuantityBU -= quantity;
		this.movedQuantityBU -= quantity;
		this.moveDoc.pickBack(quantity);
		calculateLoad();
	}
	
	public void originalPickBack(Double quantity) {
		this.allocatedQuantityBU -= quantity;
		this.movedQuantityBU -= quantity;
		this.moveDoc.originalPickBack(quantity);
		calculateLoad();
	}
	/**未移位量*/
	public Double unMoveQty(){
		return this.planQuantityBU-this.movedQuantityBU;
	}
	
	public void calculateLoad(){
		setWeight(DoubleUtils.formatByPrecision(getPlanQuantity() * getPackageUnit().getWeight(),3));
		setVolume(DoubleUtils.formatByPrecision(getPlanQuantity() * getPackageUnit().getVolume(),3));
	}
}