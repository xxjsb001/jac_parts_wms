package com.vtradex.wms.client.ui.page.allocate.page.utils;

import com.google.gwt.user.client.ui.HTML;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.TimeField;
import com.vtradex.thorn.client.config.page.RemoteUIConfig;
import com.vtradex.thorn.client.config.page.TextUIConfig;
import com.vtradex.thorn.client.ui.CheckBoxUI;
import com.vtradex.thorn.client.ui.DateRangerUI;
import com.vtradex.thorn.client.ui.DateUI;
import com.vtradex.thorn.client.ui.HiddenUI;
import com.vtradex.thorn.client.ui.ListUI;
import com.vtradex.thorn.client.ui.NumberTextUI;
import com.vtradex.thorn.client.ui.SeparatorUI;
import com.vtradex.thorn.client.ui.TextAreaUI;
import com.vtradex.thorn.client.ui.TextUI;
import com.vtradex.thorn.client.utils.LocaleUtils;
import com.vtradex.thorn.client.utils.StringUtils;
import com.vtradex.thorn.client.validate.NumberValidator;
import com.vtradex.thorn.client.validate.RequiredValidator;

/**
 * 
 * @author <a href="yan.li@vtradex.com">李炎</a>
 * @description 
 * 
 */

public class GxtUIFactory {
	
	public static TextField createTextField(String name,final boolean allowBlank,final boolean readOnly){
		return new TextField(LocaleUtils.getTextWithoutException(name),name){
			{
				setAllowBlank(allowBlank);
				setReadOnly(readOnly);
				setStyleName("popwin_gxt_input");
				setFocusClass("popwin_gxt_input_on");
				setWidth(220);
			}
		};
	}
	
	public static NumberField createNumberField(String name,final boolean allowBlank,final boolean readOnly){
		return new NumberField(LocaleUtils.getTextWithoutException(name),name){
			{
				setAllowBlank(allowBlank);
				setReadOnly(readOnly);
				setStyleName("popwin_gxt_input");
				setFocusClass("popwin_gxt_input_on");
				setWidth(220);
			}
		};
	}
	
	public static Checkbox createCheckbox(String name){
		return new Checkbox(LocaleUtils.getTextWithoutException(name),name);
	}
	
	public static ComboBox createComboBox(String name,final String valueField,final ComboBox.Trigger triggerAction){
		return new ComboBox(LocaleUtils.getTextWithoutException(name),name){
			{
				setTriggerAction(triggerAction);
				setValueField(valueField);
				setDisplayField("name");
				setMode(ComboBox.REMOTE);
				setForceSelection(true);
				setTypeAhead(true);
				setSelectOnFocus(true);
				setWidth(220);
			}
		};
	}
	
	public static TextArea createTextArea(String name,final int width, final int height){
		return new TextArea(LocaleUtils.getTextWithoutException(name),name){
			{
				setSize(width, height);
			}
		};
	}
	
	public static DateField createDateField(String name,final String format){
		return new DateField(LocaleUtils.getTextWithoutException(name),name){
			{
				setFormat(format);
				setWidth(220);
			}
		};
	}
	
	public static TimeField createTimeField(String name,final String format){
		return new TimeField(LocaleUtils.getTextWithoutException(name),name){
			{
				if(!StringUtils.isEmpty(format))
					setFormat(format);
				setWidth(220);
			}
		};
	}
	
	public static Hidden createHidden(String name){
		return new Hidden(name,null);
	}
	
	public static HTML createSeparator(){
		return new HTML("<hr size='1' />");
	}
	
	/** 创建远程查找控件 **/
//	public static RemoteUI createRemoteUI(String id,boolean readOnly,boolean required,int span){
//		CustomRemoteUI remoteUI = new CustomRemoteUI(null);
//		remoteUI.setId(id);
//		remoteUI.setTitle(LocaleUtils.getTextWithoutException(id));
//		remoteUI.setReadOnly(readOnly);
//		remoteUI.setWindowWidth("400");
//		remoteUI.setWindowHeight("250");
//		remoteUI.setRequired(required);
//		remoteUI.setSpan(span);
//		if(required)
//			remoteUI.setValidator(new RequiredValidator());
//		return remoteUI;
//	}
	
