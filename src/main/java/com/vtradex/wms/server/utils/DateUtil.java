package com.vtradex.wms.server.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author <a href="yan.li@vtradex.com">李炎</a>
 * @description 日期工具类s
 * 
 */
public class DateUtil {

	private static Log log = LogFactory.getLog(DateUtil.class);

	public static String DEFAULT_DATE_PATTERN = getDatePattern();

	public static final String HMS_DATE_PATTERN = "HH:mm:ss";

	public static final String YMSHMS_DATE_PATTERN = getYMD_HMSDatePattern();

	public static SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat ymd2 = new SimpleDateFormat("yyyy/MM/dd");
	
	public static SimpleDateFormat dmy = new SimpleDateFormat("dd-MM-yyyy");
	public static SimpleDateFormat dmy2 = new SimpleDateFormat("dd/MM/yyyy");
	
	public static SimpleDateFormat dmy_hms = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	public static SimpleDateFormat dmy_hms2 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	public static SimpleDateFormat dmy_hm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static SimpleDateFormat dmy_hm2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

	public static SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");
	
	public static SimpleDateFormat hm = new SimpleDateFormat("HH:mm");

	public static SimpleDateFormat ymd_hms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat ymd_hms2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public static SimpleDateFormat ymd_hm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static SimpleDateFormat ymd_hm2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

	public static SimpleDateFormat yy = new SimpleDateFormat("yyyyMMdd");

	private static Calendar cale = Calendar.getInstance();

	private static final String YMD_KEY = "ymd";
	private static final String YMD_KEY2 = "ymd2";
	
	private static final String DMY_KEY = "dmy";
	private static final String DMY_KEY2 = "dmy2";
	
	private static final String DMY_HMS_KEY = "dmy_hms";
	private static final String DMY_HMS_KEY2 = "dmy_hms2";
	
	private static final String DMY_HM_KEY = "dmy_hm";
	private static final String DMY_HM_KEY2 = "dmy_hm2";

	private static final String HMS_KEY = "hms";

	private static final String YMD_HMS_KEY = "ymd_hms";
	private static final String YMD_HMS_KEY2 = "ymd_hms2";

	private static final String YMD_HM_KEY = "ymd_hm";
	private static final String YMD_HM_KEY2 = "ymd_hm2";

	private static final String YY = "yy";
	
	public static final String ymdPattern = "^\\d{4}[-]\\d{1,2}[-]\\d{1,2}$";
	public static final String ymdPattern2 = "^\\d{4}[/]\\d{1,2}[/]\\d{1,2}$";
	public static final String ymdPattern3 = "^\\d{4}\\d{2}\\d{2}$";
	
	public static final String dmyPattern = "^\\d{1,2}[-]\\d{1,2}[-]\\d{4}$";
	public static final String dmyPattern2 = "^\\d{1,2}[/]\\d{1,2}[/]\\d{4}$";
	
	public static final String dmy_hmsPattern = "^\\d{1,2}[-]\\d{1,2}[-]\\d{4}\\s\\d{1,2}[:]\\d{1,2}[:]\\d{1,2}$";
	public static final String dmy_hmsPattern2 = "^\\d{1,2}[/]\\d{1,2}[/]\\d{4}\\s\\d{1,2}[:]\\d{1,2}[:]\\d{1,2}$";
	
	public static final String dmy_hmPattern = "^\\d{1,2}[-]\\d{1,2}[-]\\d{4}\\s\\d{1,2}[:]\\d{1,2}$";
	public static final String dmy_hmPattern2 = "^\\d{1,2}[/]\\d{1,2}[/]\\d{4}\\s\\d{1,2}[:]\\d{1,2}$";
	

	public static final String hmsPattern = "^\\d{1,2}[:]\\d{1,2}[:]\\d{1,2}$";

	public static final String ymd_hmsPattern = "^\\d{4}[-]\\d{1,2}[-]\\d{1,2}\\s\\d{1,2}[:]\\d{1,2}[:]\\d{1,2}$";
	public static final String ymd_hmsPattern2 = "^\\d{4}[/]\\d{1,2}[/]\\d{1,2}\\s\\d{1,2}[:]\\d{1,2}[:]\\d{1,2}$";

	public static final String ymd_hmPattern = "^\\d{4}[-]\\d{1,2}[-]\\d{1,2}\\s\\d{1,2}[:]\\d{1,2}$";
	public static final String ymd_hmPattern2 = "^\\d{4}[/]\\d{1,2}[/]\\d{1,2}\\s\\d{1,2}[:]\\d{1,2}$";

