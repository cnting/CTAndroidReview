package com.example.aspectjdemo.aspectJ;

import android.util.Log;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created by cnting on 2022/7/29
 */
@Aspect
public class ReportExceptionAspectj {
    @AfterThrowing(pointcut = "call(* *..*.testAfterThrowing())",throwing = "throwable")
    public void reportException(Throwable throwable){
        Log.e("===>","throwable:"+throwable);
    }
}
