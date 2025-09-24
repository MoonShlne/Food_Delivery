package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author polar
 * @version 1.0
 * @since 2025/9/24 20:41
 * 自定义切面类.实现公共字段的填充
 *
 *
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
//使用mybatis plus 自带的公共字段填充功能 舍弃aop


//    //切入点             扫描 com.sky.service.impl 包下所有类的所有方法，并且这些方法上有 @AutoFill 注解
//    @Pointcut("execution(* com.sky.service.impl.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
//    public void autoFillPointCut() {
//    }
//
//
//    //前置通知
//    @Before("autoFillPointCut()")
//    public void autoFill(JoinPoint joinPoint) {
//        log.info("公共字段自动填充...");
//        //获取方法注解的的参数 是 insert 还是 update
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
//        OperationType value = annotation.value();
//
//        //获取被拦截方法的参数
//
//
//    }


}
