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
import com.vtradex.wms.client.inventoryviewUI.page.Sub_Warehouse_Image_Panel;
import com.vtradex.wms.client.ui.javabean.JB_IV;


public class UiColumnChart extends UiColumnChartParent {
	public UiColumnChart(final JB_IV iv,final Sub_Warehouse_Image_Panel wPanel ) {
		
		final UiClickedPanel mainPanel = new UiClickedPanel();
	
		VerticalPanel panel = new VerticalPanel();
		panel.setSize(DEFAULT_WIDTH + "" , DEFAULT_HEIGHT + "");
		panel.setBorderWidth(2);
		panel.setStyleName(DEFAULT_VERTICAL_PANEL);
		if(iv.getIv_rate() >0) {
			double per_height = iv.getIv_rate() * DEFAULT_HEIGHT;
			per_height = per_height < 1.0D ? 1.0D : per_height;
			HTMLPanel yChart = new HTMLPanel(
					"<div style=\"background:" + CT_IV.DEFAULT_CHART_COLOR + ";width:" + DEFAULT_WIDTH + "px;height:" + per_height + "px;\">&nbsp;&nbsp;&nbsp;&nbsp;</div>");
			yChart.setWidth(DEFAULT_WIDTH);
			yChart.setFrame(false);
			yChart.setHeight(Integer.valueOf(NumberFormat.getFormat(CT_IV.DEFAULT_INTEGER_FORMAT).format(per_height)));
			panel.add(yChart);			
			panel.setCellVerticalAlignment(yChart , HasVerticalAlignment.ALIGN_BOTTOM);
			panel.setCellHeight(yChart,"100%");				
		}else{
			Label l = new Label();
			l.setWidth(DEFAULT_WIDTH + "px");
			panel.add(l);
			panel.setBorderWidth(2);			
		}
		
		Label label = new Label(NumberFormat.getFormat(CT_IV.DEFAULT_PERCENT_FORMAT).format(iv.getIv_rate()));
		mainPanel.add(label);
		mainPanel.setCellHorizontalAlignment(label,HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setCellVerticalAlignment(label , HasVerticalAlignment.ALIGN_TOP);
		mainPanel.add(panel);
		mainPanel.setCellVerticalAlignment(panel, HasVerticalAlignment.ALIGN_BOTTOM);
		mainPanel.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				wPanel.curIVDataAccessor().setCur_JB_IV(iv);
				wPanel.curIVDataAccessor().sendMessage(Page_IV_DataAccessor.MSG_OPEN_ZONE_IMAGE_PANEL);
			}
		});
   	    this.add(mainPanel);
		this.setPopupPosition(iv.toXY()[0] + CT_IV.DEFAULT_OFF_WIDTH , iv.toXY()[1] + CT_IV.DEFAULT_OFF_HEIGHT);
		this.applyToolTips(iv);		
	}
}
