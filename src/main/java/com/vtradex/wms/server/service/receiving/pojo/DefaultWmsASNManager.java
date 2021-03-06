package com.vtradex.wms.server.service.receiving.pojo;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.vtradex.engine.utils.DateUtils;
import com.vtradex.sequence.service.sequence.SequenceGenerater;
import com.vtradex.thorn.client.ui.page.IPage;
import com.vtradex.thorn.client.ui.table.RowData;
import com.vtradex.thorn.client.utils.StringUtils;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.exception.OriginalBusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.message.Task;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.LotInfo;
import com.vtradex.wms.server.model.interfaces.HeadType;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.inventory.WmsInventoryLogType;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.middle.MiddleCompanyExtends;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.model.move.WmsMoveDocType;
import com.vtradex.wms.server.model.move.WmsTask;
import com.vtradex.wms.server.model.move.WmsTaskStatus;
import com.vtradex.wms.server.model.organization.TypeOfBill;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsItemState;
import com.vtradex.wms.server.model.organization.WmsLotRule;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.receiving.JacPalletSerial;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.model.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.receiving.WmsASNQuality;
import com.vtradex.wms.server.model.receiving.WmsASNQualityStauts;
import com.vtradex.wms.server.model.receiving.WmsASNStatus;
import com.vtradex.wms.server.model.receiving.WmsBooking;
import com.vtradex.wms.server.model.receiving.WmsBookingStatus;
import com.vtradex.wms.server.model.receiving.WmsReceivedRecord;
import com.vtradex.wms.server.model.warehouse.WmsDock;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.service.interfaces.WmsDealInterfaceDataManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryExtendManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.receiving.WmsASNDetailManager;
import com.vtradex.wms.server.service.receiving.WmsASNManager;
import com.vtradex.wms.server.service.receiving.WmsMoveDocManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.task.WmsTaskManager;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.utils.PackageUtils;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

@SuppressWarnings("unchecked")
public class DefaultWmsASNManager extends DefaultBaseManager implements WmsASNManager {
	
	/**绑定库位*/
	private static String BD_Loc = "绑定库位";
	/**存储类型*/
	private static String STORE_Type = "存储类型";
	
	private WorkflowManager workflowManager;
	private WmsBussinessCodeManager codeManager;
	private WmsASNDetailManager detailManager;
	private WmsMoveDocManager wmsMoveDocManager;
	private WmsInventoryManager wmsInventoryManager;
	private WmsRuleManager wmsRuleManager;
	protected WmsTaskManager taskManager;
	protected WmsInventoryExtendManager inventoryExtendManager;
	protected WmsDealInterfaceDataManager dealInterfaceDataManager;
	
	public DefaultWmsASNManager (WorkflowManager workflowManager, WmsBussinessCodeManager codeManager, 
			WmsASNDetailManager detailManager, WmsMoveDocManager wmsMoveDocManager, 
			WmsInventoryManager wmsInventoryManager, WmsRuleManager wmsRuleManager
			,WmsTaskManager taskManager,WmsInventoryExtendManager inventoryExtendManager,
			WmsDealInterfaceDataManager dealInterfaceDataManager) {
		super();
		this.workflowManager = workflowManager;
		this.codeManager = codeManager;
		this.detailManager = detailManager;
		this.wmsMoveDocManager = wmsMoveDocManager;
		this.wmsInventoryManager = wmsInventoryManager;
		this.wmsRuleManager = wmsRuleManager;
		this.taskManager = taskManager;
		this.inventoryExtendManager = inventoryExtendManager;
		this.dealInterfaceDataManager = dealInterfaceDataManager;
	}
	public void addDetail(Long id, WmsASNDetail detail, double expectedQuantity) {
		// 当前明细对应ASN状态校验
		WmsASN asn =commonDao.load(WmsASN.class, id);
		WmsItem item = commonDao.load(WmsItem.class, detail.getItem().getId());
		WmsLotRule lotRule = item.getDefaultLotRule();
		
		if (detail.getLotInfo() != null) {
			detail.getLotInfo().setSupplier(asn.getSupplier());//yc.min
			detail.getLotInfo().isEmptyProductDate(new Date());//yc.min
			detail.getLotInfo().prepare(lotRule, detail.getItem(),asn.getCode());
		}
		
		if (!asn.getStatus().equals(WmsASNStatus.OPEN)) {
			throw new BusinessException("asn.status.error");
		}
		if (detail.isNew()) {
			detail.setAsn(asn);
			asn.addDetail(detail);
		} else {
			detail = this.commonDao.load(WmsASNDetail.class, detail.getId());
		} 
		
		// 预期收货数量计算(lineno为1表示是基本包装，基本包装只能有1个，不为1表示是件装)
		WmsPackageUnit packageUnit = commonDao.load(WmsPackageUnit.class, detail.getPackageUnit().getId());
		if (packageUnit.getLineNo().intValue() == 1) {
			detail.setExpectedQuantity(PackageUtils.convertPackQuantity(expectedQuantity, packageUnit));
			detail.setExpectedQuantityBU(expectedQuantity);
		} else {
			detail.setExpectedQuantity(expectedQuantity);
			detail.setExpectedQuantityBU(PackageUtils.convertBUQuantity(expectedQuantity, packageUnit));
		}
		if(detail.getPalletNo()==null||detail.getPalletNo()<=0){
			detail.setPalletNo(0);
		}
		asn.refreshQtyBU();
	}
	
	public void removeDetails(WmsASNDetail detail) {
		WmsASN asn = commonDao.load(WmsASN.class, detail.getAsn().getId());
		
		asn.removeDetail(detail);
		
		asn.refreshQtyBU();
//		workflowManager.doWorkflow(asn, "wmsASNQualityProcess.new");
	}
	
	public void receiveAll(Long asnId,Long workerId) {
		WmsASN asn = commonDao.load(WmsASN.class, asnId);
		//收货库位只新建一条[A-1-收货],库位类型[收货],库区[发动机库房],区排列层[0,0,0,0]
		WmsLocation loc = detailManager.findReceiveLocation(asn.getWarehouse(),"ASN", 
				asn.getBillType().getCode(), false,asn.getSupplier().getCode());
		for (WmsASNDetail detail : asn.getDetails()) {
			singleReceive(asn, detail, loc, null);
		}
		workflowManager.doWorkflow(asn, "wmsASNProcess.receiveAll");
	}
	public void multiReceive(WmsASNDetail asnDetail){
		WmsASN asn = commonDao.load(WmsASN.class, asnDetail.getAsn().getId());
		//收货库位只新建一条[A-1-收货],库位类型[收货],库区[发动机库房],区排列层[0,0,0,0]
		WmsLocation loc = detailManager.findReceiveLocation(asn.getWarehouse(),"ASN", 
				asn.getBillType().getCode(), false,asn.getSupplier().getCode());
		singleReceive(asn, asnDetail, loc, null);
		workflowManager.doWorkflow(asn, "wmsASNProcess.lineReceive");
	}
	private void singleReceive(WmsASN asn,WmsASNDetail detail,WmsLocation loc,Long workerId){
		if (detail.getUnReceivedQtyBU() > 0 && 
				!detail.getBeReceived()) {
			WmsLotRule lotRule = detail.getItem().getDefaultLotRule();
			LotInfo lotInfo = detail.getLotInfo();
			if(lotInfo == null){
				lotInfo = new LotInfo();
				detail.setLotInfo(lotInfo);
			}
			lotInfo.prepare(lotRule, detail.getItem(),asn.getCode());
			String pallet = StringUtils.isEmpty(detail.getPallet()) ? BaseStatus.NULLVALUE : detail.getPallet();
			String carton = StringUtils.isEmpty(detail.getCarton()) ? BaseStatus.NULLVALUE : detail.getCarton();
			String serialNo = StringUtils.isEmpty(detail.getSerialNo()) ? BaseStatus.NULLVALUE : detail.getSerialNo();
			detailManager.receiving(detail, PackageUtils.convertPackQuantity(detail.getUnReceivedQtyBU(), detail.getPackageUnit()), detail.getPackageUnit(), lotInfo, 
					BaseStatus.NULLVALUE, loc,pallet, carton, serialNo,workerId);
		}
	}
	public void detailReceive(WmsASNDetail detail, Long packageUnitId,double quantity, 
					Long receiveLocId, String itemStateId,Long workerId) {
//		if(detail.getBooking() == null) {
//			throw new OriginalBusinessException("未预约收货月台,请预约！");
//		}	
		if(quantity > detail.getExpectedQuantityBU()){
			throw new BusinessException("收货数量不能大于ASN明细的期待数量");
		}
		LotInfo lotInfo = detail.getLotInfo();
		//itemStateId页面隐藏不显示
		String status = "";
		if(StringUtils.isEmpty(itemStateId)){
			status = BaseStatus.NULLVALUE;
		}else {
			WmsItemState itemState = commonDao.load(WmsItemState.class, Long.valueOf(itemStateId));
			status = itemState.getName();
		}	
		if (receiveLocId == 0){
			//收货库位只新建一条[A-1-收货],库位类型[收货],库区[发动机库房],区排列层[0,0,0,0]
			WmsLocation loc = detailManager.findReceiveLocation(detail.getAsn().getWarehouse(), "ASN", 
					detail.getAsn().getBillType().getCode(), false, detail.getLotInfo().getSupplier().getCode());
			receiveLocId = loc.getId();
		}
		detailManager.receive(detail.getId(), lotInfo, packageUnitId, quantity, status, receiveLocId, 
			detail.getPallet(), detail.getCarton(), detail.getSerialNo(),workerId);
		LocalizedMessage.addLocalizedMessage("单一明细收货成功！");
	}
	
	
	public void detailReceive(WmsASNDetail detail, Long packageUnitId,double quantity, 
			Long receiveLocId, String itemStateId,Long workerId,String inventoryStatus) {
		if(quantity > (detail.getExpectedQuantityBU()-detail.getReceivedQuantityBU())){
			throw new BusinessException("收货数量不能大于ASN明细的期待数量");
		}
		LotInfo lotInfo = detail.getLotInfo();
		//itemStateId页面隐藏不显示
		String status = "";
		if(StringUtils.isEmpty(itemStateId)){
			status = BaseStatus.NULLVALUE;
		}else {
			WmsItemState itemState = commonDao.load(WmsItemState.class, Long.valueOf(itemStateId));
			status = itemState.getName();
		}	
		if (receiveLocId == 0){
			//收货库位只新建一条[A-1-收货],库位类型[收货],库区[发动机库房],区排列层[0,0,0,0]
			WmsLocation loc = detailManager.findReceiveLocation(detail.getAsn().getWarehouse(), "ASN", 
					detail.getAsn().getBillType().getCode(), false, detail.getLotInfo().getSupplier().getCode());
			receiveLocId = loc.getId();
		}
		detailManager.receive(detail.getId(), lotInfo, packageUnitId, quantity, status, receiveLocId, 
			detail.getPallet(), detail.getCarton(), detail.getSerialNo(),workerId,inventoryStatus);
		LocalizedMessage.addLocalizedMessage("单一明细收货成功！");
	}
	public void autoCreateMoveDoc(WmsASN wmsAsn) {
		if (wmsAsn.getReceivedQuantityBU().doubleValue() >= wmsAsn.getExpectedQuantityBU().doubleValue()) {
			receiveConfirm(wmsAsn);
		}
	}
	
