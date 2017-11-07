package com.vtradex.wms.client.customUI;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.Widget;
import com.vtradex.thorn.client.config.page.TableConfig;
import com.vtradex.thorn.client.ui.RemoteUI;
import com.vtradex.thorn.client.ui.page.IPage;

/**
 * @author: 李炎
 */
public class CustomRemoteUI extends RemoteUI{

	public CustomRemoteUI(IPage ownerPage) {
		super(ownerPage);
	}
	
	protected void addTextFocusListener() {
		textBox.addFocusListener(new FocusListener(){

			public void onFocus(Widget sender) {
				if(rowData!=null) {
					textBox.setText(rowData.getColumnValue(1).toString());
				}
//				textBox.setText(code);
				textBox.setStyleName("popwin_input01_on");
			}

			public void onLostFocus(Widget sender) {
				String value = textBox.getText().trim();
				if(value == null || value.equals("")){
					resetValue();

					textBox.setStyleName("popwin_input01");
					return;
				}
				if(rowData!=null && value.equals(rowData.getColumnValue(1).toString()) ) {
					if(rowData.getColumnValues().size() > 2){
						textBox.setText(rowData.getColumnValue(displayColumn - 1).toString());
					}else{
						textBox.setText(rowData.getColumnValue(1).toString());
					}
					textBox.setStyleName("popwin_input01");
					return;
				}
				isShow = false;
				if(getPercentSignPosition().equals("both"))
					invokeRemoteQuery("%"+textBox.getText()+"%");
				else if(getPercentSignPosition().equals("left"))
					invokeRemoteQuery("%"+textBox.getText());
				else
					invokeRemoteQuery(textBox.getText()+"%");
				textBox.setStyleName("popwin_input02");
			}
		});

		image.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				textBox.setFocus(false);
				isShow = false;
				SelectorTemplate template = new CustomSelectorTemplate(ownerPage,title,true);
				template.setPopupPosition(image.getAbsoluteLeft(),image.getAbsoluteTop(),true);
				template.draw();
				template.query();
			}
			
		});
		
	}
	
	protected void invokeRemoteQuery(final String value){
		if(value == null || value.equals("")){
			entityId = null;
			code = "";
			return;
		}
		SelectorTemplate template = new CustomSelectorTemplate(ownerPage,title,false);
		template.setPopupPosition(image.getAbsoluteLeft(),image.getAbsoluteTop(),true);
		template.draw();
		template.query(value);
	}
	
	public SelectorTableUI getSelectorTableUI(SelectorTemplate s,TableConfig t,IPage page){
		return  new CustomSelectorTableUI(s,t , page);
	}
	
	public class CustomSelectorTemplate extends SelectorTemplate{

		public CustomSelectorTemplate(IPage parentPage, String uiTitle, boolean onImageClick) {
			super(parentPage, uiTitle, onImageClick);
		}
		
	}
	
	public class CustomSelectorTableUI extends SelectorTableUI{

		public CustomSelectorTableUI(SelectorTemplate template, TableConfig tableConfig, IPage ownerPage) {
			super(template, tableConfig, ownerPage);
		}
	}
}