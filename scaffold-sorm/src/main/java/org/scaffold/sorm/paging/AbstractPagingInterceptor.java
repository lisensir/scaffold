package org.scaffold.sorm.paging;

public abstract class AbstractPagingInterceptor {
	
	/**
	 * 当前页码
	 */
	public static final String PAGINATION = "pagination";
	
	/**
	 * 每页显示的记录数
	 */
	public static final String PAGE_LENGTH = "pageLength";
	
	
	/**
	 * 设置分页上下文
	 * @param pagination 当前页码
	 * @param pageLength 每页显示的记录数
	 */
	public void setPagingContext(int pagination, int pageLength) {
		PagingContext pg = new PagingContext(pagination <= 0 ? 1 : pagination,pageLength <= 0 ? 20 : pageLength);
		PagingContextHolder.setPagingContextHoler(pg);
	}

}
