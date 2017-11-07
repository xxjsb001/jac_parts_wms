package com.vtradex.wms.client.ui.page.allocate.page.utils;

import java.util.List;

/**
 * 
 * @author <a href="yan.li@vtradex.com">李炎</a>
 * @description
 * 
 */

public class ArrayUtils {

	public static Object[][] listToArray(List items) {
		int len = items.size();
		Object[][] objs = new Object[len][];
		for (int i = 0; i < len; i++) {
			List item = (List) items.get(i);
			Object[] obj = new Object[item.size()];
			for (int j = 0; j < item.size(); j++) {
				obj[j] = item.get(j);
			}
			objs[i] = obj;
		}
		return objs;
	}
}
