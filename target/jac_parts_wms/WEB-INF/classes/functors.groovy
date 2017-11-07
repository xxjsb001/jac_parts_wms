String 拼接条件(String condition,Double zoneBegin,Double zoneEnd,Double lineBegin,Double lineEnd,
	Double colBegin,Double colEnd,Double layerBegin,Double layerEnd){
  if(condition == null){
  	condition = "";
  }
  String result =  condition + 
  				" ((location.ZONE_NO >= " + zoneBegin + " AND location.ZONE_NO <= " + zoneEnd + ") AND " +
  				"(location.LINE_NO >= " + lineBegin + " AND location.LINE_NO <= " + lineEnd + ") AND " +
  				"(location.COL_NO >= " + colBegin + " AND location.COL_NO <= " + colEnd + ") AND " + 
  				"(location.LAYER_NO >= " + layerBegin + " AND location.LAYER_NO <= " + layerEnd + "))"; 

  return result;
  
}

Date 多种方式增加时间(Date date, String increaseType, Double delta){
	return com.vtradex.wms.server.utils.DateUtil.increaseDate(date,increaseType,delta.intValue());
}

Integer 获取几号(Date date){
  return com.vtradex.wms.server.utils.DateUtil.getDayNumber(date);
}

String 出锁拼接(String condition,String areaCode,Double zoneBegin,Double zoneEnd,Double lineBegin,Double lineEnd,
	Double colBegin,Double colEnd,Double layerBegin,Double layerEnd){
  if(condition == null){
  	condition = "";
  }

  String result =  condition + " (area.CODE = " + "'" + areaCode + "'" + " AND " +
  				"(location.ZONE_NO >= " + zoneBegin + " AND location.ZONE_NO <= " + zoneEnd + ") AND " +
  				"(location.LINE_NO >= " + lineBegin + " AND location.LINE_NO <= " + lineEnd + ") AND " +
  				"(location.COL_NO >= " + colBegin + " AND location.COL_NO <= " + colEnd + ") AND " + 
  				"(location.LAYER_NO >= " + layerBegin + " AND location.LAYER_NO <= " + layerEnd + "))"; 

  return result;
  
}

String 取子字符串(String str,Integer start,Integer end){
	String result = "";
	result = str.substring(start,end);
    return result;
}

Date 转化为日期(String str){
	return com.vtradex.wms.server.utils.DateUtil.getDateFormat(str);
}

Integer 转化为分钟(String str){
	return  com.vtradex.wms.server.utils.DateUtil.getMinute(str);
}

Integer 转化为分钟(Date date){
	return  com.vtradex.wms.server.utils.DateUtil.getMinute(date);
}

Integer 字符串长度(String str){
	return str.length();
}

Map 执行规则(String warehouseName,String orgName,String referenceRule,Map<String, Object> problem){
	return getBean("wmsRuleManager").execute(warehouseName,orgName,referenceRule,problem);
}

Double 包装数量转换(Double quantityBU, Integer convertFigure, Integer precision) {
	return com.vtradex.wms.server.utils.PackageUtils.convertPackQuantity(quantityBU, convertFigure, precision);
}

Double 转化数(Object str){
	return Double.valueOf(str);
}

Integer 转化整数(Object str){
	return Integer.valueOf(str);
}

Boolean 开头包含(String str,String value){
	return str.startsWith(value);
}
Boolean 包含(String str,String value){
	return str.contains(value);
}

