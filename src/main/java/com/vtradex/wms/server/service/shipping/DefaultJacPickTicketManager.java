package com.vtradex.wms.server.service.shipping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.wms.server.model.move.WmsMoveDocType;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.shipping.WmsPickTicketDetail;
import com.vtradex.wms.server.model.shipping.WmsPickTicketStatus;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

public class DefaultJacPickTicketManager extends DefaultBaseManager implements JacPickTicketManager{
	protected WmsPickTicketManager wmsPickTicketManager;
	
	public DefaultJacPickTicketManager(WmsPickTicketManager wmsPickTicketManager) {
		this.wmsPickTicketManager = wmsPickTicketManager;
	}

	@SuppressWarnings("unchecked")
	public void lotPick() {
		//看板件,打开,当前库
		String hql = "FROM WmsPickTicketDetail pp WHERE pp.pickTicket.billType.code IN ('KB_PICKING')" +
				" AND pp.pickTicket.status =:status AND pp.pickTicket.warehouse.id =:warehouse";
		List<WmsPickTicketDetail> pps = commonDao.findByQuery(hql,new String[]{"status","warehouse"},
				new Object[]{WmsPickTicketStatus.OPEN,WmsWarehouseHolder.getWmsWarehouse().getId()});
		Map<String,List<Long>> xs = new HashMap<String,List<Long>>();
		List<Long> ids = null;
		String x = "",batch = JavaTools.getMark();
		int size = pps.size();
		for(WmsPickTicketDetail pp : pps){
			//X规则:货主+单据类型+收货道口+计划日期+产线+工位
			x = pp.getPickTicket().getCompany().getCode()+MyUtils.spilt1
					+pp.getPickTicket().getBillType().getCode()+MyUtils.spilt1
					+(pp.getPickTicket().getReceiveDoc()==null?"-":pp.getPickTicket().getReceiveDoc())+MyUtils.spilt1
					+JavaTools.format(pp.getPickTicket().getRequireArriveDate(), JavaTools.dmy_hms)+MyUtils.spilt1
					+(pp.getProductionLine()==null?"-":pp.getProductionLine())+MyUtils.spilt1
					+(pp.getStation()==null?"-":pp.getStation());
			if(xs.containsKey(x)){
				ids = xs.get(x);
			}else{
				ids = new ArrayList<Long>();
			}
			ids.add(pp.getId());
			xs.put(x, ids);
		}pps.clear();
		
		Boolean isError = false;
		Map<String,WmsBillType> bills = new HashMap<String, WmsBillType>();
		hql = "FROM WmsBillType WHERE code=:code AND status='ENABLED' AND company.code=:companyCode";
		WmsBillType bType = null;
		String[] xp = null;
		Iterator<Entry<String, List<Long>>> it = xs.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, List<Long>> entry = it.next();
			x = entry.getKey();
			ids = entry.getValue();
//			System.out.println(x+":"+ids.size());
			xp = x.split(MyUtils.spilt1);
			if(bills.containsKey(xp[0])){
				bType = bills.get(xp[0]);
			}else{
				bType = (WmsBillType) commonDao.findByQueryUniqueResult(hql, 
						new String[]{"code","companyCode"},new Object[]{WmsMoveDocType.LOT_PICKING,xp[0]});
				if(bType==null){
					isError = true;
					x = "批拣单号不存在:LOT_PICKING";
					break;
				}
				bills.put(x, bType);
			}
			wmsPickTicketManager.lotPick(x, ids,bType,batch);
		}
		if(isError){
			LocalizedMessage.setMessage(MyUtils.font(x));
		}else{
			LocalizedMessage.setMessage(MyUtils.fontByBlue("成功(明细条数:"+size+")"));
		}
	}
}
