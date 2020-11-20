package com.cnting.recyclerview.card_swipe

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.cnting.recyclerview.item_touch_helper.CustomItemTouchListener

/**
 * Created by cnting on 2020/11/10
 *
 */
class CardSwipeTouchCallback(private val callback: CustomItemTouchListener) :
    ItemTouchHelper.Callback() {

    private val maxVisibleCount = 5
    private val defaultScale = 0.8

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(0, swipeFlag)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        callback.onSwipe(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            var ratio = dX / getSwipeThreshold(viewHolder)
            ratio = ratio.coerceAtMost(1f).coerceAtLeast(-1f)
            //跟着角度旋转
            viewHolder.itemView.rotation = ratio * 15
            (0 until maxVisibleCount).forEach {
                val child = recyclerView.getChildAt(it)
                val curScale = Math.pow(defaultScale, (maxVisibleCount - 1 - it).toDouble())
                val nextScale = curScale / defaultScale
                val scale = nextScale - curScale
                child.scaleX = Math.min(1f, (curScale + scale * Math.abs(ratio)).toFloat())
                child.scaleY = Math.min(1f, (curScale + scale * Math.abs(ratio)).toFloat())
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.rotation = 0f
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

}