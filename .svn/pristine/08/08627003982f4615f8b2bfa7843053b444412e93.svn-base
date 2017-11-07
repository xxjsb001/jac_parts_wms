package com.vtradex.wms.server.service.receiving.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mlw.vlh.ValueList;
import net.mlw.vlh.ValueListInfo;

import com.vtradex.thorn.client.template.gantt.GanttConstant;
import com.vtradex.thorn.client.template.gantt.model.GanttCharModel;
import com.vtradex.thorn.client.template.gantt.model.GanttCharNodeModel;
import com.vtradex.thorn.client.template.gantt.model.GanttChartSearchModel;
import com.vtradex.thorn.client.ui.table.RowData;
import com.vtradex.thorn.server.exception.OriginalBusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.thorn.server.util.GanttChartUtil;
import com.vtradex.thorn.server.valuelist.adapter.hib3.ValueListAdapter;
import com.vtradex.wms.server.model.organization.WmsBillType;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.model.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.receiving.WmsBooking;
import com.vtradex.wms.server.model.receiving.WmsBookingStatus;
import com.vtradex.wms.server.model.receiving.WmsBookingType;
import com.vtradex.wms.server.model.shipping.WmsMasterBOL;
import com.vtradex.wms.server.model.warehouse.WmsDock;
import com.vtradex.wms.server.model.warehouse.WmsWarehouseArea;
import com.vtradex.wms.server.service.receiving.OrderMonitorQueryConst;
import com.vtradex.wms.server.service.receiving.WmsBookingManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;
import com.vtradex.wms.server.utils.DateUtil;
import com.vtradex.wms.server.utils.DoubleUtils;

/**
 * @author: 李炎
 */
