package com.vtradex.wms.client.ui.page.allocate.page.data;

import java.util.HashMap;
import java.util.Map;

import com.vtradex.thorn.client.ApplicationWindow;
import com.vtradex.thorn.client.data.DataAccessor;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.thorn.client.ui.support.MessageKey;
import com.vtradex.thorn.client.utils.Constants;
import com.vtradex.thorn.client.utils.LocaleUtils;
import com.vtradex.wms.client.ui.page.allocate.page.AllocateConstant;
import com.vtradex.wms.client.ui.page.allocate.page.AllocateMessageConstant;
import com.vtradex.wms.client.ui.page.allocate.page.model.CustomMoveDoc;
import com.vtradex.wms.client.ui.page.allocate.page.model.CustomMoveDocDetail;

@SuppressWarnings({"rawtypes", "unused"})
public class AllocateDataAccessor extends DataAccessor {
	private MoveDocData moveDocData = new MoveDocData();
	private MoveDocDetailData moveDocDetailData = new MoveDocDetailData();
	private AllocatedInfoData allocatedInfoData = new AllocatedInfoData();
	private InventoryData inventoryData = new InventoryData();
	
	public AllocateDataAccessor(IMessagePage page) {
		super(page);
	}

	public void onSuccess(String message, Map result) {
		if(AllocateMessageConstant.MSG_INIT_PAGE.equals(message)) {
			moveDocData.update(result);
			moveDocDetailData.update(result);
			sendMessage(AllocateMessageConstant.MSG_INIT_PAGE);
		} else if(AllocateMessageConstant.MSG_AVAILABLE_INVENTORY_DATA_CHANGE.equals(message)) {
			if(result == null || result.size() <= 0) {
				inventoryData.clear();
			} else {
				inventoryData.update(result);
			}
			sendMessage(AllocateMessageConstant.MSG_AVAILABLE_INVENTORY_DATA_CHANGE);
		} else if(AllocateMessageConstant.MSG_AUTO_ALLOCATE.equals(message)) {
//			moveDocData.clear();
//			moveDocDetailData.clear();
//			taskData.clear();
			inventoryData.clear();
			init(result);
			initAllocatedInfo(result);
//			sendMessage(AllocateMessageConstant.MSG_AUTO_ALLOCATE);
		} else if(AllocateMessageConstant.MSG_ALL_CANCLE_ALLOCATE.equals(message)) {
//			moveDocData.clear();
//			moveDocDetailData.clear();
//			taskData.clear();
			inventoryData.clear();
			init(result);
			initAllocatedInfo(result);
//			sendMessage(AllocateMessageConstant.MSG_ALL_CANCLE_ALLOCATE);
		} else if(AllocateMessageConstant.MSG_MANUEL_ALLOCATE.equals(message)) {
//			moveDocData.clear();
//			moveDocDetailData.clear();
//			taskData.clear();
			inventoryData.clear();
			init(result);
			initAllocatedInfo(result);
//			sendMessage(AllocateMessageConstant.MSG_MANUEL_ALLOCATE);
		} else if(AllocateMessageConstant.MSG_MANUEL_CANCEL_ALLOCATE.equals(message)) {
//			moveDocData.clear();
//			moveDocDetailData.clear();
//			taskData.clear();
			inventoryData.clear();
			init(result);
			initAllocatedInfo(result);
//			sendMessage(AllocateMessageConstant.MSG_MANUEL_CANCEL_ALLOCATE);
		} else if(AllocateMessageConstant.MSG_INIT_MOVE_DOC_ALLOCATED_PAGE.equals(message)) {
			allocatedInfoData.update(result);
			sendMessage(AllocateMessageConstant.MSG_INIT_MOVE_DOC_ALLOCATED_PAGE);
		} else if(AllocateMessageConstant.MSG_INIT_MOVE_DOC_DETAIL_PAGE.equals(message)) {
			moveDocDetailData.update(result);
			inventoryData.clear();
			sendMessage(AllocateMessageConstant.MSG_INIT_MOVE_DOC_DETAIL_PAGE);
		}
	}
	
	public boolean onTimeOutFailure(String message) {
		return false;
	}
	
