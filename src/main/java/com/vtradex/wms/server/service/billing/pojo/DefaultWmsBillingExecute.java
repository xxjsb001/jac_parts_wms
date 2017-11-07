package com.vtradex.wms.server.service.billing.pojo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.message.Task;
import com.vtradex.thorn.server.model.message.TaskStatus;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.billing.WMSBillingCategoryBigEnum;
import com.vtradex.wms.server.model.billing.WMSBillingCategoryMiddleEnum;
import com.vtradex.wms.server.model.billing.WMSBillingModelInterface;
import com.vtradex.wms.server.model.billing.WMSContact;
import com.vtradex.wms.server.model.billing.WMSContactDetail;
import com.vtradex.wms.server.model.billing.WMSPurchaseInvoiceDetail;
import com.vtradex.wms.server.model.billing.WMSPurchaseStatus;
import com.vtradex.wms.server.model.billing.WmsBillMode;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.shipping.WmsPickTicketStatus;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.service.GlobalParamUtils;
import com.vtradex.wms.server.service.billing.WMSBillingCategoryRuleChain;
import com.vtradex.wms.server.service.billing.WmsBillingExecuteManager;
import com.vtradex.wms.server.service.billing.WmsBillingManager;
import com.vtradex.wms.server.utils.EdiTaskType;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;

public class DefaultWmsBillingExecute extends DefaultBaseManager implements WmsBillingExecuteManager{
	protected WmsBillingManager billingManager;
	public DefaultWmsBillingExecute(WmsBillingManager billingManager){
		this.billingManager = billingManager;
	}
	//一天一次
	public void billingContactExecute() {
		String rijiewarehouse = GlobalParamUtils.getGloableStringValue("rijie_sys");
		String[] warehouses = rijiewarehouse.split(MyUtils.spilt1);
		for(String warehouse : warehouses){
			Long warehouseId = Long.parseLong(warehouse);
			billingContactWs(warehouseId);
		}
	}
	@SuppressWarnings("unchecked")
	private void billingContactWs(Long warehouseId) {
		List<WMSContact> ccs = commonDao.findByQuery("FROM WMSContact contact WHERE contact.warehouse.id=:warehouseID"
				+ " and contact.status='ENABLED' and contact.startDate <=:startDate and contact.endDate >=:endDate", 
				new String[]{"warehouseID","startDate","endDate"}, new Object[]{warehouseId,new Date(),new Date()});
		for(WMSContact w : ccs){
			List<WMSContactDetail> ccds = commonDao.findByQuery("FROM WMSContactDetail detail"
					+ " WHERE detail.contact.id =:contactId and detail.startDate <=:startDate and detail.endDate >=:endDate", 
					new String[]{"contactId","startDate","endDate"}, new Object[]{w.getId(),new Date(),new Date()});
			for(WMSContactDetail ww : ccds){
				Task task = new Task(WMSBillingCategoryRuleChain.BILLLFCS, 
						"wmsBillingExecuteManager"+MyUtils.spilt1+"billExecute", ww.getId());
				commonDao.store(task);
			}
		}
	}
	/**********************************************S------P-----I-----L-----T*********************************/
	/**********************************************S------P-----I-----L-----T*********************************/
	/**********************************************S------P-----I-----L-----T*********************************/
	@SuppressWarnings("unchecked")
	public void billExecuteTaskByEDI(){
		List<Task> tasks = commonDao.findByQuery("FROM Task t WHERE 1=1"
				+ " AND t.status =:status AND t.type NOT IN ("
				+StringUtils.substringBetween(EdiTaskType.types, "[", "]")+")", 
				new String[]{"status"},new Object[]{TaskStatus.STAT_READY});
		if(tasks!=null && tasks.size()>0){
			for(Task task : tasks){
				Boolean beBill = Boolean.FALSE;
				try {
					beBill = task.getSubscriber().split(MyUtils.spilt1)[0].equals("wmsBillingExecuteManager");
				} catch (Exception e) {
					System.out.println("this task is error id = "+task.getId());
				}
				if(beBill){
					billExecuteTaskEdi(task);
				}
			}
		}
	}
	/**********************************************S------P-----I-----L-----T*********************************/
	/**********************************************S------P-----I-----L-----T*********************************/
	/**********************************************S------P-----I-----L-----T*********************************/
	private void billExecuteTaskEdi(Task task){
		task.setStartTime(new Date());
		commonDao.store(task);
		billExecuteTask(task);
	}
	//重算
	public void billExecuteTask(Task task){
		String[] type = task.getSubscriber().split(MyUtils.spilt1);
		String managerName = type[0];//StringUtils.substringBefore(type,".");
		String methodName = type[1];//StringUtils.substringAfter(type,".");
		Object manager = applicationContext.getBean(managerName);
		Boolean isError = Boolean.FALSE;
		try {
			Method method = manager.getClass().getMethod(methodName, new Class[]{Long.class});
			method.invoke(manager,new Object[]{task.getId()});
		} catch (IllegalArgumentException e) {
			isError = Boolean.TRUE;
			e.printStackTrace();
		} catch (SecurityException e) {
			isError = Boolean.TRUE;
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			isError = Boolean.TRUE;
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			isError = Boolean.TRUE;
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			isError = Boolean.TRUE;
			e.printStackTrace();
		}finally{
			task = commonDao.load(Task.class, task.getId());
			if(isError){
				task.setStatus(TaskStatus.STAT_FAIL);
			}
//			else{//这里只记录意外失败状态,成功与否取决于实际业务来定
//			}
			task.setEndTime(new Date());
			task.setRepeatCount(task.getRepeatCount()+1);
			commonDao.store(task);
		}
	}
	public void billExecute(Long id){
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww==null){
			task.setStatus(TaskStatus.STAT_FAIL);
			commonDao.store(task);
			billingManager.initLog(task.getType(), task.getId().toString(), 
					"合同明细ID="+task.getMessageId()+"数据不存在", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"数据不存在");
			return;
//			throw new BusinessException("合同明细ID="+task.getMessageId()+"数据不存在");
		}
		String smallCategory = ww.getSmallCategory().getName();//chain01~04 WMSBillingCategoryMiddleEnum
		Map<String,String> chainMethod = WMSBillingCategoryRuleChain.chainMethod(WMSBillingCategoryMiddleEnum.class);
		String methodName = getChainMethod(chainMethod, WMSBillingCategoryRuleChain.chain01, smallCategory);
		if(methodName==null){
			methodName = getChainMethod(chainMethod, WMSBillingCategoryRuleChain.chain02, smallCategory);
			if(methodName==null){
				methodName = getChainMethod(chainMethod, WMSBillingCategoryRuleChain.chain03, smallCategory);
				if(methodName==null){
					methodName = getChainMethod(chainMethod, WMSBillingCategoryRuleChain.chain04, smallCategory);
				}
			}
		}
		if(methodName!=null){
			System.out.println(methodName);
			Object manager = applicationContext.getBean("wmsBillingExecuteManager");
			try {
				Method method = manager.getClass().getMethod(methodName, new Class[]{Long.class});
				method.invoke(manager,new Object[]{id});
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}
	/**********************************************S------P-----I-----L-----T*********************************/
	/**********************************************S------P-----I-----L-----T*********************************/
	/**********************************************S------P-----I-----L-----T*********************************/
	//仓储租赁(恒定)服务费
	@SuppressWarnings("unchecked")
	public void BILL001_01(Long id){
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww==null){
			task.setStatus(TaskStatus.STAT_FAIL);
			commonDao.store(task);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL001_01, task.getId().toString(), 
					"合同明细ID="+task.getMessageId()+"数据不存在", task.getSubscriber(), 
					"合同明细ID="+task.getMessageId()+"数据不存在");
			return;
			//throw new BusinessException("合同明细ID="+task.getMessageId()+"数据不存在");
		}
		String billingMode = ww.getBillingMode();//WMSBillingModelInterface
		System.out.println("BILL001_01....."+id+"...."+billingMode);
		String smallCategory = ww.getSmallCategory().getName();
		//0-无,1-day,2-month
		Object[] obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleDay,1);
		int mode = (Integer) obj[0];
		if(mode==0){
			obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleMonth,2);
			mode = (Integer) obj[0];
		}
		if(mode==2){//仓储租凭费用目前不涉及按天计算.........
			String date = JavaTools.mosaicDate(1,task.getStartTime()==null?new Date():task.getStartTime(),JavaTools.y_m_d);// yyyy-MM-01
			//每月1号计算
			List<Long> tasks = commonDao.findByQuery("SELECT t.id FROM Task t WHERE t.type =:type AND t.messageId =:messageId"
					+ " AND to_char(t.createTime,'yyyy-MM-dd') =:createTime", 
					new String[]{"type","messageId","createTime"}, 
					new Object[]{WMSBillingCategoryRuleChain.BILL00101month,task.getMessageId(),date});
			if(tasks==null || tasks.size()<=0){
				if(date.equals(JavaTools.format(new Date(),JavaTools.y_m_d))){
					Task taskEntity = new Task(WMSBillingCategoryRuleChain.BILL00101month,"wmsBillingExecuteManager"
							+MyUtils.spilt1+WMSBillingCategoryRuleChain.BILL00101month,ww.getId());
					commonDao.store(taskEntity);
					task.setStatus(TaskStatus.STAT_FINISH);
				}else{
					task.setStatus(TaskStatus.STAT_FAIL);
					billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL001_01, task.getId().toString(), 
							"[仓储租赁(恒定)服务费]每月1号计算", task.getSubscriber(), "[仓储租赁(恒定)服务费]每月1号计算");
				}
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL001_01, task.getId().toString(), 
						"[仓储租赁(恒定)服务费]已计算过", task.getSubscriber(), "[仓储租赁(恒定)服务费]已计算过");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL001_01, task.getId().toString(), 
					"[仓储租赁(恒定)服务费]目前不涉及按天计算", task.getSubscriber(), "[仓储租赁(恒定)服务费]目前不涉及按天计算");
		}
		commonDao.store(task);
	}
	//仓储服务增租费
	@SuppressWarnings("unchecked")
	public void BILL001_02(Long id){
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww==null){
			task.setStatus(TaskStatus.STAT_FAIL);
			commonDao.store(task);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL001_02, task.getId().toString(), 
					"合同明细ID="+task.getMessageId()+"数据不存在", task.getSubscriber(), 
					"合同明细ID="+task.getMessageId()+"数据不存在");
			return;
			//throw new BusinessException("合同明细ID="+task.getMessageId()+"数据不存在");
		}
		String billingMode = ww.getBillingMode();//WMSBillingModelInterface
		System.out.println("BILL001_02....."+id+"...."+billingMode);
		String smallCategory = ww.getSmallCategory().getName();
		//0-无,1-day,2-month
		Object[] obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleDay,1);
		int mode = (Integer) obj[0];
		if(mode==0){
			obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleMonth,2);
			mode = (Integer) obj[0];
		}
		if(mode==2){//仓储服务增租费用目前不涉及按天计算.........
			String date = JavaTools.mosaicDate(1,task.getStartTime()==null?new Date():task.getStartTime(),JavaTools.y_m_d);// yyyy-MM-01
			//每月1号计算
			List<Long> tasks = commonDao.findByQuery("SELECT t.id FROM Task t WHERE t.type =:type AND t.messageId =:messageId"
					+ " AND to_char(t.createTime,'yyyy-MM-dd') =:createTime", 
					new String[]{"type","messageId","createTime"}, 
					new Object[]{WMSBillingCategoryRuleChain.BILL00102month,task.getMessageId(),date});
			if(tasks==null || tasks.size()<=0){
				if(date.equals(JavaTools.format(new Date(),JavaTools.y_m_d))){
					Task taskEntity = new Task(WMSBillingCategoryRuleChain.BILL00102month,"wmsBillingExecuteManager"
							+MyUtils.spilt1+WMSBillingCategoryRuleChain.BILL00102month,ww.getId());
					commonDao.store(taskEntity);
					task.setStatus(TaskStatus.STAT_FINISH);
				}else{
					task.setStatus(TaskStatus.STAT_FAIL);
					billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL001_02, task.getId().toString(), 
							"[仓储服务增租费]每月1号计算", task.getSubscriber(), "[仓储服务增租费]每月1号计算");
				}
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL001_02, task.getId().toString(), 
						"[仓储服务增租费]已计算过", task.getSubscriber(), "[仓储服务增租费]已计算过");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL001_02, task.getId().toString(), 
					"[仓储服务增租费]目前不涉及按天计算", task.getSubscriber(), "[仓储服务增租费]目前不涉及按天计算");
		}
		commonDao.store(task);
	}
	//仓库租赁(托盘数)服务费
	@SuppressWarnings("unchecked")
	public void BILL001_03(Long id){
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww==null){
			task.setStatus(TaskStatus.STAT_FAIL);
			commonDao.store(task);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL001_03, task.getId().toString(), 
					"合同明细ID="+task.getMessageId()+"数据不存在", task.getSubscriber(), 
					"合同明细ID="+task.getMessageId()+"数据不存在");
			return;
			//throw new BusinessException("合同明细ID="+task.getMessageId()+"数据不存在");
		}
		String billingMode = ww.getBillingMode();//WMSBillingModelInterface
		System.out.println("BILL001_03....."+id+"...."+billingMode);
		String smallCategory = ww.getSmallCategory().getName();
		//0-无,1-day,2-month
		Object[] obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleDay,1);
		int mode = (Integer) obj[0];
		if(mode==0){
			obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleMonth,2);
			mode = (Integer) obj[0];
		}
		if(mode==1){
			List<Long> tasks = commonDao.findByQuery("SELECT t.id FROM Task t WHERE t.type =:type"
					+ " AND t.messageId =:messageId AND to_char(t.createTime,'yyyy-MM-dd') =:createTime", 
					new String[]{"type","messageId","createTime"}, 
					new Object[]{WMSBillingCategoryRuleChain.BILL00103day,task.getMessageId(),
							JavaTools.format(task.getCreateTime(), JavaTools.y_m_d)});
			if(tasks==null || tasks.size()<=0){
				Task taskEntity = new Task(WMSBillingCategoryRuleChain.BILL00103day,"wmsBillingExecuteManager"
						+MyUtils.spilt1+WMSBillingCategoryRuleChain.BILL00103day, ww.getId());
				commonDao.store(taskEntity);
				task.setStatus(TaskStatus.STAT_FINISH);
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL001_03, task.getId().toString(), 
						"【仓库租赁(托盘数)服务费】按件已计算过", task.getSubscriber(), "【仓库租赁(托盘数)服务费】按件已计算过");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL001_03, task.getId().toString(), 
					"【仓库租赁(托盘数)服务费】只能按件计算", task.getSubscriber(), "【仓库租赁(托盘数)服务费】只能按件计算");
		}
		commonDao.store(task);
	}
	//配送服务费
	@SuppressWarnings("unchecked")
	public void BILL002_01(Long id){
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww==null){
			task.setStatus(TaskStatus.STAT_FAIL);
			commonDao.store(task);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
					"合同明细ID="+task.getMessageId()+"数据不存在", task.getSubscriber(), 
					"合同明细ID="+task.getMessageId()+"数据不存在");
			return;
