package com.cnting.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cnting.view.scroll_conflict.TestScrollConflictActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun testScrollConflictBtn(view: View) {
        startActivity(Intent(this, TestScrollConflictActivity::class.java))
    }
}