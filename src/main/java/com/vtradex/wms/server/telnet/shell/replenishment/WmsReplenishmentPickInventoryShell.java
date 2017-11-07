package com.vtradex.wms.server.telnet.shell.replenishment;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.PageableBaseShell;
import com.vtradex.wms.server.telnet.dto.WmsMoveTaskDTO;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;
/**
 * 补货拣选库位库存
 * @author Administrator
 *
 */
public class WmsReplenishmentPickInventoryShell extends PageableBaseShell{
	
	public static final String PAGE_ID = "wmsReplenishmentPickInventoryShell";
	
	@Override
	public String[] getTableHeader() {
		return new String[]{"序号", "货品编码","可用数量"};
	}

	@Override
	public String getHql() {
		String hql = "SELECT e.id,e.inventory.itemKey.item.code,e.inventory.quantityBU-e.inventory.allocatedQuantityBU " +
				" FROM WmsInventoryExtend e WHERE 1=1 AND e.inventory.quantityBU-e.inventory.allocatedQuantityBU > 0 AND e.quantityBU >0 " +
				" /~仓库: AND e.inventory.location.warehouse = {仓库}~/" +
				" /~货主: AND e.inventory.itemKey.item.company.code = {货主}~/" +
				" /~库位: AND e.locationCode = {库位}~/ ";
		return hql;
	}

	@Override
	public String getNextShell() {
		Object[] rowData = (Object[])get(ROW_DATA_KEY);
		if(rowData != null){
			this.put("invExtId", rowData[0]);
		}
		return WmsReplenishmentPickDetailShell.PAGE_ID;
	}

	@Override
	public Map<String, Object> getParams() {
		WmsMoveTaskDTO dto = (WmsMoveTaskDTO) this.getParentContext().get("CURRENT_DTO");
		this.put("CURRENT_DTO", dto);
		String locCode = this.getParentValue("pickLocCode").toString();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("库位", locCode);
		params.put("货主", dto.getCompanyCode());
		params.put("仓库", WmsWarehouseHolder.getWmsWarehouse());
		return params;
	}
	
	protected void chooseChange(String choose) throws BreakException, ContinueException{
		if("00".equals(choose)){
			WmsMoveTaskDTO dto = (WmsMoveTaskDTO) this.getParentContext().get("CURRENT_DTO");
			this.put("MOVEDOC_ID", dto.getMoveDocId());
			this.put("CURRENT_DTO", dto);
			forward(WmsReplenishmentPickLocShell.PAGE_ID);
		}else{
			super.chooseChange(choose);
		}
	}
}
