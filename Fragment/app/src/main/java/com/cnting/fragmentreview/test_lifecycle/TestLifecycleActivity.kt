package com.cnting.fragmentreview.test_lifecycle


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cnting.fragmentreview.R
import kotlinx.android.synthetic.main.activity_test_lifecycle.*
/**
 * Created by cnting on 2020/9/25
 *
 */
class TestLifecycleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TestLifecycleActivity", "=>onCreate")
        setContentView(R.layout.activity_test_lifecycle)

        //在Activity重建时，会自动创建上次的Fragment，所以不需要我们再手动创建了
        if (savedInstanceState == null) {
            addFragment()
        }

        fragmentContainer.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, TestLifecycleFragment2())
                .addToBackStack("")
                .commit()

            supportFragmentManager.popBackStack()
        }

    }

    private fun addFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, TestLifecycleFragment())
            .addToBackStack("")
            .commit()
    }

    override fun onStart() {
        super.onStart()
        Log.d("TestLifecycleActivity", "=>onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TestLifecycleActivity", "=>onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("TestLifecycleActivity", "=>onRestart")
    }

    override fun onPause() {
        super.onPause()
        Log.d("TestLifecycleActivity", "=>onPause")
//        addFragment()
    }


    override fun onStop() {
        super.onStop()
        Log.d("TestLifecycleActivity", "=>onStop")
//        addFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TestLifecycleActivity", "=>onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("TestLifecycleActivity", "=>onSaveInstanceState")
    }
}

