package com.vtradex.wms.server.web.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vtradex.thorn.server.util.Constant;
import com.vtradex.thorn.server.model.security.User;
import com.vtradex.wms.server.service.middle.MilldleTableManager;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.ReportName;
/**
 * yc.min
 * @see SearchServlet,TMSMainFrameServlet,RfServlet
 */
public class ReportButtonServlet extends HttpServlet {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6350341816422535974L;
	protected final Log logger = LogFactory.getLog(this.getClass());
	protected ApplicationContext ac;
	
	public void init(ServletConfig sc) throws ServletException {
		super.init(sc);
		ac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc.getServletContext());
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		User loginUser = (User)req.getSession().getAttribute(Constant.USER_IN_SESSION);
		String printName = loginUser.getName();
		req.setCharacterEncoding("UTF-8");
		// 获得当前打印报表的名称和参数的ID
		String[] param = req.getParameter("param").split(";");
		String parentIds="";
		String raq = "";
		//把获取的参数值保存到一个Map
		Map<Object, Object> maps = new HashMap<Object, Object>();
		for (String pr : param){
			String[] prs = pr.split("=");
			if (prs[0].equals("parentIds")){
				parentIds = prs[1];
			}
			if (prs[0].equals("raq")){
				raq = prs[1];
			}
			
			maps.put(prs[0], prs.length>1?prs[1]:null);
		}
		if(raq.equals(ReportName.jacMesDisWms)){				
			//[MATERIAL_CODE=, DELIVERY_CODE=JAC2016032333027-01, raq=jacMesDisWms.raq]
			String mes = (String) maps.get("DELIVERY_CODE");
			String item = (String) maps.get("MATERIAL_CODE");
			MilldleTableManager milldleTableManager = (MilldleTableManager)ac.getBean("milldleTableManager");
			logger.error("补发MES签收WMS未发运 3>2 "+printName+"--------------------01---"+JavaTools.format(new Date(), JavaTools.dmy_hms));
			try {
				milldleTableManager.sysPickShipBack(mes, item);
			} catch (Exception e) {
				logger.error("", e);
			}
			logger.error("补发MES签收WMS未发运 3>2 "+printName+"--------------------02---"+JavaTools.format(new Date(), JavaTools.dmy_hms));
		}else if(raq.equals(ReportName.jacMesDisWms7)){
			//[MATERIAL_CODE=, DELIVERY_CODE=JAC2016032333027-01, raq=jacMesDisWms7.raq]
			String mes = (String) maps.get("DELIVERY_CODE");
			String item = (String) maps.get("MATERIAL_CODE");
			MilldleTableManager milldleTableManager = (MilldleTableManager)ac.getBean("milldleTableManager");
			logger.error("更新MES签收WMS无拣货任务 7>2 "+printName+"--------------------01---"+JavaTools.format(new Date(), JavaTools.dmy_hms));
			try {
				milldleTableManager.updateFlag7(mes, item);
			} catch (Exception e) {
				logger.error("", e);
			}
			logger.error("更新MES签收WMS无拣货任务 7>2 "+printName+"--------------------02---"+JavaTools.format(new Date(), JavaTools.dmy_hms));
		}
		else{
			System.out.println("其他...."+printName);
		}
		
	}
	
	protected void doGet(final HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		this.doPost(req, res);
	}

}
