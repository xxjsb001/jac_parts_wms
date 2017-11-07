package com.vtradex.wms.server.service.billing;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.vtradex.wms.server.model.billing.WMSBillingCategoryBigEnum;
import com.vtradex.wms.server.model.billing.WMSBillingCategoryMiddleEnum;
import com.vtradex.wms.server.model.billing.WMSBillingModelInterface;
import com.vtradex.wms.server.utils.MyUtils;

/**yc 计费类枚举规则链*/
public class WMSBillingCategoryRuleChain {
	
	public static String and = "&";
	public static String  or= "@";
	public static String  BILLLFCS= "BILL_LFCS";
	
	/**仓租租凭(恒定)服务费-month*/
	public static String BILL00101month= "BILL001_01_month";
	/**仓储服务增租费-month*/
	public static String BILL00102month= "BILL001_02_month";
	/**仓库租赁(托盘数)服务费-day*/
	public static String BILL00103day= "BILL001_03_day";
	/**配送服务费-month*/
	public static String BILL00201month= "BILL002_01_month";
	/**配送服务费-按签收*/
	public static String BILL00201signed= "BILL002_01_SIGNED";
	/**配送服务费-按收货*/
	public static String BILL00201receive= "BILL002_01_RECEIVE";
	/**配送服务费-day*/
//	public static String BILL00201day= "BILL002_01_day";
	/**分选服务费-month*/
	public static String BILL00301month= "BILL003_01_month";
	/**分选服务费-day*/
	public static String BILL00301day= "BILL003_01_day";
	/**信息服务费-month*/
	public static String BILL00302month= "BILL003_02_month";
	/**不合格品管理费-month*/
	public static String BILL00303month= "BILL003_03_month";
	/**不合格品管理费-day*/
	public static String BILL00303day= "BILL003_03_day";
	/**退货服务费-month*/
	public static String BILL00304month= "BILL003_04_month";
	/**退货服务费-day*/
	public static String BILL00304day= "BILL003_04_day";
	/**工装租赁费-month*/
	public static String BILL00305month= "BILL003_05_month";
	/**工装管理费 -month*/
	public static String BILL00306month= "BILL003_06_month";
	/**工装管理费 -day*/
	public static String BILL00306day= "BILL003_06_day";
	/**打包服务费-month*/
	public static String BILL00307month= "BILL003_07_month";
	/**基本服务费-month*/
	public static String BILL00401month= "BILL004_01_month";
	//chain01~04:四大计费类,链结构:计费大类#小类1&小类2&...
	/**仓储租赁费用*/
	public static final String chain01 = WMSBillingCategoryBigEnum.BILL001+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL001_01+and+
			WMSBillingCategoryMiddleEnum.BILL001_02+and+
			WMSBillingCategoryMiddleEnum.BILL001_03;
	/**配送费用*/
	public static final String chain02 = WMSBillingCategoryBigEnum.BILL002+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL002_01;
	/**增值服务费用 */
	public static final String chain03 = WMSBillingCategoryBigEnum.BILL003+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL003_01+and+
			WMSBillingCategoryMiddleEnum.BILL003_02+and+
			WMSBillingCategoryMiddleEnum.BILL003_03+and+
			WMSBillingCategoryMiddleEnum.BILL003_04+and+
			WMSBillingCategoryMiddleEnum.BILL003_05+and+
			WMSBillingCategoryMiddleEnum.BILL003_06+and+
			WMSBillingCategoryMiddleEnum.BILL003_07;
	/**基本服务费 */
	public static final String chain04 = WMSBillingCategoryBigEnum.BILL004+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL004_01;
	//日结(chainModleDay),月结(chainModleMonth),链结构:小类1&计费类型1||计费类型2||...#小类2&计费类型1||计费类型2||...
	public static final String chainModleDay = WMSBillingCategoryMiddleEnum.BILL001_03+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL002_01+and+WMSBillingModelInterface.SIGNED+or+WMSBillingModelInterface.RECEIVE+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL003_01+and+WMSBillingModelInterface.PACKAGES+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL003_03+and+WMSBillingModelInterface.PACKAGES+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL003_04+and+WMSBillingModelInterface.PACKAGES+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL003_06+and+WMSBillingModelInterface.PACKAGES;
	public static final String chainModleMonth = WMSBillingCategoryMiddleEnum.BILL001_01+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL001_02+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL002_01+and+WMSBillingModelInterface.SALE_AMOUNT+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL003_01+and+WMSBillingModelInterface.MONTH+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL003_02+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL003_03+and+WMSBillingModelInterface.MONTH+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL003_04+and+WMSBillingModelInterface.MONTH+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL003_05+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL003_06+and+WMSBillingModelInterface.MONTH+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL003_07+MyUtils.spilt1+
			WMSBillingCategoryMiddleEnum.BILL004_01;
	public String billRuleChain(String chain){
		return chain.split(MyUtils.spilt1)[0];
	}
	public static Map<String,String> chainMethod(Object obj){
		Map<String,String> chainMethod = new HashMap<String,String>();
		Field[] ff = null;
		if(obj.getClass().isInstance(WMSBillingCategoryMiddleEnum.class)){
			ff = WMSBillingCategoryMiddleEnum.class.getFields();
		}
		String key = null,value = null;
		for(Field f : ff){
			value = f.getName();
			try {
				key = (String) f.get(null);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			chainMethod.put(key, value);
		}
		return chainMethod;
	}
	public static void main(String[] args) {
		Map<String,String> chainMethod = WMSBillingCategoryRuleChain.chainMethod(WMSBillingCategoryMiddleEnum.class);
		Iterator<Entry<String, String>> ii = chainMethod.entrySet().iterator();
		while(ii.hasNext()){
			Entry<String, String> en = ii.next();
			System.out.println(en.getKey()+":"+en.getValue());
		}
		/*Field[] ff = WMSBillingCategoryMiddleEnum.class.getDeclaredFields();
		for(Field f : ff){
			System.out.println(f.getName());
			try {
				System.out.println(f.get(null));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		System.out.println(WMSBillingCategoryRuleChain.chain01);*/
	}
}
