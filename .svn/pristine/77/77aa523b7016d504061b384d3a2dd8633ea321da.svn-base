package com.vtradex.wms.client.ui.page;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.thorn.client.ui.page.IPage;
import com.vtradex.thorn.client.ui.panel.AbstractSupportPanel;
import com.vtradex.thorn.client.utils.LocaleUtils;
import com.vtradex.wms.client.ui.data.Page_PA_DataAccessor;
import com.vtradex.wms.client.ui.javabean.PT_ALLOCATED;

@SuppressWarnings({"unchecked"})
public class Sub_AllocatedTable_Panel extends AbstractSupportPanel {
	protected final static String DEFAULT_NAME = "pickTicketAllocated_Panel";
	
	private static final DateTimeFormat dateFormater = DateTimeFormat.getFormat("yyyy-MM-dd");
	
	transient EditorGridPanel allocatedTable;
	
	private RecordDef recordDef;
	private ColumnModel columnModel;
	private Store store1;
	private MemoryProxy proxy1;
	private ArrayReader reader;
	
	transient Button singleCancelAllocate;
	transient Button allCancelAllocate;
	
	private String flag;
	
	public Sub_AllocatedTable_Panel(IMessagePage page) {
		super(page, DEFAULT_NAME);
		this.drawWidget();
	}
	
