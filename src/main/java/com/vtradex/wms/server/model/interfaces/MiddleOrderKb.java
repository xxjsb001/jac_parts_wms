package com.vtradex.wms.server.model.interfaces;

import java.util.Date;

import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;

/**
 * 存储看板件接口通过校验的数据
 * @author fs
 *
 */
public class MiddleOrderKb  extends VersionalEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8918079213461661829L;


	/**料单号*/
	private String odrNo;
	/**单据类型*/
	private WmsBillType billType;
	/**行号*/
	private Integer lineNo;
	/**生产日期*/
	private Date demandDate;
	/**供应商*/
	private WmsOrganization supply;
	/**物料编码*/
	private WmsItem item;
	/**最小包装量*/
	private Integer smallQty;
	/**包装单位*/
	private String pcs;
	/**需求数量*/
	private Double qty;
	/**计划源*/
	private String odrSu;
	/**寄存、林采*/
	private WmsOrganization company;
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
	
	private WHead head;
	
	private WmsPackageUnit packageUnit;
	
	private Integer sx;
	
	
	public Integer getSx() {
		return sx;
	}
	public void setSx(Integer sx) {
		this.sx = sx;
	}
	public WmsPackageUnit getPackageUnit() {
		return packageUnit;
	}
	public void setPackageUnit(WmsPackageUnit packageUnit) {
		this.packageUnit = packageUnit;
	}
	public WHead getHead() {
		return head;
	}
	public void setHead(WHead head) {
		this.head = head;
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
	public Integer getLineNo() {
		return lineNo;
	}
	public void setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
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
	public Integer getSmallQty() {
		return smallQty;
	}
	public void setSmallQty(Integer smallQty) {
		this.smallQty = smallQty;
	}
	public String getPcs() {
		return pcs;
	}
	public void setPcs(String pcs) {
		this.pcs = pcs;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public String getOdrSu() {
		return odrSu;
	}
	public void setOdrSu(String odrSu) {
		this.odrSu = odrSu;
	}
	public WmsOrganization getCompany() {
		return company;
	}
	public void setCompany(WmsOrganization company) {
		this.company = company;
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
	
	public MiddleOrderKb(){}
	
	public MiddleOrderKb(String odrNo, WmsBillType billType, Integer lineNo,
			Date demandDate, WmsOrganization supply, WmsItem item,
			Integer smallQty, String pcs, Double qty, String odrSu,
			WmsOrganization company, String dware, String productLine,
			String shdk, Boolean isJp, String batch, String station, WHead head,
			WmsPackageUnit packageUnit,Integer sx) {
		super();
		this.odrNo = odrNo;
		this.billType = billType;
		this.lineNo = lineNo;
		this.demandDate = demandDate;
		this.supply = supply;
		this.item = item;
		this.smallQty = smallQty;
		this.pcs = pcs;
		this.qty = qty;
		this.odrSu = odrSu;
		this.company = company;
		this.dware = dware;
		this.productLine = productLine;
		this.shdk = shdk;
		this.isJp = isJp;
		this.batch = batch;
		this.station = station;
		this.head = head;
		this.packageUnit = packageUnit;
		this.sx = sx;
	}

	
	
}
