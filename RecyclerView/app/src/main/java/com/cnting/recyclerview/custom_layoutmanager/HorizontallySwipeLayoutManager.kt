package com.cnting.recyclerview.custom_layoutmanager

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by cnting on 2020/11/6
 * 水平切换的LayoutManager
 */
class HorizontallySwipeLayoutManager(val itemHeightWidthRatio: Float, val scale: Float) :
    RecyclerView.LayoutManager() {

    private var hasMeasureChild = false
    private var itemViewHeight = 0
    private var itemViewWidth = 0
    private var scrollOffset = Int.MAX_VALUE

    /**
     * 1. 用于给ItemView生成LayoutParams
     */
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    /**
     * 2. 布局ItemView
     * (1)定位每个ItemView的位置，然后布局
     * (2)适配滑动和缩放效果
     */
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (state.itemCount == 0 || state.isPreLayout) return
        removeAndRecycleAllViews(recycler)  // TODO: 下面fill()方法有【先回收再布局】的操作，这里为什么要再来一遍  
        //计算初始宽高
        if (!hasMeasureChild) {
            itemViewHeight = getVerticalSpace()
            itemViewWidth = (itemViewHeight / itemHeightWidthRatio).toInt()
            hasMeasureChild = true
        }
        scrollOffset = makeScrollOffsetWithinRange(scrollOffset)
        fill(recycler)
    }

    private fun getVerticalSpace(): Int {
        return height - paddingTop - paddingBottom
    }

    private fun getHorizontalSpace(): Int {
        return width - paddingLeft - paddingRight
    }

    private fun makeScrollOffsetWithinRange(scrollOffset: Int): Int {
        return Math.min(Math.max(itemViewWidth, scrollOffset), itemCount * itemViewWidth)
    }

    private fun fill(recycler: RecyclerView.Recycler) {
        //1. 初始化基本变量
        var bottomVisiblePosition =
            scrollOffset / itemViewWidth  //表示RecyclerView最右边能看见的ItemView的Position
        val bottomItemVisibleSize = scrollOffset % itemViewWidth
        val offsetPercent = bottomItemVisibleSize.toFloat() / itemViewWidth  //滑动的百分比
        val space = getHorizontalSpace()
        var remainSpace = space.toDouble()
        val defaultOffset = itemViewWidth / 2  //每个itemView偏移的值
        val itemViewInfos = mutableListOf<ItemViewInfo>()

        //2. 计算每个ItemView的位置信息
        var j = 1
        for (i in bottomVisiblePosition - 1 downTo 0) {
            val maxOffset = defaultOffset * Math.pow(scale.toDouble(), (j - 1).toDouble())
            val start = (remainSpace - offsetPercent * maxOffset - itemViewWidth).toInt()
            val info =
                ItemViewInfo(
                    start,
                    (Math.pow(
                        scale.toDouble(),
                        j - 1.toDouble()
                    ) * (1 - offsetPercent * (1 - scale))).toFloat()
                )
            Log.d("===>", "info:$info")
            itemViewInfos.add(0, info)
            remainSpace -= maxOffset
            if (remainSpace < 0) {
                info.left = (remainSpace + maxOffset - itemViewWidth).toInt()
                info.scale = Math.pow(scale.toDouble(), j - 1.toDouble()).toFloat()
                break
            }
            j++
        }
        //3.添加最右边itemView的相关信息
        if (bottomVisiblePosition < itemCount) {
            val left = space - bottomItemVisibleSize
            itemViewInfos.add(ItemViewInfo(left, 1f))
        } else {
            bottomVisiblePosition -= 1
        }
        //4. 回收其他位置的view
        val layoutCount = itemViewInfos.size
        val startPosition = bottomVisiblePosition - (layoutCount - 1)
        val endPosition = bottomVisiblePosition
        Log.d(
            "===>",
            "startPosition:$startPosition,endPosition:$endPosition,layoutCount:$layoutCount,itemCount:$itemCount"
        )
        (childCount - 1..0).forEach {
            val childView = getChildAt(it)
            if (childView != null) {
                val posiiton = convert2LayoutPosition(it)
                if (posiiton > endPosition || posiiton < startPosition) {
                    detachAndScrapView(childView, recycler)
                }
            }
        }
        //5.先回收再布局
        detachAndScrapAttachedViews(recycler)
        (0 until layoutCount).forEach {
            fillChild(
                recycler.getViewForPosition(convert2AdapterPosition(startPosition + it)),
                itemViewInfos.get(it)
            )
        }
    }

    private fun fillChild(view: View, itemViewInfo: ItemViewInfo) {
        addView(view)
        measureChildWithExactlySize(view)
        layoutDecoratedWithMargins(
            view,
            itemViewInfo.left,
            paddingTop,
            itemViewInfo.left + itemViewWidth,
            paddingTop + itemViewHeight
        )
        view.scaleX = itemViewInfo.scale
        view.scaleY = itemViewInfo.scale
    }

    private fun measureChildWithExactlySize(view: View) {
        val params = view.layoutParams as RecyclerView.LayoutParams
        val widthSpec = View.MeasureSpec.makeMeasureSpec(
            itemViewWidth - params.leftMargin - params.rightMargin,
            View.MeasureSpec.EXACTLY
        )
        val heightSpec = View.MeasureSpec.makeMeasureSpec(
            itemViewHeight - params.topMargin - params.bottomMargin,
            View.MeasureSpec.EXACTLY
        )
        view.measure(widthSpec, heightSpec)
    }

    private fun convert2LayoutPosition(position: Int): Int {
        return itemCount - 1 - position
    }

    private fun convert2AdapterPosition(position: Int): Int {
        Log.d("===>", "itemCount:$itemCount,position:$position")
        return itemCount - 1 - position
    }
}

private data class ItemViewInfo(var left: Int, var scale: Float)