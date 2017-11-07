package com.vtradex.wms.server.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

public class NewLotInfoParser {
	private static String lotparam = "inventory.itemKey.lotInfo.";
	/**
	 * 对日期批次属性进行解析 -- 日期型解析形式存在  1.无输入  2.单个日期 3.范围  4. 2和3的组合，逗号分隔
	 * @param str
	 * @return
	 */
	public static String decryptDateOfLot(String name,String prop) {
		if(prop == null || "".equals(prop)){
			return "1 = 1";
		}
		String str = prop;
		String parans[] = new String[org.apache.commons.lang.StringUtils.countMatches(str, "(")];
		String paranStr = org.apache.commons.lang.StringUtils.substringBetween(str, "(", ")");
		
		for (int i = 0; !org.apache.commons.lang.StringUtils.isEmpty(paranStr); i++) {
			parans[i] = paranStr;
			str = org.apache.commons.lang.StringUtils.replaceOnce(str, "(" + paranStr + ")", "");
			paranStr = org.apache.commons.lang.StringUtils.substringBetween(str, "(", ")");
		}
		
		StringTokenizer stn = new StringTokenizer(str,",");
		String contion = "";
		StringBuffer sb = new StringBuffer();
		
		
		if(stn.countTokens() > 0){
			if(stn.countTokens() == 1){		
//				contion += lotparam+name+"=to_date('"+ (stn.nextToken().trim())+"','yyyy-mm-dd')";//oracle环境下
				contion += lotparam+name+"='"+ (stn.nextToken().trim())+"'";//mysql环境下
			}else{
				contion += lotparam+name+" in (";
				for(;stn.hasMoreTokens();){
					sb.append("'"+stn.nextToken().trim()+"'");
					sb.append(",");
				}
				
				contion += sb.substring(0, sb.length()-1)+")";
			}
		}
		if(parans.length > 0 ){
			if(!"".equals(contion)){
				contion += " OR ";
			}
			
			String sd1 = "";
			String sd2 = "";
			for (int i = 0; i < parans.length; i++) {
				int index = parans[i].indexOf(",", 0);
				
				sd1 = parans[i].substring(0, index);
				sd2 = parans[i].substring(index+1);
				
				if (!StringUtils.isEmpty(sd1) && StringUtils.isEmpty(sd2)) {
					contion += lotparam + name + " >= '" + sd1 + "'";
				} else if (StringUtils.isEmpty(sd1) && !StringUtils.isEmpty(sd2)) {
					contion += lotparam + name + " <= '" + sd2 + "'";
				} else if (!StringUtils.isEmpty(sd1) && !StringUtils.isEmpty(sd2)) {
					contion += lotparam + name + " between '" + sd1	+ "' and '"+sd2+"' ";
				}

				contion += " OR ";
			}
			contion = contion.substring(0,contion.length()-4);
		}
		return contion;
	}
	/**
	 * 解析数字型批次属性  --  数字型解析形式存在  1.无输入 0.0  2.单个数字 不同于0.0的单值 3.范围  4. 2和3的组合，逗号分隔
	 * @param str
	 * @return
	 */
	public static String decryptDoubleOfLot(String name,String prop) {
		if(prop == null || "".equals(prop)||"0.0".equals(prop)){
			return "1 = 1";
		}
		String str = prop;
		String parans[] = new String[org.apache.commons.lang.StringUtils.countMatches(str, "(")];
		String paranStr = org.apache.commons.lang.StringUtils.substringBetween(str, "(", ")");
		for (int i = 0; !org.apache.commons.lang.StringUtils.isEmpty(paranStr); i++) {
			parans[i] = paranStr;
			str = org.apache.commons.lang.StringUtils.replaceOnce(str, "(" + paranStr + ")", "");
			paranStr = org.apache.commons.lang.StringUtils.substringBetween(str, "(", ")");
		}
		
		StringTokenizer stn = new StringTokenizer(str,",");
		String contion = "";
		StringBuffer sb = new StringBuffer();
		
		
		if(stn.countTokens() > 0){
			if(stn.countTokens() == 1){			
				String d = stn.nextToken();
				contion += lotparam + name + "="+d;
			}else{
				contion += lotparam+name+" in (";
				for(;stn.hasMoreTokens();){
					String d = stn.nextToken();
					sb.append(d+",");
				}
				
				contion += sb.substring(0, sb.length()-1)+")";
			}
		}
		if(parans.length > 0 ){
			if(!"".equals(contion)){
				contion += " OR ";
			}
			for (int i = 0; i < parans.length; i++) {
				int index = parans[i].indexOf(",", 0);
				String sd1 = parans[i].substring(0, index);
				String sd2 = parans[i].substring(index+1);

				if (!StringUtils.isEmpty(sd1) && StringUtils.isEmpty(sd2)) {
					contion += lotparam + name + " >= "+ sd1;
				} else if (StringUtils.isEmpty(sd1) && !StringUtils.isEmpty(sd2)) {
					contion += lotparam + name + " <= "+ sd2;
				} else if (!StringUtils.isEmpty(sd1) && !StringUtils.isEmpty(sd2)) {
					contion += lotparam + name + " between "+ sd1 + " and " + sd2;
				}
				
				contion += " OR ";
			}
			contion = contion.substring(0,contion.length()-4);
		}
		return contion;
	}
	/**
	 * 解析字符串批次属性  --  字符串解析形式存在  1.""（过滤后为null） 2.单字符串 3.用"，"号隔开的多个字符串 
	 * @param string
	 * @return
	 */
	public static String decryptStringOfLot(String name, String prop) {
		String str = prop;
		
		if ("".equals(str) || prop == null) {
			return "1 = 1 ";
		} else {
			StringTokenizer stn = new StringTokenizer(str,",");
			StringBuffer sb = new StringBuffer();
			
			if (stn.countTokens() == 1) {
				return lotparam + name + " = '" + str.trim() + "' ";
			} else {
				for ( ; stn.hasMoreTokens();) {
					sb.append("'" + stn.nextToken().trim() + "' ");
					sb.append(",");
				}
				
				return lotparam + name + " in (" + sb.substring(0, sb.length() - 1) + ") ";
			}
		}
	}
	
