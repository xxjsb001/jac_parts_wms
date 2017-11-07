package com.vtradex.wms.server.model.organization;

import java.util.HashSet;
import java.util.Set;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.warehouse.WmsWorker;

/**
 * @category 货品
 * @author shengpei.zhang
 * @version 1.0
 * @created 15-二月-2011 10:05:46
 */

public class WmsItem extends Entity {
	private static final long serialVersionUID = -5283407436265806229L;
	/** 客户*/
	@UniqueKey
	private WmsOrganization company;
	/** 货品代码*/
	@UniqueKey
	private String code;
	/** 货品名称*/
	private String name;
	/** 基本包装单位*/
	private String baseUnit;
	/** 货品分类一 */
	private String class1;
	/** 货品分类二 拣选分类*/
	private String class2;
	/** 货品分类三 托盘分类*/
	private String class3;	
	/** 货品分类四  ABC分类*/
	private String class4;
	/** 货品分类五 */
	private String class5;
	/**货品条码**/
	private String barCode;
	/** 批次规则*/
	private WmsLotRule lotRule;
	/**基本单位精度**/
	private Integer precision = 0;
	/** 保质期限*/
	private Integer validPeriod = 0;
	/** 预警时间*/
	private Integer alertLeadingDays = 0;
	/** 描述*/
	private String description;
	/** 状态*/
	private String status;
	/** 包含包装*/
	private Set<WmsPackageUnit> packageUnits;
	/**备料工*/
	private WmsWorker blg;
	/**保管员*/
	private WmsWorker bgy;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		return result;
	}

	public WmsItem(){}
	
	public WmsItem(WmsOrganization company, String code, String name,
			String baseUnit, String status,WmsLotRule lotRule) {
		super();
		this.company = company;
		this.code = code;
		this.name = name;
		this.baseUnit = baseUnit;
		this.status = status;
		this.precision = 0;
		this.validPeriod = 0;
		this.alertLeadingDays = 0;
		this.lotRule = lotRule;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		WmsItem other = (WmsItem) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		return true;
	}
    
	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getClass4() {
		return class4;
	}

	public void setClass4(String class4) {
		this.class4 = class4;
	}

	public String getClass5() {
		return class5;
	}

	public void setClass5(String class5) {
		this.class5 = class5;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public String getBaseUnit() {
		return baseUnit;
	}

	public void setBaseUnit(String baseUnit) {
		this.baseUnit = baseUnit;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getAlertLeadingDays() {
		return alertLeadingDays;
	}

	public void setAlertLeadingDays(Integer alertLeadingDays) {
		this.alertLeadingDays = alertLeadingDays;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public WmsOrganization getCompany() {
		return company;
	}

	public void setCompany(WmsOrganization company) {
		this.company = company;
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

	public Set<WmsPackageUnit> getPackageUnits() {
		return packageUnits;
	}

	public void setPackageUnits(Set<WmsPackageUnit> packageUnits) {
		this.packageUnits = packageUnits;
	}

	public Integer getValidPeriod() {
		return validPeriod;
	}

	public void setValidPeriod(Integer validPeriod) {
		this.validPeriod = validPeriod;
	}
	/**
	 * 增加商品的包装单位
	 * @param packageUnit 商品包装单位
	 */
	public void addPackageUnit(WmsPackageUnit packageUnit) {
		packageUnit.setItem(this);
		if (this.getPackageUnits() == null) {
			this.setPackageUnits(new HashSet<WmsPackageUnit>());
		}
		this.getPackageUnits().add(packageUnit);
	}

	/**
	 * 获取包装行号指定的货品包装
	 * @param lineNo
	 * @return
	 */
	public WmsPackageUnit getPackageByLineNo(Integer lineNo) {
		for (WmsPackageUnit pu : this.getPackageUnits()) {
			if (pu.getLineNo().intValue() == lineNo.intValue()) {
				return pu;
			}
		}
		return null;
	}
	
	public WmsPackageUnit getPackageByUnitLevel(String level) {
		for (WmsPackageUnit pu : this.getPackageUnits()) {
			if (pu.getLevel().equals(level)) {
				return pu;
			}
		}
		return null;
	}
	
	public String getClass1() {
		return class1;
	}

	public void setClass1(String class1) {
		this.class1 = class1;
	}

	public String getClass2() {
		return class2;
	}

	public void setClass2(String class2) {
		this.class2 = class2;
	}

	public String getClass3() {
		return class3;
	}

	public void setClass3(String class3) {
		this.class3 = class3;
	}

	public WmsWorker getBlg() {
		return blg;
	}

	public void setBlg(WmsWorker blg) {
		this.blg = blg;
	}

	public WmsWorker getBgy() {
		return bgy;
	}

	public void setBgy(WmsWorker bgy) {
		this.bgy = bgy;
	}

	public WmsLotRule getDefaultLotRule() {
		WmsLotRule lotRule = null;

		if (this.getLotRule() != null && this.getLotRule().getStatus().equals(BaseStatus.ENABLED)) {
			lotRule = this.getLotRule();
		}
		
		if (lotRule == null && this.getCompany().getLotRule() != null 
				&& this.getCompany().getLotRule().getStatus().equals(BaseStatus.ENABLED)) {
			lotRule = this.getCompany().getLotRule();
		}
		
		// 检验有效批次规则是否存在, 不存在则抛出异常
		if (lotRule == null) {
			throw new BusinessException("doesn't.exsit.enable.lotRule");
		}
		
		return lotRule;
	}

}