package com.cnting.coordinatorlayout.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView

/**
 * Created by cnting on 2020/11/20
 *
 */
class ScrollBehavior : CoordinatorLayout.Behavior<View> {
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        child.scrollY = target.scrollY
    }

    /**
     * 这个方法如果返回true，表示响应的child自己处理了这次fling的意图，那么NestedScrollView反而操作不了这个动作
     */
    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        (child as NestedScrollView).fling(velocityY.toInt())
        return false
    }

}