	public WmsMoveDoc manualCreateMoveDoc(WmsASN wmsAsn) {
		wmsAsn = load(WmsASN.class,wmsAsn.getId());
		/*if(WmsASNQualityStauts.UNQUALITY.equals(wmsAsn.getQualityStauts()) && !wmsAsn.isQualityPutaway()) {			
			if(!isExistUnQuality(wmsAsn)) {
				throw new BusinessException("存在未质检完成的记录，请先处理！");
			}
		}*/
		autoPallet(wmsAsn);
		return autoCreateMoveDocByResults(wmsAsn);
	}
	public void putawayAutoAllocate(WmsASN wmsAsn){
		//加载上架规则表,一个托盘分配一个库位,产生task,锁定原库存,预分配目标库存,目标库位托盘数字段加一(palletQuantity),刷新库满度,JacPalletSerial目标库位信息赋值,asn分配状态和分配数量调整
		List<Long> asnDetailIds = commonDao.findByQuery("SELECT detail.id FROM WmsASNDetail detail"
				+ " WHERE detail.asn.id =:asnId", 
				new String[]{"asnId"}, new Object[]{wmsAsn.getId()});
		WmsOrganization supplier = commonDao.load(WmsOrganization.class, wmsAsn.getSupplier().getId());
		WmsBillType billType = commonDao.load(WmsBillType.class, wmsAsn.getBillType().getId());
		
		//将ASN下【已码托未分配上架库位】的托盘信息按照明细汇总
		List<Object[]> jpsObjs = commonDao.findByQuery("SELECT jps.asnDetail.id,jps.id FROM JacPalletSerial jps WHERE jps.asnDetail.id IN("
				+ "SELECT detail.id FROM WmsASNDetail detail WHERE detail.asn.id =:asnId)"
				+ " AND jps.expectedQuantityBU>0 AND jps.toLocationId IS NULL", 
				new String[]{"asnId"},new Object[]{wmsAsn.getId()});
		Map<Long,List<Long>> jpsMap = getJpsMap(wmsAsn,jpsObjs);
		if(jpsMap.size()>0){
			//将ASN下收货记录信息按照明细汇总
			Map<Long,Long> recMap = receivedRecordMap(wmsAsn);
			Double allocatedQuantityBU = 0.0;
			WmsReceivedRecord rec = null;
			Map<Long,String> otherLoc = new HashMap<Long,String>();//asnDetailId,存储类型:维护了存储类型,继续按照存储类型上到推荐库位
			Map<Long,String> bindingLoc = new HashMap<Long, String>();//asnDetailId,绑定库位:维护了绑定库位,则直接上到此库位
			//一次性将该ASN所需库位全部查找20150611
			Integer palletNo = 0;
			for(Long asnDetailId : asnDetailIds){
				if(!recMap.containsKey(asnDetailId)){
					continue;
				}
				WmsASNDetail detail = commonDao.load(WmsASNDetail.class, asnDetailId);
				
				//先加载【R102_上架库位类型表】,匹配上的物料不参与自动分配,库位分类从规则表获取,如果绑定库位不为空,则直接上到此库位
				Object[] bindingMap = bindingLoc(wmsAsn, detail, billType, BD_Loc);
				if(bindingMap==null){
					palletNo += detail.getPalletNo();
				}else{
					if(BD_Loc.equals(bindingMap[0])){
						bindingLoc.put(asnDetailId, bindingMap[1].toString());
					}else if(STORE_Type.equals(bindingMap[0])){
						otherLoc.put(asnDetailId, bindingMap[1].toString());
					}
				}
			}
			Object[] obj = null;
			List<List<Map<String, Object>>> resultObjs = null;
			Boolean beRemove = null;
			if(palletNo>0){
				obj = putawayAutoRule(supplier, billType, wmsAsn,palletNo,BaseStatus.NULLVALUE);
				resultObjs = (List<List<Map<String, Object>>>) obj[0];
				beRemove = (Boolean) obj[1];
				for(List<Map<String, Object>> rs : resultObjs){
					for(Long asnDetailId : asnDetailIds){
						if(!recMap.containsKey(asnDetailId)){
							continue;
						}
						if(bindingLoc.containsKey(asnDetailId) 
								|| otherLoc.containsKey(asnDetailId)){//过滤已经维护了【存储类型,绑定库位】的物料
							continue;
						}
						rec = commonDao.load(WmsReceivedRecord.class, recMap.get(asnDetailId));
						allocatedQuantityBU = putawayAutoSingle(rec,jpsMap,asnDetailId, 
								supplier, billType,beRemove, wmsAsn, allocatedQuantityBU,rs);
					}
				}
			}
			//将【R102_上架库位类型表】相同的【存储类型】的明细汇总
			Iterator<Entry<Long, String>> ii = otherLoc.entrySet().iterator();
			List<Long> dds = null;
			Map<String,List<Long>> ortherType = new HashMap<String, List<Long>>(); 
			while(ii.hasNext()){
				Entry<Long, String> entry = ii.next();
				String value = entry.getValue();
				if(ortherType.containsKey(value)){
					dds = ortherType.get(value);
				}else{
					dds = new ArrayList<Long>();
				}
				dds.add(entry.getKey());
				ortherType.put(value.toString(), dds);
			}otherLoc.clear();
			//按照【存储类型】依次执行【上架分配规则】
			Iterator<Entry<String, List<Long>>> iii = ortherType.entrySet().iterator();
			while(iii.hasNext()){
				palletNo = 0;
				Entry<String, List<Long>> entry = iii.next();
				for(Long asnDetailId : entry.getValue()){
					WmsASNDetail detail = commonDao.load(WmsASNDetail.class, asnDetailId);
					palletNo += detail.getPalletNo();
				}
				obj = putawayAutoRule(supplier, billType, wmsAsn,palletNo,entry.getKey());
				resultObjs = (List<List<Map<String, Object>>>) obj[0];
				beRemove = (Boolean) obj[1];
				for(List<Map<String, Object>> rs : resultObjs){
					for(Long asnDetailId : entry.getValue()){
						rec = commonDao.load(WmsReceivedRecord.class, recMap.get(asnDetailId));
						allocatedQuantityBU = putawayAutoSingle(rec,jpsMap,asnDetailId, 
								supplier, billType,beRemove, wmsAsn, allocatedQuantityBU,rs);
					}
				}
			}ortherType.clear();
			//按照【绑定库位】依次执行上架
			Iterator<Entry<Long, String>> iiii = bindingLoc.entrySet().iterator();
			while(iiii.hasNext()){
				Entry<Long, String> entry = iiii.next();
				rec = commonDao.load(WmsReceivedRecord.class, recMap.get(entry.getKey()));
				WmsLocation toLoc = (WmsLocation) commonDao.findByQueryUniqueResult("FROM WmsLocation l WHERE l.code =:code", 
						new String[]{"code"}, new Object[]{entry.getValue()});
				if(toLoc==null){
					throw new BusinessException("绑定库位不存在:"+entry.getValue());
				}
				WmsASNDetail detail = commonDao.load(WmsASNDetail.class, entry.getKey());
				List<Long> jpsIds = jpsMap.get(entry.getKey());
				for(int i = 0; i < jpsIds.size(); i++){
					JacPalletSerial jps = commonDao.load(JacPalletSerial.class, jpsIds.get(i));
					putawayAutoSingleStep(rec, jps, toLoc, detail, wmsAsn);
					allocatedQuantityBU += jps.getExpectedQuantityBU();
				}
			}
			wmsAsn.editAllocatedQuantityBU(allocatedQuantityBU);
			commonDao.store(wmsAsn);
		}else{
			LocalizedMessage.addLocalizedMessage(MyUtils.font("当前ASN无已码托还未分配信息,请码托后分配"));
		}
	}
	private Object[] bindingLoc(WmsASN wmsAsn,WmsASNDetail detail,WmsBillType billType,String key){
		String extendPropC1 = MyUtils.checkExtendPropc1(detail.getLotInfo().getExtendPropC1());
		if(extendPropC1==null){
			extendPropC1 = BaseStatus.NULLVALUE;
//			throw new BusinessException("明细工艺状态错误:"+detail.getLotInfo().getExtendPropC1());
		}
		Object value = wmsRuleManager.getSingleRuleTableDetail(wmsAsn.getWarehouse().getName(),
				"R102_上架库位类型表",key,detail.getLotInfo().getSupplier().getCode(),
				billType.getCode(),extendPropC1,detail.getItem().getCode());
		if((value==null || value.equals(BaseStatus.NULLVALUE)) && !STORE_Type.equals(key)){
			key = STORE_Type;
			value = wmsRuleManager.getSingleRuleTableDetail(wmsAsn.getWarehouse().getName(),
					"R102_上架库位类型表",key,detail.getLotInfo().getSupplier().getCode(),
					billType.getCode(),extendPropC1,detail.getItem().getCode());
		}
		Object[] bindingMap = null;
		if(value!=null){
			bindingMap = new Object[]{
					key, value
			};
		}
		return bindingMap;
	}
	/**单一上架分配*/
	private Double putawayAutoSingle(WmsReceivedRecord rec,Map<Long,List<Long>> jpsMap
			,Long asnDetailId,WmsOrganization supplier,WmsBillType billType,Boolean beRemove
			,WmsASN wmsAsn,Double allocatedQuantityBU,List<Map<String, Object>> resultObjs){
		WmsASNDetail detail = commonDao.load(WmsASNDetail.class, asnDetailId);
		if(detail.getReceivedQuantityBU()<=0){
			return allocatedQuantityBU;
		}
		
		List<Long> jpsIds = jpsMap.get(asnDetailId);
		if(jpsIds==null || jpsIds.size()<=0){
			return allocatedQuantityBU;
		}
		try {
			for(int i = 0; i < jpsIds.size(); i++){
				JacPalletSerial jps = commonDao.load(JacPalletSerial.class, jpsIds.get(i));
				if(jps.getBePutawayAuto()){
					continue;
				}
				int size = resultObjs.size();
				for (int j = 0; j < size; j++) {
					allocatedQuantityBU = putawayAutoByPallet(resultObjs, jps, rec, 
							detail, wmsAsn, allocatedQuantityBU, j);
					if(beRemove){
						resultObjs.remove(j);
					}
					break;
				}
			}
		} catch (Exception e) {
			throw new BusinessException(e.getLocalizedMessage());
		}
		return allocatedQuantityBU;
	}
	/**单一托盘自动分配*/
	private Double putawayAutoByPallet(List<Map<String, Object>> resultObjs,JacPalletSerial jps
			,WmsReceivedRecord rec,WmsASNDetail detail,WmsASN wmsAsn,Double allocatedQuantityBU
			,int j){
		//{动线号=4, 库位序号=13955, 库位代码=A-01010406, 列=4, 区=1, 排=1, 层=6}
		Map<String, Object> wmsTaskInfos = resultObjs.get(j);
		WmsLocation toLoc = commonDao.load(WmsLocation.class,(Long) wmsTaskInfos.get("库位序号"));
		//DefaultWmsTransactionalManager.putawayAllocate
		//单一上架步骤
		putawayAutoSingleStep(rec, jps, toLoc, detail, wmsAsn);
		allocatedQuantityBU += jps.getExpectedQuantityBU();
		return allocatedQuantityBU;
	}
	/**加载上架规则*/
	private Object[] putawayAutoRule(WmsOrganization supplier,WmsBillType billType
			,WmsASN wmsAsn,Integer palletNo,String storageType){
		Map<String,Object> problem = new HashMap<String, Object>();
		problem.put("供应商编码",supplier.getCode());
		problem.put("单据类型编码",billType.getCode());
		//是否托盘依据ASN明细新建时录入的托盘个数决定,当大于0时'是',小于等于0时'否'
		problem.put("存储类型",storageType);//detail.getPalletNo()>0?"是":"否",palletNo>0?"是":"否",BaseStatus.NULLVALUE
		problem.put("托盘个数",palletNo);//detail.getPalletNo()
		problem.put("仓库ID", wmsAsn.getWarehouse().getId());
		problem.put("仓库代码", wmsAsn.getWarehouse().getCode());
		List<Map<String, Object>> resultObjs = null;
		String beRemove = "是";
		try {
			Map<String, Object> result = wmsRuleManager.execute(wmsAsn.getWarehouse().getName(), 
					wmsAsn.getCompany().getName(), "上架分配规则", problem);
			resultObjs = (List<Map<String, Object>>) result.get("上架策略库位列表");
			beRemove = (String) result.get("是否过滤库存");
		} catch (Exception e) {
			throw new BusinessException(e.getLocalizedMessage());
		}
		return new Object[]{
				resultObjs,beRemove.equals("是")?true:false	
		};
	}
	/**单一上架步骤*/
	private void putawayAutoSingleStep(WmsReceivedRecord rec,JacPalletSerial jps
			,WmsLocation toLoc,WmsASNDetail detail,WmsASN wmsAsn){
		WmsLocation srcLoc = commonDao.load(WmsLocation.class, rec.getLocationId());
		WmsItemKey itemKey = commonDao.load(WmsItemKey.class, rec.getItemKey().getId());
//
		WmsInventory srcInventory = load(WmsInventory.class,rec.getInventoryId());
		srcInventory.allocatePickup(jps.getExpectedQuantityBU());
		commonDao.store(srcInventory);
		//预分配目标库存
		WmsInventory dstInventory = wmsInventoryManager.allocatePutaway(toLoc, itemKey, detail.getPackageUnit() 
				,rec.getInventoryStatus(),jps.getExpectedQuantityBU(),1);
		// 调用规则刷新库满度
		wmsInventoryManager.refreshLocationUseRate(toLoc, 0);
		commonDao.store(toLoc);
		//产生task
		WmsTask task = taskManager.getTaskByJAC(srcLoc, WmsMoveDocType.MV_PUTAWAY, wmsAsn.getCode(), detail.getId().toString(), 
				jps.getPalletNo(), toLoc, itemKey, rec.getPackageUnit(), rec.getInventoryStatus(), 
				jps.getExpectedQuantityBU(), rec.getInventoryId(), dstInventory.getId());
		
		jps.setToLocationCode(toLoc.getCode());
		jps.setToLocationId(toLoc.getId());
		jps.setBePutawayAuto(true);
		commonDao.store(jps);
	}
	//单一上架预分配步骤
	public WmsTask putawayAutoSingleStep(WmsReceivedRecord rec,Double allocateQty
			,WmsLocation toLoc,WmsASNDetail detail,String asnCode){
		WmsLocation srcLoc = commonDao.load(WmsLocation.class, rec.getLocationId());
		WmsItemKey itemKey = commonDao.load(WmsItemKey.class, rec.getItemKey().getId());
		WmsInventory srcInventory = load(WmsInventory.class,rec.getInventoryId());
		srcInventory.allocatePickup(allocateQty);
		commonDao.store(srcInventory);
		//预分配目标库存
		WmsInventory dstInventory = wmsInventoryManager.allocatePutaway(toLoc, itemKey, detail.getPackageUnit() 
				,rec.getInventoryStatus(),allocateQty,1);
		// 调用规则刷新库满度
		wmsInventoryManager.refreshLocationUseRate(toLoc, 0);
		commonDao.store(toLoc);
		//产生task
		WmsTask task = taskManager.getTaskByJAC(srcLoc, WmsMoveDocType.MV_PUTAWAY, asnCode, detail.getId().toString(), 
				BaseStatus.NULLVALUE, toLoc, itemKey, rec.getPackageUnit(), rec.getInventoryStatus(), 
				allocateQty, rec.getInventoryId(), dstInventory.getId());
		return task;
	}
	public void putawayAutoByHand(JacPalletSerial jps,Long asnId,Long locationId,Long workerId){
//		System.out.println(jps+","+asnId+","+locationId+","+workerId);
		jps = commonDao.load(JacPalletSerial.class, jps.getId());
		WmsASNDetail asnDetail = commonDao.load(WmsASNDetail.class, jps.getAsnDetail().getId());
		WmsASN wmsAsn = commonDao.load(WmsASN.class, asnDetail.getAsn().getId());
		WmsOrganization supplier = commonDao.load(WmsOrganization.class, wmsAsn.getSupplier().getId());
		WmsBillType billType = commonDao.load(WmsBillType.class, wmsAsn.getBillType().getId());
		if(org.apache.commons.lang.StringUtils.isEmpty(jps.getToLocationCode())){
			//将ASN下收货记录信息按照明细汇总
			Map<Long,Long> recMap = receivedRecordMap(wmsAsn);
			WmsReceivedRecord rec = commonDao.load(WmsReceivedRecord.class, recMap.get(asnDetail.getId()));
			
			Double allocatedQuantityBU = 0.0;
			if(locationId==0L){//自动分配
				List<Map<String, Object>> resultObjs = (List<Map<String, Object>>) putawayAutoRule(
						supplier, billType,wmsAsn,asnDetail.getPalletNo(),BaseStatus.NULLVALUE)[0];
				int size = resultObjs.size();
				for (int j = 0; j < size; j++) {
					allocatedQuantityBU = putawayAutoByPallet(resultObjs, jps, rec, 
							asnDetail, wmsAsn, allocatedQuantityBU, j);
					resultObjs.remove(j);
					break;
				}
			}else{//手工上架
				WmsLocation toLoc = commonDao.load(WmsLocation.class,locationId);
				putawayAutoSingleStep(rec, jps, toLoc, asnDetail, wmsAsn);
				allocatedQuantityBU = jps.getExpectedQuantityBU();
			}
			wmsAsn.editAllocatedQuantityBU(allocatedQuantityBU);
			commonDao.store(wmsAsn);
		}else{
			LocalizedMessage.addLocalizedMessage(MyUtils.font("当前托盘已经分配过,不允许再次分配:"+jps.getToLocationCode()));
		}
	}
	/**将ASN下【已码托未分配上架库位】的托盘信息按照明细汇总*/
	private Map<Long,List<Long>> getJpsMap(WmsASN wmsAsn,List<Object[]> jpsObjs){
		Map<Long,List<Long>> jpsMap = new HashMap<Long, List<Long>>();
		Long asnDetailId = null,jpsId = null;
		List<Long> jpsIds = null;
		for(Object[] obj : jpsObjs){
			asnDetailId = Long.parseLong(obj[0].toString());
			jpsId = Long.parseLong(obj[1].toString());
			if(jpsMap.containsKey(asnDetailId)){
				jpsIds = jpsMap.get(asnDetailId);
			}else{
				jpsIds = new ArrayList<Long>();
			}
			jpsIds.add(jpsId);
			jpsMap.put(asnDetailId, jpsIds);
		}
		return jpsMap;
	}
	/**将ASN下收货记录信息按照明细汇总*/
	private Map<Long,Long> receivedRecordMap(WmsASN wmsAsn){
		Long asnDetailId = null,jpsId = null;
		Map<Long,Long> recMap = new HashMap<Long,Long>();
		List<Object[]> recObjs = commonDao.findByQuery("SELECT rec.asnDetail.id,rec.id FROM WmsReceivedRecord rec WHERE rec.asnDetail.id IN("
				+ "SELECT detail.id FROM WmsASNDetail detail WHERE detail.asn.id =:asnId)"
				+ " AND rec.receivedQuantityBU>0", 
				new String[]{"asnId"},new Object[]{wmsAsn.getId()});
		for(Object[] obj : recObjs){
			asnDetailId = Long.parseLong(obj[0].toString());
			jpsId = Long.parseLong(obj[1].toString());
			recMap.put(asnDetailId, jpsId);
		}
		return recMap;
	}
	/**取消分配  yc.min 20150409*/
	public void unPutawayAutoAllocate(WmsASN wmsAsn){
		//删除TASK(task的originalBillCode字段取值为ASN的code),源库存取消拣货分配,目标库存取消上架分配,目标库位托盘数字段减一(palletQuantity),刷新库满度,JacPalletSerial目标库位信息置空,asn分配状态[打开]和分配数量[0]
		List<WmsTask> tasks = commonDao.findByQuery("FROM WmsTask task WHERE task.originalBillCode =:asnCode"
				+ " AND task.type =:type AND task.status =:status", 
				new String[]{"asnCode","type","status"},
				new Object[]{wmsAsn.getCode(),WmsMoveDocType.MV_PUTAWAY,WmsTaskStatus.OPEN});
		Double unPutawayAutoAllocate = 0D;
		List<String> pallets = new ArrayList<String>();
		for(WmsTask task : tasks){
			unPutawayAutoSingle(task);
			unPutawayAutoAllocate += task.getPlanQuantityBU();
			pallets.add("'"+task.getPallet()+"'");
		}
		if(tasks.size()>0){
			//更新JacPalletSerial目标库位信息为空
			commonDao.executeByHql("UPDATE JacPalletSerial jps SET jps.toLocationId = NULL,jps.toLocationCode = NULL,jps.bePutawayAuto = false"
					+ " WHERE jps.asnDetail.id IN("
					+ "SELECT detail.id FROM WmsASNDetail detail WHERE detail.asn.id =:asnId)"
					+ " AND jps.palletNo IN ("
					+org.apache.commons.lang.StringUtils.substringBetween(pallets.toString(), "[", "]")+")", 
					new String[]{"asnId"},new Object[]{wmsAsn.getId()});
			//删除task
			commonDao.executeByHql("DELETE FROM WmsTask task WHERE task.originalBillCode =:asnCode"
					+ " AND task.type =:type AND task.status =:status", 
					new String[]{"asnCode","type","status"},
					new Object[]{wmsAsn.getCode(),WmsMoveDocType.MV_PUTAWAY,WmsTaskStatus.OPEN});
			//更新asn分配状态和分配数量
			wmsAsn.editAllocatedQuantityBU(
					wmsAsn.getAllocatedQuantityBU()>=unPutawayAutoAllocate?-unPutawayAutoAllocate:-wmsAsn.getAllocatedQuantityBU());
			commonDao.store(wmsAsn);
		}else{
			LocalizedMessage.addLocalizedMessage(MyUtils.font("当前ASN码托分配信息已上架,不允许取消分配"));
		}
	}
	private void unPutawayAutoSingle(WmsTask task){
		//源库存取消拣货分配
		unallocatePickup(task);
		//目标库存取消上架分配
		unallocatePutaway(task);
		//目标库位托盘数字段减一
		toLocRemovePallet(task);
	}
	public void unPutawayAutoSingle(WmsASN wmsAsn,WmsTask task){
//		System.out.println(wmsAsn+","+task);
		wmsAsn = commonDao.load(WmsASN.class, wmsAsn.getId());
		task = commonDao.load(WmsTask.class, task.getId());
		if(!task.getStatus().equals(WmsTaskStatus.OPEN)){
			throw new BusinessException("当前任务状态不是打开状态");
		}
		unPutawayAutoSingle(task);
		//更新JacPalletSerial目标库位信息为空
		List<String> pallets = new ArrayList<String>();
		pallets.add("'"+task.getPallet()+"'");
		commonDao.executeByHql("UPDATE JacPalletSerial jps SET jps.toLocationId = NULL,jps.toLocationCode = NULL,jps.bePutawayAuto = false"
				+ " WHERE jps.asnDetail.id IN("
				+ "SELECT detail.id FROM WmsASNDetail detail WHERE detail.asn.id =:asnId)"
				+ " AND jps.palletNo IN ("
				+org.apache.commons.lang.StringUtils.substringBetween(pallets.toString(), "[", "]")+")", 
				new String[]{"asnId"},new Object[]{wmsAsn.getId()});
		//更新asn分配状态和分配数量
		wmsAsn.editAllocatedQuantityBU(-task.getPlanQuantityBU());
		commonDao.store(wmsAsn);
		//删除task
		commonDao.delete(task);
	}
	/**源库存取消拣货分配*/
	private void unallocatePickup(WmsTask task){
		WmsInventory srcInventory = load(WmsInventory.class,task.getSrcInventoryId());
		srcInventory.unallocatePickup(task.getPlanQuantityBU());
		commonDao.store(srcInventory);
	}
	/**目标库存取消上架分配*/
	private void unallocatePutaway(WmsTask task){
		WmsInventory dstInventory = load(WmsInventory.class,task.getDescInventoryId());
		dstInventory.unallocatePutaway(task.getPlanQuantityBU());
		commonDao.store(dstInventory);
	}
	/**目标库位托盘数字段减一*/
	private void toLocRemovePallet(WmsTask task){
		WmsLocation toLoc = commonDao.load(WmsLocation.class,task.getToLocationId());
		toLoc.removePallet(1);
		wmsInventoryManager.refreshLocationUseRate(toLoc, 0);
		commonDao.store(toLoc);
	}
	/**分配库存更换目标库位*/
	private void changeLoc(WmsTask task){
		WmsLocation toLoc = commonDao.load(WmsLocation.class,task.getToLocationId());
		WmsInventory dstInventory = load(WmsInventory.class,task.getDescInventoryId());
		dstInventory.setLocation(toLoc);
		commonDao.store(dstInventory);
		toLoc.addPallet(1);
		commonDao.store(toLoc);
		task.setResourcetype("更换库位");
		commonDao.store(task);
	}
	
