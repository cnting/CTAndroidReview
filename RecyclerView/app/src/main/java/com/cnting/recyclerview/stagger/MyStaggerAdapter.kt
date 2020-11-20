package com.cnting.recyclerview.stagger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.cnting.recyclerview.R
import com.cnting.recyclerview.util.GlideApp
import com.cnting.recyclerview.util.ImageBean
import kotlinx.android.synthetic.main.view_stagger_item.view.*

/**
 * Created by cnting on 2020/10/13
 *
 */
class MyStaggerAdapter(val list: MutableList<ImageBean>) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_stagger_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val imageBean = list[position]
        val layoutParams = holder.itemView.imageView.layoutParams
        val width = layoutParams.width
        val height = imageBean.height * layoutParams.width / imageBean.width
        holder.itemView.imageView.layoutParams.height = height
        GlideApp.with(holder.itemView.imageView)
            .load(object : GlideUrl(
                list[position].url,
                LazyHeaders.Builder().addHeader(
                    "User-Agent",
                    WebSettings.getDefaultUserAgent(holder.itemView.context)
                ).build()
            ) {})
            .load(list[position].url)
            .override(width, height)
            .into((holder.itemView.imageView) as ImageView)

    }

}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}