//			throw new BusinessException("合同明细ID="+task.getMessageId()+"数据不存在");
		}
		String billingMode = ww.getBillingMode();//WMSBillingModelInterface
		System.out.println("BILL002_01....."+id+"...."+billingMode);
		String smallCategory = ww.getSmallCategory().getName();
		//0-无,1-day,2-month
		Object[] obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleDay,1);
		int mode = (Integer) obj[0];
		if(mode==0){
			obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleMonth,2);
			mode = (Integer) obj[0];
		}
		int modeLength = (Integer) obj[1];//如果大于1,则该计费中类存在两种计费模式(day/month)相同的计费小类
		String type = null;
		if(mode==1){
			if(modeLength>1){
				if(billingMode.equals(WMSBillingModelInterface.SIGNED)){
					type = WMSBillingCategoryRuleChain.BILL00201signed;
				}else if(billingMode.equals(WMSBillingModelInterface.RECEIVE)){
					type = WMSBillingCategoryRuleChain.BILL00201receive;
				}
//				type = WMSBillingCategoryRuleChain.BILL00201day+"_"+billingMode;
			}
//			else{
//				type = WMSBillingCategoryRuleChain.BILL00201day;
//			}
			List<Long> tasks = commonDao.findByQuery("SELECT t.id FROM Task t WHERE t.type =:type"
					+ " AND t.messageId =:messageId AND to_char(t.createTime,'yyyy-MM-dd') =:createTime", 
					new String[]{"type","messageId","createTime"}, 
					new Object[]{type,task.getMessageId(),
							JavaTools.format(task.getCreateTime(), JavaTools.y_m_d)});
			if(tasks==null || tasks.size()<=0){
				Task taskEntity = new Task(type,"wmsBillingExecuteManager"+MyUtils.spilt1+type, ww.getId());
				commonDao.store(taskEntity);
				task.setStatus(TaskStatus.STAT_FINISH);
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
						"配送服务费按天已计算过", task.getSubscriber(), "配送服务费按天已计算过");
			}
		}else if(mode==2){
			Date dateTime = task.getStartTime()==null?new Date():task.getStartTime();
			String date = JavaTools.mosaicDate(1,dateTime,JavaTools.y_m_d);// yyyy-MM-01
			//每月1号计算(上个月的配送服务费)
			List<Long> tasks = commonDao.findByQuery("SELECT t.id FROM Task t WHERE t.type =:type"
					+ " AND t.messageId =:messageId AND to_char(t.createTime,'yyyy-MM-dd') =:createTime", 
					new String[]{"type","messageId","createTime"}, 
					new Object[]{WMSBillingCategoryRuleChain.BILL00201month,task.getMessageId(),date});
			if(tasks==null || tasks.size()<=0){
				if(date.equals(JavaTools.format(dateTime,JavaTools.y_m_d))){
					Task taskEntity = new Task(WMSBillingCategoryRuleChain.BILL00201month,"wmsBillingExecuteManager"
							+MyUtils.spilt1+WMSBillingCategoryRuleChain.BILL00201month, ww.getId());
					taskEntity.setCreateTime(dateTime);
					commonDao.store(taskEntity);
					task.setStatus(TaskStatus.STAT_FINISH);
				}else{
					task.setStatus(TaskStatus.STAT_FAIL);
					billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
							"配送服务费按月每月1号计算", task.getSubscriber(), "配送服务费按月每月1号计算");
				}
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
						"配送服务费按月已计算过", task.getSubscriber(), "配送服务费按月已计算过");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
					"配送服务费只能按收货或签收或销售金额计算", task.getSubscriber(), "配送服务费只能按收货或签收或销售金额计算");
		}
		commonDao.store(task);
	}
	//分选服务费
	@SuppressWarnings("unchecked")
	public void BILL003_01(Long id){
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww==null){
			task.setStatus(TaskStatus.STAT_FAIL);
			commonDao.store(task);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_01, task.getId().toString(), 
					"合同明细ID="+task.getMessageId()+"数据不存在", task.getSubscriber(), 
					"合同明细ID="+task.getMessageId()+"数据不存在");
			return;
//			throw new BusinessException("合同明细ID="+task.getMessageId()+"数据不存在");
		}
		String billingMode = ww.getBillingMode();//WMSBillingModelInterface
		System.out.println("BILL003_01....."+id+"...."+billingMode);
		String smallCategory = ww.getSmallCategory().getName();
		//0-无,1-day,2-month
		Object[] obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleDay,1);
		int mode = (Integer) obj[0];
		if(mode==0){
			obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleMonth,2);
			mode = (Integer) obj[0];
		}
		if(mode==1){
			List<Long> tasks = commonDao.findByQuery("SELECT t.id FROM Task t WHERE t.type =:type"
					+ " AND t.messageId =:messageId AND to_char(t.createTime,'yyyy-MM-dd') =:createTime", 
					new String[]{"type","messageId","createTime"}, 
					new Object[]{WMSBillingCategoryRuleChain.BILL00301day,task.getMessageId(),
							JavaTools.format(task.getCreateTime(), JavaTools.y_m_d)});
			if(tasks==null || tasks.size()<=0){
				Task taskEntity = new Task(WMSBillingCategoryRuleChain.BILL00301day,"wmsBillingExecuteManager"
						+MyUtils.spilt1+WMSBillingCategoryRuleChain.BILL00301day, ww.getId());
				commonDao.store(taskEntity);
				task.setStatus(TaskStatus.STAT_FINISH);
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_01, task.getId().toString(), 
						"分选服务费按件已计算过", task.getSubscriber(), "分选服务费按件已计算过");
			}
		}else if(mode==2){
			String date = JavaTools.mosaicDate(1,task.getStartTime()==null?new Date():task.getStartTime(),JavaTools.y_m_d);// yyyy-MM-01
			//每月1号计算
			List<Long> tasks = commonDao.findByQuery("SELECT t.id FROM Task t WHERE t.type =:type"
					+ " AND t.messageId =:messageId AND to_char(t.createTime,'yyyy-MM-dd') =:createTime", 
					new String[]{"type","messageId","createTime"}, 
					new Object[]{WMSBillingCategoryRuleChain.BILL00301month,task.getMessageId(),date});
			if(tasks==null || tasks.size()<=0){
				if(date.equals(JavaTools.format(new Date(),JavaTools.y_m_d))){
					Task taskEntity = new Task(WMSBillingCategoryRuleChain.BILL00301month,"wmsBillingExecuteManager"
							+MyUtils.spilt1+WMSBillingCategoryRuleChain.BILL00301month, ww.getId());
					commonDao.store(taskEntity);
					task.setStatus(TaskStatus.STAT_FINISH);
				}else{
					task.setStatus(TaskStatus.STAT_FAIL);
					billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_01, task.getId().toString(), 
							"分选服务费按月每月1号计算", task.getSubscriber(), "分选服务费按月每月1号计算");
				}
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_01, task.getId().toString(), 
						"分选服务费按月已计算过", task.getSubscriber(), "分选服务费按月已计算过");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_01, task.getId().toString(), 
					"分选服务费只能按件或按月计算", task.getSubscriber(), "分选服务费只能按件或按月计算");
		}
		commonDao.store(task);
	}
	//信息服务费
	@SuppressWarnings("unchecked")
	public void BILL003_02(Long id){
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww==null){
			task.setStatus(TaskStatus.STAT_FAIL);
			commonDao.store(task);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_02, task.getId().toString(), 
					"合同明细ID="+task.getMessageId()+"数据不存在", task.getSubscriber(), 
					"合同明细ID="+task.getMessageId()+"数据不存在");
			return;
//			throw new BusinessException("合同明细ID="+task.getMessageId()+"数据不存在");
		}
		String billingMode = ww.getBillingMode();//WMSBillingModelInterface
		System.out.println("BILL003_02....."+id+"...."+billingMode);
		String smallCategory = ww.getSmallCategory().getName();
		//0-无,1-day,2-month
		Object[] obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleDay,1);
		int mode = (Integer) obj[0];
		if(mode==0){
			obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleMonth,2);
			mode = (Integer) obj[0];
		}
		if(mode==2){//信息服务费目前不涉及按天计算.........
			String date = JavaTools.mosaicDate(1,task.getStartTime()==null?new Date():task.getStartTime(),JavaTools.y_m_d);// yyyy-MM-01
			//每月1号计算
			List<Long> tasks = commonDao.findByQuery("SELECT t.id FROM Task t WHERE t.type =:type AND t.messageId =:messageId"
					+ " AND to_char(t.createTime,'yyyy-MM-dd') =:createTime", 
					new String[]{"type","messageId","createTime"}, 
					new Object[]{WMSBillingCategoryRuleChain.BILL00302month,task.getMessageId(),date});
			if(tasks==null || tasks.size()<=0){
				if(date.equals(JavaTools.format(new Date(),JavaTools.y_m_d))){
					Task taskEntity = new Task(WMSBillingCategoryRuleChain.BILL00302month,"wmsBillingExecuteManager"
							+MyUtils.spilt1+WMSBillingCategoryRuleChain.BILL00302month,ww.getId());
					commonDao.store(taskEntity);
					task.setStatus(TaskStatus.STAT_FINISH);
				}else{
					task.setStatus(TaskStatus.STAT_FAIL);
					billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_02, task.getId().toString(), 
							"信息服务费每月1号计算", task.getSubscriber(), "信息服务费每月1号计算");
				}
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_02, task.getId().toString(), 
						"信息服务费按月已计算过", task.getSubscriber(), "信息服务费按月已计算过");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_02, task.getId().toString(), 
					"信息服务费目前不涉及按天计算", task.getSubscriber(), "信息服务费目前不涉及按天计算");
		}
		commonDao.store(task);
	}
	
	//不合格品管理费
	@SuppressWarnings("unchecked")
	public void BILL003_03(Long id){
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww==null){
			task.setStatus(TaskStatus.STAT_FAIL);
			commonDao.store(task);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_03, task.getId().toString(), 
					"合同明细ID="+task.getMessageId()+"数据不存在", task.getSubscriber(), 
					"合同明细ID="+task.getMessageId()+"数据不存在");
			return;