	public static final String yyPattern = "^\\d{8}$";

	public final static String MONTH = "MONTH";
	
	public final static String WEEK = "WEEK";
	
	public final static String DAY = "DAY";

	public final static String HOUR = "HOUR";

	public final static String MINUTE = "MINUTE";

	public final static String SECOND = "SECOND";

	public static Map<String, SimpleDateFormat> formatPattern = new HashMap<String, SimpleDateFormat>();

	public static DateFormat getDateFormatPattern(String str) {
		str.trim();
		if (str.matches(ymdPattern)) {
			return formatPattern.get(YMD_KEY);
		} else if(str.matches(ymdPattern2)) {
			return formatPattern.get(YMD_KEY2);
		} else if (str.matches(hmsPattern)) {
			return formatPattern.get(HMS_KEY);
		} else if (str.matches(ymd_hmsPattern)) {
			return formatPattern.get(YMD_HMS_KEY);
		} else if (str.matches(ymd_hmsPattern2)) {
			return formatPattern.get(YMD_HMS_KEY2);
		} else if (str.matches(ymd_hmPattern)) {
			return formatPattern.get(YMD_HM_KEY);
		} else if (str.matches(ymd_hmPattern2)) {
			return formatPattern.get(YMD_HM_KEY2);
		} else if (str.matches(yyPattern)) {
			return formatPattern.get(YY);
		} else if (str.matches(dmyPattern)) {
			return formatPattern.get(DMY_KEY);
		} else if (str.matches(dmyPattern2)) {
			return formatPattern.get(DMY_KEY2);
		} else if (str.matches(dmy_hmsPattern)) {
			return formatPattern.get(DMY_HMS_KEY);
		} else if (str.matches(dmy_hmsPattern2)) {
			return formatPattern.get(DMY_HMS_KEY2);
		} else if (str.matches(dmy_hmPattern)) {
			return formatPattern.get(DMY_HM_KEY);
		} else if (str.matches(dmy_hmPattern2)) {
			return formatPattern.get(DMY_HM_KEY2);
		} 
		return new SimpleDateFormat();
	}
	