	/** 目的地查找 **/
//	public static RemoteUI createToLocationRemoteUI(String id,boolean readOnly,boolean required,int span){
////		final String hql = "SELECT tl.id,tl.code,tl.name FROM TransLocation tl WHERE 1=1"+
////				" and tl.name like :param or tl.code like :param order by tl.type";
//		final String hql="SELECT tld.location.id,tld.location.code,tld.location.name from TransLocationDetail tld where 1=1" +
//				" and tld.subLocation.platform=#{SESSION_PLATFORM} and (tld.location.name like :param or tld.location.code like :param)";
//		RemoteUI ui = createRemoteUI(id, readOnly, required,span);
//		ui.setDisplayedTableHead("ID,编码,名称");
//		ui.setManualEditable(true);
//		ui.setForceOverride(true);
//		ui.setDisplayColumn(3);
//		ui.setUis(new ArrayList());
//		ui.setHql(hql);
//		ui.afterPropertySet();
//		ui.setPercentSignPosition("both");
////		ui.setRemoteUIConfig(getRemoteUIConfig("shipment.city.name"));
//		return ui;
//	}
	
	
	/** 承运商查找 **/
//	public static RemoteUI createCarriersRemoteUI(String id,boolean readOnly,boolean required,int span){
////		final String hql="SELECT carrier.id,carrier.code,carrier.name FROM Organization carrier where 1=1 and carrier.code like '000%'  and carrier.beCarrier=1" +
////				" and carrier.id in (select od.org.id from OrganizationDetail od where od.subOrg.id=#{SESSION_PLATFORM})" +
////				" and (carrier.name like :param or carrier.code like :param)";
//		final String hql="SELECT carrier.id,carrier.code,carrier.name FROM OrganizationDetail od LEFT JOIN od.org carrier" +
//			" WHERE (carrier.name LIKE :param OR carrier.code LIKE :param)" +
//			" AND carrier.carrierType = 'ANZHUANG' AND od.subOrg.id = #{SESSION_PLATFORM} AND carrier.beCarrier = 1";
//		RemoteUI ui = createRemoteUI(id, readOnly, required,span);
//		ui.setDisplayedTableHead("ID,编码,名称");
//		ui.setManualEditable(true);
//		ui.setForceOverride(true);
//		ui.setDisplayColumn(3);
//		ui.setUis(new ArrayList());
//		ui.setHql(hql);
//		ui.afterPropertySet();
//		ui.setPercentSignPosition("both");
////		ui.setRemoteUIConfig(getRemoteUIConfig("shipment.city.name"));
//		return ui;
//	}
	
	public static DateUI createDateUI(String id,boolean readOnly,boolean required,boolean DefaultCurrentDate,int span){
		DateUI dateUI = new DateUI();
		dateUI.setId(id);
		dateUI.setTitle(LocaleUtils.getText(id));
		dateUI.setReadOnly(readOnly);
		dateUI.setRequired(required);
		dateUI.setDefaultCurrentDate(DefaultCurrentDate);
		dateUI.setSpan(span);
		dateUI.setShowTime(false);
		dateUI.setManualEditable(true);
		if(required)
			dateUI.setValidator(new RequiredValidator());
		dateUI.afterPropertySet();
		return dateUI;
	}
	
	public static DateRangerUI DateRangerUI(String id,boolean readOnly,boolean required,boolean DefaultCurrentDate,int span){
		DateRangerUI dateUI = new DateRangerUI();
		dateUI.setId(id);
		dateUI.setTitle(LocaleUtils.getText(id));
		dateUI.setReadOnly(readOnly);
		dateUI.setRequired(required);
		dateUI.setDefaultCurrentDate(DefaultCurrentDate);
		dateUI.setSpan(span);
		dateUI.setShowTime(true);
		dateUI.setManualEditable(true);
		if(required)
			dateUI.setValidator(new RequiredValidator());
		dateUI.afterPropertySet();
		return dateUI;
	}

