package com.vtradex.wms.server.utils;

import com.vtradex.wms.server.model.organization.WmsPackageUnit;

public class PackageUtils {
	
	/**
	 * 根据基本包装数量计算对应大包装数量
	 * @param quantityBU
	 * @param packageUnit
	 * @return
	 */
	public static Double convertPackQuantity(Double quantityBU, WmsPackageUnit packageUnit) {
		if (packageUnit.getItem().getPrecision() == 0) {
			//向上取整，得到整包装数量
			return Math.ceil(quantityBU / packageUnit.getConvertFigure());
		} else {
			//根据包装精度计算包装数量
			return DoubleUtils.formatByPrecision(quantityBU / packageUnit.getConvertFigure(), packageUnit.getItem().getPrecision());
		}
	}
	
	public static Double convertPackQuantity(Double quantityBU, Integer convertFigure, Integer precision) {
		if (precision == 0) {
			return Math.ceil(quantityBU / convertFigure);
		} else {
			return DoubleUtils.formatByPrecision(quantityBU / convertFigure, precision);
		}
	}
	
	
	public static Double convertBUQuantity(Double quantity, WmsPackageUnit packageUnit) {
		return DoubleUtils.formatByPrecision(quantity * packageUnit.getConvertFigure(), 
					packageUnit.getPrecision());
	}
}