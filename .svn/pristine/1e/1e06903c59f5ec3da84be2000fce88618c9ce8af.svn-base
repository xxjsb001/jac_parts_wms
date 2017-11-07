package com.vtradex.wms.server.service.middle.pojo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTransactionManager;

import com.vtradex.engine.utils.DateUtils;
import com.vtradex.sequence.service.sequence.SequenceGenerater;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.message.Task;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.Contact;
import com.vtradex.wms.server.model.base.LotInfo;
import com.vtradex.wms.server.model.base.ShipLotInfo;
import com.vtradex.wms.server.model.billing.WMSBillDetail;
import com.vtradex.wms.server.model.billing.WMSBillingCategory;
import com.vtradex.wms.server.model.billing.WMSBillingModelInterface;
import com.vtradex.wms.server.model.billing.WMSContact;
import com.vtradex.wms.server.model.billing.WMSPurchaseInvoiceDetail;
import com.vtradex.wms.server.model.billing.WMSPurchaseStatus;
import com.vtradex.wms.server.model.interfaces.HeadType;
import com.vtradex.wms.server.model.interfaces.MiddleAsnSrmDetail;
import com.vtradex.wms.server.model.interfaces.WHead;
import com.vtradex.wms.server.model.middle.MesMisInventory;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsMoveDocShipStatus;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.model.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.shipping.WmsPickTicket;
import com.vtradex.wms.server.model.shipping.WmsPickTicketDetail;
import com.vtradex.wms.server.model.shipping.WmsPickTicketStatus;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.service.middle.MiddleTableName;
import com.vtradex.wms.server.service.middle.MilldleSessionManager;
import com.vtradex.wms.server.service.receiving.WmsMoveDocManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.shipping.WmsPickTicketManager;
import com.vtradex.wms.server.service.workDoc.WmsWorkDocManager;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;

public class DefaultmilldleSessionManager extends DefaultBaseManager implements MilldleSessionManager{
	
