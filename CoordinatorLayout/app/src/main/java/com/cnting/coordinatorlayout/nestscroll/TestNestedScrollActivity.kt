package com.cnting.coordinatorlayout.nestscroll

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cnting.coordinatorlayout.R
import kotlinx.android.synthetic.main.activity_nested_scroll.*

/**
 * Created by cnting on 2020/11/19
 *
 */
class TestNestedScrollActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_scroll)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TestNestedScrollAdapter()
        recyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.HORIZONTAL))
    }
}