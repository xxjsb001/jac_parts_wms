package com.vtradex.wms.server.model.organization;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;

/**
 * @category 包装单位
 * @author shengpei.zhang
 * @version 1.0
 * @created 15-二月-2011 10:05:47
 */
public class WmsPackageUnit extends Entity{
	
	private static final long serialVersionUID = -7429282040702286259L;
	
	/** 货品*/
	@UniqueKey
	private WmsItem item;
	/** 行号(说明手工填写，方便排序)*/
	@UniqueKey
	private Integer lineNo;
	/** 单位*/
	private String unit;
	/** 包装级别（如：件、箱、大箱等） */
	private String level;
	/** 长*/
	private Double length = 0D;
	/** 宽*/
	private Double width = 0D;
	/** 高*/
	private Double height = 0D;
	/** 重量*/
	private Double weight = 0D;
	/** 体积*/
	private Double volume = 0D;
	/** 件装量*/
	private Integer convertFigure;
	/** 描述*/
	private String description;
	
	public WmsPackageUnit(){
		
	}

	public WmsPackageUnit(WmsItem item, Integer lineNo, String unit,
			String level,Integer convertFigure) {
		super();
		this.item = item;
		this.lineNo = lineNo;
		this.unit = unit;
		this.level = level;
		this.convertFigure = convertFigure;
	}

	public WmsItem getItem() {
		return item;
	}

	public void setItem(WmsItem item) {
		this.item = item;
	}

	public Integer getConvertFigure() {
		return convertFigure;
	}

	public void setConvertFigure(Integer convertFigure) {
		this.convertFigure = convertFigure;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Integer getLineNo() {
		return lineNo;
	}

	public void setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	
	public Integer getPrecision(){
		return item.getPrecision();
	}

	public void finalize() throws Throwable {

	}

}