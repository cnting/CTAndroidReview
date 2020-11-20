package com.cnting.recyclerview.release_time

import android.graphics.Color
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
class ReleaseTimeAdapter(val data: MutableList<String>) : RecyclerView.Adapter<MyViewHolder>() {
    private val colorArr = arrayOf(Color.BLUE, Color.CYAN, Color.MAGENTA, Color.RED, Color.GREEN)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item_touch, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.scrollTo(0, 0)
        holder.itemView.tv.setBackgroundColor(colorArr[position % colorArr.size])
        holder.itemView.tv.text = data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onViewAttachedToWindow(holder: MyViewHolder) {
        super.onViewAttachedToWindow(holder)
        Log.d("===>","onAttach ${holder.bindingAdapterPosition}")
    }

    override fun onViewDetachedFromWindow(holder: MyViewHolder) {
        super.onViewDetachedFromWindow(holder)
        Log.d("===>","onDetach ${holder.bindingAdapterPosition}")
    }

}