package com.vtradex.wms.client.ui.page;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vtradex.thorn.client.template.BaseCustomPopupTemplate;
import com.vtradex.thorn.client.ui.page.IPage;
import com.vtradex.wms.client.ui.data.Page_PA_DataAccessor;

public class ManualWmsWaveAllocatePage extends BaseCustomPopupTemplate implements
		IsSerializable {
	public ManualWmsWaveAllocatePage(){}
	/** 绘制主面板--主入口 */
	public void draw(VerticalPanel content) {
    	super.draw(content);
    	this.initDataAccessor();
    	this.initDatas();
    	this.drawMainPanel(content);

    }

	private void initDatas() {
		String flag = "waveDocPage";
		Long waveDocId = (Long) params.get(IPage.ENTITY_ID);
		final Map<String,Long> param = new HashMap<String,Long>();
		param.put("waveDocId", waveDocId);
		this.curPADataAccessor().setFlag(flag);
		this.curPADataAccessor().initWaveDocInfo(param);
		this.curPADataAccessor().initWaveDocDetailsInfo(param);
		this.curPADataAccessor().initWaveDocAllocatedInfo(param);
	}
	
	/** 绘制主面板 */
	protected void drawMainPanel(com.google.gwt.user.client.ui.VerticalPanel content) {
		content.add(new Sub_PA_Panel(this));
	}
	/** 初始化数据存储区 */
	protected void initDataAccessor() {
		this.data = new Page_PA_DataAccessor(this);
	}

	protected Page_PA_DataAccessor curPADataAccessor() {
		return (Page_PA_DataAccessor)this.data;
	}

	
	public String getTitle(){
		return "部分分配";
	}
	public void release() {
		
	}
}
