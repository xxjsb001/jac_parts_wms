package com.vtradex.wms.client.inventoryviewUI.page;


import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.thorn.client.ui.panel.AbstractSupportPanel;
import com.vtradex.wms.client.inventoryviewUI.constant.CT_IV;
import com.vtradex.wms.client.inventoryviewUI.data.Page_IV_DataAccessor;

@SuppressWarnings("unchecked")
public class Sub_Navigate_Panel extends AbstractSupportPanel{

	protected final static String DEFAULT_NAME = "navigate_panel";
	
	/** 仓库链接 */
	protected HTML warehouse_HyperLink = new HTML("<b style='color:#19488c; font-family:Arial, Helvetica, sans-serif;'>供应链网络</b>");
	/** 库区链接 */
	protected HTML zone_HyperLink = new HTML("");
	/** 区链接 */
	protected HTML region_HyperLink = new HTML("");
	/** 库位链接 */
	protected HTML location_HyperLink = new HTML("");
	
	
	
	protected HTML firstImg = new HTML("");
	protected HTML secondImg = new HTML("");
	protected HTML thirdImg = new HTML("");
	
	protected Panel subPanel;
	
	protected Sub_Abstract_Panel currentPanel;
	
	public Sub_Abstract_Panel getCurrentPanel() {
		return currentPanel;
	}

	public void setCurrentPanel(Sub_Abstract_Panel currentPanel) {
		this.currentPanel = currentPanel;
	}

	public Sub_Navigate_Panel(IMessagePage page) {
		super(page, DEFAULT_NAME);
		this.initListener();
		this.drawWidget();
	}
	
	protected void drawWidget(){

		subPanel = new Panel();
		subPanel.setLayout(new HorizontalLayout(5));
		subPanel.add(warehouse_HyperLink);
		subPanel.add(firstImg);
		subPanel.add(zone_HyperLink);
		subPanel.add(secondImg);
		subPanel.add(region_HyperLink);
		subPanel.add(thirdImg);
		subPanel.add(location_HyperLink);
		subPanel.setBodyStyle("background-color:#e9f4f6;");
		subPanel.setWidth("100%");
		
		this.setBodyBorder(true);
		this.setBorder(true);
		this.setLayout(new FitLayout());
		this.setHeight("30px");
		this.setBodyStyle("background-color:#e9f4f6;");
		this.setWidth("100%");
		this.add(subPanel);
	}
	
	protected void removeHistoryPanel(){
		if(this.currentPanel == null) return;
		this.currentPanel.hideCharts();
		page.remove(this.currentPanel);
		
	}
	
	protected void addCenterPanel(Sub_Abstract_Panel newPanel){
		this.currentPanel = newPanel;
		curIvPanel().addCenterPanel(newPanel);
	}
	
