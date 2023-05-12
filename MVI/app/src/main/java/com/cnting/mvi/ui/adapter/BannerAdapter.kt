package com.cnting.mvi.ui.adapter

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.cnting.mvi.model.Banner

/**
 * Created by cnting on 2023/4/18
 *
 */
class BannerAdapter(private val list: MutableList<Banner>) :
    RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent.context)
        imageView.layoutParams = ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,100)
        return BannerViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        Glide.with(holder.itemView).load(list[position].url).into(holder.itemView as ImageView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(newList: List<Banner>) {
        list.addAll(newList)
        notifyItemRangeChanged(0, newList.size)
    }

    class BannerViewHolder(itemView: ImageView) : ViewHolder(itemView) {

    }
}