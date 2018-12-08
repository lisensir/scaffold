package org.scaffold.sorm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lisen on 2016/8/16.
 */
public class DateUtil {

  public static final String DATE_MIN_FORMAT = "yyyyMMdd";
  public static final String TIME_MIN_FORMAT = "yyyyMMddHHmmss";
  public static final String DATE_DAY_FORMAT = "yyyy/MM/dd";
  public static final String DATE_MON_FORMAT = "yyyy/MM";
  public static final String DATE_DAYTIME_FORMAT = "yyyy/MM/dd HH:mm:ss";


  /**
   * 按指定的格式格式化日期
   * @param date
   * @param format
   * @return
   */
  public static String format(Date date, String format) {

    if(date == null) return "";

    SimpleDateFormat sdf = new SimpleDateFormat(format);

    return sdf.format(date);
  }


  /**
   * 将日期按指定格式解析成日期
   * @param dateStr 日期字符串
   * @param format 格式字符串
   * @return Date
   */
  public static Date parse(String dateStr, String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    Date d = null;
    try {
      d = sdf.parse(dateStr);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return d;
  }

  
  /**
   * 获取当前日期
   * @return
   */
  public static Date getNow() {
    return new Date();
  }

  public static Date getNow(String format) {
    Date d = getNow();
    if(format.equals(DATE_DAY_FORMAT)) {
      String day = format(d, DATE_DAY_FORMAT);
      return parse(day, DATE_DAY_FORMAT);
    }
    if(format.equals(DATE_MON_FORMAT)) {
      String mon = format(d, DATE_MON_FORMAT);
      mon += "-01";
      return parse(mon, DATE_DAY_FORMAT);
    }
    if(format.equals(DATE_DAYTIME_FORMAT)) {
      return d;
    }
    throw new IllegalDateFormatException();
  }

  /**
   * 获取指定格式的当前日期的字符串表示
   * @param format 格式字符
   * @return string
   */
  public static String getNowStr(String format) {

    return format(getNow(format),format);
  }
  
  /**
   * 获取指定日期时月中的第几天
   * @param date
   * @return
   */
  public static int getDayOfMon(Date date) {
  	Calendar c = Calendar.getInstance();
    c.setTime(date);
    return c.get(Calendar.DAY_OF_MONTH);
  }


  public static int getDayOfCurrMon() {
    return getDayOfMon(getNow());
  }
  
  /**
   * 对传入的日期参数增加指定的天数.
   * 如果传入的天数值为负则为减
   * @param date 日期
   * @param day 天数
   * @return
   */
  public static Date addDay(Date date, int day) {
    return calendarClac(date, Calendar.DAY_OF_MONTH ,day);
  }
  
  
  /**
   * 对传入的日期参数增加指定的月数.
   * 如果传入的月数值为负则为减
   * @param date 日期
   * @param month 月数值
   * @return
   */
  public static Date addMonth(Date date, int month) {
  	return calendarClac(date, Calendar.MONTH ,month);
  }

  
  /**
   * 对传入的日期参数增加指定的年数.
   * 如果传入的年数值为负则为减
   * @param date 日期
   * @param year 年数值
   * @return
   */
  public static Date addYear(Date date, int year) {
  	return calendarClac(date, Calendar.YEAR, year);
  }
  
  
  /**
   * 获取月初日期
   * @param date
   * @return
   */
  public static Date getMon(Date date) {
  	Calendar c = Calendar.getInstance();
  	c.setTime(date);
  	c.set(Calendar.DAY_OF_MONTH, 1);
  	return c.getTime();
  }


  /**
   * 获取两个日期间隔的小时数
   * @param fromDt
   * @param toDt
   * @return
   */
  public static int getIntervalHours(Date fromDt, Date toDt) {
    long f = fromDt.getTime();
    long t = toDt.getTime();

    return (int)((t - f) / (1000 * 60 * 60));
  }
  
  
  //日期计算
  private static Date calendarClac(Date date, int field, int step) {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(field, step);
      return c.getTime();
  }
  
}