	public static String decryptStringOfWaveLot(String name, String prop) {
		String str = prop;
		
		if ("".equals(str) || prop == null) {
			return "(" + name + " = '' OR " + name + " IS  NULL )" ;
		} else {
			StringTokenizer stn = new StringTokenizer(str,",");
			StringBuffer sb = new StringBuffer();
			
			if (stn.countTokens() == 1) {
				return  name + " = '" + str.trim() + "' ";
			} else {
				for ( ; stn.hasMoreTokens();) {
					sb.append("'" + stn.nextToken().trim() + "' ");
					sb.append(",");
				}
				
				return  name + " in (" + sb.substring(0, sb.length() - 1) + ") ";
			}
		}
	}
	
	public static String decryptStringOfInventoryLot(String name, String prop) {
		String str = prop;
		
		if ("".equals(str) || prop == null) {
			return "1 = 1" ;
		} else {
			StringTokenizer stn = new StringTokenizer(str,",");
			StringBuffer sb = new StringBuffer();
			
			if (stn.countTokens() == 1) {
				return  name + " = '" + str.trim() + "' ";
			} else {
				for ( ; stn.hasMoreTokens();) {
					sb.append("'" + stn.nextToken().trim() + "' ");
					sb.append(",");
				}
				
				return  name + " in (" + sb.substring(0, sb.length() - 1) + ") ";
			}
		}
	}
	
	public static String decryptLongStringOfLot(String name,String prop) {
		String result = "";
		String str = prop;
		if("".equals(str)||prop == null){
			result = "1 = 1";
		}else{
			StringTokenizer stn = new StringTokenizer(str,",");
			if(stn.countTokens() == 1){
				result = lotparam + name + " LIKE '%" + str.trim() + "%'";
			}else{
				result += " ( ";
				for(;stn.hasMoreTokens();){
					result += lotparam + name + " LIKE '%" + stn.nextToken().trim() + "%' OR ";
				}
				
				result = result.substring(0, result.length() - 3) + ")";
			}
		}
		
		return result;
	}
	
