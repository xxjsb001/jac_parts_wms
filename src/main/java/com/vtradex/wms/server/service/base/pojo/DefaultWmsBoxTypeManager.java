package com.vtradex.wms.server.service.base.pojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.exception.ExceptionLog;
import com.vtradex.thorn.server.model.security.User;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.LotInfo;
import com.vtradex.wms.server.model.inventory.WmsInventory;
import com.vtradex.wms.server.model.inventory.WmsInventoryExtend;
import com.vtradex.wms.server.model.inventory.WmsItemKey;
import com.vtradex.wms.server.model.organization.WmsEnumType;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.model.organization.WmsPackageUnit;
import com.vtradex.wms.server.model.receiving.WmsASN;
import com.vtradex.wms.server.model.warehouse.WmsBoxType;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.warehouse.WmsWarehouseArea;
import com.vtradex.wms.server.service.base.WmsBoxTypeManager;
import com.vtradex.wms.server.service.receiving.WmsASNManager;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.utils.StringHelper;

public class DefaultWmsBoxTypeManager 
		extends DefaultBaseManager implements WmsBoxTypeManager{

	@Override
	public void importBoxType(File file) {
		Integer error = 0;
		String name = file.getName();
		if (file == null) {
			throw new BusinessException("file.not.found");
		} else if (!name.substring(name.lastIndexOf('.') + 1,
				name.lastIndexOf('.') + 4).equals("xls")) {
			throw new BusinessException("this.file.error");
		}

		Sheet dataSheet = null;
		List<Cell[]> cellArray = new ArrayList<Cell[]>();
		try {
			Workbook wb = Workbook
					.getWorkbook(new java.io.FileInputStream(file));
			dataSheet = wb.getSheet(0);

			int rowNum = dataSheet.getRows();
			for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
				cellArray.add(dataSheet.getRow(rowIndex));
				if (cellArray.size() > 0 && cellArray.size() % 50 == 0) {
					error += this.resolveBoxTypeJac(cellArray);
					cellArray.clear();
				}
			}
			if (cellArray.size() > 0 && cellArray.size() < 50) {
				error += this.resolveBoxTypeJac(cellArray);
				cellArray.clear();
			}
		} catch (Exception e1) {

		}
		LocalizedMessage.addMessage("导入失败" + error + "条,请查看日志");
	}
	
	public int resolveBoxTypeJac(List<Cell[]> strList) throws ParseException {
		StringBuffer logBuffer = new StringBuffer("");
		Integer errorNum = 0;
		Cell[] strArr;
		
		for (int i = 0; i < strList.size(); i++) {
			String lineNum = (i+1)+"";
			try {
				strArr = strList.get(i);
				String code = strArr[0].getContents();//编码
				String lengthStr = strArr[1].getContents();// 长
				String widthStr = strArr[2].getContents();// 宽
				String hightStr = strArr[3].getContents();// 高
				String volumeStr = strArr[4].getContents();// 体积
				String weightStr = strArr[5].getContents();//重量
				String type = strArr[6].getContents();// 箱型

				Double length = StringHelper.replaceStrToDouble(lengthStr);
				Double width = StringHelper.replaceStrToDouble(widthStr);
				Double hight = StringHelper.replaceStrToDouble(hightStr);
				Double volume = StringHelper.replaceStrToDouble(volumeStr);
				Double weight = StringHelper.replaceStrToDouble(weightStr);
				
				if(StringHelper.isNullOrEmpty(code)){
					errorNum++;
					logBuffer.append(lineNum + "行编码不能为空/");
					// log.debug(lineNum+",1 error \n");
					continue;
				}
				String hql = "from WmsBoxType w where w.code=:code";
				List<WmsBoxType> w = commonDao.findByQuery(hql,"code",code);
				if(w.size() > 0){
					errorNum++;
					logBuffer.append(lineNum + "行编码已经存在/");
					// log.debug(lineNum+",1 error \n");
					continue;
				}
				
				if(StringHelper.isNullOrEmpty(type)){
					errorNum++;
					logBuffer.append(lineNum + "行箱型不能为空/");
					// log.debug(lineNum+",1 error \n");
					continue;
				}
				hql = "from WmsEnumType w where enumValue=:enumValue";
				List<WmsEnumType> enumTypes = commonDao.findByQuery(hql,"enumValue",type);
				if(enumTypes.size() <= 0){
					errorNum++;
					logBuffer.append(lineNum + "行箱型没有找到对应的枚举值/");
					// log.debug(lineNum+",1 error \n");
					continue;
				}
				WmsBoxType box = new WmsBoxType(code, length, width, hight, volume, 
									weight, BaseStatus.ENABLED, type, enumTypes.get(0));
				commonDao.store(box);
				
			} catch (Exception e) {
				logBuffer.append(lineNum + "行导入失败/");
				errorNum++;
				logger.error("", e);
			}
		}
		ExceptionLog log = EntityFactory.getEntity(ExceptionLog.class);
		if (UserHolder.getUser() != null) {
			log.setOperUserId(UserHolder.getUser().getId());
			log.setOperUserName(UserHolder.getUser().getName());
		} else {
			User user = this.commonDao.load(User.class, new Long(1));
			log.setOperUserId(user.getId());
			log.setOperUserName(user.getName());
		}
		log.setType("箱型导入");
		log.setOperDate(new Date());
		log.setOperException(logBuffer.toString());
		log.setOperExceptionMess("");
		log.setOperPageId("maintainWmsBoxTypePage");
		log.setOperPageName("箱型管理");
		log.setOperComponentId("箱型导入");
		log.setOperComponentName("importBoxType");
		if (org.apache.commons.lang.StringUtils.isEmpty(log.getOperException())) {
			log.setStrExtend1("成功");
		} else {
			log.setStrExtend1("失败");
		}
		commonDao.store(log);
		return errorNum;
	}
	
	
}
