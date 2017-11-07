package com.vtradex.wms.client.ui.page;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vtradex.thorn.client.template.BaseCustomPopupTemplate;
import com.vtradex.thorn.client.ui.page.IPage;
import com.vtradex.thorn.client.utils.LocaleUtils;
import com.vtradex.wms.client.ui.data.Page_PA_DataAccessor;

public class ManualPickingAllocatePage extends BaseCustomPopupTemplate implements IsSerializable {

	public ManualPickingAllocatePage() {}
	/** 绘制主面板--主入口 */
	public void draw(VerticalPanel content) {
    	super.draw(content);
    	this.initDataAccessor();
    	this.initDatas();
    	this.drawMainPanel(content);
    }

	private void initDatas() {
		String flag = "pickTicketPage";
		Long pickTicketId = (Long) params.get(IPage.ENTITY_ID);
		
		final Map<String,Long> param = new HashMap<String,Long>();
		
		param.put("pickTicketId", pickTicketId);
		
		this.curPADataAccessor().setFlag(flag);
		this.curPADataAccessor().initPickTicketInfo(param);	//拣货单信息初始化
		this.curPADataAccessor().initDetailsInfo(param);	//拣货明细信息初始化
		this.curPADataAccessor().initAllocatedInfo(param);	//已分配信息初始化
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
		return (Page_PA_DataAccessor) this.data;
	}
	
	public String getTitle(){
		return LocaleUtils.getText("manualPickingAllocatePage");
	}
	
	public void release() {
	}
	
//	public List<String> getLocaleMessageKeys() {
//		List<String> keys = new ArrayList<String>();
//		keys.add("manualPickingAllocatePage");
//		keys.add("allocating");
//		keys.add("cancelAllocate");
//		keys.add("waveDoc.code");
//		keys.add("pickTicket.code");
//		keys.add("pickTicket.orderQuantity");
//		keys.add("pickTicket.preAllocatedQuantity");
//		keys.add("pickTicket.allocatedQuantity");
//		keys.add("pickTicket.pickedQuantity");
//		keys.add("item.code");
//		keys.add("item.name");
//		keys.add("packageUnit.unit");
//		keys.add("item.code");
//		keys.add("pickTicketDetail.orderQuantity");
//		keys.add("pickTicketDetail.orderQtyOfMasterUnit");
//		keys.add("pickTicketDetail.preAllocatedQuantity");
//		keys.add("pickTicketDetail.allocatedQuantity");
//		keys.add("pickTicketDetail.pickedQuantity");
//		keys.add("pickTicketDetail.shippedQuantity");
//		keys.add("lotInfor");
//		keys.add("location.code");
//		keys.add("packageUnit.convertFigure");
//		keys.add("inventory.quantity");
//		keys.add("inventory.availableQuantity");
//		keys.add("inventory.manualQuantity");
//		keys.add("itemKey.lot");
//		keys.add("inventory.storageDate");
//		keys.add("inventory.SOI");
//		keys.add("task.packageUnit");
//		keys.add("task.planQuantity");
//		keys.add("task.planQuantityMU");
//		keys.add("pickedRecord.manualQuantity");
//		keys.add("task.storageDate");
//		keys.add("task.SOI");
//		
//		return keys;
//	}
}