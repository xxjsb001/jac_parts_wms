package com.vtradex.wms.server.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.vtradex.wms.server.model.base.BaseStatus;


public class WmsStringUtils {
	
	public static boolean isEmpty(String str) {
		return (str == null || "-".equals(str) || "".equals(str));
	}

	/** 
	 * 锁定-增加库存状态
	 */
	public static String addStatus(String status, String state) {
		if (status.indexOf(state) == -1) {
			if( BaseStatus.NULLVALUE.equals(status)) {
				return state;
			} else {
				return (state + BaseStatus.NULLVALUE + status);
			}	
		}
		return status;
	}
	
	/** 
	 * 解锁-减少库存状态
	 * */
	public static String removeStatus(String status, String state) {
		if (status.indexOf(state) != -1) {
			String[] os = StringUtils.split(status, BaseStatus.NULLVALUE);
			List<String> lst = new ArrayList<String>();
			int j = 0;
			for (int i=0; i<os.length; i++) {
				if (!os[i].equals(state)) {
					lst.add(os[i]);
					j++;
				}
			}
			String result = StringUtils.join(lst.toArray(), BaseStatus.NULLVALUE);
			if (StringUtils.isEmpty(result)) {
				result = BaseStatus.NULLVALUE;
			}
			return result;
		}
		return status;
	}
	
	/** 
	 * 锁定-库存状态替换
	 */
	public static String replaceStatus(String status, String oldState, String newState) {
		return StringUtils.replace(status, oldState, newState);
	}
}
