package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author polar
 * @version 1.0
 * @since 2025/9/24 17:10
 * 这个注解的作用是在切面扫描的方法中，精准的定位到 insert 和 update 方法，
 * 从而实现公共字段的自动填充 防止其余方法被切面扫描到影响性能
 *value 用来标记当前方法是 insert 还是 update 操作 ，operationType 枚举类记录了这两种操作
 *
 *Target(ElementType.METHOD)  这个注解表示该注解只能作用在方法上
 *retention(RetentionPolicy.RUNTIME)  这个注解表示该注解会在运行时保留，可以通过反射获取
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    OperationType value();


}
