package com.vtradex.wms.server.model.move;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;

/**
 * 质检基础信息
 * 来源：QIS接口 
 * 字段类型全部为string,是因为姚瑶主管的建表语句都是varchar类型
 * @author fs
 *
 */
public class QisPlan extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2331565275410413375L;

	/**计划年份*/
	private String planYear;
	/**计划月份*/
	private String planMonth;
	/**工厂*/
	private String factory;
	/**物料编码*/
	private String partNo;
	/**供应商编码*/
	private String supplyNo;
	/**物料名称*/
	private String partName;
	/**供应商名称*/
	private String supplyName;
	/**备注*/
	private String note;
	/**数量*/
	private String quantity;
	/**创建时间*/
	private Date createTime;
	/**库房*/
	private String companyCode;
	
	public QisPlan(String planYear, String planMonth, String factory,
			String partNo, String supplyNo, String partName, String supplyName,
			String note, String quantity, Date createTime, String companyCode) {
		super();
		this.planYear = planYear;
		this.planMonth = planMonth;
		this.factory = factory;
		this.partNo = partNo;
		this.supplyNo = supplyNo;
		this.partName = partName;
		this.supplyName = supplyName;
		this.note = note;
		this.quantity = quantity;
		this.createTime = createTime;
		this.companyCode = companyCode;
	}
	public QisPlan() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getPlanYear() {
		return planYear;
	}
	public void setPlanYear(String planYear) {
		this.planYear = planYear;
	}
	public String getPlanMonth() {
		return planMonth;
	}
	public void setPlanMonth(String planMonth) {
		this.planMonth = planMonth;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getPartNo() {
		return partNo;
	}
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	public String getSupplyNo() {
		return supplyNo;
	}
	public void setSupplyNo(String supplyNo) {
		this.supplyNo = supplyNo;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getSupplyName() {
		return supplyName;
	}
	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
