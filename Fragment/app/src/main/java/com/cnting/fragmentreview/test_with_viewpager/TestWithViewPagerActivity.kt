package com.cnting.fragmentreview.test_with_viewpager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.cnting.fragmentreview.R
import com.cnting.fragmentreview.test_with_viewpager.viewpager.MyFragmentPagerAdapter
import com.cnting.fragmentreview.test_with_viewpager.viewpager2.MyViewPager2Adapter
import kotlinx.android.synthetic.main.activity_test_with_viewpager.*

/**
 * Created by cnting on 2020/9/28
 *
 */
class TestWithViewPagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_with_viewpager)
//        viewPager.adapter = MyFragmentStatePagerAdapter(supportFragmentManager)
//        viewPager.adapter =
//            MyFragmentPagerAdapter(
//                supportFragmentManager
//            )
        viewPager2.adapter = MyViewPager2Adapter(this)
//        viewPager2.offscreenPageLimit = 3
    }
}