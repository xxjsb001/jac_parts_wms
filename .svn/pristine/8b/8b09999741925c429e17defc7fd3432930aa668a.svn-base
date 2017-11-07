package com.vtradex.wms.server.service.inventory;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.receiving.WmsReceivedRecord;

public interface WmsInventoryExtendManager extends BaseManager {
	/**
	 * 增加序列号信息
	 * @param inv
	 * @param pallet
	 * @param carton
	 * @param serialNo
	 * @param receivedQuantityBU
	 */
	@Transactional
	WmsInventoryExtend addInventoryExtend(WmsInventory inv, String pallet, String carton,
			String serialNo,Double quantityBU);

	
	@Transactional
	WmsInventoryExtend addInventoryExtend(WmsInventory inv, WmsInventoryExtend wie,Double quantityBU);
	
	@Transactional
	WmsInventoryExtend addInventoryExtend(WmsInventory inv, WmsReceivedRecord wrr,Double quantityBU);
	
	/** 打印托盘号 */ 
	Map printPalletNo(WmsInventoryExtend inventoryExtend,Long printNumber);
	@Transactional
	WmsInventoryExtend addInventoryExtend(WmsInventory inv, String pallet, String carton, String serialNo);
	/**jac*/
	@Transactional
	WmsInventoryExtend newInventoryExtend(WmsInventory inv, String pallet, String carton,String serialNo);
}
