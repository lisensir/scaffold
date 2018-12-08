package org.scaffold.sorm.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类，用于处理Sql中的条件表达式
 *
 * @author lisen
 * @date 2016/8/29
 */
public class RegexUtil {

  public static final Pattern mailtoPattern = Pattern
          .compile("mailto:([a-zA-Z0-9\\.]+@[a-zA-Z0-9\\.]+\\.[a-zA-Z0-9]+)");

  public static final Pattern emailPattern = Pattern
          .compile("\\b[a-zA-Z0-9\\.]+(@)([a-zA-Z0-9\\.]+)(\\.)([a-zA-Z0-9]+)\\b");

  public static final Pattern pTag = Pattern.compile("(\\$\\{[^}]+\\})", Pattern.CASE_INSENSITIVE);// 匹配${}

  public static final Pattern pCon = Pattern.compile(
          "([A-Za-z0-9_]+)\\.{0,1}([A-Za-z0-9_]+)[\\,|\\s]*.*(in|like|=|>=|<=|<>|<|>)\\s*\\(*:{1}\\s*(\\w+)\\s*",
          Pattern.CASE_INSENSITIVE);// 匹配条件属性

  public static final Pattern pDate = Pattern.compile(".*(dt|date|dateBegin|dateStart|dateEnd){1}$",
          Pattern.CASE_INSENSITIVE);// 匹配日期属性

  public static final Pattern pTime = Pattern
          .compile(".*(time|timeBegin|timeStart|timeEnd){1}$", Pattern.CASE_INSENSITIVE);// 匹配日期属性

  public static final Pattern pDtime = Pattern.compile(".*(datetime|datetimeBegin|datetimeStart|datetimeEnd){1}$",
          Pattern.CASE_INSENSITIVE);// 匹配日期时间属性

  public static final Pattern pDtimeFn = Pattern.compile(".*(date_format|to_char|to_date).*", Pattern.CASE_INSENSITIVE);// 匹配日期或时间列已经做过函数处理的

  public static final Pattern pDateFormat = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}$", Pattern.CASE_INSENSITIVE);// 匹配日期或时间列已经做过函数处理的

  public static final Pattern pDatetimeFormat = Pattern.compile(
          "^[0-9]{4}-[0-9]{2}-[0-9]{2}\\s{1}[0-9]{2}:[0-9]{2}:[0-9]{2}$", Pattern.CASE_INSENSITIVE);// 匹配日期或时间列已经做过函数处理的

  public static final Pattern pTimeFormat = Pattern.compile("^[0-9]{2}:[0-9]{2}:[0-9]{2}$", Pattern.CASE_INSENSITIVE);// 匹配日期或时间列已经做过函数处理的

  public static final Pattern pCusAddress=Pattern.compile("[\\u4e00-\\u9fa5]+[a-zA-Z]*|[0-9]+");

  public static final Pattern pDigit=Pattern.compile("[0-9]+");


  /**
   * Return the specified match "groups" from the pattern. For each group
   * matched a String will be entered in the ArrayList.
   *
   * @param pattern
   *            The Pattern to use.
   * @param match
   *            The String to match against.
   * @param group
   *            The group number to return in case of a match.
   * @return
   */
  public static ArrayList getMatches(Pattern pattern, String match, int group) {

    ArrayList matches = new ArrayList();
    Matcher matcher = pattern.matcher(match);
    while (matcher.find()) {
      matches.add(matcher.group(group));
    }
    return matches;
  }

}
