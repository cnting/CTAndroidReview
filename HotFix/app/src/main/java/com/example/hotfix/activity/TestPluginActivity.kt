package com.example.hotfix.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.hotfix.R
import com.example.plugin.PluginManager

/**
 * Created by cnting on 2022/8/13
 *
 */
class TestPluginActivity : AppCompatActivity() {
    private val pluginName = "plugin.apk"
    private val componentName = "com.example.myplugin.TestPluginActivity"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_plugin)
        findViewById<View>(R.id.loadPlugin).setOnClickListener {
            val result = PluginManager.loadPlugin(this, pluginName)
            Toast.makeText(this, if (result) "加载成功" else "加载失败", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.toPluginActivity).setOnClickListener {
            PluginManager.startActivity(this, pluginName, componentName)
        }
    }
}