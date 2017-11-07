package com.vtradex.wms.client.packagingtable.panel;

import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.thorn.client.ui.panel.AbstractSupportPanel;
import com.vtradex.wms.client.packagingtable.MaintainWmsPackagingTablePage;
import com.vtradex.wms.client.packagingtable.data.PackagingTableConstants;
import com.vtradex.wms.client.packagingtable.data.PackagingTableWokerDTO;

/**
 * West Panel
 *
 * @category Panel UI
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:49 $
 */
@SuppressWarnings("unchecked")
public class WestPackagingTablePanel extends AbstractSupportPanel {
	
	private transient Panel topPackagingTableNamePanel;
	private WestCenterPanel centerPanel;
	private transient FlexTable jobInfoTable;
	private transient FlexTable statisticsTable;
	
	private MaintainWmsPackagingTablePage mwptp;

	public WestPackagingTablePanel(IMessagePage page, String name, MaintainWmsPackagingTablePage mwptp) {
		super(page, name);
		this.mwptp = mwptp;
		this.init();
		this.draw();
	}
	
	private void init(){
		setTitle("包装台");
		setWidth(270);
		setCollapsible(true);
		setLayout(new FitLayout()); 
	}
	
	protected void draw(){
		Panel container = new Panel();
		container.setLayout(new BorderLayout());
    	container.setBorder(false);
    	
    	// Top
    	topPackagingTableNamePanel = new Panel();
    	topPackagingTableNamePanel.setLayout(new FitLayout());
    	topPackagingTableNamePanel.setBorder(false);
    	topPackagingTableNamePanel.setHeight(100);
//    	topPackagingTableNamePanel.setBodyStyle("background-color:#fff1f1;color: #b90000;");
    	topPackagingTableNamePanel.setBodyStyle("background-color:#d9e8fb;color: #15428b;");
    	String packagingTableName = "   ";
    	topPackagingTableNamePanel.setHtml("<p style='text-align:center;vertical-align:middle;font-size:48px;font-weight:bold;margin:20px 0 10px 0;'>" + packagingTableName + "</p>");
    	BorderLayoutData topLayoutData = new BorderLayoutData(RegionPosition.NORTH);
		container.add(topPackagingTableNamePanel, topLayoutData);
    	
		// Center
		centerPanel = new WestCenterPanel(this, "WestCenterPanel");
    	BorderLayoutData centerLayoutData = new BorderLayoutData(RegionPosition.CENTER);
		container.add(centerPanel, centerLayoutData);
    	
		this.add(container);
	}
	
	public void doDispath(String message){
		super.doDispath(message);
		if (PackagingTableConstants.MSG_GET_STATISTICSI_NFO.equals(message)) {
			refreshStatisticsTable(mwptp.getData().getResult());
		} else if (PackagingTableConstants.MSG_GET_PACKAGING_JOBS.equals(message)) {
			refreshJobInfo(mwptp.getData().getResult());
			mwptp.getCenterPackagingTablePanel().setMoveDocCodeTextUIFocus();
		}
	}
	
	protected class WestCenterPanel extends AbstractSupportPanel {

		public WestCenterPanel(IMessagePage page, String name) {
			super(page, name);
			init();
			draw();
		}
		
		protected void init() {
			setBorder(false);
			setLayout(new FitLayout()); 
		}
		
		protected void draw(){
			Panel container = new Panel();
			container.setLayout(new RowLayout());
	    	container.setBorder(false);
	    	
	    	// Top
	    	Panel topPanel = new Panel();
	    	topPanel.setBorder(false);
	    	topPanel.setLayout(new FitLayout());
	    	topPanel.add(this.createStatisticsFlexTable());
	    	
			// Bottom
			Panel bottomPanel = new Panel();
			bottomPanel.setBorder(false);
			bottomPanel.add(this.createJobInfoFlexTable());
			
			container.add(topPanel, new RowLayoutData("40%"));
			container.add(bottomPanel, new RowLayoutData("60%"));
			this.add(container);
		}
		
		private FlexTable createStatisticsFlexTable() {
			statisticsTable = new FlexTable();
			statisticsTable.setStyleName("packaging_panel");
			statisticsTable.setWidth("100%");
			statisticsTable.setHeight("100%");
			statisticsTable.setCellPadding(0);
			statisticsTable.setCellPadding(0);
			
			statisticsTable.setWidget(0, 0, new HTML(""));
			statisticsTable.setText(0, 1, "订单数");
			statisticsTable.setText(0, 2, "订单件数");
			
			statisticsTable.setWidget(1, 0, new HTML("<b>已完成</b>"));
			statisticsTable.setText(1, 1, "0");  
			statisticsTable.setText(1, 2, "0");
			
			statisticsTable.setWidget(2, 0, new HTML("<b>未完成</b>"));  
			statisticsTable.setText(2, 1, "0");  
			statisticsTable.setText(2, 2, "0");
			
			DOM.setElementProperty(DOM.getParent(DOM.getParent(statisticsTable.getWidget(0, 0).getElement())), "className", "packaging_panel_head");
			DOM.setElementProperty(DOM.getParent(DOM.getParent(statisticsTable.getWidget(1, 0).getElement())), "className", "packaging_panel_normal");
			DOM.setElementProperty(DOM.getParent(DOM.getParent(statisticsTable.getWidget(2, 0).getElement())), "className", "packaging_panel_abnormal");
			
			return statisticsTable;
		}
		
