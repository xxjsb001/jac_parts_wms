package com.vtradex.wms.server.service.shipping;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;

/**
 * 
 *
 * @category 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:54 $
 */
@SuppressWarnings("rawtypes")
public interface WmsPackagingTableManager extends BaseManager {
	
	public Map getPackagingJobs(Map params);
	
	public Map getStatisticsInfo(Map params);
	
	public Map queryMoveDocDetail(Map params);
	
	public Map getMoveDocDetail(Map params);
	
	@Transactional
	public Map finishPackaging(Map params);
}
