package com.vtradex.wms.server.model.receiving;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.exception.OriginalBusinessException;
import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.base.Contact;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;

/**
 * @category ASN
 */
public class WmsASN extends VersionalEntity {	
	private static final long serialVersionUID = -6136786512413727612L;
	/** 仓库 */
	@UniqueKey
	private WmsWarehouse warehouse;
	/** 货主 */
	@UniqueKey
	private WmsOrganization company;
	/** 单据类型 */
	@UniqueKey
	private WmsBillType billType;
	/** 单据号 */
	@UniqueKey
	private String code;
	/** 
	 * 状态
	 * {@link WmsASNStatus}
	 *  */
	private String status;
	/** 相关单号1 送货单号 */
	private String relatedBill1;
	/** 相关单号2 订单号*/
	private String relatedBill2;
	/** 相关单号3 */
	private String relatedBill3;
	/** 订单日期 */
	private Date orderDate;
	/** 预计到货日期 */
	private Date estimateDate;
	/**收货开始时间*/
	private Date startReceivedDate;
	/**收货结束时间*/
	private Date endReceivedDate;
	/** 发货人 --备用*/
	private String fromName;
	/** 发货人联系方式 --备用 */
	private Contact fromContact;
	/** 期待数量BU */
	private Double expectedQuantityBU = 0.0D;
	/** 收货数量BU */
	private Double receivedQuantityBU = 0.0D;
	/**上架数量*/
	private Double movedQuantityBU=0.0D;
	/** ASN明细清单 */
	private Set<WmsASNDetail> details = new HashSet<WmsASNDetail>();
	/** ASN收货日志 */
	private Set<WmsReceivedRecord> records = new HashSet<WmsReceivedRecord>();
	/** WmsASN上架状态*/
	private String shelvesStauts = WmsASNShelvesStauts.UNPUTAWAY;
	/** 质检状态*/
	private String qualityStauts = WmsASNQualityStauts.UNQUALITY;
	
	/** 待检可上架 **/
	private boolean qualityPutaway = true;
	/**===============JAC====================*/
	/** 供应商*/
	private WmsOrganization supplier;
	/** 分配状态*/
	private String autoAllocateStauts = WmsMoveDocStatus.OPEN;
	/** 分配数量BU */
	private Double allocatedQuantityBU = 0.0D;
	/**打印ASN收货单时间*/
	private Date printASNReportDate;
	/**
	 * {@link WmsSource}
	 * 来源
	 */
	private String source =WmsSource.MANUAL;
	/**
	 * 备注
	 */
	private String remark;
	
	//过账确认 1=过账确认成功
	private Boolean confirmAccount;
	//是否送检码托
	private Boolean isCheckMT=Boolean.FALSE;
	/**打印托盘标签人、打印时间、是否打印标签*/
	private String printPerson;
	private Date printDate;
	private Boolean isPrint = Boolean.FALSE;
	
	
	public WmsASN(){

	}
	public WmsASN(WmsWarehouse warehouse, WmsOrganization company,
			WmsBillType billType, String code, String status,
			String relatedBill1, String relatedBill2, Date orderDate,
			String shelvesStauts, WmsOrganization supplier, 
			String autoAllocateStauts,Double allocatedQuantityBU,
			String source,Double expectedQuantityBU,Boolean confirmAccount) {
		super();
		this.warehouse = warehouse;
		this.company = company;
		this.billType = billType;
		this.code = code;
		this.status = status;
		this.relatedBill1 = relatedBill1;
		this.relatedBill2 = relatedBill2;
		this.orderDate = orderDate;
		this.shelvesStauts = shelvesStauts;
		this.supplier = supplier;
		this.autoAllocateStauts = autoAllocateStauts;
		this.allocatedQuantityBU = allocatedQuantityBU;
		this.source = source;
		this.expectedQuantityBU = expectedQuantityBU;
		this.confirmAccount = confirmAccount;
	}

	

	public String getPrintPerson() {
		return printPerson;
	}

