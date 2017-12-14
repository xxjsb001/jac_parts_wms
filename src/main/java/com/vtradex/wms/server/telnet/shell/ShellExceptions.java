package com.vtradex.wms.server.telnet.shell;

public interface ShellExceptions {
	public static String MESSAGE = "";
	
	public static String CONTAINER_NULL_FINISH = "错误:器具型号已完成或不存在";
	public static String CONTAINER_IS_USING = "错误:器具已有未发运拣货完成信息,请更换器具";
	public static String CONTAINER_TYPE_NOT_EXITS = "错误:箱型未维护信息或器具型号不对";
	public static String CONTAINER_CODE_IS_NULL = "警告:器具编码不能为空";
	public static String CONTAINER_NOT_PICKING = "器具没有拣货或已加入未发运";
	public static String CONTAINER_IN_BOL = "器具已经加入装车单";
	public static String CONTAINER_FULL = "器具已满";
	public static String CONTAINER_NOT_FULL = "器具未满";
	public static String CONTAINER_NEXT = "请扫下一个容器";
	public static String CONTAINER_UNSHIP_MOVE = "器具存在未发运的数据(找到多条)";
	public static String CONTAINER_TAG_ERROR = "错误:一个拣货任务下相同容器不能多个标签";
	public static String CONTAINER_TAG_ONE = "错误:同一容器,只可存在一个未发运标签";
	public static String CONTAINER_TAG_NONE = "错误:拣货单下无明细与物料器具关系";
	public static String CONTAINER_PART_DIF_PICK = "错误:散件容器不可装不同类型发货单料";
	
	public static String PICK_QTY_IS_ERROE = "警告:拣选数量不能为空或小于0";
	public static String PICK_QTY_IS_OVER = "警告:拣选数量不能大于待拣数量";
	public static String PICK_LOC_IS_NULL = "警告:拣选库位不能为空";
	public static String PICK_TASK_NULL = "该拣货单没有未完成明细";
	public static String PICK_QTY_NOT_DOUBLE = "警告:拣选数量不是有效数字";
	public static String PICK_QTY_NOT_FULL = "警告:任务待拣量不足";
	
	public static String PICK_BACK_QTY_IS_OVER = "警告:退拣数量不能大于已拣数量";
	public static String PICK_BACK_QTY_IS_ERROE = "警告:退拣数量不能为空或小于0";
	
	public static String NEXT_ITEM = "请拣下一个物料";
	public static String NEXT_VEHICLE = "请扫描下一辆车";
	public static String VEHICLE_NULL = "车辆信息未维护";
	
	public static String TASKS_NULL = "警告:该器具下无任务";
	
	public static String PICK_BACK_IS_NULL = "错误:无拣货任务";
	
}
