package com.cnting.bitmap

import android.graphics.Bitmap
import android.util.Log

/**
 * Created by cnting on 2021/8/23
 *
 */
object BitmapUtil {
    fun getBitmapSize(width: Int, height: Int, config: Bitmap.Config): Int {
        return width * height * getByteFromConfig(config)
    }

    private fun getByteFromConfig(config: Bitmap.Config): Int {
        return when (config) {
            Bitmap.Config.ALPHA_8 -> 1
            Bitmap.Config.RGB_565 -> 2
            Bitmap.Config.ARGB_4444 -> 2
            Bitmap.Config.ARGB_8888 -> 4
            else -> 0
        }
    }

    fun computeSampleSize(
        originWidth: Int,
        originHeight: Int,
        targetWidth: Int,
        targetHeight: Int
    ): Int {
        var sampleSize = 1
        var finalWidth = originWidth
        var finalHeight = originHeight
        while (finalWidth > targetWidth || finalHeight > targetHeight) {
            sampleSize *= 2
            finalWidth /= 2
            finalHeight /= 2
            Log.i(
                "===>",
                "finalWidth:$finalWidth,finalHeight:$finalHeight,targetWidth:$targetWidth,targetHeight:$targetHeight"
            )
        }
        return sampleSize
    }
}