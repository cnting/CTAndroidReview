package com.cnting.fragmentreview.test_lifecycle

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cnting.fragmentreview.R
import kotlinx.android.synthetic.main.fragment_test_lifecycle.*

/**
 * Created by cnting on 2020/9/25
 *
 */
class TestLifecycleFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("TestLifecycleFragment", "=======>onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("TestLifecycleFragment", "=======>onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("TestLifecycleFragment", "=======>onCreateView")
        return inflater.inflate(R.layout.fragment_test_lifecycle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment1Tv.text = "Fragment1 ${hashCode()}"
        Log.i("TestLifecycleFragment", "=======>onViewCreated")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("TestLifecycleFragment", "=======>onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.i("TestLifecycleFragment", "=======>onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("TestLifecycleFragment", "=======>onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("TestLifecycleFragment", "=======>onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("TestLifecycleFragment", "=======>onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("TestLifecycleFragment", "=======>onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TestLifecycleFragment", "=======>onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i("TestLifecycleFragment", "=======>onDetach")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("TestLifecycleFragment", "=======>onSaveInstanceState")
    }
}