package com.vtradex.wms.client.packagingtable.data;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 包装台
 *
 * @category 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:49 $
 */
public class PackagingTableLocationDTO implements IsSerializable {
	
	private Long id;
	private String code;
	private String name;
	
	private Long workGroupId;
	
	public PackagingTableLocationDTO() {
	}
	
	public PackagingTableLocationDTO(Long id, String code, String name) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
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

	public Long getWorkGroupId() {
		return workGroupId;
	}

	public void setWorkGroupId(Long workGroupId) {
		this.workGroupId = workGroupId;
	}
}
