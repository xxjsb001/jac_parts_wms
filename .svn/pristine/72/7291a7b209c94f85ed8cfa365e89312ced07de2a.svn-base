package com.vtradex.wms.server.service.receiving;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.client.ui.table.RowData;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.receiving.JacPalletSerial;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.model.receiving.WmsASNDetail;

public interface WmsASNManager extends BaseManager {
	
	
	boolean isExistUnQuality(WmsASN asn);
	/**
	 * 判断ASN是否需要质检
	 */
	@Transactional
	boolean asnQuality(WmsASN asn);
	/** 
	 * 保存asn时,编码由规则产生
	 * */
	void storeASN(WmsASN asn);
	
	/**
	 * 新增ASN明细
	 * @param asnId
	 * @param detail
	 * @param expectedQuantity
	 */
	@Transactional
	void addDetail(Long id, WmsASNDetail detail, double expectedQuantity);
	
	/**
	 * 移除ASN明细
	 * @param details
	 */
	@Transactional
	void removeDetails(WmsASNDetail detail);
	
	/**
	 * 整单收货
	 * @param wmsASN
	 */
	@Transactional
	void receiveAll(Long asnId,Long workerId);
	/**
	 * 批量收货
	 */
	@Transactional
	void multiReceive(WmsASNDetail asnDetail);
	
	/**
	 * 单一明细收货 web
	 * @param detail
	 * @param quantity
	 * @param receiveLocId
	 */
	@Transactional
	void detailReceive(WmsASNDetail detail, Long packageUnitId,double quantity, Long receiveLocId,String itemStateId,Long workerId);
	
	/**
	 * 单一明细收货telnet
	 */
	@Transactional
	void detailReceive(WmsASNDetail detail, Long packageUnitId,double quantity, Long receiveLocId,String itemStateId,Long workerId,String inventoryStatus);
	/** 整单取消收货	 */
	@Transactional
	void cancelAllReceive(WmsASN wmsASN);
	
	/** 单一取消收货	 */
	@Transactional
	void singleCanelReceive(WmsASNDetail asnDetail,List<String> cancelReceiveQtyList);
	
	/**
	 * 自动码托
	 * @param wmsASN
	 */
	@Transactional
	void autoPallet(WmsASN wmsASN);
	
	/**
	 * ASN过账: 更新收货记录
	 * @param wmsASN
	 */
	@Transactional
	void receiveConfirm(WmsASN wmsASN);
	
	//--------------------------------------------------
	/**
	 * ASN单一明细收货获取明细货品信息
	 * @param map
	 * @return
	 */
	@Transactional
	RowData getItemByASNDetail(Map map);
	
	/**
	 * ASN单一明细收货获取明细包装信息
	 * @param map
	 * @return
	 */
	@Transactional
	RowData getPackageUnitByASNDetail(Map map);
	
	/**
	 * ASN单一明细收货获取明细批次属性信息
	 * @param map
	 * @return
	 */
	@Transactional
	Map getLotInfoByASNDetail(Map map);
	
	/**
	 * 获取收货库位
	 * @param map
	 */
	@Transactional
	RowData getReceiveLocationObj(Map map);
	
	/**
	 * 获取明细最大行号
	 * @param param
	 * @return
	 */
	String getMaxLineNoByASNDetail(Map param);
	
	@Transactional
	void autoCreateMoveDoc(WmsASN wmsAsn);
	
	/** 获取收货库位	 */
	Double getUnReceivedQuantity(Map map);
	
	/** 创建上架单	 */
	@Transactional
	WmsMoveDoc manualCreateMoveDoc(WmsASN wmsAsn);
	/**整单上架分配 yc.min 20150407*/
	@Transactional
	void putawayAutoAllocate(WmsASN wmsAsn);
	/**手工上架分配 yc.min 201504010*/
	@Transactional
	void putawayAutoByHand(JacPalletSerial jps,Long asnId,Long locationId,Long workerId);
	/**取消上架分配 yc.min 20150409*/
	@Transactional
	void unPutawayAutoAllocate(WmsASN wmsAsn);
	/**单一取消上架分配 yc.min 20150410*/
	@Transactional
	void unPutawayAutoSingle(WmsASN wmsAsn,WmsTask task);
	/**整单上架确认 yc.min 20150407*/
	@Transactional
	void shelvesConfirm(Long asnId,Long workerId);
	/**单一上架确认 yc.min 201504010*/
	@Transactional
	void singleConfirm(WmsTask task,Long id,Long locationId,Long workerId);
	
	/** 完成收货 */
	@Transactional
	void finishReceive(WmsASN asn);
	
	/** 关闭 */
	@Transactional
	void close(WmsASN asn);
	
	/**打印托盘标签 */
	@Transactional
	public Map trayTagPrint(WmsASN wmsAsn,Long printNumber);
	/**打印ASN */
	public Map printASNReport(WmsASN asn );
	
	/**
	 * 质检确认
	 */
	@Transactional
	void qualityConfirm(WmsASN wmsAsn);
	
	/**
	 * 质检登记
	 */
	@Transactional
	void qualitySave(WmsInventoryExtend wsn,String status,String packageUnit,Double num);
	
	
	/**
	 * 质损
	 */
	@Transactional
	void qualityDamage(WmsASN wmsAsn, WmsInventoryExtend wsn, List<String> adjustQuantityBUList);
	
	/**
	 * 单一质检不通过
	 */
	@Transactional
	void qualitySingleFail(WmsASN wmsAsn, WmsInventoryExtend wsn, List<String> adjustQuantityBUList);
	
	/**
	 * 质检不通过
	 */
	@Transactional
	void qualityAllFail(WmsASN wmsAsn);
	
	/**
	 * 质检通过
	 */
	@Transactional
	void qualitySuccess(WmsASN wmsAsn);
	
	/**
	 * 激活
	 */
	@Transactional
	void active(WmsASN wmsAsn);
	
	
	/**
	 * 反激活
	 */
	@Transactional
	void unActive(WmsASN wmsAsn);
	/**订单确认 yc.min 20150402*/
	@Transactional
	void activeBySuppier(WmsASN wmsAsn);
	/**码托 yc.min 20150409*/
	@Transactional
	void palletSerial(JacPalletSerial jps,List<String> values);
	/**自由码托 yc.min 20151117*/
	@Transactional
	void palletSerialAuto(String palletNo,WmsASNDetail detail);
	/**自动码托 yc.min 20150409*/
	@Transactional
	String palletAuto(Long asnId);
	/**保存导入的ASN文件*/
	@Transactional
	void saveAsnFile(WmsOrganization company,Long billTypeId,Map<String,List<Object[]>> csMap,Map<String,WmsASN> asns);
	/**生成托盘号*/
	@Transactional
	String getPalletSerialAuto(Map map);
	@Transactional
	RowData[] getBillType(Map params);
	
	/**
	 * 过账确认--将ASN明细传给ERP
	 * @author fs
	 * */
	@Transactional
	void confirmAccount(WmsASN asn);
	
	/**
	 * 是否送检码托
	 * @author fs
	 * */
	@Transactional
	void CheckMt(WmsASNDetail detail,List tableValues);
	
	/**
	 * 直接打印托盘标签
	 * @author fs
	 */
	Map printPalltDirec(WmsASN asn);
}