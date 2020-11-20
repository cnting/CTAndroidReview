package com.cnting.coordinatorlayout.nestscroll

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by cnting on 2020/11/19
 *
 */
class TestNestedScrollAdapter : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val textView = TextView(parent.context)
        textView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)
        textView.gravity = Gravity.CENTER
        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        (holder.itemView as TextView).text = position.toString()
    }

    override fun getItemCount(): Int {
        return 100
    }

}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}