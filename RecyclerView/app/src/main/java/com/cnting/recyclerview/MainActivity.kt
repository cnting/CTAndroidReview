package com.cnting.recyclerview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cnting.recyclerview.custom_layoutmanager.CustomLayoutManagerActivity
import com.cnting.recyclerview.stagger.StaggerActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        staggerBtn.setOnClickListener { startActivity(Intent(this, StaggerActivity::class.java)) }
        layoutManagerBtn.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    CustomLayoutManagerActivity::class.java
                )
            )
        }
    }
}