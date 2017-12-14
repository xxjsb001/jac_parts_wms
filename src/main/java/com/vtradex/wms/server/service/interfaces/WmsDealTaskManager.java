package com.vtradex.wms.server.service.interfaces;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;

/**
 * 处理Task任务
 * @author fs
 *
 */
public interface WmsDealTaskManager  extends BaseManager {
	/**处理接口传输过来的ASN信息*/
	@Transactional
	void dealAsn(Long id);
	
	/**处理接口传输过来的看板件信息*/
	@Transactional
	void dealKbPickData(Long id);
	
	/**处理接口传输过来的计划件信息*/
	@Transactional
	void dealJhPickData(Long id);
	
	/**处理接口传输过来的时序件信息*/
	@Transactional
	void dealSpsPickData(Long id);
	/**过账确认*/
	@Transactional
	void confirmAccount(Long asnId);
	/**mesBom信息同步到WMS*/
	void mesxgBom(Long id);
	/**mesBom信息产生发运计划*/
	void mesxgBomShipLot(Long id);
}
