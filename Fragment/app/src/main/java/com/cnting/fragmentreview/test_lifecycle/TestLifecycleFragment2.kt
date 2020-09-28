package com.cnting.fragmentreview.test_lifecycle

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cnting.fragmentreview.R

/**
 * Created by cnting on 2020/9/25
 *
 */
class TestLifecycleFragment2 : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.v("TestLifecycleFragment2", "=======>onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("TestLifecycleFragment2", "=======>onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v("TestLifecycleFragment2", "=======>onCreateView")
        return inflater.inflate(R.layout.fragment_test_lifecycle2, container, false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v("TestLifecycleFragment2", "=======>onViewCreated")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v("TestLifecycleFragment2", "=======>onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.v("TestLifecycleFragment2", "=======>onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.v("TestLifecycleFragment2", "=======>onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.v("TestLifecycleFragment2", "=======>onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.v("TestLifecycleFragment2", "=======>onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.v("TestLifecycleFragment2", "=======>onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("TestLifecycleFragment2", "=======>onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.v("TestLifecycleFragment2", "=======>onDetach")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.v("TestLifecycleFragment2", "=======>onSaveInstanceState")
    }
}