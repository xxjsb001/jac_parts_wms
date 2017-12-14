package com.vtradex.wms.server.telnet.receiving;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.organization.WmsItemState;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.model.receiving.WmsASNDetail;
import com.vtradex.wms.server.telnet.dto.WmsASNDetailDTO;

/**
 * 
 *
 * @category Manager
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.4 $Date: 2015/07/21 08:27:29 $
 */
public interface WmsReceivingRFManager extends BaseManager {
	
	public WmsASNDetailDTO getWmsASNDetailDTO(Long asnId, String barCode) throws BusinessException;
	
	public void detailReceive(WmsASNDetailDTO dto) throws BusinessException;
	
	Long findAsnId(String asnCode);
	Long findConfirmAsnId(String asnCode);
	String asnReceiveAll(Long asnId);
	String asnConfirmAll(Long asnId);
	String driectPrint(Long asnId) throws PrinterException, HttpException, IOException;
	String palletAuto(Long asnId);
	
	Integer palletSerial(String palletNo,Double quantity);
	
	public WmsASNDetailDTO getWmsASNDetailDTO(Long detailId) throws BusinessException;

	public List<WmsItemState> getItemStateStatus(Long detailId);
	
	public WmsASNDetail getAsnDetail(Long id);
	public WmsASN getAsnById(Long id);
	@Transactional
	Map<String,String> checkAsnAll(Long id);
	@Transactional
	void detailReceive(Long detailId,Long stateId,Double receivingQuantityBU,Long userId,String inventoryState);
	/**明细直接上架*/
	@Transactional
	Map<String,String> detailReceiveUp(Long detailId,Double receivingQuantityBU,Long userId,String inventoryState,String locationCode);
}
