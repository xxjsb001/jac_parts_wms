package com.vtradex.wms.server.model.move;

import java.util.HashSet;
import java.util.Set;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWorkArea;
import com.vtradex.wms.server.model.warehouse.WmsWorker;

/**
 * 作业单, 任务的集合
 *
 * @category Entity 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:51 $
 */
public class WmsWorkDoc extends VersionalEntity{
	
	private static final long serialVersionUID = -727092088956302135L;
	
	/** 作业单号*/
	@UniqueKey
	private String code;
	/** 所属仓库*/
	@UniqueKey
	private WmsWarehouse warehouse;
	
	/** 客户*/
	private WmsOrganization company;
	
	/** 作业区 */
	private WmsWorkArea workArea;
	
	/** 责任人*/
	private WmsWorker worker;
	
	/**
	 * 作业任务类型
	 * 
	 * {@link WmsTaskType}
	 */
	private String type;
	
	/**
	 * 状态：打开、作业中、关闭
	 * 
	 * {@link WmsWorkDocStatus}
	 */
	private String status = WmsWorkDocStatus.OPEN;
	
	/** 关联单据号*/
	private String originalBillCode;
	
	/** 计划作业数量BU*/
	private Double expectedQuantityBU = 0.0D;
	/** 实际作业数量BU*/
	private Double movedQuantityBU = 0.0D;
	
	/** 打印次数 */
	private Integer printTimes=0;
	
	/** 包含任务清单*/
	private Set<WmsTask> tasks = new HashSet<WmsTask>();
	
	public WmsWorkDoc() {
	}
	
	public WmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public WmsOrganization getCompany() {
		return company;
	}

	public void setCompany(WmsOrganization company) {
		this.company = company;
	}

	public WmsWorkArea getWorkArea() {
		return workArea;
	}

	public void setWorkArea(WmsWorkArea workArea) {
		this.workArea = workArea;
	}

	public WmsWorker getWorker() {
		return worker;
	}

	public void setWorker(WmsWorker worker) {
		this.worker = worker;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOriginalBillCode() {
		return originalBillCode;
	}

	public void setOriginalBillCode(String originalBillCode) {
		this.originalBillCode = originalBillCode;
	}

	public Double getExpectedQuantityBU() {
		return expectedQuantityBU;
	}

	public void setExpectedQuantityBU(Double expectedQuantityBU) {
		this.expectedQuantityBU = expectedQuantityBU;
	}

	public Double getMovedQuantityBU() {
		return movedQuantityBU;
	}

	public void setMovedQuantityBU(Double movedQuantityBU) {
		this.movedQuantityBU = movedQuantityBU;
	}

	public Integer getPrintTimes() {
		return printTimes;
	}

	public void setPrintTimes(Integer printTimes) {
		this.printTimes = printTimes;
	}

	public Set<WmsTask> getTasks() {
		return tasks;
	}

	public void setTasks(Set<WmsTask> tasks) {
		this.tasks = tasks;
	}
	
	

	/**
	 * 添加任务
	 * @param task
	 */
	public void addTask(WmsTask task){
		task.setWorkDoc(this);
		
		this.expectedQuantityBU += task.getPlanQuantityBU();
		
		this.tasks.add(task);
	}
	
	public boolean isFinished() {
		if (this.expectedQuantityBU.doubleValue()==this.movedQuantityBU.doubleValue()) {
			return true;
		}
		return false;
	}

	public void addQuantityBU(Double quantity) {
		this.expectedQuantityBU += quantity;
	}

	/**
	 * 扣减计划作业数量
	 * @param quantity
	 */
	public void removeQuantityBU(Double quantity) {
		this.expectedQuantityBU -= quantity;
	}

	/** 
	 * 增加移位数量BU
	 * */
	public void addMovedQuantityBU(Double quantity) {
		this.movedQuantityBU += quantity;
	}

	/**
	 * 更新作业单的计划数量
	 */
	public void refreshQuantity() {
		this.expectedQuantityBU = 0D;
		for (WmsTask task : this.getTasks()) {
			this.expectedQuantityBU += task.getPlanQuantityBU();
		}
	}
	/**
	 * 退拣数量BU
	 */
	public void pickBackMoveQuantityBU(Double quantity){
		this.expectedQuantityBU -= quantity;
		this.movedQuantityBU -= quantity;
	}
	
	/** 移除Task */
	public void removeTask(WmsTask task) {
		this.tasks.remove(task);
		task.setWorkDoc(null);
		refreshQuantity();
	}
}