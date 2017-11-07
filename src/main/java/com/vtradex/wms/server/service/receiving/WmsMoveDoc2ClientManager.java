package com.vtradex.wms.server.service.receiving;

import java.util.Map;

import com.vtradex.thorn.server.service.BaseManager;

@SuppressWarnings("rawtypes")
public interface WmsMoveDoc2ClientManager extends BaseManager {
	Map findMoveDocById(Map params);
	Map findMoveDocDetailsId(Map params);
	Map findAvailableInventories(Map params);
	Map findTaskById(Map params);
	Map autoAllocate(Map params);
	Map cancelAllocate(Map params);
	Map manuelAllocate(Map params);
	Map manuelCancelAllocate(Map params);
}
