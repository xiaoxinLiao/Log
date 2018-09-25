package com.liaoxin.log.annotations;

import java.lang.annotation.*;

/**
 * 操作日志注解标志
 * 作用在类，方法，接口上
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD,ElementType.TYPE})
public @interface OperationLog {
    /**
     * 操作功能名称
     * @return
     */
    String functionName() default "";
}
