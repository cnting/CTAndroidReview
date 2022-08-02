package com.example.asmdemo

import com.android.build.gradle.AppExtension
import com.example.asmdemo.transform.AnnotationParserClassTransform
import com.example.asmdemo.transform.PrivacyMethodReplaceTransform
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.*

/**
 * Created by cnting on 2022/7/28
 */
class CustomGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("这是自定义插件")
        val appExtension = project.properties["android"] as AppExtension?
        appExtension?.registerTransform(AnnotationParserClassTransform(project))
        appExtension?.registerTransform(
            PrivacyMethodReplaceTransform(project),
            Collections.EMPTY_LIST
        );
    }
}