package com.example.ioc_annotation;

/**
 * Created by cnting on 2022/7/27
 */
public interface ViewInjector<T> {
    void inject(T t, Object source);
}