	public void onFailure(String message, Map result) {
		MessageKey messKey = (MessageKey)result.get(Constants.THORN_MESSAGE_KEY);
		if(messKey != null && messKey.isErrorMessage()) {
			ApplicationWindow.setMessgeLabel(messKey.getMessage());
		}
		if(result.get(AllocateConstant.ERROR) != null){
			ApplicationWindow.setMessgeLabel(LocaleUtils.getTextWithoutException(result.get(AllocateConstant.ERROR).toString()));
		}
	}
	
	/** 初化始页面 **/
	public void initPage(Map params) {
//		initPickTicketInfo(params);
//		initPickTicketDetailInfo(params);
//		initAllocatedInfo(params);
		init(params);
		initAllocatedInfo(params);
	}
	
	public void init(Map params) {
		remoteCall(AllocateMessageConstant.MSG_INIT_PAGE, AllocateConstant.MOVE_DOC_TO_CLIENT_MANAGER, AllocateConstant.FIND_MOVE_DOC_BY_ID, params);
	}
	
	public void initPickTicketInfo(Map params) {
		remoteCall(AllocateMessageConstant.MSG_INIT_MOVE_DOC_PAGE, AllocateConstant.MOVE_DOC_TO_CLIENT_MANAGER, AllocateConstant.FIND_MOVE_DOC_BY_ID, params);
	}
	
	public void initPickTicketDetailInfo(Map params) {
		remoteCall(AllocateMessageConstant.MSG_INIT_MOVE_DOC_DETAIL_PAGE, AllocateConstant.MOVE_DOC_TO_CLIENT_MANAGER, AllocateConstant.FIND_DETAIL_BY_ID, params);
	}
	
	public void initAllocatedInfo(Map params) {
		remoteCall(AllocateMessageConstant.MSG_INIT_MOVE_DOC_ALLOCATED_PAGE, AllocateConstant.MOVE_DOC_TO_CLIENT_MANAGER, AllocateConstant.FIND_TASK_ID, params);
	}
	
	public void findAvailabeInventories(Map params) {
		remoteCall(AllocateMessageConstant.MSG_AVAILABLE_INVENTORY_DATA_CHANGE, AllocateConstant.MOVE_DOC_TO_CLIENT_MANAGER, AllocateConstant.FIND_AVAILABLE_INVENTORIES, params);
	}
	
	public void autoAllocate(Map params) {
		remoteCall(AllocateMessageConstant.MSG_AUTO_ALLOCATE, AllocateConstant.MOVE_DOC_TO_CLIENT_MANAGER, AllocateConstant.AUTO_ALLOCATE, params);
	}
	
	public void cancelAllocate(Map params) {
		remoteCall(AllocateMessageConstant.MSG_ALL_CANCLE_ALLOCATE, AllocateConstant.MOVE_DOC_TO_CLIENT_MANAGER, AllocateConstant.CANCEL_ALLOCATE, params);
	}
	
	public void manualAllocate(Map params) {
		remoteCall(AllocateMessageConstant.MSG_MANUEL_ALLOCATE, AllocateConstant.MOVE_DOC_TO_CLIENT_MANAGER, AllocateConstant.MANUEL_ALLOCATE, params);
	}
	
	public void manualCancelAllocate(Map params) {
		remoteCall(AllocateMessageConstant.MSG_MANUEL_CANCEL_ALLOCATE, AllocateConstant.MOVE_DOC_TO_CLIENT_MANAGER, AllocateConstant.MANUEL_CANCEL_ALLOCATE, params);
	}
	
	public void reloadPickAvailableInventoryPanel() {
		sendMessage(AllocateMessageConstant.MSG_AVAILABLE_INVENTORY_DATA_CLEAR);
	}
	
	public Map getMoveDocData() {
		return moveDocData.getData();
	}
	
	public Map getMoveDocDetailData() {
		return moveDocDetailData.getData();
	}
	
	public Map getAllocatedInfoData() {
		return allocatedInfoData.getData();
	}
	
	public Map getInventoryData() {
		return inventoryData.getData();
	}
	
	public CustomMoveDoc getCustomMoveDocByDetail() {
		return moveDocDetailData.getCustomMoveDoc();
	}
	
	public CustomMoveDoc getCustomMoveDocByTask() {
		return allocatedInfoData.getCustomMoveDoc();
	}
	
	public CustomMoveDocDetail getPickAvailableDetail() {
		return inventoryData.getCustomMoveDocDetail();
	}
	
