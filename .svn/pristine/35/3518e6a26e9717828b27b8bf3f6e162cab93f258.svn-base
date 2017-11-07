package com.vtradex.wms.server.utils;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.vtradex.thorn.server.util.DateUtil;
import com.vtradex.wms.server.model.base.ShipLotInfo;

/**
 * 批次属性工具类
 *
 * @category Util
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.3 $Date: 2015/04/20 05:33:52 $
 */
public class LotInfoUtil {
	
	/**
	 * @param problem
	 * @param shipLotInfo 发货批次属性
	 * @param isMatchLot 是否严格批次匹配
	 */
	public static void generateShipLotInfo(Map<String, Object> problem, ShipLotInfo shipLotInfo, Boolean isMatchLot) {
		if (isMatchLot) {
			convertStringLotInfo(problem, shipLotInfo.getSoi(), "收货单号");
			convertStringLotInfo(problem, shipLotInfo.getSupplier(), "供应商");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC1(), "扩展属性1");//JAC工艺状态
//			convertStringLotInfo(problem, JavaTools.dateYMDToStr(shipLotInfo.getStorageDate()),"存货日期");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC2(), "扩展属性2");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC3(), "扩展属性3");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC4(), "扩展属性4");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC5(), "扩展属性5");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC6(), "扩展属性6");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC7(), "扩展属性7");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC8(), "扩展属性8");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC9(), "扩展属性9");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC10(), "扩展属性10");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC11(), "扩展属性11");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC12(), "扩展属性12");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC13(), "扩展属性13");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC14(), "扩展属性14");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC15(), "扩展属性15");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC16(), "扩展属性16");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC17(), "扩展属性17");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC18(), "扩展属性18");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC19(), "扩展属性19");
			convertStringLotInfo(problem, shipLotInfo.getExtendPropC20(), "扩展属性20");
		} else {
			problem.put("收货单号", "");
			problem.put("供应商", "");
			problem.put("扩展属性1", "");
			problem.put("扩展属性2", "");
			problem.put("扩展属性3", "");
			problem.put("扩展属性4", "");
			problem.put("扩展属性5", "");
			problem.put("扩展属性6", "");
			problem.put("扩展属性7", "");
			problem.put("扩展属性8", "");
			problem.put("扩展属性9", "");
			problem.put("扩展属性10", "");
			problem.put("扩展属性11", "");
			problem.put("扩展属性12", "");
			problem.put("扩展属性13", "");
			problem.put("扩展属性14", "");
			problem.put("扩展属性15", "");
			problem.put("扩展属性16", "");
			problem.put("扩展属性17", "");
			problem.put("扩展属性18", "");
			problem.put("扩展属性19", "");
			problem.put("扩展属性20", "");
		}
	}
	
	/**
	 * 批次属性为日期时参数处理
	 * 
	 * @param problem 
	 * @param key 键
	 * @param value 值
	 */
	@SuppressWarnings("unused")
	private static void convertDateLotInfo(Map<String, Object> problem, String key, String value) {
		if (!StringUtils.isEmpty(value)) {
			if (value.startsWith("(")) {
				String[] dates = value.split(",");
				problem.put(key + "BEGIN", DateUtil.formatStrToDateYMD(dates[0].substring(1)));
				problem.put(key + "END", DateUtil.formatStrToDateYMD(dates[1].substring(0, dates[1].length() - 1)));
				problem.put(key, "");
			} else {
				problem.put(key + "BEGIN", "");
				problem.put(key + "END", "");
				problem.put(key, DateUtil.formatStrToDateYMD(value));
			}
		} else {
			problem.put(key + "BEGIN", "");
			problem.put(key + "END", "");
			problem.put(key, "");
		}
	}
	
	/**
	 * 批次属性为字符串时参数处理
	 * 
	 * @param problem 
	 * @param key 键
	 * @param value 值
	 */
	private static void convertStringLotInfo(Map<String, Object> problem, String value, String key) {
		if (!StringUtils.isEmpty(value)) {
			problem.put(key, value);
		} else {
			problem.put(key, "");
		}
	}
}
