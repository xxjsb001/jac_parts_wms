package com.vtradex.inport.manager.pojo;


import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;

import com.vtradex.inport.manager.WmsOrganizationTestManager;
import com.vtradex.thorn.server.dao.hibernate.HibernateCommonDao;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.UpdateInfo;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.base.Contact;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.service.rule.WmsRuleManager;

public class DefaultWmsOrganizationTestManager extends DefaultBaseManager implements WmsOrganizationTestManager {

	private WmsRuleManager wmsRuleManager;
	
	public DefaultWmsOrganizationTestManager(WmsRuleManager wmsRuleManager){
		this.wmsRuleManager = wmsRuleManager;
	}
	
    /**导入组织信息*/
	public void importCompany(File file) {
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
			for (int i = 0; i < rows; i++) {
				String status=sheet.getCell((Integer)colNames.get("STATUS"), i).getContents();
				if(sheet.getCell(0, i).getContents().trim() == null || sheet.getCell(0, i).getContents().trim().equals("")||status.equals("DISABLED")){
		  			continue;	  			
		  		}
				WmsOrganization company=EntityFactory.getEntity(WmsOrganization.class);
				company.setCode(sheet.getCell((Integer)colNames.get("CODE"), i).getContents());
				company.setName(sheet.getCell((Integer)colNames.get("NAME"), i).getContents());
				Contact contact=new Contact();
				contact.setCountry(sheet.getCell((Integer)colNames.get("C_COUNTRY"), i).getContents());
				contact.setProvince(sheet.getCell((Integer)colNames.get("C_PROVINCE"), i).getContents());
				contact.setCity(sheet.getCell((Integer)colNames.get("C_CITY"), i).getContents());
				contact.setAddress(sheet.getCell((Integer)colNames.get("C_ADDRESS"), i).getContents());
				contact.setPostCode(sheet.getCell((Integer)colNames.get("C_POSTCODE"), i).getContents());
				contact.setContactName(sheet.getCell((Integer)colNames.get("C_CONTACT_NAME"), i).getContents());
				contact.setMobile(sheet.getCell((Integer)colNames.get("C_MOBILE"), i).getContents());
				contact.setTelephone(sheet.getCell((Integer)colNames.get("C_TELEPHONE"), i).getContents());
				contact.setFax(sheet.getCell((Integer)colNames.get("C_FAX"), i).getContents());
				contact.setEmail(sheet.getCell((Integer)colNames.get("C_EMAIL"), i).getContents());
				company.setContact(contact);
				company.setBeCarrier(getBoolean(sheet.getCell((Integer)colNames.get("IS_CARRIER"), i).getContents()));
				company.setBeCustomer(getBoolean(sheet.getCell((Integer)colNames.get("IS_CUSTOMER"), i).getContents()));
				company.setBeSupplier(getBoolean(sheet.getCell((Integer)colNames.get("IS_SUPPLIER"), i).getContents()));
				company.setBeCompany(getBoolean(sheet.getCell((Integer)colNames.get("IS_COMPANY"), i).getContents()));
				company.setLotRule(null);
				company.setDescription(sheet.getCell((Integer)colNames.get("DESCRIPTION"), i).getContents());
				company.setStatus(sheet.getCell((Integer)colNames.get("STATUS"), i).getContents());
		        UpdateInfo updateinfo=new UpdateInfo();
		        String creatorId=sheet.getCell((Integer)colNames.get("CREATOR_ID"), i).getContents();
		        if(!creatorId.equals("")&&creatorId!=null){
		        	updateinfo.setCreatorId(Long.parseLong(creatorId));
		        }		        
		        updateinfo.setCreator(sheet.getCell((Integer)colNames.get("CREATOR"), i).getContents());
			    updateinfo.setCreatedTime(new Date());
				String lastOperatorId=sheet.getCell((Integer)colNames.get("LAST_OPERATOR_ID"), i).getContents();
				if(!lastOperatorId.equals("")&&lastOperatorId!=null){
					updateinfo.setLastOperatorId(Long.parseLong(lastOperatorId));
				}				
				updateinfo.setLastOperator(sheet.getCell((Integer)colNames.get("LAST_OPERATOR"), i).getContents());				
				updateinfo.setUpdateTime(new Date());
				company.setUpdateInfo(updateinfo);
				((HibernateCommonDao)commonDao).getHibernateTemplate().save(company);
				System.out.println("导入成功"+i+"条");
			}				
		} catch (Exception e) {
			System.out.println("导入失败！");
			e.printStackTrace();
		}
		
	}
	//将Y/N转换成布尔值
	private Boolean getBoolean(String s){
		if(s.equals("Y")){
			return Boolean.TRUE;
		}else{
			return Boolean.FALSE;
		}		
	}    
}
