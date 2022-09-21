package com.example.glide.progress

import android.content.Context
import android.graphics.Color
import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.request.BaseRequestOptions

/**
 * Created by cnting on 2022/9/9
 *
 */
@GlideExtension
object ProgressExtension {
    @GlideOption
    @JvmStatic
    fun progress(options: BaseRequestOptions<*>, context: Context): BaseRequestOptions<*> {
        val progressPlaceholderDrawable = ProgressPlaceholderDrawable(
            context,
            options.placeholderDrawable,
            options.placeholderId
        )
        progressPlaceholderDrawable.setTint(Color.GRAY)
        return options.placeholder(progressPlaceholderDrawable)
    }
}