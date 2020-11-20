package com.cnting.recyclerview.release_time

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.cnting.recyclerview.R
import kotlinx.android.synthetic.main.activity_stagger.*

/**
 * Created by cnting on 2020/11/9
 * 测试itemView释放时机
 */
class ReleaseTimeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stagger)

        val data = (0..50).map { "position $it" }.toMutableList()
        val adapter = ReleaseTimeAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}