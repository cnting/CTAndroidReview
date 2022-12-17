package com.cnting.recyclerview.item_visibility

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cnting.recyclerview.R

/**
 * Created by cnting on 2022/12/17
 *
 */
class TestItemVisibilityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stagger)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TestVisibilityAdapter(10)
        recyclerView.onItemVisibilityChange { itemView, adapterIndex, isVisible ->
            Log.d("===>", "position：$adapterIndex，是否可见:$isVisible")
        }
    }
}