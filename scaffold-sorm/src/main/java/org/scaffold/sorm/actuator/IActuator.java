package org.scaffold.sorm.actuator;

import java.util.List;
import java.util.Map;

import org.scaffold.sorm.paging.PagingData;
import org.scaffold.sorm.processor.IORMConfig;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

public interface IActuator {

	/**
	 * 将数据库查询结果转换为实体Bean，如果为空则返回一个空结果集
	 * 
	 * @param sql
	 *            查询语句
	 * @param paramMap
	 *            参数列表
	 * @param requiredType
	 *            实体bean类型
	 * @param config
	 *            查询结果转换配置类
	 * @return List
	 * @throws DataAccessException
	 */
	<T> List<T> query(String sql, Map<String, ?> paramMap, Class<T> requiredType, IORMConfig<T> config)
			throws DataAccessException;

	
	/**
	 * 执行SQL，将数据库查询结果转换为实体Bean，如果为空则返回一个空结果集
	 * 
	 * @param sql
	 *            查询语句
	 * @param args
	 *            参数数组
	 * @param requiredType
	 *            实体bean类型
	 * @param config
	 *            查询结果转换配置类
	 * @return List<T>
	 * @throws DataAccessException
	 */
	<T> List<T> query(String sql, Object[] args, Class<T> requiredType, IORMConfig<T> config);

	
	/**
	 * 通过查询语句获取单条记录结果，如果查询到的结果多于一条记录则会报异常
	 * 
	 * @param sql
	 *            查询sql
	 * @param paramMap
	 *            参数MAP
	 * @param requiredType
	 *            实体bean类型
	 * @param config
	 *            查询结果转换配置类
	 * @return T
	 * @throws IllegalDataRecordException
	 * @throws EmptyResultDataAccessException
	 */
	<T> T queryForOneRecord(String sql, Map<String, ?> paramMap, Class<T> requiredType, IORMConfig<T> config)
			throws IllegalDataRecordException, EmptyResultDataAccessException;

	
	/**
	 * 通过查询语句获取单条记录结果，如果查询到的结果多于一条记录则会报异常
	 * 
	 * @param sql
	 * @param args
	 * @param requiredType
	 * @param config
	 * @return
	 */
	<T> T queryForOneRecord(String sql, Object[] args, Class<T> requiredType, IORMConfig<T> config);

	
	/**
	 * 执行sql返回<code>List<Map<String,Object>></code>结构的结果集
	 * 
	 * @param sql
	 *            查询语句
	 * @param paramMap
	 *            参数Map
	 * @param config
	 *            查询结果转换配置类
	 * @return List<Map<String,Object>>
	 * @throws DataAccessException
	 */
	List<Map<String, Object>> query(String sql, Map<String, ?> paramMap, IORMConfig<Map<String, Object>> config)
			throws DataAccessException;

	
	/**
	 * 执行sql返回<code>List<Map<String,Object>></code>结构的结果集
	 * 
	 * @param sql
	 *            查询sql
	 * @param args
	 *            参数数组
	 * @param config
	 *            查询结果转换配置类
	 * @return List<Map<String, Object>>
	 * @throws DataAccessException
	 */
	List<Map<String, Object>> query(String sql, Object[] args, IORMConfig<Map<String, Object>> config)
			throws DataAccessException;

	
	/**
	 * 执行查询并返回分页结果，且分页结果以<code>List<Bean></code>格式存放
	 * 
	 * @param sql
	 *            查询语句
	 * @param paramMap
	 *            查询参数
	 * @param requiredType
	 *            实体bean类型
	 * @param config
	 *            查询结果转换配置类
	 * @return PagingData
	 * @throws DataAccessException
	 */
	<T> PagingData<T> queryForPagingData(String sql, Map<String, ?> paramMap, Class<T> requiredType,
			IORMConfig<T> config) throws DataAccessException;

	
	/**
	 * 执行查询并返回分页结果，且分页结果以<code>List<Map<String,Object>></code>格式存放
	 * 
	 * @param sql
	 *            查询语句
	 * @param args
	 *            参数数组
	 * @param requiredType
	 * @param config
	 * @return PagingData
	 * @throws DataAccessException
	 */
	<T> PagingData<T> queryForPagingData(String sql, Object[] args, Class<T> requiredType, IORMConfig<T> config)
			throws DataAccessException;

	
	/**
	 * 执行查询并返回分页结果，且分页结果以<code>List<Map<String,Object>></code>格式存放
	 * 
	 * @param sql
	 * @param paramMap
	 * @param config
	 * @return
	 * @throws DataAccessException
	 */
	PagingData<Map<String, Object>> queryForPagingData(String sql, Map<String, ?> paramMap,
			IORMConfig<Map<String, Object>> config) throws DataAccessException;

	
	/**
	 * 执行查询并返回分页结果，且分页结果以<code>List<Map<String,Object>></code>格式存放
	 * 
	 * @param sql
	 * @param args
	 * @param config
	 * @return
	 * @throws DataAccessException
	 */
	PagingData<Map<String, Object>> queryForPagingData(String sql, Object[] args,
			IORMConfig<Map<String, Object>> config) throws DataAccessException;

	
	/**
	 * 执行更新或新增操作
	 * 
	 * @param sql 执行sql
	 * @param args 参数
	 * @return int 
	 * @throws DataAccessException
	 */
	int update(String sql, Object... args) throws DataAccessException;

	
	/**
	 * 执行更新
	 * @param sql sql语句
	 * @param paramMap 参数MAP
	 * @return int
	 * @throws DataAccessException
	 */
	int update(String sql, Map<String, ?> paramMap) throws DataAccessException;

	
	/**
	 * 执行更新
	 * @param sql sql语句
	 * @return int
	 * @throws DataAccessException
	 */
	int update(String sql) throws DataAccessException;

	
	/**
	 * 
	 * @param sql
	 * @param entity
	 * @return int
	 * @throws DataAccessException
	 */
	<T> int update(String sql, T entity) throws DataAccessException;

	
	/**
	 * 更新Bean对应的数据表的记录.
	 * <ul>
	 * <li>如果数据实体名与数据表名没有按规则匹配，则可以在类上使用注解 <code>@Table(name="xxx_xxx")</code>的方式指定
	 * <li>如果数据实体中有字段不需要持久化，则在该字段中使用注解<code>@NotPersit</code>
	 * <li>如果数据实体中的字段与表中的字段命名没有按规则匹配，则可以使用注解<code>@Persist(name="xxx_xxx")</code>的方式指定
	 * <li>可通过<code>@Key</code>指定关键字
	 * </ul>
	 * 
	 * @param bean bean
	 * @return int 
	 * @throws DataAccessException
	 */
	<T> int update(T bean) throws DataAccessException;
	
