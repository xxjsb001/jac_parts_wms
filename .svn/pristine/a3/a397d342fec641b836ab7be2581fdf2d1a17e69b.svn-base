package com.vtradex.wms.server.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.vtradex.thorn.server.model.security.User;
import com.vtradex.thorn.server.security.acegi.holder.SecurityContextHolder;
import com.vtradex.thorn.server.util.Constant;

public class SessionObjectResolveFilter implements Filter{

	public void init(FilterConfig arg0) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession();
		User currentUser = (User)session.getAttribute(Constant.USER_IN_SESSION);
		System.out.println(Thread.currentThread());
//		SessionHolder.setSession(session);
		SecurityContextHolder.setCurrentUser(currentUser);
		chain.doFilter(request,response);
	}

	public void destroy() {
	}

}
