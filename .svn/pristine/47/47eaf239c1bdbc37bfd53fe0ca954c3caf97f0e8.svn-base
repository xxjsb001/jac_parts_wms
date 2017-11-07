package com.vtradex.wms.client.ui.page.allocate.page;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.widgets.layout.RowLayoutData;
import com.vtradex.thorn.client.ui.page.IPage;
import com.vtradex.thorn.client.utils.LocaleUtils;
import com.vtradex.wms.client.ui.page.allocate.AbstractAllocatePage;
import com.vtradex.wms.client.ui.page.allocate.page.data.AllocateDataAccessor;
import com.vtradex.wms.client.ui.page.allocate.page.panel.AllocatedPickInfoPanel;
import com.vtradex.wms.client.ui.page.allocate.page.panel.PickAvailableInventoryPanel;
import com.vtradex.wms.client.ui.page.allocate.page.panel.PickTicketDetailsPanel;
import com.vtradex.wms.client.ui.page.allocate.page.panel.PickTicketInfoNorthPanel;
import com.vtradex.wms.client.ui.page.allocate.page.ui.AbstractManualAllocateGridPanel;

public class ManualPickingAllocatePage extends AbstractAllocatePage {
	public void draw(VerticalPanel content) {
		super.draw(content);
	}
	
	public String getTitle() {
		return LocaleUtils.getText("manualPickingAllocatePage");
	}

	public void initDetailsPanel() {
		detailPanel = new PickTicketDetailsPanel(this);
		allocatingPanel.add(detailPanel, new RowLayoutData("50%"));
	}

	public void initAvailablePanel() {
		availablePanel = new PickAvailableInventoryPanel(this);
		allocatingPanel.add(availablePanel, new RowLayoutData("50%"));
	}

	public void initAllocatedPanel() {
		allocatedInfoPanel = new AllocatedPickInfoPanel(this);
		allocatedPanel.add(allocatedInfoPanel, new RowLayoutData("100%"));
	}
	
	public void initData() {
		Long moveDocId = (Long) params.get(IPage.ENTITY_ID);
		final Map<String,Long> param = new HashMap<String,Long>();
		param.put(AllocateConstant.PARENT_ID, moveDocId);
		((AllocateDataAccessor)data).initPage(param);
	}
	
//	public void initAllocateInfo() {
//		Long moveDocId = (Long) params.get(IPage.ENTITY_ID);
//		final Map<String,Long> param = new HashMap<String,Long>();
//		param.put(AllocateConstant.PARENT_ID, moveDocId);
//		((AllocateDataAccessor)data).initAllocatedInfo(param);
//	}

	public PickTicketInfoNorthPanel getInfosNorthPanel() {
		return new PickTicketInfoNorthPanel(this);
	}

	public AbstractManualAllocateGridPanel getDetailsPanel() {
		return new PickTicketDetailsPanel(this);
	}

	public AbstractManualAllocateGridPanel getAvailablePanel() {
		return new PickAvailableInventoryPanel(this);
	}

	public AbstractManualAllocateGridPanel getAllocatedPanel() {
		return new AllocatedPickInfoPanel(this);
	}
}
