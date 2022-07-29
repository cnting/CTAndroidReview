package com.example.asmdemo;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Collections;

/**
 * Created by cnting on 2022/7/28
 */
class CustomGradlePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        System.out.println("这是自定义插件");
        AppExtension appExtension = (AppExtension) project.getProperties().get("android");
        appExtension.registerTransform(new CustomTransform(), Collections.EMPTY_LIST);
    }
}
