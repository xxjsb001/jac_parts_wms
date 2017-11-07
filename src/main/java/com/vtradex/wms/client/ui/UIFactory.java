package com.vtradex.wms.client.ui;

import java.util.ArrayList;

import com.vtradex.thorn.client.config.page.RemoteUIConfig;
import com.vtradex.thorn.client.config.page.TextUIConfig;
import com.vtradex.thorn.client.config.page.UIConfig;
import com.vtradex.thorn.client.ui.ComplexDUI;
import com.vtradex.thorn.client.ui.CustomRemoteUI;
import com.vtradex.thorn.client.ui.DateRangerUI;
import com.vtradex.thorn.client.ui.DateUI;
import com.vtradex.thorn.client.ui.HiddenUI;
import com.vtradex.thorn.client.ui.ListUI;
import com.vtradex.thorn.client.ui.TextUI;
import com.vtradex.thorn.client.ui.page.IPage;
import com.vtradex.thorn.client.utils.LocaleUtils;
import com.vtradex.thorn.client.validate.RequiredValidator;
import com.vtradex.thorn.client.validate.Validator;

/**
 * UI Factory
 *
 * @author <a href="mailto:ningbo.pan@gmail.com">潘宁波</a>
 * @since 2011-7-15
 */
@SuppressWarnings("rawtypes")
public class UIFactory {
	
	public static TextUI createTextUI(String id, boolean readOnly, boolean required, int span) {
		TextUI textUI = new TextUI();
		textUI.setId(id);
		textUI.setTitle(LocaleUtils.getText(id));
		textUI.setReadOnly(readOnly);
		textUI.setSpan(span);
		textUI.setRequired(required);
		if ( required ) {
			RequiredValidator rv = new RequiredValidator();
			rv.setMessageKey(Validator.REQUIRED);
			textUI.setValidator(rv);
		}
		textUI.afterPropertySet();
		return textUI;
	}
	
	public static TextUI createTextUI(String id, boolean readOnly, boolean required, int span, int wdith){
		TextUI textUI = createTextUI(id, readOnly, required, span);
		textUI.setWidth(wdith + "px");
		return textUI;
	}
	
	public static DateUI createDateUI(String id, boolean readOnly, boolean required, boolean showTime, boolean defaultCurrentDate, int span, int wdith) {
		DateUI dateUI = new DateUI(id, LocaleUtils.getText(id));
		dateUI.setSpan(span);
		dateUI.setReadOnly(readOnly);
		dateUI.setRequired(required);
		dateUI.setShowTime(showTime);
		dateUI.setEditable(true);
		dateUI.setManualEditable(true);
		dateUI.setDefaultCurrentDate(defaultCurrentDate);
		if ( required ) {
			RequiredValidator rv = new RequiredValidator();
			rv.setMessageKey(Validator.REQUIRED);
			dateUI.setValidator(rv);
		}
		dateUI.afterPropertySet();
		return dateUI;
	}
	
	public static DateRangerUI createDateRangerUI(String id, String fromDate, String toDate, boolean readOnly, boolean required, boolean showTime, boolean defaultCurrentDate, int span, int wdith){
		DateRangerUI dateUI = new DateRangerUI();
		dateUI.setId(id);
		dateUI.setTitle(LocaleUtils.getText(id));
		dateUI.setReadOnly(readOnly);
		dateUI.setRequired(required);
		dateUI.setDefaultCurrentDate(defaultCurrentDate);
		dateUI.setSpan(span);
		dateUI.setShowTime(showTime);
		dateUI.setEditable(true);
		dateUI.setManualEditable(true);
		dateUI.setFromDate(fromDate);
		dateUI.setToDate(toDate);
		dateUI.setDateRanger(-1);
		if(required) {
			RequiredValidator rv = new RequiredValidator();
			rv.setMessageKey(Validator.REQUIRED);
			dateUI.setValidator(rv);
		}
		dateUI.afterPropertySet();
		return dateUI;
	}
	
	public static HiddenUI createHiddenUI(String id, boolean reserve){
		HiddenUI ui = new HiddenUI(id, reserve);
		return ui;
	}
	
	public static ListUI createListUI(String id, String enumType, boolean readOnly, boolean required, int span, int width) {
		String hql = " SELECT e.enumValue, e.enumValue FROM Enumerate e where e.enumType = '" +  enumType + "' order by e.id ";
		ListUI listUI = createListUI(id, readOnly, required, span);
		listUI.setHql(hql);
		listUI.setForceOverride(true);
		listUI.setForceSpace(false);
		listUI.setEnumType(enumType);
		listUI.setSpan(span);
		listUI.setWidth(width + "px");
		listUI.afterPropertySet();
		listUI.initData();
		if( required ) {
			listUI.setValidator(new RequiredValidator());
		}
		return listUI;
	}
	
	private static ListUI createListUI(String id, boolean readOnly, boolean required, int span) {
		ListUI listUI = new ListUI();
		listUI.setId(id);
		listUI.setTitle(LocaleUtils.getText(id));
		listUI.setReadOnly(readOnly);
		listUI.setRequired(required);
		listUI.setSpan(span);
		if ( required ) {
			listUI.setValidator(new RequiredValidator());
		}
		return listUI;
	}
	
	public static CustomRemoteUI createStandardRemoteUI(String id, String hql, boolean readOnly, boolean required, int span, int width, int displayColumn, String heads) {
		CustomRemoteUI remoteUI = new CustomRemoteUI(null);
		remoteUI.setId(id);
		remoteUI.setTitle(LocaleUtils.getTextWithoutException(id));
		remoteUI.setReadOnly(readOnly);
		remoteUI.setRequired(required);
		remoteUI.setSpan(span);
		remoteUI.setWidth(width + "px");
		remoteUI.setManualEditable(true);
		remoteUI.setForceOverride(true);
		
		remoteUI.setDisplayColumn(displayColumn);
		remoteUI.setDisplayedTableHead(heads);
		
		remoteUI.setUis(new ArrayList());
		remoteUI.setHql(hql);
		remoteUI.afterPropertySet();
		remoteUI.setPercentSignPosition("both");
		if( required ) {
			remoteUI.setValidator(new RequiredValidator());
		}
		return remoteUI;
	}
	
	public static TextUIConfig createTextUIConfig(String id) {
		TextUIConfig config = new TextUIConfig();
		config.setId(id);
		config.setTitle(id);
		config.setPrecision(false);
		config.setColumn(2);
		config.setRow(1);
		config.setSpan(1);
		config.setReadOnly(false);
		return config;
	}
	
	public static RemoteUIConfig createRemoteUIConfig(UIConfig uiConfig) {
		RemoteUIConfig remoteUIConfig = new RemoteUIConfig();
		remoteUIConfig.addUIConfig(uiConfig);
		return remoteUIConfig;
	}
	
	public static ComplexDUI createComplex(String id, String type) {
		ComplexDUI complex = new ComplexDUI();
		complex.setId(id);
		complex.setType(type);
		return complex;
	}
}
