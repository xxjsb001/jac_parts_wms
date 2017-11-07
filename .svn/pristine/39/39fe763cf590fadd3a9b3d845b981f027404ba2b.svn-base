package com.vtradex.wms.client.ui.javabean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *@author <a href="kaisheng.liu@vtradex.com">刘开胜</a>
 *@version $Project:swms $Date: 2015/03/25 02:48:49 $Version:V1.1
 */
public class JB_Location_RC_IV implements IsSerializable {

	private Long id;
	private int row;
	private int col;
	private int aisle;
	private int line;
	private double rate;
	private String code;
	private String name;
	private double length;// 长
	private double width;// 宽
	private double height;// 高
	private double totalWeight;//总重量
	private double totalVolume;//总体积
	private double totalQuantity;//总件数
	private int totalLP;//总托数
	private double fullRate;//库满度
	
	public JB_Location_RC_IV(){}
	
	public JB_Location_RC_IV(Object row,Object col,Object rate) {
		this.rate = (Double)rate;
		this.row = (Integer)row;
		this.col = (Integer)col;
	}
	
	public double getFullRate() {
		return fullRate;
	}

	public void setFullRate(double fullRate) {
		this.fullRate = fullRate;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotalLP() {
		return totalLP;
	}

	public void setTotalLP(int totalLP) {
		this.totalLP = totalLP;
	}

	public double getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(double totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public double getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(double totalVolume) {
		this.totalVolume = totalVolume;
	}

	public double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}

	public int getAisle() {
		return aisle;
	}

	public void setAisle(int aisle) {
		this.aisle = aisle;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}
	
	public String[] toTipArray(){
		return new String[]{
				this.code,
				this.length + "",
				this.width + "",
				this.height + "",
				this.totalQuantity + "",
				this.totalWeight + "",
				this.totalVolume + "",
				this.totalLP + "",
				this.fullRate + ""
		};
	}

}