	/**整单上架确认 yc.min 20150409*/
	public void shelvesConfirm(Long asnId,Long workerId){
		//从task获取所需信息(源库存,目标库存,托盘号,上架量),原库存扣减库存量WmsInventory.removeQuantityBU/取消拣货分配unallocatePickup,
		//目标库存增加库存量addQuantityBU/取消上架分配unallocatePutaway,刷新库满度,增加目标库位序列号信息,调整TASK状态和上架量,
		//调整ASN明细上架量,调整收货记录上架量,调整ASN上架状态和上架量
		WmsASN wmsAsn = commonDao.load(WmsASN.class, asnId);
		
		//将ASN下收货记录信息按照明细汇总
		Map<Long,Long> recMap = receivedRecordMap(wmsAsn);
		WmsReceivedRecord rec = null;
		
		List<WmsTask> tasks = commonDao.findByQuery(MyUtils.getTaskOriginalBillCode(), 
				new String[]{"billCode","type","status"},
				new Object[]{wmsAsn.getCode(),WmsMoveDocType.MV_PUTAWAY,WmsTaskStatus.OPEN});
		if(tasks.size()>0){
			for(WmsTask task : tasks){
				singleConfirmNormal(task, rec, recMap,workerId);
			}
			wmsAsn.editShelvesStauts();
			commonDao.store(wmsAsn);
		}else{
			LocalizedMessage.addLocalizedMessage(MyUtils.font("当前ASN无已码托分配还未上架信息,请码托分配后上架"));
		}
	}
	
	private void singleConfirmNormal(WmsTask task,WmsReceivedRecord rec,Map<Long,Long> recMap
			,Long workerId){
		if(!task.getStatus().equals(WmsTaskStatus.OPEN)){
			throw new BusinessException("当前任务状态不是打开状态");
		}
		//原库存扣减库存量/取消拣货分配
		wmsInventoryManager.moveSrcInv(task.getSrcInventoryId(),task.getUnmovedQuantityBU());
		//目标库存增加库存量/取消上架分配
		WmsInventory dstInventory = wmsInventoryManager.moveDescInv(task.getDescInventoryId(),
				task.getUnmovedQuantityBU());
		
		WmsLocation toLoc = commonDao.load(WmsLocation.class,task.getToLocationId());
		wmsInventoryManager.refreshLocationUseRate(toLoc, 0);
		commonDao.store(toLoc);
		
		// 增加目标库位序列号信息
		inventoryExtendManager.addInventoryExtend(dstInventory, task.getPallet()
				, BaseStatus.NULLVALUE,BaseStatus.NULLVALUE,task.getUnmovedQuantityBU());
		
		rec = commonDao.load(WmsReceivedRecord.class, recMap.get(Long.parseLong(task.getRelatedBill())));
		rec.addMovedQuantity(task.getUnmovedQuantityBU());
		commonDao.store(rec);
		
		task.editMovedQuantityBU(task.getUnmovedQuantityBU());
		commonDao.store(task);
	}

	public void singleConfirm(WmsTask task,Long asnId,Long locationId,Long workerId){
//		System.out.println(task.getId()+","+asnId+","+locationId+","+workerId);
		WmsASN wmsAsn = commonDao.load(WmsASN.class, asnId);
		task = commonDao.load(WmsTask.class, task.getId());
		//将ASN下收货记录信息按照明细汇总
		Map<Long,Long> recMap = receivedRecordMap(wmsAsn);
		WmsReceivedRecord rec = null;
		if(locationId==0L){
			singleConfirmNormal(task, rec, recMap,workerId);
		}else{//分配库位托盘数减一/更换task目标库位/分配库存更换目标库位
			wmsInventoryManager.verifyLocation(BaseStatus.LOCK_IN,locationId, task.getPackageUnit(),task.getItemKey().getId(),
					task.getItemKey().getItem().getId(),task.getUnmovedQuantityBU(),task.getPallet());
			toLocRemovePallet(task);
			//更换task目标库位
			WmsLocation toLoc = commonDao.load(WmsLocation.class,locationId);
			if(toLoc==null){
				throw new BusinessException("目标库位不存在");
			}
			task.setToLocationId(locationId);
			task.setToLocationCode(toLoc.getCode());
			commonDao.store(task);
			//分配库存更换目标库位
			changeLoc(task);
			singleConfirmNormal(task, rec, recMap,workerId);
		}
		wmsAsn.editShelvesStauts();
		commonDao.store(wmsAsn);
		recMap.clear();
	}
	public void finishReceive(WmsASN asn) {
		asn.setEndReceivedDate(new Date());
		commonDao.store(asn);
		close(asn);
	}
	
