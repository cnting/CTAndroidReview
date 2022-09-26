package com.cnting.bitmap.bigimg

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cnting.bitmap.R
import com.cnting.bitmap.bigimg.bean.ImageResource

/**
 * Created by cnting on 2022/9/26
 *
 */
class BigImageActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_big_image)
        findViewById<BigImageView>(R.id.img).apply {
            setImageSource(ImageResource(assets.open("b.jpg")))
        }
    }
}