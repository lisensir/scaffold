package org.scaffold.sorm.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.scaffold.sorm.annotation.NotPersist;

/**
 * Bean操作工具类
 *
 * @author lisen 2016/8/31
 */
public abstract class BeanUtil {

	/**
	 * 将传人的Bean转换为Map结构，用于对不存在继承情况的Bean执行转换，如果存在继承，
	 * 则在转换完成的Map中不包含从继承类字段,如果用于处理不存在继承关系的Bean，则该
	 * 方法的效率比<code>toMap(Object bean)</code>高
	 * 
	 * @param bean
	 * @return Map
	 * @throws BeanToMapException
	 */
	public static Map<String, Object> simpleBeanToMap(Object bean) throws BeanToMapException {

		if (bean == null)
			return null;

		Field[] fields = bean.getClass().getDeclaredFields();
		if (fields == null || fields.length <= 0)
			return null;

		Map<String, Object> map = new HashMap<String, Object>();
		AccessibleObject.setAccessible(fields, true);

		try {
			for (Field f : fields) {
				if ("serialVersionUID".equals(f.getName()))
					continue;
				map.put(f.getName(), f.get(bean));
			}
		} catch (IllegalAccessException e) {
			throw new BeanToMapException("将Bean转化为Map时发生异常:" + e.getMessage(), e);
		}

		return map;
	}

	/**
	 * 将传人的Bean转换为Map结构，该方法在转换完成的Map中包含从父类继承来的的字段，但在第一次某个Bean
	 * 转换时效率没有<code>simpleBeanToMap(bean)</code>高。
	 * 
	 * @param bean
	 * @return Map<String, Object>
	 * @throws BeanToMapException
	 */
	public static Map<String, Object> toMap(Object bean) throws BeanToMapException {

		return toMap(bean, false);
	}

	/**
	 * 将传人的Bean转换为Map结构，可以通过参数指定在转换为Map时是否忽略为空的字段
	 * 
	 * @param bean
	 *            需要转化的Bean对象
	 * @param ignoreNullValField
	 *            是否忽略为空的字段
	 * @return
	 */
	public static Map<String, Object> toMap(Object bean, boolean ignoreNullValField) throws BeanToMapException {
		if (bean == null)
			return null;
		Class<?> beanClass = bean.getClass();
		PropertyDescriptor[] pdArr = getPropertyDescriptors(beanClass);

		Map<String, Object> map = new HashMap<>();
		try {
			for (PropertyDescriptor pd : pdArr) {
				if (pd.getReadMethod() == null || "class".equals(pd.getName()))
					continue;

				if ("serialVersionUID".equals(pd.getName()))
					continue;

				Field f = getField(beanClass, pd.getName());
				if (f.getDeclaredAnnotation(NotPersist.class) != null)
					continue;

				Object b = getPropertyValue(bean, pd.getName());
				if (ignoreNullValField && b == null)
					continue;
				map.put(pd.getName(), b);
			}
		} catch (IllegalAccessException e) {
			throw new BeanToMapException("Bean转换Map异常：" + e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new BeanToMapException("Bean转换Map异常：" + e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new BeanToMapException("Bean转换Map异常：" + e.getMessage(), e);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		return map;
	}

	public static Field getField(Class<?> clazz, String propName) throws NoSuchFieldException {
		try {
			Field f = clazz.getDeclaredField(propName);
			return f;
		} catch (NoSuchFieldException e) {
			if (clazz.getGenericSuperclass() != null) {
				Class<?> superClazz = clazz.getSuperclass();
				return getField(superClazz, propName);
			} else {
				throw e;
			}
		}
	}

	/**
	 * 判断传人的类型是否是基本型或对应的包装类型
	 * <p>
	 * 注意：<code>Class.isPrimitive()</code>判断时不包括基本类型对应的包装类型，该方法包含基本 类型对应的包装类型。
	 * </p>
	 * 
	 * @param clazz
	 *            Class
	 * @return boolean
	 */
	public static boolean isPrimitiveClass(Class<?> clazz) {

		return clazz.isPrimitive() || Long.class == clazz || Integer.class == clazz || Double.class == clazz
				|| Boolean.class == clazz || Byte.class == clazz || Float.class == clazz || Short.class == clazz
				|| String.class == clazz;
	}

	/**
	 * 判断类型是否为数值类型。
	 * <p>
	 * 该方法考虑了实现<code>java.lang.Number</code>接口的类型，及表示数值的基本类型，
	 * 如<code>long,float,short,byte,double</code>等
	 * </p>
	 * 
	 * @param clazz
	 *            Class
	 * @return boolean
	 */
	public static boolean isNumberClass(Class<?> clazz) {
		return isNumberSbuClass(clazz) 
				|| long.class == clazz 
				|| double.class == clazz
				|| byte.class == clazz
				|| float.class == clazz 
				|| short.class == clazz;
	}

	
	/**
	 * 判断是否是Map接口类型或HashMap类型
	 * @param clazz
	 * @return
	 */
	public static boolean isMapOrHashMap(Class<?> clazz) {
		if ("java.util.HashMap".equals(clazz.getName()) 
				|| "java.util.Map".equals(clazz.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * 判断类型是否为<code>Date</code>型
	 * <p>
	 * 如果<code>Class</code>为<code>java.util.Date</code>或是其子类（如：<code>java.sql.Date,java.sql.Time</code>等），
	 * 该方法都将返回<code>true</code>
	 * </p>
	 * 
	 * @param clazz
	 *            Class
	 * @return boolean
	 */
	public static boolean isDateClass(Class<?> clazz) {

		return clazz == java.util.Date.class 
				|| isDateSubClass(clazz);
	}

	/*
	 * 判断是否是Number的子类
	 */
	private static boolean isNumberSbuClass(Class<?> clazz) {
		if (clazz.getSuperclass() == Number.class)
			return true;
		return false;
	}

	/*
	 * 判断是否是<code>java.util.Date</code>子类
	 */
	private static boolean isDateSubClass(Class<?> clazz) {
		if (clazz.getSuperclass() == java.util.Date.class)
			return true;
		return false;
	}

	public static Object getPropertyValue(Object bean, String propertyName)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return BeanUtilsBean.getInstance().getPropertyUtils().getSimpleProperty(bean, propertyName);
	}

	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
		return org.springframework.beans.BeanUtils.getPropertyDescriptors(clazz);
	}

	/**
	 * 根据执行的clazz实例化一个对象
	 * 
	 * @param clazz
	 *            带判断类型
	 * @return T 对象
	 */
	public static <T> T getBeanInstance(Class<T> clazz) {
		try {
			return (T) clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 判断是否是基本类型
	 * <p>
	 * 如果传入的参数<code>Class</code>是原始类型或其包装类型，<code>Number</code>及其子类，
	 * <code>java.util.Date</code>及其子类型，则判断为基本类型，如果<code>Class</code> 是用户定义，则返回false
	 * </p>
	 * 
	 * @param clazz
	 *            待判断的类型
	 * @return boolean
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isBaseClass(Class clazz) {
		return BeanUtil.isPrimitiveClass(clazz) || BeanUtil.isNumberClass(clazz) || BeanUtil.isDateClass(clazz);
	}

}
