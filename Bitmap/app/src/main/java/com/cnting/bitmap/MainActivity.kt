package com.cnting.bitmap

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cnting.bitmap.bigimg.BigImageActivity
import com.cnting.bitmap.imagebinder.RemoteActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.bigImgBtn).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    BigImageActivity::class.java
                )
            )
        }
        findViewById<View>(R.id.putBinderBtn).setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.a)
            val intent = Intent(this@MainActivity, RemoteActivity::class.java)
            intent.putExtras(Bundle().apply {
                putBinder("bitmap", object : IRemoteBitmapService.Stub() {
                    override fun getIntentBitmap(): Bitmap {
                        return bitmap
                    }

                })
            })
            startActivity(intent)
        }

        Glide.with(this)
//            .load("https://www.baidu.com/img/PCfb_5bf082d29588c07f842ccde3f97243ea.png")
            .load("https://wwwwwwwwwww")
            .fitCenter()
            .into(findViewById(R.id.imageView))
    }

}
