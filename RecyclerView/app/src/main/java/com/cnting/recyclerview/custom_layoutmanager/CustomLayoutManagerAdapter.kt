package com.cnting.recyclerview.custom_layoutmanager

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cnting.recyclerview.R
import com.cnting.recyclerview.stagger.MyViewHolder
import kotlinx.android.synthetic.main.view_custom_layout_manager.view.*

/**
 * Created by cnting on 2020/11/6
 *
 */
class CustomLayoutManagerAdapter : RecyclerView.Adapter<MyViewHolder>() {

    private val colorArr = arrayOf(Color.BLUE, Color.CYAN, Color.MAGENTA, Color.RED, Color.GREEN)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_custom_layout_manager, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setBackgroundColor(colorArr[position % colorArr.size])
        holder.itemView.tv.text = position.toString()
    }

    override fun getItemCount(): Int {
        return 6
    }

}
