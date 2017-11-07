package com.vtradex.wms.server.service.formatter;

import java.util.List;

import com.vtradex.thorn.server.format.Formatter;
import com.vtradex.wms.server.service.billing.WMSBillingCategoryRuleChain;

public class TaskTypeFormatter implements Formatter {

	public String format(String property, Object cellValue, List origenData,
			String referenceModel, String locale) {
		String type = cellValue == null ? "" : cellValue.toString();
		String result = "";
		if (WMSBillingCategoryRuleChain.BILL00101month.equals(type)) {
			result = "仓租租凭(恒定)服务费-month";
		} else if (WMSBillingCategoryRuleChain.BILL00102month.equals(type)) {
			result = "仓储服务增租费-month";
		} else if (WMSBillingCategoryRuleChain.BILL00103day.equals(type)) {
			result = "仓库租赁(托盘数)服务费-day";
		} else if (WMSBillingCategoryRuleChain.BILL00201month.equals(type)) {
			result = "配送服务费-month";
		}else if (WMSBillingCategoryRuleChain.BILL00201signed.equals(type)) {
			result = "配送服务费-按签收";
		}else if (WMSBillingCategoryRuleChain.BILL00201receive.equals(type)) {
			result = "配送服务费-按收货";
		}else if (WMSBillingCategoryRuleChain.BILL00301month.equals(type)) {
			result = "分选服务费-month";
		}else if (WMSBillingCategoryRuleChain.BILL00301day.equals(type)) {
			result = "分选服务费-day";
		}else if (WMSBillingCategoryRuleChain.BILL00302month.equals(type)) {
			result = "信息服务费-month";
		}else if (WMSBillingCategoryRuleChain.BILL00303month.equals(type)) {
			result = "不合格品管理费-month";
		}else if (WMSBillingCategoryRuleChain.BILL00303day.equals(type)) {
			result = "不合格品管理费-day";
		}else if (WMSBillingCategoryRuleChain.BILL00304month.equals(type)) {
			result = "退货服务费-month";
		}else if (WMSBillingCategoryRuleChain.BILL00304day.equals(type)) {
			result = "退货服务费-day";
		}else if (WMSBillingCategoryRuleChain.BILL00305month.equals(type)) {
			result = "工装租赁费-month";
		}else if (WMSBillingCategoryRuleChain.BILL00306month.equals(type)) {
			result = "工装管理费-month";
		}else if (WMSBillingCategoryRuleChain.BILL00306day.equals(type)) {
			result = "工装管理费-day";
		}else if (WMSBillingCategoryRuleChain.BILL00307month.equals(type)) {
			result = "打包服务费-month";
		}else if (WMSBillingCategoryRuleChain.BILL00401month.equals(type)) {
			result = "基本服务费-month";
		}else {
			result = type;
		}
		return result;
	}

	public String exportFormat(String property, Object cellValue,
			List origenData, String referenceModel, String locale) {
		return format(property, cellValue, origenData, referenceModel, locale);
	}

}
