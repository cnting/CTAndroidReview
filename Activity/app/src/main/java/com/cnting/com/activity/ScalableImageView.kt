package com.cnting.com.activity

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller

/**
 * Created by cnting on 2021/6/10
 *
 */
class ScalableImageView : View {
    constructor(context: Context?) : super(context)
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

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    lateinit var bitmap: Bitmap
    private lateinit var gestureDetector: GestureDetector
    private val size = 400
    private var originalOffsetX = 0f
    private var originalOffsetY = 0f
    private var offsetX = 0f
    private var offsetY = 0f
    private var smallScale = 0f
    private var bigScale = 0f
    private var isBig = false
    private var scaleFraction = 0f
        set(value) {
            field = value
            invalidate()
        }
    private lateinit var overScroller: OverScroller

    private fun init() {
        bitmap = getBitmap(R.drawable.a, size)
        gestureDetector = GestureDetector(context, gestureDetectorListener)
        overScroller = OverScroller(context)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        originalOffsetX = (w - bitmap.width) / 2f
        originalOffsetY = (h - bitmap.height) / 2f

        if (bitmap.width / bitmap.height > w / h) {
            smallScale = w / bitmap.width.toFloat()
            bigScale = h / bitmap.height.toFloat() * 1.5f
        } else {
            smallScale = h / bitmap.height.toFloat()
            bigScale = w / bitmap.width.toFloat() * 1.5f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(offsetX, offsetY)
        val scale = smallScale + (bigScale - smallScale) * scaleFraction
        canvas.scale(scale, scale, width / 2f, height / 2f)
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint)
    }

    private val gestureDetectorListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            isBig = !isBig
            if (isBig) {
                scaleAnimator.start()
            } else {
                scaleAnimator.reverse()
            }
            return super.onDoubleTap(e)
        }

        override fun onScroll(
            down: MotionEvent?,
            move: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (isBig) {
                offsetX -= distanceX
                offsetY -= distanceY
                offsetX = offsetX.coerceAtMost((bitmap.width * bigScale - width) / 2)
                    .coerceAtLeast(-(bitmap.width * bigScale - width) / 2)
                offsetY = offsetY.coerceAtMost((bitmap.height * bigScale - height) / 2)
                    .coerceAtLeast(-(bitmap.height * bigScale - height) / 2)
                invalidate()
            }
            return super.onScroll(down, move, distanceX, distanceY)
        }

        override fun onFling(
            down: MotionEvent?,
            move: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (isBig) {
                overScroller.fling(
                    offsetX.toInt(),
                    offsetY.toInt(),
                    velocityX.toInt(),
                    velocityY.toInt(),
                    (-(bitmap.width * bigScale - width) / 2).toInt(),
                    ((bitmap.width * bigScale - width) / 2).toInt(),
                    (-(bitmap.height * bigScale - height) / 2).toInt(),
                    ((bitmap.height * bigScale - height) / 2).toInt(),
                    100,
                    100
                )
                invalidate()
            }
            return super.onFling(down, move, velocityX, velocityY)
        }
    }

    override fun computeScroll() {
        super.computeScroll()
        refresh()
    }


    private fun refresh() {
        if (overScroller.computeScrollOffset()) {
            offsetX = overScroller.currX.toFloat()
            offsetY = overScroller.currY.toFloat()
            invalidate()
        }
    }

    private val scaleAnimator = ObjectAnimator.ofFloat(this, "scaleFraction", 0f, 1f)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private fun getBitmap(resId: Int, width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, resId, options)
    }
}