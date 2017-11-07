package com.vtradex.wms.server.service.sequence;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;

/**
 * @author <a href="mailto:xiaofeng.pu@cd.vtradex.com">刘旭华</a>
 */
public interface WmsBussinessCodeManager extends BaseManager {
	/**
	 * 自动生成批次号
	 * @return
	 */
	@Transactional
	String generateLot();
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	@Transactional
	String generateMoveDocCode(String key);
		
	/**
	 * 自动生成单据编码
	 * @param warehouse
	 * @param companyName
	 * @param document
	 * @param type
	 * @return
	 */
	@Transactional
	String generateCodeByRule(WmsWarehouse warehouse, String companyName, String document,String type);
	
	/** 
	 *  自动创建库区编码
	 * */
	@Transactional
	Map<String, Object> generateLocationCodeByRule(String warehouseName, String zoneCode, Integer zone, Integer line, Integer column, Integer layer);
	
	String getWmsBillDetailCode(String fromType , String supplierCode);
}