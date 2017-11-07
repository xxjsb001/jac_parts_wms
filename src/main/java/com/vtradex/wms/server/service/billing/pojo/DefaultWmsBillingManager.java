/**   
* @Title: DefaultWmsBillingManager.java
* @Package com.vtradex.wms.server.service.billing.pojo
* @Description: TODO(用一句话描述该文件做什么)
* @author abyss
* @date 2015-9-17 上午09:37:12
* @version V1.0
*/ 
package com.vtradex.wms.server.service.billing.pojo;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.hsqldb.lib.StringUtil;

import com.vtradex.engine.utils.DateUtils;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.exception.ExceptionLog;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.billing.WMSBillDetail;
import com.vtradex.wms.server.model.billing.WMSBillDetailWrap;
import com.vtradex.wms.server.model.billing.WMSBillingCategory;
import com.vtradex.wms.server.model.billing.WMSBillingCategoryTypeInterface;
import com.vtradex.wms.server.model.billing.WMSContact;
import com.vtradex.wms.server.model.billing.WMSContactDetail;
import com.vtradex.wms.server.model.billing.WMSInvoice;
import com.vtradex.wms.server.model.billing.WMSInvoiceDetailCategory;
import com.vtradex.wms.server.model.billing.WMSInvoiceStatusInterface;
import com.vtradex.wms.server.model.billing.WMSPurchaseInvoiceDetail;
import com.vtradex.wms.server.model.billing.WMSPurchaseStatus;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.service.GlobalParamUtils;
import com.vtradex.wms.server.service.billing.WmsBillingManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.utils.SortRule;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

