package com.simpleIoC.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)//表明该注解可使用范围是变量
@Retention(RetentionPolicy.RUNTIME)//表明注解在什么时候执行
@Documented//javadoc
public @interface myAutowired {
    public String id() default ""; 
    public boolean required() default true;
}