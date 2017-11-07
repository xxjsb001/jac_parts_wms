package com.vtradex.wms.server.service;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.vtradex.thorn.server.config.UniConfig;
import com.vtradex.thorn.server.config.globalparam.GlobalParam;
import com.vtradex.thorn.server.exception.BusinessException;

@SuppressWarnings("all")
public class GlobalParamUtils implements ApplicationContextAware {
	private static ApplicationContext ac;
	
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.ac = arg0;
	}

	/**
     * 按照配置的全局变量的数字格式格式化数字
     * decimal 保留的小数位数(0－不保留、1－保留1位、以此类推)
     * round 保留的小数位数时是否四舍五入(Y－是、N－否)
     * @param   number   a <code>double</code>.
     */
	public static double formatDecimalByType(double number, String type) {
		if (ac.containsBean("uniConfig")) {
			UniConfig uniConfig = (UniConfig) ac.getBean("uniConfig");
			
			int decimal = uniConfig.getGlobalParam(type).getInt();
			
			String round = uniConfig.getGlobalParam("round").getValue();
			String cResult = "";
			
			DecimalFormat df = null;
			
			if (round.equals("Y")) {
				df = new DecimalFormat("#." + StringUtils.repeat("0", decimal));
			} else {
				df = new DecimalFormat("#." + StringUtils.repeat("0", decimal + 1));
			}
			
			cResult = df.format(number);
			
			if (cResult.indexOf(".") != -1) {
				cResult = cResult.substring(0, cResult.indexOf(".") + decimal + 1 > cResult.length() 
						? cResult.length()
						: cResult.indexOf(".") + decimal + 1);
			}
			
			try {
				return df.parse(cResult).doubleValue();
			} catch (ParseException e) {
				throw new BusinessException("number.format.error");
			}
		} else {
			throw new BusinessException("system.error");
		}
	}
	
    /**
     * 从配置的全局变量中取得布尔值
     * @param beanId 全局参数bean的ID值
     * @return boolean 全局参数bean对应的value值
     */
    public static boolean getGloableBooleanValue(String beanId) {
    	if (ac.containsBean("uniConfig")) {
            UniConfig uniConfig = (UniConfig) ac.getBean("uniConfig");
            
            GlobalParam globalParam = uniConfig.getGlobalParam(beanId);
            
            if (globalParam != null && globalParam.getString() != null && globalParam.getString().equals("Y")) {
                return true;
            }
        }
    	
        return false;
    }
    
    public static int getGloableIntegerValue(String beanId) {
    	if (ac.containsBean("uniConfig")) {
            UniConfig uniConfig = (UniConfig) ac.getBean("uniConfig");
            
            GlobalParam globalParam = uniConfig.getGlobalParam(beanId);
            
            if (globalParam != null) {
                return globalParam.getInt();
            }
        }
    	
        return 0;
    }
    
    public static double getGloableDoubleValue(String beanId){
    	if (ac.containsBean("uniConfig")) {
            UniConfig uniConfig = (UniConfig) ac.getBean("uniConfig");
            
            GlobalParam globalParam = uniConfig.getGlobalParam(beanId, "origen");
            
            if (globalParam != null) {
                return globalParam.getDouble();
            }
        }
    	
        return 0;
    }
    
    public static double getGloableDoubleValue(String beanId, String ref) {
    	if (ac.containsBean("uniConfig")) {
            UniConfig uniConfig = (UniConfig) ac.getBean("uniConfig");
            
            GlobalParam globalParam = uniConfig.getGlobalParam(beanId, ref);
            
            if (globalParam != null) {
                return globalParam.getDouble();
            }
        }
    	
        return 0;
    }

    /**
     * 从配置的全局变量中取得字符串值
     * @param beanId 全局参数bean的ID值
     * @return String 全局参数bean对应的value值
     */
    public static String getGloableStringValue(String beanId){
    	return getGloableStringValue(beanId,"origen");
    }
    
    public static String getGloableStringValue(String beanId, String ref) {
    	if (ac.containsBean("uniConfig")) {
            UniConfig uniConfig = (UniConfig) ac.getBean("uniConfig");
            
            GlobalParam globalParam = uniConfig.getGlobalParam(beanId, ref);
            
            if (globalParam != null && globalParam.getString() != null) {
                return globalParam.getString();
            }
        }
    	
        return "";
    }
}