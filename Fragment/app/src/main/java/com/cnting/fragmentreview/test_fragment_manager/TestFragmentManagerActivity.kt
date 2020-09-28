package com.cnting.fragmentreview.test_fragment_manager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cnting.fragmentreview.R

/**
 * Created by cnting on 2020/9/27
 *
 */
class TestFragmentManagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_lifecycle)
        Log.d("TestLifecycleActivity", "=>supportFragmentManager:$supportFragmentManager")

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, ParentFragment())
            .commit()
    }
}


