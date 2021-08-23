package com.cnting.bitmap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadBitmap()
    }

    private fun loadBitmap() {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        BitmapFactory.decodeResource(resources, R.drawable.a, options)

        Log.d("===>", "targetDensity:${options.inTargetDensity}，inDensity:${options.inDensity}")

        Log.d("===>", "图片宽:${options.outWidth},高:${options.outHeight},占用内存:${BitmapUtil.getBitmapSize(options.outWidth, options.outHeight, options.inPreferredConfig)}")
//        options.inPreferredConfig = Bitmap.Config.RGB_565
        options.inJustDecodeBounds = false
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.a, options)
        Log.d("===>", "图片宽:${bitmap.width},高:${bitmap.height},占用内存:${BitmapUtil.getBitmapSize(bitmap.width, bitmap.height, bitmap.config)}")

    }
}