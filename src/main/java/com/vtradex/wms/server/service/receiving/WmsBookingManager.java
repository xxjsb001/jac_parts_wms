package com.vtradex.wms.server.service.receiving;

import java.util.Date;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.client.ui.table.RowData;
import com.vtradex.thorn.server.service.Thorn4GanttChartManager;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.model.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.receiving.WmsBooking;

/**
 * @author: 李炎
 */
public interface WmsBookingManager extends Thorn4GanttChartManager {
	
	/**
	 * 保存收货预约计划
	 */
	@Transactional
	void storeWmsBooking(WmsBooking wmsBooking);
	
	Date initStartTime(Map map);
	
	Date initEndTime(Map map);
	
	RowData initAsnID(Map map);
	
	RowData initMasterID(Map map);
	
	RowData initDockID(Map map);
	/**
	 * 修改收货预约计划
	 */
	@Transactional
	void editWmsBooking(Long planId,Long asnID,Long dockId,Date startTime,Date endTime,String remark,String planType);
	/**
	 * 补录ASN
	 */
	@Transactional
	void inputASN(WmsBooking wmsBooking,Long asnId);
	/**
	 * 删除预约单
	 */
	@Transactional
	void deleteBooking(WmsBooking wmsBooking);
	/**
	 * 到库登记
	 */
	@Transactional
	void arriveRegister(WmsBooking wmsBooking);
	
	/**
	 * 删除预约单明细
	 */
	@Transactional
	void deleteASNDetailBooking(WmsASNDetail detail);
	
	/**
	 * 加入预约单
	 */
	@Transactional
	void addBooking(WmsASNDetail detail,Long bookingId);
	
	/**
	 * 创建预约单
	 */
	@Transactional
	void createBooking(WmsASNDetail detail,WmsBooking wmsBooking);

}
