package com.vtradex.wms.server.model.middle;

import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.base.ItmsLogType;
/**Job日志 yc*/
public class WmsJobLog extends VersionalEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 日志类型 */
	private String type = ItmsLogType.ERROR;
	/** 内容标题 */
	private String operName;
	/** 实际内容 */
	private String operException;
	/** 内容国际化 */
	private String operExceptionMess;
	/** 扩展字段 **/
	private String strExtend1;
	private String strExtend2;
	private String strExtend3;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOperException() {
		return operException;
	}

	public void setOperException(String operException) {
		this.operException = operException;
	}

	public String getOperExceptionMess() {
		return operExceptionMess;
	}

	public void setOperExceptionMess(String operExceptionMess) {
		this.operExceptionMess = operExceptionMess;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public String getStrExtend1() {
		return strExtend1;
	}

	public void setStrExtend1(String strExtend1) {
		this.strExtend1 = strExtend1;
	}

	public String getStrExtend2() {
		return strExtend2;
	}

	public void setStrExtend2(String strExtend2) {
		this.strExtend2 = strExtend2;
	}

	public String getStrExtend3() {
		return strExtend3;
	}

	public void setStrExtend3(String strExtend3) {
		this.strExtend3 = strExtend3;
	}

	public WmsJobLog(String type,String operName,String operException,String operExceptionMess) {
		this.type = type;
		this.operName = operName;
		this.operException = operException;
		this.operExceptionMess = operExceptionMess;
	}
	public WmsJobLog(String type,String operName,String strExtend1,String strExtend2,String strExtend3){
		this.type = type;
		this.operName = operName;
		this.strExtend1 = strExtend1;
		this.strExtend2 = strExtend2;
		this.strExtend3 = strExtend3;
	}
	public WmsJobLog(){
		// 供Hibernate使用，开发者不允许调用
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
