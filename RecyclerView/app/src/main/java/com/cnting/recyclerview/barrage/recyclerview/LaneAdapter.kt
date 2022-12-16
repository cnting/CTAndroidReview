package com.cnting.recyclerview.barrage.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cnting.recyclerview.R
import com.cnting.recyclerview.barrage.animate.Comment

/**
 * Created by cnting on 2022/12/16
 *
 */
class LaneAdapter(private val dataList: List<Comment>) :
    RecyclerView.Adapter<LaneAdapter.LaneViewHolder>() {
    private fun getIndex(position: Int): Int = position % dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaneViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_barrage_item, parent, false)
        return LaneViewHolder(view)
    }

    override fun onBindViewHolder(holder: LaneViewHolder, position: Int) {
        val realIndex = getIndex(position)
        holder.tv.text = dataList[realIndex].text
    }


    //设置表项无限大，可以无限滚动弹幕
    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    class LaneViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv = view.findViewById<TextView>(R.id.laneTv)
        val imageView = view.findViewById<ImageView>(R.id.laneImageView)
    }


}