package com.vtradex.wms.server.web.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vtradex.thorn.client.exception.BaseAlertException;
import com.vtradex.thorn.server.action.ConfigAction;
import com.vtradex.thorn.server.security.acegi.holder.SecurityContextHolder;
import com.vtradex.thorn.server.servlet.LoginServlet;
import com.vtradex.wms.client.ui.page.service.CustomService;
import com.vtradex.wms.server.model.warehouse.WmsWarehouse;
import com.vtradex.wms.server.service.security.WmsWarehouseManager;

@SuppressWarnings("unchecked")
public class ChangeWarehouseServlet extends RemoteServiceServlet implements CustomService{
	private static final long serialVersionUID = 650011292923529192L;
	
	protected ApplicationContext ac;
	 
	public void init(ServletConfig sc) throws ServletException {
		super.init(sc);
		ac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc.getServletContext());
	}

	public Map<String, Object> swichWarehouse(Long warehouseId) throws Exception {
		Long wId = Long.valueOf(warehouseId);
		
		if (isSameWarehouse(wId,this.getThreadLocalRequest())) {
			throw new BaseAlertException("未切换仓库！");
		}
		
		Object sessionWH = this.getThreadLocalRequest().getSession(false).getAttribute("WMS_SESSION_WAREHOUSE");
		
		if (sessionWH == null) {
			throw new BaseAlertException("session错误！");
		}
		
		WmsWarehouseManager wm = (WmsWarehouseManager)ac.getBean("wmsWarehouseManager");
		WmsWarehouse warehouse = wm.load(WmsWarehouse.class,wId);
		
		if (warehouse == null) {
			throw new BaseAlertException("所选仓库错误！");
		}
		
		this.getThreadLocalRequest().getSession(false).removeAttribute(WMSLoginServlet.WMS_SESSION_WAREHOUSE);
		this.getThreadLocalRequest().getSession(false).setAttribute(WMSLoginServlet.WMS_SESSION_WAREHOUSE, warehouse);
		this.getThreadLocalRequest().getSession(false).removeAttribute(WMSLoginServlet.SESSION_WAREHOUSE_NAME);
		this.getThreadLocalRequest().getSession(false).setAttribute(WMSLoginServlet.SESSION_WAREHOUSE_NAME, warehouse.getName());
//		this.getThreadLocalRequest().getSession(false).removeAttribute(WMSLoginServlet.SESSION_WAREHOUSE_ID);
//		this.getThreadLocalRequest().getSession(false).setAttribute(WMSLoginServlet.SESSION_WAREHOUSE_ID,warehouse.getId());
		
		SecurityContextHolder.clearAllSession();
		SecurityContextHolder.addSecurityContext(this.getThreadLocalRequest().getSession(false));
		
		//重新初始化全局参数
		ConfigAction configAction = (ConfigAction) ac.getBean("configAction");
		configAction.setReferenceModel(this.getThreadLocalRequest().getSession().getAttribute(LoginServlet.MODELTYPE).toString());
		
		Map<String, Object> results = new HashMap<String, Object>();
		Map globalMap = configAction.reInitGlobalParams(new HashMap());
		
		results.put("globals",globalMap);
		results.put("warehouseName",warehouse.getName());
		
		return results;
	}
	 
	private boolean isSameWarehouse(Long id,HttpServletRequest req) {
		WmsWarehouse w = (WmsWarehouse) req.getSession(false).getAttribute(WMSLoginServlet.WMS_SESSION_WAREHOUSE);
		
		Assert.notNull(w, "Transfer OrgWarehouse to  DesWarehouse.id = " + id);
		
		if (w.getId() == id) {
			return true;
		} else {
			return false;
		}
	}
}