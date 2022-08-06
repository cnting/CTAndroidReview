package com.example.webview

import android.app.Application

/**
 * Created by cnting on 2022/8/6
 *
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        WebViewPool.prepareWebView(this)
    }
}