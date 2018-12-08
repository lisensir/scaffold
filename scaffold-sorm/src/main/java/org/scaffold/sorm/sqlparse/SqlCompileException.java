package org.scaffold.sorm.sqlparse;

import org.springframework.dao.DataAccessException;

/**
 * SQL编译异常
 *
 * @author lisen 2016/9/18
 */
public class SqlCompileException extends DataAccessException {

  public SqlCompileException(String msg) {
    super(msg);
  }

  public SqlCompileException(String msg, Throwable cause) {
    super(msg, cause);
  }

}