	public void setPrintPerson(String printPerson) {
		this.printPerson = printPerson;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public Boolean getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(Boolean isPrint) {
		this.isPrint = isPrint;
	}

	public Boolean getIsCheckMT() {
		return isCheckMT;
	}

	public void setIsCheckMT(Boolean isCheckMT) {
		this.isCheckMT = isCheckMT;
	}

	public Boolean getConfirmAccount() {
		return confirmAccount;
	}

	public void setConfirmAccount(Boolean confirmAccount) {
		this.confirmAccount = confirmAccount;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getPrintASNReportDate() {
		return printASNReportDate;
	}

	public void setPrintASNReportDate(Date printASNReportDate) {
		this.printASNReportDate = printASNReportDate;
	}

	public Double getAllocatedQuantityBU() {
		return allocatedQuantityBU;
	}

	public void setAllocatedQuantityBU(Double allocatedQuantityBU) {
		this.allocatedQuantityBU = allocatedQuantityBU;
	}

	public String getAutoAllocateStauts() {
		return autoAllocateStauts;
	}

	public void setAutoAllocateStauts(String autoAllocateStauts) {
		this.autoAllocateStauts = autoAllocateStauts;
	}

	public void setSupplier(WmsOrganization supplier){
		this.supplier = supplier;
	}
	public WmsOrganization getSupplier(){
		return supplier;
	}
	public boolean isQualityPutaway() {
		return qualityPutaway;
	}

	public void setQualityPutaway(boolean qualityPutaway) {
		this.qualityPutaway = qualityPutaway;
	}



	public String getQualityStauts() {
		return qualityStauts;
	}



	public void setQualityStauts(String qualityStauts) {
		this.qualityStauts = qualityStauts;
	}



	public Set<WmsReceivedRecord> getRecords() {
		return records;
	}


	public void setRecords(Set<WmsReceivedRecord> records) {
		this.records = records;
	}


	public Date getStartReceivedDate() {
		return startReceivedDate;
	}


	public void setStartReceivedDate(Date startReceivedDate) {
		this.startReceivedDate = startReceivedDate;
	}


	public Date getEndReceivedDate() {
		return endReceivedDate;
	}


	public void setEndReceivedDate(Date endReceivedDate) {
		this.endReceivedDate = endReceivedDate;
	}


	public Double getMovedQuantityBU() {
		return movedQuantityBU;
	}


	public void setMovedQuantityBU(Double movedQuantityBU) {
		this.movedQuantityBU = movedQuantityBU;
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

	public WmsOrganization getCompany() {
		return company;
	}

	public void setCompany(WmsOrganization company) {
		this.company = company;
	}

	public Set<WmsASNDetail> getDetails() {
		return details;
	}

	public void setDetails(Set<WmsASNDetail> details) {
		this.details = details;
	}


	public Date getEstimateDate() {
		return estimateDate;
	}

	public void setEstimateDate(Date estimateDate) {
		this.estimateDate = estimateDate;
	}

	public Double getExpectedQuantityBU() {
		return expectedQuantityBU;
	}

	public void setExpectedQuantityBU(Double expectedQuantityBU) {
		this.expectedQuantityBU = expectedQuantityBU;
	}

	public Contact getFromContact() {
		return fromContact;
	}

	public void setFromContact(Contact fromContact) {
		this.fromContact = fromContact;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}


	public Double getReceivedQuantityBU() {
		return receivedQuantityBU;
	}

	public void setReceivedQuantityBU(Double receivedQuantityBU) {
		this.receivedQuantityBU = receivedQuantityBU;
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


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public String getShelvesStauts() {
		return shelvesStauts;
	}

	public void setShelvesStauts(String shelvesStauts) {
		this.shelvesStauts = shelvesStauts;
	}
	
	/**
	 * 新增ASN明细
	 * @param detail
	 */
	public void addDetail(WmsASNDetail detail) {
		this.details.add(detail);
	}
	
	/**
	 * 移除明细
	 * @param details
	 */
	public void removeDetail(WmsASNDetail detail) {
		this.details.remove(detail);
		detail.setAsn(null);
	}
	
	/**
	 * 刷新ASN订单数量
	 */
	public void refreshQtyBU() {
		this.expectedQuantityBU = 0.0D;
		
		for (WmsASNDetail detail : this.details) {
			this.expectedQuantityBU += detail.getExpectedQuantityBU();
		}
	}
	
	/**
	 * 收货
	 * @param quantity
	 */
	public void receive(double quantity) {
//		if(this.printASNReportDate==null){
//			throw new OriginalBusinessException("未打印收货单不允许收货");
//		}
		if (this.receivedQuantityBU <= 0)  {
			this.setStartReceivedDate(new Date());
		}
		this.receivedQuantityBU += quantity;
	}
	
	/**
	 * 取消收货
	 * @param quantity
	 */
	public void cancelReceive(double quantity) {
		this.receivedQuantityBU -= quantity;
		this.setEndReceivedDate(null);
		if (receivedQuantityBU <= 0) {
			this.setStartReceivedDate(null);
		}
	}
	
	/**
	 * 获取未收货数量
	 * @return
	 */
	public double getUnReceivedQtyBU() {
		return this.expectedQuantityBU - this.receivedQuantityBU;
	}
	
	/** 上架 */
	public void addMovedQuantity(Double movedQuantityBU) {
		this.movedQuantityBU += movedQuantityBU;
	}
	
	/** 取消上架 */
	public void cancelMovedQuantity(Double movedQuantityBU) {
		this.movedQuantityBU -= movedQuantityBU;
	}
	/** 分配调整  yc.min 20150409 */
	public void editAllocatedQuantityBU(Double allocatedQuantityBU){
		this.allocatedQuantityBU += allocatedQuantityBU;
		if(this.allocatedQuantityBU>=this.expectedQuantityBU){
			this.autoAllocateStauts = WmsMoveDocStatus.ALLOCATED;
		}else if(this.allocatedQuantityBU>0){
			this.autoAllocateStauts = WmsMoveDocStatus.PARTALLOCATED;
		}else{
			this.autoAllocateStauts = WmsMoveDocStatus.OPEN;
		}
	}
	/** 上架状态调整  yc.min 20150409 */
	public void editShelvesStauts(){
		if(this.movedQuantityBU>=this.expectedQuantityBU){
			this.shelvesStauts = WmsASNShelvesStauts.FINISHED;
		}else if(this.movedQuantityBU>0){
			this.shelvesStauts = WmsASNShelvesStauts.PUTAWAY;
		}else{
			this.shelvesStauts = WmsASNShelvesStauts.UNPUTAWAY;
		}
	}
}