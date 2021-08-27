package com.cnting.bitmap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadBitmap()
    }


    private fun loadBitmap() {
        val assetManager = resources.assets
        val inputStream = assetManager.open("long.jpeg")
        findViewById<BigImageView>(R.id.img).setFilePath(inputStream)
    }
}
