package com.vtradex.wms.client.inventoryviewUI.page;


import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.widgets.layout.FitLayout;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.wms.client.inventoryviewUI.companent.DraggableUiPieChartWrapper;
import com.vtradex.wms.client.inventoryviewUI.companent.UiPieChart;
import com.vtradex.wms.client.inventoryviewUI.constant.CT_IV;
import com.vtradex.wms.client.inventoryviewUI.data.Page_IV_DataAccessor;
import com.vtradex.wms.client.ui.javabean.JB_Zone_IV;


public class Sub_Zone_Image_Panel extends Sub_Abstract_Panel {

	protected static final int DEFAULT_WIDTH = Sub_IV_Panel.DEFAULT_WIDTH;
	protected static final int DEFAULT_HEIGHT = Sub_IV_Panel.DEFAULT_HEIGHT;
	protected static final String DEFAULT_NAME = "zoneImagePanel";
	
	protected List<UiPieChart> pieCharts;
	
	public Sub_Zone_Image_Panel(IMessagePage page) {
		super(page, DEFAULT_NAME);
		this.init_Sub_Image_Panel();
	}
	
	protected void init_Sub_Image_Panel() {
		this.setSize(DEFAULT_WIDTH , DEFAULT_HEIGHT);
		this.setBodyBorder(false);
		this.setBorder(false);
		this.setBodyStyle(CT_IV.DEFAULT_BODYSTYLE);
		VerticalPanel panel = new VerticalPanel();
		panel.setSize("100%","100%");
		Image z_image=new Image(curIVDataAccessor().current_JB_IV().getImage_url());
		z_image.setPixelSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		z_image.setPixelSize(800, 470);
		panel.add(z_image);
		this.add(panel);
		this.setLayout(new FitLayout());
		this.curDataAccessor().initZoneIvDatas();
	}
	
	public void doDispath(String message){
		super.doDispath(message);
		if(Page_IV_DataAccessor.MSG_INIT_ZONE_DATAS.equals(message)){
			this.initPieChart();
		}
	}
	
	protected void initPieChart() {
		if(this.curDataAccessor() !=null){
		for(JB_Zone_IV ziv : this.curDataAccessor().all_Zone_IV()){
			
			UiPieChart uip = new DraggableUiPieChartWrapper(ziv,this);
			//this.register(new UiPieChart(ziv , this));
			this.register(uip);
			//add
			
		}
		}
	}
	
	public Page_IV_DataAccessor curIVDataAccessor() {
		return (Page_IV_DataAccessor)super.getData();
	}
	
	protected void register(UiPieChart chart) {
		if(pieCharts == null) {
			pieCharts =  new ArrayList<UiPieChart>();
		}
		chart.show();
		pieCharts.add(chart);
	}
	
	public void hideCharts() {
		if(pieCharts == null) return;
		for(UiPieChart pc : pieCharts) {
			pc.hide();
		}
	}
	
	public void showCharts() {
		if(pieCharts == null) return;
		for(UiPieChart pc : pieCharts) {
			pc.show();
		}
	}
}
