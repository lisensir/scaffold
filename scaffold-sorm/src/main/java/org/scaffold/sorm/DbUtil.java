package org.scaffold.sorm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.scaffold.sorm.actuator.ActuatorFactroy;
import org.scaffold.sorm.actuator.IllegalDataRecordException;
import org.scaffold.sorm.paging.PagingData;
import org.scaffold.sorm.processor.IORMConfig;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

public class DbUtil {
	
	/**
	 * 执行查询，并将结果转化为<code>List&ltBean&gt</code>结构
	 * @param sql 查询语句
	 * @param paramMap 参数Map
	 * @param requiredType 实体Bean类型
	 * @return List<T>
	 * @throws DataAccessException
	 */
	public static <T> List<T> query(String sql, Map<String, ?> paramMap, Class<T> requiredType)
			throws DataAccessException {
		Map<String, ?> param = paramMap;
		if(paramMap == null)  param = new HashMap<String,Object>();
		return ActuatorFactroy.getActuator().query(sql, param, requiredType, null);
	}
	
	
	/**
	 * 执行查询，并将结果转化为<code>List&ltBean&gt</code>结构
	 * @param sql
	 * @param args 参数数组
	 * @param requiredType 实体Bean类型
	 * @return List
	 * @throws DataAccessException
	 */
	public static <T> List<T> query(String sql, Object[] args, Class<T> requiredType)
			throws DataAccessException {
		return ActuatorFactroy.getActuator().query(sql, args, requiredType, null);
	}
	
	
	/**
	 * 执行查询，并将结果转化为<code>List&ltBean&gt</code>结构
	 * @param sql 查询sql
	 * @param paramMap 参数Map
	 * @param requiredType 实体Bean类型
	 * @param config 查询结果转换配置类
	 * @return List<T>
	 * @throws DataAccessException
	 */
	public static <T> List<T> query(String sql, Map<String, ?> paramMap, Class<T> requiredType, IORMConfig<T> config)
			throws DataAccessException {
		Map<String, ?> param = paramMap;
		if(paramMap == null)  param = new HashMap<String,Object>();
		return ActuatorFactroy.getActuator().query(sql, param, requiredType, config);
	}
	
	
	/**
	 * 执行查询，并将结果转化为<code>List&ltBean&gt</code>结构
	 * @param sql 查询sql
	 * @param paramMap 参数Map
	 * @param requiredType 实体Bean类型
	 * @param config 查询结果转换配置类
	 * @return List<T>
	 * @throws DataAccessException
	 */
	public static <T> List<T> query(String sql, Object[] args, Class<T> requiredType, IORMConfig<T> config)
			throws DataAccessException {
		return ActuatorFactroy.getActuator().query(sql, args, requiredType, config);
	}
	
	
	/**
	 * 执行查询，并将结果转化为List&ltMap&ltString,Object&gt&gt结构
	 * @param sql sql
	 * @param paramMap 参数Map
	 * @return List<Map<String,Object>>
	 * @throws DataAccessException
	 */
	public static List<Map<String,Object>> query(String sql, Map<String, ?> paramMap) throws DataAccessException {
		return ActuatorFactroy.getActuator().query(sql, paramMap, null);
	}
	
	
	/**
	 * 执行查询，并将结果转化为List&ltMap&ltString,Object&gt&gt结构
	 * @param sql sql
	 * @param args 参数数组
	 * @return List<Map<String,Object>>
	 * @throws DataAccessException
	 */
	public static List<Map<String,Object>> query(String sql, Object[] args) throws DataAccessException {
		return ActuatorFactroy.getActuator().query(sql, args, null);
	}
	
	
	/**
	 * 执行查询，并将结果转化为List&ltMap&ltString,Object&gt&gt结构
	 * @param sql sql
	 * @param paramMap 参数Map
	 * @param config 查询结果转换配置类
	 * @return List&ltMap&ltString,Object&gt&gt
	 * @throws DataAccessException
	 */
	public static List<Map<String,Object>> query(String sql, Map<String, ?> paramMap, IORMConfig<Map<String,Object>> config) 
			throws DataAccessException {
		return ActuatorFactroy.getActuator().query(sql, paramMap, config);
	}
	
	
	/**
	 * 执行查询，并将结果转化为List&ltMap&ltString,Object&gt&gt结构
	 * @param sql sql
	 * @param args 参数数组
	 * @param config 查询结果转换配置类
	 * @return List&ltMap&ltString,Object&gt&gt
	 * @throws DataAccessException
	 */
	public static List<Map<String,Object>> query(String sql, Object[] args, IORMConfig<Map<String,Object>> config) 
			throws DataAccessException {
		return ActuatorFactroy.getActuator().query(sql, args, config);
	}
	
	
	/**
	 * 执行查询，并将结果转化为<code>List&ltBean&gt</code>结构
	 * @param sql 查询语句
	 * @param requiredType 实体Bean类型
	 * @return List
	 * @throws DataAccessException
	 */
	public static <T> List<T> query(String sql, Class<T> requiredType)
			throws DataAccessException {
		return ActuatorFactroy.getActuator().query(sql, new HashMap<String,Object>(), requiredType, null);
	}
	
	
	/**
	 * 执行查询，并将结果转化为<code>List&ltBean&gt</code>结构
	 * @param sql 查询语句
	 * @param requiredType 实体Bean类型
	 * @param config 查询结果转换配置类
	 * @return List<T>
	 * @throws DataAccessException
	 */
	public static <T> List<T> query(String sql, Class<T> requiredType, IORMConfig<T> config)
			throws DataAccessException {
		return ActuatorFactroy.getActuator().query(sql, new HashMap<String,Object>(), requiredType, config);
	}
	
