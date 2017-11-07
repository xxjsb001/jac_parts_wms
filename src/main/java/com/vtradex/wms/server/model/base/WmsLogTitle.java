package com.vtradex.wms.server.model.base;

public interface WmsLogTitle {
	/** 器具更换 */
	static final String CONTAINER_CHANGE = "CONTAINER_CHANGE";
	/** 器具释放 */
	static final String CONTAINER_RELEASE = "CONTAINER_RELEASE";
	/** 器具拣货 */
	static final String CONTAINER_PICKING = "CONTAINER_PICKING";
	/** 加入装车单 */
	static final String CONTAINER_BOL = "CONTAINER_BOL";
	/** 库位更换 */
	static final String LOCATION_CHANGE = "LOCATION_CHANGE";
	/** 容器退拣 */
	static final String CONTAINER_PIC_BACK = "CONTAINER_PIC_BACK";
}
