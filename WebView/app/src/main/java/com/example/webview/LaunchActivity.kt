package com.example.webview

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by cnting on 2022/8/6
 *
 */
class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val button = Button(this)
        button.text = "点击进入MainActivity"
        button.setOnClickListener {
            startActivity(
                Intent(
                    this@LaunchActivity,
                    MainActivity::class.java
                )
            )
        }
        setContentView(button)
    }
}