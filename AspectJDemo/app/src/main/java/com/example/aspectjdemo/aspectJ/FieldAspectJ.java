package com.example.aspectjdemo.aspectJ;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by cnting on 2022/7/29
 */
@Aspect
public class FieldAspectJ {

    /**
     * 不包含构造方法
     */
    @Pointcut("!withincode(com.example.aspectjdemo.Person.new())")
    public void invokePerson(){

    }

    /**
     * set(int com.example.aspectjdemo.Person.age)会包含构造方法中的age赋值 和 setAge()方法
     * invokePerson() 设置不包含构造方法
     */
    @Around("set(int com.example.aspectjdemo.Person.age) && invokePerson()")
    public void aroundFieldSet(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.e("===>", "around->" + joinPoint.getTarget().toString() + "#" + joinPoint.getSignature().getName());
    }
}
