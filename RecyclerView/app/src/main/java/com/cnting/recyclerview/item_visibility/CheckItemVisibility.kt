package com.cnting.recyclerview.item_visibility

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import androidx.recyclerview.widget.RecyclerView.OnScrollListener

/**
 * Created by cnting on 2022/12/17
 * https://juejin.cn/post/7165428399282847757
 */
fun RecyclerView.onItemVisibilityChange(
    percent: Float = 0.5f,
    viewGroups: List<ViewGroup> = emptyList(),
    block: (itemView: View, adapterIndex: Int, isVisible: Boolean) -> Unit
) {
    //可复用的矩形区域
    val childVisibleRect = Rect()
    //记录所有可见表项搜索的列表
    val visibleAdapterIndexes = mutableSetOf<Int>()
    val checkVisibility = {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val adapterIndex = getChildAdapterPosition(child)
            if (adapterIndex == NO_POSITION) continue
            //计算子控件可见区域并获取是否可见标记位
            val isChildVisible = child.getLocalVisibleRect(childVisibleRect)
            //子控件可见面积
            val visibleArea = childVisibleRect.let { it.height() * it.width() }
            //子控件真实面积
            val realArea = child.width * child.height
            // 比对可见面积和真实面积，若大于阈值，则回调可见，否则不可见
            if (isChildVisible && visibleArea >= realArea * percent) {
                if (visibleAdapterIndexes.add(adapterIndex)) {
                    block(child, adapterIndex, true)
                }
            } else {
                if (adapterIndex in visibleAdapterIndexes) {
                    block(child, adapterIndex, false)
                    visibleAdapterIndexes.remove(adapterIndex)
                }
            }
        }
    }
    //为列表添加滚动监听
    val scrollListener = object : OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            checkVisibility()
        }
    }

    addOnScrollListener(scrollListener)
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View?) {
        }

        override fun onViewDetachedFromWindow(v: View?) {
            if (v == null || v !is RecyclerView) return
            v.removeOnScrollListener(scrollListener)
            removeOnAttachStateChangeListener(this)
        }
    })
}