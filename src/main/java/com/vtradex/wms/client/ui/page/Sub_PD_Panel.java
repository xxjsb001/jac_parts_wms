package com.vtradex.wms.client.ui.page;


import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;
import com.vtradex.wms.client.ui.data.Page_PA_DataAccessor;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.thorn.client.ui.panel.AbstractSupportPanel;
import com.vtradex.thorn.client.utils.LocaleUtils;

public class Sub_PD_Panel extends AbstractSupportPanel {
	private TabPanel centerPanel;
	
	transient Long processDocId;
	transient Panel allocatingPanel;
	transient Panel allocatedPanel;
	
	public Sub_PD_Panel(IMessagePage page) {
		super(page, "pa_main_panel");
		this.initialize();
	}
	
	protected void initialize(){
		centerPanel = new TabPanel();
     		
		allocatingPanel = new Panel();
		allocatingPanel.setTitle(LocaleUtils.getText("allocating"));
		allocatingPanel.setAutoWidth(true);
		allocatingPanel.setLayout(new RowLayout());
		
        Panel detailPanel = getDetailsTable();
        
		allocatingPanel.add(detailPanel, new RowLayoutData("50%"));
		
        Panel availablePanel = getAvailableTable();
        
		allocatingPanel.add(availablePanel, new RowLayoutData("50%"));
		
		centerPanel.add(allocatingPanel);
		
		allocatedPanel = new Panel();
		allocatedPanel.setTitle(LocaleUtils.getText("cancelAllocate"));
		allocatedPanel.setAutoWidth(true);
		allocatedPanel.setLayout(new RowLayout());
		
        Panel allocated1Panel = getAllocatedTable();
        
		allocatedPanel.add(allocated1Panel,new RowLayoutData("100%"));
		
		centerPanel.add(allocatedPanel);
        centerPanel.activate(0);
        
        this.setSize("950px", "500px");
        
        Panel northPanel = getNorthPanel();
        
        BorderLayoutData centerLayoutData = new BorderLayoutData(RegionPosition.CENTER);
        centerLayoutData.setMargins(new Margins(1, 1, 1, 1));

        BorderLayoutData northLayoutData = new BorderLayoutData(RegionPosition.NORTH);

        this.setBodyBorder(false);
        this.setBorder(false);
        this.setLayout(new BorderLayout());
        this.setAutoDestroy(true);

		this.add(northPanel,northLayoutData);
        this.add(centerPanel, centerLayoutData);
        
        DeferredCommand.addCommand(new Command(){
			public void execute() {
				 getData().sendMessage(Page_PA_DataAccessor.INIT_PROCESS_DOCS_INFO);
			}
        });
	}

	protected Panel getAllocatedTable() {
		return new Sub_AllocatedTable_Panel(this);
	}
	
	protected Panel getDetailsTable() {
		return new Sub_DetailsTable_Panel(this);
	}

	protected Panel getAvailableTable() {
		return new Sub_AvailableTable_Panel(this);
	}
	
	protected Panel getNorthPanel(){
		return new Sub_PickTicketInfo_Panel(this);
	}
}