package com.example.glide.progress

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import java.lang.Integer.min

/**
 * Created by cnting on 2022/9/9
 *
 */
class ProgressPlaceholderDrawable(
    private val context: Context,
    private var placeholderDrawable: Drawable?,
    placeholderId: Int = 0
) : Drawable() {

    private var progress = 0
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val startAngle = 270f
    private val paintStrokeWidth = getDensity() * 1.5f
    private val progressPadding = getDensity() * 3f
    private val maxRadius = getDensity() * 30f

    init {
        if (placeholderDrawable == null && placeholderId != 0) {
            placeholderDrawable = ContextCompat.getDrawable(context, placeholderId)
        }
        paint.color = Color.GRAY
        paint.strokeWidth = paintStrokeWidth
    }

    override fun setBounds(bounds: Rect) {
        super.setBounds(bounds)
        placeholderDrawable?.bounds = bounds
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        placeholderDrawable?.setBounds(left, top, right, bottom)
    }

    override fun setTint(tintColor: Int) {
        super.setTint(tintColor)
        paint.color = tintColor
    }

    fun setProgress(@IntRange(from = 0, to = 100) progress: Int) {
        this.progress = progress
        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        //画背景
        placeholderDrawable?.draw(canvas)

        val centerX = bounds.width() / 2f
        val centerY = bounds.height() / 2f
        val radius = (min(bounds.width(), bounds.height()) / 2f).coerceAtMost(maxRadius)
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(centerX, centerY, radius, paint)

        paint.style = Paint.Style.FILL
        val endAngle = (progress / 100) * 360F
        val rect = RectF(
            centerX - radius + progressPadding,
            centerY - radius + progressPadding,
            centerX + radius - progressPadding,
            centerY + radius - progressPadding,
        )
        canvas.drawArc(rect, startAngle, endAngle, true, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    private fun getDensity() = context.resources.displayMetrics.density
}