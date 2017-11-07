package com.vtradex.wms.client.inventoryviewUI.page;


import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.thorn.client.ui.panel.AbstractSupportPanel;
import com.vtradex.wms.client.inventoryviewUI.data.Page_IV_DataAccessor;


public class Sub_IV_Panel extends AbstractSupportPanel {

	public static final int DEFAULT_WIDTH = Window.getClientWidth() - 220;
	public static final int DEFAULT_HEIGHT = Window.getClientHeight() - 170;
	private Panel centerPanelWrappper;
	private Panel introPanel;
	
	protected transient Sub_Navigate_Panel navigatePanel;
	
	public Sub_Navigate_Panel getNavigatePanel() {
		return navigatePanel;
	}

	public void setNavigatePanel(Sub_Navigate_Panel navigatePanel) {
		this.navigatePanel = navigatePanel;
	}

	public Sub_IV_Panel(IMessagePage page) {
		super(page, "iv_main_panel");
		this.initialize();
	}
	
	protected void initialize(){
		centerPanelWrappper = new Panel();
		centerPanelWrappper.setSize(DEFAULT_WIDTH , DEFAULT_HEIGHT);
        centerPanelWrappper.setLayout(new FitLayout());
        centerPanelWrappper.setBorder(true);
        centerPanelWrappper.setBodyBorder(true);
        
        introPanel = new Panel();
        introPanel.setLayout(new RowLayout());
        introPanel.setAutoDestroy(true);
        introPanel.setAutoScroll(true);
        centerPanelWrappper.add(introPanel);
        
        Panel northPanel = getNorthPanel();
        northPanel.setPaddings(5);
        northPanel.setLayout(new FitLayout());
        northPanel.setAutoDestroy(true);
        
        BorderLayoutData centerLayoutData = new BorderLayoutData(RegionPosition.CENTER);
        centerLayoutData.setMargins(new Margins(1, 1, 1, 1));

        BorderLayoutData northLayoutData = new BorderLayoutData(RegionPosition.NORTH);
//        northLayoutData.setMargins(new Margins(1, 1, 1, 1));
//        northLayoutData.setCMargins(new Margins(1, 1, 1, 1));
//        northLayoutData.setMinSize(20);
//        northLayoutData.setMaxSize(100);
//        northLayoutData.setSplit(true);
        
        this.setBodyBorder(true);
        this.setBorder(true);
        this.setLayout(new BorderLayout());
        this.setAutoDestroy(true);
        this.setSize(Window.getClientWidth() - 50 , Window.getClientHeight() - 130);
        this.add(northPanel,northLayoutData);
        this.add(centerPanelWrappper, centerLayoutData);
        DeferredCommand.addCommand(new Command(){
			public void execute() {
				 getData().sendMessage(Page_IV_DataAccessor.MSG_OPEN_WAREHOUSE_IMAGE_PANEL);
			}
        });
       
	}

	protected Panel getNorthPanel(){
		navigatePanel =  new Sub_Navigate_Panel(this);
		this.setNavigatePanel(navigatePanel);
		return navigatePanel;
	}
	
	public void addCenterPanel(Panel panel) {
		introPanel.clear();
		introPanel.add(panel , new RowLayoutData("100%"));
	}
}

