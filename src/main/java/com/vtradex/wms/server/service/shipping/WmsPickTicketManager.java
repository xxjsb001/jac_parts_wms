package com.vtradex.wms.server.service.shipping;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.move.WmsTaskLog;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.shipping.WmsPickTicket;
import com.vtradex.wms.server.model.shipping.WmsPickTicketDetail;

/**
 * 发货单管理 
 *
 * @category Manager
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.5 $Date: 2016/03/18 04:12:12 $
 */
public interface WmsPickTicketManager extends BaseManager {
	
	/**
	 * 新增/修改发货单
	 * @param pickTicket
	 */
	@Transactional
	void storePickTicket(WmsPickTicket pickTicket);

	/**
	 * 新增/修改发货单明细
	 * 
	 * @param pickTicketId
	 * @param pickTicketDetail
	 */
	@Transactional
	void addPickTicketDetail(Long pickTicketId,WmsPickTicketDetail pickTicketDetail,Long status);

	/**
	 * 删除发货单明细
	 * @param pickTicketId
	 * @param pickTickets
	 */
	@Transactional
	void removePickTicketDetail(WmsPickTicketDetail pickTicketDetail);
	/**
	 * 激活发货单--jac
	 * @param pickTicket
	 */
	@Transactional
	void activeWmsPickTicket(WmsPickTicket pickTicket);
	/**
	 * 接口激活发货单--jac
	 * @param pickTicket
	 */
	@Transactional
	void activePickTicket(Long pickTicketId);
	/**
	 * 反激活发货单
	 * @param pickTicket
	 */
	@Transactional
	void unActiveWmsPickTicket(WmsPickTicket pickTicket);

	/**
	 * 退拣
	 * @paraam pickTicketId
	 * @param taskLog
	 */
	@Transactional
	void pickBack(WmsTaskLog taskLog,List<String> tableValue);
	/**
	 * 退拣
	 * @paraam pickTicketId
	 * @param taskLog
	 */
	@Transactional
	void pickBack(WmsTaskLog taskLog,Double pickBackQuantity);
	
	/**
	 * 拣货确认
	 * @param objects
	 */
	@Transactional
	void pickConfirm(List<WmsPickTicket> pickTickets);

	/**
	 * 拣货确认
	 * @param pickTicket
	 */
	@Transactional
	void pickConfirm(WmsPickTicket pickTicket);	
	
	/**
	 * 拣货完成
	 * @param pickTicket
	 */
	@Transactional
	void createBOL(WmsPickTicket pickTicket);
	
	//----------------------------------------------------
	//手工分配客户化页面数据准备
	//----------------------------------------------------
	@Transactional
	Map init_PA_INFO(Map params);

	@Transactional
	Map init_PD_INFO(Map params);
	
	@Transactional
	Map init_ALD_INFO(Map params);
	
	@Transactional
	Map init_AL_INFO(Map params);
	
	/**
	 * 分单
	 * @param pickTickets
	 */
	@Transactional
	void seprate(WmsPickTicket pickTicket);
	
	/**
	 * 取消分单
	 * @param pickTickets
	 */
	@Transactional
	void unseprate(WmsPickTicket pickTicket);
	
	/**
	 * 获取最大行号
	 * @param param
	 * @return
	 */
	String getMaxLineNoByPickTicketDetail(Map param);
	
	Long getStatusByPickTicketDetail(Map param);

	/**
	 * 复制发货单
	 */
	@Transactional
	void copyPickTicket(WmsPickTicket pickTicket, Double quantity);
	
	/**
	 * 订单发生器
	 * @param num:发货单件数
	 * @param skuKindNum:SKU种类数
	 * @param skuNum:SKU数量
	 * @param skuLevel:包装级别
	 * @param skuClass1:货品分类
	 * @param skuClass2:货品分类
	 */
	void batchPickTicket(Long companyId, Long billTypeId, Long num, Long skuKindNum, Long skuNum, String skuLevel, String skuClass1, String skuClass2);
	
	
	@Transactional
	String batchPickTicketSingle(Long companyId, Long billTypeId, Long num, Long skuNum, List<WmsPackageUnit> currentPackageUnits);
	@Transactional
	void lotPick(String x,List<Long> ids,WmsBillType bType,String batch);
	@Transactional
	void cancelLot(WmsPickTicket pickTicket);
	
}