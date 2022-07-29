package com.example.aspectjdemo.aspectJ;

import android.os.Trace;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Created by cnting on 2022/7/29
 */
@Aspect
public class TraceTagAspectJ {
    @Before("execution(* android.app.Activity+.onCreate(..))")
    public void before(JoinPoint joinPoint) {
        Trace.beginSection(joinPoint.getSignature().toString());
    }

    @After("execution(* android.app.Activity+.onCreate(..))")
    public void after() {
        Trace.endSection();
    }

}
