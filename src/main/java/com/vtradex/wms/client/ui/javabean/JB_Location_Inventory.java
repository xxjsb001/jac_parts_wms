package com.vtradex.wms.client.ui.javabean;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *@author <a href="kaisheng.liu@vtradex.com">刘开胜</a>
 *@version $Project:swms $Date: 2015/03/25 02:48:49 $Version:V1.1
 */
public class JB_Location_Inventory implements IsSerializable {

	private Long id;
	private String org_name;
	private String loc_code;
	private String item_code;
	private String item_name;
	private String lot;
	private String unit;
	private double quantity;
	private double weight;
	private double volume;
	private int age;
	private String lp;
	
	public JB_Location_Inventory(){	
	}
	/** 两个日期之间间隔的天数 */
	public Integer getBetweenDays(Date sDate,Date eDate) {
		long DAY = 24L * 60L * 60L * 1000L; 
		return new Integer((int)((eDate.getTime() - sDate.getTime())/DAY));
	}
	
	public JB_Location_Inventory(Object id ,Object org_name, Object loc_code,Object code,Object name
			,Object lot,Object unit,Object quantity,Object weight,Object volume , Object age , Object lp){
		this(id ,org_name, loc_code,code, name, lot, unit, quantity, weight, volume);
		this.age = age == null ? 0 : getBetweenDays((Date)age , new Date());
		this.lp = lp == null ? " " : (String)lp;
	}
	
	public JB_Location_Inventory(Object id ,Object org_name, Object loc_code,Object code,Object name
			,Object lot,Object unit,Object quantity,Object weight,Object volume){
		this.id = (Long)id;
		this.org_name = (String)org_name;
		this.loc_code = (String)loc_code;
		this.item_code = (String)code;
		this.item_name = (String)name;
		this.lot = (lot == null ? " ":lot.toString());
		this.unit = (String)unit;
		this.quantity = (Double)quantity;
		this.weight = (Double)weight;
		this.volume = (Double)volume;
	}
	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getLp() {
		return lp;
	}

	public void setLp(String lp) {
		this.lp = lp;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getItem_code() {
		return item_code;
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getLoc_code() {
		return loc_code;
	}
	public void setLoc_code(String loc_code) {
		this.loc_code = loc_code;
	}
	public String getLot() {
		return lot;
	}
	public void setLot(String lot) {
		this.lot = lot;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	
	public Object[] toArray(){
		return new Object[]{
				this.org_name,
				this.loc_code,
				this.item_code,
				this.item_name,
				this.lot,
				this.unit,
				this.quantity,
				this.weight,
				this.volume,
				this.age,
				this.lp
				};
	}
	
	public static String[] propertyToArray(){
		return new String[]{
				"org_name",
				"loc_code",
				"item_code",
				"item_name",
				"lot",
				"unit",
				"quantity",
				"weight",
				"volume",
				"age",
				"lp"
			};
	}
	
	public static String[] localizedToArray(){
		return new String[]{
				"货主",
				"库位",
				"货品编码",
				"货品名称",
				"批次属性",
				"包装",
				"库存数量",
				"重量",
				"体积",
				"库龄",
				"托盘号"
		};
	}
}
