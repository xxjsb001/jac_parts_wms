package com.vtradex.wms.server.service.billing;

import java.io.File;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.model.exception.ExceptionLog;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.billing.WMSBillDetail;
import com.vtradex.wms.server.model.billing.WMSContact;
import com.vtradex.wms.server.model.billing.WMSContactDetail;
import com.vtradex.wms.server.model.billing.WMSInvoice;
import com.vtradex.wms.server.model.billing.WMSInvoiceDetailCategory;
import com.vtradex.wms.server.model.billing.WMSPurchaseInvoiceDetail;


public interface WmsBillingManager extends BaseManager {
	
	//合同保存
	@Transactional
	void saveWMSContact(WMSContact contact);
	//合同明细保存
	@Transactional
	void saveWMSContactDetail(Long contactId,WMSContactDetail detail);
	//发票保存
	@Transactional
	void saveWMSInvoice(WMSInvoice invoice);
	
	//加入账单明细
	@Transactional
	void addBillDetails(WMSInvoice invoice);
	
	Long getContactId(Map<String,String>  map);
	
	//开票确认
	@Transactional
	void makeInvoiceConfirm(WMSInvoice invoice);
	
	//导入采购发票
	@Transactional
	void importWmsPurchaseInvoice(Map<String,String>  map);
	
	//采购发票保存
	@Transactional
	void saveWmsPurchaseInvoice(WMSPurchaseInvoiceDetail invoice);
	
	//人工账单明细保存
	@Transactional
	void saveWmsBillDetail(WMSBillDetail billDetail);
	
	//发票明细保存
	@Transactional
	void saveWmsInvoiceDetailCategory(WMSInvoiceDetailCategory detail,Long invoiceId);
	
	//发票明细删除
	@Transactional
	void deleteWmsInvoiceDetailCategroy(WMSInvoiceDetailCategory detail);
	@Transactional
	ExceptionLog initLog(String operPageName, String pageId, String operComponentName,String companentId,
			String operExceptionMess);
	//发票删除
	@Transactional
	void deleteWmsInvoice(WMSInvoice invoice);
	
	//移除账单明细
	@Transactional
	void removeBillDetail(WMSBillDetail billDetail);
	
	//完善账单明细（关联相关实体对象，用于接口插入WMS账单表后，调用）
	@Transactional
	void addInfoByBillDetail(WMSBillDetail detail);
	/**同步费用账单金额**/
	@Transactional
	void sysBilling();
	/********************核算*******************/
	/**批量新建发票账单*/
	@Transactional
	void multiNewBillDetails(Double qualityQuantityBU);
	/**
	 * 费用发票导入
	 * shenda.yuan  2016.6.3
	 * @param file 
	 */
	void invoiceImport(File file);
	
}