package com.vtradex.wms.server.telnet.shell.pick;

public interface WmsScanPickShellMessage {
	public static final String ERROR_MESG = "ERROR_MESG";
	public static final String PICK_CODE = "PICK_CODE";
	public static final String CONTAINER = "CONTAINER";
	public static final String LOC_CODE = "LOC_CODE";
	public static final String ITEM_CODE = "ITEM_CODE";
	
	public static String ERROR_ITEM_NULL = "失败!物料明细不存在";
	public static String ERROR_ITEM_FINISHED = "失败!物料拣货完成";
	public static String ERROR_CONTAINER_NULL = "失败!容器不存在或失效";
	public static String ERROR_LOSS = "失败!系统库存不足";
	public static String ERROR_LOC_NULL = "失败!库位号不存在";
	public static String ERROR_SHIP_LOC_NULL = "失败!备货库位不存在";
	public static String ERROR_PIC_MOVE = "失败!拣货量超出明细可用量";
	public static String ERROR_PIC_MOVE_INV = "失败!拣货量超出库存可用量";
	public static String ERROR_TASK_NULL = "失败!任务不存在";
}
