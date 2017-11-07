package com.vtradex.wms.client.scanBol;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.vtradex.thorn.client.ApplicationWindow;
import com.vtradex.thorn.client.template.BaseCustomPopupTemplate;
import com.vtradex.thorn.client.ui.ClickListener;
import com.vtradex.thorn.client.ui.TextAreaUI;
import com.vtradex.thorn.client.ui.TextUI;
import com.vtradex.thorn.client.ui.commonWidgets.BeautyButton;
import com.vtradex.thorn.client.ui.table.FormTable;
import com.vtradex.wms.client.scanBol.businessObject.BusinessNode;
import com.vtradex.wms.client.scanBol.date.Scan_DataAccessor;
import com.vtradex.wms.client.ui.page.allocate.page.utils.GxtUIFactory;

public class EditWmsScanBol extends BaseCustomPopupTemplate implements IsSerializable{
	public static final String INFO = "EditWmsScanBol";
	public static final String SAVE_MANAGER = "wmsWorkDocManager";
	public static final String SAVE_METHOD = "getWmsScanBolGwt";//pickShipAll
	public static final String PARAM_CODE = "PARAM_CODE";
	public static final String RETURN_NAME = "RETURN_NAME";		// 返回的值
	
	/*protected transient VerticalPanel mainPanel;
	protected transient FormTable formTable;
	protected transient TextUI bolCode;
	protected transient Panel panel;
	protected transient Panel panel1;
	protected transient Map inputUIValues = new HashMap();
	protected transient String mesInfo = "";
	protected transient String mesMess = "";*/
	
	protected transient VerticalPanel mainPanel;
	protected transient FormTable formTable;
	protected transient TextUI bolCode;
	protected transient TextAreaUI showLocation;
	protected transient SaveButton saveButton;
	protected transient Panel bottomPanel;
	protected transient String mesInfo = "";
	protected transient String mesMess = "";
	protected transient Map inputUIValues = new HashMap();
	public static final String MESSAGE_VIN_ERROR = "MESSAGE_VIN_ERROR";
	public static final String MESSAGE_SYS_ERROR = "MESSAGE_SYS_ERROR";
	public static final String RETURN_LOCATION_CODE = "RETURN_LOCATION_CODE";
	
	public Scan_DataAccessor getData() {
		return (Scan_DataAccessor) super.getData();
	}
	public void draw(VerticalPanel content) {
		super.draw(content);
		content.setSize("600px", "350px");
		data = new Scan_DataAccessor(this);
		mainPanel = new VerticalPanel();
		mainPanel.setSize("600px", "350px");
		content.add(mainPanel);
		initCodeUI();
		/*super.draw(content);
		content.setSize("600px", "200px");
		data = new Scan_DataAccessor(this);
		mainPanel = new VerticalPanel();
		mainPanel.setSize("600px", "200px");
		content.add(mainPanel);
		formTable =	new FormTable();
		panel = new Panel();  
        panel.setBorder(false);  
        panel.setPaddings(15);
        initCodeUI();*/
	}
	protected void initCodeUI(){
		formTable =	new FormTable();
		bolCode = GxtUIFactory.createTextUI("bolCode", false, false, 1, 250, false);
		bolCode.addToTable(formTable, 1, 1);
		((TextBox)bolCode.getInputWidget()).addKeyboardListener(new KeyboardListener() {
			public void onKeyUp(Widget sender, char keyCode, int modifiers) {
				if(KeyboardListener.KEY_ENTER == keyCode){
					storeInputUIValues();
				}
			}
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
			}
			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
			}
		});
		showLocation = GxtUIFactory.createTextAreaUI("info", true, false, 150, 150, 2);
		showLocation.addToTable(formTable, 4, 1);
		showLocation.getInputWidget().setStyleName("xx_lz");
		mainPanel.add(formTable.getForm());
		bottomPanel = new Panel();
        bottomPanel.setBorder(false);
        bottomPanel.setPaddings(0);
        bottomPanel.setLayout(new HorizontalLayout(2));
        saveButton = new SaveButton();
        bottomPanel.add(saveButton);
        mainPanel.add(bottomPanel);
		/*bolCode = GxtUIFactory.createTextUI("bolCode", false, false, 1);//.createTextUI("vin", false, false, 1, 200, false);
		bolCode.addToTable(formTable, 1, 1);
		((TextBox)bolCode.getInputWidget()).setFocus(true);
		((TextBox)bolCode.getInputWidget()).addKeyboardListener(new KeyboardListener() {
			public void onKeyUp(Widget sender, char keyCode, int modifiers) {
				if(KeyboardListener.KEY_ENTER == keyCode){
					storeInputUIValues();
					bolCode.setOnFocus();
				}
			}
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
			}
			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
			}
		});
		panel.add(formTable.getForm());
		panel1 = new Panel();
        panel1.setBorder(true);  
        panel1.setBodyStyle("background-color:#e6e6e6;font:normal 12px/15px '宋体';");//background-color:#000000;color:#FF0000;
        panel1.setSize(400, 50);
        panel.add(panel1);
        mainPanel.add(panel);*/
	}
	@SuppressWarnings("unchecked")
	protected void storeInputUIValues() {
		mesInfo = bolCode.getText().trim();
		if(mesInfo.isEmpty()){
			return;
		}
		inputUIValues.put(PARAM_CODE, mesInfo);
		getData().getWmsScanBol(inputUIValues);
	}
	protected void reset(){
		bolCode.setValue("");
		bolCode.setOnFocus();
	}
	protected void hiden(){
		bolCode.setVisible(false);
	}
	protected void initLED(String message){
//		panel1.setHtml("<p>"+message);
	}
	/** 保存 */
	protected class SaveButton extends BeautyButton implements ClickListener {
		
		public SaveButton() {
			super("保存");
			saveButton = this;
			this.addClickListener(this);
		}
		
		public void onClick(Object sender) {
			storeInputUIValues();
		}
	}
	public void doDispath(String message) {
		if(BusinessNode.SYS_ERROR.equals(message)){
			mesMess = getData().result.get(RETURN_NAME).toString();
//			initLED(mesMess);
			showLocation.setValue(mesMess);
			ApplicationWindow.setMessgeLabel(mesMess, 180);
			hiden();
			Window.alert(mesMess);
		}else if(BusinessNode.ON_SUCCESS.equals(message)){
			mesMess = getData().result.get(RETURN_NAME).toString();
//			initLED(mesMess);
			showLocation.setValue(mesMess);
			ApplicationWindow.setNormalMessageLabel("操作成功!", 180);
			reset();
		}else{
			mesMess = "未知错误,请联系信息部!";
			ApplicationWindow.setMessgeLabel(mesMess, 180);
			reset();
			Window.alert(mesMess);
		}
	}
}
