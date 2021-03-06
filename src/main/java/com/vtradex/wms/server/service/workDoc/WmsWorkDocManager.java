package com.vtradex.wms.server.service.workDoc;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.client.ui.table.RowData;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.middle.WmsJobLog;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.move.WmsWorkDoc;
import com.vtradex.wms.server.model.shipping.WmsBOL;
import com.vtradex.wms.server.model.shipping.WmsTaskAndStation;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;

public interface WmsWorkDocManager extends BaseManager {
	@Transactional
	void invokeMethod();
	/**
	 * 整单自动作业确认
	 * @param workDocId
	 * @param workerId
	 */
	void confirmAll(Long workDocId, Long workerId);
	
	/** 单一作业确认 */
	@Transactional
	void singleWorkConfirm(WmsTask task, Long toLocationId, Long fromLocationId, Double quantityBU, Long workerId);
	
	WmsMoveDoc presubscriberCreateWorkDocByWmsMoveDoc(List<WmsMoveDoc> wmsMoveDocs);
	
	/**
	 * 创建作业单
	 * 
	 * @param moveDoc 移位单
	 */
	void createWorkDocByRule(WmsMoveDoc moveDoc) throws BusinessException ;

	/**
	 * 作业任务加入作业单
	 */
	@Transactional
	void addTaskForWorkDoc(Long workDocId, WmsTask wmsTask);
	
	/**
	 * 作业任务退出作业单
	 */
	@Transactional
	void delTaskForWorkDoc(WmsWorkDoc workDoc , WmsTask wmsTask);
	/** 
	 * 初始化目标库位
	 * */
	@Transactional
	RowData getToLocationByTask(Map map);
	
	
	Double getUnWorkQuantityByPageMap(Map map);
	/**拣货单生效  yc.min*/
	@Transactional
	void activePickByJac(WmsMoveDoc moveDoc);
	/**拆分移位单*/
	@Transactional
	void splitMoveDoc(WmsMoveDoc moveDoc,String type);
	/**拣货单失效  yc.min*/
	@Transactional
	void unActivePickByJac(WmsMoveDoc moveDoc);
	/**拣货单整单拣货确认 yc.min*/
	@Transactional
	void pickConfirmAll(WmsMoveDoc moveDoc);
	/**拣货单退拣 yc.min*/
	@Transactional
	void unPickConfirm(WmsTask task,List<String> values);
	/**容器退拣*/
	void unPickConfirm(WmsTaskAndStation ts,String supCode,Double pickBackQty,Long toLocId);
	/**拣货单单一拣货确认 yc.min*/
	@Transactional
	void singleConfirm(WmsTask task,Long moveDocId,Double moveQty,Long wsnId,Long workerId);
	/**扫码核单yc.min*/
	@Transactional
	Map getWmsScanBolGwt(Map param);
	/**备料交接_扫码器具码yc.min*/
	@Transactional
	Map getWmsScanContainer(Map param);
	/**拣货单发运确认yc.min*/
	@Transactional
	void pickShipAll(WmsMoveDoc moveDoc);
	/**拣货单发运确认yc.min*/
	@Transactional
	void pickShipByTask(WmsTask task,Double shipQty,WmsMoveDoc moveDoc);
	
	/**回单扣库存*/
	@Transactional
	Map<String,String> pickShipByTaskScan(WmsTask task,Double picQuantity,WmsMoveDoc moveDoc);
	/**库存可用量统计*/
	Map<String,String> pickShipInvsSum(String itemCode,Double shipQty);
	/**发运登记yc.min*/
	@Transactional
	void shipRecord(WmsMoveDoc moveDoc);
	/**获取备料库位信息 yc.min*/
	@Transactional
	List<Map<String, Object>> getStockUpLocationByJac(WmsWarehouse warehouse, String type
			,String billCode,Double quantity,String locationType,String supper
			,Boolean isBack,String company);
	@Transactional
	void upBolTagsNum(WmsBOL bol);
	/**更新标签状态为发运*/
	@Transactional
	void upBolTagsShip(WmsBOL bol,List<String> boxTags,List<String> containers,List<Long> wmsTaskAndStationIds);
	/**
	 * 初始化容器拣货单信息
	 */
	@Transactional
	void initPickContainer(Long id);
}