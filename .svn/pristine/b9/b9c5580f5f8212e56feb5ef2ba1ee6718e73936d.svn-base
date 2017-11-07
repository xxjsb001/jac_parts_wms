package com.vtradex.wms.client.ui.javabean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *@author <a href="kaisheng.liu@vtradex.com">刘开胜</a>
 *@version $Project:swms $Date: 2015/03/25 02:48:49 $Version:V1.1
 */
public class JB_Zone_IV implements IsSerializable {
	
	private Long loc_id;
	private String loc_name;
	private String loc_code;
	private Double loc_rate;
	private Integer loc_xPos;
	private Integer loc_yPos;
	
	public JB_Zone_IV(){}
	
	public JB_Zone_IV(Object id , Object code, Object name , Object rate ,Object x , Object y){
		this.loc_id = (Long)id;
		this.loc_code = (String)code;
		this.loc_name = (String)name;
		this.loc_rate = rate == null ? 0D: (Double)rate;
		this.loc_xPos = (Integer)x;
		this.loc_yPos = (Integer)y;
	}
	
	public String getLoc_code() {
		return loc_code;
	}

	public void setLoc_code(String loc_code) {
		this.loc_code = loc_code;
	}

	public Long getLoc_id() {
		return loc_id;
	}

	public void setLoc_id(Long loc_id) {
		this.loc_id = loc_id;
	}

	public String getLoc_name() {
		return loc_name;
	}

	public void setLoc_name(String loc_name) {
		this.loc_name = loc_name;
	}

	public Double getLoc_rate() {
		return loc_rate;
	}

	public void setLoc_rate(Double loc_rate) {
		this.loc_rate = loc_rate;
	}

	public Integer getLoc_xPos() {
		return loc_xPos;
	}

	public void setLoc_xPos(Integer loc_xPos) {
		this.loc_xPos = loc_xPos;
	}

	public Integer getLoc_yPos() {
		return loc_yPos;
	}

	public void setLoc_yPos(Integer loc_yPos) {
		this.loc_yPos = loc_yPos;
	}
	
	public int[] toXY() {
		return new int[]{this.loc_xPos , this.loc_yPos};
	}

	public String[] toStrArray() {
		return new String[]{this.loc_code , this.loc_name , this.loc_xPos + "" , this.loc_yPos + ""};
	}
}
