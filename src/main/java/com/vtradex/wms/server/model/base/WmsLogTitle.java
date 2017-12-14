package com.vtradex.wms.server.model.base;

public interface WmsLogTitle {
	/** 器具更换 */
	static final String CONTAINER_CHANGE = "CONTAINER_CHANGE";
	/** 器具释放 */
	static final String CONTAINER_RELEASE = "CONTAINER_RELEASE";
	/** 器具拣货 */
	static final String CONTAINER_PICKING = "CONTAINER_PICKING";
	/** 库位更换 */
	static final String LOCATION_CHANGE = "LOCATION_CHANGE";
	/** 容器退拣 */
	static final String CONTAINER_PIC_BACK = "CONTAINER_PIC_BACK";
	/////增加备料环节节点名
	/** 打印备料单 */
	static final String PICK_PRINT = "PICK_PRINT";
	/** 打印器具单 */
	static final String CONTAINER_PRINT = "CONTAINER_PRINT";
	/** 加入装车单 */
	static final String CONTAINER_BOL = "CONTAINER_BOL";
	/** 发运 */
	static final String CONTAINER_SHIP = "CONTAINER_SHIP";
	/** 器具签收 */
	static final String CONTAINER_SIGN = "CONTAINER_SIGN";
	/** 回单扫描 */
	static final String CONTAINER_SCAN = "CONTAINER_SCAN";
}
