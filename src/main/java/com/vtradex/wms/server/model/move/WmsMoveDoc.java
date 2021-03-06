package com.vtradex.wms.server.model.move;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.UpdateInfo;
import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.base.LotInfo;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.process.WmsProcessPlan;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.model.shipping.WmsBOLStateLog;
import com.vtradex.wms.server.model.shipping.WmsMasterBOL;
import com.vtradex.wms.server.model.shipping.WmsPickTicket;
import com.vtradex.wms.server.model.shipping.WmsWaveDoc;
import com.vtradex.wms.server.model.warehouse.WmsDock;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
/**
 * 移位单
 * 
 * @category Entity
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.8 $Date: 2016/05/16 07:15:01 $
 */
public class WmsMoveDoc extends VersionalEntity {

	private static final long serialVersionUID = 7405076234886403104L;

	/** 仓库 */
	@UniqueKey
	private WmsWarehouse warehouse;

	/** 货主 */
	@UniqueKey
	private WmsOrganization company;

	/** 单据类型 */
	@UniqueKey
	private WmsBillType billType;

	/** 单据编码 */
	@UniqueKey
	private String code;

	/**
	 * 移位单类型
	 * 
	 * {@link WmsMoveDocType}
	 */
	private String type;

	/** 发货单 */
	private WmsPickTicket pickTicket;

	/** 收货单 */
	private WmsASN asn;

	/** 波次 */
	private WmsWaveDoc waveDoc;

	/** 加工货品 */
	private WmsItem item;

	/** 批次属性要求 */
	private LotInfo lotInfo;

	/** 加工方案 */
	private WmsProcessPlan processPlan;

	/**
	 * 状态: 打开，整单分配，部分分配，生效，作业中，完成
	 * 
	 * {@link WmsMoveDocStatus}
	 */
	private String status = WmsMoveDocStatus.OPEN;

	/**
	 * 状态: 未发运，已发运
	 * 
	 * {@link WmsMoveDocShipStatus}
	 */
	private String shipStatus = WmsMoveDocShipStatus.UNSHIP;

	/***
	 * 运输状态 WmsTransState/fdj质检用作接口状态 WmsWorkDocStatus
	 */
	private String transStatus;

	/** 原始单据类型 */
	private WmsBillType originalBillType;
	/** 原始单据号 */
	private String originalBillCode;

	/** 相关单号1 */
	private String relatedBill1;
	/** 相关单号2 / 质检用作记录SOI */
	private String relatedBill2;
	/** 相关单号3 */
	private String relatedBill3;

	/** 计划移位数量BU */
	private Double planQuantityBU = 0.0D;
	/** 分配数量BU */
	private Double allocatedQuantityBU = 0.0D;
	/** 移位数量BU */
	private Double movedQuantityBU = 0.0D;
	/** 发货数量BU */
	private Double shippedQuantityBU = 0.0D;
	/** 已加工套数 */
	private Double processQuantityBU = 0.0D;
	/** 包装数量 */
	private Double packagingQuantityBU = 0.0D;

	/** 加工费用 */
	private Double payment = 0.0D;

	/** 拣货扫码开始时间 */
	private Date reserveBeginTime;
	/** 收货预约结束时间 */
	private Date reserveFinishTime;
	/** 预计发货时间 */
	private Date intendShipDate;
	/** 发车时间 */
	private Date shipTime;

	/** 装车单 */
	private WmsMasterBOL masterBOL;

	/** 承运商 */
	private WmsOrganization carrier;
	/** 司机 /拣货备料作为RF扫码人员使用*/
	private String driver;
	/** 车牌号 */
	private String vehicleNo;
	/** 是否已加入波次 */
	private boolean beWave = false;

	/** 是否越库 */
	private boolean beCrossDock = false;
	/** 备货或加工库位 */
	private WmsLocation shipLocation;

	/** 发货月台 */
	private WmsDock dock;

	/** 移位明细 */
	private Set<WmsMoveDocDetail> details = new HashSet<WmsMoveDocDetail>();

	/** 包装明细 */
	private Set<WmsBoxDetail> boxDetails = new HashSet<WmsBoxDetail>();

