package com.vtradex.wms.server.telnet.dto;

public class WmsCountTaskDTO {

	private Long countPlanId;
	private String countPlanCode;
	private Long countPlanDetailId;
	private Long countLocationId;
	private String countLocationCode;
	
	public WmsCountTaskDTO(Long countPlanId, String countPlanCode,
			Long countPlanDetailId, Long countLocationId,
			String countLocationCode) {
		this.countPlanId = countPlanId;
		this.countPlanCode = countPlanCode;
		this.countPlanDetailId = countPlanDetailId;
		this.countLocationId = countLocationId;
		this.countLocationCode = countLocationCode;
	}

	public Long getCountPlanId() {
		return countPlanId;
	}

	public void setCountPlanId(Long countPlanId) {
		this.countPlanId = countPlanId;
	}

	public String getCountPlanCode() {
		return countPlanCode;
	}

	public void setCountPlanCode(String countPlanCode) {
		this.countPlanCode = countPlanCode;
	}

	public Long getCountPlanDetailId() {
		return countPlanDetailId;
	}

	public void setCountPlanDetailId(Long countPlanDetailId) {
		this.countPlanDetailId = countPlanDetailId;
	}

	public Long getCountLocationId() {
		return countLocationId;
	}

	public void setCountLocationId(Long countLocationId) {
		this.countLocationId = countLocationId;
	}

	public String getCountLocationCode() {
		return countLocationCode;
	}

	public void setCountLocationCode(String countLocationCode) {
		this.countLocationCode = countLocationCode;
	}
}