	/**
	 * 执行查询，并将结果转化为List&ltMap&ltString,Object&gt&gt结构
	 * @param sql sql
	 * @return List&ltMap&ltString,Object&gt&gt
	 * @throws DataAccessException
	 */
	public static List<Map<String,Object>> query(String sql) throws DataAccessException {
		return ActuatorFactroy.getActuator().query(sql, new HashMap<String,Object>(), null);
	}
	
	/**
	 * 执行查询，并将结果转化为List&ltMap&ltString,Object&gt&gt结构
	 * @param sql sql
	 * @param config 查询结果转换配置类
	 * @return List&ltMap&ltString,Object&gt&gt
	 * @throws DataAccessException
	 */
	public static List<Map<String,Object>> query(String sql, IORMConfig<Map<String,Object>> config) throws DataAccessException {
		return ActuatorFactroy.getActuator().query(sql, new HashMap<String,Object>(), config);
	}
	
	
	/**
	 * 执行查询返回分页数据，分页结果的数据格式为<code>List&ltBean&gt</code>结构
	 * @param sql sql
	 * @param paramMap 参数Map
	 * @param requiredType 实体Bean类型
	 * @return PagingData<T>
	 */
	public static <T> PagingData<T> queryForPagingData(String sql, Map<String,Object> paramMap,Class<T> requiredType) {
		return ActuatorFactroy.getActuator().queryForPagingData(sql, paramMap, requiredType, null);
	}
	
	
	/**
	 * 执行查询返回分页数据，分页结果的数据格式为<code>List&ltBean&gt</code>结构
	 * @param sql sql
	 * @param args 参数数组
	 * @param requiredType 实体Bean类型
	 * @return PagingData<T>
	 */
	public static <T> PagingData<T> queryForPagingData(String sql, Object[] args, Class<T> requiredType) {
		return ActuatorFactroy.getActuator().queryForPagingData(sql, args, requiredType, null);
	}
	
	
	/**
	 * 执行查询返回分页数据，分页结果的数据格式为<code>List&ltBean&gt</code>结构
	 * @param sql sql
	 * @param paramMap 参数Map
	 * @param requiredType 实体Bean类型
	 * @param config 查询结果转换配置类
	 * @return PagingData<T>
	 */
	public static <T> PagingData<T> queryForPagingData(String sql, Map<String,Object> paramMap,Class<T> requiredType, IORMConfig<T> config) {
		return ActuatorFactroy.getActuator().queryForPagingData(sql, paramMap, requiredType, config);
	}
	
	
	/**
	 * 执行查询返回分页数据，分页结果的数据格式为<code>List&ltBean&gt</code>结构
	 * @param sql sql
	 * @param args 参数数组
	 * @param requiredType 实体Bean类型
	 * @param config 查询结果转换配置类
	 * @return PagingData<T>
	 */
	public static <T> PagingData<T> queryForPagingData(String sql, Object[] args,Class<T> requiredType, IORMConfig<T> config) {
		return ActuatorFactroy.getActuator().queryForPagingData(sql, args, requiredType, config);
	}
	
	
	/**
	 * 执行查询返回分页数据，分页结果的数据格式为<code>List&ltMap&ltString,Object&gt&gt</code>结构
	 * @param sql sql
	 * @param param 参数Map
	 * @return PagingData<Map<String,Object>>
	 */
	public static PagingData<Map<String,Object>> queryForPagingData(String sql, Map<String,Object> paramMap) {
		return ActuatorFactroy.getActuator().queryForPagingData(sql, paramMap, null);
	}
	
	
	/**
	 * 执行查询返回分页数据，分页结果的数据格式为<code>List&ltMap&ltString,Object&gt&gt</code>结构
	 * @param sql sql
	 * @param args 参数数组
	 * @return PagingData<Map<String,Object>>
	 */
	public static PagingData<Map<String,Object>> queryForPagingData(String sql, Object[] args) {
		return ActuatorFactroy.getActuator().queryForPagingData(sql, args, null);
	}
	
	
	/**
	 * 执行查询返回分页数据，分页结果的数据格式为<code>List&ltMap&ltString,Object&gt&gt</code>结构
	 * @param sql sql
	 * @param paramMap 参数Map  
	 * @param config 查询结果转换配置类
	 * @return PagingData<Map<String,Object>>
	 */
	public static PagingData<Map<String,Object>> queryForPagingData(String sql, Map<String,Object> paramMap,IORMConfig<Map<String,Object>> config) {
		return ActuatorFactroy.getActuator().queryForPagingData(sql, paramMap, config);
	}
	
	
	/**
	 * 执行查询返回分页数据，分页结果的数据格式为<code>List&ltMap&ltString,Object&gt&gt</code>结构
	 * @param sql sql
	 * @param args 参数数组
	 * @param config 查询结果转换配置类
	 * @return PagingData<Map<String,Object>>
	 */
	public static PagingData<Map<String,Object>> queryForPagingData(String sql, Object[] args, IORMConfig<Map<String,Object>> config) {
		return ActuatorFactroy.getActuator().queryForPagingData(sql, args, config);
	}
	
	
	/**
	 * 执行查询返回分页数据，分页结果的数据格式为<code>List&ltBean&gt</code>结构
	 * @param sql 查询sql
	 * @param requiredType 实体Bean类型
	 * @return
	 */
	public static <T> PagingData<T> queryForPagingData(String sql, Class<T> requiredType, IORMConfig<T> config) {
		return ActuatorFactroy.getActuator().queryForPagingData(sql, new HashMap<String,Object>(), requiredType, config);
	}
	
	
	/**
	 * 执行查询返回分页数据，分页结果的数据格式为<code>List&ltBean&gt</code>结构
	 * @param sql 查询sql
	 * @param requiredType 实体Bean类型
	 * @return List&ltBean&gt
	 */
	public static <T> PagingData<T> queryForPagingData(String sql, Class<T> requiredType) {
		return ActuatorFactroy.getActuator().queryForPagingData(sql, new HashMap<String,Object>(), requiredType, null);
	}
	
