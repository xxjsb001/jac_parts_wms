package com.vtradex.wms.client.inventoryviewUI.page;


import java.util.HashMap;
import java.util.Map;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.thorn.client.utils.CustomMessageBox;
import com.vtradex.wms.client.inventoryviewUI.constant.CT_IV;
import com.vtradex.wms.client.inventoryviewUI.data.Page_IV_DataAccessor;
import com.vtradex.wms.client.inventoryviewUI.util.Sub_L_Image_Panel_Util;
import com.vtradex.wms.client.inventoryviewUI.util.Ui_Pie_Chart_Util;
import com.vtradex.wms.client.ui.javabean.JB_Location_IV;
import com.vtradex.wms.client.ui.javabean.JB_Location_RC_IV;

@SuppressWarnings("unchecked")
public class Sub_Location_Image_Panel extends Sub_Abstract_Panel {
	
	private EditorGridPanel leftPanel;
	private EditorGridPanel rightTopPanel;
	
	public Sub_Location_Image_Panel(IMessagePage page) {
		super(page, Sub_L_Image_Panel_Util.DEFAULT_NAME);
		this.initialize();
		
		this.setSize(Sub_IV_Panel.DEFAULT_WIDTH , Sub_IV_Panel.DEFAULT_HEIGHT);
		this.setBorder(false);
		this.setBodyBorder(false);
    	this.setLayout(new BorderLayout());
    	
        Panel centerPanelWrappper = new Panel();
        centerPanelWrappper.setWidth(Sub_IV_Panel.DEFAULT_WIDTH);
        centerPanelWrappper.setHeight(Sub_IV_Panel.DEFAULT_HEIGHT);
        centerPanelWrappper.setLayout(new FitLayout());
        centerPanelWrappper.setBorder(false);
        centerPanelWrappper.setBodyBorder(false);
        centerPanelWrappper.setPaddings(0);
        
        leftPanel.setAutoDestroy(true);
        Panel introPanel = new Panel();
        introPanel.setWidth(Sub_IV_Panel.DEFAULT_WIDTH);
        introPanel.setLayout(new HorizontalLayout(0));
        introPanel.setAutoDestroy(true);
        introPanel.setBodyBorder(false);
        introPanel.setBorder(false);
        centerPanelWrappper.add(introPanel);
        
        introPanel.add(this.leftPanel , new BorderLayoutData(RegionPosition.WEST));
        introPanel.add(this.rightTopPanel , new BorderLayoutData(RegionPosition.CENTER));

        this.add(centerPanelWrappper, new BorderLayoutData(RegionPosition.CENTER));
		this.initLocationDatas();
	}
	
	protected void initLocationDatas() {
		this.curIVDataAccessor().initLocationIvDatas();
	}
	
	public Page_IV_DataAccessor curIVDataAccessor() {
		return (Page_IV_DataAccessor)super.getData();
	}
	
	protected void initialize() {
		this.initLeftPanel();
		this.initRightTopPanel();
	}
	
