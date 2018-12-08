package org.scaffold.sorm.utils;

/**
 * 当日期日期格式化字符串不被支持时报该异常
 *
 * @author lisen 2017/2/2
 */
public class IllegalDateFormatException extends RuntimeException {

  public IllegalDateFormatException() {
    super();
  }

  public IllegalDateFormatException(String msg) {
    super(msg);
  }

  public IllegalDateFormatException(String msg, Throwable cause) {
    super(msg, cause);
  }

}
