package com.vtradex.wms.server.model.receiving;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;

/**
 * @author: 李炎
 */
public class WmsASNQuality extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 769248185177315440L;

	
	private WmsWarehouse warehouse;
	
	private String uniqueKey;
	
	private int qualityRate = 0;
	
	
	public WmsASNQuality(){
		
	}
	
	public WmsASNQuality(WmsWarehouse warehouse,String uniqueKey){
		this.warehouse = warehouse;
		this.uniqueKey = uniqueKey;
	}

	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public int getQualityRate() {
		return qualityRate;
	}

	public void setQualityRate(int qualityRate) {
		this.qualityRate = qualityRate;
	}
	
	public void addQualityRate(){
		this.qualityRate++;
	}
	
	public void cutQualityRate(){
		if(qualityRate > 0){
			this.qualityRate--;
		}
	}
}
