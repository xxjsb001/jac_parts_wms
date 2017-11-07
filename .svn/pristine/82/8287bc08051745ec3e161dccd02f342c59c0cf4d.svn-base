package com.vtradex.wms.server.action.move;

import com.vtradex.thorn.server.action.ProcessAction;
import com.vtradex.thorn.server.dao.CommonDao;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.model.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.receiving.WmsASNShelvesStauts;

/**
 * 移位单分配Action
 *
 * @category Decision Action
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:50 $
 */
public class WmsASNMoveStatusDecisionAction implements ProcessAction {
	
	private CommonDao commonDao;

	public WmsASNMoveStatusDecisionAction(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public String processAction(Object object) {
		WmsASN asn = (WmsASN)object;
		if(asn.getExpectedQuantityBU().doubleValue()  <=  asn.getMovedQuantityBU().doubleValue() && asn.getReceivedQuantityBU().doubleValue() <= asn.getMovedQuantityBU().doubleValue()){
			boolean finish = true;
			for(WmsASNDetail detail : asn.getDetails()){
				if(detail.getExpectedQuantityBU().doubleValue() > detail.getMovedQuantityBU().doubleValue() || detail.getReceivedQuantityBU().doubleValue() > detail.getMovedQuantityBU().doubleValue()){
					finish = false;
					break;
				}
			}
			return finish ? WmsASNShelvesStauts.FINISHED : WmsASNShelvesStauts.PUTAWAY;
		}
		if(asn.getMovedQuantityBU().doubleValue() > 0D){
			return WmsASNShelvesStauts.PUTAWAY;
		}
		
		String hql = "SELECT COUNT(*) FROM WmsMoveDoc moveDoc WHERE moveDoc.asn.id = :asnId";
		Long count = (Long)commonDao.findByQueryUniqueResult(hql, "asnId", asn.getId());
		if (count.intValue() > 0) {
			return WmsASNShelvesStauts.PUTAWAY;
		}
		return WmsASNShelvesStauts.UNPUTAWAY;
//		return WmsASNShelvesStauts.UNPUTAWAY;
	}
}
