package com.vtradex.wms.server.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vtradex.thorn.server.dao.CommonDao;
import com.vtradex.thorn.server.exception.AuthenticationException;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.exception.ThornServerRuntimeException;
import com.vtradex.thorn.server.model.security.User;
import com.vtradex.thorn.server.security.acegi.holder.SecurityContextHolder;
import com.vtradex.thorn.server.service.security.SecurityFilter;
import com.vtradex.thorn.server.servlet.LoginServlet;
import com.vtradex.thorn.server.util.Constant;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.service.security.WmsWarehouseManager;

public class WMSLoginServlet extends LoginServlet {
	private static final long serialVersionUID = 7483681481829020964L;
	/** 当前仓库*/
	public static final String WMS_SESSION_WAREHOUSE = "WMS_SESSION_WAREHOUSE"; //key=SESSION_WAREHOUSE
	public static final String SESSION_WAREHOUSE_NAME = "SESSION_WAREHOUSE_NAME";
	public static String SESSION_ORGANIZATION_CODE = "SESSION_ORGANIZATION_CODE";
	
	protected CommonDao commonDao;
	
	public void init(ServletConfig sc) throws ServletException {
		super.init(sc);
		commonDao = (CommonDao)ac.getBean("commonDao");
	}
	
	protected void doPost(final HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		String loginName = req.getParameter("login_name");
		String password = req.getParameter("password");
		final String referenceModel = req.getParameter("referenceModel");
		final String locale = req.getParameter("locale");
		
		loginName = decodeParams(loginName);
		password = decodeParams(password);
		User loginUser = null;
		
		try {
			loginUser = authenticateUser(loginName,password,referenceModel,locale);
			
		} catch (AuthenticationException e) {
//			e.printStackTrace();
			String hintMsg = LocalizedMessage.getLocalizedMessage(e.getClass().getSimpleName(),referenceModel,locale);
			res.getOutputStream().write(hintMsg.getBytes("UTF-8"));
			return;
		}
		
		SecurityFilter sf = buildSecurityFilter(loginUser,referenceModel,locale);
		sf.addHql("switchWareHousePage.loadWarehouseDefault", "FROM WmsWarehouse wh WHERE 1=1 AND wh.status = 'ENABLED'");
		req.getSession().setAttribute(Constant.SECURITY_FILTER_IN_SESSION , sf);
		req.getSession().setAttribute(Constant.USER_IN_SESSION , loginUser);
		req.getSession().setAttribute(Constant.MODELTYPE , referenceModel);
		req.getSession().setAttribute(Constant.LOCALE , locale);
		req.getSession().setAttribute(Constant.MODULE , "wms");
		req.getSession().setAttribute(SESSION_ORGANIZATION_CODE, checkOrganizationCode(loginUser.getStrExtend1()));
		SecurityContextHolder.addSecurityContext(req.getSession(false));
		
//		final WmsWarehouse wh = queryWarehouse(loginName);
		final List<WmsWarehouse> whs = queryWarehouse(loginUser);
		if(whs.size() <= 0){
			String hintMsg = "用户"+loginName+"没有在仓库用户关系表里维护,无法登录";
			res.getOutputStream().write(hintMsg.getBytes("UTF-8"));
			return;
		}
		WmsWarehouse wh = whs.get(0);
		req.getSession().setAttribute(WMS_SESSION_WAREHOUSE, wh);//当前的仓库
		req.getSession().setAttribute("SESSION_WAREHOUSE_NAME", wh.getName());
		
		/**
		 * 绑定属性:字符类型
		 * 无法时默太为NULL:Constant.NULL
		 */
		res.setContentType("text/html; charset=utf-8");
		res.getOutputStream().write("success".getBytes());
	}
	
	private WmsWarehouse queryWarehouse(String loginName) {
		WmsWarehouseManager wmsWarehouseManager = (WmsWarehouseManager) ac.getBean("wmsWarehouseManager");
		WmsWarehouse wh = wmsWarehouseManager.getDefaultLoginWmsWarehouse();
		if (wh == null) {
			throw new ThornServerRuntimeException("No warehouse avaliable for user [" + loginName + "].");
		}
		return wh ;
	}
	
	private List<WmsWarehouse> queryWarehouse(User loginUser) {
		WmsWarehouseManager wmsWarehouseManager = (WmsWarehouseManager) ac.getBean("wmsWarehouseManager");
//		WmsWarehouse wh = wmsWarehouseManager.getDefaultLoginWmsWarehouse(loginUser.getId());
		List<WmsWarehouse> wh = wmsWarehouseManager.getWmsAvailableWarehousesByUserId(loginUser.getId());
//		if (wh.size() <= 0) {
//			throw new ThornServerRuntimeException("No warehouse avaliable for user [" + loginUser.getLoginName() + "].");
//			throw new BusinessException("用户 "+ loginUser.getLoginName()+" 未在仓库用户关系映射里维护,无法登录");
//		}
		return wh;
	}
	//yc.min
	private String checkOrganizationCode(String code){
		if(code==null || "".equals(code) || " ".equals(code)){
			code = "-1";
		}
		return code;
	}
}
