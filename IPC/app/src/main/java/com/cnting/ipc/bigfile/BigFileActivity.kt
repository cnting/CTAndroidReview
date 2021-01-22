package com.cnting.ipc.bigfile

import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cnting.ipc.MainActivity
import com.cnting.ipc.aidl.IBigBitmapManager

/**
 * Created by cnting on 2021/1/20
 * 进程间传递大文件
 */
class BigFileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bitmap: Bitmap? =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                val binder: IBinder? = intent.extras?.getBinder("bitmap")
                val bitmapManager: IBigBitmapManager? = IBigBitmapManager.Stub.asInterface(binder)
                bitmapManager?.bitmap
            } else {
                null
            }
//        val bitmap = intent.getParcelableExtra<Bitmap>("bitmap")
        Log.d("===>", "接收:" + (bitmap?.byteCount ?: 0) / 1024 + "kb")

    }
}