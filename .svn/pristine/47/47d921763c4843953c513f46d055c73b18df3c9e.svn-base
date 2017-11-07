package com.vtradex.wms.client.ui.page.allocate.page.panel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.CheckboxColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
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
import com.vtradex.wms.client.ui.page.allocate.page.model.CustomMoveDoc;
import com.vtradex.wms.client.ui.page.allocate.page.model.CustomTask;
import com.vtradex.wms.client.ui.page.allocate.page.ui.AbstractManualAllocateGridPanel;
import com.vtradex.wms.client.ui.page.allocate.page.utils.GridUtils;

@SuppressWarnings({"unchecked", "unused"})
public class AllocatedPickInfoPanel extends AbstractManualAllocateGridPanel {
	transient Toolbar toolbar;
//	transient ToolbarButton singleCancelAllocate;
//	transient Checkbox selectAll;
//	transient ToolbarButton allCancelAllocate;
	public AllocatedPickInfoPanel(IMessagePage page) {
		super(page, "allocatedDetailsPanel");
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
		return new RecordDef(GridUtils.generateFiledDefs("allocatedDetailsPanel"));
	}

	protected ColumnModel generateColumnModel() {
//		return new ColumnModel(GridUtils.generateColumnConfigs("allocatedDetailsPanel"));
		int size = Integer.parseInt(LocaleUtils.getText("allocatedDetailsPanel.title.size"));
		int index = -1;
		if(LocaleUtils.getText("allocatedDetailsPanel.editable.index") != null) {
			index = Integer.parseInt(LocaleUtils.getText("allocatedDetailsPanel.editable.index"));
		}
		BaseColumnConfig[] configs = new BaseColumnConfig[size + 1];
		final CheckboxSelectionModel cbSelectionModel = new CheckboxSelectionModel();
		gridPanel.setSelectionModel(cbSelectionModel);
		configs[0] = new CheckboxColumnConfig(cbSelectionModel);
		for(int i = 0 ; i < size; i++){
			ColumnConfig config = new ColumnConfig("<font style='font-weight:bold' color='#2a2a2a'>" + LocaleUtils.getText("allocatedDetailsPanel.title." + i) + "</font>",LocaleUtils.getText("allocatedDetailsPanel.recordDef." + (i + 3)),Integer.parseInt(LocaleUtils.getText("allocatedDetailsPanel.title.size."  + i)));
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
//		            && gridPanel.getSelectionModel().getSelections().length > 0) {
//					singleCancelAllocate.setDisabled(Boolean.FALSE);
//				} else {
//					singleCancelAllocate.setDisabled(Boolean.TRUE);
//				}
//			}
			
//			public void onRowContextMenu(GridPanel grid, int rowIndex,
//				EventObject e) {
//				e.stopEvent();
//				showGridMenu(grid, rowIndex, e);
//			}
//		});
	}
	
	protected void showGridMenu(EventObject e) {
		if (menu == null) {
			menu = rightContextMenu.getAllocatedGridMenu();
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
		if(AllocateMessageConstant.MSG_ALLOCATED_INFO_DATA_CHANGE.equals(message)) {
			if(getData().getAllocatedInfoData() == null) {
				reset();
			} else {
				reloadDetails();
			}
		} else if(AllocateMessageConstant.MSG_INIT_MOVE_DOC_ALLOCATED_PAGE.equals(message)) {
			reloadDetails();
		}
	}
	
	private void reloadDetails() {
		List<CustomTask> customTasks = (List<CustomTask>)getData().getAllocatedInfoData().get(AllocateConstant.ALLOCATED_RESULT);
		Object[][] data = new Object[customTasks.size()][];
		for(int i = 0; i < customTasks.size(); i++) {
			data[i] = customTasks.get(i).toArray();
		}
		proxy = new MemoryProxy(data);
		store.setDataProxy(proxy);
		store.reload();
//		singleCancelAllocate.setDisabled(Boolean.TRUE);
	}
	
	public void reset() {
		super.reset();
//		singleCancelAllocate.setDisabled(Boolean.TRUE);
	}
	
	private void initToolbar() {
		toolbar = new Toolbar();
//		singleCancelAllocate = new ToolbarButton(LocaleUtils.getText("allocatedDetailsPanel.singleCancelAllocate"), new ButtonListenerAdapter() {
//            public void onClick(Button button, EventObject e) {
//            	if(gridPanel.getSelectionModel().getSelections() != null 
//    	            && gridPanel.getSelectionModel().getSelections().length > 0) {
//            		manualCancelAllocate();
//            	}
//            }
//        });
//		singleCancelAllocate.setDisabled(Boolean.TRUE);
//		singleCancelAllocate.setPressed(Boolean.TRUE);
		
//		selectAll = new Checkbox(LocaleUtils.getText("selectAll"), new CheckboxListenerAdapter() {
//			public void onCheck(Checkbox field, boolean checked) {
//				if (checked) {
//					gridPanel.getSelectionModel().selectAll();
//				} else {
//					gridPanel.getSelectionModel().clearSelections();
//				}
//			}
//		});
		
//		allCancelAllocate = new ToolbarButton(LocaleUtils.getText("allocatedDetailsPanel.allCancelAllocate"), new ButtonListenerAdapter() {
//            public void onClick(Button button, EventObject e) {
//            	cancelAllocate();
//            }
//        });
//		allCancelAllocate.setDisabled(Boolean.FALSE);
//      toolbar.addField(selectAll);
//      toolbar.addSeparator();
//		toolbar.addButton(singleCancelAllocate);
	}
	
	private void cancelAllocate() {
		final Map<String, Object> params = new HashMap<String, Object>();
		CustomMoveDoc customMoveDoc = ((AllocateDataAccessor)getData()).getCustomMoveDocByTask();
		params.put(AllocateConstant.CLIENT_ENTITY, customMoveDoc);
		((AllocateDataAccessor)getData()).cancelAllocate(params);
	}
	
	private void manualCancelAllocate() {
		final Map<String, Object> params = new HashMap<String, Object>();
		CustomMoveDoc customMoveDoc = ((AllocateDataAccessor)getData()).getCustomMoveDocByTask();
		params.put(AllocateConstant.CLIENT_ENTITY, customMoveDoc);
		final Map<Long, Double> subParam = new HashMap<Long, Double>();
		for (int i = 0; i < gridPanel.getStore().getCount(); i++) {
			if (gridPanel.getSelectionModel().isSelected(i)) {
				Long taskId = Long.valueOf(gridPanel.getStore().getAt(i).getAsString("id"));
				
				Double allocatedQuantity = Double.valueOf(gridPanel.getStore().getAt(i).getAsString("planQuantityBU"));
				Double cancelQuantity = Double.valueOf(gridPanel.getStore().getAt(i).getAsString("cancelQuantity"));
				
				if (cancelQuantity.doubleValue() > allocatedQuantity.doubleValue()) {
					Window.alert(LocaleUtils.getText("availableQuantity.not.enough"));
					return;
				}
				
				subParam.put(taskId, cancelQuantity);
			}
		}
		params.put(IPage.TABLE_INPUT_VALUES, subParam);
		
		((AllocateDataAccessor)getData()).manualCancelAllocate(params);
	}
	
	public BaseItemListenerAdapter genSingleCancelAllocateListenerAdapter() {
		return new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				manualCancelAllocate();
			}
		};
	}
	
	public BaseItemListenerAdapter genSearchListenerAdapter() {
		return new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				dialog = new DialogBoxTemplate(LocaleUtils.getText("manualPickingAllocatePage.allocatedDetailsPanel.search"), 
					new ManualAllocateSearchPanel(AllocatedPickInfoPanel.this));
				int positionX = (Window.getClientWidth() - 430) / 2;
				int positionY = (Window.getClientHeight() - 450) / 2;
				dialog.setPopupPosition(positionX , positionY);
				dialog.show();
			}
		};
	}
}
