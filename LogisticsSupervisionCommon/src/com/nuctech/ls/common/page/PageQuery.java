package com.nuctech.ls.common.page;

/**
 * 分页查询对象
 * 
 * @author badqiu
 * @version $Id: PageQuery.java,v 0.1 2010-11-29 下午05:34:12 zhongxuan Exp $
 */
public class PageQuery<T> implements java.io.Serializable {
	private static final long serialVersionUID = -8000900575354501298L;

	public static final int DEFAULT_PAGE_SIZE = 10;
	private T filters;
	private int page;
	private int pageSize = DEFAULT_PAGE_SIZE;
	/**
	 * 排序的多个列,如: username desc
	 */
	private String sortColumns;
	public PageQuery() {
	}

	public PageQuery(int pageSize) {
		this.pageSize = pageSize;
	}

	public PageQuery(PageQuery query) {
		this.page = query.page;
		this.pageSize = query.pageSize;
	}

	public PageQuery(int pageNo, int pageSize) {
		this.page = pageNo;
		this.pageSize = pageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int pageNo) {
		this.page = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return "page:" + page + ",pageSize:" + pageSize;
	}

	public T getFilters() {
		return filters;
	}

	public void setFilters(T filters) {
		this.filters = filters;
	}

	public String getSortColumns() {
		return sortColumns;
	}

	public void setSortColumns(String sortColumns) {
		this.sortColumns = sortColumns;
	}

}