	private class MoveDocData {
		private Map data = new HashMap();
		private CustomMoveDoc customMoveDoc;
		public Map getData() {
			return data;
		}
		public void setData(Map data) {
			this.data = data;
		}
		public CustomMoveDoc getCustomMoveDoc() {
			return customMoveDoc;
		}
		public void setCustomMoveDoc(CustomMoveDoc customMoveDoc) {
			this.customMoveDoc = customMoveDoc;
		}
		
		public void update(Map data) {
			this.data = data;
			if(data == null) {
				customMoveDoc = new CustomMoveDoc();
			} else {
				customMoveDoc = (CustomMoveDoc)data.get(AllocateConstant.CLIENT_ENTITY);
			}
			sendMessage(AllocateMessageConstant.MSG_MOVE_DOC_DATA_CHANGE);
		}
		
		public void clear() {
			data = null;
			customMoveDoc = null;
			sendMessage(AllocateMessageConstant.MSG_MOVE_DOC_DATA_CHANGE);
		}
	}
	
	private class MoveDocDetailData {
		private Map data = new HashMap();
		private CustomMoveDoc customMoveDoc;
		public Map getData() {
			return data;
		}
		public void setData(Map data) {
			this.data = data;
		}
		public CustomMoveDoc getCustomMoveDoc() {
			return customMoveDoc;
		}
		public void setCustomMoveDoc(CustomMoveDoc customMoveDoc) {
			this.customMoveDoc = customMoveDoc;
		}
		
		public void update(Map data) {
			this.data = data;
			if(data == null) {
				customMoveDoc = new CustomMoveDoc();
			} else {
				customMoveDoc = (CustomMoveDoc)data.get(AllocateConstant.CLIENT_ENTITY);
			}
			sendMessage(AllocateMessageConstant.MSG_MOVE_DOC_DETAIL_DATA_CHANGE);
		}
		
		public void clear() {
			data = null;
			customMoveDoc = null;
			sendMessage(AllocateMessageConstant.MSG_MOVE_DOC_DETAIL_DATA_CHANGE);
		}
	}
	
	private class AllocatedInfoData {
		private Map data = new HashMap();
		private CustomMoveDoc customMoveDoc;
		public Map getData() {
			return data;
		}
		public void setData(Map data) {
			this.data = data;
		}
		public CustomMoveDoc getCustomMoveDoc() {
			return customMoveDoc;
		}
		public void setCustomMoveDoc(CustomMoveDoc customMoveDoc) {
			this.customMoveDoc = customMoveDoc;
		}
		
		public void update(Map data) {
			this.data = data;
			if(data == null) {
				customMoveDoc = new CustomMoveDoc();
			} else {
				customMoveDoc = (CustomMoveDoc)data.get(AllocateConstant.CLIENT_ENTITY);
			}
			sendMessage(AllocateMessageConstant.MSG_ALLOCATED_INFO_DATA_CHANGE);
		}
		
		public void clear() {
			data = null;
			customMoveDoc = null;
			sendMessage(AllocateMessageConstant.MSG_ALLOCATED_INFO_DATA_CHANGE);
		}
	}
	
	private class InventoryData {
		private Map data = new HashMap();
		private CustomMoveDocDetail customMoveDocDetail;
		public Map getData() {
			return data;
		}
		public void setData(Map data) {
			this.data = data;
		}
		public CustomMoveDocDetail getCustomMoveDocDetail() {
			return customMoveDocDetail;
		}
		public void setCustomMoveDocDetail(CustomMoveDocDetail customMoveDocDetail) {
			this.customMoveDocDetail = customMoveDocDetail;
		}
		
		public void update(Map data) {
			this.data = data;
			if(data == null) {
				customMoveDocDetail = new CustomMoveDocDetail();
			} else {
				customMoveDocDetail = (CustomMoveDocDetail)data.get(AllocateConstant.CLIENT_ENTITY);
			}
			sendMessage(AllocateMessageConstant.MSG_AVAILABLE_INVENTORY_DATA_CHANGE);
		}
		
		public void clear() {
			data = null;
			customMoveDocDetail = null;
			sendMessage(AllocateMessageConstant.MSG_AVAILABLE_INVENTORY_DATA_CHANGE);
		}
	}
}
