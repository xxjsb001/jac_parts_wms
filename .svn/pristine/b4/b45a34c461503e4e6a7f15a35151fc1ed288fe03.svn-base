package com.vtradex.wms.server.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.util.DateUtil;


public class StringHelper {
    
    /**打印obj 便于上线去掉*/
    public static void println(Object obj) {
        System.out.println(obj);
    }
    /**
     * @Description:   递归获取错误信息 传入一个excpetion
     * @Author:        <a href="xuyan.xia@vtradex.net">夏绪焰</a>
     * @CreateDate:    Nov 19, 2012 3:08:37 PM
     * @param e
     * @return:
     */
    public static String getErrorMessageByException(Throwable e) {
        if(e==null) {
            return null;
        }
        if(e.getMessage()==null && e.getCause()!=null) {
            return getErrorMessageByException(e.getCause());
        }
        return e.getMessage();
    }
    
    public static String replaceNullToEmpty(String str) {
        if(str==null) {
            return "";
        }
        return str;
    }
    
    public static Integer replaceNullToZero(Integer i) {
        return i==null?0:i;
    }
    
    public static Long replaceNullToZero(Long i) {
        return i==null?0L:i;
    }
    
    public static Double replaceNullToZero(Double i) {
        return i==null?0D:i;
    }
    
    public static Float replaceNullToZero(Float i) {
        return i==null?0F:i;
    }
    
    /**如果str1为null或者空则替换成str2*/
    public static String replaceNullOrEmptyToStr(String str1, String str2) {
        if(isNullOrEmpty(str1)) {
            return str2;
        }
        else {
            return str1;
        }
    }
    /**如果str等于str1 则返回str2 否则返回str*/
    public static String replaceStr(String str, String str1, String str2) {
        if(str==null && str1==null) {
            return str2;
        }
        else if(str1 !=null && str2!=null && str.equals(str1)) {
            return str2;
        }
        else {
            return str;
        }
    }
    
    /**
     * @Description:   判断str是否为null或空
     * @Author:        <a href="xuyan.xia@ah.vtradex.com">夏绪焰</a>
     * @CreateDate:    2011-1-19
     * @param str
     * @return:        ture(为null或者为空)  false(不为null且不为空)
     */
    public static boolean isNullOrEmpty(String str) {
        if (str==null || "".equals(str)) {
            return true;
        }
        return false;
    }
    /**
     * @Description:   判断str是否在String[] strArray中，区分大小写
     * @Author:        <a href="xuyan.xia@ah.vtradex.com">夏绪焰</a>
     * @CreateDate:    2011-1-18
     * @return:        true在 false不在
     */
    public static boolean in(String str, String[] strArray) {
        if (str==null) {            
            return false;
        }
        for (String s : strArray) {
            if (s != null && s.equals(str)) {
                return true;
            }
        }
        return false;
    }
    
    /**String类型的decode 模仿oracle的decode
     * StringHelper.decode(str,str1,str2,str3,str4,str5)
     * 如果str=str1 返回str2
     * 如果str=str3 返回str4
     * 依次类推
     * 都不等返回最后一个
     * */
    public static String decode(String... str) {
        if(str.length<=1 || str.length%2!=0) {
            throw new BusinessException("参数格式错误，参数必须为大于1的偶数个");
        }
        for(int i=1;i<=str.length-2;i=i+2) {
            String thisStr=str[i];
            String nextStr=str[i+1];
            if(str[0]==null && thisStr==null) {
                return nextStr;
            }
            if(str[0]!=null && thisStr!=null && str[0].equals(thisStr)) {
                return nextStr;
            }
            
        }
        return str[str.length-1];
    }
    
    public static String substring(String str,int length) {
        if(isNullOrEmpty(str)) {
            return str;
        }
        if(str.length()<=length) {
            return str;
        }
        else {
            return str.substring(0, length);
        }
    }
    /**删除最后一个字符*/
    public static String deleteLastChar(String str) {
        if(isNullOrEmpty(str)) {
            return str;
        }
        return substring(str,str.length()-1);
    }
    /**时间精确到毫秒+5位随机数*/
    public static String getUUID() {
        Random r = new Random();
        String dateStr=DateUtil.format(new Date(), "yyyyMMddHHmmssSSS");
        String rnd = r.nextInt(99999)+"";
        int n = 5-rnd.length();
        for(int i=0;i<n;i++) {
            rnd="0"+rnd;
        }
        return dateStr+""+rnd; //时间+5未随机数
    }
    
