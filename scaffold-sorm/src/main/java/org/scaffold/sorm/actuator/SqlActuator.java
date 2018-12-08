package org.scaffold.sorm.actuator;

import java.util.List;
import java.util.Map;

import org.scaffold.sorm.paging.PagingContextHolder;
import org.scaffold.sorm.paging.PagingData;
import org.scaffold.sorm.processor.IORMConfig;
import org.scaffold.sorm.processor.ProcessorFactory;
import org.scaffold.sorm.sqlparse.SqlCompiler;
import org.scaffold.sorm.utils.BeanUtil;
import org.scaffold.sorm.validator.DataValidateException;
import org.scaffold.sorm.validator.DataValidator;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class SqlActuator implements IActuator {

	private JdbcTemplate jdbcTemplate;

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	//private SimpleJdbcInsert simpleJdbcInsert;

	private final String ILLEGAL_DATA_RECORD_EXCEPTION_INFO = "查询到的记录数多于一条，请核对查询条件是否能唯一确定一条记录";

	private SqlActuator(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		//this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
	}

	private static SqlActuator actuator = null;

	
	static synchronized IActuator getInstance(JdbcTemplate jdbcTemplate) {
		if (actuator == null)
			actuator = new SqlActuator(jdbcTemplate);
		return actuator;
	}

	
	@Override
	public <T> List<T> query(String sql, Map<String, ?> paramMap, Class<T> requiredType, IORMConfig<T> config)
			throws DataAccessException {
		SqlCompiler.QueryStruct sqlStruct = SqlCompiler.compileQuery(paramMap, sql);
		List<T> lst = (List<T>) namedParameterJdbcTemplate.query(sqlStruct.getSql(), sqlStruct.getParam(), rs -> {
			return ProcessorFactory.getProcessor(requiredType).convertToObject(rs, requiredType, config);
		});
		return lst;
	}

	
	@Override
	public List<Map<String, Object>> query(String sql, Map<String, ?> paramMap, IORMConfig<Map<String, Object>> config)
			throws DataAccessException {

		SqlCompiler.QueryStruct sqlStruct = SqlCompiler.compileQuery(paramMap, sql);
		List<Map<String, Object>> data = (List<Map<String, Object>>) namedParameterJdbcTemplate
				.query(sqlStruct.getSql(), sqlStruct.getParam(), rs -> {
					return ProcessorFactory.getProcessor(Map.class).convertToObject(rs, null, config);
				});

		return data;
	}

	
	@Override
	public <T> List<T> query(String sql, Object[] args, Class<T> requiredType, IORMConfig<T> config)
			throws DataAccessException {
		List<T> lst = jdbcTemplate.query(sql, args, rs -> {
			return ProcessorFactory.getProcessor(requiredType).convertToObject(rs, requiredType, config);
		});

		return lst;
	}

	
	@Override
	public List<Map<String, Object>> query(String sql, Object[] args, IORMConfig<Map<String, Object>> config)
			throws DataAccessException {
		List<Map<String, Object>> datas = jdbcTemplate.query(sql, args, rs -> {
			return ProcessorFactory.getProcessor(Map.class).convertToObject(rs, null, config);
		});
		return datas;
	}

	
	@Override
	public <T> T queryForOneRecord(String sql, Map<String, ?> paramMap, Class<T> requiredType, IORMConfig<T> config)
			throws IllegalDataRecordException, EmptyResultDataAccessException {
		
		List<T> lst = query(sql, paramMap, requiredType, config);
		if (lst == null || lst.size() == 0) {
			return null;
		}
		if (lst.size() > 1)
			throw new IllegalDataRecordException(ILLEGAL_DATA_RECORD_EXCEPTION_INFO);
		return lst.get(0);
	}
	
	
	@Override
	public <T> T queryForOneRecord(String sql, Object[] args, Class<T> requiredType, IORMConfig<T> config) {
		List<T> lst = query(sql, args, requiredType, config);
		if (lst == null || lst.size() == 0) {
			return null;
		}
		if (lst.size() > 1)
			throw new IllegalDataRecordException(ILLEGAL_DATA_RECORD_EXCEPTION_INFO);
		return lst.get(0);
	}

	
	@Override
	public <T> PagingData<T> queryForPagingData(String sql, Map<String, ?> paramMap, Class<T> requiredType,
			IORMConfig<T> config) throws DataAccessException {
		SqlCompiler.QueryStruct struct = SqlCompiler.compileQuery(paramMap, sql);
		String countSQL = SqlCompiler.getDialect().getCountSQL(struct.getSql());
		int count = queryForOneRecord(countSQL, paramMap, Integer.class, null);

		String sqlTemp = getPageSql(struct.getSql());
		List<T> data = query(sqlTemp, paramMap, requiredType, config);

		return buildPagingData(data, count);
	}
	
	
	@Override
	public <T> PagingData<T> queryForPagingData(String sql, Object[] args, Class<T> requiredType, IORMConfig<T> config) 
			throws DataAccessException {
		
		String countSql = SqlCompiler.getDialect().getCountSQL(sql);
		int count = queryForOneRecord(countSql, args, Integer.class, null);
		String sqlTemp = getPageSql(sql);
	    List<T> data = query(sqlTemp, args, requiredType, config);
		
		return buildPagingData(data, count);
	}
	
	
	@Override
	public PagingData<Map<String,Object>> queryForPagingData(String sql, Map<String, ?> paramMap, IORMConfig<Map<String,Object>> config) 
			throws DataAccessException {
			
		SqlCompiler.QueryStruct struct = SqlCompiler.compileQuery(paramMap, sql);
		String countSQL = SqlCompiler.getDialect().getCountSQL(struct.getSql());
		int count = queryForOneRecord(countSQL, paramMap, Integer.class, null);

		String sqlTemp = getPageSql(struct.getSql());
		List<Map<String,Object>> data = query(sqlTemp, paramMap, config);

		return buildPagingData(data, count);
	}
	
	
	@Override
	public PagingData<Map<String,Object>> queryForPagingData(String sql, Object[] args, IORMConfig<Map<String,Object>> config) 
			throws DataAccessException {
		
		String countSql = SqlCompiler.getDialect().getCountSQL(sql);
		int count = queryForOneRecord(countSql, args, Integer.class, null);
		String sqlTemp = getPageSql(sql);
	    List<Map<String,Object>> data = query(sqlTemp, args, config);
		
		return buildPagingData(data, count);
	}
	
	
	private <T> PagingData<T> buildPagingData(List<T> data, int count) {
		int pagination = PagingContextHolder.getPagingContext().getPagination();
		int pageLength = PagingContextHolder.getPagingContext().getPageLength();
		
		int pages = count / pageLength;
		if(count % pageLength > 0) pages++;
		
		return new PagingData<T>(pagination, pages, pageLength, count, data);
	}

	
	private String getPageSql(String sql) {
		int start = PagingContextHolder.getPagingContext().getPageStart();
		int length = PagingContextHolder.getPagingContext().getPageLength();
		return SqlCompiler.getDialect().getPagedSQL(sql, start, length);
	}

	
	@Override
	public int update(String sql, Object... args) throws DataAccessException {
		return jdbcTemplate.update(sql, args);
	}

	
	@Override
	public int update(String sql, Map<String, ?> paramMap) throws DataAccessException {
		return namedParameterJdbcTemplate.update(sql, paramMap);
	}

	
	@Override
	public <T> int update(String sql, T bean) throws DataAccessException {
		Map<String, Object> map = BeanUtil.toMap(bean);
		return update(sql, map);
	}

	
	@Override
	public <T> int update(T bean) throws DataAccessException {
		DataValidator dv = new DataValidator(bean.getClass(), DataValidator.VALIDAT_MODE_UPDATE);

		try {
			dv.validate(bean);
		} catch (DataValidateException e) {
			throw e;
		}

		String sql = SqlCompiler.getUpdateSql(bean.getClass());
		return update(sql, bean);
	}
	
	@Override
	public <T> int update(T bean, String[] specifiedProp) {
		DataValidator dv = new DataValidator(bean.getClass(), DataValidator.VALIDAT_MODE_UPDATE);

		try {
			dv.validate(bean, specifiedProp);
		} catch (DataValidateException e) {
			throw e;
		}

		String sql = SqlCompiler.getUpdateSql(bean.getClass(), specifiedProp);
		return update(sql, bean);
	}

	
	@Override
	public int update(String sql) throws DataAccessException {
		return jdbcTemplate.update(sql);
	}
	
	
	@Override
	public <T> int[] batchUpdate(String sql, List<T> datas) throws DataAccessException {
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(datas.toArray());
		return batchUpdate(sql, batch);
	}

	
	private int[] batchUpdate(String sql, SqlParameterSource[] batch) {
		int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, batch);
		return updateCounts;
	}
	
	
	@Override
	public int[] batchUpdate(String sql, Map<String, ?>[] paramMapArray) throws DataAccessException {
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(paramMapArray);
		return batchUpdate(sql, batch);
	}
	
	
	@Override
	public <T> int[] batchInsert(List<T> datas) throws DataAccessException {
		T temp = datas.get(0);
		DataValidator dv = new DataValidator(temp.getClass(), DataValidator.VALIDAT_MODE_INSERT);

		try {
			for (T bean : datas) {
				dv.validate(bean);
			}
		} catch (DataValidateException e) {
			throw e;
		}

		String sql = SqlCompiler.getInsertSql(temp.getClass());
		return batchUpdate(sql, datas);
	}

	
	@Override
	public <T> Number insertAndReturnKey(T bean) throws DataAccessException {
		DataValidator dv = new DataValidator(bean.getClass(), DataValidator.VALIDAT_MODE_INSERT);

		try {
			dv.validate(bean);
		} catch (DataValidateException e) {
			throw e;
		}

		String tableName = SqlCompiler.getTableName(bean.getClass());
		String autoIncrementColName = SqlCompiler.getAutoIncrementColName(bean.getClass());
		if (autoIncrementColName == null) {
			throw new DataValidateException("bean中没有指定自增字段.....");
		}
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
		simpleJdbcInsert.withTableName(tableName).usingGeneratedKeyColumns(autoIncrementColName);
		SqlParameterSource param = new BeanPropertySqlParameterSource(bean);
		Number key = simpleJdbcInsert.executeAndReturnKey(param);
		return key;
	}
	
	
	@Override
	public <T> int delete(T bean) {
		String sql = SqlCompiler.getDelSql(bean.getClass());
		return update(sql, bean);
	}

}
