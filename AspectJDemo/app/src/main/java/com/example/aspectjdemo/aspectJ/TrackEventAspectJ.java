package com.example.aspectjdemo.aspectJ;

import android.util.Log;

import com.example.aspectjdemo.track.TrackEvent;
import com.example.aspectjdemo.track.TrackParameter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.JSONObject;

import java.lang.annotation.Annotation;

/**
 * Created by cnting on 2022/7/29
 * 埋点
 */
@Aspect
public class TrackEventAspectJ {
    @Around("execution(@com.example.aspectjdemo.track.TrackEvent * *(..))")
    public void trackEvent(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取方法上的注解
        TrackEvent trackEvent = signature.getMethod().getAnnotation(TrackEvent.class);

        String eventName = trackEvent.eventName();
        String eventId = trackEvent.eventId();

        JSONObject params = new JSONObject();
        params.put("eventName", eventName);
        params.put("eventId", eventId);

        //获取方法参数的注解
        Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();
        if (parameterAnnotations.length != 0) {
            int i = 0;
            for (Annotation[] parameterAnnotation : parameterAnnotations) {
                for (Annotation annotation : parameterAnnotation) {
                    if (annotation instanceof TrackParameter) {
                        String key = ((TrackParameter) annotation).value();
                        params.put(key, joinPoint.getArgs()[i++]);
                    }
                }
            }
        }

        Log.e("===>", "上报数据---->" + params.toString());

        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
