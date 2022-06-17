package com.cnting.bitmap

import android.graphics.Bitmap
import android.os.Binder

/**
 * Created by cnting on 2021/8/30
 *
 */
class ImageBinder(var bitmap: Bitmap) : Binder() {
}