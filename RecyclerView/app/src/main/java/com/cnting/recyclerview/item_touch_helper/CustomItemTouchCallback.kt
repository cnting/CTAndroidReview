package com.cnting.recyclerview.item_touch_helper

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by cnting on 2020/11/10
 *
 */
class CustomItemTouchCallback(private val itemTouchListener: CustomItemTouchListener) :
    ItemTouchHelper.Callback() {

    private var curScrollX = 0
    private var firstInactive = false
    private val defaultScrollX = 200  //大于这个值，显示删除按钮

    /**
     * 设置支持拖动还是滑动的flag
     */
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        //上下拖动
        val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        //左右滑动
        val swipeFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(dragFlag, swipeFlag)
    }

    /**
     * 拖动效果产生时，会回调该方法，我们需要更新数据源
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return itemTouchListener.onMove(viewHolder.adapterPosition, target.adapterPosition)
    }

    /**
     * 滑动效果产生时的回调
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//        if (direction == ItemTouchHelper.END) {
//            itemTouchListener.onSwipe(viewHolder.adapterPosition)
//        }
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return Float.MAX_VALUE
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return Float.MAX_VALUE
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
        //首次滑动，记录下ItemView当前滑动距离
        if (dX == 0f) {
            curScrollX = viewHolder.itemView.scrollX
            firstInactive = true
        }
        //手指滑动
        if (isCurrentlyActive) {
            //基于当前的距离滑动
            viewHolder.itemView.scrollTo((curScrollX - dX).toInt(), 0)
        } else {
            //动画滑动
            if (firstInactive) {
                firstInactive = false
            }
            //滑动距离超过删除按钮，显示删除按钮
            if (viewHolder.itemView.scrollX >= defaultScrollX) {
                viewHolder.itemView.scrollTo(Math.max((curScrollX - dX).toInt(), defaultScrollX), 0)
            } else {
                //需要回到原位
                viewHolder.itemView.scrollTo(0, 0)
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (viewHolder.itemView.scrollX > defaultScrollX) {
            viewHolder.itemView.scrollTo(defaultScrollX, 0)
        } else if (viewHolder.itemView.scrollX < 0) {
            viewHolder.itemView.scrollTo(0, 0)
        }
        itemTouchListener.onSwipe(viewHolder.adapterPosition)
    }
}

interface CustomItemTouchListener {
    fun onMove(oldPosition: Int, newPosition: Int): Boolean

    fun onSwipe(position: Int)
}