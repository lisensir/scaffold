package org.scaffold.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DateUtil {


	/**
	 * 将字符串形式的日期值转化为Date，格式:yyyy-MM-dd HH:mm:ss
	 * @param dateStr 日期字符串
	 * @return Date
	 */
	public static Date parseDateTime(String dateStr) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			Date d = sdf.parse(dateStr);
			return d;
		} catch (ParseException e) {
			throw new DateParseException(e.getMessage(), e.getCause());
		}

	}


	/**
	 * 根据指定的格式将日期转化为Date
	 * @param dateStr 日期字符串
	 * @param format 格式字符串
	 * @return Date
	 */
	public static Date parseDateTime(String dateStr, String format) {

		SimpleDateFormat sdf = new SimpleDateFormat(format);

		try {
			Date d = sdf.parse(dateStr);
			return d;
		} catch (ParseException e) {
			throw new DateParseException(e.getMessage(), e.getCause());
		}

	}
	
	
	/**
	 * 将日期型按照指定的格式转换为字符串
	 * @param date 需要转换的日期
	 * @param format 指定的转换格式，如果为null，则默认为<code>yyyy-MM-dd HH:mm:ss</code>
	 * @return 日期的字符串格式
	 */
	public static String formatDate(Date date, String format) {
		
		String _format = format;
		if(_format == null || "".equals(_format)) {
			_format = "yyyy-MM-dd HH:mm:ss";
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat(_format);
		
		return sdf.format(date);
	}


	/**
	 * 获取当前日期
	 * @return Date
	 */
	public static Date getNow(){
		return new Date(System.currentTimeMillis());
	}

}
