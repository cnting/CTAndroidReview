package com.cnting.recyclerview.item_touch_helper

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cnting.recyclerview.R
import com.cnting.recyclerview.stagger.MyViewHolder
import kotlinx.android.synthetic.main.view_item_touch.view.*
import java.util.*

/**
 * Created by cnting on 2020/11/9
 *
 */
class ItemTouchHelperAdapter(val data: MutableList<ItemTouchHelperActivity.ItemData>) :
    RecyclerView.Adapter<MyViewHolder>(),
    CustomItemTouchListener {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item_touch, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setBackgroundColor(data[position].color)
        holder.itemView.tv.text = data[position].text
        holder.itemView.deleteBtn.setOnClickListener {
            data.removeAt(holder.bindingAdapterPosition)
            notifyItemRemoved(holder.bindingAdapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onMove(fromPos: Int, toPos: Int): Boolean {
        Collections.swap(data, fromPos, toPos)
        notifyItemMoved(fromPos, toPos)
        return true
    }

    override fun onSwipe(position: Int) {
//        data.removeAt(position)
//        notifyItemRemoved(position)
    }
}