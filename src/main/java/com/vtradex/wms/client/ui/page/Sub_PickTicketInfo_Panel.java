package com.vtradex.wms.client.ui.page;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.gwtext.client.widgets.form.Label;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.thorn.client.ui.panel.AbstractSupportPanel;
import com.vtradex.thorn.client.utils.LocaleUtils;
import com.vtradex.wms.client.ui.data.Page_PA_DataAccessor;

@SuppressWarnings("unused")
public class Sub_PickTicketInfo_Panel extends AbstractSupportPanel {
	protected final static String DEFAULT_NAME = "pickTicketInfo_Panel";
		
	transient HorizontalPanel subPanel;
	transient TextBox pickTicketCodeTextBox;
	transient TextBox preAllocateQuantityTextBox;
	transient TextBox allocateQuantityTextBox;
	transient TextBox pickItemQuantityTextBox;
	
	transient Label pickTicketCodeLabel;
	transient Label preAllocateQuantityLabel;
	transient Label allocateQuantityLabel;	
	transient Label pickItemQuantityLabel;
	
	private String flag;
	
	public Sub_PickTicketInfo_Panel(IMessagePage page) {
		super(page, DEFAULT_NAME);
		this.drawWidget();
	}
	
	protected void drawWidget(){
		flag = curPADataAccessor().getFlag();
		
		subPanel = new HorizontalPanel();
		subPanel.setWidth("100%");
		
		pickTicketCodeLabel = new Label(LocaleUtils.getText("pickTicket.code"));
		pickTicketCodeTextBox = new TextBox();
//		pickTicketCodeTextBox.setEnabled(false);
		
		preAllocateQuantityLabel = new Label(LocaleUtils.getText("pickTicket.expectedQuantityBU"));
		preAllocateQuantityTextBox = new TextBox();
//		preAllocateQuantityTextBox.setEnabled(false);

		allocateQuantityLabel = new Label(LocaleUtils.getText("pickTicket.allocatedQuantityBU"));
		allocateQuantityTextBox = new TextBox();
//		allocateQuantityTextBox.setEnabled(false);
		
		pickItemQuantityLabel = new Label(LocaleUtils.getText("pickTicket.pickedQuantityBU"));
		pickItemQuantityTextBox = new TextBox();
//		pickItemQuantityTextBox.setEnabled(false);

		pickTicketCodeLabel.setWidth("80px");
		pickTicketCodeTextBox.setWidth("160px");
		
		preAllocateQuantityLabel.setWidth("100px");
		preAllocateQuantityTextBox.setWidth("160px");
		
		allocateQuantityLabel.setWidth("80px");
		allocateQuantityTextBox.setWidth("160px");
		
		pickItemQuantityLabel.setWidth("80px");
		pickItemQuantityTextBox.setWidth("160px");
		
		if(flag.equals("waveDocPage")){
			pickTicketCodeLabel = new Label(LocaleUtils.getText("waveDoc.code"));
		}
		subPanel.add(pickTicketCodeLabel);
		subPanel.add(pickTicketCodeTextBox);
		subPanel.add(preAllocateQuantityLabel);
		subPanel.add(preAllocateQuantityTextBox);
		subPanel.add(allocateQuantityLabel);
		subPanel.add(allocateQuantityTextBox);
		subPanel.add(pickItemQuantityLabel);
		subPanel.add(pickItemQuantityTextBox);
		
		this.setSize("950px", "25px");
		this.setBodyBorder(false);
		
		this.add(subPanel);	
	}

	public Page_PA_DataAccessor curPADataAccessor() {
		return (Page_PA_DataAccessor)super.getData();
	}

	public void doDispath(String message){
		super.doDispath(message);
		if(Page_PA_DataAccessor.INIT_PICK_TICKETS_INFO.equals(message)||Page_PA_DataAccessor.INIT_WAVE_DOC_INFO.equals(message)){
				pickTicketCodeTextBox.setText(String.valueOf(this.curPADataAccessor().currentPickTicket().getCode()));
				preAllocateQuantityTextBox.setText(String.valueOf(this.curPADataAccessor().currentPickTicket().getExpectedQuantityBU()));
				allocateQuantityTextBox.setText(String.valueOf(this.curPADataAccessor().currentPickTicket().getAllocatedQuantityBU()));
				pickItemQuantityTextBox.setText(String.valueOf(this.curPADataAccessor().currentPickTicket().getPickedQuantityBU()));			
		}
	}
}