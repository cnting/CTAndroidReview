package com.cnting.recyclerview.barrage.animate

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cnting.recyclerview.R

/**
 * Created by cnting on 2022/12/16
 * 弹幕
 */
class BarrageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barrage)
        val laneView = findViewById<LaneView>(R.id.laneView)
        laneView.apply {
            createView = {
                layoutInflater.inflate(R.layout.view_barrage_item, laneView, false)
            }
            bindView = { data, view ->
                view.findViewById<TextView>(R.id.laneTv)?.apply {
                    text = (data as? Comment)?.text
                }
                view.findViewById<ImageView>(R.id.laneImageView)?.let {
                    Glide.with(it.context).load((data as? Comment)?.url).into(it)
                }
            }
        }
        val pic = "https://p.qqan.com/up/2021-4/16189694769878569.jpg"
        laneView.show(mutableListOf(Comment("1111111", pic),
            Comment("22222222", pic),
            Comment("dsljilfsijlxi", pic),
            Comment("受理费巨力索具李经理四", pic),
            Comment("发病机理忘记了佛饿哦i；我", pic),
            Comment("列举了斯嘉丽吉里吉里", pic),
            Comment("巴黎世家浪费IE肌无力进啦", pic),
            Comment("大咧咧斯嘉丽就类似", pic),
            Comment("布鲁斯家乐福i金额", pic),
            Comment("额外列家里现金流", pic),
            Comment("巴黎世家饿了费死劲福利姬", pic),
            Comment("不理人吉里吉里", pic),
            Comment("额外类五级哦我i", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic),
            Comment("22222222", pic)
        ))
    }
}