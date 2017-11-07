package com.vtradex.wms.client.ui.page.allocate.page.ui;

import com.google.gwt.user.client.Window;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.event.GridListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.thorn.client.template.DialogBoxTemplate;
import com.vtradex.wms.client.ui.page.allocate.page.model.CustomMoveDocDetail;

@SuppressWarnings("unchecked")
public abstract class AbstractManualAllocateGridPanel extends AbstractPanel {
	protected EditorGridPanel gridPanel;
	protected Store store;
	protected RecordDef recordDef;
	protected ArrayReader reader;
	protected MemoryProxy proxy;
	protected ColumnModel columnModel;
	protected DialogBoxTemplate dialog;
	
	public AbstractManualAllocateGridPanel(IMessagePage page, String name) {
		super(page, name);
	}
	
	protected void init() {
		rightContextMenu = new RightContextMenu(this);
		setWidth("100%");
		setLayout(new BorderLayout());
		setCollapsible(true);
		setAutoDestroy(true);
		initGridPanel();
	}
	
	protected void draw() {
		add(gridPanel, new BorderLayoutData(RegionPosition.CENTER));
	}

	protected void initGridPanel() {
		proxy = new MemoryProxy(new String[][]{});
		recordDef = generateRecordDef();
		reader = new ArrayReader(0, recordDef);
		store = new Store(proxy, reader);
		gridPanel = new EditorGridPanel() {
			{
				setStore(store);
				setWidth(Window.getClientWidth() - 300);
				setHeight((Window.getClientHeight() - 138) / 2 - 30);
				setAutoScroll(true);
				setLoadMask(true);
				setStripeRows(true);
				setEnableColumnHide(false);
				setEnableHdMenu(true);
				setEnableColumnMove(false);
				setBorder(false);
				setClicksToEdit(1);
				addGridListener(new GridListenerAdapter(){
					public void onContextMenu(EventObject e) {
						e.stopEvent();
						showGridMenu(e);
					}
				});
			};
		};
		columnModel = generateColumnModel();
		gridPanel.setColumnModel(columnModel);
		addGridRowListener();
	}
	
	public void reset() {
		proxy = new MemoryProxy(new String[][]{});
		
		store.setDataProxy(proxy);
		store.reload();
	}
	
	protected void reload(CustomMoveDocDetail customDetail) {
		store.load();
	}
	
	public Store getStore() {
		return gridPanel.getStore();
	}
	
	public void hideDialog(){
		gridPanel.getSelectionModel().clearSelections();
		dialog.hide(true);
	}
	
	protected abstract RecordDef generateRecordDef();
	protected abstract ColumnModel generateColumnModel();
	protected abstract void addGridRowListener();
	protected abstract void updateGridMenuItemStatus();
	protected abstract void showGridMenu(EventObject e);
}
