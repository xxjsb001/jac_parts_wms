package com.vtradex.wms.server.model.shipping;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;

/**
 * 发货单和器具对照表
 * 存储接口数据
 * @author fs
 *
 */
public class WmsPickTicketAndAppliance extends Entity  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 738705103831685287L;

	private String sheetNo;//直送送货单单号
	private String no;//器具标签号（唯一码）
	private Date planTime;//计划日期
	private String fromStorage;//来源仓库
	private String toStorage;//目的仓库
	private String supplierNo;//供应商代码
	private String supplierName;//供应商名称
	private String packageNo;//器具型号
	private String packageName;//工位器具名称
	private Double packageNum;//工位器具容量
	private String partNo;//物料代码
	private String partName;//物料名称
	private Double deliveryQty;//配送单数量
	private Double qty;//数量
	private Double seq;//序号
	private Double endseq;//结束序号
	private Boolean isSps;//是否SPS
	private String lineNo;
	private String dockNo;//收货道口
	private Integer curPag;//第几张
	private Integer totalPage;//总张数
	private String ulocNo;//工位
	private String remark;//备注
	private Integer sx;//顺序
	
	
	private Double activeQty = 0D;//激活数量,解决同一物料拆分拣货的场景
	
	private Double pickQty = 0D;//虚拟扣减时判断当前明细已拣货量
	
	public WmsPickTicketAndAppliance(){}
	
	
	public WmsPickTicketAndAppliance(String sheetNo, String no, Date planTime,
			String fromStorage, String toStorage, String supplierNo,
			String supplierName, String packageNo, String packageName,
			Double packageNum, String partNo, String partName,
			Double deliveryQty, Double qty, Double seq,Double endseq,Boolean isSps,
			String lineNo, String dockNo, Integer curPag, Integer totalPage,
			String ulocNo, String remark,Integer sx) {
		super();
		this.sheetNo = sheetNo;
		this.no = no;
		this.planTime = planTime;
		this.fromStorage = fromStorage;
		this.toStorage = toStorage;
		this.supplierNo = supplierNo;
		this.supplierName = supplierName;
		this.packageNo = packageNo;
		this.packageName = packageName;
		this.packageNum = packageNum;
		this.partNo = partNo;
		this.partName = partName;
		this.deliveryQty = deliveryQty;
		this.qty = qty;
		this.seq = seq;
		this.endseq = endseq;
		this.isSps = isSps;
		this.lineNo = lineNo;
		this.dockNo = dockNo;
		this.curPag = curPag;
		this.totalPage = totalPage;
		this.ulocNo = ulocNo;
		this.remark = remark;
		this.sx = sx;
	}

	public Double getPickQty() {
		return pickQty;
	}


	public void setPickQty(Double pickQty) {
		this.pickQty = pickQty;
	}


	public Double getActiveQty() {
		return activeQty;
	}


	public void setActiveQty(Double activeQty) {
		this.activeQty = activeQty;
	}


	public Integer getSx() {
		return sx;
	}
	public void setSx(Integer sx) {
		this.sx = sx;
	}
	public String getToStorage() {
		return toStorage;
	}
	public void setToStorage(String toStorage) {
		this.toStorage = toStorage;
	}
	public String getSheetNo() {
		return sheetNo;
	}
	public void setSheetNo(String sheetNo) {
		this.sheetNo = sheetNo;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public Date getPlanTime() {
		return planTime;
	}
	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}
	public String getFromStorage() {
		return fromStorage;
	}
	public void setFromStorage(String fromStorage) {
		this.fromStorage = fromStorage;
	}
	public String getSupplierNo() {
		return supplierNo;
	}
	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Double getPackageNum() {
		return packageNum;
	}
	public void setPackageNum(Double packageNum) {
		this.packageNum = packageNum;
	}
	public String getPartNo() {
		return partNo;
	}
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public Double getDeliveryQty() {
		return deliveryQty;
	}
	public void setDeliveryQty(Double deliveryQty) {
		this.deliveryQty = deliveryQty;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public Double getSeq() {
		return seq;
	}
	public void setSeq(Double seq) {
		this.seq = seq;
	}
	public Double getEndseq() {
		return endseq;
	}


	public void setEndseq(Double endseq) {
		this.endseq = endseq;
	}


	public Boolean getIsSps() {
		return isSps;
	}
	public void setIsSps(Boolean isSps) {
		this.isSps = isSps;
	}
	public String getLineNo() {
		return lineNo;
	}
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	public String getDockNo() {
		return dockNo;
	}
	public void setDockNo(String dockNo) {
		this.dockNo = dockNo;
	}
	public Integer getCurPag() {
		return curPag;
	}
	public void setCurPag(Integer curPag) {
		this.curPag = curPag;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public String getUlocNo() {
		return ulocNo;
	}
	public void setUlocNo(String ulocNo) {
		this.ulocNo = ulocNo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getAvailableQuantityBU(){
		return this.qty-this.pickQty;
	}
	
	
	
	
}
