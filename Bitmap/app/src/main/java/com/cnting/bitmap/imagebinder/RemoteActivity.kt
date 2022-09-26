package com.cnting.bitmap.imagebinder

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.cnting.bitmap.IRemoteBitmapService

/**
 * Created by cnting on 2021/8/30
 *
 */
class RemoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent?.extras
        val imageBinder = bundle?.getBinder("bitmap")
        val bitmapService: IRemoteBitmapService = IRemoteBitmapService.Stub.asInterface(imageBinder)
        val bitmap = bitmapService.intentBitmap
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