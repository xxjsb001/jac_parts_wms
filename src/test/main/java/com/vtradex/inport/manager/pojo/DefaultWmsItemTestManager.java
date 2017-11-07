package com.vtradex.inport.manager.pojo;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

import com.vtradex.inport.manager.WmsItemTestManager;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.service.rule.WmsRuleManager;
import com.vtradex.wms.server.service.sequence.WmsBussinessCodeManager;

@SuppressWarnings("unchecked")
public class DefaultWmsItemTestManager extends DefaultBaseManager implements WmsItemTestManager {
	protected WmsBussinessCodeManager wmsBussinessCodeManager;
	protected WorkflowManager workflowManager;
	protected WmsRuleManager wmsRuleManager;
	
	public DefaultWmsItemTestManager(WmsBussinessCodeManager wmsBussinessCodeManager, WorkflowManager workflowManager,
			WmsRuleManager wmsRuleManager) {
		this.wmsBussinessCodeManager = wmsBussinessCodeManager;
		this.workflowManager = workflowManager;
		this.wmsRuleManager = wmsRuleManager;
	}
	public void importItem(File file) {
		int rows = 0;
		int cols=0;
		Workbook book;
		try {
			book = Workbook.getWorkbook(file);//获取Excel表
			Sheet sheet = book.getSheet(0);//获取工作表
			rows = sheet.getRows();//获取行数
			cols=sheet.getColumns();//获取列数
			Map<String, Object> colNames=new HashMap<String, Object>();//存储列名信息
			for (int i = 0; i < cols; i++) {
				if(sheet.getCell(i, 0).getContents()!=null&&!sheet.getCell(i, 0).getContents().equals("")){
					colNames.put(sheet.getCell(i, 0).getContents().trim(), i);
				}			
			}	
			WmsOrganization company=(WmsOrganization) commonDao.findByQuery("From WmsOrganization company Where company.id=1").get(0);
			int j=1;
			for (int i = 0; i < rows; i++) {
				String status=sheet.getCell((Integer)colNames.get("STATUS"), i).getContents();
				if(sheet.getCell(0, i).getContents().trim() == null || sheet.getCell(0, i).getContents().trim().equals("")){
		  			continue;	  			
		  		}
				if(status.trim().equals("ENABLED")){
					WmsItem item=EntityFactory.getEntity(WmsItem.class);				
					item.setCompany(company);
					item.setCode(sheet.getCell((Integer)colNames.get("CODE"), i).getContents());
					item.setName(sheet.getCell((Integer)colNames.get("NAME"), i).getContents());
					item.setBaseUnit(sheet.getCell((Integer)colNames.get("BASE_UNIT"), i).getContents());					
					String validPeriod=sheet.getCell((Integer)colNames.get("VALID_PERIOD"), i).getContents();
					item.setValidPeriod(Integer.parseInt(validPeriod));
					item.setAlertLeadingDays(Integer.parseInt(sheet.getCell((Integer)colNames.get("ALERT_DAY"), i).getContents()));
					item.setClass1(sheet.getCell((Integer)colNames.get("CLASS1"), i).getContents());
					item.setClass2(sheet.getCell((Integer)colNames.get("CLASS2"), i).getContents());
					item.setClass3(sheet.getCell((Integer)colNames.get("CLASS3"), i).getContents());
					item.setStatus(sheet.getCell((Integer)colNames.get("STATUS"), i).getContents());
					item.setPrecision(0);
					commonDao.store(item);
					System.out.println(j++);
				}				
			}				
		} catch (Exception e) {
			System.out.println("导入失败！");
			e.printStackTrace();
		}
	}

	public void importPackageUnit(File file) {
		int rows = 0;
		int cols=0;
		Workbook book;
		Map<String, Object> colNames=null;
		List<WmsPackageUnit> units=null;
		int j=0;		
		try {
			book = Workbook.getWorkbook(file);//获取Excel表
			Sheet sheet = book.getSheet(0);//获取工作表
			rows = sheet.getRows();//获取行数
			cols=sheet.getColumns();//获取列数
			colNames=new HashMap<String, Object>();//存储列名信息
			units=new ArrayList<WmsPackageUnit>();			
			for (int i = 0; i < cols; i++) {
				if(sheet.getCell(i, 0).getContents()!=null&&!sheet.getCell(i, 0).getContents().equals("")){
					colNames.put(sheet.getCell(i, 0).getContents().trim(), i);
				}			
			}			
			for (int i = 0; i < rows; i++) {			
				if(sheet.getCell(0, i).getContents().trim() == null || sheet.getCell(0, i).getContents().trim().equals("")){
		  			continue;	  			
		  		}
				String code=sheet.getCell((Integer)colNames.get("CODE"), i).getContents();
				String hql="Select item.id From WmsItem item Where item.code=:code";
				List<Long> items=commonDao.findByQuery(hql, new String[]{"code"}, new Object[]{code});
				if(items!=null&&items.size()>0){
					WmsItem item=commonDao.get(WmsItem.class, items.get(0));
					WmsPackageUnit unit=EntityFactory.getEntity(WmsPackageUnit.class);
					unit.setItem(item);
					
					unit.setLineNo(Integer.parseInt(sheet.getCell((Integer)colNames.get("LINE_NO"), i).getContents()));					
					unit.setUnit(sheet.getCell((Integer)colNames.get("UNIT"), i).getContents());
					if(unit.getLineNo()==1){
						unit.setLevel("件");
					}else if(unit.getLineNo()==2){
						unit.setLevel("箱");
					}	
					unit.setLength(((NumberCell)sheet.getCell((Integer)colNames.get("LENGTH"), i)).getValue());
					unit.setWidth(((NumberCell)sheet.getCell((Integer)colNames.get("WIDTH"), i)).getValue());
					unit.setHeight(((NumberCell)sheet.getCell((Integer)colNames.get("HEIGHT"), i)).getValue());
					unit.setWeight(Double.valueOf((sheet.getCell((Integer)colNames.get("WEIGHT"), i)).getContents()));
					unit.setVolume(Double.valueOf((sheet.getCell((Integer)colNames.get("VOLUME"), i)).getContents()));
					unit.setConvertFigure(Integer.parseInt(sheet.getCell((Integer)colNames.get("CONVERT_FIGURE"), i).getContents()));
					units.add(unit);					
					commit(units);
					units.clear();					
					j++;
					System.out.println(j+sheet.getCell((Integer)colNames.get("ID"), i).getContents());
				}				
			}				
		} catch (Exception e) {
			System.out.println("导入失败！");
			e.printStackTrace();
		}
		
	}    
	public void commit(List<WmsPackageUnit> units) {
		for (WmsPackageUnit unit:units) {
			commonDao.store(unit);
		}		
	}	
}