	/** 创建hidden控件 **/
	public static HiddenUI createHiddenUI(String id,boolean reserve){
		HiddenUI ui = new HiddenUI(id,reserve);
		return ui;
	}
	
	/** 创建文本框控件 **/
	public static TextUI createTextUI(String id,boolean readOnly,boolean required,int span){
		TextUI textUI = new TextUI();
		textUI.setTitle(LocaleUtils.getText(id));
		textUI.setId(id);
		textUI.setReadOnly(readOnly);
		textUI.setRequired(required);
		textUI.setSpan(span);
		if(required)
			textUI.setValidator(new RequiredValidator());
		textUI.afterPropertySet();
		return textUI;
	}
	/** 创建文本框控件 comm **/
	public static TextUI createTextUI(String id,boolean readOnly,boolean required,int span,int wdith,boolean comm){
		TextUI textUI = new TextUI();
		textUI.setTitle(LocaleUtils.getText(id));
		textUI.setId(id);
		textUI.setWidth(wdith + "px");
		textUI.setComm(comm);
		textUI.setReadOnly(readOnly);
		textUI.setRequired(required);
		textUI.setSpan(span);
		if(required)
			textUI.setValidator(new RequiredValidator());
		textUI.afterPropertySet();
		return textUI;
	}
	public static TextUI createTextUI(String id,boolean readOnly,boolean required,int span,int wdith){
		TextUI textUI = createTextUI(id,readOnly,required,span);
		textUI.setWidth(wdith + "px");
		return textUI;
	}
	
	public static NumberTextUI createNumberTextUI(String id,boolean readOnly,boolean required,int span){
		NumberTextUI numberTextUI = new NumberTextUI();
		numberTextUI.setTitle(LocaleUtils.getText(id));
		numberTextUI.setId(id);
		numberTextUI.setReadOnly(readOnly);
		numberTextUI.setRequired(required);
		numberTextUI.setSpan(span);
		numberTextUI.setDecimal(3);
		numberTextUI.setReturnType("double");
		numberTextUI.setValidator(new NumberValidator());
		numberTextUI.afterPropertySet();
		return numberTextUI;
	}
	public static NumberTextUI createDeliveryNumberTextUI(String id,boolean readOnly,boolean required,int span){
		NumberTextUI numberTextUI = new NumberTextUI();
		numberTextUI.setTitle(LocaleUtils.getText(id));
		numberTextUI.setId(id);
		numberTextUI.setReadOnly(readOnly);
		numberTextUI.setRequired(required);
		numberTextUI.setSpan(span);
		numberTextUI.setDecimal(0);
		numberTextUI.setReturnType("int");
		numberTextUI.setValidator(new NumberValidator());
		numberTextUI.afterPropertySet();
		return numberTextUI;
	}
	
	
	/** 创建checkBox控件 **/
	public static CheckBoxUI createCheckBoxUI(String id,int span){
		CheckBoxUI checkBoxUI = new CheckBoxUI();
		checkBoxUI.setId(id);
		checkBoxUI.setTitle(LocaleUtils.getText(id));
		checkBoxUI.setSpan(span);
		checkBoxUI.afterPropertySet();
		return checkBoxUI;
	}
	
	/** 创建TextArea控件 **/
	public static TextAreaUI createTextAreaUI(String id,int rows,int cols,int span){
		TextAreaUI areaUI = new TextAreaUI();
		areaUI.setId(id);
		areaUI.setTitle(LocaleUtils.getText(id));
		areaUI.setRows(rows);
		areaUI.setSpan(span);
		areaUI.setCols(cols);
		areaUI.afterPropertySet();
		return areaUI;
	}
	/** 创建TextArea控件 **/
	public static TextAreaUI createTextAreaUI(String id,boolean readOnly,boolean required,int rows,int cols,int span){
		TextAreaUI areaUI = new TextAreaUI();
		areaUI.setId(id);
		areaUI.setTitle(LocaleUtils.getText(id));
		areaUI.setRows(rows);
		areaUI.setSpan(span);
		areaUI.setCols(cols);
		areaUI.setReadOnly(readOnly);
		areaUI.setRequired(required);
		areaUI.afterPropertySet();
		return areaUI;
	}
	
