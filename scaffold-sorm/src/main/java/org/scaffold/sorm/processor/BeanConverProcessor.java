package org.scaffold.sorm.processor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.scaffold.sorm.utils.BeanUtil;
import org.scaffold.sorm.utils.ResultUtil;

public class BeanConverProcessor implements IORMProcessor {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private final String COLUMN_NAME = "columNameArr";
	
	private final String PD_OBJ = "beanFieldPDArr";
	
	
	private BeanConverProcessor() {}

	private static BeanConverProcessor beanConverProcessor;
	
	public static synchronized BeanConverProcessor getInstance() {
		if(beanConverProcessor == null) beanConverProcessor = new BeanConverProcessor();
		return beanConverProcessor;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> convertToObject(ResultSet rs, Class<T> targetClazz, IORMConfig<T> config) throws SQLException {
		
		long t1 = System.currentTimeMillis();

	    List<T> lst  = new ArrayList<>();

	    try {

	      if (BeanUtil.isBaseClass(targetClazz)) {
	        while (rs.next()) {
	          lst.add((T) ResultUtil.getColVal(rs, 1, targetClazz));
	        }
	      } else {
	        Map<String, Object> ORMCache = ResultUtil.parseORM(rs, targetClazz);
	        PropertyDescriptor[] pdObjArr = (PropertyDescriptor[])ORMCache.get(PD_OBJ);
	        String[] columnNameArr = (String[])ORMCache.get(COLUMN_NAME);
	        while (rs.next()) {
	          T obj = BeanUtil.getBeanInstance(targetClazz);
	          setBean(rs, obj, pdObjArr, columnNameArr, config);
	          lst.add(obj);
	        }
	      }

	    } catch (SQLException e) {
	      throw new ORMException("ORM发生异常：" + e.getMessage(), e);
	    } catch (IllegalAccessException e) {
	      throw new ORMException("ORM发生异常：" + e.getMessage(), e);
	    } catch (InvocationTargetException e) {
	      throw new ORMException("ORM发生异常：" + e.getMessage(), e);
	    }

	    long t2 = System.currentTimeMillis();
	    if (logger.isDebugEnabled()) {
	      logger.debug(MessageFormat.format("$$Bean:[{0}]ORM总耗时:{1}ms", targetClazz.getName(), (t2 - t1)));
	    }

	    return lst;
	}

	  /*
	   * 根据rs中的一行数据，设置Bean对象
	   * @param rs  结果集
	   * @param obj 待设置的对象
	   * @throws SQLException
	   * @throws ORMException
	   */
	  private <T> void setBean(ResultSet rs, T obj,
	                           PropertyDescriptor[] ORMCache,
	                           String[] columnNameArr,
	                           IORMConfig<T> config)
	          throws SQLException, InvocationTargetException, IllegalAccessException {

	    int columnCount = rs.getMetaData().getColumnCount();

	    for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	    	PropertyDescriptor pd = ORMCache[columnIndex];
	    	if (pd == null) continue;

	      if (config != null
	              && config.convert(columnNameArr[columnIndex],
	            		  ResultUtil.getColVal(rs, columnIndex, pd.getPropertyType()),
	                                obj)) {
	        Object v = ResultUtil.getColVal(rs, columnIndex, pd.getPropertyType());
	        if (rs.wasNull() || v == null) continue;
	        pd.getWriteMethod().invoke(obj, v);
	        continue;
	      }

	      Object v = ResultUtil.getColVal(rs, columnIndex, pd.getPropertyType());
	      if (rs.wasNull() || v == null) continue;

	      pd.getWriteMethod().invoke(obj, v);
	    }

	  }
}
