package com.cnting.com.activity

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn).setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
//            val intent = Intent()
//            intent.component = ComponentName("com.example.taskaffinityreparent","com.example.taskaffinityreparent.ReparentActivity")
//            startActivity(intent)
        }
    }
}