package com.vtradex.wms.client.ui.page.allocate.page;

import com.vtradex.thorn.client.utils.MessageConstants;

public interface AllocateMessageConstant extends MessageConstants {
	/**页面初始化**/
	public static final String MSG_INIT_PAGE = "msgInitPage";
	public static final String MSG_INIT_MOVE_DOC_PAGE = "msgInitMoveDocPage";
	public static final String MSG_INIT_MOVE_DOC_DETAIL_PAGE = "msgInitMoveDocDetailPage";
	public static final String MSG_INIT_MOVE_DOC_ALLOCATED_PAGE = "msgInitMoveDocAllocatedPage";
	
	public static final String MSG_MOVE_DOC_DATA_CHANGE = "msgMoveDocDataChange";
	public static final String MSG_MOVE_DOC_DETAIL_DATA_CHANGE = "msgMoveDocDetailDataChange";
	public static final String MSG_ALLOCATED_INFO_DATA_CHANGE = "msgAllocatedInfoDataChange";
	public static final String MSG_AVAILABLE_INVENTORY_DATA_CHANGE = "msgAvailableInventoryDataChange";
	public static final String MSG_AVAILABLE_INVENTORY_DATA_CLEAR = "msgAvailableInventoryDataClear";
	public static final String MSG_AUTO_ALLOCATE = "msgAutoAllocate";
	public static final String MSG_MANUEL_ALLOCATE = "msgManuelAllocate";
	public static final String MSG_ALL_CANCLE_ALLOCATE = "msgAllCancelAllocate";
	public static final String MSG_MANUEL_CANCEL_ALLOCATE = "msgManuelCancelAllocate";
}
