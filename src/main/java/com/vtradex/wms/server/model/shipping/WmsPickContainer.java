package com.vtradex.wms.server.model.shipping;

import com.vtradex.thorn.server.model.Entity;

/**拣货单报表展示*/
public class WmsPickContainer extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WmsPickContainer() {
		super();
	}
	//拣货单ID
	private Long pickId;
	//pick_code 拣货单号
	private String pickCode;
	//require_arrive_date 需求日期
	private String requireArriveDate;
	//bill_type 单据类型
	private String billType;
	//production_line 生产线
	private String productionLine;
	//blg_name 备料工
	private String blgName;
	//box_tag 器具标签
	private String boxTag;
	//box_type 器具型号
	private String boxType;
	//type_name 器具名称 
	private String typeName;
	//sup_code
	private String supCode;
	//sup_name
	private String supName;
	//item_code
	private String itemCode;
	//item_name
	private String itemName;
	//from_loc_code
	private String fromLocCode;
	/**station 工位*/
	private String station;
	//doc  收货道口
	private String dockNo;
	//seq
	private Double seq = 0D;
	//end_seq
	private Double endseq = 0D;//结束序号
	//quantity
	private Double quantity = 0D;
	
	/**工作中心*/
	private String odrSu;//WmsPickTicket.odrSu
	
	public WmsPickContainer(Long pickId, String pickCode,
			String requireArriveDate, String billType, String productionLine,
			String blgName, String boxTag, String boxType, String typeName,
			String supCode, String supName, String itemCode, String itemName,
			String fromLocCode, String station, String dockNo, Double seq,
			Double endseq, Double quantity,String odrSu) {
		super();
		this.pickId = pickId;
		this.pickCode = pickCode;
		this.requireArriveDate = requireArriveDate;
		this.billType = billType;
		this.productionLine = productionLine;
		this.blgName = blgName;
		this.boxTag = boxTag;
		this.boxType = boxType;
		this.typeName = typeName;
		this.supCode = supCode;
		this.supName = supName;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.fromLocCode = fromLocCode;
		this.station = station;
		this.dockNo = dockNo;
		this.seq = seq;
		this.endseq = endseq;
		this.quantity = quantity;
		this.odrSu = odrSu;
	}
	
	public String getOdrSu() {
		return odrSu;
	}

	public void setOdrSu(String odrSu) {
		this.odrSu = odrSu;
	}

	public Long getPickId() {
		return pickId;
	}
	public void setPickId(Long pickId) {
		this.pickId = pickId;
	}
	public String getPickCode() {
		return pickCode;
	}
	public void setPickCode(String pickCode) {
		this.pickCode = pickCode;
	}
	public String getRequireArriveDate() {
		return requireArriveDate;
	}
	public void setRequireArriveDate(String requireArriveDate) {
		this.requireArriveDate = requireArriveDate;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getProductionLine() {
		return productionLine;
	}
	public void setProductionLine(String productionLine) {
		this.productionLine = productionLine;
	}
	public String getBlgName() {
		return blgName;
	}
	public void setBlgName(String blgName) {
		this.blgName = blgName;
	}
	public String getBoxTag() {
		return boxTag;
	}
	public void setBoxTag(String boxTag) {
		this.boxTag = boxTag;
	}
	public String getBoxType() {
		return boxType;
	}
	public void setBoxType(String boxType) {
		this.boxType = boxType;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
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
	public String getFromLocCode() {
		return fromLocCode;
	}
	public void setFromLocCode(String fromLocCode) {
		this.fromLocCode = fromLocCode;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public String getDockNo() {
		return dockNo;
	}
	public void setDockNo(String dockNo) {
		this.dockNo = dockNo;
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
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	

}
