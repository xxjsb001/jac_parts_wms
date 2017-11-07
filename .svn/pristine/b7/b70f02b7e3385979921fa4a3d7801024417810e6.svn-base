package com.vtradex.wms.client.ui.page.allocate.page.panel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Window;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.GridListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.thorn.client.template.DialogBoxTemplate;
import com.vtradex.thorn.client.utils.LocaleUtils;
import com.vtradex.wms.client.ui.page.allocate.page.AllocateConstant;
import com.vtradex.wms.client.ui.page.allocate.page.AllocateMessageConstant;
import com.vtradex.wms.client.ui.page.allocate.page.data.AllocateDataAccessor;
import com.vtradex.wms.client.ui.page.allocate.page.model.CustomMoveDoc;
import com.vtradex.wms.client.ui.page.allocate.page.model.CustomMoveDocDetail;
import com.vtradex.wms.client.ui.page.allocate.page.ui.AbstractManualAllocateGridPanel;
import com.vtradex.wms.client.ui.page.allocate.page.utils.GridUtils;

@SuppressWarnings({"unchecked"})
public class PickTicketDetailsPanel extends AbstractManualAllocateGridPanel {
	transient Toolbar toolbar;
	transient Checkbox lotInfoCheckBox, containLockInvCheckBox;
//	transient Checkbox selectAll;
	private Long moveDocDetailId;
	public PickTicketDetailsPanel(IMessagePage page) {
		super(page, "pickTicketDetailsPanel");
	}
	
	protected void init() {
		super.init();
		initToolbar();
	}
	
	protected void initGridPanel() {
		proxy = new MemoryProxy(new String[][]{});
		recordDef = generateRecordDef();
		reader = new ArrayReader(0, recordDef);
		store = new Store(proxy, reader);
		columnModel = generateColumnModel();
		gridPanel = new EditorGridPanel() {
			{
				setStore(store);
				setColumnModel(columnModel);
				setSelectionModel(new RowSelectionModel(true));
				setWidth(Window.getClientWidth() - 300);
				setHeight((Window.getClientHeight() - 138) / 2 - 30);
				setAutoScroll(true);
				setLoadMask(true);
				setStripeRows(true);
				setEnableColumnHide(false);
				setEnableHdMenu(false);
				setEnableColumnMove(false);
				setBorder(false);
				addGridListener(new GridListenerAdapter(){
					public void onContextMenu(EventObject e) {
						e.stopEvent();
						showGridMenu(e);
					}
				});
			};
		};
		addGridRowListener();
	}
	
	protected void draw() {
		super.draw();
		add(toolbar, new BorderLayoutData(RegionPosition.SOUTH));
	}

	protected RecordDef generateRecordDef() {
		return new RecordDef(GridUtils.generateFiledDefs("pickTicketDetailsPanel"));
	}

