package com.vtradex.wms.client.ui.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vtradex.wms.client.WMS;
import com.vtradex.wms.client.ui.page.service.CustomService;
import com.vtradex.wms.client.ui.page.service.CustomServiceAsync;
import com.vtradex.thorn.client.ApplicationWindow;
import com.vtradex.thorn.client.context.ConfigContext;
import com.vtradex.thorn.client.rpc.AjaxServiceUtil;
import com.vtradex.thorn.client.rpc.AsyncCallBackAdapter;
import com.vtradex.thorn.client.rpc.CommitServiceAsync;
import com.vtradex.thorn.client.template.BaseCustomPopupTemplate;
import com.vtradex.thorn.client.ui.ClickListener;
import com.vtradex.thorn.client.ui.HiddenUI;
import com.vtradex.thorn.client.ui.ListUI;
import com.vtradex.thorn.client.ui.commonWidgets.BeautyButton;
import com.vtradex.thorn.client.ui.panel.AbstractSupportPanel;
import com.vtradex.thorn.client.ui.table.FormTable;
import com.vtradex.thorn.client.ui.table.RowData;
import com.vtradex.thorn.client.utils.Constants;
import com.vtradex.thorn.client.utils.LocaleUtils;

@SuppressWarnings("all")
public class SwithWareHousePage extends BaseCustomPopupTemplate implements IsSerializable{
	transient FormTable formTable;
	
	transient HiddenUI userIdUI;
	transient ListUI wareHousesUI; 
	transient List wareHouses;
	
	transient SwitchButton switchButton;
	
	public void init() {
		draw();
	}
	
	public void draw() {
		AbstractSupportPanel content = new AbstractSupportPanel (this,""){};
		content.setBorder(false);
		content.add(getPagePanel());
		userIdUI.setValue((Long)params.get("userId"));
		composites.add(content);
		reloadFormData();
	}
	
	public String getTitle() {
		return LocaleUtils.getText("switchWareHousePage");
	}
	
	/** 从后台获取页面所有数据,参考参数entity的ID **/
	protected void reloadFormData(){
		final Map wareHouseParams = new HashMap();
		wareHouseParams.put("user.id",userIdUI.getValue());
		AsyncCallBackAdapter callBack = new AsyncCallBackAdapter(){

			protected void exec() {
				CommitServiceAsync serviceAsync = AjaxServiceUtil.initialAsyncService(ConfigContext.DEFAULT);
				serviceAsync.executeCustom("wmsWarehouseManager","getWmsWareHousesForThorn",wareHouseParams,this);
			}

			public void onSuccess() {
				Map resultParam = (Map)result;
				
				//设置控件值
				resultParam.remove(Constants.THORN_MESSAGE_KEY);
				initAllControlKit(resultParam);
			}
		};
		callBack.exec("");
	}
	
	/** 对取回的数据进行处理:将取回的数据写入到页面中,此功能用于修改装运单的时候初始化页面 **/
	protected void initAllControlKit(Map map){
		wareHouses = new ArrayList();
		if(map.get("wareHouses.list")!=null){
			wareHouses.addAll((Collection) map.get("wareHouses.list"));
		}
		((ListBox)wareHousesUI.getInputWidget()).clear();;
		for(int i=0; i<wareHouses.size(); i++){
			Long key = (Long) ((RowData) wareHouses.get(i)).getColumnValue(0);
			String value = (String) ((RowData) wareHouses.get(i)).getColumnValue(1);
			((ListBox) wareHousesUI.getInputWidget()).addItem(value,key.toString());
		}
//		wareHousesUI.initData(wareHouses.toArray());
	}
	
	protected Widget getPagePanel(){
		VerticalPanel pagePanel = new VerticalPanel();
		formTable = new FormTable();
		initInputUIsInForm();
		pagePanel.add(formTable.getForm());
		pagePanel.add(new SwitchButton());
		return pagePanel;
	}
	
	/** 初始化控件 **/
	protected void initInputUIsInForm() {
		initUserId();
		initWareHouses();
	}
	
	private void initWareHouses(){
		String hql = "";
		/*"select w from UserGroupWarehouse uw "
			 +"left join uw.warehouse w"
			 +"left join uw.userGroup ug"
			 +"where ug.id in (select g.id from Group g,User user where g in elements(user.groups) and user.id = ${user.id} ) and w.disabled=false"*/;
		this.wareHousesUI = this.makeListUI("wareHouse.List");
		this.wareHousesUI.setRow(1);
		this.wareHousesUI.setForceOverride(true);
		this.wareHousesUI.setForceSpace(false);
		this.wareHousesUI.setRequired(true);
		this.wareHousesUI.setHql(hql);
		this.wareHousesUI.setSpan(1);
		this.wareHousesUI.setWidth("300px");
		this.wareHousesUI.afterPropertySet();
//		this.wareHousesUI.initData(new RowData[]{});
		this.wareHousesUI.addToTable(this.formTable);
	}
	
	private void initUserId(){
		this.userIdUI = this.makeHiddenUI("user.id",false);
		this.userIdUI.addToTable(formTable);
	}
	
	/** 创建ListBox控件 **/
	private ListUI makeListUI(String id){
		ListUI listUI = new ListUI();
		listUI.setId(id);
		listUI.setTitle(LocaleUtils.getText(id));
		return listUI;
	}
	
	/** 创建hidden控件 **/
	public HiddenUI makeHiddenUI(String id,boolean reserve){
		HiddenUI ui = new HiddenUI(id,reserve);
		return ui;
	}
	
	protected class SwitchButton extends BeautyButton implements ClickListener {
		SwitchButton() {
			super(LocaleUtils.getText("switchWareHouse"));
			this.setEnabled(true);
			switchButton = this;
			this.addClickListener(this);
		}

		public void onClick(Object obj) {
//			Map map = getCommitData();
//			Object wareHouseId = map.get("wareHouseId");
			int index = ((ListBox)wareHousesUI.getInputWidget()).getSelectedIndex();
			RowData rowData = (RowData) wareHouses.get(index);
			final Object wareHouseId = rowData.getColumnValue(0);
			
			
			CustomServiceAsync asyncService = (CustomServiceAsync) GWT.create(CustomService.class);
			ServiceDefTarget endpoint = (ServiceDefTarget) asyncService;
			String server = GWT.getModuleBaseURL()+ "*.changeWarehouse";
			endpoint.setServiceEntryPoint(server);
			
			asyncService.swichWarehouse((Long)wareHouseId , new AsyncCallback<Map>(){

				public void onFailure(Throwable caught) {
					ApplicationWindow.setMessgeLabel(caught.getMessage());
				}

				public void onSuccess(Map result) {
					ApplicationWindow.aw.context.setGlobalParams((Map<String, Object>) result.get("globals"));
					ApplicationWindow.tabPanelClear();
					WMS.changeWarehouse.setText("仓库:"+result.get("warehouseName"));
					hide();
				}
				
			});
			
				  			
//			HTTPRequest.asyncGet(GWT.getModuleBaseURL()+"*.changeWarehouse?warehouseId="+wareHouseId,new ResponseTextHandler(){
//				public void onCompletion(String responseText) {
//					if(responseText.startsWith("success")){
//						ApplicationWindow.getMyTabPanel().clear();
//						SWMS.changeWarehouse.setText("仓库:"+responseText.substring(responseText.indexOf("_")+1));
//						hide();
//					}
//				}	
//			});
			
		}
	}

	public void release() {
		
	}
	
}
