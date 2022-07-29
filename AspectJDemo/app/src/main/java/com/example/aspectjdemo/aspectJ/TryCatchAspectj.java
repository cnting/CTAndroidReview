package com.example.aspectjdemo.aspectJ;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by cnting on 2022/7/29
 */
@Aspect
public class TryCatchAspectj {

    @Pointcut("execution(* *..*.testAround())")
    public void methodTryCatch(){

    }

    /**
     * @Around 可以替换原代码。如果需要执行原代码，可以使用 ProceedingJoinPoint#proceed()
     */
    @Around("methodTryCatch()")
    public void aroundTryJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable{
        try {
            joinPoint.proceed();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
