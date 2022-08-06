package com.example.webview

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.MutableContextWrapper
import android.os.Looper
import android.view.ViewGroup
import android.webkit.WebView
import java.util.concurrent.LinkedBlockingQueue

/**
 * Created by cnting on 2022/8/6
 * WebView复用
 */
object WebViewPool {
    private val pool = LinkedBlockingQueue<WebView>()

    /**
     * 预加载
     */
    fun prepareWebView(application: Application) {
        if (pool.isEmpty()) {
            //IdleHandler等主线程空闲时执行
            Looper.myQueue().addIdleHandler {
                if (pool.isEmpty()) {
                    pool.add(createWebView(MutableContextWrapper(application)))
                }
                false
            }
        }
    }

    /**
     * 回收
     * 1. 去除WebView的Context，避免内存泄漏
     * 2. 加入缓存
     */
    fun recycle(webView: WebView) {
        //替换成applicationContext
        if (webView.context is MutableContextWrapper) {
            val context = webView.context as MutableContextWrapper
            context.baseContext = context.applicationContext

            webView.stopLoading()
            webView.clearHistory()
            (webView.parent as? ViewGroup)?.removeView(webView)

            pool.offer(webView)
        }
        if (webView.context is Activity) {
            throw RuntimeException("webview leaked")
        }
    }

    /**
     * 获取WebView
     */
    fun acquireWebView(activity: Activity): WebView {
        val webView = pool.poll()
        return if (webView == null) {
            createWebView(MutableContextWrapper(activity))
        } else {
            //替换成Activity
            val mutableContextWrapper = webView.context as MutableContextWrapper
            mutableContextWrapper.baseContext = activity
            webView
        }
    }

    fun destory() {
        pool.forEach {
            it.removeAllViews()
            it.destroy()
        }
        pool.clear()
    }

    private fun createWebView(context: Context): WebView {
        return WebView(context)
    }
}