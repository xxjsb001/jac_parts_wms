package com.vtradex.wms.server.service.inventory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTransactionManager;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.inventory.WmsInventoryLogType;
import com.vtradex.wms.server.model.inventory.WmsQualityMoveSoiLog;
import com.vtradex.wms.server.model.inventory.WmsTheKont;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.service.GlobalParamUtils;
import com.vtradex.wms.server.utils.BeanUtils;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

public class TheKnotManagerImp extends DefaultBaseManager implements TheKnotManager{

	public void saveStorageDataSys(){
		Date beginDate = new Date();
		String rijiewarehouse = GlobalParamUtils.getGloableStringValue("rijie_sys");
		String[] warehouses = rijiewarehouse.split(MyUtils.spilt1);
		for(String warehouse : warehouses){
			Long warehouseId = Long.parseLong(warehouse);
			saveStorageDataDo(warehouseId, beginDate, "定时任务",true);
		}
	}
	public void sysStorage(){
		String beginDay = "2016-04-21";//从这天之前开始补数据
		String endDay = beginDay;//JavaTools.format(new Date(), JavaTools.y_m_d);
		
		Boolean ending = true;
		while(ending){
			Date beginDate = JavaTools.stringToDate(beginDay);
			String rijiewarehouse = GlobalParamUtils.getGloableStringValue("rijie_sys");
			String[] warehouses = rijiewarehouse.split(MyUtils.spilt1);
			for(String warehouse : warehouses){
				Long warehouseId = Long.parseLong(warehouse);
				saveStorageDataDo(warehouseId, beginDate, "补数据",true);
			}
			if(beginDay.equals(endDay)){
				ending = false;
			}else{
				beginDate = JavaTools.addNumday(beginDate, 1);
				beginDay = JavaTools.format(beginDate, JavaTools.y_m_d);
			}
			System.out.println("补数据=="+beginDay);
		}
	}
	public void saveStorageData(Date beginDate,String description){
		Long warehouseId = WmsWarehouseHolder.getWmsWarehouse().getId();
		saveStorageDataDo(warehouseId, beginDate, description,false);
	}
	private void saveStorageDataDo(Long warehouseId,Date beginDate,
			String description,Boolean beSys){
		
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class, warehouseId);
		Date beforeOneDate = JavaTools.beforeOneDate(beginDate);
		String beforeDate = JavaTools.format(beforeOneDate, JavaTools.y_m_d);
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().openSession();
		try {
			String nowHms = JavaTools.format(new Date(), JavaTools.hms);
			String today = JavaTools.format(beginDate, JavaTools.y_m_d);
			String todayTime = today+JavaTools.tab+nowHms;
			System.out.println("定时日期=="+todayTime+",日结日期=="+beforeDate);
			
			List<Object[]> objsRec = getObj(findRec(beSys, beforeDate),session);
			List<Object[]> objsInv = getObj(findInv(beSys, beforeDate),session);
			List<Object[]> objsShipInv = getObj(findShipInv(beSys,beforeDate,
					today,todayTime),session);
			List<Object[]> objsPick = getObj(findPick(beSys, beforeDate),session);
			List<Object[]> objsShip = getObj(findShip(beSys, beforeDate),session);
			List<Object[]> objsInvAdjustAdd = getObj(findInvAdjustAdd(beSys,beforeDate,
					today,todayTime),session);
			List<Object[]> objsAdjustDel = getObj(findAdjustDel(beSys,beforeDate,
					today,todayTime),session);
			
			List<Object[]> objsInvQualityInAdd = getObj(findInvQualityInAdd(beSys,beforeDate,
					today,todayTime),session);
			List<Object[]> objsInvQualityInDel = getObj(findInvQualityInDel(beSys,beforeDate,
					today,todayTime),session);
			
			List<Object[]> objsInvQualityMoveSoiAdd = getObj(findQualityMoveSoiAdd(beSys,beforeDate,
					today,todayTime),session);
			List<Object[]> objsInvQualityMoveSoiDel = getObj(findQualityMoveSoiDel(beSys,beforeDate,
					today,todayTime),session);
			
			List<Object[]> objsQualityMoveSois = getObj(findQualityMoveSoi(beSys,beforeDate,
					today,todayTime),session);
			List<Object[]> objsQualityMoveSoiAdd = this.getQualityMoveSois(objsQualityMoveSois, 3);
			List<Object[]> objsQualityMoveSoiDel = this.getQualityMoveSois(objsQualityMoveSois, 2);
			//收货
			initTheKnot(warehouse,objsRec,WmsTheKont.type_1,beforeOneDate,beforeDate,session,description,WmsTheKont.type_1);
			//日结库存
			initTheKnot(warehouse,objsInv,WmsTheKont.type_2,beforeOneDate,beforeDate,session,description,WmsTheKont.type_2);
			//次日发运
			initTheKnot(warehouse,objsShipInv,WmsTheKont.type_2,beforeOneDate,beforeDate,session,description,WmsTheKont.type_3);
			//在途库存
			initTheKnot(warehouse,objsPick,WmsTheKont.type_4,beforeOneDate,beforeDate,session,description,WmsTheKont.type_4);
			//发货
			initTheKnot(warehouse,objsShip,WmsTheKont.type_5,beforeOneDate,beforeDate,session,description,WmsTheKont.type_5);
			//库存调整+
			initTheKnot(warehouse,objsInvAdjustAdd,WmsTheKont.type_7,beforeOneDate,beforeDate,session,description,WmsTheKont.type_7);
			//库存调整-
			initTheKnot(warehouse,objsAdjustDel,WmsTheKont.type_8,beforeOneDate,beforeDate,session,description,WmsTheKont.type_8);
			//库内质检+
			initTheKnot(warehouse,objsInvQualityInAdd,WmsTheKont.type_9,beforeOneDate,beforeDate,session,description,WmsInventoryLogType.QUALITY_IN_ADD);
			//库内质检-
			initTheKnot(warehouse,objsInvQualityInDel,WmsTheKont.type_10,beforeOneDate,beforeDate,session,description,WmsInventoryLogType.QUALITY_IN_DEL);
			//送检质检+
			initTheKnot(warehouse,objsInvQualityMoveSoiAdd,WmsTheKont.type_9,beforeOneDate,beforeDate,session,description,WmsInventoryLogType.QUALITY_MOVE_DOC_SOI);
			//送检质检-
			initTheKnot(warehouse,objsInvQualityMoveSoiDel,WmsTheKont.type_10,beforeOneDate,beforeDate,session,description,WmsInventoryLogType.QUALITY_MOVE_DOC_SOI);
			//送检质检回写+
			initTheKnot(warehouse,objsQualityMoveSoiAdd,WmsTheKont.type_9,beforeOneDate,beforeDate,session,description,WmsQualityMoveSoiLog.QUALITY_MOVE_DOC_SOI);
			//送检质检回写-
			initTheKnot(warehouse,objsQualityMoveSoiDel,WmsTheKont.type_10,beforeOneDate,beforeDate,session,description,WmsQualityMoveSoiLog.QUALITY_MOVE_DOC_SOI);
			
			objsRec.clear();objsInv.clear();objsShipInv.clear();objsPick.clear();objsShip.clear();
			objsInvAdjustAdd.clear();objsAdjustDel.clear();objsInvQualityInAdd.clear();objsInvQualityInDel.clear();
			objsInvQualityMoveSoiAdd.clear();objsInvQualityMoveSoiDel.clear();objsQualityMoveSoiAdd.clear();objsQualityMoveSoiDel.clear();
			
			Date beforeTwoDate = JavaTools.beforeOneDate(beforeOneDate);
			String formatTwoDate = JavaTools.format(beforeTwoDate, JavaTools.y_m_d);
			System.out.println("期初日期=="+formatTwoDate);
			//增加期初日结库存
			initBeforeInv(warehouse,beSys, formatTwoDate, session, beforeDate, warehouseId, WmsTheKont.type_2,beforeOneDate);
			//增加期初在途(签收区)
			initBeforeInv(warehouse,beSys, formatTwoDate, session, beforeDate, warehouseId, WmsTheKont.type_4,beforeOneDate);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getLocalizedMessage());
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
		
	}
	private List<Object[]> getQualityMoveSois(List<Object[]> objsQualityMoveSois,int i){
		List<Object[]> objsQualityMoveSoi = new ArrayList<Object[]>();
		for(Object[] o : objsQualityMoveSois){
			objsQualityMoveSoi.add(new Object[]{
					o[0],o[1],o[i],o[4],o[5],o[6],o[7],o[8]
			});
		}
		return objsQualityMoveSoi;
	}
	private void deleteTheKnot(Long warehouseId,String type,String formatDate,Session session
			,String note,String column){
		SQLQuery query = session.createSQLQuery("delete from wms_the_kont t where t.type = '"+type+"'"
				+ " AND t."+column+" = '"+note+"'"//+ " AND t.note = '"+note+"'"
				+ " AND to_char(t.ORDER_DATE,'yyyy-MM-dd') = '"+formatDate+"'"
				+ " AND t.warehouse_id = "+warehouseId);
		query.executeUpdate();
	}
	private void initTheKnot(WmsWarehouse warehouse,List<Object[]> objs,String type,Date beforeOneDate
			,String formatDate,Session session,String description,String note){
		deleteTheKnot(warehouse.getId(),type, formatDate, session,note,"note");
		for(Object[] o : objs){
			WmsTheKont w  = EntityFactory.getEntity(WmsTheKont.class);
			w.setExtendPropc1(o[1].toString());
			w.setInventoryStatus(o[2].toString());
			w.setItemCode(o[3].toString());
			w.setItemName(o[4].toString());
			w.setQuantity(Double.valueOf(o[0].toString()));
			w.setStorageDate((Date)o[7]);
			w.setSupCode(o[5].toString());
			w.setSupName(o[6].toString());
			w.setType(type);
			w.setWarehouse(warehouse);
			w.setOrderDate(beforeOneDate);
			w.setDescription(description);
			w.setNote(note);
			w.initHashcode();
			commonDao.store(w);
		}
	}
	@SuppressWarnings("unchecked")
	private void initBeforeInv(WmsWarehouse warehouse,Boolean beSys,String formatTwoDate,Session session
			,String formatDate,Long warehouseId,String type,Date beforeOneDate){
		deleteTheKnot(warehouse.getId(),type, formatDate, session,WmsTheKont.back,"note_1");
		List<Object[]> objsBeforeInv = getObj(findBeforeInv(beSys, formatTwoDate, type),session);
		if(objsBeforeInv!=null && objsBeforeInv.size()>0){
			Map<String,Double> beforeInvs = new HashMap<String,Double>();
			for(Object[] o : objsBeforeInv){
				beforeInvs.put(o[0].toString(), Double.valueOf(o[1].toString()));
			}objsBeforeInv.clear();
			List<WmsTheKont> ws = commonDao.findByQuery("FROM WmsTheKont w"
					+ " WHERE to_char(w.orderDate,'yyyy-MM-dd') =:orderDate"
					+ " AND w.warehouse.id =:warehouseId"
//					+ " AND w.supCode = 'L22046'"//test
//					+ " AND w.itemCode = '1025100GE'"//test
					+ " AND w.type =:type"
					+ " AND (w.note > '"+WmsTheKont.type_3+"' or w.note < '"+WmsTheKont.type_3+"')", 
					new String[]{"orderDate","warehouseId","type"}, 
					new Object[]{formatDate,warehouseId,type});
			for(WmsTheKont w : ws){
				if(WmsTheKont.type_2.equals(type)){
					w.setBeforeQuantity(0D);//期初库存
				}else if(WmsTheKont.type_4.equals(type)){
					w.setBeforeOnWayQuantity(0D);
				}
				if(beforeInvs.containsKey(w.getHashCode())){
					if(WmsTheKont.type_2.equals(type)){
						w.setBeforeQuantity(beforeInvs.get(w.getHashCode()));//期初库存
					}else if(WmsTheKont.type_4.equals(type)){
						w.setBeforeOnWayQuantity(beforeInvs.get(w.getHashCode()));
					}
					commonDao.store(w);
					beforeInvs.remove(w.getHashCode());
				}
			}
			//期初库存/期初在途被完全发运,需要将这批数据还原到当前日结库存中,参与收发存日结计算
			if(beforeInvs!=null && beforeInvs.size()>0){
				//如果'日结库存',则只增加'期初库存量',如果'在途库存',则只增加'期初在途'
				List<String> hashcodes = new ArrayList<String>();
				Iterator<Entry<String, Double>> i = beforeInvs.entrySet().iterator();
				while(i.hasNext()){
					Entry<String, Double> entry = i.next();
					hashcodes.add("'"+entry.getKey()+"'");
				}
				int PAGE_NUMBER = 500;
				int size = hashcodes.size();
				int j = JavaTools.getSize(size, PAGE_NUMBER);
				for(int k=0;k<j;k++){
					int toIndex = JavaTools.getIndex(k, size, PAGE_NUMBER);
					List<String> ret = JavaTools.getListStr(hashcodes, k, toIndex, PAGE_NUMBER);
					objsBeforeInv = getObj(findBeforeInvObj(ret,formatTwoDate,type),session);
					
					for(Object[] o : objsBeforeInv){
						WmsTheKont src = commonDao.load(WmsTheKont.class, Long.parseLong(o[0].toString()));
						WmsTheKont desc  = EntityFactory.getEntity(WmsTheKont.class);
						BeanUtils.copyEntity(desc, src);
						desc.setId(null);
						if(WmsTheKont.type_2.equals(type)){
							desc.setQuantity(0D);//日结库存数量
							desc.setBeforeQuantity(src.getQuantity());//期初库存=期初日结库存数量
							desc.setBeforeOnWayQuantity(0D);//期初在途
						}else if(WmsTheKont.type_4.equals(type)){
							desc.setQuantity(0D);//日结库存数量
							desc.setBeforeQuantity(0D);//期初库存
							desc.setBeforeOnWayQuantity(src.getQuantity());//期初在途=期初日结库存数量
						}
						desc.setOrderDate(beforeOneDate);
						desc.setNote1(WmsTheKont.back);
						commonDao.store(desc);
					}objsBeforeInv.clear();
				}hashcodes.clear();

			}
		}
	}
	@SuppressWarnings("unchecked")
	private List<Object[]> getObj(String sql,Session session){
		SQLQuery query = session.createSQLQuery(sql);
		List<Object[]> objs = query.list();
		return objs;
	}
	private String findRec(Boolean beSys,String formatDate){
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(r.received_quantity_bu) as 数量,");
		sb.append("(case ik.extend_propc1 "+
  "when 'NEWBEST' then '最新'"+
  "when 'NEW' then '新'"+
  "when 'OLD' then '老'"+
  "when 'OLDBEST' then '最老'"+
  "else '-' end)as 工艺状态,");
		sb.append("r.inventory_status as 库存状态,i.code as 物料编码,i.name as 物料名称,");
		sb.append("sup.code as 供应商编码,sup.name as 供应商名称,");
		sb.append("a.start_received_date as 日期 ");
		sb.append("from wms_received_record r ");
		sb.append("left join wms_asn a on a.id = r.asn_id ");
		sb.append("left join wms_item_key ik on ik.id = r.item_key_id ");
		sb.append("left join wms_item i on i.id = ik.item_id ");
		sb.append("left join wms_organization sup on sup.id = ik.supplier_id ");
		sb.append("where r.received_quantity_bu>0 ");
		if(beSys){
			sb.append("and trunc(a.start_received_date, 'dd') = trunc(systimestamp-1, 'dd') ");
		}else{
			sb.append("and to_char(a.start_received_date, 'yyyy-MM-dd') = '"+formatDate+"' ");
		}
		sb.append("group by a.start_received_date,");
		sb.append("ik.extend_propc1,r.inventory_status,i.code,i.name,sup.code,sup.name");
		System.out.println("findRec:"+sb.toString());
		return sb.toString();
	}
	private String findShipInv(Boolean beSys,String beforeDate,String today,String todayTime){
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(l.quantity_bu) as 数量,");
		sb.append("(case l.extend_propc1 "+
  "when 'NEWBEST' then '最新' "+
  "when 'NEW' then '新' "+
  "when 'OLD' then '老' "+
  "when 'OLDBEST' then '最老' "+
  "else '-' end) as 工艺状态,");
		sb.append("l.inventory_status as 库存状态,l.item_code as 物料编码,l.item_name as 物料名称,");
		sb.append("sup.code as 供应商编码,sup.name as 供应商名称,");
		sb.append("l.storage_date as 日期 ");
		sb.append("from wms_inventory_log l ");
		sb.append("left join wms_organization sup on sup.id = l.supplier_id ");
		sb.append("where l.log_type='SHIPPING' ");
		if(beSys){
			sb.append("and to_char(l.storage_date,'yyyy-MM-dd') <= '"+beforeDate+"' ");//日结当日(系统执行前一日)
			sb.append("and to_char(l.created_time,'yyyy-MM-dd') = '"+today+"' ");//日结次日(系统执行当日)
			sb.append("and to_char(l.created_time,'yyyy-MM-dd HH24:mi:ss') < '"+todayTime+"' ");//日结次日执行截止点(系统执行当日截止点)
//			sb.append("and trunc(l.storage_date, 'dd') <= trunc(systimestamp-1, 'dd') ");//日结当日(系统执行前一日)
//			sb.append("and trunc(l.created_time, 'dd') = trunc(systimestamp, 'dd') ");//日结次日(系统执行当日)
//			sb.append("and to_char(l.created_time,'yyyy-MM-dd HH24:mi:ss') < '"+todayTime+"' ");//日结次日执行截止点(系统执行当日截止点)
		}else{
			sb.append("and to_char(l.storage_date,'yyyy-MM-dd') <= '"+beforeDate+"' ");//人工选择时找到所有存货日期在日结日期之前的已发运的日志
			sb.append("and to_char(l.created_time,'yyyy-MM-dd') >= '"+beforeDate+"' ");//日结当日以及以后已发运的属于当日的库存数据
		}
		sb.append("group by l.storage_date,l.extend_propc1,l.inventory_status,");
		sb.append("l.item_code,l.item_name,sup.code,sup.name");
		System.out.println("findShipInv:"+sb.toString());
		return sb.toString();
	}
	private String findInvAdjustAdd(Boolean beSys,String formatDate,String today,String todayTime){
		//库存调整+
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(l.quantity_bu) as 数量,");
		sb.append("(case l.extend_propc1 "+
  "when 'NEWBEST' then '最新' "+
  "when 'NEW' then '新' "+
  "when 'OLD' then '老' "+
  "when 'OLDBEST' then '最老' "+
  "else '-' end) as 工艺状态,");
		sb.append("l.inventory_status as 库存状态,l.item_code as 物料编码,l.item_name as 物料名称,");
		sb.append("sup.code as 供应商编码,sup.name as 供应商名称,");
		sb.append("l.storage_date as 日期 ");
		sb.append("from wms_inventory_log l ");
		sb.append("left join wms_organization sup on sup.id = l.supplier_id ");
		sb.append("where l.log_type='"+WmsInventoryLogType.INVENTORY_ADJUST+"' and l.inorout > 0 ");
//		sb.append("and l.item_code='Q1840825F61' ");//test
		sb.append("and (l.location_code > 'A-盘点' or l.location_code < 'A-盘点') ");//不太严谨
		if(beSys){
			sb.append("and trunc(l.storage_date, 'dd') <= trunc(systimestamp-1, 'dd') ");//日结当日(系统执行前一日)
			sb.append("and trunc(l.created_time, 'dd') = trunc(systimestamp-1, 'dd') ");//日结次日(系统执行当日)
//			sb.append("and to_char(l.created_time,'yyyy-MM-dd HH24:mi:ss') < '"+todayTime+"' ");//日结次日执行截止点(系统执行当日截止点)
		}else{
			sb.append("and to_char(l.storage_date,'yyyy-MM-dd') <= '"+formatDate+"' ");//人工选择时找到所有存货日期在日结日期之前的已发运的日志
			sb.append("and to_char(l.created_time,'yyyy-MM-dd') >= '"+formatDate+"' ");//日结当日以及以后已发运的属于当日的库存数据
		}
		sb.append("group by l.storage_date,l.extend_propc1,l.inventory_status,");
		sb.append("l.item_code,l.item_name,sup.code,sup.name");
		System.out.println("findInvAdjustAdd:"+sb.toString());
		return sb.toString();
	}
	//库存调整-
	private String findAdjustDel(Boolean beSys,String formatDate,String today,String todayTime){
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(l.quantity_bu) as 数量,");
		sb.append("(case l.extend_propc1 "+
  "when 'NEWBEST' then '最新' "+
  "when 'NEW' then '新' "+
  "when 'OLD' then '老' "+
  "when 'OLDBEST' then '最老' "+
  "else '-' end) as 工艺状态,");
		sb.append("l.inventory_status as 库存状态,l.item_code as 物料编码,l.item_name as 物料名称,");
		sb.append("sup.code as 供应商编码,sup.name as 供应商名称,");
		sb.append("l.storage_date as 日期 ");
		sb.append("from wms_inventory_log l ");
		sb.append("left join wms_organization sup on sup.id = l.supplier_id ");
		sb.append("where l.log_type='"+WmsInventoryLogType.INVENTORY_ADJUST+"' and l.inorout < 0 ");
//		sb.append("and l.item_code='1014201GD052ZC1' ");//test
		sb.append("and (l.location_code > 'A-盘点' or l.location_code < 'A-盘点') ");//不太严谨
		if(beSys){
			sb.append("and trunc(l.storage_date, 'dd') <= trunc(systimestamp-1, 'dd') ");//日结当日(系统执行前一日)
			sb.append("and trunc(l.created_time, 'dd') = trunc(systimestamp-1, 'dd') ");//日结次日(系统执行当日)
//			sb.append("and to_char(l.created_time,'yyyy-MM-dd HH24:mi:ss') < '"+todayTime+"' ");//日结次日执行截止点(系统执行当日截止点)
		}else{
			sb.append("and to_char(l.storage_date,'yyyy-MM-dd') <= '"+formatDate+"' ");//人工选择时找到所有存货日期在日结日期之前的已发运的日志
			sb.append("and to_char(l.created_time,'yyyy-MM-dd') >= '"+formatDate+"' ");//日结当日以及以后已发运的属于当日的库存数据
		}
		sb.append("group by l.storage_date,l.extend_propc1,l.inventory_status,");
		sb.append("l.item_code,l.item_name,sup.code,sup.name");
		System.out.println("findAdjustDel:"+sb.toString());
		return sb.toString();
	}
	/**送检质检*/
	private String findQualityMoveSoi(Boolean beSys,String formatDate,String today,String todayTime){
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(t.quantity) as 数量,");
		sb.append("(case t.extendpropc1 "+
				  "when 'NEWBEST' then '最新' "+
				  "when 'NEW' then '新' "+
				  "when 'OLD' then '老' "+
				  "when 'OLDBEST' then '最老' "+
				  "else '-' end) as 工艺状态,");
		sb.append("t.inventorystatus,t.quality_status,t.itemcode,t.itemname,t.supcode,t.supname,");
		sb.append("t.storagedate from WMS_QUALITY_MOVE_SOI_LOG t");
		sb.append(" where t.description = '"+WmsQualityMoveSoiLog.QUALITY_MOVE_DOC_SOI+"'");
		if(beSys){
			sb.append(" and trunc(t.storagedate, 'dd') <= trunc(systimestamp-1, 'dd') ");//日结当日(系统执行前一日)
			sb.append(" and trunc(t.created_time, 'dd') = trunc(systimestamp-1, 'dd') ");//日结次日(系统执行当日)
		}else{
			sb.append(" and to_char(t.storagedate,'yyyy-MM-dd') <= '"+formatDate+"' ");//人工选择时找到所有存货日期在日结日期之前的已发运的日志
			sb.append(" and to_char(t.created_time,'yyyy-MM-dd') = '"+formatDate+"' ");//日结次日(系统执行当日)
		}
		sb.append(" group by t.extendpropc1,t.inventorystatus,t.quality_status,t.itemcode,t.itemname,t.supcode,t.supname,t.storagedate");
		System.out.println("findQualityMoveSoiAdd:"+sb.toString());
		return sb.toString();
	}
	/**送检质检+*/
	private String findQualityMoveSoiAdd(Boolean beSys,String formatDate,String today,String todayTime){
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(l.quantity_bu) as 数量,");
		sb.append("(case l.extend_propc1 "+
  "when 'NEWBEST' then '最新' "+
  "when 'NEW' then '新' "+
  "when 'OLD' then '老' "+
  "when 'OLDBEST' then '最老' "+
  "else '-' end) as 工艺状态,");
		sb.append("l.inventory_status as 库存状态,l.item_code as 物料编码,l.item_name as 物料名称,");
		sb.append("sup.code as 供应商编码,sup.name as 供应商名称,");
		sb.append("l.storage_date as 日期 ");
		sb.append("from wms_inventory_log l ");
		sb.append("left join wms_organization sup on sup.id = l.supplier_id ");
		sb.append("where l.log_type='"+WmsInventoryLogType.QUALITY+"' and l.inorout > 0 ");
		sb.append("and l.description='"+WmsInventoryLogType.QUALITY_MOVE_DOC_SOI+"' ");
//		sb.append("and l.item_code='Q1840825F61' ");//test
		if(beSys){
			sb.append("and trunc(l.storage_date, 'dd') <= trunc(systimestamp-1, 'dd') ");//日结当日(系统执行前一日)
			sb.append("and trunc(l.created_time, 'dd') = trunc(systimestamp-1, 'dd') ");//日结次日(系统执行当日)
		}else{
			sb.append("and to_char(l.storage_date,'yyyy-MM-dd') <= '"+formatDate+"' ");//人工选择时找到所有存货日期在日结日期之前的已发运的日志
			sb.append("and to_char(l.created_time,'yyyy-MM-dd') = '"+formatDate+"' ");//日结次日(系统执行当日)
		}
		sb.append("group by l.storage_date,l.extend_propc1,l.inventory_status,");
		sb.append("l.item_code,l.item_name,sup.code,sup.name");
		/*sb.append("select sum(t.quantity) as 数量,");
		sb.append("(case t.extendpropc1 "+
				  "when 'NEWBEST' then '最新' "+
				  "when 'NEW' then '新' "+
				  "when 'OLD' then '老' "+
				  "when 'OLDBEST' then '最老' "+
				  "else '-' end) as 工艺状态,");
		sb.append("t.inventorystatus,t.quality_status,t.itemcode,t.itemname,t.supcode,t.supname,");
		sb.append("t.storagedate from WMS_QUALITY_MOVE_SOI_LOG t");
		sb.append(" where t.description = '"+WmsInventoryLogType.QUALITY_MOVE_DOC_SOI+"'");
		if(beSys){
			sb.append(" and trunc(t.storagedate, 'dd') <= trunc(systimestamp-1, 'dd') ");//日结当日(系统执行前一日)
			sb.append(" and trunc(t.created_time, 'dd') = trunc(systimestamp-1, 'dd') ");//日结次日(系统执行当日)
		}else{
			sb.append(" and to_char(t.storagedate,'yyyy-MM-dd') <= '"+formatDate+"' ");//人工选择时找到所有存货日期在日结日期之前的已发运的日志
			sb.append(" and to_char(t.created_time,'yyyy-MM-dd') = '"+formatDate+"' ");//日结次日(系统执行当日)
		}
		sb.append(" group by t.extendpropc1,t.inventorystatus,t.quality_status,t.itemcode,t.itemname,t.supcode,t.supname,t.storagedate");*/
		System.out.println("findQualityMoveSoiAdd:"+sb.toString());
		return sb.toString();
	}
	/**送检质检-*/
	private String findQualityMoveSoiDel(Boolean beSys,String formatDate,String today,String todayTime){
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(l.quantity_bu) as 数量,");
		sb.append("(case l.extend_propc1 "+
  "when 'NEWBEST' then '最新' "+
  "when 'NEW' then '新' "+
  "when 'OLD' then '老' "+
  "when 'OLDBEST' then '最老' "+
  "else '-' end) as 工艺状态,");
		sb.append("l.inventory_status as 库存状态,l.item_code as 物料编码,l.item_name as 物料名称,");
		sb.append("sup.code as 供应商编码,sup.name as 供应商名称,");
		sb.append("l.storage_date as 日期 ");
		sb.append("from wms_inventory_log l ");
		sb.append("left join wms_organization sup on sup.id = l.supplier_id ");
		sb.append("where l.log_type='"+WmsInventoryLogType.QUALITY+"' and l.inorout < 0 ");
		sb.append("and l.description='"+WmsInventoryLogType.QUALITY_MOVE_DOC_SOI+"' ");
//		sb.append("and l.item_code='Q1840825F61' ");//test
		if(beSys){
			sb.append("and trunc(l.storage_date, 'dd') <= trunc(systimestamp-1, 'dd') ");//日结当日(系统执行前一日)
			sb.append("and trunc(l.created_time, 'dd') = trunc(systimestamp-1, 'dd') ");//日结次日(系统执行当日)
		}else{
			sb.append("and to_char(l.storage_date,'yyyy-MM-dd') <= '"+formatDate+"' ");//人工选择时找到所有存货日期在日结日期之前的已发运的日志
			sb.append("and to_char(l.created_time,'yyyy-MM-dd') = '"+formatDate+"' ");//日结次日(系统执行当日)
		}
		sb.append("group by l.storage_date,l.extend_propc1,l.inventory_status,");
		sb.append("l.item_code,l.item_name,sup.code,sup.name");
		System.out.println("findQualityMoveSoiDel:"+sb.toString());
		return sb.toString();
	}
	private String findInvQualityInAdd(Boolean beSys,String formatDate,String today,String todayTime){
		//库内质检-库存增加
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(l.quantity_bu) as 数量,");
		sb.append("(case l.extend_propc1 "+
  "when 'NEWBEST' then '最新' "+
  "when 'NEW' then '新' "+
  "when 'OLD' then '老' "+
  "when 'OLDBEST' then '最老' "+
  "else '-' end) as 工艺状态,");
		sb.append("l.inventory_status as 库存状态,l.item_code as 物料编码,l.item_name as 物料名称,");
		sb.append("sup.code as 供应商编码,sup.name as 供应商名称,");
		sb.append("l.storage_date as 日期 ");
		sb.append("from wms_inventory_log l ");
		sb.append("left join wms_organization sup on sup.id = l.supplier_id ");
		sb.append("where l.log_type='"+WmsInventoryLogType.QUALITY+"' and l.inorout > 0 ");
		sb.append("and l.description='"+WmsInventoryLogType.QUALITY_IN_ADD+"' ");
//		sb.append("and l.item_code='Q1840825F61' ");//test
		if(beSys){
			sb.append("and trunc(l.storage_date, 'dd') <= trunc(systimestamp-1, 'dd') ");//日结当日(系统执行前一日)
			sb.append("and trunc(l.created_time, 'dd') = trunc(systimestamp-1, 'dd') ");//日结次日(系统执行当日)
		}else{
			sb.append("and to_char(l.storage_date,'yyyy-MM-dd') <= '"+formatDate+"' ");//人工选择时找到所有存货日期在日结日期之前的已发运的日志
			sb.append("and to_char(l.created_time,'yyyy-MM-dd') = '"+formatDate+"' ");//日结次日(系统执行当日)
		}
		sb.append("group by l.storage_date,l.extend_propc1,l.inventory_status,");
		sb.append("l.item_code,l.item_name,sup.code,sup.name");
		System.out.println("findInvQualityInAdd:"+sb.toString());
		return sb.toString();
	}
	private String findInvQualityInDel(Boolean beSys,String formatDate,String today,String todayTime){
		//库内质检-库存增加
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(l.quantity_bu) as 数量,");
		sb.append("(case l.extend_propc1 "+
  "when 'NEWBEST' then '最新' "+
  "when 'NEW' then '新' "+
  "when 'OLD' then '老' "+
  "when 'OLDBEST' then '最老' "+
  "else '-' end) as 工艺状态,");
		sb.append("l.inventory_status as 库存状态,l.item_code as 物料编码,l.item_name as 物料名称,");
		sb.append("sup.code as 供应商编码,sup.name as 供应商名称,");
		sb.append("l.storage_date as 日期 ");
		sb.append("from wms_inventory_log l ");
		sb.append("left join wms_organization sup on sup.id = l.supplier_id ");
		sb.append("where l.log_type='"+WmsInventoryLogType.QUALITY+"' and l.inorout < 0 ");
		sb.append("and l.description='"+WmsInventoryLogType.QUALITY_IN_DEL+"' ");
//		sb.append("and l.item_code='Q1840825F61' ");//test
		if(beSys){
			sb.append("and trunc(l.storage_date, 'dd') <= trunc(systimestamp-1, 'dd') ");//日结当日(系统执行前一日)
			sb.append("and trunc(l.created_time, 'dd') = trunc(systimestamp-1, 'dd') ");//日结次日(系统执行当日)
		}else{
			sb.append("and to_char(l.storage_date,'yyyy-MM-dd') <= '"+formatDate+"' ");//人工选择时找到所有存货日期在日结日期之前的已发运的日志
			sb.append("and to_char(l.created_time,'yyyy-MM-dd') = '"+formatDate+"' ");//日结次日(系统执行当日)
		}
		sb.append("group by l.storage_date,l.extend_propc1,l.inventory_status,");
		sb.append("l.item_code,l.item_name,sup.code,sup.name");
		System.out.println("findInvQualityInDel:"+sb.toString());
		return sb.toString();
	}
	private String findInv(Boolean beSys,String formatDate){
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(ii.quantity_bu) as 数量,");
		sb.append("(case ik.extend_propc1 "+
  "when 'NEWBEST' then '最新'"+
  "when 'NEW' then '新'"+
  "when 'OLD' then '老'"+
  "when 'OLDBEST' then '最老'"+
  "else '-' end)as 工艺状态,");
		sb.append("ii.status as 库存状态,i.code as 物料编码,i.name as 物料名称,");
		sb.append("sup.code as 供应商编码,sup.name as 供应商名称,");
		sb.append("ik.storage_date as 日期 ");
		sb.append("from wms_inventory ii ");
		sb.append("left join wms_item_key ik on ik.id = ii.item_key_id ");
		sb.append("left join wms_item i on i.id = ik.item_id ");
		sb.append("left join wms_organization sup on sup.id = ik.supplier_id ");
		sb.append("left join wms_location l on l.id = ii.location_id ");
		sb.append("where ii.quantity_bu>0 and (l.type > 'SHIP' or l.type < 'SHIP')");//过滤签收区(备货)
		if(beSys){
			sb.append("and trunc(ik.storage_date, 'dd') <= trunc(systimestamp-1, 'dd') ");
		}else{
			sb.append("and to_char(ik.storage_date, 'yyyy-MM-dd') <= '"+formatDate+"' ");
		}
		sb.append("group by ik.storage_date,");
		sb.append("ik.extend_propc1,ii.status,i.code,i.name,sup.code,sup.name");
		System.out.println("findInv:"+sb.toString());
		return sb.toString();
	}
	private String findPick(Boolean beSys,String formatDate){
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(ii.quantity_bu) as 数量,");
		sb.append("(case ik.extend_propc1 "+
  "when 'NEWBEST' then '最新'"+
  "when 'NEW' then '新'"+
  "when 'OLD' then '老'"+
  "when 'OLDBEST' then '最老'"+
  "else '-' end)as 工艺状态,");
		sb.append("ii.status as 库存状态,i.code as 物料编码,i.name as 物料名称,");
		sb.append("sup.code as 供应商编码,sup.name as 供应商名称,");
		sb.append("ik.storage_date as 日期 ");
		sb.append("from wms_inventory ii ");
		sb.append("left join wms_item_key ik on ik.id = ii.item_key_id ");
		sb.append("left join wms_item i on i.id = ik.item_id ");
		sb.append("left join wms_organization sup on sup.id = ik.supplier_id ");
		sb.append("left join wms_location l on l.id = ii.location_id ");
		sb.append("where ii.quantity_bu>0 and l.type ='SHIP'");//签收区(备货)
		if(beSys){
			sb.append("and trunc(ik.storage_date, 'dd') <= trunc(systimestamp-1, 'dd') ");
		}else{
			sb.append("and to_char(ik.storage_date, 'yyyy-MM-dd') <= '"+formatDate+"' ");
		}
		sb.append("group by ik.storage_date,");
		sb.append("ik.extend_propc1,ii.status,i.code,i.name,sup.code,sup.name");
		
		System.out.println("findPick:"+sb.toString());
		return sb.toString();
		
		/*sb.append("select sum(t.moved_quantity_bu) as 数量,");
		sb.append("(case ik.extend_propc1 "+
  "when 'NEWBEST' then '最新'"+
  "when 'NEW' then '新'"+
  "when 'OLD' then '老'"+
  "when 'OLDBEST' then '最老'"+
  "else '-' end)as 工艺状态,");
		sb.append("t.inventory_status as 库存状态,i.code as 物料编码,i.name as 物料名称,");
		sb.append("sup.code as 供应商编码,sup.name as 供应商名称,");
		sb.append("t.pick_time as 日期 ");
		sb.append("from wms_task t ");
		sb.append("left join wms_move_doc_detail mm on mm.id = t.move_doc_detail_id ");
		sb.append("left join wms_move_doc m on m.id = mm.move_doc_id ");
		sb.append("left join wms_item_key ik on ik.id = t.item_key_id ");
		sb.append("left join wms_item i on i.id = ik.item_id ");
		sb.append("left join wms_organization sup on sup.id = ik.supplier_id ");
		sb.append("where t.moved_quantity_bu>0 ");
		sb.append("and t.type='MV_PICKTICKET_PICKING' ");
		if(beSys){
			sb.append("and trunc(t.pick_time, 'dd') = trunc(systimestamp-1, 'dd') ");
		}else{
			sb.append("and to_char(t.pick_time, 'yyyy-MM-dd') = '"+formatDate+"' ");
		}
		//23号拣货完成,23号就发运不属于在途,但之后发运也算作23号在途
		sb.append("and not exists (");
		sb.append("select 1 from wms_task tt ");
		sb.append("left join wms_move_doc_detail mmt on mmt.id = tt.move_doc_detail_id ");
		sb.append("left join wms_move_doc mt on mt.id = mmt.move_doc_id ");
		sb.append("where tt.tired_moved_quantity_bu>0 and tt.type='MV_PICKTICKET_PICKING' ");
		sb.append("and mt.ship_status = 'SHIPPED' ");
		if(beSys){
			sb.append("and trunc(mt.ship_time, 'dd') = trunc(systimestamp-1, 'dd') ");
		}else{
			sb.append("and to_char(mt.ship_time, 'yyyy-MM-dd') = '"+formatDate+"' ");
		}
		sb.append("and tt.id = t.id) ");
		sb.append("group by t.pick_time,ik.extend_propc1,t.inventory_status,");
		sb.append("i.code,i.name,sup.code,sup.name");*/
		
	}
	private String findShip(Boolean beSys,String formatDate){
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(t.tired_moved_quantity_bu) as 数量,");
		sb.append("(case ik.extend_propc1 "+
  "when 'NEWBEST' then '最新'"+
  "when 'NEW' then '新'"+
  "when 'OLD' then '老'"+
  "when 'OLDBEST' then '最老'"+
  "else '-' end)as 工艺状态,");
		sb.append("t.inventory_status as 库存状态,i.code as 物料编码,i.name as 物料名称,");
		sb.append("sup.code as 供应商编码,sup.name as 供应商名称,");
		sb.append("m.ship_time as 日期 ");
		sb.append("from wms_task t ");
		sb.append("left join wms_move_doc_detail mm on mm.id = t.move_doc_detail_id ");
		sb.append("left join wms_move_doc m on m.id = mm.move_doc_id ");
		sb.append("left join wms_item_key ik on ik.id = t.item_key_id ");
		sb.append("left join wms_item i on i.id = ik.item_id ");
		sb.append("left join wms_organization sup on sup.id = ik.supplier_id ");
		sb.append("where t.tired_moved_quantity_bu>0 and t.type='MV_PICKTICKET_PICKING' ");
		sb.append("and m.ship_status = 'SHIPPED' ");
		if(beSys){
			sb.append("and trunc(m.ship_time, 'dd') = trunc(systimestamp-1, 'dd') ");
		}else{
			sb.append("and to_char(m.ship_time, 'yyyy-MM-dd') = '"+formatDate+"' ");
		}
		sb.append("group by m.ship_time,ik.extend_propc1,t.inventory_status,");
		sb.append("i.code,i.name,sup.code,sup.name");
		System.out.println("findShip:"+sb.toString());
		return sb.toString();
	}
	private String findBeforeInv(Boolean beSys,String formatTwoDate,String type){
		StringBuffer sb = new StringBuffer();
		sb.append("select t.hashcode,sum(t.quantity) ");
		sb.append("from wms_the_kont t ");
		sb.append("where to_char(t.order_date,'yyyy-MM-dd') = '"+formatTwoDate+"' ");
		sb.append("and t.type in ('"+type+"') ");
//		sb.append("and t.supcode = 'L22046' ");//test
//		sb.append("and t.itemcode = '1025100GE' ");//test
		sb.append("and (t.note > '").append(WmsTheKont.type_3).append("' or t.note < '").append(WmsTheKont.type_3).append("') ");//add 20160422
		sb.append("group by t.hashcode ");
		sb.append("having sum(t.quantity) > 0");
		System.out.println("findBeforeInv:"+sb.toString());
		return sb.toString();
	}
	private String findBeforeInvObj(List<String> hashcodes,String formatTwoDate,String type){
		StringBuffer sb = new StringBuffer();
		sb.append("select t.id,t.id ");
		sb.append("from wms_the_kont t ");
		sb.append("where 1=1 ");
		sb.append("and to_char(t.order_date,'yyyy-MM-dd') = '"+formatTwoDate+"' ");
		sb.append("and t.type in ('"+type+"') ");
		sb.append("and t.hashcode in ("+StringUtils.substringBetween(hashcodes.toString(), "[", "]")+") ");
		sb.append("and (t.note > '").append(WmsTheKont.type_3).append("' or t.note < '").append(WmsTheKont.type_3).append("')");//add 20160422
		System.out.println("findBeforeInvObj:"+sb.toString());
		return sb.toString();
	}
}
