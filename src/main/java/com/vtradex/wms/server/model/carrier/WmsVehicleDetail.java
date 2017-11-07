package com.vtradex.wms.server.model.carrier;

import com.vtradex.thorn.server.model.Entity;

public class WmsVehicleDetail extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6314641831000831949L;
	/**
     * 运输工具信息
     */
    private WmsVehicle vehicle;
    
    /**
     * 附件名称 类型为图片
     */
    private String name;
    
    /**
     * 附件路径 类型为图片
     */
    private String filePath;
    
    /**图片路径生成的url 查看的时候用*/
    private String fileUrl;

    public WmsVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(WmsVehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
