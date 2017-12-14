package com.vtradex.wms.client.scanPickOver;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.RootPanel;
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
/**备料交接*/
public class EditScanPickOver extends BaseCustomPopupTemplate implements IsSerializable{
	public static final String INFO = "EditScanPickOver";
	public static final String SAVE_MANAGER = "wmsWorkDocManager";
	public static final String SAVE_METHOD = "getWmsScanPickOver";
	public static final String SAVE_METHOD_C = "getWmsScanContainer";
	public static final String PARAM_CODE = "PARAM_CODE";//保存拣货单号
	public static final String PARAM_CONTAINER = "PARAM_CONTAINER";//保存容器编码
	public static final String RETURN_NAME = "RETURN_NAME";		// 返回的值
	protected transient String printUrl = "";
	protected transient Integer reportPrintNum = 0;
	
	protected transient VerticalPanel mainPanel;
	protected transient FormTable formTable;
//	protected transient TextUI pickCode;
	protected transient TextUI container;
	protected transient TextAreaUI showLocation;
	protected transient SaveButton saveButton;
	protected transient Panel bottomPanel;
	protected transient String mesInfo = "";
	protected transient String mesMess = "";
	protected transient Map inputUIValues = new HashMap();
	
	
	
	public static final String RETURN_PRINT_URL = "RETURN_PRINT_URL";
	public static final String REPORT_PRINT_NUM = "REPORT_PRINT_NUM";
	
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
	}
	protected void initCodeUI(){
		formTable =	new FormTable();
//		pickCode = GxtUIFactory.createTextUI("pickCode", false, false, 1, 250, false);
//		pickCode.addToTable(formTable, 1, 1);
//		((TextBox)pickCode.getInputWidget()).addKeyboardListener(new KeyboardListener() {
//			public void onKeyUp(Widget sender, char keyCode, int modifiers) {
//				if(KeyboardListener.KEY_ENTER == keyCode){
//					checkInputPickCode();
//				}
//			}
//			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
//			}
//			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
//			}
//		});
		container = GxtUIFactory.createTextUI("wts.container", false, false, 1, 250, false);
		container.addToTable(formTable, 2, 1);
		((TextBox)container.getInputWidget()).addKeyboardListener(new KeyboardListener() {
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
	}
	@SuppressWarnings("unchecked")
	protected void storeInputUIValues(){
		mesInfo = container.getText().trim();
		if(mesInfo.isEmpty()){
			return;
		}
		inputUIValues.put(PARAM_CONTAINER, mesInfo);
//		inputUIValues.put(PARAM_CODE, pickCode.getText().trim());
		getData().getWmsScanContainer(inputUIValues);
	}
//	protected void checkInputPickCode() {
//		mesInfo = pickCode.getText().trim();
//		if(mesInfo.isEmpty()){
//			return;
//		}
//		container.setOnFocus();
//	}
//	protected void resetpickCode(){
//		pickCode.setValue("");
//		pickCode.setOnFocus();
//	}
//	protected void focusPickCode(){
//		pickCode.setValue("");
//		container.setValue("");
//		pickCode.setOnFocus();
//	}
	protected void focusContainer(){
		container.setValue("");
		container.setOnFocus();
	}
//	protected void hiden(){
//		pickCode.setVisible(false);
//	}
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
			showLocation.setValue(mesMess);
			ApplicationWindow.setMessgeLabel(mesMess, 180);
//			hiden();
			focusContainer();
//			Window.alert(mesMess);
		}else if(BusinessNode.ON_SUCCESS.equals(message)){
			mesMess = getData().result.get(RETURN_NAME).toString();
			showLocation.setValue(mesMess);
			ApplicationWindow.setNormalMessageLabel("操作成功!", 180);
			reportPrintNum = Integer.valueOf(getData().result.get(REPORT_PRINT_NUM).toString());
			printUrl = getData().result.get(RETURN_PRINT_URL).toString();
			directPrint(printUrl, reportPrintNum);
			focusContainer();
		}else{
			mesMess = "未知错误,请联系信息部!";
			ApplicationWindow.setMessgeLabel(mesMess, 180);
			focusContainer();
			Window.alert(mesMess);
		}
	}
	protected void directPrint(String printUrl,Integer reportPrintNum){
		for(int i = 0 ; i< reportPrintNum ; i++){
			Frame export = new Frame();
			export.setSize("0px","0px");
			export.setUrl(printUrl);
			RootPanel.get().add(export);
		}
		
	}
}
