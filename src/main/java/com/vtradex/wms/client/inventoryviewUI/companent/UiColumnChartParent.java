package com.vtradex.wms.client.inventoryviewUI.companent;


import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.PopupPanel;
import com.gwtext.client.widgets.ToolTip;
import com.vtradex.wms.client.inventoryviewUI.util.Ui_Pie_Chart_Util;
import com.vtradex.wms.client.ui.javabean.JB_IV;
import com.vtradex.wms.client.ui.javabean.JB_Zone_IV;

public class UiColumnChartParent extends PopupPanel {
	protected static final int DEFAULT_WIDTH = 15;
	protected static final int DEFAULT_HEIGHT = 50;
	protected static final String DEFAULT_UI_CC_SUBPANEL_STYLE = "ui_cc_subpanel_style";
	protected static final String DEFAULT_UI_CC_MAINPANEL_STYLE = "ui_cc_mainpanel_style";
	protected static final String DEFAULT_VERTICAL_PANEL = "iv_cc_vertical_panel";
	
	protected void applyToolTips(IsSerializable iv) {
		if(iv == null) return;
		ToolTip tt = null;
		if(iv instanceof JB_IV) {
			tt = new ToolTip(Ui_Pie_Chart_Util.generateChartToolTip((JB_IV)iv));
		} else if(iv instanceof JB_Zone_IV) {
			tt = new ToolTip(Ui_Pie_Chart_Util.generatePieToolTip((JB_Zone_IV)iv));
		}
		if(tt != null) {
			tt.setWidth(300);
			tt.setMinWidth(200);
			tt.setMaxWidth(500);
			tt.applyTo(this.getElement());
		}
			
	}
}
