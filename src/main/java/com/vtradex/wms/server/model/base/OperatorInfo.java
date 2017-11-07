package com.vtradex.wms.server.model.base;

import java.util.Date;

/**
 * 用户操作信息
 *
 * @category Component
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2015/03/25 02:48:50 $
 */
public class OperatorInfo {
	
	private Date operateTime;
	
	private Long operator;
	
	private String operatorName;
	
	public OperatorInfo() {
	}

	public OperatorInfo(Date operateTime, Long operator, String operatorName) {
		super();
		this.operateTime = operateTime;
		this.operator = operator;
		this.operatorName = operatorName;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public Long getOperator() {
		return operator;
	}

	public void setOperator(Long operator) {
		this.operator = operator;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
}
