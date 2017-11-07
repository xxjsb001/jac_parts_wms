package com.vtradex.wms.client.inventoryviewUI.page;


import java.util.ArrayList;
import java.util.List;


import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.widgets.layout.FitLayout;
import com.vtradex.thorn.client.ApplicationWindow;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.wms.client.inventoryviewUI.companent.DraggableUiColumnChartWrapper;
import com.vtradex.wms.client.inventoryviewUI.companent.UiColumnChart;
import com.vtradex.wms.client.inventoryviewUI.constant.CT_IV;
import com.vtradex.wms.client.inventoryviewUI.data.Page_IV_DataAccessor;
import com.vtradex.wms.client.ui.javabean.JB_IV;


public class Sub_Warehouse_Image_Panel extends Sub_Abstract_Panel {
	
	/** 背景主面板的宽度和高度 */
	protected static final String DEFAULT_WIDTH = "100%";
	protected static final String DEFAULT_HEIGHT = "100%";
	
	public static final String DEFAULT_NAME = "sub_image_panel";
	protected static final String DEFAULT_WAREHOUSE_BGIMAGE = "w_bg_image_url";
	
	protected List<UiColumnChart> uccs;
	
	public Sub_Warehouse_Image_Panel(IMessagePage page) {
		super(page, DEFAULT_NAME);
	    this.init_Sub_Image_Panel();	    
	    
	}
	
	protected void init_Sub_Image_Panel() {
		this.setSize(Sub_IV_Panel.DEFAULT_WIDTH , Sub_IV_Panel.DEFAULT_HEIGHT);
		this.setLayout(new FitLayout());
		this.setBodyStyle(CT_IV.DEFAULT_BODYSTYLE);
		VerticalPanel panel = new VerticalPanel();
		panel.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		Object back = ApplicationWindow.context.getGlobalParams().get(DEFAULT_WAREHOUSE_BGIMAGE);
		Image w_image=new Image(back == null?"" : back.toString());
		w_image.setPixelSize(800, 470);
		panel.add(w_image);
		this.add(panel);
		this.curIVDataAccessor().initIVDatas();
		
	}
	
	
	public void doDispath(String message){
		super.doDispath(message);
		if(Page_IV_DataAccessor.MSG_INIT_IV_DATA.equals(message)) {
			this.init_WH_IV_ColumnChart();
		}
	}
	
	public Page_IV_DataAccessor curIVDataAccessor() {
		return (Page_IV_DataAccessor)super.getData();
	}
	  
	/** 绘制仓库布局中的满库率（用柱图显示） */
	protected void init_WH_IV_ColumnChart() {
		if (this.curIVDataAccessor() != null) {
			for(final JB_IV iv : this.curIVDataAccessor().all_IV()) {
				UiColumnChart wd = new DraggableUiColumnChartWrapper(iv,this);  
				//this.regeditSubWidgetAndShow(new UiColumnChart(iv,this));
				//add	
				this.regeditSubWidgetAndShow(wd);
			}
		}
	}
	
	protected void regeditSubWidgetAndShow(UiColumnChart ucc) {
		if(uccs == null) {
			uccs = new ArrayList<UiColumnChart>();
		}
		uccs.add(ucc);
		ucc.show();
		
	}
	
	public void hideCharts() {
		if(uccs == null) return;
		for(UiColumnChart ucc : uccs) {
			ucc.hide();
		}
	}
	
	public void showCharts() {
		if(uccs == null) return;
		for(UiColumnChart ucc : uccs) {
			ucc.show();
		}
	}
	
	
}
