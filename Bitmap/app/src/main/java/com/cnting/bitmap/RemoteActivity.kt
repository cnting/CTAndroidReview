package com.cnting.bitmap

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by cnting on 2021/8/30
 *
 */
class RemoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent?.extras
        val imageBinder = bundle?.getBinder("bitmap") as ImageBinder
        val bitmap = imageBinder.bitmap
        val imageView = ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        Log.d("===>", "bundle:$bundle, imageBinder:$imageBinder,bitmap:$bitmap")
        imageView.setImageBitmap(bitmap)
        setContentView(imageView)
    }
}