	public void qualityConfirm(WmsASN wmsAsn) {
		//自动码托 创建上架单
		if (wmsAsn.getReceivedQuantityBU().doubleValue() >= wmsAsn.getExpectedQuantityBU().doubleValue()) {
			List<WmsReceivedRecord> records = this.getUnVerifiedRecordByASN(wmsAsn.getId());
			if (records != null && !records.isEmpty()) {
				autoPallet(wmsAsn);
			}
			autoCreateMoveDocByResults(wmsAsn);
		}
	}
	
	public void close(WmsASN asn) {
		Long unFinishPutawayMoveDocCount = (Long)commonDao.findByQueryUniqueResult("SELECT count(*) FROM WmsMoveDoc moveDoc WHERE " +
			"moveDoc.asn.id = :asnId AND moveDoc.type = :type AND moveDoc.status <> :status",
			new String[]{"asnId","type","status"},
			new Object[]{asn.getId(),WmsMoveDocType.MV_PUTAWAY,WmsMoveDocStatus.FINISHED});
		if (unFinishPutawayMoveDocCount > 0) {
			throw new BusinessException("存在未处理完成的上架单，请先处理！");
		}
		checkUndercharged(asn);
		workflowManager.doWorkflow(asn, "wmsASNPutawayProcess.close");
		
		bookingFinish(asn);
	}
	
	private void bookingFinish(WmsASN asn){
		String hql = "UPDATE WmsBooking booking SET booking.finishTime = :finishTime, booking.status = :status  " +
				"WHERE booking.asn.id = :asnId AND booking.status != :finishStatus";
		commonDao.executeByHql(hql, new String[]{"finishTime","status","asnId","finishStatus"}, new Object[]{new Date(),WmsBookingStatus.FINISH,asn.getId(),WmsBookingStatus.FINISH});
	}
	
	
	protected void checkUndercharged(WmsASN asn){
		String warehouseName = WmsWarehouseHolder.getWmsWarehouse().getName();
		String companyName = asn.getCompany().getName();
		for(WmsASNDetail detail : asn.getDetails()){
			if(detail.getReceivedQuantityBU() < detail.getExpectedQuantityBU()){
				String class1 = detail.getItem().getClass1();
				String item = detail.getItem().getCode(); 
				String msg = "【" + class1 + "," + item + "】";
				Object value = wmsRuleManager.getSingleRuleTableDetail(warehouseName,
						"R101_收货_收货规则表","可短收数量",companyName,class1,item);
				if(value == null){
					value = wmsRuleManager.getSingleRuleTableDetail(warehouseName,
							"R101_收货_收货规则表","可短收数量",companyName,class1,"所有");
					msg = "【" + class1 + "】";
				}
				if(value != null){
					Double underQuantity = Double.valueOf(value.toString());
					if(DoubleUtils.compareByPrecision(detail.getUnReceivedQtyBU(), underQuantity, detail.getPackageUnit().getPrecision()) == 1){
						throw new BusinessException(msg + "未收货数量大于可短收数量！");
					}
				}
			}
			
		}
		if(asn.getReceivedQuantityBU() < asn.getExpectedQuantityBU()){
			Object value = wmsRuleManager.getSingleRuleTableDetail(warehouseName,
					"R101_收货_收货规则表","可短收数量",companyName,"所有","所有");
			if(value != null){
				Double underQuantity = Double.valueOf(value.toString());
				if(DoubleUtils.compareByPrecision(asn.getUnReceivedQtyBU(), underQuantity, 4) == 1){
					throw new BusinessException("【" + companyName + "】未收货数量大于可短收数量！");
				}
			}
		}
	}
	
	public void cancelAllReceive(WmsASN wmsASN) {
		List<WmsReceivedRecord> receivedRecords = commonDao.findByQuery("FROM WmsReceivedRecord record"
				+ " WHERE record.asn.id = :asnId"
				+ " AND record.movedQuantity<=0", 
			new String[]{"asnId"},new Object[]{wmsASN.getId()});

		for (WmsReceivedRecord receivedRecord : receivedRecords) {
			cancelReceive(receivedRecord,receivedRecord.getReceivedQuantityBU());
		}
	}
	
	public void singleCanelReceive(WmsASNDetail asnDetail,List<String> cancelReceiveQtyList) {
		Double cancelQty;
		try {
			cancelQty = Double.valueOf(cancelReceiveQtyList.get(0));
		} catch (NumberFormatException nfe) {
			throw new BusinessException("取消收货数量只能为数值类型！");
		}
		if (cancelQty.doubleValue() > asnDetail.getReceivedQuantityBU().doubleValue()) {
			throw new BusinessException("取消收货数量不能大于收货数量！");
		}
		// 优先处理已过帐且托盘为空的
		List<WmsReceivedRecord> receivedRecords = commonDao.findByQuery("FROM WmsReceivedRecord receivedRecord WHERE receivedRecord.asnDetail.id = :asnDetailId AND receivedRecord.beVerified = false AND receivedRecord.pallet = :pallet",
			new String[]{"asnDetailId","pallet"}, new Object[]{asnDetail.getId(),BaseStatus.NULLVALUE});
		cancelQty = cancelReceives(receivedRecords,cancelQty);
		// 然后处理已过账有托盘的
		if (cancelQty.doubleValue() > 0) {
			receivedRecords = commonDao.findByQuery("FROM WmsReceivedRecord receivedRecord WHERE receivedRecord.asnDetail.id = :asnDetailId AND receivedRecord.beVerified = false",
				"asnDetailId", asnDetail.getId());
			cancelQty = cancelReceives(receivedRecords,cancelQty);
		}
		// 再处理已经过账且托盘为空的
		if (cancelQty.doubleValue() > 0) {
			receivedRecords = commonDao.findByQuery("FROM WmsReceivedRecord receivedRecord WHERE receivedRecord.asnDetail.id = :asnDetailId AND receivedRecord.pallet = :pallet",
				new String[]{"asnDetailId","pallet"}, new Object[]{asnDetail.getId(),BaseStatus.NULLVALUE});
			cancelQty = cancelReceives(receivedRecords,cancelQty);
		}
		// 最后处理已过帐的
		if (cancelQty.doubleValue() > 0) {
			receivedRecords = commonDao.findByQuery("FROM WmsReceivedRecord receivedRecord WHERE receivedRecord.asnDetail.id = :asnDetailId",
					"asnDetailId", asnDetail.getId());
			cancelQty = cancelReceives(receivedRecords,cancelQty);
		}
		workflowManager.doWorkflow(asnDetail.getAsn(), "wmsASNProcess.singleCanel");
	}
	
	public Double cancelReceives(List<WmsReceivedRecord> receivedRecords,Double cancelQty) {
		for (WmsReceivedRecord receivedRecord : receivedRecords) {
			if (cancelQty.doubleValue() > receivedRecord.getReceivedQuantityBU().doubleValue()) {
				cancelQty -= receivedRecord.getReceivedQuantityBU();
				cancelReceive(receivedRecord,receivedRecord.getReceivedQuantityBU());
			} else {
				cancelReceive(receivedRecord,cancelQty);
				cancelQty = 0D;
			}
			if (cancelQty.doubleValue() <= 0) {
				return 0D;
			}
		}
		return cancelQty;
	}
	public void cancelReceive(WmsReceivedRecord receivedRecord) {
		
		wmsInventoryManager.cancelReceive(receivedRecord);
		
		receivedRecord.cancelReceive(receivedRecord.getReceivedQuantity());
		receivedRecord.cancelMovedQuantity(receivedRecord.getMovedQuantity());
		commonDao.delete(receivedRecord);
	}
	public void cancelReceive(WmsReceivedRecord receivedRecord,Double cancelQtyBU) {
		List<String> status = new ArrayList<String>();
		status.add(WmsMoveDocStatus.OPEN);
		status.add(WmsMoveDocStatus.PARTALLOCATED);
		status.add(WmsMoveDocStatus.ALLOCATED);
		status.add(WmsMoveDocStatus.ACTIVE);
		
		Long unFinishPutawayMoveDocCount = (Long)commonDao.findByQueryUniqueResult("SELECT count(*) FROM WmsMoveDoc moveDoc WHERE " +
			"moveDoc.asn.id = :asnId AND moveDoc.type = :type AND moveDoc.status in (:status)",
			new String[]{"asnId","type","status"},
			new Object[]{receivedRecord.getAsn().getId(),WmsMoveDocType.MV_PUTAWAY,status});
		if (unFinishPutawayMoveDocCount > 0) {
			throw new BusinessException("存在未处理完成的上架单，请先处理！");
		}
		//清除已经码托信息
		List<JacPalletSerial> jps = commonDao.findByQuery("FROM JacPalletSerial jps WHERE 1=1"
				+ " AND jps.asnDetail.id =:asnDetailId AND jps.expectedQuantityBU>0", 
				new String[]{"asnDetailId"}, new Object[]{receivedRecord.getAsnDetail().getId()});
		if(jps!=null && jps.size()>0){
			for(JacPalletSerial j : jps){
				j.setExpectedQuantityBU(0D);
				j.setMovedQuantityBU(0D);
				commonDao.store(j);
			}
		}
		if (receivedRecord.getBeVerified()) {
			wmsInventoryManager.cancelReceive(receivedRecord, cancelQtyBU);
		}
		receivedRecord.cancelReceive(cancelQtyBU);
		receivedRecord.cancelMovedQuantity(cancelQtyBU);
		if (receivedRecord.getReceivedQuantityBU() <= 0.0) {
			commonDao.delete(receivedRecord);
		}
		workflowManager.doWorkflow(receivedRecord.getAsn(), "wmsASNPutawayProcess.cancelMoveDoc");
		if(!WmsASNQualityStauts.NOQUALITY.equals(receivedRecord.getAsn().getQualityStauts())){
			workflowManager.doWorkflow(receivedRecord.getAsn(), "wmsASNQualityProcess.qualitySuccess");
		}
	}
	
	public void cancelReceivedRecordPallet(String palletNO) {
		List<WmsReceivedRecord> receivedRecords = commonDao.findByQuery("FROM WmsReceivedRecord receivedRecord WHERE receivedRecord.pallet = :palletNO",
			"palletNO", palletNO);
		for (WmsReceivedRecord receivedRecord : receivedRecords) {
			receivedRecord.setPallet(BaseStatus.NULLVALUE);
		}
	}
	
	public void autoPallet(WmsASN wmsASN) {
		List<WmsInventory> inventorys = wmsInventoryManager.getInventoryBySoi(wmsASN.getCode());
		
		if (inventorys == null || inventorys.isEmpty()) {
			return;
		}
		
		//码托处理
		for(WmsInventory inventory : inventorys) {
			
			String hql = "SELECT sum(invExtend.quantityBU) FROM WmsInventoryExtend invExtend WHERE invExtend.inventory.id = :inventoryId AND invExtend.pallet != '-' AND invExtend.quantityBU > 0";
			Double palletQuantityBU = (Double)commonDao.findByQueryUniqueResult(hql, "inventoryId", inventory.getId());
			hql = "SELECT sum(invExtend.allocatedQuantityBU) FROM WmsInventoryExtend invExtend WHERE invExtend.inventory.id = :inventoryId AND invExtend.pallet = '-' AND invExtend.quantityBU  > 0";
			Double allocatedQuantityBU = (Double)commonDao.findByQueryUniqueResult(hql, "inventoryId", inventory.getId());
			Double palletQuantity = PackageUtils.convertPackQuantity(palletQuantityBU == null ? 0D : palletQuantityBU, inventory.getPackageUnit());
			Double allocatedQuantity = PackageUtils.convertPackQuantity(allocatedQuantityBU == null ? 0D : allocatedQuantityBU, inventory.getPackageUnit());
			Double unPalletQuantity = inventory.getQuantity() - palletQuantity - allocatedQuantity;
			if(unPalletQuantity <= 0D){
				continue;
			}
			
			// 调用码托规则, 进行码托处理
			Map problem = new HashMap();
			
			problem.put("类型", "收货上架");
			problem.put("货主", wmsASN.getCompany().getName());
			problem.put("码托分类", inventory.getItemKey().getItem().getClass3());
			problem.put("包装级别", inventory.getPackageUnit().getLevel());
			problem.put("收货状态", inventory.getStatus());
			problem.put("单据类型", wmsASN.getBillType().getName());
			problem.put("收货数量", unPalletQuantity);
			
			Map<String, Object> result = wmsRuleManager.execute(wmsASN.getWarehouse().getName(), problem.get("货主").toString(), "码托规则", problem);
			
			List<Map<String, Object>> palletMethods = (List<Map<String, Object>>) result.get("码托详情列表");
			
			
			hql = " FROM WmsInventoryExtend invExtend WHERE invExtend.inventory.id = :inventoryId AND invExtend.pallet = '-' AND invExtend.quantityBU - invExtend.allocatedQuantityBU > 0";
			List<WmsInventoryExtend> invExtends = commonDao.findByQuery(hql, "inventoryId", inventory.getId());
			for (Map<String, Object> palletMethod : palletMethods) {
				String palletNo = palletMethod.get("托盘号").toString();
				Double palletQty = Double.valueOf(palletMethod.get("数量").toString());
				//不能码托的记录无需处理
				if (BaseStatus.NULLVALUE.equals(palletNo)) {
					continue;
				}
				
				for (WmsInventoryExtend invExtend : invExtends) {
					Double quantity = PackageUtils.convertPackQuantity(invExtend.getAvailableQuantityBU(), inventory.getPackageUnit());
					if (invExtend.getAllocatedQuantityBU() <= 0D && DoubleUtils.compareByPrecision(quantity, palletQty, inventory.getPackageUnit().getPrecision()) <= 0) {
						//收货记录数量少于或等于一个托盘数量
						invExtend.setPallet(palletNo);
						palletQty -= quantity;
					} else {
						//将托盘数量计入新产生的收货记录中
						WmsInventoryExtend nrr = EntityFactory.getEntity(WmsInventoryExtend.class);
						try {
							BeanUtils.copyEntity(nrr, invExtend);
						} catch (Exception e) {
							throw new OriginalBusinessException("数据处理异常，请重新码托处理！");
						}
						nrr.setId(null);
						nrr.setPallet(palletNo);
						nrr.setAllocatedQuantityBU(0D);
						Double moveQty = PackageUtils.convertBUQuantity(palletQty, inventory.getPackageUnit());
						wmsInventoryManager.addInventoryExtend(inventory, nrr, moveQty);
						invExtend.removeQuantity(moveQty);
						commonDao.store(invExtend);
						palletQty = 0D;
					}
					if (palletQty.doubleValue() <= 0) {
						break;
					}
				}
			}
		}
	}
	