	/** 创建ListBox控件 **/
	public static ListUI createListUI(String id,boolean readOnly,boolean required){
		ListUI listUI = new ListUI();
		listUI.setId(id);
		listUI.setTitle(LocaleUtils.getText(id));
		listUI.setReadOnly(readOnly);
		listUI.setRequired(required);
		if(required)
			listUI.setValidator(new RequiredValidator());
		return listUI;
	}
	
	/** 创建separator控件 **/
	public static SeparatorUI createSeparatorUI(int row){
		SeparatorUI ui = new SeparatorUI(row);
		return ui;
	}
	
	public static ListUI createShipMethodListUI(String id,boolean readOnly,boolean required,int span){
		String hql = "SELECT enumerate.enumValue,enumerate.enumValue From Enumerate enumerate " +
		"where enumerate.enumType = 'TmsShipmentMethod' order by enumerate.id";
		ListUI ui = createListUI(id, readOnly, required);
		ui.setForceOverride(true);
		ui.setForceSpace(false);
		ui.setHql(hql);
		ui.setSpan(span);
		ui.setEnumType("TmsShipmentMethod");
		ui.setWidth("120");
		ui.afterPropertySet();
		ui.initData();
		if(required)
			ui.setValidator(new RequiredValidator());
		return ui;
	}
	
//	public static RemoteUI createDriverRemoteUI(String id,boolean readOnly,boolean required,int span){
//		final String hql = "SELECT d.id,d.phone,d.name FROM Driver d WHERE 1=1 ";
//		RemoteUI ui = createRemoteUI(id, readOnly, required,span);
//		ui.setDisplayedTableHead("ID,电话,名称");
//		ui.setManualEditable(true);
//		ui.setForceOverride(true);
//		ui.setDisplayColumn(3);
//		ui.setUis(new ArrayList());
//		ui.setHql(hql);
//		ui.afterPropertySet();
//		ui.setPercentSignPosition("both");
//		ui.setRemoteUIConfig(getRemoteUIConfig("shipment.city.name"));
//		return ui;
//	}
	
//	public static RemoteUI createCarrierRemoteUI(String id,int span){
////		final String hql = "SELECT c.id,c.code,c.name "
////							+ " FROM Organization c" 
////							+ " WHERE (c.code like :param or c.name like :param)"
////							+"/~shipment.city.name: and c.name like {shipment.city.name}~/"
////							+ " AND c.status='ACTIVE' AND c.beCarrier = true";
////							+ " AND c.id in "
////							+ " (select detail.subOrg.id from OrganizationDetail detail "
////							+ "  where detail.org =  #{SESSION_PLATFORM})";
//		
//		final String hql = "SELECT org.id,org.code,org.name FROM OrganizationDetail od LEFT JOIN od.org org" +
//			" WHERE (org.code like :param OR org.name like :param) AND org.status = 'ACTIVE' AND org.beCarrier = true AND od.subOrg = #{SESSION_PLATFORM}";
//		
//		
//		RemoteUI ui = createRemoteUI(id, false, false,span);
//		ui.setDisplayedTableHead("ID,编码,名称");
//		ui.setManualEditable(true);
//		ui.setForceOverride(true);
//		ui.setDisplayColumn(3);
//		ui.setUis(new ArrayList());
//		ui.setHql(hql);
//		ui.afterPropertySet();
//		ui.setPercentSignPosition("both");
//		ui.setRemoteUIConfig(getRemoteUIConfig("shipment.city.name"));
//		return ui;
//	}
	
//	public static RemoteUI createCustomerRemoteUI(String id,int span,int width){
//		final String hql = "SELECT c.id,c.code,c.name "
//							+ " FROM Organization c" 
//							+ " WHERE c.code like :param"
//							+ " AND c.status='ACTIVE' AND c.beCustomer = true";
////							+ " AND c.id in "
////							+ " (select detail.subOrg.id from OrganizationDetail detail "
////							+ "  where detail.org =  #{SESSION_PLATFORM})";
//		RemoteUI ui = createRemoteUI(id, false, false,span);
//		ui.setDisplayedTableHead("ID,编码,名称");
//		ui.setManualEditable(true);
//		ui.setForceOverride(true);
//		ui.setDisplayColumn(3);
//		ui.setWidth(width + "px");
//		ui.setUis(new ArrayList());
//		ui.setHql(hql);
//		ui.afterPropertySet();
//		ui.setPercentSignPosition("both");
//		ui.setRemoteUIConfig(new RemoteUIConfig());
//		return ui;
//	}
	
//	public static RemoteUI createRouteRemoteUI(String id,int span,int width){
//		final String hql = "SELECT r.id,r.name "
//							+ " FROM Route r" 
//							+ " WHERE r.code like :param"
//							+ " AND r.disabled=false AND r.platForm.id = #{SESSION_PLATFORM} ";
//		RemoteUI ui = createRemoteUI(id, false, false,span);
//		ui.setDisplayedTableHead("ID,编码,名称");
//		ui.setManualEditable(true);
//		ui.setForceOverride(true);
//		ui.setDisplayColumn(3);
//		ui.setWidth(width + "px");
//		ui.setUis(new ArrayList());
//		ui.setHql(hql);
//		ui.afterPropertySet();
//		ui.setPercentSignPosition("both");
//		ui.setRemoteUIConfig(new RemoteUIConfig());
//		return ui;
//	}
	
