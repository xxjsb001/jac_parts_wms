package com.vtradex.wms.client.ui.page.allocate.page.panel;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.gwtext.client.widgets.form.Label;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.thorn.client.utils.LocaleUtils;
import com.vtradex.wms.client.ui.page.allocate.page.AllocateConstant;
import com.vtradex.wms.client.ui.page.allocate.page.AllocateMessageConstant;
import com.vtradex.wms.client.ui.page.allocate.page.model.CustomMoveDoc;
import com.vtradex.wms.client.ui.page.allocate.page.ui.AbstractPanel;

@SuppressWarnings("unchecked")
public class PickTicketInfoNorthPanel extends AbstractPanel implements HasAlignment{
	private HorizontalAlignmentConstant horzAlign = ALIGN_CENTER;
	private VerticalAlignmentConstant vertAlign = ALIGN_MIDDLE;
	transient HorizontalPanel subPanel;
	transient TextBox pickTicketCode, preAllocateQuantity, 
		allocateQuantity, pickItemQuantity;
	transient Label pickTicketCodeLabel, preAllocateQuantityLabel, 
		allocateQuantityLabel, pickItemQuantityLabel;
	
	public PickTicketInfoNorthPanel(IMessagePage page) {
		super(page, "pickTicketInfoNorthPanel");
	}
	
	public PickTicketInfoNorthPanel(IMessagePage page, String name) {
		super(page, name);
	}

	public void init(){
		subPanel = new HorizontalPanel();
//		subPanel.setWidth("80%");
//		subPanel.setSize("963px", "465px");
		pickTicketCodeLabel = new Label(LocaleUtils.getText("pickTicket.code"));
		pickTicketCode = new TextBox();
//		pickTicketCodeTextBox.setEnabled(false);
		
		preAllocateQuantityLabel = new Label(LocaleUtils.getText("pickTicket.expectedQuantityBU"));
		preAllocateQuantity = new TextBox();
//		preAllocateQuantityTextBox.setEnabled(false);

		allocateQuantityLabel = new Label(LocaleUtils.getText("pickTicket.allocatedQuantityBU"));
		allocateQuantity = new TextBox();
//		allocateQuantityTextBox.setEnabled(false);
		
		pickItemQuantityLabel = new Label(LocaleUtils.getText("pickTicket.pickedQuantityBU"));
		pickItemQuantity = new TextBox();
//		pickItemQuantityTextBox.setEnabled(false);

//		pickTicketCodeLabel.setWidth("50px");
//		pickTicketCode.setWidth("100px");
//		
//		preAllocateQuantityLabel.setWidth("70px");
//		preAllocateQuantity.setWidth("40px");
//		
//		allocateQuantityLabel.setWidth("70px");
//		allocateQuantity.setWidth("40px");
//		
//		pickItemQuantityLabel.setWidth("70px");
//		pickItemQuantity.setWidth("40px");
		
		subPanel.add(pickTicketCodeLabel);
		subPanel.add(pickTicketCode);
		subPanel.add(preAllocateQuantityLabel);
		subPanel.add(preAllocateQuantity);
		subPanel.add(allocateQuantityLabel);
		subPanel.add(allocateQuantity);
		subPanel.add(pickItemQuantityLabel);
		subPanel.add(pickItemQuantity);
		
		subPanel.setCellHorizontalAlignment(pickTicketCodeLabel, ALIGN_CENTER);
		subPanel.setCellHorizontalAlignment(preAllocateQuantityLabel, ALIGN_CENTER);
		subPanel.setCellHorizontalAlignment(allocateQuantityLabel, ALIGN_CENTER);
		subPanel.setCellHorizontalAlignment(pickItemQuantityLabel, ALIGN_CENTER);
		
		subPanel.setCellVerticalAlignment(pickTicketCodeLabel, ALIGN_MIDDLE);
		subPanel.setCellVerticalAlignment(preAllocateQuantityLabel, ALIGN_MIDDLE);
		subPanel.setCellVerticalAlignment(allocateQuantityLabel, ALIGN_MIDDLE);
		subPanel.setCellVerticalAlignment(pickItemQuantityLabel, ALIGN_MIDDLE);
		
		subPanel.setCellWidth(pickTicketCodeLabel, "50px");
		subPanel.setCellWidth(pickTicketCode, "50px");
		
//		subPanel.setCellWidth(preAllocateQuantityLabel, "60px");
//		subPanel.setCellWidth(preAllocateQuantity, "40px");
//		
//		subPanel.setCellWidth(allocateQuantityLabel, "60px");
//		subPanel.setCellWidth(allocateQuantity, "40px");
//		
//		subPanel.setCellWidth(pickItemQuantityLabel, "60px");
//		subPanel.setCellWidth(pickItemQuantity, "40px");
	}
	
	public void draw() {
//		setSize("100%", "25px");
		this.setHeight("25px");
		this.setBodyBorder(false);
		
		this.add(subPanel);
	}
	
	private void moveDocInfoByLoadSuccess() {
		CustomMoveDoc moveDocInfo = (CustomMoveDoc)getData().getMoveDocData().get(AllocateConstant.CLIENT_ENTITY);
		pickTicketCode.setText(moveDocInfo.getCode());
		preAllocateQuantity.setText(String.valueOf(moveDocInfo.getPlanQuantityBU()));
		allocateQuantity.setText(String.valueOf(moveDocInfo.getAllocatedQuantityBU()));
		pickItemQuantity.setText(String.valueOf(moveDocInfo.getMovedQuantityBU()));
	}
	
	public void doDispath(String message){
		if(AllocateMessageConstant.MSG_INIT_PAGE.equals(message)){
			moveDocInfoByLoadSuccess();
		} else if (AllocateMessageConstant.MSG_MOVE_DOC_DATA_CHANGE.equals(message)) {
			if(getData().getMoveDocData() == null) {
				reset();
			} else {
				moveDocInfoByLoadSuccess();
			}
		}
	}
	
	private void reset() {
		pickTicketCode.setText("");
		preAllocateQuantity.setText("");
		allocateQuantity.setText("");
		pickItemQuantity.setText("");
	}

	public HorizontalAlignmentConstant getHorizontalAlignment() {
		return horzAlign;
	}

	public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
		horzAlign = align;
	}

	public VerticalAlignmentConstant getVerticalAlignment() {
		return vertAlign;
	}

	public void setVerticalAlignment(VerticalAlignmentConstant align) {
		vertAlign = align;
	}
}
