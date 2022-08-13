package com.example.plugin

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.patch.PatchUtil
import com.example.plugin.activity.PluginContainerActivity
import dalvik.system.DexClassLoader

/**
 * Created by cnting on 2022/8/12
 *
 */
object PluginManager {

    const val KEY_COMPONENT = "component_name"
    const val KEY_PLUGIN = "plugin_name"

    val pluginRuntimeInfo = mutableMapOf<String, PluginRuntimeInfo>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadPlugin(context: Context, pluginName: String): Boolean {
        if (!PatchUtil.hasPatch(context, pluginName)) {
            PatchUtil.downloadPatch(context, pluginName)
        }
        if (!pluginRuntimeInfo.containsKey(pluginName)) {
            val pluginFilePath = PatchUtil.getPatchFile(context, pluginName).absolutePath
            val pluginClassLoader = createPluginClassLoader(context, pluginFilePath)
            val pluginResources = createPluginResources(context, pluginFilePath)

            pluginRuntimeInfo[pluginName] = PluginRuntimeInfo(pluginClassLoader, pluginResources)
        }

        return pluginRuntimeInfo.containsKey(pluginName)
    }

    private fun createPluginClassLoader(context: Context, pluginFilePath: String): ClassLoader {
        return DexClassLoader(
            pluginFilePath,
            context.cacheDir.absolutePath,
            null,
            javaClass.classLoader
        )
    }

    /**
     * 访问插件资源
     */
    private fun createPluginResources(hostContext: Context, pluginFilePath: String): Resources {
        val packageManager = hostContext.packageManager
        val applicationInfo = ApplicationInfo()
        val hostApplicationInfo = hostContext.applicationInfo
        applicationInfo.packageName = hostApplicationInfo.packageName
        applicationInfo.uid = hostApplicationInfo.uid
        applicationInfo.publicSourceDir = pluginFilePath
        applicationInfo.sourceDir = pluginFilePath
        applicationInfo.sharedLibraryFiles = hostApplicationInfo.sharedLibraryFiles

        val pluginResources = packageManager.getResourcesForApplication(applicationInfo)
        return PluginResources(pluginResources, hostContext.resources)
    }


    fun startActivity(context: Context, pluginName: String, componentName: String) {
        context.startActivity(Intent(context, PluginContainerActivity::class.java)
            .apply {
                putExtra(KEY_PLUGIN, pluginName)
                putExtra(KEY_COMPONENT, componentName)
            })
    }
}