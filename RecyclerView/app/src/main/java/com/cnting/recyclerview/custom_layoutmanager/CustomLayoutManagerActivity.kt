package com.cnting.recyclerview.custom_layoutmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cnting.recyclerview.R
import kotlinx.android.synthetic.main.activity_stagger.*

/**
 * Created by cnting on 2020/11/6
 *
 */
class CustomLayoutManagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stagger)
        val adapter = CustomLayoutManagerAdapter()
        val layoutManager = HorizontallySwipeLayoutManager(2f, 0.8f)
//        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
}