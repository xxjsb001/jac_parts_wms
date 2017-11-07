package com.vtradex.wms.server.utils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.vtradex.wms.server.service.GlobalParamUtils;

public class DoubleUtils{

	
	
	/**
	 * 将一个数字的字符串，格式化为标准的三位小数的显示方式并且四舍五入
	 * @param a
	 * @return
	 */
	public static double format3Fraction(Object a) {
		return formatByPrecision(a,3);
	}
	
	public static double format2Fraction(Object a){
		return formatByPrecision(a,2);
	}
	
	public static double format1Fraction(Object a){
		return formatByPrecision(a,1);
	}
	
	public static double formatByPrecision(Object o,int precision){
//		double pDouble = Double.parseDouble(o.toString());
		BigDecimal  bd = new  BigDecimal(o.toString());
		bd = bd.divide(new BigDecimal(1), precision, 4);
//		BigDecimal  bdWithPrecision = bd.setScale(precision,BigDecimal.ROUND_HALF_UP);
//		pDouble = bdWithPrecision.doubleValue();
//		double pDouble = bd.doubleValue();
		return bd.doubleValue();
	}
	public static void printFormatData(BigDecimal data, int scale) {
		data = data.divide(new BigDecimal(1), scale, 4);
//		System.out.println(data.toString());
		System.out.println(data.doubleValue());
		//printFormatData(a, 2);
	}
	
	public static double formateByDefaultPrecesion(Object o){
		int pre = Integer.valueOf(GlobalParamUtils.getGloableStringValue("maintainTableDecimal")).intValue();
		return formatByPrecision(o,pre);
	}
	
	/**
	 * 比较Double型字段大小
	 * 
	 * eg. source < target >>>> -1
	 * eg. source > target >>>> 1
	 * eg. source == target >>>> 0
	 * 
	 * @param source
	 * @param target
	 * @param precision
	 * @return
	 */
	public static int compareByPrecision(Double source, Double target, int precision) {
		BigDecimal src = (new BigDecimal(source.toString())).setScale(precision,BigDecimal.ROUND_HALF_UP);
		BigDecimal dst = (new BigDecimal(target.toString())).setScale(precision,BigDecimal.ROUND_HALF_UP);
		
		return src.compareTo(dst);
	}
	
	public static void main(String[] args) {
		System.out.println(DoubleUtils.formatByPrecision(10.055, 0));
		System.out.println(DoubleUtils.formatByPrecision(10.055, 1));
		System.out.println(DoubleUtils.formatByPrecision("10.055", 2));
		BigDecimal a = new BigDecimal("1.055");
		printFormatData(a, 2); 
		
		System.out.println(DoubleUtils.compareByPrecision(0.015D, 0.012D, 2));
		System.out.println(DoubleUtils.compareByPrecision(0.02D, 0.00002D, 2));
	}
	public static Double getDecimalHourByDate(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		
		int a = cal.get(Calendar.HOUR_OF_DAY);
		double b = cal.get(Calendar.MINUTE) / 60.00;
		//double c = cal.get(Calendar.SECOND) / 3600.00;
		double hour = a + b;
		
		return formatByPrecision(hour,4);
    }

}
