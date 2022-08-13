package com.example.plugin

import android.content.res.AssetManager
import android.content.res.Resources
import androidx.appcompat.view.ContextThemeWrapper


/**
 * Created by cnting on 2022/8/13
 *
 */
open class PluginContext : ContextThemeWrapper() {

    private var pluginClassLoader: ClassLoader? = null
    private var pluginResources: Resources? = null

    fun setClassLoader(classLoader: ClassLoader) {
        pluginClassLoader = classLoader
    }

    fun setResources(resources: Resources) {
        pluginResources = resources
    }

    override fun getClassLoader(): ClassLoader {
        return pluginClassLoader ?: super.getClassLoader()
    }

    override fun getResources(): Resources {
        return pluginResources ?: super.getResources()
    }

    override fun getAssets(): AssetManager {
        return pluginResources?.assets ?: super.getAssets()
    }

    override fun getSystemService(name: String): Any {
        return super.getSystemService(name)
    }
}