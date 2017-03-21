package com.nuctech.ls.center.utils;

import java.io.IOException;
import java.util.ArrayList;
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
        excludedPages.add("/needLogin.jsp");// 重新登录页面
		excludedPages.add("/security/exitSystem.action"); //登出请求action
	 }

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filter)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
        request.getSession().setAttribute("userLocale", setUserLocal(request));
        
		String uri = request.getRequestURI();
		String url = request.getRequestURL().toString();
		String contextPath = request.getContextPath();
		int i = url.indexOf(contextPath);
		String host = url.substring(0, i);
		String targetUrl = uri.substring(contextPath.length());
		if (needLogin(targetUrl)) {
			HttpSession session = request.getSession();
			SessionUser sessionUser = (SessionUser) session.getAttribute(Constant.SESSION_USER);
			// 如果session中的user对象为null则认为会话已经失效
			if (sessionUser == null) {
				// 判断是否为ajax请求，若为ajax请求，则返回json串（支持JQuery）
				if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                    response.setCharacterEncoding("UTF-8");
                    response.getOutputStream().write("{\"forwardTo\":\"sessionTimeOut.jsp\"}".getBytes("UTF-8"));
                } else {
                    String loginPath = host + contextPath + GlobalConstants._SESSION_TIMEOUT;
                    response.sendRedirect(loginPath);
                }
				return;
			}
			// 如果一个账户在不同地点登录，前者则被迫下线。
			if (!UserOnlineListener.isOnline(session)) {
				// 判断是否为ajax请求，若为ajax请求，则返回json串（支持JQuery）
				if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                    response.setCharacterEncoding("UTF-8");
                    response.getOutputStream().write("{\"forwardTo\":\"needLogin.jsp\"}".getBytes("UTF-8"));
                } else {
    				String loginPath = host + contextPath + GlobalConstants._NEED_LOGIN;
    				response.sendRedirect(loginPath);
                }
                return;
			}
			filter.doFilter(servletRequest, servletResponse);
		} else {
			filter.doFilter(servletRequest, servletResponse);
		}
	}

	/**
	 * 设置客户端语言
	 * @param request
	 * @return
	 */
	private String setUserLocal(HttpServletRequest request) {
		String userLocale = request.getParameter("language");
        if(NuctechUtil.isNull(userLocale)) {
        	userLocale = (String) request.getSession().getAttribute("userLocale");
        	if(NuctechUtil.isNull(userLocale)) {
        		userLocale = request.getLocale().getCountry().equals("") ? request.getLocale().getLanguage() : request.getLocale().getLanguage() + "_" + request.getLocale().getCountry();
        		if(NuctechUtil.isNull(userLocale)) {
        			userLocale = "en_US";
        		}
        	}
        }
		return userLocale;
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
	public void init(FilterConfig filterConfig) throws ServletException {
		// WebApplicationContext springContext = WebApplicationContextUtils
		// .getWebApplicationContext(fc.getServletContext());
		// userManagementService =
		// springContext.getBean("userManagementService",UserManagementService.class);
	}
}