	/**
	 * 执行查询返回分页数据，分页结果的数据格式为<code>List&ltMap&ltString,Object&gt&gt</code>结构
	 * @param sql sql
	 * @return PagingData&ltMap&ltString,Object&gt&gt
	 */
	public static PagingData<Map<String,Object>> queryForPagingData(String sql) {
		return ActuatorFactroy.getActuator().queryForPagingData(sql, new HashMap<String,Object>(), null);
	}
	
	
	/**
	 * 执行查询返回分页数据，分页结果的数据格式为<code>List&ltMap&ltString,Object&gt&gt</code>结构
	 * @param sql sql
	 * @param config 查询结果转换配置类
	 * @return PagingData&ltMap&ltString,Object&gt&gt
	 */
	public static  PagingData<Map<String,Object>> queryForPagingData(String sql, IORMConfig<Map<String,Object>> config) {
		return ActuatorFactroy.getActuator().queryForPagingData(sql, new HashMap<String,Object>(), config);
	}
	
	
	/**
	 * 通过查询语句获取单条记录结果，如果查询到的结果多于一条记录则会报异常
	 * @param sql 查询sql
	 * @param paramMap 参数MAP
	 * @param requiredType 实体bean类型
	 * @return T
	 * @throws IllegalDataRecordException
	 * @throws EmptyResultDataAccessException
	 */
	public static <T> T queryForOneRecord(String sql, Map<String, ?> paramMap, Class<T> requiredType)
			throws IllegalDataRecordException, EmptyResultDataAccessException {
		return ActuatorFactroy.getActuator().queryForOneRecord(sql, paramMap, requiredType, null);
	}
	
	
	/**
	 * 通过查询语句获取单条记录结果，如果查询到的结果多于一条记录则会报异常
	 * @param sql 查询sql
	 * @param paramMap 参数MAP
	 * @return T
	 * @throws IllegalDataRecordException
	 * @throws EmptyResultDataAccessException
	 */
	public static <T> T queryForOneRecord(String sql, Map<String, ?> paramMap)
			throws IllegalDataRecordException, EmptyResultDataAccessException {
		return ActuatorFactroy.getActuator().queryForOneRecord(sql, paramMap, null, null);
	}
	
