package com.vtradex.wms.server.model.shipping;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.carrier.WmsDriver;
import com.vtradex.wms.server.model.carrier.WmsVehicle;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;

public class WmsBOL extends Entity {

	private static final long serialVersionUID = 7391834635362415729L;
	
	/** BOL流水号 */
	private String code;
	
	/** 托盘数量  */
	private Double palletQuantity=0D;
	
	/** 物料总数 */
	private Double quantity=0D;

	/** 物料总数BU */
	private Double quantityBU=0D;
	
	/** 已发运数量 */
	private Double shippedQuantity=0D;
	
	/** 状态 */
	private String status = WmsBOLStatus.UNSHIP;
	
	/** 托盘号 */
	private String pallet;
	
	/** 作业单号 */
	private String relateCode;
	
	/** BOL明细 */
	private Set<WmsBOLDetail> details = new HashSet<WmsBOLDetail>();
	
	/** 仓库 */
	private WmsWarehouse warehouse;
	
	/** 承运商 */
	private WmsOrganization carrier;
	/** 司机 */
	private String driver;
	/** 司机 */
	private WmsDriver wmsDriver;
	/** 车牌号 */
	private WmsVehicle vehicle;
	/** 车牌号 */
	private String vehicleNo;
	/** 发运时间*/
	private Date shipTime;
	/**描述*/
	private String description;
	
	/** 发货类型*/
	private String billTypeName;
	
	/**打印配送单时间*/
	private Date printTime;
	/**配送单打印人*/
	private String printWorker; 
	/**打印器具单时间*/
	private Date printTagTime;
	/**器具单打印人*/
	private String printTagWorker; 
	
	/**发货单号*/
	private String pickCode;
	/** 要求达到时间 */
	private Date requireArriveDate;
	/**器具标签总数*/
	private Integer boxTagNums = 0;
	/**生产线*/
	private String productionLine;
	
	public String getProductionLine() {
		return productionLine;
	}

	public void setProductionLine(String productionLine) {
		this.productionLine = productionLine;
	}

	public Date getRequireArriveDate() {
		return requireArriveDate;
	}

	public void setRequireArriveDate(Date requireArriveDate) {
		this.requireArriveDate = requireArriveDate;
	}

	public Integer getBoxTagNums() {
		return boxTagNums;
	}

	public void setBoxTagNums(Integer boxTagNums) {
		this.boxTagNums = boxTagNums;
	}

	public String getPickCode() {
		return pickCode;
	}

	public void setPickCode(String pickCode) {
		this.pickCode = pickCode;
	}

	public Date getPrintTime() {
		return printTime;
	}

	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
	}

	public String getPrintWorker() {
		return printWorker;
	}

	public void setPrintWorker(String printWorker) {
		this.printWorker = printWorker;
	}

	public Date getPrintTagTime() {
		return printTagTime;
	}

	public void setPrintTagTime(Date printTagTime) {
		this.printTagTime = printTagTime;
	}

	public String getPrintTagWorker() {
		return printTagWorker;
	}

	public void setPrintTagWorker(String printTagWorker) {
		this.printTagWorker = printTagWorker;
	}

	public String getBillTypeName() {
		return billTypeName;
	}

	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Set<WmsBOLDetail> getDetails() {
		return details;
	}

	public void setDetails(Set<WmsBOLDetail> details) {
		this.details = details;
	}

	public Date getShipTime() {
		return shipTime;
	}

	public void setShipTime(Date shipTime) {
		this.shipTime = shipTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPallet() {
		return pallet;
	}

	public void setPallet(String pallet) {
		this.pallet = pallet;
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
	
	public WmsDriver getWmsDriver() {
		return wmsDriver;
	}

	public void setWmsDriver(WmsDriver wmsDriver) {
		this.wmsDriver = wmsDriver;
	}

	public WmsVehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(WmsVehicle vehicle) {
		this.vehicle = vehicle;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getRelateCode() {
		return relateCode;
	}

	public void setRelateCode(String relateCode) {
		this.relateCode = relateCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getPalletQuantity() {
		return palletQuantity;
	}

	public void setPalletQuantity(Double palletQuantity) {
		this.palletQuantity = palletQuantity;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getQuantityBU() {
		return quantityBU;
	}

	public void setQuantityBU(Double quantityBU) {
		this.quantityBU = quantityBU;
	}

	public Double getShippedQuantity() {
		return shippedQuantity;
	}

	public void setShippedQuantity(Double shippedQuantity) {
		this.shippedQuantity = shippedQuantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public void addDetail(WmsBOLDetail detail){
		this.details.add(detail);
		detail.setBol(this);
	}
	
	public void removeDetail(WmsBOLDetail detail){
		this.details.remove(detail);
		detail.setBol(null);
	}
	
	public void refreshQuantity(){
		this.quantity=0D;
		this.quantityBU=0D;
		this.shippedQuantity=0D;
		for (WmsBOLDetail detail : this.details) {
			this.quantity+=detail.getQuantity();
			this.quantityBU+=detail.getQuantityBU();
			this.shippedQuantity+=detail.getShippedQuantity();
		}
	}
	
	public void refreshPalletQuantity(){
		Set<String> pallets = new HashSet<String>();
		for (WmsBOLDetail detail : this.details) {
			if(!BaseStatus.NULLVALUE.equals(detail.getPallet())){
				pallets.add(detail.getPallet());
			}
		}
	}
}
