package com.cnting.recyclerview.stagger

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * Created by cnting on 2020/10/13
 *
 */
class MyStaggerAdapter(val list: MutableList<String>) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ImageView(parent.context)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(holder.itemView).load(list[position]).into((holder.itemView as ImageView))
    }

}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}