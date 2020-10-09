package com.cnting.fragmentreview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cnting.fragmentreview.test_backstack.TestBackStackActivity
import com.cnting.fragmentreview.test_fragment_manager.TestFragmentManagerActivity
import com.cnting.fragmentreview.test_lifecycle.TestLifecycleActivity
import com.cnting.fragmentreview.test_send_msg.TestSendMsgActivity
import com.cnting.fragmentreview.test_with_viewpager.TestWithViewPagerActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testLifecycleBtn.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    TestLifecycleActivity::class.java
                )
            )
        }
        testFragmentManager.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    TestFragmentManagerActivity::class.java
                )
            )
        }
        testViewPager.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    TestWithViewPagerActivity::class.java
                )
            )
        }
        testBackStack.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    TestBackStackActivity::class.java
                )
            )
        }
        testSendMsg.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    TestSendMsgActivity::class.java
                )
            )
        }
    }
}