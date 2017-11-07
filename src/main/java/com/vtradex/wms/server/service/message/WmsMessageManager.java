package com.vtradex.wms.server.service.message;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;

public interface WmsMessageManager extends BaseManager {
	/**
	 * 新增组织对象后自动创建单据类型
	 * @param objects
	 */
	@Transactional
	void subscriberCreateBillType(Object object);
	/**
	 * 根据新建仓库创建仓库默认货主
	 * @param object
	 */
	@Transactional
	void subscriberCreateWmsOrganization(Object object);
	
	@Transactional
	void subscriberEditWmsOrganization(Object object);
	/**
	 * 创建移位单
	 * @param entity
	 */
	void subscriberCreateWmsMoveDoc(Object entity);
	
	/**
	 * 移位单自动分配
	 * 
	 * @param entity
	 */
	void subscriberAutoAllocateWmsMoveDoc(Object entity);
	
	/**
	 * 移位单取消分配
	 * 
	 * @param entity
	 */
	void subscriberCancelAllocateWmsMoveDoc(Object entity);
	
	/**
	 * 创建虚拟库位
	 * @param objects
	 */
	@Transactional
	void subScriberWmsLocationForArea(Object object);
	
	/**
	 * 新增货品后自动闯将基本包装
	 * @param objects
	 */
	@Transactional
	void subscriberCreatePackageUnit(Object object);
	
	/**
	 * 新建月台自动创建对应收、发、退拣库位
	 * @param objects
	 */
	@Transactional
	void subScriberWmsLocationForDock(Object object);
	
	/**
	 * 新建货主时创建库存状态
	 */
	@Transactional
	void subscriberCreateItemStateByOrganization(Object object);
	
	/**
	 * 根据上架单自动创建作业单
	 * @param objects
	 */
	@Transactional
	void subscriberCreateWorkDocByWmsMoveDoc(Object object);
	
	/** 根据移位单（上架、拣货、库内移位等 取消作业单） */
	@Transactional
	void subscriberCancelWorkDocByWmsMoveDoc(Object object);
	
	/**
	 * 消息驱动以下动作：
     * 扣减发货库位库存；
     * 更新发货单及其明细的发运数量、发运时间
     * @param objects
     */
	@Transactional
	void subscriberShip(Object objects);
	/**
	 * 记录发运日志
	 * */
	@Transactional
	void subscriberShipLog(Object objects);
	/**
	 * 记录下单日志
	 * */
	@Transactional
	void subscriberToMoveDocLog(Object objects);
	/**
	 * 记录拣选日志
	 * */
	@Transactional
	void subscriberPickLog(Object objects);
	/**
	 * 装车单发运
	 * @param objs
	 */
	@Transactional
	void subscriberMasterBOLShip(Object objs);
	
	/**
	 * 收货过账时异步生成卸车费用
     * @param objects
     */
	@Transactional
	void subscriberCreateUnloadingCharges(Object object);
	
	/**
	 * 发车时异步生成装车费用
     * @param objects
     */
	@Transactional
	void subscriberCreateloadedCharges(Object objects);
	
	/**
	 * 发货单承运商自动分派
     * @param objects
     */
	@Transactional
	void subscriberPickTicketApportion(Object object);
	
	
	/**
	 * 根据TASKLOG创建费用
	 * @param taskLog
	 */
	@Transactional
	public void subscriberTaskLogCharges(Long taskLog);
	
	/**
	 * 根据收货记录创建费用
	 * @param rrId
	 */
	@Transactional
	public void subscriberReceivedRecordCharges(Long rrId);
	
	/**
	 * 根据包装信息创建费用
	 * @param boxDetailId
	 */
	@Transactional
	public void subscriberBoxDetailCharges(Long boxDetailId);
}