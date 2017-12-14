package com.vtradex.wms.server.model.middle;

import java.util.Date;

import com.vtradex.thorn.server.model.VersionalEntity;
/**时序件生产排序 中间表数据同步 wms获取信息转化后插入对象 WmsPickTicketAndAppliance 打印器具标签  扫码交接 装车 发运*/
public class WmsSpsProductLine extends VersionalEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sheetNo;//直送送货单单号(MES单号,和WMS接口的发货单匹配上之后更新)
	private String orderNo;//bom里面维护订单号
	private Date planTime;//计划日期(上线日期)
	private Integer sx;//上线顺序
	private Integer containSx;//备料顺序
	private String itemCode;//物料代码
	private String itemName;//物料名称
	private String productLine;//产线
	private String station;//工位
	private String stock;//道口
	private Double qty;//数量
	private String remark;//备注
	private Integer isSps = 0;
	//@WmsMoveDocType.ORDER_PICKING/SPS_PICKING
	private String type;//类型(时序/排序)
	private String c5;//器具名称
	private Integer loadage =0;//满载量
	private Integer line = 0;//备料顺序
	private String boxTag;
	private String supCode;
	private String supName;
	
	public WmsSpsProductLine() {
		super();
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getProductLine() {
		return productLine;
	}
	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getSheetNo() {
		return sheetNo;
	}
	public void setSheetNo(String sheetNo) {
		this.sheetNo = sheetNo;
	}
	public Date getPlanTime() {
		return planTime;
	}
	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}
	public Integer getSx() {
		return sx;
	}
	public void setSx(Integer sx) {
		this.sx = sx;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getIsSps() {
		return isSps;
	}
	public void setIsSps(Integer isSps) {
		this.isSps = isSps;
	}
	public Integer getContainSx() {
		return containSx;
	}
	public void setContainSx(Integer containSx) {
		this.containSx = containSx;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getC5() {
		return c5;
	}
	public void setC5(String c5) {
		this.c5 = c5;
	}
	public Integer getLoadage() {
		return loadage;
	}
	public void setLoadage(Integer loadage) {
		this.loadage = loadage;
	}
	public Integer getLine() {
		return line;
	}
	public void setLine(Integer line) {
		this.line = line;
	}
	public String getBoxTag() {
		return boxTag;
	}
	public void setBoxTag(String boxTag) {
		this.boxTag = boxTag;
	}
	public String getSupCode() {
		return supCode;
	}
	public void setSupCode(String supCode) {
		this.supCode = supCode;
	}
	public String getSupName() {
		return supName;
	}
	public void setSupName(String supName) {
		this.supName = supName;
	}
}
