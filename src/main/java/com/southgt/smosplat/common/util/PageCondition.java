package com.southgt.smosplat.common.util;

/**
 * 该类定义easyui的datagrid组件load数据时传递的参数以及刷新数据时需要的参数
 * @author Administrator
 *
 */
public class PageCondition{
	/**
	 * 默认每页记录数
	 */
	public static final int DEFAULT_PAGE_SIZE = 15;
	/**
	 * 默认初始页码
	 */
	public static final int DEFAULT_PAGE_NUM=1;
	/**
	 * 当前页码
	 */
	private int pageNumber = DEFAULT_PAGE_NUM;
	/**
	 * 每页多少条记录
	 */
	private int pageSize = DEFAULT_PAGE_SIZE;
	/**
	 * 总页数
	 */
	private int pageCount = 1;
	/**
	 * 总记录数
	 */
	private long totalCount = 0;
	
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize > 0 ? pageSize : 10;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
		pageCount = (int)(totalCount - 1) / this.pageSize + 1;
	}
}
