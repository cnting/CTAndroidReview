package com.cnting.coordinatorlayout.nestscroll

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.core.math.MathUtils
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat

/**
 * Created by cnting on 2020/11/19
 * 向上滑动时，head先消费
 * 向下滑动时，child先消费
 * https://juejin.im/post/6844903761060577294#heading-8
 */
class TestNestedScrollParent : LinearLayout, NestedScrollingParent3 {
    constructor(context: Context?) : super(context)
    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs)

    constructor (
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    private val parentHelper = NestedScrollingParentHelper(this)

    /**
     * 只支持纵向滑动
     */
    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        parentHelper.onNestedScrollAccepted(child, target, axes, type)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        parentHelper.onStopNestedScroll(target, type)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        onNestedScrollInternal(dyUnconsumed, type, consumed)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        onNestedScrollInternal(dyUnconsumed, type, null)
    }

    private fun onNestedScrollInternal(
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray?
    ) {
        //子view都滑动完了
        if (dyUnconsumed < 0) {
            scrollDown(dyUnconsumed, consumed)
        }
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (dy > 0) {
            scrollUp(dy, consumed)
        }
    }

    private fun scrollDown(dyUnconsumed: Int, consumed: IntArray?) {
        val oldScrollY = scrollY

        scrollBy(0, dyUnconsumed)
        val myConsumed = scrollY - oldScrollY

        if (consumed != null) {
            consumed[1] += myConsumed
        }
    }

    private fun scrollUp(dy: Int, consumed: IntArray) {
        val oldScrollY = scrollY
        scrollBy(0, dy)
        consumed[1] = scrollY - oldScrollY
    }

    override fun scrollTo(x: Int, y: Int) {
        val validY = MathUtils.clamp(y, 0, headerHeight)
        super.scrollTo(x, validY)
    }

    private var headerHeight = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (childCount > 0) {
            val headView = getChildAt(0)
            measureChildWithMargins(
                headView,
                widthMeasureSpec,
                0,
                MeasureSpec.AT_MOST,
                0
            )
            headerHeight = headView.measuredHeight

            super.onMeasure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(
                    MeasureSpec.getSize(heightMeasureSpec) + headerHeight,  // 需要额外加上header的高度，当header隐藏时，RecyclerView下面需要撑满
                    MeasureSpec.EXACTLY
                )
            )
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

}