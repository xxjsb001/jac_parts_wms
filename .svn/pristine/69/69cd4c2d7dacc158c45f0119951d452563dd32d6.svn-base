package com.vtradex.wms.server.model.inventory;

import java.util.Date;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
/***
 * 日结
 * @author myc
 *
 */
public class WmsTheKont extends VersionalEntity{
	/**
	 * 
	 */
	public static final String back = "还原";
	
	public static final String type_1 = "收货";
	public static final String type_2 = "日结库存";//当日算作期末库存,次日算作期初库存
	public static final String type_3 = "次日发运";
	public static final String type_4 = "在途库存";//当日算作期末在途,次日算作期初在途
	public static final String type_5 = "发货";
	public static final String type_6 = "期初在途";
	public static final String type_7 = "库存调整(增)";
	public static final String type_8 = "库存调整(减)";
	public static final String type_9 = "库内质检(增)";
	public static final String type_10 = "库内质检(减)";
	
	private static final long serialVersionUID = 1L;
	/** 所属仓库 */
	@UniqueKey
	private WmsWarehouse warehouse;
	/**供应商编码 */
	private String supCode;
	/**供应商名称 */
	private String supName;
	/**类型 */
	private String type;
	/**日期 */
	private Date storageDate;
	/**物料编码 */
	private String itemCode;
	/**物料名称 */
	private String itemName;
	/**数量 */
	private Double quantity = 0D;
	/**库存状态 */
	private String inventoryStatus;
	/**工艺状态 */
	private String extendPropc1;
	/**备注 */
	private String description;
	/**日结日期 */
	private Date orderDate;
	/**其他备注 */
	private String note;
	/**期初库存 */
	private Double beforeQuantity = 0D;
	/**期初在途 */
	private Double beforeOnWayQuantity = 0D;
	/**和历史日结数据匹配*/
	private String hashCode;
	/**其他备注1-期初库存/期初在途 还原 */
	private String note1;
	/**其他备注2 */
	private String note2;
	/**其他备注3 */
	private String note3;
	/**其他备注4 */
	private String note4;
	/**其他备注5 */
	private String note5;
	/**其他备注6 */
	private String note6;
	
	public WmsTheKont() {
		super();
	}
	public void initHashcode(){
		this.hashCode = BeanUtils.getFormat(this.getWarehouse().getId(),this.getSupCode(),this.getItemCode()
				,this.getInventoryStatus(),this.getExtendPropc1(),this.getType());
	}
	public WmsWarehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(WmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}
	public String getSupCode() {
		return supCode;
	}
	public void setSupCode(String supCode) {
		this.supCode = supCode;
	}
	public String getSupName() {
		return supName;
	}
	public void setSupName(String supName) {
		this.supName = supName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getStorageDate() {
		return storageDate;
	}
	public void setStorageDate(Date storageDate) {
		this.storageDate = storageDate;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public String getInventoryStatus() {
		return inventoryStatus;
	}
	public void setInventoryStatus(String inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}
	public String getExtendPropc1() {
		return extendPropc1;
	}
	public void setExtendPropc1(String extendPropc1) {
		this.extendPropc1 = extendPropc1;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Double getBeforeQuantity() {
		return beforeQuantity;
	}
	public void setBeforeQuantity(Double beforeQuantity) {
		this.beforeQuantity = beforeQuantity;
	}
	public String getHashCode() {
		return hashCode;
	}
	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}
	public Double getBeforeOnWayQuantity() {
		return beforeOnWayQuantity;
	}
	public void setBeforeOnWayQuantity(Double beforeOnWayQuantity) {
		this.beforeOnWayQuantity = beforeOnWayQuantity;
	}
	public String getNote1() {
		return note1;
	}
	public void setNote1(String note1) {
		this.note1 = note1;
	}
	public String getNote2() {
		return note2;
	}
	public void setNote2(String note2) {
		this.note2 = note2;
	}
	public String getNote3() {
		return note3;
	}
	public void setNote3(String note3) {
		this.note3 = note3;
	}
	public String getNote4() {
		return note4;
	}
	public void setNote4(String note4) {
		this.note4 = note4;
	}
	public String getNote5() {
		return note5;
	}
	public void setNote5(String note5) {
		this.note5 = note5;
	}
	public String getNote6() {
		return note6;
	}
	public void setNote6(String note6) {
		this.note6 = note6;
	}
}
