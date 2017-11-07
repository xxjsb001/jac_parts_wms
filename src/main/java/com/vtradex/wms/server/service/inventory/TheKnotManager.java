package com.vtradex.wms.server.service.inventory;

import java.util.Date;

import com.vtradex.thorn.server.service.BaseManager;
/***
 * 日结
 * @author myc
 *
 */
public interface TheKnotManager extends BaseManager{
	/**日结保存*/
	void saveStorageData(Date beginDate,String description);
	/**系统日结*/
	void saveStorageDataSys();
	/**日结补数据*/
	void sysStorage();
}
