package com.cnting.fragmentreview.test_backstack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cnting.fragmentreview.R
import com.cnting.fragmentreview.test_lifecycle.TestLifecycleFragment
import com.cnting.fragmentreview.test_lifecycle.TestLifecycleFragment2
import kotlinx.android.synthetic.main.activity_test_backstack.*

/**
 * Created by cnting on 2020/9/29
 *
 */
class TestBackStackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_backstack)

        var clickFlag = 0
        addFragmentBtn.setOnClickListener {
            val fragment =
                if (clickFlag % 2 == 0) TestLifecycleFragment() else TestLifecycleFragment2()
            supportFragmentManager.beginTransaction()
                .addToBackStack("clickFlag-$clickFlag")
                .replace(R.id.fragmentContainer, fragment)
                .commit()
            clickFlag++
        }

        removeFragmentBtn.setOnClickListener {
            supportFragmentManager.popBackStack()
            clickFlag--
        }
    }
}