	protected void initListener(){
		warehouse_HyperLink.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				getData().sendMessage(Page_IV_DataAccessor.MSG_OPEN_WAREHOUSE_IMAGE_PANEL);
			}
		});
		
		zone_HyperLink.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				getData().sendMessage(Page_IV_DataAccessor.MSG_OPEN_ZONE_IMAGE_PANEL);
			}
		});
		
		region_HyperLink.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				getData().sendMessage(Page_IV_DataAccessor.MSG_OPEN_REGION_IMAGE_PANEL);
			}
		});
		
		location_HyperLink.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				getData().sendMessage(Page_IV_DataAccessor.MSG_OPEN_LOCATION_IMAGE_PANEL);
			}
		});
	}
	
	private Sub_IV_Panel curIvPanel(){
		return (Sub_IV_Panel)this.page;
	}
	
	protected void refreshNavigatePanel(){
		if(((Page_IV_DataAccessor)page.getData()).current_JB_IV() != null){
			firstImg.setHTML(CT_IV.DEFAULT_ARROW_IMG);
			zone_HyperLink.setHTML("<b style='color:#19488c; font-family:Arial, Helvetica, sans-serif;'>"+((Page_IV_DataAccessor)page.getData()).current_JB_IV().getIv_wh_name()+"</b>");
		}
		if(((Page_IV_DataAccessor)page.getData()).getCur_JB_Zone_IV() != null){
			secondImg.setHTML(CT_IV.DEFAULT_ARROW_IMG);
			region_HyperLink.setHTML("<b style='color:#19488c; font-family:Arial, Helvetica, sans-serif;'>"+((Page_IV_DataAccessor)page.getData()).getCur_JB_Zone_IV().getLoc_name()+"</b>");
		}
		if(((Page_IV_DataAccessor)page.getData()).getCur_JB_Region_IV() != null){
			thirdImg.setHTML(CT_IV.DEFAULT_ARROW_IMG);
			location_HyperLink.setHTML("<b style='color:#19488c; font-family:Arial, Helvetica, sans-serif;'>"+((Page_IV_DataAccessor)page.getData()).getCur_JB_Region_IV().getRegion_code()+"区"+"</b>");
		}
		
	}
	
	public void doDispath(String message){
		if(Page_IV_DataAccessor.MSG_REFRESH_NAVIGATE_PANEL.equals(message)) {
			this.refreshNavigatePanel();
		}else if(Page_IV_DataAccessor.MSG_OPEN_WAREHOUSE_IMAGE_PANEL.equals(message)) {
			this.removeHistoryPanel();
			this.addCenterPanel(new Sub_Warehouse_Image_Panel(page));
			DOM.setStyleAttribute(this.warehouse_HyperLink.getElement() , "border" , "1px solid blue");
			DOM.setStyleAttribute(this.zone_HyperLink.getElement() , "border" , "none");
			DOM.setStyleAttribute(this.region_HyperLink.getElement() , "border" , "none");
			DOM.setStyleAttribute(this.location_HyperLink.getElement() , "border" , "none");
		}else if(Page_IV_DataAccessor.MSG_OPEN_ZONE_IMAGE_PANEL.equals(message)) {
			this.removeHistoryPanel();
			this.addCenterPanel(new Sub_Zone_Image_Panel(page));
			this.refreshNavigatePanel();
			DOM.setStyleAttribute(this.warehouse_HyperLink.getElement() , "border" , "none");
			DOM.setStyleAttribute(this.zone_HyperLink.getElement() , "border" , "1px solid blue");
			DOM.setStyleAttribute(this.region_HyperLink.getElement() , "border" , "none");
			DOM.setStyleAttribute(this.location_HyperLink.getElement() , "border" , "none");
		}else if(Page_IV_DataAccessor.MSG_OPEN_REGION_IMAGE_PANEL.equals(message)) {
			this.removeHistoryPanel();
			this.addCenterPanel(new Sub_Region_Image_Panel(page));
			this.refreshNavigatePanel();
			DOM.setStyleAttribute(this.warehouse_HyperLink.getElement() , "border" , "none");
			DOM.setStyleAttribute(this.zone_HyperLink.getElement() , "border" , "none");
			DOM.setStyleAttribute(this.region_HyperLink.getElement() , "border" , "1px solid blue");
			DOM.setStyleAttribute(this.location_HyperLink.getElement() , "border" , "none");
		}else if(Page_IV_DataAccessor.MSG_OPEN_LOCATION_IMAGE_PANEL.equals(message)) {
			this.removeHistoryPanel();
			this.addCenterPanel(new Sub_Location_Image_Panel(page));
			this.refreshNavigatePanel();
			DOM.setStyleAttribute(this.warehouse_HyperLink.getElement() , "border" , "none");
			DOM.setStyleAttribute(this.zone_HyperLink.getElement() , "border" , "none");
			DOM.setStyleAttribute(this.region_HyperLink.getElement() , "border" , "none");
			DOM.setStyleAttribute(this.location_HyperLink.getElement() , "border" , "1px solid blue");
		}
	}
}
