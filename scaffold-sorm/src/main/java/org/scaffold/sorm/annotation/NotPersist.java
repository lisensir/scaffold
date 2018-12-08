package org.scaffold.sorm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当数据实体中的字段使用了该注解，则说明该字段不参与数据持久化
 *
 * @author lisen
 * @date 2016/9/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotPersist {
}
