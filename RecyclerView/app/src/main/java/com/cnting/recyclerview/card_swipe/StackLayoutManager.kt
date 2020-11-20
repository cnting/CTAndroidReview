package com.cnting.recyclerview.card_swipe

import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by cnting on 2020/11/10
 *
 */
class StackLayoutManager(
    private val recyclerView: RecyclerView,
    private val itemTouchHelper: ItemTouchHelper
) : RecyclerView.LayoutManager() {

    private val maxVisibleCount = 5
    private val defaultScale = 0.8

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State?) {
        val layoutCount = Math.min(itemCount, maxVisibleCount)
        //回收所有ViewHolder
        detachAndScrapAttachedViews(recycler)

        (layoutCount - 1 downTo 0).forEach {
            //从缓存取
            val view = recycler.getViewForPosition(it)
            addView(view)
            //测量
            measureChildWithMargins(view, 0, 0)
            val widthSpace = width - getDecoratedMeasuredWidth(view)
            val heightSpace = height - getDecoratedMeasuredHeight(view)
            //布局
            layoutDecoratedWithMargins(
                view,
                widthSpace / 2,
                heightSpace / 2,
                widthSpace / 2 + getDecoratedMeasuredWidth(view),
                heightSpace / 2 + getDecoratedMeasuredHeight(view)
            )
            view.scaleX = Math.pow(defaultScale, it.toDouble()).toFloat()
            view.scaleY = Math.pow(defaultScale, it.toDouble()).toFloat()

            if (it == 0) {
                view.setOnTouchListener { v, event ->
                    val viewHolder = recyclerView.getChildViewHolder(view)
                    if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                        itemTouchHelper.startSwipe(viewHolder)
                    }
                    false
                }
            } else {
                view.setOnTouchListener(null)
            }
        }
    }

}