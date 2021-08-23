package com.cnting.com.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val viewPager = findViewById<ViewPager>(R.id.viewPager)
//        viewPager.adapter = object : PagerAdapter() {
//            override fun getCount(): Int {
//                return 10
//            }
//
//            override fun isViewFromObject(view: View, `object`: Any): Boolean {
//                return view == `object`
//            }
//
//            override fun instantiateItem(container: ViewGroup, position: Int): Any {
//                val view = TextView(this@MainActivity)
//                view.layoutParams = ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT
//                )
//                view.text = position.toString()
//                view.textSize = 40f
//                view.gravity = Gravity.CENTER
//                view.setBackgroundColor(if (position % 2 == 0) Color.RED else Color.GREEN)
//                container.addView(view)
//                return view
//            }
//
//            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//                container.removeView(`object` as View)
//            }
//
//        }
    }
}