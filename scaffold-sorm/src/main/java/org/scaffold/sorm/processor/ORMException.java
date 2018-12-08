package org.scaffold.sorm.processor;

/**
 * 结果集转换异常
 *
 * @author lisen
 * @date 2016/8/30
 */
public class ORMException extends RuntimeException{

  public ORMException(String msg) {
    super(msg);
  }

  public ORMException(String msg, Throwable t) {
    super(msg, t);
  }

}
