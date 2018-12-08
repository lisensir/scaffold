package org.scaffold.sorm.utils;

/**
 * 为方便使用，将Bean转换Map时可能发生的受查异常转化为运行是异常
 *
 * @author lisen 2016/8/31
 */
public class BeanToMapException extends RuntimeException {

  public BeanToMapException() {
    super();
  }

  public BeanToMapException(String msg) {
    super(msg);
  }

  public BeanToMapException(String msg, Throwable cause) {
    super(msg, cause);
  }

}