@SuppressWarnings("unchecked")
public class DefaultWmsBookingManager extends DefaultBaseManager implements
WmsBookingManager {
	
    private final ValueListAdapter valueListAdapter;
    private WmsBussinessCodeManager codeManager;
    private static SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
	public DefaultWmsBookingManager(ValueListAdapter valueListAdapter,WmsBussinessCodeManager codeManager){
		this.valueListAdapter = valueListAdapter;
		this.codeManager = codeManager;
	}
	
	public ValueList queryByValueList(String hql, Map pageParams) {
		ValueListInfo info = new ValueListInfo(pageParams);
		return valueListAdapter.getValueList(hql, info);
	}
	public Map<String, Object> getGanttCharModelByParams(
			Map<String, Object> params) {
		
		int num = Integer.valueOf(""
				+ params.remove(OrderMonitorQueryConst.FILTER_CURRENT_PAGE_NUM));
		int row = Integer.valueOf(""
				+ params.remove(OrderMonitorQueryConst.FILTER_DISPLAY_ROW_COUNT));
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(params.get("date") == null){
			params.put("date", com.vtradex.thorn.server.util.DateUtil.formatDateYMDToStr(new Date()));
		}
		List<GanttCharModel> ms = new ArrayList<GanttCharModel>();
		
		String hql = "select distinct dock from WmsBooking booking" +
				" where 1=1 " + 
				"/~date: AND date_format(booking.planDate,'%Y-%m-%d') = {date}~/" + 
				"/~class: AND booking.classify = {class}~/" + 
				"/~type: AND booking.planType = {type}~/" + 
				"/~code: AND booking.planCode = {code}~/" +
				" order by booking.code desc" ;
		
		params.put(ValueListInfo.PAGING_NUMBER_PER,row + "");
		params.put(ValueListInfo.PAGING_PAGE,num + "");
	
		ValueList valueList = queryByValueList(filterHqlWithNullParam(hql, params),params);
        List<WmsDock> dockList = (List<WmsDock>) valueList.getList();
		
        for (WmsDock dock : dockList) {
			GanttCharModel g1 = new GanttCharModel();
			g1.setCode(dock.getCode()+"");
			g1.setType("receive");
			g1.setTypeImage("asn.gif");
			WmsWarehouseArea warehouseArea = commonDao.load(WmsWarehouseArea.class, dock.getWarehouseArea().getId());
			g1.setTypeDescription("月台编码："+ dock.getCode() +"</br>" +
					"库区编码："+ warehouseArea.getCode()+"</br>" +
					"库区名称： "+ warehouseArea.getName() 
									);
			List<Date> legPlannedTimes = new ArrayList<Date>();
			
			String hql1 = "from WmsBooking booking where booking.dock.id="+dock.getId()+ " and booking.planDate = '"+params.get("date")+"'";
			if(params.get("class") != null){
				hql1 += " and booking.classify = '"+params.get("class")+"'";
			}
			if(params.get("type") != null){
				hql1 += " and booking.planType = '"+params.get("type")+"'";
			}
			if(params.get("code") != null){
				hql1 += " and booking.planCode = '"+params.get("code")+"'";
			}
			List<WmsBooking> bookings = commonDao.findByQuery(hql1);
			if(bookings.size() == 0){
				legPlannedTimes.add(new Date());
			}
			
			for (WmsBooking booking : bookings) {
				if(!(booking.getAsnPlannedStartTime()==null) && !legPlannedTimes.contains(booking.getAsnPlannedStartTime())) {
					legPlannedTimes.add(booking.getAsnPlannedStartTime());
				}
				if(!(booking.getAsnPlannedEndTime()==null) && !legPlannedTimes.contains(booking.getAsnPlannedEndTime())) {
					legPlannedTimes.add(booking.getAsnPlannedEndTime());
				}
			}
			
			if(legPlannedTimes.size()>0) {
				Date currentStart = DateUtil.formatStrToDate(DateUtil.formatDateYMDToStr(legPlannedTimes.get(0))+" 00:00:00");
				Date currentEnd = DateUtil.formatStrToDate(DateUtil.formatDateYMDToStr(legPlannedTimes.get(0))+" 23:59:59");
				if(!legPlannedTimes.contains(currentStart)) {
					legPlannedTimes.add(currentStart);
				}
				if(!legPlannedTimes.contains(currentEnd)) {
					legPlannedTimes.add(currentEnd);
				}
			}
			
	
			Collections.sort(legPlannedTimes, new Comparator<Date>() {
				public int compare(Date o1, Date o2) {
					if(o1.before(o2))
	                	 return -1;
					else
	                	 return 1;
				}
			});
			
			for (int i = 0; i < legPlannedTimes.size()-1; i++) {
				Date date1 = legPlannedTimes.get(i);
				Date date2 = legPlannedTimes.get(i+1);
				
				List<WmsBooking> plans = getWmsBookingByZone(date1, date2, dock.getId());
				double startHour = DoubleUtils.getDecimalHourByDate(date1);
				double endHour = DoubleUtils.getDecimalHourByDate(date2);
				
				GanttCharNodeModel m1 = new GanttCharNodeModel();
				
				m1.setStartTime(startHour);
				m1.setEndTime(endHour);
				
				int size = plans.size();
				if(size == 0) {
					m1.setType(GanttCharNodeModel.A);
				} else if(size == 1) {
					WmsBooking plan = plans.get(0);
					String desc = "";
					String code ="";
					SimpleDateFormat hm = new SimpleDateFormat("HH:mm");
					String arriveTime = "-";
					String finishTime = "-";
					String description = plan.getRemark();
					if(plan.getActualStartTime() != null){
						arriveTime = hm.format(plan.getActualStartTime());
					}
					if(plan.getFinishTime() != null){
						finishTime = hm.format(plan.getFinishTime());
					}
					if(WmsBookingType.RECEIVE.equals(plan.getPlanType())){
						String company = "";
						String billType = "";
						Double expectedQuantityBU = 0.0D;
						if(plan.getAsn() != null){
							WmsASN asn = commonDao.load(WmsASN.class, plan.getAsn().getId());
							code = asn.getCode()==null? "" : asn.getCode();
							WmsOrganization wmsCompany = commonDao.load(WmsOrganization.class, asn.getCompany().getId());
							WmsBillType wmsBillType = commonDao.load(WmsBillType.class, asn.getBillType().getId());
							company = wmsCompany.getName()==null? "" : wmsCompany.getName();
							billType = wmsBillType.getName()==null? "" : wmsBillType.getName();
							List<WmsASNDetail> details = commonDao.findByQuery("from WmsASNDetail detail where detail.asn.id = "+asn.getId());
							for(WmsASNDetail detail : details){
								if(detail.getBooking() != null && detail.getBooking().getId().longValue()==plan.getPreId().longValue()){
									expectedQuantityBU += detail.getExpectedQuantityBU();
								}
							}
						}
					    desc = "单号： "+ code +"</br>" +
						"货主： "+ company +"</br>" +
						"单据类型："+ billType +"</br>" +
						"预约时间："+ DateUtil.formatDateHMToStr(plan.getAsnPlannedStartTime()) +" - " + DateUtil.formatDateHMToStr(plan.getAsnPlannedEndTime()) +"</br>" +
						"到库时间："+ arriveTime +"</br>" +
						"完成时间："+ finishTime +"</br>" +
						"期待数量BU："+ expectedQuantityBU +"</br>" +
					    "备注："+ description;
					
					}
					if(WmsBookingType.SHIP.equals(plan.getPlanType())){
						String carrier = "";
						String vehicleNo = "";
						
						if(plan.getMasterBOL() != null){
							WmsMasterBOL bol = commonDao.load(WmsMasterBOL.class, plan.getMasterBOL().getId());
							code = bol.getCode()==null? "" : bol.getCode();
							if(bol.getCarrier() != null){
								WmsOrganization wmsCarrier = commonDao.load(WmsOrganization.class, bol.getCarrier().getId());
								carrier = wmsCarrier.getName()==null? "" : wmsCarrier.getName();
							}
							vehicleNo = bol.getVehicleNo()==null? "" : bol.getVehicleNo();
							
						}
					    desc = "装车单号： "+ code +"</br>" +
						"承运商： "+ carrier +"</br>" +
						"车牌号："+ vehicleNo +"</br>" +
						"预约时间："+ DateUtil.formatDateHMToStr(plan.getAsnPlannedStartTime()) +" - " + DateUtil.formatDateHMToStr(plan.getAsnPlannedEndTime()) +"</br>" +
						"到库时间："+ arriveTime +"</br>" +
						"完成时间："+ finishTime +"</br>" +
						"备注："+ description;
					
					}
					m1.setDescription(desc);
					if(plan.getStatus().equals(WmsBookingStatus.FINISH)) {
						m1.setType(GanttCharNodeModel.C);
					} else if(plan.getStatus().equals(WmsBookingStatus.EXECUTING)){
						m1.setType(GanttCharNodeModel.E);
					}
					else {
						m1.setType(GanttCharNodeModel.B);
					}
					
				} 
				else {
					m1.setType(GanttCharNodeModel.D);
					String desc = "";
					SimpleDateFormat hm = new SimpleDateFormat("HH:mm");
					for (int j = 0; j < plans.size(); j++) {
						WmsBooking plan = plans.get(j);
						if(j > 0) {
							desc += "</br>-----------------------------------</br>";
						}
						String code = "";
						String arriveTime = "-";
						String finishTime = "-";
						String description = plan.getRemark();
						if(plan.getActualStartTime() != null){
							arriveTime = hm.format(plan.getActualStartTime());
						}
						if(plan.getFinishTime() != null){
							finishTime = hm.format(plan.getFinishTime());
						}
						if(WmsBookingType.RECEIVE.equals(plan.getPlanType())){
							String company = "";
							String billType = "";
							Double expectedQuantityBU = 0.0D;
							if(plan.getAsn() != null){
								WmsASN asn = commonDao.load(WmsASN.class, plan.getAsn().getId());
								code = asn.getCode()==null? "" : asn.getCode();
								WmsOrganization wmsCompany = commonDao.load(WmsOrganization.class, asn.getCompany().getId());
								WmsBillType wmsBillType = commonDao.load(WmsBillType.class, asn.getBillType().getId());
								company = wmsCompany.getName()==null? "" : wmsCompany.getName();
								billType = wmsBillType.getName()==null? "" : wmsBillType.getName();
								List<WmsASNDetail> details = commonDao.findByQuery("from WmsASNDetail detail where detail.asn.id = "+asn.getId());
								for(WmsASNDetail detail : details){
									if(detail.getBooking() != null && detail.getBooking().getId().longValue()==plan.getPreId().longValue()){
										expectedQuantityBU += detail.getExpectedQuantityBU();
									}
								}
							}
						    desc += "单号： "+ code +"</br>" +
							"货主： "+ company +"</br>" +
							"单据类型："+ billType +"</br>" +
							"预约时间："+ DateUtil.formatDateHMToStr(plan.getAsnPlannedStartTime()) +" - " + DateUtil.formatDateHMToStr(plan.getAsnPlannedEndTime()) +"</br>" +
							"完成时间："+ finishTime +"</br>" +
							"期待数量BU："+ expectedQuantityBU +"</br>" +
						    "备注："+ description;
						
						}
						if(WmsBookingType.SHIP.equals(plan.getPlanType())){
							String carrier = "";
							String vehicleNo = "";
							if(plan.getMasterBOL() != null){
								WmsMasterBOL bol = commonDao.load(WmsMasterBOL.class, plan.getMasterBOL().getId());
								code = bol.getCode()==null? "" : bol.getCode();
								if(bol.getCarrier() != null){
									WmsOrganization wmsCarrier = commonDao.load(WmsOrganization.class, bol.getCarrier().getId());
									carrier = wmsCarrier.getName()==null? "" : wmsCarrier.getName();
								}
								vehicleNo = bol.getVehicleNo()==null? "" : bol.getVehicleNo();
								description = bol.getDescription();
							}
						    desc += "装车单号： "+ code +"</br>" +
							"承运商： "+ carrier +"</br>" +
							"车牌号："+ vehicleNo +"</br>" +
							"预约时间："+ DateUtil.formatDateHMToStr(plan.getAsnPlannedStartTime()) +" - " + DateUtil.formatDateHMToStr(plan.getAsnPlannedEndTime()) +"</br>" +
							"到库时间："+ arriveTime +"</br>" +
							"完成时间："+ finishTime +"</br>" +
							"备注："+ description;
					}
				 }
					m1.setDescription(desc);
				}
				g1.addGanttCharNodeModel(m1);				
			}			
			ms.add(g1);
		 
        }
        for(GanttCharModel gcm : ms){
        	for(int loop = 0; loop < gcm.getDetails().size(); loop++){
        		GanttCharNodeModel gcnm = gcm.getDetails().get(loop);
        		double useRate = GanttChartUtil.getGanttChartUseRate(gcnm.getStartTime(), gcnm.getEndTime());
        		gcnm.setUseRate(useRate);
        	}
        }
        
		result.put(GanttConstant.LIST, ms);
		result.put(GanttConstant.TOTAL_PAGE,new Integer(valueList.getValueListInfo().getTotalNumberOfPages()));
		result.put(GanttConstant.TOTAL_NUMBER,new Integer(valueList.getValueListInfo().getTotalNumberOfEntries()));
		return result;
	}

	private List<WmsBooking> getWmsBookingByZone(Date startTime, Date endTime, Long dockId) {
		return (List<WmsBooking>) commonDao
				.findByQuery(
						" from WmsBooking plan where :startTime>=plan.asnPlannedStartTime" +
						" and :endTime <=plan.asnPlannedEndTime and plan.dock.id =:dockId order by plan.asnPlannedStartTime",
						new String[]{"startTime","endTime","dockId"}, new Object[]{startTime,endTime,dockId});
	}
	
	
	private List<String> getClassifyByASNPlan() {
		return (List<String>) commonDao.findByQuery(" SELECT company.name FROM   WmsOrganization  company " +
				"WHERE   (company.beCompany  = true or company.beCarrier = true) and company.beVirtual =false AND company.status = 'ENABLED' ") ;
	}
	

	public Map<String, Object> getGanttCharSearchInfoByParams(
			Map<String, Object> params) {Map<String, Object> result = new HashMap<String, Object>();
			GanttChartSearchModel searchModel = new GanttChartSearchModel();
			searchModel.setDateKey("date");
			searchModel.setDateTitle("日期");
			searchModel.setDateDefaultValue(DateUtil.formatDateYMDToStr(new Date()));
			
			searchModel.setClassKey("class");
			searchModel.setClassTitle("货主/承运商");
			List<RowData> classValues = new ArrayList<RowData>();
			List<String> classifys = getClassifyByASNPlan();
			for (String classify : classifys) {
				RowData rd1 = new RowData();
				rd1.addColumnValue(classify);
				rd1.addColumnValue(classify);
				classValues.add(rd1);
			}
			searchModel.setClassValues(classValues);
			
			searchModel.setTypeKey("type");
			searchModel.setTypeTitle("类型");
			List<RowData> typeValues = new ArrayList<RowData>();
			List<String> vehicleTypes = Arrays.asList(new String[]{WmsBookingType.RECEIVE,WmsBookingType.SHIP});
			for (String vehicleType : vehicleTypes) {
				RowData rd2 = new RowData();
				rd2.addColumnValue(vehicleType);
				rd2.addColumnValue(vehicleType);
				typeValues.add(rd2);
			}
			searchModel.setTypeValues(typeValues);
			
			searchModel.setCodeKey("code");
			searchModel.setCodeTitle("编号");
			
			result.put(GanttConstant.SEARCH_INFO, searchModel);
			
			return result;
	}
	
	public void storeWmsBooking(WmsBooking wmsBooking){
		Date startDate = wmsBooking.getAsnPlannedStartTime();
		Date endDate;
		if(wmsBooking.getAsnPlannedEndTime() != null){
			endDate = wmsBooking.getAsnPlannedEndTime();
		}else{
			endDate = wmsBooking.getAsnPlannedLastTime();
			wmsBooking.setAsnPlannedEndTime(endDate);
		}
		if(startDate.after(endDate) || startDate.equals(endDate)){
			throw new OriginalBusinessException("预约结束时间不能早于或等于预约开始时间！");
		}
		WmsASN asn = null;
		WmsMasterBOL bol = null;
		wmsBooking.setCode(codeManager.generateCodeByRule(wmsBooking.getDock().getWarehouseArea().getWarehouse(), 
				wmsBooking.getDock().getWarehouseArea().getWarehouse().getName(), "预约单", ""));
		wmsBooking.setAsnPlannedLastTime(endDate);
		try {
			wmsBooking.setPlanDate(ymd.parse(ymd.format(wmsBooking.getAsnPlannedStartTime())));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		if(wmsBooking.getAsn() != null && WmsBookingType.RECEIVE.equals(wmsBooking.getPlanType())){
			asn = wmsBooking.getAsn();
			WmsOrganization wmsCompany = commonDao.load(WmsOrganization.class, asn.getCompany().getId());
			wmsBooking.setClassify(wmsCompany.getName()==null?"":wmsCompany.getName());
			wmsBooking.setAsn(asn);
			commonDao.store(asn);
			for(WmsASNDetail detail : asn.getDetails()){
				if(detail.getBooking() == null){
					detail.setBooking(wmsBooking);
				}
			}
		}
		if(wmsBooking.getMasterBOL() != null && WmsBookingType.SHIP.equals(wmsBooking.getPlanType())){
			bol = wmsBooking.getMasterBOL();
			if(bol.getCarrier() != null){
				WmsOrganization wmsCarrier = commonDao.load(WmsOrganization.class, bol.getCarrier().getId());
				wmsBooking.setClassify(wmsCarrier.getName()==null?"":wmsCarrier.getName());
			}
			wmsBooking.setMasterBOL(bol);
		}
		commonDao.store(wmsBooking);
		wmsBooking.setPreId(wmsBooking.getId());
		int j = DateUtil.getMargin(DateUtil.formatDateYMDToStr(endDate),DateUtil.formatDateYMDToStr(startDate));
		if(j > 0){
			wmsBooking.setAsnPlannedEndTime(DateUtil.formatStrToDate(DateUtil.formatDateYMDToStr(wmsBooking.getPlanDate())+" 23:59:59"));
			for(int i=1 ; i<=j ; i++){
				WmsBooking plan = EntityFactory.getEntity(WmsBooking.class);
		        BeanUtils.copyEntity(plan, wmsBooking);//对象属性拷贝
		        plan.setId(null);
				try {
					plan.setPlanDate(DateUtil.addDayToDate(ymd.parse(ymd.format(wmsBooking.getAsnPlannedStartTime())), i));
					plan.setAsnPlannedStartTime(DateUtil.formatStrToDate(DateUtil.formatDateYMDToStr(plan.getPlanDate())+" 00:00:00"));
					plan.setClassify(wmsBooking.getClassify());
					plan.setDock(wmsBooking.getDock());
					plan.setCode(codeManager.generateCodeByRule(wmsBooking.getDock().getWarehouseArea().getWarehouse(), 
							wmsBooking.getDock().getWarehouseArea().getWarehouse().getName(), "预约单", ""));
					if(i == j){
						plan.setAsnPlannedEndTime(endDate);
					}else{
						plan.setAsnPlannedEndTime(DateUtil.formatStrToDate(DateUtil.formatDateYMDToStr(plan.getPlanDate())+" 23:59:59"));
					}
					commonDao.store(plan);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void editWmsBooking(Long planId,Long asnID,Long dockId,Date startTime,Date endTime,String remark,String planType){
		if(startTime.after(endTime) || startTime.equals(endTime)){
			throw new OriginalBusinessException("预约结束时间不能早于或等于预约开始时间！");
		}
		WmsBooking plan1 = commonDao.load(WmsBooking.class, planId);
		WmsASN asn = null;
		WmsMasterBOL bol = null;
		if(asnID != 0 && WmsBookingType.SHIP.equals(planType)){
			bol = commonDao.load(WmsMasterBOL.class, asnID);
		}
		if(asnID != 0 && WmsBookingType.RECEIVE.equals(planType)){
			asn = commonDao.load(WmsASN.class, asnID);
			if(plan1.getAsn() != null && asnID != plan1.getAsn().getId()){
				WmsASN asn1 = commonDao.load(WmsASN.class, plan1.getAsn().getId());
				for(WmsASNDetail detail : asn1.getDetails()){
					detail.setBooking(null);
				}
			}
		}
		Long preId = plan1.getPreId();
		List<WmsBooking> asnPlans = getPlans(preId);
		Date nowStartTime = asnPlans.get(0).getAsnPlannedStartTime();
		Date nowEndTime = asnPlans.get(asnPlans.size() - 1).getAsnPlannedEndTime();
		if(!DateUtil.isSameDay(nowStartTime,startTime) || 
				!DateUtil.isSameDay(nowEndTime,endTime)){
			commonDao.deleteAll(asnPlans);
		}else{
			for(int i = 0;i<asnPlans.size();i++){
				WmsBooking asnPlan = asnPlans.get(i);
				if(asn != null){
					WmsOrganization wmsCompany = commonDao.load(WmsOrganization.class, asn.getCompany().getId());
					asnPlan.setClassify(wmsCompany.getName()==null?"":wmsCompany.getName());
					asnPlan.setAsn(asn);
					if( i == 0){
						for(WmsASNDetail detail : asn.getDetails()){
							detail.setBooking(asnPlan);
						}
					}
				}
				if(i == 0){
					asnPlan.setAsnPlannedStartTime(startTime);
				}
				if(i == asnPlans.size() - 1){
					asnPlan.setAsnPlannedEndTime(endTime);
				}
				asnPlan.setAsnPlannedLastTime(endTime);
				if(bol != null){
					if(bol.getCarrier() != null){
						WmsOrganization wmsCarrier = commonDao.load(WmsOrganization.class, bol.getCarrier().getId());
						asnPlan.setClassify(wmsCarrier.getName()==null?"":wmsCarrier.getName());
					}else{
						asnPlan.setClassify(null);
					}
					asnPlan.setMasterBOL(bol);
				}
				asnPlan.setDock(commonDao.load(WmsDock.class, dockId));
				asnPlan.setRemark(remark);
				commonDao.store(asnPlan);
			}
			return;
		}
		
		Date startDate = startTime;
		Date endDate = endTime;
		int j = DateUtil.getMargin(DateUtil.formatDateYMDToStr(endDate),DateUtil.formatDateYMDToStr(startDate))+1;
		
		WmsBooking firstPlan = null;
		if(j > 0){
			for(int i=0 ; i<j ; i++){
				WmsBooking plan = new WmsBooking();
				try {
					plan.setPlanDate(DateUtil.addDayToDate(ymd.parse(ymd.format(startTime)), i));
					if(i == 0){
						plan.setAsnPlannedStartTime(startTime);
						if(asn != null){
							for(WmsASNDetail detail : asn.getDetails()){
								detail.setBooking(plan);
							}
						}
					}else{
						plan.setAsnPlannedStartTime(DateUtil.formatStrToDate(DateUtil.formatDateYMDToStr(plan.getPlanDate())+" 00:00:00"));
					}
					WmsDock dock = commonDao.load(WmsDock.class, dockId);
					plan.setDock(dock);
					if(asn != null){
						WmsOrganization wmsCompany = commonDao.load(WmsOrganization.class, asn.getCompany().getId());
						plan.setClassify(wmsCompany.getName()==null?"":wmsCompany.getName());
						plan.setAsn(asn);
					}
					if(bol != null){
						if(bol.getCarrier() != null){
							WmsOrganization wmsCarrier = commonDao.load(WmsOrganization.class, bol.getCarrier().getId());
							plan.setClassify(wmsCarrier.getName()==null?"":wmsCarrier.getName());
						}else{
							plan.setClassify(null);
						}
						plan.setMasterBOL(bol);
					}
					plan.setPlanType(planType);
					plan.setAsnPlannedLastTime(endDate);
					plan.setRemark(remark);
					if(i == j-1){
						plan.setAsnPlannedEndTime(endTime);
					}else{
						plan.setAsnPlannedEndTime(DateUtil.formatStrToDate(DateUtil.formatDateYMDToStr(plan.getPlanDate())+" 23:59:59"));
					}
					commonDao.store(plan);
					plan.setCode(codeManager.generateCodeByRule(dock.getWarehouseArea().getWarehouse(), 
							dock.getWarehouseArea().getWarehouse().getName(), "预约单", ""));
					if(i == 0){
						firstPlan = plan;
					}
					plan.setPreId(firstPlan.getId()==null?null:firstPlan.getId());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public Date initStartTime(Map map) {
		
		if(map.get("booking.id") != null){
			WmsBooking plan = commonDao.load(WmsBooking.class, Long.valueOf(map.get("booking.id").toString()));
			Long preId = plan.getPreId();
			List<WmsBooking> asnPlans = getPlans(preId);
			return asnPlans.get(0).getAsnPlannedStartTime();
		}
		return null;
	}
	
	public Date initEndTime(Map map) {
		if(map.get("booking.id") != null){
			WmsBooking plan = commonDao.load(WmsBooking.class, Long.valueOf(map.get("booking.id").toString()));
			Long preId = plan.getPreId();
			List<WmsBooking> asnPlans = getPlans(preId);
			return asnPlans.get(asnPlans.size() - 1).getAsnPlannedEndTime();
		}
		return null;
	}
	
	public RowData initAsnID(Map map) {
		RowData data = null;
		if(map.get("booking.id") != null){
			WmsBooking plan = commonDao.load(WmsBooking.class, Long.valueOf(map.get("booking.id").toString()));
			if(plan.getAsn()!=null){
				WmsASN asn = commonDao.load(WmsASN.class, plan.getAsn().getId());
				WmsOrganization wmsCompany = commonDao.load(WmsOrganization.class, asn.getCompany().getId());
				data = new RowData();
				data.addColumnValue(asn.getId());
				data.addColumnValue(asn.getCode());
				data.addColumnValue(wmsCompany.getName());
			}
			
		}
			
		return data;
	}
	
	public RowData initMasterID(Map map) {
		RowData data = null;
		if(map.get("booking.id") != null){
			WmsBooking plan = commonDao.load(WmsBooking.class, Long.valueOf(map.get("booking.id").toString()));
			WmsOrganization wmsCarrier = null;
			if(plan.getMasterBOL()!=null){
				WmsMasterBOL bol = commonDao.load(WmsMasterBOL.class, plan.getMasterBOL().getId());
				if(bol.getCarrier() != null){
					wmsCarrier = commonDao.load(WmsOrganization.class, bol.getCarrier().getId());
				}
				data = new RowData();
				data.addColumnValue(bol.getId());
				data.addColumnValue(bol.getCode());
				if(wmsCarrier == null){
					data.addColumnValue("-");
				}else{
					data.addColumnValue(wmsCarrier.getName());
				}
			}
		}
			
		return data;
	}
	
	public RowData initDockID(Map map) {
		RowData data = null;
		if(map.get("booking.id") != null){
			WmsBooking plan = commonDao.load(WmsBooking.class, Long.valueOf(map.get("booking.id").toString()));
			if(plan.getDock() != null){
				WmsDock dock = commonDao.load(WmsDock.class,plan.getDock().getId());
				data = new RowData();
				data.addColumnValue(dock.getId());
				data.addColumnValue(dock.getCode());
			}
		}
			
		return data;
	}
	
	public void deleteBooking(WmsBooking wmsBooking){
		if(wmsBooking != null){
			Long preId = wmsBooking.getPreId();
			List<WmsBooking> asnPlans = getPlans(preId);
			if(WmsBookingType.RECEIVE.equals(wmsBooking.getPlanType())){
				if(wmsBooking.getAsn() != null){
					WmsASN asn = commonDao.load(WmsASN.class, wmsBooking.getAsn().getId());
					for(WmsASNDetail detail : asn.getDetails()){
						if(detail.getBooking() != null && detail.getBooking().getId().longValue()==wmsBooking.getPreId().longValue()){
							detail.setBooking(null);
							commonDao.store(detail);
						}
					}
			    }
			}
			commonDao.deleteAll(asnPlans);
		}
		
	}
	
	public void inputASN(WmsBooking wmsBooking,Long asnId){
		Long preId = wmsBooking.getPreId();
		List<WmsBooking> asnPlans = getPlans(preId);
		WmsASN asn = commonDao.load(WmsASN.class, asnId);
		for(int i=0;i<asnPlans.size();i++){
			WmsBooking asnPlan = asnPlans.get(i);
			WmsOrganization wmsCompany = commonDao.load(WmsOrganization.class, asn.getCompany().getId());
			asnPlan.setClassify(wmsCompany.getName()==null?"":wmsCompany.getName());
			asnPlan.setAsn(asn);
			commonDao.store(asnPlan);
			if(i == 0){
				for(WmsASNDetail detail : asn.getDetails()){
					detail.setBooking(asnPlan);
				}
			}
		}
		
	}
	
	public void arriveRegister(WmsBooking wmsBooking){
		Long preId = wmsBooking.getPreId();
		List<WmsBooking> asnPlans = getPlans(preId);
		for(WmsBooking asnPlan : asnPlans){
			asnPlan.setStatus(WmsBookingStatus.EXECUTING);
			asnPlan.setActualStartTime(wmsBooking.getActualStartTime());
		}
	}
	
	public List<WmsBooking> getPlans(Long preId){
		String hql = "from WmsBooking booking where booking.preId = :preId order by booking.id ";
		return commonDao.findByQuery(hql,"preId",preId);
	}
	
	public void deleteASNDetailBooking(WmsASNDetail detail){
		detail.setBooking(null);
		commonDao.store(detail);
	}
	
	public void addBooking(WmsASNDetail detail,Long bookingId){
		WmsBooking booking = commonDao.load(WmsBooking.class, bookingId);
		if(booking.getAsn() == null){
			booking.setAsn(commonDao.load(WmsASN.class, detail.getAsn().getId()));
		}
		detail.setBooking(booking);
		commonDao.store(detail);
	}
	
	public void createBooking(WmsASNDetail detail,WmsBooking wmsBooking){
		if(wmsBooking.isNew()){
			storeWmsBooking(wmsBooking);
		}
		detail.setBooking(wmsBooking);
	}
}