	public static ListUI createCustomerListUI(String id,boolean readOnly,boolean required,int span,int width){
		final String hql = "SELECT c.id,c.shortName "
			+ " FROM Organization c" 
			+ " WHERE c.status='ACTIVE' AND c.beCustomer = true";
//			+ " AND c.id in "
//			+ " (select detail.subOrg.id from OrganizationDetail detail "
//			+ "  where detail.org =  #{SESSION_PLATFORM})";
		ListUI ui = createListUI(id, readOnly, required);
		ui.setForceOverride(true);
		ui.setForceSpace(false);
		ui.setHql(hql);
		ui.setSpan(span);
		ui.setWidth(width + "px");
		ui.afterPropertySet();
		ui.initData();
		if(required)
			ui.setValidator(new RequiredValidator());
		return ui;
	}
	public static ListUI createcusTypeListUI(String id,boolean readOnly,boolean required,int span,int width){
		final String hql = "SELECT c.id,c.shortName "
			+ " FROM Organization c" 
			+ " WHERE c.status='ACTIVE' AND c.beCustomer = true";
		ListUI ui = createListUI(id, readOnly, required);
		ui.setForceOverride(true);
		ui.setForceSpace(false);
		ui.setHql(hql);
		ui.setSpan(span);
		ui.setWidth(width + "px");
		ui.afterPropertySet();
		ui.initData();
		if(required)
			ui.setValidator(new RequiredValidator());
		return ui;
	}
	public static ListUI createWavesUI(String id,boolean readOnly,boolean required,int span,int width){
		final String hql = "SELECT c.id,c.shortName "
			+ " FROM Organization c" 
			+ " WHERE c.status='ACTIVE' AND c.beCustomer = true";
		ListUI ui = createListUI(id, readOnly, required);
		ui.setForceOverride(true);
		ui.setForceSpace(false);
		ui.setHql(hql);
		ui.setSpan(span);
		ui.setWidth(width + "px");
		ui.afterPropertySet();
		ui.initData();
		if(required)
			ui.setValidator(new RequiredValidator());
		return ui;
	}
	
	public static ListUI createRouteListUI(String id,boolean readOnly,boolean required,int span,int width){
		final String hql = "SELECT r.id,r.name "
			+ " FROM Route r" 
			+ " WHERE r.disabled=false AND r.platForm.id = #{SESSION_PLATFORM} ";
		ListUI ui = createListUI(id, readOnly, required);
		ui.setForceOverride(true);
		ui.setForceSpace(false);
		ui.setHql(hql);
		ui.setSpan(span);
		ui.setWidth(width + "px");
		ui.afterPropertySet();
		ui.initData();
		if(required)
			ui.setValidator(new RequiredValidator());
		return ui;
	}
	
