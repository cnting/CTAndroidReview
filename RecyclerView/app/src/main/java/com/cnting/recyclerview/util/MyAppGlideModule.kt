package com.cnting.recyclerview.util

import android.app.ActivityManager
import android.content.Context
import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.Excludes
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.module.LibraryGlideModule
import com.bumptech.glide.request.RequestOptions
import java.io.InputStream

/**
 * Created by cnting on 2020/11/18
 *
 */
@Excludes(OkHttpLibraryGlideModule::class)
@GlideModule
class MyAppGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val calculator = MemorySizeCalculator.Builder(context)
            .setMemoryCacheScreens(3f)
            .build()
        builder.setMemoryCache(LruResourceCache(calculator.memoryCacheSize.toLong()))
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (null != activityManager) {
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            builder.setDefaultRequestOptions(RequestOptions.formatOf(if (memoryInfo.lowMemory) DecodeFormat.PREFER_RGB_565 else DecodeFormat.PREFER_ARGB_8888))
        }

    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

//    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
//        registry.replace(
//            GlideUrl::class.java,
//            InputStream::class.java,
//            OkHttpUrlLoader.Factory(NetConfig.okHttpClient)
//        )
//    }

}