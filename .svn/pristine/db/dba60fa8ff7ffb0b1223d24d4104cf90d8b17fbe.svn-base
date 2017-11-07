/**
 * 
 */
package com.vtradex.wms.client.ui.javabean;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.vtradex.wms.client.inventoryviewUI.constant.CT_IV;

/**
 * @author <a href="mailto:jin.liu@vtradex.net">刘晋</a>
 * @since 2012-11-16 下午03:23:00
 */
public class JB_Region_IV implements IsSerializable {

	private String region_code;
	private Double region_rate;
	
	public JB_Region_IV(){}
	
	public JB_Region_IV(Object code, Object rate){
		this.region_code = (String)code.toString();
		this.region_rate = rate == null ? 0D: (Double)rate;
	}

	public String getRegion_code() {
		return region_code;
	}

	public void setRegion_code(String region_code) {
		this.region_code = region_code;
	}

	public Double getRegion_rate() {
		return region_rate;
	}

	public void setRegion_rate(Double region_rate) {
		this.region_rate = region_rate;
	}	
	
	public String[] toStrArray() {
		return new String[]{this.region_code , NumberFormat.getFormat(CT_IV.DEFAULT_PERCENT_FORMAT).format(this.region_rate) };
	}
}
