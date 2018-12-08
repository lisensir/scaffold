package org.scaffold.sorm.validator;

import org.springframework.dao.DataAccessException;

/**
 * 数据验证异常.
 * 在通过数据实体生成数据库插入、更新、删除等语句时会先对数据进行合法性验证。
 * 如果验证不通过则报该异常。
 * @author lisen
 */
public class DataValidateException extends DataAccessException {

	private static final long serialVersionUID = -5383488556289133464L;

	public DataValidateException(String msg) {
		super(msg);
	}
	
	public DataValidateException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
