package com.vtradex.wms.server.utils;

import java.lang.reflect.InvocationTargetException;

/**
 * 
 * @author <a href="yan.li@vtradex.com">李炎</a>
 * @description 
 * 
 */

public class BeanUtils {

	/**
	 * 属性拷贝
	 * @param one
	 * @param two
	 */
	public static void copyEntity(Object desc,Object src){
		try {
			org.apache.commons.beanutils.BeanUtils.copyProperties(desc, src);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}


