package com.vtradex.wms.client.ui.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
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
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
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

@SuppressWarnings("unchecked")
public class Sub_DetailsTable_Panel extends AbstractSupportPanel {
	private final static String DEFAULT_NAME = "pickTicketDetails_Panel";
	
	transient GridPanel detailsTable;
	
	private RecordDef recordDef;
	private ColumnModel columnModel;
	private Store store;
	private MemoryProxy proxy;
	private ArrayReader reader;
	private String flag;
	
	transient ToolbarButton autoAllocateButton;
	
	private ColumnConfig ptiColumn;
	private ColumnConfig iiColumn;
	private ColumnConfig pcColumn;
	private ColumnConfig pkuiColumn;
	private ColumnConfig icColumn;
	private ColumnConfig inColumn;
	private ColumnConfig puColumn;
	private ColumnConfig pp1Column;
	private ColumnConfig paColumn;
	private ColumnConfig pp2Column;
	private ColumnConfig psColumn;
	private ColumnConfig lColumn;
	
	transient CheckBox lotInfoCheckBox, containLockInv;
	
	private Long itemId;
	private Integer convertFigure;
	private String storageDate;
	private String soi;
	private String batchNum;
	private String produceDate;
	private String expireDate;
	private String warnDate;
	private String supplier; 
	
	public Sub_DetailsTable_Panel(IMessagePage page) {
		super(page, DEFAULT_NAME);
		this.drawWidget();
	}
	
	private void innitAvailableDatas(Long itemId,Integer convertFigure,String storageDate,String soi,
			String batchNum,String produceDate,String expireDate,
			String warnDate,String supplier,boolean isFitAsLot,boolean containLockInv){
		final Map<String, Object> param = new HashMap<String, Object>();

		param.put("itemId", itemId);
		param.put("convertFigure", convertFigure);
		param.put("containLockInv", containLockInv);
		param.put("isFitAsLot", isFitAsLot);

		if (isFitAsLot) {
			param.put("storageDate", storageDate);
			param.put("soi", soi);
			param.put("batchNum", batchNum);
			param.put("produceDate", produceDate);
			param.put("expireDate", expireDate);
			param.put("warnDate", warnDate);
			param.put("supplier", supplier);
		}
		if(flag.equals("pickTicketPage")){
			//查询对应发货单明细的库存信息
			this.curPADataAccessor().initAvailableInfo(param);
		}else if(flag.equals("waveDocPage")){
			//查询对应波次明细的库存信息
			this.curPADataAccessor().initWaveDocAvailableInfo(param);
		}else if(flag.equals("processDocPage")) {
			//查询对应加工单明细的库存信息
			this.curPADataAccessor().initAvailableProcessInfo(param);
		}
	}
		
	private void getDatasFromGrid(GridPanel grid, int rowIndex, boolean isFitAsLot, boolean containLockInv) {
		  itemId = Long.valueOf(grid.getStore().getAt(rowIndex).getAsString("item.id"));
		   convertFigure = grid.getStore().getAt(rowIndex).getAsInteger("packageUnit.convertFigure");
		  storageDate = grid.getStore().getAt(rowIndex).getAsString("storageDate");
		  soi = grid.getStore().getAt(rowIndex).getAsString("soi");
		  batchNum = grid.getStore().getAt(rowIndex).getAsString("batchNum");
		  produceDate = grid.getStore().getAt(rowIndex).getAsString("produceDate");
		  expireDate = grid.getStore().getAt(rowIndex).getAsString("expireDate");
		  warnDate = grid.getStore().getAt(rowIndex).getAsString("warnDate");
		  supplier = grid.getStore().getAt(rowIndex).getAsString("supplier");
		 innitAvailableDatas(itemId,convertFigure,storageDate,soi,
					batchNum,produceDate,expireDate,
					warnDate,supplier,isFitAsLot,containLockInv);
		
	}