	/**
	 * 更新Bean中的指定字段到对应的数据表的记录
	 * 
	 * @param bean bean
	 * @param specifiedProp 指定需要更新的字段
	 *            
	 * @return int
	 */
	<T> int update(T bean, String[] specifiedProp);
	
	
	/**
	 * 批量执行更新或新增操作
	 * 
	 * @param sql 执行sql
	 * @param datas 结果集
	 * @return int[]
	 * @throws DataAccessException
	 */
	<T> int[] batchUpdate(String sql, List<T> datas) throws DataAccessException;
	
	
	/**
	 * 根据提供的Map数组执行批量更新
	 * 
	 * @param sql
	 * @param paramMapArray
	 * @return int[]
	 * @throws DataAccessException
	 */
	int[] batchUpdate(String sql, Map<String, ?>[] paramMapArray) throws DataAccessException;
	
	
	/**
	 * 插入一条记录到数据表.
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
	<T> int[] batchInsert(List<T> datas) throws DataAccessException;
	
	
	/**
	 * 插入一条记录，并返回自增字段值
	 * 
	 * @param bean 记录bean
	 * @return Number 自增字段值
	 * @throws DataAccessException
	 */
	<T> Number insertAndReturnKey(T bean) throws DataAccessException;
	
	
	/**
	 * 删除记录.
	 * <p>
	 * 需要在bean中使用<code>@Key</code>指定关键字或使用<code>@Unique</code>指定唯一约束
	 * 
	 * @param bean
	 * @return
	 */
	<T> int delete(T bean) throws DataAccessException;
	
}
