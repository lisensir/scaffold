package org.scaffold.sorm.validator;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.scaffold.sorm.annotation.Key;
import org.scaffold.sorm.annotation.NotNull;
import org.scaffold.sorm.annotation.NotPersist;
import org.scaffold.sorm.annotation.Unique;
import org.scaffold.sorm.sqlparse.SqlCompiler;
import org.scaffold.sorm.utils.BeanUtil;

public class DataValidator {

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(this.getClass());

	private Class<?> beanClass;
	private PropertyDescriptor[] pdArr;

	public static int VALIDAT_MODE_INSERT = 1;
	public static int VALIDAT_MODE_UPDATE = 2;

	private int validatMode = 1;

	public DataValidator(Class<?> beanClass, int validatMode) {
		this.beanClass = beanClass;
		this.validatMode = validatMode;
		pdArr = BeanUtil.getPropertyDescriptors(this.beanClass);
	}

	public <T> void validate(T bean) {
		_validate(bean, null);
	}

	public <T> void validate(T bean, String[] specifiedProp) {
		_validate(bean, specifiedProp);
	}

	<T> void _validate(T bean, String[] specifiedProp) throws DataValidateException {

		boolean hasUniqueConstraint = SqlCompiler.hasUniqueConstraint(this.beanClass, pdArr);

		try {
			for (PropertyDescriptor pd : pdArr) {
				if (pd.getName().equals("class"))
					continue;
				Field f = beanClass.getDeclaredField(pd.getName());
				if (f.getDeclaredAnnotation(NotPersist.class) != null)
					continue;

				Key key = f.getDeclaredAnnotation(Key.class);
				Unique unique = f.getDeclaredAnnotation(Unique.class);

				/*
				 * 当生成Insert语句，或者是生成update语句但是Bean中没有指定唯一约束时，需要验证Key及指定为NotNull的字段是否为空，
				 * 当生成update语句，同时指定了唯一约束时，则在只要验证唯一约束及指定为NotNull的字段是否为空，不需在验证主键。因为
				 * 唯一约束是具有业务含义的，Key有时只是流水号。
				 */
				if (!hasUniqueConstraint && key != null) {
					if (this.validatMode == VALIDAT_MODE_INSERT && !key.autoIncrement()
							&& pd.getReadMethod().invoke(bean) == null) {
						throw new DataValidateException(MessageFormat.format("数据验证失败：[{0}]为主键不能为空", pd.getName()));
					}
					if (this.validatMode == VALIDAT_MODE_UPDATE && pd.getReadMethod().invoke(bean) == null) {
						throw new DataValidateException(MessageFormat.format("数据验证失败：[{0}]为主键不能为空", pd.getName()));
					}
				}

				if (unique != null) {
					if (this.validatMode == VALIDAT_MODE_INSERT && !unique.autoIncrement()
							&& pd.getReadMethod().invoke(bean) == null) {
						throw new DataValidateException(MessageFormat.format("数据验证失败：[{0}]为唯一约束字段不能为空", pd.getName()));
					}
					if (this.validatMode == VALIDAT_MODE_UPDATE && pd.getReadMethod().invoke(bean) == null) {
						throw new DataValidateException(MessageFormat.format("数据验证失败：[{0}]为唯一约束字段不能为空", pd.getName()));
					}
				}

				// 判断字段是否被标记为不能为空
				if (specifiedProp == null && f.getDeclaredAnnotation(NotNull.class) != null) {
					if (pd.getReadMethod().invoke(bean) == null) {
						throw new DataValidateException(MessageFormat.format("数据验证失败：[{0}]不允许为空", pd.getName()));
					}
				}

				// 当生成update语句，同时提供了更新的具体字段，则判断指定的字段是否有NotNull限制即可。
				if (this.validatMode == VALIDAT_MODE_UPDATE && specifiedProp != null) {
					if (isContained(specifiedProp, pd.getName()) && f.getDeclaredAnnotation(NotNull.class) != null) {
						if (pd.getReadMethod().invoke(bean) == null) {
							throw new DataValidateException(
									MessageFormat.format("数据验证失败：[{0}]为唯一约束字段不能为空", pd.getName()));
						}
					}
				}
			}

		} catch (NoSuchFieldException e) {
			throw new DataValidateException("数据验证失败：" + e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new DataValidateException("数据验证失败：" + e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new DataValidateException("数据验证失败：" + e.getMessage(), e);
		}
	}

	private boolean isContained(String[] properties, String propName) {

		return SqlCompiler.isContained(properties, propName);
	}

}