	protected void initLeftPanel(){
		leftPanel = new EditorGridPanel();
		leftPanel.setBorder(true);
		leftPanel.setBodyBorder(true);
		leftPanel.setWidth(220);
		leftPanel.setHeight(Sub_IV_Panel.DEFAULT_HEIGHT - 8);
		leftPanel.setLayout(new FitLayout());
		leftPanel.setColumnModel(Sub_L_Image_Panel_Util.getALColumnModel());
		leftPanel.setStore(Sub_L_Image_Panel_Util.getALStore(new Object[][]{}));
		leftPanel.setAutoScroll(true);
		leftPanel.addGridCellListener(new GridCellListenerAdapter(){
			public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
				initRightTopPanelDatas(rowIndex,colindex);
		    }
		});
		leftPanel.setEnableHdMenu(false);
		leftPanel.setEnableColumnMove(false);
	}
	
	protected void initRightTopPanel(){
		rightTopPanel = new EditorGridPanel();
		rightTopPanel.setBodyBorder(true);
		rightTopPanel.setBorder(true);
		rightTopPanel.setLayout(new FitLayout());
		rightTopPanel.setWidth(Sub_IV_Panel.DEFAULT_WIDTH - 220);
		rightTopPanel.setHeight(Sub_IV_Panel.DEFAULT_HEIGHT - 8);
		rightTopPanel.setColumnModel(Sub_L_Image_Panel_Util.getRCColumnModel());
		rightTopPanel.setStore(Sub_L_Image_Panel_Util.getRCStore(new Object[][]{}));
		rightTopPanel.addGridCellListener(new GridCellListenerAdapter(){
			public void onCellDblClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
				initRightBottomPanelDatas(rowIndex , colindex);
		    }
		});
		rightTopPanel.setEnableHdMenu(false);
		rightTopPanel.setAutoScroll(true);
		rightTopPanel.setEnableColumnMove(false);
	}
	
	protected void initRightTopPanelDatas(int row,int col){
		if(col == 0) return;
		boolean isNaN = curIVDataAccessor().all_Location_IV().get(row).get(col - 1).getLoc_rate() < 0;
		if(isNaN) return;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(CT_IV.IV_LINE , row + 1);
		params.put(CT_IV.IV_AISLE , this.curDataAccessor().getCur_JB_Region_IV().getRegion_code());
		params.put(CT_IV.IV_ZONE_ID , this.curDataAccessor().getCur_JB_Zone_IV().getLoc_id());
		this.curIVDataAccessor().initLocationIvRCDatas(params);
	}
	
	protected void initRightBottomPanelDatas(int row,int col) {
		if(col == 0) return;
		JB_Location_RC_IV liv = curIVDataAccessor().all_Location_RC_IV().get(row).get(col - 1);
		if(liv.getRate() <= 0D) return;
		this.curIVDataAccessor().setCur_JB_Location_RC_IV(liv);
		if(liv.getId() == null || liv.getRate() <0) return;
		CT_IV.CURRENT_ROW = row;CT_IV.CURRENT_COL= col;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(CT_IV.IV_LOCATION_ID , liv.getId() );
		this.curIVDataAccessor().initInventoryDatas(params);
	}
	
	protected void applyToToolTip() {
		for(int i = 0 ; i < CT_IV.DEFAULT_ROW ; i ++) {
			for(int j = 1 ; j <= CT_IV.DEFAULT_COL ; j ++) {
				JB_Location_RC_IV lriv = this.curIVDataAccessor().all_Location_RC_IV().get(i).get(j - 1);
				if(lriv.getId() == null || lriv.getRate() < 0) continue;
				ToolTip tip = new ToolTip(Sub_L_Image_Panel_Util.generateToolTip(lriv));
				tip.setSize(200, 40);
				tip.applyTo(this.rightTopPanel.getView().getCell(i,j));
			}
		}
	}
	
	protected void applyToToolTip2(){
		for(int i = 0 ; i < CT_IV.DEFAULT_LINE ; i ++) {
			for(int j = 1 ; j <= CT_IV.DEFAULT_AISLE ; j ++) {
				JB_Location_IV lriv = this.curIVDataAccessor().all_Location_IV().get(i).get(j - 1);
				if(lriv.getLoc_rate() < 0) continue;
				ToolTip tip = new ToolTip(Sub_L_Image_Panel_Util.generateToolTip(lriv));
				tip.setSize(150, 60);
				tip.applyTo(this.leftPanel.getView().getCell(i,j));
			}
		}
	}
	
	public void doDispath(String message){
		super.doDispath(message);
		if(Page_IV_DataAccessor.MSG_INIT_LOCATION_DATAS.equals(message)){
			CT_IV.DEFAULT_LINE = this.curIVDataAccessor().getZld().getCurrentMaxLine();
			Sub_L_Image_Panel_Util.initALColumnConfig();
			leftPanel.reconfigure(Sub_L_Image_Panel_Util.getALStore(
					Sub_L_Image_Panel_Util.convertToAL2Array(this.curIVDataAccessor().all_Location_IV())) , 
					Sub_L_Image_Panel_Util.getALColumnModel());applyToToolTip2();

		}else if(Page_IV_DataAccessor.MSG_INIT_LOCATION_RC_DATAS.equals(message)) {
			CT_IV.DEFAULT_COL = this.curIVDataAccessor().getZld().getCurrentMaxCol();
			CT_IV.DEFAULT_ROW = this.curIVDataAccessor().getZld().getCurrentMaxRow();
			Sub_L_Image_Panel_Util.initRCColumnConfig();
			rightTopPanel.reconfigure(Sub_L_Image_Panel_Util.getRCStore(
					Sub_L_Image_Panel_Util.convertToRC2Array(this.curIVDataAccessor().all_Location_RC_IV())) , 
					Sub_L_Image_Panel_Util.getRCColumnModel());
			applyToToolTip();
			
		}else if(Page_IV_DataAccessor.MSG_INIT_INVENTORY_DATAS.equals(message)) {
			if(this.curIVDataAccessor().all_Location_Inv().size()>0) {
				JB_Location_RC_IV location = this.curIVDataAccessor().getCur_JB_Location_RC_IV();
				Element e = this.rightTopPanel.getView().getCell(CT_IV.CURRENT_ROW, CT_IV.CURRENT_COL);
				int left = e.getAbsoluteLeft();
				int top = e.getAbsoluteTop();
				String invMessage = Ui_Pie_Chart_Util.generateInventoryDetail(this.curIVDataAccessor().all_Location_Inv());
				String invTitle = "库位编码：" + location.getCode() + "&nbsp;&nbsp;" + "库位容积：" + NumberFormat.getFormat(CT_IV.DEFAULT_PERCENT_FORMAT).format(location.getRate())
								+ "&nbsp;&nbsp;" + "列：" + location.getCol() + "&nbsp;&nbsp;" + "层：" + location.getRow();
				String customMessage = Ui_Pie_Chart_Util.generateCustomMessageBox(invTitle, invMessage);
				CustomMessageBox.showMessage(customMessage, left, top+100, 400);
			}
		}
	}
}
