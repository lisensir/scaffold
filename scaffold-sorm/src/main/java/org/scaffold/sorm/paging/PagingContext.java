package org.scaffold.sorm.paging;

import java.io.Serializable;

public class PagingContext implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 当前页码
	 */
	private int pagination;
	
	/**
	 * 每页起始记录
	 */
	private int pageStart;
	
	/**
	 * 每页显示的记录
	 */
	private int pageLength;
	
	
	public PagingContext(int pagination, int pageLength) {
		this.pagination = pagination;
		this.pageLength = pageLength;
		this.pageStart = (pagination-1) * pageLength;
	}

	
	public int getPageStart() {
		return pageStart;
	}

	public int getPageLength() {
		return pageLength;
	}


	public void setPageLength(int pageLength) {
		this.pageLength = pageLength;
	}


	public int getPagination() {
		return pagination;
	}


	public void setPagination(int pagination) {
		this.pagination = pagination;
	}


	public void setPageStart(int pageStart) {
		this.pageStart = pageStart;
	}

}
