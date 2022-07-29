package com.example.aspectjdemo.aspectJ;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created by cnting on 2022/7/29
 * 拦截点击
 */
@Aspect
public class InterceptClickAspectJ {
    private Long lastTime = 0L;
    private static final Long INTERVAL = 300L;

    @Around("execution(* android.view.View.OnClickListener.onClick(..))")
    public void clickIntercept(ProceedingJoinPoint joinPoint) throws Throwable {
        if (System.currentTimeMillis() - lastTime >= INTERVAL) {
            lastTime = System.currentTimeMillis();
            joinPoint.proceed();
        } else {
            Log.e("===>", "重复点击");
        }
    }
}
