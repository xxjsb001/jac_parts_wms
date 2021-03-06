package com.vtradex.wms.client.ui.page.allocate.page.panel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Window;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.CheckboxColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.thorn.client.template.DialogBoxTemplate;
import com.vtradex.thorn.client.ui.page.IPage;
import com.vtradex.thorn.client.utils.LocaleUtils;
import com.vtradex.wms.client.ui.page.allocate.page.AllocateConstant;
import com.vtradex.wms.client.ui.page.allocate.page.AllocateMessageConstant;
import com.vtradex.wms.client.ui.page.allocate.page.data.AllocateDataAccessor;
import com.vtradex.wms.client.ui.page.allocate.page.model.CustomInventory;
import com.vtradex.wms.client.ui.page.allocate.page.model.CustomMoveDocDetail;
import com.vtradex.wms.client.ui.page.allocate.page.ui.AbstractManualAllocateGridPanel;
import com.vtradex.wms.client.ui.page.allocate.page.utils.GridUtils;

@SuppressWarnings("unchecked")
public class PickAvailableInventoryPanel extends AbstractManualAllocateGridPanel {
	transient Toolbar toolbar;
//	transient Checkbox selectAll;
//	transient ToolbarButton manualAllocateButton;
	public PickAvailableInventoryPanel(IMessagePage page) {
		super(page, "pickAvailablePanel");
	}
	
	protected void init() {
		super.init();
		initToolbar();
	}
	
	protected void draw() {
		super.draw();
		add(toolbar, new BorderLayoutData(RegionPosition.SOUTH));
	}

	protected RecordDef generateRecordDef() {
		return new RecordDef(GridUtils.generateFiledDefs("pickAvailablePanel"));
	}

