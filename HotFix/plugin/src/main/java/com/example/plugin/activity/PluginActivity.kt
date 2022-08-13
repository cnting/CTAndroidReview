package com.example.plugin.activity

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View

open class PluginActivity : BasePluginActivity() {
    fun setHostActivityDelegator(hostActivityDelegator: HostActivityDelegator) {
        super.hostActivityDelegator = hostActivityDelegator
    }

    override fun setContentView(layoutResID: Int) {
        val inflate: View = LayoutInflater.from(this).inflate(layoutResID, null)
        hostActivityDelegator.setContentView(inflate)
    }

    fun setHostContextAsBase(context: Context) {
        attachBaseContext(context)
    }
}