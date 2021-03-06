package com.vtradex.wms.server.model.warehouse;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.organization.WmsEnumType;

/**
 * 箱型
 *
 * @category Entity 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:51 $
 */
public class WmsBoxType extends Entity {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@UniqueKey
	private String code;
	
	/**
	 * 长
	 */
	private Double length = 0D;
	
	/**
	 * 宽
	 */
	private Double width = 0D;
 	
	/**
	 * 高
	 */
	private Double hight = 0D;
	
	/**
	 * 体积
	 */
	private Double volume = 0D;
	
	/**
	 * 重量
	 * */
	private Double weight = 0D;

	/**
	 * 描述
	 */
	private String remark;
	
	/**
	 * 状态-生效、失效
	 * 
	 * {@link BaseStatus}
	 */
	private String status;
	
	/**
	 * {@WmsContainerType}
	 * 类型
	 */
	private String type;
	
	/**
	 * 是否在拣选:RF扫描容器后设为true,发运之后设为false,后台定时跑:如果不存在未发运的设为false;
	 */
	private Boolean isPicking = Boolean.FALSE;
	/**
	 * 是否加入装车单
	 */
	private Boolean isBol = Boolean.FALSE;
	
	private WmsEnumType enumType;
	
	public WmsEnumType getEnumType() {
		return enumType;
	}

	public void setEnumType(WmsEnumType enumType) {
		this.enumType = enumType;
	}

	public WmsBoxType() {
	}

	public WmsBoxType(String code, Double length, Double width, Double hight,
			Double volume, Double weight, String status, String type,
			WmsEnumType enumType) {
		super();
		this.code = code;
		this.length = length;
		this.width = width;
		this.hight = hight;
		this.volume = volume;
		this.weight = weight;
		this.status = status;
		this.type = type;
		this.enumType = enumType;
	}

	public Boolean getIsBol() {
		return isBol;
	}

	public void setIsBol(Boolean isBol) {
		this.isBol = isBol;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHight() {
		return hight;
	}

	public void setHight(Double hight) {
		this.hight = hight;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}
	
	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Boolean getIsPicking() {
		return isPicking;
	}

	public void setIsPicking(Boolean isPicking) {
		this.isPicking = isPicking;
	}
	
}
