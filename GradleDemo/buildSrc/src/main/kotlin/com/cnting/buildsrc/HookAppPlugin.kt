package com.cnting.buildsrc

import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin

/**
 * Created by cnting on 2023/1/7
 * https://juejin.cn/post/7095511659925471240
 */
class HookAppPlugin : BasePlugin() {
    override fun apply(project: Project) {
//        super.apply(project)
        println("====>hook AppPlugin demo")

        RuntimeException().printStackTrace()

        project.apply(INTERNAL_PLUGIN_ID)
    }

    private val INTERNAL_PLUGIN_ID = mapOf("plugin" to "com.android.internal.application")
}