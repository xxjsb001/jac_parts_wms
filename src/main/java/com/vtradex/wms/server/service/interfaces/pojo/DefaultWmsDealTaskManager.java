package com.vtradex.wms.server.service.interfaces.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.message.Task;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.base.LotInfo;
import com.vtradex.wms.server.model.base.ShipLotInfo;
import com.vtradex.wms.server.model.interfaces.HeadType;
import com.vtradex.wms.server.model.interfaces.MiddleAsnSrmDetail;
import com.vtradex.wms.server.model.interfaces.MiddleOrderJh;
import com.vtradex.wms.server.model.interfaces.MiddleOrderKb;
import com.vtradex.wms.server.model.interfaces.MiddleOrderSps;
import com.vtradex.wms.server.model.interfaces.WHead;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.model.move.WmsMoveDocType;
import com.vtradex.wms.server.model.move.WmsWorkDocStatus;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsStationAndItem;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.model.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.receiving.WmsASNShelvesStauts;
import com.vtradex.wms.server.model.receiving.WmsASNStatus;
import com.vtradex.wms.server.model.receiving.WmsSource;
import com.vtradex.wms.server.model.shipping.WmsPickTicket;
import com.vtradex.wms.server.model.shipping.WmsPickTicketDetail;
import com.vtradex.wms.server.model.shipping.WmsPickTicketStatus;
import com.vtradex.wms.server.model.shipping.WmsShipLot;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.service.GlobalParamUtils;
import com.vtradex.wms.server.service.interfaces.WmsDealTaskManager;
import com.vtradex.wms.server.service.middle.MiddleTableName;
import com.vtradex.wms.server.service.receiving.WmsASNManager;
import com.vtradex.wms.server.service.receiving.WmsMoveDocManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;

