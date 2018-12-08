package org.scaffold.sorm.dialect;

/**
 * 数据库方言接口
 *
 * @author lisen
 * @date 2016/8/29
 */
public interface Dialect {

  /**
   * <pre>
   * 返回将java String值转为数据库日期类型的sql片段的方法。主要在 insert,update 中使用。
   *
   * 	如 Oracle 返回  to_date('2007-01-01','yyyy-mm-dd')
   * </pre>
   *
   * @param colValue
   *            要插入列的值
   * @return
   */
  String char2Date(String colValue);

  /**
   * <pre>
   * 返回将java String值转为数据库日期时间类型的sql片段的方法。主要在 insert,update 中使用。
   *
   * 	如 Oracle 返回  to_date('2007-01-01','yyyy-mm-dd hh24:mi:ss')
   * </pre>
   *
   * @param colValue
   *            要插入列的值
   * @return
   */
  String char2Datetime(String colValue);

  /**
   * <pre>
   *
   * 根据当前的数据库语法，返回连接两个字符串的SQL 片断。
   *   改进要求： public 类型 修改为 protected 类型。
   *
   * 		Oracle 数据库返回 str1 +&quot;||&quot;+ str2;
   * 			concat( userid, username) = userid||username;
   * 			concat( '001',  'admin' ) = '001'||'admin';
   * 		MySQL  数据库返回 concat(str1, str2);
   * 			concat( userid, username) = concat(userid,username);
   * 			concat( '001',  'admin' ) = concat('001' ,'admin');
   * 		SQLServer  数据库返回 str1 +&quot;+&quot;+ str2;
   * 			concat( userid, username) = userid + &quot;+&quot; + username);
   * 			concat( '001',  'admin' ) = '001' + &quot;+&quot; + 'admin');
   * </pre>
   *
   * @param str1
   *            字符串或者列名
   * @param str2
   *            字符串或者列名
   * @return
   */
  String concat(String str1, String str2);

  /**
   * <pre>
   * 返回将数据库日期类型转为字符类型的sql片段的方法。主要在 select 语句中使用。
   *
   * 	如 Oracle 返回  to_char(birthday,'yyyy-mm-dd')
   * </pre>
   *
   * @param colName
   *            列名称
   * @return
   */
  String date2Char(String colName);

  /**
   * <pre>
   * 返回将数据库日期类型转为字符类型的sql片段的方法。主要在 select 语句中使用。
   *
   * 	如 Oracle 返回  to_char(birthday,'yyyy')，to_char(birthday,'yyyy-MM')，to_char(birthday,'yyyy-MM-dd')
   * </pre>
   *
   * @param colName
   *            列名称
   * @param type
   *            精度到达的类型， YY年 MM月 DD日 HH时 MI分 SS秒
   * @return
   */
  String date2Char(String colName, String type);

  /**
   * <pre>
   * 返回将数据库日期时间类型转为字符类型的sql片段的方法。主要在 select 语句中使用。
   *
   * 	如 Oracle 返回  to_char( beginTime,'yyyy-mm-dd hh24:mi:ss')
   * </pre>
   *
   * @param colName
   *            列名称
   * @return
   */
  String datetime2Char(String colName);

  String getDateCollHour(String colName);

  /**
   * <pre>
   * 返回mySQL的时间类型中的，小时
   *
   * 	如 Oracle 返回  to_char(sysdate,'hh')
   * My SQL 返回 HOUR('2010-01-01 10:05:03')
   * </pre>
   *
   * @param colValue
   * @return
   */
  String getHour(String colValue);

  /**
   * <pre>
   *
   * 获取唯一标识符的表达式。
   * </pre>
   *
   * @param tblName
   *            需要使用标识符的表，如"tsys_flow","employee","demo_table"
   * @return
   */
  //String identity(String tblName);

  /**
   * <pre>
   *
   * 获取唯一标识符数组的表达式。
   * </pre>
   *
   * @param tblName
   *            需要使用标识符的表，如"tsys_flow","employee","demo_table"
   * @param numbers
   *            指定唯一标识的个数
   * @return
   */
  //String[] identity(String tblName, int numbers);


  /**
   *
   * 注册序列号Entity并初始化序列值.
   *
   * @param tblName 序列名，通常就是数据库表名
   * @return 成功为0，不成功为负数
   */
  //int regSeqEntity(String tblName);


  /**
   * <pre>
   *
   * 根据当前的数据库语法，转换可能为null的表达式。
   *
   * 		Oracle 数据库返回 nvl(exp1, exp2);
   * 		MySQL  数据库返回 coalesce( exp1, exp2);
   * 		SQLServer  数据库返回 isNull(exp1, exp2);
   * </pre>
   *
   * @param str1
   *            字符串或者列名
   * @param str2
   *            字符串或者列名
   * @return
   */
  String switchNull(String exp1, String exp2);

  /**
   * <pre>
   * 获取系统时间的函数表达式。
   * </pre>
   *
   * @return
   */
  String sysDatetime();


  /**
   * <pre>
   * 列转化为行 函数，如oracle返回：wm_concat(colName)
   * </pre>
   *
   * @return
   */
  String colConcat(String colName);

  /**
   * <pre>
   * 字符串截取函数。
   * @param colName
   * @param start
   * @param length
   * @return
   */
  String subStr(String colName, int start, Integer length);

  /**
   * 获取前topSize条记录.
   * @param originalSql
   * @param topSize
   * @return
   */
  String getTopSQL(String originalSql, int topSize);

  /**
   * 获取第pageIndex页记录，每页记录数pageSize
   * @param originalSql
   * @param pageIndex  页码(从1开始)
   * @param pageSize 每页大小
   * @return
   */
  String getPagedSQL(String originalSql, int pageIndex, int pageSize) ;

  /**
   * 获取统计语句.
   * @param originalSql
   * @return
   */
  String getCountSQL(String originalSql);


  /**
   * @param colName 列名
   * @param howManyMonth  几个月（正负）
   * @return
   */
  String addMonths(String colName, int howManyMonth);

  /**
   * @param colName1 列名1
   * @param colName2 列名2
   * @return
   */
  String addMonths(String colName1, String colName2);

  /**
   * @param colName 列名
   * @param howManyDay  几天（正负）
   * @return
   */
  String addDays(String colName, int howManyDay);

}
