package com.cnting.bitmap.bigimg.bean

import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import java.io.InputStream

data class ImageResource(val inputStream: InputStream)

/**
 * 切片
 */
data class Tile(
    //切片原始大小
    val sRect: Rect,
    //保存切片绘制大小（经过缩放、平移）
    var vRect: RectF,
    val sampleSize: Int,
    var bitmap: Bitmap? = null,
    var loading: Boolean = false,
    var visible: Boolean = false,
)

data class ScaleAndTranslate(var scale: Float, var vTranslate: PointF)
