package com.nuctech.ls.center.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * 全局常量工具类
 * 
 * @author liuchao
 *
 */
public class GlobalConstants {
    /**
     * 当前登录用户
     */
    public static final String LOGIN_USER = "loginUser";
    /**
     * 超级管理员
     */
    public static final String ADMINISTRATOR = "Administrator";
    /**
     * 允许访问的地址列表(functionDefine对象List)（左侧菜单列表）
     */
    public static final String 	ALLOW_ACCESS_ADDRESS ="allowAccessAddress"; 

    /**
     * 不需要session过期过滤的路径---1.登录页面 不需要权限判断的路径
     */
    public static final String _ROOT = "/";
    public static final String _LOGIN_PAGE = "/login.jsp";
    public static final String _INDEX = "/index.jsp";
    
    /**
     * 不需要session过期过滤的路径---2.登录action 不需要权限判断的路径
     */
    public static final String _LOGIN_URL = "/login.action";
    /**
     * 不需要session过期过滤的路径---3.退出action 不需要权限判断的路径
     */
    public static final String _LOGOUT_ACTION = "/logout.action";

    /**
     * 不需要session过期过滤的路径---4.session过期提示页面 不需要权限判断的路径
     */
    public static final String _NEED_LOGIN = "/needLogin.jsp";
    /**
     * 不需要权限判断的路径 若当前登录用户的角色与其所访问的模块不一致，则跳转到此页面提示
     */
    public static final String _NOPERMISSION = "/noPermission.jsp";
   
    /**
     * 没有登录的情况下直接跳转到登录页面而不是提示页面的路径集合
     */
    public static Set<String> _directLoginPathSet = new HashSet<String>();
	static {
		_directLoginPathSet.add(_ROOT);
		_directLoginPathSet.add(_LOGOUT_ACTION);
		_directLoginPathSet.add(_INDEX);
	}

    /**
     * 不需要权限判断的路径
     */
	public static Set<String> _noCheckPathSet = new HashSet<String>();
	static {
		_noCheckPathSet.add("/");
		_noCheckPathSet.add(_LOGIN_PAGE);
		_noCheckPathSet.add(_LOGIN_URL);
		_noCheckPathSet.add(_NEED_LOGIN);
		_noCheckPathSet.add(_NOPERMISSION);
	}
}
