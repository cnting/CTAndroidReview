package com.cnting.coordinatorlayout.behavior

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.cnting.coordinatorlayout.R
import kotlinx.android.synthetic.main.activity_behavior.*

/**
 * Created by cnting on 2020/11/20
 *
 */
class DependentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_behavior)
//        dependentView.setOnClickListener { ViewCompat.offsetTopAndBottom(it, 5) }
    }
}