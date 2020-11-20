package com.cnting.coordinatorlayout.appbarlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cnting.coordinatorlayout.R
import com.cnting.coordinatorlayout.nestscroll.TestNestedScrollAdapter
import kotlinx.android.synthetic.main.activity_appbar.*

/**
 * Created by cnting on 2020/11/20
 *
 */
class TestAppbarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appbar)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TestNestedScrollAdapter()
        recyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
    }
}