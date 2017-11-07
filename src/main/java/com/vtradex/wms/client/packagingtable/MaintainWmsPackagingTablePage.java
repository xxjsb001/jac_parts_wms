package com.vtradex.wms.client.packagingtable;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.vtradex.thorn.client.ApplicationWindow;
import com.vtradex.thorn.client.config.page.PageConfig;
import com.vtradex.thorn.client.rpc.CatchPageConfigAsync;
import com.vtradex.thorn.client.template.BaseCustomMaintainTemplate;
import com.vtradex.thorn.client.ui.page.IPage;
import com.vtradex.thorn.client.ui.page.IPopupPage;
import com.vtradex.wms.client.packagingtable.data.PackagingTableDataAccessor;
import com.vtradex.wms.client.packagingtable.data.PackagingTableLocationDTO;
import com.vtradex.wms.client.packagingtable.panel.CenterPackagingTablePanel;
import com.vtradex.wms.client.packagingtable.panel.WestPackagingTablePanel;

/**
 * 
 *
 * @category 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:49 $
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class MaintainWmsPackagingTablePage extends BaseCustomMaintainTemplate implements IsSerializable {
	
	private PackagingTableLocationDTO locDTO;
	
	private transient WestPackagingTablePanel westPackagingTablePanel;
	private transient CenterPackagingTablePanel centerPackagingTablePanel;
	
	public void draw(com.gwtext.client.widgets.Panel content) {
		this.composites = new Vector();
		
		this.data = new PackagingTableDataAccessor(this);
		
		Panel container = new Panel();
		container.setLayout(new BorderLayout());
    	container.setBorder(false);
    	
    	BorderLayoutData westLayoutData = new BorderLayoutData(RegionPosition.WEST);
    	westLayoutData.setSplit(true);  
    	westLayoutData.setMinSize(250);  
    	westLayoutData.setMaxSize(250);  
    	westLayoutData.setMargins(new Margins(5, 5, 0, 5));
    	westPackagingTablePanel = new WestPackagingTablePanel(this, "WestPackagingTablePanel", this);
    	container.add(westPackagingTablePanel, westLayoutData);
    	
    	BorderLayoutData centerLayoutData = new BorderLayoutData(RegionPosition.CENTER);
    	centerLayoutData.setMargins(new Margins(5, 0, 5, 5));
    	centerPackagingTablePanel = new CenterPackagingTablePanel(this, "CenterPackagingTablePanel", this);
        container.add(centerPackagingTablePanel, centerLayoutData);
    	content.add(container);
    	
    	openEditWmsPackagingTablePage();
    	
    	centerPackagingTablePanel.setMoveDocCodeTextUIFocus();
	}
	
	private void openEditWmsPackagingTablePage() {
		final Map parentParams = new HashMap();
		parentParams.put(IPage.IS_EDIT_PAGE, Boolean.TRUE);
		ApplicationWindow.context.getPageConfig("editWmsPackagingTablePage", new CatchPageConfigAsync() {
			public void afterInvotion(PageConfig pageConfig) {
				ApplicationWindow.getCurrentMessageLabel().setText("");
				IPopupPage page = pageConfig.createPage(parentParams, MaintainWmsPackagingTablePage.this);;
				page.show();
				page.initData();
			}
		});
	}
	
	public PackagingTableDataAccessor getData() {
		return (PackagingTableDataAccessor)super.getData();
	}
	
	public void reload() {
		centerPackagingTablePanel.clearSnNoTextUI();
	}
	
	public WestPackagingTablePanel getWestPackagingTablePanel() {
		return westPackagingTablePanel;
	}
	
	public CenterPackagingTablePanel getCenterPackagingTablePanel() {
		return centerPackagingTablePanel;
	}

	public PackagingTableLocationDTO getLocDTO() {
		return locDTO;
	}

	public void setLocDTO(PackagingTableLocationDTO locDTO) {
		this.locDTO = locDTO;
	}
}
