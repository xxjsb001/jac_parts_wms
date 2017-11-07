package com.vtradex.wms.server.web.servlet;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vtradex.wms.server.service.rule.WmsTransactionalManager;


/**
 * yc.min
 * @see SearchServlet,TMSMainFrameServlet
 */
public class RfServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	protected static ApplicationContext ac;
	protected static WmsTransactionalManager wmsTransactionalManager;
	protected final String CHARACTER = "utf-8";
	
	public void init(ServletConfig sc) throws ServletException {
		super.init(sc);
		ac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc.getServletContext());
		wmsTransactionalManager = (WmsTransactionalManager) ac.getBean("wmsTransactionalManager");
	}
	 
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException,ServletException  {
			doPost(req , res);
		}
	
	protected void doPost(final HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding(CHARACTER);
		String method = null,value = null;
		try {
			method = URLDecoder.decode(request.getParameter("methodName"),CHARACTER);
			value = URLDecoder.decode(request.getParameter("value"),CHARACTER);
			if("RuleTable.onLine".equals(method)){
				wmsTransactionalManager.uplineRuleTableRf(Long.parseLong(value));
			}
			response.getOutputStream().write("success".getBytes());
			System.out.println(method);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		returnRequest(response,responseContext);
		
	}
	/** 响应请求 */
	protected void returnRequest(HttpServletResponse response,String returnRequest) throws IOException {
		response.setCharacterEncoding(CHARACTER);
		response.getWriter().write(returnRequest);
		response.getWriter().flush();
	}
}
