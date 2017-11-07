package com.vtradex.wms.server.service.inventory.pojo;

import java.util.HashMap;
import java.util.Map;

import com.vtradex.thorn.client.ui.page.IPage;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.receiving.WmsReceivedRecord;
import com.vtradex.wms.server.service.inventory.WmsInventoryExtendManager;

public class DefaultWmsInventoryExtendManager extends DefaultBaseManager implements
		WmsInventoryExtendManager {

	public WmsInventoryExtend addInventoryExtend(WmsInventory inv, String pallet, String carton,String serialNo, Double quantityBU) {
		WmsInventoryExtend wsn = getInventoryExtendWithNew(inv, pallet, carton, serialNo);
		wsn.addQuantity(quantityBU);
		return wsn;
	}
	
	public WmsInventoryExtend addInventoryExtend(WmsInventory inv,
			WmsInventoryExtend wie, Double quantityBU) {
		WmsInventoryExtend wsn = getInventoryExtendWithNew(inv, wie.getPallet(), wie.getCarton(), wie.getSerialNo());
		wsn.addQuantity(quantityBU);
		return wsn;
	}

	public WmsInventoryExtend addInventoryExtend(WmsInventory inv,
			WmsReceivedRecord wrr, Double quantityBU) {
		WmsInventoryExtend wsn = getInventoryExtendWithNew(inv, wrr.getPallet(), wrr.getCarton(), wrr.getSerialNo());
		wsn.addQuantity(quantityBU);
		return wsn;
	}

	public WmsInventoryExtend addInventoryExtend(WmsInventory inv, String pallet, String carton, String serialNo) {
		String hashCode = BeanUtils.getFormat(inv.getId(), pallet, carton, serialNo);
		String hql = "FROM WmsInventoryExtend wsn WHERE wsn.hashCode = :hashCode";
		WmsInventoryExtend wsn = (WmsInventoryExtend) commonDao.findByQueryUniqueResult(hql, new String[]{"hashCode"}, new Object[]{hashCode});
		return wsn;
		
	}
	
	private WmsInventoryExtend getInventoryExtendWithNew(WmsInventory inv, String pallet, String carton, String serialNo) {
		pallet = pallet == null ? BaseStatus.NULLVALUE : pallet;
		carton = carton == null ? BaseStatus.NULLVALUE : carton;
		serialNo = serialNo == null ? BaseStatus.NULLVALUE : serialNo;
		WmsInventoryExtend wsn = addInventoryExtend(inv, pallet, carton, serialNo);
		if (wsn == null) {
			wsn = newInventoryExtend(inv, pallet, carton, serialNo);
		}
		return wsn;
		
	}

	public WmsInventoryExtend newInventoryExtend(WmsInventory inv, String pallet, String carton,String serialNo) {
		WmsInventoryExtend wsn = EntityFactory.getEntity(WmsInventoryExtend.class);
		wsn.setInventory(inv);
		wsn.setLocationId(inv.getLocation().getId());
		wsn.setLocationCode(inv.getLocation().getCode());
		wsn.setPallet(pallet);
		wsn.setCarton(carton);
		wsn.setSerialNo(serialNo);
		wsn.setItem(inv.getItemKey().getItem());
		wsn.setStatus(BaseStatus.ENABLED);
		wsn.setHashCode(wsn.genHashCode());
		commonDao.store(wsn);
		return wsn;
	}

	@SuppressWarnings("unchecked")
	public Map printPalletNo(WmsInventoryExtend inventoryExtend,Long printNumber) {
		Map result = new HashMap();
		Map<Long,String> reportValue = new HashMap<Long, String>();
		String param = ";pallet=" + inventoryExtend.getPallet();
		reportValue.put(new Long(0), "wmsPallet.raq&raqParams=" + param);
		if(!reportValue.isEmpty()){
			result.put(IPage.REPORT_VALUES, reportValue);
			result.put(IPage.REPORT_PRINT_NUM, printNumber.intValue());
		}
		return result;
	}
}
