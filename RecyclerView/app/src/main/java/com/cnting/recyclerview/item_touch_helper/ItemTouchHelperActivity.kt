package com.cnting.recyclerview.item_touch_helper

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cnting.recyclerview.R
import kotlinx.android.synthetic.main.activity_stagger.*

/**
 * Created by cnting on 2020/11/9
 *
 */
class ItemTouchHelperActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stagger)

        val data = (0..50).map { "position $it" }.toMutableList()
        val adapter = ItemTouchHelperAdapter(data)
        adapter.setHasStableIds(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(CustomItemTouchCallback(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.setRecyclerListener { holder ->
            Log.d(
                "===>",
                "onViewRecycled:${holder.itemId}"
            )
        }
    }
}