		private FlexTable createJobInfoFlexTable() {
			jobInfoTable = new FlexTable();
			jobInfoTable.setStyleName("mmp_statpanel");
			jobInfoTable.setWidth("100%");
			jobInfoTable.setHeight("100%");
			jobInfoTable.setCellPadding(0);
			jobInfoTable.setCellPadding(0);
			
			jobInfoTable.setWidget(0, 0, new HTML("岗位名称"));
			jobInfoTable.setWidget(0, 1, new HTML("工作人员"));
			DOM.setElementProperty(DOM.getParent(DOM.getParent(jobInfoTable.getWidget(0, 0).getElement())), "className", "packaging_panel_head");
			
			jobInfoTable.setWidget(1, 0, new HTML(""));
			jobInfoTable.setWidget(1, 1, new HTML(""));
			DOM.setElementProperty(DOM.getParent(DOM.getParent(jobInfoTable.getWidget(1, 0).getElement())), "className", "packaging_panel_normal");
			
			jobInfoTable.setWidget(2, 0, new HTML(""));
			jobInfoTable.setWidget(2, 1, new HTML(""));
			DOM.setElementProperty(DOM.getParent(DOM.getParent(jobInfoTable.getWidget(2, 0).getElement())), "className", "packaging_panel_abnormal");
			
			jobInfoTable.setWidget(3, 0, new HTML(""));
			jobInfoTable.setWidget(3, 1, new HTML(""));
			DOM.setElementProperty(DOM.getParent(DOM.getParent(jobInfoTable.getWidget(3, 0).getElement())), "className", "packaging_panel_normal");
			
			jobInfoTable.setWidget(4, 0, new HTML(""));
			jobInfoTable.setWidget(4, 1, new HTML(""));
			DOM.setElementProperty(DOM.getParent(DOM.getParent(jobInfoTable.getWidget(4, 0).getElement())), "className", "packaging_panel_abnormal");
			
			return jobInfoTable;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void refreshJobInfo(Map workerMap) {
		if (workerMap != null) {
			int rowCount = jobInfoTable.getRowCount();
			if (rowCount > 1) {
				for (int r = 1; r < rowCount; r++) {
					for (int c = 0 ; c < jobInfoTable.getCellCount(r); c++) {
						jobInfoTable.clearCell(r, c);
					}
				}
			}
			jobInfoTable.setWidget(0, 0, new HTML("岗位名称"));
			jobInfoTable.setWidget(0, 1, new HTML("工作人员"));
			DOM.setElementProperty(DOM.getParent(DOM.getParent(jobInfoTable.getWidget(0, 0).getElement())), "className", "packaging_panel_head");
			
			int i = 1;
			Set<Long> keySet = workerMap.keySet();
			for (Long key : keySet) {
				PackagingTableWokerDTO workerDTO = (PackagingTableWokerDTO)workerMap.get(key);
				jobInfoTable.setWidget(i, 0, new HTML(workerDTO.getJob()));
				jobInfoTable.setText(i, 1, workerDTO.getName());
				if (i % 2 == 0) {
					DOM.setElementProperty(DOM.getParent(DOM.getParent(jobInfoTable.getWidget(i, 0).getElement())), "className", "packaging_panel_abnormal");
				} else {
					DOM.setElementProperty(DOM.getParent(DOM.getParent(jobInfoTable.getWidget(i, 0).getElement())), "className", "packaging_panel_normal");
				}
				i++;
			}
		}
	}
	
	public void refreshPackagingTableName(String name) {
		topPackagingTableNamePanel.setHtml("<p style='text-align:center;vertical-align:middle;font-size:48px;font-weight:bold;margin:20px 0 10px 0;'>" + name + "</p>");
	}
	
	@SuppressWarnings("rawtypes")
	public void refreshStatisticsTable(Map result) {
		statisticsTable.setText(1, 1, result.get(PackagingTableConstants.KEY_ORDER_FINISH_NUM).toString());
		statisticsTable.setText(1, 2, result.get(PackagingTableConstants.KEY_ORDER_FINISH_QUANTITY).toString());
		statisticsTable.setText(2, 1, result.get(PackagingTableConstants.KEY_ORDER_UNFINISH_NUM).toString());
		statisticsTable.setText(2, 2, result.get(PackagingTableConstants.KEY_ORDER_UNFINISH_QUANTITY).toString());
	}
}