	/** 发运状态日志 **/
	private Set<WmsBOLStateLog> stateLogs = new HashSet<WmsBOLStateLog>();

	/** 标题 */
	private String movePlanTitle;

	/** 作业班组 */
	private WmsWorker worker;

	/** 行号 */
	private Integer lineNo;

	/** 物料属性-分类**/
	private String classType;
	/** 物料优先级-ABC分类**/
	private String class4;

	/** 是否虚拟移位计划 */
	private Boolean beVirtualMove = Boolean.FALSE;
	
	/**
	 * 状态: 待加工，加工中，完成
	 * 
	 * {@link WmsMoveDocProcessStatus&WmsQualityStatus}
	 */
	private String processStatus;
	
	/** 是否核单 */
	private boolean beScanBol = false;
	/** 核单时间 */
	private Date scanBolTime;
	
	/** 备料工*/
	private WmsWorker blg;
	
	/**
	 * 打印送检单时间
	 */
	private Date printQualityReportDate;
	/**
	 * 打印送检单人员
	 */
	private String printWorker;
	
	/** 是否欠品*/
	private Boolean isOweProduct = Boolean.FALSE;
	/**工位*/
	private String station;

	/**是否已经打印*/
	private Boolean isPrint;
	
	/**打印时间*/
	private Date printDate;
	
	/**打印人*/
	private String printUser;
	/**生产线*/
	private String productionLine;
	
	public WmsMoveDoc(WmsWarehouse warehouse, WmsOrganization company,
		WmsBillType billType, String code,String title) {
		super();
		this.warehouse = warehouse;
		this.company = company;
		this.billType = billType;
		this.code = code;
		this.movePlanTitle = title;
		this.type = WmsMoveDocType.MV_QUALITY_MOVE;
		this.processStatus = WmsQualityStatus.NULLVALUE;
		this.transStatus = WmsWorkDocStatus.OPEN;
		this.originalBillCode = null;
		this.originalBillType = null;
	}
	
	
	public String getProductionLine() {
		return productionLine;
	}
	public void setProductionLine(String productionLine) {
		this.productionLine = productionLine;
	}

	public String getPrintUser() {
		return printUser;
	}

	public String getClass4() {
		return class4;
	}

	public void setClass4(String class4) {
		this.class4 = class4;
	}

	public void setPrintUser(String printUser) {
		this.printUser = printUser;
	}



	public Boolean getIsPrint() {
		return isPrint;
	}



	public void setIsPrint(Boolean isPrint) {
		this.isPrint = isPrint;
	}