    /**
     * @Description:   将字符串拆分为整数数组
     * @Author:        <a href="hao.shen@vtradex.net"/>沈浩</a>
     * @CreateDate:    2012-10-31
     * @param:         str
     * @param:         sp
     * @return:        intArray
     */
    public static Integer[] splitStrToIntArray(String str,String sp) {

    	String[] tempStrArr = str.split(sp);
    	Integer[] intArray = new Integer[tempStrArr.length];
    	for(int i = 0;i<tempStrArr.length;i++) {
    		intArray[i] = Integer.parseInt(tempStrArr[i]);
    	}	
    	return intArray; 
    }
    
    public static void assertError(boolean flag,String errorMsg) {
        if(isNullOrEmpty(errorMsg)) {
            errorMsg="属性不能为空";
        }
        if (flag) {
             throw new BusinessException(errorMsg);
        }
    }
    public static void assertNullOrEmpty(String str,String errorMsg) {
        assertError(isNullOrEmpty(str), errorMsg);
    }
    public static void assertNull(Double d, String errorMsg) {
        assertError(d==null,errorMsg);
    }
    public static void assertNull(Long l, String errorMsg) {
        assertError(l==null,errorMsg);
    }
    public static void assertNull(Integer i, String errorMsg) {
        assertError(i==null,errorMsg);
    }
    public static void assertNull(Float f, String errorMsg) {
        assertError(f==null,errorMsg);
    }
    public static void assertNull(Date date, String errorMsg) {
        assertError(date==null,errorMsg);
    }
    /**
     * 是否是正整数 是返回true 否返回false
     */
    public static boolean isPositiveInteger(String str) {
        return isInteger(str) && Double.valueOf(str).intValue()>0;
    }
    /***
     * 判断str是否是整数  主要是处理11.0也算整数
     */
    public static boolean isInteger(String str) {
        try {
            Double.valueOf(str);
        }
        catch (Exception e) {
            return false;
        }
        boolean zs = Double.valueOf(str).equals(Double.valueOf(Double.valueOf(str).intValue()));
        return zs;
    }
    public static String readXml(String path){
        StringBuffer s = new StringBuffer(""); // 文件很长的话建议使用StringBuffer 
        try { 
            FileInputStream fis = new FileInputStream(path); 
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8"); 
            BufferedReader br = new BufferedReader(isr); 
            String line = null; 
            while ((line = br.readLine()) != null) { 
                s.append(line);
                s.append("\r\n"); // 补上换行符 
            } 
        } catch (Exception e) { 
            throw new BusinessException("读取文件失败"+e.getMessage());
        }
        return s.toString();
    }
    /**读取目录下的文件*/
    public static List<String> getFiles(String dirname){
    	File file = new File(dirname);
    	
    	if(!file.isDirectory()){
    		throw new BusinessException("你输入的不是一个文件夹，请检查路径是否有误！");
    	}
    	File[]files = file.listFiles();
    	List<String> filenames = new ArrayList<String>();
    	for(int i=0;i<files.length;i++){
    		if(files[i].isDirectory()||files[i].isHidden()){
    			continue;
    		}
    		if(files[i].isFile()){
    			filenames.add(files[i].getPath());
    		}
    	}
    	return filenames;
    }
    /** 移动文件
     * backPath 备份文件夹
     * srcPath 源文件夹
     * uploadFileName 文件名
     *  */
    public static void moveFile( String backPath, String srcPath, String uploadFileName) {
        File path=new File(backPath);
        if(!path.exists()) {
            path.mkdirs();
        }
        File serverPath=new File(srcPath);
        if(!serverPath.exists()) {
            serverPath.mkdirs();
        }
        copyFile(srcPath + uploadFileName, backPath+uploadFileName); //复制文件
        new File(srcPath + uploadFileName).delete(); //删除老文件        
    }
    
