package com.cnting.fragmentreview.test_with_viewpager

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.cnting.fragmentreview.R
import kotlinx.android.synthetic.main.fragment_test_lifecycle.*

/**
 * Created by cnting on 2020/9/28
 *
 */
class MyFragment : Fragment(R.layout.fragment_test_lifecycle) {

    var position: Int? = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        position = arguments?.getInt("position")
        Log.d("===>", "onAttach $position")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.getBoolean("isViewPager2") == true) {
            view.setBackgroundColor(resources.getColor(android.R.color.holo_blue_dark))
        }
        fragment1Tv.text = "第${position}个"

        Log.d("===>", "onViewCreated $position")
    }

    override fun onStart() {
        super.onStart()
        Log.d("===>", "onStart $position")
    }

    override fun onResume() {
        super.onResume()
        Log.d("===>", "onResume $position")
    }

    override fun onPause() {
        super.onPause()
        Log.d("===>", "onPause $position")
    }

    override fun onStop() {
        super.onStop()
        Log.d("===>", "onStop $position")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("===>", "onDestroyView $position")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("===>", "onDestroy $position")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("===>", "onDetach $position")
    }

}