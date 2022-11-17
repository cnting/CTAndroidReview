package com.cnting.com.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.greenrobot.eventbus.EventBus

/**
 * Created by cnting on 2021/5/11
 *
 */
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        findViewById<TextView>(R.id.tv).text = "这是第二个页面"
        findViewById<View>(R.id.btn).setOnClickListener {
            startActivity(Intent(this, TestActivity1::class.java))
        }
    }
}