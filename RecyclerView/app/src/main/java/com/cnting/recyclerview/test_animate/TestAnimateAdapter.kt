package com.cnting.recyclerview.test_animate

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cnting.recyclerview.R
import com.cnting.recyclerview.item_touch_helper.CustomItemTouchListener
import com.cnting.recyclerview.stagger.MyViewHolder
import kotlinx.android.synthetic.main.view_custom_layout_manager.view.*

/**
 * Created by cnting on 2022/12/15
 *
 */
class TestAnimateAdapter(val data: MutableList<String>) : RecyclerView.Adapter<MyViewHolder>(),
    CustomItemTouchListener {

    private val colorArr = arrayOf(Color.BLUE, Color.CYAN, Color.MAGENTA, Color.RED, Color.GREEN)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_custom_layout_manager, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setBackgroundColor(colorArr[position % colorArr.size])
        holder.itemView.tv.text = data[position]
        holder.itemView.setOnClickListener { notifyItemRemoved(position) }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onMove(oldPosition: Int, newPosition: Int): Boolean {
        return true
    }

    override fun onSwipe(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

}