package org.scaffold.sorm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定字段持久化时，对应的数据表中的字段名.
 * 如果Bean中的字段和数据库中的字段是通过默认规则对应的，则可以
 * 不用指定
 * <p>
 *   默认规则：
 * <ul>
 *   <li>数据表中的字段: 以“_”风格分隔</li>
 *   <li>Bean中字段：将以“_”风格分隔改为驼峰风格</li>
 * </ul>
 *
 * @author lisen
 * @date 2016/9/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Persist {

  String name() default "";

}
