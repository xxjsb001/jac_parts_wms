package com.vtradex.wms.server.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vtradex.thorn.server.exception.ThornServerRuntimeException;
import com.vtradex.thorn.server.model.security.User;
import com.vtradex.thorn.server.security.acegi.holder.SecurityContextHolder;
import com.vtradex.thorn.server.service.security.SecurityFilter;
import com.vtradex.thorn.server.service.security.UserManager;
import com.vtradex.thorn.server.servlet.MainFrameServlet;
import com.vtradex.thorn.server.util.Constant;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.service.security.WmsWarehouseManager;

/**
 * 
 * @author <a href="yan.li@vtradex.com">李炎</a>
 * @description 
 * 
 */

public class WmsMainFrameServlet extends MainFrameServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	protected void doPost(final HttpServletRequest req,HttpServletResponse res)throws ServletException, IOException{
//		if(requireSetupLicense()){
//			res.sendRedirect("setupLicense.html");
//		}
		if(isSSO(req)){
			sso(req,res);
		}
		else{
			if(req.getSession(false) != null){
				req.getSession(false).invalidate();
				res.sendRedirect("mainFrame.html");
			}else {
				InputStream inputStream =  Thread.currentThread().getContextClassLoader().getResourceAsStream(getMainFrameHtmlPath());
				byte[] contents = new byte[inputStream.available()];
				inputStream.read(contents);
				res.getOutputStream().write(contents);
				res.getOutputStream().flush();
				res.getOutputStream().close();
				inputStream.close();
			}
		}
	}
	
	protected boolean isSSO(final HttpServletRequest req){
		Map parameterMap = req.getParameterMap();
		return parameterMap.containsKey(SINGLE_SIGN_ON);
	}
	
	protected void sso(final HttpServletRequest req,HttpServletResponse res)throws ServletException, IOException{
		String userId = req.getParameter(SINGLE_SIGN_ON);
		userId = decodingUserId(userId);
		final UserManager userManager = (UserManager) ac.getBean("userManager");
		User user = userManager.retrieve(userId);
		if(doAuthenticate(user)){
			res.sendRedirect("SSOError.html");
			return;
		}
		SecurityFilter sf = buildSecurityFilter(user,user.getReferenceModel(),user.getLocale().getLanguage());
		sf.addHql("switchWareHousePage.loadWarehouseDefault", "FROM WmsWarehouse wh WHERE 1=1 AND wh.status = 'ENABLED'");
		req.getSession().setAttribute(Constant.SECURITY_FILTER_IN_SESSION , sf);
		req.getSession().setAttribute(Constant.USER_IN_SESSION , user);
		req.getSession().setAttribute(Constant.MODELTYPE , user.getReferenceModel());
		req.getSession().setAttribute(Constant.LOCALE , user.getLocale().getLanguage());
		req.getSession().setAttribute(Constant.MODULE , "wms");
		SecurityContextHolder.addSecurityContext(req.getSession(false));
		
		final WmsWarehouse wh = queryWarehouse(user.getLoginName());
		req.getSession().setAttribute(WMSLoginServlet.WMS_SESSION_WAREHOUSE, wh);//当前的仓库
		req.getSession().setAttribute("SESSION_WAREHOUSE_NAME", wh.getName());
		
		res.sendRedirect("index.htm");
	}
	
	private WmsWarehouse queryWarehouse(String loginName) {
		WmsWarehouseManager wmsWarehouseManager = (WmsWarehouseManager) ac.getBean("wmsWarehouseManager");
		WmsWarehouse wh = wmsWarehouseManager.getDefaultLoginWmsWarehouse();
		if (wh == null) {
			throw new ThornServerRuntimeException("No warehouse avaliable for user [" + loginName + "].");
		}
		return wh ;
	}

	protected String getMainFrameHtmlPath() {
		return "com"+System.getProperty("file.separator")+
		"vtradex"+System.getProperty("file.separator")+
		"wms"+System.getProperty("file.separator")+
		"server"+System.getProperty("file.separator")+
		"web"+System.getProperty("file.separator")+
		"servlet"+System.getProperty("file.separator")+
		"mainFrame.html";
	}

}


