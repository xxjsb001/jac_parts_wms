package com.vtradex.wms.client.inventoryviewUI.page;



import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.thorn.client.ui.panel.AbstractSupportPanel;
import com.vtradex.wms.client.inventoryviewUI.data.Page_IV_DataAccessor;


public abstract class Sub_Abstract_Panel extends AbstractSupportPanel {

	public Sub_Abstract_Panel(IMessagePage page, String name) {
		super(page, name);
	}
	
	protected Page_IV_DataAccessor curDataAccessor() {
		return (Page_IV_DataAccessor)this.getData();
	}
	
	public void doDispath(String message){
	    if(Page_IV_DataAccessor.MSG_CLOSE_ALL_COLUMN_CHART.equals(message)) {
			this.hideCharts();
		}
	}

	public void hideCharts(){}
	
	public void showCharts(){}
}
