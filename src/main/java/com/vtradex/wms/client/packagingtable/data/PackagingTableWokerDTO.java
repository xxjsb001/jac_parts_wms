package com.vtradex.wms.client.packagingtable.data;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 工作人员
 *
 * @category 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:49 $
 */
public class PackagingTableWokerDTO implements IsSerializable {
	
	private Long id;
	private String code;
	private String name;
	private String job;
	
	public PackagingTableWokerDTO() {
	}
	
	public PackagingTableWokerDTO(Long id, String code, String name, String job) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.job = job;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}
}