	public void receiveConfirm(WmsASN wmsASN) {
//		List<WmsReceivedRecord> records = this.getUnVerifiedRecordByASN(wmsASN.getId());
//		
//		if (records != null && !records.isEmpty()) {
//			// 汇总收货记录, 统一进行码托规则调用
//			for (WmsReceivedRecord record : records) {
//				//对收货记录进行库存登记
//				wmsInventoryManager.receive(record);
//				// 更新收货记录状态
//				record.setBeVerified(Boolean.TRUE);
//			}
//		}
		autoPallet(wmsASN);
		autoCreateMoveDocByResults(wmsASN);
	}
	
	
	private List<WmsInventory> getReceiveInventoryBySoi(String soi) {
		String hql = " FROM WmsInventory inv WHERE inv.location.type = 'RECEIVE' AND inv.itemKey.lotInfo.soi = :soi AND inv.quantityBU - inv.allocatedQuantityBU > 0";
		return commonDao.findByQuery(hql, "soi", soi);
	}
	
	/**
	 * 自动调用规则创建上架计划
	 * @param asn
	 * @param recordTemps
	 */
	private WmsMoveDoc autoCreateMoveDocByResults(WmsASN asn) {
		WmsMoveDoc moveDoc = wmsMoveDocManager.createWmsMoveDoc(asn);
		commonDao.store(moveDoc);
		
		List<WmsInventory> inventorys = getReceiveInventoryBySoi(asn.getCode());
		for(WmsInventory inventory : inventorys) {
			wmsMoveDocManager.createMoveDocDetail(moveDoc, inventory);
		}
		// 如果当前上架计划明细数为0，则不创建上架计划
		if (moveDoc.getDetails().size() == 0) {
			commonDao.delete(moveDoc);
			return null;
		}
		else{
			workflowManager.doWorkflow(moveDoc, "wmsMoveDocProcess.new");
			workflowManager.doWorkflow(asn, "wmsASNPutawayProcess.createMoveDoc");
		}
		return moveDoc;
	}
	
	public RowData getReceiveLocationObj(Map map) {
		WmsASNDetail detail = commonDao.load(WmsASNDetail.class, (Long) ((List) map.get("parentIds")).get(0));
		
		WmsBooking booking = detail.getBooking();
		if(booking == null){
			return null;
		}
		WmsDock dock = booking.getDock();
		
		
		RowData rd = new RowData();
		
		WmsLocation receiveLocation = commonDao.load(WmsLocation.class, dock.getReceiveLocationId());
		
		rd.addColumnValue(receiveLocation.getId());
		rd.addColumnValue(receiveLocation.getCode());
		
		return rd;
	}
	
	public Double getUnReceivedQuantity(Map map) {
		WmsASNDetail asnDetail = commonDao.load(WmsASNDetail.class, (Long) ((List) map.get("parentIds")).get(0));
		WmsPackageUnit packageUnit = load(WmsPackageUnit.class,asnDetail.getPackageUnit().getId());
		if(packageUnit.getLineNo() == 1){
			return asnDetail.getUnReceivedQtyBU();
		}
		WmsItem item = load(WmsItem.class,packageUnit.getItem().getId());
		return  PackageUtils.convertPackQuantity(asnDetail.getUnReceivedQtyBU(), packageUnit.getConvertFigure(),item.getPrecision()); 
	}
	
	public RowData getItemByASNDetail(Map map) {
		WmsASNDetail detail = commonDao.load(WmsASNDetail.class, (Long) map.get("parentId"));

		RowData rd = new RowData();
		
		rd.addColumnValue(detail.getItem().getId());
		rd.addColumnValue(detail.getItem().getCode());
		rd.addColumnValue(detail.getItem().getName());
		
		return rd;
	}
	
	public RowData getPackageUnitByASNDetail(Map map) {
		WmsASNDetail detail = commonDao.load(WmsASNDetail.class, (Long) map.get("parentId"));

		RowData rd = new RowData();
		
		rd.addColumnValue(detail.getPackageUnit().getId());
		rd.addColumnValue(detail.getPackageUnit().getLineNo());
		rd.addColumnValue(detail.getPackageUnit().getUnit());
			
		return rd;
	}

	public Map getLotInfoByASNDetail(Map param) {
		WmsASNDetail detail = commonDao.load(WmsASNDetail.class, (Long) param.get("parentId"));

		Map map = new HashMap();
		
		map.put("lotInfo.soi", detail.getLotInfo().getSoi());
		
		RowData rd = new RowData();
		
		if (detail.getLotInfo().getSupplier() != null) {
			rd.addColumnValue(detail.getLotInfo().getSupplier().getId());
			rd.addColumnValue(detail.getLotInfo().getSupplier().getCode());
			rd.addColumnValue(detail.getLotInfo().getSupplier().getName());
			map.put("lotInfo.supplier.id", rd);
		} else {
			map.put("lotInfo.supplier.id", null);
		}
		
		map.put("lotInfo.extendPropC1", detail.getLotInfo().getExtendPropC1());
		map.put("lotInfo.extendPropC2", detail.getLotInfo().getExtendPropC2());
		map.put("lotInfo.extendPropC3", detail.getLotInfo().getExtendPropC3());
		map.put("lotInfo.extendPropC4", detail.getLotInfo().getExtendPropC4());
		map.put("lotInfo.extendPropC5", detail.getLotInfo().getExtendPropC5());
		map.put("lotInfo.extendPropC6", detail.getLotInfo().getExtendPropC6());
		map.put("lotInfo.extendPropC7", detail.getLotInfo().getExtendPropC7());
		map.put("lotInfo.extendPropC8", detail.getLotInfo().getExtendPropC8());
		map.put("lotInfo.extendPropC9", detail.getLotInfo().getExtendPropC9());
		map.put("lotInfo.extendPropC10", detail.getLotInfo().getExtendPropC10());
		map.put("lotInfo.extendPropC11", detail.getLotInfo().getExtendPropC11());
		map.put("lotInfo.extendPropC12", detail.getLotInfo().getExtendPropC12());
		map.put("lotInfo.extendPropC13", detail.getLotInfo().getExtendPropC13());
		map.put("lotInfo.extendPropC14", detail.getLotInfo().getExtendPropC14());
		map.put("lotInfo.extendPropC15", detail.getLotInfo().getExtendPropC15());
		map.put("lotInfo.extendPropC16", detail.getLotInfo().getExtendPropC16());
		map.put("lotInfo.extendPropC17", detail.getLotInfo().getExtendPropC17());
		map.put("lotInfo.extendPropC18", detail.getLotInfo().getExtendPropC18());
		map.put("lotInfo.extendPropC19", detail.getLotInfo().getExtendPropC19());
		map.put("lotInfo.extendPropC20", detail.getLotInfo().getExtendPropC20());
		
		return map;
	}
	
	public List<WmsReceivedRecord> getUnVerifiedRecordByASN(Long asnId) {
		List<WmsReceivedRecord> records = new ArrayList<WmsReceivedRecord>();
		
		String query = "FROM WmsReceivedRecord record WHERE record.asn.id = :asnId " +
				"AND record.beVerified = false";
		
		records = commonDao.findByQuery(query, new String[]{"asnId"}, new Object[]{asnId});
		
		return records;
	}
	
	public void storeASN(WmsASN asn) {
		asn.setCompany(asn.getBillType().getCompany());
		if(asn.isNew()) {
			asn.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
			asn.setCode(codeManager.generateCodeByRule(asn.getWarehouse(), asn.getCompany().getName(), "ASN", asn.getBillType().getName()));
			asn.setConfirmAccount(Boolean.FALSE);//过账确认初始化为否
			workflowManager.doWorkflow(asn, "wmsASNProcess.new");
		}
		if(!asn.getBillType().getCompany().getId().equals(asn.getCompany().getId())){
			throw new BusinessException("单据类型与货主不匹配");
		}
	}
	
	public boolean asnQuality(WmsASN asn){
		asn = load(WmsASN.class,asn.getId());
		
		Set<String> qualityQniqueKeys = new HashSet<String>();
		Boolean isQuality = false;
		for(WmsASNDetail detail : asn.getDetails()){
			Map<String,Object> problem = new HashMap<String, Object>();
			problem.put("货主", asn.getCompany().getName());
			problem.put("单据类型", asn.getBillType().getName());
			problem.put("货品分类", detail.getItem().getClass1());
			problem.put("供应商", detail.getLotInfo().getSupplier() == null ? null : detail.getLotInfo().getSupplier().getName());
			Map<String, Object> result = wmsRuleManager.execute(asn.getWarehouse().getName(), asn.getCompany().getName(), "收货质检规则", problem);
			int  qualityRate = 0;
			if(result.get("质检频率") != null){
				qualityRate = Integer.valueOf(result.get("质检频率").toString());
			}
			if(qualityRate == 0){
				isQuality = false;
			}
			else if(qualityRate == 1){
				isQuality = true;
				detail.setBeQuality(true);
			}
			else{
				String quality = (String)result.get("质检条件");
				String uniqueKey = BeanUtils.getFormat(quality);
				if(qualityQniqueKeys.contains(uniqueKey)){
					detail.setBeQuality(true);
					continue;
				}
				WmsASNQuality asnQuality = getWmsASNQuality(uniqueKey);
				if(asnQuality == null){
					asnQuality = new WmsASNQuality(WmsWarehouseHolder.getWmsWarehouse(),uniqueKey);
				}
				if(asnQuality.getQualityRate() >= qualityRate){
					asnQuality.setQualityRate(0);
				}
				boolean beQuality = asnQuality.getQualityRate() == 0;
				if(beQuality){
					detail.setBeQuality(true);
					qualityQniqueKeys.add(uniqueKey);
				}
				asnQuality.addQualityRate();
				commonDao.store(asnQuality);
			}
			if(!isQuality){
				isQuality = detail.isBeQuality();
			}
		}
		return isQuality;
	}
	
	private WmsASNQuality getWmsASNQuality(String uniqueKey){
		String hql = "FROM WmsASNQuality waq WHERE waq.warehouse.id = :warehouseId AND waq.uniqueKey = :uniqueKey";
		return (WmsASNQuality)commonDao.findByQueryUniqueResult(hql, new String[]{"warehouseId","uniqueKey"},
				new Object[]{WmsWarehouseHolder.getWmsWarehouse().getId(),uniqueKey});
	}
	
	
	public boolean isExistUnQuality(WmsASN asn){
		Long unQualityCount = (Long)commonDao.findByQueryUniqueResult("SELECT count(*) FROM WmsInventory inv WHERE " +
				"inv.status = :status AND inv.itemKey.lotInfo.soi = :soi AND inv.quantityBU - inv.allocatedQuantityBU > 0",
				new String[]{"status","soi"},
				new Object[]{WmsASNQualityStauts.UNQUALITY_KEY, asn.getCode()});
		return unQualityCount == null || unQualityCount.longValue() == 0;
	}
	
	public String getMaxLineNoByASNDetail(Map param) {
		Integer lineNo = (Integer) commonDao.findByQueryUniqueResult("SELECT MAX(detail.lineNo) FROM WmsASNDetail detail WHERE detail.asn.id = :asnId", 
				new String[] {"asnId"}, new Object[] {(Long) param.get("asn.id")});
		if (lineNo == null || lineNo.intValue() == 0) {
			lineNo = 10;
		} else {
			lineNo += 10;
		}

		return ""+lineNo;
	}

