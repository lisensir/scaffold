package org.scaffold.sorm.dialect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scaffold.sorm.sqlparse.SqlCompiler;

/**
 * MYSQL方言实现类
 *
 * @author lisen
 * @date 2016/8/29
 */
public class MysqlDialect implements Dialect {

  private final String DATEFORMAT = "%Y-%m-%d";

  private final String DATETIMEFORMAT = "%Y-%m-%d %H:%i:%s";

  private static MysqlDialect mysqlDialect = null;

  private MysqlDialect() {

  }

  public static MysqlDialect getInstance() {
    if(mysqlDialect == null)
      return new MysqlDialect();
    return mysqlDialect;
  }

  /**
   * colValue 一般是变量,用到=号的右边
   */
  @Override
  public String char2Date(String colValue) {

    return "str_to_date('" + colValue + "','" + this.DATEFORMAT + "')";
  }

  /**
   * colValue 一般是变量,用到=号的右边
   */
  @Override
  public String char2Datetime(String colValue) {

    return "str_to_date('" + colValue + "','" + this.DATETIMEFORMAT + "')";
  }

  @Override
  public String concat(String str1, String str2) {

    return "CONCAT(" + str1 + "," + str2 + ")";
  }

  @Override
  public String date2Char(String colName) {

    return "DATE_FORMAT(" + colName + ",'%Y-%m-%d')";
  }

  @Override
  public String date2Char(String colName, String type) {

    String rstStr = "DATE_FORMAT(" + colName + ", '%Y-%m-%d')";
    if (type.equalsIgnoreCase("YY")) {
      rstStr = "DATE_FORMAT(" + colName + ", '%Y')";
    }
    if (type.equalsIgnoreCase("MM")) {
      rstStr = "DATE_FORMAT(" + colName + ", '%Y-%m')";
    }
    if (type.equalsIgnoreCase("DD")) {
      rstStr = "DATE_FORMAT(" + colName + ", '%Y-%m-%d')";
    }
    if (type.equalsIgnoreCase("HH")) {
      rstStr = "DATE_FORMAT(" + colName + ", '%Y-%m-%d %H')";
    }
    if (type.equalsIgnoreCase("MI")) {
      rstStr = "MINUTE(" + colName +")";
    }
    if (type.equalsIgnoreCase("SS")) {
      rstStr = "DATE_FORMAT(" + colName + ", '%Y-%m-%d %H:%i:%s')";
    }

    if (type.equalsIgnoreCase("SHORT_YY")) {
      rstStr = "DATE_FORMAT(" + colName + ", '%Y')";
    }
    if (type.equalsIgnoreCase("SHORT_MM")) {
      rstStr = "DATE_FORMAT(" + colName + ", '%m')";
    }
    if (type.equalsIgnoreCase("SHORT_DD")) {
      rstStr = "DATE_FORMAT(" + colName + ", '%d')";
    }
    if (type.equalsIgnoreCase("SHORT_HH")) {
      rstStr = "DATE_FORMAT(" + colName + ", '%H')";
    }
    if (type.equalsIgnoreCase("SHORT_MI")) {
      rstStr = "MINUTE(" + colName +")";
    }
    if (type.equalsIgnoreCase("SHORT_SS")) {
      rstStr = "DATE_FORMAT(" + colName + ", '%s')";
    }
    if (type.equalsIgnoreCase("YYYYMMDD")) {
      rstStr = "DATE_FORMAT(" + colName + ", '%Y%m%d')";
    }
    if (type.equalsIgnoreCase("MMDD")) {
      rstStr = "DATE_FORMAT(" + colName + ", '%m%d')";
    }

    return rstStr;
  }

  @Override
  public String datetime2Char(String colName) {

    return "DATE_FORMAT(" + colName + ",'%Y-%m-%d %H:%i:%s')";
  }

  @Override
  public String getDateCollHour(String colName) {

    return " HOUR('" + colName + "')";
  }

  @Override
  public String getHour(String colValue) {

    return " HOUR(" + this.datetime2Char(colValue) + ")";
  }


  @Override
  public String switchNull(String exp1, String exp2) {

    return "coalesce(" + exp1 + "," + exp2 + ")";
  }

  /*
   * (non-Javadoc)
   *
   * @see com.wzwen.pub.context.DatabaseManager#sysDatetime()
   */
  @Override
  public String sysDatetime() {

    return "now()"; // sysdate();
  }

  @Override
  public String colConcat(String colName){
    return "group_concat("+colName+")";
  }

  @Override
  public String subStr(String colName, int start, Integer length) {

    if(length == null) {

      return "substr(" + colName + ", " + start + ")";

    } else {

      return "substr(" + colName + ", " + start + ", " + length + ")";

    }

  }

  @Override
  public String getTopSQL(String originalSql, int topSize) {
    StringBuffer sql = new StringBuffer(originalSql);
    sql.append(" limit 0,").append(topSize);
    return sql.toString();
  }


  @Override
  public String getPagedSQL(String originalSql, int pageStart, int pageLength) {
    StringBuffer sql = new StringBuffer(originalSql);
    sql.append(" limit " + pageStart + ", " + pageLength + "");
    return sql.toString();
  }


  @Override
  public String getCountSQL(String originalSql) {

    return SqlCompiler.getCountSql(originalSql);
  }


  @Override
  public String addMonths(String colName, int howManyMonth) {
    return "DATE_ADD( " + colName + " , INTERVAL " + howManyMonth + " MONTH)" ;
  }


  @Override
  public String addMonths(String colName1, String colName2) {
    return "DATE_ADD( " + colName1 + " , INTERVAL " + colName2 + " MONTH)" ;
  }


  @Override
  public String addDays(String colName, int howManyDay) {
    return "DATE_ADD( " + colName + " , INTERVAL "+howManyDay + " DAY)" ;
  }


}
