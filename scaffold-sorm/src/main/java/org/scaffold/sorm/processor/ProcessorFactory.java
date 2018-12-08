package org.scaffold.sorm.processor;

import org.scaffold.sorm.utils.BeanUtil;

public abstract class ProcessorFactory {
	
	public static <T> IORMProcessor getProcessor(Class<T> requiredType) {
		
		if(requiredType == null || BeanUtil.isMapOrHashMap(requiredType)) {
			return MapConverProcessor.getInstance();
		}
		
		return BeanConverProcessor.getInstance();
	}
	
}
