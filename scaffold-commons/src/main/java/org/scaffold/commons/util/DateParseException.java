package org.scaffold.commons.util;

/**
 * 在日期处理时如果发生解析异常则将解析异常转换为该异常抛出。
 * ParseException异常为受查异常，为方便使用转换为非受查异常。
 * @see java.text.ParseException
 */
@SuppressWarnings("unused")
public class DateParseException extends RuntimeException {

    public DateParseException() {}

    public DateParseException(String msg) {
        super(msg);
    }

    public DateParseException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
