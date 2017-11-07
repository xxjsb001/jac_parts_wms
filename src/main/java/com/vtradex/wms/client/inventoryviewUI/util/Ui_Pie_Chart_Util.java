package com.vtradex.wms.client.inventoryviewUI.util;


import java.util.List;

import com.gwtext.client.util.Format;
import com.vtradex.wms.client.ui.javabean.JB_IV;
import com.vtradex.wms.client.ui.javabean.JB_Location_Inventory;
import com.vtradex.wms.client.ui.javabean.JB_Region_IV;
import com.vtradex.wms.client.ui.javabean.JB_Zone_IV;


public class Ui_Pie_Chart_Util {

	public static String generateChartToolTip(JB_IV iv) {
		return Format.format("<TABLE WIDTH='100%' STYLE='border:1px dotted #a3bae9'>" +
				"<TR>" +
				"<TD ALGIN='RIGHT' WIDTH='30%' STYLE='border:1px dotted #a3bae9'>仓库名称：</TD>" +
				"<TD ALGIN='LEFT'  STYLE='border:1px dotted #a3bae9;white-space:nowrap;'><font color='blue' style='font-weight:bold;'>{0}</font></TD>" +
				"</TR>" +
				"<TR>" +
				"<TD ALGIN='RIGHT' STYLE='border:1px dotted #a3bae9'>X坐标：</TD>" +
				"<TD ALGIN='LEFT'  STYLE='border:1px dotted #a3bae9;white-space:nowrap;'><font color='blue' style='font-weight:bold;'>{1}</font></TD>" +
				"</TR>" +
				"<TR>" +
				"<TD ALGIN='RIGHT' STYLE='border:1px dotted #a3bae9'>Y坐标：</TD>" +
				"<TD ALGIN='LEFT'  STYLE='border:1px dotted #a3bae9;white-space:nowrap;'><font color='blue' style='font-weight:bold;'>{2}</font></TD>" +
				"</TR></TABLE>" , iv.toStrArray());
	}
	
	public static String generatePieToolTip(JB_Zone_IV ziv) {
		return Format.format("<TABLE WIDTH='100%' STYLE='border:1px dotted #a3bae9'>" +
				"<TR>" +
				"<TD ALIGN='LEFT' WIDTH='30%' STYLE='border:1px dotted #a3bae9'>库区编码：</TD>" +
				"<TD ALIGN='LEFT' STYLE='border:1px dotted #a3bae9;white-space:nowrap;'><font color='blue' style='font-weight:bold;'>{0}</font></TD>" +
				"</TR>" +
				"<TR>" +
				"<TD ALIGN='LEFT' STYLE='border:1px dotted #a3bae9'>库区名称：</TD>" +
				"<TD ALIGN='LEFT' STYLE='border:1px dotted #a3bae9;white-space:nowrap;'><font color='blue' style='font-weight:bold;'>{1}</font></TD>" +
				"</TR>" +
				"<TR>" +
				"<TD ALIGN='LEFT' STYLE='border:1px dotted #a3bae9'>X坐标：</TD>" +
				"<TD ALIGN='LEFT' STYLE='border:1px dotted #a3bae9;white-space:nowrap;'><font color='blue' style='font-weight:bold;'>{2}</font></TD>" +
				"</TR>" +
				"<TR>" +
				"<TD ALIGN='LEFT' STYLE='border:1px dotted #a3bae9'>Y坐标：</TD>" +
				"<TD ALIGN='LEFT' STYLE='border:1px dotted #a3bae9;white-space:nowrap;'><font color='blue' style='font-weight:bold;'>{3}</font></TD>" +
				"</TR></TABLE>" , ziv.toStrArray());
	}
	
	public static String generateRegionToolTip(JB_Region_IV riv) {
		return Format.format("<TABLE WIDTH='100%' STYLE='border:1px dotted #a3bae9'>" +
				"<TR>" +
				"<TD ALIGN='LEFT' WIDTH='50%' STYLE='border:1px dotted #a3bae9'>区号：</TD>" +
				"<TD ALIGN='LEFT' STYLE='border:1px dotted #a3bae9;white-space:nowrap;'><font color='blue' style='font-weight:bold;'>{0}</font></TD>" +
				"</TR>" +
				"<TR>" +
				"<TD ALIGN='LEFT' STYLE='border:1px dotted #a3bae9'>容积率：</TD>" +
				"<TD ALIGN='LEFT' STYLE='border:1px dotted #a3bae9;white-space:nowrap;'><font color='blue' style='font-weight:bold;'>{1}</font></TD>" +
				"</TR>" +
				"</TABLE>" , riv.toStrArray());
	}
	
	public static String generateInventoryDetail(List<JB_Location_Inventory> invs) {
		String html = "<TABLE WIDTH='94%' STYLE='border:1px dotted #a3bae9'>";
		html += "<TR>" +
				"<TD WIDTH='15%' ALIGN='CENTER' STYLE='border:1px dotted #a3bae9;'>货主</TD>" +
				"<TD WIDTH='20%' ALIGN='CENTER' STYLE='border:1px dotted #a3bae9;'>货品编码</TD>" +
				"<TD WIDTH='25%' ALIGN='CENTER' STYLE='border:1px dotted #a3bae9;'>货品名称</TD>" +
				"<TD WIDTH='10%' ALIGN='CENTER' STYLE='border:1px dotted #a3bae9;'>包装</TD>" +
				"<TD WIDTH='10%' ALIGN='CENTER' STYLE='border:1px dotted #a3bae9;'>数量</TD>" +
				"<TD WIDTH='10%' ALIGN='CENTER' STYLE='border:1px dotted #a3bae9;'>重量</TD>" +
				"<TD WIDTH='10%' ALIGN='CENTER' STYLE='border:1px dotted #a3bae9;'>体积</TD>" +
				"</TR>";
		for (JB_Location_Inventory inv : invs) {
			html += "<TR>" +
					"<TD ALIGN='CENTER' STYLE='border:1px dotted #a3bae9;'>" + inv.getOrg_name() + "</TD>" +
					"<TD ALIGN='CENTER' STYLE='border:1px dotted #a3bae9;'>" + inv.getItem_code() + "</TD>" +
					"<TD ALIGN='CENTER' STYLE='border:1px dotted #a3bae9;'>" + inv.getItem_name() + "</TD>" +
					"<TD ALIGN='CENTER' STYLE='border:1px dotted #a3bae9;'>" + inv.getUnit() + "</TD>" +
					"<TD ALIGN='CENTER' STYLE='border:1px dotted #a3bae9;'>" + inv.getQuantity() + "</TD>" +
					"<TD ALIGN='CENTER' STYLE='border:1px dotted #a3bae9;'>" + inv.getWeight() + "</TD>" +
					"<TD ALIGN='CENTER' STYLE='border:1px dotted #a3bae9;'>" + inv.getVolume() + "</TD>" +
					"</TR>";
		}
		html += "</TABLE>";
		return html;
	}
	
	public static String generateCustomMessageBox(String title, String message) {
		String html="<div class='msg' style='word-wrap:break-word; text-align:left;'>"+
	            	"<div class='x-box-tl'><div class='x-box-tr'><div class='x-box-tc'></div></div></div>"+
	            	"<div class='x-box-ml'><div class='x-box-mr'><div class='x-box-mc'>" +
	            	"<h4 align='left'>" + title + "</h4>" + message +"</div></div></div>"+
	            	"<div class='x-box-bl'><div class='x-box-br'><div class='x-box-bc'></div></div></div>"+
	            	"</div>";
		return html;
	}
}