    /**
     * @Description:   复制文件
     * @Author:        <a href="xuyan.xia@ah.vtradex.com">夏绪焰</a>
     * @CreateDate:    2011-1-13
     * @param baseFile 需要复制的文件
     * @param newFile: 复制生成的文件
     * @return:         true复制成功  false复制失败
     */
    @SuppressWarnings("finally")
    public static boolean copyFile(File baseFile, File newFile) {
        boolean result = false;
        try {
            FileInputStream fis = new FileInputStream(baseFile);
            FileOutputStream fos = new FileOutputStream(newFile);
            try {
                byte[] buff = new byte[1024];
                int readed = -1;
                while ((readed = fis.read(buff)) > 0) {
                    fos.write(buff, 0, readed);
                }
                result=true;
            }
            catch (Exception e) {
                System.out.println("将文件从" + baseFile.getPath() + "复制到" + newFile.getPath() + "发生错误");
                e.printStackTrace();
                throw new RuntimeException("com.vtradex.license.util.FileUtils#copyFile: copy file error");
            }
            finally {
                fis.close();
                fos.close();
            }
        }
        catch (Exception ee) {
            System.out.println("将文件从" + baseFile.getPath() + "复制到" + newFile.getPath() + "发生错误");
            ee.printStackTrace();
        }
        finally {
            return result;
        }
    }
    
    /**
     * @Description:   复制文件
     * @Author:        <a href="xuyan.xia@ah.vtradex.com">夏绪焰</a>
     * @CreateDate:    2011-1-14
     * @param baseFileFullName 需要复制的文件 （路径+文件名）
     * @param newFileFullName: 复制生成的文件 （路径+文件名）
     * @return:         true复制成功  false复制失败
     */
    public static boolean copyFile(String baseFileFullName, String newFileFullName) {
        return copyFile(new File(baseFileFullName), new File(newFileFullName));
    }

    /**将数字类型的字符串去掉后面的0返回  比如 11.0返回11  11.10返回11.1*/
    public static String removeLastZero(String str) {
        Double d = null;
        if(isNullOrEmpty(str)) {
        	return "";
        }
        str= str.trim();
        if(isNullOrEmpty(str)) {
        	return "";
        }
        try {
            d = Double.valueOf(str);
        }
        catch (Exception e) {
        	return str;
//            throw new BusinessException(str+"不是数字");
        }
        if(isInteger(str)) {
            return d.intValue()+"";
        }
        return d.toString();
    }
    public static String trim(String str) {
        if(str==null) {
            return str;
        }
        return str.trim();
    }
    public static double replaceStrToDouble(String str) {
    	if(isNullOrEmpty(str)) {
    		return 0D;
    	}
    	try {
    		return Double.valueOf(str);
    	}
    	catch (Exception e) {
			throw new BusinessException(str+"不是数字");
		}
    }
    /**处理库位占用率*/
    public static Double dealRate(Double rate) {
        if(rate == null) {
            rate = 0d;
        }
        if(rate>=99.98d) { //超过这个值认为100% xuyan.xia
            rate =100d;
        }
        return rate;
    }
    
    /**长度不足n在字符串str后补c*/
    public static String addCharAfterStr(String str,int length,String c) {
        return addCharToStr(str, length, c, false);
    }
    
    /**长度不足n在字符串str前补c*/
    public static String addCharBeforeStr(String str,int length,String c) {
        return addCharToStr(str, length, c, true);
    }
    /**长度不足n在字符串str前面后者后面补c
     * before 为true在前面  false在后面
     * */
    private static String addCharToStr(String str,int length,String c,boolean before) {   
        if(length<=0) {
            return str;
        }
        if(isNullOrEmpty(c) || c.length()!=1) {
            throw new BusinessException("参数错误");
        }
        str = replaceNullToEmpty(str);
        while(str.length()<length) {
            if(before) {
                str = c + str;
            }
            else {
                str = str + c;
            }
        }
        return str;
    }
    public static String double2String(Double d) {
        if(d==null) {
            return "";
        }
        String c =  new DecimalFormat("#0.0000000000").format(d);
        while(c.indexOf(".")>=0 && (c.endsWith("0") || c.endsWith("."))) {
            c = c.substring(0,c.length()-1);
        }
        return c;
    }
    /**处理double的误差*/
    public static Double dealDoubleError(Double d) {
        if(d==null) {
            return null;
        }
        return DoubleUtils.formatByPrecision(d, 8); //小数相减出现无限循环 比如 301.9d - 211.5d; 保留小数后8位
    }
    /**获取ERP部门编码*/
    public static String getDepartCode(String supplyCode) {
        String departCode="PR01";
        if(!isNullOrEmpty(supplyCode) && in(supplyCode,new String[]{"3005","7301","3023","3041","3040","3039","CW1250"})) {
            departCode = "PR02"; //集采
        }
        return departCode;
    }
}
