package com.vtradex.wms.client.ui.page.allocate.page.ui;

import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.thorn.client.ui.panel.AbstractSupportPanel;
import com.vtradex.thorn.client.ui.securityUI.Menu;
import com.vtradex.wms.client.ui.page.allocate.page.data.AllocateDataAccessor;

@SuppressWarnings("unchecked")
public abstract class AbstractPanel extends AbstractSupportPanel{
	protected Menu menu;
	protected RightContextMenu rightContextMenu;
	public AbstractPanel(IMessagePage page, String name) {
		super(page, name);
		init();
		draw();
	}
	
	public AllocateDataAccessor getData(){
		return (AllocateDataAccessor)super.getData();
	}
	
	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public RightContextMenu getRightContextMenu() {
		return rightContextMenu;
	}

	public void setRightContextMenu(RightContextMenu rightContextMenu) {
		this.rightContextMenu = rightContextMenu;
	}

	protected abstract void init();
	protected abstract void draw();
}
