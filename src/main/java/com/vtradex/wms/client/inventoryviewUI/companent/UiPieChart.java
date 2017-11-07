package com.vtradex.wms.client.inventoryviewUI.companent;


import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.HTMLPanel;
import com.vtradex.wms.client.inventoryviewUI.constant.CT_IV;
import com.vtradex.wms.client.inventoryviewUI.data.Page_IV_DataAccessor;
import com.vtradex.wms.client.inventoryviewUI.page.Sub_Zone_Image_Panel;
import com.vtradex.wms.client.ui.javabean.JB_Zone_IV;

//package com.vtradex.swms.client.ui.companent;
//
//import com.google.gwt.i18n.client.NumberFormat;
//import com.google.gwt.user.client.ui.ClickListener;
//import com.google.gwt.user.client.ui.HasHorizontalAlignment;
//import com.google.gwt.user.client.ui.HasVerticalAlignment;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.PopupPanel;
//import com.google.gwt.user.client.ui.Widget;
//import com.gwtext.client.data.ArrayReader;
//import com.gwtext.client.data.FieldDef;
//import com.gwtext.client.data.FloatFieldDef;
//import com.gwtext.client.data.MemoryProxy;
//import com.gwtext.client.data.RecordDef;
//import com.gwtext.client.data.Store;
//import com.gwtext.client.data.StringFieldDef;
//import com.gwtext.client.widgets.chart.yui.PieChart;
//import com.vtradex.swms.client.ui.data.Page_IV_DataAccessor;
//import com.vtradex.swms.client.ui.javabean.JB_Zone_IV;
//import com.vtradex.swms.client.ui.page.Sub_Z_Image_Panel;
//
//public class UiPieChart extends PopupPanel {
//	
//	protected static final int DEFAULT_WIDTH = 50;
//	protected static final int DEFAULT_HEIGHT = 50;
//	
//	protected static final int DEFAULT_OFF_X = 60;
////	protected static final int DEFAULT_OFF_Y = 60;
//	
//	public static final String DEFAULT_X_FIELD = "location_rate";
//	public static final String DEFAULT_Y_FIELD = "location_name";
//	
//	protected PieChart chart;
//
//	public UiPieChart(final JB_Zone_IV ziv , final Sub_Z_Image_Panel zPanel) {
//		this.initPieChart(ziv);
//		UiClickedPanel mainPanel = new UiClickedPanel();
//		mainPanel.setSize(DEFAULT_WIDTH + "" , DEFAULT_HEIGHT + "");
//		
//		Label label = new Label(NumberFormat.getPercentFormat().format(ziv.getLoc_rate()));
//		mainPanel.add(label);
//		mainPanel.setCellHorizontalAlignment(label,HasHorizontalAlignment.ALIGN_CENTER);
//		mainPanel.setCellVerticalAlignment(label , HasVerticalAlignment.ALIGN_TOP);
//		
//		mainPanel.add(chart);
//		mainPanel.setCellVerticalAlignment(chart, HasVerticalAlignment.ALIGN_BOTTOM);
//		mainPanel.setCellHorizontalAlignment(chart,HasHorizontalAlignment.ALIGN_CENTER);
//		mainPanel.addClickListener(new ClickListener(){
//
//			public void onClick(Widget sender) {
//				zPanel.curIVDataAccessor().setCur_JB_Zone_IV(ziv);
//				zPanel.curIVDataAccessor().sendMessage(Page_IV_DataAccessor.MSG_OPEN_L_IMAGE_PANEL);
//			}
//			
//		});
//		this.add(mainPanel);
//		this.setPopupPosition(ziv.getLoc_xPos() + DEFAULT_OFF_X , ziv.getLoc_yPos() + DEFAULT_OFF_Y);
//	}
//
//	protected void initPieChart(JB_Zone_IV ziv) {
//		MemoryProxy proxy = new MemoryProxy(ziv.to2Array());
//		RecordDef recordDef = new RecordDef(new FieldDef[] {
//				new StringFieldDef(DEFAULT_Y_FIELD), new FloatFieldDef(DEFAULT_X_FIELD)}
//		);
//		ArrayReader reader = new ArrayReader(recordDef);
//		Store store = new Store(proxy, reader);
//		store.load();
//		chart = new PieChart();
//		chart.setBodyBorder(false);
//		chart.setBorder(false);
//		chart.setWMode("transparent");
//		chart.setStore(store);
//		chart.setDataField(DEFAULT_X_FIELD);
//		chart.setCategoryField(DEFAULT_Y_FIELD);
//		chart.setExpressInstall("js/yui/assets/expressinstall.swf");
//		chart.setWidth(DEFAULT_WIDTH);
//		chart.setHeight(DEFAULT_HEIGHT);
//		chart.setAutoShow(true);
//	}
//}

public class UiPieChart extends UiColumnChartParent {
	
	public UiPieChart(final JB_Zone_IV ziv,final Sub_Zone_Image_Panel zPanel ) {
		final UiClickedPanel mainPanel = new UiClickedPanel();
		VerticalPanel panel = new VerticalPanel();
		panel.setSize(DEFAULT_WIDTH + "" , DEFAULT_HEIGHT + "");
		panel.setStyleName(DEFAULT_VERTICAL_PANEL);
		if(ziv.getLoc_rate() > 0) {
			double per_height = ziv.getLoc_rate() * DEFAULT_HEIGHT;
			per_height = per_height < 1.0D ? 1.0D : per_height;
			HTMLPanel yChart = new HTMLPanel(
					"<div style=\"background:"+ CT_IV.DEFAULT_CHART_COLOR + ";width:" + DEFAULT_WIDTH + "px;height:" + per_height + "px;\">&nbsp;&nbsp;&nbsp;&nbsp;</div>");
			yChart.setWidth(DEFAULT_WIDTH);
			yChart.setFrame(false);
			yChart.setHeight(Integer.valueOf(NumberFormat.getFormat(CT_IV.DEFAULT_INTEGER_FORMAT).format(per_height)));
			panel.add(yChart);
			panel.setBorderWidth(2);
			panel.setCellVerticalAlignment(yChart , HasVerticalAlignment.ALIGN_BOTTOM);
			panel.setCellHeight(yChart,"100%");
		}else {
			Label l = new Label();
			l.setWidth(DEFAULT_WIDTH + "px");
			panel.add(l);
			panel.setBorderWidth(2);
		}
		
		Label label = new Label(NumberFormat.getFormat(CT_IV.DEFAULT_PERCENT_FORMAT).format(ziv.getLoc_rate()));
		mainPanel.add(label);
		mainPanel.setCellHorizontalAlignment(label,HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setCellVerticalAlignment(label , HasVerticalAlignment.ALIGN_TOP);
		
		mainPanel.add(panel);
		mainPanel.setCellVerticalAlignment(panel, HasVerticalAlignment.ALIGN_BOTTOM);
		
		mainPanel.addClickListener(new ClickListener(){

			public void onClick(Widget sender) {
				zPanel.curIVDataAccessor().setCur_JB_Zone_IV(ziv);
				zPanel.curIVDataAccessor().sendMessage(Page_IV_DataAccessor.MSG_OPEN_REGION_IMAGE_PANEL);
			}
			
		});
		this.add(mainPanel);
		this.setPopupPosition(ziv.toXY()[0] + CT_IV.DEFAULT_OFF_WIDTH , ziv.toXY()[1] + CT_IV.DEFAULT_OFF_HEIGHT);
		this.applyToolTips(ziv);
	}
	
}
