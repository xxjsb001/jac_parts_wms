package com.vtradex.wms.client.ui.page.allocate;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.RowLayout;
import com.vtradex.thorn.client.config.page.PageConfig;
import com.vtradex.thorn.client.template.BaseCustomPopupTemplate;
import com.vtradex.thorn.client.utils.LocaleUtils;
import com.vtradex.wms.client.ui.page.allocate.page.data.AllocateDataAccessor;
import com.vtradex.wms.client.ui.page.allocate.page.panel.PickTicketInfoNorthPanel;
import com.vtradex.wms.client.ui.page.allocate.page.ui.AbstractManualAllocateGridPanel;

public abstract class AbstractAllocatePage extends BaseCustomPopupTemplate implements IsSerializable {
	protected transient Panel container;
	protected transient Panel allocatingPanel;
	protected transient Panel allocatedPanel;
	
	protected transient PickTicketInfoNorthPanel northPanel;
	protected transient AbstractManualAllocateGridPanel detailPanel;
	protected transient AbstractManualAllocateGridPanel availablePanel;
	protected transient AbstractManualAllocateGridPanel allocatedInfoPanel;
	
	public void draw(VerticalPanel content) {
		super.draw(content);
		initContainer();
		initDetailsPanel();
		initAvailablePanel();
		initAllocatedPanel();
		content.add(container);
		initDataAccessor();
		initData();
	}
	 
	protected void initContainer() {
		container = new Panel();
//		if (Window.getClientWidth() > 1200) {
//			width = Window.getClientWidth() - 250;
//		} else {
//			width = Window.getClientWidth() - 50;
//		}
//		container.setHeight(Window.getClientHeight() - 130);
		container.setSize("963px", "465px");
		container.setBorder(false);
		container.setLayout(new BorderLayout());

		northPanel = getInfosNorthPanel();
		northPanel.setWidth("963px");
		northPanel.setAutoDestroy(true);

		TabPanel centerPanel = new TabPanel();

		allocatingPanel = new Panel();
		allocatingPanel.setTitle(LocaleUtils.getText("allocating"));
		allocatingPanel.setLayout(new RowLayout());
		centerPanel.add(allocatingPanel);

		allocatedPanel = new Panel();
		allocatedPanel.setTitle(LocaleUtils.getText("cancelAllocate"));
		allocatedPanel.setLayout(new RowLayout());

		centerPanel.add(allocatedPanel);
		centerPanel.setSize("963px", "465px");
		centerPanel.activate(0);
		
		container.add(northPanel, new BorderLayoutData(RegionPosition.NORTH));
		container.add(centerPanel, new BorderLayoutData(RegionPosition.CENTER));
	}
	 
	protected void initDataAccessor() {
		data = new AllocateDataAccessor(this);
	}

	public PageConfig getPageConfig() {
		return pageConfig;
	}
	 
	protected abstract PickTicketInfoNorthPanel getInfosNorthPanel();
	protected abstract AbstractManualAllocateGridPanel getDetailsPanel();
	protected abstract AbstractManualAllocateGridPanel getAvailablePanel();
	protected abstract AbstractManualAllocateGridPanel getAllocatedPanel();
	protected abstract void initDetailsPanel();
	protected abstract void initAvailablePanel();
	protected abstract void initAllocatedPanel();
	public abstract void initData();
}
