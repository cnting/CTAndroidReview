package com.cnting.recyclerview.card_swipe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import com.cnting.recyclerview.R
import kotlinx.android.synthetic.main.activity_stagger.*

/**
 * Created by cnting on 2020/11/10
 *
 */
class CardSwipeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stagger)
        val data = (0..20).map { "position:$it" }.toMutableList()
        val adapter = CardSwipeAdapter(data)
        val itemTouchHelper = ItemTouchHelper(CardSwipeTouchCallback(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = StackLayoutManager(recyclerView, itemTouchHelper)
        recyclerView.adapter = adapter
    }
}