	@SuppressWarnings("rawtypes")
	public Map trayTagPrint(WmsASN wmsAsn, Long printNumber) {
		Map result = new HashMap();
		Map<Long,String> reportValue = new HashMap<Long, String>();
		List<String> records = getPalletByASN(wmsAsn);
		int i = 0;		
		for(String pallet : records){
			if(org.apache.commons.lang.StringUtils.isEmpty(pallet) || BaseStatus.NULLVALUE.equals(pallet)){
				continue;
			}
			String str = ";pallet=" +pallet;			
			reportValue.put(new Long(i++), "wmsPallet.raq&raqParams=" + str);
		}
		if(!reportValue.isEmpty()){
			result.put(IPage.REPORT_VALUES, reportValue);
			result.put(IPage.REPORT_PRINT_NUM, printNumber.intValue());
		}
		return result;		
	}
	private List<String> getPalletByASN(WmsASN wmsAsn) {
		String hql = "SELECT invExtend.pallet FROM WmsInventoryExtend invExtend WHERE invExtend.inventory.itemKey.lotInfo.soi = :soi AND invExtend.pallet is not null AND invExtend.pallet != '-' AND invExtend.quantityBU  > 0 GROUP BY invExtend.pallet";
		return commonDao.findByQuery(hql, "soi", wmsAsn.getCode());
	}
	
	public void qualitySave(WmsInventoryExtend wsn, String status,
			String packageUnit, Double num) {
		WmsInventory inventory = wsn.getInventory();
		WmsItemState itemState = commonDao.load(WmsItemState.class, (Long.parseLong(status)));
		WmsPackageUnit pack = commonDao.load(WmsPackageUnit.class, (Long.parseLong(packageUnit)));
		String soi = inventory.getItemKey().getLotInfo().getSoi();
		WmsASN asn = getAsnBySoi(soi);
		
		String newStatus = itemState.getName();
		
		Double quantityBU = PackageUtils.convertBUQuantity(num, pack);
		
		if(wsn.getQuantityBU() < quantityBU) {
			throw new BusinessException("数量不能大于库存数量！");
		}
		
//		if (inventory.getAllocatedQuantityBU() > quantityBU || inventory.getPutawayQuantityBU() > quantityBU) {
//			throw new BusinessException("数量不能大于拣货分配数量或上架数量！");
//		}
		
		//对inventory进行拆分
		WmsInventory desInv = wmsInventoryManager.getInventoryWithNew(inventory.getLocation(), 
				inventory.getItemKey(), inventory.getPackageUnit(), newStatus);
		desInv.addQuantityBU(quantityBU);
		
		wmsInventoryManager.addInventoryLog(WmsInventoryLogType.QUALITY, 1, soi, asn==null?null:asn.getBillType(), desInv.getLocation(), 
				desInv.getItemKey(), quantityBU, desInv.getPackageUnit(), inventory.getStatus(), "质检登记-库存增加");
		inventory.removeQuantityBU(quantityBU);
		wmsInventoryManager.addInventoryLog(WmsInventoryLogType.QUALITY, -1, soi, asn==null?null:asn.getBillType(), inventory.getLocation(), 
				inventory.getItemKey(), quantityBU, inventory.getPackageUnit(), inventory.getStatus(), "质检登记-库存减少");
		
		//对serialNo进行拆分
		WmsInventoryExtend desWsn = wmsInventoryManager.addInventoryExtend(desInv, wsn, quantityBU);
		wsn.removeQuantity(quantityBU);
		if (DoubleUtils.compareByPrecision(wsn.getQuantityBU(), 0D, wsn.getInventory().getPackageUnit().getPrecision()) == 0) {
			commonDao.delete(wsn);
		}
	}
	
	private WmsASN getAsnBySoi(String soi) {
		String hql = " FROM WmsASN asn WHERE asn.code = :soi";
		List<WmsASN> wmsAsn = commonDao.findByQuery(hql, "soi", soi);
		if(wmsAsn.size()>0) {
			return wmsAsn.get(0);
		} else {
			return null;
		}
	}

	public void qualityDamage(WmsASN wmsAsn, WmsInventoryExtend wsn,
			List<String> adjustQuantityBUList) {
		checkPutawayMoveDoc(wmsAsn);
		qualitySingle(wmsAsn, wsn, adjustQuantityBUList, WmsASNQualityStauts.QUALITY_DAMAGE_KEY);
		workflowManager.doWorkflow(wmsAsn, "wmsASNQualityProcess.qualityDamage");
	}

	public void qualitySingleFail(WmsASN wmsAsn, WmsInventoryExtend wsn,
			List<String> adjustQuantityBUList) {
		checkPutawayMoveDoc(wmsAsn);
		qualitySingle(wmsAsn, wsn, adjustQuantityBUList, WmsASNQualityStauts.QUALITY_FAIL_KEY);
		workflowManager.doWorkflow(wmsAsn, "wmsASNQualityProcess.qualityFail");
		
	}

	public void qualityAllFail(WmsASN wmsAsn) {
		checkPutawayMoveDoc(wmsAsn);
		List<WmsInventory> inventoryList = commonDao.findByQuery(" FROM WmsInventory inv WHERE " +
				"inv.status = :status AND inv.itemKey.lotInfo.soi = :soi AND inv.quantityBU - inv.allocatedQuantityBU > 0",
				new String[]{"status","soi"},
				new Object[]{WmsASNQualityStauts.UNQUALITY_KEY, wmsAsn.getCode()});
		for (WmsInventory wmsInventory : inventoryList) {
			wmsInventory.setStatus(WmsASNQualityStauts.QUALITY_FAIL_KEY);
		}
		workflowManager.doWorkflow(wmsAsn, "wmsASNQualityProcess.qualityFail");
	}
	
	
	protected void checkPutawayMoveDoc(WmsASN asn){
		List<String> status = new ArrayList<String>();
		status.add(WmsMoveDocStatus.OPEN);
		status.add(WmsMoveDocStatus.PARTALLOCATED);
		status.add(WmsMoveDocStatus.ALLOCATED);
		status.add(WmsMoveDocStatus.ACTIVE);
		
		Long unFinishPutawayMoveDocCount = (Long)commonDao.findByQueryUniqueResult("SELECT count(*) FROM WmsMoveDoc moveDoc WHERE " +
			"moveDoc.asn.id = :asnId AND moveDoc.type = :type AND moveDoc.status in (:status)",
			new String[]{"asnId","type","status"},
			new Object[]{asn.getId(),WmsMoveDocType.MV_PUTAWAY,status});
		if (unFinishPutawayMoveDocCount > 0) {
			throw new BusinessException("存在未处理完成的上架单，请先处理后在进行质检！");
		}
	}

	public void qualitySuccess(WmsASN wmsAsn) {
		checkPutawayMoveDoc(wmsAsn);
		List<WmsInventory> inventoryList = commonDao.findByQuery(" FROM WmsInventory inv WHERE " +
				"inv.status = :status AND inv.itemKey.lotInfo.soi = :soi AND inv.quantityBU - inv.allocatedQuantityBU > 0",
				new String[]{"status","soi"},
				new Object[]{WmsASNQualityStauts.UNQUALITY_KEY, wmsAsn.getCode()});
		for (WmsInventory wmsInventory : inventoryList) {
			wmsInventory.setStatus(BaseStatus.NULLVALUE);
		}
		workflowManager.doWorkflow(wmsAsn, "wmsASNQualityProcess.qualitySuccess");
		
	}
	
	public void qualitySingle(WmsASN wmsAsn, WmsInventoryExtend wsn,
			List<String> adjustQuantityBUList, String status) {
		checkPutawayMoveDoc(wmsAsn);
		Double adjustQuantityBU;
		try {
			adjustQuantityBU = Double.valueOf(adjustQuantityBUList.get(0));
		} catch (NumberFormatException nfe) {
			throw new BusinessException("数量只能为数值类型！");
		}
		if(adjustQuantityBU <= 0) {
			throw new BusinessException("数量必须为正整数！");
		}
		if (adjustQuantityBU.doubleValue() > wsn.getQuantityBU().doubleValue()) {
			throw new BusinessException("数量不能大于原库存数量！");
		}
		
		WmsInventory inventory = wsn.getInventory();

		String soi = inventory.getItemKey().getLotInfo().getSoi();
		
		//对inventory进行拆分
		WmsInventory desInv = wmsInventoryManager.getInventoryWithNew(inventory.getLocation(), 
				inventory.getItemKey(), inventory.getPackageUnit(), status);
		desInv.addQuantityBU(adjustQuantityBU);
		
		wmsInventoryManager.addInventoryLog(WmsInventoryLogType.QUALITY, 1, soi, wmsAsn.getBillType(), desInv.getLocation(), 
				desInv.getItemKey(), adjustQuantityBU, desInv.getPackageUnit(), status, status+"-库存增加");
		inventory.removeQuantityBU(adjustQuantityBU);
		wmsInventoryManager.addInventoryLog(WmsInventoryLogType.QUALITY, -1, soi, wmsAsn.getBillType(), inventory.getLocation(), 
				inventory.getItemKey(), adjustQuantityBU, inventory.getPackageUnit(), inventory.getStatus(), status+"-库存减少");
		
		//对serialNo进行拆分
		WmsInventoryExtend desWsn = wmsInventoryManager.addInventoryExtend(desInv, wsn, adjustQuantityBU);
		wsn.removeQuantity(adjustQuantityBU);
		if (DoubleUtils.compareByPrecision(wsn.getQuantityBU(), 0D, wsn.getInventory().getPackageUnit().getPrecision()) == 0) {
			commonDao.delete(wsn);
		}
		workflowManager.doWorkflow(wmsAsn, "wmsASNQualityProcess.qualityDamage");
	}

	public void unActive(WmsASN wmsAsn){
		/*Set<String> qualityQniqueKeys = new HashSet<String>();
		for(WmsASNDetail detail : wmsAsn.getDetails()){
			detail.setBeQuality(false);
			Map<String,Object> problem = new HashMap<String, Object>();
			problem.put("货主", wmsAsn.getCompany().getName());
			problem.put("单据类型", wmsAsn.getBillType().getName());
			problem.put("货品分类", detail.getItem().getClass1());
			problem.put("供应商", detail.getLotInfo().getSupplier() == null ? null : detail.getLotInfo().getSupplier().getName());
			Map<String, Object> result = wmsRuleManager.execute(wmsAsn.getWarehouse().getName(), wmsAsn.getCompany().getName(), "收货质检规则", problem);
			int  qualityRate = 0;
			if(result.get("质检频率") != null){
				qualityRate = Integer.valueOf(result.get("质检频率").toString());
			}
			commonDao.store(detail);
			if(qualityRate > 1){
				String quality = (String)result.get("质检条件");
				String uniqueKey = BeanUtils.getFormat(quality);
				if(qualityQniqueKeys.contains(uniqueKey)){
					continue;
				}
				WmsASNQuality asnQuality = getWmsASNQuality(uniqueKey);
				if(asnQuality != null){
					asnQuality.cutQualityRate();
					qualityQniqueKeys.add(uniqueKey);
				}
				commonDao.store(asnQuality);
			}
		}
		wmsAsn.setQualityStauts(WmsASNQualityStauts.NOQUALITY);*/
//		if(!WmsASNQualityStauts.NOQUALITY.equals(wmsAsn.getQualityStauts())){
//			workflowManager.doWorkflow(wmsAsn, "wmsASNQualityProcess.qualitySuccess");
//		}
		/**=============================JAC=====================*/
		commonDao.executeByHql("DELETE FROM JacPalletSerial jps WHERE jps.asnDetail.id IN("
				+ "SELECT detail.id FROM WmsASNDetail detail WHERE detail.asn.id =:asnId)", 
				new String[]{"asnId"},new Object[]{wmsAsn.getId()});
		
	}
	
