package com.vtradex.wms.client.ui.javabean;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PT_INFO implements IsSerializable {
	private Long id;
	/** 发货单号 */
	private String code;
	/** 预分配数量 */
	private Double expectedQuantityBU;
	/** 已分配数量 */
	private Double allocatedQuantityBU;
	/** 拣货数量 */
	private Double pickedQuantityBU;

	public PT_INFO() {
	}

	public PT_INFO(Long id, Object code, Object expectedQuantityBU, Object allocatedQuantityBU, 
			Object pickedQuantityBU) {
		this.id = (Long) id;
		this.code = (String) code;
		this.expectedQuantityBU = (Double) expectedQuantityBU;
		this.allocatedQuantityBU = (Double) allocatedQuantityBU;
		this.pickedQuantityBU = (Double) pickedQuantityBU;
	}

	public PT_INFO(Object code, Object expectedQuantityBU, Object allocatedQuantityBU, Object pickedQuantityBU) {
		this(null, code, expectedQuantityBU, allocatedQuantityBU, pickedQuantityBU);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getExpectedQuantityBU() {
		return expectedQuantityBU;
	}

	public void setExpectedQuantityBU(Double expectedQuantityBU) {
		this.expectedQuantityBU = expectedQuantityBU;
	}

	public double getAllocatedQuantityBU() {
		return allocatedQuantityBU;
	}

	public void setAllocatedQuantityBU(Double allocatedQuantityBU) {
		this.allocatedQuantityBU = allocatedQuantityBU;
	}

	public double getPickedQuantityBU() {
		return pickedQuantityBU;
	}

	public void setPickedQuantityBU(Double pickedQuantityBU) {
		this.pickedQuantityBU = pickedQuantityBU;
	}
}