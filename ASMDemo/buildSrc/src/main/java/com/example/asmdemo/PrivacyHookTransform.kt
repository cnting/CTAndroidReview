package com.example.asmdemo

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.AppExtension
import com.example.asmdemo.transform.AnnotationParserClassTransform
import com.example.asmdemo.transform.PrivacyMethodReplaceTransform
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.*

/**
 * Created by cnting on 2022/7/28
 */
class PrivacyHookTransform : Plugin<Project> {
    override fun apply(project: Project) {
        println("这是自定义插件")

//        val appExtension = project.properties["android"] as AppExtension?
//        appExtension?.registerTransform(AnnotationParserClassTransform(project))
//        appExtension?.registerTransform(
//            PrivacyMethodReplaceTransform(project),
//            Collections.EMPTY_LIST
//        )


        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            variant.instrumentation.transformClassesWith(
                AnnotationParserClassTransform::class.java,
                InstrumentationScope.PROJECT
            ) {}
            variant.instrumentation.setAsmFramesComputationMode(
                FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
            )
        }
    }
}