//			throw new BusinessException("合同明细ID="+task.getMessageId()+"数据不存在");
		}
		String billingMode = ww.getBillingMode();//WMSBillingModelInterface
		System.out.println("BILL003_03....."+id+"...."+billingMode);
		String smallCategory = ww.getSmallCategory().getName();
		//0-无,1-day,2-month
		Object[] obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleDay,1);
		int mode = (Integer) obj[0];
		if(mode==0){
			obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleMonth,2);
			mode = (Integer) obj[0];
		}
		if(mode==1){
			List<Long> tasks = commonDao.findByQuery("SELECT t.id FROM Task t WHERE t.type =:type"
					+ " AND t.messageId =:messageId AND to_char(t.createTime,'yyyy-MM-dd') =:createTime", 
					new String[]{"type","messageId","createTime"}, 
					new Object[]{WMSBillingCategoryRuleChain.BILL00303day,task.getMessageId(),
							JavaTools.format(task.getCreateTime(), JavaTools.y_m_d)});
			if(tasks==null || tasks.size()<=0){
				Task taskEntity = new Task(WMSBillingCategoryRuleChain.BILL00303day,"wmsBillingExecuteManager"
						+MyUtils.spilt1+WMSBillingCategoryRuleChain.BILL00303day, ww.getId());
				commonDao.store(taskEntity);
				task.setStatus(TaskStatus.STAT_FINISH);
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_03, task.getId().toString(), 
						"不合格品管理费按天已计算过", task.getSubscriber(), "不合格品管理费按天已计算过");
			}
		}else if(mode==2){
			String date = JavaTools.mosaicDate(1,task.getStartTime()==null?new Date():task.getStartTime(),JavaTools.y_m_d);// yyyy-MM-01
			//每月1号计算
			List<Long> tasks = commonDao.findByQuery("SELECT t.id FROM Task t WHERE t.type =:type"
					+ " AND t.messageId =:messageId AND to_char(t.createTime,'yyyy-MM-dd') =:createTime", 
					new String[]{"type","messageId","createTime"}, 
					new Object[]{WMSBillingCategoryRuleChain.BILL00303month,task.getMessageId(),date});
			if(tasks==null || tasks.size()<=0){
				if(date.equals(JavaTools.format(new Date(),JavaTools.y_m_d))){
					Task taskEntity = new Task(WMSBillingCategoryRuleChain.BILL00303month,"wmsBillingExecuteManager"
							+MyUtils.spilt1+WMSBillingCategoryRuleChain.BILL00303month, ww.getId());
					commonDao.store(taskEntity);
					task.setStatus(TaskStatus.STAT_FINISH);
				}else{
					task.setStatus(TaskStatus.STAT_FAIL);
					billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_03, task.getId().toString(), 
							"不合格品管理费按月每月1号计算", task.getSubscriber(), "不合格品管理费按月每月1号计算");
				}
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_03, task.getId().toString(), 
						"不合格品管理费按月已计算过", task.getSubscriber(), "不合格品管理费按月已计算过");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_03, task.getId().toString(), 
					"不合格品管理费只能按天或按月计算", task.getSubscriber(), "不合格品管理费只能按天或按月计算");
		}
		commonDao.store(task);
	}
	//退货服务费
	@SuppressWarnings("unchecked")
	public void BILL003_04(Long id){
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww==null){
			throw new BusinessException("合同明细ID="+task.getMessageId()+"数据不存在");
		}
		String billingMode = ww.getBillingMode();//WMSBillingModelInterface
		System.out.println("BILL003_04....."+id+"...."+billingMode);
		String smallCategory = ww.getSmallCategory().getName();
		//0-无,1-day,2-month
		Object[] obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleDay,1);
		int mode = (Integer) obj[0];
		if(mode==0){
			obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleMonth,2);
			mode = (Integer) obj[0];
		}
		if(mode==1){
			List<Long> tasks = commonDao.findByQuery("SELECT t.id FROM Task t WHERE t.type =:type"
					+ " AND t.messageId =:messageId AND to_char(t.createTime,'yyyy-MM-dd') =:createTime", 
					new String[]{"type","messageId","createTime"}, 
					new Object[]{WMSBillingCategoryRuleChain.BILL00304day,task.getMessageId(),
							JavaTools.format(task.getCreateTime(), JavaTools.y_m_d)});
			if(tasks==null || tasks.size()<=0){
				Task taskEntity = new Task(WMSBillingCategoryRuleChain.BILL00304day,"wmsBillingExecuteManager"
						+MyUtils.spilt1+WMSBillingCategoryRuleChain.BILL00304day, ww.getId());
				commonDao.store(taskEntity);
				task.setStatus(TaskStatus.STAT_FINISH);
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_04, task.getId().toString(), 
						"退货服务费按天已计算过", task.getSubscriber(), "退货服务费按天已计算过");
			}
		}else if(mode==2){
			String date = JavaTools.mosaicDate(1,task.getStartTime()==null?new Date():task.getStartTime(),JavaTools.y_m_d);// yyyy-MM-01
			//每月1号计算
			List<Long> tasks = commonDao.findByQuery("SELECT t.id FROM Task t WHERE t.type =:type"
					+ " AND t.messageId =:messageId AND to_char(t.createTime,'yyyy-MM-dd') =:createTime", 
					new String[]{"type","messageId","createTime"}, 
					new Object[]{WMSBillingCategoryRuleChain.BILL00304month,task.getMessageId(),date});
			if(tasks==null || tasks.size()<=0){
				if(date.equals(JavaTools.format(new Date(),JavaTools.y_m_d))){
					Task taskEntity = new Task(WMSBillingCategoryRuleChain.BILL00304month,"wmsBillingExecuteManager"
							+MyUtils.spilt1+WMSBillingCategoryRuleChain.BILL00304month, ww.getId());
					commonDao.store(taskEntity);
					task.setStatus(TaskStatus.STAT_FINISH);
				}else{
					task.setStatus(TaskStatus.STAT_FAIL);
					billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_04, task.getId().toString(), 
							"退货服务费按月每月1号计算", task.getSubscriber(), "退货服务费按月每月1号计算");
				}
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_04, task.getId().toString(), 
						"退货服务费按月已计算过", task.getSubscriber(), "退货服务费按月已计算过");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_04, task.getId().toString(), 
					"退货服务费只能按天或按月计算", task.getSubscriber(), "退货服务只能按天或按月计算");
		}
		commonDao.store(task);
	}
	//工装租赁费
	@SuppressWarnings("unchecked")
	public void BILL003_05(Long id){
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww==null){
			throw new BusinessException("合同明细ID="+task.getMessageId()+"数据不存在");
		}
		String billingMode = ww.getBillingMode();//WMSBillingModelInterface
		System.out.println("BILL003_05....."+id+"...."+billingMode);
		String smallCategory = ww.getSmallCategory().getName();
		//0-无,1-day,2-month
		Object[] obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleDay,1);
		int mode = (Integer) obj[0];
		if(mode==0){
			obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleMonth,2);
			mode = (Integer) obj[0];
		}
		if(mode==2){
			String date = JavaTools.mosaicDate(1,task.getStartTime()==null?new Date():task.getStartTime(),JavaTools.y_m_d);// yyyy-MM-01
			//每月1号计算
			List<Long> tasks = commonDao.findByQuery("SELECT t.id FROM Task t WHERE t.type =:type"
					+ " AND t.messageId =:messageId AND to_char(t.createTime,'yyyy-MM-dd') =:createTime", 
					new String[]{"type","messageId","createTime"}, 
					new Object[]{WMSBillingCategoryRuleChain.BILL00305month,task.getMessageId(),date});
			if(tasks==null || tasks.size()<=0){
				if(date.equals(JavaTools.format(new Date(),JavaTools.y_m_d))){
					Task taskEntity = new Task(WMSBillingCategoryRuleChain.BILL00305month,"wmsBillingExecuteManager"
							+MyUtils.spilt1+WMSBillingCategoryRuleChain.BILL00305month, ww.getId());
					commonDao.store(taskEntity);
					task.setStatus(TaskStatus.STAT_FINISH);
				}else{
					task.setStatus(TaskStatus.STAT_FAIL);
					billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_05, task.getId().toString(), 
							"工装租赁费费按月每月1号计算", task.getSubscriber(), "工装租赁费按月每月1号计算");
				}
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_05, task.getId().toString(), 
						"工装租赁费按月已计算过", task.getSubscriber(), "工装租赁费按月已计算过");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_05, task.getId().toString(), 
					"工装租赁费只能按月计算", task.getSubscriber(), "工装租赁费只能按月计算");
		}
		commonDao.store(task);
	}
	//工装管理费
	@SuppressWarnings("unchecked")
	public void BILL003_06(Long id){
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww==null){
			throw new BusinessException("合同明细ID="+task.getMessageId()+"数据不存在");
		}
		String billingMode = ww.getBillingMode();//WMSBillingModelInterface
		System.out.println("BILL003_06....."+id+"...."+billingMode);
		String smallCategory = ww.getSmallCategory().getName();
		//0-无,1-day,2-month
		Object[] obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleDay,1);
		int mode = (Integer) obj[0];
		if(mode==0){
			obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleMonth,2);
			mode = (Integer) obj[0];
		}
		if(mode==1){
			List<Long> tasks = commonDao.findByQuery("SELECT t.id FROM Task t WHERE t.type =:type"
					+ " AND t.messageId =:messageId AND to_char(t.createTime,'yyyy-MM-dd') =:createTime", 
					new String[]{"type","messageId","createTime"}, 
					new Object[]{WMSBillingCategoryRuleChain.BILL00306day,task.getMessageId(),
							JavaTools.format(task.getCreateTime(), JavaTools.y_m_d)});
			if(tasks==null || tasks.size()<=0){
				Task taskEntity = new Task(WMSBillingCategoryRuleChain.BILL00306day,"wmsBillingExecuteManager"
						+MyUtils.spilt1+WMSBillingCategoryRuleChain.BILL00306day, ww.getId());
				commonDao.store(taskEntity);
				task.setStatus(TaskStatus.STAT_FINISH);
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_06, task.getId().toString(), 
						"工装管理费按天已计算过", task.getSubscriber(), "工装管理费按天已计算过");
			}
		}else if(mode==2){
			String date = JavaTools.mosaicDate(1,task.getStartTime()==null?new Date():task.getStartTime(),JavaTools.y_m_d);// yyyy-MM-01
			//每月1号计算
			List<Long> tasks = commonDao.findByQuery("SELECT t.id FROM Task t WHERE t.type =:type"
					+ " AND t.messageId =:messageId AND to_char(t.createTime,'yyyy-MM-dd') =:createTime", 
					new String[]{"type","messageId","createTime"}, 
					new Object[]{WMSBillingCategoryRuleChain.BILL00306month,task.getMessageId(),date});
			if(tasks==null || tasks.size()<=0){
				if(date.equals(JavaTools.format(new Date(),JavaTools.y_m_d))){
					Task taskEntity = new Task(WMSBillingCategoryRuleChain.BILL00306month,"wmsBillingExecuteManager"
							+MyUtils.spilt1+WMSBillingCategoryRuleChain.BILL00306month, ww.getId());
					commonDao.store(taskEntity);
					task.setStatus(TaskStatus.STAT_FINISH);
				}else{
					task.setStatus(TaskStatus.STAT_FAIL);
					billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_06, task.getId().toString(), 
							"工装管理费按月每月1号计算", task.getSubscriber(), "工装管理费按月每月1号计算");
				}
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_06, task.getId().toString(), 
						"工装管理费按月已计算过", task.getSubscriber(), "工装管理费按月已计算过");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_06, task.getId().toString(), 
					"工装管理费只能按天或按月计算", task.getSubscriber(), "工装管理费只能按天或按月计算");
		}
		commonDao.store(task);
	}
	//打包服务费
	@SuppressWarnings("unchecked")
	public void BILL003_07(Long id){
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww==null){
			task.setStatus(TaskStatus.STAT_FAIL);
			commonDao.store(task);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_07, task.getId().toString(), 
					"合同明细ID="+task.getMessageId()+"数据不存在", task.getSubscriber(), 
					"合同明细ID="+task.getMessageId()+"数据不存在");
