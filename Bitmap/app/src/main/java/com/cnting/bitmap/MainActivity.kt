package com.cnting.bitmap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cnting.bitmap.bigimg.BigImageView
import com.cnting.bitmap.bigimg.bean.ImageResource

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

        findViewById<BigImageView>(R.id.img).apply {
            setImageSource(ImageResource(assets.open("b.jpg")))
        }
    }
}
