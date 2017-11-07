package com.vtradex.wms.server.model.move;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsWorker;
/**
 * 记录补货移位的库位
 * @author Administrator
 *
 */
public class WmsMoveDocLocation extends Entity{

	private static final long serialVersionUID = -5102019079566554364L;
	/**
	 * 移位单
	 */
	private WmsMoveDoc moveDoc;
	/**
	 * 库位
	 */
	private WmsLocation location;
	/**
	 * 是否结束
	 */
	private Boolean isEnd = Boolean.FALSE;
	
	/**
	 * 作业人员
	 * @return
	 */
	private WmsWorker worker;
	
	public WmsWorker getWorker() {
		return worker;
	}
	public void setWorker(WmsWorker worker) {
		this.worker = worker;
	}
	public WmsMoveDoc getMoveDoc() {
		return moveDoc;
	}
	public void setMoveDoc(WmsMoveDoc moveDoc) {
		this.moveDoc = moveDoc;
	}
	public WmsLocation getLocation() {
		return location;
	}
	public void setLocation(WmsLocation location) {
		this.location = location;
	}
	public Boolean getIsEnd() {
		return isEnd;
	}
	public void setIsEnd(Boolean isEnd) {
		this.isEnd = isEnd;
	}

}
