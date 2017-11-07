package com.vtradex.wms.server.service.middle.pojo;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.util.Assert;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.message.Task;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.middle.MesMisInventory;
import com.vtradex.wms.server.model.middle.WmsBusiness;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.organization.TypeOfBill;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.service.billing.WmsBillingManager;
import com.vtradex.wms.server.service.middle.MiddleTableName;
import com.vtradex.wms.server.service.middle.MilldleSessionManager;
import com.vtradex.wms.server.service.middle.MilldleTableManager;
import com.vtradex.wms.server.service.shipping.WmsPickTicketManager;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;

public class DefaultmilldleTableManager extends DefaultBaseManager implements MilldleTableManager {
	/**500*/
	public static Integer PAGE_NUMBER = 500;
	private JdbcTemplate jdbcTemplate;
	protected MilldleSessionManager milldleSessionManager;
	protected WmsPickTicketManager wmsPickTicketManager;
	public DefaultmilldleTableManager(MilldleSessionManager milldleSessionManager
			,WmsPickTicketManager wmsPickTicketManager){
		this.milldleSessionManager = milldleSessionManager;
		this.wmsPickTicketManager = wmsPickTicketManager;
	}
	
	public void sysMiddleSupplier(){
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().openSession();
		try {
			String sql = "select ID,COMPANY,SUPPLIER_CODE,SUPPLIER_NAME,COUNTRY,PROVINCE,CITY,"+
					"POSTCODE,CONTACT_PERSON,CONTACT_PHONE,CELL_PHONE,FAX,EMAIL,"+
					"ADDRESS,MEMO,SENDER "+
					"from "+MiddleTableName.MIDDLESUPPLIER+
					" WHERE COMPANY = '106'";
			SQLQuery query1 = session.createSQLQuery(sql);
			List<Object[]> objs1 = query1.list();
			
			sql = "select code from wms_organization where 1=1";//is_supplier = 'Y'
			SQLQuery query2 = session.createSQLQuery(sql);
			List<String> objs2 = query2.list();
			
			List<Object[]> editObj = new ArrayList<Object[]>();
			List<String> codes = new ArrayList<String>();
			List<Object[]> newObj = new ArrayList<Object[]>();
			for(Object[] obj : objs1){
				if(objs2.contains(obj[2].toString())){//edit
					editObj.add(obj);
					codes.add("'"+obj[2]+"'");
				}else{//new
					newObj.add(obj);
				}
			}
			int PAGE_NUMBER = 500;
			int size = editObj.size();
			int j = JavaTools.getSize(size, PAGE_NUMBER);
			for(int k=0;k<j;k++){
				int toIndex = JavaTools.getIndex(k, size, PAGE_NUMBER);
				List<Object[]> ret = JavaTools.getListObj(editObj, k, toIndex, PAGE_NUMBER);
				List<String> retCodes = JavaTools.getListStr(codes, k, toIndex, PAGE_NUMBER);
				milldleSessionManager.sysMiddleSupplier(new Object[]{
						Boolean.FALSE,ret,retCodes
				});
			}
			editObj.clear();
			
			size = newObj.size();
			j = JavaTools.getSize(size, PAGE_NUMBER);
			for(int k=0;k<j;k++){
				int toIndex = JavaTools.getIndex(k, size, PAGE_NUMBER);
				List<Object[]> ret = JavaTools.getListObj(newObj, k, toIndex, PAGE_NUMBER);
				milldleSessionManager.sysMiddleSupplier(new Object[]{
						Boolean.TRUE,ret
				});
			}
			newObj.clear();
			
			sql = "update "+MiddleTableName.MIDDLESUPPLIER+" set DEAL_TIME = systimestamp";
			SQLQuery query3 = session.createSQLQuery(sql);
			query3.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getLocalizedMessage());
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
		
	}
	@SuppressWarnings("unchecked")
	public void sysMiddleMaterial(){
		
		Map<String,String[]> senderMap = senderMap();
		
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().openSession();
		try {
			String sql = "select ID,COMPANY,MATERIAL_CODE,MATERIAL_NAME,MATERIAL_ABBR,"+
					"BASE_UINT,MATERIAL_TYPE,MEMO,SENDER"+
					" from "+MiddleTableName.MIDDLEMATERIAL+
					" WHERE COMPANY = '106'";
			SQLQuery query1 = session.createSQLQuery(sql);
			List<Object[]> objs1 = query1.list();
			
			Map<String,List<Object[]>> senders = senders(objs1,new int[]{
					8
			});
			
			String key = null;
			String[] wearhouseCompany = null;
			Iterator<Entry<String, List<Object[]>>> iterator = senders.entrySet().iterator();
			while(iterator.hasNext()){
				Entry<String, List<Object[]>> entry = iterator.next();
				key = entry.getKey();//sender
				objs1 = entry.getValue();
				if(!senderMap.containsKey(key)){
					continue;
				}
				wearhouseCompany = senderMap.get(key);//[warehouse,company]
				
				List<WmsItem> items = commonDao.findByQuery("FROM WmsItem i WHERE 1=1"
						+ " AND i.company.id =:companyId",
						new String[]{"companyId"},new Object[]{Long.parseLong(wearhouseCompany[1])});//根据货主过滤
				Map<String,WmsItem> itemCode = new HashMap<String,WmsItem>();
				for(WmsItem i : items){
					itemCode.put(i.getCode(), i);
				}
				items.clear();
				
				List<Object[]> pus = commonDao.findByQuery("SELECT pu.id,pu.item.code FROM WmsPackageUnit pu"
						+ " WHERE 1=1 AND pu.item.company.id =:companyId AND pu.lineNo = 1",//根据货主过滤
						new String[]{"companyId"},new Object[]{Long.parseLong(wearhouseCompany[1])});
				Map<String,WmsPackageUnit> puCode = new HashMap<String,WmsPackageUnit>();
				WmsPackageUnit pu = null;
				for(Object[] o : pus){
					pu = commonDao.load(WmsPackageUnit.class, Long.parseLong(o[0].toString()));
					puCode.put(o[1].toString(),pu);
				}
				pus.clear();
				
				List<Object[]> editObj = new ArrayList<Object[]>();
				List<Object[]> newObj = new ArrayList<Object[]>();
				for(Object[] obj : objs1){
					if(itemCode.containsKey(obj[2].toString())){//edit
						editObj.add(obj);
					}else{//new
						newObj.add(obj);
					}
				}
				objs1.clear();
				
				int PAGE_NUMBER = 500;
				int size = editObj.size();
				int j = JavaTools.getSize(size, PAGE_NUMBER);
				for(int k=0;k<j;k++){
					int toIndex = JavaTools.getIndex(k, size, PAGE_NUMBER);
					List<Object[]> ret = JavaTools.getListObj(editObj, k, toIndex, PAGE_NUMBER);
					milldleSessionManager.sysMiddleMaterial(new Object[]{
							Boolean.FALSE,ret,itemCode,puCode
					});
					System.out.println("edit=="+j+":"+(k+1));
				}
				editObj.clear();itemCode.clear();puCode.clear();
				
				size = newObj.size();
				j = JavaTools.getSize(size, PAGE_NUMBER);
				WmsOrganization company = commonDao.load(WmsOrganization.class,Long.parseLong(wearhouseCompany[1]));
				for(int k=0;k<j;k++){
					int toIndex = JavaTools.getIndex(k, size, PAGE_NUMBER);
					List<Object[]> ret = JavaTools.getListObj(newObj, k, toIndex, PAGE_NUMBER);
					milldleSessionManager.sysMiddleMaterial(new Object[]{
							Boolean.TRUE,ret,company
					});
					System.out.println("new=="+j+":"+(k+1));
				}
				newObj.clear();
			}
			sql = "update "+MiddleTableName.MIDDLEMATERIAL+" set DEAL_TIME = systimestamp";
			SQLQuery query3 = session.createSQLQuery(sql);
			query3.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getLocalizedMessage());
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void sysMiddleDeliverydoc(){
		Map<String,String[]> senderMap = senderMap();
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().openSession();
		try {
			String sql = "select ID,COMPANY,DELIVERY_CODE,JIT_CODE,PULL_TYPE,"+//ID,COMPANY,发货单编码2,看板编码3,物料拉动方式4
					"DESTINATION_PLANT,DESTINATION_DOCK,DESTINATION_WAREHOUSE,SENDER,"+//收货工厂5,收货道口6,收货仓库7,发送方8
					"REQUIRED_TIME,CONFIRMED_TIME,DELIVERY_TIME,RECEIVED_TIME,"+//需求时间9,确认时间10,发货时间11,验收时间12
					"CREATOR,PUBLISHEDUSER,SIGNER,SCANNER,"+//创建人13,发布人14,签收人15,扫码人16
					"MES_HEAD_MEMO,WMS_MEMO,CREATE_TIME,SUPPLIER_CODE,"+//MES头备注17,WMS备注18,创建时间19,供应商编码20
					"MATERIAL_CODE,MATERIAL_MAME,ORDER_CODE,MODEL,"+//物料编码21,物料名称22,生产订单编码23,机型24
					"REQUIRED_QTY,RECEIVED_QTY,MATERIAL_WORKER,"+//需求数量25,验收数量26,物料工27
					"MATERIAL_KEEPER,MES_DETAIL_MEMO,SEND_TIME"+//保管员28,MES明细备注29,插入时间30
					" from "+MiddleTableName.MIDDLEDELIVERYDOCDETAIL+
					" WHERE COMPANY = '106'"
					+" AND FLAG = 0 AND SENDER IS NOT NULL"
//					+" AND delivery_code in ('JAC2015061533055-291')"//test
					+ " AND REQUIRED_QTY>0";
			SQLQuery query2 = session.createSQLQuery(sql);
			List<Object[]> objs2 = query2.list();
			//d[2],d[20],d[21],d[24],d[8],d[29]
			Map<String,List<Object[]>> detailSenders = senders(objs2,new int[]{
					8
			});
			List<Long> errorPicIds = null;
			List<Long> allPicDetailIds = new ArrayList<Long>();
			List<String> nullPicSenders = new ArrayList<String>();
			//优化 
			Map<String,WmsBillType> bills = new HashMap<String, WmsBillType>();
			
			String key = null,billKey = null;
			String[] wearhouseCompany = null;
			Iterator<Entry<String, List<Object[]>>> iterator = detailSenders.entrySet().iterator();
			while(iterator.hasNext()){
				Entry<String, List<Object[]>> entry = iterator.next();
				key = entry.getKey();//sender
				objs2 = entry.getValue();
				if(!senderMap.containsKey(key)){
					nullPicSenders.add(key);//发货单未匹配到发送方
					continue;
				}
				wearhouseCompany = senderMap.get(key);//[warehouse,company]
				
				WmsOrganization company = commonDao.load(WmsOrganization.class,Long.parseLong(wearhouseCompany[1]));
				WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class, Long.parseLong(wearhouseCompany[0]));
				WmsBillType bill = null;
				billKey = company.getId()+TypeOfBill.SHIP;
				if(bills.containsKey(billKey)){
					bill = bills.get(billKey);
				}else{
					bill = (WmsBillType) commonDao.findByQueryUniqueResult("FROM WmsBillType b WHERE b.company.id =:companyId"
							+ " AND b.type =:type AND b.code = 'OUT-001'", 
							new String[]{"companyId","type"}, new Object[]{company.getId(),TypeOfBill.SHIP});
				}
				if(bill==null){
					bill = EntityFactory.getEntity(WmsBillType.class);
					bill.setCode("OUT-001");
					bill.setName("正常发货单");
					bill.setStatus(BaseStatus.ENABLED);
					bill.setCompany(company);
					bill.setBeInner(true);
					bill.setType(TypeOfBill.SHIP);
					bill.setDescription("系统创建");
					commonDao.store(bill);
				}
				bills.put(billKey,bill);
				
				Object[] dbToMap = dbToMap(company, objs2,new int[]{
						21,20
				});
				Map<String,WmsItem> itemMaps = (Map<String, WmsItem>) dbToMap[0];
				Map<String,WmsPackageUnit> puMaps = (Map<String, WmsPackageUnit>) dbToMap[1];
				Map<String,WmsOrganization> supMaps = (Map<String, WmsOrganization>) dbToMap[2];
				for(Object[] o : objs2){
					allPicDetailIds.add(Long.parseLong(o[0].toString()));
				}
				
				//按照发货单号分组明细
				Map<String,List<Object[]>> detailDeliverycodes = senders(objs2,new int[]{
						2
				});
				System.out.println("sysMiddleDeliverydoc_"+detailDeliverycodes.size()+":"+objs2.size());
				Iterator<Entry<String, List<Object[]>>> iterator1 = detailDeliverycodes.entrySet().iterator();
				while(iterator1.hasNext()){
					Entry<String, List<Object[]>>  entry1 = iterator1.next();
					Object[] backObj = milldleSessionManager.sysMiddleDeliverydoc(new Object[]{
							company,warehouse,bill,entry1,itemMaps,puMaps,supMaps
					});
					List<Long> succesPic = (List<Long>) backObj[0];
					List<Long> errorPic = (List<Long>) backObj[1];
					Boolean beActive = (Boolean) backObj[2];
					if(succesPic.size()>0){
						int size = succesPic.size();
						int j = JavaTools.getSize(size, PAGE_NUMBER);
						for(int k=0;k<j;k++){
							int toIndex = JavaTools.getIndex(k, size, PAGE_NUMBER);
							List<Long> ret = JavaTools.getListLong(succesPic, k, toIndex, PAGE_NUMBER);
							sql = "update "+MiddleTableName.MIDDLEDELIVERYDOCDETAIL+" set FLAG = 1,DEAL_TIME = systimestamp"
									+ " where COMPANY = '106' AND ID in "
									+ "("+StringUtils.substringBetween(ret.toString(), "[", "]")+")";
							SQLQuery query = session.createSQLQuery(sql);
							query.executeUpdate();
						}
						allPicDetailIds.removeAll(succesPic);//清除已成功的发货单ID
					}
					if(errorPic!=null && errorPic.size()>0){
						int size = errorPic.size();
						int j = JavaTools.getSize(size, PAGE_NUMBER);
						for(int k=0;k<j;k++){
							int toIndex = JavaTools.getIndex(k, size, PAGE_NUMBER);
							List<Long> ret = JavaTools.getListLong(errorPic, k, toIndex, PAGE_NUMBER);
							sql = "update "+MiddleTableName.MIDDLEDELIVERYDOCDETAIL+" set FLAG = 8,WMS_MEMO='物料单位供应商信息不全',DEAL_TIME = systimestamp"
									+ " where COMPANY = '106' AND ID in "
									+ "("+StringUtils.substringBetween(ret.toString(), "[", "]")+")";
							SQLQuery query = session.createSQLQuery(sql);
							query.executeUpdate();
						}
						allPicDetailIds.removeAll(errorPic);//清除已失败的发货单ID
					}
					if(beActive){
						try {
							wmsPickTicketManager.activePickTicket(Long.parseLong(backObj[3].toString()));
						} catch (Exception e) {
							System.out.println("发货单激活失败:"+entry1.getKey()+MyUtils.enter+e.getLocalizedMessage());
						}
					}
				}
				
				detailSenders.remove(key);//清除已处理完毕的发货明细,剩余未处理最后做失败标记
			}
			//WMS未维护的发送方做失败处理8
			for(String sender : nullPicSenders){
				objs2 = detailSenders.get(sender);
				errorPicIds = new ArrayList<Long>();
				for(Object[] o : objs2){
					errorPicIds.add(Long.parseLong(o[0].toString()));
				}
				if(errorPicIds.size()>0){
					int size = errorPicIds.size();
					int j = JavaTools.getSize(size, PAGE_NUMBER);
					for(int k=0;k<j;k++){
						int toIndex = JavaTools.getIndex(k, size, PAGE_NUMBER);
						List<Long> ret = JavaTools.getListLong(errorPicIds, k, toIndex, PAGE_NUMBER);
						sql = "update "+MiddleTableName.MIDDLEDELIVERYDOCDETAIL+" set FLAG = 8,WMS_MEMO='WMS未维护发送方',DEAL_TIME = systimestamp"
								+ " where COMPANY = '106' AND ID in "
								+ "("+StringUtils.substringBetween(ret.toString(), "[", "]")+")";
						SQLQuery query = session.createSQLQuery(sql);
						query.executeUpdate();
					}
					errorPicIds.clear();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getLocalizedMessage());
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void sysPickShip(){
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().openSession();
		try {
			String h1 = "select ID,COMPANY,DELIVERY_CODE,SUPPLIER_CODE,MATERIAL_CODE,MATERIAL_MAME,ORDER_CODE,MODEL,";
			String h2 = "SENDER,REQUIRED_QTY,RECEIVED_QTY,MATERIAL_WORKER,MATERIAL_KEEPER,MES_DETAIL_MEMO,WMS_MEMO,FLAG";
			String h3 = " from "+MiddleTableName.MIDDLEDELIVERYDOCDETAIL+" dd";
			String h4 = " WHERE COMPANY = '106' AND FLAG = 2 AND RECEIVED_QTY>0 AND SENDER IS NOT NULL";
			String s1 = " select 1 from wms_pick_ticket_detail pp";
			String s2 = " left join wms_pick_ticket p on p.id = pp.pick_ticket_id left join wms_item i on i.id = pp.item_id";
			String s3 = " where p.related_bill1 = dd.delivery_code and pp.supplier = dd.supplier_code and i.code = dd.material_code and p.sender = dd.sender";
			String s4 = " and pp.picked_quantity_bu > 0 and pp.shipped_quantity_bu < pp.picked_quantity_bu";
			String s5 = " and pp.descrption is null)";
			String s6 = " and dd.mes_detail_memo is null";
			String s7 = " and pp.descrption is not null and pp.descrption = dd.mes_detail_memo)";
			
			WmsBillingManager wmsBillingManager = (WmsBillingManager) applicationContext.getBean("wmsBillingManager");
			String sql = h1+h2+h3+
					h4+s6+" and exists ("+
					s1+s2+s3+s4+s5+
					" union "+
					h1+h2+h3+h4+
					" and exists ("+
					s1+s2+s3+s4+s7;
					
//					" WHERE ID IN (273757,273758)";
			SQLQuery query2 = session.createSQLQuery(sql);
			List<Object[]> objs2 = query2.list();
			//先按发送方汇总
			Map<String,List<Object[]>> detailSenders = senders(objs2,new int[]{
					8
			});
			String sender = null,hashCode = null;
			List<String> hashCodes = null;
			Map<String,Double> detailHashCodeQty = null;
			List<Long> succesPicDetailIds = null;
			Iterator<Entry<String, List<Object[]>>> itertor = detailSenders.entrySet().iterator();
			while(itertor.hasNext()){
				Entry<String, List<Object[]>> entry = itertor.next();
				sender = entry.getKey();
				objs2 = entry.getValue();
				
				int size = objs2.size();
				int j = JavaTools.getSize(size, PAGE_NUMBER);
				for(int k=0;k<j;k++){
					int toIndex = JavaTools.getIndex(k, size, PAGE_NUMBER);
					List<Object[]> ret = JavaTools.getListObj(objs2, k, toIndex, PAGE_NUMBER);
					//再按hashcode汇总(相同发货单下相同物料汇总)
					//DELIVERY_CODE,SUPPLIER_CODE,MATERIAL_CODE,MODEL(CANCLE),SENDER,MES_DETAIL_MEMO==RECEIVED_QTY,HASH_CODE
					Map<String,List<Object[]>> detailKeys = senders(ret,new int[]{
							2,3,4,8,13//7,
					});
					Iterator<Entry<String, List<Object[]>>> itertor1 = detailKeys.entrySet().iterator();
					hashCodes = new ArrayList<String>();
					detailHashCodeQty = new HashMap<String, Double>();
					succesPicDetailIds = new ArrayList<Long>();
					while(itertor1.hasNext()){
						Entry<String, List<Object[]>> entry1 = itertor1.next();
						hashCode = entry1.getKey();
						hashCode = BeanUtils.getFormat(hashCode.split(MyUtils.spilt1));//d[2],d[3],d[4],d[8],d[13]    d[7],
						if(!hashCodes.contains("'"+hashCode+"'")){
							hashCodes.add("'"+hashCode+"'");
						}
						Double RECEIVED_QTY = 0.0;
						for(Object[] o : entry1.getValue()){
							RECEIVED_QTY += Double.valueOf(o[10].toString());
							succesPicDetailIds.add(Long.parseLong(o[0].toString()));
						}
						detailHashCodeQty.put(hashCode, RECEIVED_QTY);//计算发货单明细要发货量
					}
					List<Long> ptdds = commonDao.findByQuery("SELECT pp.id FROM WmsPickTicketDetail pp WHERE"
							+ " pp.hashCode in ("+StringUtils.substringBetween(hashCodes.toString(), "[", "]")+")");
					List<WmsTask> tasks = new ArrayList<WmsTask>();
					if(ptdds!=null && ptdds.size()>0){
						tasks = commonDao.findByQuery("FROM WmsTask t WHERE 1=1 AND t.movedQuantityBU-t.tiredMovedQuantityBU>0"
								+ " AND t.moveDocDetail.relatedId"
								+ " in ("+StringUtils.substringBetween(ptdds.toString(), "[", "]")+")");
					}
					
					sql = "update middle_delivery_doc_detail set FLAG = ?,WMS_MEMO=?,DEAL_TIME = systimestamp"
							+ " where COMPANY = '106' AND ID in "
							+ "("+StringUtils.substringBetween(succesPicDetailIds.toString(), "[", "]")+")";
					int flag = 0;
					String wmsMemo = null;
					String time = JavaTools.format(new Date(), JavaTools.ymd_Hms);
					if(tasks!=null && tasks.size()>0){
						milldleSessionManager.sysPickShip(new Object[]{
								tasks,detailHashCodeQty
						});
						flag = 3;
						wmsMemo = "WMS已读取验收信息"+time;
					}else{
						flag = 7;
						wmsMemo = "WMS发货单还无拣货任务生成"+time;
					}
					//增加日志,记录WMS回写数据的真是信息,近期发现WMS无拣货任务,却成功回写状态3
					wmsBillingManager.initLog("发运签收", "999", 
							flag+"", wmsMemo, StringUtils.substringBetween(succesPicDetailIds.toString(), "[", "]"));
					SQLQuery query = session.createSQLQuery(sql);
					query.setInteger(0,flag);
					query.setString(1,wmsMemo);
					query.executeUpdate();
					System.out.println("发运签收:"+(k+1)+"---"+j);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getLocalizedMessage());
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
	}
	//补发MES签收WMS未发运 3>2 20160318 yc.min
	public void sysPickShipBack(String mes,String item){
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().openSession();
		try {
			String h1 = "select ID";
			String h2 = " from "+MiddleTableName.MIDDLEDELIVERYDOCDETAIL+" dd";
			String h3 = " WHERE COMPANY = '106' AND FLAG = 3 AND RECEIVED_QTY>0 AND SENDER IS NOT NULL";
			String s1 = " select 1 from wms_pick_ticket_detail pp";
			String s2 = " left join wms_pick_ticket p on p.id = pp.pick_ticket_id left join wms_item i on i.id = pp.item_id";
			String s3 = " where p.related_bill1 = dd.delivery_code and pp.supplier = dd.supplier_code and i.code = dd.material_code and p.sender = dd.sender";
			String s4 = " and pp.picked_quantity_bu > 0 and pp.shipped_quantity_bu = 0";
			String s5 = " and pp.descrption is null)";
			String s6 = " and dd.mes_detail_memo is null";
			String s7 = " and pp.descrption is not null and pp.descrption = dd.mes_detail_memo)";
			
			String s8 = " and d.delivery_code = '"+mes+"'";
			String s9 = " and d.material_code = '"+item+"'";
			
			String sql = "update "+MiddleTableName.MIDDLEDELIVERYDOCDETAIL+" d set d.flag = 2,d.wms_memo = 'EDI重新发运' where 1=1";
			sql += " and exists ("+
					" select 1 from ("+
					h1+h2+h3+s6+
					" and exists ("+
					s1+s2+s3+s4+s5+
					" union "+
					h1+h2+h3+
					" and exists ("+
					s1+s2+s3+s4+s7+
					")oo where oo.id = d.id"+
					")";
			if(!StringUtils.isEmpty(mes)){
				sql += s8;
			}
			if(!StringUtils.isEmpty(item)){
				sql += s9;
			}
			SQLQuery query = session.createSQLQuery(sql);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getLocalizedMessage());
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void updateHashCode(){
		List<Object> ptds = commonDao.findByQuery("FROM WmsPickTicketDetail ptd WHERE ptd.expectedQuantityBU>0 AND ptd.shippedQuantityBU=0");// AND ptd.pickTicket.relatedBill1 = 'JAC2016031433075-172'
		if(ptds!=null && ptds.size()>0){
			int size = ptds.size();
			int j = JavaTools.getSize(size, PAGE_NUMBER);
			for(int k=0;k<j;k++){
				int toIndex = JavaTools.getIndex(k, size, PAGE_NUMBER);
				List<Object> ret = JavaTools.getList(ptds, k, toIndex, PAGE_NUMBER);
				milldleSessionManager.updateHashCode(ret);
				System.out.println((k+1)+"---"+j);
			}
		}
	}
	//批量更新MES签收WMS无拣货任务 7>2 20160323 yc.min
	public void updateFlag7(String mes,String item){
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().openSession();
		try {
			String s1 = " and delivery_code = '"+mes+"'";
			String s2 = " and material_code = '"+item+"'";
			String sql = "update middle_delivery_doc_detail set flag = 2 where 1=1 and flag = 7 and received_qty >0";
			if(!StringUtils.isEmpty(mes)){
				sql += s1;
			}
			if(!StringUtils.isEmpty(item)){
				sql += s2;
			}
			SQLQuery query = session.createSQLQuery(sql);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getLocalizedMessage());
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
	}
	/**物料编码a[0]，供应商编码a[1]*/
	@SuppressWarnings("unchecked")
	private Object[] dbToMap(WmsOrganization company,List<Object[]> objs,int[] a){
		List<String> itemCodes = new ArrayList<String>();
		List<String> supCodes = new ArrayList<String>();
		for(Object[] o : objs){
			if(!itemCodes.contains("'"+o[a[0]]+"'")){
				itemCodes.add("'"+o[a[0]]+"'");
			}
			if(!supCodes.contains("'"+o[a[1]]+"'")){
				supCodes.add("'"+o[a[1]]+"'");
			}
		}
		
		Map<String,WmsItem> itemMaps = new HashMap<String, WmsItem>();
		Map<String,WmsPackageUnit> puMaps = new HashMap<String,WmsPackageUnit>();
		int size = itemCodes.size();
		int j = JavaTools.getSize(size, PAGE_NUMBER);
		for(int k=0;k<j;k++){
			int toIndex = JavaTools.getIndex(k, size, PAGE_NUMBER);
			List<String> ret = JavaTools.getListStr(itemCodes, k, toIndex, PAGE_NUMBER);
			
			List<WmsItem> items = commonDao.findByQuery("FROM WmsItem i WHERE i.company.id =:companyId"
					+ " AND i.code in("+StringUtils.substringBetween(ret.toString(), "[", "]")+")", 
					new String[]{"companyId"}, new Object[]{company.getId()});
			for(WmsItem i:items){
				itemMaps.put(i.getCode(), i);
			}
			items.clear();
			
			List<Object[]> pus = commonDao.findByQuery("SELECT pu.id,pu.item.code FROM WmsPackageUnit pu"
					+ " WHERE 1=1 AND pu.item.company.id =:companyId AND pu.lineNo = 1"//根据货主过滤
					+ " AND pu.item.code in("+StringUtils.substringBetween(ret.toString(), "[", "]")+")",
					new String[]{"companyId"},new Object[]{company.getId()});
			WmsPackageUnit pu = null;
			for(Object[] o : pus){
				pu = commonDao.load(WmsPackageUnit.class, Long.parseLong(o[0].toString()));
				puMaps.put(o[1].toString(),pu);
			}
			pus.clear();
		}
		itemCodes.clear();
		List<WmsOrganization> suppliers = commonDao.findByQuery("FROM WmsOrganization sup WHERE sup.code "
				+ "in ("+StringUtils.substringBetween(supCodes.toString(), "[", "]")+")");
		Map<String,WmsOrganization> supMaps = new HashMap<String, WmsOrganization>();
		for(WmsOrganization o : suppliers){
			supMaps.put(o.getCode(), o);
		}
		supCodes.clear();suppliers.clear();
		return new Object[]{
				itemMaps,puMaps,supMaps
		};
	}
	@SuppressWarnings("unchecked")
	private Map<String,String[]> senderMap(){
		List<Object[]> mces = commonDao.findByQuery("SELECT m.sender,m.warehouse.id,m.company.id"
				+ " FROM MiddleCompanyExtends m WHERE 1=1");
		if(mces==null || mces.size()<=0){
			throw new BusinessException("仓库货主接口映射关系表未维护信息");
		}
		Map<String,String[]> senderMap = new HashMap<String, String[]>();//sender,[warehouse,company]
		for(Object[] mce : mces){
			senderMap.put(mce[0].toString(), new String[]{
				mce[1].toString(),mce[2].toString()
			});
		}
		mces.clear();
		return senderMap;
	}
	private Map<String,List<Object[]>> senders(List<Object[]> objs,int[] a){
		String key = null;
		Map<String,List<Object[]>> senders = new HashMap<String, List<Object[]>>();
		List<Object[]> tempObj = null;
		for(Object[] o : objs){
			key = "";
			for (int i = 0; i < a.length; i++) {
				key += o[a[i]]+MyUtils.spilt1;
			}
			key = StringUtils.substringBeforeLast(key,MyUtils.spilt1);
			if(senders.containsKey(key)){
				tempObj = senders.get(key);
			}else{
				tempObj = new ArrayList<Object[]>();
			}
			tempObj.add(o);
			senders.put(key, tempObj);
		}
		return senders;
	}
	@SuppressWarnings("unchecked")
	public void readMidQuality(){
		Map<String,String[]> senderMap = senderMap();
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)
				applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().openSession();
		try {
			//CODE-质检单CODE,QUALITY_STATUS-质检状态,PROCESS_STATE-工艺状态,SCRAP_QTY-报损数量,MES_QUALITY_CODE-MES质检单号
			String sql = "select ID,COMPANY,CODE,ASN_CODE,"
					+ "SUPPLIER_CODE,MATERIAL_CODE,QUALITY_STATUS,PROCESS_STATE,"
					+ "SEND_QTY,BACK_QTY,SCRAP_QTY,MES_MEMO,"
					+ "SENDER,MES_QUALITY_CODE from "+MiddleTableName.MIDDLEQUALITYTESTING
					+ " where COMPANY = '106' AND FLAG = 2 AND SENDER IS NOT NULL";
//					+ " where CODE= 'JAC_FDJZJ150826000003'";
			SQLQuery query = session.createSQLQuery(sql);
			List<Object[]> objs = query.list();
			//按发送方汇总
			Map<String,List<Object[]>> detailSenders = senders(objs,new int[]{
					12
			});
			//查询质检状态映射表
			sql = "select COMPANY,SENDER,WMS_NAME,INTER_FACE_NAME,SCRAP_NAME,MEMO from "+MiddleTableName.MIDDLE_QUALITY_STATUS;
			query = session.createSQLQuery(sql);
			List<Object[]> status = query.list();
			//按发送方汇总(匹配所属的客户,以便正确的获取质检状态)
			Map<String,List<Object[]>> statusSenders = senders(status,new int[]{
					1
			});
			
			String sender = null,moveCode=null;
			String[] wearhouseCompany=null;
			Iterator<Entry<String, List<Object[]>>> itertor = detailSenders.entrySet().iterator();
			while(itertor.hasNext()){
				List<String> nullSenders = new ArrayList<String>();//未匹配到发送方-12
				List<String> nullItemStatus = new ArrayList<String>();//WMS未维护货品状态信息-13
				List<Long> nullMoves = new ArrayList<Long>();//无法匹配上质检单的数据-11
				List<String> nullmidStatus = new ArrayList<String>();//质检状态映射表未维护发送方数据-14
				List<Long> errStatus = new ArrayList<Long>();//质检状态不能正确匹配WMS-15
				List<Long> errBackQty = new ArrayList<Long>();//返回数量有误-16
				List<Long> succIds = new ArrayList<Long>();//返回正常执行完毕数据-3
				
				Entry<String, List<Object[]>> entry = itertor.next();
				sender = entry.getKey();
				if(!senderMap.containsKey(sender)){
					nullSenders.add(sender);//未匹配到发送方
					continue;
				}
				wearhouseCompany = senderMap.get(sender);//[warehouse,company]
				List<String> itemStatus = commonDao.findByQuery("SELECT t.name FROM WmsItemState t"
						+ " WHERE t.company.id =:companyId GROUP BY t.name", "companyId", Long.parseLong(wearhouseCompany[1]));
				if(itemStatus==null || itemStatus.size()<=0){
					nullItemStatus.add(sender);//WMS未维护货品状态信息
					continue;
				}
				if(!statusSenders.containsKey(sender)){
					nullmidStatus.add(sender);//质检状态映射表未维护发送方数据
					continue;
				}
				Map<String,String[]> midStatus = new HashMap<String, String[]>();
				for(Object[] o : statusSenders.get(sender)){
					midStatus.put(o[3].toString(), 
							new String[]{
						o[2].toString(),o[4].toString()
					});//INTER_FACE_NAME,[WMS_NAME,SCRAP_NAME] 
					//MES回写状态,[wms维护状态(对应BACK_QTY>0回写wms),wms维护报废状态(对应SCRAP_QTY>0回写wms)]
				}
				
				objs = entry.getValue();
				//按质检单号汇总CODE
				Map<String,List<Object[]>> movelKeys = senders(objs,new int[]{
						2
				});
				Iterator<Entry<String, List<Object[]>>> itertor1 = movelKeys.entrySet().iterator();
				while(itertor1.hasNext()){
					Entry<String, List<Object[]>> entry1 = itertor1.next();
					moveCode = entry1.getKey();
					objs = entry1.getValue();
					//mes质检是一条物料返回一次,同一个质检单号下的明细会存在多次返回
					List<WmsMoveDocDetail> mdds = commonDao.findByQuery("FROM WmsMoveDocDetail mm"
							+ " WHERE mm.moveDoc.code =:moveDocCode"
//							+ " AND (mm.moveDoc.transStatus >:transStatus OR mm.moveDoc.transStatus <:transStatus)", 
							+ " AND mm.processQuantityBU=0",
							new String[]{"moveDocCode"}, new Object[]{moveCode});
					if(mdds.size()>0){
						Object[] backObj = milldleSessionManager.readMidQuality(objs, mdds, midStatus, itemStatus);
						errStatus.addAll((List<Long>) backObj[0]);
						errBackQty.addAll((List<Long>) backObj[1]);
						succIds.addAll((List<Long>) backObj[2]);
					}else{
						for(Object[] o : objs){
							nullMoves.add(Long.parseLong(o[0].toString()));
						}
						continue;
					}
				}
				//回写-WMS读取完毕,状态置为3
				if(succIds!=null&&succIds.size()>0){
					int size = succIds.size();
					int j = JavaTools.getSize(size, PAGE_NUMBER);
					for(int k = 0 ; k<j ; k++){
						int index = JavaTools.getIndex(k, size, PAGE_NUMBER);
						List<Long> ids = JavaTools.getListLong(succIds, k, index, PAGE_NUMBER);
						sql = "update "+MiddleTableName.MIDDLEQUALITYTESTING+" set flag = 3,wms_memo = 'WMS读取完毕',DEAL_TIME = systimestamp"
								+ " where id in ("+StringUtils.substringBetween(ids.toString(), "[", "]")+")";
						session.createSQLQuery(sql).executeUpdate();
					}
				}
				//回写-无法匹配上质检单的数据,状态置为11-nullMoves
				if(nullMoves!=null&&nullMoves.size()>0){
					int size = nullMoves.size();
					int j = JavaTools.getSize(size, PAGE_NUMBER);
					for(int k = 0 ; k < j ; k++){
						int index = JavaTools.getIndex(k, size, PAGE_NUMBER);
						List<Long> ids = JavaTools.getListLong(nullMoves, k, index, PAGE_NUMBER);
						sql = "update "+MiddleTableName.MIDDLEQUALITYTESTING+" set flag = 11,wms_memo = '无法匹配质检单号',DEAL_TIME = systimestamp"
								+ " where id in ("+StringUtils.substringBetween(ids.toString(), "[", "]")+")";
						session.createSQLQuery(sql).executeUpdate();
					}
					nullMoves.clear();
				}
				//回写-无法匹配发送方,状态置为12-nullSenders
				if(nullSenders!=null&&nullSenders.size()>0){
					List<String> ss = JavaTools.charList(nullSenders);
					sql = "update "+MiddleTableName.MIDDLEQUALITYTESTING+" set flag = 12,wms_memo = '无法匹配发送方',DEAL_TIME = systimestamp"
							+ " where SENDER in ("+StringUtils.substringBetween(ss.toString(), "[", "]")+")";
					session.createSQLQuery(sql).executeUpdate();
				}
				//回写-WMS未维护货品状态信息,状态置为13-nullItemStatus
				if(nullItemStatus!=null&&nullItemStatus.size()>0){
					List<String> ss = JavaTools.charList(nullItemStatus);
					sql = "update "+MiddleTableName.MIDDLEQUALITYTESTING+" set flag = 13,wms_memo = 'WMS未维护货品状态信息',DEAL_TIME = systimestamp"
							+ " where SENDER in ("+StringUtils.substringBetween(ss.toString(), "[", "]")+")";
					session.createSQLQuery(sql).executeUpdate();
				}
				//回写-质检状态映射表未维护发送方数据,状态置为14-nullmidStatus
				if(nullmidStatus!=null&&nullmidStatus.size()>0){
					List<String> ss = JavaTools.charList(nullmidStatus);
					sql = "update "+MiddleTableName.MIDDLEQUALITYTESTING+" set flag = 14,wms_memo = '质检状态映射表未维护发送方数据',DEAL_TIME = systimestamp"
							+ " where SENDER in ("+StringUtils.substringBetween(ss.toString(), "[", "]")+")";
					session.createSQLQuery(sql).executeUpdate();
				}
				//回写-质检状态不能正确匹配WMS-15
				if(errStatus!=null&&errStatus.size()>0){
					int size = errStatus.size();
					int j = JavaTools.getSize(size, PAGE_NUMBER);
					for(int k = 0 ; k < j ; k++){
						int index = JavaTools.getIndex(k, size, PAGE_NUMBER);
						List<Long> ids = JavaTools.getListLong(errStatus, k, index, PAGE_NUMBER);
						sql = "update "+MiddleTableName.MIDDLEQUALITYTESTING+" set flag = 15,wms_memo = '质检状态不能正确匹配WMS',DEAL_TIME = systimestamp"
								+ " where id in ("+StringUtils.substringBetween(ids.toString(), "[", "]")+")";
						session.createSQLQuery(sql).executeUpdate();
					}
				}
				//回写-返回数量有误-16
				if(errBackQty!=null&&errBackQty.size()>0){
					int size = errBackQty.size();
					int j = JavaTools.getSize(size, PAGE_NUMBER);
					for(int k = 0 ; k < j ; k++){
						int index = JavaTools.getIndex(k, size, PAGE_NUMBER);
						List<Long> ids = JavaTools.getListLong(errBackQty, k, index, PAGE_NUMBER);
						sql = "update "+MiddleTableName.MIDDLEQUALITYTESTING+" set flag = 16,wms_memo = '返回数量有误'"
								+ " where id in ("+StringUtils.substringBetween(ids.toString(), "[", "]")+")";
						session.createSQLQuery(sql).executeUpdate();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getLocalizedMessage());
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void sysASNSrm(){
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().openSession();
		try {
			String sql = "select ASNNO,PONO,VENDORCODE,to_char(REQDATE,'yyyy-MM-dd'),SENDQTY,STORECODE,UNIT,MATERIALCODE,T$OUT"
					+ " from "+MiddleTableName.srm_erp_v_Poasn106+" where T$OUT = 'Y'";
			SQLQuery query = session.createSQLQuery(sql);
			List<Object[]> objs = query.list();
			if(objs!=null && objs.size()>0){
				//存放成功的信息
				List<String> succes = new ArrayList<String>();
				//存放失败的信息
				List<String> errors = new ArrayList<String>();
				//
				WmsOrganization company = null;
				List<WmsOrganization> companys = commonDao.findByQuery("FROM WmsOrganization c WHERE c.code =:code"
						+ " AND c.beCompany = true", 
						new String[]{"code"}, new Object[]{"FDJ"});
				if(companys!=null && companys.size()>0){
					company = companys.get(0);
				}else{
					company = EntityFactory.getEntity(WmsOrganization.class); 
					company.setBeCompany(true);
					company.setCode("FDJ");
					company.setDescription("系统创建");
					company.setName("安徽江淮发动机有限公司");
					company.setStatus(BaseStatus.ENABLED);
					commonDao.store(company);
				}
				WmsBillType billType = null;
				List<WmsBillType> billTypes = commonDao.findByQuery("FROM WmsBillType b WHERE b.code =:code AND b.type =:type", 
						new String[]{"code","type"}, new Object[]{"IN-001",TypeOfBill.RECEIVE});
				if(billTypes!=null && billTypes.size()>0){
					billType = billTypes.get(0);
				}
				if(billType==null){
					billType = EntityFactory.getEntity(WmsBillType.class);
					billType.setCode("IN-001");
					billType.setName("正常入库单");
					billType.setStatus(BaseStatus.ENABLED);
					billType.setCompany(company);
					billType.setBeInner(true);
					billType.setType(TypeOfBill.RECEIVE);
					billType.setDescription("系统创建");
					commonDao.store(billType);
				}
				WmsWarehouse warehouse = null;
				List<WmsWarehouse> ws = commonDao.findByQuery("FROM WmsWarehouse w WHERE w.code =:code", 
						new String[]{"code"}, new Object[]{"JAC_FDJ"});
				if(ws!=null && ws.size()>0){
					warehouse = ws.get(0);
				}else{
					warehouse = EntityFactory.getEntity(WmsWarehouse.class);
					warehouse.setCode("JAC_FDJ");
					warehouse.setName("发动机仓库");
					warehouse.setDescription("系统创建");
					warehouse.setStatus(BaseStatus.ENABLED);
					commonDao.store(warehouse);
				}
				Object[] dbToMap = dbToMap(company, objs,new int[]{
						7,2
				});
				Map<String,WmsItem> itemMaps = (Map<String, WmsItem>) dbToMap[0];
				Map<String,WmsPackageUnit> puMaps = (Map<String, WmsPackageUnit>) dbToMap[1];
				Map<String,WmsOrganization> supMaps = (Map<String, WmsOrganization>) dbToMap[2];
				
				
				WmsOrganization supplier = null;
				String[] keys = null;
				//将结果集按照ASNNO+PONO+REQDATE+VENDORCODE分组
				Map<String,List<Object[]>> map = senders(objs,new int[]{
						0,1,2,3
				});
				Iterator<Entry<String, List<Object[]>>> interator = map.entrySet().iterator();
				while(interator.hasNext()){
					Entry<String, List<Object[]>> entry = interator.next();
//					System.out.println(entry.getKey());
					//保存数据到ASN.....
					keys = entry.getKey().split(MyUtils.spilt1);//ASNNO+PONO+SUPCODE+REQDATE
					supplier = supMaps.get(keys[2]);
					if(supplier==null){
						errors.add(entry.getKey());
						continue;
					}
					Object[] back = milldleSessionManager.sysASNSrm(entry.getKey(), entry.getValue(), billType,company, warehouse, supplier, itemMaps, puMaps);
					String errorkey = (String) back[0];
					if(errorkey!=null){
						errors.add(errorkey);
						continue;
					}else{
						succes.add(entry.getKey());//SH0951664#151003869#L20077#2015-03-01
					}
				}
				if(succes.size()>0){
					updateASNSrm(succes, session, "S");
				}
				if(errors.size()>0){
					updateASNSrm(errors, session, "N");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getLocalizedMessage());
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
	}
	private void updateASNSrm(List<String> values,Session session,String status){
		String[] keys = null;
		for(String key : values){
			keys = key.split(MyUtils.spilt1);
			String sql = "update "+MiddleTableName.srm_erp_v_Poasn106+" set T$OUT = ? where ASNNO = ? and PONO = ? and VENDORCODE = ? and to_char(REQDATE,'yyyy-MM-dd') =? and T$OUT = 'Y'";
			SQLQuery query = session.createSQLQuery(sql);
			query.setString(0, status);
			query.setString(1, keys[0]);
			query.setString(2, keys[1]);
			query.setString(3, keys[2]);
			query.setString(4, keys[3]);
			query.executeUpdate();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void readLFCSBillDetail(){
		
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().openSession();
		try {
			String sql = "select ID,BILL_ID,BILL_CODE,BE_INCOME,EXPENSE,GROUP_NO1,GROUP_NO2,HAPPEN_DATE," +
					" FEE_CATEGORY_NAME,SUPCODE,SUPNAME,ITEMCODE,BILLING_MODEL,WAREHOUSECODE_CONTACT_POCODE," +
					" QTY_AMOUNT,FIXED_PRICE,CREATED_TIME,STATUS,FLAG "+
					"from "+MiddleTableName.FDJ_BMS_FEE_DATA_VIEW+
					" WHERE flag = 0 and EXPENSE is not null";//and BILL_ID is not null
			SQLQuery selectQuery = session.createSQLQuery(sql);
			List<Object[]> billObjList = selectQuery.list();
			int size = billObjList.size();
			int circulation = JavaTools.getSize(size, PAGE_NUMBER);
			List<Object> sucessFeeData= null;
			List<Object> failFeeData = null;
			Map<String, List<Object>>totalFeeDataMap=null;
			Object[] failDataStr = null;
			int success=0,fail =0;
			for(int index=0;index<circulation;index++){
				int toIndex = JavaTools.getIndex(index, size, PAGE_NUMBER);
				List<Object[]> feedDataList = JavaTools.getListObj(billObjList, index, toIndex, PAGE_NUMBER);
				totalFeeDataMap = milldleSessionManager.getFeedIdsAndInsertBillDetail(feedDataList);
				sucessFeeData = totalFeeDataMap.get("success");
				failFeeData = totalFeeDataMap.get("error");
				String time = JavaTools.format(new Date(), JavaTools.ymd_Hms);
				if(null !=  sucessFeeData){
					success+=sucessFeeData.size();
					for(Object successId : sucessFeeData){
						sql = "update "+MiddleTableName.BMS_FEE_DATA+" set d_refer30 = 1 , refer100 = '"+time+"' where id = "+Long.valueOf(successId.toString());
						SQLQuery updateQuery = session.createSQLQuery(sql);
						updateQuery.executeUpdate();
					}
				}
				if(null !=  failFeeData){
					fail += failFeeData.size();
					for(Object failData : failFeeData){
						failDataStr = failData.toString().split("#");
						sql = "update "+MiddleTableName.BMS_FEE_DATA+" set d_refer30 = 2 , refer100 = '"+failDataStr[1]+"/"+time+"' where id = "+Long.valueOf(failDataStr[0].toString());
						SQLQuery updateQuery = session.createSQLQuery(sql);
						updateQuery.executeUpdate();
					}
				}
			}
			LocalizedMessage.addMessage("成功"+success+",失败"+fail);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getLocalizedMessage());
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
		
	
	}
	//MES触发要货单
	public void mesMisPick(){
//		Boolean beEmpty = wmsBusiness(WmsBusiness.MES_MIS_INVENTORY, WmsBusiness.MES);
		int num = dataIsNull();
		if(num>0){
			Integer flag = 9;
			//锁定这批数据
			updateMesOrderNine(flag);
			//读取状态为flag的中间表
			Map<String,String> mesPick = mesPick(flag);
			//获取最新批次和批次日期
			Integer lot = maxLot();
			Date lastTime = lastTime(lot);
			//查找结存日期之后收货完成且状态符合的ASN
			Map<String,String> asnMap = asnMap(lastTime);
			//找上一个最新批次[初始化期初量]
			List<Object[]> obj1 = initObj(mesPick,asnMap,lot);//供应商编码,供应商名称,物料编码,物料名称,期初量,入库量,MES要货量
			//结果保存
			int size = obj1.size();
			int j = JavaTools.getSize(size, PAGE_NUMBER);
			for(int k = 0 ; k < j ; k++){
				System.out.println(j+"..."+(k+1));
				int index = JavaTools.getIndex(k, size, PAGE_NUMBER);
				List<Object[]> objs = JavaTools.getListObj(obj1, k, index, PAGE_NUMBER);
				milldleSessionManager.initMesMisInventory(objs, lot+1);
			}
			//标记读取完毕
			updateMesOrderOne(flag);
			//最后释放
//			releaseWmsBusiness(WmsBusiness.MES_MIS_INVENTORY, WmsBusiness.MES);
		}
		
	}
	/**找上一个最新批次[初始化期初量]*/
	@SuppressWarnings("unchecked")
	private List<Object[]> initObj(Map<String,String> mesPick,Map<String,String> asnMap,Integer lot){
		String key = "",mesQty = "0",asnQty = "0";
		List<Object[]> obj1 = new ArrayList<Object[]>();//供应商编码,供应商名称,物料编码,物料名称,期初量,入库量,MES要货量
		Double initQty = 0D;
		String hql = "FROM MesMisInventory m WHERE m.lot =:lot AND (m.calQuantity>0 OR m.calQuantity<0)";
		List<MesMisInventory> oldMes = commonDao.findByQuery(hql, new String[]{"lot"}, new Object[]{lot});
		if(oldMes!=null && oldMes.size()>0){
			for(MesMisInventory m : oldMes){
				key = m.getSupCode()+MyUtils.spilt1+m.getSupName()+MyUtils.spilt1+m.getItemCode()+MyUtils.spilt1+m.getItemName();
				mesQty = mesPick.get(key);//新的mes要货量
				asnQty = asnMap.get(key);//新的asn入库量
				initQty = m.getCalQuantity();//实际结存量作为期初量
				obj1.add(new Object[]{
						m.getSupCode(),m.getSupName(),m.getItemCode(),m.getItemName(),initQty,asnQty==null?"0":asnQty,mesQty==null?"0":mesQty
				});
				mesPick.remove(key);
				asnMap.remove(key);
			}oldMes.clear();
		}
		if(mesPick.size()>0){//中间表存在上一批结存中不包含的数据
			String[] ss = null;
			Iterator<Entry<String, String>> iter = mesPick.entrySet().iterator();
			while(iter.hasNext()){
				Entry<String, String> entry = iter.next();
				key = entry.getKey();
				mesQty = entry.getValue();//新的mes要货量
				asnQty = asnMap.get(key);//新的asn入库量
				initQty = 0D;//实际结存量作为期初量
				ss = key.split(MyUtils.spilt1);
				obj1.add(new Object[]{
						ss[0],ss[1],ss[2],ss[3],initQty,asnQty==null?"0":asnQty,mesQty
				});
				asnMap.remove(key);
			}mesPick.clear();
		}
		if(asnMap.size()>0){//asn存在新的数据,初始化asn入库量即可
			String[] ss = null;
			Iterator<Entry<String, String>> iter = asnMap.entrySet().iterator();
			while(iter.hasNext()){
				Entry<String, String> entry = iter.next();
				key = entry.getKey();
				mesQty = "0";//新的mes要货量
				asnQty = entry.getValue();//新的asn入库量
				initQty = 0D;//实际结存量作为期初量
				ss = key.split(MyUtils.spilt1);
				obj1.add(new Object[]{
						ss[0],ss[1],ss[2],ss[3],initQty,asnQty,mesQty
				});
			}asnMap.clear();
		}
		return obj1;
	}
	/**查找结存日期之后收货完成且状态符合的ASN*/
	private Map<String,String> asnMap(Date lastTime){
		Map<String,String> asnMap = new HashMap<String, String>();
		String supCode = "",supName = "",itemCode = "",itemMame = "",asnQty = "0",key = "";
		StringBuffer sb = new StringBuffer();
		sb.append("'").append("-").append("'")
		.append(",")
		.append("'").append("待检").append("'");
		String sql = "select sup.code as supplier_code,sup.name as supplier_name,item.code as material_code,item.name as material_name,sum(i.quantity_bu) as asnQty "+ 
				" from wms_inventory i"+ 
				" left join wms_item_key ik on ik.id = i.item_key_id"+
		" left join wms_item item on item.id = ik.item_id"+
		" left join wms_organization sup on sup.id = ik.supplier_id" +
		" left join wms_location l on l.id = i.location_id"+
		" where i.status in ("+sb+")" +
		" and to_char(ik.storage_date,'yyyy-MM-dd') >= ?"+//'2016-07-02'---->=
		" and i.quantity_bu>0 and l.type in ('STORAGE','SHIP')"+
		" and exists (" +
		" select 1 from wms_task t" +
		" where t.type = 'MV_PUTAWAY' and to_char(t.pick_time,'yyyy-MM-dd  HH24:mi:ss') >= ?" +
		" and t.original_billcode = ik.soi and t.item_key_id = i.item_key_id"+
		" )"+//'2016-07-02 00:00:00'
		" group by sup.code,sup.name,item.code,item.name"+
		" union"+
		" select sup.code as supplier_code,sup.name as supplier_name,l.item_code as material_code,l.item_name as material_name,sum(l.quantity_bu) as asnQty"+
		" from wms_inventory_log l"+ 
		" left join wms_organization sup on sup.id = l.supplier_id"+ 
		" where l.log_type='SHIPPING'"+ 
		" and l.inventory_status in ("+sb+")"+
		" and to_char(l.storage_date,'yyyy-MM-dd') >= ?"+//'2016-07-02' ---->=
		" and to_char(l.created_time,'yyyy-MM-dd HH24:mi:ss') >= ? "+//'2016-07-02 00:00:00'
		" group by l.item_code,l.item_name,sup.code,sup.name";
		String time = JavaTools.format(lastTime, JavaTools.y_m_d);
		String timemill = JavaTools.format(lastTime, JavaTools.dmy_hms);
		System.out.println("查找结存日期之后收货完成且状态符合的ASN:"+"\n"+sql+"\n"+timemill);
		List asn = jdbcTemplate.queryForList(sql, new Object[]{time,time,timemill,timemill});
		if(asn!=null && asn.size()>0){
			Iterator iasn = asn.iterator();
			while(iasn.hasNext()){
				Map m = (Map) iasn.next();
				supCode = m.get("supplier_code")==null?"-": m.get("supplier_code").toString();
				supName = m.get("supplier_name")==null?"-": m.get("supplier_name").toString();
				itemCode = m.get("material_code")==null?"-": m.get("material_code").toString();
				itemMame = m.get("material_name")==null?"-": m.get("material_name").toString();
				asnQty = m.get("asnQty")==null?"0": m.get("asnQty").toString();
				key = supCode+MyUtils.spilt1+supName+MyUtils.spilt1+itemCode+MyUtils.spilt1+itemMame;
				asnMap.put(key, asnQty);
			}asn.clear();
		}
		return asnMap;
	}
	/**获取最新批次日期*/
	private Date lastTime(Integer lot){
		Date lastTime = new Date();//JavaTools.stringToDate("2016-07-02")
		String sql = "select m.lot_date from "+MesMisInventory.tableName+" m where m.lot = ? and ROWNUM =1";
		List list = jdbcTemplate.queryForList(sql, new Object[]{lot});
		if(list!=null && list.size()>0){
			Iterator i = list.iterator();
			while(i.hasNext()){
				Map m =(Map) i.next();
				lastTime = (Date) m.get("lot_date");
			}
		}
		return lastTime;
	}
	/**读取状态为flag的中间表*/
	private Map<String,String> mesPick(Integer flag){
		Map<String,String> mesPick = new HashMap<String, String>();
		String supCode = "",supName = "",itemCode = "",itemMame = "",mesQty = "0",key = "";
		String sql = "select m.supplier_code,m.supplier_name,m.material_code,m.material_name,sum(m.required_qty) as required_qty" + 
				" from "+MiddleTableName.MIDDLE_MES_ORDER+" m" +
				" where m.flag = "+flag+" and m.sender = 'FDJ' and m.company = '106' and m.required_qty>0" +
				" group by m.supplier_code,m.supplier_name,m.material_code,m.material_name" +
				" order by m.supplier_code,m.material_code";
		List mes = jdbcTemplate.queryForList(sql);
		Iterator iMes = mes.iterator();
		while(iMes.hasNext()){
			Map m = (Map) iMes.next();
			supCode = m.get("supplier_code")==null?"-": m.get("supplier_code").toString();
			supName = m.get("supplier_name")==null?"-": m.get("supplier_name").toString();
			itemCode = m.get("material_code")==null?"-": m.get("material_code").toString();
			itemMame = m.get("material_name")==null?"-": m.get("material_name").toString();
			mesQty = m.get("required_qty")==null?"0": m.get("required_qty").toString();
			key = supCode+MyUtils.spilt1+supName+MyUtils.spilt1+itemCode+MyUtils.spilt1+itemMame;
			mesPick.put(key, mesQty);
		}mes.clear();
		return mesPick;
	}
	/**锁定这批数据状态为9*/
	private void updateMesOrderNine(Integer flag){
		String sql = "update "+MiddleTableName.MIDDLE_MES_ORDER+" m set m.flag = " +flag+
				" where m.flag = 0 and m.sender = 'FDJ' and m.company = '106' and m.required_qty>0";
		jdbcTemplate.execute(sql);
	}
	/**标记读取完毕*/
	private void updateMesOrderOne(Integer flag){
		String sql = "update "+MiddleTableName.MIDDLE_MES_ORDER+" m set m.flag = 1," +
		 		"m.wms_memo = to_char(systimestamp,'yyyy-MM-dd hh24:mi:ss')" +
				" where m.flag = " +flag+" and m.sender = 'FDJ' and m.company = '106' and m.required_qty>0";
		jdbcTemplate.execute(sql);
	}
	/**统计是否存在可触发接口数据*/
	private Integer dataIsNull(){
		String sql = "select count(*) from "+MiddleTableName.MIDDLE_MES_ORDER+" m" +
				" WHERE  m.flag = 0 and m.sender = 'FDJ' and m.company = '106' and m.required_qty>0";
		int num = jdbcTemplate.queryForInt(sql);
		return num;
	}
	/**获取最新的lot*/
	@SuppressWarnings("unchecked")
	private Integer maxLot(){
		String hql = "select max(m.lot) from MesMisInventory m";
		List<Integer> lots = commonDao.findByQuery(hql);
		if(lots!=null && lots.size()>0){
			if(lots.get(0)==null){
				return 0;
			}
			return lots.get(0);
		}
		return 0;
	}
	public void mesDisInitImport(File file){
		String name = file.getName();
		if (file == null) {
			throw new BusinessException("file_not_found");
		} else if (!name.substring(name.lastIndexOf(".") + 1,
				name.lastIndexOf(".") + 4).equals("xls")) {
			throw new BusinessException("this.file.error");
		}
		Sheet sheet = null;
		@SuppressWarnings("unused")
		Sheet[] sheets = null;
		try {
			Workbook wb = Workbook.getWorkbook(new FileInputStream(file));
			sheet = wb.getSheet(0);
			sheets = wb.getSheets();// 获取所有的sheet  
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int rowNum = sheet.getRows();
		List<Object[]> objs = new ArrayList<Object[]>();
		for (int i = 1; i < rowNum; i++) {//含头信息
			String supCode = sheet.getCell(0,i).getContents();
			String supName = sheet.getCell(1,i).getContents();
			String itemCode = sheet.getCell(2,i).getContents();
			String itemName = sheet.getCell(3,i).getContents();
			String initQty = sheet.getCell(4,i).getContents();
			objs.add(new Object[]{//供应商编码,供应商名称,物料编码,物料名称,期初量,入库量,MES要货量
					supCode,supName,itemCode,itemName,initQty,0,0
			});
		}
		int size = objs.size();
		if(size>0){
			String sql = "truncate table "+MesMisInventory.tableName;
			jdbcTemplate.execute(sql);
			sql = "update "+MiddleTableName.MIDDLE_MES_ORDER+" m set m.flag = 8 where m.flag = 0";
			jdbcTemplate.execute(sql);
			int j = JavaTools.getSize(size, PAGE_NUMBER);
			for(int k = 0 ; k < j ; k++){
				System.out.println(j+"..."+(k+1));
				int index = JavaTools.getIndex(k, size, PAGE_NUMBER);
				List<Object[]> obj = JavaTools.getListObj(objs, k, index, PAGE_NUMBER);
				milldleSessionManager.initMesMisInventory(obj, 1);
			}
		}
		
	}
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	public void invokeMethod(String type,String subscriber) throws Exception{
		String managerName =  StringUtils.substringBefore(subscriber.trim(),".");;
		String methodName = StringUtils.substringAfter(subscriber.trim(),".");
		Object manager = applicationContext.getBean(managerName);
		Boolean isError = Boolean.FALSE;
		try {
			Method method = manager.getClass().getMethod(methodName, new Class[]{});
			method.invoke(manager,new Object[]{});
		} catch (IllegalArgumentException e) {
			isError = Boolean.TRUE;
			e.printStackTrace();
		} catch (SecurityException e) {
			isError = Boolean.TRUE;
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			isError = Boolean.TRUE;
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			isError = Boolean.TRUE;
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			isError = Boolean.TRUE;
			e.printStackTrace();
		}
	}
	public void thornTaskMethod(String type,String taskIds) throws Exception{
		String[] ids = taskIds.split(",");
		for(String id : ids){
			Task task = commonDao.load(Task.class, Long.parseLong(id.trim()));
			if(task!=null){
				doHandleSubscriber(task.getSubscriber(),task.getMessageId());
			}
		}
	}
	protected void doHandleSubscriber(String type,Long id) throws Exception{
		String managerName = StringUtils.substringBefore(type,".");
		String methodName = StringUtils.substringAfter(type,".");
		Object[] params = new Object[]{id};
		Object manager = applicationContext.getBean(managerName);
		Assert.notNull(manager,type + " message not found managerName is not find " + managerName);
		Method method = getExactlyMethod(manager,methodName,params);
		Assert.notNull(method,type + " message not found methodName is not find  " + methodName);
		Object obj = method.invoke(manager,params);
		if(!(null == obj || "".equals(obj.toString()))){
			System.out.println("--------------managerName="+managerName+",id="+id+",methodName="+methodName+",type="+type);
		}
	}
	private Method getExactlyMethod(Object manager, String methodName, Object...args) throws NoSuchMethodException {
		logger.warn("The args passing to  Manager [" + manager + "] method [" + methodName + "] contains null arg(s)");
		for(Method method : manager.getClass().getMethods()){
			if(method.getName().equals(methodName) && method.getParameterTypes().length == args.length){
				return method;
			}
		}
		return null;
	}
}
