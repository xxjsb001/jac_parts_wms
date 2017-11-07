package com.vtradex.wms.server.service.replenish;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;

/**
 * 补货管理
 *
 * @category Manager 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.3 $Date: 2015/04/28 06:57:55 $
 */
public interface WmsMoveDocReplenishmentManager extends BaseManager {
	
	/**
	 * 手工创建补货单
	 */
	void manualCreateReplenishment();
	
	@Transactional
	void doManualCreateReplenishment(Map<String, Object> result) throws BusinessException;
	/**手工创建补货单  yc.min*/
	@Transactional
	void manualCreateReplenishmentJac(Long companyId);
	@Transactional
	void deleteDetail(WmsMoveDocDetail detail);
	
}