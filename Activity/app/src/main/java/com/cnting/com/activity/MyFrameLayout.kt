package com.cnting.com.activity

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import java.util.jar.Attributes

/**
 * Created by cnting on 2021/4/26
 *
 */
class MyFrameLayout(context: Context, attributes: AttributeSet) : FrameLayout(context, attributes) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d("===>", "MyFrameLayout.onMeasure()")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d("===>", "MyFrameLayout.onLayout()")
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        Log.d("===>", "MyFrameLayout.dispatchDraw()")
    }
}

class MyView(context: Context, attributes: AttributeSet) : View(context, attributes) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d("===>", "MyView.onMeasure()")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d("===>", "MyView.onLayout()")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.d("===>", "MyView.dispatchDraw()")
    }
}