	public void active(WmsASN wmsAsn) {
		/*
		WmsDock dock = null;
		for (WmsASNDetail detail : asn.getDetails()) {
			WmsBooking booking = detail.getBooking();
			if(booking != null){
				dock = booking.getDock();
			}
			if(dock == null) {
				throw new OriginalBusinessException("未预约收货月台,请预约！");
			}
		}
		workflowManager.doWorkflow(asn, "wmsASNQualityProcess.new");*/
		/**=============================JAC=====================*/
		WmsASN asn = commonDao.load(WmsASN.class, wmsAsn.getId());
		for (WmsASNDetail detail : asn.getDetails()) {
			if(!detail.getIsSupport()){//不码托的视为散件,后面要么手工入库要么人工组托之后自动分配
				detail.setPalletNo(1);
				commonDao.store(detail);
			}
//			for(int i =0 ; i<detail.getPalletNo(); i++){
//				JacPalletSerial jps = EntityFactory.getEntity(JacPalletSerial.class);
//				jps.setAsnDetail(detail);
//				jps.setPalletNo(detail.getId()+"_"+(i+1));
//				commonDao.store(jps);
//			}
		}
	}
	public void activeBySuppier(WmsASN wmsAsn){
		String warehouseName = WmsWarehouseHolder.getWmsWarehouse().getName();
		String suppierCode = wmsAsn.getSupplier().getCode();
		Double safeQuantity = 0D,palletNo = 0D;
		Object value = wmsRuleManager.getSingleRuleTableDetail(warehouseName,
				"R102_供应商供货量规则表","上限",suppierCode);
		if(value == null){
			throw new OriginalBusinessException("R102_供应商供货量规则表:未找到信息["+suppierCode+"]");
		}
		safeQuantity = Double.valueOf(value.toString());
		//统计目前安全库存：所有在库,还没签收,状态是合格和待检
		String safeInventory = "SELECT wsn.pallet FROM WmsInventoryExtend wsn"
				+ " LEFT JOIN wsn.inventory inventory WHERE 1=1"
				+ " AND inventory.itemKey.lotInfo.supplier =:supplier"
				//+ " AND (inventory.status ='-' OR inventory.status ='待检')"
				+ " AND inventory.quantityBU>0"
				+ " AND wsn.pallet IS NOT NULL AND (wsn.pallet>'-' OR wsn.pallet<'-')"
				+ " GROUP BY wsn.pallet";// AND inventory.location.type IN ('STORAGE')
		List<String> pallets = commonDao.findByQuery(safeInventory, new String[]{"supplier"}, 
				new Object[]{wmsAsn.getSupplier()});
		Double inWarehouseQty = (pallets==null||pallets.size()<=0)?0D:pallets.size();
		inWarehouseQty = inWarehouseQty==null?0D:inWarehouseQty;
		for (WmsASNDetail detail : wmsAsn.getDetails()) {
			palletNo += detail.getPalletNo();
		}
		inWarehouseQty += palletNo;//wmsAsn.getExpectedQuantityBU();
		if(inWarehouseQty > safeQuantity){//理论在库量>=安全库存
			throw new BusinessException("供应商["+suppierCode+"]供货上限值:"+safeQuantity+",已超出:"+(inWarehouseQty-safeQuantity));
		}
		
		/*String key1 = null,key2 = null;
		 for (WmsASNDetail detail : wmsAsn.getDetails()) {
			key1 = suppierCode+MyUtils.spilt1+detail.getItem().getCode();
			List<Map<String, Object>> list = wmsRuleManager.getAllRuleTableDetail(warehouseName,
					"R102_料号级安全库存规则表",wmsAsn.getCompany().getName());
			for(Map<String, Object> obj : list){
				key2 = obj.get("供应商编码")+MyUtils.spilt1+obj.get("物料编码");
				if(key1.equals(key2)){
					safeQuantity = Double.valueOf(obj.get("ASN激活库存上限").toString());
					break;
				}
			}
			
//			Object value = wmsRuleManager.getSingleRuleTableDetail(warehouseName,
//					"R102_料号级安全库存规则表","安全库存",suppierCode,detail.getItem().getCode());
//			if(value == null){
//				throw new OriginalBusinessException("R102_料号级安全库存规则表:未找到物料["+detail.getItem().getCode()+"]");
//			}
//			safeQuantity = Double.valueOf(value.toString());
			if(safeQuantity==0){
				throw new OriginalBusinessException("R102_料号级安全库存规则表:未找到物料["+detail.getItem().getCode()+"]");
			}
			//统计目前安全库存：所有在库,还没签收,状态是合格和待检
			String safeInventory = "SELECT SUM(inventory.quantityBU) FROM WmsInventory inventory WHERE 1=1"
					+ " AND inventory.itemKey.item =:item"
					+ " AND inventory.itemKey.lotInfo.supplier =:supplier"
					+ " AND (inventory.status ='-' OR inventory.status ='待检')";
			Double inWarehouseQty = (Double) commonDao.findByQueryUniqueResult(safeInventory, 
					new String[]{"item","supplier"}, 
					new Object[]{detail.getItem(),wmsAsn.getSupplier()});
			inWarehouseQty = inWarehouseQty==null?0D:inWarehouseQty;
			inWarehouseQty += detail.getExpectedQuantityBU();
			if(inWarehouseQty > safeQuantity){//理论在库量>=安全库存
				throw new OriginalBusinessException("物料["+detail.getItem().getCode()+"]安全库存最大值:"+safeQuantity+",已超出:"+(inWarehouseQty-safeQuantity));
			}
		}*/
		active(wmsAsn);
		workflowManager.doWorkflow(wmsAsn, "wmsASNProcess.active");
	}
	public String getPalletSerialAuto(Map map){
		String pallet = null;
		try {
			Object[] obj = (Object[]) map.get("maintainWmsASNPage.ids");
			pallet = obj[0]+"_"+JavaTools.format(new Date(), JavaTools.HMS)+"_"+JavaTools.getRandomUtils(5);
		} catch (Exception e) {
			pallet = JavaTools.format(new Date(), JavaTools.yymd)+"_"+JavaTools.format(new Date(), JavaTools.HMS)+"_"+JavaTools.getRandomUtils(5);
		}
		return pallet;
	}
	public void palletSerialAuto(String palletNo,WmsASNDetail detail){
		if(!org.apache.commons.lang.StringUtils.isEmpty(detail.getPallet())){
			throw new BusinessException("所选明细已参与码托,不可重复码托[第"+detail.getLineNo()+"行]");
		}
		//生成托盘
		JacPalletSerial jps = EntityFactory.getEntity(JacPalletSerial.class);
		jps.setAsnDetail(detail);
		jps.setPalletNo(palletNo);
		commonDao.store(jps);
		//码托
		palletSerial(jps, detail.getReceivedQuantityBU());
		detail.setPalletNo(1);
		detail.setPallet(palletNo);
		commonDao.store(detail);
	}
	public void palletSerial(JacPalletSerial jps,List<String> values){
		palletSerial(jps, Double.valueOf(values.get(0)));
	}
	private void palletSerial(JacPalletSerial jps,Double expectedQuantityBU){
		jps.setExpectedQuantityBU(expectedQuantityBU);
		commonDao.store(jps);
		//判断当前码托总量是否在总收货量范围内
		Double receiveQty = (Double) commonDao.findByQueryUniqueResult("SELECT SUM(rec.receivedQuantityBU) FROM WmsReceivedRecord rec"
				+ " WHERE rec.asnDetail.id =:asnDetailId", 
				new String[]{"asnDetailId"}, new Object[]{jps.getAsnDetail().getId()});
		receiveQty = receiveQty==null?0D:receiveQty;
		Double palletQty = (Double) commonDao.findByQueryUniqueResult("SELECT SUM(jps.expectedQuantityBU) FROM JacPalletSerial jps"
				+ " WHERE jps.asnDetail.id =:asnDetailId", 
				new String[]{"asnDetailId"}, new Object[]{jps.getAsnDetail().getId()});
		palletQty = palletQty==null?0D:palletQty;
		if(receiveQty<palletQty){
			throw new BusinessException("码托量超过总收货量:"+(receiveQty-palletQty));
		}
		Long count = (Long)this.commonDao.findByQueryUniqueResult("SELECT count(jps.expectedQuantityBU) FROM JacPalletSerial jps"
				+ " WHERE jps.asnDetail.id =:asnDetailId and jps.expectedQuantityBU = 0 ", 
				new String[]{"asnDetailId"}, new Object[]{jps.getAsnDetail().getId()});
		if(count == 0){
			if(DoubleUtils.compareByPrecision(receiveQty, palletQty, 1) != 0){
				throw new BusinessException("码托总量不等于总收货量");
			}
			
		}
	}
	public String palletAuto(Long asnId){
		String message = "1";
		WmsASN wmsAsn = commonDao.load(WmsASN.class, asnId);
		String warehouseName = WmsWarehouseHolder.getWmsWarehouse().getName();
		String suppierCode = wmsAsn.getSupplier().getCode();
		
		//将ASN下的托盘信息按照明细汇总
		List<Object[]> jpsObjs = commonDao.findByQuery("SELECT jps.asnDetail.id,jps.id FROM JacPalletSerial jps WHERE jps.asnDetail.id IN("
				+ "SELECT detail.id FROM WmsASNDetail detail WHERE detail.asn.id =:asnId)"
				+ " AND jps.expectedQuantityBU<=0 AND jps.toLocationId IS NULL", 
				new String[]{"asnId"},new Object[]{wmsAsn.getId()});
		Map<Long,List<Long>> jpsMap = getJpsMap(wmsAsn,jpsObjs);
		
		List<Long> ddids = commonDao.findByQuery("SELECT d.id FROM WmsASNDetail d WHERE d.asn.id =:asnId and d.palletNo = 1", 
				new String[]{"asnId"}, new Object[]{asnId});
		//找到托盘个数是1的明细
		for(Long id : ddids){
			WmsASNDetail detail = commonDao.load(WmsASNDetail.class, id);
			Double totalQty = detail.getExpectedQuantityBU();
			List<Long> jpsIds = jpsMap.get(id);
			for(int i = 0; i < jpsIds.size(); i++){
				JacPalletSerial jps = commonDao.load(JacPalletSerial.class, jpsIds.get(i));
				jps.setExpectedQuantityBU(totalQty);
				commonDao.store(jps);
			}
		}
		//剩余的按照原有逻辑执行
		ddids = commonDao.findByQuery("SELECT d.id FROM WmsASNDetail d WHERE d.asn.id =:asnId and d.palletNo > 1", 
				new String[]{"asnId"}, new Object[]{asnId});
		for(Long id : ddids){
			WmsASNDetail detail = commonDao.load(WmsASNDetail.class, id);
			
			Object value = wmsRuleManager.getSingleRuleTableDetail(warehouseName,
					"R102_供应商码托参数规则表","满托量",suppierCode,detail.getItem().getCode());
			if(value == null){
				message = "存在非标托";
				continue;
			}
			int size = JavaTools.getSize(detail.getExpectedQuantityBU().intValue(), Double.valueOf(value.toString()).intValue());
			if(size>detail.getPalletNo() || size < detail.getPalletNo()){
				message = "托盘数和指定量不符";
				continue;
			}
			Double totalQty = detail.getExpectedQuantityBU();
			Double palletMaxQty = Double.valueOf(value.toString());
			Double palletQty = 0D;
			
			List<Long> jpsIds = jpsMap.get(id);
			for(int i = 0; i < jpsIds.size(); i++){
				if(palletMaxQty<=totalQty){
					palletQty = palletMaxQty;
				}else{
					palletQty = totalQty;
				}
				JacPalletSerial jps = commonDao.load(JacPalletSerial.class, jpsIds.get(i));
				jps.setExpectedQuantityBU(palletQty);
				commonDao.store(jps);
				totalQty -= palletQty;
			}
		}
		return message;
	}
	public void saveAsnFile(WmsOrganization company,Long billTypeId,Map<String,List<Object[]>> csMap,Map<String,WmsASN> asns){
		WmsBillType billType = commonDao.load(WmsBillType.class, billTypeId);
		Iterator<Entry<String, List<Object[]>>> ii = csMap.entrySet().iterator();
		String key = null;
		while(ii.hasNext()){
			Entry<String, List<Object[]>> entry = ii.next();
			key = entry.getKey();
			List<Object[]> objs = entry.getValue();
			WmsASN asn = null;
			Map<String,Long> detailM = null;
			if(asns.containsKey(key)){
				asn = asns.get(key);
				List<Object[]> details = commonDao.findByQuery("SELECT asnDetail.lineNo,asnDetail.item.id,asnDetail.id"
						+ " FROM WmsASNDetail asnDetail WHERE asnDetail.asn.id =:asnId", 
						new String[]{"asnId"}, new Object[]{asn.getId()});
				if(details!=null && details.size()>0){
					detailM = new HashMap<String, Long>();
					for(Object[] o : details){
						detailM.put(o[0]+MyUtils.spilt1+o[1], Long.parseLong(o[2].toString()));
					}
				}
				
			}else{
				asn = EntityFactory.getEntity(WmsASN.class);
				WmsOrganization supplier = (WmsOrganization) objs.get(0)[1];
				Date orderDate = (Date) objs.get(0)[2];
				Date estimateDate = (Date) objs.get(0)[3];
				asn.setCompany(company);
				asn.setSupplier(supplier);
				asn.setBillType(billType);
				asn.setOrderDate(orderDate);
				asn.setEstimateDate(estimateDate);
				this.storeASN(asn);
			}
			//
			int lineNo = 1;
			WmsItem item = null;
			WmsPackageUnit packageUnit = null;
			String notes = null;
			Double quantity = 0D;
			String extendPropC1 = null;
			for(Object[] obj : objs){
				item = (WmsItem) obj[4];
				notes = obj[5]==null?null:obj[5].toString();
				quantity = Double.valueOf(obj[6].toString());
				packageUnit = (WmsPackageUnit) obj[10];
				extendPropC1 = obj[7].toString();
				
				WmsASNDetail detail = null;
				if(detailM!=null && detailM.containsKey(lineNo+MyUtils.spilt1+item.getId())){
					detail = commonDao.load(WmsASNDetail.class, detailM.get(lineNo+MyUtils.spilt1+item.getId()));
					packageUnit = commonDao.load(WmsPackageUnit.class, detail.getPackageUnit().getId());
					if (packageUnit.getLineNo().intValue() == 1) {
						detail.setExpectedQuantity(PackageUtils.convertPackQuantity(quantity, packageUnit)+detail.getExpectedQuantity());
						detail.setExpectedQuantityBU(quantity+detail.getExpectedQuantityBU());
					} else {
						detail.setExpectedQuantity(quantity+detail.getExpectedQuantity());
						detail.setExpectedQuantityBU(PackageUtils.convertBUQuantity(quantity, packageUnit)+detail.getExpectedQuantityBU());
					}
					if(detail.getPalletNo()==null||detail.getPalletNo()<=0){
						detail.setPalletNo(0);
					}
					asn = commonDao.load(WmsASN.class, asn.getId());
					asn.refreshQtyBU();
				}else{
					detail = EntityFactory.getEntity(WmsASNDetail.class);
					detail.setItem(item);
					detail.setDescription(notes);
					detail.setLineNo(lineNo);
					detail.setPackageUnit(packageUnit);
					detail.getLotInfo().setExtendPropC1(extendPropC1);
					addDetail(asn.getId(), detail, quantity);
				}
				commonDao.store(detail);
				lineNo++;
			}
		}
	}
	
