package com.example.aspectjdemo.track;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by cnting on 2022/7/29
 * 埋点
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackEvent {
    /**
     * 事件名称
     */
    String eventName() default "";

    /**
     * 事件id
     */
    String eventId() default "";
}

