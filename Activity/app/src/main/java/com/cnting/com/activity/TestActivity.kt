package com.cnting.com.activity

import android.os.Bundle
import android.util.Log
import android.view.View
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
        findViewById<View>(R.id.btn).setOnClickListener {
            Log.d("===>","发送数据啦")
        }
    }
}