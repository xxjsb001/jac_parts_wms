package com.vtradex.wms.server.telnet.putaway;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.receiving.WmsASNDetail;
import com.vtradex.wms.server.telnet.dto.WmsASNDetailDTO;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.telnet.dto.WmsPutawayDTO;
import com.vtradex.wms.server.telnet.exception.RFFinishException;

/**
 * RF 上架
 *
 * @category Manager
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.3 $ $Date: 2015/05/06 09:21:22 $
 */
public interface WmsPutawayRFManager extends BaseManager {

	public WmsASNDetail getWmsAsnDetailByBarCode(String asnCode,String barCode, Long packageUnitId) throws BusinessException;
	
	/**
	 * 根据收货单获得最早创建的未完成且无托盘的task
	 * */
	@Transactional
	WmsTask getPutawayTaskByAsnCode(String asnCode, Boolean bePallet);
	
	/**
	 * 根据收货单获得最早创建的未完成且无托盘的task
	 * */
	@Transactional
	void deleteOPENMoveDoc(String asnCode);
	
	/**
	 * 根据Task获得上架单
	 * */
	@Transactional
	WmsMoveDoc getWmsMoveDocByTaskId(Long taskId);
	
	/**
	 * 根据task获得上架信息
	 * */
	@Transactional
	Map<String,Object> getInfoByTask(long taskId);
	
	/**
	 * 根据ASN和货品条码获得未上架的货品信息
	 * */
	Map<String,Object> getUnPutawayItemInfoByAsnAndBarCode(String asnCode,String barCode,String unit);
	
	/**
	 * 获得指定收货单指定条码货品的包装信息
	 * */
	List<WmsPackageUnit> getUnPutawayItemUnitByAsnAndBarCode(String asnCode,String barCode);
	
	/**
	 * 判断ASN是否存在并判断是否存在未码托待上架物料
	 * @param code 收货单号
	 * */
	void findAsnByCode(String code);
	
	/**
	 * 根据收货单号创建移位单
	 * */
	@Transactional
	WmsMoveDoc createWmsMoveDoc(Long moveDocId,String asnCode,Long inventoryId,Long packageId,Double putawayQuantity);
	
	/**
	 * 生效作业单
	 * */
	@Transactional
	void activeMoveDoc(Long moveDocId);
	
	@Transactional
	public WmsPutawayDTO getWmsPutawayDTO(String asnCode, Long moveDocId, String barCode) throws RFFinishException, BusinessException;
	
	public Boolean validateLocation(String locationCode) ;
	
	public void confirmPutaway(WmsPutawayDTO putawayDTO) throws BusinessException;
	
	/**
	 * 上架确认
	 * */
	@Transactional
	void confirmPutaway(Long taskId,String putawayLocationCode) throws BusinessException;
	
	@Transactional
	public WmsPutawayDTO getWmsPutawayDTOByPalletNo(String palletNo) throws BusinessException;
	
	@Transactional
	public void markExceptionWmsLocation(Long toLocationId) throws BusinessException;
	
	/**
	 * 重新分配库位
	 */
	@Transactional
	void resetAllocate(Long moveDocDetailId);
	
	@Transactional
	public String validatePalletRule(Long asnDetialId);

	@Transactional
	public WmsMoveDoc manualPalletAndAutoCreateMoveDoc(Long asnDetialId, String palletNum, String itemCode, String putawayLocationCode);
	
	public WmsASNDetailDTO getWmsASNDetailDTO(String asnCode, String barCode, Long packageUnitId) throws BusinessException;

	public List<WmsPackageUnit> getPackageUnitList(String asnCode, String barCode);
	
	/**
	 * 分配到指定库位
	 * */
	@Transactional
	void manualAllocate(Long moveDocId,String locationCode);
	@Transactional
	Integer putawayAutoAllocate(String asnCode);
	/**通过托盘获取分配的库位*/
	String palletSerial(String palletNo);
	/**RF单一上架确认*/
	@Transactional
	String shelvesConfirm(String palletNos,String locationCode);
	
	Long findAsnId(String asnCode);
	Long findLoc(String locationCode);
	Long findDetail(String itemCode,Long asnId);
	/**RF扫码上架确认*/
	@Transactional
	String putAway(Long detailId,Double putQuantity,Long locId);
}