	/**
	 * 验证日期格式
	 * @param dateStr
	 * @param pattern
	 */
	public static boolean verifyDateFormat(String dateStr, String pattern) {
		if (dateStr.matches(pattern)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public static Date getDateFormat(String str) {
		str.trim();
		if (str.matches(ymdPattern)) {
			return formatStrToDateYMD(str);
		} else if (str.matches(hmsPattern)) {
			return formatDateHMS(str);
		} else if (str.matches(ymd_hmsPattern)) {
			return formatStrToDate(str);
		} else if (str.matches(ymd_hmPattern)) {
			return formatDateYMD_HM(str);
		} else if (str.matches(yyPattern)) {
			return formatDate(str);
		}
		return new Date();
	}

	public static Date formatDate(String str) {
		try {
			return yy.parse(str);
		} catch (Exception e) {
			log.debug("DateUtil.formatDate():" + e.getMessage());
			return null;
		}
	}
	
	public static Date formatDateByPattern(String dateString) {
		try {
			if (dateString.equals("0000/00/00")) {
				return null;
			}
			DateFormat format = getDateFormatPattern(dateString);
			return format.parse(dateString);
		} catch (Exception e) {
			log.debug("DateUtil.formatDate():" + e.getMessage());
			return null;
		}
	}

	/**
	 * 
	 * @param str
	 *            符串时期格式形式
	 * @param pattern
	 *            格式规格
	 * @return 格式后的字符
	 */
	public static String formatDateByPattern(String str, String pattern) {
		try {
			if (StringUtils.isEmpty(pattern)) {
				return str;
			}
			return formatPattern.get(pattern).format(formatStrToDate(str));
		} catch (Exception e) {
			log.debug("DateUtil.formatDateByPattern():" + e.getMessage());
			return "";
		}
	}

	/**
	 * 
	 * 返回默认的日期格式
	 * 
	 */
	public static synchronized String getDatePattern() {
		DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
		return DEFAULT_DATE_PATTERN;
	}

	public static String getYMD_HMSDatePattern() {
		return DEFAULT_DATE_PATTERN + " " + HMS_DATE_PATTERN;
	}

	/**
	 * 传入格式为日期的字符, 格式为：yyyy-MM-dd 的日期字符串形式返回
	 */
	public static Date formatStrToDateYMD(String str) {
		try {
			return ymd.parse(str);
		} catch (Exception e) {
			log.debug("DateUtil.formatStrToDateYMD():" + e.getMessage());
			return null;
		}

	}

	public static String formatDateYMDToStr(Date date) {
		try {
			return ymd.format(date);
		} catch (Exception e) {
			log.debug("DateUtil.formatDateYMDToStr():" + e.getMessage());
			return null;
		}

	}

	/**
	 * 传入格式为日期的字符, 格式为：yyyy-MM-dd HH:mm:ss 的日期字符串形式返回
	 */
	public static Date formatStrToDate(String str) {
		try {
			return ymd_hms.parse(str);
		} catch (Exception e) {
			log.debug("DateUtil.formatStrToDate():" + e.getMessage());
			return null;
		}

	}

	public static String formatDateToStr(Date date) {
		try {
			return ymd_hms.format(date);
		} catch (Exception e) {
			log.debug("DateUtil.formatDateToStr():" + e.getMessage());
			return null;
		}

	}

	public static String formatDateToYMD_HM(Date date) {
		try {
			return ymd_hm.format(date);
		} catch (Exception e) {
			log.debug("DateUtil.formatDateToYMD_HM():" + e.getMessage());
			return null;
		}

	}

	/**
	 * 传入格式为日期的字符, 格式为：yyyy-MM-dd HH:mm 的日期字符串形式返回
	 */
	public static Date formatDateYMD_HM(String str) {
		try {
			return ymd_hm.parse(str);
		} catch (Exception e) {
			log.debug("DateUtil.formatDateYMD_HM():" + e.getMessage());
			return null;
		}

	}

	/**
	 * 传入格式为日期的字符, 格式为：HH:mm:ss 的日期字符串形式返回
	 */
	public static Date formatDateHMS(String str) {
		try {
			return hms.parse(str);
		} catch (Exception e) {
			log.debug("DateUtil.formatDateHMS:" + e.getMessage());
			return null;
		}

	}

	/**
	 * 获得服务器当前日期及时间，以格式为：yyyy-MM-dd HH:mm:ss的日期字符串形式返回
	 */
	public static String getDateTime() {
		try {
			return ymd_hms.format(cale.getTime());
		} catch (Exception e) {
			log.debug("DateUtil.getDateTime():" + e.getMessage());
			return "";
		}
	}

	/**
	 * 获得服务器当前日期，以格式为：yyyy-MM-dd的日期字符串形式返回
	 */
	public static String getDate() {
		try {
			return ymd.format(cale.getTime());
		} catch (Exception e) {
			log.debug("DateUtil.getDate():" + e.getMessage());
			return "";
		}
	}

	/**
	 * 获得服务器当前时间，以格式为：HH:mm:ss的日期字符串形式返回
	 */
	public static String getTime() {
		String temp = " ";
		try {
			temp += hms.format(cale.getTime());
			return temp;
		} catch (Exception e) {
			log.debug("DateUtil.getTime():" + e.getMessage());
			return "";
		}
	}

	/**
	 * 获得服务器当前日期的年份
	 */
	public static String getYear() {
		try {
			return String.valueOf(cale.get(Calendar.YEAR));
		} catch (Exception e) {
			log.debug("DateUtil.getYear():" + e.getMessage());
			return "";
		}
	}

	/**
	 * 获得服务器当前日期的月份
	 */
	public static String getMonth() {
		try {
			java.text.DecimalFormat df = new java.text.DecimalFormat();
			df.applyPattern("00;00");
			return df.format((cale.get(Calendar.MONTH) + 1));
		} catch (Exception e) {
			log.debug("DateUtil.getMonth():" + e.getMessage());
			return "";
		}
	}

	/**
	 * 获得服务器在当前月中天数
	 */
	public static String getDay() {
		try {
			return String.valueOf(cale.get(Calendar.DAY_OF_MONTH));
		} catch (Exception e) {
			log.debug("DateUtil.getDay():" + e.getMessage());
			return "";
		}
	}

	/**
	 * 比较两个日期相差的天数
	 * 
	 */
	public static int getMargin(String date1, String date2) {
		int margin;
		try {
			ParsePosition pos = new ParsePosition(0);
			ParsePosition pos1 = new ParsePosition(0);
			Date dt1 = ymd.parse(date1, pos);
			Date dt2 = ymd.parse(date2, pos1);
			long l = dt1.getTime() - dt2.getTime();
			margin = (int) (l / (24 * 60 * 60 * 1000));
			return margin;
		} catch (Exception e) {
			log.debug("DateUtil.getMargin():" + e.toString());
			return 0;
		}
	}

	/**
	 * 比较两个日期相差的天数
	 */
	public static double getDoubleMargin(String date1, String date2) {
		double margin;
		try {
			ParsePosition pos = new ParsePosition(0);
			ParsePosition pos1 = new ParsePosition(0);
			Date dt1 = ymd_hms.parse(date1, pos);
			Date dt2 = ymd_hms.parse(date2, pos1);
			long l = dt1.getTime() - dt2.getTime();
			margin = (l / (24 * 60 * 60 * 1000.00));
			return margin;
		} catch (Exception e) {
			log.debug("DateUtil.getMargin():" + e.toString());
			return 0;
		}
	}

	/**
	 * 比较两个日期相差的月数
	 */
	public static int getMonthMargin(String date1, String date2) {
		int margin;
		try {
			margin = (Integer.parseInt(date2.substring(0, 4)) - Integer
					.parseInt(date1.substring(0, 4))) * 12;
			margin += (Integer.parseInt(date2.substring(4, 7).replaceAll("-0",
					"-")) - Integer.parseInt(date1.substring(4, 7).replaceAll(
					"-0", "-")));
			return margin;
		} catch (Exception e) {
			log.debug("DateUtil.getMargin():" + e.toString());
			return 0;
		}
	}

	/**
	 * 初始化
	 * 
	 */
	private static void addFormatPattern() {
		formatPattern.put(YMD_KEY, ymd);
		formatPattern.put(YMD_KEY2, ymd2);
		formatPattern.put(HMS_KEY, hms);
		formatPattern.put(YMD_HMS_KEY, ymd_hms);
		formatPattern.put(YMD_HMS_KEY2, ymd_hms2);
		formatPattern.put(YMD_HM_KEY, ymd_hm);
		formatPattern.put(YMD_HM_KEY2, ymd_hm2);
		formatPattern.put(YY, yy);
		formatPattern.put(DMY_KEY, dmy);
		formatPattern.put(DMY_KEY2, dmy2);
		formatPattern.put(DMY_HMS_KEY, dmy_hms);
		formatPattern.put(DMY_HMS_KEY2, dmy_hms2);
		formatPattern.put(DMY_HM_KEY, dmy_hm);
		formatPattern.put(DMY_HM_KEY2, dmy_hm2);
	}

	static {
		addFormatPattern();
	}

	/*
	 * DAY, HOUR, MINUTE,SECOND 多种方式增加时间
	 * 
	 * @see com.order007.server.util.EasyDate#increaseDate(java.util.Date,
	 *      java.lang.String, int)
	 */
	public static Date increaseDate(Date date, String increaseType, int delta) {

		Calendar cal = new java.util.GregorianCalendar();
		try {
			cal.setTime(date);
			
			if (increaseType.equals(MONTH)) {
				cal.add(Calendar.MONTH, delta);
			}
			
			if (increaseType.equals(WEEK)) {
				cal.add(Calendar.WEEK_OF_MONTH, delta);
			}
			
			if (increaseType.equals(DAY)) {
				cal.add(Calendar.DATE, delta);
			}

			if (increaseType.equals(HOUR)) {
				cal.add(Calendar.HOUR, delta);
			}

			if (increaseType.equals(MINUTE)) {
				cal.add(Calendar.MINUTE, delta);
			}

			if (increaseType.equals(SECOND)) {
				cal.add(Calendar.SECOND, delta);
			}

			return cal.getTime();

		} catch (Exception e) {

			throw new RuntimeException("date format error");
		}
	}

	/*
	 * 将一个 “1999-12-01 12:22:00" 的字符串转换为Date 类型返回
	 */
	public static Date getDate(String sDate) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return df.parse(sDate);
		} catch (ParseException e) {

			throw new RuntimeException("date format error");
		}
	}