	private void drawWidget() {	
		flag = curPADataAccessor().getFlag();
		
		recordDef = new RecordDef(new FieldDef[] { 
				 new IntegerFieldDef("task.id"), 
				 new StringFieldDef("location.code"), 			 			 			 		
				 new StringFieldDef("item.code"), 
				 new StringFieldDef("item.name"), 
				 new StringFieldDef("task.packageUnit"),
				 new FloatFieldDef("task.planQuantity"), 
				 new FloatFieldDef("task.planQuantityBU"),
				 new FloatFieldDef("pickedRecord.manualQuantity"), 
				 new StringFieldDef("lotInfor"),
				 new StringFieldDef("task.storageDate"), 
				 new StringFieldDef("task.soi"),				 
				 new StringFieldDef("task.batchNum"),				 
				 new StringFieldDef("task.produceDate"),				 
				 new StringFieldDef("task.expireDate"),				 
				 new StringFieldDef("task.warnDate"),				 
				 new StringFieldDef("task.supplier")				 
				 }); 			
		 
			ColumnConfig tiColumn = new ColumnConfig(LocaleUtils.getText("task.id"), "task.id"); 
			
			tiColumn.setHidden(true);
		 
			ColumnConfig lcColumn = new ColumnConfig(LocaleUtils.getText("location.code"), "location.code", 150); 
			
			lcColumn.setAlign(TextAlign.CENTER);
			lcColumn.setSortable(true);
		 
			ColumnConfig icColumn = new ColumnConfig(LocaleUtils.getText("item.code"), "item.code", 150); 
			
			icColumn.setAlign(TextAlign.CENTER);
			icColumn.setSortable(true);

			ColumnConfig inColumn = new ColumnConfig(LocaleUtils.getText("item.name"), "item.name", 200); 
			
			inColumn.setAlign(TextAlign.CENTER); 

			ColumnConfig tpColumn = new ColumnConfig(LocaleUtils.getText("task.packageUnit"), "task.packageUnit", 80); 
			
			tpColumn.setAlign(TextAlign.CENTER); 

			ColumnConfig tp1Column = new ColumnConfig(LocaleUtils.getText("task.planQuantity"), "task.planQuantity", 100); 
			
			tp1Column.setAlign(TextAlign.CENTER); 
			
			ColumnConfig tp2Column = new ColumnConfig(LocaleUtils.getText("task.planQuantityBU"), "task.planQuantityBU", 150); 
			
			tp2Column.setAlign(TextAlign.CENTER);

            NumberField numberField = new NumberField();
            
            numberField.setAllowBlank(false);
            numberField.setAllowNegative(false);
			
			ColumnConfig pmColumn = new ColumnConfig(LocaleUtils.getText("pickedRecord.manualQuantity"), "pickedRecord.manualQuantity", 100); 
			
			pmColumn.setAlign(TextAlign.CENTER);
			pmColumn.setEditor(new GridEditor(numberField));
			pmColumn.setCss("border-style: solid; border-width: 1px 1px 1px 1px;border-color: blue");

			ColumnConfig lColumn = new ColumnConfig(LocaleUtils.getText("lotInfor"), "lotInfor", 150); 
			
			lColumn.setAlign(TextAlign.CENTER);
			
			ColumnConfig tsColumn = new ColumnConfig(LocaleUtils.getText("task.storageDate"), "task.storageDate", 150); 
			
			tsColumn.setAlign(TextAlign.CENTER);
			tsColumn.setSortable(true);
		 
			ColumnConfig tSOColumn = new ColumnConfig(LocaleUtils.getText("task.soi"),"task.soi", 100); 
			tSOColumn.setAlign(TextAlign.CENTER);
			
			ColumnConfig tBColumn = new ColumnConfig(LocaleUtils.getText("task.batchNum"),"task.batchNum", 100); 
			tBColumn.setAlign(TextAlign.CENTER);
			
			ColumnConfig tPDColumn = new ColumnConfig(LocaleUtils.getText("task.produceDate"),"task.produceDate", 100); 
			tPDColumn.setAlign(TextAlign.CENTER);
			tPDColumn.setSortable(true);
			
			ColumnConfig tEColumn = new ColumnConfig(LocaleUtils.getText("task.expireDate"),"task.expireDate", 100); 
			tEColumn.setAlign(TextAlign.CENTER);
			tEColumn.setSortable(true);
			
			ColumnConfig tWColumn = new ColumnConfig(LocaleUtils.getText("task.warnDate"),"task.warnDate", 100); 
			tWColumn.setAlign(TextAlign.CENTER);
			tWColumn.setSortable(true);
			
			ColumnConfig tSPColumn = new ColumnConfig(LocaleUtils.getText("task.supplier"),"task.supplier", 100); 
			tSPColumn.setAlign(TextAlign.CENTER);
		 
		 	columnModel = new ColumnModel(new ColumnConfig[]{ 
		 			tiColumn, lcColumn, icColumn, inColumn, tpColumn, tp1Column,
		 			tp2Column, pmColumn, lColumn, tsColumn, tSOColumn, tBColumn,
		 			tPDColumn, tEColumn, tWColumn, tSPColumn
				 });
		 	
			proxy1 = new MemoryProxy(new String[][]{}); 
			
			reader = new ArrayReader(recordDef); 
			
			store1 = new Store(proxy1, reader);
			store1.load();
			
			allocatedTable = new EditorGridPanel();
            allocatedTable.setLayout(new RowLayout());
			allocatedTable.setAutoScroll(true);
			allocatedTable.setStore(store1);
			allocatedTable.setClicksToEdit(1);
			allocatedTable.setSelectionModel(new RowSelectionModel(false));
			allocatedTable.setColumnModel(columnModel);			

            ButtonListenerAdapter singleCancelListener = new ButtonListenerAdapter() {
                public void onClick(Button button, EventObject e) {
                	singleCancelAllocate();
                }
            };
            
            singleCancelAllocate = new Button("单一取消", singleCancelListener);
            singleCancelAllocate.setDisabled(Boolean.TRUE);
			
			allocatedTable.addGridRowListener(new GridRowListener() {
				public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
					singleCancelAllocate.setDisabled(Boolean.FALSE);
				}

				public void onRowContextMenu(GridPanel grid, int rowIndex, EventObject e) {
				}

				public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
				}
			});
			
			ButtonListenerAdapter allCancelListener = new ButtonListenerAdapter() {
				public void onClick(Button button, EventObject e) {
					allCancelAllocate();
                }
			};
			
			allCancelAllocate = new Button("整单取消", allCancelListener);
			allCancelAllocate.setDisabled(Boolean.FALSE);
			
			allocatedTable.addGridRowListener(new GridRowListener() {
				public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				}

				public void onRowContextMenu(GridPanel grid, int rowIndex, EventObject e) {
				}

				public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
				}
			});
			
			HorizontalPanel panel = new HorizontalPanel();
			
			panel.add(allCancelAllocate);
			panel.add(singleCancelAllocate);
			
			panel.setCellWidth(allCancelAllocate, "120px");
			
		 	this.setLayout(new RowLayout());
			this.add(allocatedTable, new RowLayoutData("94%"));
			this.add(panel, new RowLayoutData("6%"));
	}
	
	/**
	 * 发货单单一明细取消分配
	 */
	private void singleCancelAllocate() {
		final Map param = new HashMap();
		
		if(flag.equals("pickTicketPage")){
			//存放发货单ID，以便取消分配后刷新信息
			Long pickTicketId = this.curPADataAccessor().currentPickTicket().getId();
			param.put("pickTicketId", pickTicketId);
		}else if(flag.equals("waveDocPage")){
			//存放波次ID，以便取消分配后刷新信息
			Long waveDocId = this.curPADataAccessor().currentPickTicket().getId();
			param.put("waveDocId", waveDocId);
		}else if(flag.equals("processDocPage")) {
			Long processDocId = this.curPADataAccessor().currentPickTicket().getId();
			param.put("processDocId", processDocId);
		}
			
		final Map subParam = new HashMap();

		for (int i = 0; i < allocatedTable.getStore().getCount(); i++) { 
			if (allocatedTable.getSelectionModel().isSelected(i)) {
				Long taskId = Long.valueOf(allocatedTable.getStore().getAt(i).getAsString("task.id"));
				
				Double availableQuantity = Double.valueOf(allocatedTable.getStore().getAt(i).getAsString("task.planQuantityBU"));
				Double manualQuantity = Double.valueOf(allocatedTable.getStore().getAt(i).getAsString("pickedRecord.manualQuantity"));
				
				if (manualQuantity.doubleValue() > availableQuantity.doubleValue()) {
					Window.alert(LocaleUtils.getText("pickedQuantity.not.enough"));
					
					return;
				}
				
				subParam.put(taskId, manualQuantity);
			}
		}
		
		param.put(IPage.TABLE_INPUT_VALUES, subParam);
		
		if(flag.equals("pickTicketPage")){
			//发货单取消分配
			this.curPADataAccessor().singleCancelAllocate(param);
		}else if(flag.equals("waveDocPage")){
			//波次取消分配
			this.curPADataAccessor().singleCancellWaveDocAllocate(param);
		}else if(flag.equals("processDocPage")) {
			this.curPADataAccessor().singleCancelProcessDocAllocate(param);
		}
		
	}
	
	/**
	 * 发货单整单取消分配
	 */
	private void allCancelAllocate() {
		final Map param = new HashMap();
		if(flag.equals("pickTicketPage")){
			//存放发货单ID，以便取消分配后刷新信息
			Long pickTicketId = this.curPADataAccessor().currentPickTicket().getId();
			param.put("pickTicketId", pickTicketId);
			this.curPADataAccessor().allCancelAllocate(param);
		}else if(flag.equals("waveDocPage")){
			//存放波次ID，以便取消分配后刷新信息
			Long waveDocId = this.curPADataAccessor().currentPickTicket().getId();
			param.put("waveDocId", waveDocId);
			this.curPADataAccessor().cancellWaveDocAllocate(param);
		}else if(flag.equals("processDocPage")){
			//存放加工单ID，以便取消分配后刷新信息
			Long processDocId = this.curPADataAccessor().currentPickTicket().getId();
			param.put("processDocId", processDocId);
			this.curPADataAccessor().allCancelProcessDocAllocate(param);
		}
	}
	
	public Page_PA_DataAccessor curPADataAccessor() {
		return (Page_PA_DataAccessor)super.getData();
	}	
	
	public void doDispath(String message) {
		super.doDispath(message);
		
		if (Page_PA_DataAccessor.INIT_ALLOCATED_INFO.equals(message) || Page_PA_DataAccessor.INIT_WD_ALLOCATED_INFO.equals(message) 
				|| Page_PA_DataAccessor.INIT_ALLOCATED_PROCESS_DOCS_INFO.equals(message)) {
			Object[][] data = new Object[this.curPADataAccessor().all_PTAD().size()][];
				for (int i = 0;i < this.curPADataAccessor().all_PTAD().size(); i++) {
					data[i] = toArray(this.curPADataAccessor().all_PTAD().get(i));
				}
				
				proxy1 = new MemoryProxy(data);
				
				store1.setDataProxy(proxy1);
				store1.reload();
				
				allocatedTable.reconfigure(store1, columnModel);
		}
	}
	
	public Object[] toArray(PT_ALLOCATED allocated) {
		return new Object[]{allocated.getTaskId(),
				allocated.getLocationCode(),
				allocated.getItemCode(),
				allocated.getItemName(),
				allocated.getPackageUnit(),
				allocated.getPlanQuantity(),
				allocated.getPlanQuantityBU(),
				allocated.getManualQuantity(),
				allocated.getLotInfor(),
				allocated.getStorageDate() == null ? "" : dateFormater.format(allocated.getStorageDate()).toString(),
				allocated.getSoi(),
				allocated.getBatchNum(),
				allocated.getProduceDate() == null ? "" : dateFormater.format(allocated.getProduceDate()).toString(),
			    allocated.getExpireDate() == null ? "" : dateFormater.format(allocated.getExpireDate()).toString(),
			    allocated.getWarnDate() == null ? "" : dateFormater.format(allocated.getWarnDate()).toString(),
				allocated.getSupplier()
				};		
	}
}