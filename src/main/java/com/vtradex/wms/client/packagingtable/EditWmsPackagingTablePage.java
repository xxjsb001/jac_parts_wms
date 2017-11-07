package com.vtradex.wms.client.packagingtable;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;
import com.vtradex.thorn.client.template.BaseCustomPopupTemplate;
import com.vtradex.thorn.client.ui.ClickListener;
import com.vtradex.thorn.client.ui.CustomRemoteUI;
import com.vtradex.thorn.client.ui.commonWidgets.BeautyButton;
import com.vtradex.thorn.client.ui.table.FormTable;
import com.vtradex.thorn.client.utils.LocaleUtils;
import com.vtradex.wms.client.packagingtable.data.PackagingTableConstants;
import com.vtradex.wms.client.packagingtable.data.PackagingTableLocationDTO;
import com.vtradex.wms.client.ui.UIFactory;

/**
 * 
 * @category Custom Page
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:49 $
 */
@SuppressWarnings("rawtypes")
public class EditWmsPackagingTablePage extends BaseCustomPopupTemplate implements IsSerializable {
	
	private transient Panel container;
	private transient Panel centerPanel;
	private transient Panel bottomPanel;
	
	private transient FormTable formTable;
	private transient CustomRemoteUI packagingLocation;
	private transient CustomRemoteUI workGroup;
	private transient ConfirmButton confirmBtn;
	private transient ResetButton resetBtn;
	
	private transient MaintainWmsPackagingTablePage mwptp;
	
	public EditWmsPackagingTablePage() {
	}
	
	@Override
	public void draw(VerticalPanel content) {
		super.draw(content);
		content.setWidth("260px");
		content.setHeight("100px");

		initContainer();
		ininWidgets();
		
		content.add(container);
	}
	
	protected void initContainer() {
		formTable = new FormTable();
		centerPanel = new Panel();
		centerPanel.setBorder(false);
		centerPanel.setPaddings(4);
		centerPanel.add(formTable.getForm());
		
		bottomPanel = new Panel();
		bottomPanel.setBorder(false);
		bottomPanel.setPaddings(4);
		bottomPanel.setLayout(new HorizontalLayout(2));
		confirmBtn = new ConfirmButton();
		resetBtn = new ResetButton();
		bottomPanel.add(resetBtn);
		bottomPanel.add(confirmBtn);
		
		container = new Panel();
		container.setWidth(260);
		container.setHeight(100);
		container.setBorder(false);
		container.setLayout(new RowLayout());
		container.add(centerPanel, new RowLayoutData(70));
		
        container.add(bottomPanel, new RowLayoutData(30));
	}
	
	protected void ininWidgets() {
		String pageName = "editWmsPackagingTablePage.";
		String packagingLocationHql = "SELECT loc.id, loc.warehouseArea.code, loc.code FROM WmsLocation loc " +
				" WHERE loc.status = 'ENABLED' AND loc.type = 'SHIP' AND loc.warehouse.id = #{SESSION_WAREHOUSE} " +
				" AND (loc.code LIKE :param OR loc.warehouseArea.code LIKE :param)";
		
		packagingLocation = UIFactory.createStandardRemoteUI(pageName + "packagingLocation", packagingLocationHql, false, true, 1, 180, 3, "序号,库区,库位");
		packagingLocation.setRemoteUIConfig(UIFactory.createRemoteUIConfig(UIFactory.createTextUIConfig(pageName + "packagingLocation")));
		packagingLocation.setFocusUI(true);
		packagingLocation.setRow(1);packagingLocation.setColumn(1);
		packagingLocation.addToTable(formTable);
		
		String workGroupHql = "SELECT workGroup.id, workGroup.code, workGroup.name FROM WmsWorker workGroup " +
				" WHERE 1=1 AND workGroup.status = 'ENABLED' AND workGroup.warehouse.id = #{SESSION_WAREHOUSE} AND workGroup.type = 'GROUP' " +
				" ORDER BY workGroup.code ";
		
		workGroup = UIFactory.createStandardRemoteUI(pageName + "workGroup", workGroupHql, false, true, 1, 180, 3, "序号, 班组编码, 班组名称");
		workGroup.setRemoteUIConfig(UIFactory.createRemoteUIConfig(UIFactory.createTextUIConfig(pageName + "workGroup")));
		workGroup.setFocusUI(true);
		workGroup.setRow(2);workGroup.setColumn(1);
		workGroup.addToTable(formTable);
	}
	
	protected class ConfirmButton extends BeautyButton implements ClickListener {
		public ConfirmButton() {
			super(LocaleUtils.getText("editPackagingTablePage.confirm"));
			this.setEnabled(true);
			confirmBtn = this;
			this.addClickListener(this);
		}
		
		@SuppressWarnings("unchecked")
		public void onClick(Object obj) {
			
			if (packagingLocation.getRowData() == null) {
				MessageBox.alert("请选择包装台.");
				return;
			}
			
			if (workGroup.getRowData() == null) {
				MessageBox.alert("请选择作业班组.");
				return;
			}
			
			// 设置包装台名称
			PackagingTableLocationDTO locDTO = new PackagingTableLocationDTO();
			locDTO.setId(Long.valueOf(packagingLocation.getRowData().getColumnValue(0).toString()));
			locDTO.setCode(packagingLocation.getRowData().getColumnValue(1).toString());
			locDTO.setName(packagingLocation.getRowData().getColumnValue(2).toString());
			locDTO.setWorkGroupId(Long.valueOf(workGroup.getRowData().getColumnValue(0).toString()));
			mwptp = (MaintainWmsPackagingTablePage)EditWmsPackagingTablePage.this.getParent();
			mwptp.setLocDTO(locDTO);
			mwptp.getWestPackagingTablePanel().refreshPackagingTableName(locDTO.getName());
			
			// 获取统计信息
			Map params = new HashMap();
			params.put(PackagingTableConstants.KEY_PACKAGING, locDTO);
			mwptp.getData().remoteCallGetStatisticsInfo(params);
			
			// 获取工作人员信息
			Map wgParams = new HashMap();
			wgParams.put(PackagingTableConstants.KEY_WORKGROUPID, mwptp.getLocDTO().getWorkGroupId());
			mwptp.getData().remoteCallGetWorkerInfo(wgParams);
			
			EditWmsPackagingTablePage.this.hide();
		}
	}

	protected class ResetButton extends BeautyButton implements ClickListener {
		public ResetButton() {
			super(LocaleUtils.getText("editPackagingTablePage.reset"));
			this.setEnabled(true);
			resetBtn = this;
			this.addClickListener(this);
		}
		
		public void onClick(Object obj) {
			formTable.resetInputWidgets();
		}
	}
}
