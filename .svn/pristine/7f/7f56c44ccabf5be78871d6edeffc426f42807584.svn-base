/**
 * 
 */
package com.vtradex.wms.server.model.receiving;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.shipping.WmsMasterBOL;
import com.vtradex.wms.server.model.warehouse.WmsDock;

/**
 * @Description: 月台预约计划
 * @Author: <a href="qiufeng.chen@vtradex.net"/>陈秋凤</a>
 * @CreateDate：  2012-11-20
 * @version: v1.0
 */
public class WmsBooking extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4867500388011637577L;
	
	/**
	 * 月台
	 */
	private WmsDock dock;
	/**
	 * ASN
	 */
	private WmsASN asn;
	/**
	 * 装车单
	 */
	private WmsMasterBOL masterBOL;

	/**
	 * 日期
	 */
	private Date planDate;

	
	/**
	 * 编号（单号）
	 */
	private String code;
	
	/**
	 * 类型(收货/发货)
	 */
	private String planType;
	
	/**
	 * 分类(承运商/供应商)
	 */
	private String classify;
	
	/**
	 * 预约开始时间
	 */
	private Date asnPlannedStartTime;
	
	/**
	 * 预约最后时间
	 */
	private Date asnPlannedLastTime;
	
	/**
	 * 预约结束时间
	 */
	private Date asnPlannedEndTime;
	
	/**
	 * 实际到库时间
	 */
	private Date actualStartTime;
	
	/**
	 * 完成时间
	 */
	private Date finishTime;
	
	/**
	 * 状态
	 * {@link WmsASNPlanStatus}
	 */
	private String status = WmsBookingStatus.OPEN;
	
	/**
	 * 原拆分id
	 */
	private Long preId;
	
	/**
	 * 备注
	 */
	private String remark;

	public Date getPlanDate() {
		return planDate;
	}

	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public String getClassify() {
		return classify;
	}

	public void setClassify(String classify) {
		this.classify = classify;
	}

	public Date getAsnPlannedStartTime() {
		return asnPlannedStartTime;
	}

	public void setAsnPlannedStartTime(Date asnPlannedStartTime) {
		this.asnPlannedStartTime = asnPlannedStartTime;
	}

	public Date getAsnPlannedEndTime() {
		return asnPlannedEndTime;
	}

	public void setAsnPlannedEndTime(Date asnPlannedEndTime) {
		this.asnPlannedEndTime = asnPlannedEndTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setDock(WmsDock dock) {
		this.dock = dock;
	}

	public WmsDock getDock() {
		return dock;
	}

	public Date getActualStartTime() {
		return actualStartTime;
	}

	public void setActualStartTime(Date actualStartTime) {
		this.actualStartTime = actualStartTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public void setPreId(Long preId) {
		this.preId = preId;
	}

	public Long getPreId() {
		return preId;
	}

	public WmsASN getAsn() {
		return asn;
	}

	public void setAsn(WmsASN asn) {
		this.asn = asn;
	}

	public WmsMasterBOL getMasterBOL() {
		return masterBOL;
	}

	public void setMasterBOL(WmsMasterBOL masterBOL) {
		this.masterBOL = masterBOL;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setAsnPlannedLastTime(Date asnPlannedLastTime) {
		this.asnPlannedLastTime = asnPlannedLastTime;
	}

	public Date getAsnPlannedLastTime() {
		return asnPlannedLastTime;
	}

	
}
