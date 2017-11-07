package com.vtradex.wms.client.ui.page.allocate.page.utils;

import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.ObjectFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.Renderer;
import com.vtradex.thorn.client.utils.LocaleUtils;

/**
 * 
 * @author <a href="yan.li@vtradex.com">李炎</a>
 * @description 
 * 
 */

public class GridUtils {
	/**
	 * 生成ColumnConfigs.
	 * 
	 * @param prefix 存储在国际化文件中的相关配置的前缀
	 * @return
	 */
	public static ColumnConfig[] generateColumnConfigs(String prefix) {
		int size = Integer.parseInt(LocaleUtils.getText(prefix + ".title.size"));
		int index = -1;
		if(LocaleUtils.getText(prefix + ".editable.index") != null) {
			index = Integer.parseInt(LocaleUtils.getText(prefix + ".editable.index"));
		}
		ColumnConfig[] configs = new ColumnConfig[size];
		for(int i = 0 ; i < size; i++){
			if(index != -1 && i == index) {
				NumberField numberField = new NumberField();
	            numberField.setAllowBlank(false);
	            numberField.setAllowNegative(false);
	            configs[i] = new ColumnConfig("<font style='font-weight:bold' color='#2a2a2a'>" + LocaleUtils.getText(prefix + ".title." + i) + "</font>",LocaleUtils.getText(prefix + ".recordDef." + (i + 3)),Integer.parseInt(LocaleUtils.getText(prefix + ".title.size."  + i)));
	            configs[i].setAlign(TextAlign.CENTER);
				configs[i].setEditor(new GridEditor(numberField));
				configs[i].setCss("border-style: solid; border-width: 1px 1px 1px 1px;border-color: blue");
			} else {
				configs[i] = new ColumnConfig("<font style='font-weight:bold' color='#2a2a2a'>" + LocaleUtils.getText(prefix + ".title." + i) + "</font>",LocaleUtils.getText(prefix + ".recordDef." + (i + 3)),Integer.parseInt(LocaleUtils.getText(prefix + ".title.size."  + i)));
				configs[i].setAlign(TextAlign.CENTER);
//				final int index = i;
				configs[i].setRenderer(new Renderer(){
					public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
						if(value == null)
							return "-";
						String displayValue = value.toString();
//						if(1 == index)
//							displayValue = LocaleUtils.getText("ProductPackageUnit." + displayValue);
						return displayValue;
					}
				});
			}
		}
		return configs;
	}
	
	/**
	 * 生成FieldDef.
	 * @param prefix 存储在国际化文件中的相关配置的前缀
	 * @return
	 */
	public static FieldDef[] generateFiledDefs(String prefix){
		int size = Integer.parseInt(LocaleUtils.getText(prefix + ".recordDef.size"));
		FieldDef[] fieldDefs = new FieldDef[size];
		for(int i = 0 ; i < size; i++){
			fieldDefs[i] = new ObjectFieldDef(LocaleUtils.getText(prefix + ".recordDef." + i));
		}
		return fieldDefs;
	}
}


