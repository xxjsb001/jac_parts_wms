package com.vtradex.wms.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.VerticalLayout;
import com.gwtext.client.widgets.tree.TreeNode;
import com.vtradex.thorn.client.ApplicationWindow;
import com.vtradex.thorn.client.config.page.IMainPageConfig;
import com.vtradex.thorn.client.config.page.PageConfig;
import com.vtradex.thorn.client.config.page.ReportPageConfig;
import com.vtradex.thorn.client.context.ConfigContext;
import com.vtradex.thorn.client.rpc.AjaxServiceUtil;
import com.vtradex.thorn.client.rpc.AsyncCallBackAdapter;
import com.vtradex.thorn.client.rpc.CatchPageConfigAsync;
import com.vtradex.thorn.client.rpc.CommitServiceAsync;
import com.vtradex.thorn.client.template.AbstractCustomPageTemplate;
import com.vtradex.thorn.client.template.EditPageTemplate;
import com.vtradex.thorn.client.ui.page.IPage;
import com.vtradex.thorn.client.ui.page.IPopupPage;
import com.vtradex.thorn.client.utils.LocaleUtils;

@SuppressWarnings("all")
public class WMS extends ApplicationWindow {
	
	public static ToolbarButton changeWarehouse;
	
	public void initNativeMethod() {
	}

	public void afterInitializingUI() {
	}

	public void afterInitConfigContext() {
	}

	public void topLinkContent(final Element td) {
	}
	
	public void topLinkContent2(Element td) {
		DOM.setInnerHTML(td , "&nbsp;");
	}
	
	public void setOtherInfo(final String otherMessage) {
		changeWarehouse = new ToolbarButton(otherMessage){
			public void setText(String text) {
				super.setText("<font color='#0000FF'>" + text + "</font>");
			}
		};
		toolbar.addFill();
		toolbar.addSeparator();	
		toolbar.addButton(changeWarehouse);
		toolbar.addSeparator();
		ToolbarButton changePassword = new ToolbarButton("<font color='#0000FF'>" + LocaleUtils.getText("editUserPassword") + "</font>",new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				changePage2("editUserPassword");
		    }
		});
		toolbar.addButton(changePassword);
		AsyncCallBackAdapter acba = new AsyncCallBackAdapter() {
		    public void exec() {
			CommitServiceAsync serviceAsync
			    = AjaxServiceUtil
				  .initialAsyncService(ConfigContext.DEFAULT);
			serviceAsync.executeCustom("wmsWarehouseManager","getThisWarehouse",new HashMap(),this);
		    }
		    
		    public void onSuccess() {
		    	final Map map = (Map)result;
		    	changeWarehouse.addListener(new ButtonListenerAdapter(){
					public void onClick(Button button, EventObject e) {
						changePopPage("switchWareHousePage",map);
				    }
				});
		    	
		    }
		    public void onFailure() {}
		};
		acba.exec("");
	}

	public void bottomLinkContent2(Element td) {
	}
	
	public static void changePopPage(final String pageId,final Map map) {
		context.getPageConfig(pageId,new CatchPageConfigAsync(){
			public void afterInvotion(PageConfig pageConfig) {
				IPopupPage iPage = pageConfig.createPage(map,null);
				if(iPage instanceof AbstractCustomPageTemplate){
					((AbstractCustomPageTemplate)iPage).show();
					((AbstractCustomPageTemplate)iPage).initData();
				}
				ApplicationWindow.getCurrentMessageLabel().setText("");
				return;
			}
			
		});
		
	}
	
	public void drawNorthPanel(Panel northPanel) {
		northPanel.setHeight(60);
		northPanel.setPaddings(0);
		northPanel.setHtml("<IFRAME NAME='north_frame' width='100%'  height='100%' frameborder=0 scrolling=no src='north.html' ></IFRAME>");
	}
	
	public void drawSouthPanel(Panel southPanel) {
		southPanel.setHeight(20);
		southPanel.setPaddings(0);
		southPanel.setBorder(false);
		southPanel.setHtml("<IFRAME NAME='south_frame' width='100%' height='100%' frameborder=0 scrolling=no src='south.html' ></IFRAME>");
	}	
	
	public native Element getDownloadIframeHandle()/*-{
		return $wnd.document.getElementById('__tms_dl');
	}-*/;
	public native void setDownloadIframeSrc(String src)/*-{
		$wnd.document.getElementById('__tms_dl').src = src;
	}-*/;

	public void appendBottomCompanyName(Element td) {
		DOM.setInnerHTML(td , LocaleUtils.getText("logo.company.name"));
	}

	@Override
	public void bottomLinkContent(Element td) {
		// TODO Auto-generated method stub
		
	}
	
	public static void changePage2(final String pageId) {
		context.getPageConfig(pageId,new CatchPageConfigAsync(){

			public void afterInvotion(PageConfig pageConfig) {
				if(pageConfig instanceof ReportPageConfig){
					changPagePri3(pageConfig, new TreeNode());
				}else{
					changPagePri2(pageConfig);
				}
				
			}
			
		});
		
	}
	private static void changPagePri3(PageConfig pageConfig , TreeNode node){
		if(pageConfig instanceof IMainPageConfig){
			Panel panel = new Panel();
			panel.setAutoDestroy(true);
			panel.setLayout(new VerticalLayout());
			IPage page = ((ReportPageConfig)pageConfig).createPage(panel);
			pages.put(pageConfig.getId() , page);
			addTabPanel(page.getTitle(),panel,pageConfig.getId() , node);
		}
	}
	public static void changPagePri2(PageConfig pageConfig){
		if(!(pageConfig instanceof IMainPageConfig)){
			final IPopupPage iPage = pageConfig.createPage(new HashMap(),null);
			if(iPage instanceof EditPageTemplate){
				((EditPageTemplate)iPage).show();
				((EditPageTemplate)iPage).initData();
			}
			ApplicationWindow.getCurrentMessageLabel().setText("");
			return;
		}
	}
//	public static void changePage2(final String pageId) {
//		context.getPageConfig(pageId,new CatchPageConfigAsync(){
//
//			public void afterInvotion(PageConfig pageConfig) {
//				if(pageConfig instanceof ReportPageConfig){
//					changPagePri3(pageConfig, new TreeNode());
//				}else{
//					changPagePri2(pageConfig);
//				}
//				
//			}
//			
//		});
//		
//	}

}