	public static ListUI createVehicleStatusListUI(String id,boolean readOnly,boolean required,int span){
		String hql = "SELECT enumerate.enumValue,enumerate.enumValue From Enumerate enumerate " +
		"where enumerate.enumType = 'VehicleStatus' order by enumerate.id";
		ListUI ui = createListUI(id, readOnly, required);
		ui.setForceOverride(true);
		ui.setForceSpace(false);
		ui.setHql(hql);
		ui.setSpan(span);
		ui.setEnumType("VehicleStatus");
		ui.setWidth("120");
		ui.afterPropertySet();
		ui.initData();
		if(required)
			ui.setValidator(new RequiredValidator());
		return ui;
	}
	public static ListUI createDelayResonListUI(String id,boolean readOnly,boolean required,int span){
		String hql = "SELECT enumerate.enumValue,enumerate.enumValue From Enumerate enumerate " +
		"where enumerate.enumType = 'DelayReson' order by enumerate.id";
		ListUI ui = createListUI(id, readOnly, required);
		ui.setForceOverride(true);
		ui.setForceSpace(false);
		ui.setHql(hql);
		ui.setSpan(span);
		ui.setEnumType("DelayReson");
		ui.setWidth("200px");
		ui.afterPropertySet();
		ui.initData();
		if(required)
			ui.setValidator(new RequiredValidator());
		return ui;
	}
	
//	public static RemoteUI createCityRemoteUI(String id){
//		final String hql = "SELECT c.id,c.code,c.name "
//							+ " FROM City c" 
//							+ " WHERE c.code like :param "
//							+ "/~shipment.city.name: and c.name like {shipment.city.name}~/"
//							+ " AND c.status='ACTIVE'";
//		RemoteUI ui = createRemoteUI(id, false, false,1);
//		ui.setDisplayedTableHead("ID,编码,名称");
//		ui.setManualEditable(true);
//		ui.setForceOverride(true);
//		ui.setDisplayColumn(3);
//		ui.setUis(new ArrayList());		
//		ui.setHql(hql);
//		ui.afterPropertySet();
//		ui.setPercentSignPosition("both");		
//		ui.setRemoteUIConfig(new RemoteUIConfig());
//		ui.setRemoteUIConfig(getRemoteUIConfig("shipment.city.name"));
//		return ui;
//	}
//	public static RemoteUI createZoneRemoteUI(String id){
//		final String hql = "SELECT z.id,z.code,z.name "
//			+ " FROM Zone z" 
//			+ " WHERE z.code like :param or z.name like :param"
//			+ " AND z.status='ACTIVE'";
//		RemoteUI ui = createRemoteUI(id, false, false,1);
//		ui.setDisplayedTableHead("ID,编码,名称");
//		ui.setManualEditable(true);
//		ui.setForceOverride(true);
//		ui.setDisplayColumn(3);
//		ui.setUis(new ArrayList());		
//		ui.setHql(hql);
//		ui.afterPropertySet();
//		ui.setPercentSignPosition("both");		
//		ui.setRemoteUIConfig(new RemoteUIConfig());
//		ui.setRemoteUIConfig(getRemoteUIConfig("shipment.city.name"));
//		return ui;
//	}
	
	private static RemoteUIConfig getRemoteUIConfig(String id){
		RemoteUIConfig remoteUIConfig = new RemoteUIConfig();
		remoteUIConfig.addUIConfig(makeTextUIConfig(id));
		//remoteUIConfig.addUIConfig(transLocationAddress);
		return remoteUIConfig;
	}
	/** 创建TextUIConfig **/
	public static TextUIConfig makeTextUIConfig(String id){
		TextUIConfig config = new TextUIConfig();		
		config.setColumn(2);
		config.setRow(1);
		config.setSpan(1);
		config.setTitle(id);
		config.setId(id);
		config.setPrecision(false);
		return config;
	}
}


