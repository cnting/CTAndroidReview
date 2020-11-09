package com.cnting.recyclerview.custom_layoutmanager

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

/**
 * Created by cnting on 2020/11/9
 *
 */
class HorizontallySwipeSnapHelper : SnapHelper() {

    /**
     * 计算最终滑动的距离，返回的是一个长度为2的数组，其中0位置表示水平滑动距离，1位置表示垂直滑动距离
     */
    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray? {
        if (layoutManager is HorizontallySwipeLayoutManager) {
            val arr = IntArray(2)
            if (layoutManager.canScrollHorizontally()) {
                arr[0] = layoutManager.calculateDistanceToPosition(
                    layoutManager.getPosition(targetView)
                )
                arr[1] = 0
            } else {
                arr[0] = 0
                arr[1] = layoutManager.calculateDistanceToPosition(
                    layoutManager.getPosition(targetView)
                )
            }
            return arr
        }
        return null
    }

    /**
     * 最终滑动位置对应的view
     */
    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        if (layoutManager is HorizontallySwipeLayoutManager) {
            val position = layoutManager.getFixedScrollPosition()
            if (position != RecyclerView.NO_POSITION) {
                return layoutManager.findViewByPosition(position)
            }
        }
        return null
    }

    /**
     * 表示fling最终能滑动到itemView的position，这个一般不处理，返回NO_POSITION，交给RecyclerView处理
     */
    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager?,
        velocityX: Int,
        velocityY: Int
    ): Int {
        return RecyclerView.NO_POSITION
    }

}