package com.vtradex.wms.server.model.carrier;

import java.util.Date;

import com.vtradex.thorn.server.model.Entity;

public class WmsDriver extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6072869325900929925L;
	
	/**
	 * 编码
	 */
	private String code;
	/**
	 * 司机姓名
	 */
	private String name;
	/**
	 * 司机身份证号
	 */
	private String idCard;
	/**
	 * 司机家庭地址
	 */
	private String address;
	/**
	 * 驾驶证号
	 */
	private String card;
	/**
	 * 驾照类型
	 * A、B、C等
	 */
	private String cardType;
	/**
	 * 司机住宅电话
	 */
	private String homeTel;
	
	/**
	 * 驾照领取时间
	 */
	private Date licenseDate;
	/**
	 * 司机移动电话
	 */
	private String mobileTel;
	
	/**
	 * 证明人姓名
	 */
	private String proveName;
	/**
	 * 证明人电话
	 */
	private String proveTel;
	/**
	 * 司机性别
	 */
	private String sex;
	/**
	 * 状态
	 * {@link WmsDriverStatus}
	 */
	private String status = WmsDriverStatus.OPEN;
	/**
	 * 描述
	 */
	private String description;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHomeTel() {
		return homeTel;
	}
	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public Date getLicenseDate() {
		return licenseDate;
	}
	public void setLicenseDate(Date licenseDate) {
		this.licenseDate = licenseDate;
	}
	public String getMobileTel() {
		return mobileTel;
	}
	public void setMobileTel(String mobileTel) {
		this.mobileTel = mobileTel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProveName() {
		return proveName;
	}
	public void setProveName(String proveName) {
		this.proveName = proveName;
	}
	public String getProveTel() {
		return proveTel;
	}
	public void setProveTel(String proveTel) {
		this.proveTel = proveTel;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
