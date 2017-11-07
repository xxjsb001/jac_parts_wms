package com.vtradex.wms.client.ui.page.allocate.page.ui;

import com.gwtext.client.widgets.Panel;
import com.vtradex.thorn.client.ui.securityUI.Menu;
import com.vtradex.thorn.client.ui.securityUI.MenuItem;
import com.vtradex.thorn.client.utils.LocaleUtils;
import com.vtradex.wms.client.ui.page.allocate.page.panel.AllocatedPickInfoPanel;
import com.vtradex.wms.client.ui.page.allocate.page.panel.PickAvailableInventoryPanel;
import com.vtradex.wms.client.ui.page.allocate.page.panel.PickTicketDetailsPanel;

/**
 * 
 * @author <a href="yan.li@vtradex.com">李炎</a>
 * @description 右键菜单
 * 
 */

public class RightContextMenu {
	
	Panel eventPanel;
	
	public RightContextMenu(Panel eventPanel){
		this.eventPanel = eventPanel;
	}
	
	public Menu getAllocatingGridMenu(){
		Menu menu = new Menu();
		MenuItem searchItem = new MenuItem("manualAllocateProcess.search", 
			LocaleUtils.getText("manualPickingAllocatePage.rightMenu.search"),
			((PickAvailableInventoryPanel)eventPanel).genSearchListenerAdapter(), 
			((PickAvailableInventoryPanel)eventPanel).getPageConfig());
		MenuItem manualAllocatedItem = new MenuItem("manualAllocateProcess.manualAllocate", 
			LocaleUtils.getText("pickAvailablePanel.rightMenu.manualAllocate"),
			((PickAvailableInventoryPanel)eventPanel).genManualAllocateListenerAdapter(), 
			((PickAvailableInventoryPanel)eventPanel).getPageConfig());
		menu.addMenuItem(searchItem);
		menu.addMenuItem(manualAllocatedItem);
		return menu;
	}
	
	public Menu getAllocatedGridMenu(){
		Menu menu = new Menu();
		MenuItem searchItem = new MenuItem("manualAllocateProcess.search", 
			LocaleUtils.getText("manualPickingAllocatePage.rightMenu.search"),
			((AllocatedPickInfoPanel)eventPanel).genSearchListenerAdapter(), 
			((AllocatedPickInfoPanel)eventPanel).getPageConfig());
		MenuItem cancelAllocatedItem = new MenuItem("manualAllocateProcess.singleCancelAllocate", 
			LocaleUtils.getText("allocatedDetailsPanel.rightMenu.singleCancelAllocate"),
			((AllocatedPickInfoPanel)eventPanel).genSingleCancelAllocateListenerAdapter(), 
			((AllocatedPickInfoPanel)eventPanel).getPageConfig());
		menu.addMenuItem(searchItem);
		menu.addMenuItem(cancelAllocatedItem);
		return menu;
	}
	
	public Menu getDetailGridMenu(){
		Menu menu = new Menu();
//		MenuItem getAvailableInventoriesItem = new MenuItem("manualAllocateProcess.findAvailableInventories", 
//			LocaleUtils.getText("pickTicketDetailsPanel.rightMenu.getAvailableInventories"),
//			((PickTicketDetailsPanel)eventPanel).genAvailableInventoriesListenerAdapter(), 
//			((PickTicketDetailsPanel)eventPanel).getPageConfig());
		MenuItem searchItem = new MenuItem("manualAllocateProcess.search", 
			LocaleUtils.getText("manualPickingAllocatePage.rightMenu.search"),
			((PickTicketDetailsPanel)eventPanel).genSearchListenerAdapter(), 
			((PickTicketDetailsPanel)eventPanel).getPageConfig());
		menu.addMenuItem(searchItem);
		return menu;
	}
}


