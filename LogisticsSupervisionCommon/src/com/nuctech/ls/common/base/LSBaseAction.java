/**
 * 
 */
package com.nuctech.ls.common.base;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.common.page.PageQueryFactory;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Action 基类
 * 
 * @author sunming
 *
 */
@ParentPackage("nuctech-struts-base")
@SuppressWarnings("serial")
public class LSBaseAction extends ActionSupport 
		implements SessionAware, ServletRequestAware, ServletResponseAware {
	
	/**
     * 请求action返回信息
     */
    public String message;
    /**
     * 请求action返回结果
     */
    public boolean result;
	
	/**
	 * 获取主键
	 * 
	 * @return
	 */
	public String generatePrimaryKey() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	/**
	 * 内置的request对象，由容器注入。
	 */
	protected HttpServletRequest request;
	/**
	 * 内置的response对象，由容器注入。
	 */
	protected HttpServletResponse response;
	/**
	 * 内置的session对象，由容器注入。
	 */
	protected Map<String, Object> session;

	/**
	 * 设置Session Map对象，由容器调用。
	 * 
	 * @param att
	 *            被设置的Session Map对象。
	 */
	@Override
	public void setSession(Map<String, Object> att) {
		this.session = att;
	}

	/**
	 * 设置HttpServletRequest实例，由容器调用
	 * 
	 * @param request
	 *            被设置的HttpServletRequest实例
	 */
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * 设置response实例，供容器调用
	 * 
	 * @param response
	 *            被设置的HttpServletResponse实例
	 */
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * 
	 * 
	 * @param locale
	 * @return
	 */
	private ResourceBundle getResourceBundle(Locale locale) {
		String baseName = ServletActionContext.getServletContext().getInitParameter("javax.servlet.jsp.jstl.fmt.localizationContext");
		ResourceBundle rb = ResourceBundle.getBundle(baseName);
		return rb;
	}

	protected PageQuery<Map> pageQuery = null;
	
	public PageQuery newPageQuery(String defaultSortColumns) {
		return PageQueryFactory.newPageQuery(ServletActionContext.getRequest(), defaultSortColumns);
	}
	
	/**
	 * 获取国际化内容
	 * 
	 * @param messageKey
	 * @return
	 */
	protected String getLocaleString(String messageKey) {
		return getResourceBundle(request.getLocale()).getString(messageKey);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public PageQuery<Map> getPageQuery() {
		return pageQuery;
	}

	public void setPageQuery(PageQuery<Map> pageQuery) {
		this.pageQuery = pageQuery;
	}

}