	/**
	 * 通过查询语句获取单条记录结果，如果查询到的结果多于一条记录则会报异常
	 * @param sql sql
	 * @param args 参数数组
	 * @param requiredType 返回参数类型
	 * @return T
	 */
	public static <T> T queryForOneRecord(String sql, Object[] args, Class<T> requiredType)
			throws IllegalDataRecordException, EmptyResultDataAccessException {
		return ActuatorFactroy.getActuator().queryForOneRecord(sql, args, requiredType, null);
	}
	
	/**
	 * 通过查询语句获取单条记录结果，如果查询到的结果多于一条记录则会报异常
	 * @param sql sql
	 * @param args 参数数组
	 * @param requiredType 返回参数类型
	 * @return T
	 */
	public static <T> T queryForOneRecord(String sql, Object[] args)
			throws IllegalDataRecordException, EmptyResultDataAccessException {
		return ActuatorFactroy.getActuator().queryForOneRecord(sql, args, null, null);
	}
	
	
	/**
	 * 通过查询语句获取单条记录结果，如果查询到的结果多于一条记录则会报异常
	 * @param sql 查询sql
	 * @param paramMap 参数MAP
	 * @param requiredType 实体bean类型
	 * @param config 查询结果转换配置类
	 * @return T
	 * @throws IllegalDataRecordException
	 * @throws EmptyResultDataAccessException
	 */
	public static <T> T queryForOneRecord(String sql, Map<String, ?> paramMap, Class<T> requiredType, IORMConfig<T> config)
			throws IllegalDataRecordException, EmptyResultDataAccessException {
		return ActuatorFactroy.getActuator().queryForOneRecord(sql, paramMap, requiredType, config);
	}
	
	
	/**
	 * 通过查询语句获取单条记录结果，如果查询到的结果多于一条记录则会报异常
	 * @param sql 查询sql
	 * @param args 参数数组
	 * @param requiredType 实体bean类型
	 * @param config 查询结果转换配置类
	 * @return T
	 * @throws IllegalDataRecordException
	 * @throws EmptyResultDataAccessException
	 */
	public static <T> T queryForOneRecord(String sql, Object[] args, Class<T> requiredType, IORMConfig<T> config)
			throws IllegalDataRecordException, EmptyResultDataAccessException {
		return ActuatorFactroy.getActuator().queryForOneRecord(sql, args, requiredType, config);
	}

	
	/**
	 * 执行更新操作
	 * 
	 * @param sql 执行sql
	 * @param args 参数
	 * @return int 
	 * @throws DataAccessException
	 */
	public static int update(String sql, Object... args) throws DataAccessException {
		return ActuatorFactroy.getActuator().update(sql, args);
	}
	
	
	/**
	 * 执行更新操作
	 * @param sql sql
	 * @param paramMap 参数Map
	 * @return int
	 * @throws DataAccessException
	 */
	public static int update(String sql, Map<String, ?> paramMap) throws DataAccessException {
		return ActuatorFactroy.getActuator().update(sql, paramMap);
	}
	
	
	/**
	 * 执行更新操作
	 * @param sql sql
	 * @return int
	 * @throws DataAccessException
	 */
	public static int update(String sql)  throws DataAccessException {
		return ActuatorFactroy.getActuator().update(sql);
	}
	
	
	/**
	 * 更新Bean对应的数据表.
	 * <p>
	 * 该方法会更新除主键之外的所有字段. 在执行更新之前会检查标记为<code>@Key,@NotNull,@Unique</code>的字段不能为空
	 * </p>
	 * 
	 * @param bean Bean
	 * @return int 更新记录数
	 * @throws DataAccessException
	 */
	public static <T> int update(T bean)  throws DataAccessException {
		return ActuatorFactroy.getActuator().update(bean);
	}
	
	
	/**
	 * 只更新Bean中指定的字段到数据表
	 * <p>
	 * 该方法会更新除主键之外的由参数<code>specifiedProp</code>指定的字段.
	 * 在执行更新之前会检查标记为<code>@Key,@NotNull</code>的字段不能为空
	 * </p>
	 * 
	 * @param bean Bean
	 * @param specifiedProp 指定需要更新的属性名称（Bean中定义）
	 * @return int
	 */
	public static <T> int update(T bean, String[] specifiedProp) throws DataAccessException {
		return ActuatorFactroy.getActuator().update(bean, specifiedProp);
	}

	
	/**
	 * 批量插入记录到数据表.
	 * <ul>
	 * <li>如果数据实体名与数据表名没有按规则匹配，则可以在类上使用注解 <code>@Table(name="xxx_xxx")</code>的方式指定
	 * <li>如果数据实体中有字段不需要持久化，则在该字段中使用注解<code>@NotPersit</code>
	 * <li>如果数据实体总的字段与表中的字段命名没有按规则匹配，则可以使用注解
	 * <code>@Persist(name="xxx_xxx")</code>的方式指定
	 * </ul>
	 * 
	 * @param datas 数据实体
	 * @return int[]
	 * @throws DataAccessException
	 */
	public static <T> int[] batchInsert(List<T> datas) throws DataAccessException {
		return ActuatorFactroy.getActuator().batchInsert(datas);
	}
	
	
	/**
	 * 插入一条记录到数据表.
	 * <ul>
	 * <li>如果数据实体名与数据表名没有按规则匹配，则可以在类上使用注解 <code>@Table(name="xxx_xxx")</code>的方式指定
	 * <li>如果数据实体中有字段不需要持久化，则在该字段中使用注解<code>@NotPersit</code>
	 * <li>如果数据实体总的字段与表中的字段命名没有按规则匹配，则可以使用注解
	 * <code>@Persist(name="xxx_xxx")</code>的方式指定
	 * </ul>
	 * 
	 * @param data 数据实体
	 * @return int
	 * @throws DataAccessException
	 */
	public static <T> int insert(T data) throws DataAccessException {
		List<T> datas = new ArrayList<>();
		datas.add(data);
		int[] rv = batchInsert(datas);
		if (rv.length > 0)
			return rv[0];
		return 0;
	}

	
	/**
	 * 插入一条记录，并返回自增字段值
	 * @param bean 记录bean
	 * @return Number 自增字段值
	 * @throws DataAccessException
	 */
	public static <T> Number insertAndReturnKey(T bean) throws DataAccessException {
		return ActuatorFactroy.getActuator().insertAndReturnKey(bean);
	}
	
	
	/**
	 * 删除数据表中对应的记录.<p>
	 * 需要在bean中使用<code>@Key</code>指定关键字或使用<code>@Unique</code>指定唯一约束
	 * @param bean
	 * @return
	 * @throws DataAccessException
	 */
	public static <T> int delete(T bean) throws DataAccessException {
		return ActuatorFactroy.getActuator().delete(bean);
	}
	
}
