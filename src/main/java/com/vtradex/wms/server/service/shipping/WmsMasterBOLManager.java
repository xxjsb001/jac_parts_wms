package com.vtradex.wms.server.service.shipping;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.shipping.WmsBOL;
import com.vtradex.wms.server.model.shipping.WmsBOLDetail;
import com.vtradex.wms.server.model.shipping.WmsMasterBOL;

public interface WmsMasterBOLManager extends BaseManager {
	
	@Transactional
	void invokeMethod();
	/** 
	 * 新增装车单
	 */
	@Transactional
	void storeMasterBOL(WmsBOL bol);
	
	/** 
	 * 加入发货单
	 */
	@Transactional
	void joinMasterBOLDetail(Long masterBOLId,WmsMoveDoc bol);
	
	/** 
	 * 删除装车单
	 */
	@Transactional
	void deleteMasterBOL(WmsMasterBOL masterBOL);
	
	/** 
	 * 移除发货单
	 */
	@Transactional
	void deleteMasterBOLDetail( WmsMoveDoc bol);
	
	
	/**
	 * 预约月台
	 */
	@Transactional
	void reservationDock(WmsMasterBOL masterBOL);
	
	/** 
	 * 调整行号
	 */
	@Transactional
	void adjustMasterBOLDetail( WmsMoveDoc bol, List<String> lineNo);
	
	Boolean beRequirePopupPage(Map map);
	
	/**
	 * 移除装车单明细
	 */
	@Transactional
	void removeBOLDetail(WmsBOLDetail bolDetail);
	/**
	 * 发运装车单
	 */
	@Transactional
	void shippingWmsBOL(WmsBOL bol);
	@Transactional
	void shippingScanBOL(WmsBOL bol);
	/**
	 * 删除装车单
	 * @param bol
	 */
	@Transactional
	void deleteWmsBOL(WmsBOL bol);
	/**
	 * 子单号回单扫描
	 */
	@Transactional
	void scanSubBol(String subCode);
	
	@Transactional
	void createWmsBolDoAsyn(Long id);
	@Transactional
	void createWmsBolSacnAsyn(Long id);
	/**
	 * 打印配送单
	 * @param bol
	 */
	@Transactional
	Map printWmsBOL(WmsBOL bol);
	
	/**
	 * 打印器具单
	 */
	Map printWmsBOLTag(WmsBOL bol);
}