//			throw new BusinessException("合同明细ID="+task.getMessageId()+"数据不存在");
			return;
		}
		String billingMode = ww.getBillingMode();//WMSBillingModelInterface
		System.out.println("BILL003_07....."+id+"...."+billingMode);
		String smallCategory = ww.getSmallCategory().getName();
		//0-无,1-day,2-month
		Object[] obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleDay,1);
		int mode = (Integer) obj[0];
		if(mode==0){
			obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleMonth,2);
			mode = (Integer) obj[0];
		}
		if(mode==2){
			String date = JavaTools.mosaicDate(1,task.getStartTime()==null?new Date():task.getStartTime(),JavaTools.y_m_d);// yyyy-MM-01
			//每月1号计算
			List<Long> tasks = commonDao.findByQuery("SELECT t.id FROM Task t WHERE t.type =:type"
					+ " AND t.messageId =:messageId AND to_char(t.createTime,'yyyy-MM-dd') =:createTime", 
					new String[]{"type","messageId","createTime"}, 
					new Object[]{WMSBillingCategoryRuleChain.BILL00307month,task.getMessageId(),date});
			if(tasks==null || tasks.size()<=0){
				if(date.equals(JavaTools.format(task.getStartTime()==null?new Date():task.getStartTime(),JavaTools.y_m_d))){
					Task taskEntity = new Task(WMSBillingCategoryRuleChain.BILL00307month,"wmsBillingExecuteManager"
							+MyUtils.spilt1+WMSBillingCategoryRuleChain.BILL00307month, ww.getId());
					commonDao.store(taskEntity);
					task.setStatus(TaskStatus.STAT_FINISH);
				}else{
					task.setStatus(TaskStatus.STAT_FAIL);
					billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_07, task.getId().toString(), 
							"打包服务费按月每月1号计算", task.getSubscriber(), "打包服务费按月每月1号计算");
				}
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_07, task.getId().toString(), 
						"打包服务费按月已计算过", task.getSubscriber(), "打包服务费按月已计算过");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_07, task.getId().toString(), 
					"打包服务费只能按月计算", task.getSubscriber(), "打包服务费只能按月计算");
		}
		commonDao.store(task);
	}
	//基本服务费
	@SuppressWarnings("unchecked")
	public void BILL004_01(Long id){
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww==null){
			throw new BusinessException("合同明细ID="+task.getMessageId()+"数据不存在");
		}
		String billingMode = ww.getBillingMode();//WMSBillingModelInterface
		System.out.println("BILL004_01....."+id+"...."+billingMode);
		String smallCategory = ww.getSmallCategory().getName();
		//0-无,1-day,2-month
		Object[] obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleDay,1);
		int mode = (Integer) obj[0];
		if(mode==0){
			obj = getChainMode(smallCategory, billingMode,WMSBillingCategoryRuleChain.chainModleMonth,2);
			mode = (Integer) obj[0];
		}
		if(mode==2){
			String date = JavaTools.mosaicDate(1,task.getStartTime()==null?new Date():task.getStartTime(),JavaTools.y_m_d);// yyyy-MM-01
			//每月1号计算
			List<Long> tasks = commonDao.findByQuery("SELECT t.id FROM Task t WHERE t.type =:type"
					+ " AND t.messageId =:messageId AND to_char(t.createTime,'yyyy-MM-dd') =:createTime", 
					new String[]{"type","messageId","createTime"}, 
					new Object[]{WMSBillingCategoryRuleChain.BILL00401month,task.getMessageId(),date});
			if(tasks==null || tasks.size()<=0){
				if(date.equals(JavaTools.format(new Date(),JavaTools.y_m_d))){
					Task taskEntity = new Task(WMSBillingCategoryRuleChain.BILL00401month,"wmsBillingExecuteManager"
							+MyUtils.spilt1+WMSBillingCategoryRuleChain.BILL00401month, ww.getId());
					commonDao.store(taskEntity);
					task.setStatus(TaskStatus.STAT_FINISH);
				}else{
					task.setStatus(TaskStatus.STAT_FAIL);
					billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL004_01, task.getId().toString(), 
							"基本服务费按月每月1号计算", task.getSubscriber(), "基本服务费按月每月1号计算");
				}
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL004_01, task.getId().toString(), 
						"基本服务费按月已计算过", task.getSubscriber(), "基本服务费按月已计算过");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL004_01, task.getId().toString(), 
					"基本服务费只能按月计算", task.getSubscriber(), "基本服务费只能按月计算");
		}
		commonDao.store(task);
	}
	/**********************************************S------P-----I-----L-----T*********************************/
	/**********************************************S------P-----I-----L-----T*********************************/
	/**********************************************S------P-----I-----L-----T*********************************/
	//仓租租凭(恒定)服务费-month
	public void BILL001_01_month(Long id){
		System.out.println("BILL001_01_month....."+id);
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww!=null && ww.getContact()!=null){
			WMSContact w = commonDao.load(WMSContact.class, ww.getContact().getId());
			if(w.getSupplier()!=null){
				WmsOrganization supplier = commonDao.load(WmsOrganization.class,w.getSupplier().getId());
				WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,w.getWarehouse().getId());
				//每月1号计算(上个月的仓租租凭(恒定)服务费)
				Date yesTerday = JavaTools.beforeOneDate(task.getCreateTime());
				String yyyyMM = JavaTools.format(yesTerday, JavaTools.ym);

				String g1 = null,//L20002#仓租租凭(恒定)服务费#201509
						g2 = null;//仓储租凭费用
				StringBuffer row=new StringBuffer();
				row.append("organization="+WmsBillMode.organization+JavaTools.enter);
		    	row.append("template="+WmsBillMode.template001+JavaTools.enter);
		    	g1 = supplier.getCode()+MyUtils.spilt1+WMSBillingCategoryMiddleEnum.BILL001_01+MyUtils.spilt1+yyyyMM;
		    	g2 = WMSBillingCategoryBigEnum.BILL001;
		    	//L20002#仓租租凭(恒定)服务费#201509,仓储租凭费用,2015-09-16,L20002,锦州光和密封实业有限公司,仓库code#合同编码,-,仓储租凭(恒定)服务费,MONTH,0,2000
		    	row.append(g1)
		    		.append(",").append(g2)
		    		.append(",").append(JavaTools.format(yesTerday, JavaTools.y_m_d))//2015-08-31
		    		.append(",").append(supplier.getCode())//L20002
		    		.append(",").append(supplier.getName())//锦州光和密封实业有限公司
		    		.append(",").append(warehouse.getCode()+MyUtils.spilt1+w.getCode())//仓库code#合同编码
		    		.append(",").append(BaseStatus.NULLVALUE)
		    		.append(",").append(WMSBillingCategoryMiddleEnum.BILL001_01)//仓储租凭(恒定)服务费
		    		.append(",").append(WMSBillingModelInterface.MONTH)//MONTH
		    		.append(",").append(0)//件数
		    		.append(",").append(0);//一口价,代表总金额
		    	//fdj_供应商编码_yyyyMM_101.txt
		    	JavaTools.createTxt(WmsBillMode.txtPath+"/fdj_"+supplier.getCode()+"_"+
		    		JavaTools.format(task.getCreateTime(), JavaTools.ym)+"_101.txt", row, "utf-8");
		    	task.setStatus(TaskStatus.STAT_FINISH);
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
						"[仓租租凭(恒定)服务费-month]无供应商信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到供应商信息");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
					"[仓租租凭(恒定)服务费-month]无合同信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到合同信息");
		}
		commonDao.store(task);
	}
	//仓储服务增租费-month
	public void BILL001_02_month(Long id){
		System.out.println("BILL001_02_month....."+id);
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww!=null && ww.getContact()!=null){
			WMSContact w = commonDao.load(WMSContact.class, ww.getContact().getId());
			if(w.getSupplier()!=null){
				WmsOrganization supplier = commonDao.load(WmsOrganization.class,w.getSupplier().getId());
				WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,w.getWarehouse().getId());
				//每月1号计算(上个月的仓储服务增租费)
				Date yesTerday = JavaTools.beforeOneDate(task.getCreateTime());
				String yyyyMM = JavaTools.format(yesTerday, JavaTools.ym);

				String g1 = null,//L20002#仓储服务增租费#201509
						g2 = null;//仓储租凭费用
				StringBuffer row=new StringBuffer();
				row.append("organization="+WmsBillMode.organization+JavaTools.enter);
		    	row.append("template="+WmsBillMode.template001+JavaTools.enter);
		    	g1 = supplier.getCode()+MyUtils.spilt1+WMSBillingCategoryMiddleEnum.BILL001_02+MyUtils.spilt1+yyyyMM;
		    	g2 = WMSBillingCategoryBigEnum.BILL001;
		    	//L20002#仓储服务增租费#201509,仓储租凭费用,2015-09-17,L20002,锦州光和密封实业有限公司,仓库code#合同编码,-,仓储服务增租费,MONTH,0,2000
		    	row.append(g1)
		    		.append(",").append(g2)
		    		.append(",").append(JavaTools.format(yesTerday, JavaTools.y_m_d))//2015-09-30
		    		.append(",").append(supplier.getCode())//L20002
		    		.append(",").append(supplier.getName())//锦州光和密封实业有限公司
		    		.append(",").append(warehouse.getCode()+MyUtils.spilt1+w.getCode())//仓库code#合同编码
		    		.append(",").append(BaseStatus.NULLVALUE)
		    		.append(",").append(WMSBillingCategoryMiddleEnum.BILL001_02)//仓储服务增租费
		    		.append(",").append(WMSBillingModelInterface.MONTH)//MONTH
		    		.append(",").append(0)//件数
		    		.append(",").append(0);//一口价,代表总金额
		    	//fdj_供应商编码_yyyyMM_102.txt
		    	JavaTools.createTxt(WmsBillMode.txtPath+"/fdj_"+supplier.getCode()+"_"+
		    		JavaTools.format(task.getCreateTime(), JavaTools.ym)+"_102.txt", row, "utf-8");
		    	task.setStatus(TaskStatus.STAT_FINISH);
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
						"[仓储服务增租费-month]无供应商信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到供应商信息");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
					"[仓储服务增租费-month]无合同信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到合同信息");
		}
		commonDao.store(task);
	}
	//仓库租赁(托盘数)服务费-day
	@SuppressWarnings("unchecked")
	public void BILL001_03_day(Long id){
		System.out.println("BILL001_03_day....."+id);
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww==null){
			throw new BusinessException("合同明细ID="+task.getMessageId()+"数据不存在");
		}
		WMSContact w = commonDao.load(WMSContact.class,ww.getContact().getId());
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,w.getWarehouse().getId());
		WmsOrganization supplier = commonDao.load(WmsOrganization.class,w.getSupplier().getId());
		String happenDate = JavaTools.format(task.getCreateTime(),JavaTools.y_m_d);;//2015-09-18
		List<String> pallets = commonDao.findByQuery("select distinct ix.pallet"+
				" from WmsInventoryExtend ix"+
				" left join ix.inventory inv"+
				" left join ix.inventory.itemKey ik"+
				" left join ix.inventory.itemKey.lotInfo.supplier sup"+
				" left join ix.inventory.location l"+
				" left join ix.inventory.location.warehouse w"+
				" where w.id =:warehouseId and sup.code =:supCode"+
				" and ix.pallet is not null and ix.pallet not in ('-') and ix.quantityBU>0", 
				new String[]{"warehouseId","supCode"}, 
				new Object[]{warehouse.getId(),supplier.getCode()});
		if(pallets!=null && pallets.size()>0){
			String g1 = null,//L20002#仓储租赁(托盘数)服务费#20150918
					g2 = null;//仓储租凭费用
			//L20002#仓储租赁(托盘数)服务费#20150918,仓储租凭费用,2015-09-18,L20002,锦州光和密封实业有限公司,仓库code#合同编码,-,仓储租赁(托盘数)服务费,-,200,0
			StringBuffer row=new StringBuffer();
	    	row.append("organization="+WmsBillMode.organization+JavaTools.enter);
	    	row.append("template="+WmsBillMode.template001+JavaTools.enter);
	    	g1 = supplier.getCode()+MyUtils.spilt1+WMSBillingCategoryMiddleEnum.BILL001_03+
	    			MyUtils.spilt1+JavaTools.format(task.getCreateTime(), JavaTools.dmy);
	    	g2 = WMSBillingCategoryBigEnum.BILL001;
	    	row.append(g1)
    		.append(",").append(g2)
    		.append(",").append(happenDate)
    		.append(",").append(supplier.getCode())//L20002
    		.append(",").append(supplier.getName())//锦州光和密封实业有限公司
    		.append(",").append(warehouse.getCode()+MyUtils.spilt1+w.getCode())//仓库code#合同编码
    		.append(",").append(BaseStatus.NULLVALUE)//物料编码
    		.append(",").append(WMSBillingCategoryMiddleEnum.BILL001_03)//仓储租赁(托盘数)服务费
    		.append(",").append(WMSBillingModelInterface.PACKAGES)//PACKAGES
    		.append(",").append(pallets.size())//件数
    		.append(",").append(0);//一口价
	    	//fdj_供应商编码_yyyyMMdd_103.txt
	    	JavaTools.createTxt(WmsBillMode.txtPath+"/fdj_"+supplier.getCode()+"_"+
	    			JavaTools.format(task.getCreateTime(), JavaTools.yymd)+"_103.txt", row, "utf-8");
	    	task.setStatus(TaskStatus.STAT_FINISH);
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_03, task.getId().toString(), 
					"仓储租赁(托盘数)服务费-day无托盘信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到托盘信息");
		}
		commonDao.store(task);
	}
	//配送服务费-month(销售金额)
	@SuppressWarnings("unchecked")
	public void BILL002_01_month(Long id){
		System.out.println("BILL002_01_month....."+id);
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww!=null && ww.getContact()!=null){
			WMSContact w = commonDao.load(WMSContact.class, ww.getContact().getId());
			if(w.getSupplier()!=null){
				WmsOrganization supplier = commonDao.load(WmsOrganization.class,w.getSupplier().getId());
				WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,w.getWarehouse().getId());
				//每月1号计算(上个月的配送服务费)
				Date yesTerday = JavaTools.beforeOneDate(task.getCreateTime());
				String yyyyMM = JavaTools.format(yesTerday, JavaTools.ym);
				List<Long> wmspurchaseIds = commonDao.findByQuery("SELECT pui.id FROM WMSPurchaseInvoiceDetail pui"
						+ " WHERE pui.supplier.id =:supplierId"
						+ " AND pui.status =:status AND to_char(pui.invoiceDate,'yyyyMM') = :invoiceDate", 
						new String[]{"supplierId","status","invoiceDate"}, 
						new Object[]{supplier.getId(),WMSPurchaseStatus.ENABLED,yyyyMM});
				if(wmspurchaseIds!=null && wmspurchaseIds.size()>0){
					Double amount=0D;
					String pus = "";//采购发票号
					for(Long purId : wmspurchaseIds){
						WMSPurchaseInvoiceDetail pui = commonDao.load(WMSPurchaseInvoiceDetail.class, purId);
						amount += pui.getAmount();
						pus += pui.getCode()+WMSBillingCategoryRuleChain.and;
						pui.setStatus(WMSPurchaseStatus.SENDED);
						commonDao.store(pui);
					}
					pus = StringUtils.substring(pus, 0, pus.length()-1);
					if(amount>0){
						String g1 = null,//L20002#配送服务费#201509
								g2 = null;//配送费用
						StringBuffer row=new StringBuffer();
						row.append("organization="+WmsBillMode.organization+JavaTools.enter);
				    	row.append("template="+WmsBillMode.template001+JavaTools.enter);
				    	g1 = supplier.getCode()+MyUtils.spilt1+WMSBillingCategoryMiddleEnum.BILL002_01+MyUtils.spilt1+yyyyMM;
				    	g2 = WMSBillingCategoryBigEnum.BILL002;
				    	//L20002#配送服务费#201508,配送费用,2015-08-31,L20002,锦州光和密封实业有限公司,仓库code#合同编码#采购发票号,-,配送服务费,SALE_AMOUNT,0,300
				    	row.append(g1)
				    		.append(",").append(g2)
				    		.append(",").append(JavaTools.format(yesTerday, JavaTools.y_m_d))//2015-08-31
				    		.append(",").append(supplier.getCode())//L20002
				    		.append(",").append(supplier.getName())//锦州光和密封实业有限公司
				    		.append(",").append(warehouse.getCode()+MyUtils.spilt1+w.getCode()+MyUtils.spilt1+pus)//仓库code#合同编码#采购发票号
				    		.append(",").append(BaseStatus.NULLVALUE)
				    		.append(",").append(WMSBillingCategoryMiddleEnum.BILL002_01)//配送服务费
				    		.append(",").append(WMSBillingModelInterface.SALE_AMOUNT)//SALE_AMOUNT
				    		.append(",").append(amount)//件数,代表总金额
				    		.append(",").append(0);//一口价
				    	//fdj_供应商编码_yyyyMM_203.txt
				    	JavaTools.createTxt(WmsBillMode.txtPath+"/fdj_"+supplier.getCode()+"_"+
				    		JavaTools.format(task.getCreateTime(), JavaTools.ym)+"_203.txt", row, "utf-8");
				    	task.setStatus(TaskStatus.STAT_FINISH);
					}else{
						task.setStatus(TaskStatus.STAT_FAIL);
						billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
								"配送服务费-month费用采购发票金额有误", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"费用采购发票金额有误");
					}
					
				}else{
					task.setStatus(TaskStatus.STAT_FAIL);
					billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
							"配送服务费-month费用采购无发票信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到费用采购发票信息");
				}
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
						"配送服务费-month无供应商信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到供应商信息");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
					"配送服务费-month无合同信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到合同信息");
		}
		commonDao.store(task);
	}
	//配送服务费-day(收货)
	@SuppressWarnings("unchecked")
	public void BILL002_01_RECEIVE(Long id){
		System.out.println("BILL002_01_RECEIVE....."+id);
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww!=null && ww.getContact()!=null){
			WMSContact w = commonDao.load(WMSContact.class, ww.getContact().getId());
			if(w.getSupplier()!=null){
				WmsOrganization supplier = commonDao.load(WmsOrganization.class,w.getSupplier().getId());
				WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,w.getWarehouse().getId());
				String happenDate = JavaTools.format(task.getCreateTime(),JavaTools.y_m_d);;//2015-09-18
				List<Object[]> asns = commonDao.findByQuery("SELECT r.asnDetail.item.code,r.asnDetail.item.name,sum(r.receivedQuantityBU)"
						+ " FROM WmsReceivedRecord r"
						+ " LEFT JOIN r.itemKey.lotInfo.supplier supplier "
						+ " WHERE r.asn.warehouse.id =:warehouseId AND supplier.id =:supplierId"
						+ " AND r.asn.billType.code ='IN-001'"
						+ " AND to_char(r.updateInfo.createdTime,'yyyy-MM-dd') =:createdTime"
						+ " GROUP BY r.asnDetail.item.code,r.asnDetail.item.name", 
						new String[]{"warehouseId","supplierId","createdTime"}, 
						new Object[]{warehouse.getId(),supplier.getId(),happenDate});
				if(asns!=null && asns.size()>0){
					String g1 = null,//L20002#配送服务费#20150918
							g2 = null;//配送费用
					//L20002#配送服务费#20150918,配送费用,2015-09-18,L20002,锦州光和密封实业有限公司,仓库code#合同编码,1044013GG010,配送服务费,RECEIVE,120,0
					StringBuffer row=new StringBuffer();
			    	row.append("organization="+WmsBillMode.organization+JavaTools.enter);
			    	row.append("template="+WmsBillMode.template001+JavaTools.enter);
			    	g1 = supplier.getCode()+MyUtils.spilt1+WMSBillingCategoryMiddleEnum.BILL002_01+
			    			MyUtils.spilt1+JavaTools.format(task.getCreateTime(), JavaTools.dmy);
			    	g2 = WMSBillingCategoryBigEnum.BILL002;
			    	int k = 0;
			    	for(Object[] obj : asns){
			    		row.append(g1)
			    		.append(",").append(g2)
			    		.append(",").append(happenDate)
			    		.append(",").append(supplier.getCode())//L20002
			    		.append(",").append(supplier.getName())//锦州光和密封实业有限公司
			    		.append(",").append(warehouse.getCode()+MyUtils.spilt1+w.getCode())//仓库code#合同编码
			    		.append(",").append(obj[0])//物料编码
			    		.append(",").append(WMSBillingCategoryMiddleEnum.BILL002_01)//配送服务费
			    		.append(",").append(WMSBillingModelInterface.RECEIVE)//RECEIVE
			    		.append(",").append(obj[2])//件数
			    		.append(",").append(0);//一口价
			    		if(k<asns.size()-1){
			    			row.append(JavaTools.enter);
			    		}
			    		k++;
			    	}
			    	//fdj_供应商编码_yyMMdd _201.txt
			    	JavaTools.createTxt(WmsBillMode.txtPath+"/fdj_"+supplier.getCode()+"_"+
			    			JavaTools.format(task.getCreateTime(), JavaTools.yymd)+"_201.txt", row, "utf-8");
			    	task.setStatus(TaskStatus.STAT_FINISH);
				}else{
					task.setStatus(TaskStatus.STAT_FAIL);
					billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
							"[配送服务费-收货]无入库信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到入库信息");
				}
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
						"[配送服务费-收货]无供应商信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到供应商信息");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
					"[配送服务费-收货]无合同信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到合同信息");
		}
		commonDao.store(task);
	}
	//配送服务费-day(签收)
	@SuppressWarnings("unchecked")
	public void BILL002_01_SIGNED(Long id){
		System.out.println("BILL002_01_SIGNED....."+id);
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww!=null && ww.getContact()!=null){
			WMSContact w = commonDao.load(WMSContact.class, ww.getContact().getId());
			if(w.getSupplier()!=null){
				WmsOrganization supplier = commonDao.load(WmsOrganization.class,w.getSupplier().getId());
				WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,w.getWarehouse().getId());
				String happenDate = JavaTools.format(task.getCreateTime(),JavaTools.y_m_d);;//2015-09-18
				List<Object[]> pallets = commonDao.findByQuery("select pp.item.code,pp.item.name,sum(pp.shippedQuantityBU)"+
						" from WmsPickTicketDetail pp"+
						" left join pp.pickTicket p "+
						" left join pp.pickTicket.warehouse w"+
						" left join pp.supplier sup"+
						" left join p.billType billType"+
						" where w.id =:warehouseId and sup.code =:supCode"+
						" and billType.code =:billType and p.status =:status"+
						" and to_char(p.finshDate,'yyyy-MM-dd') =:finshDate"+
						" GROUP BY pp.item.code,pp.item.name", 
						new String[]{"warehouseId","supCode","billType","status","finshDate"}, 
						new Object[]{warehouse.getId(),supplier.getCode(),"OUT-001",WmsPickTicketStatus.FINISHED,happenDate});
				if(pallets!=null && pallets.size()>0){
					String g1 = null,//L20002#配送服务费#20150918
							g2 = null;//配送费用
					//L20002#配送服务费#20150918,配送费用,2015-09-18,L20002,锦州光和密封实业有限公司,仓库code#合同编码,1044013GG010,配送服务费,SIGNED,200,0
					StringBuffer row=new StringBuffer();
			    	row.append("organization="+WmsBillMode.organization+JavaTools.enter);
			    	row.append("template="+WmsBillMode.template001+JavaTools.enter);
			    	g1 = supplier.getCode()+MyUtils.spilt1+WMSBillingCategoryMiddleEnum.BILL002_01+
			    			MyUtils.spilt1+JavaTools.format(task.getCreateTime(), JavaTools.dmy);
			    	g2 = WMSBillingCategoryBigEnum.BILL002;
			    	int k = 0;
			    	for(Object[] obj : pallets){
			    		row.append(g1)
			    		.append(",").append(g2)
			    		.append(",").append(happenDate)
			    		.append(",").append(supplier.getCode())//L20002
			    		.append(",").append(supplier.getName())//锦州光和密封实业有限公司
			    		.append(",").append(warehouse.getCode()+MyUtils.spilt1+w.getCode())//仓库code#合同编码
			    		.append(",").append(obj[0])//物料编码
			    		.append(",").append(WMSBillingCategoryMiddleEnum.BILL002_01)//配送服务费
			    		.append(",").append(WMSBillingModelInterface.SIGNED)//SIGNED
			    		.append(",").append(obj[2])//件数
			    		.append(",").append(0);//一口价
			    		if(k<pallets.size()-1){
			    			row.append(JavaTools.enter);
			    		}
			    		k++;
			    	}
			    	//fdj_供应商编码_ yyMMdd_202.txt
			    	JavaTools.createTxt(WmsBillMode.txtPath+"/fdj_"+supplier.getCode()+"_"+
			    			JavaTools.format(task.getCreateTime(), JavaTools.yymd)+"_202.txt", row, "utf-8");
			    	task.setStatus(TaskStatus.STAT_FINISH);
				}else{
					task.setStatus(TaskStatus.STAT_FAIL);
					billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
							"[配送服务费-签收]无签收信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到签收信息");
				}
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
						"[配送服务费-签收]无供应商信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到供应商信息");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL002_01, task.getId().toString(), 
					"[配送服务费-签收]无合同信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到合同信息");
		}
		commonDao.store(task);
	}
	@SuppressWarnings("unchecked")
	public void BILL003_01_day(Long id){
		System.out.println("BILL003_01_day....."+id);
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww==null){
			throw new BusinessException("合同明细ID="+task.getMessageId()+"数据不存在");
		}
		WMSContact w = commonDao.load(WMSContact.class,ww.getContact().getId());
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,w.getWarehouse().getId());
		WmsOrganization supplier = commonDao.load(WmsOrganization.class,w.getSupplier().getId());
		String happenDate = JavaTools.format(task.getCreateTime(),JavaTools.y_m_d);;//2015-09-18
		List<Object[]> pallets = commonDao.findByQuery("select mdd.itemKey.item.code,mdd.itemKey.item.name,sum(mdd.movedQuantityBU)"+
				" from WmsMoveDocDetail mdd"+
				" LEFT JOIN mdd.moveDoc moveDoc "+
				" left join mdd.moveDoc.warehouse w"+
				" left join mdd.itemKey.lotInfo.supplier sup"+
				" left join mdd.moveDoc.billType billType"+
				" where w.id =:warehouseId and sup.code =:supCode"+
				" and billType.code =:billType and moveDoc.status =:status"+
				" and to_char(moveDoc.updateInfo.updateTime,'yyyy-MM-dd') =:finshDate"+
				" and moveDoc.type = 'MV_QUALITY_MOVE'"+
				" GROUP BY mdd.itemKey.item.code,mdd.itemKey.item.name", 
				new String[]{"warehouseId","supCode","billType","status","finshDate"}, 
				new Object[]{warehouse.getId(),supplier.getCode(),"MV_QUALITY_MOVE_02",WmsMoveDocStatus.FINISHED,happenDate});
		if(pallets!=null && pallets.size()>0){
			String g1 = null,//L20002#分选服务费#20150918
					g2 = null;//增值服务费用
			//L20002#分选服务费#20150918,增值服务费用,2015-09-18,L20002,锦州光和密封实业有限公司,仓库code#合同编码,1017014GG01,分选服务费,PACKAGES,20,0
			StringBuffer row=new StringBuffer();
	    	row.append("organization="+WmsBillMode.organization+JavaTools.enter);
	    	row.append("template="+WmsBillMode.template001+JavaTools.enter);
	    	g1 = supplier.getCode()+MyUtils.spilt1+WMSBillingCategoryMiddleEnum.BILL003_01+
	    			MyUtils.spilt1+JavaTools.format(task.getCreateTime(), JavaTools.dmy);
	    	g2 = WMSBillingCategoryBigEnum.BILL003;
	    	int k = 0;
	    	for(Object[] obj : pallets){
	    		row.append(g1)
	    		.append(",").append(g2)
	    		.append(",").append(happenDate)
	    		.append(",").append(supplier.getCode())//L20002
	    		.append(",").append(supplier.getName())//锦州光和密封实业有限公司
	    		.append(",").append(warehouse.getCode()+MyUtils.spilt1+w.getCode())//仓库code#合同编码
	    		.append(",").append(obj[0])//物料编码
	    		.append(",").append(WMSBillingCategoryMiddleEnum.BILL003_01)//分选服务费
	    		.append(",").append(WMSBillingModelInterface.PACKAGES)//PACKAGES
	    		.append(",").append(obj[2])//件数
	    		.append(",").append(0);//一口价
	    		if(k<pallets.size()-1){
	    			row.append(JavaTools.enter);
	    		}
	    		k++;
	    	}
	    	//fdj_供应商编码_yyMMdd_301.txt
	    	JavaTools.createTxt(WmsBillMode.txtPath+"/fdj_"+supplier.getCode()+"_"+
	    			JavaTools.format(task.getCreateTime(), JavaTools.yymd)+"_301.txt", row, "utf-8");
	    	task.setStatus(TaskStatus.STAT_FINISH);
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_01, task.getId().toString(), 
					"[分选服务费-件]无分选信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到分选信息");
		}
		commonDao.store(task);
	}
	//分选服务费-month
	public void BILL003_01_month(Long id){
		System.out.println("BILL003_01_month....."+id);
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww!=null && ww.getContact()!=null){
			WMSContact w = commonDao.load(WMSContact.class, ww.getContact().getId());
			if(w.getSupplier()!=null){
				WmsOrganization supplier = commonDao.load(WmsOrganization.class,w.getSupplier().getId());
				WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,w.getWarehouse().getId());
				String g1 = null,//L20002#分选服务费#201509
						g2 = null;//增值服务费用
				StringBuffer row=new StringBuffer();
				row.append("organization="+WmsBillMode.organization+JavaTools.enter);
		    	row.append("template="+WmsBillMode.template001+JavaTools.enter);
		    	//每月1号计算(上个月的分选服务费)
				Date yesTerday = JavaTools.beforeOneDate(task.getCreateTime());
				String yyyyMM = JavaTools.format(yesTerday, JavaTools.ym);
		    	g1 = supplier.getCode()+MyUtils.spilt1+WMSBillingCategoryMiddleEnum.BILL003_01+MyUtils.spilt1+yyyyMM;
		    	g2 = WMSBillingCategoryBigEnum.BILL003;
		    	//L20002#分选服务费#201509,增值服务费用,2015-09-18,L20002,锦州光和密封实业有限公司,仓库code#合同编码,-,分选服务费,MONTH,0,2000
		    	row.append(g1)
		    		.append(",").append(g2)
		    		.append(",").append(JavaTools.format(yesTerday, JavaTools.y_m_d))//2015-09-01
		    		.append(",").append(supplier.getCode())//L20002
		    		.append(",").append(supplier.getName())//锦州光和密封实业有限公司
		    		.append(",").append(warehouse.getCode()+MyUtils.spilt1+w.getCode())//仓库code#合同编码
		    		.append(",").append(BaseStatus.NULLVALUE)
		    		.append(",").append(WMSBillingCategoryMiddleEnum.BILL003_01)//分选服务费
		    		.append(",").append(WMSBillingModelInterface.MONTH)//MONTH
		    		.append(",").append(0)//件数
		    		.append(",").append(0);//一口价,将来可以这里赋值,也可以从LFCS规则表读取
		    	//fdj_供应商编码_yyyyMM_302.txt
		    	JavaTools.createTxt(WmsBillMode.txtPath+"/fdj_"+supplier.getCode()+"_"+
		    		JavaTools.format(task.getCreateTime(), JavaTools.ym)+"_302.txt", row, "utf-8");
		    	task.setStatus(TaskStatus.STAT_FINISH);
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_01, task.getId().toString(), 
						"分选服务费-month无供应商信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到供应商信息");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_01, task.getId().toString(), 
					"分选服务费-month无合同信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到合同信息");
		}
		commonDao.store(task);
	}
	//打包服务费-month
	public void BILL003_07_month(Long id){
		System.out.println("BILL003_07_month....."+id);
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww!=null && ww.getContact()!=null){
			WMSContact w = commonDao.load(WMSContact.class, ww.getContact().getId());
			if(w.getSupplier()!=null){
				WmsOrganization supplier = commonDao.load(WmsOrganization.class,w.getSupplier().getId());
				WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,w.getWarehouse().getId());
				String g1 = null,//L20002#打包服务费#201509
						g2 = null;//增值服务费用
				StringBuffer row=new StringBuffer();
				row.append("organization="+WmsBillMode.organization+JavaTools.enter);
		    	row.append("template="+WmsBillMode.template001+JavaTools.enter);
		    	//每月1号计算(上个月的打包服务费)
				Date yesTerday = JavaTools.beforeOneDate(task.getCreateTime());
				String yyyyMM = JavaTools.format(yesTerday, JavaTools.ym);
		    	g1 = supplier.getCode()+MyUtils.spilt1+WMSBillingCategoryMiddleEnum.BILL003_07+MyUtils.spilt1+yyyyMM;
		    	g2 = WMSBillingCategoryBigEnum.BILL003;
		    	//L20002#打包服务费#201509,增值服务费,2015-09-01,L20002,锦州光和密封实业有限公司,仓库code#合同编码,-,打包服务费,MONTH ,0,2000
		    	row.append(g1)
		    		.append(",").append(g2)
		    		.append(",").append(JavaTools.format(yesTerday, JavaTools.y_m_d))//2015-09-01
		    		.append(",").append(supplier.getCode())//L20002
		    		.append(",").append(supplier.getName())//锦州光和密封实业有限公司
		    		.append(",").append(warehouse.getCode()+MyUtils.spilt1+w.getCode())//仓库code#合同编码
		    		.append(",").append(BaseStatus.NULLVALUE)
		    		.append(",").append(WMSBillingCategoryMiddleEnum.BILL003_07)//打包服务费
		    		.append(",").append(WMSBillingModelInterface.MONTH)//MONTH
		    		.append(",").append(0)//件数
		    		.append(",").append(0);//一口价,将来可以这里赋值,也可以从LFCS规则表读取
		    	//fdj_供应商编码_yyyyMM_311.txt
		    	JavaTools.createTxt(WmsBillMode.txtPath+"/fdj_"+supplier.getCode()+"_"+
		    		JavaTools.format(task.getCreateTime(), JavaTools.ym)+"_311.txt", row, "utf-8");
		    	task.setStatus(TaskStatus.STAT_FINISH);
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_07, task.getId().toString(), 
						"打包服务费-month无供应商信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到供应商信息");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_07, task.getId().toString(), 
					"打包服务费-month无合同信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到合同信息");
		}
		commonDao.store(task);
	}
	//工装管理费-day
	@SuppressWarnings("unchecked")
	public void BILL003_06_day(Long id){
		System.out.println("BILL003_06_day....."+id);
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww==null){
			throw new BusinessException("合同明细ID="+task.getMessageId()+"数据不存在");
		}
		WMSContact w = commonDao.load(WMSContact.class,ww.getContact().getId());
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,w.getWarehouse().getId());
		WmsOrganization supplier = commonDao.load(WmsOrganization.class,w.getSupplier().getId());
		String happenDate = JavaTools.format(task.getCreateTime(),JavaTools.y_m_d);;//2015-09-18
		List<Object[]> pallets = commonDao.findByQuery("select pp.item.code,pp.item.name,sum(pp.shippedQuantityBU)"+
				" from WmsPickTicketDetail pp"+
				" left join pp.pickTicket p "+
				" left join pp.pickTicket.warehouse w"+
				" left join pp.supplier sup"+
				" left join p.billType billType"+
				" where w.id =:warehouseId and sup.code =:supCode"+
				" and billType.code =:billType and p.status =:status"+
				" and to_char(p.finshDate,'yyyy-MM-dd') =:finshDate"+
				" GROUP BY pp.item.code,pp.item.name", 
				new String[]{"warehouseId","supCode","billType","status","finshDate"}, 
				new Object[]{warehouse.getId(),supplier.getCode(),"OUT-004",WmsPickTicketStatus.FINISHED,happenDate});
		if(pallets!=null && pallets.size()>0){
			String g1 = null,//L20002#工装管理费#20150918
					g2 = null;//增值服务费用
			//L20002#工装管理费#20150918,增值服务费,2015-09-18,L20002,锦州光和密封实业有限公司,仓库code#合同编码,1044013GG010,工装管理费,PACKAGES,18,0
			StringBuffer row=new StringBuffer();
	    	row.append("organization="+WmsBillMode.organization+JavaTools.enter);
	    	row.append("template="+WmsBillMode.template001+JavaTools.enter);
	    	g1 = supplier.getCode()+MyUtils.spilt1+WMSBillingCategoryMiddleEnum.BILL003_06+
	    			MyUtils.spilt1+JavaTools.format(task.getCreateTime(), JavaTools.dmy);
	    	g2 = WMSBillingCategoryBigEnum.BILL003;
	    	int k = 0;
	    	for(Object[] obj : pallets){
	    		row.append(g1)
	    		.append(",").append(g2)
	    		.append(",").append(happenDate)
	    		.append(",").append(supplier.getCode())//L20002
	    		.append(",").append(supplier.getName())//锦州光和密封实业有限公司
	    		.append(",").append(warehouse.getCode()+MyUtils.spilt1+w.getCode())//仓库code#合同编码
	    		.append(",").append(obj[0])//物料编码
	    		.append(",").append(WMSBillingCategoryMiddleEnum.BILL003_06)//工装管理费
	    		.append(",").append(WMSBillingModelInterface.PACKAGES)//PACKAGES
	    		.append(",").append(obj[2])//件数
	    		.append(",").append(0);//一口价
	    		if(k<pallets.size()-1){
	    			row.append(JavaTools.enter);
	    		}
	    		k++;
	    	}
	    	//fdj_供应商编码_yyyyMMdd_310.txt
	    	JavaTools.createTxt(WmsBillMode.txtPath+"/fdj_"+supplier.getCode()+"_"+
	    			JavaTools.format(task.getCreateTime(), JavaTools.yymd)+"_310.txt", row, "utf-8");
	    	task.setStatus(TaskStatus.STAT_FINISH);
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_06, task.getId().toString(), 
					"[工装管理费-day]无发货信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到发货信息");
		}
		commonDao.store(task);
	}
	//工装管理费 -month
	public void BILL003_06_month(Long id){
		System.out.println("BILL003_06_month....."+id);
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww!=null && ww.getContact()!=null){
			WMSContact w = commonDao.load(WMSContact.class, ww.getContact().getId());
			if(w.getSupplier()!=null){
				WmsOrganization supplier = commonDao.load(WmsOrganization.class,w.getSupplier().getId());
				WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,w.getWarehouse().getId());
				String g1 = null,//L20002#工装管理费#201509
						g2 = null;//增值服务费用
				StringBuffer row=new StringBuffer();
				row.append("organization="+WmsBillMode.organization+JavaTools.enter);
		    	row.append("template="+WmsBillMode.template001+JavaTools.enter);
		    	//每月1号计算(上个月的工装管理费)
				Date yesTerday = JavaTools.beforeOneDate(task.getCreateTime());
				String yyyyMM = JavaTools.format(yesTerday, JavaTools.ym);
		    	g1 = supplier.getCode()+MyUtils.spilt1+WMSBillingCategoryMiddleEnum.BILL003_06+MyUtils.spilt1+yyyyMM;
		    	g2 = WMSBillingCategoryBigEnum.BILL003;
		    	//L20002#工装管理费#201509,增值服务费,2015-09-18,L20002,锦州光和密封实业有限公司,仓库code#合同编码,-,工装管理费,MONTH,0,2000
		    	row.append(g1)
		    		.append(",").append(g2)
		    		.append(",").append(JavaTools.format(yesTerday, JavaTools.y_m_d))//2015-09-01
		    		.append(",").append(supplier.getCode())//L20002
		    		.append(",").append(supplier.getName())//锦州光和密封实业有限公司
		    		.append(",").append(warehouse.getCode()+MyUtils.spilt1+w.getCode())//仓库code#合同编码
		    		.append(",").append(BaseStatus.NULLVALUE)
		    		.append(",").append(WMSBillingCategoryMiddleEnum.BILL003_06)//工装管理费
		    		.append(",").append(WMSBillingModelInterface.MONTH)//MONTH
		    		.append(",").append(0)//件数
		    		.append(",").append(0);//一口价,将来可以这里赋值,也可以从LFCS规则表读取
		    	//fdj_供应商编码_yyyyMM_309.txt
		    	JavaTools.createTxt(WmsBillMode.txtPath+"/fdj_"+supplier.getCode()+"_"+
		    		JavaTools.format(task.getCreateTime(), JavaTools.ym)+"_309.txt", row, "utf-8");
		    	task.setStatus(TaskStatus.STAT_FINISH);
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_06, task.getId().toString(), 
						"工装管理费-month无供应商信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到供应商信息");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_06, task.getId().toString(), 
					"工装管理费-month无合同信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到合同信息");
		}
		commonDao.store(task);
	}
	//工装租赁费-month
	public void BILL003_05_month(Long id){
		System.out.println("BILL003_05_month....."+id);
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww!=null && ww.getContact()!=null){
			WMSContact w = commonDao.load(WMSContact.class, ww.getContact().getId());
			if(w.getSupplier()!=null){
				WmsOrganization supplier = commonDao.load(WmsOrganization.class,w.getSupplier().getId());
				WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,w.getWarehouse().getId());
				String g1 = null,//L20002#工装租赁费#201509
						g2 = null;//增值服务费用
				StringBuffer row=new StringBuffer();
				row.append("organization="+WmsBillMode.organization+JavaTools.enter);
		    	row.append("template="+WmsBillMode.template001+JavaTools.enter);
		    	//每月1号计算(上个月的工装租赁费)
				Date yesTerday = JavaTools.beforeOneDate(task.getCreateTime());
				String yyyyMM = JavaTools.format(yesTerday, JavaTools.ym);
		    	g1 = supplier.getCode()+MyUtils.spilt1+WMSBillingCategoryMiddleEnum.BILL003_05+MyUtils.spilt1+yyyyMM;
		    	g2 = WMSBillingCategoryBigEnum.BILL003;
		    	//L20002#工装租赁费#201509,增值服务费,2015-09-01,L20002,锦州光和密封实业有限公司,仓库code#合同编码,-,工装租赁费,MONTH ,0,2000
		    	row.append(g1)
		    		.append(",").append(g2)
		    		.append(",").append(JavaTools.format(yesTerday, JavaTools.y_m_d))//2015-09-01
		    		.append(",").append(supplier.getCode())//L20002
		    		.append(",").append(supplier.getName())//锦州光和密封实业有限公司
		    		.append(",").append(warehouse.getCode()+MyUtils.spilt1+w.getCode())//仓库code#合同编码
		    		.append(",").append(BaseStatus.NULLVALUE)
		    		.append(",").append(WMSBillingCategoryMiddleEnum.BILL003_05)//工装租赁费
		    		.append(",").append(WMSBillingModelInterface.MONTH)//MONTH
		    		.append(",").append(0)//件数
		    		.append(",").append(0);//一口价,将来可以这里赋值,也可以从LFCS规则表读取
		    	//fdj_供应商编码_yyyyMM_308.txt
		    	JavaTools.createTxt(WmsBillMode.txtPath+"/fdj_"+supplier.getCode()+"_"+
		    		JavaTools.format(task.getCreateTime(), JavaTools.ym)+"_308.txt", row, "utf-8");
		    	task.setStatus(TaskStatus.STAT_FINISH);
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_05, task.getId().toString(), 
						"工装租赁费-month无供应商信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到供应商信息");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_05, task.getId().toString(), 
					"工装租赁费-month无合同信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到合同信息");
		}
		commonDao.store(task);
	}
	//退货服务费-month
	public void BILL003_04_month(Long id){
		System.out.println("BILL003_04_month....."+id);
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww!=null && ww.getContact()!=null){
			WMSContact w = commonDao.load(WMSContact.class, ww.getContact().getId());
			if(w.getSupplier()!=null){
				WmsOrganization supplier = commonDao.load(WmsOrganization.class,w.getSupplier().getId());
				WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,w.getWarehouse().getId());
				String g1 = null,//L20002#退货服务费#201509
						g2 = null;//增值服务费用
				//L20002#退货服务费#201509,增值服务费用,2015-09-01,L20002,锦州光和密封实业有限公司,仓库code#合同编码,-,退货服务费,MONTH ,0,2000
				StringBuffer row=new StringBuffer();
				row.append("organization="+WmsBillMode.organization+JavaTools.enter);
		    	row.append("template="+WmsBillMode.template001+JavaTools.enter);
		    	//每月1号计算(上个月的退货服务费)
				Date yesTerday = JavaTools.beforeOneDate(task.getCreateTime());
				String yyyyMM = JavaTools.format(yesTerday, JavaTools.ym);
		    	g1 = supplier.getCode()+MyUtils.spilt1+WMSBillingCategoryMiddleEnum.BILL003_04+MyUtils.spilt1+yyyyMM;
		    	g2 = WMSBillingCategoryBigEnum.BILL003;
		    	row.append(g1)
		    		.append(",").append(g2)
		    		.append(",").append(JavaTools.format(yesTerday, JavaTools.y_m_d))//2015-09-01
		    		.append(",").append(supplier.getCode())//L20002
		    		.append(",").append(supplier.getName())//锦州光和密封实业有限公司
		    		.append(",").append(warehouse.getCode()+MyUtils.spilt1+w.getCode())//仓库code#合同编码
		    		.append(",").append(BaseStatus.NULLVALUE)
		    		.append(",").append(WMSBillingCategoryMiddleEnum.BILL003_04)//退货服务费
		    		.append(",").append(WMSBillingModelInterface.MONTH)//MONTH
		    		.append(",").append(0)//件数
		    		.append(",").append(0);//一口价,将来可以这里赋值,也可以从LFCS规则表读取
		    	//fdj_供应商编码_yyyyMM_307.txt
		    	JavaTools.createTxt(WmsBillMode.txtPath+"/fdj_"+supplier.getCode()+"_"+
		    		JavaTools.format(task.getCreateTime(), JavaTools.ym)+"_307.txt", row, "utf-8");
		    	task.setStatus(TaskStatus.STAT_FINISH);
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_04, task.getId().toString(), 
						"退货服务费-month无供应商信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到供应商信息");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_04, task.getId().toString(), 
					"退货服务费-month无合同信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到合同信息");
		}
		commonDao.store(task);
	}
	//退货服务费-day
	@SuppressWarnings("unchecked")
	public void BILL003_04_day(Long id){
		System.out.println("BILL003_04_day....."+id);
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww==null){
			throw new BusinessException("合同明细ID="+task.getMessageId()+"数据不存在");
		}
		WMSContact w = commonDao.load(WMSContact.class,ww.getContact().getId());
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,w.getWarehouse().getId());
		WmsOrganization supplier = commonDao.load(WmsOrganization.class,w.getSupplier().getId());
		String happenDate = JavaTools.format(task.getCreateTime(),JavaTools.y_m_d);;//2015-09-18
		List<Object[]> pallets = commonDao.findByQuery("select pp.item.code,pp.item.name,sum(pp.shippedQuantityBU)"+
				" from WmsPickTicketDetail pp"+
				" left join pp.pickTicket p "+
				" left join pp.pickTicket.warehouse w"+
				" left join pp.supplier sup"+
				" left join p.billType billType"+
				" where w.id =:warehouseId and sup.code =:supCode"+
				" and billType.code =:billType and p.status =:status"+
				" and to_char(p.finshDate,'yyyy-MM-dd') =:finshDate"+
				" GROUP BY pp.item.code,pp.item.name", 
				new String[]{"warehouseId","supCode","billType","status","finshDate"}, 
				new Object[]{warehouse.getId(),supplier.getCode(),"OUT-002",WmsPickTicketStatus.FINISHED,happenDate});
		if(pallets!=null && pallets.size()>0){
			String g1 = null,//L20002#退货服务费#20150918
					g2 = null;//增值服务费用
			//L20002#退货服务费#20150918,增值服务费用,2015-09-18,L20002,锦州光和密封实业有限公司,仓库code#合同编码,1017014GG01,退货服务费,PACKAGES,20,0
			StringBuffer row=new StringBuffer();
	    	row.append("organization="+WmsBillMode.organization+JavaTools.enter);
	    	row.append("template="+WmsBillMode.template001+JavaTools.enter);
	    	g1 = supplier.getCode()+MyUtils.spilt1+WMSBillingCategoryMiddleEnum.BILL003_04+
	    			MyUtils.spilt1+JavaTools.format(task.getCreateTime(), JavaTools.dmy);
	    	g2 = WMSBillingCategoryBigEnum.BILL003;
	    	int k = 0;
	    	for(Object[] obj : pallets){
	    		row.append(g1)
	    		.append(",").append(g2)
	    		.append(",").append(happenDate)
	    		.append(",").append(supplier.getCode())//L20002
	    		.append(",").append(supplier.getName())//锦州光和密封实业有限公司
	    		.append(",").append(warehouse.getCode()+MyUtils.spilt1+w.getCode())//仓库code#合同编码
	    		.append(",").append(obj[0])//物料编码
	    		.append(",").append(WMSBillingCategoryMiddleEnum.BILL003_04)//退货服务费
	    		.append(",").append(WMSBillingModelInterface.PACKAGES)//PACKAGES
	    		.append(",").append(obj[2])//件数
	    		.append(",").append(0);//一口价
	    		if(k<pallets.size()-1){
	    			row.append(JavaTools.enter);
	    		}
	    		k++;
	    	}
	    	//fdj_供应商编码_yyMMdd_306.txt
	    	JavaTools.createTxt(WmsBillMode.txtPath+"/fdj_"+supplier.getCode()+"_"+
	    			JavaTools.format(task.getCreateTime(), JavaTools.yymd)+"_306.txt", row, "utf-8");
	    	task.setStatus(TaskStatus.STAT_FINISH);
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_03, task.getId().toString(), 
					"[退货服务费-day]无退货信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到退货信息");
		}
		commonDao.store(task);
	}
	//不合格品管理费-day
	@SuppressWarnings("unchecked")
	public void BILL003_03_day(Long id){
		System.out.println("BILL003_03_day....."+id);
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww==null){
			throw new BusinessException("合同明细ID="+task.getMessageId()+"数据不存在");
		}
		WMSContact w = commonDao.load(WMSContact.class,ww.getContact().getId());
		WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,w.getWarehouse().getId());
		WmsOrganization supplier = commonDao.load(WmsOrganization.class,w.getSupplier().getId());
		Date beforeOneDate = JavaTools.beforeOneDate(task.getCreateTime());//当天抓取昨天的日结
		String happenDate = JavaTools.format(beforeOneDate,JavaTools.y_m_d);;//2015-09-18
		List<Object[]> theKnots = commonDao.findByQuery("SELECT w.itemCode,SUM(w.quantity)"
				+ " FROM WmsTheKont w WHERE w.warehouse.id =:warehouseId AND w.supCode =:supCode AND w.type='日结库存'"
				+ " AND to_char(w.orderDate,'yyyy-MM-dd') =:beforeOneDate AND w.inventoryStatus LIKE :inventoryStatus"
				+ " GROUP BY w.itemCode", 
				new String[]{"warehouseId","supCode","beforeOneDate","inventoryStatus"}, 
				new Object[]{warehouse.getId(),supplier.getCode(),happenDate,"%不合格%"});
		if(theKnots!=null && theKnots.size()>0){
			String g1 = null,//L20002#不合格品管理费#20150918
					g2 = null;//增值服务费用
			//L20002#不合格品管理费#20150918,增值服务费用,2015-09-18,L20002,锦州光和密封实业有限公司,仓库code#合同编码,1017014GG01,不合格品管理费,PACKAGES,20,0
			StringBuffer row=new StringBuffer();
	    	row.append("organization="+WmsBillMode.organization+JavaTools.enter);
	    	row.append("template="+WmsBillMode.template001+JavaTools.enter);
	    	g1 = supplier.getCode()+MyUtils.spilt1+WMSBillingCategoryMiddleEnum.BILL003_03+
	    			MyUtils.spilt1+JavaTools.format(beforeOneDate, JavaTools.dmy);
	    	g2 = WMSBillingCategoryBigEnum.BILL003;
	    	int k = 0;
	    	for(Object[] obj : theKnots){
	    		row.append(g1)
	    		.append(",").append(g2)
	    		.append(",").append(happenDate)
	    		.append(",").append(supplier.getCode())//L20002
	    		.append(",").append(supplier.getName())//锦州光和密封实业有限公司
	    		.append(",").append(warehouse.getCode()+MyUtils.spilt1+w.getCode())//仓库code#合同编码
	    		.append(",").append(obj[0])//物料编码
	    		.append(",").append(WMSBillingCategoryMiddleEnum.BILL003_03)//不合格品管理费
	    		.append(",").append(WMSBillingModelInterface.PACKAGES)//PACKAGES
	    		.append(",").append(obj[1])//件数
	    		.append(",").append(0);//一口价
	    		if(k<theKnots.size()-1){
	    			row.append(JavaTools.enter);
	    		}
	    		k++;
	    	}
	    	//fdj_供应商编码_yyMMdd_304.txt
	    	JavaTools.createTxt(WmsBillMode.txtPath+"/fdj_"+supplier.getCode()+"_"+
	    			JavaTools.format(task.getCreateTime(), JavaTools.yymd)+"_304.txt", row, "utf-8");
	    	task.setStatus(TaskStatus.STAT_FINISH);
		}else{
			//如果不合格品管理费-day已经执行5次仍然没有发现前日日结数据,那么将状态置为失败,不再执行.
			//否则不改变状态,即仍然为READY继续执行
			if(task.getRepeatCount()>=5){
				task.setStatus(TaskStatus.STAT_FAIL);
			}
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_03, task.getId().toString(), 
					"不合格品管理费-day无日结信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到前日库存");
		}
		commonDao.store(task);
	}
	//不合格品管理费-month
	public void BILL003_03_month(Long id){
		System.out.println("BILL003_03_month....."+id);
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww!=null && ww.getContact()!=null){
			WMSContact w = commonDao.load(WMSContact.class, ww.getContact().getId());
			if(w.getSupplier()!=null){
				WmsOrganization supplier = commonDao.load(WmsOrganization.class,w.getSupplier().getId());
				WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,w.getWarehouse().getId());
				String g1 = null,//L20002#不合格品管理费#201509
						g2 = null;//增值服务费用
				//L20002#不合格品管理费#201509,增值服务费用,2015-09-18,L20002,锦州光和密封实业有限公司,仓库code#合同编码,-,不合格品管理费,MONTH ,0,2000
				StringBuffer row=new StringBuffer();
				row.append("organization="+WmsBillMode.organization+JavaTools.enter);
		    	row.append("template="+WmsBillMode.template001+JavaTools.enter);
		    	//每月1号计算(上个月的费用)
				Date yesTerday = JavaTools.beforeOneDate(task.getCreateTime());
				String yyyyMM = JavaTools.format(yesTerday, JavaTools.ym);
		    	g1 = supplier.getCode()+MyUtils.spilt1+WMSBillingCategoryMiddleEnum.BILL003_03+MyUtils.spilt1+yyyyMM;
		    	g2 = WMSBillingCategoryBigEnum.BILL003;
		    	row.append(g1)
		    		.append(",").append(g2)
		    		.append(",").append(JavaTools.format(yesTerday, JavaTools.y_m_d))//2015-09-01
		    		.append(",").append(supplier.getCode())//L20002
		    		.append(",").append(supplier.getName())//锦州光和密封实业有限公司
		    		.append(",").append(warehouse.getCode()+MyUtils.spilt1+w.getCode())//仓库code#合同编码
		    		.append(",").append(BaseStatus.NULLVALUE)
		    		.append(",").append(WMSBillingCategoryMiddleEnum.BILL003_03)//不合格品管理费
		    		.append(",").append(WMSBillingModelInterface.MONTH)//MONTH
		    		.append(",").append(0)//件数
		    		.append(",").append(0);//一口价,将来可以这里赋值,也可以从LFCS规则表读取
		    	//fdj_供应商编码_yyyyMM_305.txt
		    	JavaTools.createTxt(WmsBillMode.txtPath+"/fdj_"+supplier.getCode()+"_"+
		    		JavaTools.format(task.getCreateTime(), JavaTools.ym)+"_305.txt", row, "utf-8");
		    	task.setStatus(TaskStatus.STAT_FINISH);
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_03, task.getId().toString(), 
						"不合格品管理费-month无供应商信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到供应商信息");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_03, task.getId().toString(), 
					"不合格品管理费-month无合同信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到合同信息");
		}
		commonDao.store(task);
	}
	//信息服务费-month
	@SuppressWarnings("unchecked")
	public void BILL003_02_month(Long id){
		System.out.println("BILL003_02_month....."+id);
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww!=null && ww.getContact()!=null){
			WMSContact w = commonDao.load(WMSContact.class, ww.getContact().getId());
			if(w.getSupplier()!=null){
				WmsOrganization supplier = commonDao.load(WmsOrganization.class,w.getSupplier().getId());
				WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,w.getWarehouse().getId());
				List<Long> userIds = commonDao.findByQuery("SELECT user.id FROM User user WHERE user.strExtend1 =:strExtend1"
						+ " AND user.status ='ACTIVE'", 
						new String[]{"strExtend1"}, new Object[]{supplier.getCode()});
				if(userIds!=null && userIds.size()>0){
					//每月1号计算(上个月的费用)
					Date yesTerday = JavaTools.beforeOneDate(task.getCreateTime());
					String yyyyMM = JavaTools.format(yesTerday, JavaTools.ym);
					//L20002#信息服务费#201509,增值服务费,2015-09-01,L20002,锦州光和密封实业有限公司,仓库code#合同编码,-,信息服务费,MONTH,2,0
					StringBuffer row=new StringBuffer();
			    	row.append("organization="+WmsBillMode.organization+JavaTools.enter);
			    	row.append("template="+WmsBillMode.template001+JavaTools.enter);
			    	row.append(supplier.getCode()).append(MyUtils.spilt1).append(WMSBillingCategoryMiddleEnum.BILL003_02).append(MyUtils.spilt1)
			    		.append(yyyyMM)
			    		.append(",").append(WMSBillingCategoryBigEnum.BILL003)
			    		.append(",").append(JavaTools.format(yesTerday, JavaTools.y_m_d))
			    		.append(",").append(supplier.getCode())
			    		.append(",").append(supplier.getName())
			    		.append(",").append(warehouse.getCode()+MyUtils.spilt1+w.getCode())//仓库code#合同编码
			    		.append(",").append(BaseStatus.NULLVALUE)
			    		.append(",").append(WMSBillingCategoryMiddleEnum.BILL003_02)
			    		.append(",").append(WMSBillingModelInterface.MONTH)
			    		.append(",").append(userIds.size())
			    		.append(",").append(0);
			    	//fdj_供应商编码_yyyyMM_303.txt
			    	JavaTools.createTxt(WmsBillMode.txtPath+"/fdj_"+supplier.getCode()+"_"+
			    			JavaTools.format(task.getCreateTime(), JavaTools.ym)+"_303.txt", row, "utf-8");
			    	task.setStatus(TaskStatus.STAT_FINISH);
				}else{
					task.setStatus(TaskStatus.STAT_FAIL);
					billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_02, task.getId().toString(), 
							"信息服务费-month无用户信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到用户信息");
				}
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_02, task.getId().toString(), 
						"信息服务费-month无供应商信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到供应商信息");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_02, task.getId().toString(), 
					"信息服务费-month无合同信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到合同信息");
		}
		commonDao.store(task);
	}
	//基本服务费-month
	public void BILL004_01_month(Long id){
		System.out.println("BILL004_01_month....."+id);
		Task task = commonDao.load(Task.class, id);
		WMSContactDetail ww = commonDao.load(WMSContactDetail.class, task.getMessageId());
		if(ww!=null && ww.getContact()!=null){
			WMSContact w = commonDao.load(WMSContact.class, ww.getContact().getId());
			if(w.getSupplier()!=null){
				WmsOrganization supplier = commonDao.load(WmsOrganization.class,w.getSupplier().getId());
				WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class,w.getWarehouse().getId());
				String g1 = null,//L20002#基本服务费#201509
						g2 = null;//基本服务费
				StringBuffer row=new StringBuffer();
				row.append("organization="+WmsBillMode.organization+JavaTools.enter);
		    	row.append("template="+WmsBillMode.template001+JavaTools.enter);
		    	//每月1号计算(上个月的基本服务费)
				Date yesTerday = JavaTools.beforeOneDate(task.getCreateTime());
				String yyyyMM = JavaTools.format(yesTerday, JavaTools.ym);
		    	g1 = supplier.getCode()+MyUtils.spilt1+WMSBillingCategoryMiddleEnum.BILL004_01+MyUtils.spilt1+yyyyMM;
		    	g2 = WMSBillingCategoryBigEnum.BILL004;
		    	//L20002#基本服务费#201509,基本服务费,2015-09-01,L20002,锦州光和密封实业有限公司,仓库code#合同编码,-,基本服务费,MONTH,0,2000
		    	row.append(g1)
		    		.append(",").append(g2)
		    		.append(",").append(JavaTools.format(yesTerday, JavaTools.y_m_d))//2015-09-01
		    		.append(",").append(supplier.getCode())//L20002
		    		.append(",").append(supplier.getName())//锦州光和密封实业有限公司
		    		.append(",").append(warehouse.getCode()+MyUtils.spilt1+w.getCode())//仓库code#合同编码
		    		.append(",").append(BaseStatus.NULLVALUE)
		    		.append(",").append(WMSBillingCategoryMiddleEnum.BILL004_01)//基本服务费
		    		.append(",").append(WMSBillingModelInterface.MONTH)//MONTH
		    		.append(",").append(0)//件数
		    		.append(",").append(0);//一口价,将来可以这里赋值,也可以从LFCS规则表读取
		    	//fdj_供应商编码_yyyyMM_401.txt
		    	JavaTools.createTxt(WmsBillMode.txtPath+"/fdj_"+supplier.getCode()+"_"+
		    		JavaTools.format(task.getCreateTime(), JavaTools.ym)+"_401.txt", row, "utf-8");
		    	task.setStatus(TaskStatus.STAT_FINISH);
			}else{
				task.setStatus(TaskStatus.STAT_FAIL);
				billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL004_01, task.getId().toString(), 
						"基本服务费-month无供应商信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到供应商信息");
			}
		}else{
			task.setStatus(TaskStatus.STAT_FAIL);
			billingManager.initLog(WMSBillingCategoryMiddleEnum.BILL003_05, task.getId().toString(), 
					"基本服务费-month无合同信息", task.getSubscriber(), "合同明细ID="+task.getMessageId()+"查询不到合同信息");
		}
		commonDao.store(task);
	}
	/**********************************************S------P-----I-----L-----T*********************************/
	/**********************************************S------P-----I-----L-----T*********************************/
	/**********************************************S------P-----I-----L-----T*********************************/
	/**判断计费属于什么中类 @WMSBillingCategoryMiddleEnum*/
	private String getChainMethod(Map<String,String> chainMethod,String chains,String smallCategory){
		String methodName = null;
		String[] chain01 = chains.split(MyUtils.spilt1);
		String[] chain02 = chain01[1].split(WMSBillingCategoryRuleChain.and);
		for(String chain : chain02){
			if(chain.equals(smallCategory)){
				methodName = chainMethod.get(chain);
				break;
			}
		}
		return methodName;
	}
	/**获取计费类别的计费模式 @WMSBillingModelInterface*/
	private Object[] getChainMode(String smallCategory,String billingMode,String chainName,int mode){
		String[] chain01 = chainName.split(MyUtils.spilt1);
		String[] chain02 = null;
		String[] chain03 = null;
		int donce = 0;//0-无,1-day,2-month
		for(String chain : chain01){
			chain02 = chain.split(WMSBillingCategoryRuleChain.and);
			if(chain02[0].equals(smallCategory)){
				if(chain02.length>1){
					chain03 = chain02[1].split(WMSBillingCategoryRuleChain.or);
					for(String chain3 : chain03){
						if(chain3.equals(billingMode)){
							donce = mode;
							return new Object[]{
									donce,chain03.length
							};
						}
					}
				}else{
					donce = mode;
					return new Object[]{
							donce,0
					};
				}
			}
		}
		return new Object[]{
				donce,0
		};
	}
}