	protected ColumnModel generateColumnModel() {
//		System.out.println("X001-X002-0006");
//		return new ColumnModel(GridUtils.generateColumnConfigs("pickAvailablePanel"));
		int size = Integer.parseInt(LocaleUtils.getText("pickAvailablePanel.title.size"));
		int index = -1;
		if(LocaleUtils.getText("pickAvailablePanel.editable.index") != null) {
			index = Integer.parseInt(LocaleUtils.getText("pickAvailablePanel.editable.index"));
		}
		BaseColumnConfig[] configs = new BaseColumnConfig[size + 1];
		final CheckboxSelectionModel cbSelectionModel = new CheckboxSelectionModel();
		gridPanel.setSelectionModel(cbSelectionModel);
		configs[0] = new CheckboxColumnConfig(cbSelectionModel);
		for(int i = 0 ; i < size; i++){
			ColumnConfig config = new ColumnConfig("<font style='font-weight:bold' color='#2a2a2a'>" + LocaleUtils.getText("pickAvailablePanel.title." + i) + "</font>",LocaleUtils.getText("pickAvailablePanel.recordDef." + (i + 3)),Integer.parseInt(LocaleUtils.getText("pickAvailablePanel.title.size."  + i)));
			if(index != -1 && i == index) {
				NumberField numberField = new NumberField();
	            numberField.setAllowBlank(false);
	            numberField.setAllowNegative(false);
	            config.setAlign(TextAlign.CENTER);
	            config.setEditor(new GridEditor(numberField));
	            config.setCss("border-style: solid; border-width: 1px 1px 1px 1px;border-color: blue");
			} else {
				config.setAlign(TextAlign.CENTER);
				config.setRenderer(new Renderer(){
					public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
						if(value == null)
							return "-";
						String displayValue = value.toString();
						return displayValue;
					}
				});
			}
			configs[i + 1] = config;
		}
		return new ColumnModel(configs);
	}

	protected void addGridRowListener() {
//		gridPanel.addGridRowListener(new GridRowListenerAdapter() {
//			public void onRowClick(GridPanel grid, int rowIndex,
//				EventObject e) {
//				e.stopEvent();
//				if(gridPanel.getSelectionModel().getSelections() != null 
//	            	&& gridPanel.getSelectionModel().getSelections().length > 0) { 
//					manualAllocateButton.setDisabled(Boolean.FALSE);
//				} else {
//					manualAllocateButton.setDisabled(Boolean.TRUE);
//				}
//			}
//		});
	}
	
	protected void showGridMenu(EventObject e) {
		if (menu == null) {
			menu = rightContextMenu.getAllocatingGridMenu();
		}
		updateGridMenuItemStatus();
		menu.showAt(e.getXY());
	}
	
	protected void updateGridMenuItemStatus() {
		int selectCount = gridPanel.getSelectionModel().getCount();
		this.menu.getMenuItems()[0].setVisible(true);
		this.menu.getMenuItems()[0].setDisabled(false);
		if(selectCount > 0) {
			this.menu.getMenuItems()[1].setVisible(true);
			this.menu.getMenuItems()[1].setDisabled(false);
		} else {
			this.menu.getMenuItems()[1].setVisible(true);
			this.menu.getMenuItems()[1].setDisabled(true);
		}
	}

	public void doDispath(String message){
//		System.out.println("X001-X002-0005-"+message);
		if(AllocateMessageConstant.MSG_AVAILABLE_INVENTORY_DATA_CHANGE.equals(message)) {
			if(getData().getInventoryData() == null) {
				reset();
			} else {
				reloadDetails();
			}
		} else if(AllocateMessageConstant.MSG_INIT_MOVE_DOC_DETAIL_PAGE.equals(message)) {
			reset();
		}
	}
	
	private void reloadDetails() {
//		System.out.println("X001-X002-0004");
		List<CustomInventory> customInventories = (List<CustomInventory>)getData().getInventoryData().get(AllocateConstant.AVAILABLE_RESULT);
		Object[][] data = new Object[customInventories.size()][];
		for(int i = 0; i < customInventories.size(); i++) {
			data[i] = customInventories.get(i).toArray();
		}
		proxy = new MemoryProxy(data);
		store.setDataProxy(proxy);
		store.reload();
//		manualAllocateButton.setDisabled(Boolean.TRUE);
	}
	
	public void reset() {
		super.reset();
		proxy = new MemoryProxy(new String[][]{});
		store.setDataProxy(proxy);
		store.reload();
//		manualAllocateButton.setDisabled(Boolean.TRUE);
	}
	
	private void initToolbar() {
		toolbar = new Toolbar();
//		manualAllocateButton = new ToolbarButton(LocaleUtils.getText("pickAvailablePanel.manualAllocateButton"), new ButtonListenerAdapter() {
//            public void onClick(Button button, EventObject e) {
//            	if(gridPanel.getSelectionModel().getSelections() != null 
//            		&& gridPanel.getSelectionModel().getSelections().length > 0) { 
//            		manualAllocate();
//            	}
//            }
//        });
//		manualAllocateButton.setDisabled(Boolean.TRUE);
//		manualAllocateButton.setPressed(Boolean.TRUE);
//		selectAll = new Checkbox(LocaleUtils.getText("selectAll"), new CheckboxListenerAdapter() {
//			public void onCheck(Checkbox field, boolean checked) {
//				if (checked) {
//					gridPanel.getSelectionModel().selectAll();
//				} else {
//					gridPanel.getSelectionModel().clearSelections();
//				}
//			}
//		});
		
//		toolbar.addField(selectAll);
//      toolbar.addSeparator();
//		toolbar.addButton(manualAllocateButton);
	}
	
	private void manualAllocate() {
		final Map<String, Object> params = new HashMap<String, Object>();
		CustomMoveDocDetail customMoveDocDetail = ((AllocateDataAccessor)getData()).getPickAvailableDetail();
		params.put(AllocateConstant.CLIENT_ENTITY, customMoveDocDetail);
		final Map<Long, Double> subParam = new HashMap<Long, Double>();
		for (int i = 0; i < gridPanel.getStore().getCount(); i++) {
			if (gridPanel.getSelectionModel().isSelected(i)) {
				Long inventoryId = Long.valueOf(gridPanel.getStore().getAt(i).getAsString("id"));
				
				Double availableQuantity = Double.valueOf(gridPanel.getStore().getAt(i).getAsString("availableQuantity"));
				Double manualQuantity = Double.valueOf(gridPanel.getStore().getAt(i).getAsString("allocateQuantity"));
				
				if (manualQuantity.doubleValue() > availableQuantity.doubleValue()) {
					Window.alert(LocaleUtils.getText("availableQuantity.not.enough"));
					return;
				}
				
				subParam.put(inventoryId, manualQuantity);
			}
		}
		params.put(IPage.TABLE_INPUT_VALUES, subParam);
		
		((AllocateDataAccessor)getData()).manualAllocate(params);
	}
	
	public BaseItemListenerAdapter genManualAllocateListenerAdapter() {
		return new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				manualAllocate();
			}
		};
	}
	
	public BaseItemListenerAdapter genSearchListenerAdapter() {
		return new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				dialog = new DialogBoxTemplate(LocaleUtils.getText("manualPickingAllocatePage.pickAvailablePanel.search"), 
					new ManualAllocateSearchPanel(PickAvailableInventoryPanel.this));
				int positionX = (Window.getClientWidth() - 430) / 2;
				int positionY = (Window.getClientHeight() - 450) / 2;
				dialog.setPopupPosition(positionX , positionY);
				dialog.show();
			}
		};
	}
}
