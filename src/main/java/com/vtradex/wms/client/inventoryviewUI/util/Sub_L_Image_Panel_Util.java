package com.vtradex.wms.client.inventoryviewUI.util;


import java.util.List;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.Renderer;
import com.vtradex.thorn.client.utils.LocaleUtils;
import com.vtradex.wms.client.inventoryviewUI.constant.CT_IV;
import com.vtradex.wms.client.inventoryviewUI.page.Sub_IV_Panel;
import com.vtradex.wms.client.ui.javabean.JB_Location_IV;
import com.vtradex.wms.client.ui.javabean.JB_Location_IV_List;
import com.vtradex.wms.client.ui.javabean.JB_Location_Inventory;
import com.vtradex.wms.client.ui.javabean.JB_Location_RC_IV;
import com.vtradex.wms.client.ui.javabean.JB_Location_RC_IV_List;


public class Sub_L_Image_Panel_Util {
	
	public static final int DEFAULT_WIDTH = Window.getClientWidth() - 100 ;
	public static final int DEFAULT_HEIGHT = Window.getClientHeight() - 100;
	public static final String DEFAULT_NAME = "l_image_panel";
	
	public static final int DEFAULT_PROGRESS_WIDTH = 153;
	public static final int DEFAULT_PROGRESS_HEIGHT = 40;
	
	public static final int DEFAULT_INVENTORY_COLUMN_WIDTH = 100;
	public static final int DEFAULT_INVENTORY_ROW_HEIGHT = 100;
	
	public static ColumnModel AL_MODEL  , RC_MODEL , INV_MODEL;
	public static String[] RC_COLUMN_DEFINES , AL_COLUMN_DEFINES ;
	
	public static final int DEFAULT_COLUMN_HEAD_WIDTH = 62;
	public static final int DEFAULT_COLUMN_LOCATION_WIDTH = 100;
	
	private static void initALColumnDefine(){
		AL_COLUMN_DEFINES = new String[2];
		AL_COLUMN_DEFINES[0] = "line";
		AL_COLUMN_DEFINES[1] = "aisle";
	}
	
	private static void initRCColumnDefine(){
		RC_COLUMN_DEFINES = new String[CT_IV.DEFAULT_COL + 1];
		RC_COLUMN_DEFINES[0] = "row";
		for(int i = 1 ; i <= CT_IV.DEFAULT_COL ; i++) {
			RC_COLUMN_DEFINES[i] = "col_" + i;
		}
	}
	
