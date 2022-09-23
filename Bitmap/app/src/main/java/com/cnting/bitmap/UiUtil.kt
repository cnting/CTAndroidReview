package com.cnting.bitmap

import android.content.res.Resources
import android.util.TypedValue

/**
 * Created by cnting on 2022/9/22
 *
 */
fun dp2px(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        Resources.getSystem().displayMetrics
    )
}