	/**
	 * 将指定格式的字符串转为Date
	 * 
	 * @param sDate
	 * @param format
	 *            遵循 simpleDateFormat 规范
	 * @return
	 */
	public static Date getDate(String sDate, String format) {

		DateFormat df = new SimpleDateFormat(format);
		try {
			return df.parse(sDate);
		} catch (ParseException e) {

			throw new RuntimeException("date format error");
		}
	}

	/**
	 * 用于产生指定日期的时间对。 从当天0:00:00 秒 到第二天 0:0:0 秒
	 * 
	 * @param date
	 * @return Date[0] 起始时间， Date[1] 终止时间
	 */
	public static Date[] getOneDayRange(Date date) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date[] d = new Date[] { null, null };
		Calendar cal = new java.util.GregorianCalendar();
		try {
			d[0] = df.parse(df.format(date));
			cal.setTime(df.parse(df.format(date)));
			cal.add(Calendar.DATE, 1);
			d[1] = cal.getTime();
		} catch (Exception e) {

			throw new RuntimeException("convert date error");
		}

		return d;
	}

	/**
	 * 把一个Date 按照指定格式转换为String
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	/**
	 * 将使用hh:mm 描述的时间，转换为相对于0点的秒数
	 * 
	 * @param time
	 */
	public static Integer getSeconds(String st) {

		if (st == null || st.length() < 3)
			return 0;

		int seperator = st.indexOf(":");
		if (seperator <= 0) {
			return 0;
		}

		String h = st.substring(0, seperator);
		String m = st.substring(seperator + 1);

		return (Integer.parseInt(h) * 60 + Integer.parseInt(m)) * 60;
	}

	public static int getMinute(double d) {
		return (int) Math.ceil(d * 60);
	}

	/**
	 * 将使用Date 描述的时间，转换为相对于当天 0点的秒数
	 * 
	 * @param time
	 */
	public static Integer getIntTime(Date d) {

		Calendar cal = new GregorianCalendar();
		cal.setTime(d);
		return (cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE))
				* 60 + cal.get(Calendar.SECOND);
	}

	/**
	 * 判断两个date 是否同一天
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static Boolean isSameDay(Date d1, Date d2) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(d1);
		int id1 = cal.get(Calendar.DAY_OF_YEAR);
		int iy1 = cal.get(Calendar.YEAR);
		cal.setTime(d2);
		int id2 = cal.get(Calendar.DAY_OF_YEAR);
		int iy2 = cal.get(Calendar.YEAR);

		return (id1 == id2 && iy1 == iy2);
	}

	/**
	 * 将一个java.sql.Timestamp 型的时间转换为一个 java.util.Date 的时间
	 * 
	 * @param t
	 *            Date
	 * @return
	 */
	public static Date timeStamp2Date(Date t) {

		Date d = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm ss");

		try {
			d = df.parse(df.format(t));
		} catch (Exception e) {
			// do nothing
		}

		return d;
	}

	/**
	 * 获取明天凌晨零点的日期
	 * 
	 * @return Date
	 */
	public static Date getTomorrowBegin() {

		Date d[] = DateUtil.getTodayRange();

		return d[1];
	}

	/**
	 * 获取今天凌晨零点的日期
	 * 
	 * @return Date
	 */
	public static Date getTodayBegin() {

		Date d[] = DateUtil.getTodayRange();

		return d[0];
	}

	/**
	 * 获得当天的范围
	 * 
	 * @return d[0] 今天凌晨0点， d[1] 明天凌晨零点
	 */
	public static Date[] getTodayRange() {

		return DateUtil.getOneDayRange(new Date());
	}

	// 获得当前日期与本周日相差的天数
	private static int getWeekPlus() {
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 1) {
			return 0;
		} else {
			return 1 - dayOfWeek;
		}
	}

	public static Date getFirstHourOFDay() {
		Calendar firstDate = Calendar.getInstance();
		firstDate.set(Calendar.HOUR_OF_DAY, 0);
		firstDate.set(Calendar.MINUTE, 0);
		firstDate.set(Calendar.SECOND, 0);
		return firstDate.getTime();
	}

	// 获取当月第一天
	public static Date getFirstDayOfMonth() {
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.set(Calendar.HOUR_OF_DAY, 0);
		lastDate.set(Calendar.MINUTE, 0);
		lastDate.set(Calendar.SECOND, 0);
		return lastDate.getTime();
	}

	// 获得本周日的日期
	public static Date getSundayOFWeek() {
		int mondayPlus = getWeekPlus() - 1;
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		currentDate.set(GregorianCalendar.HOUR_OF_DAY, 0);
		currentDate.set(GregorianCalendar.MINUTE, 0);
		currentDate.set(GregorianCalendar.SECOND, 0);
		return currentDate.getTime();
	}

	// 获得本周一的日期
	public static Date getMondayOFWeek() {
		int mondayPlus = getWeekPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		currentDate.set(GregorianCalendar.HOUR_OF_DAY, 0);
		currentDate.set(GregorianCalendar.MINUTE, 0);
		currentDate.set(GregorianCalendar.SECOND, 0);
		return currentDate.getTime();
	}

	// 获得当前日期与本周日相差的天数
	public static int getMondayPlus() {
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 1) {
			return 0;
		} else {
			return 1 - dayOfWeek;
		}
	}

	// 获得下周星期一的日期
	public static Date getNextMonday() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		return currentDate.getTime();
	}
	
	//获得下一天的时间
	public static Date getNextDay(){
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DAY_OF_MONTH, 1);
		return currentDate.getTime();
	}
	
	//获得上一天的时间
	public static Date getAscendDay(){
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DAY_OF_MONTH, -1);
		return currentDate.getTime();
	}

	// 获得下周星期日的日期
	public static Date getNextSunday() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		return currentDate.getTime();
	}

	/**
	 * 时间计算
	 */
	public static Date addHourToDate(Date date, double hour) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, (int) Math.ceil(hour * 60));
		return c.getTime();
	}
	
	public static Date addMinuteToDate(Date date,int min){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, min);
		return c.getTime();
	}
	
	public static Date addDayToDate(Date date , int day){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, day);
		return c.getTime();
	}
	
	public static Date getFirstHourOFDay(Date date) {
		Calendar firstDate = Calendar.getInstance();
		firstDate.setTime(date);
		firstDate.set(Calendar.HOUR_OF_DAY, 0);
		firstDate.set(Calendar.MINUTE, 0);
		firstDate.set(Calendar.SECOND, 0);
		return firstDate.getTime();
	}

	/**
	 * 根据一个日期，返回是星期几的字符串
	 * 
	 * @param sdate
	 * @return
	 */
	public static String getWeek(Date date) {
		// 再转换为时间
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// int hour=c.get(Calendar.DAY_OF_WEEK);
		// hour中存的就是星期几了，其范围 1~7
		// 1=星期日 7=星期六，其他类推
		return new SimpleDateFormat("EEEE").format(c.getTime());
	}
	
	public static int getWeekNumber(Date date){
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		return cd.get(Calendar.DAY_OF_WEEK) - 1; 
	}
	public static int getDayNumber(Date date){
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		return cd.get(Calendar.DAY_OF_MONTH); 
	}
	
	/**
	 * 将使用hh:mm 描述的时间，转换为相对于0点的分钟数
	 * 
	 * @param time
	 */
	public static Integer getMinute(String st){
		if (st == null || st.length() < 3)
			return 0;

		int seperator = st.indexOf(":");
		if (seperator <= 0) {
			return 0;
		}

		String h = st.substring(0, seperator);
		String m = st.substring(seperator + 1);

		return (Integer.parseInt(h.trim()) * 60 + Integer.parseInt(m.trim()));
	}
	
	public static Date setMinuteToDate(Date date,int min){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, min);
		return c.getTime();
	}
	
	/**
	 * hh:mm
	 * @param date
	 * @param min
	 * @return
	 */
	public static Date setMinuteToDate(Date date,String min){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, getMinute(min));
		return c.getTime();
	}
	
	public static Integer getSeconds(Date date){
		String str = hm.format(date);
		return getSeconds(str);
	}
	
	public static Integer getMinute(Date date){
		String str = hm.format(date);
		return getMinute(str);
	}
	
	public static void main(String[] args) {
		String str = "00:00";
		System.out.println(DateUtil.getMinute(str));
		System.out.println(DateUtil.getMinute("23:59"));
	}
	
	/** 
     * 获得日期字符串数组 
     * @param calendarType 日期跨度的类型， 
     * */  
      
    public static Date[] getDateArrays(Date start,Date end ,int calendarType){  
        ArrayList<Date> ret = new ArrayList<Date>();  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(start);  
        Date tmpDate = calendar.getTime();  
        long endTime = end.getTime();  
        while(tmpDate.before(end)||tmpDate.getTime() == endTime){  
            ret.add(calendar.getTime());  
            calendar.add(calendarType, 1);  
            tmpDate = calendar.getTime();  
        }         
          
        Date[] dates = new Date[ret.size()];  
        return ret.toArray(dates);        
    }  
    public static String formatDateHMToStr(Date date) {
		try {
			return hm.format(date);
		} catch (Exception e) {
			log.debug("DateUtil.formatDateYMDToStr():" + e.getMessage());
			return null;
		}

	}
}