package com.nuctech.util;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

/**
 * 资源文件工具类
 * @author nuctech
 *
 */
public class MessageResourceUtil {

	public static Locale getLocale() {
		HttpServletRequest request = ServletActionContext.getRequest();
		return request.getLocale();
	}

	/**
	 * 得到国际化信息
	 * 
	 * @param obj
	 * @return value
	 */
	public static String getMessageInfo(String obj) {
		Locale locale = getLocale();
		String baseName = ServletActionContext.getServletContext().getInitParameter("javax.servlet.jsp.jstl.fmt.localizationContext");
		ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
		String value = bundle.getString(obj);
		return value;
	}
}
