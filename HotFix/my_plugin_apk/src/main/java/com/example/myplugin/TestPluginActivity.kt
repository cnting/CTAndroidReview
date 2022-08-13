package com.example.myplugin

import android.os.Bundle
import com.example.plugin.activity.PluginActivity

/**
 * 独立运行时继承Activity，打插件包时通过改为继承 PluginActivity
 * TODO 通过 Transform 改成继承 PluginActivity
 */
class TestPluginActivity : PluginActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plugin)
    }
}