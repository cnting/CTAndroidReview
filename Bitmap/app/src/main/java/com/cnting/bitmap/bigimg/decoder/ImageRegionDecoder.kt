package com.cnting.bitmap.bigimg.decoder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import java.io.InputStream

/**
 * Created by cnting on 2022/9/20
 *
 */
interface ImageRegionDecoder {
    /**
     * 计算Bitmap尺寸
     */
    fun init(context: Context, inputStream: InputStream): Point

    /**
     * 加载bitmap片段
     */
    fun decodeRegion(rect: Rect, sampleSize: Int): Bitmap

    /**
     * decoder是否准备好
     */
    fun isReady(): Boolean

    fun recycle()
}