package com.cnting.view

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun testScrollConflictBtn(view: View) {
//        startActivity(Intent(this, TestScrollConflictActivity::class.java))
        val dialog = Dialog(this)
        dialog.setTitle("sssss")
        dialog.show()
    }
}