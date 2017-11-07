package com.vtradex.wms.client.packagingtable.data;

import java.util.Map;

import com.vtradex.thorn.client.data.DataAccessor;
import com.vtradex.thorn.client.message.IMessagePage;

/**
 * 
 *
 * @category 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:49 $
 */
@SuppressWarnings("rawtypes")
public class PackagingTableDataAccessor extends DataAccessor {
	
	private Map result;
	
	public PackagingTableDataAccessor(IMessagePage page) {
		super(page);
	}

	@Override
	public void onSuccess(String message, Map result) {
		if (PackagingTableConstants.MSG_GET_MOVEDOCDETAIL.equals(message)) {
			this.result = result;
			this.sendMessage(PackagingTableConstants.MSG_GET_MOVEDOCDETAIL);
		} else if (PackagingTableConstants.MSG_FINISH_PACKAGING.equals(message)) {
			this.result = result;
			this.sendMessage(PackagingTableConstants.MSG_FINISH_PACKAGING);
		} else if (PackagingTableConstants.MSG_GET_STATISTICSI_NFO.equals(message)) {
			this.result = result;
			this.sendMessage(PackagingTableConstants.MSG_GET_STATISTICSI_NFO);
		} else if (PackagingTableConstants.MSG_QUERY_MOVEDOCDETAIL.equals(message)) {
			this.result = result;
			this.sendMessage(PackagingTableConstants.MSG_QUERY_MOVEDOCDETAIL);
		} else if (PackagingTableConstants.MSG_GET_PACKAGING_JOBS.equals(message)) {
			this.result = result;
			this.sendMessage(PackagingTableConstants.MSG_GET_PACKAGING_JOBS);
		}
	}
	
	/**
	 * 获取统计信息
	 * @param params
	 */
	public void remoteCallGetStatisticsInfo(Map params) {
		this.remoteCall(PackagingTableConstants.MSG_GET_STATISTICSI_NFO, PackagingTableConstants.MANAGER_ID, 
				PackagingTableConstants.METHOD_GET_STATISTICSI_NFO, params);
	}
	
	/**
	 * 获取工作人员岗位信息
	 * @param params
	 */
	public void remoteCallGetWorkerInfo(Map params) {
		this.remoteCall(PackagingTableConstants.MSG_GET_PACKAGING_JOBS, PackagingTableConstants.MANAGER_ID, 
				PackagingTableConstants.METHOD_GET_PACKAGING_JOBS, params);
	}
	
	public void remoteCallQueryMoveDocDetail(Map params) {
		this.remoteCall(PackagingTableConstants.MSG_QUERY_MOVEDOCDETAIL, PackagingTableConstants.MANAGER_ID, 
				PackagingTableConstants.METHOD_QUERY_MOVEDOCDETAIL, params);
	}
	
	public void remoteCallGetMoveDocDetail(Map params) {
		this.remoteCall(PackagingTableConstants.MSG_GET_MOVEDOCDETAIL, PackagingTableConstants.MANAGER_ID, 
				PackagingTableConstants.METHOD_GET_MOVEDOCDETAIL, params);
	}
	
	public void remoteCallFinishPackaging(Map params) {
		this.remoteCall(PackagingTableConstants.MSG_FINISH_PACKAGING, PackagingTableConstants.MANAGER_ID, 
				PackagingTableConstants.METHOD_FINISH_PACKAGING, params);
	}

	public Map getResult() {
		return result;
	}
}
