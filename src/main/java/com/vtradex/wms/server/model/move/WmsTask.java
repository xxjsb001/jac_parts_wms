package com.vtradex.wms.server.model.move;

import java.util.Date;

import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.PackageUtils;

/**
 * 作业任务
 *
 * @category Entity 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.6 $Date: 2015/12/04 03:29:27 $
 */
public class WmsTask extends VersionalEntity {
	
	private static final long serialVersionUID = 298142216744584776L;
	
	/** 所属作业单*/
	private WmsWorkDoc workDoc;
	
	/** 所属移位明细 */
	private WmsMoveDocDetail moveDocDetail;
	
	/**
	 * 任务类型
	 * 
	 * {@link WmsTaskType}
	 */
	private String type;
	
	/** 资源类型 */
	private String resourcetype;
	
	/** 关联单据号*/
	private String originalBillCode;
	
	/**
	 * 原始单据号/扫码拣货时作为容器编码
	 */
	private String relatedBill;

	

	
	/** 托盘号 */
	private String pallet = BaseStatus.NULLVALUE;
	
	/** 源库存ID */
	private Long srcInventoryId;
	/** 移出库位ID */
	private Long fromLocationId;	
	/** 移出库位 */
	private String fromLocationCode;
	
	/** 目标库存ID */
	private Long descInventoryId;
	/** 移入库位ID */
	private Long toLocationId;
	/** 移入库位 */
	private String toLocationCode;
	
	/** 批次属性*/
	private WmsItemKey itemKey;
	
	/** 库存状态*/
	private String inventoryStatus;
	
	/** 包装单位*/
	private WmsPackageUnit packageUnit;
	
	/** 计划移位数量*/
	private Double planQuantity = 0D;
	/** 计划移位数量BU*/
	private Double planQuantityBU = 0D;
	/** 移位数量BU*/
	private Double movedQuantityBU = 0D;
	/** 当前累积移位数量--JAC发运量 */
	private Double tiredMovedQuantityBU = 0D;
	
	/** 重量 */
	private Double planWeight = 0D;
	/** 体积 */
	private Double planVolume = 0D;
	
	/** 是否手工分配任务--质检用作是否返库标记 */
	private Boolean beManual = Boolean.FALSE;
	
	/** 分配作业人员(说明：仅用于RF任务申请)*/
	private WmsWorker worker;
	
	/**
	 * 任务状态
	 * 
	 * {@link WmsTaskStatus}
	 */
	private String status = WmsTaskStatus.OPEN;
	/**拣货确认时间*/
	private Date pickTime;
	
	/** 异常标识*/
	private Boolean exceptionFlag = Boolean.FALSE;
	//器具标签
	private String boxTag;
	/**库内交接扫码时间*/
	private Date scanBoxTime;
	/**签收时间*/
	private Date signTime;
	
	
	public WmsTask(){

	}
	
	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public Date getScanBoxTime() {
		return scanBoxTime;
	}

	public void setScanBoxTime(Date scanBoxTime) {
		this.scanBoxTime = scanBoxTime;
	}


	public Boolean getExceptionFlag() {
		return exceptionFlag;
	}

	public void setExceptionFlag(Boolean exceptionFlag) {
		this.exceptionFlag = exceptionFlag;
	}

	public Date getPickTime() {
		return pickTime;
	}

	public void setPickTime(Date pickTime) {
		this.pickTime = pickTime;
	}

	public String getPallet() {
		return pallet;
	}



	public void setPallet(String pallet) {
		this.pallet = pallet;
	}



	public String getRelatedBill() {
		return relatedBill;
	}


	public void setRelatedBill(String relatedBill) {
		this.relatedBill = relatedBill;
	}



	public Double getPlanWeight() {
		return planWeight;
	}



	public void setPlanWeight(Double planWeight) {
		this.planWeight = planWeight;
	}



	public Double getPlanVolume() {
		return planVolume;
	}



	public void setPlanVolume(Double planVolume) {
		this.planVolume = planVolume;
	}



	public Boolean getBeManual() {
		return beManual;
	}


	public void setBeManual(Boolean beManual) {
		this.beManual = beManual;
	}


	public WmsMoveDocDetail getMoveDocDetail() {
		return moveDocDetail;
	}



	public void setMoveDocDetail(WmsMoveDocDetail moveDocDetail) {
		this.moveDocDetail = moveDocDetail;
	}


	public WmsWorkDoc getWorkDoc() {
		return workDoc;
	}

