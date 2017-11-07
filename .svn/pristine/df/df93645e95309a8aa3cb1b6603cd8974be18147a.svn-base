package com.vtradex.wms.client.packagingtable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;
import com.vtradex.thorn.client.template.BaseCustomPopupTemplate;
import com.vtradex.thorn.client.ui.ClickListener;
import com.vtradex.thorn.client.ui.CustomRemoteUI;
import com.vtradex.thorn.client.ui.TextUI;
import com.vtradex.thorn.client.ui.commonWidgets.BeautyButton;
import com.vtradex.thorn.client.ui.table.FormTable;
import com.vtradex.thorn.client.utils.LocaleUtils;
import com.vtradex.wms.client.packagingtable.data.MoveDocDetailDTO;
import com.vtradex.wms.client.packagingtable.data.PackagingTableConstants;
import com.vtradex.wms.client.ui.UIFactory;

/**
 * 
 * @category 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:49 $
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class EditWmsScanConfirmPackagingTablePage extends BaseCustomPopupTemplate implements IsSerializable {
	
	protected transient  Panel container;
	protected transient  Panel centerPanel;
	protected transient  Panel bottomPanel;
	protected transient  FormTable formTable;
	
	protected transient  TextUI calculateWeight, autoCalculateWeight;
	protected transient  CustomRemoteUI boxType;
	protected transient  ConfirmButton confirmBtn;
	
	private Double sumCalculateWeight = 0D;
	private Double sumCalculateVolume = 0D;
	private Double quantity = 0D;
	
	public EditWmsScanConfirmPackagingTablePage() {
		
	}
	
	@Override
	public void draw(VerticalPanel content) {
		super.draw(content);
		content.setWidth("240px");
		content.setHeight("120px");

		initContainer();
		ininWidgets();
		
		content.add(container);
	}
	
	protected void initContainer() {
		centerPanel = new Panel();
		centerPanel.setBorder(false);
		centerPanel.setPaddings(4);
		centerPanel.setLayout(new FitLayout());
		formTable = new FormTable();
		
		centerPanel.add(formTable.getForm());
		
		container = new Panel();
		container.setWidth(240);
		container.setHeight(120);
		container.setBorder(false);
		container.setLayout(new RowLayout());
		container.add(centerPanel, new RowLayoutData(90));
		
		bottomPanel = new Panel();
		bottomPanel.setBorder(false);
		bottomPanel.setPaddings(4);
		bottomPanel.setLayout(new HorizontalLayout(2));
		confirmBtn = new ConfirmButton();
		bottomPanel.add(confirmBtn);
		
        container.add(bottomPanel, new RowLayoutData(30));
	}
	
	protected void ininWidgets() {
		String pageName = "editWmsScanConfirmPackagingTablePage.";
		String packagingLocationHql = "SELECT b.id, b.code FROM WmsBoxType b " +
				" WHERE b.status = 'ENABLED' " +
				" AND b.code LIKE :param ";
		
		calculateWeight = UIFactory.createTextUI(pageName + "calculateWeight", true, true, 1, 180);
		calculateWeight.setRow(1);calculateWeight.setColumn(1);
		calculateWeight.addToTable(formTable);
		
		autoCalculateWeight = UIFactory.createTextUI(pageName + "autoCalculateWeight", false, true, 1, 180);
		autoCalculateWeight.setRow(2);autoCalculateWeight.setColumn(1);
		autoCalculateWeight.addToTable(formTable);
		
		boxType = UIFactory.createStandardRemoteUI(pageName + "boxType", packagingLocationHql, false, true, 1, 120, 3, "序号,代码");
		boxType.setDisplayColumn(2);
		boxType.setRemoteUIConfig(UIFactory.createRemoteUIConfig(UIFactory.createTextUIConfig(pageName + "boxType")));
		boxType.setRow(3);boxType.setColumn(1);
		boxType.addToTable(formTable);
		boxType.setFocusUI(true);
		
		boxType.getTextBox().addKeyboardListener(new KeyboardListenerAdapter() {
			@Override
			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
				if(KeyboardListener.KEY_ENTER == keyCode){
					doConfirm();
				}
			}
		});
		
		sumCalculateWeight = 0D;
		sumCalculateVolume = 0D;
		quantity = 0D;
		List list = (List)this.getParams().get(PackagingTableConstants.KEY_SCANING_DATA);
		for (int i = 0; i < list.size(); i++) {
			MoveDocDetailDTO dto = (MoveDocDetailDTO)list.get(i);
			sumCalculateWeight += Double.valueOf(dto.getWeight()) * Double.valueOf(dto.getScanQuantityBU());
			sumCalculateVolume += Double.valueOf(dto.getVolume()) * Double.valueOf(dto.getScanQuantityBU());
			quantity += Double.valueOf(dto.getScanQuantityBU());
		}
		
		calculateWeight.setText(sumCalculateWeight + "");
		autoCalculateWeight.setText(sumCalculateWeight + "");
		
		boxType.getTextBox().setFocus(true);
	}
	
	public void onFoucs(){
		boxType.getTextBox().setFocus(true);
		boxType.getTextBox().selectAll();
	}
	
	protected class ConfirmButton extends BeautyButton implements ClickListener {
		
		public ConfirmButton() {
			super(LocaleUtils.getText("editWmsScanConfirmPackagingTablePage.confirm"));
			this.setEnabled(true);
			confirmBtn = this;
			this.addClickListener(this);
		}
		
		public void onClick(Object obj) {
			doConfirm();
		}
	}
	
	protected void doConfirm() {
		EditWmsScanConfirmPackagingTablePage.this.hide();
		
		MaintainWmsPackagingTablePage mwptp = (MaintainWmsPackagingTablePage)EditWmsScanConfirmPackagingTablePage.this.getParent();
		
		Map params = new HashMap();
		params.put(PackagingTableConstants.KEY_SCANING_DATA, 
				EditWmsScanConfirmPackagingTablePage.this.getParams().get(PackagingTableConstants.KEY_SCANING_DATA));
		params.put(PackagingTableConstants.KEY_CALCULATE_WEIGHT, sumCalculateWeight);
		params.put(PackagingTableConstants.KEY_CALCULATE_VOLUME, sumCalculateVolume);
		params.put(PackagingTableConstants.KEY_QUANTITY, quantity);
		params.put(PackagingTableConstants.KEY_AUTOCALCULATE_WEIGHT, autoCalculateWeight.getValue().toString());
		params.put(PackagingTableConstants.KEY_BOX_TYPE_ID, boxType.getValue());
		params.put(PackagingTableConstants.KEY_PACKAGING, mwptp.getLocDTO()); // 包装台对象
		mwptp.getData().remoteCallFinishPackaging(params);
	}
}