public class DefaultWmsDealTaskManager 
			extends DefaultBaseManager implements WmsDealTaskManager{

	private WmsBussinessCodeManager codeManager;
	private JdbcTemplate jdbcTemplateExt1;//中间表数据源
	
	public WmsBussinessCodeManager getCodeManager() {
		return codeManager;
	}
	public void setCodeManager(WmsBussinessCodeManager codeManager) {
		this.codeManager = codeManager;
	}
	public JdbcTemplate getJdbcTemplateExt1() {
		return jdbcTemplateExt1;
	}
	public void setJdbcTemplateExt1(JdbcTemplate jdbcTemplateExt1) {
		this.jdbcTemplateExt1 = jdbcTemplateExt1;
	}

	@SuppressWarnings("unchecked")
	public void dealAsn(Long id){
		WHead w = commonDao.load(WHead.class, id);
		if(w==null){
			return;
		}
		List<MiddleAsnSrmDetail> list = commonDao.findByQuery
				("from MiddleAsnSrmDetail a where a.head.id=:id", "id",w.getId());
		WmsWarehouse warehouse = getwareHouse();//仓库
		Date date = new Date();
		int i = 1;//标记生成ASN还是明细,以及生成行号
		Integer lineNo = 10;//行号
		WmsASN asn = null;
		List<Long> ids = new ArrayList<Long>();//中间表数据id
		Double totalQty = 0D;//ASN总数
		
		for(MiddleAsnSrmDetail detail : list){
			
			String asnCode = codeManager.generateCodeByRule(warehouse, detail.getCompany().getName(), "ASN", detail.getBillType().getName());
			
			if(i == 1){
				asn = new WmsASN(warehouse, detail.getCompany(),detail.getBillType(), asnCode,WmsASNStatus.OPEN, detail.getAsnNO(),
						detail.getPoNo(),date,WmsASNShelvesStauts.UNPUTAWAY,detail.getSupply(),WmsMoveDocStatus.OPEN,
						0d, WmsSource.INTERFACE,0d,Boolean.FALSE);
				commonDao.store(asn);
			}
			
			WmsASNDetail asnDetail = new WmsASNDetail(asn, lineNo*i, detail.getItem(), detail.getSendQty(), 
								detail.getPackageUnit(), detail.getSendQty(), 0d, 0d, detail.getTrayQty(), 0.0,detail.getIsMt());
			LotInfo lotInfo = new LotInfo(detail.getReDate(), asn.getCode(), detail.getSupply(), "-");
			asnDetail.setLotInfo(lotInfo);
			asnDetail.setPolineNo(detail.getPolineNo());
			asnDetail.setAsnLineNo(detail.getAsnLineNo());
			commonDao.store(asnDetail);
			i += 1;
			ids.add(id);
			totalQty += detail.getSendQty();
		}	
		updateTotalQty(null, asn, totalQty);//更新单头数量
	}

	WmsWarehouse getwareHouse(){
		WmsWarehouse warehouse = (WmsWarehouse) commonDao.findByQueryUniqueResult
							("from WmsWarehouse where name='新港仓库' and status='ENABLED'","","");
		if(null == warehouse){
			throw new BusinessException("未找到新港仓库");
		}
		return warehouse;
	}
	
	//更新 发货单/ASN 单头数量
	void updateTotalQty(WmsPickTicket pickTicket,WmsASN asn,Double qty){
		String updateHql = "";
		if(null == asn){
			updateHql = "update WmsPickTicket set "
							+ "expectedQuantityBU=:qtyParam where id="+pickTicket.getId();//更新整单数量
		}else{
			updateHql = "update WmsASN set expectedQuantityBU=:qtyParam where id="+asn.getId();
		}	
		commonDao.executeByHql(updateHql,"qtyParam" , qty);
	}
	
	@SuppressWarnings("unchecked")
	public void dealKbPickData(Long id){
		int i = 1;
		Integer lineNo = 10;
		Double totalQty = 0D;
		WmsPickTicket pickTicket = null;
		Date date = new Date();
		WmsWarehouse warehouse = getwareHouse();//仓库
		
		WHead w = commonDao.load(WHead.class, id);
		List<MiddleOrderKb> list = commonDao.findByQuery
				("from MiddleOrderKb a where a.head.id=:id", "id",w.getId());
		
		for(MiddleOrderKb kb : list){
			
			if(i == 1){
				String code = codeManager.generateCodeByRule(warehouse, kb.getCompany().getName(), "发货单", kb.getBillType().getName());
				pickTicket = new WmsPickTicket(warehouse,kb.getCompany(), kb.getBillType(), code, kb.getOdrNo(), 
								WmsPickTicketStatus.OPEN, kb.getDemandDate(), kb.getQty(), 0d, 0d, 0d,
								kb.getDware(), kb.getShdk(),WmsSource.INTERFACE,date,kb.getOdrSu(),kb.getBatch());
				commonDao.store(pickTicket);
			}
			WmsOrganization supplier = kb.getSupply();
			ShipLotInfo shipLotInfo = new ShipLotInfo
						(supplier == null ? null : supplier.getCode(),
								supplier == null ? null : supplier.getName());
			Double qty = kb.getQty().doubleValue();
			WmsPickTicketDetail detail = new WmsPickTicketDetail(pickTicket, kb.getItem(), 
									shipLotInfo,kb.getPackageUnit(),qty, qty, 0d, 0d, 0d, null, 
									null, kb.getProductLine(), kb.getStation(), kb.getIsJp(),lineNo*i,kb.getSx(),
									kb.getSmallQty().doubleValue(),kb.getPcs(),supplier,"-");
			commonDao.store(detail);
			
			i += 1;
			totalQty += qty;
		}
		updateTotalQty(pickTicket, null, totalQty);//更新单头数量
	}
	/**计划件*/
	@SuppressWarnings("unchecked")
	public void dealJhPickData(Long id){
		
		int i = 1;
		Integer lineNo = 10;
		Double totalQty = 0D;
		WmsPickTicket pickTicket = null;
		Date date = new Date();
		WmsWarehouse warehouse = getwareHouse();//仓库
		
		WHead w = commonDao.load(WHead.class, id);
		List<MiddleOrderJh> list = commonDao.findByQuery
				("from MiddleOrderJh a where a.head.id=:id", "id",w.getId());
		
		for(MiddleOrderJh jh : list){
			
			if(i == 1){
				String code = codeManager.generateCodeByRule(warehouse, jh.getCompany().getName(), "发货单", jh.getBillType().getName());
				pickTicket = new WmsPickTicket(warehouse,jh.getCompany(), jh.getBillType(), code, jh.getOdrNo(), 
								WmsPickTicketStatus.OPEN, jh.getDemandDate(), jh.getQty(), 0d, 0d, 0d,
								jh.getDware(), jh.getShdk(),WmsSource.INTERFACE,date,jh.getOdrSu(),jh.getBatch());
				commonDao.store(pickTicket);
			}
			WmsOrganization supplier = jh.getSupply();
			ShipLotInfo shipLotInfo = new ShipLotInfo
						(supplier == null ? null : supplier.getCode(),
								supplier == null ? null : supplier.getName());
			Double qty = jh.getQty().doubleValue();
			WmsPickTicketDetail detail = new WmsPickTicketDetail(pickTicket, jh.getItem(), 
									shipLotInfo,jh.getPackageUnit(),qty, qty, 0d, 0d, 0d, jh.getSlr(), 
									jh.getSlr(), jh.getProductLine(), jh.getStation(), jh.getIsJp(),lineNo*i,0,
									0d,jh.getPackageUnit().getUnit(),supplier,"-");
			detail.setFromSource(jh.getFromSource());
			commonDao.store(detail);
			
			i += 1;
			totalQty += qty;
		}
		updateTotalQty(pickTicket, null, totalQty);//更新单头数量
		
		Boolean ierror = false;
		String error = "";
		try {
			//判断是否存在该日期下的bom明细,如果有,产生task(HeadType.SPS_BOM)
			String sql = "SELECT COUNT(*) FROM "+MiddleTableName.W_BOM_MES+
					" WHERE to_char(production_time,'yyyy-MM-dd') = '"+JavaTools.format(pickTicket.getRequireArriveDate(), JavaTools.y_m_d)+"'"+
//					" WHERE to_char(production_time,'yyyy-MM-dd') = '2017-11-03'"+//test
					" and station_code is not null and qty>0";
			int size = jdbcTemplateExt1.queryForInt(sql);
			if(size>0){
				Task t = new Task(HeadType.SPS_BOM, 
						"wmsDealTaskManager"+MyUtils.spiltDot+"mesxgBom", pickTicket.getId());
				commonDao.store(t);
			}
		} catch (Exception e) {
			ierror = true;
			error = e.getMessage();
		}finally{
			if(ierror){
				System.out.println(error);
			}
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void mesxgBom(Long id){
		Boolean ierror = false;
		String error = "";
		WmsPickTicket pickTicket = commonDao.load(WmsPickTicket.class, id);
		String sheetNo = pickTicket.getRelatedBill1();//直送送货单单号(MES单号,和WMS接口的发货单匹配上之后更新)
		String sql = "SELECT ODR_NO as MES_NO,pro_odr_no as 订单号,itme_code as 物料编码,product_line as 产线编码" +
				",item_name as 物料名称,station_code as 工位编码,dock_code as 道口编码,SUM(qty) as 用量,is_sps" +
				",to_char(production_time,'yyyy-MM-dd hh24:mi:ss') as 时间 " +
				" FROM "+MiddleTableName.W_BOM_MES+
				" WHERE ODR_NO ='"+sheetNo+"'"+
//				" WHERE to_char(production_time,'yyyy-MM-dd') = '"+JavaTools.format(pickTicket.getRequireArriveDate(), JavaTools.y_m_d)+"'"+
//				" WHERE to_char(production_time,'yyyy-MM-dd') = '2017-11-03'"+//test
//				" AND itme_code = '5110011B3'"+//test
				" AND station_code is not null and qty>0" +
				" GROUP BY ODR_NO,pro_odr_no,itme_code,product_line,item_name,station_code,dock_code" +
				",is_sps,to_char(production_time,'yyyy-MM-dd hh24:mi:ss')" +
				" ORDER BY product_line,pro_odr_no,station_code" +
				",to_char(production_time,'yyyy-MM-dd hh24:mi:ss'),item_name,itme_code";
		try {
			List list = jdbcTemplateExt1.queryForList(sql);
			if(list!=null && list.size()>0){
				String orderNo;//bom里面维护订单号
				String planTime;//计划日期(上线日期)
				Integer sx = 0,isSps = 0;//顺序
				String itemCode;//物料代码
				String itemName;//物料名称
				String productLine;//产线
				String station;//工位
				String stock;//道口
				Integer qty;//数量
				
				String hql = "FROM WmsStationAndItem w WHERE w.item.code =:item";
				WmsStationAndItem w = null;
				Integer loadage = 0;
				Map<String,WmsStationAndItem> wsa = new HashMap<String,WmsStationAndItem>();
				
				String isPai = "SELECT i.class5 FROM WmsItem i WHERE i.code =:item AND i.class5 is not null";
				Map<String,String> class5 = new HashMap<String, String>();
				String c5 = null;
				
				Set<String> containNames = new HashSet<String>();
				
				List<Object[]> objs = new ArrayList<Object[]>();
				String key = null;
				Map<String,Integer> ps = new HashMap<String, Integer>();
				
				Map<String,List<Object[]>> psObjs = new HashMap<String, List<Object[]>>();//按照产线工位划分
				List<Object[]> orderObjs = null;
				Map<String,Integer> keyNo = new HashMap<String, Integer>();//key=产线+工位+订单号
				Integer keyLine = 1;
				
				String type = null;
				Iterator iter = list.iterator();
				while(iter.hasNext()){//第一次将时序件和排序件抓取并且加以字段区分,并汇总需要器具型号种类
					type = null;
					loadage = 0;
					c5 = null;
					Map m = (Map) iter.next();
					orderNo = (String) m.get("订单号");
					planTime = (String) m.get("时间");
					itemCode = (String) m.get("物料编码");
					itemName = (String) m.get("物料名称");
					productLine = (String) m.get("产线编码");
					station = (String) m.get("工位编码");
					stock = (String) m.get("道口编码");
					qty = ((BigDecimal)m.get("用量")).intValue();
					isSps = ((BigDecimal)m.get("is_sps")).intValue();
					
					if(wsa.containsKey(itemCode)){
						w = wsa.get(itemCode);
					}else{
						w = (WmsStationAndItem) commonDao.findByQueryUniqueResult(hql, "item", itemCode);
						wsa.put(itemCode, w);
					}
					if(w!=null){//时序件
						type = WmsMoveDocType.SPS_PICKING;
						loadage = w.getLoadage();
						c5 = w.getName();
						
						key = productLine+station;
						if(ps.containsKey(key)){
							sx = ps.get(key);
						}else{
							sx = 0;
						}
						sx++;
						ps.put(key, sx);
						objs.add(new Object[]{
								sheetNo,orderNo,planTime,sx,itemCode,itemName,productLine,station,stock,qty,isSps,type,c5,loadage
						});
						containNames.add(c5);
					}else{
						if(class5.containsKey(itemCode)){//物料维护了class5的但是未维护器具物料关系表的视为排序件
							c5 = class5.get(itemCode);
						}else{
							List<String> c5s = commonDao.findByQuery(isPai, "item", itemCode);
							if(c5s!=null && c5s.size()>0){
								c5 = c5s.get(0);
							}else{
								c5 = null;
							}
							class5.put(itemCode, c5);
						}
						if(!StringUtils.isEmpty(c5)){//排序件
							type = WmsMoveDocType.ORDER_PICKING;
							key = productLine+MyUtils.spilt1+station;
							if(psObjs.containsKey(key)){
								orderObjs = psObjs.get(key);
							}else{
								orderObjs = new ArrayList<Object[]>();
							}
							orderObjs.add(new Object[]{
									sheetNo,orderNo,planTime,itemCode,itemName,productLine,station,stock,qty,isSps,c5
							});
							psObjs.put(key, orderObjs);
							key = productLine+MyUtils.spilt1+station+MyUtils.spilt1+orderNo;
							if(!keyNo.containsKey(key)){
//								System.out.println(key+"="+keyLine);
								keyNo.put(key, keyLine);
								keyLine++;
							}
						}
					}
				}list.clear();
				
				Map<String,List<Object[]>> cosSps = new HashMap<String, List<Object[]>>();//存放不同标签下面的时序物料明细
				if(objs!=null && objs.size()>0){
					String boxTag = null;
					Iterator<String> ii = containNames.iterator();
					while(ii.hasNext()){
						String c = ii.next();
//						if(c.equals("驾驶室隔热垫")){
//							System.out.println(c);
//						}
						//构造一个容器,且计算装载量
						Map<String,Integer> ci = new HashMap<String, Integer>();
						Integer ciQty = 0,avaliableQty = 0,picQty = 0,line = 1;
						ci.put(c, ciQty);//初始化装载量=0
						List<Object[]> tempSps = new ArrayList<Object[]>();
						Boolean remove = false,isOver = false;;
						for(int i = 0;i<=objs.size();i++){
							if(remove){
								i--;
							}
							if(i==objs.size()){
								i--;
								isOver = true;
							}
							Object[] o = objs.get(i);
							sheetNo = (String) o[0];
							orderNo = (String) o[1];
							planTime = (String) o[2];
							sx = (Integer) o[3];
							itemCode = (String) o[4];
							itemName = (String) o[5];
							productLine = (String) o[6];
							station = (String) o[7];
							stock = (String) o[8];
							qty = (Integer) o[9];
							isSps = (Integer) o[10];
							type = (String) o[11];
							c5 = (String) o[12];//器具名称
							loadage = (Integer) o[13];//器具满载量(针对时序件)

							if(c.equals(c5)){//每次只找同一型号的器具去装,理论该型号的容器满载量相同(数据维护混装满载量不同的不维护进去,否则结果将不准)
//								if(itemCode.equals("5110011B3")){
//									System.out.println(itemCode);
//								}
								ciQty = ci.get(c);//该容器目前已装载量
								avaliableQty = loadage-ciQty;//该容器目前剩余容量
								picQty = avaliableQty>qty?qty:avaliableQty;//BOM用量允许装入量
								ciQty += picQty;//累加装入量数量
								ci.put(c, ciQty);//装入容器
								qty -= picQty;
								avaliableQty = loadage-ciQty;//该容器目前剩余容量
								objs.remove(o);
								remove = true;
								if(qty > 0){//这类情形基本确定器具装满
									objs.add(new Object[]{
											sheetNo,orderNo,planTime,sx,itemCode,itemName,productLine,station,stock,qty,isSps,type,c5,loadage
									});
								}
								tempSps.add(new Object[]{
										sheetNo,orderNo,planTime,sx,itemCode,itemName,productLine,station,stock,picQty,isSps,type,c5,loadage,line
								});
								if(avaliableQty==0){//满了  
									boxTag = codeManager.generateLot();//器具标签
									cosSps.put(boxTag, tempSps);
									tempSps = new ArrayList<Object[]>();
									line = 0;//下一个容器重新排序
									ci.put(c, 0);//初始化装载量=0
								}
								line++;
							}else{
								remove = false;
							}
							if(isOver){
								break;
							}
						}
						if(avaliableQty>0 && tempSps.size()>0){//找遍了也没装满
							boxTag = codeManager.generateLot();//器具标签
							cosSps.put(boxTag, tempSps);
						}
					}
				}objs.clear();
				//排序件:连续的计划单号合并原则(中间任何一个单号中断,则重新开始)
				type = WmsMoveDocType.ORDER_PICKING;
				List<Object[]> orderobjs = new ArrayList<Object[]>();
				Iterator<Entry<String, List<Object[]>>> iii = psObjs.entrySet().iterator();
				while(iii.hasNext()){
					Entry<String, List<Object[]>> e = iii.next();
					orderObjs = e.getValue();

					Integer upLine = 1,hereLine = 1,absValue = 0;
					
					//构造一个开始匹配的料
					Map<String,String> itemFor = new HashMap<String, String>();//<itemcode,orderNo>
					Map<String,Integer> orderItemLine = new HashMap<String, Integer>();//<订单号#物料,行号>
					String upOrderNo = null,upKey = null,orderItem = null;
					//计算需要合并的合并量
					Integer lot = 0,line = 0;
					
					for(int i = 0;i<orderObjs.size();i++){//同一个产线+工位下的明细
						Object[] o = orderObjs.get(i);
						sheetNo = (String) o[0];
						orderNo = (String) o[1];
						planTime = (String) o[2];
						itemCode = (String) o[3];
						itemName = (String) o[4];
						productLine = (String) o[5];
						station = (String) o[6];
						stock = (String) o[7];
						qty = (Integer) o[8];
						isSps = (Integer) o[9];
						c5 = (String) o[10];//器具名称
//						System.out.println(orderNo+"="+itemName);
						
						key = e.getKey()+MyUtils.spilt1+orderNo;//产线+工位+订单号
						if(itemFor.containsKey(itemCode)){//同一个料
							//判断是否同一订单号
							upOrderNo = itemFor.get(itemCode);
							if(orderNo.equals(upOrderNo)){//相同订单号
								orderItem = orderNo+MyUtils.spilt1+itemCode;
								line = orderItemLine.get(orderItem);
							}else{//是否连续key的(订单号序号之差绝对值为1)
								hereLine = keyNo.get(key);//当前订单号序号
								upKey = e.getKey()+MyUtils.spilt1+upOrderNo;//上一个料的产线工位订单号key
								upLine = keyNo.get(upKey);//上一个序号
								absValue = Math.abs(hereLine-upLine);
								if(absValue==1){
									//跟新物料订单号,目的跟新序号
									itemFor.put(itemCode, orderNo);
									orderItem = upOrderNo+MyUtils.spilt1+itemCode;
									line = orderItemLine.get(orderItem);
								}else{
									//连续中断,作为不同序号的物料重新构造
									lot++;
									//跟新物料订单号,目的跟新序号
									itemFor.put(itemCode, orderNo);
									line = lot;
								}
							}
						}else{
							//新物料,重新构造
							lot++;
							itemFor.put(itemCode, orderNo);
							line = lot;
						}
						orderItem = orderNo+MyUtils.spilt1+itemCode;
						orderItemLine.put(orderItem, line);//每次都记录下该订单号下物料的序号
						orderobjs.add(new Object[]{
								sheetNo,orderNo,planTime,0,itemCode,itemName,productLine,station,stock,qty,isSps,type,c5,0,line
						});
					}
				}
				
				WmsMoveDocManager wmsMoveDocManager = (WmsMoveDocManager) applicationContext.getBean("wmsMoveDocManager");
				int PAGE_NUMBER = 200;
				int j = JavaTools.getSize(orderobjs.size(), PAGE_NUMBER);
				for(int k=0;k<j;k++){
					int toIndex = JavaTools.getIndex(k, orderobjs.size(), PAGE_NUMBER);
					List<Object[]> obj = JavaTools.getListObj(orderobjs, k, toIndex, PAGE_NUMBER);
					wmsMoveDocManager.mesxgBom("",obj);
				}orderobjs.clear();
				
				Iterator<Entry<String, List<Object[]>>>iiii = cosSps.entrySet().iterator();
				while(iiii.hasNext()){
					Entry<String, List<Object[]>> e = iiii.next();
					orderobjs = e.getValue();
					wmsMoveDocManager.mesxgBom(e.getKey(),orderobjs);
				}orderobjs.clear();
			}
		} catch (Exception e) {
			ierror = true;
			error = e.getMessage();
		}finally{
			if(ierror){
				System.out.println(error);
			}
		}
	}
	//wmsDealTaskManager.mesxgBomShipLot
	@SuppressWarnings("unchecked")
	public void mesxgBomShipLot(Long id){
		Boolean ierror = false;
		String error = "";
		WmsPickTicket pickTicket = commonDao.load(WmsPickTicket.class, id);
		String sheetNo = pickTicket.getRelatedBill1();//直送送货单单号(MES单号,和WMS接口的发货单匹配上之后更新)
		String sql = "SELECT ODR_NO as MES_NO,itme_code,product_line,item_name,SUM(qty) as qty,production_time " +
				" FROM "+MiddleTableName.W_BOM_MES+
				" WHERE ODR_NO ='"+sheetNo+"'"+
//				" WHERE to_char(production_time,'yyyy-MM-dd') = '2017-11-03'"+//test
//				" AND itme_code = '5110011B3'"+//test
				" AND qty>0" +
				" GROUP BY ODR_NO,itme_code,product_line,item_name,production_time" +
				" ORDER BY production_time,itme_code";
		try {
			List list = jdbcTemplateExt1.queryForList(sql);
			String orderNo;//bom里面维护订单号
			Date planTime;//计划日期(上线日期)
			String itemCode;//物料代码
			String itemName;//物料名称
			String productLine;//产线
			Integer qty;//数量
			
			List<Long> moveIds = commonDao.findByQuery("SELECT w.id FROM WmsMoveDoc w WHERE w.pickTicket.id =:pickTicket", "pickTicket", pickTicket.getId());
			if(moveIds!=null && moveIds.size()>0){
				for(Long moveId : moveIds){
					WmsMoveDoc wm = commonDao.load(WmsMoveDoc.class, moveId);
					wm.setTransStatus(WmsWorkDocStatus.FINISHED);
					commonDao.store(wm);
				}
			}
			
			if(list!=null && list.size()>0){
				List<Object[]> items = commonDao.findByQuery("SELECT pp.item.code,pp.item.class2 FROM WmsPickTicketDetail pp" +
						" WHERE pp.pickTicket.id =:pickTicket", "pickTicket", pickTicket.getId());
				if(items!=null && items.size()>0){
					Map<String,String> itemClass2 = new HashMap<String, String>();
					for(Object[] s : items){
						itemClass2.put((String)s[0], (String)s[1]);
					}items.clear();
					Map o1 = (Map) list.get(0);
					Map o2 = (Map) list.get(list.size()-1);
					Date d1 = (Date) o1.get("production_time");
					Date d2 = (Date) o2.get("production_time");
					System.out.println("开始时间:"+d1);
					System.out.println("结束时间:"+d2);
					int mins = JavaTools.getDistMinDates(d1, d2);
					System.out.println("总分钟:"+mins);
					Integer pro = pickTicket.getPriority();
					if(pro==null || pro==0){//默认最大四个批次执行  2H 设为全局
						String shiplot = GlobalParamUtils.getGloableStringValue("shiplot");
						if(StringUtils.isEmpty(shiplot) 
								|| !JavaTools.isNumber(shiplot)){
							pro = 120;
						}else{
							pro = Integer.valueOf(shiplot);
						}
					}
					int j = JavaTools.getSize(mins, pro);//总批次
					double l = j;
					pickTicket.setBoxQuantity(l);
					commonDao.store(pickTicket);
					System.out.println("总批次:"+j);
					
					int i= 1,lot = 1,totalTime = 0;
					System.out.println(lot);
					
					Date uptime = null;
					Map<String,Double> itemQty = new HashMap<String, Double>();;
					Double quantity = 0D;
					
					String key = null;
					Iterator iter = list.iterator();
					while(iter.hasNext()){
						Map m = (Map) iter.next();
						orderNo = (String) m.get("MES_NO");
						planTime = (Date) m.get("production_time");
						itemCode = (String) m.get("itme_code");
						itemName = (String) m.get("item_name");
						productLine = (String) m.get("product_line");
						qty = ((BigDecimal)m.get("qty")).intValue();
						if(!itemClass2.containsKey(itemCode)){//mes总计划中不存在的物料不进入本次发运计划计算
							continue;
						}
						if(i==1){
							uptime = planTime;
							mins = 0;
						}else{
							mins = JavaTools.getDistMinDates(uptime, planTime);
						}
						totalTime += mins;
						if(totalTime>= pro){//达到一个批次
							lot++;
							System.out.println(lot);
							totalTime = 0;
						}
//						else if(i>=list.size()){//未达到 pro的,最后作为一个批次(代码注释,逻辑会导致批次增加)
//							lot++;
//							System.out.println(lot);
//						}
						uptime = planTime;
						i++;
						//批次#物料编码#物料名称#物料属性#产线
						key = lot+MyUtils.spilt1+itemCode+MyUtils.spilt1+itemName+MyUtils.spilt1+itemClass2.get(itemCode)+MyUtils.spilt1+productLine;
						if(itemQty.containsKey(key)){
							quantity = itemQty.get(key);
						}else{
							quantity = 0D;
						}
						quantity += qty;
						itemQty.put(key, quantity);
					}list.clear();
					
					Map<String,Set<Integer>> itemLots = new HashMap<String, Set<Integer>>();
					Set<Integer> lotNos = null;
					Integer batch = 0;
					String[] keys = null;
					String class2 = null,productionLine = null;
					Iterator<Entry<String, Double>>  ii = itemQty.entrySet().iterator();	
					while(ii.hasNext()){
						Entry<String, Double> entry = ii.next();
						key = entry.getKey();//批次#物料编码#物料名称#物料属性#产线
						quantity = entry.getValue();//数量
						keys = key.split(MyUtils.spilt1);
						batch = Integer.valueOf(keys[0]);
						itemCode = keys[1];
						itemName = keys[2];
						class2 = keys[3];
						productionLine = keys[4];
						WmsShipLot picLot = new WmsShipLot(pickTicket, pickTicket.getRequireArriveDate(), batch, 
								itemCode, itemName, class2, quantity, productionLine);
						commonDao.store(picLot);
						
						if(itemLots.containsKey(itemCode)){
							itemLots.get(itemCode);
						}else{
							lotNos = new HashSet<Integer>();
						}
						lotNos.add(batch);
						itemLots.put(itemCode, lotNos);
					}itemQty.clear();
					
					//反向更新拣货单批次信息
					List<WmsMoveDocDetail> mms = commonDao.findByQuery("FROM WmsMoveDocDetail mm" +
							" WHERE mm.moveDoc.pickTicket.id =:pickTicket", "pickTicket", pickTicket.getId());
					if(mms!=null && mms.size()>0){
						for(WmsMoveDocDetail mm : mms){
							class2 = "";
							WmsItem item = commonDao.load(WmsItem.class,mm.getItem().getId());
							if(itemLots.containsKey(item.getCode())){
								lotNos = itemLots.get(item.getCode());
								for(Integer s : lotNos){
									class2 += (s+"/");
								}
								class2 = StringUtils.substringBeforeLast(class2, "/");
								mm.setReplenishmentArea(class2==null?"":class2.trim());
								commonDao.store(mm);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			ierror = true;
			error = e.getMessage();
		}finally{
			if(ierror){
				System.out.println(error);
			}
		}
	}
	public static void main(String[] args) {
		List<Object[]> o1 = new ArrayList<Object[]>();
		for(int i = 0 ; i <= 4 ; i++){
			o1.add(new Object[]{
					"a"+i,"b"+i,"c"+i
			});
		}
		int index = 0;
		Boolean remove = false;
		for(int i = 0;i<o1.size();i++){
			if(remove){
				i--;
			}
			Object[] o = o1.get(i);
			System.out.println(i+":"+o[0]+","+o[1]+","+o[2]);
			if(o[0].equals("a0") || o[0].equals("a2") || o[0].equals("a4")){
				o1.remove(o);
				remove = true;
			}else{
				remove = false;
			}
//			o1.remove(i);
			if(o[0].equals("a1") || o[0].equals("a3")){
				o1.add(new Object[]{
						"aa"+i,"bb"+i,"cc"+i
				});
			}
//			if(index==1){
//				break;
//			}
			
		}
		System.out.println(o1.size());
		for(Object[] o : o1){
			System.out.println(o[0]+","+o[1]+","+o[2]);
		}
		/*Map<String,String> map=new TreeMap<String,String>();
		map.put("1","Level1");
		map.put("3","Level3");
		map.put("2","Level2");
		map.put("a","Levela");
		map.put("c","Levelc");
		map.put("b","Levelb");
		Iterator <Entry<String,String>> it=map.entrySet().iterator();
		while(it.hasNext()){
			Entry<String,String> e=it.next();
			System.out.println(e.getKey()+","+e.getValue());
		}*/
	}
	/**时序件*/
	@SuppressWarnings("unchecked")
	public void dealSpsPickData(Long id){
		int i = 1;
		Integer lineNo = 10;
		Double totalQty = 0D;
		WmsPickTicket pickTicket = null;
		Date date = new Date();
		WmsWarehouse warehouse = getwareHouse();//仓库
		
		WHead w = commonDao.load(WHead.class, id);
		List<MiddleOrderSps> list = commonDao.findByQuery
				("from MiddleOrderSps a where a.head.id=:id", "id",w.getId());
		
		for(MiddleOrderSps sps : list){
			WmsOrganization company = commonDao.load(WmsOrganization.class, sps.getCompany().getId());
			if(i == 1){
				String code = codeManager.generateCodeByRule(warehouse, company.getName(), "发货单", sps.getBillType().getName());
				pickTicket = new WmsPickTicket(warehouse,company, sps.getBillType(), code, sps.getOdrNo(), 
								WmsPickTicketStatus.OPEN, sps.getDemandDate(), sps.getQty(), 0d, 0d, 0d,
								sps.getDware(), sps.getShdk(),WmsSource.INTERFACE,date,sps.getOdrSu(),sps.getBatch());
				commonDao.store(pickTicket);
			}
			WmsOrganization supplier = sps.getSupply();
			ShipLotInfo shipLotInfo = new ShipLotInfo
						(supplier == null ? null : supplier.getCode(),
								supplier == null ? null : supplier.getName());
			Double qty = sps.getQty().doubleValue();
			WmsPickTicketDetail detail = new WmsPickTicketDetail(pickTicket, sps.getItem(), 
									shipLotInfo,sps.getPackageUnit(),qty, qty, 0d, 0d, 0d, sps.getSlr(), 
									sps.getSlr(), sps.getProductLine(), sps.getStation(), sps.getIsJp(),
									lineNo*i,sps.getSx(),0d,sps.getPackageUnit().getUnit(),supplier,"-");
			detail.setType(sps.getPackageNo());
			detail.setPackageNum(sps.getPackageNum());
			detail.setPackageQty(sps.getPackageQty());
			detail.setRemark(sps.getRemark());
			detail.setFromSource(sps.getFromSource());
			commonDao.store(detail);
			
			i += 1;
			totalQty += qty;
		}
		updateTotalQty(pickTicket, null, totalQty);//更新单头数量
	}
	
	public void confirmAccount(Long asnId){
		WmsASNManager wmsASNManager = (WmsASNManager) applicationContext.getBean("wmsASNManager");
		wmsASNManager.confirmAccount(commonDao.load(WmsASN.class, asnId));
	}
}