	public void setWorkDoc(WmsWorkDoc workDoc) {
		this.workDoc = workDoc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOriginalBillCode() {
		return originalBillCode;
	}

	public void setOriginalBillCode(String originalBillCode) {
		this.originalBillCode = originalBillCode;
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

	public WmsItemKey getItemKey() {
		return itemKey;
	}

	public void setItemKey(WmsItemKey itemKey) {
		this.itemKey = itemKey;
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

	public Double getMovedQuantityBU() {
		return movedQuantityBU;
	}

	public void setMovedQuantityBU(Double movedQuantityBU) {
		this.movedQuantityBU = movedQuantityBU;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public WmsWorker getWorker() {
		return worker;
	}

	public void setWorker(WmsWorker worker) {
		this.worker = worker;
	}

	public Long getSrcInventoryId() {
		return srcInventoryId;
	}

	public void setSrcInventoryId(Long srcInventoryId) {
		this.srcInventoryId = srcInventoryId;
	}

	public String getResourcetype() {
		return resourcetype;
	}

	public void setResourcetype(String resourcetype) {
		this.resourcetype = resourcetype;
	}

	public Long getDescInventoryId() {
		return descInventoryId;
	}

	public void setDescInventoryId(Long descInventoryId) {
		this.descInventoryId = descInventoryId;
	}
	
	public Double getTiredMovedQuantityBU() {
		return tiredMovedQuantityBU;
	}

	public void setTiredMovedQuantityBU(Double tiredMovedQuantityBU) {
		this.tiredMovedQuantityBU = tiredMovedQuantityBU;
	}
	
	public void addTiredMovedQuantityBU(Double tiredMovedQuantityBU) {
		this.tiredMovedQuantityBU += tiredMovedQuantityBU;
		if(this.tiredMovedQuantityBU>=this.movedQuantityBU){
			this.status = WmsTaskStatus.FINISHED;
		}
	}

	public boolean isFinished() {		
		if(DoubleUtils.compareByPrecision(this.planQuantityBU, this.movedQuantityBU, this.packageUnit.getPrecision()) <= 0) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * 获取任务中未作业数量
	 * @return
	 */
	public Double getUnmovedQuantityBU() {
		return this.planQuantityBU - this.movedQuantityBU;
	}
	/**
	 * 获取任务中未发运数量
	 * @return
	 */
	public Double getUnShipQuantityBU() {
		return this.movedQuantityBU - this.tiredMovedQuantityBU;
	}
	
	/** 
	 * 增加移位数量BU
	 * */
	public void addMovedQuantityBU(Double quantityBU) {
		this.movedQuantityBU += quantityBU;
	}
	
	/**
	 * 取消上架分配
	 */
	public void unallocatePutaway() {
		this.planQuantityBU = this.movedQuantityBU;
		this.planQuantity = PackageUtils.convertPackQuantity(this.planQuantityBU, this.packageUnit);
	}
	
	/**
	 * @param quantity 基本单位数量
	 */
	public void unallocatePick(double quantityBU) {
		this.planQuantityBU -= quantityBU;
		this.planQuantity = PackageUtils.convertPackQuantity(this.planQuantityBU, this.packageUnit);
		
		this.getMoveDocDetail().unallocate(quantityBU);
		
		if (this.getWorkDoc() != null) {
			this.getWorkDoc().removeQuantityBU(quantityBU);
		}
	}

	/**
	 * 退拣
	 * @param movedQuantity
	 */
	public void pickBak(double backQuantityBU) {
		this.planQuantityBU -= backQuantityBU;
		if(this.packageUnit.getConvertFigure()==1){
			this.planQuantity -= backQuantityBU;
		}else{
			planQuantity = planQuantityBU/this.packageUnit.getConvertFigure();
		}
		this.movedQuantityBU -= backQuantityBU;
		if(this.moveDocDetail!=null){
			this.moveDocDetail.pickBack(backQuantityBU);//更新移位单
		}
		if(this.workDoc!=null){
			this.workDoc.pickBackMoveQuantityBU(backQuantityBU);//更新作业单
		}
		calculateLoad();
	}

	/**
	 * 判断是否上架类型任务
	 * @return true-上架类型 false-拣货类型
	 */
	public boolean bePutType() {
		if (WmsTaskType.MV_PUTAWAY.equals(this.type) || WmsTaskType.MV_MOVE.equals(this.type)) {
			return true;
		}
		return false;
	}

	public void addPlanQuantityBU(Double quantity) {
		this.planQuantityBU += quantity;
		this.planQuantity = PackageUtils.convertPackQuantity(this.planQuantityBU, this.packageUnit);
	}
	public void removePlanQuantityBU(Double quantity) {
		this.planQuantityBU -= quantity;
		this.planQuantity = PackageUtils.convertPackQuantity(this.planQuantityBU, this.packageUnit);
	}
	
	/**
	 * 上架
	 * @return
	 */
	public boolean isPutawayType() {
		if (WmsTaskType.MV_PUTAWAY.equals(this.getType())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 移位
	 * @return
	 */
	public boolean isMoveType() {
		if (WmsTaskType.MV_PUTAWAY.equals(this.getType())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 拣货
	 * @return
	 */
	public boolean isPickTicketType() {
		if (WmsTaskType.MV_PICKTICKET_PICKING.equals(this.getType())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 波次拣货
	 * @return
	 */
	public boolean isWaveType() {
		if (WmsTaskType.MV_WAVE_PICKING.equals(this.getType())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 补货
	 * @return
	 */
	public boolean isReplenishmentType() {
		if (WmsTaskType.MV_REPLENISHMENT_MOVE.equals(this.getType())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 加工
	 * @return
	 */
	public boolean isProcessType() {
		if (WmsTaskType.MV_PROCESS_PICKING.equals(this.getType())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public void calculateLoad(){
		setPlanWeight(DoubleUtils.formatByPrecision(getPlanQuantity() * getPackageUnit().getWeight(),3));
		setPlanVolume(DoubleUtils.formatByPrecision(getPlanQuantity() * getPackageUnit().getVolume(),3));
	}
	/** 
	 * 调整移位数量BU yc.min 20150409
	 * */
	public void editMovedQuantityBU(Double quantityBU) {
		this.movedQuantityBU += quantityBU;
		this.pickTime = new Date();
		if(this.movedQuantityBU>=this.planQuantityBU){
			this.status = WmsTaskStatus.FINISHED;
		}else if(this.movedQuantityBU>0){
			this.status = WmsTaskStatus.WORKING;
		}else{
			this.status = WmsTaskStatus.OPEN;
		}
	}
	public void editMoveQty(Double quantityBU){
		this.movedQuantityBU += quantityBU;
		this.pickTime = new Date();
		this.status = WmsTaskStatus.OPEN;
	}
	public void removeMove(){
		this.moveDocDetail = null;
		this.originalBillCode = null;
		this.relatedBill= null;
	}

	public String getBoxTag() {
		return boxTag;
	}

	public void setBoxTag(String boxTag) {
		this.boxTag = boxTag;
	}
}