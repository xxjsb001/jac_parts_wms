package com.vtradex.wms.server.model.shipping;

import java.util.Date;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.base.Contact;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.warehouse.WmsDock;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;


/** 装车单
 * @author peng.lei
 * @version 1.0
 * @created 15-二月-2011 14:39:07
 */

public class WmsMasterBOL extends VersionalEntity {
	/** */
	private static final long serialVersionUID = 3834728061941161253L;
	/** 仓库*/
	@UniqueKey
	private WmsWarehouse warehouse;
	/** MasterBOL号*/
	@UniqueKey
	private String code;
	/** 状态*/
	private String status;
	/** 整箱数*/
	private Double packageQuantity = 0D;
	/** 散件数*/
	private Double scatteredQuantity = 0D;
	/** 重量*/
	private Double weight = 0D;
	/** 体积*/
	private Double volume = 0D;
	/** 承运商*/
	private WmsOrganization carrier;
	/** 车牌号*/
	private String vehicleNo;
	/** 司机*/
	private String driver;
	/** 收货方*/
	private WmsOrganization customer;
	/** 收货人姓名*/
	private String shipToName;
	/** 收货人联系方式*/
	private Contact shipToContact;
	/** 预计发车时间*/
	private Date intendShipDate;
	/** 发车时间*/
	private Date shipTime;
	/** 描述*/
	private String description;
	
	
	public WmsMasterBOL(){
		
	}
	
	
	public Date getIntendShipDate() {
		return intendShipDate;
	}

	public void setIntendShipDate(Date intendShipDate) {
		this.intendShipDate = intendShipDate;
	}

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
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

	public Double getPackageQuantity() {
		return packageQuantity;
	}

	public void setPackageQuantity(Double packageQuantity) {
		this.packageQuantity = packageQuantity;
	}

	public Double getScatteredQuantity() {
		return scatteredQuantity;
	}

	public void setScatteredQuantity(Double scatteredQuantity) {
		this.scatteredQuantity = scatteredQuantity;
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

	public WmsOrganization getCarrier() {
		return carrier;
	}

	public void setCarrier(WmsOrganization carrier) {
		this.carrier = carrier;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public WmsOrganization getCustomer() {
		return customer;
	}

	public void setCustomer(WmsOrganization customer) {
		this.customer = customer;
	}

	public String getShipToName() {
		return shipToName;
	}

	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
	}

	public Contact getShipToContact() {
		return shipToContact;
	}

	public void setShipToContact(Contact shipToContact) {
		this.shipToContact = shipToContact;
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
}
