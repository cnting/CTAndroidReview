package com.cnting.bitmap

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import java.io.InputStream

/**
 * Created by cnting on 2021/8/24
 *
 */
class BigImageView : View {

    lateinit var decoder: BitmapRegionDecoder
    private var imgWidth = 0
    private var imgHeight = 0
    private val rect = Rect()
    private val options =
        BitmapFactory.Options().apply { inPreferredConfig = Bitmap.Config.RGB_565 }
    private lateinit var gestureDetector: GestureDetector
    private lateinit var overScroller: OverScroller

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs) {
        init()
    }

    constructor (
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        overScroller = OverScroller(context)
        gestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onScroll(
                    e1: MotionEvent?,
                    e2: MotionEvent?,
                    distanceX: Float,
                    distanceY: Float
                ): Boolean {
                    update(distanceX.toInt(), distanceY.toInt())
//                    update(rect.left + distanceX.toInt(), rect.top + distanceY.toInt())
                    return super.onScroll(e1, e2, distanceX, distanceY)
                }

                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent?,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    Log.d("===>", "onFling(),")
                    flingStartX = scrollX
                    flingStartY = scrollY
                    overScroller.fling(
                        rect.left,
                        rect.top,
                        velocityX.toInt(),
                        velocityY.toInt(),
                        0,
                        imgWidth - width,
                        0,
                        imgHeight - height
                    )
                    invalidate()
                    return true
                }

                override fun onDown(e: MotionEvent?): Boolean {
                    overScroller.forceFinished(true)
                    return true
                }
            })
    }

    private var flingStartX = 0
    private var flingStartY = 0

    override fun computeScroll() {
        super.computeScroll()
        if (overScroller.computeScrollOffset()) {
            //内容往上时，currY为负值；内容往下时，currY为正值
            Log.d("===>", "currX:${overScroller.currX},currY:${overScroller.currY}")
//            update(scrollX-overScroller.currX , scrollY-overScroller.currY )


            if (imgWidth > width) {
                rect.offsetTo(-overScroller.currX, 0)
                checkWidth()
            }
            if (imgHeight > height) {
                rect.offsetTo(0, -overScroller.currY)
                checkHeight()
            }
            invalidate()

        }
    }

    //    private fun update(curX: Int, curY: Int) {
//        if (imgWidth > width) {
//            rect.offsetTo(curX, 0)
//            checkWidth()
//        }
//        if (imgHeight > height) {
//            rect.offsetTo(0, curY)
//            checkHeight()
//        }
//        invalidate()
//    }
    private fun update(curX: Int, curY: Int) {
        if (imgWidth > width) {
            rect.offset(curX, 0)
            checkWidth()
        }
        if (imgHeight > height) {
            rect.offset(0, curY)
            checkHeight()
        }
        invalidate()
    }

    fun setFilePath(inputStream: InputStream) {
        decoder = BitmapRegionDecoder.newInstance(inputStream, false)
        imgWidth = decoder.width
        imgHeight = decoder.height
        requestLayout()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        rect.left = 0
        rect.top = 0
        rect.right = rect.left + measuredWidth
        rect.bottom = rect.top + measuredHeight
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val bitmap = decoder.decodeRegion(rect, options)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private fun checkWidth() {
        if (rect.right > imgWidth) {
            rect.right = imgWidth
            rect.left = imgWidth - width
        }
        if (rect.left < 0) {
            rect.left = 0
            rect.right = width
        }
    }

    /**
     * 确保图不划出屏幕
     */
    private fun checkHeight() {
        if (rect.bottom > imgHeight) {
            rect.bottom = imgHeight
            rect.top = imgHeight - height
        }
        if (rect.top < 0) {
            rect.top = 0
            rect.bottom = height
        }
    }
}