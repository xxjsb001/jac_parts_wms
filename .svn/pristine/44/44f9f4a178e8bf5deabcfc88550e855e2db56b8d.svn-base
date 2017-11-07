package com.vtradex.wms.server.action;

import java.util.List;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.thorn.server.dao.CommonDao;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.shipping.WmsWaveDoc;
import com.vtradex.wms.server.model.shipping.WmsWaveDocDetail;

public class WaveDocConfirmDecisionAction implements ProcessAction{
	
	private CommonDao commonDao;

	public WaveDocConfirmDecisionAction(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	private static final String WORKING = "WORKING";
	private static final String FINISHED = "FINISHED";
	
	public String processAction(Object arg0) {
		WmsWaveDoc waveDoc = (WmsWaveDoc)arg0;
		List<WmsWaveDocDetail> waveDocDetails = commonDao.findByQuery("FROM WmsWaveDocDetail waveDocDetail where waveDocDetail.waveDoc.id = :waveDocId",
				"waveDocId", waveDoc.getId());
		double quantity = 0d;
		for(WmsWaveDocDetail waveDocDetail : waveDocDetails){
			WmsMoveDocDetail moveDocDetail = commonDao.get(WmsMoveDocDetail.class, waveDocDetail.getMoveDocDetail().getId());
			quantity+=moveDocDetail.getMovedQuantityBU();
		}
		if (quantity==waveDoc.getExpectedQuantityBU()) {
			return FINISHED;
		}else{
			return WORKING;
		}
	}

}
