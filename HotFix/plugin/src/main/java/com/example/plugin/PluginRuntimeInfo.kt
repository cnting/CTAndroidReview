package com.example.plugin

import android.content.res.Resources

/**
 * Created by cnting on 2022/8/13
 *
 */
data class PluginRuntimeInfo(val classLoader: ClassLoader, val resources: Resources)