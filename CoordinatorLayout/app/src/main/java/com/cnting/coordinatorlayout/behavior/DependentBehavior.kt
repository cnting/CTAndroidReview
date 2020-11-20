package com.cnting.coordinatorlayout.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

/**
 * Created by cnting on 2020/11/20
 * 一个view随着另一个view的滚动而滚动
 */
class DependentBehavior : CoordinatorLayout.Behavior<View> {
    /**
     * 1. 这个构造方法必须重载，CoordinatorLayout利用反射获取这个构造
     */
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    /**
     * 2. 判断监听哪个view
     */
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is TextView
    }

    /**
     * 3. 当被监听的view的状态改变时调用，child可以判断是否要发生变化，如果要发生变化，需要返回true
     */
    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val offset = dependency.top - child.top
        ViewCompat.offsetTopAndBottom(child, offset)
        return true
    }
}
 