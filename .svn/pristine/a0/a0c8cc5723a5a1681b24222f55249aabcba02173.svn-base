package com.vtradex.wms.client.ui.page;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.GridRowListener;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;
import com.vtradex.wms.client.ui.data.Page_PA_DataAccessor;
import com.vtradex.wms.client.ui.javabean.PT_AVAILABLE;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.thorn.client.ui.page.IPage;
import com.vtradex.thorn.client.ui.panel.AbstractSupportPanel;
import com.vtradex.thorn.client.utils.LocaleUtils;

@SuppressWarnings("unused")
public class Sub_AvailableTable_Panel extends AbstractSupportPanel {
	protected final static String DEFAULT_NAME = "pickTicketAvailable_Panel";
	transient EditorGridPanel availableTable;
	private RecordDef recordDef;
	private ColumnModel columnModel;
	private Store store;
	private MemoryProxy proxy;
	private ArrayReader reader;
	transient ToolbarButton manualAllocateButton;
	private String flag;
    private static final DateTimeFormat dateFormater = DateTimeFormat.getFormat("yyyy-MM-dd");
	

	public Sub_AvailableTable_Panel(IMessagePage page) {
		super(page, DEFAULT_NAME);
		this.drawWidget();
	}

	private void drawWidget() {
		flag = curPADataAccessor().getFlag();
		recordDef = new RecordDef(new FieldDef[] { 
				 new IntegerFieldDef("inventory.id"), 
				 new StringFieldDef("location.code"), 			 			 			 		
				 new StringFieldDef("item.code"), 
				 new StringFieldDef("item.name"), 
				 new StringFieldDef("packageUnit.unit"), 
				 new IntegerFieldDef("packageUnit.convertFigure"), 
				 new FloatFieldDef("inventory.quantity"),
				 new FloatFieldDef("inventory.availableQuantity"), 
				 new FloatFieldDef("inventory.manualQuantity"),
				 new StringFieldDef("inventory.status"),
				 new StringFieldDef("itemKey.lot"),
				 new StringFieldDef("lotInfor"),
				 new StringFieldDef("lotInfo.storageDate"),
				 new StringFieldDef("lotInfo.soi"),				 
				 new StringFieldDef("lotInfo.batchNum"),				 
				 new StringFieldDef("lotInfo.produceDate"),				 
				 new StringFieldDef("lotInfo.expireDate"),				 
				 new StringFieldDef("lotInfo.warnDate"),				 
				 new StringFieldDef("lotInfo.supplier")				 
				 }); 			
		 
			ColumnConfig iiColumn = new ColumnConfig(LocaleUtils.getText("inventory.id"), "inventory.id"); 
			iiColumn.setHidden(true);
		 
			ColumnConfig lcColumn = new ColumnConfig(LocaleUtils.getText("location.code"), "location.code", 80); 
			lcColumn.setAlign(TextAlign.CENTER);
			lcColumn.setSortable(true);
		 
			ColumnConfig icColumn = new ColumnConfig(LocaleUtils.getText("item.code"), "item.code", 100); 
			icColumn.setAlign(TextAlign.CENTER); 

			ColumnConfig inColumn = new ColumnConfig(LocaleUtils.getText("item.name"), "item.name", 200); 
			inColumn.setAlign(TextAlign.CENTER); 

			ColumnConfig puColumn = new ColumnConfig(LocaleUtils.getText("packageUnit.unit"), "packageUnit.unit", 80); 
			puColumn.setAlign(TextAlign.CENTER); 

			ColumnConfig pcColumn = new ColumnConfig(LocaleUtils.getText("packageUnit.convertFigure"), "packageUnit.convertFigure", 80); 
			pcColumn.setAlign(TextAlign.CENTER); 

			ColumnConfig iqColumn = new ColumnConfig(LocaleUtils.getText("inventory.quantity"), "inventory.quantity", 80); 
			iqColumn.setAlign(TextAlign.CENTER); 
		 
			ColumnConfig iaColumn = new ColumnConfig(LocaleUtils.getText("inventory.availableQuantity"), "inventory.availableQuantity", 80); 
			iaColumn.setAlign(TextAlign.CENTER); 
		 
            NumberField numberField = new NumberField();
            numberField.setAllowBlank(false);
            numberField.setAllowNegative(false);
			
			ColumnConfig imColumn = new ColumnConfig(LocaleUtils.getText("inventory.manualQuantity"), "inventory.manualQuantity", 80); 
			imColumn.setAlign(TextAlign.CENTER);
			imColumn.setEditor(new GridEditor(numberField));
			imColumn.setCss("border-style: solid; border-width: 1px 1px 1px 1px;border-color: blue");

			ColumnConfig statusColumn = new ColumnConfig(LocaleUtils.getText("inventory.status"), "inventory.status", 80); 
			statusColumn.setAlign(TextAlign.CENTER); 
			
			ColumnConfig ilColumn = new ColumnConfig(LocaleUtils.getText("itemKey.lot"), "itemKey.lot", 100); 
			ilColumn.setAlign(TextAlign.CENTER); 

			ColumnConfig lColumn = new ColumnConfig(LocaleUtils.getText("lotInfor"), "lotInfor", 150); 
			lColumn.setAlign(TextAlign.CENTER);
			
			ColumnConfig isColumn = new ColumnConfig(LocaleUtils.getText("lotInfo.storageDate"), "lotInfo.storageDate", 150); 
			isColumn.setAlign(TextAlign.CENTER);
			isColumn.setSortable(true);
		 
			ColumnConfig iSColumn = new ColumnConfig(LocaleUtils.getText("lotInfo.soi"),"lotInfo.soi", 100); 
			iSColumn.setAlign(TextAlign.CENTER);
			
			ColumnConfig lbColumn = new ColumnConfig(LocaleUtils.getText("lotInfo.batchNum"),"lotInfo.batchNum", 100); 
			lbColumn.setAlign(TextAlign.CENTER);
			
			ColumnConfig lpColumn = new ColumnConfig(LocaleUtils.getText("lotInfo.produceDate"),"lotInfo.produceDate", 100); 
			lpColumn.setAlign(TextAlign.CENTER);
			
			ColumnConfig leColumn = new ColumnConfig(LocaleUtils.getText("lotInfo.expireDate"),"lotInfo.expireDate", 100); 
			leColumn.setAlign(TextAlign.CENTER);
			
			ColumnConfig lwColumn = new ColumnConfig(LocaleUtils.getText("lotInfo.warnDate"),"lotInfo.warnDate", 100); 
			lwColumn.setAlign(TextAlign.CENTER);
			
			ColumnConfig lspColumn = new ColumnConfig(LocaleUtils.getText("lotInfo.supplier"),"lotInfo.supplier", 100); 
			lspColumn.setAlign(TextAlign.CENTER);
		 
		 	columnModel = new ColumnModel(new ColumnConfig[]{ 
				 iiColumn, lcColumn, icColumn, inColumn, puColumn, pcColumn,
				 iqColumn, iaColumn, imColumn, statusColumn, ilColumn, lColumn, isColumn, iSColumn,
				 lbColumn, lpColumn, leColumn, lwColumn, lspColumn
				 });
		 	
			proxy = new MemoryProxy(new String[][]{}); 
			
			reader = new ArrayReader(recordDef); 
			
			store = new Store(proxy, reader);
			store.load();
			
			availableTable = new EditorGridPanel();
			availableTable.setAutoScroll(true);
			availableTable.setStore(store); 							 		 		 			 	
			availableTable.setColumnModel(columnModel);
			availableTable.setClicksToEdit(1);
			availableTable.setSelectionModel(new RowSelectionModel(false));
			
            ButtonListenerAdapter listener = new ButtonListenerAdapter() {
                public void onClick(Button button, EventObject e) {
                	manualAllocate();
                }
            };
			
            manualAllocateButton = new ToolbarButton("部分分配", listener);
            manualAllocateButton.setDisabled(true);
			
			availableTable.addGridRowListener(new GridRowListener() {
				public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
					manualAllocateButton.setDisabled(Boolean.FALSE);
				}

				public void onRowContextMenu(GridPanel grid, int rowIndex, EventObject e) {
				}

				public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
				}
			});
			
			Panel panel = new Panel();
			panel.add(manualAllocateButton);
			
			this.setLayout(new RowLayout());
			this.add(availableTable, new RowLayoutData("88%"));
			this.add(panel, new RowLayoutData("12%"));
	}
	
	//部分分配	
	@SuppressWarnings("unchecked")
	private void manualAllocate() {
		final Map param = new HashMap();
		if(flag.equals("pickTicketPage")){
			//存放发货单明细ID，以便部分分配后刷新信息
			Long pickTicketDetailId = this.curPADataAccessor().getPtdId();
			param.put("pickTicketDetailId", pickTicketDetailId);
		}else if(flag.equals("waveDocPage")){
			//存放波次明细ID，以便部分分配后刷新信息
			Long waveDocDetailId = this.curPADataAccessor().getWddId();
			param.put("waveDocDetailId", waveDocDetailId);
		}else if(flag.equals("processDocPage")) {
			//存放加工单明细ID，以便部分分配后刷新信息
			Long processDocDetailId = this.curPADataAccessor().getPrdId();
			param.put("processDocDetailId", processDocDetailId);
		}
		
		final Map subParam = new HashMap();

		for (int i = 0; i < availableTable.getStore().getCount(); i++) { 
			if (availableTable.getSelectionModel().isSelected(i)) {
				Long inventoryId = Long.valueOf(availableTable.getStore().getAt(i).getAsString("inventory.id"));
				
				Double availableQuantity = Double.valueOf(availableTable.getStore().getAt(i).getAsString("inventory.availableQuantity"));
				Double manualQuantity = Double.valueOf(availableTable.getStore().getAt(i).getAsString("inventory.manualQuantity"));
				
				if (manualQuantity.doubleValue() > availableQuantity.doubleValue()) {
					Window.alert(LocaleUtils.getText("availableQuantity.not.enough"));
					return;
				}
				
				subParam.put(inventoryId, manualQuantity);
			}
		}
		
		param.put(IPage.TABLE_INPUT_VALUES, subParam);
		if(flag.equals("pickTicketPage")){
			//发货单部分分配
			this.curPADataAccessor().manualAllocate(param);
		}else if(flag.equals("waveDocPage")){
			//波次部分分配
			this.curPADataAccessor().manualWaveDocAllocate(param);
		}else if(flag.equals("processDocPage")) {
			//加工单部分分配
			this.curPADataAccessor().manualProcessDocAllocate(param);
		}
	}	
	
	public Page_PA_DataAccessor curPADataAccessor() {
		return (Page_PA_DataAccessor)super.getData();
	}	
	
	public void doDispath(String message){
		super.doDispath(message);
		
		if (Page_PA_DataAccessor.INIT_AVAILABLE_INFO.equals(message)|| Page_PA_DataAccessor.INIT_WD_AVAILABLE_INFO.equals(message)
				|| Page_PA_DataAccessor.INIT_AVAILABLE_PROCESS_INFO.equals(message)) {
			Object[][] data = new Object[this.curPADataAccessor().all_PTAS().size()][];
				for (int i = 0; i < this.curPADataAccessor().all_PTAS().size(); i++) {
					data[i] = toArray(this.curPADataAccessor().all_PTAS().get(i));
				}
		
				proxy = new MemoryProxy(data);
				
				store.setDataProxy(proxy);
				store.reload();
				
				availableTable.reconfigure(store, columnModel);
		} else if (Page_PA_DataAccessor.AUTO_ALLOCATE_INFO.equals(message)
				|| Page_PA_DataAccessor.SINGLE_CANCEL_ALLOCATE_INFO.equals(message) 
				|| Page_PA_DataAccessor.ALL_CANCEL_ALLOCATE_INFO.equals(message)
				|| Page_PA_DataAccessor.MANUAL_ALLOCATE_INFO.equals(message)
				|| Page_PA_DataAccessor.AUTO_WD_ALLOCATE_INFO.equals(message)
				|| Page_PA_DataAccessor.CANCELL_WD_ALLOCATE_INFO.equals(message)
				|| Page_PA_DataAccessor.MANUAL_WD_ALLOCATE_INFO.equals(message)
				|| Page_PA_DataAccessor.AUTO_PROCESSDOC_ALLOCATE_INFO.equals(message)
				|| Page_PA_DataAccessor.SINGLE_CANCEL_ALLOCATE_INFO.equals(message)
				|| Page_PA_DataAccessor.ALL_CANCEL_PROCESSDOC_ALLOCATE_INFO.equals(message)) {
			proxy = new MemoryProxy(new String[][]{});
			
			store.setDataProxy(proxy);
			store.reload();
			
			availableTable.reconfigure(store, columnModel);
		}
	}
	
	public Object[] toArray(PT_AVAILABLE available){
		return new Object[]{available.getInventoryId(),
				available.getLocationCode(),
				available.getItemCode(),
				available.getItemName(),
				available.getPackageUnit(),
				available.getPackageUnitConvertFigure(),
				available.getInventoryQuantity(),
				available.getInventoryAvailableQuantity(),
				available.getInventoryManualQuantity(),
				available.getStatus(),
				available.getItemKeyLot(),
				available.getLotInfor(),
				available.getStorageDate() == null ? "" : dateFormater.format(available.getStorageDate()).toString(),
				available.getSoi(),
				available.getBatchNum(),
				available.getProduceDate() == null ? "" : dateFormater.format(available.getProduceDate()).toString(),
			    available.getExpireDate() == null ? "" : dateFormater.format(available.getExpireDate()).toString(),
			    available.getWarnDate() == null ? "" : dateFormater.format(available.getWarnDate()).toString(),
				available.getSupplier()};
	}
}