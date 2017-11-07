package com.vtradex.wms.server.model.interfaces;

import java.util.Date;

import com.vtradex.thorn.server.model.VersionalEntity;

public class WHead extends VersionalEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2630686847790418471L;

	private Integer status;//0=失败，1=未处理，3=成功
	
	private Date createTime;
	
	//HeadType
	private String type;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public WHead(Integer status, Date createTime, String type) {
		super();
		this.status = status;
		this.createTime = createTime;
		this.type = type;
	}
	
	public WHead(){}
}
