package com.vtradex.wms.server.telnet.move;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.telnet.dto.WmsMoveTaskDTO;

@Transactional
public interface WmsMoveRFManager extends BaseManager {

	/***
	 * 通过移位单号查询移位任务
	 * 
	 * @param code
	 * @return
	 */
	WmsMoveTaskDTO findMoveTaskByMoveDocId(Long moveDocId);
	
	@Transactional
	WmsMoveTaskDTO findMovePutawayTaskByTired(final Long moveDocId);
	
	@Transactional
	WmsMoveTaskDTO findMoveTaskByTired(final Long moveDocId);
	
	/**
	 * 手工散货移位，需要生成虚拟移位计划
	 * 
	 * @param moveDocId
	 * @return
	 */
	WmsMoveTaskDTO findMoveTaskByVirtual(Long moveDocId);
	
	/**
	 * 非计划手工托盘移位
	 * 
	 * @param moveDocId
	 * @return
	 */
	WmsMoveTaskDTO findMoveTaskByPallet(String pallet);
	
	/**
	 * 非计划手工创建虚拟移位单和移位明细
	 */
	@Transactional
	void createMoveDocDetail(WmsMoveTaskDTO dto);
	
	WmsLocation queryWmsLocationByCode(String locationCode);
	
	Boolean validateLocation(String locationCode);
	Boolean validatePalletSign(String pallet);
	
	@Transactional
	void markExceptionWmsLocation(Long locationId) throws BusinessException;
	
	/**
	 * 按托盘检验
	 * @param dto
	 * @param pallet
	 */
	void checkMoveQuantityValidByPallet(WmsMoveTaskDTO dto, String pallet);
	
	
	/**
	 * 按箱号检验
	 * @param dto
	 * @param carton
	 */
	void checkMoveQuantityValidByCarton(WmsMoveTaskDTO dto, String carton);
	
	/**
	 * 按货品条码检验
	 * @param dto
	 * @param barCode
	 */
	void checkMoveQuantityValidByBarCode(WmsMoveTaskDTO dto, String barCode);
	
	/**
	 * 按托盘移位确认
	 * @param dto
	 * @param actualToLocationCode
	 */
	@Transactional
	void confirmMoveByPallet(WmsMoveTaskDTO dto, String pallet, String actualToLocationCode);
	
	/**
	 * 非计划手工按托盘移位确认
	 * @param dto
	 * @param actualToLocationCode
	 */
	@Transactional
	void confirmMoveManualByPallet(WmsMoveTaskDTO dto, String pallet, String actualToLocationCode);
	
	/**
	 * 按箱号移位确认
	 * @param dto
	 * @param actualToLocationCode
	 */
	@Transactional
	void confirmMoveByCarton(WmsMoveTaskDTO dto, String carton, String actualToLocationCode); 
	
	/**
	 * 按货品条码移位确认
	 * @param dto
	 * @param actualToLocationCode
	 */
	@Transactional
	void confirmMoveByBarCode(WmsMoveTaskDTO dto, String actualToLocationCode);
	
	/**
	 * 按累货货品条码移位确认
	 * @param dto
	 * @param actualToLocationCode
	 */
	@Transactional
	void confirmMoveByTired(WmsMoveTaskDTO dto, String actualToLocationCode);
	
	/**
	 * 非计划手工散货按货品条码移位确认
	 * @param dto
	 * @param actualToLocationCode
	 */
	@Transactional
	void confirmMoveManualByBarCode(WmsMoveTaskDTO dto, String actualToLocationCode);
	
	/**
	 * 标记累货
	 * @param dto
	 */
	@Transactional
	void markTiredTask(WmsMoveTaskDTO dto);
	
	Map<String, Object> checkLocationInventoryValid(String locationCode, String barCode);
	
	/**
	 * 移位单重新分配
	 * 
	 * @param dto
	 */
	void resetAllocate(WmsMoveTaskDTO dto);
	
	/**
	 * 虚拟移位计划自动分配，不产生Task
	 */
	void autoAllocateVirtual(WmsMoveTaskDTO dto);
	
	/**
	 * 非计划手工托盘移位自动分配
	 */
	void autoAllocateByPallet(WmsMoveTaskDTO dto);
	
	List<Object[]> findTaskByMoveCode(String moveCode);
	
	List<Object[]> findUnFinshMove();
	@Transactional
	void singleConfirm(Object[] obj);
	
	String pickConfirmAll(String moveCode);
	
	List<Map<String, Object>> findRuleTableBHKW();
	
	Object[] findLocBHKW(List<Map<String, Object>> details,String blCode
			,List<Map<String, Object>> dds);
	/**初始化补货移位单信息*/
	@Transactional
	String initWmsReplenishMove(List<Map<String, Object>> dds);
	
	List<Object[]> findUnFinshReplenishMove();
	
	List<Object[]> findUnActiveReplenishMove();
	
	List<Object[]> findMoveDetails(String moveCode);
	
	void setMoveDetailOver(Object[] obj);
	
	List<Object[]> findInventory(Object[] obj);
	/**补货单自动分配拣货库位*/
	@Transactional
	String subscriberAutoAllocateWmsMoveDoc(Object[] ixs,Double allocateQuantityBU,
			Long ddId);
	@Transactional
	void activePickByRf(String moveCode);
	
	List<Object[]> getInventoyExtendByPallet(String pallet);
	
	List<Object[]> getInventoyExtendByPalletJac(String pallet);
	
	WmsInventoryExtend getWmsInventoryExtend(Long id);
	WmsInventory getWmsInventoryByExtendId(Long id );
	/**
	 * 非计划手工创建虚拟移位单和移位明细
	 */
	@Transactional
	void createMoveDocDetailJac(WmsMoveTaskDTO dto);
	@Transactional
	void confirmMoveManualByPalletItem(WmsMoveTaskDTO dto, String actualToLocationCode);
	/**拼托**/
	@Transactional
	String checkPallet(String srcPallet,String descPallet);
	@Transactional
	String quickMove(Object[] ix,Double moveQty,String toLocationCode);
	
	/**
	 * 校验库位合法
	 */
	@Transactional
	Boolean checkLocationByCode(String locationCode);
	
	/**
	 * 记录补货上架库位
	 */
	@Transactional
	void createMoveDocLocation(String locationCode);
	
	/**
	 * 创建补货移位单
	 */
	@Transactional
	void createMoveDocReplenishment(WmsOrganization company);
	/**
	 * 获取货主
	 * @return
	 */
	@Transactional
	List<WmsOrganization> getWmsOrganization();
	
	/**
	 * 获取补货移位任务
	 */
	@Transactional
	WmsMoveTaskDTO getLocationByMoveDocId(Long moveDocId);
	
	/**
	 * 激活补货单
	 */
	@Transactional
	void activeReplenishment(Long moveDocId);
	/***
	 * 通过库存查询补货任务
	 * 
	 * @param code
	 * @return
	 */
	@Transactional
	WmsMoveTaskDTO findMoveTaskByInvExtId(Long invExtId,WmsMoveTaskDTO dto);
	/**
	 * 创建补货明细
	 * @param dto
	 */
	@Transactional
	void createReplenishmentMoveDocDetail(WmsMoveTaskDTO dto);
	
	/**
	 * 标记库位补货完成
	 */
	@Transactional
	void markLocationFinished(WmsMoveTaskDTO dto);
	
	/**
	 * 获取补货明细任务
	 */
	WmsMoveTaskDTO findMoveDocDetailByMoveDocId(Long moveDocId);
	
	/**
	 * 补货移位确认
	 */
	void confimReplenishment(WmsMoveTaskDTO dto);
	
}
