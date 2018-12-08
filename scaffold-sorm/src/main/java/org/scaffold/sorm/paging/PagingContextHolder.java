package org.scaffold.sorm.paging;

public class PagingContextHolder {
	
	private static ThreadLocal<PagingContext> pagingContextHolder = new ThreadLocal<PagingContext>();
	
	public static PagingContext getPagingContext() {
		return pagingContextHolder.get();
	}
	
	public static void setPagingContextHoler(PagingContext val) {
		pagingContextHolder.set(val);
	}

}
