package com.vtradex.wms.server.model.inventory;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.wms.server.model.base.LotInfo;
import com.vtradex.wms.server.model.organization.WmsItem;

/**
 * @category 货品批次属性
 * @author peng.lei
 * @version 1.0
 * @created 15-二月-2011 10:06:58
 */
public class WmsItemKey extends Entity{
	private static final long serialVersionUID = 80871922075221195L;
	/** 货品*/
	private WmsItem item;
	/**
	 * 说明：
	 * 1、批次号产生规则由规则引擎管理，不同客户批次号产生方式不同；
	 * 2、批次号直接用于管理先进先出等业务；
	 */
	private String lot;
	/** 批次属性*/
	private LotInfo lotInfo;
	/** hashCode */
	@UniqueKey
	private String hashCode;
	
	
	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public WmsItem getItem() {
		return item;
	}

	public void setItem(WmsItem item) {
		this.item = item;
	}

	public String getLot() {
		return lot;
	}

	public void setLot(String lot) {
		this.lot = lot;
	}

	public LotInfo getLotInfo() {
		return lotInfo;
	}

	public void setLotInfo(LotInfo lotInfo) {
		this.lotInfo = lotInfo;
	}
	
	/**
	 * 生成HashCode
	 */
	public String genHashCode() {
		return BeanUtils.getFormat(this.item.getId(), this.lotInfo.stringValue());
	}

	public WmsItemKey(){
	}

	public void finalize() throws Throwable {
	}
}