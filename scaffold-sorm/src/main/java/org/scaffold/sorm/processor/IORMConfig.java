package org.scaffold.sorm.processor;

/**
 * 函数接口，用来对列的值进行转换.
 * <p>
 *   例如：数据表中存放的是字典值，在显示时需要将字典值转换为描述信息。
 *   此时可以在查询中注册一个<code>IORMConfig</code>函数接口的实现用
 *   来执行转换
 * </p>
 */
@FunctionalInterface
public interface IORMConfig <T> {


  /**
   * 处理ORM映射时的属性值转换方法，在数据表中存放编码，查询结果中需要编码对应的描述值时使用.
   * <ul>
   *   <li>在处理数据库结果集时，如果当前列名与<code>IORMConfig</code>实现类中列名称匹配，则执行转换，
   *   同时返回<code>true</code>,表明该列已完成列值到属性映射，并放入结果中<code>ORMProcessor</code>
   *   将不再进行处理，直接跳过该列。该方法返回<code>false</code>表示当前列不需执行转换，系统需要使用
   *   默认的处理规则处理列值到属性值的映射.
   *   <li>如果查询结果中有多个属性需要进行转换，则可以在具体实现类中判断需要转换的列名，逐个处理，但每
   *   处理一个属性都要返回<code>true</code>，以便于告诉<code>ORMProcessor</code>该列已处理。
   * </ul>
   * @param columnName 列名
   * @param columnValue 当前类的值
   * @param data 保存结果的记录，可以是Bean，Map，json
   * @return boolean，返回true表示该列已执行转换，false表示该列不需转换，采用系统默认规则处理
   */
	boolean convert(String columnName, Object columnValue, T data);
	
	
}
