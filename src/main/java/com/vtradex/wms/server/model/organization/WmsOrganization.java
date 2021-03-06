package com.vtradex.wms.server.model.organization;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.Contact;

/**
 * @category 组织
 * @author shengpei.zhang
 * @version 1.0
 * @created 15-二月-2011 10:05:47
 */
public class WmsOrganization extends Entity{
	private static final long serialVersionUID = -5698133523389523114L;
	/** 代码*/
	@UniqueKey
	private String code;
	/** 名称*/
	private String name;
	/** 联系方式*/
	private Contact contact;
	/** 是否承运商*/
	private boolean beCarrier = false;
	/** 是否客户*/
	private boolean beCustomer = false;
	/** 是否供应商*/
	private boolean beSupplier = false;
	/** 是否货主*/
	private boolean beCompany = false;
	/** 是否虚拟 **/
	private boolean beVirtual = false;
	/** 描述*/
	private String description;
	/** 批次规则(说明：货品上的批次规则优先级高于货主上的批次规则) */
	private WmsLotRule lotRule;
	/** 状态*/
	private String status;
	/** 内部名称 ysd-2016/07/25
	 * 非必填，新建或修改或者接口同步时如果为空，初始化字段同名称列一致，如果不为空不作处理*/
	private String neiBuName;
	
	//简称
	private String shortName;
	
	public String getNeiBuName() {
		return neiBuName;
	}

	public void setNeiBuName(String neiBuName) {
		if(neiBuName==null || neiBuName.equals("")){
			this.neiBuName = this.name;
		}else{
			this.neiBuName = neiBuName;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		WmsOrganization other = (WmsOrganization) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
	
	public boolean isBeVirtual() {
		return beVirtual;
	}

	public void setBeVirtual(boolean beVirtual) {
		this.beVirtual = beVirtual;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isBeCarrier() {
		return beCarrier;
	}

	public void setBeCarrier(boolean beCarrier) {
		this.beCarrier = beCarrier;
	}

	public boolean isBeCompany() {
		return beCompany;
	}

	public void setBeCompany(boolean beCompany) {
		this.beCompany = beCompany;
	}

	public boolean isBeCustomer() {
		return beCustomer;
	}

	public void setBeCustomer(boolean beCustomer) {
		this.beCustomer = beCustomer;
	}

	public boolean isBeSupplier() {
		return beSupplier;
	}

	public void setBeSupplier(boolean beSupplier) {
		this.beSupplier = beSupplier;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public WmsLotRule getLotRule() {
		return lotRule;
	}

	public void setLotRule(WmsLotRule lotRule) {
		this.lotRule = lotRule;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public WmsOrganization(){

	}

	public WmsOrganization(String code, String name, 
			Contact contact,String status) {
		super();
		this.code = code;
		this.name = name;
		this.contact = contact;
		this.beCarrier = Boolean.FALSE;
		this.beCustomer = Boolean.FALSE;
		this.beSupplier = Boolean.TRUE;
		this.beCompany = Boolean.FALSE;
		this.beVirtual = Boolean.FALSE;
		this.status = BaseStatus.ENABLED;
		this.neiBuName = name;
	}

	public void finalize() throws Throwable {

	}

}