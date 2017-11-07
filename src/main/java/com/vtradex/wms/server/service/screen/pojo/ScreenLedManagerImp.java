package com.vtradex.wms.server.service.screen.pojo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jxl.format.Alignment;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTransactionManager;

import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.service.GlobalParamUtils;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.screen.ScreenLedManager;
import com.vtradex.wms.server.service.screen.ScreenTable;
import com.vtradex.wms.server.utils.EdiTaskMethod;
import com.vtradex.wms.server.utils.EdiTaskType;
import com.vtradex.wms.server.utils.MyUtils;
import com.xlsTools.XlsUtils;

public class ScreenLedManagerImp extends DefaultBaseManager implements ScreenLedManager{
	private JdbcTemplate jdbcTemplate;
	protected WmsInventoryManager wmsInventoryManager;
	public ScreenLedManagerImp(WmsInventoryManager wmsInventoryManager){
		this.wmsInventoryManager = wmsInventoryManager;
	}
	/**50*/  
    public static Integer PAGE_NUMBER = 50;
    
    public static Integer WIDTH = 12;
	//合格品超库存-初始化- fdj-001-1
	public void hgInventoryOutInit(){
		logger.error("合格品超库存-初始化- fdj-001-1", null);
		wmsInventoryManager.hgInventoryOutInit();
		//生成task,定时插入led表
		String[] obj = new String[]{
				EdiTaskType.SCREEN_HG_INVENTORY_OUT,EdiTaskMethod.SCREEN_HG_INVENTORY_OUT,0L+""
		};
		wmsInventoryManager.saveTask(obj);
//		hgInventoryOutXls();
	}
	public void hgInventoryOutEdi(Long taskId){
		jdbcTemplate.update("truncate table "+ScreenTable.LED_HG_INVENTORY_OUT);
		
		String sql = "select s.sup_code,substr(s.sup_name,0,15) as sup_name,s.pallet_qty_info,s.pallet_qty_in" +
				" from "+ScreenTable.SCREEN_HG_INVENTORY_OUT+" s where s.status = 0 and s.type = '合格品超库存'";
		List list = jdbcTemplate.queryForList(sql);
		if(list!=null && list.size()>0){
			//将数据插入led表 led_hg_inventory_out
			sql = "insert into "+ScreenTable.LED_HG_INVENTORY_OUT+"(SUP_CODE,SUP_NAME,PALLET_QTY_IN,PALLET_QTY_INFO)"+
					" values(?,?,?,?)";
			Iterator i = list.iterator();
			while(i.hasNext()){
				Map m = (Map) i.next();
				jdbcTemplate.update(sql, new Object[]{
						m.get("sup_code")==null?"-":m.get("sup_code").toString(),
						m.get("sup_name")==null?"-":m.get("sup_name").toString(),
						m.get("pallet_qty_info")==null?"0":Double.valueOf(m.get("pallet_qty_info").toString()),
						m.get("pallet_qty_in")==null?"0":Double.valueOf(m.get("pallet_qty_in").toString())});
			}
			sql = "update "+ScreenTable.SCREEN_HG_INVENTORY_OUT+" s set s.status = 1"
					+ " where s.type = '合格品超库存' and s.status = 0";
			jdbcTemplate.update(sql);
		}
	}
	//合格品超库存-产生文档- fdj-001-1
	@SuppressWarnings({ "unchecked", "unused" })
	private void hgInventoryOutXls(){
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().openSession();
		try {
			List<Object[]> objs = session.createSQLQuery("select substr(s.sup_name,0,15),s.pallet_qty_info,s.pallet_qty_in"//s.type,
					+ " from SCREEN_HG_INVENTORY_OUT s where s.status = 0 and s.type = '合格品超库存'").list();
			String[] title1 = {};//"合格品超库存"
			String[] title2 = {};//"供应商名称","租赁托盘个数","实际托盘个数"
			Integer[] length = {35,16,15};
			String filePath = "D:/ledShow/fdj-001-1.xls";
			//指定列对齐方式
			Map<Integer,Alignment> aligns = new HashMap<Integer, Alignment>();
			aligns.put(0, Alignment.LEFT);
			//指定自适应大小列
			Map<Integer,Boolean> autosizes = new HashMap<Integer, Boolean>();
			autosizes.put(0, Boolean.TRUE);
			//border:边框,mergeCell:合并
			if(objs==null || objs.size()<=0){
				objs = noDate(objs,length.length,"无异常");
			}
			XlsUtils.writeXls(title1,title2,length,filePath, objs,true,true,aligns,WIDTH,null);
			session.createSQLQuery("update SCREEN_HG_INVENTORY_OUT s set s.status = 1"
					+ " where s.type = '合格品超库存' and s.status = 0").executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}
	private List<Object[]> noDate(List<Object[]> objs,int length,String mesg){
		Object[] obj = new Object[length];
		for(int i = 0 ; i< length ; i++){
			obj[i] = mesg;
		}
		objs.add(obj);
		return objs;
	}
	public static void main(String[] args) {
		List<Object[]> objs = new ArrayList<Object[]>();
		Object[] obj = new Object[5];
		for(int i = 0 ; i< 5 ; i++){
			obj[i] = "无异常";
		}
		objs.add(obj);
	}
	//不合格品超库存-初始化- fdj-001-2
	public void unHgInventoryOutInit(){
		logger.error("不合格品超库存-初始化- fdj-001-2", null);
		wmsInventoryManager.unHgInventoryOutInit();
		unHgInventoryOutXls();
	}
	@SuppressWarnings("unchecked")
	public void unHgInventoryOutXls(){
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().openSession();
		try {
			List<Object[]> objs = session.createSQLQuery("select substr(s.sup_name,0,15),s.pallet_qty_info,s.pallet_qty_in"//s.type,
					+ " from SCREEN_HG_INVENTORY_OUT s where s.status = 0 and s.type = '不合格品超库存'").list();
			String[] title1 = {};//"不合格品超库存"
			String[] title2 = {};//"供应商名称","标准托盘个数","实际托盘个数"
			Integer[] length = {35,16,15};
			String filePath = "D:/ledShow/fdj-001-2.xls";
			//指定列对齐方式
			Map<Integer,Alignment> aligns = new HashMap<Integer, Alignment>();
			aligns.put(0, Alignment.LEFT);
			//指定自适应大小列
			Map<Integer,Boolean> autosizes = new HashMap<Integer, Boolean>();
			autosizes.put(0, Boolean.TRUE);
			//border:边框,mergeCell:合并
			if(objs==null || objs.size()<=0){
				objs = noDate(objs,length.length,"无异常");
			}
			XlsUtils.writeXls(title1,title2,length,filePath, objs,true,true,aligns,WIDTH,null);
			session.createSQLQuery("update SCREEN_HG_INVENTORY_OUT s set s.status = 1"
					+ " where s.type = '不合格品超库存' and s.status = 0").executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}
	//报缺库存预警-初始化- fdj-001-3
	public void sysInventoryMiss(){
		wmsInventoryManager.sysInventoryMiss();
		sysInventoryMissXls();
	}
	@SuppressWarnings("unchecked")
	public void sysInventoryMissXls(){
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().openSession();
		try {
			String[] title1 = {};//"报缺库存预警"
			String[] title2 = {};//"供应商名称","物料图号","工艺状态"
			Integer[] length = {35,24,15};
			String filePath = "D:/ledShow/fdj-001-3.xls";
			List<Object[]> objs = session.createSQLQuery("select substr(w.sup_name,0,15),w.item_code,w.extendpropc1"
					+ " from wms_mis_inventory w").list();
			//指定列对齐方式
			Map<Integer,Alignment> aligns = new HashMap<Integer, Alignment>();
			aligns.put(0, Alignment.LEFT);
			aligns.put(1, Alignment.LEFT);
			//指定自适应大小列
			Map<Integer,Boolean> autosizes = new HashMap<Integer, Boolean>();
			autosizes.put(0, Boolean.TRUE);
			autosizes.put(1, Boolean.TRUE);
			//border:边框,mergeCell:合并
			if(objs==null || objs.size()<=0){
				objs = noDate(objs,length.length,"无异常");
			}
			XlsUtils.writeXls(title1,title2,length,filePath, objs,true,true,aligns,WIDTH,null);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}
	//预到货预警-初始化-fdj-001-4
	public void screenAsnPre(){
		String mes = wmsInventoryManager.screenAsnPre();
		if(mes.equals("success")){
			screenAsnPreXls();
		}
		LocalizedMessage.addLocalizedMessage(mes);
    }  
	@SuppressWarnings("unchecked")
	public void screenAsnPreXls(){
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().openSession();
		try {
			String[] title1 = {};//"预到货预警"
			String[] title2 = {};//"供应商名称","ASN单号","预约时间"
			Integer[] length = {35,24,15};
			String filePath = "D:/ledShow/fdj-001-4.xls";
			List<Object[]> objs = session.createSQLQuery("select substr(sc.sup_name,0,15),substr(sc.asn_code,-17),to_char(sc.arrival_pre,'yyyy-MM-dd')"
					+ " from SCREEN_ASN_PRE sc where sc.type='预到货预警' and sc.status=0").list();
			//指定列对齐方式
			Map<Integer,Alignment> aligns = new HashMap<Integer, Alignment>();
			aligns.put(0, Alignment.LEFT);
			//指定自适应大小列
			Map<Integer,Boolean> autosizes = new HashMap<Integer, Boolean>();
			autosizes.put(0, Boolean.TRUE);
			autosizes.put(1, Boolean.TRUE);
			//border:边框,mergeCell:合并
			if(objs==null || objs.size()<=0){
				objs = noDate(objs,length.length,"无异常");
			}
			XlsUtils.writeXls(title1,title2,length,filePath, objs,true,true,aligns,WIDTH,null);
			session.createSQLQuery("update SCREEN_ASN_PRE sc set sc.status=1"
					+ " where sc.type = '预到货预警' and sc.status = 0").executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}
	//超时备货预警-初始化- fdj-002-1
	public void screenPickBlOut(){
		String sql = "select t.ld_type,t.related_bill1,t.receive_factory,t.blTimes,t.days,t.created_time,t.nowTime"+
				" from("+
				"select p.ld_type,p.related_bill1,p.receive_factory,"+
				"trunc(to_date(to_char(sysdate,'yyyy-MM-dd hh24:mi:ss') "+
				",'yyyy-MM-dd hh24:mi:ss') - to_date(to_char(p.created_time,'yyyy-MM-dd hh24:mi:ss') "+
				",'yyyy-MM-dd hh24:mi:ss'),1)*1*24*60 as blTimes,"+
				"trunc(to_date(to_char(sysdate,'yyyy-MM-dd hh24:mi:ss') "+
				",'yyyy-MM-dd hh24:mi:ss') - to_date(to_char(p.created_time,'yyyy-MM-dd hh24:mi:ss') "+
				",'yyyy-MM-dd hh24:mi:ss'),1)*1 as days,"+
				"to_char(p.created_time,'yyyy-MM-dd hh24:mi:ss') as created_time,to_char(sysdate,'yyyy-MM-dd hh24:mi:ss') as nowTime"+
				" from wms_pick_ticket p where p.status = 'WORKING' and p.alloacted_quantity_bu=0 and p.warehouse_id = ?"+
				" and p.related_bill1 is not null and p.receive_factory is not null and p.ld_type is not null"+
				")t where t.days <= 30";
		String[] title1 = {};//"超时备货预警"
		String[] title2 = {};//"MES单号","收货工厂","备货超时"
		Integer[] length = {26,16,15};
		String filePath = "D:/ledShow/fdj-002-1(";//+dock+").xls"
		//指定列对齐方式
		Map<Integer,Alignment> aligns = new HashMap<Integer, Alignment>();
		aligns.put(0, Alignment.LEFT);
		aligns.put(1, Alignment.LEFT);
		//指定自适应大小列
		Map<Integer,Boolean> autosizes = new HashMap<Integer, Boolean>();
		autosizes.put(0, Boolean.TRUE);
		autosizes.put(1, Boolean.TRUE);
		autosizes.put(2, Boolean.TRUE);
		screenPickOut(sql,"R101_拉动方式标准时长规则表","备货标准时长(分钟)",
				title1,title2,length,filePath,aligns,autosizes);
	}
	private void screenPickBlOutXls(Map<String,List<Object[]>> xlsObjs,String[] title1,String[] title2,
			Integer[] length,String filePath,Map<Integer,Alignment> aligns,Map<Integer,Boolean> autosizes){
		Iterator<Entry<String, List<Object[]>>> ii = xlsObjs.entrySet().iterator();
		List<String> exitsDock = new ArrayList<String>();//保存已经取到数据的道口(发货口)
		String dock = null;
		while(ii.hasNext()){
			Entry<String,List<Object[]>> en = (Entry<String, List<Object[]>>) ii.next();
			dock = en.getKey();//发货口
			List<Object[]> objs = en.getValue();
			//border:边框,mergeCell:合并
			if(objs==null || objs.size()<=0){
				objs = noDate(objs,length.length,"无异常");
			}
			XlsUtils.writeXls(title1,title2,length,filePath + dock+").xls", objs,true,true,aligns,WIDTH,null);
			exitsDock.add(dock);
		}
		//如果没取到数据的xls内容置空
		List<String> docs = new ArrayList<String>();
		for(int i = 0 ; i<10 ; i++){
			docs.add(i+"");
		}
		for(String d : exitsDock){
			docs.remove(d);
		}
		List<Object[]> objs = new ArrayList<Object[]>();
		initNullXls(objs, length, filePath, title1, title2, aligns, docs);
	}
	@SuppressWarnings("unchecked")
	private void screenPickOut(String sql,String ruleTableName,String ruleColumn,
			String[] title1,String[] title2,Integer[] length,String filePath,Map<Integer,
			Alignment> aligns,Map<Integer,Boolean> autosizes){
		String rijiewarehouse = GlobalParamUtils.getGloableStringValue("rijie_sys");
		String[] warehouses = rijiewarehouse.split(MyUtils.spilt1);
		for(String warehouse : warehouses){
			Long warehouseId = Long.parseLong(warehouse);
			WmsWarehouse ws = commonDao.load(WmsWarehouse.class, warehouseId);
			
			HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
			Session session = transactionManager.getSessionFactory().openSession();
			
			List<String> docs = new ArrayList<String>();
			for(int i = 0 ; i<10 ; i++){
				docs.add(i+"");
			}
			
			try {
				SQLQuery query = session.createSQLQuery(sql);
				query.setLong(0, warehouseId);
				
				List<Object[]> blObjs = new ArrayList<Object[]>();
				List<Object[]> objs = query.list();
				if(objs!=null && objs.size()>0){
					int bzTimes = 0,blTimes = 0;
					for(Object[] obj : objs){
						//根据拉动方式获取**标准时长(分钟)
						Object value = null;
						try {
							value = wmsInventoryManager.getSingleRuleTableDetail(ws.getName(), ruleTableName,
									ruleColumn,obj[0]);
						} catch (Exception e) {
							try {
								Thread.sleep(20000);//2min=120000,1min=60000
							} catch (InterruptedException e1) {
								logger.error("", e1);
							}
							try {
								value = wmsInventoryManager.getSingleRuleTableDetail(ws.getName(), ruleTableName,
										ruleColumn,obj[0]);
							} catch (Exception e2) {
								try {
									Thread.sleep(10000);//2min=120000,1min=60000
								} catch (InterruptedException e1) {
									logger.error("", e1);
								}
								try {
									value = wmsInventoryManager.getSingleRuleTableDetail(ws.getName(), ruleTableName,
											ruleColumn,obj[0]);
								} catch (Exception e3) {
									System.out.println(e3.getMessage());
								}
							}
						}
						if(value!=null){
							bzTimes = Integer.valueOf(value.toString());//备货/发运标准时长
							blTimes = Integer.valueOf(obj[3].toString());//实际备货/发运时长
							if(bzTimes<blTimes){//备货/发运标准时长<实际备货/发运时长
								blObjs.add(new Object[]{
										//MES单号,收货工厂,备货/发运超时(备货/发运时长标准)
										obj[1],obj[2],(blTimes-bzTimes)+"("+bzTimes+")"
								});
							}
						}
					}objs.clear();
					if(blObjs.size()>0){
						//按照收货工厂产生不同的xls
						String receive_factory = null;
						Map<String,List<Object[]>> xlsObjs = new HashMap<String,List<Object[]>>();
						for(Object[] obj : blObjs){
							//MES单号,收货工厂,备货/发运超时(备货/发运时长标准)
							receive_factory = obj[1].toString();
							//根据收货工厂获取发货口,即平台
							Object value = wmsInventoryManager.getSingleRuleTableDetail(ws.getName(), 
									"R101_发货_收货工厂关系表","发货口",receive_factory);
							if(value!=null){
								if(xlsObjs.containsKey(value)){
									objs = xlsObjs.get(value);
								}else{
									objs = new ArrayList<Object[]>();
								}
								objs.add(obj);
								xlsObjs.put(value.toString(), objs);
							}
						}blObjs.clear();
						if(xlsObjs.size()>0){
							screenPickBlOutXls(xlsObjs, title1, title2, length, filePath, aligns,autosizes);//(xlsObjs,title,length,filePath);
						}else{
							initNullXls(blObjs, length, filePath, title1, title2, aligns,docs);
						}
					}else{
						initNullXls(blObjs, length, filePath, title1, title2, aligns,docs);
					}
				}else{
					initNullXls(blObjs, length, filePath, title1, title2, aligns,docs);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(session!=null && session.isOpen()){
					session.close();
				}
			}
		}
	}
	//对于没有异常的数据要将源文件内容置空
	private void initNullXls(List<Object[]> objs,Integer[] length,String filePath,
			String[] title1,String[] title2,Map<Integer,Alignment> aligns,List<String> docs){
		objs = noDate(objs,length.length,"无异常");
		//暂定10个发货口
		for(String d : docs){
			String file = filePath + d+").xls";
			File f = new File(file);
			if(f.exists()){
				XlsUtils.writeXls(title1,title2,length,file, objs,true,true,aligns,WIDTH,null);
			}
		}
	}
	//超时发运预警-初始化- fdj-002-2
	public void screenPickShipOut(){
		String sql = "select t.ld_type,t.related_bill1,t.receive_factory,t.shipTimes,t.days,t.allocated_time,t.nowTime"+
				" from("+
				"select p.ld_type,p.related_bill1,p.receive_factory,"+
				"trunc(to_date(to_char(sysdate,'yyyy-MM-dd hh24:mi:ss') "+
				",'yyyy-MM-dd hh24:mi:ss') - to_date(to_char(p.allocated_time,'yyyy-MM-dd hh24:mi:ss') "+
				",'yyyy-MM-dd hh24:mi:ss'),1)*1*24*60 as shipTimes,"+
				"trunc(to_date(to_char(sysdate,'yyyy-MM-dd hh24:mi:ss') "+
				",'yyyy-MM-dd hh24:mi:ss') - to_date(to_char(p.allocated_time,'yyyy-MM-dd hh24:mi:ss') "+
				",'yyyy-MM-dd hh24:mi:ss'),1)*1 as days,"+
				"to_char(p.allocated_time,'yyyy-MM-dd hh24:mi:ss') as allocated_time,to_char(sysdate,'yyyy-MM-dd hh24:mi:ss') as nowTime"+
				" from wms_pick_ticket p where p.status = 'WORKING' and p.alloacted_quantity_bu>0 and p.warehouse_id = ?"+
				" and p.shipped_quantity_bu=0 and p.allocated_time is not null"+
				" and p.related_bill1 is not null and p.receive_factory is not null and p.ld_type is not null"+
				")t where t.days <= 30";
		String[] title1 = {};//"超时发运预警"
		String[] title2 = {};//"MES单号","收货工厂","发货超时"
		Integer[] length = {26,15,16};
		String filePath = "D:/ledShow/fdj-002-2(";//+dock+").xls"
		//指定列对齐方式
		Map<Integer,Alignment> aligns = new HashMap<Integer, Alignment>();
		aligns.put(0, Alignment.LEFT);
		aligns.put(1, Alignment.LEFT);
		//指定自适应大小列
		Map<Integer,Boolean> autosizes = new HashMap<Integer, Boolean>();
		autosizes.put(0, Boolean.TRUE);
		autosizes.put(1, Boolean.TRUE);
		autosizes.put(2, Boolean.TRUE);
		screenPickOut(sql, "R101_拉动方式标准时长规则表", "发运标准时长(分钟)", title1, title2, length, filePath, aligns,autosizes);
	}
	//AB料发货预警-初始化- fdj-002-3
	@SuppressWarnings("unchecked")
	public void screenPickAB(){
		//昨日已备货完成还未发运的发货单
		String sql = "select i.code,sup.code,substr(sup.name,0,8)"+
				" from wms_task t"+
				" left join wms_item_key ik on ik.id = t.item_key_id"+
				" left join wms_item i on i.id = ik.item_id"+
				" left join wms_organization sup on sup.id = ik.supplier_id"+
				" left join wms_move_doc_detail mdd on mdd.id = t.move_doc_detail_id"+
				" left join wms_move_doc m on m.id = mdd.move_doc_id "+
				" where m.warehouse_id = ? and t.moved_quantity_bu>0 and t.type = 'MV_PICKTICKET_PICKING'"+
				" and t.status in ('WORKING','FINISHED') and t.pick_time is not null "+
				" and trunc(to_date(to_char(sysdate,'yyyy-MM-dd hh24:mi:ss') "+
				" ,'yyyy-MM-dd hh24:mi:ss') - to_date(to_char(t.pick_time,'yyyy-MM-dd hh24:mi:ss') "+
				" ,'yyyy-MM-dd hh24:mi:ss'),0)*1 <= ?"+
				" group by i.code,sup.code,sup.name";
		//即将备料和未备货完成的发货单
		String sqlP = "select p.related_bill1,p.receive_factory,i.code,substr(sup.name,0,8) "+
					"from wms_pick_ticket_detail pdd "+
					"left join wms_pick_ticket p on p.id = pdd.pick_ticket_id "+
					"left join wms_item i on i.id = pdd.item_id "+
					"left join wms_organization sup on sup.id = pdd.supplier_id "+
					"where p.warehouse_id = ? and p.status = 'WORKING' "+
					"and p.alloacted_quantity_bu>0 "+
					"and p.picked_quantity_bu<p.alloacted_quantity_bu and p.shipped_quantity_bu=0 "+
					"group by p.related_bill1,p.receive_factory,i.code,sup.name";
		String rijiewarehouse = GlobalParamUtils.getGloableStringValue("rijie_sys");
		String[] warehouses = rijiewarehouse.split(MyUtils.spilt1);
		for(String warehouse : warehouses){
			Long warehouseId = Long.parseLong(warehouse);
			WmsWarehouse ws = commonDao.load(WmsWarehouse.class, warehouseId);
			
			HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
			Session session = transactionManager.getSessionFactory().openSession();
			try {
				SQLQuery query = session.createSQLQuery(sql);
				query.setLong(0, warehouseId);
				query.setInteger(1, 1);//天数差,上线120改为1
				List<Object[]> objs = query.list();
				
				String[] title1 = {};//"AB料发货预警"
				String[] title2 = {};//"MES单号","收货工厂","物料","供应商名称"
				Integer[] length = {26,16,15,29};
				String filePath = "D:/ledShow/fdj-002-3(";
				//指定列对齐方式
				Map<Integer,Alignment> aligns = new HashMap<Integer, Alignment>();
				aligns.put(0, Alignment.LEFT);
				aligns.put(1, Alignment.LEFT);
				aligns.put(2, Alignment.LEFT);
				aligns.put(3, Alignment.LEFT);
				List<String> docs = new ArrayList<String>();
				for(int i = 0 ; i<10 ; i++){
					docs.add(i+"");
				}
				
				if(objs!=null && objs.size()>0){
					Map<String,String[]> itemSups = new HashMap<String,String[]>();
					for(Object[] obj : objs){
						itemSups.put(obj[0].toString(), new String[]{
							obj[1].toString(),obj[2].toString()
						});
					}objs.clear();
					
					query = session.createSQLQuery(sqlP);
					query.setLong(0, warehouseId);
					objs = query.list();
					if(objs!=null && objs.size()>0){
						List<Object[]> ABobjs = new ArrayList<Object[]>();
						String[] ss = null;
						for(Object[] obj : objs){
							ss = itemSups.get(obj[2]);
							if(ss!=null && !ss[1].equals(obj[3])){//AB料
								ABobjs.add(obj);
							}
						}objs.clear();itemSups.clear();
						//按照收货工厂区分
						if(ABobjs!=null && ABobjs.size()>0){
							Map<String,List<Object[]>> ABmaps = new HashMap<String, List<Object[]>>();
							String key = null;
							for(Object[] obj : ABobjs){
								key = obj[1].toString();
								Object value = null;
								try {
									value = wmsInventoryManager.getSingleRuleTableDetail(ws.getName(), 
											"R101_发货_收货工厂关系表","发货口",obj[1].toString());
								} catch (Exception e) {
									try {
										Thread.sleep(20000);//2min=120000,1min=60000
									} catch (InterruptedException e1) {
										logger.error("", e1);
									}
									try {
										value = wmsInventoryManager.getSingleRuleTableDetail(ws.getName(), 
												"R101_发货_收货工厂关系表","发货口",key);
									} catch (Exception e2) {
										try {
											Thread.sleep(10000);//2min=120000,1min=60000
										} catch (InterruptedException e1) {
											logger.error("", e1);
										}
										try {
											value = wmsInventoryManager.getSingleRuleTableDetail(ws.getName(), 
													"R101_发货_收货工厂关系表","发货口",key);
										} catch (Exception e3) {
											System.out.println(e3.getMessage());
										}
									}
								}
								
								if(value!=null){
									if(ABmaps.containsKey(value)){
										objs = ABmaps.get(value);
									}else{
										objs = new ArrayList<Object[]>();
									}
									objs.add(new Object[]{
										//MES单号,收货工厂,物料,供应商名称
										obj[0],obj[1],obj[2],obj[3]
									});
									ABmaps.put(value.toString(), objs);
								}
							}ABobjs.clear();
							//产生xls
							if(ABmaps!=null && ABmaps.size()>0){
								//指定自适应大小列
								Map<Integer,Boolean> autosizes = new HashMap<Integer, Boolean>();
								autosizes.put(0, Boolean.TRUE);
								autosizes.put(1, Boolean.TRUE);
								autosizes.put(2, Boolean.TRUE);
								autosizes.put(3, Boolean.TRUE);
								screenPickBlOutXls(ABmaps, title1, title2, length, filePath, aligns,autosizes);
								ABmaps.clear();
							}else{
								initNullXls(objs, length, filePath, title1, title2, aligns, docs);
							}
						}else{
							initNullXls(objs, length, filePath, title1, title2, aligns, docs);
						}
					}else{
						initNullXls(objs, length, filePath, title1, title2, aligns, docs);
					}
				}else{
					initNullXls(objs, length, filePath, title1, title2, aligns, docs);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(session!=null && session.isOpen()){
					session.close();
				}
			}
		}
	}
	//报缺物料入库预警-初始化- fdj-002-4
	@SuppressWarnings("unchecked")
	public void screenAsnMiss(){
		//属于报缺的未分配发货单.sql
		String sql = "select t.related_bill1,t.receive_factory,t.code,t.name from("+
				"select p.related_bill1,p.receive_factory,i.code,substr(sup.name,0,12) as name,"+
				"trunc(to_date(to_char(sysdate,'yyyy-MM-dd hh24:mi:ss') "+
				",'yyyy-MM-dd hh24:mi:ss') - to_date(to_char(p.created_time,'yyyy-MM-dd hh24:mi:ss') "+
				",'yyyy-MM-dd hh24:mi:ss'),1)*1 as days"+
 " from wms_pick_ticket_detail ppd"+
 " left join wms_item i on i.id = ppd.item_id"+
 " left join wms_pick_ticket p on p.id = ppd.pick_ticket_id"+
 " left join wms_organization sup on sup.id = ppd.supplier_id"+
 " where p.status = 'WORKING' and p.alloacted_quantity_bu=0 and p.warehouse_id = ?"+
 "  and p.related_bill1 is not null and p.receive_factory is not null"+
 "  and exists (select 1 from wms_mis_inventory w where w.item_code = i.code)"+
 "  and exists (select 1 from wms_asn_detail aa"+
 "  left join wms_asn a on a.id = aa.asn_id"+
 "  where a.status = 'RECEIVED' and aa.item_id = ppd.item_id)" +
 ")t where t.days <= 30";
		
		String rijiewarehouse = GlobalParamUtils.getGloableStringValue("rijie_sys");
		String[] warehouses = rijiewarehouse.split(MyUtils.spilt1);
		for(String warehouse : warehouses){
			Long warehouseId = Long.parseLong(warehouse);
			WmsWarehouse ws = commonDao.load(WmsWarehouse.class, warehouseId);
			
			HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
			Session session = transactionManager.getSessionFactory().openSession();
			try {
				SQLQuery query = session.createSQLQuery(sql);
				query.setLong(0, warehouseId);
				List<Object[]> objs = query.list();
				
				String[] title1 = {};//"报缺物料入库预警"
				String[] title2 = {};//"MES单号","收货工厂","物料","供应商名称"
				Integer[] length = {26,16,15,29};
				String filePath = "D:/ledShow/fdj-002-4(";
				//指定列对齐方式
				Map<Integer,Alignment> aligns = new HashMap<Integer, Alignment>();
				aligns.put(0, Alignment.LEFT);
				aligns.put(1, Alignment.LEFT);
				aligns.put(2, Alignment.LEFT);
				aligns.put(3, Alignment.LEFT);
				List<String> docs = new ArrayList<String>();
				for(int i = 0 ; i<10 ; i++){
					docs.add(i+"");
				}
				
				if(objs!=null && objs.size()>0){
					Map<String,List<Object[]>> missmaps = new HashMap<String, List<Object[]>>();
					List<Object[]> temp = new ArrayList<Object[]>();
					String key = null;
					for(Object[] obj : objs){
						key = obj[1].toString();
						Object value =null;
						try {
							value = wmsInventoryManager.getSingleRuleTableDetail(ws.getName(), 
									"R101_发货_收货工厂关系表","发货口",key);
						} catch (Exception e) {
							try {
								Thread.sleep(20000);//2min=120000,1min=60000
							} catch (InterruptedException e1) {
								logger.error("", e1);
							}
							try {
								value = wmsInventoryManager.getSingleRuleTableDetail(ws.getName(), 
										"R101_发货_收货工厂关系表","发货口",key);
							} catch (Exception e2) {
								try {
									Thread.sleep(10000);//2min=120000,1min=60000
								} catch (InterruptedException e1) {
									logger.error("", e1);
								}
								try {
									value = wmsInventoryManager.getSingleRuleTableDetail(ws.getName(), 
											"R101_发货_收货工厂关系表","发货口",key);
								} catch (Exception e3) {
									System.out.println(e3.getMessage());
								}
							}
						}
						if(value!=null){
							if(missmaps.containsKey(value)){
								temp = missmaps.get(value);
							}else{
								temp = new ArrayList<Object[]>();
							}
							temp.add(new Object[]{
								//MES单号,收货工厂,物料,供应商名称
								obj[0],obj[1],obj[2],obj[3]
							});
							missmaps.put(value.toString(), temp);
						}
					}objs.clear();
					//产生xls
					if(missmaps!=null && missmaps.size()>0){
						//指定自适应大小列
						Map<Integer,Boolean> autosizes = new HashMap<Integer, Boolean>();
						autosizes.put(0, Boolean.TRUE);
						autosizes.put(1, Boolean.TRUE);
						autosizes.put(2, Boolean.TRUE);
						autosizes.put(3, Boolean.TRUE);
						screenPickBlOutXls(missmaps, title1, title2, length, filePath, aligns,autosizes);
						missmaps.clear();
					}else{
						initNullXls(objs, length, filePath, title1, title2, aligns, docs);
					}
				}else{
					initNullXls(objs, length, filePath, title1, title2, aligns, docs);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(session!=null && session.isOpen()){
					session.close();
				}
			}
		}
	}
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
