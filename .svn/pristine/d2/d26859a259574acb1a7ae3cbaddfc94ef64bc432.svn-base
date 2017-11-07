package com.vtradex.wms.client.packagingtable.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.ObjectFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;
import com.vtradex.thorn.client.ApplicationWindow;
import com.vtradex.thorn.client.config.page.PageConfig;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.thorn.client.rpc.CatchPageConfigAsync;
import com.vtradex.thorn.client.ui.TextUI;
import com.vtradex.thorn.client.ui.UIFactory;
import com.vtradex.thorn.client.ui.commonWidgets.BeautyButton;
import com.vtradex.thorn.client.ui.page.IPage;
import com.vtradex.thorn.client.ui.page.IPopupPage;
import com.vtradex.thorn.client.ui.panel.AbstractSupportPanel;
import com.vtradex.thorn.client.ui.table.FormTable;
import com.vtradex.thorn.client.utils.StringUtils;
import com.vtradex.wms.client.packagingtable.MaintainWmsPackagingTablePage;
import com.vtradex.wms.client.packagingtable.data.MoveDocDetailDTO;
import com.vtradex.wms.client.packagingtable.data.PackagingTableConstants;
import com.vtradex.wms.client.packagingtable.data.PackagingTableDataAccessor;

/**
 * Center Panel
 *
 * @category Panel UI
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:49 $
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class CenterPackagingTablePanel extends AbstractSupportPanel {
	
	private CenterSearchPanel searchPanel;
	private CenterScaningPanel scaningPanel;
	private CenterItemInfoPanel itemInfoPanel;
	
	private final MaintainWmsPackagingTablePage mwptp;

	public CenterPackagingTablePanel(IMessagePage page, String name, MaintainWmsPackagingTablePage mwptp) {
		super(page, name);
		
		this.mwptp = mwptp;
		
		this.init();
		this.draw();
	}
	
	private void init() {
		setLayout(new FitLayout()); 
	}
	
	protected void draw() {
		Panel container = new Panel();
		container.setLayout(new RowLayout());
    	container.setBorder(false);
    	
    	searchPanel = new CenterSearchPanel(this, "CenterSearchPanel");
    	scaningPanel = new CenterScaningPanel();
    	itemInfoPanel = new CenterItemInfoPanel();
    	
    	container.add(searchPanel, new RowLayoutData("12%"));
		container.add(scaningPanel, new RowLayoutData("43%"));
		container.add(itemInfoPanel, new RowLayoutData("45%"));
		
    	this.add(container);
	}
	
	@Override
	public PackagingTableDataAccessor getData() {
		return (PackagingTableDataAccessor)super.getData();
	}
	
	@Override
	public void doDispath(String message){
		super.doDispath(message);
		if (PackagingTableConstants.MSG_QUERY_MOVEDOCDETAIL.equals(message)) {
			scaningPanel.reset();
			itemInfoPanel.doQeryMoveDocDetailResult();
			this.searchPanel.showMessage("加载货品信息成功", Boolean.TRUE);
			this.clearSnNoTextUI();
		} else if (PackagingTableConstants.MSG_GET_MOVEDOCDETAIL.equals(message)) {
			scaningPanel.addDataToGrid();
			this.searchPanel.showMessage("扫描条码成功", Boolean.TRUE);
			scaningPanel.autoOpenScanConfirmPackage();
		} else if (PackagingTableConstants.MSG_FINISH_PACKAGING.equals(message)) {
			if (this.getData().getResult().get(PackagingTableConstants.ERROR) != null) {
				this.searchPanel.showMessage(this.getData().getResult().get(PackagingTableConstants.ERROR).toString(), Boolean.FALSE);
			} else {
				directPrint(this.getData().getResult());
				Boolean flag = new Boolean(this.getData().getResult().get(PackagingTableConstants.KEY_ALL_PACKAGED).toString());
				if (flag) {
					this.searchPanel.showMessage("包装完成", Boolean.TRUE);
					scaningPanel.reset();
					itemInfoPanel.reset();
					
					Map params2 = new HashMap();
					params2.put(PackagingTableConstants.KEY_PACKAGING, mwptp.getLocDTO());
					mwptp.getData().remoteCallGetStatisticsInfo(params2);
					
					searchPanel.clearSnNoTextUI();
					searchPanel.clearMoveDocCodeTextUIFocus();
				} else {
					this.searchPanel.showMessage("包装确认成功", Boolean.TRUE);
					
					Map params2 = new HashMap();
					params2.put(PackagingTableConstants.KEY_PACKAGING, mwptp.getLocDTO());
					mwptp.getData().remoteCallGetStatisticsInfo(params2);
					
					searchPanel.queryMoveDocDetail();
					scaningPanel.reset();
				}
			}
		}
	}
	
	protected class CenterSearchPanel extends AbstractSupportPanel {
		
		private FormTable formTable;
		private TextUI moveDocCodeTextUI, snNoTextUI;
		private DoneButton doneButton;
		
		public CenterSearchPanel(IMessagePage page, String name) {
			super(page, name);
			init();
			draw();
		}
		
		protected void init() {
			setLayout(new HorizontalLayout(6));
			setBorder(false);
			setTitle("扫描查找");
		}
		
		protected void draw() {
			formTable = new FormTable();
			
			moveDocCodeTextUI = UIFactory.createTextUI("拣货单号", false, true, 1, 180);
			moveDocCodeTextUI.setRow(1);moveDocCodeTextUI.setColumn(1);
			moveDocCodeTextUI.setFocusUI(true);
			moveDocCodeTextUI.addToTable(formTable);
			
			((TextBox)moveDocCodeTextUI.getInputWidget()).addKeyboardListener(new KeyboardListenerAdapter() {
				@Override
				public void onKeyDown(Widget sender, char keyCode, int modifiers) {
					if(KeyboardListener.KEY_ENTER == keyCode){
						String moveDocCode = moveDocCodeTextUI.getValue() == null ? "" : moveDocCodeTextUI.getValue().toString();
						if (!StringUtils.isEmpty(moveDocCode)) {
							queryMoveDocDetail();
						}
					}
				}
			});
			
			snNoTextUI = UIFactory.createTextUI("条码", false, true, 1, 180);
			snNoTextUI.setRow(1);snNoTextUI.setColumn(2);
			snNoTextUI.addToTable(formTable);
			
			((TextBox)snNoTextUI.getInputWidget()).addKeyboardListener(new KeyboardListenerAdapter() {
				@Override
				public void onKeyDown(Widget sender, char keyCode, int modifiers) {
					if(KeyboardListener.KEY_ENTER == keyCode){
						String moveDocCode = moveDocCodeTextUI.getValue() == null ? "" : moveDocCodeTextUI.getValue().toString();
						String snNo = snNoTextUI.getValue() == null ? "" : snNoTextUI.getValue().toString();
						if (!StringUtils.isEmpty(moveDocCode) && !StringUtils.isEmpty(snNo)) {
							scanSnNo(moveDocCode, snNo);
							clearSnNoTextUI();
						}
					}
				}
			});
			
			doneButton = new DoneButton();
			
			Panel formPanel = new Panel();
			formPanel.setLayout(new HorizontalLayout(1));
			formPanel.add(formTable.getPanel());
			
			Panel bottomPanel = new Panel();
			bottomPanel.setLayout(new HorizontalLayout(1));
			
			bottomPanel.add(doneButton);
			
			this.add(formPanel);
			this.add(bottomPanel);
		}
		
		/**
		 * 包装完成
		 */
		protected class DoneButton extends BeautyButton implements com.vtradex.thorn.client.ui.ClickListener {
			public DoneButton() {
				super("包装完成");
				this.addClickListener(this);
			}
			
			public void onClick(Object obj) {
				if (scaningPanel.getGridData().size() <= 0) {
					searchPanel.showMessage("请扫描条码", Boolean.FALSE);
					return;
				}
				final Map parentParams = new HashMap();
				parentParams.put(IPage.IS_EDIT_PAGE, Boolean.TRUE);
				parentParams.put(PackagingTableConstants.KEY_SCANING_DATA, scaningPanel.getGridData());
				
				ApplicationWindow.context.getPageConfig("editWmsScanConfirmPackagingTablePage", new CatchPageConfigAsync() {
					public void afterInvotion(PageConfig pageConfig) {
						ApplicationWindow.getCurrentMessageLabel().setText("");
						IPopupPage page = pageConfig.createPage(parentParams, mwptp);
						page.show();
						page.initData();
						page.onFoucs();
					}
				});
			}
		}
		
		public void queryMoveDocDetail() {
			Map params = new HashMap();
			params.put(PackagingTableConstants.KEY_MOVEDOC_CODE, moveDocCodeTextUI.getText());
			params.put(PackagingTableConstants.KEY_PACKAGING, mwptp.getLocDTO());
			CenterPackagingTablePanel.this.getData().remoteCallQueryMoveDocDetail(params);
		}
		
		private void scanSnNo(String moveDocCode, String barCode) {
			int flag = scaningPanel.doScanSnNo(barCode);
			if (flag != 1) {
				Map params = new HashMap();
				params.put(PackagingTableConstants.SUCCESS, flag);
				params.put(PackagingTableConstants.KEY_MOVEDOC_CODE, moveDocCode);
				params.put(PackagingTableConstants.KEY_BARCODE, barCode);
				params.put(PackagingTableConstants.KEY_SCANING_IDS, scaningPanel.getSelectedIds());
				CenterPackagingTablePanel.this.getData().remoteCallGetMoveDocDetail(params);
			}
			CenterPackagingTablePanel.this.scaningPanel.autoOpenScanConfirmPackage();
		}
		
		public void setMoveDocCodeTextUIFocus() {
			((TextBox)moveDocCodeTextUI.getInputWidget()).setFocus(true);
		}
		
		public void clearMoveDocCodeTextUIFocus() {
			moveDocCodeTextUI.setText("");
			((TextBox)moveDocCodeTextUI.getInputWidget()).setFocus(true);
		}
		
		public void clearSnNoTextUI() {
			snNoTextUI.setText("");
			((TextBox)snNoTextUI.getInputWidget()).setFocus(true);
		}
		
		public void showMessage(String message, Boolean beSuccess) {
			if (beSuccess) {
				this.setTitle("扫描查找" + " <font color='#0000FF'>" + message + "</font>");
			} else {
				this.setTitle("扫描查找" + " <font color='#FF0000'>" + message + "</font>");
			}
		}
	}
	
	/**
	 * 扫描中暂存Grid
	 */
	protected class CenterScaningPanel extends GridPanel {
		
		protected Store store;
		protected MemoryProxy proxy;
		protected ArrayReader reader;
		
		public CenterScaningPanel() {
			init();
			draw();
		}
		
		protected void init() {
			this.setTitle("扫描中");
			this.setLayout(new FitLayout());
			this.setBorder(false);
		}
		
		protected void draw() {
			proxy = new MemoryProxy(new String[][]{});
			reader = new ArrayReader(0, generateRecordDef());
			store = new Store(proxy, reader);
			setStore(store);
			setColumnModel(generateColumnModel());
			setAutoScroll(true);
			setLoadMask(true);
			setStripeRows(true);
			setEnableColumnHide(true);
			setEnableHdMenu(true);
			setEnableColumnMove(true);
			setBorder(false);
			setEnableDragDrop(false);
		}
		
		public void reset(){
			if(store != null)
				store.removeAll();
		}
		
		public void reload(){
			store.load();
			reset();
		}
		
		public void load(){
			store.load();
			reset();
		}
		
		public List<Long> getSelectedIds(){
			List<Long> ids = new ArrayList<Long>();
			for (int i = 0; i < this.store.getCount(); i++) {
				String moveDocDetailId = this.store.getAt(i).getAsString("movedocDetail.id");
				ids.add(new Long(moveDocDetailId));
			}
			return ids;
		}
		
		protected RecordDef generateRecordDef() {
			return new RecordDef(new FieldDef[]{
					new ObjectFieldDef("movedocDetail.id"),
					new ObjectFieldDef("movedocDetail.moveDocId"),
					new ObjectFieldDef("movedocDetail.item.code"),
					new ObjectFieldDef("movedocDetail.item.name"),
					new ObjectFieldDef("movedocDetail.item.barCode"),
					new ObjectFieldDef("movedocDetail.packageUnit.unit"),
					new ObjectFieldDef("movedocDetail.packageUnit.weight"),
					new ObjectFieldDef("movedocDetail.packageUnit.volume"),
					new ObjectFieldDef("movedocDetail.allocatedQuantityBU"),
					new ObjectFieldDef("movedocDetail.pickedQuantityBU"),
					new ObjectFieldDef("movedocDetail.packagingQuantityBU"),
					new ObjectFieldDef("movedocDetail.unPackageQuantityBU"),
					new ObjectFieldDef("movedocDetail.scanQuantityBU"),
					new ObjectFieldDef("movedocDetail.lotInfoStr")
			});
		}
		
		protected ColumnModel generateColumnModel() {
			int width = Window.getClientWidth() - 460;
			ColumnConfig id = new ColumnConfig("序号</font>", "movedocDetail.id", 50);
			id.setHidden(true);
			
			ColumnConfig moveDocId = new ColumnConfig("拣货单序号", "movedocDetail.moveDocId", 50);
			moveDocId.setHidden(true);
			
			ColumnConfig itemCode = new ColumnConfig("货品编码", "movedocDetail.item.code", (int)(width * 0.15));
			ColumnConfig itemName = new ColumnConfig("货品名称", "movedocDetail.item.name", (int)(width * 0.2));
			
			ColumnConfig barCode = new ColumnConfig("货品条码", "movedocDetail.item.barCode", 50);
			barCode.setHidden(true);
			
			ColumnConfig packageUnit = new ColumnConfig("包装单位", "movedocDetail.packageUnit.unit", (int)(width * 0.1));
			
			ColumnConfig packageWeight = new ColumnConfig("包装重量", "movedocDetail.packageUnit.weight", 50);
			packageWeight.setHidden(true);
			
			ColumnConfig packageVolume = new ColumnConfig("包装体积", "movedocDetail.packageUnit.volume", 50);
			packageVolume.setHidden(true);
			
			ColumnConfig allocatedQuantityBU = new ColumnConfig("分配数量", "movedocDetail.allocatedQuantityBU", (int)(width * 0.1));
			ColumnConfig pickedQuantityBU = new ColumnConfig("拣选数量", "movedocDetail.pickedQuantityBU", (int)(width * 0.1));
			ColumnConfig packagingQuantityBU = new ColumnConfig("已包装数量", "movedocDetail.packagingQuantityBU", (int)(width * 0.1));
			ColumnConfig unPackageQuantityBU = new ColumnConfig("未包装重量", "movedocDetail.unPackageQuantityBU", 50);
			unPackageQuantityBU.setHidden(true);
			
			ColumnConfig scanQuantityBU = new ColumnConfig("扫描数量", "movedocDetail.scanQuantityBU", (int)(width * 0.1));
			
			ColumnConfig lotInfoStr = new ColumnConfig("批次属性", "movedocDetail.lotInfoStr", (int)(width * 0.3));
			
			ColumnConfig[] configs = new ColumnConfig[] {
				id,
				moveDocId,
				itemCode,
				itemName,
				barCode,
				packageUnit,
				packageWeight,
				packageVolume,
				allocatedQuantityBU,
				pickedQuantityBU,
				packagingQuantityBU,
				unPackageQuantityBU,
				scanQuantityBU,
				lotInfoStr
			};
			for(ColumnConfig cc : configs){
				cc.setSortable(true);
				cc.setAlign(TextAlign.CENTER);
			}
			return new ColumnModel(configs);
		}
		
		public int doScanSnNo(String barCode) {
			int result = 0;
			for (int i = 0; i < this.store.getCount(); i++) {
				String barCodeInGrid = this.store.getAt(i).getAsString("movedocDetail.item.barCode");
				if (barCodeInGrid.trim().equals(barCode.trim())) {
					String unPackageQuantityBU = this.store.getAt(i).getAsString("movedocDetail.unPackageQuantityBU");
					String scanQuantityBU = this.store.getAt(i).getAsString("movedocDetail.scanQuantityBU");
					
					if (Double.valueOf(unPackageQuantityBU).doubleValue() == Double.valueOf(scanQuantityBU).doubleValue()) {
						result = -1;
						continue;
					}
					
					this.store.getAt(i).set("movedocDetail.scanQuantityBU", (Double.valueOf(scanQuantityBU) + 1) + "");
					result = 1;
					break;
				}
			}
			return result;
		}
		
		public Double getSumPackageQuantity() {
			for (int i = 0; i < this.store.getCount(); i++) {
				String scanQuantityBU = this.store.getAt(i).getAsString("movedocDetail.scanQuantityBU");
				return new Double(scanQuantityBU);
			}
			return 0D;
		}
		
		public List getGridData() {
			List datas = new ArrayList();
			for (int i = 0; i < this.store.getCount(); i++) {
				String id = this.store.getAt(i).getAsString("movedocDetail.id");
				String moveDocId = this.store.getAt(i).getAsString("movedocDetail.moveDocId");
				String weight = this.store.getAt(i).getAsString("movedocDetail.packageUnit.weight");
				String volume = this.store.getAt(i).getAsString("movedocDetail.packageUnit.volume");
				String scanQuantityBU = this.store.getAt(i).getAsString("movedocDetail.scanQuantityBU");
				
				MoveDocDetailDTO dto = new MoveDocDetailDTO();
				dto.setId(Long.valueOf(id));
				dto.setMoveDocId(Long.valueOf(moveDocId));
				dto.setWeight(weight);
				dto.setVolume(volume);
				dto.setScanQuantityBU(scanQuantityBU);
				
				datas.add(dto);
			}
			return datas;
		}
		
		public void addDataToGrid() {
			Map result = CenterPackagingTablePanel.this.getData().getResult();
			MoveDocDetailDTO dto = (MoveDocDetailDTO)result.get(PackagingTableConstants.KEY_MOVEDOCDETAIL_DTO);
			Record record = generateRecordDef().createRecord(dto.getNormalData());
			this.store.add(record);
		}
		
		public void autoOpenScanConfirmPackage() {
			if (CenterPackagingTablePanel.this.scaningPanel.getSumPackageQuantity().doubleValue() == CenterPackagingTablePanel.this.itemInfoPanel.getSumUnPackageQuantity().doubleValue()) {
				if (scaningPanel.getGridData().size() <= 0) {
					searchPanel.showMessage("请扫描条码", Boolean.FALSE);
					return;
				}
				final Map parentParams = new HashMap();
				parentParams.put(IPage.IS_EDIT_PAGE, Boolean.TRUE);
				parentParams.put(PackagingTableConstants.KEY_SCANING_DATA, scaningPanel.getGridData());
				
				ApplicationWindow.context.getPageConfig("editWmsScanConfirmPackagingTablePage", new CatchPageConfigAsync() {
					public void afterInvotion(PageConfig pageConfig) {
						ApplicationWindow.getCurrentMessageLabel().setText("");
						IPopupPage page = pageConfig.createPage(parentParams, mwptp);
						page.show();
						page.initData();
						page.onFoucs();
					}
				});
			}
		}
	}
	
	
	/**
	 * 货品信息Grid
	 */
	protected class CenterItemInfoPanel extends GridPanel {
		
		protected Store store;
		protected MemoryProxy proxy;
		protected ArrayReader reader;
		
		
		protected Double sumUnPackageQuantity = 0D;
		
		public CenterItemInfoPanel() {
			init();
			draw();
		}
		
		protected void init() {
			this.setTitle("货品信息");
			this.setLayout(new FitLayout());
			this.setBorder(false);
		}
		
		protected void draw() {
			proxy = new MemoryProxy(new String[][]{});
			reader = new ArrayReader(0, generateRecordDef());
			store = new Store(proxy, reader);
			setStore(store);
			setColumnModel(generateColumnModel());
			setAutoScroll(true);
			setLoadMask(true);
			setStripeRows(true);
			setEnableColumnHide(true);
			setEnableHdMenu(true);
			setEnableColumnMove(true);
			setBorder(false);
			setEnableDragDrop(false);
		}
		
		public void doQeryMoveDocDetailResult() {
			sumUnPackageQuantity = 0D;
			Map result = CenterPackagingTablePanel.this.getData().getResult();
			List<MoveDocDetailDTO> resultList = (List<MoveDocDetailDTO>)result.get(PackagingTableConstants.KEY_ITEMINFO_LIST);
			Object[][] data = new Object[resultList.size()][];
			for(int i = 0; i < resultList.size(); i++) {
				MoveDocDetailDTO dto = (MoveDocDetailDTO)resultList.get(i);
				data[i] = dto.getData();
				
				sumUnPackageQuantity += new Double(dto.getUnPackageQuantityBU());
			}
			proxy = new MemoryProxy(data);
			store.setDataProxy(proxy);
			store.reload();
		}
		
		public void reset(){
			proxy = new MemoryProxy(new String[][]{});
			store.setDataProxy(proxy);
			store.reload();
		}
		
		public void reload() {
			store.load();
			reset();
		}
		
		public void load() {
			store.load();
			reset();
		}
		
		public List<Long> getSelectedIds(){
			List<Long> ids = new ArrayList<Long>();
			for(Record r : getSelectionModel().getSelections()){
				ids.add(new Long(r.getAsString("movedocDetail.id")));
			}
			return ids;
		}
		
		protected RecordDef generateRecordDef() {
			return new RecordDef(new FieldDef[]{
					new ObjectFieldDef("movedocDetail.id"),
					new ObjectFieldDef("movedocDetail.item.code"),
					new ObjectFieldDef("movedocDetail.item.name"),
					new ObjectFieldDef("movedocDetail.packageUnit.unit"),
					new ObjectFieldDef("movedocDetail.allocatedQuantityBU"),
					new ObjectFieldDef("movedocDetail.pickedQuantityBU"),
					new ObjectFieldDef("movedocDetail.packagingQuantityBU"),
					new ObjectFieldDef("movedocDetail.unPackageQuantityBU"),
					new ObjectFieldDef("movedocDetail.lotInfoStr"),
			});
		}
		
		protected ColumnModel generateColumnModel() {
			int width = Window.getClientWidth() - 460;
			ColumnConfig id = new ColumnConfig("<font style='font-weight:bold' color='#2a2a2a'>序号", "movedocDetail.id", 50);
			id.setHidden(true);
			ColumnConfig itemCode = new ColumnConfig("货品编码", "movedocDetail.item.code", (int)(width * 0.15));
			ColumnConfig itemName = new ColumnConfig("货品名称", "movedocDetail.item.name", (int)(width * 0.2));
			ColumnConfig packageUnit = new ColumnConfig("包装单位", "movedocDetail.packageUnit.unit", (int)(width * 0.1));
			ColumnConfig allocatedQuantityBU = new ColumnConfig("分配数量", "movedocDetail.allocatedQuantityBU", (int)(width * 0.1));
			ColumnConfig pickedQuantityBU = new ColumnConfig("拣选数量", "movedocDetail.pickedQuantityBU", (int)(width * 0.1));
			ColumnConfig packagingQuantityBU = new ColumnConfig("已包装数量", "movedocDetail.packagingQuantityBU", (int)(width * 0.1));
			ColumnConfig unPackagingQuantityBU = new ColumnConfig("未包装数量", "movedocDetail.unPackageQuantityBU", (int)(width * 0.1));
			ColumnConfig lotInfoStr = new ColumnConfig("批次属性", "movedocDetail.lotInfoStr", (int)(width * 0.15));
			
			ColumnConfig[] configs = new ColumnConfig[]{
				id,
				itemCode,
				itemName,
				packageUnit,
				allocatedQuantityBU,
				pickedQuantityBU,
				packagingQuantityBU,
				unPackagingQuantityBU,
				lotInfoStr
			};
			for(ColumnConfig cc : configs){
				cc.setSortable(true);
				cc.setAlign(TextAlign.CENTER);
			}
			return new ColumnModel(configs);
		}

		public Double getSumUnPackageQuantity() {
			return sumUnPackageQuantity;
		}

		public void setSumUnPackageQuantity(Double sumUnPackageQuantity) {
			this.sumUnPackageQuantity = sumUnPackageQuantity;
		}
	}
	
	public void setMoveDocCodeTextUIFocus() {
		this.searchPanel.setMoveDocCodeTextUIFocus();
	}
	
	public void clearMoveDocCodeTextUIFocus() {
		this.searchPanel.clearMoveDocCodeTextUIFocus();
	}
	
	public void clearSnNoTextUI() {
		this.searchPanel.clearSnNoTextUI();
	}
	
	private void directPrint(Map result) {
		String raq = result.get(PackagingTableConstants.KEY_BOX_RAQ).toString();
		Long wpdId = new Long(result.get(PackagingTableConstants.KEY_WPDID).toString());
		
		String url = getReportDirectURL() + "?raq=" + raq + "&params=id=" + wpdId + ";wbdId=" + wpdId + "&needSelectPrinter=no";
        
        Frame export = new Frame();
		export.setSize("0px", "0px");
	    export.setUrl(url);
	    RootPanel.get().add(export);
	}
	
	private String getReportDirectURL(){
        Map globalParams = ApplicationWindow.context.getGlobalParams();
        String reportServer = (String) globalParams.get("reportDirectURL");
        return StringUtils.isEmpty(reportServer) ? "reportJsp/directPrint.jsp" : reportServer;
    }
}
