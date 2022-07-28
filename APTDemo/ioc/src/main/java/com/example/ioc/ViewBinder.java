package com.example.ioc;

import android.app.Activity;

import com.example.ioc_annotation.ViewInjector;

/**
 * Created by cnting on 2022/7/27
 */
public class ViewBinder {
    private static final String SUFFIX = "$$ViewInjector";

    public static void bind(Activity activity) {
        bind(activity, activity);
    }

    private static void bind(Object host, Object root) {
        if (host == null || root == null) {
            return;
        }
        Class<?> c = host.getClass();
        //自动生成的类，实现了ViewInjector接口
        String proxyClassFullName = c.getName() + SUFFIX;
        try {
            Class<?> proxyClass = Class.forName(proxyClassFullName);
            ViewInjector viewInjector = (ViewInjector) proxyClass.newInstance();
            viewInjector.inject(host, root);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