	/**
	 * 判断单个数字是否在一组数字范围之内
	 */
	public static boolean compareNum(double nowNum,String area){
		if(org.apache.commons.lang.StringUtils.isEmpty(area))return true;

		//如果是单值或枚举类型,则直接进行比较
		if(!area.startsWith("(")){
			StringTokenizer stn = new StringTokenizer(area,",");
			while(stn.hasMoreTokens()){
				if(nowNum==Double.parseDouble(stn.nextToken()))
					return true;
			}
		}
		//如果是范围类型,如(1,10),则去处括号,取数字
		String str = area;
		String parans[] = new String[org.apache.commons.lang.StringUtils.countMatches(str, "(")];
		String paranStr = org.apache.commons.lang.StringUtils.substringBetween(str, "(", ")");
		
		for (int i = 0; !org.apache.commons.lang.StringUtils.isEmpty(paranStr); i++) {
			parans[i] = paranStr;
			int index = parans[i].indexOf(",", 0);
			String start = parans[i].substring(0, index);
			String end = parans[i].substring(index+1);

			if (!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)) {
				if(nowNum>=Double.parseDouble(start) && nowNum<=Double.parseDouble(end))
					return true;
			} else if (!StringUtils.isEmpty(start) && StringUtils.isEmpty(end)) {
				if(nowNum>=Double.parseDouble(start))
					return true;
			} else if (StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)) {
				if(nowNum<=Double.parseDouble(end))
					return true;
			}
			str = org.apache.commons.lang.StringUtils.replaceOnce(str, "(" + paranStr + ")", "");
			paranStr = org.apache.commons.lang.StringUtils.substringBetween(str, "(", ")");

		}
		return false;
	}
	
	/**
	 * 判断单个日期是否在一组日期范围之内
	 * 
	 */
	public static boolean compareDate(Date nowDate,String area){
		try {
			if(org.apache.commons.lang.StringUtils.isEmpty(area))return true;
			if(nowDate==null&&org.apache.commons.lang.StringUtils.isEmpty(area))return true;
			if(nowDate==null&&!org.apache.commons.lang.StringUtils.isEmpty(area))return false;
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//如果area是单值就直接比较
			if(sdf.format(nowDate).equals(area))return true;
			
			//如果是枚举类型,则直接遍历进行比较
			if(!area.startsWith("(")){
				StringTokenizer stn = new StringTokenizer(area,",");
				String now = sdf.format(nowDate);
				while(stn.hasMoreTokens()){
					if(now.equals(stn.nextToken()))
						return true;
				}
			}
			
			//如果是范围,则去处括号进行比较
			java.util.Calendar caNow = new java.util.GregorianCalendar();
			java.util.Calendar caStart = new java.util.GregorianCalendar();
			java.util.Calendar caEnd = new java.util.GregorianCalendar();
			
			caNow.setTime(nowDate);
			
			String str = area;
			String parans[] = new String[org.apache.commons.lang.StringUtils.countMatches(str, "(")];
			String paranStr = org.apache.commons.lang.StringUtils.substringBetween(str, "(", ")");
			
			for (int i = 0; !org.apache.commons.lang.StringUtils.isEmpty(paranStr); i++) {
				parans[i] = paranStr;
				int index = parans[i].indexOf(",", 0);
				String startDate = parans[i].substring(0, index);
				String endDate = parans[i].substring(index+1);
				
				if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
					caStart.setTime(sdf.parse(startDate));
					caEnd.setTime(sdf.parse(endDate));
					if(caNow.after(caStart)&&caNow.before(caEnd))
						return true;
					if(caNow.equals(caStart)||caNow.equals(caEnd))
						return true;
				} else if (!StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {
					caStart.setTime(sdf.parse(startDate));
					if(caNow.after(caStart) || caNow.equals(caStart))
						return true;
				} else if (StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
					caEnd.setTime(sdf.parse(endDate));
					if(caNow.before(caEnd) || caNow.equals(caEnd))
						return true;
				}
				
				str = org.apache.commons.lang.StringUtils.replaceOnce(str, "(" + paranStr + ")", "");
				paranStr = org.apache.commons.lang.StringUtils.substringBetween(str, "(", ")");

			}

			} catch (Exception e) {
				
				e.printStackTrace();
			}
			return false;
	}
	
	/**
	 * 判断单个字符串是否在一组字符串枚举之内或与某个字符串相等
	 * 
	 */
	public static boolean compareString(String nowString,String area){
		
		//都为空或area为空,return true
		if(org.apache.commons.lang.StringUtils.isEmpty(nowString)
				&&org.apache.commons.lang.StringUtils.isEmpty(area)
					||org.apache.commons.lang.StringUtils.isEmpty(area))return true;
		
		//只要范围参数不为空并且被校验参数为空,则不匹配，return false
		if(org.apache.commons.lang.StringUtils.isEmpty(nowString)&&
						!org.apache.commons.lang.StringUtils.isEmpty(area))return false;
		if(nowString.equals(area))return true;
		
		if(area.startsWith("(")){
			area =area.substring(area.indexOf("(")+1,area.lastIndexOf(")"));
		}
		StringTokenizer stn = new StringTokenizer(area,",");
		if(stn.countTokens()>0){
			while(stn.hasMoreTokens()){
				if(nowString.equals(stn.nextToken()))return true;
			}
		}
		return false;
	}
	
	public static boolean compareLongString(String nowString,String area){
		
		//都为空或area为空,return true
		if(org.apache.commons.lang.StringUtils.isEmpty(nowString)
				&&org.apache.commons.lang.StringUtils.isEmpty(area)
					||org.apache.commons.lang.StringUtils.isEmpty(area))return true;
		
		//只要范围参数不为空并且被校验参数为空,则不匹配，return false
		if(org.apache.commons.lang.StringUtils.isEmpty(nowString)&&
						!org.apache.commons.lang.StringUtils.isEmpty(area))return false;
		
		StringTokenizer stn = new StringTokenizer(area,",");
		if(stn.countTokens() == 1){
			return (area.indexOf(nowString) != -1);
		} else {
			while(stn.hasMoreTokens()){
				if(area.indexOf(nowString) != -1) return true;
			}
		}
		return false;
	}
}