	protected void drawWidget() {	
		flag = curPADataAccessor().getFlag();
		recordDef = new RecordDef(new FieldDef[] { 
				 new IntegerFieldDef("pickTicketDetail.id"), 
				 new IntegerFieldDef("item.id"), 
				 new IntegerFieldDef("packageUnit.id"), 
				 new StringFieldDef("packageUnit.convertFigure"),			 			 			 		
				 new StringFieldDef("item.code"), 
				 new StringFieldDef("item.name"), 
				 new StringFieldDef("packageUnit.unit"), 
				 new FloatFieldDef("pickTicketDetail.expectedQuantityBU"), 
				 new FloatFieldDef("pickTicketDetail.allocatedQuantityBU"),
				 new FloatFieldDef("pickTicketDetail.pickedQuantityBU"), 
				 new FloatFieldDef("pickTicketDetail.shippedQuantityBU"),
				 new StringFieldDef("shipLotInfo"),
				 new StringFieldDef("storageDate"),
				 new StringFieldDef("soi"),
				 new StringFieldDef("batchNum"),
				 new StringFieldDef("serialNo"),
				 new StringFieldDef("produceDate"),
				 new StringFieldDef("expireDate"),
				 new StringFieldDef("warnDate"),
				 new StringFieldDef("supplier"),
				 }); 			
		 
			ptiColumn = new ColumnConfig(LocaleUtils.getText("pickTicketDetail.id"), "pickTicketDetail.id"); 
			ptiColumn.setHidden(true);
		 
			iiColumn = new ColumnConfig(LocaleUtils.getText("item.id"), "item.id"); 
			iiColumn.setHidden(true);
			
			pkuiColumn = new ColumnConfig(LocaleUtils.getText("packageUnit.id"), "packageUnit.id"); 
			pkuiColumn.setHidden(true);	 
		 
			pcColumn = new ColumnConfig(LocaleUtils.getText("packageUnit.convertFigure"), "packageUnit.convertFigure"); 
			pcColumn.setHidden(true);
		 
			icColumn = new ColumnConfig(LocaleUtils.getText("item.code"), "item.code", 150, false); 
			icColumn.setAlign(TextAlign.CENTER);
			icColumn.setSortable(true);

			inColumn = new ColumnConfig(LocaleUtils.getText("item.name"), "item.name", 200, false); 
			inColumn.setAlign(TextAlign.CENTER); 

			puColumn = new ColumnConfig(LocaleUtils.getText("packageUnit.unit"), "packageUnit.unit", 80, false); 
			puColumn.setAlign(TextAlign.CENTER); 

			pp1Column = new ColumnConfig(LocaleUtils.getText("pickTicketDetail.expectedQuantityBU"), "pickTicketDetail.expectedQuantityBU", 100, false); 
			pp1Column.setAlign(TextAlign.CENTER);
			
			paColumn = new ColumnConfig(LocaleUtils.getText("pickTicketDetail.allocatedQuantityBU"), "pickTicketDetail.allocatedQuantityBU", 100, false); 
			paColumn.setAlign(TextAlign.CENTER); 

			pp2Column = new ColumnConfig(LocaleUtils.getText("pickTicketDetail.pickedQuantityBU"), "pickTicketDetail.pickedQuantityBU", 100, false); 
			pp2Column.setAlign(TextAlign.CENTER); 

			psColumn = new ColumnConfig(LocaleUtils.getText("pickTicketDetail.shippedQuantityBU"), "pickTicketDetail.shippedQuantityBU", 100, false); 
			psColumn.setAlign(TextAlign.CENTER);
			if(flag.equals("waveDocPage") || flag.equals("processDocPage")){
				psColumn.setHidden(true);
        	}
			
			lColumn = new ColumnConfig(LocaleUtils.getText("shipLotInfo"), "shipLotInfo", 150, false); 
			lColumn.setAlign(TextAlign.CENTER);
		 
			ColumnConfig propC1Column = new ColumnConfig("shipLotInfo.storageDate","shipLotInfo.storageDate"); 
			propC1Column.setHidden(true);
		 
			ColumnConfig propC2Column = new ColumnConfig("shipLotInfo.soi","shipLotInfo.soi"); 
			propC2Column.setHidden(true);
		 
			ColumnConfig propC3Column = new ColumnConfig("shipLotInfo.batchNum","shipLotInfo.batchNum"); 
			propC3Column.setHidden(true);
		 
		 	ColumnConfig propC5Column = new ColumnConfig("shipLotInfo.produceDate","shipLotInfo.produceDate"); 
		 	propC5Column.setHidden(true);
		 
		 	ColumnConfig propC6Column = new ColumnConfig("shipLotInfo.expireDate","shipLotInfo.expireDate"); 
		 	propC6Column.setHidden(true);
		 
		 	ColumnConfig propC7Column = new ColumnConfig("shipLotInfo.warnDate","shipLotInfo.warnDate"); 
		 	propC7Column.setHidden(true);
		 
		 	ColumnConfig propC8Column = new ColumnConfig("shipLotInfo.supplier","shipLotInfo.supplier"); 
		 	propC8Column.setHidden(true);
		 	
		 	columnModel = new ColumnModel(new ColumnConfig[]{ 
				 ptiColumn, iiColumn, pcColumn, pkuiColumn, icColumn, inColumn, puColumn, pp1Column,
				 paColumn, pp2Column, psColumn, lColumn, propC1Column, propC2Column,
				 propC3Column, propC5Column, propC6Column, propC7Column, propC8Column
				 }); 
	
			proxy = new MemoryProxy(new String[][]{}); 
			
			reader = new ArrayReader(recordDef); 
			
			store = new Store(proxy, reader);
			store.load();
			
			detailsTable = new GridPanel();

			detailsTable.setAutoScroll(true);
			detailsTable.setStore(store); 							 		 		 			 	
			detailsTable.setColumnModel(columnModel);
			detailsTable.setSelectionModel(new RowSelectionModel(false));
			
            ButtonListenerAdapter listener = new ButtonListenerAdapter() {
                public void onClick(Button button, EventObject e) {
                	autoAllocate();
                }
            };
            
            autoAllocateButton = new ToolbarButton("整单分配", listener);
            autoAllocateButton.setDisabled(Boolean.FALSE);
            
            lotInfoCheckBox = new CheckBox("批次严格匹配");
            lotInfoCheckBox.setChecked(Boolean.TRUE);
            containLockInv = new CheckBox("包含锁定库存");
            containLockInv.setChecked(Boolean.FALSE);
            
            lotInfoCheckBox.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					innitAvailableDatas(itemId,convertFigure,storageDate,soi,
							batchNum,produceDate,expireDate,
							warnDate,supplier,lotInfoCheckBox.isChecked(),containLockInv.isChecked());
				}
			});
            containLockInv.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					innitAvailableDatas(itemId,convertFigure,storageDate,soi,
							batchNum,produceDate,expireDate,
							warnDate,supplier,lotInfoCheckBox.isChecked(),containLockInv.isChecked());
				}
			});
            
			detailsTable.addGridRowListener(new GridRowListener(){
				public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
					getDatasFromGrid(grid, rowIndex, lotInfoCheckBox.isChecked(), containLockInv.isChecked());
                	if(flag.equals("pickTicketPage")){					
						//设置当前选中的发货单ID
						final Long ptdId = Long.valueOf(grid.getStore().getAt(rowIndex).getAsString("pickTicketDetail.id"));
						curPADataAccessor().setPtdId(ptdId);
					}else if(flag.equals("waveDocPage")){						
						//设置当前选中的波次ID
						final Long wddId = Long.valueOf(grid.getStore().getAt(rowIndex).getAsString("pickTicketDetail.id"));
						curPADataAccessor().setWddId(wddId);
					}else if(flag.equals("processDocPage")){						
						//设置当前选中的加工单ID
						final Long processDocId = Long.valueOf(grid.getStore().getAt(rowIndex).getAsString("pickTicketDetail.id"));
						curPADataAccessor().setPrdId(processDocId);
					}
				}

				public void onRowContextMenu(GridPanel grid, int rowIndex, EventObject e) {
				}

				public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
				}
			});
			
			HorizontalPanel panel2 = new HorizontalPanel();
			
			panel2.add(autoAllocateButton);
			panel2.add(lotInfoCheckBox);
			panel2.add(containLockInv);
			panel2.setCellWidth(autoAllocateButton, "120px");
			panel2.setCellWidth(lotInfoCheckBox, "120px");

			this.setLayout(new RowLayout());
			this.add(detailsTable, new RowLayoutData("88%"));
			this.add(panel2, new RowLayoutData("12%"));
	}
	
	/**
	 * 整单分配
	 */
	private void autoAllocate() {
		final Map param = new HashMap();
		
		if(flag.equals("pickTicketPage")){
			//存放发货单ID，以便整单分配后刷新信息
			Long pickTicketId = this.curPADataAccessor().currentPickTicket().getId();
			param.put("pickTicketId", pickTicketId);
		}else if(flag.equals("waveDocPage")){
			//存放波次ID，以便整单分配后刷新信息
			Long waveDocId = this.curPADataAccessor().currentPickTicket().getId();
			param.put("waveDocId", waveDocId);
		}else if(flag.equals("processDocPage")){
			//存放波次ID，以便整单分配后刷新信息
			Long processDocId = this.curPADataAccessor().currentPickTicket().getId();
			param.put("processDocId", processDocId);
		}
		
		final List<Long> result = new ArrayList<Long>();
		
		for (int i = 0; i < detailsTable.getStore().getCount(); i++) {
			if(detailsTable.getSelectionModel().isSelected(i)){
				result.add(Long.valueOf(detailsTable.getStore().getAt(i).getAsString("pickTicketDetail.id")));
			}
		}
		
		param.put(IPage.PARENT_ENTITY_IDS, result);
		if(flag.equals("pickTicketPage")){
			//发货单整单分配
			this.curPADataAccessor().atuoAllocate(param);
		}else if(flag.equals("waveDocPage")){
			//波次整单分配
			this.curPADataAccessor().atuoWaveDocAllocate(param);
		}else if(flag.equals("processDocPage")){
			this.curPADataAccessor().atuoProcessDocAllocate(param);
		}
	}
	
	public Page_PA_DataAccessor curPADataAccessor() {
		return (Page_PA_DataAccessor) super.getData();
	}	
	
	public void doDispath(String message){
		super.doDispath(message);
		
		if (Page_PA_DataAccessor.INIT_DETAILS_INFO.equals(message) || Page_PA_DataAccessor.INIT_WD_DETAILS_INFO.equals(message)
				||Page_PA_DataAccessor.SINGLE_CANCEL_WAVE_ALLOCATE_INFO.equals(message) || Page_PA_DataAccessor.INIT_PROCESS_DOC_DETAILS_INFO.equals(message) 
				|| Page_PA_DataAccessor.SINGLE_CANCEL_PROCESSDOC_ALLOCATE_INFO.equals(message)) {
			Object[][] data = new Object[this.curPADataAccessor().all_PDS().size()][];
			
			for (int i = 0; i < this.curPADataAccessor().all_PDS().size(); i++) {
				data[i] = this.curPADataAccessor().all_PDS().get(i).toArray();
			}
			
			proxy = new MemoryProxy(data);
			
			store.setDataProxy(proxy);
			store.reload();
			
			detailsTable.reconfigure(store, columnModel);						 
		}
	}
}