	protected ColumnModel generateColumnModel() {
		return new ColumnModel(GridUtils.generateColumnConfigs("pickTicketDetailsPanel"));
//		int size = Integer.parseInt(LocaleUtils.getText("pickTicketDetailsPanel.title.size"));
//		int index = -1;
//		if(LocaleUtils.getText("pickTicketDetailsPanel.editable.index") != null) {
//			index = Integer.parseInt(LocaleUtils.getText("pickTicketDetailsPanel.editable.index"));
//		}
//		BaseColumnConfig[] configs = new BaseColumnConfig[size + 1];
//		final CheckboxSelectionModel cbSelectionModel = new CheckboxSelectionModel();
//		cbSelectionModel.addListener(new RowSelectionListenerAdapter() {
//			public void onSelectionChange(RowSelectionModel sm) {
//				Long[] ids = null;
//				if(sm.hasSelection() && sm.getCount() > 0) {
//					ids = new Long[sm.getCount()];
//					int index = 0;
//					for(Record record : sm.getSelections()) {
//						ids[index] = Long.valueOf(record.getAsString("id"));
//						index++;
//					}
//				}
//				if(ids == null || (ids != null && ids.length != 1)) {
//					moveDocDetailId = null;
//					findAvailabeInventoryInfo(lotInfoCheckBox.getValue(), containLockInvCheckBox.getValue());
//				} else if(ids.length == 1 && moveDocDetailId != null && ids[0].longValue() != moveDocDetailId) {
//					moveDocDetailId = null;
//					findAvailabeInventoryInfo(lotInfoCheckBox.getValue(), containLockInvCheckBox.getValue());
//				} else {
//					moveDocDetailId = ids[0];
//					findAvailabeInventoryInfo(lotInfoCheckBox.getValue(), containLockInvCheckBox.getValue());
//				}
//			}
//		});
//		gridPanel.setSelectionModel(cbSelectionModel);
//		configs[0] = new CheckboxColumnConfig(cbSelectionModel);
//		for(int i = 0 ; i < size; i++){
//			ColumnConfig config = new ColumnConfig("<font style='font-weight:bold' color='#2a2a2a'>" + LocaleUtils.getText("pickTicketDetailsPanel.title." + i) + "</font>",LocaleUtils.getText("pickTicketDetailsPanel.recordDef." + (i + 3)),Integer.parseInt(LocaleUtils.getText("pickTicketDetailsPanel.title.size."  + i)));
//			if(index != -1 && i == index) {
//				NumberField numberField = new NumberField();
//	            numberField.setAllowBlank(false);
//	            numberField.setAllowNegative(false);
//	            config.setAlign(TextAlign.CENTER);
//	            config.setEditor(new GridEditor(numberField));
//	            config.setCss("border-style: solid; border-width: 1px 1px 1px 1px;border-color: blue");
//			} else {
//				config.setAlign(TextAlign.CENTER);
//				config.setRenderer(new Renderer(){
//					public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
//						if(value == null)
//							return "-";
//						String displayValue = value.toString();
//						return displayValue;
//					}
//				});
//			}
//			configs[i + 1] = config;
//		}
//		return new ColumnModel(configs);
	}

	protected void addGridRowListener() {
		gridPanel.addGridRowListener(new GridRowListenerAdapter() {
			public void onRowClick(GridPanel grid, int rowIndex,
				EventObject e) {
				e.stopEvent();
				moveDocDetailId = null;
				if(grid.getSelectionModel().hasSelection()) {
					moveDocDetailId = Long.valueOf(grid.getSelectionModel().getSelected().getAsString("id"));
				}
				findAvailabeInventoryInfo(lotInfoCheckBox.getValue(), containLockInvCheckBox.getValue());
			}
			
			public void onRowDblClick(GridPanel grid, int rowIndex,
				EventObject e) {
				e.stopEvent();
				grid.getSelectionModel().clearSelections();
				moveDocDetailId = null;
				findAvailabeInventoryInfo(lotInfoCheckBox.getValue(), containLockInvCheckBox.getValue());
			}
		});
	}
	
	protected void showGridMenu(EventObject e) {
		if (menu == null) {
			menu = rightContextMenu.getDetailGridMenu();
		}
		updateGridMenuItemStatus();
		menu.showAt(e.getXY());
	}
	
	protected void updateGridMenuItemStatus() {
		this.menu.getMenuItems()[0].setVisible(true);
		this.menu.getMenuItems()[0].setDisabled(false);
//		int selectCount = grid.getSelectionModel().getCount();
//		if(selectCount == 1) {
//			this.menu.getMenuItems()[0].setVisible(true);
//			this.menu.getMenuItems()[0].setDisabled(false);
//		} else {
//			this.menu.getMenuItems()[0].setVisible(true);
//			this.menu.getMenuItems()[0].setDisabled(true);
//		}
	}
	
	public void doDispath(String message){
		if(AllocateMessageConstant.MSG_INIT_PAGE.equals(message)) {
			reloadDetails();
		} else if(AllocateMessageConstant.MSG_MOVE_DOC_DETAIL_DATA_CHANGE.equals(message)) {
			if(getData().getMoveDocDetailData() == null) {
				reset();
			} else {
				reloadDetails();
			}
		} else if(AllocateMessageConstant.MSG_INIT_MOVE_DOC_DETAIL_PAGE.equals(message)) {
			if(getData().getMoveDocDetailData() == null) {
				reset();
			} else {
				reloadDetails();
			}
		}
	}
	
