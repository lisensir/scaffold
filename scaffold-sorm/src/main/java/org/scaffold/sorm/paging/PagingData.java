package org.scaffold.sorm.paging;

import java.util.List;

public class PagingData<T> {
	
	/**
	 * 总记录数
	 */
	private int totalrecords;
	
	
	/**
	 * 当前页码
	 */
	private int curpage;
	
	
	/**
	 * 每页记录数
	 */
	private int pagelength;
	
	
	/**
	 * 总页数
	 */
	private int pages;
	
	
	/**
	 * 数据集合
	 */
	private List<T> datas;
	
	
	public PagingData (int pagination, int pages, int pageLength, int totalRecords, List<T> datas) {
		this.curpage = pagination;
		this.pages = pages;
		this.pagelength = pageLength;
		this.totalrecords = totalRecords;
		this.datas = datas;
	}

	
	public int getTotalrecords() {
		return totalrecords;
	}

	public void setTotalrecords(int totalrecords) {
		this.totalrecords = totalrecords;
	}

	public int getCurpage() {
		return curpage;
	}

	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	public int getPagelength() {
		return pagelength;
	}

	public void setPagelength(int pagelength) {
		this.pagelength = pagelength;
	}
	
}	
