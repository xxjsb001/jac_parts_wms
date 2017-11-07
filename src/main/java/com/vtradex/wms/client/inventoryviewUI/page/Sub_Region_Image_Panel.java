/**
 * 
 */
package com.vtradex.wms.client.inventoryviewUI.page;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.portal.Portal;
import com.gwtext.client.widgets.portal.PortalColumn;
import com.gwtext.client.widgets.portal.Portlet;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.wms.client.inventoryviewUI.companent.UiClickedPanel;
import com.vtradex.wms.client.inventoryviewUI.constant.CT_IV;
import com.vtradex.wms.client.inventoryviewUI.data.Page_IV_DataAccessor;
import com.vtradex.wms.client.inventoryviewUI.util.Ui_Pie_Chart_Util;
import com.vtradex.wms.client.ui.javabean.JB_Region_IV;


@SuppressWarnings("unchecked")
public class Sub_Region_Image_Panel extends Sub_Abstract_Panel{

	protected static final int DEFAULT_WIDTH = Sub_IV_Panel.DEFAULT_WIDTH;
	protected static final int DEFAULT_HEIGHT = Sub_IV_Panel.DEFAULT_HEIGHT;
	protected static final String DEFAULT_NAME = "zone_image_panel";
	
	protected static final int DEFAULT_CELLING_WIDTH = 146;
	protected static final int DEFAULT_CELLING_HEIGHT = 120;
	
	
	private Panel panel = new Panel();
	private VerticalPanel container = new VerticalPanel();
	
	public Sub_Region_Image_Panel(IMessagePage page) {
		super(page, DEFAULT_NAME);
		this.initialize();
		this.curDataAccessor().initRegionIvDatas();
		panel.add(container);
		this.add(panel);
	}
	
	public void doDispath(String message){
		super.doDispath(message);
		if(Page_IV_DataAccessor.MSG_INIT_REGION_DATAS.equals(message)){
			this.initDates();
		}
	}
	
	public Page_IV_DataAccessor curIVDataAccessor() {
		return (Page_IV_DataAccessor)super.getData();
	}
	
	protected void initDates() {
		if(this.curDataAccessor() !=null){
			
			Portal portal = new Portal();  
			portal.setWidth(Sub_IV_Panel.DEFAULT_WIDTH - 10);
			portal.setHeight(Sub_IV_Panel.DEFAULT_HEIGHT);
			portal.setBodyBorder(false);
			List<PortalColumn> cols = new ArrayList<PortalColumn>();  
			
			List<JB_Region_IV> regionIVs = this.curDataAccessor().all_Region_IV();
			for (int i = 0; i < regionIVs.size(); i++) {
				JB_Region_IV riv = regionIVs.get(i);
				Portlet portlet = new Portlet(riv.getRegion_code()+"区  -  "+ NumberFormat.getFormat(CT_IV.DEFAULT_PERCENT_FORMAT).format(riv.getRegion_rate()),"");  				
				portlet.setHeight(DEFAULT_CELLING_HEIGHT);
				portlet.setBodyBorder(true); 
				portlet.setLayout(new FitLayout());
				portlet.setFrame(false);
				portlet.setBodyStyle("background-color:#FFFFFF;");

				if(i < 5) {
					PortalColumn newCol = new PortalColumn();  
					newCol.setPaddings(0, 10, 0, 0);  
					newCol.add(portlet);  
					cols.add(newCol);
					getBodyHtmlContent(portlet, riv, this);
				} else {
					int j = i % 5;	
					cols.get(j).add(portlet);
					getBodyHtmlContent(portlet, riv, this);
				}
			}
			
			for (PortalColumn col : cols) {
				portal.add(col, new ColumnLayoutData(0.15));  
			}
			container.add(portal);
		}
	}

	private void initialize() {
		this.setSize(Sub_IV_Panel.DEFAULT_WIDTH , Sub_IV_Panel.DEFAULT_HEIGHT);
		this.setLayout(new FitLayout());
		this.setBodyStyle(CT_IV.DEFAULT_BODYSTYLE);
		
    	panel.setWidth(Sub_IV_Panel.DEFAULT_WIDTH);
    	panel.setHeight(Sub_IV_Panel.DEFAULT_HEIGHT);
    	panel.setBorder(false);
    	panel.setBodyBorder(false);
    	panel.setPaddings(10);
    	panel.setLayout(new FitLayout());
    	
    	container.setPixelSize(Sub_IV_Panel.DEFAULT_WIDTH, Sub_IV_Panel.DEFAULT_HEIGHT);
	}
	
	private void getBodyHtmlContent(Portlet portlet, final JB_Region_IV riv, final Sub_Region_Image_Panel rPanel) {		
		final UiClickedPanel mainPanel = new UiClickedPanel();
		mainPanel.setSize("100%", "100%");
		mainPanel.setBorderWidth(0);
		VerticalPanel panel = new VerticalPanel();
		panel.setSize("100%", "100%");
		panel.setBorderWidth(0);
		HTMLPanel yChart = null;
		if(riv.getRegion_rate() > 0) {
			double per_height = riv.getRegion_rate() * 100;
			if(per_height > 0D && per_height < 1.0D) {
				per_height = per_height < 1.0D ? 1.0D : per_height;
			}
			
			yChart = new HTMLPanel(
					"<div style=\"font-size:0; background:"+ CT_IV.DEFAULT_CHART_COLOR + ";height:" + per_height + "px;\"></div>");
			yChart.setHeight(Integer.valueOf(NumberFormat.getFormat(CT_IV.DEFAULT_INTEGER_FORMAT).format(per_height))+"px");
			//yChart.setWidth(DEFAULT_CELLING_WIDTH + "px");
			panel.add(yChart);			
			panel.setCellVerticalAlignment(yChart , HasVerticalAlignment.ALIGN_BOTTOM);
			//panel.setSize(DOM.getStyleAttribute(yChart.getElement(), "width"),DEFAULT_CELLING_HEIGHT+"");
		
		}else {
			yChart = new HTMLPanel("");
			yChart.setHeight(DEFAULT_CELLING_HEIGHT + "px");
			//yChart.setWidth(DEFAULT_CELLING_WIDTH + "px");
			panel.add(yChart);
			panel.setBorderWidth(0);
			//panel.setSize(DOM.getStyleAttribute(yChart.getElement(), "width"),DEFAULT_CELLING_HEIGHT+"");
		}

		ToolTip tip = new ToolTip(Ui_Pie_Chart_Util.generateRegionToolTip(riv));
		tip.setSize(150, 40);
		tip.applyTo(panel.getElement());
		
		mainPanel.add(panel);
		mainPanel.setCellVerticalAlignment(panel, HasVerticalAlignment.ALIGN_BOTTOM);		
		mainPanel.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				rPanel.curIVDataAccessor().setCur_JB_Region_IV(riv);
				rPanel.curIVDataAccessor().sendMessage(Page_IV_DataAccessor.MSG_OPEN_LOCATION_IMAGE_PANEL);
			}
		});
		
		portlet.add(mainPanel);
	}
}