	public static void initRCColumnConfig(){
		if(RC_COLUMN_DEFINES == null) initRCColumnDefine();
		ColumnConfig[] rc_configs = new ColumnConfig[CT_IV.DEFAULT_COL + 1] ;
		rc_configs[0] = new ColumnConfig("层\\列" , RC_COLUMN_DEFINES[0],DEFAULT_COLUMN_HEAD_WIDTH);
		rc_configs[0].setResizable(false);
		for(int i = 1 ; i <= CT_IV.DEFAULT_COL ; i++) {
			rc_configs[i] = new ColumnConfig(LocaleUtils.getText(RC_COLUMN_DEFINES[i]) , RC_COLUMN_DEFINES[i] , DEFAULT_COLUMN_LOCATION_WIDTH);
			rc_configs[i].setRenderer(new Renderer(){
				public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
					return generateProgressLocationHTML(value,cellMetadata);
				}
			});
			rc_configs[i].setAlign(TextAlign.CENTER);
			rc_configs[i].setResizable(false);
		}
		RC_MODEL = new ColumnModel(rc_configs);
	}
	
	public static void initALColumnConfig(){
		initALColumnDefine();
		ColumnConfig[] al_configs= new ColumnConfig[CT_IV.DEFAULT_AISLE + 1] ;
		al_configs[0] = new ColumnConfig("排\\占用率" , AL_COLUMN_DEFINES[0],DEFAULT_COLUMN_HEAD_WIDTH);
		al_configs[0].setResizable(false);
		for(int i = 1 ; i <= CT_IV.DEFAULT_AISLE ; i++) {
			al_configs[i] = new ColumnConfig(LocaleUtils.getText(AL_COLUMN_DEFINES[i]) , AL_COLUMN_DEFINES[i] , DEFAULT_PROGRESS_WIDTH);
			al_configs[i].setRenderer(new Renderer(){
				public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
					return generateProgressHTML(value,cellMetadata);
				}
			});
			al_configs[i].setAlign(TextAlign.LEFT);
			al_configs[i].setResizable(false);
		}
		AL_MODEL = new ColumnModel(al_configs);
	}
	
	private static void initInvColumnConfig(){
		String[] properties = JB_Location_Inventory.propertyToArray();
		ColumnConfig[] configs= new ColumnConfig[properties.length] ;
		for(int i = 0 ; i < properties.length ; i++) {
			configs[i] = new ColumnConfig(JB_Location_Inventory.localizedToArray()[i] , properties[i] , Sub_IV_Panel.DEFAULT_WIDTH/properties.length);
			configs[i].setRenderer(new Renderer(){
				public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
					cellMetadata.setHtmlAttribute("style='height:18px;border-left:1px solid #d0d0d0;'");
					return value.toString();
				}
			});
		}
		INV_MODEL = new ColumnModel(configs);
	}
	
	public static ColumnModel getInvColumnModel(){
		 if(INV_MODEL == null){
			 initInvColumnConfig();
		 }
		 return INV_MODEL;
	}
	
	private static String generateProgressHTML(Object value ,  CellMetadata cellMetadata) {
		int realWidth = Ext.isIE()?DEFAULT_PROGRESS_WIDTH: DEFAULT_PROGRESS_WIDTH - 6;
		int realHeght = Ext.isIE()?DEFAULT_PROGRESS_HEIGHT : DEFAULT_PROGRESS_HEIGHT - 6;
		if(value == null || "".equals(value)){
			return "";
		}
		Double realValue = Double.valueOf(value.toString());
		//不存在库位,背景为灰色
		if(realValue < 0) {
			cellMetadata.setHtmlAttribute("style='height:" + realHeght + "px;width:" + realWidth + "px;background:#d0d0d0;BORDER-LEFT: #d0d0d0 1px solid;'");
			return "";
		}
		//存在库位，但是容积率为0,背景为灰色
		if(realValue == 0) {
			cellMetadata.setHtmlAttribute("style='BORDER-LEFT: #d0d0d0 1px solid;" + " height:" + realHeght + "px;width:" + realWidth + "px'");
			return "";
		}
		
		String color = "";
		if(realValue > 0D && realValue < 0.33D) {
			color = "#00923f";
		} else if(realValue >= 0.33D && realValue < 0.66D) {
			color = "#fff500";
		} else if(realValue >= 0.66D && realValue <= 1D) {
			color = "#da251e";
		}
		
		realValue = realValue >= 1? realWidth : realWidth*realValue;
		
		cellMetadata.setHtmlAttribute(
					"align='left' style='BORDER-RIGHT: #000000 1px solid; BORDER-TOP: #d0d0d0 1px solid; " +
					"BORDER-LEFT: #d0d0d0 1px solid; COLOR: #d0d0d0; BORDER-BOTTOM: #000000 1px solid; " +
					"BACKGROUND-COLOR: "+color+";" +
					"height:" + realHeght + "px;width:" + 
					NumberFormat.getFormat(CT_IV.DEFAULT_INTEGER_FORMAT).format(realValue) + "px'");
		return "";
	}
	
	
	private static String generateProgressLocationHTML(Object value ,  CellMetadata cellMetadata) {
		int realWidth = Ext.isIE()?DEFAULT_INVENTORY_COLUMN_WIDTH: DEFAULT_INVENTORY_COLUMN_WIDTH - 6;
		int realHeight = Ext.isIE()?DEFAULT_INVENTORY_ROW_HEIGHT: DEFAULT_INVENTORY_ROW_HEIGHT - 6;
		if(value == null || "".equals(value)){
			return "";
		}
		Double realValue = Double.valueOf(value.toString());
		//不存在库位,背景为灰色
		if(realValue < 0) {
			cellMetadata.setHtmlAttribute("style='BORDER-LEFT: #d0ecff 1px solid; height:" + realHeight + "px;width:" + realWidth + "px;background:#d0d0d0;BORDER-LEFT: #d0d0d0 1px solid;'");
			return "";
		}
		//存在库位，但是容积率为0,背景为白色
		if(realValue == 0) {
			cellMetadata.setHtmlAttribute("style='BORDER-LEFT: #d0d0d0 1px solid;" + " height:" + realHeight + "px;width:" + realWidth + "px'");
			return "";
		}

		String backgroundImg = "";
		if(realValue > 0D && realValue <= 0.2D) {
			backgroundImg = "images/inventoryview/1.jpg";
		} else if(realValue > 0.2D && realValue <= 0.3D) {
			backgroundImg = "images/inventoryview/2.jpg";
		}else if(realValue > 0.3D && realValue <= 0.4D) {
			backgroundImg = "images/inventoryview/3.jpg";
		}else if(realValue > 0.4D && realValue <= 0.5D) {
			backgroundImg = "images/inventoryview/4.jpg";
		}else if(realValue > 0.5D && realValue <= 0.6D) {
			backgroundImg = "images/inventoryview/5.jpg";
		}else if(realValue > 0.6D && realValue <= 0.7D) {
			backgroundImg = "images/inventoryview/6.jpg";
		}else if(realValue > 0.7D && realValue <= 0.8D) {
			backgroundImg = "images/inventoryview/7.jpg";
		}else if(realValue > 0.8D && realValue <= 0.9D) {
			backgroundImg = "images/inventoryview/8.jpg";
		}else if(realValue > 0.9D) {
			backgroundImg = "images/inventoryview/9.jpg";
		}
		
		cellMetadata.setHtmlAttribute("style='BORDER-LEFT: #d0d0d0 1px solid; height:" + realHeight + "px;width:"
				+realWidth + "px; cursor:pointer; *cursor:hand; background-color:#FFFFFF; background-image: url( "+backgroundImg+");'");
		return "";
	}
	
	
	public static ColumnModel getRCColumnModel(){
		if(RC_MODEL == null) {
			initRCColumnConfig();
		}
		return RC_MODEL;
	}
	
	public static ColumnModel getALColumnModel(){
		if(AL_MODEL == null) {
			initALColumnConfig();
		}
		return AL_MODEL;
	}
	
	public static Store getALStore(Object[][] objss){
		SimpleStore store = new SimpleStore(AL_COLUMN_DEFINES,objss);
		store.load();
		return store;
	}
	
	public static Store getRCStore(Object[][] objss) {
		SimpleStore store = new SimpleStore(RC_COLUMN_DEFINES,objss);
		store.load();
		return store;
	}
	
	public static Store getInvStore(Object[][] objss) {
		SimpleStore store = new SimpleStore(JB_Location_Inventory.propertyToArray(),objss);
		store.load();
		return store;
	}
	
	public static Object[][] convertToAL2Array(List<JB_Location_IV_List> livs) {
		Object[][] objss = new Object[CT_IV.DEFAULT_LINE][];
		for(int line = 0; line < CT_IV.DEFAULT_LINE ; line++) {
			objss[line] = livs.get(line).toArray();
		}
		return objss;
	}
	
	public static Object[][] convertToRC2Array(List<JB_Location_RC_IV_List> lrivs) {
		Object[][] objss = new Object[CT_IV.DEFAULT_ROW][];
		for(int row = 0; row < CT_IV.DEFAULT_ROW ; row++) {
			objss[row] = lrivs.get(row).toArray();
		}
		return objss;
	}
	
	public static Object[][] convertToInv2Array(List<JB_Location_Inventory> lis) {
		Object[][] objss = new Object[lis.size()][];
		for(int row = 0; row < lis.size() ; row++) {
			objss[row] = lis.get(row).toArray();
		}
		return objss;
	}
	
	public static String generateToolTip(JB_Location_IV iv) {		
		String disValue = "<TABLE WIDTH='100%' STYLE='border:1px dotted #a3bae9'>" +
		  	"<TR>" +
		  	"<TD ALGIN='LEFT' WIDTH='50%' STYLE='border:1px dotted #a3bae9'>库存容积：</TD>" +
		  	"<TD ALGIN='LEFT' STYLE='border:1px dotted #a3bae9;white-space:nowrap;'><font color='blue' style='font-weight:bold;'>"+ NumberFormat.getFormat(CT_IV.DEFAULT_PERCENT_FORMAT).format(iv.getLoc_rate())+"</font></TD>" +
		  	"</TR>" +
			"<TR>" +
		  	"<TD ALGIN='LEFT' STYLE='border:1px dotted #a3bae9'>库位区：</TD>" +
		  	"<TD ALGIN='LEFT' STYLE='border:1px dotted #a3bae9;white-space:nowrap;'><font color='blue' style='font-weight:bold;'>"+ iv.getLoc_aisle() +"</font></TD>" +
		  	"</TR>" +
		  	"<TR>" +
		  	"<TD ALGIN='LEFT' STYLE='border:1px dotted #a3bae9'>库位排：</TD>" +
		  	"<TD ALGIN='LEFT' STYLE='border:1px dotted #a3bae9;white-space:nowrap;'><font color='blue' style='font-weight:bold;'>"+ iv.getLoc_line() +"</font></TD>" +
		  	"</TR>" +
		  	"</TABLE>";
		return disValue;
	}
	
	public static String generateToolTip(JB_Location_RC_IV lriv) {
		String[] realValues = lriv.toTipArray();
		String disValue = "<TABLE WIDTH='100%' STYLE='border:1px dotted #a3bae9'>" +
		  	"<TR>" +
		  	"<TD ALGIN='LEFT' WIDTH='40%' STYLE='border:1px dotted #a3bae9'>库存容积：</TD>" +
		  	"<TD ALGIN='LEFT' STYLE='border:1px dotted #a3bae9;white-space:nowrap;'><font color='blue' style='font-weight:bold;'>"+ NumberFormat.getFormat(CT_IV.DEFAULT_PERCENT_FORMAT).format(lriv.getRate())+"</font></TD>" +
		  	"</TR>" +
			"<TR>" +
		  	"<TD ALGIN='LEFT' STYLE='border:1px dotted #a3bae9'>库位编码：</TD>" +
		  	"<TD ALGIN='LEFT' STYLE='border:1px dotted #a3bae9;white-space:nowrap;'><font color='blue' style='font-weight:bold;'>"+ realValues[0] +"</font></TD>" +
		  	"</TR>" +
		  	"</TABLE>";
		return disValue;
	}
}
