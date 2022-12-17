package com.cnting.recyclerview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cnting.recyclerview.barrage.animate.BarrageActivity
import com.cnting.recyclerview.barrage.recyclerview.BarrageActivity2
import com.cnting.recyclerview.card_swipe.CardSwipeActivity
import com.cnting.recyclerview.custom_layoutmanager.CustomLayoutManagerActivity
import com.cnting.recyclerview.item_touch_helper.ItemTouchHelperActivity
import com.cnting.recyclerview.item_visibility.TestItemVisibilityActivity
import com.cnting.recyclerview.release_time.ReleaseTimeActivity
import com.cnting.recyclerview.stagger.StaggerActivity
import com.cnting.recyclerview.test_animate.TestAnimateActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        staggerBtn.setOnClickListener { startActivity(Intent(this, StaggerActivity::class.java)) }
        layoutManagerBtn.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    CustomLayoutManagerActivity::class.java
                )
            )
        }
        itemTouchHelperBtn.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ItemTouchHelperActivity::class.java
                )
            )
        }
        cardSwipeBtn.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    CardSwipeActivity::class.java
                )
            )
        }
        releaseTimeBtn.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ReleaseTimeActivity::class.java
                )
            )
        }
        testDeleteAnimateBtn.setOnClickListener {
            startActivity(Intent(this, TestAnimateActivity::class.java))
        }
        barrageBtn.setOnClickListener {
            startActivity(Intent(this, BarrageActivity::class.java))
        }
        barrageBtn2.setOnClickListener {
            startActivity(Intent(this, BarrageActivity2::class.java))
        }
        testVisibility.setOnClickListener {
            startActivity(Intent(this, TestItemVisibilityActivity::class.java))
        }
    }
}