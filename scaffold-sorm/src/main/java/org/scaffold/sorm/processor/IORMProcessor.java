package org.scaffold.sorm.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据库查询结果集转换为Bean/map/json
 *
 * @author lisen
 * @date 2016/8/30
 */
public interface IORMProcessor {

  /**
   * 根据指定的Class转换ResultSet对象
   * @param rs     数据库结果集
   * @param targetClazz  目标Class
   * @param config 字段转换配置，可以为空，表示不用转换
   * @return  list
   */
  //<T> List<T> convertToBeans(ResultSet rs,Class<T> targetClazz, IORMConfig<T> config) throws SQLException;
  
  
  
  <T> List<T> convertToObject(ResultSet rs,Class<T> targetClazz, IORMConfig<T> config) throws SQLException;

  
  /**
   * 将查询结果转换为<code>List<Map<String,Object>></code>机构.
   * 将数据表中的字段值转换为驼峰风格作为Map中的Key
   * @param rs 结果集
   * @param config 字段转换配置，可以为空，表示不用转换
   * @return List<Map<String,Object>>
   * @throws SQLException
   */
	//List<Map<String, Object>> convertToListMap(ResultSet rs, IORMConfig<Map<String,Object>> config) throws SQLException;
	

}
