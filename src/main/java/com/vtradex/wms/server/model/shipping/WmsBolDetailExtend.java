package com.vtradex.wms.server.model.shipping;

import com.vtradex.thorn.server.model.Entity;
/**发运明细扩展字段表*/
public class WmsBolDetailExtend extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WmsBOLDetail bolDetail;
	/** MES单号 */
	private String mesNo;//WmsPickTicket.relatedBill1
	/**工作中心*/
	private String odrSu;//WmsPickTicket.odrSu
	/**源仓库*/
	private String fware;//WmsMoveDocAndStation.fromStorage
	/**目的仓库*/
	private String dware;//WmsMoveDocAndStation.toStorage
	/**收货道口*/
	private String shdk;//WmsMoveDocAndStation.dockNo
	/**来源*/
	private String fromSource;//WmsPickTicketDetail.fromSource
	/**批次信息*/
	private String batch;//WmsPickTicket.batch
	/**工位*/
	private String station;//WmsPickTicketDetail.station
	/**SPS*/
	private Boolean isJp;//WmsPickTicketDetail Y--SPS   N--否
	/**器具种类*/
	private String baxType;//WmsMoveDocAndStation.type
	/**器具标签*/
	private String baxTag;//WmsMoveDocAndStation.boxTag
	/**器具数量*/
	private Double packageQty = 0D;//WmsPickTicketDetail
	/**物料的顺序*/
	private Integer itemSx = 0;//WmsPickTicketDetail.sx
	
	
	/**相同标签下物料的顺序*/
	private Integer sx = 0;//WmsPickTicketAndAppliance->WmsMoveDocAndStation.sx
	//装载量
	private Integer loadage = 0;//WmsMoveDocAndStation.loadage
	//器具编码
	private String container;//WmsMoveDocAndStation.container
	
	private Double seq;//开始序号 WmsMoveDocAndStation.seq
	private Double endseq;//结束序号 WmsMoveDocAndStation.endseq
	//拣货数量
	private Double pickQuantityBu = 0D;//WmsMoveDocAndStation.pickQuantityBu
	
	public WmsBolDetailExtend(WmsBOLDetail bolDetail, String mesNo,
			String odrSu, String fware, String dware, String shdk,
			String fromSource, String batch, String station, Boolean isJp,
			String baxType, String baxTag, Double packageQty, Integer itemSx,
			Integer loadage,String container,Integer sx,Double seq,Double endseq,Double pickQuantityBu) {
		super();
		this.bolDetail = bolDetail;
		this.mesNo = mesNo;
		this.odrSu = odrSu;
		this.fware = fware;
		this.dware = dware;
		this.shdk = shdk;
		this.fromSource = fromSource;
		this.batch = batch;
		this.station = station;
		this.isJp = isJp;
		this.baxType = baxType;
		this.baxTag = baxTag;
		this.packageQty = packageQty;
		this.itemSx = itemSx;
		this.loadage = loadage;
		this.container = container;
		this.sx = sx;
		this.seq = seq;
		this.endseq = endseq;
		this.pickQuantityBu = pickQuantityBu;
	}
	public WmsBolDetailExtend() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Double getPickQuantityBu() {
		return pickQuantityBu;
	}
	public void setPickQuantityBu(Double pickQuantityBu) {
		this.pickQuantityBu = pickQuantityBu;
	}
	public Double getSeq() {
		return seq;
	}
	public void setSeq(Double seq) {
		this.seq = seq;
	}
	public Double getEndseq() {
		return endseq;
	}
	public void setEndseq(Double endseq) {
		this.endseq = endseq;
	}
	public Integer getSx() {
		return sx;
	}
	public void setSx(Integer sx) {
		this.sx = sx;
	}
	public Integer getLoadage() {
		return loadage;
	}
	public void setLoadage(Integer loadage) {
		this.loadage = loadage;
	}
	public String getContainer() {
		return container;
	}
	public void setContainer(String container) {
		this.container = container;
	}
	public WmsBOLDetail getBolDetail() {
		return bolDetail;
	}
	public void setBolDetail(WmsBOLDetail bolDetail) {
		this.bolDetail = bolDetail;
	}
	public String getMesNo() {
		return mesNo;
	}
	public void setMesNo(String mesNo) {
		this.mesNo = mesNo;
	}
	public String getOdrSu() {
		return odrSu;
	}
	public void setOdrSu(String odrSu) {
		this.odrSu = odrSu;
	}
	public String getFware() {
		return fware;
	}
	public void setFware(String fware) {
		this.fware = fware;
	}
	public String getDware() {
		return dware;
	}
	public void setDware(String dware) {
		this.dware = dware;
	}
	public String getShdk() {
		return shdk;
	}
	public void setShdk(String shdk) {
		this.shdk = shdk;
	}
	public String getFromSource() {
		return fromSource;
	}
	public void setFromSource(String fromSource) {
		this.fromSource = fromSource;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public Boolean getIsJp() {
		return isJp;
	}
	public void setIsJp(Boolean isJp) {
		this.isJp = isJp;
	}
	public String getBaxType() {
		return baxType;
	}
	public void setBaxType(String baxType) {
		this.baxType = baxType;
	}
	public String getBaxTag() {
		return baxTag;
	}
	public void setBaxTag(String baxTag) {
		this.baxTag = baxTag;
	}
	public Double getPackageQty() {
		return packageQty;
	}
	public void setPackageQty(Double packageQty) {
		this.packageQty = packageQty;
	}
	public Integer getItemSx() {
		return itemSx;
	}
	public void setItemSx(Integer itemSx) {
		this.itemSx = itemSx;
	}
}
