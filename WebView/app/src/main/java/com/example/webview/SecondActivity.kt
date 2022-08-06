package com.example.webview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible

class SecondActivity : AppCompatActivity() {

    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val container = findViewById<LinearLayout>(R.id.container)
        val tv = findViewById<TextView>(R.id.tv)
        val btn = findViewById<Button>(R.id.btn)
        val start = System.currentTimeMillis()
        webView = WebViewPool.acquireWebView(this)
        container.addView(
            webView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        webView.loadUrl("https://zhuanlan.zhihu.com/p/454173448")
        tv.text = "第二次初始化web时间：${System.currentTimeMillis() - start}"
        btn.isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        WebViewPool.recycle(webView)
    }
}