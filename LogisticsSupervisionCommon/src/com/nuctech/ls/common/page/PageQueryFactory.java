package com.nuctech.ls.common.page;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.util.WebUtils;

import jcifs.util.transport.Request;

public class PageQueryFactory {
	protected final static Logger logger = Logger.getLogger(PageQueryFactory.class.getName());

	static int DEFAULT_PAGE_SIZE = 10;
	static int MAX_PAGE_SIZE = 50000;

	static {
		logger.info("PageRequestFactory.DEFAULT_PAGE_SIZE=" + DEFAULT_PAGE_SIZE);
		logger.info("PageRequestFactory.MAX_PAGE_SIZE=" + MAX_PAGE_SIZE);
	}

	public static <T> PageQuery<T> newPageQuery(HttpServletRequest request, String defaultSortColumns, T filters) {
		PageQuery pageQuery = newPageQuery(request, defaultSortColumns);
		pageQuery.setFilters(filters);
		return pageQuery;
	}

	public static PageQuery newPageQuery(HttpServletRequest request, String defaultSortColumns) {
		PageQuery pageQuery = new PageQuery();
		PageQuery<Map> result = bindPageQuery(pageQuery, request, defaultSortColumns);
		//稍后完善分页相关
		
//		PageQuery<Map> result = ExtremeTablePageRequestFactory.bindPageQuery(pageQuery, ExtremeTablePage.getLimit(request,
//				Integer.MAX_VALUE, defaultPageSize, getTableId(request)), defaultSortColumns);

		Map autoIncludeFilters = WebUtils.getParametersStartingWith(request, "s_");
		result.getFilters().putAll(autoIncludeFilters);

		if (result.getPageSize() > MAX_PAGE_SIZE) {
			result.setPageSize(MAX_PAGE_SIZE);
		}
		return result;
	}
	/**
	 * 绑定PageQuery的属性值
	 */
	private static PageQuery<Map> bindPageQuery(PageQuery pageQuery, HttpServletRequest request, String defaultSortColumns) {
		String offsetStr = request.getParameter("offset");
		String limitStr = request.getParameter("limit");
		int page = 0;
		int pageSize = 0;
		long offset = 0;
		long limit = 0;
		if(offsetStr!=null && limitStr!=null){
			offset = Long.parseLong(offsetStr);
			limit = Long.parseLong(limitStr);			
		}
		
		if(limit == 0L) {
			offset = 0L;
			limit = DEFAULT_PAGE_SIZE;
		}
		page = (int) (offset/limit)+1;
		pageSize = (int) limit;
		pageQuery.setPage(page);
		pageQuery.setPageSize(pageSize);
		pageQuery.setSortColumns(defaultSortColumns);
		
		Map filters = new HashMap();
		pageQuery.setFilters(filters);
		try {
			BeanUtils.copyProperties(pageQuery, filters);
		} catch (Exception e) {
			handleReflectionException(e);
		}
		return pageQuery;
	}
	private static void handleReflectionException(Exception e) {
		ReflectionUtils.handleReflectionException(e);
	}
}
