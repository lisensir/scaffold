package org.scaffold.sorm.actuator;

import org.springframework.dao.DataAccessException;

/**
 * 当查询单条记录单返回的记录数多于一条时报该异常
 *
 * @author lisen 2016/8/30
 */
public class IllegalDataRecordException extends DataAccessException {

  public IllegalDataRecordException(String msg) {
    super(msg);
  }

  public IllegalDataRecordException(String msg, Throwable cause) {
    super(msg, cause);
  }

}