	protected WmsBussinessCodeManager codeManager;
	protected WmsWorkDocManager wmsWorkDocManager;
	protected WmsPickTicketManager wmsPickTicketManager;
	protected WmsMoveDocManager moveDocManager;
	public DefaultmilldleSessionManager(WmsBussinessCodeManager codeManager,WmsWorkDocManager wmsWorkDocManager
			,WmsPickTicketManager wmsPickTicketManager,WmsMoveDocManager moveDocManager){
		this.codeManager = codeManager;
		this.wmsWorkDocManager = wmsWorkDocManager;
		this.wmsPickTicketManager = wmsPickTicketManager;
		this.moveDocManager  = moveDocManager;
	}
	/* (non-Javadoc)
	 * @see com.vtradex.wms.server.service.middle.MilldleSessionManager#sysMiddleSupplier(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public void sysMiddleSupplier(Object[] obj){
		Boolean isNew = (Boolean) obj[0];
		List<Object[]> ret = (List<Object[]>) obj[1];
		if(isNew){
			WmsOrganization sup = null;
			for(Object[] o : ret){
				sup = EntityFactory.getEntity(WmsOrganization.class);
				sup.setCode(o[2].toString());
				sup.setBeSupplier(Boolean.TRUE);
				sup.setBeCustomer(Boolean.TRUE);
				sup.setStatus(BaseStatus.ENABLED);
				editSup(sup, o);
			}
		}else{
			List<String> retCodes = (List<String>) obj[2];
			List<WmsOrganization> suppers = commonDao.findByQuery("FROM WmsOrganization o"
					+ " WHERE o.code in ("+StringUtils.substringBetween(retCodes.toString(), "[", "]")+")");
			Map<String,WmsOrganization> supCode = new HashMap<String,WmsOrganization>();
			for(WmsOrganization o : suppers){
				supCode.put(o.getCode(), o);
			}
			suppers.clear();
			String key = null;
			WmsOrganization sup = null;
			for(Object[] o : ret){
				key = o[2].toString();
				if(supCode.containsKey(key)){
					sup = supCode.get(key);
					editSup(sup, o);
				}
			}
			supCode.clear();
		}
	}
	@SuppressWarnings("unchecked")
	public void sysMiddleMaterial(Object[] obj){
		Boolean isNew = (Boolean) obj[0];
		List<Object[]> ret = (List<Object[]>) obj[1];
		if(isNew){
			WmsOrganization company = (WmsOrganization) obj[2];
			WmsItem item = null;
			for(Object[] o : ret){
				item = EntityFactory.getEntity(WmsItem.class);
				item.setCode( o[2].toString());
				item.setStatus(BaseStatus.ENABLED);
				item.setCompany(company);
				item.setLotRule(company.getLotRule());
				editItem(item, null, o);
			}
		}else{
			Map<String,WmsItem> itemCode = (Map<String, WmsItem>) obj[2];
			Map<String,WmsPackageUnit> puCode = (Map<String, WmsPackageUnit>) obj[3];
			String key = null;
			WmsItem item = null;
			for(Object[] o : ret){
				key = o[2].toString();//零部件编码
				item = itemCode.get(key);
				
				WmsPackageUnit pu = null;
				if(puCode!=null && puCode.containsKey(key)){
					pu = puCode.get(key);
				}
				editItem(item, pu, o);
			}
		}
	}
	@SuppressWarnings("unchecked")
	public Object[] sysMiddleDeliverydoc(Object[] obj){
		WmsOrganization company = (WmsOrganization) obj[0];
		WmsWarehouse warehouse = (WmsWarehouse) obj[1];
		WmsBillType bill = (WmsBillType) obj[2];
		Entry<String, List<Object[]>>  entry = (Entry<String, List<Object[]>>) obj[3];
		Map<String,WmsItem> itemMaps = (Map<String, WmsItem>) obj[4];
		Map<String,WmsPackageUnit> puMaps = (Map<String, WmsPackageUnit>) obj[5];
		Map<String,WmsOrganization> supMaps = (Map<String, WmsOrganization>) obj[6];
		
		List<Object[]> details = entry.getValue();
		
		Boolean isError = false;
		WmsItem item = null;
		WmsPackageUnit pu = null;
		WmsOrganization supplier = null;
		List<Long> errorPic = new ArrayList<Long>();
		for(Object[] d : details){
			item = itemMaps.get(d[21].toString());
			pu = puMaps.get(d[21].toString());
			supplier = supMaps.get(d[20].toString());
			if(item==null || pu==null || supplier==null){
				isError = true;//物料、单位、供应商信息缺失,当前发货单信息全部作废
			}
			errorPic.add(Long.parseLong(d[0].toString()));
		}
		WmsPickTicket pic = null;
		if(!isError){
			pic = EntityFactory.getEntity(WmsPickTicket.class);
			for(Object[] o : details){
				pic.setWarehouse(warehouse);
				pic.setCompany(company);
				pic.setBillType(bill);
				pic.setRelatedBill1(o[2].toString());//DELIVERY_CODE
				pic.setKbCode(o[3]==null?"":o[3].toString());//JIT_CODE-看板编码
				pic.setLdType(o[4]==null?"":o[4].toString());//PULL_TYPE-拉动方式
				pic.setReceiveFactory(o[5]==null?"":o[5].toString());//DESTINATION_PLANT-5收货工厂
				pic.setReceiveDoc(o[6]==null?"":o[6].toString());//DESTINATION_DOCK-6收货道口
				pic.setReceiveHouse(o[7]==null?"":o[7].toString());//DESTINATION_WAREHOUSE-7收货仓库
				pic.setSender(o[8]==null?"":o[8].toString());//SENDER
				pic.setRequireArriveDate((Date) o[9]);//REQUIRED_TIME
				pic.setDescription(o[17]==null?"":o[17].toString());//MES_HEAD_MEMO
				pic.setOrderDate((Date) o[19]);//CREATE_TIME
				pic.setSendTime((Date) o[30]);//SEND_TIME
				pic.setStatus(WmsPickTicketStatus.OPEN);
				String code = null;
				try {
					code = codeManager.generateCodeByRule(warehouse, company.getName(), "发货单", bill.getName());
				} catch (Exception e) {
					//编码 = 仓库编码 + 编码 + 格式化日期($现在,"yyMMdd")
					code = warehouse.getCode()+"ON"+DateUtils.format(new Date(),"yyMMdd");
					SequenceGenerater ss = (SequenceGenerater) applicationContext.getBean("sequenceGenerater");
					code = ss.generateSequence(code, 6);	
				}
				pic.setCode(code);
				commonDao.store(pic);
				break;
			}
		}

		List<Long> succesPic = new ArrayList<Long>();
		Double expectedQuantityBU = 0D;
		String key = null,hashCode=null;
		Map<String,Double> detailQty = new HashMap<String, Double>();
		Map<String,WmsPickTicketDetail> detail = new HashMap<String, WmsPickTicketDetail>();
		Boolean beActive = true;
		if(!isError){
			//DELIVERY_CODE,SUPPLIER_CODE,MATERIAL_CODE,MODEL(CANCLE),SENDER,MES_DETAIL_MEMO==REQUIRED_QTY,HASH_CODE
			int lineNo = 1;
			for(Object[] d : details){
				WmsPickTicketDetail ptd = null;
				key = d[2]+MyUtils.spilt1+d[20]+MyUtils.spilt1+d[21]+MyUtils.spilt1+d[8]+MyUtils.spilt1+d[29];//MyUtils.spilt1+d[24]+
				if(detailQty.containsKey(key)){
					expectedQuantityBU = detailQty.get(key)+Double.valueOf(d[25].toString());
					ptd = detail.get(key);
					
					ptd.setExpectedQuantityBU(expectedQuantityBU);//REQUIRED_QTY
					ptd.setExpectedQuantity(expectedQuantityBU);//REQUIRED_QTY
					commonDao.store(ptd);
					pic.refreshPickTicketQty();
				}else{
					expectedQuantityBU = Double.valueOf(d[25].toString());
					ptd = EntityFactory.getEntity(WmsPickTicketDetail.class);

					hashCode = BeanUtils.getFormat(key.split(MyUtils.spilt1));//d[2],d[20],d[21],d[24],d[8],d[29]
					ptd.setPickTicket(pic);
					item = itemMaps.get(d[21].toString());
					pu = puMaps.get(d[21].toString());
					supplier = supMaps.get(d[20].toString());
					ptd.setItem(item);
					ptd.setPackageUnit(pu);
					ptd.setSupplier(supplier);
					ptd.setExpectedQuantityBU(expectedQuantityBU);//REQUIRED_QTY
					ptd.setExpectedQuantity(expectedQuantityBU);//REQUIRED_QTY
					ptd.setLineNo(lineNo);
					ptd.setInventoryStatus(BaseStatus.NULLVALUE);
					ptd.setHashCode(hashCode);
					ptd.setDescription(d[29]==null?null:d[29].toString());
					ShipLotInfo shipLotInfo = new ShipLotInfo();
					//20150707当工艺状态为空时,发货单激活要提示选择工艺状态(变更)
					//20150722按照#分割,截取需要的备注信息,如果截取到可用信息则工艺状态就按照定义取值,如果找不到则默认-,都激活(变更)(二次)
					if(ptd.getDescription()==null){
						shipLotInfo.setExtendPropC1(BaseStatus.NULLVALUE);
					}else{
						String[] ss = ptd.getDescription().split(MyUtils.spilt1);//[xx,yy,zz,cc,....]
						String extendPropC1 = null;
						for(String s : ss){
							extendPropC1 = MyUtils.checkExtendPropc1Back(s);
							if(extendPropC1!=null){
								break;
							}
						}
						if(extendPropC1==null){
							shipLotInfo.setExtendPropC1(BaseStatus.NULLVALUE);
						}else{
							shipLotInfo.setExtendPropC1(extendPropC1);
						}
					}
					shipLotInfo.setSupplier(ptd.getSupplier().getCode());
					ptd.setShipLotInfo(shipLotInfo);
					commonDao.store(ptd);
					lineNo++;
					pic.addPickTicketDetail(ptd);
					//20150719MES接口信息备注明细如果任意存在一条有备注信息的，发货单不激活。同一个发货单下的所有明细都为空的才激活(变更)
//					if(beActive){
//						beActive = ptd.getDescription()==null?true:false;
//					}
				}
				detailQty.put(key, expectedQuantityBU);
				detail.put(key, ptd);
				succesPic.add(Long.parseLong(d[0].toString()));
			}
		}
		if(!isError){
			errorPic.clear();
		}else{
			succesPic.clear();
			beActive = false;
		}
		return new Object[]{
				succesPic,errorPic,beActive,pic==null?null:pic.getId()
		};
	}
	@SuppressWarnings("unchecked")
	public void sysPickShip(Object[] obj){
		List<WmsTask> tasks =  (List<WmsTask>) obj[0];
		Map<String,Double> detailHashCodeQty = (Map<String, Double>) obj[1];
		WmsPickTicketDetail pickTicketDetail =null;
		WmsMoveDocDetail moveDocDetail = null;
		WmsMoveDoc moveDoc = null;
		Double shipQty = 0.0;
		for(WmsTask task : tasks){
			moveDocDetail = commonDao.load(WmsMoveDocDetail.class, task.getMoveDocDetail().getId());
			pickTicketDetail = this.commonDao.load(WmsPickTicketDetail.class, 
					moveDocDetail.getRelatedId());
			moveDoc = commonDao.load(WmsMoveDoc.class, moveDocDetail.getMoveDoc().getId());
			shipQty = detailHashCodeQty.get(pickTicketDetail.getHashCode());
			wmsWorkDocManager.pickShipByTask(task, shipQty>task.getUnShipQuantityBU()?task.getUnShipQuantityBU():shipQty, moveDoc);
			moveDoc.setShipStatus(WmsMoveDocShipStatus.SHIPPED);
			commonDao.store(moveDoc);
		}
	}
	private void editItem(WmsItem item,WmsPackageUnit pu,Object[] o){
		item.setName(o[3].toString());//零部件名称3
		//零部件名称简称4
		item.setBaseUnit(o[5].toString());//基本单位5
		item.setClass1(o[6]==null?"":o[6].toString());//物料类型6
		item.setDescription(o[7]==null?"":o[7].toString());//描述7
		commonDao.store(item);
		
		if(pu==null){
			pu = EntityFactory.getEntity(WmsPackageUnit.class);
			pu.setItem(item);
			pu.setUnit(item.getBaseUnit());
			pu.setConvertFigure(1);
			pu.setDescription("");
			pu.setHeight(0D);
			pu.setLength(0D);
			pu.setLevel("箱");
			pu.setLineNo(1);
			pu.setVolume(0D);
			pu.setWeight(0D);
			pu.setWidth(0D);
			commonDao.store(pu);
		}else{
			HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
			Session session = transactionManager.getSessionFactory().getCurrentSession();
			pu.setUnit(item.getBaseUnit());
			session.merge(pu);
		}
	}
	private void editSup(WmsOrganization sup,Object[] o){
		sup.setName(o[3].toString());//供应商名称3
		Contact contact = sup.getContact();
		if(contact==null){
			contact = new Contact();
		}
		contact.setCountry(o[4]==null?"":o[4].toString());//国家4
		contact.setProvince(o[5]==null?"":o[5].toString());//省份5
		contact.setCity(o[6]==null?"":o[6].toString());//城市6
		contact.setPostCode(o[7]==null?"":o[7].toString());//邮编7
		contact.setContactName(o[8]==null?"":o[8].toString());//联系人8
		contact.setTelephone(o[9]==null?"":o[9].toString());//联系电话9
		contact.setMobile(o[10]==null?"":o[10].toString());//手机号10
		contact.setFax(o[11]==null?"":o[11].toString());//传真号码11
		contact.setEmail(o[12]==null?"":o[12].toString());//邮箱账号12
//		contact.setAddress(o[13]==null?"":o[13].toString());//联系地址13,有核算部负者更新(瑶瑶&王毅)
		sup.setContact(contact);
		sup.setDescription(o[14]==null?"":o[14].toString());//描述14
		commonDao.store(sup);
	}
	public Object[] readMidQuality(List<Object[]> objs,List<WmsMoveDocDetail> mdds,
			Map<String,String[]> midStatus,List<String> itemStatus){
		List<Long> errStatus = new ArrayList<Long>();//质检状态不能正确匹配WMS
		List<Long> errBackQty = new ArrayList<Long>();//返回数量有误
		List<Long> succIds = new ArrayList<Long>();//返回正常执行完毕数据
		
		String s1=null,s2=null,s3=null,s4=null,s5=null,key = null,key1 = null,status=null;
		Double d1 =0D,d2=0D,d3=0D,d4=0D,total = 0D;
		List<Long> removeDD = new ArrayList<Long>();
		for(Object[] obj : objs){
			status = (String) obj[6];//mes质检状态
			d1 = obj[9]==null?0D:Double.valueOf(obj[9].toString());//返回数量
			d2 = obj[10]==null?0D:Double.valueOf(obj[10].toString());//报废数量
			d3 = obj[8]==null?0D:Double.valueOf(obj[8].toString());//送检数量
			if(!midStatus.containsKey(status)){
				errStatus.add(Long.parseLong(obj[0].toString()));
				continue;
			}
			if(!itemStatus.contains(midStatus.get(status)[0])){
				errStatus.add(Long.parseLong(obj[0].toString()));
				continue;
			}
			if(!itemStatus.contains(midStatus.get(status)[1])){
				errStatus.add(Long.parseLong(obj[0].toString()));
				continue;
			}
			if(d1<0||d1>d3||d1+d2>d3||d1+d2<d3
					||d2<0||d2>d3){
				errBackQty.add(Long.parseLong(obj[0].toString()));
				continue;
			}
			key = obj[3]+MyUtils.spilt1+obj[4]+MyUtils.spilt1+obj[5]+MyUtils.spilt1+obj[7]+MyUtils.spilt1+obj[13];
			for(WmsMoveDocDetail mm : mdds){
				if(removeDD.contains(mm.getId())){
					continue;
				}
				mm = commonDao.load(WmsMoveDocDetail.class, mm.getId());
				s1 = mm.getItemKey().getLotInfo().getSoi();
				s2 = mm.getItemKey().getLotInfo().getSupplier().getCode();
				s3 = mm.getItem().getCode();
				s4 = mm.getItemKey().getLotInfo().getExtendPropC1();//工艺状态
				s5 = mm.getReplenishmentArea();//MES质检单号
				
				s4 = s4==null?"-":s4;
				key1 = s1+MyUtils.spilt1+s2+MyUtils.spilt1+s3+MyUtils.spilt1+s4+MyUtils.spilt1+s5;
				if(key.equals(key1)){
					total = mm.getPlanQuantityBU();
					if(d1>0 && total>0){
						d4 = d1>=total?total:d1;
						moveDocManager.readMidQuality(mm, d4, true, midStatus.get(status)[0]);
						d1 -= d4;
						total = mm.getPlanQuantityBU()-d4;
					}
					
					if(d2>0 && total>0){
						d4 = d2>=total?total:d2;
						moveDocManager.readMidQuality(mm, d4, false, midStatus.get(status)[1]);
						d2 -= d4;
						total -= d4;
					}
					removeDD.add(mm.getId());
				}
			}
			succIds.add(Long.parseLong(obj[0].toString()));
		}
		return new Object[]{
				errStatus,errBackQty,succIds
		};
	}
	public Object[] sysASNSrm(String key,List<Object[]> objs,WmsBillType billType,WmsOrganization company,
			WmsWarehouse warehouse,WmsOrganization supplier,Map<String,WmsItem> itemMaps,Map<String,WmsPackageUnit> puMaps){
		String errorkey = null;
		
		WmsItem item = null;
		WmsPackageUnit packageUnit = null;
		for(Object[] obj : objs){
			item = itemMaps.get(obj[7]);
			packageUnit = puMaps.get(obj[7]);
			if(item==null || packageUnit==null){
				errorkey = key;
				return new Object[]{
						errorkey
				};
			}
			Boolean isNumber = JavaTools.isNumber(obj[4]==null?"-":obj[4].toString());
			if(!isNumber){
				errorkey = key;
				return new Object[]{
						errorkey
				};
			}
		}
		String[] keys = key.split(MyUtils.spilt1);
		WmsASN asn = EntityFactory.getEntity(WmsASN.class);
		asn.setBillType(billType);
		String code = null;
		try {
			//编码 = 仓库编码 + 编码 + 格式化日期($现在,"yyMMdd")
			code = warehouse.getCode()+"IN"+DateUtils.format(new Date(),"yyMMdd");
			SequenceGenerater ss = (SequenceGenerater) applicationContext.getBean("sequenceGenerater");
			code = ss.generateSequence(code, 6);	
		} catch (Exception e) {
		}
		asn.setCode(code);
		asn.setRelatedBill1(keys[0]);
		asn.setRelatedBill2(keys[1]);
		asn.setSupplier(supplier);
		asn.setOrderDate(new Date());
		asn.setEstimateDate(JavaTools.stringToDate(keys[3]));
		asn.setWarehouse(warehouse);
		asn.setCompany(company);
		commonDao.store(asn);
		Integer lineNo = 1;
		Double quantity = 0.0D,expectedQuantity = 0D;
		for(Object[] obj : objs){
			WmsASNDetail wad = EntityFactory.getEntity(WmsASNDetail.class);
			wad.setAsn(asn);
			item = itemMaps.get(obj[7]);
			packageUnit = puMaps.get(obj[7]);
			expectedQuantity = JavaTools.stringToDouble(obj[4]==null?"-":obj[4].toString());
			wad.setExpectedQuantity(expectedQuantity);
			wad.setExpectedQuantityBU(expectedQuantity);
			wad.setItem(item);
			wad.setPackageUnit(packageUnit);
			wad.setLineNo(lineNo);
			LotInfo lotInfo = new LotInfo();
			lotInfo.setSoi(asn.getCode());
			lotInfo.setSupplier(supplier);
			wad.setLotInfo(lotInfo);
			commonDao.store(wad);
			lineNo++;
			quantity += wad.getExpectedQuantityBU();
		}
		asn.setExpectedQuantityBU(quantity);
		commonDao.store(asn);
		
		return new Object[]{
				errorkey
		};
	}
	
	/**
	 * 
	 * "select ID,BILL_ID,BILL_CODE,BE_INCOME,EXPENSE,GROUP_NO1,GROUP_NO2,HAPPEN_DATE," +
					" FEE_CATEGORY_NAME,SUPCODE,SUPNAME,ITEMCODE,BILLING_MODEL,WAREHOUSEID_CONTACT_POCODE," +
					" QTY_AMOUNT,FIXED_PRICE,CREATED_TIME,STATUS,FLAG "+
					"from "+MiddleTableName.FDJ_BMS_FEE_DATA_VIEW+
					" WHERE flag = 0";
	 * **/
	public Map<String , List<Object>>  getFeedIdsAndInsertBillDetail(List<Object[]> feeDataList){
		List<Object> sucessFeeDataIds= new ArrayList<Object>();
		List<Object> failFeeDataIdAndError =new ArrayList<Object>();
		Map<String , List<Object>>  totalFeedDataMap = new HashMap<String,List<Object>>();
		WMSBillDetail  detail = null;
		StringBuffer errorInfo= null;
		Double amount=0D,fixedPrice=0D,historyAmount=0D,qty_amount=0D;
		String billingModel= null,smallCategoryName= null,materialCode= null,supplierCode = null,poCode= null,contactCode = null,complexStr= null,warehouseCode = null;
		Long feeDataId= null;
		Date happenDate = null,createTime= null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Object[] feeData : feeDataList){
			if(null != errorInfo){
				failFeeDataIdAndError.add(feeDataId.toString()+"#"+errorInfo);
				errorInfo = null;
			}
			errorInfo=new StringBuffer();
			detail = new WMSBillDetail();
			try {
				feeDataId = Long.valueOf(feeData[0].toString());
				amount = Double.valueOf(feeData[4] == null ? "0":feeData[4].toString());
				fixedPrice = Double.valueOf( feeData[15].toString());
				historyAmount = amount;
				qty_amount = Double.valueOf(feeData[14].toString());
				billingModel = (String) feeData[12];
				if(WMSBillingModelInterface.MONTH.equals(billingModel) && 0 >= qty_amount){
					qty_amount=1d;
				}
				smallCategoryName = (String) feeData[8];
				materialCode = (String) feeData[11];
				supplierCode = (String) feeData[9];
				complexStr = (String) feeData[13];
				happenDate = dateFormat.parse(feeData[7].toString());
				createTime = dfm.parse(feeData[16].toString());
				String[]complex=complexStr.split("#");
				if(0 == complex.length){
					errorInfo.append("WAREHOUSEID_CONTACT_POCODE为空&");
					continue;
				}
				warehouseCode =complex[0].toString();
				contactCode = complex[1];
				if(2 < complex.length){
					poCode = complex[2];
				}
			} catch (Exception e) {
				errorInfo.append("数据解析异常&");
				continue;
			}
			
			WmsWarehouse wh =  (WmsWarehouse)this.commonDao.findByQueryUniqueResult("from WmsWarehouse wh where wh.code=:code", "code", warehouseCode);
			if(null == wh){
				errorInfo.append("仓库代码错误&");
				continue;
			}
			WMSContact contact = (WMSContact)this.commonDao.findByQueryUniqueResult(" from  WMSContact contact where contact.code=:code and contact.supplier.code=:supplierCode",
					new String[]{"code","supplierCode"}, new Object[]{contactCode,supplierCode});
			if(null == contact){
				errorInfo.append("合同号错误&");
				continue;
			}
			
			detail.setWarehouse(wh);
			detail.setWmsWarehouseCode(wh.getCode());
			detail.setWmsContactCode(contactCode);
			detail.setPurchaseInvoiceCode(poCode);
			detail.setLfcsCreateTime(createTime);
			detail.setLfcsDataId(feeDataId);
			detail.setAmount(amount);
			detail.setHistoryAmount(historyAmount);
			detail.setBillfromType("LFCS");
			detail.setBillingModel(billingModel);
			List<WMSBillingCategory> categories = (List<WMSBillingCategory>)this.commonDao.findByQuery("from WMSBillingCategory category where category.name=:name",
					"name", smallCategoryName);
			if(0  == categories.size()){
				errorInfo.append("费用类型名称错误&");
				continue;
			}
			detail.setBillingSmallCategory(smallCategoryName);
			
			detail.setFixedPrice(fixedPrice);
		    if(0 < detail.getFixedPrice()){
		    	detail.setAmount(detail.getFixedPrice());
			}
			detail.setHistoryAmount(detail.getAmount());
			detail.setHappenDate(happenDate);
			detail.setMaterialCode(materialCode);
			detail.setQty_Amount(qty_amount);
			if(0 >= detail.getAmount() || 0 >=  detail.getQty_Amount()){
				errorInfo.append("金额或者数量错误&");
				continue;
			}
			detail.setRate(detail.getAmount()/detail.getQty_Amount());
			detail.setStatus("UNCHECKED");
			WmsOrganization org = (WmsOrganization)this.commonDao.findByQueryUniqueResult("from WmsOrganization org where org.code=:code", "code", supplierCode);
			detail.setSupplier(org);
			detail.setSupplierName(org.getName());
			detail.setSupplierCode(supplierCode);
			
			if(StringUtils.isEmpty(errorInfo.toString())){
				detail.setCode(this.codeManager.getWmsBillDetailCode("LFCS", supplierCode));
				this.commonDao.store(detail);
				if(!StringUtils.isEmpty(detail.getPurchaseInvoiceCode())&&WMSBillingModelInterface.SALE_AMOUNT.equals(detail.getBillingModel()) ){
					String poErrorInfo = this.updatePoDetailByBillDetail(detail, "&");
					if(null != poErrorInfo){
						errorInfo.append(poErrorInfo);
						this.commonDao.delete(detail);
					}
				}
				
			}
			if(StringUtils.isEmpty(errorInfo.toString())){
				sucessFeeDataIds.add(feeDataId.toString());
			}else{
				failFeeDataIdAndError.add(feeDataId.toString()+"#"+errorInfo);
			}
			errorInfo = null;
		}
		if(0 < sucessFeeDataIds.size()){
			totalFeedDataMap.put("success", sucessFeeDataIds);
		}
		if(0 < failFeeDataIdAndError.size()){
			totalFeedDataMap.put("error", failFeeDataIdAndError);
		}
		return totalFeedDataMap;
	}
	
	
	@SuppressWarnings({ "unchecked" })
	public String  updatePoDetailByBillDetail(WMSBillDetail detail , String split){
		//采购发票关联账单明细
		if(!StringUtils.isEmpty(detail.getPurchaseInvoiceCode())){
			String[]poList = detail.getPurchaseInvoiceCode().split(split);
			StringBuffer poCodes = new StringBuffer();
			for(int count =0 ; count < poList.length;count++){
				poCodes.append("'");
				poCodes.append(poList[count]);
				if(count +1 == poList.length){
					poCodes.append("'");
				}else{
					poCodes.append("',");
				}
			}
			List<WMSPurchaseInvoiceDetail> poDetails= (List<WMSPurchaseInvoiceDetail>)this.commonDao.findByQuery("from WMSPurchaseInvoiceDetail detail " +
					" where detail.warehouse.code=:whCode and detail.supplier.code=:supplierCode  and detail.status=:status and detail.code in ("+ poCodes.toString()+")",
					new String[]{"whCode","supplierCode","status"},
					new Object[]{detail.getWmsWarehouseCode(),detail.getSupplierCode(),WMSPurchaseStatus.SENDED});
			
			if(poDetails.size() != poList.length ){
				return "采购发票号错误#";
			}
			if(0 < poDetails.size()){
				for(WMSPurchaseInvoiceDetail po : poDetails){
					po.setBillDetailId(detail.getId());
					po.setStatus(WMSPurchaseStatus.BACKED);
					this.commonDao.store(po);
				}
			}
		}
		return null;
	}
	public void updateHashCode(List<Object> ptds){
		if(ptds!=null && ptds.size()>0){
			String hashCode = null,key = null;
			for(Object obj : ptds){
				WmsPickTicketDetail ptd = (WmsPickTicketDetail) obj;
				WmsPickTicket pickTicket = commonDao.load(WmsPickTicket.class, ptd.getPickTicket().getId());
//				WmsOrganization supplier = commonDao.load(WmsOrganization.class, ptd.getSupplier().getId());
				WmsItem item = commonDao.load(WmsItem.class, ptd.getItem().getId());
				//DELIVERY_CODE,SUPPLIER_CODE,MATERIAL_CODE,MODEL(CANCLE),SENDER,MES_DETAIL_MEMO
				key = pickTicket.getRelatedBill1()+MyUtils.spilt1+ptd.getShipLotInfo().getSupplier()
						+MyUtils.spilt1+item.getCode()+MyUtils.spilt1+pickTicket.getSender()+MyUtils.spilt1+ptd.getDescription();
				hashCode = BeanUtils.getFormat(key.split(MyUtils.spilt1));
				ptd.setHashCode(hashCode);
				commonDao.store(ptd);
			}
		}
	}
	public void initMesMisInventory(List<Object[]> objs,Integer lot){
		for(Object[] obj : objs){////供应商编码,供应商名称,物料编码,物料名称,期初量,入库量,MES要货量
			MesMisInventory mis = EntityFactory.getEntity(MesMisInventory.class);
			mis.setSupCode(obj[0].toString());
			mis.setSupName(obj[1].toString());
			mis.setItemCode(obj[2].toString());
			mis.setItemName(obj[3].toString());
			mis.setInitQty(Double.valueOf(obj[4].toString()));
			mis.setAsnQty(Double.valueOf(obj[5].toString()));
			mis.setMesQty(Double.valueOf(obj[6].toString()));
			
			mis.setQuantity(mis.getInitQty()+mis.getAsnQty()-mis.getMesQty());
			mis.setCalQuantity(mis.getQuantity());
			mis.setLot(lot);
			mis.setLotDate(new Date());
			commonDao.store(mis);
		}
	}
	public void saveAsnDataToWms(List<Object[]> tempDatas,WmsWarehouse warehouse,
			String tableName){
		
		WHead w = new WHead(1, new Date(), "ASN");
		commonDao.store(w);
		for(Object[] obj : tempDatas){
//			Long id = (Long) obj[0];
			String relateBill1 = (String) obj[1];//送货单号
			String orderNo = obj[2].toString().toUpperCase();//订单号
			Double qty =  (Double) obj[7];//数量
			Date demandDate = (Date) obj[8];//生产日期
			Boolean isMt = obj[10].equals("1") ? Boolean.TRUE : Boolean.FALSE;//是否码托
			Integer palletNo = (Integer) obj[11];//托盘总个数
			WmsOrganization company = (WmsOrganization) obj[5] ;//货主
			WmsItem item = (WmsItem)obj[3] ;//货品
			WmsOrganization supplier = (WmsOrganization) obj[4] ;//供应商
			WmsBillType type = (WmsBillType) obj[9] ;//单据类型
			WmsPackageUnit packageUnit = (WmsPackageUnit) obj[6];//包装
			
			MiddleAsnSrmDetail mas = new MiddleAsnSrmDetail(relateBill1, orderNo, 
							item, supplier, company, packageUnit,
					qty, demandDate, type, isMt, palletNo,w);
			if(tableName.equals(MiddleTableName.W_ASN_SRM)){
				Integer polineNo = (Integer) obj[12];//
				Integer asnLineNo = (Integer) obj[13];
				mas.setPolineNo(polineNo);
				mas.setAsnLineNo(asnLineNo);
			}
			
			commonDao.store(mas);
		}	
		
		Task task = new Task(HeadType.ASN, 
				"wmsDealTaskManager"+MyUtils.spiltDot+"dealAsn", w.getId());
		commonDao.store(task);
	}
}
