package com.vtradex.wms.server.service.replenish.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.ShipLotInfo;
import com.vtradex.wms.server.model.move.WmsMoveDoc;
import com.vtradex.wms.server.model.move.WmsMoveDocDetail;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.model.move.WmsMoveDocType;
import com.vtradex.wms.server.model.organization.TypeOfBill;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.warehouse.WmsLocationType;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.service.base.WmsBillTypeManager;
import com.vtradex.wms.server.service.inventory.WmsInventoryManager;
import com.vtradex.wms.server.service.replenish.WmsMoveDocReplenishmentManager;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.rule.WmsTransactionalManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.service.workDoc.WmsWorkDocManager;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.utils.PackageUtils;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

/**
 * 补货管理
 *
 * @category Manager 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.9 $Date: 2015/06/15 06:51:07 $
 */
@SuppressWarnings({"unchecked"})
public class DefaultWmsMoveDocReplenishmentManager extends DefaultBaseManager implements WmsMoveDocReplenishmentManager {
	
	public static final String MANUAL_TYPE = "手工创建";
	
	public static final String REPLENISHMENT_RULE_TABLE = "R101_补货_补货规则表";
	
	protected WorkflowManager workflowManager;
	protected WmsInventoryManager inventoryManager;
	protected WmsBussinessCodeManager codeManager;
	protected WmsRuleManager wmsRuleManager;
	protected WmsWorkDocManager wmsWorkDocManager;
	protected WmsTransactionalManager wmsTransactionalManager;
	protected WmsBillTypeManager wmsBillTypeManager;

	public DefaultWmsMoveDocReplenishmentManager(WorkflowManager workflowManager, WmsInventoryManager inventoryManager,
			WmsBussinessCodeManager codeManager,WmsRuleManager wmsRuleManager ,WmsWorkDocManager wmsWorkDocManager
			,WmsTransactionalManager wmsTransactionalManager, WmsBillTypeManager wmsBillTypeManager) {
		this.workflowManager = workflowManager;
		this.inventoryManager = inventoryManager;
		this.codeManager =  codeManager;
		this.wmsRuleManager =  wmsRuleManager;
		this.wmsWorkDocManager =  wmsWorkDocManager;
		this.wmsTransactionalManager =  wmsTransactionalManager;
		this.wmsBillTypeManager =  wmsBillTypeManager;
	}
	public void deleteDetail(WmsMoveDocDetail detail){
		WmsMoveDoc moveDoc = commonDao.load(WmsMoveDoc.class, detail.getMoveDoc().getId());
		moveDoc.removeDetail(detail);
		commonDao.delete(detail);
		commonDao.store(moveDoc);
	}
	public void manualCreateReplenishmentJac(Long companyId) {
		String warehouseName = WmsWarehouseHolder.getWmsWarehouse().getName();
		//自动删除该用户之前已经创建过的打开状态的数据
		List<WmsMoveDoc> docs = commonDao.findByQuery("FROM WmsMoveDoc d WHERE d.type =:type"
				+ " AND d.warehouse.name =:warehouseName AND d.company.id =:companyId"
				+ " AND d.status =:status", 
				new String[]{"type","warehouseName","companyId","status"}, 
				new Object[]{WmsMoveDocType.MV_REPLENISHMENT_MOVE,warehouseName,companyId,WmsMoveDocStatus.OPEN});
		if(docs.size()>0){
			List<Long> docIds = new ArrayList<Long>();
			for(WmsMoveDoc doc : docs){
				docIds.add(doc.getId());
			}
			List<WmsMoveDocDetail> docds = commonDao.findByQuery("FROM WmsMoveDocDetail dd WHERE dd.moveDoc.id"
					+ " in ("+StringUtils.substringBetween(docIds.toString(), "[", "]")+")");
			if(docds.size()>0){
				commonDao.deleteAll(docds);
			}
			commonDao.deleteAll(docs);
		}
		/**现场备料区每种物料都设有安全库存,当创建补货单时系统会将属于备料区的库存数量进行逐一统计,
		 * 然后与规则表维护的安全库存用量对比,当小于安全库存值时列为待补货对象,
		 * 最后统一创建补货单头信息和明细信息。单据类型是补货移位单*/
		WmsOrganization company = commonDao.load(WmsOrganization.class,companyId);
		String type = MyUtils.getMoveType(WmsMoveDocType.MV_REPLENISHMENT_MOVE);
		List<Map<String, Object>> list = wmsRuleManager.getAllRuleTableDetail(warehouseName,
				"R102_料号级安全库存规则表",company.getName());
		Map<String,List<Object[]>> safeInv = new HashMap<String, List<Object[]>>();
		List<Object[]> safeItemInv = null;
		Double totalQty = 0D,safeQty = 0D;
		if(list!=null && list.size()>0){
			for(Map<String, Object> obj : list){
				if(obj.get("是否补货").equals("是")){
//					System.out.println(obj.get("供应商编码")+MyUtils.spilt1+obj.get("物料编码")+":"+obj.get("安全库存"));
					safeQty = Double.valueOf(obj.get("拉动库存").toString());
					if(safeInv.containsKey(obj.get("供应商编码").toString())){
						safeItemInv = safeInv.get(obj.get("供应商编码").toString());
					}else{
						safeItemInv = new ArrayList<Object[]>();
					}
					safeItemInv.add(new Object[]{obj.get("物料编码"),safeQty,obj.get("工艺状态")});
					safeInv.put(obj.get("供应商编码").toString(),safeItemInv);
					totalQty += safeQty;
				}
			}
			list.clear();
		}
		
		Double tempQty = totalQty;
		List<Object[]> replenishments = new ArrayList<Object[]>();
		Iterator<Entry<String, List<Object[]>>> iterator = safeInv.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, List<Object[]>> entry = iterator.next();
			List<Map<String, Object>> resultObjs = null;
			try {
				resultObjs = getStockUpLocationByJac(WmsWarehouseHolder.getWmsWarehouse(),type, 
						WmsMoveDocType.MV_REPLENISHMENT_MOVE,tempQty,WmsLocationType.STORAGE,entry.getKey());
			} catch (Exception e) {
				throw new BusinessException(e.getLocalizedMessage());
			}
			Map<String,Double> blInv = new HashMap<String, Double>();
			if(resultObjs!=null && resultObjs.size()>0){
				for(Map<String, Object> result : resultObjs){
//					System.out.println(result.get("供应商")+","+result.get("货品CODE")+","+result.get("库存数量"));
					blInv.put(result.get("货品CODE")+MyUtils.spilt1+result.get("工艺状态"), 
							Double.valueOf(result.get("库存数量").toString()));
				}
			}
			resultObjs.clear();
			for(Object[] temp : entry.getValue()){//[物料编码,拉动库存,工艺状态]
				safeQty = Double.valueOf(temp[1].toString());
				totalQty = blInv.get(temp[0]+MyUtils.spilt1+temp[2]);
				if(totalQty != null){
					if(totalQty < safeQty){
						replenishments.add(new Object[]{temp[0],(safeQty-totalQty),entry.getKey(),temp[2]});
					}
				}else{
					replenishments.add(new Object[]{temp[0],safeQty,entry.getKey(),temp[2]});
				}
			}
		}
		
