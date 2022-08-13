package com.example.hotfix.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.hotfix.App
import com.example.hotfix.ISayHello
import com.example.hotfix.R
import com.example.patch.PatchUtil

class MainActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.testPatchBtn).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    TestPatchActivity::class.java
                )
            )
        }
        findViewById<View>(R.id.testPluginBtn).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    TestPluginActivity::class.java
                )
            )
        }
    }
}