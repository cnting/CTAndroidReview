package com.example.breakpointdownload

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.breakpointdownload.download.DefaultDownloadListener
import com.example.breakpointdownload.download.DownloadManager
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val downloadBtn = findViewById<View>(R.id.downloadBtn)
        val imageView = findViewById<ImageView>(R.id.img)

        val parentFile = File(getExternalFilesDir("download")?.absolutePath ?: "")
        if (!parentFile.exists()) {
            parentFile.createNewFile()
        }
        val file = File(parentFile.absolutePath + File.separator + "a.webp");
        Log.d("===>", file.absolutePath)

        downloadBtn.setOnClickListener {
            DownloadManager().download(
                this,
                "https://img2.baidu.com/it/u=3641069436,3076409164&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=711",
                file.absolutePath,
                object : DefaultDownloadListener() {
                    override fun onChildProgress(
                        threadId: Int,
                        curLocation: Long,
                        progress: Float
                    ) {
                        Log.d("===>", "onChildProgress(),threadId:$threadId,progress:$progress")
                    }

                    override fun onComplete() {
                        runOnUiThread { imageView.setImageURI(Uri.fromFile(file)) }
                    }
                }
            )
        }
    }
}