package com.vtradex.wms.server.service.shipping;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.client.ui.table.RowData;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.shipping.WmsWaveDoc;
import com.vtradex.wms.server.model.shipping.WmsWaveDocDetail;

@SuppressWarnings("unchecked")
public interface WmsWaveDocManager  extends BaseManager {
	
	/** 创建波次 */
	@Transactional
	void createWaveDoc(String type);
	
	
	/** 删除波次 */
	@Transactional
	void deleteWaveDoc(WmsWaveDoc waveDoc);
	
	
	/** 获取规则配置的波次 */
	RowData[] getRuleConfigWave(Map params);
	
	/** 从指定的拣货单添加移位单明细 */
	@Transactional
	void addWaveDocDetail(Long waveDocId,WmsMoveDoc moveDoc);
	
	/** 移出指定的拣货单 */
	@Transactional
	void removeWaveDocDetail(WmsWaveDocDetail waveDocDetail);
	
	/**
	 * 反激活
	 */
	@Transactional
	void unActiveWaveDoc(WmsWaveDoc waveDoc);
	
	/** 分单调整 */
	@Transactional
	void seprateAdjustConfirm(WmsWaveDocDetail waveDocDetail, WmsWaveDocDetail pickedWaveDocDetail, List<String> seprateQuantityList);
}