package com.example.aspectjdemo.aspectJ;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created by cnting on 2022/7/29
 */
@Aspect
public class TimingAspect {
    /**
     * cflow:掌握调用流
     * cflow(execution(* com.example.aspectjdemo.MainActivity.test(..))) 表示调用test()时所包含的JPoint，包括自身JPoint
     * execution(* *(..)) 的作用是去除TimingAspect自身的代码，避免自己拦截自己，形成死循环
     */
    @Around("execution(* *(..)) && cflow(execution(* com.example.aspectjdemo.MainActivity.test(..)))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        Log.e("===>", joinPoint.getSignature().toString() + "->" + (end - start) + " ms");
        return result;
    }
}
