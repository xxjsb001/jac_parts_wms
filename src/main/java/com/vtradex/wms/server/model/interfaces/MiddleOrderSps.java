package com.vtradex.wms.server.model.interfaces;

import java.util.Date;

import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;

public class MiddleOrderSps extends VersionalEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7417778914963306472L;
	
	/**料单号*/
	private String odrNo;
 	/**单据类型*/
	private WmsBillType billType;
	/**生产日期*/
	private Date demandDate;
	/**供应商*/
	private WmsOrganization supply;
	/**物料*/
	private WmsItem item;
	/**数量*/
	private Double qty;
	/**货主*/
	private WmsOrganization company;
	/**计划源*/
	private String odrSu;
	/**目的仓库*/
	private String dware;
	/**生产线*/
	private String productLine;
	/**收货道口*/
	private String shdk;
	/**是否集配*/
	private Boolean isJp;
	/**批次*/
	private String batch;
	/**工位*/
	private String station;
	/**送料人*/
	private String slr;
	
	private WHead head; 
	
	private WmsPackageUnit packageUnit;
	
	/**顺序*/
	private Integer sx;
	/**器具型号*/
	private String packageNo;
	/**器具容量*/
	private Double packageNum;
	/**器具数量*/
	private Double 	packageQty;
	
	/**备注*/
	private String remark;
	
	
	//来源
	private String fromSource;
	
	
	
	public String getFromSource() {
		return fromSource;
	}
	public void setFromSource(String fromSource) {
		this.fromSource = fromSource;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOdrNo() {
		return odrNo;
	}
	public void setOdrNo(String odrNo) {
		this.odrNo = odrNo;
	}
	public WmsBillType getBillType() {
		return billType;
	}
	public void setBillType(WmsBillType billType) {
		this.billType = billType;
	}
	public Date getDemandDate() {
		return demandDate;
	}
	public void setDemandDate(Date demandDate) {
		this.demandDate = demandDate;
	}
	public WmsOrganization getSupply() {
		return supply;
	}
	public void setSupply(WmsOrganization supply) {
		this.supply = supply;
	}
	public WmsItem getItem() {
		return item;
	}
	public void setItem(WmsItem item) {
		this.item = item;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public WmsOrganization getCompany() {
		return company;
	}
	public void setCompany(WmsOrganization company) {
		this.company = company;
	}
	public String getOdrSu() {
		return odrSu;
	}
	public void setOdrSu(String odrSu) {
		this.odrSu = odrSu;
	}
	public String getDware() {
		return dware;
	}
	public void setDware(String dware) {
		this.dware = dware;
	}
	public String getProductLine() {
		return productLine;
	}
	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}
	public String getShdk() {
		return shdk;
	}
	public void setShdk(String shdk) {
		this.shdk = shdk;
	}
	public Boolean getIsJp() {
		return isJp;
	}
	public void setIsJp(Boolean isJp) {
		this.isJp = isJp;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public String getSlr() {
		return slr;
	}
	public void setSlr(String slr) {
		this.slr = slr;
	}
	public WHead getHead() {
		return head;
	}
	public void setHead(WHead head) {
		this.head = head;
	}
	public WmsPackageUnit getPackageUnit() {
		return packageUnit;
	}
	public void setPackageUnit(WmsPackageUnit packageUnit) {
		this.packageUnit = packageUnit;
	}
	public Integer getSx() {
		return sx;
	}
	public void setSx(Integer sx) {
		this.sx = sx;
	}
	public String getPackageNo() {
		return packageNo;
	}
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	public Double getPackageNum() {
		return packageNum;
	}
	public void setPackageNum(Double packageNum) {
		this.packageNum = packageNum;
	}
	public Double getPackageQty() {
		return packageQty;
	}
	public void setPackageQty(Double packageQty) {
		this.packageQty = packageQty;
	}
	
	public MiddleOrderSps(){}
	public MiddleOrderSps(String odrNo, WmsBillType billType, Date demandDate,
			WmsOrganization supply, WmsItem item, Double qty,
			WmsOrganization company, String odrSu, String dware,
			String productLine, String shdk, Boolean isJp, String batch,
			String station, String slr, WHead head, WmsPackageUnit packageUnit,
			Integer sx, String packageNo, Double packageNum, Double packageQty,
			String remark) {
		super();
		this.odrNo = odrNo;
		this.billType = billType;
		this.demandDate = demandDate;
		this.supply = supply;
		this.item = item;
		this.qty = qty;
		this.company = company;
		this.odrSu = odrSu;
		this.dware = dware;
		this.productLine = productLine;
		this.shdk = shdk;
		this.isJp = isJp;
		this.batch = batch;
		this.station = station;
		this.slr = slr;
		this.head = head;
		this.packageUnit = packageUnit;
		this.sx = sx;
		this.packageNo = packageNo;
		this.packageNum = packageNum;
		this.packageQty = packageQty;
		this.remark = remark;
	}
	
			
}
