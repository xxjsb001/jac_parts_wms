package com.vtradex.wms.client.inventoryviewUI.page;


import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.widgets.Panel;
import com.vtradex.thorn.client.ApplicationWindow;
import com.vtradex.thorn.client.template.BaseCustomMaintainTemplate;
import com.vtradex.thorn.client.ui.panel.AbstractSupportPanel;
import com.vtradex.wms.client.inventoryviewUI.data.Page_IV_DataAccessor;


public class Page_InventoryVisibility extends BaseCustomMaintainTemplate
		implements IsSerializable {
	
	/** 背景主面板--用于仓库地域分布显示 */
	protected transient Panel imagePanel;
	protected transient Sub_IV_Panel ivPanel;
	
	public Sub_IV_Panel getIvPanel() {
		return ivPanel;
	}
	public void setIvPanel(Sub_IV_Panel ivPanel) {
		this.ivPanel = ivPanel;
	}
	/** 绘制主面板--主入口 */
	public void draw(VerticalPanel content) {
    	super.draw(content);
    	this.initDataAccessor();
    	this.drawMainPanel(content);
//    	this.initIVDatas();
    }
	/** 绘制主面板 */
	protected void drawMainPanel(VerticalPanel content) {
		ivPanel = new Sub_IV_Panel(this);
		content.add(ivPanel);
		this.setIvPanel(ivPanel);
	}
	/** 初始化数据存储区 */
	protected void initDataAccessor() {
		this.data = new Page_IV_DataAccessor(this);
	}
	/** 获取当前的数据存储区对象实例 */
	protected Page_IV_DataAccessor curIVDataAccessor() {
		return (Page_IV_DataAccessor)this.data;
	}
	/** 初始化库存可视化的数据 */
	protected void initIVDatas() {
		this.curIVDataAccessor().initIVDatas();
	}
	
	public void userFinalize() {
		this.curIVDataAccessor().sendMessage(Page_IV_DataAccessor.MSG_CLOSE_ALL_COLUMN_CHART);
	}
	
	public String getTitle(){
		return "库存可视化";
	}
	
	public void onPageUnselected() {
		this.curIVDataAccessor().sendMessage(Page_IV_DataAccessor.MSG_CLOSE_ALL_COLUMN_CHART);
	}
	
	@Override
	public void doBeforePageClose() {
		onPageUnselected();
	}
	
	@Override
	public void onPageSelected() {
		Sub_Navigate_Panel nPanel = this.getIvPanel().getNavigatePanel();
		if(nPanel!=null) {
			if(nPanel.getCurrentPanel() instanceof Sub_Warehouse_Image_Panel){
				nPanel.getCurrentPanel().showCharts();
			}
			if(nPanel.getCurrentPanel() instanceof Sub_Zone_Image_Panel){
				nPanel.getCurrentPanel().showCharts();
			}
		}
	}
	
	public void userChangeTab() {
		this.curIVDataAccessor().sendMessage(Page_IV_DataAccessor.MSG_CLOSE_ALL_COLUMN_CHART);
	}
	public void release() {
		
	}
	public List<String> getLocaleMessageKeys() {
		List<String> keys = new ArrayList<String>();
		keys.add("aisle_1");
		keys.add("aisle_2");
		keys.add("aisle_3");
		keys.add("aisle_4");
		keys.add("aisle_5");
		keys.add("aisle_6");
		keys.add("aisle_7");
		keys.add("aisle_8");
		keys.add("aisle_9");
		keys.add("aisle_10");
		
		keys.add("col_1");
		keys.add("col_2");
		keys.add("col_3");
		keys.add("col_4");
		keys.add("col_5");
		keys.add("col_6");
		keys.add("col_7");
		keys.add("col_8");
		keys.add("col_9");
		keys.add("col_10");

		return keys;
	}
}
