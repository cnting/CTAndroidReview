package com.cnting.coordinatorlayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cnting.coordinatorlayout.appbarlayout.TestAppbarActivity
import com.cnting.coordinatorlayout.behavior.DependentActivity
import com.cnting.coordinatorlayout.nestscroll.TestNestedScrollActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testNestScroll.setOnClickListener {
            startActivity(Intent(this, TestNestedScrollActivity::class.java))
        }

        testBehavior.setOnClickListener {
            startActivity(Intent(this, DependentActivity::class.java))
        }
        testAppbar.setOnClickListener {
            startActivity(Intent(this, TestAppbarActivity::class.java))
        }

    }
}