		if(replenishments.size()>0){
			WmsMoveDoc moveDoc = newMoveDoc(company);
			for(Object[] obj : replenishments){//[物料CODE,待补货量,供应商CODE,工艺状态]
//				System.out.println(obj[0]+":"+obj[1]+":"+obj[2]);
				WmsMoveDocDetail moveDocDetial = newMoveDetail(moveDoc, obj[0].toString(), null, 
						null, Double.valueOf(obj[1].toString()),obj[2].toString(), obj[3].toString());
				moveDoc.addDetail(moveDocDetial);
			}
			commonDao.store(moveDoc);
			replenishments.clear();
		}else{
			LocalizedMessage.addLocalizedMessage(MyUtils.font("没有待补货明细"));
		}
	}
	private WmsMoveDoc newMoveDoc(WmsOrganization company){
		WmsMoveDoc moveDoc = EntityFactory.getEntity(WmsMoveDoc.class);
		moveDoc.setType(WmsMoveDocType.MV_REPLENISHMENT_MOVE);
		moveDoc.setWarehouse(WmsWarehouseHolder.getWmsWarehouse());
		if(company == null){
			moveDoc.setCompany(this.getCompanyByWarehouse(WmsWarehouseHolder.getWmsWarehouse()));
		}else{
			moveDoc.setCompany(company);
		}
		
		WmsBillType billType = wmsBillTypeManager.getWmsBillType(moveDoc.getCompany(), TypeOfBill.MOVE, moveDoc.getType());
		moveDoc.setBillType(billType);
		if (moveDoc.isNew()) {
			String code = codeManager.generateCodeByRule(moveDoc.getWarehouse(), moveDoc.getWarehouse().getName(), "补货单", billType.getName());
			moveDoc.setCode(code);
		}
		this.commonDao.store(moveDoc);
		return moveDoc;
	}
	/**replenishmentArea-补货区,pickArea-拣货区,extendPropC1-工艺状态*/
	private WmsMoveDocDetail newMoveDetail(WmsMoveDoc moveDoc,String itemCode,String replenishmentArea
			,String pickArea,Double planQuantityBU,String supplier,String extendPropC1){
		WmsMoveDocDetail moveDocDetial = EntityFactory.getEntity(WmsMoveDocDetail.class);
		moveDocDetial.setMoveDoc(moveDoc);
		moveDocDetial.setItem(getItem(itemCode));
		moveDocDetial.setReplenishmentArea(replenishmentArea);
		moveDocDetial.setPickArea(pickArea);
		WmsPackageUnit basePackageUnit = this.getPackageUnit(moveDocDetial.getItem());
		basePackageUnit.setItem(moveDocDetial.getItem());
		moveDocDetial.setPackageUnit(basePackageUnit);
		moveDocDetial.setPlanQuantityBU(planQuantityBU);
		moveDocDetial.setPlanQuantity(PackageUtils.convertPackQuantity(moveDocDetial.getPlanQuantityBU(), basePackageUnit));
		ShipLotInfo shipLotInfo = new ShipLotInfo();
		shipLotInfo.setSupplier(supplier);
		shipLotInfo.setExtendPropC1(extendPropC1);
		moveDocDetial.setShipLotInfo(shipLotInfo);
		this.commonDao.store(moveDocDetial);
		return moveDocDetial;
	}
	public List<Map<String, Object>> getStockUpLocationByJac(WmsWarehouse warehouse, String type
			,String billCode,Double quantity,String locationType,String supper){
		Map<String, Object> problem = new HashMap<String, Object>();
		problem.put("仓库ID", warehouse.getId());
		problem.put("类型", type);
		problem.put("单据类型编码",billCode);
		problem.put("数量", quantity);
		problem.put("库位类型", locationType);
		problem.put("供应商",supper);
		
		Map<String, Object> resultMap = null;
		List<Map<String, Object>> resultObjs = null;//[{库位序号=21233, 库位代码=1号包装台}]
		try {
			resultMap = wmsRuleManager.execute(warehouse.getName(), warehouse.getName(), "补货生成规则", problem);
			resultObjs = (List<Map<String, Object>>) resultMap.get("补货策略库位列表");
		} catch (Exception e) {
			throw new BusinessException(e.getLocalizedMessage());
		}
		return resultObjs;
	}
	/**
	 * 手工创建补货单
	 */
	public void manualCreateReplenishment() {
		String warehouseName = WmsWarehouseHolder.getWmsWarehouse().getName();
		
		Map<String, Object> problem = new HashMap<String, Object>();
		problem.put("类型", MANUAL_TYPE);
		problem.put("仓库序号", WmsWarehouseHolder.getWmsWarehouse().getId());
		Map<String, Object> result = wmsRuleManager.execute(warehouseName, warehouseName, "补货生成规则", problem);
		
		try {
			this.doManualCreateReplenishment(result);
		} catch (BusinessException be) {
			logger.error("", be);
			throw new BusinessException(be.getMessage());
		}
	}
	
	public void doManualCreateReplenishment(Map<String, Object> result) throws BusinessException {
		
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("返回列表");	
		
		if (resultList == null || resultList.size() <= 0) {
			LocalizedMessage.addMessage("manual.create.replenishment.noplan");
			return;
		}
		
		WmsMoveDoc moveDoc = newMoveDoc(null);
		
		for (Map<String, Object> resultObject : resultList) {
			String itemCode = resultObject.get("货品代码").toString();
			String replenishmentArea = resultObject.get("补货区").toString();
			String pickArea = resultObject.get("拣货区").toString();
			String planQuantityBU = resultObject.get("计划补货数量").toString();
			
			WmsMoveDocDetail moveDocDetial = newMoveDetail(moveDoc, itemCode, replenishmentArea, 
					pickArea, Double.valueOf(planQuantityBU),null,BaseStatus.NULLVALUE);
			moveDoc.addDetail(moveDocDetial);
		}
		this.commonDao.store(moveDoc);
	}
	
	/**
	 * 根据仓库获取
	 * @param warehouse
	 * @return
	 */
	private WmsOrganization getCompanyByWarehouse(WmsWarehouse warehouse) {
		String hql = "from WmsOrganization org where org.code = :code AND org.beCompany = true";
		Object obj = this.commonDao.findByQueryUniqueResult(hql, "code", warehouse.getCode());
		if (obj == null) {
			throw new BusinessException("仓库: " + warehouse.getCode() + ", 虚拟货主未设置");
		}
		return (WmsOrganization)obj;
	}
	
	private WmsItem getItem(String code) {
		String hql = "from WmsItem i where i.code = :code and i.status = 'ENABLED'";
		Object obj = this.commonDao.findByQueryUniqueResult(hql, "code", code);
		if (obj == null) {
			throw new BusinessException("货品: " + code + ", 没有找到");
		}
		return (WmsItem)obj;
	}
	
	private WmsPackageUnit getPackageUnit(WmsItem item) {
		String hql = "from WmsPackageUnit p where p.item.id = :itemId and p.unit = :unit";
		Object obj = this.commonDao.findByQueryUniqueResult(hql, new String[]{"itemId", "unit"}, new Object[]{item.getId(), item.getBaseUnit()});
		if (obj == null) {
			throw new BusinessException("货品: " + item.getCode() + ", 没有找到基本包装单位");
		}
		return (WmsPackageUnit)obj;
	}
}