	private void reloadDetails() {
		List<CustomMoveDocDetail> customDetails = (List<CustomMoveDocDetail>)getData().getMoveDocDetailData().get(AllocateConstant.DETAILS_RESULT);
		Object[][] data = new Object[customDetails.size()][];
		for(int i = 0; i < customDetails.size(); i++) {
			data[i] = customDetails.get(i).toArray();
		}
		proxy = new MemoryProxy(data);
		store.setDataProxy(proxy);
		store.reload();
	}
	
	private void initToolbar() {
		toolbar = new Toolbar();

        lotInfoCheckBox = new Checkbox(LocaleUtils.getText("pickTicketDetailsPanel.lotInfoCheckBox"));
        lotInfoCheckBox.setChecked(Boolean.TRUE);
        containLockInvCheckBox = new Checkbox(LocaleUtils.getText("pickTicketDetailsPanel.containLockInvCheckBox"));
        containLockInvCheckBox.setChecked(Boolean.FALSE);
        
        lotInfoCheckBox.addListener(new CheckboxListenerAdapter() {
        	public void onCheck(Checkbox field, boolean checked) {
        		if(gridPanel.getSelectionModel().getSelections() != null  
		            && gridPanel.getSelectionModel().getSelections().length > 0 && checked) {
        			findAvailabeInventoryInfo(lotInfoCheckBox.getValue(), containLockInvCheckBox.getValue());
        		}
        	}
        });
        containLockInvCheckBox.addListener(new CheckboxListenerAdapter() {
        	public void onCheck(Checkbox field, boolean checked) {
				if(gridPanel.getSelectionModel().getSelections() != null 
		            && gridPanel.getSelectionModel().getSelections().length > 0) { 
        			findAvailabeInventoryInfo(lotInfoCheckBox.getValue(), containLockInvCheckBox.getValue());
				}
			}
		});
        
        toolbar.addField(lotInfoCheckBox);
        toolbar.addField(containLockInvCheckBox);
	}
	
	private void findAvailabeInventoryInfo(boolean isFitAsLot, boolean containLockInv) {
		final Map<String, Object> params = new HashMap<String, Object>();
		boolean isClear = Boolean.TRUE;
		if(moveDocDetailId != null) {
			isClear = Boolean.FALSE;
		}
		params.put(AllocateConstant.ID, moveDocDetailId);
		params.put(AllocateConstant.IS_FIT_AS_LOT, isFitAsLot);
		params.put(AllocateConstant.CONTAIN_LOCK_INV, containLockInv);
		params.put(AllocateConstant.IS_CLEAR_INVENTORIES, isClear);
		((AllocateDataAccessor)getData()).findAvailabeInventories(params);
	}
	
	@SuppressWarnings("unused")
	private void autoAllocate() {
		final Map<String, Object> params = new HashMap<String, Object>();
		CustomMoveDoc customMoveDoc = ((AllocateDataAccessor)getData()).getCustomMoveDocByDetail();
		params.put(AllocateConstant.CLIENT_ENTITY, customMoveDoc);
		((AllocateDataAccessor)getData()).autoAllocate(params);
	}
	
	public BaseItemListenerAdapter genAvailableInventoriesListenerAdapter() {
		return new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
//				findAvailabeInventoryInfo(lotInfoCheckBox.getValue(), containLockInvCheckBox.getValue());
			}
		};
	}
	
	public BaseItemListenerAdapter genSearchListenerAdapter() {
		return new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				dialog = new DialogBoxTemplate(LocaleUtils.getText("manualPickingAllocatePage.pickTicketDetailsPanel.search"), 
					new ManualAllocateSearchPanel(PickTicketDetailsPanel.this));
				int positionX = (Window.getClientWidth() - 430) / 2;
				int positionY = (Window.getClientHeight() - 450) / 2;
				dialog.setPopupPosition(positionX , positionY);
				dialog.show();
			}
		};
	}
}
