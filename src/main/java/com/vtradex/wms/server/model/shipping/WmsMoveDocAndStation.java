package com.vtradex.wms.server.model.shipping;


import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsMoveDocShipStatus;
import com.vtradex.wms.server.model.organization.WmsItem;
/**
 * 拣货明细与器具关系
 * @author Administrator
 *
 */
public class WmsMoveDocAndStation extends Entity {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7463700320399829544L;

	public static final String short_name = "wmsMoveDocAndStation";//规范:对象简称一律用这个替换
	//物料
	private WmsItem item;
	
	//拣货单明细
	private WmsMoveDocDetail moveDocDetail;
	
	//数量
	private Double quantity = 0D;

	//库位
	private String locationCode;
	
	//库位动线号
	private Integer routeNo = 0;
	
	//装载量
	private Integer loadage;
	
	//拣货量
	private Double pickQuantity = 0D;
	
	//器具型号
	private String type;
	//器具名称
	private String typeName;
	
	//是否完成
	private Boolean isFinished = Boolean.FALSE;
	
	//是否散件
	private Boolean isPartPick = Boolean.FALSE;
	
//	//器具编码
//	private String container;
	
	//器具标签
	private String boxTag;
	
	
	/**排序号、当前页数、总页数*/
	private Double seq;
	private Double endseq;//结束序号
	private Integer curPag;
	private Integer totalPage;
	
	//-------------------------------
	/**标签满载量  同一个标签下的器具总订单量*/
	private Double boxTagQty  = 0D;
	//------------------报表打印需要
	private String fromStorage;//来源仓库
	private String toStorage;//目的仓库
	private String dockNo;//收货道口
	/**相同标签下物料的顺序*/
	private Integer sx = 0;//WmsPickTicketAndAppliance.sx
	/**
	 * 是否加入装车单
	 */
	private Boolean isBol = Boolean.FALSE;
	/**
	 * 状态: 未发运，已发运
	 * 
	 * {@link WmsMoveDocShipStatus}
	 */
//	private String shipStatus = WmsMoveDocShipStatus.UNSHIP;
	//时序件拣货器具明细关系表ID
	private Long spsId;
	
	/**发货单号*/
//	private String pickCode;
	
	
	public Boolean getIsBol() {
		return isBol;
	}
	public Long getSpsId() {
		return spsId;
	}
	public void setSpsId(Long spsId) {
		this.spsId = spsId;
	}
//	public String getShipStatus() {
//		return shipStatus;
//	}
//	public void setShipStatus(String shipStatus) {
//		this.shipStatus = shipStatus;
//	}
	public void setIsBol(Boolean isBol) {
		this.isBol = isBol;
	}
	public Integer getSx() {
		return sx;
	}
	public void setSx(Integer sx) {
		this.sx = sx;
	}
	public String getFromStorage() {
		return fromStorage;
	}
	public void setFromStorage(String fromStorage) {
		this.fromStorage = fromStorage;
	}
	public String getToStorage() {
		return toStorage;
	}
	public void setToStorage(String toStorage) {
		this.toStorage = toStorage;
	}
	public String getDockNo() {
		return dockNo;
	}
	public void setDockNo(String dockNo) {
		this.dockNo = dockNo;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Double getEndseq() {
		return endseq;
	}

	public void setEndseq(Double endseq) {
		this.endseq = endseq;
	}

	public Double getBoxTagQty() {
		return boxTagQty;
	}

	public void setBoxTagQty(Double boxTagQty) {
		this.boxTagQty = boxTagQty;
	}

	public Double getSeq() {
		return seq;
	}

	public void setSeq(Double seq) {
		this.seq = seq;
	}
	public Integer getCurPag() {
		return curPag;
	}


	public void setCurPag(Integer curPag) {
		this.curPag = curPag;
	}


	public Integer getTotalPage() {
		return totalPage;
	}


	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}


	public String getBoxTag() {
		return boxTag;
	}


	public void setBoxTag(String boxTag) {
		this.boxTag = boxTag;
	}


//	public String getContainer() {
//		return container;
//	}
//
//
//	public void setContainer(String container) {
//		this.container = container;
//	}


	public Boolean getIsPartPick() {
		return isPartPick;
	}


	public void setIsPartPick(Boolean isPartPick) {
		this.isPartPick = isPartPick;
	}


	public Boolean getIsFinished() {
		return isFinished;
	}


	public void setIsFinished(Boolean isFinished) {
		this.isFinished = isFinished;
	}


	public Double getPickQuantity() {
		return pickQuantity;
	}


	public void setPickQuantity(Double pickQuantity) {
		this.pickQuantity = pickQuantity;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public WmsMoveDocAndStation(){}
	
	
	public WmsMoveDocAndStation(WmsItem item,
			WmsMoveDocDetail moveDocDetail, Double quantity,Integer loadage,String type,String typeName,
			Double seq,Double endseq,Integer curPag,Integer totalPage) {
		super();
		this.item = item;
		this.moveDocDetail = moveDocDetail;
		this.quantity = quantity;
		this.loadage = loadage;
		this.type=type;
		this.typeName = typeName;
		this.seq = seq;
		this.endseq = endseq;
		this.curPag = curPag;
		this.totalPage = totalPage;
	}


	

	public Integer getLoadage() {
		return loadage;
	}


	public void setLoadage(Integer loadage) {
		this.loadage = loadage;
	}


	public WmsMoveDocDetail getMoveDocDetail() {
		return moveDocDetail;
	}


	public void setMoveDocDetail(WmsMoveDocDetail moveDocDetail) {
		this.moveDocDetail = moveDocDetail;
	}


	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public Integer getRouteNo() {
		return routeNo;
	}


	public void setRouteNo(Integer routeNo) {
		this.routeNo = routeNo;
	}

	public WmsItem getItem() {
		return item;
	}

	public void setItem(WmsItem item) {
		this.item = item;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	
	public Double getAvailableQuantityBU(){
		return this.quantity-this.pickQuantity;
	}
	public void cancelPickQuantity(Double quantityBU) {
		this.pickQuantity -= quantityBU;
		if(this.pickQuantity<this.quantity){
			this.isFinished = false;
		}
	}
}
