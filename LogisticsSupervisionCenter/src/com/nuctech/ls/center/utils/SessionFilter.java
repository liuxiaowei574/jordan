package com.nuctech.ls.center.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.ContextLoader;

import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.system.LsSystemUserLogBO;
import com.nuctech.ls.model.service.SystemUserLogService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

public class SessionFilter implements Filter {
	// private UserManagementService userManagementService;

	 public static final List<String> excludedPages = new ArrayList<String>();
	
	 {
		excludedPages.add("/");// 直接访问项目
		excludedPages.add("/security/login.action");// 登录首页登录请求action
		excludedPages.add("/login.jsp");// 访问登录首页面
		excludedPages.add("/sessionTimeOut.jsp");// 会话超时不会记录session用户
		excludedPages.add("/security/exitSystem.action");
	 }

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filter)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		request.getSession().setAttribute("userLocale", request.getLocale().getCountry().equals("") ? request.getLocale().getLanguage() : request.getLocale().getLanguage() + "_" + request.getLocale().getCountry());
		String uri = request.getRequestURI();
		String url = request.getRequestURL().toString();
		String contextPath = request.getContextPath();
		int i = url.indexOf(contextPath);
		String host = url.substring(0, i);
		if (needLogin(uri)) {
			HttpSession session = request.getSession();
			SessionUser sessionUser = (SessionUser) session.getAttribute(Constant.SESSION_USER);
			// 如果session中的user对象为null则认为会话已经失效
			if (sessionUser == null) {
				String loginPath = host + contextPath + "/login.jsp";
				// 设置重定向到登录页面，并且保存一个会话超时的数据信息，这里暂时取不到会话超时的用户ID信息
//				String loginPath = request.getContextPath() + "/login.jsp";
				response.sendRedirect(loginPath);
				return;
			}
			SystemUserService systemUserService = ContextLoader.getCurrentWebApplicationContext()
					.getBean(SystemUserService.class);
			LsSystemUserBO systemUser = systemUserService.findById(sessionUser.getUserId());
			String dbToken = systemUser.getToken();
			if (dbToken == null || !dbToken.equals(systemUser.getToken())) {// 用户被踢出
				// 设置重定向到登录页面，并且保存一个会话超时的数据信息，这里暂时取不到会话超时的用户ID信息
				String loginPath = host + contextPath + "/login.jsp";
				response.sendRedirect(loginPath);
				return;
			}
			String onlineUserLogId = (String) request.getSession().getAttribute(Constant.USER_LOG_ID);
			if (!NuctechUtil.isNull(onlineUserLogId)) {
				SystemUserLogService systemUserLogService = ContextLoader.getCurrentWebApplicationContext()
						.getBean(SystemUserLogService.class);
				LsSystemUserLogBO systemUserLog = systemUserLogService.findById(onlineUserLogId);
				systemUserLog.setLogoutTime(new Date());
				systemUserLogService.modify(systemUserLog);
			}
			filter.doFilter(servletRequest, servletResponse);
		} else {
			filter.doFilter(servletRequest, servletResponse);
		}

	}
	
	/**
	 * 需要登录的URL
	 * 
	 * @param url
	 * @return
	 */
	private boolean needLogin(String url){
    	if(excludedPages.contains(url)){
    		return false;
    	}else{
    		for (String notNeedLoginPage : excludedPages) {
				if(notNeedLoginPage.length()>1 && url.endsWith(notNeedLoginPage)){
					return false;
				}
			}
    		return true;
    	}
    	
    }

	@Override
	public void init(FilterConfig fc) throws ServletException {
		// WebApplicationContext springContext = WebApplicationContextUtils
		// .getWebApplicationContext(fc.getServletContext());
		// userManagementService =
		// springContext.getBean("userManagementService",UserManagementService.class);
	}
}
