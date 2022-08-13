package com.example.plugin.activity

import android.app.Activity

/**
 * Created by cnting on 2022/8/12
 * 插件容器Activity
 */
class PluginContainerActivity : BasePluginContainerActivity() {

    init {
        hostActivityDelegate =
            PluginActivityDelegate()
                .apply { setDelegator(this@PluginContainerActivity) }
    }

}