public class DefaultWmsBillingManager extends DefaultBaseManager implements
		WmsBillingManager {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat sdfm = new SimpleDateFormat("yyyyMMddhhmmss");
	@SuppressWarnings("unchecked")
	public void saveWMSContact(WMSContact contact) {
		if(DateUtils.before(contact.getStartDate(), contact.getEndDate())){
			throw new BusinessException("起始日期要小于截止日期");
		}
		StringBuffer hqlBuffer= new StringBuffer("from WMSContact contact where contact.warehouse.id=:warehouseID " +
				" and contact.supplier.id=:supplierID and contact.status='ENABLED' " +
				" and ((contact.startDate <=:startDate and contact.endDate >=:startDate )or ( contact.startDate <=:endDate and contact.endDate >=:endDate ))");
        String hql =null;
		if(contact.isNew()){
           contact.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
           hql=hqlBuffer.append(" and contact.id is not null ").toString();
        }else{
            hql=hqlBuffer.append(" and contact.id !="+contact.getId()).toString();
         }
        List<WMSContact>contacts =(List<WMSContact>)this.commonDao.findByQuery(hql,
        		new String[]{"warehouseID","supplierID","startDate","startDate","endDate","endDate"},
        		new Object[]{contact.getWarehouse().getId(),contact.getSupplier().getId(),contact.getStartDate(),contact.getStartDate(),contact.getEndDate(),contact.getEndDate()});
		if(contacts.size()>0){
			throw new BusinessException("合同期出现交叉");
		}
	}


	public void saveWMSContactDetail(Long contactId,WMSContactDetail detail) {
		WMSContact contact = this.commonDao.load(WMSContact.class, contactId);
		if(null == contact){
			throw new BusinessException("数据异常，合同表头丢失");
		}
		if(null==detail.getStartDate() || null == detail.getEndDate()){
			detail.setStartDate(contact.getStartDate());
			detail.setEndDate(contact.getEndDate());
		}
		if((DateUtils.before(contact.getStartDate(), detail.getStartDate()) || DateUtils.before( detail.getStartDate(),contact.getEndDate()))
				|| (DateUtils.before(contact.getStartDate(), detail.getEndDate()) || DateUtils.before( detail.getEndDate(),contact.getEndDate())) ){
			throw new BusinessException("合同明细区间超过主合同周期");
		}
		if(DateUtils.before(detail.getStartDate(), detail.getEndDate())){
			throw new BusinessException("起始日期要小于截止日期");
		}
		detail.setContact(contact);
		String hql = "from WMSContactDetail detail where detail.contact.id=:contactId and detail.smallCategory.id=:categoryId " +
				"and " +
				"((detail.startDate <=:startDate and detail.endDate >=:startDate ) or ( detail.startDate <=:endDate and detail.endDate >=:endDate )) ";
		if(detail.isNew()){
			hql+=" and detail.id is not null ";
		}else{
			hql+=" and detail.id !="+detail.getId();
		}
		List<WMSContactDetail> details = (List<WMSContactDetail>)this.commonDao.findByQuery(hql,
				new String[]{"contactId","categoryId","startDate","startDate","endDate","endDate"},
				new Object[]{contactId,detail.getSmallCategory().getId(),detail.getStartDate(),detail.getStartDate(),detail.getEndDate(),detail.getEndDate()});
		if(details.size()>0){
			throw new BusinessException("同一个费用类型出现交叉期");
		}
		detail.setSupplier(contact.getSupplier());
		contact.getDetails().add(detail);
		this.commonDao.store(contact);
	}


	public void saveWMSInvoice(WMSInvoice invoice) {
		if(invoice.isNew()){
			invoice.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
			this.commonDao.store(invoice);
			WMSInvoiceDetailCategory detailCategory=null;
			for(WMSContactDetail contactDetail : invoice.getContact().getDetails()){
				detailCategory = this.getInvoiceDetailCategory(contactDetail, invoice);
				detailCategory.setInvoice(invoice);
				this.commonDao.store(detailCategory);
				invoice.getCategories().add(detailCategory);
			}
		}else{
			invoice.setTax(0D);//invoice.getSumManualAmount()%invoice.getTaxingPoint()
		}
		if(!invoice.getSupplier().getCode().equals(invoice.getContact().getSupplier().getCode())){
			throw new BusinessException("合同有误");
		}
		if((null == invoice.getStartDate() && null != invoice.getEndDate()) 
				||(null != invoice.getStartDate() && null == invoice.getEndDate()) ){
			throw new BusinessException("开票起始日期和截止日期不能只存在一个为空");
		}
		invoice.setSerialCode(this.getSequenceGenerater().generateSequence(invoice.getSupplier().getCode()+sdf.format(new Date()), 4));
		this.commonDao.store(invoice);
	}

   private WMSInvoiceDetailCategory getInvoiceDetailCategory(WMSContactDetail contactDetail,WMSInvoice invoice){
	   WMSInvoiceDetailCategory detailCategory =new WMSInvoiceDetailCategory();
	   detailCategory.setEndDate(contactDetail.getEndDate());
	   detailCategory.setInvoice(invoice);
	   detailCategory.setSmallCategory(contactDetail.getSmallCategory());
	   detailCategory.setStartDate(contactDetail.getStartDate());
	   detailCategory.setWarehouse(invoice.getWarehouse());
	   if(null != invoice.getStartDate() && null != invoice.getEndDate()){
		   if(DateUtils.before(invoice.getStartDate(), detailCategory.getStartDate())){
			   detailCategory.setStartDate(invoice.getStartDate());
		   }
		   if(DateUtils.before(detailCategory.getEndDate(), invoice.getEndDate())){
			   detailCategory.setEndDate(invoice.getEndDate());
		   }
	   }else{
		   detailCategory.setStartDate(null);
		   detailCategory.setEndDate(null);
	   }
	   return detailCategory;
   }
	@Override
	public <T extends Entity> List<T> load(Class<T> arg0, List<Long> arg1) {
		// TODO Auto-generated method stub
		return null;
	}


	@SuppressWarnings("unchecked")
	public void addBillDetails(WMSInvoice invoice) {
		if(0 == invoice.getCategories().size()){
			throw new BusinessException("需要维护发票涉及费用类型");
		}
		this.removeBillDetailByInvoice(invoice);
		List<WMSBillDetail> billDetails = null;
		Set<WMSBillDetail>billAllDetails = new HashSet<WMSBillDetail>();
		StringBuffer billDetailBuffer = new StringBuffer("from WMSBillDetail detail where detail.wmsWarehouseCode=:wmsWarehouseCode " +
				"and detail.supplierCode =:supplierCode and detail.invoiceDetail.id is null and  detail.status='CHECKED'  " +
				"and detail.billingSmallCategory=:billingSmallCategory " +
				"and detail.happenDate<=:endDate");
		if(null != invoice.getStartDate()){//前端已经验证截止日期和起始日期不能只存在一个为空，无需考虑过多参数
			billDetailBuffer.append(" and detail.happenDate >=:startDate");
			for(WMSInvoiceDetailCategory detailCategory :invoice.getCategories()){
				billDetails = (List<WMSBillDetail>)this.commonDao.findByQuery(billDetailBuffer.toString(),
						new String[]{"wmsWarehouseCode","supplierCode","billingSmallCategory","endDate","startDate"},
						new Object[]{WmsWarehouseHolder.getWmsWarehouse().getCode(),invoice.getSupplier().getCode(),detailCategory.getSmallCategory().getName(),detailCategory.getEndDate(),detailCategory.getStartDate()});
				if(0 == billDetails.size()){
					continue;
				}
				detailCategory.addBillDetails(billDetails);
				billAllDetails.addAll(billDetails);
			}
		}else{
			for(WMSInvoiceDetailCategory detailCategory :invoice.getCategories()){
				billDetails = (List<WMSBillDetail>)this.commonDao.findByQuery(billDetailBuffer.toString(),
						new String[]{"wmsWarehouseCode","supplierCode","billingSmallCategory","endDate"},
						new Object[]{WmsWarehouseHolder.getWmsWarehouse().getCode(),invoice.getSupplier().getCode(),detailCategory.getSmallCategory().getName(),invoice.getInvoiceDate()});
				if(0 == billDetails.size()){
					continue;
				}
				detailCategory.addBillDetails(billDetails);
				billAllDetails.addAll(billDetails);
			}
		}
		Date minDate=invoice.getInvoiceDate(),maxDate=minDate;
	
		if(0 < billAllDetails.size()){
			for(WMSBillDetail detail:billAllDetails){
				if(DateUtils.before(detail.getHappenDate(), maxDate)){
					maxDate = detail.getHappenDate();
				}
				if(DateUtils.after(detail.getHappenDate(), minDate)){
					minDate = detail.getHappenDate();
				}
			}
		}
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
		invoice.setBillScope(dateformat.format(minDate)+"-"+dateformat.format(maxDate));//发票账期
		if(null == invoice.getSumManualAmount()){
			invoice.setSumManualAmount(0D);
		}
		invoice.calculator();
		invoice.setTax(0D);
	}
	public void multiNewBillDetails(Double qualityQuantityBU){
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
		StringBuffer billDetailBuffer = new StringBuffer("FROM WMSBillDetail detail WHERE detail.wmsWarehouseCode=:wmsWarehouseCode" +
				" AND detail.invoiceDetail.id is null AND detail.status='CHECKED'");
		String rijiewarehouse = GlobalParamUtils.getGloableStringValue("rijie_sys");
		String[] warehouses = rijiewarehouse.split(MyUtils.spilt1);
		for(String warehouse : warehouses){
			Long warehouseId = Long.parseLong(warehouse);
			WmsWarehouse w = commonDao.load(WmsWarehouse.class, warehouseId);
			//获取账单明细数据
			List<WMSBillDetail> billDetails = commonDao.findByQuery(billDetailBuffer.toString(), 
					new String[]{"wmsWarehouseCode"}, new Object[]{w.getCode()});
			if(billDetails!=null && billDetails.size()>0){
				WMSBillingCategory accountingSubject = (WMSBillingCategory)commonDao.findByQueryUniqueResult("FROM WMSBillingCategory wc WHERE wc.type =:c1",
						"c1",WMSBillingCategoryTypeInterface.ACCOUNTING_SUBJECT);
				//按照供应商+合同号分组(A)
				String key = null;
				Map<String,List<WMSBillDetail>> bills = new HashMap<String,List<WMSBillDetail>>();
				List<WMSBillDetail> details = null;
				for(WMSBillDetail detail:billDetails){
					key = detail.getSupplierCode()+MyUtils.spilt1+detail.getWmsContactCode();
					if(bills.containsKey(key)){
						details = bills.get(key);
					}else{
						details = new ArrayList<WMSBillDetail>();
					}
					details.add(detail);
					bills.put(key, details);
				}
				if(bills!=null && bills.size()>0){
					List<String> sortKeys = new ArrayList<String>();
					sortKeys.add("1"+MyUtils.spilt1+"happenDate"+MyUtils.spilt1+"ASC");
					//循环A
					Iterator<Entry<String, List<WMSBillDetail>>> i = bills.entrySet().iterator();
					while(i.hasNext()){
						Entry<String, List<WMSBillDetail>> entry = i.next();
						//按照发生日期排序
						details = entry.getValue();
						List<WMSBillDetailWrap> wraps = new ArrayList<WMSBillDetailWrap>();
						for(WMSBillDetail b : details){
							wraps.add(new WMSBillDetailWrap(b,b.getHappenDate()));
						}
						details = sortByRule(wraps,sortKeys);//排序后的账单明细
						Date startDate = details.get(0).getHappenDate();//起始日期
						Date endDate = details.get(details.size()-1).getHappenDate();//结束日期
						//验证是否满足开票金额
						Double amount=0D;
						for(WMSBillDetail b : details){
							amount += b.getAmount();
						}
						if(amount<qualityQuantityBU){
							continue;
						}
						//初始化费用发票头/明细信息
						String[] keys = entry.getKey().split(MyUtils.spilt1);//[供应商,合同号]
						WmsOrganization supplier = (WmsOrganization)this.commonDao.findByQueryUniqueResult("FROM WmsOrganization org WHERE org.code=:code", 
								"code", keys[0]);
						if(supplier==null){
							throw new BusinessException("供应商不存在"+keys[0]);
						}
						WMSContact contact = (WMSContact)commonDao.findByQueryUniqueResult("FROM WMSContact c WHERE c.code =:c1" +
								" AND c.supplier.code =:c2 AND c.warehouse.code =:c3", 
								new String[]{"c1","c2","c3"}, new Object[]{keys[1],keys[0],w.getCode()});
						if(contact==null){
							throw new BusinessException("合同不存在"+keys[0]+"/"+keys[1]+"/"+w.getCode());
						}
						WMSInvoice invoice = EntityFactory.getEntity(WMSInvoice.class);
						invoice.setSupplier(supplier);
						invoice.setContact(contact);//合同
						invoice.setAccountingSubject(accountingSubject);//会计科目
						invoice.setPayType(contact.getPayType());//支付方式:WMSInvoicePayTypeInterface
						invoice.setStartDate(startDate);
						invoice.setEndDate(endDate);
						invoice.setInvoiceDate(new Date());
						invoice.setMakeupPerson(UserHolder.getUser().getName());//开票人
						invoice.setTaxingPoint(contact.getTaxPoints());//税点
						invoice.setStatus(WMSInvoiceStatusInterface.UNCHECKED);
						invoice.setMemo(qualityQuantityBU+"");
						this.saveWMSInvoice(invoice);
						//加入账单明细
						this.addBillDetails(invoice);
						invoice.setBillScope(dateformat.format(startDate)+"-"+dateformat.format(endDate));//发票账期
						commonDao.store(invoice);
					}
				}
			}
		}
		
	}
	//sortKeys={1#C1#ASC,2#C2#DESC,......} 序号#列名#排序方式 这种方式便于后期排序规则改变升级
	private List<WMSBillDetail> sortByRule(List<WMSBillDetailWrap> wraps,List<String> sortKeys){
		List<SortRule> sortRules = new ArrayList<SortRule>();
		for(int i = 0 ; i<sortKeys.size() ; i++){
			String[] column = sortKeys.get(i).split(MyUtils.spilt1);//[1,C2,ASC]
			sortRules.add(new SortRule(Integer.parseInt(column[0]), column[1], column[2]));
		}
		ComparatorChain chain =  new ComparatorChain();
		 for (SortRule sortRule : sortRules) {
			 if (sortRule.getSortMethod().equals("DESC")) {
                 chain.addComparator(new ReverseComparator(
					new BeanComparator(sortRule.getSortKey())));
             } else {
                 chain.addComparator(
					new BeanComparator(sortRule.getSortKey()));
             }
		 }
		 Collections.sort(wraps,chain);
		 List<WMSBillDetail> tempDetails = new ArrayList<WMSBillDetail>();
		 for(WMSBillDetailWrap w : wraps){
			 tempDetails.add(w.getBill());
		 }
		 return tempDetails;
	}

   private void removeBillDetailByInvoice(WMSInvoice invoice){
	   String hql = "from WMSInvoiceDetailCategory detail where detail.invoice.id="+invoice.getId();
	   List<WMSInvoiceDetailCategory> details = (List<WMSInvoiceDetailCategory>)this.commonDao.findByQuery(hql);
	   
	   if(0 < details.size()){
		   for(WMSInvoiceDetailCategory detail : details){
			   this.removeBillDetailByCategory(detail);
		   }
		   invoice.calculator();
	   }
   }
   
   private void removeBillDetailByCategory(WMSInvoiceDetailCategory detailCategory){
	   String hql ="from WMSBillDetail detail where detail.invoiceDetail.id= "+detailCategory.getId();
       List<WMSBillDetail> billDetails = (List<WMSBillDetail>)this.commonDao.findByQuery(hql);
	   if(0 < billDetails.size()){
		   for(WMSBillDetail detail : billDetails){
			   detail.setInvoiceDetail(null);
		   }
		   detailCategory.getBillDetails().removeAll(billDetails);
		   detailCategory.calculator();
	   }
   }
   public Long getContactId(Map map){
	   
	   return Long.valueOf(map.get("modifyWMSInvoiceDetailPage.invoice.contact.id").toString());
   }

   public void makeInvoiceConfirm(WMSInvoice invoice){
	   if(null != invoice.getTax() && 0 < invoice.getTax()){
		   return ;
	   }
	   invoice.setTax(0D);//invoice.getTaxingPoint()*invoice.getSumManualAmount()
   }

   
   public  void importWmsPurchaseInvoice(Map<String,String>map){
	   String year = map.get("T$FYER");
	   String month= map.get("T$FPRD");
	   String supplierCode = map.get("T$BPID");
	   String amount = map.get("T$AMNT");
	   String code = map.get("T$REFR");
	   String memo = map.get("T$MEMO");
	   if(StringUtils.isEmpty(year)){
		   throw new BusinessException("T$FYER 不能为空");
	   }
	   if(StringUtils.isEmpty(month)){
		   throw new BusinessException("T$FPRD 不能为空");
	   }
	   if(StringUtils.isEmpty(supplierCode)){
		   throw new BusinessException("T$BPID 不能为空");
	   }
	   if(StringUtils.isEmpty(amount)){
		   throw new BusinessException("T$AMNT 不能为空");
	   }
	   if(StringUtils.isEmpty(code)){
		   throw new BusinessException("T$REFR 不能为空");
	   }
	   String invoiceDateStr = year.substring(0,4)+"-"+month+"01";
	   SimpleDateFormat  sdf =new SimpleDateFormat("yyyy-MM-dd");
	   Date invoiceDate =null;
	   try {
	      invoiceDate = sdf.parse(invoiceDateStr);
	   } catch (ParseException e) {
		 
		e.printStackTrace();
	   }
	   WmsOrganization supplier = (WmsOrganization)this.commonDao.findByQueryUniqueResult("from WmsOrganization org where org.code=:code and org.status='ENABLED'", "code", supplierCode);
	   if(null == supplier){
		   throw new BusinessException("供应商编码不对");
	   }
	   WMSPurchaseInvoiceDetail purchaseDetail = new WMSPurchaseInvoiceDetail();
	   purchaseDetail.setAmount(Double.valueOf(amount));
	   purchaseDetail.setCode(code);
	   purchaseDetail.setInvoiceDate(invoiceDate);
	   purchaseDetail.setSupplier(supplier);
	   purchaseDetail.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
	   purchaseDetail.setMemo(memo);
   }
   
   public  void saveWmsPurchaseInvoice(WMSPurchaseInvoiceDetail purchaseDetail){
	   purchaseDetail.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
	   
   }
   
   public void saveWmsBillDetail(WMSBillDetail billDetail){
	   billDetail.setBillfromType("WMS");
	   billDetail.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
	   billDetail.setWmsWarehouseCode(billDetail.getWarehouse().getCode());
	   billDetail.setSupplierCode(billDetail.getSupplier().getCode());
	   billDetail.setSupplierName(billDetail.getSupplier().getName());
	   if(null == billDetail.getFixedPrice()){
		   billDetail.setFixedPrice(0D);
	   }
	   if(null == billDetail.getAmount()){
		   billDetail.setAmount(0D);
	   }
	   if(null == billDetail.getQty_Amount()){
		   billDetail.setQty_Amount(0D);
	   }
	   if(null == billDetail.getRate()){
		   billDetail.setRate(0D);
	   }
	   if(0 < billDetail.getAmount()){
		   
	   }else{
		   if(0 < billDetail.getQty_Amount() && 0 < billDetail.getRate()){
			   billDetail.setAmount(billDetail.getQty_Amount() * billDetail.getRate());
		   }
	   }
       
	   if(0 < billDetail.getFixedPrice()){
		   billDetail.setAmount(billDetail.getFixedPrice());
	   }
//	   if(0 >= billDetail.getAmount()){
//		   throw new BusinessException("费用信息不完整！");
//	   }
	   billDetail.setHistoryAmount(billDetail.getAmount());
	   
	   billDetail.setCode(this.getSequenceGenerater().generateSequence("WMS"+billDetail.getSupplier().getCode()+sdfm.format(new Date()), 4));
   }
   
   
 
	public void saveWmsInvoiceDetailCategory(WMSInvoiceDetailCategory detail,Long invoiceId){
		
		WMSInvoice invoice = (WMSInvoice)this.commonDao.load(WMSInvoice.class, invoiceId);
		detail.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
		detail.setInvoice(invoice);
		invoice.getCategories().add(detail);
		this.commonDao.store(detail);
	}
	

	public void deleteWmsInvoiceDetailCategroy(WMSInvoiceDetailCategory detail){
		WMSInvoice invoice = detail.getInvoice();
		this.removeBillDetailByCategory(detail);
		invoice.getCategories().remove(detail);
		invoice.calculator();
		this.commonDao.store(invoice);
	}
	public ExceptionLog initLog(String operPageName, String pageId, String operComponentName,String companentId,
			String operExceptionMess) {
		ExceptionLog log = EntityFactory.getEntity(ExceptionLog.class);
		log.setOperUserId(1L);
		log.setOperUserName("system");
		log.setOperPageName(operPageName);
		log.setOperPageId(pageId);
		log.setOperComponentName(operComponentName);
		log.setOperComponentId(companentId);
		log.setOperDate(new Date());
		log.setOperExceptionMess(operExceptionMess);
		log.setType("LFCS");
		commonDao.store(log);
		return log;
	}
	
	@SuppressWarnings("unchecked")
	public  void deleteWmsInvoice(WMSInvoice invoice){
		if(invoice.getCategories().size()>0){
			Iterator<WMSInvoiceDetailCategory> i = invoice.getCategories().iterator();
			while(i.hasNext()){
				WMSInvoiceDetailCategory detail = i.next();
				i.remove();
				this.deleteWmsInvoiceDetailCategroy(detail);
			}
		}
//		List<WMSInvoiceDetailCategory> details = (List<WMSInvoiceDetailCategory>) invoice.getCategories();
//		if(0 < details.size()){
//			for(WMSInvoiceDetailCategory detail : details){
//				this.deleteWmsInvoiceDetailCategroy(detail);
//			}
//		}
	}
	public void removeBillDetail(WMSBillDetail billDetail){
		
		WMSInvoiceDetailCategory invoiceDetail = billDetail.getInvoiceDetail();
		if(null == invoiceDetail){
			return ;
		}
		WMSInvoice wmsInvoice = invoiceDetail.getInvoice();
		if(!WMSInvoiceStatusInterface.UNCHECKED.equals(wmsInvoice.getStatus())){
			throw new BusinessException("发票已经审核无法移除");
		}
	    billDetail.setInvoiceDetail(null);
	    invoiceDetail.getBillDetails().remove(billDetail);
	    invoiceDetail.calculator();
	    if(0 >= invoiceDetail.getSumManualAmount()){
	    	invoiceDetail.getInvoice().getCategories().remove(invoiceDetail);
	    }
	    invoiceDetail.getInvoice().calculator();
	}
	
	public void addInfoByBillDetail(WMSBillDetail detail){
		//账单明细关联仓库
		if(StringUtils.isEmpty(detail.getWmsWarehouseCode())){
			throw new BusinessException("仓库代码为空！");
		}
		WmsWarehouse wh = (WmsWarehouse)this.commonDao.findByQueryUniqueResult("from WmsWarehouse wh where wh.code=:code and wh.status='ENABLED'", "code", detail.getWmsWarehouseCode());
		if(null == wh){
			throw new BusinessException(detail.getWmsWarehouseCode()+"仓库找不到");
		}
		detail.setWarehouse(wh);
		//账单明细关联供应商
		if(StringUtils.isEmpty(detail.getSupplierCode())){
			throw new BusinessException("供应商代码为空");
		}
		WmsOrganization supplier = (WmsOrganization)this.commonDao.findByQueryUniqueResult("from WmsOrganization org where org.code=:code and org.status='ENABLED' and org.beSupplier=1", "code", detail.getSupplierCode());
		if(null == supplier){
			throw new BusinessException(detail.getSupplierCode()+"供应商找不到");
		}
		this.commonDao.store(detail);
		//采购发票关联账单明细
		if(!StringUtils.isEmpty(detail.getPurchaseInvoiceCode())){
			String[]poList = detail.getPurchaseInvoiceCode().split("#");
			List<WMSPurchaseInvoiceDetail> poDetails= (List<WMSPurchaseInvoiceDetail>)this.commonDao.findByQuery("from WMSPurchaseInvoiceDetail detail " +
					" where detail.warehouse.code=:whCode and detail.supplier.code=:supplierCode and detail.code in (:poCodes) and detail.status='SENDED' ",
					new String[]{"whCode","supplierCode","poCodes"},new Object[]{detail.getWmsWarehouseCode(),detail.getSupplierCode(),poList});
			if(0 < poDetails.size()){
				for(WMSPurchaseInvoiceDetail po : poDetails){
					po.setBillDetailId(detail.getId());
					po.setStatus(WMSPurchaseStatus.BACKED);
					this.commonDao.store(po);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void sysBilling(){
		List<WMSBillDetail> bs = commonDao.findByQuery("FROM WMSBillDetail b WHERE b.warehouse.id =:warehouseId"
				,new String[]{"warehouseId"},new Object[]{WmsWarehouseHolder.getWmsWarehouse().getId()});
		if(bs!=null && bs.size()>0){
			WmsRuleManager wmsRuleManager = (WmsRuleManager) applicationContext.getBean("wmsRuleManager");
			List<Map<String, Object>> list = wmsRuleManager.getAllRuleTableDetail(WmsWarehouseHolder.getWmsWarehouse().getName(), 
					"R101_费用账单金额",WmsWarehouseHolder.getWmsWarehouse().getName());
			Map<String,String> billMap = new HashMap<String, String>();
			String key1 = null,key2 = null;
			for(Map<String, Object> obj : list){
				key1 = obj.get("供应商编码").toString();
				key2 = obj.get("事务处理参考")+MyUtils.spilt1+obj.get("本币金额");
				billMap.put(key1, key2);
			}
			
			String[] value = null;
			for(WMSBillDetail b : bs){
				if(billMap.containsKey(b.getSupplierCode())){
					value = billMap.get(b.getSupplierCode()).split(MyUtils.spilt1);
					b.setAmount(Double.valueOf(value[1]));
					b.setMemo(value[0]);
					commonDao.store(b);
				}
			}
		}
		
		
	}
	@Override
	public void invoiceImport(File file) {
		// 判断文件是否存在
				if (file == null) {
					throw new BusinessException("操作失败,未找对应文件!");
				}
				// 验证文件格式
				String name = file.getName();
				String fileName = name.substring(0, name.lastIndexOf("."));
				String suffix = name.substring(name.lastIndexOf(".") + 1,
						name.lastIndexOf(".") + 4);
				if (!suffix.equals("xls")) {
					throw new BusinessException("操作失败,导入文件格式错误!");
				}
				Sheet sheet = null;
				Workbook wb = null;
				try {
					// 构造Workbook（工作薄）对象
					wb = Workbook.getWorkbook(new java.io.FileInputStream(file));
					sheet = wb.getSheet(0);
					resolveInvoiceOfSheet(sheet, fileName);
				} catch (BiffException e) {
					LocalizedMessage.addMessage("文件转换失败!");
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					LocalizedMessage.addMessage("未找到文件!");
					e.printStackTrace();
				} catch (IOException e) {
					LocalizedMessage.addMessage("导入失败!文件读写错误,");
					e.printStackTrace();
				} finally {
					if (wb != null) {
						wb.close();
						wb = null;
					}
				}
	}
				public void resolveInvoiceOfSheet(Sheet sheet, String fileName) {
					// 获取行数
					int rowCounts = sheet.getRows();

					StringBuffer sbAll = new StringBuffer();// 记录错误信息
					for (int rowIndex = 0; rowIndex < rowCounts; rowIndex++) {
						StringBuffer sb = new StringBuffer();// 记录错误信息
						if (rowIndex == 0) {
							String strr = "发票账期,发票序列号,供应商编码,合同编号,会计科目,开票日期,对方已付款金额,发票号";
							String[] str;
							str = strr.split(",");
							for (int i = 0; i < str.length;) {

								if (sheet.getCell(i, rowIndex).getContents() != null
										&& sheet.getCell(i, rowIndex).getContents()
												.equals(str[i])) {

								} else {

									throw new BusinessException("导入模板类型或模版字段有误" + "\r");
								}
								i++;
							}
							rowIndex = 1;

						}
						// boolean flag = true;
						// 获取表格中当前行数
						int row = rowIndex + 1;
						if (sb.length() > 0) {
							sb.append("/n");
						}
						String billScope = sheet.getCell(0,rowIndex).getContents().trim();//发票账期
						String serialCode = sheet.getCell(1, rowIndex).getContents().trim();// 发票序列号
						String supplierCode = sheet.getCell(2,rowIndex).getContents().trim();//供应商编码
						String contactCode = sheet.getCell(3, rowIndex).getContents().trim();//合同编号
						String accountingSubject = sheet.getCell(4,rowIndex).getContents().trim();//会计科目
						String invoiceDate;//开票日期
						Date invoiceDatedate = null;
						if (sheet.getCell(5, rowIndex).getContents().trim() != null
								&& !sheet.getCell(5, rowIndex).getContents().equals("")) {
							invoiceDate = sheet.getCell(5, rowIndex).getContents().trim();
							String dateStr1 = invoiceDate.substring(2,3);
							String dateStr2 = invoiceDate.substring(5,6);
							String dateStr3 = invoiceDate.substring(4,5);
							invoiceDatedate = new Date();
							if(dateStr1.equals("-")&&dateStr2.equals("-")){
								SimpleDateFormat purchase = new SimpleDateFormat("yy-MM-dd");
								try {
									invoiceDatedate = purchase.parse(invoiceDate);
								} catch (ParseException e) {
									sb.append("第" + row + "行:" + "开票日期" + invoiceDate
											+ "格式不正确" + "\r");
									}
							}else if(dateStr1.equals("-")){
								SimpleDateFormat purchase = new SimpleDateFormat("yy-M-dd");
								try {
									invoiceDatedate = purchase.parse(invoiceDate);
								} catch (ParseException e) {
									sb.append("第" + row + "行:" + "开票日期" + invoiceDate
											+ "格式不正确" + "\r");
								}
							}else if(dateStr3.equals("/")){
								SimpleDateFormat purchase = new SimpleDateFormat("yyyy/MM/dd");
								try {
									invoiceDatedate = purchase.parse(invoiceDate);
								} catch (ParseException e) {
									sb.append("第" + row + "行:" + "开票日期" + invoiceDate
											+ "格式不正确" + "\r");
								}
							}else{
								SimpleDateFormat purchase = new SimpleDateFormat("yyyy-MM-dd");
								try {
									invoiceDatedate = purchase.parse(invoiceDate);
								} catch (ParseException e) {
									sb.append("第" + row + "行:" + "开票日期" + invoiceDate
											+ "格式不正确" + "\r");
									}
							}
						}
						String hql = "From WMSInvoice wi where wi.billScope =:billScope and wi.serialCode =:serialCode" +
								" and wi.supplier.code =:supplierCode and wi.contact.code =:contactCode and " +
								"wi.accountingSubject.name =:accountingSubject and wi.invoiceDate =:invoiceDatedate";
						List<WMSInvoice> wmsInvoice = commonDao.findByQuery(hql,new String[]{"billScope","serialCode","supplierCode",
								"contactCode","accountingSubject","invoiceDatedate"},new Object[]{billScope,serialCode,supplierCode,contactCode,
								accountingSubject,invoiceDatedate});
						if(wmsInvoice.size()<=0){
							sb.append("第" + row + "行:" + "找不到对应的费用发票" + "\r");
							  
						}
						Double paidAmount = 0D;
						try {
							if (sheet.getCell(6, rowIndex).getContents() != "") {
								paidAmount = Double.parseDouble(sheet
										.getCell(6, rowIndex).getContents().trim());
							}
						} catch (Exception e) {
							sb.append("第" + row + "行:" + "对方已付款金额有误;" + "\r");
						}
						String code = sheet.getCell(7,rowIndex).getContents().trim();//发票号
						
						if (sb.length() <= 0) {
							WMSInvoice invoice = commonDao.load(WMSInvoice.class, wmsInvoice.get(0).getId());
							invoice.setPaidAmount(paidAmount);
							invoice.setCode(code);
							commonDao.store(invoice);
						} else {
							sbAll.append(sb);
						}

					}
					if (sbAll.length() > 0) {
						ExceptionLog el = EntityFactory.getEntity(ExceptionLog.class);
						el.setOperUserId(UserHolder.getUser().getId());
						el.setOperUserName(UserHolder.getUser().getName());
						el.setOperPageName("费用发票导入");
						el.setOperComponentId("error");
						el.setOperComponentName("导入错误");
						el.setOperDate(new Date());
						el.setOperExceptionMess(sbAll.toString());
						commonDao.store(el);
						LocalizedMessage.addMessage("导入失败,请查看操作日志");
					} else {
						LocalizedMessage.addMessage("导入成功");
					}

				}
}
