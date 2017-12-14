package com.vtradex.wms.server.service.mail.pojo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.format.Alignment;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTransactionManager;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.service.email.EmailManager;
import com.vtradex.wms.server.service.mail.WmsMailManager;
import com.vtradex.wms.server.service.middle.MiddleTableName;
import com.vtradex.wms.server.test.ExcelShower;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;
import com.xlsTools.XlsUtils;

public class DefaultWmsMailManager extends DefaultBaseManager implements WmsMailManager {
	public static Integer WIDTH = 12;
	@SuppressWarnings("unchecked")
	public void sendFileByEmail() {
		
		String sql = "select dd.delivery_code,dd.sender,to_char(dd.create_time,'yyyy-MM-dd hh24:mi') as create_time,"+
				"dd.creator,dd.supplier_code,dd.material_code,dd.material_mame "+
				"from "+MiddleTableName.MIDDLEDELIVERYDOCDETAIL+" dd where 1=1 and dd.company = '106' "+
				"and not exists(select 1 from middle_supplier sp where sp.supplier_code = dd.supplier_code) "+
				"order by dd.delivery_code";
		HibernateTransactionManager transactionManager = (HibernateTransactionManager)applicationContext.getBean("transactionManager");
		Session session = transactionManager.getSessionFactory().openSession();
		try {
			List<Object[]> objs1 = session.createSQLQuery(sql).list();
			if(objs1!=null && objs1.size()>0){
				String title = "发货中间表失败(供应商不存在)通知"+JavaTools.format(new Date(), JavaTools.dmy_hm);
				String[] title1 = {title};
				String[] title2 = {"MES单号","发送方","创建日期","创建人","供应商编码","物料号","物料名称"};
				Integer[] length = {15,10,15,10,10,20,35};
				String fileName = "fdj-wms-001.xls";
				String localPath = ".."+File.separator;
				File file = new File(localPath+fileName);
				//指定列对齐方式
				Map<Integer,Alignment> aligns = new HashMap<Integer, Alignment>();
				aligns.put(0, Alignment.LEFT);
				aligns.put(6, Alignment.LEFT);
				//border:边框,mergeCell:合并
				XlsUtils.writeXls(title1,title2,length,localPath+fileName, objs1,true,true,aligns,WIDTH,null);
				//xls->> html
				ExcelShower es = new ExcelShower();
				StringBuffer html = es.read(fileName, localPath);
				
				String htmlName = "fdj-wms-001-"+JavaTools.format(new Date(), JavaTools.ymd_Hms)+".html";
				List<Object[]> values = new ArrayList<Object[]>();
				String[] ss = html.toString().split(MyUtils.spilt4);
				values.add(ss);
				JavaTools.createTxt(values, 1,localPath+htmlName,"",MyUtils.spilt4,"utf-8");
				
				List<Object[]> users = commonDao.findByQuery("select user.id,user.name,user.email from User user,Group g"
						+ " where user in elements(g.users) and g.code = 'G-SHIP'");
				if(users!=null && users.size()>0){
					EmailManager emailManager = (EmailManager) applicationContext.getBean("emailManager");
					String[] emails = new String[users.size()];
					String contextStr = "附件为发货中间表失败(供应商不存在)通知，请注意查收。谢谢！";
					int i = 0;
					for(Object[] o : users){
						emails[i] = o[2]==null?"-":o[2].toString();
						i++;
					}
					emailManager.sendFileByEmail(localPath+htmlName, emails, title, contextStr);
				}
				System.out.println(localPath+fileName);
				if(file.exists()){
					file.delete();
				}
				System.out.println(localPath+htmlName);
				File htmlFile = new File(localPath+htmlName);
				if(htmlFile.exists()){
					htmlFile.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getLocalizedMessage());
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
		
	}
	
	public void sendNotesMail(Long id){
		
	}

}
