package com.vtradex.wms.client.ui.page.allocate.page.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.core.UrlParam;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.vtradex.thorn.client.utils.LocaleUtils;
import com.vtradex.wms.client.ui.page.allocate.page.AllocateConstant;
import com.vtradex.wms.client.ui.page.allocate.page.data.AllocateDataAccessor;
import com.vtradex.wms.client.ui.page.allocate.page.model.CustomMoveDoc;
import com.vtradex.wms.client.ui.page.allocate.page.model.CustomMoveDocDetail;
import com.vtradex.wms.client.ui.page.allocate.page.ui.AbstractManualAllocateGridPanel;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ManualAllocateSearchPanel extends FormPanel {
	public static final String BOOLEAN_TYPE = "boolean";
	public static final String STRING_TYPE = "string";
	private Button query;
	private Field focusUI = null;
	private FieldSet conditionFieldSet;
	private AbstractManualAllocateGridPanel gridPanel;
	private List<Field> conditionList = new ArrayList<Field>();
	
	public ManualAllocateSearchPanel() {
		super();
	}
	
	public ManualAllocateSearchPanel(AbstractManualAllocateGridPanel gridPanel) {
		super();
		this.gridPanel = gridPanel;
		draw();
	}
	
	public void draw(){
		setFrame(true);  
	    setBorder(false);
	    setWidth(450);
	    setHeight(300);
	    setLabelWidth(80);
	    setAutoScroll(true);
	    setButtonAlign(Position.LEFT);
	    setPaddings(5);
	    
	    conditionFieldSet = new FieldSet(LocaleUtils.getText("manualPickingAllocatePage.search.conditions"));
	    conditionFieldSet.setCollapsible(true);  
	    conditionFieldSet.setAutoHeight(true); 
	    conditionFieldSet.setWidth(425);
	    conditionFieldSet.setPaddings(5);
	    
	    Map<String, String> conditionItems = new HashMap<String, String>();
	    if(gridPanel instanceof PickTicketDetailsPanel) {
	    	//移位明细按照货品编码和货品名称查询
	    	conditionItems.put("manualPickingAllocatePage.item.code", ManualAllocateSearchPanel.STRING_TYPE);
	    	conditionItems.put("manualPickingAllocatePage.item.name", ManualAllocateSearchPanel.STRING_TYPE);
//	    	conditionItems.put("manualPickingAllocatePage.shipLotInfo", ManualAllocateSearchPanel.STRING_TYPE);
	    } else if(gridPanel instanceof PickAvailableInventoryPanel) {
	    	//可用库存按照库位编码和库存状态查询
	    	conditionItems.put("manualPickingAllocatePage.locationCode", ManualAllocateSearchPanel.STRING_TYPE);
//	    	conditionItems.put("manualPickingAllocatePage.item.code", ManualAllocateSearchPanel.STRING_TYPE);
//	    	conditionItems.put("manualPickingAllocatePage.item.name", ManualAllocateSearchPanel.STRING_TYPE);
	    	conditionItems.put("manualPickingAllocatePage.inventoryStatus", ManualAllocateSearchPanel.STRING_TYPE);
//	    	conditionItems.put("manualPickingAllocatePage.lotInfo", ManualAllocateSearchPanel.STRING_TYPE);
	    } else if(gridPanel instanceof AllocatedPickInfoPanel) {
	    	//已分配任务按照库位编码、货品编码和货品名称查询
	    	conditionItems.put("manualPickingAllocatePage.locationCode", ManualAllocateSearchPanel.STRING_TYPE);
	    	conditionItems.put("manualPickingAllocatePage.item.code", ManualAllocateSearchPanel.STRING_TYPE);
	    	conditionItems.put("manualPickingAllocatePage.item.name", ManualAllocateSearchPanel.STRING_TYPE);
//	    	conditionItems.put("manualPickingAllocatePage.lotInfo", ManualAllocateSearchPanel.STRING_TYPE);
	    }
	    drawSearchConditions(conditionItems);
	    this.add(conditionFieldSet);
	    query = new Button(LocaleUtils.getText("query"),new ButtonListenerAdapter(){
    		public void onClick(Button button, EventObject e) {
    			queryDetail();
    	    }
    	});
		addButton(query);
	}
	
	private void drawSearchConditions(Map<String, String> conditionItems) {
		for(String item : conditionItems.keySet()) {
			String type = conditionItems.get(item);
			Field fieldUI = null;
			if(type.equals(ManualAllocateSearchPanel.STRING_TYPE)){
				TextField textField =  new TextField(LocaleUtils.getText(item), item,200);
				textField.addListener(new TextFieldListenerAdapter(){
					public void onFocus(Field field) {
						((TextField)field).selectText();
				    }
				});
				fieldUI = textField;
			} else if(type.equals(ManualAllocateSearchPanel.BOOLEAN_TYPE)){
				Checkbox checkbox = new Checkbox(LocaleUtils.getText(item), item);
				fieldUI = checkbox;
			}
//			if(fieldUI instanceof TextField 
//				&& (item.contains("lotInfo") || item.contains("LotInfo"))) {
//				fieldUI.setValue("-");
//			}
			conditionList.add(fieldUI);
			conditionFieldSet.add(fieldUI);
			if(focusUI == null){
				focusUI = fieldUI;
			}
		}
	}
	
	private void queryDetail() {
		UrlParam urlparams[] = new UrlParam[conditionList.size()];
		int j = 0;
		for(Field field : conditionList){
			String prefix = "manualPickingAllocatePage.";
			int pos = field.getName().indexOf(prefix);
			urlparams[j++] = new UrlParam(field.getName().substring(pos + prefix.length()),field.getValueAsString());
		}
		if(gridPanel instanceof PickTicketDetailsPanel) {
			findMoveDocDetails(urlparams);
	    } else if(gridPanel instanceof PickAvailableInventoryPanel) {
	    	findAvailableInventories(urlparams);
	    } else if(gridPanel instanceof AllocatedPickInfoPanel) {
	    	findAllocatedInfos(urlparams);
	    }
		gridPanel.hideDialog();
	}
	
	
	private void findMoveDocDetails(UrlParam[] baseParams) {
		if(gridPanel.getData() == null) {
			return;
		}
		Map<String, String> queryParams = getQueryParams(baseParams);
		CustomMoveDoc customMoveDoc = ((AllocateDataAccessor)gridPanel.getData()).getCustomMoveDocByDetail();
		Map params = new HashMap();
		params.put(AllocateConstant.PARENT_ID, customMoveDoc.getId());
		params.put(AllocateConstant.QUERY_PARAMS, queryParams);
		((AllocateDataAccessor)gridPanel.getData()).initPickTicketDetailInfo(params);
	}
	
	private void findAvailableInventories(UrlParam[] baseParams) {
		Map<String, String> queryParams = getQueryParams(baseParams);
		CustomMoveDocDetail customMoveDocDetail = ((AllocateDataAccessor)gridPanel.getData()).getPickAvailableDetail();
		if(customMoveDocDetail == null) {
			return;
		}
		Map params = new HashMap();
		params.put(AllocateConstant.ID, customMoveDocDetail.getId());
		params.put(AllocateConstant.IS_CLEAR_INVENTORIES, Boolean.FALSE);
		params.put(AllocateConstant.IS_FIT_AS_LOT, customMoveDocDetail.isFitAsLot());
		params.put(AllocateConstant.CONTAIN_LOCK_INV, customMoveDocDetail.isContainLockInv());
		params.put(AllocateConstant.QUERY_PARAMS, queryParams);
		((AllocateDataAccessor)gridPanel.getData()).findAvailabeInventories(params);
	}
	
	private void findAllocatedInfos(UrlParam[] baseParams) {
		Map<String, String> queryParams = getQueryParams(baseParams);
		CustomMoveDoc customMoveDoc = ((AllocateDataAccessor)gridPanel.getData()).getCustomMoveDocByTask();
		if(customMoveDoc == null) {
			return;
		}
		Map params = new HashMap();
		params.put(AllocateConstant.PARENT_ID, customMoveDoc.getId());
		params.put(AllocateConstant.QUERY_PARAMS, queryParams);
		((AllocateDataAccessor)gridPanel.getData()).initAllocatedInfo(params);
	}
	
	private Map<String, String> getQueryParams(UrlParam[] baseParams) {
		Map<String, String> queryParams = new HashMap<String, String>();
		for(int index = 0 ; index < baseParams.length ; index ++) {
			if(!"".equals(baseParams[index].getValue())){
				queryParams.put(baseParams[index].getName(),"%"+baseParams[index].getValue() + "%");
			}
		}
		return queryParams;
	}
}
