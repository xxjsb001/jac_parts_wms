package com.vtradex.wms.client.ui.javabean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *@author <a href="kaisheng.liu@vtradex.com">刘开胜</a>
 *@version $Project:swms $Date: 2015/03/25 02:48:49 $Version:V1.1
 */
public class JB_Location_IV implements IsSerializable {

	private Long loc_id;
	private int loc_aisle;
	private int loc_line;
	private Double loc_rate;
	
	public JB_Location_IV(){}
	
	public JB_Location_IV(Object id,Object line , Object aisle,Object rate) {
		this.loc_id = (Long)id;
		this.loc_rate = ((Double)rate > 1D ? 1D:(Double)rate);
		this.loc_aisle = (Integer)aisle;
		this.loc_line = (Integer)line;
	}
	
	public JB_Location_IV(Object line , Object aisle,Object rate) {
		this(null,line,aisle,rate);
	}

	public int getLoc_aisle() {
		return loc_aisle;
	}

	public void setLoc_aisle(int loc_aisle) {
		this.loc_aisle = loc_aisle;
	}

	public Long getLoc_id() {
		return loc_id;
	}

	public void setLoc_id(Long loc_id) {
		this.loc_id = loc_id;
	}

	public int getLoc_line() {
		return loc_line;
	}

	public void setLoc_line(int loc_line) {
		this.loc_line = loc_line;
	}

	public Double getLoc_rate() {
		return loc_rate;
	}

	public void setLoc_rate(Double loc_rate) {
		this.loc_rate = loc_rate;
	}
    
	public boolean equals(Object obj) {
		JB_Location_IV castOther = (JB_Location_IV)obj;
		return this.loc_aisle == castOther.getLoc_aisle() && this.loc_line == castOther.getLoc_line();
	}
	
	public int hashCode(){
		return -1;
	}
}
