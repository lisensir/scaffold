package org.scaffold.sorm.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.scaffold.sorm.annotation.Persist;
import org.scaffold.sorm.processor.ORMException;
import org.scaffold.sorm.sqlparse.SqlCompiler;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.support.JdbcUtils;

public abstract class ResultUtil {

	private static Logger logger = Logger.getLogger(ResultUtil.class);

	private static Map<String, Map<String,PropertyDescriptor>> BEAN_PD_C = new ConcurrentHashMap<>();
	
	public static final int DATE_MODEL_DATE = 1;

	public static final int DATE_MODEL_DATESTR = 2;

	/**
	 * 根据RS元数据和索引号，获取列名
	 * 
	 * @param rsmd
	 * @param index
	 * @return
	 * @throws SQLException
	 */
	public static String getColName(ResultSetMetaData rsmd, int index) throws SQLException {
		return JdbcUtils.lookupColumnName(rsmd, index);
	}
	
	/**
	 * 获取列值，如果是日期型则转换为yyyy-MM-dd格式，如果是日期时间型则转换为yyyy-MM-dd HH:mm:ss格式， 
	 * 对应其他类型未做转换。
	 * 注意：在数据表中如果不是明确需要boolean型数据，则不要将类型设置为tinyint型，这种数据类型在获取
	 * 值时自动转换为boolean类型值。例如：1：新增、2：更换、3：删除，这种数据在表中最好设置为int型的.
	 * 
	 * @param rs sql结果集
	 * @param columnIndex 列序号
	 * @param dateModel 日期模式，DATE_MODEL_DATE日期型，DATE_MODEL_DATESTR字符串型
	 * @return Object 列值
	 * @throws SQLException
	 */
	public static Object getColVal(ResultSet rs, int columnIndex, int dateModel) throws SQLException {

		int type = rs.getMetaData().getColumnType(columnIndex);
		switch (type) {
		case Types.TIMESTAMP:
			if (dateModel == DATE_MODEL_DATE)
				return (Date) getColVal(rs, columnIndex, Date.class);
			else if (dateModel == DATE_MODEL_DATESTR)
				return DateUtil.format((Date) getColVal(rs, columnIndex, Date.class), DateUtil.DATE_DAYTIME_FORMAT);
			else
				return (Date) getColVal(rs, columnIndex, Date.class);
		case Types.DATE:
			if (dateModel == DATE_MODEL_DATE)
				return (Date) getColVal(rs, columnIndex, Date.class);
			else if (dateModel == DATE_MODEL_DATESTR)
				return DateUtil.format((Date) getColVal(rs, columnIndex, Date.class), DateUtil.DATE_DAY_FORMAT);
			else
				return (Date) getColVal(rs, columnIndex, Date.class);
		default:
			return JdbcUtils.getResultSetValue(rs, columnIndex);
		}

	}

	/**
	 * 根据rs,索引号，类型获取列值
	 * 
	 * @param rs
	 * @param index
	 * @param rType
	 * @return
	 * @throws SQLException
	 */
	public static Object getColVal(ResultSet rs, int index, Class<?> rType) throws SQLException {
		return JdbcUtils.getResultSetValue(rs, index, rType);
	}

	/**
	 * 生成ORM映射数组，放入ORM缓存. <p> 如果在Bean中使用了@Persist(name="XX")定义了与数据表字段的对应关系，
	 * 则使用定义，否则使用默认规则进行映射
	 * @param rs 结果集
	 * @param targetClazz
	 * @return Map key: 0 存放列名称数组， key：1 存放Bean反射对象数组
	 * @throws SQLException
	 */
	public static <T> Map<String, Object> parseORM(ResultSet rs, Class<T> targetClazz) throws SQLException {

		Map<String, Object> map = new HashMap<>();

		Map<String, PropertyDescriptor> beanFieldPDCache = getBeanPD(targetClazz);

		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();

		PropertyDescriptor[] beanFieldPDArr = new PropertyDescriptor[columnCount + 1];
		String[] columNameArr = new String[columnCount + 1];

		for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
			String columnName = getColName(rsmd, columnIndex);

			columNameArr[columnIndex] = columnName;

			String fn = columnName.replaceAll("_", "").toLowerCase();
			if (beanFieldPDCache.containsKey(fn)) {
				beanFieldPDArr[columnIndex] = beanFieldPDCache.get(fn);
				continue;
			}

			if (beanFieldPDCache.containsKey(columnName)) {
				beanFieldPDArr[columnIndex] = beanFieldPDCache.get(columnName);
				continue;
			}

			beanFieldPDArr[columnIndex] = null;
			if (logger.isDebugEnabled()) {
				logger.debug("@@RTB转化：列[" + columnName + "]在Bean[" + targetClazz.getName() + "]中没有相应的定义");
			}

		}

		map.put("columNameArr", columNameArr);
		map.put("beanFieldPDArr", beanFieldPDArr);

		return map;
	}

	private static <T> Map<String, PropertyDescriptor> getBeanPD(Class<T> targetClass) {

		if(ResultUtil.BEAN_PD_C.containsKey(targetClass.getName())) {
			return ResultUtil.BEAN_PD_C.get(targetClass.getName());
		}

		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(targetClass);
		Map<String, PropertyDescriptor> beanFieldPDCache = new HashMap<>();
		try {
			for (PropertyDescriptor pd : pds) {
				if (pd.getWriteMethod() == null)
					continue;

				Field f = BeanUtil.getField(targetClass, pd.getName());
				Persist pa = f.getDeclaredAnnotation(Persist.class);
				if (pa != null && !pa.name().trim().equals(""))
					beanFieldPDCache.put(pa.name(), pd);
				else
					beanFieldPDCache.put(pd.getName().toLowerCase().replace("_", ""), pd);
			}
		} catch (NoSuchFieldException e) {
			throw new ORMException(e.getMessage(), e);
		}

		ResultUtil.BEAN_PD_C.put(targetClass.getName(), beanFieldPDCache);
		return beanFieldPDCache;
	}
	
	public static final int COLUMN_NAME = 0;

	public static final int PROP_NAME = 1;

	public static final int COLUMN_TYPE = 2;
	
	
	/**
	 * 获取映射出的属性名与在查找结果中的映射关系，数组的Index为结果中的序号，对应的值为属性名
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static String[][] getColumnStructure(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		String[][] fieldNameIndexMap = new String[3][columnCount + 1];

		for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
			String columnName = getColName(rsmd, columnIndex);
			String propName = columnName;
			//如果列名包含"_"则转换为驼峰风格，以对应实体Bean中的属性名或Map中的key
			//否则，列名与实体Bean中的属性或Map中的key直接对应
			if (columnName.contains("_")) {
				propName = SqlCompiler.toHumpPropName(columnName);
			}
			fieldNameIndexMap[COLUMN_NAME][columnIndex] = columnName;
			fieldNameIndexMap[PROP_NAME][columnIndex] = propName;
			fieldNameIndexMap[COLUMN_TYPE][columnIndex] = rsmd.getColumnType(columnIndex) + "";
		}

		return fieldNameIndexMap;
	}

}
