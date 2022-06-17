package com.cnting.bitmap

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadBitmap()
    }


    private fun loadBitmap() {
//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.a)
//        findViewById<ImageView>(R.id.img).apply {
//            setImageBitmap(bitmap)
//
//            setOnClickListener {
//                val intent = Intent(this@MainActivity, RemoteActivity::class.java)
//                intent.putExtras(Bundle().apply { putBinder("bitmap", ImageBinder2(bitmap)) })
//                startActivity(intent)
//            }
//        }

        Glide.with(this)
            .load("https://cn.bing.com/az/hprichbg/rb/Dongdaemun_ZH-CN10736487148_1920x1080.jpg")
            .into(findViewById<ImageView>(R.id.img))
    }
}