	public Date getPrintDate() {
		return printDate;
	}



	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}



	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public Boolean getIsOweProduct() {
		return isOweProduct;
	}

	public void setIsOweProduct(Boolean isOweProduct) {
		this.isOweProduct = isOweProduct;
	}

	public Date getPrintQualityReportDate() {
		return printQualityReportDate;
	}

	public void setPrintQualityReportDate(Date printQualityReportDate) {
		this.printQualityReportDate = printQualityReportDate;
	}

	public String getPrintWorker() {
		return printWorker;
	}

	public void setPrintWorker(String printWorker) {
		this.printWorker = printWorker;
	}

	public WmsWorker getBlg() {
		return blg;
	}

	public void setBlg(WmsWorker blg) {
		this.blg = blg;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public WmsMoveDoc() {

	}

	public Boolean getBeVirtualMove() {
		return beVirtualMove;
	}

	public void setBeVirtualMove(Boolean beVirtualMove) {
		this.beVirtualMove = beVirtualMove;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public WmsWorker getWorker() {
		return worker;
	}

	public void setWorker(WmsWorker worker) {
		this.worker = worker;
	}

	public String getMovePlanTitle() {
		return movePlanTitle;
	}

	public void setMovePlanTitle(String movePlanTitle) {
		this.movePlanTitle = movePlanTitle;
	}

	public Set<WmsBOLStateLog> getStateLogs() {
		return stateLogs;
	}

	public void setStateLogs(Set<WmsBOLStateLog> stateLogs) {
		this.stateLogs = stateLogs;
	}

	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}

	public Date getIntendShipDate() {
		return intendShipDate;
	}

	public void setIntendShipDate(Date intendShipDate) {
		this.intendShipDate = intendShipDate;
	}

	public String getRelatedBill1() {
		return relatedBill1;
	}

	public void setRelatedBill1(String relatedBill1) {
		this.relatedBill1 = relatedBill1;
	}

	public String getRelatedBill2() {
		return relatedBill2;
	}

	public void setRelatedBill2(String relatedBill2) {
		this.relatedBill2 = relatedBill2;
	}

	public String getRelatedBill3() {
		return relatedBill3;
	}

	public void setRelatedBill3(String relatedBill3) {
		this.relatedBill3 = relatedBill3;
	}

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public WmsOrganization getCompany() {
		return company;
	}

	public void setCompany(WmsOrganization company) {
		this.company = company;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public WmsPickTicket getPickTicket() {
		return pickTicket;
	}

	public void setPickTicket(WmsPickTicket pickTicket) {
		this.pickTicket = pickTicket;
	}

	public WmsASN getAsn() {
		return asn;
	}

	public void setAsn(WmsASN asn) {
		this.asn = asn;
	}

	public WmsWaveDoc getWaveDoc() {
		return waveDoc;
	}

	public void setWaveDoc(WmsWaveDoc waveDoc) {
		this.waveDoc = waveDoc;
	}

	public WmsItem getItem() {
		return item;
	}

	public void setItem(WmsItem item) {
		this.item = item;
	}

	public LotInfo getLotInfo() {
		return lotInfo;
	}

	public void setLotInfo(LotInfo lotInfo) {
		this.lotInfo = lotInfo;
	}

	public WmsProcessPlan getProcessPlan() {
		return processPlan;
	}

	public void setProcessPlan(WmsProcessPlan processPlan) {
		this.processPlan = processPlan;
	}

	public WmsBillType getBillType() {
		return billType;
	}

	public void setBillType(WmsBillType billType) {
		this.billType = billType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public WmsBillType getOriginalBillType() {
		return originalBillType;
	}

	public void setOriginalBillType(WmsBillType originalBillType) {
		this.originalBillType = originalBillType;
	}

	public String getOriginalBillCode() {
		return originalBillCode;
	}

	public void setOriginalBillCode(String originalBillCode) {
		this.originalBillCode = originalBillCode;
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

	public Double getProcessQuantityBU() {
		return processQuantityBU;
	}

	public void setProcessQuantityBU(Double processQuantityBU) {
		this.processQuantityBU = processQuantityBU;
	}

	public Double getPackagingQuantityBU() {
		return packagingQuantityBU;
	}

	public void setPackagingQuantityBU(Double packagingQuantityBU) {
		this.packagingQuantityBU = packagingQuantityBU;
	}

	public Double getPayment() {
		return payment;
	}

	public void setPayment(Double payment) {
		this.payment = payment;
	}

	public Date getShipTime() {
		return shipTime;
	}

	public void setShipTime(Date shipTime) {
		this.shipTime = shipTime;
	}

	public Set<WmsMoveDocDetail> getDetails() {
		return details;
	}

	public void setDetails(Set<WmsMoveDocDetail> details) {
		this.details = details;
	}

	public WmsMasterBOL getMasterBOL() {
		return masterBOL;
	}

	public void setMasterBOL(WmsMasterBOL masterBOL) {
		this.masterBOL = masterBOL;
	}

	public String getShipStatus() {
		return shipStatus;
	}

	public void setShipStatus(String shipStatus) {
		this.shipStatus = shipStatus;
	}

	public Double getShippedQuantityBU() {
		return shippedQuantityBU;
	}

	public void setShippedQuantityBU(Double shippedQuantityBU) {
		this.shippedQuantityBU = shippedQuantityBU;
	}

	public WmsOrganization getCarrier() {
		return carrier;
	}

	public void setCarrier(WmsOrganization carrier) {
		this.carrier = carrier;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public boolean getBeWave() {
		return beWave;
	}

	public void setBeWave(boolean beWave) {
		this.beWave = beWave;
	}

	public boolean isBeCrossDock() {
		return beCrossDock;
	}

	public void setBeCrossDock(boolean beCrossDock) {
		this.beCrossDock = beCrossDock;
	}

	public WmsLocation getShipLocation() {
		return shipLocation;
	}

	public void setShipLocation(WmsLocation shipLocation) {
		this.shipLocation = shipLocation;
	}

	public WmsDock getDock() {
		return dock;
	}

	public void setDock(WmsDock dock) {
		this.dock = dock;
	}

	public Set<WmsBoxDetail> getBoxDetails() {
		return boxDetails;
	}

	public void setBoxDetails(Set<WmsBoxDetail> boxDetails) {
		this.boxDetails = boxDetails;
	}

	public String isAllocate() {
		if (this.planQuantityBU.doubleValue() <= 0D
				&& this.allocatedQuantityBU.doubleValue() <= 0D) {
			return "NONE";
		}
		if (this.planQuantityBU.doubleValue() <= this.allocatedQuantityBU
				.doubleValue()) {
			return "ALL";
		}
		if (this.planQuantityBU.doubleValue() != this.allocatedQuantityBU
				.doubleValue() && this.allocatedQuantityBU.doubleValue() > 0.0d) {
			return "PART";
		}
		return "NONE";
	}

	public String isFinish() {
		if (this.allocatedQuantityBU.doubleValue() <= this.movedQuantityBU
				.doubleValue()
				&& this.planQuantityBU.doubleValue() <= this.movedQuantityBU
						.doubleValue()) {
			return "Y";
		}
		return "N";
	}

	public void process(Double quantity) {
		this.processQuantityBU += quantity;
	}

	public void unprocess(Double quantity) {
		this.processQuantityBU -= quantity;
	}
	
	public void allocate(Double quantity) {
		this.allocatedQuantityBU += quantity;
	}

	public void unallocate(Double quantity) {
		this.allocatedQuantityBU -= quantity;
		if(this.allocatedQuantityBU<0){
			this.allocatedQuantityBU = 0D;
		}
	}

	/**
	 * 新增明细
	 * 
	 * @param detail
	 */
	public void addDetail(WmsMoveDocDetail detail) {
		this.getDetails().add(detail);
		refreshQuantity();
	}

	public void addBoxDetail(WmsBoxDetail boxDetail) {
		this.getBoxDetails().add(boxDetail);

		this.packagingQuantityBU = 0.0D;
		for (WmsBoxDetail bd : this.boxDetails) {
			this.packagingQuantityBU += bd.getQuantity();
		}
	}

	/**
	 * 删除明细
	 * 
	 * @param detail
	 */
	public void removeDetail(WmsMoveDocDetail detail) {
		this.getDetails().remove(detail);
		refreshQuantity();
	}

	/**
	 * 刷新单头数量
	 */
	public void refreshQuantity() {
		this.planQuantityBU = 0.0D;
		for (WmsMoveDocDetail moveDocDetail : this.getDetails()) {
			this.planQuantityBU += moveDocDetail.getPlanQuantityBU();
		}
	}

	/**
	 * 移位确认
	 * 
	 * @param quantity
	 */
	public void move(double quantity) {
		this.movedQuantityBU += quantity;
	}

	/**
	 * 取消移位确认
	 * 
	 * @param quantity
	 */
	public void cancelMove(double quantity) {
		this.movedQuantityBU -= quantity;
	}

	/**
	 * 发货确认
	 */
	public void ship() {
		this.refreshShippedQty();
		this.setShipTime(new Date());
	}

	/**
	 * 刷新发货单发运数量
	 */
	public void refreshShippedQty() {
		this.shippedQuantityBU = 0.0D;
		for (WmsMoveDocDetail detail : this.details) {
			this.shippedQuantityBU += detail.getShippedQuantityBU();
		}
	}

	/**
	 * 获取未分配数量
	 */
	public Double getUnAllocateQuantityBU() {
		return this.planQuantityBU - this.allocatedQuantityBU;
	}

	/**
	 * 上架
	 * 
	 * @return
	 */
	public boolean isPutawayType() {
		if (WmsMoveDocType.MV_PUTAWAY.equals(this.getType())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 移位
	 * 
	 * @return
	 */
	public boolean isMoveType() {
		if (WmsMoveDocType.MV_MOVE.equals(this.getType())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 拣货
	 * 
	 * @return
	 */
	public boolean isPickTicketType() {
		if (WmsMoveDocType.MV_PICKTICKET_PICKING.equals(this.getType())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 波次拣货
	 * 
	 * @return
	 */
	public boolean isWaveType() {
		if (WmsMoveDocType.MV_WAVE_PICKING.equals(this.getType())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 补货
	 * 
	 * @return
	 */
	public boolean isReplenishmentType() {
		if (WmsMoveDocType.MV_REPLENISHMENT_MOVE.equals(this.getType())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 加工
	 * 
	 * @return
	 */
	public boolean isProcessType() {
		if (WmsMoveDocType.MV_PROCESS_PICKING.equals(this.getType())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 退拣数量
	 * 
	 * @param quantity
	 */
	public void pickBack(Double quantity) {
		this.planQuantityBU -= quantity;
		this.allocatedQuantityBU -= quantity;
		this.movedQuantityBU -= quantity;
	}
	public void pickBackStatus(){
		if(this.movedQuantityBU==0){
			this.status = WmsMoveDocStatus.ACTIVE;
		}else if(this.movedQuantityBU>0 && this.movedQuantityBU<this.allocatedQuantityBU){
			this.status = WmsMoveDocStatus.WORKING;
		}
	}
	public void originalPickBack(Double quantity) {
		this.allocatedQuantityBU -= quantity;
		this.movedQuantityBU -= quantity;
	}
	public double getUnMoveQuantityBU(){
		return this.planQuantityBU-this.movedQuantityBU;
	}
	
	public void refreshPlanQuantity(){
		this.planQuantityBU = 0D;
		for(WmsMoveDocDetail detail : this.details){
			this.planQuantityBU += detail.getPlanQuantityBU();
		}
	}
	public WmsMoveDoc cloneWmsMoveDoc(WmsMoveDoc cloneMoveDoc) {
		BeanUtils.copyEntity(cloneMoveDoc, this);
		cloneMoveDoc.setId(null);
		cloneMoveDoc.setCode(null);
		cloneMoveDoc.setMasterBOL(null);
		cloneMoveDoc.setVehicleNo(null);
		cloneMoveDoc.setShipTime(null);
		cloneMoveDoc.setUpdateInfo(new UpdateInfo(UserHolder.getUser()));
		cloneMoveDoc.setDriver(null);
		cloneMoveDoc.setPlanQuantityBU(0D);
		cloneMoveDoc.setAllocatedQuantityBU(0D);
		cloneMoveDoc.setMovedQuantityBU(0D);
		cloneMoveDoc.setShippedQuantityBU(0D);
		cloneMoveDoc.setDetails(new HashSet<WmsMoveDocDetail>());
		cloneMoveDoc.setBoxDetails(new HashSet<WmsBoxDetail>());
		cloneMoveDoc.setStateLogs(new HashSet<WmsBOLStateLog>());
		return cloneMoveDoc;
	}

	public WmsMoveDoc cloneWmsMoveDoc() {
		return cloneWmsMoveDoc(new WmsMoveDoc());
	}

	public Date getReserveBeginTime() {
		return reserveBeginTime;
	}

	public void setReserveBeginTime(Date reserveBeginTime) {
		this.reserveBeginTime = reserveBeginTime;
	}

	public Date getReserveFinishTime() {
		return reserveFinishTime;
	}

	public void setReserveFinishTime(Date reserveFinishTime) {
		this.reserveFinishTime = reserveFinishTime;
	}

	public Integer getLineNo() {
		return lineNo;
	}

	public void setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
	}

	public boolean isBeScanBol() {
		return beScanBol;
	}

	public void setBeScanBol(boolean beScanBol) {
		this.beScanBol = beScanBol;
	}

	public Date getScanBolTime() {
		return scanBolTime;
	}

	public void setScanBolTime(Date scanBolTime) {
		this.scanBolTime = scanBolTime;
	}
	
}