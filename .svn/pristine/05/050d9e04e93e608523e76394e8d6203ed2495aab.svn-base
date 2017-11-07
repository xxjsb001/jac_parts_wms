package com.vtradex.wms.server.service.billing;

import com.vtradex.thorn.server.model.message.Task;
import com.vtradex.thorn.server.service.BaseManager;

public interface WmsBillingExecuteManager extends BaseManager{
	/**计费入口*/
	void billingContactExecute();
	/***************************************************/
	/**定时任务执行元task和次task*/
	void billExecuteTaskByEDI();
	/***************************************************/
	/**计费功能分摊*/
	void billExecuteTask(Task task);
	/**计费功能分摊*/
	void billExecute(Long id);
	/***************************************************/
	/**配送服务费*/
	void BILL002_01(Long id);
	/**分选服务费*/
	void BILL003_01(Long id);
	/**信息服务费*/
	void BILL003_02(Long id);
	/**不合格品管理费*/
	void BILL003_03(Long id);
	/**仓储租赁(恒定)服务费*/
	void BILL001_01(Long id);
	/**仓储服务增租费*/
	void BILL001_02(Long id);
	/**仓库租赁(托盘数)服务费*/
	void BILL001_03(Long id);
	/**退货服务费*/
	void BILL003_04(Long id);
	/**工装租赁费*/
	void BILL003_05(Long id);
	/**工装管理费*/
	void BILL003_06(Long id);
	/**打包服务费*/
	void BILL003_07(Long id);
	/**基本服务费*/
	void BILL004_01(Long id);
	/***************************************************/
	/**仓租租凭(恒定)服务费-month*/
	void BILL001_01_month(Long id);
	/**仓储服务增租费-month*/
	void BILL001_02_month(Long id);
	/**仓库租赁(托盘数)服务费-day*/
	void BILL001_03_day(Long id);
	/**配送服务费-month(销售金额)*/
	void BILL002_01_month(Long id);
	/**配送服务费-day(收货)*/
	void BILL002_01_RECEIVE(Long id);
	/**配送服务费-day(签收)*/
	void BILL002_01_SIGNED(Long id);
	/**分选服务费-day*/
	void BILL003_01_day(Long id);
	/**分选服务费-month*/
	void BILL003_01_month(Long id);
	/**信息服务费-month*/
	void BILL003_02_month(Long id);
	/**不合格品管理费-day*/
	void BILL003_03_day(Long id);
	/**不合格品管理费-month*/
	void BILL003_03_month(Long id);
	/**退货服务费-month*/
	void BILL003_04_month(Long id);
	/**退货服务费-day*/
	void BILL003_04_day(Long id);
	/**工装租赁费-month*/
	void BILL003_05_month(Long id);
	/**工装管理费-day*/
	void BILL003_06_day(Long id);
	/**工装管理费-month*/
	void BILL003_06_month(Long id);
	/**打包服务费-month*/
	void BILL003_07_month(Long id);
	/**基本服务费-month*/
	void BILL004_01_month(Long id);
}