	public RowData[] getBillType(Map params){
		String billType = "";
		if(params.containsKey("editWmsASNPage.id")){
			billType = TypeOfBill.RECEIVE;
		}else if(params.containsKey("editCreateMoveDocQualityPage.id")){
			billType = TypeOfBill.QUALITY;
		}else if(params.containsKey("modifyWmsPickTicketPage.id")){
			billType = TypeOfBill.SHIP;
		}
		//先获取货主
		WmsOrganization company = null;
		List<MiddleCompanyExtends> mis = commonDao.findByQuery("FROM MiddleCompanyExtends mi WHERE mi.warehouse.id =:warehouseId",
				new String[]{"warehouseId"}, new Object[]{WmsWarehouseHolder.getWmsWarehouse().getId()});
		if(mis!=null && mis.size()>0){
			company = mis.get(0).getCompany();
		}
		if(company!=null){
			//获取货主对应的单据类型
			List<WmsBillType> bills = commonDao.findByQuery("FROM WmsBillType billType WHERE 1=1"
					+ " AND billType.type =:billType AND billType.status = 'ENABLED' AND billType.company.id =:companyId", 
					new String[]{"billType","companyId"}, new Object[]{billType,company.getId()});
			if(bills!=null && bills.size()>0){
				RowData[] rowDatas = new RowData[bills.size()];
				int index = 0;
				for(WmsBillType bill : bills){
					RowData rowData = new RowData();
					rowData.addColumnValue(bill.getId());
					rowData.addColumnValue(bill.getName());
					rowDatas[index++] = rowData;
				}
				return rowDatas;
			}
		}
		return null;
	}
	public Map printASNReport(WmsASN asn ){
		List<Long> ids = commonDao.findByQuery("SELECT aa.id FROM WmsASNDetail aa WHERE aa.asn.id =:asnId" +
				" ORDER BY aa.item.code", 
				new String[]{"asnId"}, new Object[]{asn.getId()});
		if(ids!=null && ids.size()>0){
			SequenceGenerater ss = (SequenceGenerater) applicationContext.getBean("sequenceGenerater");
			String qualityCode = null;
			for(Long id :ids){
				WmsASNDetail aa = commonDao.load(WmsASNDetail.class, id);
				if(StringUtils.isEmpty(aa.getQualityCode())){
					//编码 = FDJZJ + 格式化日期($现在,"yyMMdd") + 3位流水号
					qualityCode = "FDJZJ"+DateUtils.format(new Date(),"yyMMdd");
					qualityCode = ss.generateSequence(qualityCode, 3);
					aa.setQualityCode(qualityCode);
					commonDao.store(aa);
				}
			}
		}
		asn.setPrintASNReportDate(new Date());
		commonDao.store(asn);
		
        Map result = new HashMap();
		Map<Long,String> reportValue = new HashMap<Long, String>();
		reportValue.put(asn.getId(), "jacASNReport.raq");
		result.put(IPage.REPORT_VALUES, reportValue);
		result.put(IPage.REPORT_PRINT_NUM, 1);
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("asnId", asn.getId());
		params.put("asnReceiveType", "RECEIVE_NORMAL");
		result.put(IPage.REPORT_PARAMS, params);
		return result;
	}
	//将来这块作成异步任务
	public void confirmAccount(WmsASN asn) {
		if(asn.getConfirmAccount()){
			return;
		}
		//回传ERP
		dealInterfaceDataManager.dealAsnData(asn);
		asn.setConfirmAccount(Boolean.TRUE);
		commonDao.store(asn);
		/**创建托盘对象*/
		/*for(WmsASNDetail asnDetail : asn.getDetails()){
			for(int i =0 ; i<asnDetail.getPalletNo(); i++){
				JacPalletSerial jps = EntityFactory.getEntity(JacPalletSerial.class);
				jps.setAsnDetail(asnDetail);
				jps.setPalletNo(asnDetail.getId()+"_"+(i+1));
				commonDao.store(jps);
			}
			
			if(!asnDetail.getIsSupport() && asnDetail.getPalletNo() == 1){
				String hql = "from JacPalletSerial where asnDetail.id=:detailId";
				JacPalletSerial jacPalletSerial = (JacPalletSerial) commonDao.findByQueryUniqueResult(hql,"detailId",asnDetail.getId());
				jacPalletSerial.setExpectedQuantityBU(asnDetail.getReceivedQuantityBU());
				commonDao.store(jacPalletSerial);
			}
			
			*//**更新库存状态*//*
			String inventoryStatus = "";
			Map<String,Object> problem = new HashMap<String, Object>();
			problem.put("供应商",asnDetail.getAsn().getSupplier().getCode());
			problem.put("货品代码", asnDetail.getItem().getCode());
			problem.put("工艺状态", 
					MyUtils.getTypeOfExtendPropC1("TypeOfExtendPropC1."+asnDetail.getLotInfo().getExtendPropC1()));
			problem.put("单据类型", asnDetail.getAsn().getBillType().getCode());
			problem.put("货主代码",asnDetail.getAsn().getCompany().getCode());
			problem.put("待检量",asnDetail.getQualityQuantityBU()==null?0:asnDetail.getQualityQuantityBU());
			Map<String, Object> result = wmsRuleManager.execute(asnDetail.getAsn().getWarehouse().getName(), 
					asnDetail.getAsn().getCompany().getName(), "收货货品状态规则", problem);
			inventoryStatus = result.get("状态").toString();
			
			*//**更新库存以及收货记录里面的库存状态*//*
			String hql = "from WmsReceivedRecord r where r.asnDetail.id=:id";
			List<WmsReceivedRecord> records = commonDao.findByQuery(hql,"id",asnDetail.getId());
			if(records!=null && records.size()>0){
				Long invId= records.get(0).getInventoryId();//库存ID
				records.get(0).setInventoryStatus(inventoryStatus);
				commonDao.store(records.get(0));//
				
				hql = "update WmsInventory set status=:status where id="+invId;
				commonDao.executeByHql(hql, "status", inventoryStatus);
			}
		}*/
	}
	@Override
	public void CheckMt(WmsASNDetail detail,List tableValues) {
		Double qualityQty = 0d;//待检量
		Integer palletNo = 0;//托盘数
		try {
			qualityQty = tableValues.get(0) == null ? 
						0d : Double.valueOf(tableValues.get(0).toString());
			palletNo = tableValues.get(1) == null ? 
						0 : Integer.valueOf(tableValues.get(1).toString());
			if(qualityQty < 0 || palletNo < 1){
				throw new BusinessException("待检量必须大于等于0,托盘数必须大于等于1");
			}
		} catch (Exception e) {
			throw new BusinessException("待检量或托盘数格式有误");
		}
		if(qualityQty > detail.getReceivedQuantityBU()){
			throw new BusinessException("待检量不得大于收货量");
		}
		detail.setIsCheckMT(Boolean.TRUE);
		detail.setQualityQuantityBU(qualityQty);
		detail.setPalletNo(palletNo);
		commonDao.store(detail);
		/**当所有已收货明细的是否送检码托=true,那么把ASN的是否送检码托也要设为TRUE
		 * 当任何收货数量大于0的明细是否码托=false,都不允许ASN是否送检码托为true*/
		for(WmsASNDetail asnDetail : detail.getAsn().getDetails()){
			if((null == asnDetail.getIsCheckMT() || !asnDetail.getIsCheckMT()) && asnDetail.getReceivedQuantityBU()>0){
				return;
			}
		}
		detail.getAsn().setIsCheckMT(Boolean.TRUE);
		commonDao.store(detail.getAsn());
	}
	public void scanAsnConfirm(String asnCode){
		Boolean iserror = false;
		String mesg = "成功";
		asnCode = asnCode==null?"-":asnCode.trim();
		String hql = "FROM WmsASN asn WHERE asn.relatedBill1 =:code";
		WmsASN asn = (WmsASN) commonDao.findByQueryUniqueResult(hql, 
				new String[]{"code"}, new Object[]{asnCode});
		if(asn==null){
			hql = "FROM WmsASN asn WHERE asn.code =:code";
			asn = (WmsASN) commonDao.findByQueryUniqueResult(hql, 
					new String[]{"code"}, new Object[]{asnCode});
			if(asn==null){
				iserror = true;
				mesg = MyUtils.font2("单据号不存在:"+asnCode);
//				throw new BusinessException("单据号不存在:"+asnCode);
			}
		}
		if(iserror){
			LocalizedMessage.setMessage(mesg);
		}else{
			if(asn!=null){
				//统计明细是否全部确认
				Boolean beReceived = true;
				Set<WmsASNDetail> details = asn.getDetails();
				for(WmsASNDetail detail : details){
					beReceived = detail.getBeReceived();
					if(!beReceived){
						iserror = true;
						mesg = MyUtils.font2("明细未全部确认:"+asnCode);
						break;
//						throw new BusinessException("明细未全部确认:"+asnCode);
					}
				}
				if(iserror){
					LocalizedMessage.setMessage(mesg);
				}else{
					if(asn.getStatus().equals(WmsASNStatus.RECEIVED) 
							|| asn.getStatus().equals(WmsASNStatus.RECEIVING)){
						if(asn.getIsPrint()){
							LocalizedMessage.setMessage(MyUtils.font2("重复过账:"+asnCode));
						}else{
							asn.setPrintDate(new Date());//打印上架单时间
							asn.setPrintPerson(UserHolder.getUser().getName());//打印人
							asn.setIsPrint(Boolean.TRUE);//是否打印
							commonDao.store(asn);
							Task task = new Task(HeadType.CONFIRM_ACCOUNT, 
									"wmsDealTaskManager"+MyUtils.spiltDot+"confirmAccount", asn.getId());
							commonDao.store(task);
						}
					}else{
						LocalizedMessage.setMessage(MyUtils.font2("未完成收货:"+asnCode));
					}
				}
			}
		}
	}
	public Map printPutDirect(String asnCode){
		asnCode = asnCode==null?"-":asnCode.trim();
		String hql = "FROM WmsASN asn WHERE asn.relatedBill1 =:code";
		WmsASN asn = (WmsASN) commonDao.findByQueryUniqueResult(hql, 
				new String[]{"code"}, new Object[]{asnCode});
		if(asn==null){
			hql = "FROM WmsASN asn WHERE asn.code =:code";
			asn = (WmsASN) commonDao.findByQueryUniqueResult(hql, 
					new String[]{"code"}, new Object[]{asnCode});
			if(asn==null){
				throw new BusinessException("单据号不存在:"+asnCode);
			}
		}
		Map result = new HashMap();
		if(asn!=null){
			//统计明细是否全部确认
			Boolean beReceived = true;
			Set<WmsASNDetail> details = asn.getDetails();
			for(WmsASNDetail detail : details){
				beReceived = detail.getBeReceived();
				if(!beReceived){
					throw new BusinessException("明细未全部确认:"+asnCode);
				}
			}
			if(asn.getStatus().equals(WmsASNStatus.RECEIVED) 
					|| asn.getStatus().equals(WmsASNStatus.RECEIVING)){
				asn.setPrintDate(new Date());//打印上架单时间
				asn.setPrintPerson(UserHolder.getUser().getName());//打印人
				asn.setIsPrint(Boolean.TRUE);//是否打印
				commonDao.store(asn);
				Task task = new Task(HeadType.CONFIRM_ACCOUNT, 
						"wmsDealTaskManager"+MyUtils.spiltDot+"confirmAccount", asn.getId());
				commonDao.store(task);
				//统计明细是否全部确认
				/*Boolean beReceived = false;
				Set<WmsASNDetail> details = asn.getDetails();
				for(WmsASNDetail detail : details){
					beReceived = detail.getBeReceived();
				}
				if(beReceived){
					//发送过账确认任务
					Task task = new Task(HeadType.CONFIRM_ACCOUNT, 
							"wmsDealTaskManager"+MyUtils.spiltDot+"confirmAccount", asn.getId());
					commonDao.store(task);
				}*/
				
				
				Map<Long,String> reportValue = new HashMap<Long, String>();
				reportValue.put(asn.getId(), "parts_sjzyd.raq");
				
				result.put(IPage.REPORT_VALUES, reportValue);
				result.put(IPage.REPORT_PRINT_NUM, 1);
				
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("ids", asn.getId());
				result.put(IPage.REPORT_PARAMS, params);
			}else{
				System.out.println("未完成收货:"+asnCode);
			}
		}
		return result;
	}
	@Override
	public Map printPalltDirec(WmsASN asn){
		asn.setPrintDate(new Date());//打印托盘标签时间
		asn.setPrintPerson(UserHolder.getUser().getName());//打印人
		asn.setIsPrint(Boolean.TRUE);//是否打印
		commonDao.store(asn);
		
        Map result = new HashMap();
		Map<Long,String> reportValue = new HashMap<Long, String>();
		reportValue.put(asn.getId(), "wmsPalletSerial.raq");
		
		result.put(IPage.REPORT_VALUES, reportValue);
		result.put(IPage.REPORT_PRINT_NUM, 1);
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ids", asn.getId());
		result.put(IPage.REPORT_PARAMS, params);
		
		return result;
	}
}